# Clean Architecture. Production-grade.

# Objectives and SLOs

- Freshness: new PDF → persisted within ≤2 min p95.
- Reliability: ≥99.5% daily success. Zero dupes on re-runs.
- Throughput: ≥10 PDFs/sec on dev laptop; scale with partitions.
- Recoverability: resumable after crash; DLQ triage under 5 min.

# Domain model and invariants

- `DocumentId = SHA256(file bytes)`. Stable across the path moves.
- One `Book` per `DocumentId`. Many `BookFile` records per `Book`.
- Page text immutable once parsed for a given `DocumentId`.
- Event streams are append-only. Reprocessing must be idempotent.

# Modules

- `domain`: entities, value objects, ports, domain events.
- `application`: use cases (`IngestUseCase`, `ParseUseCase`, `IndexUseCase`, `BackfillUseCase`), DTOs, validators.
- `infrastructure`: PDFBox/Tesseract adapters, Kafka producer/consumer, JDBC repositories, Liquibase, metrics.
- `batch`: Spring Batch jobs and job configs.
- `workflows`: Temporal workflows and activities.
- `shared`: REST + CLI, health, security, Actuator.

# Kafka design

Topics:

- `pdf.ingest.raw` (file discovered)
- `pdf.ingest.parsed` (metadata + per-page text summary + stats)
- `pdf.ingest.indexed` (DB upsert done)
- `pdf.ingest.dlq` (errors)

Keys and partitions:

- Key: `DocumentId` on all topics.
- Partitions: start 6. Scale by CPU and parse cost.
- Headers: `source_host`, `parser=pdfbox|ocr`, `lang`, `attempt`, `trace_id`.
- Producer: enable idempotence, `acks=all`, `max.in.flight.requests.per.connection=1`.
- Consumer: `max.poll.records=64`, `max.poll.interval.ms` tuned for parse time.

Schema contracts (JSON Schema or Avro; JSON shown):

- `pdf.ingest.raw` (add `content_type`, `mtime_epoch_ms`, `root_alias`).
- `pdf.ingest.parsed` (add `avg_chars_per_page`, `empty_pages`, `checksum_algo`, `ocr_ratio`).
- `pdf.ingest.indexed` (include DB row counts and version).

# Storage schema (Liquibase deltas)

- Keep your tables. Add:

- `book_stats(document_id PK, text_bytes bigint, empty_pages int, avg_chars_per_page numeric(6,2))`
- `outbox(id uuid pk, aggregate_id varchar(64), topic text, payload jsonb, created_at timestamptz, sent_at timestamptz null)`
- Unique on `book_file(path_norm, sha256)` already defined.
- Add DB trigger to update `updated_at` on `book`.

# Ports (domain)

```java
interface FileScannerPort {
  Stream<FileMeta> scan(Path root, GlobPattern exclude);
}

interface ChecksumPort {
  String sha256(Path p) throws IOException;
}

interface PdfParserPort {
  ParsedPdf parse(Path p, ParseOptions o) throws ParseException;
}

interface BooksRepository {
  boolean upsertBook(Book b);
  void upsertFile(BookFile f);
  void upsertPages(String documentId, List<PageText> pages);
  Book findById(String documentId);
}

interface OutboxPort {
  void save(OutboxEvent e);
  List<OutboxEvent> fetchUnsent(int batch);
  void markSent(UUID id);
}

interface EventsPort {
  void publish(String topic, String key, Object event, Map<String, String> headers);
}

```

# Application use cases

- `IngestUseCase.scan(root, exclude)`: emits `raw` for each new/changed file; writes outbox for reliability.
- `ParseUseCase.parse(raw)`: PDFBox parse, page slicing, optional OCR fallback; write DB + `book_stats`; outbox `parsed`.
- `IndexUseCase.index(parsed)`: confirms DB upserts; emits `indexed`.
- `BackfillUseCase.reparse(documentId|fromDate)`: deterministic re-runs.

Rules:

- No Spring types in `application` or `domain`. DTOs are plain records.

# Infrastructure adapters

- `fs-watcher`: NIO recursive scan or WatchService. Normalize path, enforce allowlist roots, apply glob excludes.
- `checksum`: streaming SHA-256, 8MB buffer.
- `pdfbox-parser`: extract XMP, bookmarks, per-page text; detect languages with `com.github.pemistahl:lingua` or heuristic; configurable `maxPages`, `ocrIfEmpty=true`.
- `ocr (optional)`: `tess4j` with language packs folder; throttle with semaphore.
- `jdbc-repository`: Spring JDBC or jOOQ. Use UPSERT with conflict handling. Batch insert `book_page_text` with prepared statements.
- `outbox`: DB-backed outbox publisher that drains to Kafka in a local transaction boundary for “effectively exactly once.”
- `kafka`: producers/consumers with tracing headers; DLQ producer on non-retryable errors.

