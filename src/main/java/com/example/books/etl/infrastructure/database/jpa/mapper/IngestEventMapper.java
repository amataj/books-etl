package com.example.books.etl.infrastructure.database.jpa.mapper;

import com.example.books.etl.domain.ingestrun.IngestEvent;
import com.example.books.etl.domain.ingestrun.IngestRun;
import com.example.books.etl.infrastructure.database.jpa.entity.IngestEventEntity;
import com.example.books.etl.infrastructure.database.jpa.entity.IngestRunEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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
