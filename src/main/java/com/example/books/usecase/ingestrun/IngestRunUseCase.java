package com.example.books.usecase.ingestrun;

import com.example.books.domain.core.ingestrun.IngestRun;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

public interface IngestRunUseCase {
    IngestRun save(IngestRun ingestRun);

    IngestRun update(IngestRun ingestRun);

    Optional<IngestRun> partialUpdate(IngestRun ingestRun);

    PageResult<IngestRun> findAll(PageCriteria criteria);

    Optional<IngestRun> findOne(Long id);

    void delete(Long id);

    boolean existsById(Long id);
}
