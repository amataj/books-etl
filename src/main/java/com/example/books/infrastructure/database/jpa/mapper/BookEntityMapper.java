package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.core.book.Book;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import org.mapstruct.*;

/**
 * Maps between domain {@link Book} and {@link BookEntity}.
 */
@Mapper(componentModel = "spring")
public interface BookEntityMapper {
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "pageTexts", ignore = true)
    BookEntity toEntity(Book book);

    Book toDomain(BookEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "pageTexts", ignore = true)
    void updateEntity(Book book, @MappingTarget BookEntity entity);
}
