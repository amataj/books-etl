package com.example.books.domain.ingestevent;

import com.example.books.domain.ingestrun.IngestEvent;

/**
 * Command-side port for mutating ingest events.
 */
public interface IngestEventCommandRepository {
    IngestEvent save(IngestEvent ingestEvent);

    void deleteById(Long id);
}
