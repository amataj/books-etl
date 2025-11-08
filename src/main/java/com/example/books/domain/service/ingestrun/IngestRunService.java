package com.example.books.domain.service.ingestrun;

import com.example.books.domain.core.ingestrun.IngestRun;
import java.util.Optional;

public interface IngestRunService {
    IngestRun save(IngestRun ingestRun);

    IngestRun update(IngestRun ingestRun);

    Optional<IngestRun> partialUpdate(IngestRun ingestRun);

    void delete(Long id);
}
