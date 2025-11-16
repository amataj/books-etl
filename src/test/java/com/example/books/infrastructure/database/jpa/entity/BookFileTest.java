package com.example.books.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.database.jpa.entity.BookFileTestSamples.*;
import static com.example.books.infrastructure.database.jpa.entity.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.interfaces.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookFileEntity.class);
        BookFileEntity bookFile1 = getBookFileSample1();
        BookFileEntity bookFile2 = new BookFileEntity();
        assertThat(bookFile1).isNotEqualTo(bookFile2);

        bookFile2.setId(bookFile1.getId());
        assertThat(bookFile1).isEqualTo(bookFile2);

        bookFile2 = getBookFileSample2();
        assertThat(bookFile1).isNotEqualTo(bookFile2);
    }

    @Test
    void bookTest() {
        BookFileEntity bookFile = getBookFileRandomSampleGenerator();
        BookEntity bookBack = getBookRandomSampleGenerator();

        bookFile.setBook(bookBack);
        assertThat(bookFile.getBook()).isEqualTo(bookBack);

        bookFile.book(null);
        assertThat(bookFile.getBook()).isNull();
    }
}
