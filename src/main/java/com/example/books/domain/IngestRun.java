package com.example.books.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IngestRun.
 */
@Entity
@Table(name = "ingest_run")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngestRun implements Serializable {

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

    public IngestRun id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartedAt() {
        return this.startedAt;
    }

    public IngestRun startedAt(ZonedDateTime startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public ZonedDateTime getFinishedAt() {
        return this.finishedAt;
    }

    public IngestRun finishedAt(ZonedDateTime finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public void setFinishedAt(ZonedDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getStatus() {
        return this.status;
    }

    public IngestRun status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFilesSeen() {
        return this.filesSeen;
    }

    public IngestRun filesSeen(Integer filesSeen) {
        this.setFilesSeen(filesSeen);
        return this;
    }

    public void setFilesSeen(Integer filesSeen) {
        this.filesSeen = filesSeen;
    }

    public Integer getFilesParsed() {
        return this.filesParsed;
    }

    public IngestRun filesParsed(Integer filesParsed) {
        this.setFilesParsed(filesParsed);
        return this;
    }

    public void setFilesParsed(Integer filesParsed) {
        this.filesParsed = filesParsed;
    }

    public Integer getFilesFailed() {
        return this.filesFailed;
    }

    public IngestRun filesFailed(Integer filesFailed) {
        this.setFilesFailed(filesFailed);
        return this;
    }

    public void setFilesFailed(Integer filesFailed) {
        this.filesFailed = filesFailed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngestRun)) {
            return false;
        }
        return getId() != null && getId().equals(((IngestRun) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngestRun{" +
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
