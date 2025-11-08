package com.example.books.domain.service.mapper;

import com.example.books.domain.core.BookDTO;
import com.example.books.domain.core.BookPageTextDTO;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookPageTextEntity} and its DTO {@link BookPageTextDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookPageTextMapper extends EntityMapper<BookPageTextDTO, BookPageTextEntity> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookDocumentId")
    BookPageTextDTO toDto(BookPageTextEntity s);

    @Named("bookDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentId", source = "documentId")
    BookDTO toDtoBookDocumentId(BookEntity book);
}
