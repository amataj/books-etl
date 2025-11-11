package com.example.books.infrastructure.database.jpa.entity;

import com.example.books.infrastructure.broker.KafkaTopicConfiguration;
import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IngestEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IngestEventEntity getIngestEventSample1() {
        return new IngestEventEntity()
            .id(1L)
            .runId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .documentId("f4d5f160a2b9c8deabf4d2e8bb2c9a8cd1234567890abcdef1234567890abcd")
            .topic(KafkaTopicConfiguration.PDF_INGEST_RAW)
            .payload(
                "{\"event_id\":\"1f6cc052-79f9-4c5c-9f65-ea8f0d7f7a3d\",\"discovered_at\":\"2025-10-15T10:00:00Z\",\"path\":\"file:///Books/foo/Bar.pdf\",\"size_bytes\":123456,\"sha256\":\"f4d5f160a2b9c8deabf4d2e8bb2c9a8cd1234567890abcdef1234567890abcd\",\"source_host\":\"ingest-host-01\"}"
            );
    }

    public static IngestEventEntity getIngestEventSample2() {
        return new IngestEventEntity()
            .id(2L)
            .runId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .documentId("0c5e68b9dcb4a53c98f68fd1a2c47e3b11223344556677889900aabbccddeeff")
            .topic(KafkaTopicConfiguration.PDF_INGEST_PARSED)
            .payload(
                "{\"event_id\":\"5b9e97b8-d52a-4a36-94fa-9e2d0cd4d29c\",\"document_id\":\"f4d5f160a2b9c8deabf4d2e8bb2c9a8cd1234567890abcdef1234567890abcd\",\"title_guess\":\"Clean Architecture\",\"author_guess\":\"Robert C. Martin\",\"pages\":432,\"text_bytes\":3456789,\"lang\":\"en\",\"parser\":\"pdfbox\",\"parse_warnings\":[]}"
            );
    }

    public static IngestEventEntity getIngestEventRandomSampleGenerator() {
        long nextId = longCount.incrementAndGet();
        UUID rawEventId = UUID.randomUUID();
        String documentId = UUID.randomUUID().toString();
        return new IngestEventEntity()
            .id(nextId)
            .runId(UUID.randomUUID())
            .documentId(documentId)
            .topic(nextId % 2 == 0 ? KafkaTopicConfiguration.PDF_INGEST_PARSED : KafkaTopicConfiguration.PDF_INGEST_RAW)
            .payload(
                "{\"event_id\":\"" +
                rawEventId +
                "\",\"document_id\":\"" +
                documentId +
                "\",\"parser\":\"pdfbox\",\"parse_warnings\":[],\"size_bytes\":1024}"
            );
    }
}
