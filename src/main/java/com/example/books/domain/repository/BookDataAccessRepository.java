package com.example.books.domain.repository;

import com.example.books.domain.core.Book;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Read-only port for accessing Book aggregates.
 */
public interface BookDataAccessRepository {
    Optional<Book> findById(Long id);

    Optional<Book> findByDocumentId(String documentId);

    PageResult<Book> findAll(PageCriteria criteria);
}
