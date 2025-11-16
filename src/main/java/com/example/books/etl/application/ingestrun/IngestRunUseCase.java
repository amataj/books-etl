package com.example.books.etl.application.ingestrun;

import com.example.books.etl.domain.ingestrun.IngestRun;
import java.util.List;
import java.util.Optional;

/**
 * Use case boundary for ingest run workflows.
 */
public interface IngestRunUseCase {
    IngestRun create(IngestRun ingestRun);

    IngestRun update(IngestRun ingestRun);

    Optional<IngestRun> partialUpdate(IngestRun ingestRun);

    List<IngestRun> findAll();

    Optional<IngestRun> findOne(Long id);

    void delete(Long id);

    boolean exists(Long id);
}