# Spring Batch jobs

- `scanAndStageJob`

  - reader: filesystem reader yields `FileMeta` chunks (size 200).
  - processor: compute sha256, build `raw`.
  - writer: persist outbox and `ingest_event` row; a background `OutboxPublisher` sends to Kafka.

- `parseAndPersistJob`

  - reader: Kafka item reader from `pdf.ingest.raw`.
  - processor: parse; compute stats; build `parsed`.
  - writer: transactional upsert of `book`, `book_file`, `book_page_text`, `book_stats`; write outbox(`parsed`).
  - retries: backoff 1s→30s, maxAttempts=5; classify `IO` as retryable, `CorruptPdf` as non-retryable → DLQ.

- `dlqHandlerJob`

  - reader: `pdf.ingest.dlq`; writer: persist error with stacktrace and last good offset.

Chunking and memory:

- Do not hold full text for large PDFs in memory. Stream per page. Flush every N pages to DB.

# Temporal workflows

- `DailyScanWorkflow` cron `30 6 * * *` America/Toronto. Activity: run `scanAndStageJob`.
- `ParseWorkflow` signals on topic lag or on a fixed timer. Activities wrap Spring Batch `JobLauncher`.
- `BackfillWorkflow(fromDate, rootAlias)` replays scan results; throttles parse activities with concurrency controls.

# Idempotency and consistency

- Keyed by `DocumentId`.
- DB constraints prevent dupes.
- Outbox pattern ensures atomic DB+event publication.
- Consumer idempotency: re-emit `parsed/indexed` only if version advanced.
- Re-seen file with same `sha256` updates `book_file.last_seen_at` only.

# Observability and alerts

Metrics (Micrometer):

- Timers: `pdf.parse.time`, `pdf.ocr.time`, `batch.step.time`, `kafka.publish.time`.
- Counters: `files.seen`, `files.parsed`, `files.failed`, `dlq.count`.
- Gauges: `kafka.lag.raw`, `kafka.lag.parsed`, `queue.backlog.raw`.
  Tracing:
- Add OpenTelemetry SDK. Propagate `trace_id` in Kafka headers. Export OTLP locally.
  Dashboards:
- Panels for p50/p95 parse time, success rate, DLQ by cause, throughput, topic lag.
  Alerts:
- `DLQ > 0 for 5m`
- `Freshness p95 > 2m`
- `Outbox backlog > 1000 for 10m`.

# Security and safety

- Allowlist roots. Deny symlinks outside roots. Ignore >200MB by default.
- Enforce `content_type=application/pdf`. Magic-bytes sniff.
- Sanitize XMP. Strip embedded scripts or attachments.
- Quotas: max OCR concurrency 2. CPU cap via container limits.

# Interfaces (API/CLI)

REST (minimal):

- `POST /ingest/scan?root=...` → triggers scan job.
- `POST /ingest/backfill?from=YYYY-MM-DD`
- `GET /docs/{documentId}` → metadata + file locations.

CLI:

```
java -jar books-interfaces.jar scan --root=/Volumes/Books --exclude="**/.trash/**,**/*.tmp"
java -jar books-interfaces.jar backfill --from=2018-01-01
java -jar books-interfaces.jar reparse --document-id=<sha256>
```

# Configuration keys

```
books.root=/Volumes/Books
books.exclude=**/.trash/**,**/*.tmp
books.maxFileBytes=200000000
kafka.bootstrap=localhost:9092
kafka.partitions=6
spring.batch.job.enabled=false
temporal.namespace=books-etl
storage.s3.enabled=false
storage.s3.bucket=books
observability.tracing.enabled=true
ocr.enabled=false
```

# Testing matrix

- **Unit**: ports, parsers (good PDF, encrypted, image-only, corrupt).
- **Contract**: JSON schema validation for topic payloads.
- **Integration (Testcontainers)**: Postgres + Kafka + optional MinIO.
- **E2E**: drop fixture PDFs in a temp folder → expect DB rows, events, metrics.
- **Chaos**: kill consumer mid-batch, corrupt one page, simulate slow disk.
- **Performance**: 1k PDFs synthetic corpus. Record p95 and throughput.

# Rollout

1. Implement `scanAndStageJob` + outbox publisher.
2. Implement `ParseUseCase` with PDFBox only.
3. Wire Kafka producer/consumer.
4. Add DB upserts and `book_stats`.
5. Add dashboards + alerts.
6. Enable Temporal scheduling.
7. Optional: OCR path and S3 archival.

# Next actions

- Pick JSON Schema vs Avro for payload contracts.
