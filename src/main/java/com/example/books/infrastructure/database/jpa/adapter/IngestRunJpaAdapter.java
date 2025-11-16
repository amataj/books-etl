package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.ingestrun.IngestRun;
import com.example.books.domain.ingestrun.IngestRunCommandRepository;
import com.example.books.domain.ingestrun.IngestRunQueryRepository;
import com.example.books.infrastructure.database.jpa.mapper.IngestRunMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestRunJpaRepository;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * JPA-backed adapter for ingest runs.
 */
@Repository
public class IngestRunJpaAdapter implements IngestRunQueryRepository, IngestRunCommandRepository {

    private final IngestRunJpaRepository ingestRunJpaRepository;
    private final IngestRunMapper ingestRunMapper;

    public IngestRunJpaAdapter(IngestRunJpaRepository ingestRunJpaRepository, IngestRunMapper ingestRunMapper) {
        this.ingestRunJpaRepository = ingestRunJpaRepository;
        this.ingestRunMapper = ingestRunMapper;
    }

    @Override
    public Optional<IngestRun> findById(Long id) {
        return ingestRunJpaRepository.findById(id).map(ingestRunMapper::toDto);
    }

    @Override
    public IngestRun save(IngestRun ingestRun) {
        var entity = ingestRunMapper.toEntity(ingestRun);
        var persisted = ingestRunJpaRepository.save(entity);
        return ingestRunMapper.toDto(persisted);
    }

    @Override
    public void deleteById(Long id) {
        ingestRunJpaRepository.deleteById(id);
    }

    @Override
    public PageResult<IngestRun> findAll(PageCriteria criteria) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        var page = ingestRunJpaRepository.findAll(pageable);
        var content = page.stream().map(ingestRunMapper::toDto).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
