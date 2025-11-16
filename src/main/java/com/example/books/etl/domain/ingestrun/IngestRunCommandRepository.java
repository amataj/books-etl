package com.example.books.etl.domain.ingestrun;

/**
 * Command-side port for mutating ingest runs.
 */
public interface IngestRunCommandRepository {
    IngestRun save(IngestRun ingestRun);

    void deleteById(Long id);
}
