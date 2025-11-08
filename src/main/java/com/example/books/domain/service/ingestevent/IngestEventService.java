package com.example.books.domain.service.ingestevent;

import com.example.books.domain.core.ingestevent.IngestEvent;
import java.util.Optional;

public interface IngestEventService {
    IngestEvent save(IngestEvent ingestEvent);

    IngestEvent update(IngestEvent ingestEvent);

    Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent);

    void delete(Long id);
}
