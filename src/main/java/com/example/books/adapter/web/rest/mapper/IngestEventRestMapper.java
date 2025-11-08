package com.example.books.adapter.web.rest.mapper;

import com.example.books.adapter.web.rest.dto.IngestEventDTO;
import com.example.books.domain.core.ingestevent.IngestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IngestRunRestMapper.class)
public interface IngestEventRestMapper {
    @Mapping(target = "ingestRunId", source = "ingestRun.id")
    IngestEvent toDomain(IngestEventDTO dto);

    @Mapping(target = "ingestRun.id", source = "ingestRunId")
    IngestEventDTO toDto(IngestEvent domain);
}
