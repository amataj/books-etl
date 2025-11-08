package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.core.ingestevent.IngestEvent;
import com.example.books.domain.repository.ingestevent.IngestEventDataAccessRepository;
import com.example.books.domain.repository.ingestevent.IngestEventRepository;
import com.example.books.infrastructure.database.jpa.mapper.IngestEventEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestEventJpaRepository;
import com.example.books.infrastructure.database.jpa.util.PageResultFactory;
import com.example.books.infrastructure.database.jpa.util.PageableFactory;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class IngestEventJpaRepositoryAdapter implements IngestEventRepository, IngestEventDataAccessRepository {

    private final IngestEventJpaRepository ingestEventJpaRepository;
    private final IngestEventEntityMapper mapper;

    public IngestEventJpaRepositoryAdapter(IngestEventJpaRepository ingestEventJpaRepository, IngestEventEntityMapper mapper) {
        this.ingestEventJpaRepository = ingestEventJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public IngestEvent save(IngestEvent ingestEvent) {
        return mapper.toDomain(ingestEventJpaRepository.save(mapper.toEntity(ingestEvent)));
    }

    @Override
    public void deleteById(Long id) {
        ingestEventJpaRepository.deleteById(id);
    }

    @Override
    public Optional<IngestEvent> findById(Long id) {
        return ingestEventJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResult<IngestEvent> findAll(PageCriteria criteria) {
        return PageResultFactory.from(ingestEventJpaRepository.findAll(PageableFactory.from(criteria)).map(mapper::toDomain));
    }
}
