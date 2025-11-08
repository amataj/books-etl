package com.example.books.domain.core.ingestevent;

import com.example.books.domain.DomainValidationException;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Domain aggregate for ingest events.
 */
public record IngestEvent(Long id, UUID runId, String documentId, String topic, String payload, ZonedDateTime createdAt, Long ingestRunId) {
    public IngestEvent {
        if (topic == null || topic.isBlank() || topic.length() > 64) {
            throw new DomainValidationException("topic must be provided (<= 64 chars)");
        }
        if (payload == null || payload.isBlank()) {
            throw new DomainValidationException("payload must be provided");
        }
        if (documentId != null && documentId.length() > 64) {
            throw new DomainValidationException("documentId must be <= 64 characters");
        }
    }

    public IngestEvent withId(Long newId) {
        return new IngestEvent(newId, runId, documentId, topic, payload, createdAt, ingestRunId);
    }
}
