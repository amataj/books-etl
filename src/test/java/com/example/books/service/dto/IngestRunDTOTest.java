package com.example.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestRunDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngestRunDTO.class);
        IngestRunDTO ingestRunDTO1 = new IngestRunDTO();
        ingestRunDTO1.setId(1L);
        IngestRunDTO ingestRunDTO2 = new IngestRunDTO();
        assertThat(ingestRunDTO1).isNotEqualTo(ingestRunDTO2);
        ingestRunDTO2.setId(ingestRunDTO1.getId());
        assertThat(ingestRunDTO1).isEqualTo(ingestRunDTO2);
        ingestRunDTO2.setId(2L);
        assertThat(ingestRunDTO1).isNotEqualTo(ingestRunDTO2);
        ingestRunDTO1.setId(null);
        assertThat(ingestRunDTO1).isNotEqualTo(ingestRunDTO2);
    }
}
