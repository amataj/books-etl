package com.example.books.domain.service.ingestevent.impl;

import com.example.books.domain.core.ingestevent.IngestEvent;
import com.example.books.domain.repository.ingestevent.IngestEventDataAccessRepository;
import com.example.books.domain.repository.ingestevent.IngestEventRepository;
import com.example.books.domain.service.ingestevent.IngestEventService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestEventServiceImpl implements IngestEventService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestEventServiceImpl.class);

    private final IngestEventRepository ingestEventRepository;
    private final IngestEventDataAccessRepository ingestEventDataAccessRepository;

    public IngestEventServiceImpl(
        IngestEventRepository ingestEventRepository,
        IngestEventDataAccessRepository ingestEventDataAccessRepository
    ) {
        this.ingestEventRepository = ingestEventRepository;
        this.ingestEventDataAccessRepository = ingestEventDataAccessRepository;
    }

    @Override
    public IngestEvent save(IngestEvent ingestEvent) {
        LOG.debug("Domain service: save IngestEvent {}", ingestEvent);
        return ingestEventRepository.save(ingestEvent);
    }

    @Override
    public IngestEvent update(IngestEvent ingestEvent) {
        LOG.debug("Domain service: update IngestEvent {}", ingestEvent);
        return ingestEventRepository.save(ingestEvent);
    }

    @Override
    public Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent) {
        LOG.debug("Domain service: partial update IngestEvent {}", ingestEvent);
        return ingestEventDataAccessRepository
            .findById(ingestEvent.id())
            .map(existing ->
                new IngestEvent(
                    existing.id(),
                    ingestEvent.runId() != null ? ingestEvent.runId() : existing.runId(),
                    ingestEvent.documentId() != null ? ingestEvent.documentId() : existing.documentId(),
                    ingestEvent.topic() != null ? ingestEvent.topic() : existing.topic(),
                    ingestEvent.payload() != null ? ingestEvent.payload() : existing.payload(),
                    ingestEvent.createdAt() != null ? ingestEvent.createdAt() : existing.createdAt(),
                    ingestEvent.ingestRunId() != null ? ingestEvent.ingestRunId() : existing.ingestRunId()
                )
            )
            .map(ingestEventRepository::save);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Domain service: delete IngestEvent {}", id);
        ingestEventRepository.deleteById(id);
    }
}
