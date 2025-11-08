package com.example.books.domain;

import com.example.books.domain.core.ingestrun.IngestRun;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IngestRunTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IngestRun getIngestRunSample1() {
        return buildSample(1L, "RUNNING", 10, 5, 1);
    }

    public static IngestRun getIngestRunSample2() {
        return buildSample(2L, "COMPLETED", 25, 25, 0);
    }

    public static IngestRun getIngestRunRandomSampleGenerator() {
        int base = Math.abs(intCount.incrementAndGet() % 1000);
        return buildSample(
            longCount.incrementAndGet(),
            "STATUS-" + UUID.randomUUID().toString().substring(0, 8),
            base + 10,
            base + 5,
            base % 3
        );
    }

    private static IngestRun buildSample(Long id, String status, Integer seen, Integer parsed, Integer failed) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
        return new IngestRun(id, now.minusMinutes(10), now, status, seen, parsed, failed);
    }
}
