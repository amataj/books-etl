package com.example.books.domain.repository.ingestrun;

import com.example.books.domain.core.ingestrun.IngestRun;

/**
 * Port for mutating ingest runs.
 */
public interface IngestRunRepository {
    IngestRun save(IngestRun ingestRun);

    void deleteById(Long id);
}
