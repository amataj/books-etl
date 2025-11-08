package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Book getBookSample1() {
        return new Book().id(1L).documentId("documentId1").title("title1").author("author1").lang("lang1").pages(1);
    }

    public static Book getBookSample2() {
        return new Book().id(2L).documentId("documentId2").title("title2").author("author2").lang("lang2").pages(2);
    }

    public static Book getBookRandomSampleGenerator() {
        return new Book()
            .id(longCount.incrementAndGet())
            .documentId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .author(UUID.randomUUID().toString())
            .lang(UUID.randomUUID().toString())
            .pages(intCount.incrementAndGet());
    }
}
