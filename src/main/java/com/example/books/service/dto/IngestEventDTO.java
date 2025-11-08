package com.example.books.service.dto;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestEventEntity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link IngestEventEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngestEventDTO implements Serializable {

    private Long id;

    private UUID runId;

    @Size(max = 64)
    private String documentId;

    @NotNull
    @Size(max = 64)
    private String topic;

    @Lob
    private String payload;

    private ZonedDateTime createdAt;

    private IngestRunDTO ingestRun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRunId() {
        return runId;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public IngestRunDTO getIngestRun() {
        return ingestRun;
    }

    public void setIngestRun(IngestRunDTO ingestRun) {
        this.ingestRun = ingestRun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngestEventDTO)) {
            return false;
        }

        IngestEventDTO ingestEventDTO = (IngestEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingestEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngestEventDTO{" +
            "id=" + getId() +
            ", runId='" + getRunId() + "'" +
            ", documentId='" + getDocumentId() + "'" +
            ", topic='" + getTopic() + "'" +
            ", payload='" + getPayload() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", ingestRun=" + getIngestRun() +
            "}";
    }
}
