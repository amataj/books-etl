package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BookFile.
 */
@Entity
@Table(name = "book_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "path_norm", nullable = false)
    private String pathNorm;

    @NotNull
    @Size(min = 10, max = 64)
    @Column(name = "sha_256", length = 64, nullable = false)
    private String sha256;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "mtime")
    private ZonedDateTime mtime;

    @Lob
    @Column(name = "storage_uri")
    private String storageUri;

    @Column(name = "first_seen_at")
    private ZonedDateTime firstSeenAt;

    @Column(name = "last_seen_at")
    private ZonedDateTime lastSeenAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "files", "pageTexts" }, allowSetters = true)
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathNorm() {
        return this.pathNorm;
    }

    public BookFile pathNorm(String pathNorm) {
        this.setPathNorm(pathNorm);
        return this;
    }

    public void setPathNorm(String pathNorm) {
        this.pathNorm = pathNorm;
    }

    public String getSha256() {
        return this.sha256;
    }

    public BookFile sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Long getSizeBytes() {
        return this.sizeBytes;
    }

    public BookFile sizeBytes(Long sizeBytes) {
        this.setSizeBytes(sizeBytes);
        return this;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public ZonedDateTime getMtime() {
        return this.mtime;
    }

    public BookFile mtime(ZonedDateTime mtime) {
        this.setMtime(mtime);
        return this;
    }

    public void setMtime(ZonedDateTime mtime) {
        this.mtime = mtime;
    }

    public String getStorageUri() {
        return this.storageUri;
    }

    public BookFile storageUri(String storageUri) {
        this.setStorageUri(storageUri);
        return this;
    }

    public void setStorageUri(String storageUri) {
        this.storageUri = storageUri;
    }

    public ZonedDateTime getFirstSeenAt() {
        return this.firstSeenAt;
    }

    public BookFile firstSeenAt(ZonedDateTime firstSeenAt) {
        this.setFirstSeenAt(firstSeenAt);
        return this;
    }

    public void setFirstSeenAt(ZonedDateTime firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public ZonedDateTime getLastSeenAt() {
        return this.lastSeenAt;
    }

    public BookFile lastSeenAt(ZonedDateTime lastSeenAt) {
        this.setLastSeenAt(lastSeenAt);
        return this;
    }

    public void setLastSeenAt(ZonedDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookFile book(Book book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookFile)) {
            return false;
        }
        return getId() != null && getId().equals(((BookFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookFile{" +
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
