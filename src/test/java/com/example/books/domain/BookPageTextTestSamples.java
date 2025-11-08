package com.example.books.domain;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.core.bookpagetext.BookPageText;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookPageTextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BookPageText getBookPageTextSample1() {
        return buildSample(1L, BookTestSamples.getBookSample1(), 1, "Sample text 1");
    }

    public static BookPageText getBookPageTextSample2() {
        return buildSample(2L, BookTestSamples.getBookSample2(), 2, "Sample text 2");
    }

    public static BookPageText getBookPageTextRandomSampleGenerator() {
        Book book = BookTestSamples.getBookRandomSampleGenerator();
        int pageNo = Math.max(1, intCount.incrementAndGet() % 500);
        return buildSample(longCount.incrementAndGet(), book, pageNo, "Text-" + UUID.randomUUID());
    }

    private static BookPageText buildSample(Long id, Book book, Integer pageNo, String text) {
        return new BookPageText(id, book.documentId(), pageNo, text, book);
    }
}
