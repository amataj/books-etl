package com.example.books.domain.repository.ingestrun;

import com.example.books.domain.core.ingestrun.IngestRun;
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
