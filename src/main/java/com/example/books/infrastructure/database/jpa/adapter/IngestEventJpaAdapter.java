package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.ingestevent.IngestEventDataAccessRepository;
import com.example.books.domain.ingestevent.IngestEventRepository;
import com.example.books.domain.ingestrun.IngestEvent;
import com.example.books.infrastructure.database.jpa.mapper.IngestEventMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestEventJpaRepository;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * JPA-backed read adapter for ingest events.
 */
@Repository
public class IngestEventJpaAdapter implements IngestEventDataAccessRepository, IngestEventRepository {

    private final IngestEventJpaRepository ingestEventJpaRepository;
    private final IngestEventMapper ingestEventMapper;

    public IngestEventJpaAdapter(IngestEventJpaRepository ingestEventJpaRepository, IngestEventMapper ingestEventMapper) {
        this.ingestEventJpaRepository = ingestEventJpaRepository;
        this.ingestEventMapper = ingestEventMapper;
    }

    @Override
    public Optional<IngestEvent> findById(Long id) {
        return ingestEventJpaRepository.findById(id).map(ingestEventMapper::toDto);
    }

    @Override
    public IngestEvent save(IngestEvent ingestEvent) {
        var entity = ingestEventMapper.toEntity(ingestEvent);
        var persisted = ingestEventJpaRepository.save(entity);
        return ingestEventMapper.toDto(persisted);
    }

    @Override
    public void deleteById(Long id) {
        ingestEventJpaRepository.deleteById(id);
    }

    @Override
    public PageResult<IngestEvent> findAll(PageCriteria criteria) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        var page = ingestEventJpaRepository.findAll(pageable);
        var content = page.stream().map(ingestEventMapper::toDto).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
