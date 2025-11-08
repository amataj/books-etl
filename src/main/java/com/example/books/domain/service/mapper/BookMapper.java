package com.example.books.domain.service.mapper;

import com.example.books.domain.core.BookDTO;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookEntity} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, BookEntity> {}
