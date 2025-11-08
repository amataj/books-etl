package com.example.books.domain.core.bookfile;

import com.example.books.domain.DomainValidationException;
import com.example.books.domain.core.book.Book;
import java.time.ZonedDateTime;

/**
 * Domain model representing the metadata of a book file.
 */
public record BookFile(
    Long id,
    String pathNorm,
    String sha256,
    Long sizeBytes,
    ZonedDateTime mtime,
    String storageUri,
    ZonedDateTime firstSeenAt,
    ZonedDateTime lastSeenAt,
    Book book
) {
    public BookFile {
        if (pathNorm == null || pathNorm.isBlank()) {
            throw new DomainValidationException("pathNorm is required");
        }
        if (sha256 == null || sha256.length() < 10 || sha256.length() > 64) {
            throw new DomainValidationException("sha256 must be between 10 and 64 characters");
        }
        if (book == null) {
            throw new DomainValidationException("book reference is required");
        }
    }

    public BookFile withId(Long newId) {
        return new BookFile(newId, pathNorm, sha256, sizeBytes, mtime, storageUri, firstSeenAt, lastSeenAt, book);
    }
}
