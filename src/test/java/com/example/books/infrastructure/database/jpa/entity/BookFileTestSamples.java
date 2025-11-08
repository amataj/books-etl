package com.example.books.infrastructure.database.jpa.entity;

import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookFileEntity getBookFileSample1() {
        return new BookFileEntity().id(1L).sha256("sha2561").sizeBytes(1L);
    }

    public static BookFileEntity getBookFileSample2() {
        return new BookFileEntity().id(2L).sha256("sha2562").sizeBytes(2L);
    }

    public static BookFileEntity getBookFileRandomSampleGenerator() {
        return new BookFileEntity()
            .id(longCount.incrementAndGet())
            .sha256(UUID.randomUUID().toString())
            .sizeBytes(longCount.incrementAndGet());
    }
}
