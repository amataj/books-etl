package com.example.books.domain.repository.book;

import com.example.books.domain.core.book.Book;
import java.util.Optional;

/**
 * Port for mutating Book aggregates.
 */
public interface BookRepository {
    Book save(Book book);

    void deleteById(Long id);

    Optional<Book> findByDocumentId(String documentId);
}
