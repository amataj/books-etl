package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookFileEntity;
import com.example.books.service.dto.BookDTO;
import com.example.books.service.dto.BookFileDTO;
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
