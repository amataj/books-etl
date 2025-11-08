package com.example.books.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookFileDTO.class);
        BookFileDTO bookFileDTO1 = new BookFileDTO();
        bookFileDTO1.setId(1L);
        BookFileDTO bookFileDTO2 = new BookFileDTO();
        assertThat(bookFileDTO1).isNotEqualTo(bookFileDTO2);
        bookFileDTO2.setId(bookFileDTO1.getId());
        assertThat(bookFileDTO1).isEqualTo(bookFileDTO2);
        bookFileDTO2.setId(2L);
        assertThat(bookFileDTO1).isNotEqualTo(bookFileDTO2);
        bookFileDTO1.setId(null);
        assertThat(bookFileDTO1).isNotEqualTo(bookFileDTO2);
    }
}
