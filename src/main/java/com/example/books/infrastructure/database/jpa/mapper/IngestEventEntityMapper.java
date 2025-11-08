package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.core.ingestevent.IngestEvent;
import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IngestEventEntityMapper {
    IngestEventEntity toEntity(IngestEvent ingestEvent);

    IngestEvent toDomain(IngestEventEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(IngestEvent ingestEvent, @MappingTarget IngestEventEntity entity);
}
