package com.example.books.etl.infrastructure.database.jpa.mapper;

import com.example.books.etl.domain.book.Book;
import com.example.books.etl.infrastructure.database.jpa.entity.BookEntity;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BookEntity} and its DTO {@link Book}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<Book, BookEntity> {}
