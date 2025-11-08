package com.example.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookPageTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookPageTextDTO.class);
        BookPageTextDTO bookPageTextDTO1 = new BookPageTextDTO();
        bookPageTextDTO1.setId(1L);
        BookPageTextDTO bookPageTextDTO2 = new BookPageTextDTO();
        assertThat(bookPageTextDTO1).isNotEqualTo(bookPageTextDTO2);
        bookPageTextDTO2.setId(bookPageTextDTO1.getId());
        assertThat(bookPageTextDTO1).isEqualTo(bookPageTextDTO2);
        bookPageTextDTO2.setId(2L);
        assertThat(bookPageTextDTO1).isNotEqualTo(bookPageTextDTO2);
        bookPageTextDTO1.setId(null);
        assertThat(bookPageTextDTO1).isNotEqualTo(bookPageTextDTO2);
    }
}
