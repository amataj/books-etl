package com.example.books.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import com.example.books.domain.bookpage.BookPageText;
import org.junit.jupiter.api.Test;

class BookPageTextTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookPageText.class);
        BookPageText bookPageTextDTO1 = new BookPageText();
        bookPageTextDTO1.setId(1L);
        BookPageText bookPageTextDTO2 = new BookPageText();
        assertThat(bookPageTextDTO1).isNotEqualTo(bookPageTextDTO2);
        bookPageTextDTO2.setId(bookPageTextDTO1.getId());
        assertThat(bookPageTextDTO1).isEqualTo(bookPageTextDTO2);
        bookPageTextDTO2.setId(2L);
        assertThat(bookPageTextDTO1).isNotEqualTo(bookPageTextDTO2);
        bookPageTextDTO1.setId(null);
        assertThat(bookPageTextDTO1).isNotEqualTo(bookPageTextDTO2);
    }
}
