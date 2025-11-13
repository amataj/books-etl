### What I see in the logs

- Consumers start, but no partitions are assigned:
  - 15:37:06 — three listener containers start and your monitor logs: `Kafka consumers started - runningContainers=3, assignedPartitions=0` (lines 1319–1321).
  - 15:37:21 — after another restart, they again start with `assignedPartitions=0` (lines 1886–1888).
- Shortly after the producer sends messages to `pdf.ingest.raw`, DevTools triggers a restart and the consumers proactively leave their groups:
  - Producer sends at 15:37:08 and 15:37:10 (lines 1326, 1430).
  - 15:37:12–15:37:13 — all consumer containers log “consumer pro‑actively leaving the group … Consumer stopped” and your monitor reports `runningContainers=0` (lines 1439–1474).
- The app then restarts (Spring DevTools ‘restartedMain’ signature is present multiple times), consumers subscribe again, but by end of log they still have 0 assigned partitions and no consumption log lines like `Received raw PDF event` appear.
- Connectivity to Kafka looks OK (Cluster ID logs are present; e.g., lines 1323–1325 and 1890–1892).
- The listeners are defined as:
  - `@KafkaListener(topics = "${application.kafka.topics.raw}", groupId = "books-etl-app")`
  - `@KafkaListener(topics = "${application.kafka.topics.parsed}", groupId = "books-etl-app")`
  - `@KafkaListener(topics = "${application.kafka.topics.dlq}", groupId = "books-etl-app-dlq")`
- At runtime, at least the raw topic resolves to `pdf.ingest.raw` (logs show `Subscribed to topic(s): pdf.ingest.raw`, line 1781).

### Likely causes

1. Frequent DevTools restarts interrupting rebalance/assignment

- Evidence: repeated “restartedMain” sequences and “consumer pro‑actively leaving the group” just after messages are produced. This can easily make you think the consumer is not reading when it’s being constantly torn down during startup/reload.

2. Topic(s) missing or created late → 0 assigned partitions

- Your monitor shows 0 assigned partitions every time. If a subscribed topic doesn’t exist yet (or exists but creation happens after listener start), consumers remain in the group with no partitions. You are definitely producing to `pdf.ingest.raw`, but I see no evidence that `pdf.ingest.parsed` (and `indexed`) exist. Since two listeners share the same group `books-etl-app` but subscribe to different topics, this mismatch can complicate assignment if one of the topics doesn’t exist yet.

3. Multiple containers with the same group id but different topic sets

- Both `consumeRawDocument` and `consumeParsedDocument` use `groupId = "books-etl-app"`. In Kafka, all consumers in the same group participate in the same subscription (the union of topics for that group). With Spring Kafka running them as separate containers, that can be OK when all topics exist — but if one topic is missing, you may end up with no partitions assigned until metadata settles. It’s safer to give distinct groups when using separate containers per topic or to have a single container for multiple topics.

4. Another process consuming with the same group id

- If there’s another instance of the app (or any consumer) with `group.id=books-etl-app`, it could steal partitions. I don’t see that explicitly in the logs, but it’s a common reason for “not reading”.

### Concrete things to fix/check

- Eliminate DevTools interference

  - Temporarily run without DevTools reload, e.g. start with `spring.devtools.restart.enabled=false`, or run the app with `mvn spring-boot:run -Dspring-boot.run.profiles=dev,api-docs -Dspring-boot.run.arguments=--spring.devtools.restart.enabled=false`.
  - Alternatively, exclude your Kafka classes from triggering restarts (e.g., configure `spring.devtools.restart.additional-exclude=**/broker/**`).

- Ensure the application topic properties are set and topics exist

  - Make sure these are present in `application.yml` or your active profile:
    ```yaml
    application:
      kafka:
        topics:
          raw: pdf.ingest.raw
          parsed: pdf.ingest.parsed
          indexed: pdf.ingest.indexed
          dlq: pdf.ingest.dlq
    ```
  - Create the topics with partitions explicitly (don’t rely on auto‑create) before starting the app. Example (adjust to your Kafka setup):
    ```bash
    kafka-topics --bootstrap-server localhost:9092 --create --topic pdf.ingest.raw --partitions 3 --replication-factor 1
    kafka-topics --bootstrap-server localhost:9092 --create --topic pdf.ingest.parsed --partitions 3 --replication-factor 1
    kafka-topics --bootstrap-server localhost:9092 --create --topic pdf.ingest.indexed --partitions 3 --replication-factor 1
    kafka-topics --bootstrap-server localhost:9092 --create --topic pdf.ingest.dlq --partitions 1 --replication-factor 1
    ```
  - Verify they exist and have partitions: `kafka-topics --bootstrap-server localhost:9092 --describe --topic pdf.ingest.raw`.

