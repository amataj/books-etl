package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.ingestrun.IngestRun;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link IngestRunEntity} and its DTO {@link IngestRun}.
 */
@Mapper(componentModel = "spring")
public interface IngestRunMapper extends EntityMapper<IngestRun, IngestRunEntity> {}
