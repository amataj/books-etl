package com.example.books.domain.core;

import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link BookFileEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookFile implements Serializable {

    private Long id;

    @Lob
    private String pathNorm;

    @NotNull
    @Size(min = 10, max = 64)
    private String sha256;

    private Long sizeBytes;

    private ZonedDateTime mtime;

    @Lob
    private String storageUri;

    private ZonedDateTime firstSeenAt;

    private ZonedDateTime lastSeenAt;

    private Book book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathNorm() {
        return pathNorm;
    }

    public void setPathNorm(String pathNorm) {
        this.pathNorm = pathNorm;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public ZonedDateTime getMtime() {
        return mtime;
    }

    public void setMtime(ZonedDateTime mtime) {
        this.mtime = mtime;
    }

    public String getStorageUri() {
        return storageUri;
    }

    public void setStorageUri(String storageUri) {
        this.storageUri = storageUri;
    }

    public ZonedDateTime getFirstSeenAt() {
        return firstSeenAt;
    }

    public void setFirstSeenAt(ZonedDateTime firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public ZonedDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(ZonedDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookFile)) {
            return false;
        }

        BookFile bookFileDTO = (BookFile) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookFileDTO{" +
            "id=" + getId() +
            ", pathNorm='" + getPathNorm() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", sizeBytes=" + getSizeBytes() +
            ", mtime='" + getMtime() + "'" +
            ", storageUri='" + getStorageUri() + "'" +
            ", firstSeenAt='" + getFirstSeenAt() + "'" +
            ", lastSeenAt='" + getLastSeenAt() + "'" +
            ", book=" + getBook() +
            "}";
    }
}
