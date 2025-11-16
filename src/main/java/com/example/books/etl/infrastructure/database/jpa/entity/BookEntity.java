package com.example.books.etl.infrastructure.database.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * JPA entity backing the Book aggregate.
 */
@Entity
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 64)
    @Column(name = "document_id", length = 64, nullable = false, unique = true)
    private String documentId;

    @Size(max = 512)
    @Column(name = "title", length = 512)
    private String title;

    @Size(max = 256)
    @Column(name = "author", length = 256)
    private String author;

    @Size(max = 16)
    @Column(name = "lang", length = 16)
    private String lang;

    @Min(value = 1)
    @Column(name = "pages")
    private Integer pages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "book" }, allowSetters = true)
    private Set<BookFileEntity> files = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "book" }, allowSetters = true)
    private Set<BookPageTextEntity> pageTexts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public BookEntity documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookEntity title(String title) {
        this.setTitle(title);
        return this;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookEntity author(String author) {
        this.setAuthor(author);
        return this;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public BookEntity lang(String lang) {
        this.setLang(lang);
        return this;
    }

    public Integer getPages() {
        return this.pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public BookEntity pages(Integer pages) {
        this.setPages(pages);
        return this;
    }

    public Set<BookFileEntity> getFiles() {
        return this.files;
    }

    public void setFiles(Set<BookFileEntity> bookFiles) {
        if (this.files != null) {
            this.files.forEach(i -> i.setBook(null));
        }
        if (bookFiles != null) {
            bookFiles.forEach(i -> i.setBook(this));
        }
        this.files = bookFiles;
    }

    public BookEntity files(Set<BookFileEntity> bookFiles) {
        this.setFiles(bookFiles);
        return this;
    }

    public BookEntity addFile(BookFileEntity bookFile) {
        this.files.add(bookFile);
        bookFile.setBook(this);
        return this;
    }

    public BookEntity removeFile(BookFileEntity bookFile) {
        this.files.remove(bookFile);
        bookFile.setBook(null);
        return this;
    }

    public Set<BookPageTextEntity> getPageTexts() {
        return this.pageTexts;
    }

    public void setPageTexts(Set<BookPageTextEntity> bookPageTexts) {
        if (this.pageTexts != null) {
            this.pageTexts.forEach(i -> i.setBook(null));
        }
        if (bookPageTexts != null) {
            bookPageTexts.forEach(i -> i.setBook(this));
        }
        this.pageTexts = bookPageTexts;
    }

    public BookEntity pageTexts(Set<BookPageTextEntity> bookPageTexts) {
        this.setPageTexts(bookPageTexts);
        return this;
    }

    public BookEntity addPageText(BookPageTextEntity bookPageText) {
        this.pageTexts.add(bookPageText);
        bookPageText.setBook(this);
        return this;
    }

    public BookEntity removePageText(BookPageTextEntity bookPageText) {
        this.pageTexts.remove(bookPageText);
        bookPageText.setBook(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((BookEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookEntity{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", title='" + getTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", lang='" + getLang() + "'" +
            ", pages=" + getPages() +
            "}";
    }
}
