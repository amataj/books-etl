package com.example.books.domain.ingestevent;

import com.example.books.domain.ingestrun.IngestEvent;

/**
 * Port for mutating ingest events.
 */
public interface IngestEventRepository {
    IngestEvent save(IngestEvent ingestEvent);

    void deleteById(Long id);
}
