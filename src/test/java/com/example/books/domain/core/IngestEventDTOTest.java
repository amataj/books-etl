package com.example.books.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngestEventDTO.class);
        IngestEventDTO ingestEventDTO1 = new IngestEventDTO();
        ingestEventDTO1.setId(1L);
        IngestEventDTO ingestEventDTO2 = new IngestEventDTO();
        assertThat(ingestEventDTO1).isNotEqualTo(ingestEventDTO2);
        ingestEventDTO2.setId(ingestEventDTO1.getId());
        assertThat(ingestEventDTO1).isEqualTo(ingestEventDTO2);
        ingestEventDTO2.setId(2L);
        assertThat(ingestEventDTO1).isNotEqualTo(ingestEventDTO2);
        ingestEventDTO1.setId(null);
        assertThat(ingestEventDTO1).isNotEqualTo(ingestEventDTO2);
    }
}
