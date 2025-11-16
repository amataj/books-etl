package com.example.books.etl.application.ingestrun.impl;

import com.example.books.etl.application.ingestrun.IngestRunUseCase;
import com.example.books.etl.domain.ingestrun.IngestRun;
import com.example.books.etl.domain.ingestrun.IngestRunQueryRepository;
import com.example.books.etl.domain.ingestrun.IngestRunService;
import com.example.books.etl.shared.pagination.PageCriteria;
import com.example.books.etl.shared.pagination.PageResult;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngestRunUseCaseImpl implements IngestRunUseCase {

    private final IngestRunService ingestRunService;
    private final IngestRunQueryRepository ingestRunQueryRepository;

    public IngestRunUseCaseImpl(IngestRunService ingestRunService, IngestRunQueryRepository ingestRunQueryRepository) {
        this.ingestRunService = ingestRunService;
        this.ingestRunQueryRepository = ingestRunQueryRepository;
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
        PageResult<IngestRun> all = ingestRunQueryRepository.findAll(new PageCriteria(0, Integer.MAX_VALUE));
        return all.content();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngestRun> findOne(Long id) {
        return ingestRunQueryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        ingestRunService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return ingestRunQueryRepository.findById(id).isPresent();
    }
}
