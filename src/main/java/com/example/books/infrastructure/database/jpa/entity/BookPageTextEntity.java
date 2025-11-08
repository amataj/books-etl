package com.example.books.infrastructure.database.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * JPA entity for page text persistence.
 */
@Entity(name = "BookPageText")
@Table(name = "book_page_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookPageTextEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 64)
    @Column(name = "document_id", length = 64, nullable = false)
    private String documentId;

    @NotNull
    @Min(value = 1)
    @Column(name = "page_no", nullable = false)
    private Integer pageNo;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "text", columnDefinition = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "files", "pageTexts" }, allowSetters = true)
    private BookEntity book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookPageTextEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public BookPageTextEntity documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public BookPageTextEntity pageNo(Integer pageNo) {
        this.setPageNo(pageNo);
        return this;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BookPageTextEntity text(String text) {
        this.setText(text);
        return this;
    }

    public BookEntity getBook() {
        return this.book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public BookPageTextEntity book(BookEntity book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookPageTextEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((BookPageTextEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookPageTextEntity{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", pageNo=" + getPageNo() +
            ", text='" + getText() + "'" +
            "}";
    }
}
