package com.example.books.domain.service.book;

import com.example.books.domain.core.book.Book;
import java.util.Optional;

/**
 * Domain service responsible for mutating {@link Book} aggregates.
 */
public interface BookService {
    Book save(Book book);

    Book update(Book book);

    Optional<Book> partialUpdate(Book book);

    void delete(Long id);
}
