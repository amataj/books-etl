package com.example.books.domain.repository;

import com.example.books.domain.core.IngestEvent;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Read-only port for ingest events.
 */
public interface IngestEventDataAccessRepository {
    Optional<IngestEvent> findById(Long id);

    PageResult<IngestEvent> findAll(PageCriteria criteria);
}
