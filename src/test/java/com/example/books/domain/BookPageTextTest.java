package com.example.books.domain;

import static com.example.books.domain.BookPageTextTestSamples.getBookPageTextRandomSampleGenerator;
import static com.example.books.domain.BookPageTextTestSamples.getBookPageTextSample1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.book.Book;
import com.example.books.domain.core.bookpagetext.BookPageText;
import org.junit.jupiter.api.Test;

class BookPageTextTest {

    @Test
    void withIdReusesOtherAttributes() {
        BookPageText original = getBookPageTextSample1();
        BookPageText updated = original.withId(111L);

        assertThat(updated.id()).isEqualTo(111L);
        assertThat(updated.book()).isEqualTo(original.book());
        assertThat(updated.pageNo()).isEqualTo(original.pageNo());
    }

    @Test
    void pageNumberMustBePositive() {
        Book book = getBookPageTextSample1().book();
        assertThatThrownBy(() -> new BookPageText(1L, book.documentId(), 0, "text", book)).isInstanceOf(DomainValidationException.class);
    }

    @Test
    void randomSampleIncludesBookReference() {
        BookPageText random = getBookPageTextRandomSampleGenerator();
        assertThat(random.book()).isNotNull();
        assertThat(random.documentId()).isEqualTo(random.book().documentId());
    }
}
