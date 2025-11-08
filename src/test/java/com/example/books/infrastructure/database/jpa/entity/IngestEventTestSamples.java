package com.example.books.infrastructure.database.jpa.entity;

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
            .documentId("documentId1")
            .topic("topic1");
    }

    public static IngestEventEntity getIngestEventSample2() {
        return new IngestEventEntity()
            .id(2L)
            .runId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .documentId("documentId2")
            .topic("topic2");
    }

    public static IngestEventEntity getIngestEventRandomSampleGenerator() {
        return new IngestEventEntity()
            .id(longCount.incrementAndGet())
            .runId(UUID.randomUUID())
            .documentId(UUID.randomUUID().toString())
            .topic(UUID.randomUUID().toString());
    }
}
