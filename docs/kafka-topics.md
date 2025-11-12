# Kafka Topic and Consumer Group Overview

This document summarizes the Kafka topics and consumer groups used by the Books ETL service.

## Topics

Topic names are configured through `application.kafka.topics` in `src/main/resources/config/application.yml` and are injected into adapters via `KafkaTopicProperties`.

| Topic Purpose                 | Property Key                       | Topic Name           |
| ----------------------------- | ---------------------------------- | -------------------- |
| Raw file change notifications | `application.kafka.topics.raw`     | `pdf.ingest.raw`     |
| Parsed PDF documents          | `application.kafka.topics.parsed`  | `pdf.ingest.parsed`  |
| Indexed document events       | `application.kafka.topics.indexed` | `pdf.ingest.indexed` |
| Dead-letter queue             | `application.kafka.topics.dlq`     | `pdf.ingest.dlq`     |

The Kafka producer (`KafkaProducer`) publishes serialized payloads to these topics via the `KafkaTemplate`.

## Consumer Groups

`KafkaConsumer` subscribes to the configured topics with the following consumer groups:

| Listener Method         | Topics                               | Consumer Group      |
| ----------------------- | ------------------------------------ | ------------------- |
| `consumeRawDocument`    | `${application.kafka.topics.raw}`    | `books-etl-app`     |
| `consumeParsedDocument` | `${application.kafka.topics.parsed}` | `books-etl-app`     |
| `consumeDeadLetter`     | `${application.kafka.topics.dlq}`    | `books-etl-app-dlq` |

When debugging missing log messages, ensure that the broker contains messages on these topics and that the application instance joins the expected consumer group.
