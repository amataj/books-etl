package com.example.books.domain.book;

import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Read-only port for accessing Book aggregates.
 */
public interface BookQueryRepository {
    Optional<Book> findById(Long id);

    Optional<Book> findByDocumentId(String documentId);

    PageResult<Book> findAll(PageCriteria criteria);

    Optional<Book> findByTitle(String title);
}
