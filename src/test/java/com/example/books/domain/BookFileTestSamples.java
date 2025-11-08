package com.example.books.domain;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.core.bookfile.BookFile;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static BookFile getBookFileSample1() {
        return buildBookFile(1L, "/data/books/sample-1.pdf", "aaaaaaaaaa", 1_024L, BookTestSamples.getBookSample1());
    }

    public static BookFile getBookFileSample2() {
        return buildBookFile(2L, "/data/books/sample-2.pdf", "bbbbbbbbbb", 2_048L, BookTestSamples.getBookSample2());
    }

    public static BookFile getBookFileRandomSampleGenerator() {
        Book book = BookTestSamples.getBookRandomSampleGenerator();
        return buildBookFile(
            longCount.incrementAndGet(),
            "/data/books/" + UUID.randomUUID() + ".pdf",
            randomSha(),
            (Math.abs(random.nextLong()) % 10_000) + 1,
            book
        );
    }

    private static BookFile buildBookFile(Long id, String path, String sha256, Long size, Book book) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
        return new BookFile(id, path, sha256, size, now.minusHours(2), "s3://bucket" + path, now.minusDays(1), now, book);
    }

    private static String randomSha() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
