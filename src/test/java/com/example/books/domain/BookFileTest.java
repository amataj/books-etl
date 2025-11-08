package com.example.books.domain;

import static com.example.books.domain.BookFileTestSamples.getBookFileRandomSampleGenerator;
import static com.example.books.domain.BookFileTestSamples.getBookFileSample1;
import static com.example.books.domain.BookTestSamples.getBookSample1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.book.Book;
import com.example.books.domain.core.bookfile.BookFile;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class BookFileTest {

    @Test
    void withIdCreatesNewInstanceWithSameAttributes() {
        BookFile original = getBookFileSample1();
        BookFile rekeyed = original.withId(50L);

        assertThat(rekeyed.id()).isEqualTo(50L);
        assertThat(rekeyed.pathNorm()).isEqualTo(original.pathNorm());
        assertThat(rekeyed.book()).isEqualTo(original.book());
    }

    @Test
    void constructorRequiresBookReference() {
        Book book = getBookSample1();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);

        assertThatThrownBy(() -> new BookFile(1L, "/tmp/book.pdf", "1234567890", 10L, now, "s3://bucket/book.pdf", now, now, null)
        ).isInstanceOf(DomainValidationException.class);

        BookFile valid = new BookFile(1L, "/tmp/book.pdf", "1234567890", 10L, now, "s3://bucket/book.pdf", now, now, book);
        assertThat(valid.book()).isEqualTo(book);
    }

    @Test
    void randomSampleProvidesNonNullRelationships() {
        BookFile sample = getBookFileRandomSampleGenerator();
        assertThat(sample.book()).isNotNull();
        assertThat(sample.sha256()).hasSizeBetween(10, 64);
    }
}
