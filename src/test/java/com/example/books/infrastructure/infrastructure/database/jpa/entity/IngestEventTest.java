package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestEventTestSamples.*;
import static com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRunTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngestEvent.class);
        IngestEvent ingestEvent1 = getIngestEventSample1();
        IngestEvent ingestEvent2 = new IngestEvent();
        assertThat(ingestEvent1).isNotEqualTo(ingestEvent2);

        ingestEvent2.setId(ingestEvent1.getId());
        assertThat(ingestEvent1).isEqualTo(ingestEvent2);

        ingestEvent2 = getIngestEventSample2();
        assertThat(ingestEvent1).isNotEqualTo(ingestEvent2);
    }

    @Test
    void ingestRunTest() {
        IngestEvent ingestEvent = getIngestEventRandomSampleGenerator();
        IngestRun ingestRunBack = getIngestRunRandomSampleGenerator();

        ingestEvent.setIngestRun(ingestRunBack);
        assertThat(ingestEvent.getIngestRun()).isEqualTo(ingestRunBack);

        ingestEvent.ingestRun(null);
        assertThat(ingestEvent.getIngestRun()).isNull();
    }
}
