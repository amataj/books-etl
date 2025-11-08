package com.example.books.adapter.web.rest.mapper;

import com.example.books.adapter.web.rest.dto.BookFileDTO;
import com.example.books.domain.core.bookfile.BookFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BookRestMapper.class)
public interface BookFileRestMapper {
    BookFile toDomain(BookFileDTO dto);

    BookFileDTO toDto(BookFile domain);
}
