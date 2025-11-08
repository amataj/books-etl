package com.example.books.domain.service.ingestrun.impl;

import com.example.books.domain.core.ingestrun.IngestRun;
import com.example.books.domain.repository.ingestrun.IngestRunDataAccessRepository;
import com.example.books.domain.repository.ingestrun.IngestRunRepository;
import com.example.books.domain.service.ingestrun.IngestRunService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestRunServiceImpl implements IngestRunService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunServiceImpl.class);

    private final IngestRunRepository ingestRunRepository;
    private final IngestRunDataAccessRepository ingestRunDataAccessRepository;

    public IngestRunServiceImpl(IngestRunRepository ingestRunRepository, IngestRunDataAccessRepository ingestRunDataAccessRepository) {
        this.ingestRunRepository = ingestRunRepository;
        this.ingestRunDataAccessRepository = ingestRunDataAccessRepository;
    }

    @Override
    public IngestRun save(IngestRun ingestRun) {
        LOG.debug("Domain service: save IngestRun {}", ingestRun);
        return ingestRunRepository.save(ingestRun);
    }

    @Override
    public IngestRun update(IngestRun ingestRun) {
        LOG.debug("Domain service: update IngestRun {}", ingestRun);
        return ingestRunRepository.save(ingestRun);
    }

    @Override
    public Optional<IngestRun> partialUpdate(IngestRun ingestRun) {
        LOG.debug("Domain service: partial update IngestRun {}", ingestRun);
        return ingestRunDataAccessRepository
            .findById(ingestRun.id())
            .map(existing ->
                new IngestRun(
                    existing.id(),
                    ingestRun.startedAt() != null ? ingestRun.startedAt() : existing.startedAt(),
                    ingestRun.finishedAt() != null ? ingestRun.finishedAt() : existing.finishedAt(),
                    ingestRun.status() != null ? ingestRun.status() : existing.status(),
                    ingestRun.filesSeen() != null ? ingestRun.filesSeen() : existing.filesSeen(),
                    ingestRun.filesParsed() != null ? ingestRun.filesParsed() : existing.filesParsed(),
                    ingestRun.filesFailed() != null ? ingestRun.filesFailed() : existing.filesFailed()
                )
            )
            .map(ingestRunRepository::save);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Domain service: delete IngestRun {}", id);
        ingestRunRepository.deleteById(id);
    }
}
