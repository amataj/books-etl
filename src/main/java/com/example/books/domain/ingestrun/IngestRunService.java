package com.example.books.domain.ingestrun;

import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Domain service for managing {@link IngestRun} aggregates.
 */
public class IngestRunService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunService.class);

    private final IngestRunCommandRepository ingestRunCommandRepository;
    private final IngestRunQueryRepository ingestRunQueryRepository;

    public IngestRunService(IngestRunCommandRepository ingestRunCommandRepository, IngestRunQueryRepository ingestRunQueryRepository) {
        this.ingestRunCommandRepository = ingestRunCommandRepository;
        this.ingestRunQueryRepository = ingestRunQueryRepository;
    }

    /**
     * Save an ingestRun.
     *
     * @param ingestRun the aggregate to save.
     * @return the persisted aggregate.
     */
    public IngestRun save(IngestRun ingestRun) {
        LOG.debug("Request to save IngestRun : {}", ingestRun);
        return ingestRunCommandRepository.save(ingestRun);
    }

    /**
     * Update an ingestRun.
     *
     * @param ingestRun the aggregate to update.
     * @return the persisted aggregate.
     */
    public IngestRun update(IngestRun ingestRun) {
        LOG.debug("Request to update IngestRun : {}", ingestRun);
        return ingestRunCommandRepository.save(ingestRun);
    }

    /**
     * Partially update an ingestRun.
     *
     * @param ingestRun the aggregate to update partially.
     * @return the persisted aggregate.
     */
    public Optional<IngestRun> partialUpdate(IngestRun ingestRun) {
        LOG.debug("Request to partially update IngestRun : {}", ingestRun);
        if (ingestRun.getId() == null) {
            return Optional.empty();
        }

        return ingestRunQueryRepository
            .findById(ingestRun.getId())
            .map(existing -> {
                partialUpdate(existing, ingestRun);
                return ingestRunCommandRepository.save(existing);
            });
    }

    /**
     * Delete the ingestRun by id.
     *
     * @param id the id of the aggregate.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IngestRun : {}", id);
        ingestRunCommandRepository.deleteById(id);
    }

    private void partialUpdate(IngestRun existing, IngestRun incoming) {
        if (incoming == null) {
            return;
        }

        if (incoming.getId() != null) {
            existing.setId(incoming.getId());
        }
        if (incoming.getStartedAt() != null) {
            existing.setStartedAt(incoming.getStartedAt());
        }
        if (incoming.getFinishedAt() != null) {
            existing.setFinishedAt(incoming.getFinishedAt());
        }
        if (incoming.getStatus() != null) {
            existing.setStatus(incoming.getStatus());
        }
        if (incoming.getFilesSeen() != null) {
            existing.setFilesSeen(incoming.getFilesSeen());
        }
        if (incoming.getFilesParsed() != null) {
            existing.setFilesParsed(incoming.getFilesParsed());
        }
        if (incoming.getFilesFailed() != null) {
            existing.setFilesFailed(incoming.getFilesFailed());
        }
    }
}
