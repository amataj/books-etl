package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = BookEntityMapper.class)
public interface BookPageTextEntityMapper {
    BookPageText toDomain(BookPageTextEntity entity);

    @Mapping(target = "book", ignore = true)
    BookPageTextEntity toEntity(BookPageText domain);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "book", ignore = true)
    void updateEntity(BookPageText domain, @MappingTarget BookPageTextEntity entity);
}
