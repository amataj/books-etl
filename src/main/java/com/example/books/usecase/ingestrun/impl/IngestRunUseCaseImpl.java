package com.example.books.usecase.ingestrun.impl;

import com.example.books.domain.core.IngestRun;
import com.example.books.domain.service.IngestRunService;
import com.example.books.usecase.ingestrun.IngestRunUseCase;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngestRunUseCaseImpl implements IngestRunUseCase {

    private final IngestRunService ingestRunService;

    public IngestRunUseCaseImpl(IngestRunService ingestRunService) {
        this.ingestRunService = ingestRunService;
    }

    @Override
    public IngestRun create(IngestRun ingestRun) {
        return ingestRunService.save(ingestRun);
    }

    @Override
    public IngestRun update(IngestRun ingestRun) {
        return ingestRunService.update(ingestRun);
    }

    @Override
    public Optional<IngestRun> partialUpdate(IngestRun ingestRun) {
        return ingestRunService.partialUpdate(ingestRun);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngestRun> findAll() {
        return ingestRunService.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngestRun> findOne(Long id) {
        return ingestRunService.findOne(id);
    }

    @Override
    public void delete(Long id) {
        ingestRunService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return ingestRunService.exists(id);
    }
}
