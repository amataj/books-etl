package com.example.books.infrastructure.database.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * JPA entity for BookFile persistence.
 */
@Entity
@Table(name = "book_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookFileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 4096)
    @Column(name = "path_norm", length = 4096, nullable = false)
    private String pathNorm;

    @NotNull
    @Size(min = 10, max = 64)
    @Column(name = "sha_256", length = 64, nullable = false)
    private String sha256;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "mtime")
    private ZonedDateTime mtime;

    @Size(max = 2048)
    @Column(name = "storage_uri", length = 2048)
    private String storageUri;

    @Column(name = "first_seen_at")
    private ZonedDateTime firstSeenAt;

    @Column(name = "last_seen_at")
    private ZonedDateTime lastSeenAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "files", "pageTexts" }, allowSetters = true)
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull
    private BookEntity book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookFileEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public String getPathNorm() {
        return this.pathNorm;
    }

    public void setPathNorm(String pathNorm) {
        this.pathNorm = pathNorm;
    }

    public BookFileEntity pathNorm(String pathNorm) {
        this.setPathNorm(pathNorm);
        return this;
    }

    public String getSha256() {
        return this.sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public BookFileEntity sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public Long getSizeBytes() {
        return this.sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public BookFileEntity sizeBytes(Long sizeBytes) {
        this.setSizeBytes(sizeBytes);
        return this;
    }

    public ZonedDateTime getMtime() {
        return this.mtime;
    }

    public void setMtime(ZonedDateTime mtime) {
        this.mtime = mtime;
    }

    public BookFileEntity mtime(ZonedDateTime mtime) {
        this.setMtime(mtime);
        return this;
    }

    public String getStorageUri() {
        return this.storageUri;
    }

    public void setStorageUri(String storageUri) {
        this.storageUri = storageUri;
    }

    public BookFileEntity storageUri(String storageUri) {
        this.setStorageUri(storageUri);
        return this;
    }

    public ZonedDateTime getFirstSeenAt() {
        return this.firstSeenAt;
    }

    public void setFirstSeenAt(ZonedDateTime firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public BookFileEntity firstSeenAt(ZonedDateTime firstSeenAt) {
        this.setFirstSeenAt(firstSeenAt);
        return this;
    }

    public ZonedDateTime getLastSeenAt() {
        return this.lastSeenAt;
    }

    public void setLastSeenAt(ZonedDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public BookFileEntity lastSeenAt(ZonedDateTime lastSeenAt) {
        this.setLastSeenAt(lastSeenAt);
        return this;
    }

    public BookEntity getBook() {
        return this.book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public BookFileEntity book(BookEntity book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookFileEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((BookFileEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookFileEntity{" +
            "id=" + getId() +
            ", pathNorm='" + getPathNorm() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", sizeBytes=" + getSizeBytes() +
            ", mtime='" + getMtime() + "'" +
            ", storageUri='" + getStorageUri() + "'" +
            ", firstSeenAt='" + getFirstSeenAt() + "'" +
            ", lastSeenAt='" + getLastSeenAt() + "'" +
            "}";
    }
}
