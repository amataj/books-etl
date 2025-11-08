package com.example.books.service.mapper;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.service.dto.BookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookEntity} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, BookEntity> {}
