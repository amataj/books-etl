package com.example.books.etl.domain.ingestevent;

import com.example.books.etl.domain.ingestrun.IngestEvent;
import com.example.books.etl.shared.pagination.PageCriteria;
import com.example.books.etl.shared.pagination.PageResult;
import java.util.Optional;

/**
 * Query-side port for ingest events.
 */
public interface IngestEventQueryRepository {
    Optional<IngestEvent> findById(Long id);

    PageResult<IngestEvent> findAll(PageCriteria criteria);
}
