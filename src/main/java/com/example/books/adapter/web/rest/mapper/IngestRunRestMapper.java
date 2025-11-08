package com.example.books.adapter.web.rest.mapper;

import com.example.books.adapter.web.rest.dto.IngestRunDTO;
import com.example.books.domain.core.ingestrun.IngestRun;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngestRunRestMapper {
    IngestRun toDomain(IngestRunDTO dto);

    IngestRunDTO toDto(IngestRun domain);
}
