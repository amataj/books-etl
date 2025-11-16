package com.example.books.etl.domain.ingestevent;

import com.example.books.etl.domain.ingestrun.IngestEvent;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Domain service for managing {@link IngestEvent} aggregates.
 */
public class IngestEventService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestEventService.class);

    private final IngestEventCommandRepository ingestEventCommandRepository;
    private final IngestEventQueryRepository ingestEventQueryRepository;

    public IngestEventService(
        IngestEventCommandRepository ingestEventCommandRepository,
        IngestEventQueryRepository ingestEventQueryRepository
    ) {
        this.ingestEventCommandRepository = ingestEventCommandRepository;
        this.ingestEventQueryRepository = ingestEventQueryRepository;
    }

    /**
     * Save an ingestEvent.
     *
     * @param ingestEvent the aggregate to save.
     * @return the persisted aggregate.
     */
    public IngestEvent save(IngestEvent ingestEvent) {
        LOG.debug("Request to save IngestEvent : {}", ingestEvent);
        return ingestEventCommandRepository.save(ingestEvent);
    }

    /**
     * Update an ingestEvent.
     *
     * @param ingestEvent the aggregate to update.
     * @return the persisted aggregate.
     */
    public IngestEvent update(IngestEvent ingestEvent) {
        LOG.debug("Request to update IngestEvent : {}", ingestEvent);
        return ingestEventCommandRepository.save(ingestEvent);
    }

    /**
     * Partially update an ingestEvent.
     *
     * @param ingestEvent the aggregate to update partially.
     * @return the persisted aggregate.
     */
    public Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent) {
        LOG.debug("Request to partially update IngestEvent : {}", ingestEvent);
        if (ingestEvent.getId() == null) {
            return Optional.empty();
        }

        return ingestEventQueryRepository
            .findById(ingestEvent.getId())
            .map(existing -> {
                partialUpdate(existing, ingestEvent);
                return ingestEventCommandRepository.save(existing);
            });
    }

    /**
     * Delete the ingestEvent by id.
     *
     * @param id the id of the aggregate.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IngestEvent : {}", id);
        ingestEventCommandRepository.deleteById(id);
    }

    private void partialUpdate(IngestEvent existing, IngestEvent incoming) {
        if (incoming == null) {
            return;
        }

        if (incoming.getId() != null) {
            existing.setId(incoming.getId());
        }
        if (incoming.getRunId() != null) {
            existing.setRunId(incoming.getRunId());
        }
        if (incoming.getDocumentId() != null) {
            existing.setDocumentId(incoming.getDocumentId());
        }
        if (incoming.getTopic() != null) {
            existing.setTopic(incoming.getTopic());
        }
        if (incoming.getPayload() != null) {
            existing.setPayload(incoming.getPayload());
        }
        if (incoming.getCreatedAt() != null) {
            existing.setCreatedAt(incoming.getCreatedAt());
        }
        if (incoming.getIngestRun() != null) {
            existing.setIngestRun(incoming.getIngestRun());
        }
    }
}
