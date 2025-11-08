package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.infrastructure.database.jpa.entity.BookPageTextTestSamples.*;
import static com.example.books.infrastructure.infrastructure.database.jpa.entity.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookPageTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookPageTextEntity.class);
        BookPageTextEntity bookPageText1 = getBookPageTextSample1();
        BookPageTextEntity bookPageText2 = new BookPageTextEntity();
        assertThat(bookPageText1).isNotEqualTo(bookPageText2);

        bookPageText2.setId(bookPageText1.getId());
        assertThat(bookPageText1).isEqualTo(bookPageText2);

        bookPageText2 = getBookPageTextSample2();
        assertThat(bookPageText1).isNotEqualTo(bookPageText2);
    }

    @Test
    void bookTest() {
        BookPageTextEntity bookPageText = getBookPageTextRandomSampleGenerator();
        BookEntity bookBack = getBookRandomSampleGenerator();

        bookPageText.setBook(bookBack);
        assertThat(bookPageText.getBook()).isEqualTo(bookBack);

        bookPageText.book(null);
        assertThat(bookPageText.getBook()).isNull();
    }
}
