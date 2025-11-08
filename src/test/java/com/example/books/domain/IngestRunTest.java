package com.example.books.domain;

import static com.example.books.domain.IngestRunTestSamples.getIngestRunRandomSampleGenerator;
import static com.example.books.domain.IngestRunTestSamples.getIngestRunSample1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.ingestrun.IngestRun;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class IngestRunTest {

    @Test
    void withIdReturnsNewValueWithoutMutatingOriginal() {
        IngestRun original = getIngestRunSample1();
        IngestRun rekeyed = original.withId(321L);

        assertThat(rekeyed.id()).isEqualTo(321L);
        assertThat(rekeyed).isNotSameAs(original);
        assertThat(rekeyed.status()).isEqualTo(original.status());
    }

    @Test
    void statusAndCountersAreValidated() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        assertThatThrownBy(() -> new IngestRun(1L, now, now, "", 0, 0, 0)).isInstanceOf(DomainValidationException.class);

        assertThatThrownBy(() -> new IngestRun(1L, now, now, "OK", -1, 0, 0)).isInstanceOf(DomainValidationException.class);
    }

    @Test
    void randomSampleMaintainsInvariants() {
        IngestRun run = getIngestRunRandomSampleGenerator();
        assertThat(run.filesSeen()).isGreaterThanOrEqualTo(0);
        assertThat(run.filesParsed()).isGreaterThanOrEqualTo(0);
        assertThat(run.filesFailed()).isGreaterThanOrEqualTo(0);
    }
}
