package com.example.books.infrastructure.database.jpa.entity;

import static com.example.books.infrastructure.database.jpa.entity.IngestEventTestSamples.*;
import static com.example.books.infrastructure.database.jpa.entity.IngestRunTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.books.adapter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngestEventEntity.class);
        IngestEventEntity ingestEvent1 = getIngestEventSample1();
        IngestEventEntity ingestEvent2 = new IngestEventEntity();
        assertThat(ingestEvent1).isNotEqualTo(ingestEvent2);

        ingestEvent2.setId(ingestEvent1.getId());
        assertThat(ingestEvent1).isEqualTo(ingestEvent2);

        ingestEvent2 = getIngestEventSample2();
        assertThat(ingestEvent1).isNotEqualTo(ingestEvent2);
    }

    @Test
    void ingestRunTest() {
        IngestEventEntity ingestEvent = getIngestEventRandomSampleGenerator();
        IngestRunEntity ingestRunBack = getIngestRunRandomSampleGenerator();

        ingestEvent.setIngestRun(ingestRunBack);
        assertThat(ingestEvent.getIngestRun()).isEqualTo(ingestRunBack);

        ingestEvent.ingestRun(null);
        assertThat(ingestEvent.getIngestRun()).isNull();
    }
}
