package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.book.Book;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BookEntity} and its DTO {@link Book}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<Book, BookEntity> {}
