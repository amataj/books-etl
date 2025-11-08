package com.example.books.domain;

import com.example.books.domain.core.book.Book;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Book getBookSample1() {
        return new Book(1L, "document-00001", "Sample Title 1", "Author 1", "en", 120);
    }

    public static Book getBookSample2() {
        return new Book(2L, "document-00002", "Sample Title 2", "Author 2", "fr", 180);
    }

    public static Book getBookRandomSampleGenerator() {
        return new Book(
            longCount.incrementAndGet(),
            "document-" + UUID.randomUUID(),
            "Title-" + UUID.randomUUID(),
            "Author-" + UUID.randomUUID(),
            "en",
            1 + (intCount.incrementAndGet() % 500)
        );
    }
}
