package com.example.books.domain.repository;

import com.example.books.domain.core.BookFile;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Read-only port for book files.
 */
public interface BookFileDataAccessRepository {
    Optional<BookFile> findById(Long id, boolean eagerRelationships);

    PageResult<BookFile> findAll(PageCriteria criteria, boolean eagerRelationships);
}
