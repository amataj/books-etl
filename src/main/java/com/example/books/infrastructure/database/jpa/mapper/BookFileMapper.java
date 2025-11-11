package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.book.Book;
import com.example.books.domain.bookfile.BookFile;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookFileEntity} and its DTO {@link BookFile}.
 */
@Mapper(componentModel = "spring")
public interface BookFileMapper extends EntityMapper<BookFile, BookFileEntity> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookDocumentId")
    BookFile toDto(BookFileEntity s);

    @Named("bookDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentId", source = "documentId")
    Book toDtoBookDocumentId(BookEntity book);
}
