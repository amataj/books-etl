package com.example.books.domain;

import static com.example.books.domain.IngestEventTestSamples.getIngestEventRandomSampleGenerator;
import static com.example.books.domain.IngestEventTestSamples.getIngestEventSample1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.ingestevent.IngestEvent;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class IngestEventTest {

    @Test
    void withIdReturnsNewInstance() {
        IngestEvent original = getIngestEventSample1();
        IngestEvent rekeyed = original.withId(77L);

        assertThat(rekeyed.id()).isEqualTo(77L);
        assertThat(rekeyed.runId()).isEqualTo(original.runId());
        assertThat(rekeyed).isNotSameAs(original);
    }

    @Test
    void topicAndPayloadMustBeProvided() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        UUID runId = UUID.randomUUID();

        assertThatThrownBy(() -> new IngestEvent(1L, runId, "document-1", "", "payload", now, 1L)).isInstanceOf(
            DomainValidationException.class
        );

        assertThatThrownBy(() -> new IngestEvent(1L, runId, "document-1", "topic", "", now, 1L)).isInstanceOf(
            DomainValidationException.class
        );
    }

    @Test
    void randomSampleContainsRunAndIngestIds() {
        IngestEvent sample = getIngestEventRandomSampleGenerator();
        assertThat(sample.runId()).isNotNull();
        assertThat(sample.ingestRunId()).isNotNull();
    }
}
