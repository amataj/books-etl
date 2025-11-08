package com.example.books.domain;

import static com.example.books.domain.BookPageTextTestSamples.*;
import static com.example.books.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookPageTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookPageText.class);
        BookPageText bookPageText1 = getBookPageTextSample1();
        BookPageText bookPageText2 = new BookPageText();
        assertThat(bookPageText1).isNotEqualTo(bookPageText2);

        bookPageText2.setId(bookPageText1.getId());
        assertThat(bookPageText1).isEqualTo(bookPageText2);

        bookPageText2 = getBookPageTextSample2();
        assertThat(bookPageText1).isNotEqualTo(bookPageText2);
    }

    @Test
    void bookTest() {
        BookPageText bookPageText = getBookPageTextRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        bookPageText.setBook(bookBack);
        assertThat(bookPageText.getBook()).isEqualTo(bookBack);

        bookPageText.book(null);
        assertThat(bookPageText.getBook()).isNull();
    }
}
