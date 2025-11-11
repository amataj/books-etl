package com.example.books.usecase.book;

import com.example.books.domain.book.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use case boundary for book operations consumed by adapters.
 */
public interface BookUseCase {
    Book create(Book book);

    Book update(Book book);

    Optional<Book> partialUpdate(Book book);

    Page<Book> findAll(Pageable pageable);

    Optional<Book> findOne(Long id);

    void delete(Long id);

    boolean exists(Long id);
}
