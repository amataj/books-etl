package com.example.books.domain.core.ingestrun;

import com.example.books.domain.DomainValidationException;
import java.time.ZonedDateTime;

/**
 * Domain aggregate for an ingest run.
 */
public record IngestRun(
    Long id,
    ZonedDateTime startedAt,
    ZonedDateTime finishedAt,
    String status,
    Integer filesSeen,
    Integer filesParsed,
    Integer filesFailed
) {
    public IngestRun {
        if (status == null || status.isBlank() || status.length() > 32) {
            throw new DomainValidationException("status is required and must be <= 32 characters");
        }
        ensureNonNegative(filesSeen, "filesSeen");
        ensureNonNegative(filesParsed, "filesParsed");
        ensureNonNegative(filesFailed, "filesFailed");
    }

    private static void ensureNonNegative(Integer value, String field) {
        if (value != null && value < 0) {
            throw new DomainValidationException(field + " must be >= 0");
        }
    }

    public IngestRun withId(Long newId) {
        return new IngestRun(newId, startedAt, finishedAt, status, filesSeen, filesParsed, filesFailed);
    }
}
