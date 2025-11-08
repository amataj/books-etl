package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestEventEntity;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRunEntity;
import com.example.books.service.dto.IngestEventDTO;
import com.example.books.service.dto.IngestRunDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IngestEventEntity} and its DTO {@link IngestEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngestEventMapper extends EntityMapper<IngestEventDTO, IngestEventEntity> {
    @Mapping(target = "ingestRun", source = "ingestRun", qualifiedByName = "ingestRunId")
    IngestEventDTO toDto(IngestEventEntity s);

    @Named("ingestRunId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IngestRunDTO toDtoIngestRunId(IngestRunEntity ingestRun);
}
