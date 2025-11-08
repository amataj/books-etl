package com.example.books.domain.service.mapper;

import com.example.books.domain.core.BookDTO;
import com.example.books.domain.core.BookFileDTO;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookFileEntity} and its DTO {@link BookFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookFileMapper extends EntityMapper<BookFileDTO, BookFileEntity> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookDocumentId")
    BookFileDTO toDto(BookFileEntity s);

    @Named("bookDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentId", source = "documentId")
    BookDTO toDtoBookDocumentId(BookEntity book);
}
