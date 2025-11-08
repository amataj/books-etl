package com.example.books.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.database.jpa.entity.BookFileTestSamples.*;
import static com.example.books.infrastructure.database.jpa.entity.BookPageTextTestSamples.*;
import static com.example.books.infrastructure.database.jpa.entity.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookEntity.class);
        BookEntity book1 = getBookSample1();
        BookEntity book2 = new BookEntity();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void fileTest() {
        BookEntity book = getBookRandomSampleGenerator();
        BookFileEntity bookFileBack = getBookFileRandomSampleGenerator();

        book.addFile(bookFileBack);
        assertThat(book.getFiles()).containsOnly(bookFileBack);
        assertThat(bookFileBack.getBook()).isEqualTo(book);

        book.removeFile(bookFileBack);
        assertThat(book.getFiles()).doesNotContain(bookFileBack);
        assertThat(bookFileBack.getBook()).isNull();

        book.files(new HashSet<>(Set.of(bookFileBack)));
        assertThat(book.getFiles()).containsOnly(bookFileBack);
        assertThat(bookFileBack.getBook()).isEqualTo(book);

        book.setFiles(new HashSet<>());
        assertThat(book.getFiles()).doesNotContain(bookFileBack);
        assertThat(bookFileBack.getBook()).isNull();
    }

    @Test
    void pageTextTest() {
        BookEntity book = getBookRandomSampleGenerator();
        BookPageTextEntity bookPageTextBack = getBookPageTextRandomSampleGenerator();

        book.addPageText(bookPageTextBack);
        assertThat(book.getPageTexts()).containsOnly(bookPageTextBack);
        assertThat(bookPageTextBack.getBook()).isEqualTo(book);

        book.removePageText(bookPageTextBack);
        assertThat(book.getPageTexts()).doesNotContain(bookPageTextBack);
        assertThat(bookPageTextBack.getBook()).isNull();

        book.pageTexts(new HashSet<>(Set.of(bookPageTextBack)));
        assertThat(book.getPageTexts()).containsOnly(bookPageTextBack);
        assertThat(bookPageTextBack.getBook()).isEqualTo(book);

        book.setPageTexts(new HashSet<>());
        assertThat(book.getPageTexts()).doesNotContain(bookPageTextBack);
        assertThat(bookPageTextBack.getBook()).isNull();
    }
}
