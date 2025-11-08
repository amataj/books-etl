package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookPageTextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BookPageTextEntity getBookPageTextSample1() {
        return new BookPageTextEntity().id(1L).documentId("documentId1").pageNo(1);
    }

    public static BookPageTextEntity getBookPageTextSample2() {
        return new BookPageTextEntity().id(2L).documentId("documentId2").pageNo(2);
    }

    public static BookPageTextEntity getBookPageTextRandomSampleGenerator() {
        return new BookPageTextEntity()
            .id(longCount.incrementAndGet())
            .documentId(UUID.randomUUID().toString())
            .pageNo(intCount.incrementAndGet());
    }
}
