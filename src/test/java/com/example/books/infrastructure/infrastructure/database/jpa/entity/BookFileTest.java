package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.infrastructure.database.jpa.entity.BookFileTestSamples.*;
import static com.example.books.infrastructure.infrastructure.database.jpa.entity.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookFile.class);
        BookFile bookFile1 = getBookFileSample1();
        BookFile bookFile2 = new BookFile();
        assertThat(bookFile1).isNotEqualTo(bookFile2);

        bookFile2.setId(bookFile1.getId());
        assertThat(bookFile1).isEqualTo(bookFile2);

        bookFile2 = getBookFileSample2();
        assertThat(bookFile1).isNotEqualTo(bookFile2);
    }

    @Test
    void bookTest() {
        BookFile bookFile = getBookFileRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        bookFile.setBook(bookBack);
        assertThat(bookFile.getBook()).isEqualTo(bookBack);

        bookFile.book(null);
        assertThat(bookFile.getBook()).isNull();
    }
}
