package com.example.books.domain.bookpage;

import com.example.books.domain.book.Book;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link BookPageTextEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookPageText implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10, max = 64)
    private String documentId;

    @NotNull
    @Min(value = 1)
    private Integer pageNo;

    @Lob
    private String text;

    private Book book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        if (!(o instanceof BookPageText)) {
            return false;
        }

        BookPageText bookPageTextDTO = (BookPageText) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookPageTextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookPageTextDTO{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", pageNo=" + getPageNo() +
            ", text='" + getText() + "'" +
            ", book=" + getBook() +
            "}";
    }
}
