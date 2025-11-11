package com.example.books.usecase.ingestevent;

import com.example.books.domain.core.IngestEvent;
import java.util.List;
import java.util.Optional;

/**
 * Use case boundary for ingest event flows.
 */
public interface IngestEventUseCase {
    IngestEvent create(IngestEvent ingestEvent);

    IngestEvent update(IngestEvent ingestEvent);

    Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent);

    List<IngestEvent> findAll();

    Optional<IngestEvent> findOne(Long id);

    void delete(Long id);

    boolean exists(Long id);
}
