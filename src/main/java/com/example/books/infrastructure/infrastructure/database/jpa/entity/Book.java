package com.example.books.infrastructure.infrastructure.database.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Book implements Serializable {

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
    private Set<BookFile> files = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "book" }, allowSetters = true)
    private Set<BookPageText> pageTexts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Book id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public Book documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public Book author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLang() {
        return this.lang;
    }

    public Book lang(String lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getPages() {
        return this.pages;
    }

    public Book pages(Integer pages) {
        this.setPages(pages);
        return this;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Set<BookFile> getFiles() {
        return this.files;
    }

    public void setFiles(Set<BookFile> bookFiles) {
        if (this.files != null) {
            this.files.forEach(i -> i.setBook(null));
        }
        if (bookFiles != null) {
            bookFiles.forEach(i -> i.setBook(this));
        }
        this.files = bookFiles;
    }

    public Book files(Set<BookFile> bookFiles) {
        this.setFiles(bookFiles);
        return this;
    }

    public Book addFile(BookFile bookFile) {
        this.files.add(bookFile);
        bookFile.setBook(this);
        return this;
    }

    public Book removeFile(BookFile bookFile) {
        this.files.remove(bookFile);
        bookFile.setBook(null);
        return this;
    }

    public Set<BookPageText> getPageTexts() {
        return this.pageTexts;
    }

    public void setPageTexts(Set<BookPageText> bookPageTexts) {
        if (this.pageTexts != null) {
            this.pageTexts.forEach(i -> i.setBook(null));
        }
        if (bookPageTexts != null) {
            bookPageTexts.forEach(i -> i.setBook(this));
        }
        this.pageTexts = bookPageTexts;
    }

    public Book pageTexts(Set<BookPageText> bookPageTexts) {
        this.setPageTexts(bookPageTexts);
        return this;
    }

    public Book addPageText(BookPageText bookPageText) {
        this.pageTexts.add(bookPageText);
        bookPageText.setBook(this);
        return this;
    }

    public Book removePageText(BookPageText bookPageText) {
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
        if (!(o instanceof Book)) {
            return false;
        }
        return getId() != null && getId().equals(((Book) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", title='" + getTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", lang='" + getLang() + "'" +
            ", pages=" + getPages() +
            "}";
    }
}
