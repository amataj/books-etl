package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRun;
import com.example.books.service.dto.IngestRunDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IngestRun} and its DTO {@link IngestRunDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngestRunMapper extends EntityMapper<IngestRunDTO, IngestRun> {}
