package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.Book;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookPageText;
import com.example.books.service.dto.BookDTO;
import com.example.books.service.dto.BookPageTextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookPageText} and its DTO {@link BookPageTextDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookPageTextMapper extends EntityMapper<BookPageTextDTO, BookPageText> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookDocumentId")
    BookPageTextDTO toDto(BookPageText s);

    @Named("bookDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentId", source = "documentId")
    BookDTO toDtoBookDocumentId(Book book);
}
