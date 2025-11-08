package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * JPA entity for ingest event persistence.
 */
@Entity
@Table(name = "ingest_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngestEventEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "run_id")
    private UUID runId;

    @Size(max = 64)
    @Column(name = "document_id", length = 64)
    private String documentId;

    @NotNull
    @Size(max = 64)
    @Column(name = "topic", length = 64, nullable = false)
    private String topic;

    @Column(name = "payload", nullable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private IngestRunEntity ingestRun;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngestEventEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public UUID getRunId() {
        return this.runId;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public IngestEventEntity runId(UUID runId) {
        this.setRunId(runId);
        return this;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public IngestEventEntity documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public IngestEventEntity topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public IngestEventEntity payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public IngestEventEntity createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public IngestRunEntity getIngestRun() {
        return this.ingestRun;
    }

    public void setIngestRun(IngestRunEntity ingestRun) {
        this.ingestRun = ingestRun;
    }

    public IngestEventEntity ingestRun(IngestRunEntity ingestRun) {
        this.setIngestRun(ingestRun);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngestEventEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((IngestEventEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngestEventEntity{" +
            "id=" + getId() +
            ", runId='" + getRunId() + "'" +
            ", documentId='" + getDocumentId() + "'" +
            ", topic='" + getTopic() + "'" +
            ", payload='" + getPayload() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
