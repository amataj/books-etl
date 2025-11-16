package com.example.books.etl.domain.ingestrun;

import com.example.books.etl.shared.pagination.PageCriteria;
import com.example.books.etl.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Query-side port for ingest runs.
 */
public interface IngestRunQueryRepository {
    Optional<IngestRun> findById(Long id);

    PageResult<IngestRun> findAll(PageCriteria criteria);
}
