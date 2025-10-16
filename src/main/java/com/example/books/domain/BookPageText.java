package com.example.books.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BookPageText.
 */
@Entity
@Table(name = "book_page_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookPageText implements Serializable {

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

    @Lob
    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "files", "pageTexts" }, allowSetters = true)
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookPageText id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public BookPageText documentId(String documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public BookPageText pageNo(Integer pageNo) {
        this.setPageNo(pageNo);
        return this;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getText() {
        return this.text;
    }

    public BookPageText text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookPageText book(Book book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookPageText)) {
            return false;
        }
        return getId() != null && getId().equals(((BookPageText) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookPageText{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", pageNo=" + getPageNo() +
            ", text='" + getText() + "'" +
            "}";
    }
}
