package com.example.books.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import com.example.books.domain.book.Book;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book bookDTO1 = new Book();
        bookDTO1.setId(1L);
        Book bookDTO2 = new Book();
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO2.setId(bookDTO1.getId());
        assertThat(bookDTO1).isEqualTo(bookDTO2);
        bookDTO2.setId(2L);
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO1.setId(null);
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
    }
}
