package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IngestEvent.
 */
@Entity
@Table(name = "ingest_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngestEvent implements Serializable {

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

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private IngestRun ingestRun;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IngestEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRunId() {
        return this.runId;
    }

    public IngestEvent runId(UUID runId) {
        this.setRunId(runId);
        return this;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public IngestEvent documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTopic() {
        return this.topic;
    }

    public IngestEvent topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return this.payload;
    }

    public IngestEvent payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public IngestEvent createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public IngestRun getIngestRun() {
        return this.ingestRun;
    }

    public void setIngestRun(IngestRun ingestRun) {
        this.ingestRun = ingestRun;
    }

    public IngestEvent ingestRun(IngestRun ingestRun) {
        this.setIngestRun(ingestRun);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngestEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((IngestEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngestEvent{" +
            "id=" + getId() +
            ", runId='" + getRunId() + "'" +
            ", documentId='" + getDocumentId() + "'" +
            ", topic='" + getTopic() + "'" +
            ", payload='" + getPayload() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
