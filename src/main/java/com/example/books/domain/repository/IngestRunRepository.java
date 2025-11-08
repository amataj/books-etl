package com.example.books.domain.repository;

import com.example.books.domain.core.IngestRun;

/**
 * Port for mutating ingest runs.
 */
public interface IngestRunRepository {
    IngestRun save(IngestRun ingestRun);

    void deleteById(Long id);
}
