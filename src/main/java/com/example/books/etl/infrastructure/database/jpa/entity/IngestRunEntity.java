package com.example.books.etl.infrastructure.database.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * JPA entity for ingest run persistence.
 */
@Entity
@Table(name = "ingest_run")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngestRunEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "finished_at")
    private ZonedDateTime finishedAt;

    @NotNull
    @Size(max = 32)
    @Column(name = "status", length = 32, nullable = false)
    private String status;

    @Min(value = 0)
    @Column(name = "files_seen")
    private Integer filesSeen;

    @Min(value = 0)
    @Column(name = "files_parsed")
    private Integer filesParsed;

    @Min(value = 0)
    @Column(name = "files_failed")
    private Integer filesFailed;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngestRunEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public ZonedDateTime getStartedAt() {
        return this.startedAt;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public IngestRunEntity startedAt(ZonedDateTime startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public ZonedDateTime getFinishedAt() {
        return this.finishedAt;
    }

    public void setFinishedAt(ZonedDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public IngestRunEntity finishedAt(ZonedDateTime finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IngestRunEntity status(String status) {
        this.setStatus(status);
        return this;
    }

    public Integer getFilesSeen() {
        return this.filesSeen;
    }

    public void setFilesSeen(Integer filesSeen) {
        this.filesSeen = filesSeen;
    }

    public IngestRunEntity filesSeen(Integer filesSeen) {
        this.setFilesSeen(filesSeen);
        return this;
    }

    public Integer getFilesParsed() {
        return this.filesParsed;
    }

    public void setFilesParsed(Integer filesParsed) {
        this.filesParsed = filesParsed;
    }

    public IngestRunEntity filesParsed(Integer filesParsed) {
        this.setFilesParsed(filesParsed);
        return this;
    }

    public Integer getFilesFailed() {
        return this.filesFailed;
    }

    public void setFilesFailed(Integer filesFailed) {
        this.filesFailed = filesFailed;
    }

    public IngestRunEntity filesFailed(Integer filesFailed) {
        this.setFilesFailed(filesFailed);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngestRunEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((IngestRunEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngestRunEntity{" +
            "id=" + getId() +
            ", startedAt='" + getStartedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", filesSeen=" + getFilesSeen() +
            ", filesParsed=" + getFilesParsed() +
            ", filesFailed=" + getFilesFailed() +
            "}";
    }
}
