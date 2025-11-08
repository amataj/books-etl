package com.example.books.usecase.ingestrun.impl;

import com.example.books.domain.core.ingestrun.IngestRun;
import com.example.books.domain.repository.ingestrun.IngestRunDataAccessRepository;
import com.example.books.domain.service.ingestrun.IngestRunService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.ingestrun.IngestRunUseCase;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngestRunUseCaseImpl implements IngestRunUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunUseCaseImpl.class);

    private final IngestRunService ingestRunService;
    private final IngestRunDataAccessRepository ingestRunDataAccessRepository;

    public IngestRunUseCaseImpl(IngestRunService ingestRunService, IngestRunDataAccessRepository ingestRunDataAccessRepository) {
        this.ingestRunService = ingestRunService;
        this.ingestRunDataAccessRepository = ingestRunDataAccessRepository;
    }

    @Override
    public IngestRun save(IngestRun ingestRun) {
        LOG.debug("Use case: save IngestRun {}", ingestRun);
        return ingestRunService.save(ingestRun);
    }

    @Override
    public IngestRun update(IngestRun ingestRun) {
        LOG.debug("Use case: update IngestRun {}", ingestRun);
        return ingestRunService.update(ingestRun);
    }

    @Override
    public Optional<IngestRun> partialUpdate(IngestRun ingestRun) {
        LOG.debug("Use case: partial update IngestRun {}", ingestRun);
        return ingestRunService.partialUpdate(ingestRun);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<IngestRun> findAll(PageCriteria criteria) {
        return ingestRunDataAccessRepository.findAll(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngestRun> findOne(Long id) {
        return ingestRunDataAccessRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Use case: delete IngestRun {}", id);
        ingestRunService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return ingestRunDataAccessRepository.findById(id).isPresent();
    }
}
