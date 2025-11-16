package com.example.books.etl.domain.bookfile;

import com.example.books.etl.shared.pagination.PageCriteria;
import com.example.books.etl.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Query-side port for book files.
 */
public interface BookFileQueryRepository {
    Optional<BookFile> findById(Long id);

    Optional<BookFile> findById(Long id, boolean eagerRelationships);

    PageResult<BookFile> findAll(PageCriteria criteria, boolean eagerRelationships);
}
