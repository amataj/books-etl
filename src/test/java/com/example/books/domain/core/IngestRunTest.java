package com.example.books.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.domain.ingestrun.IngestRun;
import com.example.books.interfaces.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestRunTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngestRun.class);
        IngestRun ingestRunDTO1 = new IngestRun();
        ingestRunDTO1.setId(1L);
        IngestRun ingestRunDTO2 = new IngestRun();
        assertThat(ingestRunDTO1).isNotEqualTo(ingestRunDTO2);
        ingestRunDTO2.setId(ingestRunDTO1.getId());
        assertThat(ingestRunDTO1).isEqualTo(ingestRunDTO2);
        ingestRunDTO2.setId(2L);
        assertThat(ingestRunDTO1).isNotEqualTo(ingestRunDTO2);
        ingestRunDTO1.setId(null);
        assertThat(ingestRunDTO1).isNotEqualTo(ingestRunDTO2);
    }
}
