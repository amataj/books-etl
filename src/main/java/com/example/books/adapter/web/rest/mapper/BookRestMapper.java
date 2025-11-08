package com.example.books.adapter.web.rest.mapper;

import com.example.books.adapter.web.rest.dto.BookDTO;
import com.example.books.domain.core.book.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookRestMapper {
    Book toDomain(BookDTO dto);

    BookDTO toDto(Book book);
}
