package com.example.books.infrastructure.database.jpa.entity;

import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IngestRunTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IngestRunEntity getIngestRunSample1() {
        return new IngestRunEntity().id(1L).status("status1").filesSeen(1).filesParsed(1).filesFailed(1);
    }

    public static IngestRunEntity getIngestRunSample2() {
        return new IngestRunEntity().id(2L).status("status2").filesSeen(2).filesParsed(2).filesFailed(2);
    }

    public static IngestRunEntity getIngestRunRandomSampleGenerator() {
        return new IngestRunEntity()
            .id(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .filesSeen(intCount.incrementAndGet())
            .filesParsed(intCount.incrementAndGet())
            .filesFailed(intCount.incrementAndGet());
    }
}
