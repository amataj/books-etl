package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.book.Book;
import com.example.books.domain.bookpage.BookPageText;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookPageTextEntity} and its DTO {@link BookPageText}.
 */
@Mapper(componentModel = "spring")
public interface BookPageTextMapper extends EntityMapper<BookPageText, BookPageTextEntity> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookDocumentId")
    BookPageText toDto(BookPageTextEntity s);

    @Named("bookDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentId", source = "documentId")
    Book toDtoBookDocumentId(BookEntity book);
}
