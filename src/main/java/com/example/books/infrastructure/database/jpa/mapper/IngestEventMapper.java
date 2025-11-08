package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.core.IngestEvent;
import com.example.books.domain.core.IngestRun;
import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IngestEventEntity} and its DTO {@link IngestEvent}.
 */
@Mapper(componentModel = "spring")
public interface IngestEventMapper extends EntityMapper<IngestEvent, IngestEventEntity> {
    @Mapping(target = "ingestRun", source = "ingestRun", qualifiedByName = "ingestRunId")
    IngestEvent toDto(IngestEventEntity s);

    @Named("ingestRunId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IngestRun toDtoIngestRunId(IngestRunEntity ingestRun);
}
