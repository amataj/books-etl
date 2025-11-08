package com.example.books.domain.repository.bookpagetext;

import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Read-only port for BookPageText aggregates.
 */
public interface BookPageTextDataAccessRepository {
    Optional<BookPageText> findById(Long id, boolean eagerRelationships);

    PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships);
}