- Use distinct consumer groups per listener (optional but recommended)

  - To avoid cross‑subscription quirks, either:
    - Use one `@KafkaListener` subscribing to both topics that belong to the same stream, or
    - Give `consumeRawDocument` and `consumeParsedDocument` different `groupId`s if you intend them to scale independently.

- Turn on DEBUG for assignment visibility

  - Temporarily set (already INFO in dev, but DEBUG gives assignment details):
    ```yaml
    logging:
      level:
        org.springframework.kafka: DEBUG
        org.apache.kafka.clients.consumer: DEBUG
    ```
  - You should see logs like “Added partitions …” when assignment happens. Right now, your monitor shows `assignedPartitions=0`, confirming the core issue.

- Sanity checks
  - Make sure no other process is consuming with `group.id=books-etl-app`.
  - Confirm broker is healthy and not auto‑restarting.
  - If you want the consumer to reprocess earlier produced messages, keep `auto-offset-reset: earliest` (you already have it in `application-dev.yml`).

### Recommended quick path to verify

1. Stop the app.
2. Create/verify the four topics exist and have partitions.
3. Start the app with DevTools restart disabled for this run.
4. Drop one PDF file to trigger a new message, or use the CLI producer to send a test record to `pdf.ingest.raw`.
5. Watch for:
   - Assignment logs “Added partitions …” and your health/monitor to change from `assignedPartitions=0` to a positive number.
   - Application log `Received raw PDF event from Kafka` emitted by `KafkaConsumer.consumeRawDocument`.

### Bottom line

- The consumer wasn’t reading because the listener containers never received any partition assignment while the app was being restarted by DevTools; and likely one or more subscribed topics were missing at the time, keeping assignment at zero. Stabilize the app (disable DevTools restart), ensure all configured topics exist with partitions, and consider separating consumer groups or consolidating listeners. After doing this, you should see partitions assigned and messages consumed.

---

### Repo-Specific Notes and Aids

- Listener topics use properties, not hard-coded strings (ensures profile consistency):
  - `src/main/java/com/example/books/infrastructure/broker/KafkaConsumer.java:40`
- Topics are defined via configuration and exposed as beans to auto-create on startup:
  - `src/main/resources/config/application.yml:260`
  - `src/main/java/com/example/books/infrastructure/broker/KafkaTopicConfiguration.java:10`
  - Ensure the broker is reachable at app start; otherwise `NewTopic` creation won’t execute.
- Dev profile is configured to consume from earliest when no offsets exist:
  - `src/main/resources/config/application-dev.yml:33`
- Health and visibility helpers:
  - Listener health indicator: `src/main/java/com/example/books/infrastructure/broker/KafkaListenersHealthIndicator.java:1`
  - REST status endpoint: `src/main/java/com/example/books/adapter/web/rest/KafkaListenerStatusResource.java:1` → `GET /api/kafka/listeners`
  - These make it easy to confirm running containers and assigned partitions without tailing logs.

### Quick Triage Checklist (Actionable)

- Start Kafka locally: `npm run docker:kafka:up`
- Confirm topics exist (or let `NewTopic` beans create them):
  - `pdf.ingest.raw`, `pdf.ingest.parsed`, `pdf.ingest.indexed`, `pdf.ingest.dlq`
- Launch the app without DevTools restarts for this session:
  - `./mvnw -ntp spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.arguments=--spring.devtools.restart.enabled=false`
- Verify listeners:
  - `GET /api/kafka/listeners` shows `runningContainers > 0` and `assignedPartitions > 0`
  - `GET /management/health` includes Kafka listener details as UP
- Produce a fresh message (drop a file to inbox) and expect:
  - Log line: `Received raw PDF event from Kafka`
  - Assigned partitions remain > 0

### Suggested Doc Tweaks

- Avoid brittle absolute log line numbers; prefer timestamp + message pattern excerpts for longevity.
- Clarify that multiple `@KafkaListener`s using the same `groupId` subscribe to the union of topics, and require all topics to exist for clean assignment.
- Link to the internal status endpoints above to reduce reliance on DEBUG logging.
