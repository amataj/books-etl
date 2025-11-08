package com.example.books.domain.repository;

import com.example.books.domain.core.IngestRun;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Read-only port for ingest runs.
 */
public interface IngestRunDataAccessRepository {
    Optional<IngestRun> findById(Long id);

    PageResult<IngestRun> findAll(PageCriteria criteria);
}
