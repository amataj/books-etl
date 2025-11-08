package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRunTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestRunTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngestRun.class);
        IngestRun ingestRun1 = getIngestRunSample1();
        IngestRun ingestRun2 = new IngestRun();
        assertThat(ingestRun1).isNotEqualTo(ingestRun2);

        ingestRun2.setId(ingestRun1.getId());
        assertThat(ingestRun1).isEqualTo(ingestRun2);

        ingestRun2 = getIngestRunSample2();
        assertThat(ingestRun1).isNotEqualTo(ingestRun2);
    }
}
