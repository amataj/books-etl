package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.core.ingestrun.IngestRun;
import com.example.books.domain.repository.ingestrun.IngestRunDataAccessRepository;
import com.example.books.domain.repository.ingestrun.IngestRunRepository;
import com.example.books.infrastructure.database.jpa.mapper.IngestRunEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestRunJpaRepository;
import com.example.books.infrastructure.database.jpa.util.PageResultFactory;
import com.example.books.infrastructure.database.jpa.util.PageableFactory;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class IngestRunJpaRepositoryAdapter implements IngestRunRepository, IngestRunDataAccessRepository {

    private final IngestRunJpaRepository ingestRunJpaRepository;
    private final IngestRunEntityMapper mapper;

    public IngestRunJpaRepositoryAdapter(IngestRunJpaRepository ingestRunJpaRepository, IngestRunEntityMapper mapper) {
        this.ingestRunJpaRepository = ingestRunJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public IngestRun save(IngestRun ingestRun) {
        return mapper.toDomain(ingestRunJpaRepository.save(mapper.toEntity(ingestRun)));
    }

    @Override
    public void deleteById(Long id) {
        ingestRunJpaRepository.deleteById(id);
    }

    @Override
    public Optional<IngestRun> findById(Long id) {
        return ingestRunJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResult<IngestRun> findAll(PageCriteria criteria) {
        return PageResultFactory.from(ingestRunJpaRepository.findAll(PageableFactory.from(criteria)).map(mapper::toDomain));
    }
}
