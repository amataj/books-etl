package com.example.books.domain.core.book;

import com.example.books.domain.DomainValidationException;
import java.util.Objects;

/**
 * Domain representation of a Book aggregate.
 */
public record Book(Long id, String documentId, String title, String author, String lang, Integer pages) {
    public Book {
        if (documentId == null || documentId.length() < 10 || documentId.length() > 64) {
            throw new DomainValidationException("documentId must be between 10 and 64 characters");
        }
        if (title != null && title.length() > 512) {
            throw new DomainValidationException("title exceeds 512 characters");
        }
        if (author != null && author.length() > 256) {
            throw new DomainValidationException("author exceeds 256 characters");
        }
        if (lang != null && lang.length() > 16) {
            throw new DomainValidationException("lang exceeds 16 characters");
        }
        if (pages != null && pages < 1) {
            throw new DomainValidationException("pages must be >= 1");
        }
    }

    public Book withId(Long newId) {
        return new Book(newId, documentId, title, author, lang, pages);
    }

    public Book updateDetails(String newTitle, String newAuthor, String newLang, Integer newPages) {
        return new Book(id, documentId, newTitle, newAuthor, newLang, newPages);
    }

    public boolean sameIdentity(Book other) {
        return other != null && Objects.equals(documentId, other.documentId);
    }
}
