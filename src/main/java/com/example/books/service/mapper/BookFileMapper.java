package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.Book;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookFile;
import com.example.books.service.dto.BookDTO;
import com.example.books.service.dto.BookFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookFile} and its DTO {@link BookFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookFileMapper extends EntityMapper<BookFileDTO, BookFile> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookDocumentId")
    BookFileDTO toDto(BookFile s);

    @Named("bookDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentId", source = "documentId")
    BookDTO toDtoBookDocumentId(Book book);
}
