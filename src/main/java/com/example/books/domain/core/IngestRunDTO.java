package com.example.books.domain.core;

import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link IngestRunEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngestRunDTO implements Serializable {

    private Long id;

    private ZonedDateTime startedAt;

    private ZonedDateTime finishedAt;

    @NotNull
    @Size(max = 32)
    private String status;

    @Min(value = 0)
    private Integer filesSeen;

    @Min(value = 0)
    private Integer filesParsed;

    @Min(value = 0)
    private Integer filesFailed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public ZonedDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(ZonedDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFilesSeen() {
        return filesSeen;
    }

    public void setFilesSeen(Integer filesSeen) {
        this.filesSeen = filesSeen;
    }

    public Integer getFilesParsed() {
        return filesParsed;
    }

    public void setFilesParsed(Integer filesParsed) {
        this.filesParsed = filesParsed;
    }

    public Integer getFilesFailed() {
        return filesFailed;
    }

    public void setFilesFailed(Integer filesFailed) {
        this.filesFailed = filesFailed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngestRunDTO)) {
            return false;
        }

        IngestRunDTO ingestRunDTO = (IngestRunDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingestRunDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngestRunDTO{" +
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
