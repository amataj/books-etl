package com.example.books.adapter.web.rest.mapper;

import com.example.books.adapter.web.rest.dto.BookPageTextDTO;
import com.example.books.domain.core.bookpagetext.BookPageText;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BookRestMapper.class)
public interface BookPageTextRestMapper {
    BookPageText toDomain(BookPageTextDTO dto);

    BookPageTextDTO toDto(BookPageText domain);
}
