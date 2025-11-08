package com.example.books.domain.repository.ingestevent;

import com.example.books.domain.core.ingestevent.IngestEvent;

/**
 * Port for mutating ingest events.
 */
public interface IngestEventRepository {
    IngestEvent save(IngestEvent ingestEvent);

    void deleteById(Long id);
}
