package com.example.books.domain;

import com.example.books.domain.core.ingestevent.IngestEvent;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IngestEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static IngestEvent getIngestEventSample1() {
        return buildSample(
            1L,
            UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"),
            "document-00001",
            "topic.alpha",
            "{\"payload\":1}",
            10L
        );
    }

    public static IngestEvent getIngestEventSample2() {
        return buildSample(
            2L,
            UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"),
            "document-00002",
            "topic.beta",
            "{\"payload\":2}",
            20L
        );
    }

    public static IngestEvent getIngestEventRandomSampleGenerator() {
        long ingestRunId = Math.abs(random.nextLong() % 1000) + 1;
        return buildSample(
            longCount.incrementAndGet(),
            UUID.randomUUID(),
            "document-" + UUID.randomUUID(),
            "topic-" + UUID.randomUUID().toString().substring(0, 8),
            "{\"payload\":\"" + UUID.randomUUID() + "\"}",
            ingestRunId
        );
    }

    private static IngestEvent buildSample(Long id, UUID runId, String documentId, String topic, String payload, Long ingestRunId) {
        ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
        return new IngestEvent(id, runId, documentId, topic, payload, createdAt, ingestRunId);
    }
}
