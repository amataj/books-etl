package com.example.books.usecase.ingestevent;

import com.example.books.domain.core.ingestevent.IngestEvent;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

public interface IngestEventUseCase {
    IngestEvent save(IngestEvent ingestEvent);

    IngestEvent update(IngestEvent ingestEvent);

    Optional<IngestEvent> partialUpdate(IngestEvent ingestEvent);

    PageResult<IngestEvent> findAll(PageCriteria criteria);

    Optional<IngestEvent> findOne(Long id);

    void delete(Long id);

    boolean existsById(Long id);
}
