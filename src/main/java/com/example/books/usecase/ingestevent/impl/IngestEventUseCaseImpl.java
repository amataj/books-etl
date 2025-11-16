package com.example.books.usecase.ingestevent.impl;

import com.example.books.domain.ingestevent.IngestEventQueryRepository;
import com.example.books.domain.ingestevent.IngestEventService;
import com.example.books.domain.ingestrun.IngestEvent;
import com.example.books.usecase.ingestevent.IngestEventUseCase;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngestEventUseCaseImpl implements IngestEventUseCase {

    private final IngestEventService ingestEventService;
    private final IngestEventQueryRepository ingestEventQueryRepository;

    public IngestEventUseCaseImpl(IngestEventService ingestEventService, IngestEventQueryRepository ingestEventQueryRepository) {
        this.ingestEventService = ingestEventService;
        this.ingestEventQueryRepository = ingestEventQueryRepository;
    }

    @Override
    public IngestEvent create(IngestEvent ingestEvent) {
        return ingestEventService.save(ingestEvent);
    }

    @Override
    public IngestEvent update(IngestEvent ingestEvent) {
        return ingestEventService.update(ingestEvent);
    }

    @Override
    public Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent) {
        return ingestEventService.partialUpdate(ingestEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngestEvent> findAll() {
        return ingestEventService.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngestEvent> findOne(Long id) {
        return ingestEventService.findOne(id);
    }

    @Override
    public void delete(Long id) {
        ingestEventService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return ingestEventService.findOne(id).isPresent();
    }
}
