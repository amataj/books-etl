package com.example.books.domain.service.mapper;

import com.example.books.domain.core.IngestRunDTO;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IngestRunEntity} and its DTO {@link IngestRunDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngestRunMapper extends EntityMapper<IngestRunDTO, IngestRunEntity> {}
