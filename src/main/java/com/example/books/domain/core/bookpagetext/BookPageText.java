package com.example.books.domain.core.bookpagetext;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.book.Book;

/**
 * Domain model representing textual content of a book page.
 */
public record BookPageText(Long id, String documentId, Integer pageNo, String text, Book book) {
    public BookPageText {
        if (documentId == null || documentId.length() < 10 || documentId.length() > 64) {
            throw new DomainValidationException("documentId must be 10-64 characters");
        }
        if (pageNo == null || pageNo < 1) {
            throw new DomainValidationException("pageNo must be >= 1");
        }
        if (book == null) {
            throw new DomainValidationException("book link is required");
        }
    }

    public BookPageText withId(Long newId) {
        return new BookPageText(newId, documentId, pageNo, text, book);
    }
}
