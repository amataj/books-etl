package com.example.books.domain.ingestrun;

/**
 * Command-side port for mutating ingest runs.
 */
public interface IngestRunCommandRepository {
    IngestRun save(IngestRun ingestRun);

    void deleteById(Long id);
}
