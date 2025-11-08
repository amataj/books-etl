package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestEvent;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRun;
import com.example.books.service.dto.IngestEventDTO;
import com.example.books.service.dto.IngestRunDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IngestEvent} and its DTO {@link IngestEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngestEventMapper extends EntityMapper<IngestEventDTO, IngestEvent> {
    @Mapping(target = "ingestRun", source = "ingestRun", qualifiedByName = "ingestRunId")
    IngestEventDTO toDto(IngestEvent s);

    @Named("ingestRunId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IngestRunDTO toDtoIngestRunId(IngestRun ingestRun);
}
