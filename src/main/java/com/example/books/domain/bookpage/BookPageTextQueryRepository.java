package com.example.books.domain.bookpage;

import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Query-side port for BookPageText aggregates.
 */
public interface BookPageTextQueryRepository {
    Optional<BookPageText> findById(Long id);

    Optional<BookPageText> findById(Long id, boolean eagerRelationships);

    PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships);
}
