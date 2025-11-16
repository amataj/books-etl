package com.example.books.etl.infrastructure.database.jpa.mapper;

import com.example.books.etl.domain.ingestrun.IngestRun;
import com.example.books.etl.infrastructure.database.jpa.entity.IngestRunEntity;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link IngestRunEntity} and its DTO {@link IngestRun}.
 */
@Mapper(componentModel = "spring")
public interface IngestRunMapper extends EntityMapper<IngestRun, IngestRunEntity> {}
