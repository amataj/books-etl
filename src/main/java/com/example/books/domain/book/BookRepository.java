package com.example.books.domain.book;

import java.util.Optional;

/**
 * Port for mutating Book aggregates.
 */
public interface BookRepository {
    Book save(Book book);

    void deleteById(Long id);

    Optional<Book> findByDocumentId(String documentId);
}
