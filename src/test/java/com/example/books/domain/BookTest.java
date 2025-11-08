package com.example.books.domain;

import static com.example.books.domain.BookTestSamples.getBookRandomSampleGenerator;
import static com.example.books.domain.BookTestSamples.getBookSample1;
import static com.example.books.domain.BookTestSamples.getBookSample2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.book.Book;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void withIdReturnsNewInstanceWithCopiedState() {
        Book original = getBookSample1();
        Book rekeyed = original.withId(999L);

        assertThat(rekeyed.id()).isEqualTo(999L);
        assertThat(rekeyed.documentId()).isEqualTo(original.documentId());
        assertThat(rekeyed).isNotSameAs(original);
    }

    @Test
    void updateDetailsReplacesMutableFields() {
        Book original = getBookSample1();
        Book updated = original.updateDetails("New Title", "New Author", "es", 42);

        assertThat(updated.documentId()).isEqualTo(original.documentId());
        assertThat(updated.title()).isEqualTo("New Title");
        assertThat(updated.author()).isEqualTo("New Author");
        assertThat(updated.lang()).isEqualTo("es");
        assertThat(updated.pages()).isEqualTo(42);
    }

    @Test
    void sameIdentityMatchesByDocumentId() {
        Book bookOne = getBookSample1();
        Book bookTwo = new Book(bookOne.id(), bookOne.documentId(), "Changed", "Changed", "en", 300);
        Book bookThree = getBookSample2();

        assertThat(bookOne.sameIdentity(bookTwo)).isTrue();
        assertThat(bookOne.sameIdentity(bookThree)).isFalse();
    }

    @Test
    void documentIdMustRespectLengthConstraints() {
        assertThatThrownBy(() -> new Book(1L, "short", "Title", "Author", "en", 10)).isInstanceOf(DomainValidationException.class);
    }

    @Test
    void randomSampleAlwaysMeetsInvariants() {
        Book book = getBookRandomSampleGenerator();
        assertThat(book.documentId()).hasSizeBetween(10, 64);
        assertThat(book.pages()).isGreaterThanOrEqualTo(1);
    }
}
