package com.example.books.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IngestEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IngestEvent getIngestEventSample1() {
        return new IngestEvent()
            .id(1L)
            .runId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .documentId("documentId1")
            .topic("topic1");
    }

    public static IngestEvent getIngestEventSample2() {
        return new IngestEvent()
            .id(2L)
            .runId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .documentId("documentId2")
            .topic("topic2");
    }

    public static IngestEvent getIngestEventRandomSampleGenerator() {
        return new IngestEvent()
            .id(longCount.incrementAndGet())
            .runId(UUID.randomUUID())
            .documentId(UUID.randomUUID().toString())
            .topic(UUID.randomUUID().toString());
    }
}
