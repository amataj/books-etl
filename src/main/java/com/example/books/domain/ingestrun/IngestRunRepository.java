package com.example.books.domain.ingestrun;

/**
 * Port for mutating ingest runs.
 */
public interface IngestRunRepository {
    IngestRun save(IngestRun ingestRun);

    void deleteById(Long id);
}
