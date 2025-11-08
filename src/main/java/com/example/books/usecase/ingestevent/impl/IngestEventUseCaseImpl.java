package com.example.books.usecase.ingestevent.impl;

import com.example.books.domain.core.ingestevent.IngestEvent;
import com.example.books.domain.repository.ingestevent.IngestEventDataAccessRepository;
import com.example.books.domain.service.ingestevent.IngestEventService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.ingestevent.IngestEventUseCase;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngestEventUseCaseImpl implements IngestEventUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(IngestEventUseCaseImpl.class);

    private final IngestEventService ingestEventService;
    private final IngestEventDataAccessRepository ingestEventDataAccessRepository;

    public IngestEventUseCaseImpl(IngestEventService ingestEventService, IngestEventDataAccessRepository ingestEventDataAccessRepository) {
        this.ingestEventService = ingestEventService;
        this.ingestEventDataAccessRepository = ingestEventDataAccessRepository;
    }

    @Override
    public IngestEvent save(IngestEvent ingestEvent) {
        LOG.debug("Use case: save IngestEvent {}", ingestEvent);
        return ingestEventService.save(ingestEvent);
    }

    @Override
    public IngestEvent update(IngestEvent ingestEvent) {
        LOG.debug("Use case: update IngestEvent {}", ingestEvent);
        return ingestEventService.update(ingestEvent);
    }

    @Override
    public Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent) {
        LOG.debug("Use case: partial update IngestEvent {}", ingestEvent);
        return ingestEventService.partialUpdate(ingestEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<IngestEvent> findAll(PageCriteria criteria) {
        return ingestEventDataAccessRepository.findAll(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngestEvent> findOne(Long id) {
        return ingestEventDataAccessRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Use case: delete IngestEvent {}", id);
        ingestEventService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return ingestEventDataAccessRepository.findById(id).isPresent();
    }
}
