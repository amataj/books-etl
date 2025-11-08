package com.example.books.adapter.web.rest.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.books.domain.core.bookpagetext.BookPageText} aggregate.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookPageTextDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10, max = 64)
    private String documentId;

    @NotNull
    @Min(value = 1)
    private Integer pageNo;

    @Lob
    private String text;

    private BookDTO book;

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

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookPageTextDTO)) {
            return false;
        }

        BookPageTextDTO bookPageTextDTO = (BookPageTextDTO) o;
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
