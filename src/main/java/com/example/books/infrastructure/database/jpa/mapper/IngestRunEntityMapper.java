package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.core.ingestrun.IngestRun;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IngestRunEntityMapper {
    IngestRunEntity toEntity(IngestRun ingestRun);

    IngestRun toDomain(IngestRunEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(IngestRun ingestRun, @MappingTarget IngestRunEntity entity);
}
