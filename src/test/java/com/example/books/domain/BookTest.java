package com.example.books.domain;

import static com.example.books.domain.BookFileTestSamples.*;
import static com.example.books.domain.BookPageTextTestSamples.*;
import static com.example.books.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void fileTest() {
        Book book = getBookRandomSampleGenerator();
        BookFile bookFileBack = getBookFileRandomSampleGenerator();

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
        Book book = getBookRandomSampleGenerator();
        BookPageText bookPageTextBack = getBookPageTextRandomSampleGenerator();

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
