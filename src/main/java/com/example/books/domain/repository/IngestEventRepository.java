package com.example.books.domain.repository;

import com.example.books.domain.core.IngestEvent;

/**
 * Port for mutating ingest events.
 */
public interface IngestEventRepository {
    IngestEvent save(IngestEvent ingestEvent);

    void deleteById(Long id);
}
