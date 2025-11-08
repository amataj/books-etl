package com.example.books.infrastructure.database.jpa.mapper;

import com.example.books.domain.core.bookfile.BookFile;
import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = BookEntityMapper.class)
public interface BookFileEntityMapper {
    BookFile toDomain(BookFileEntity entity);

    @Mapping(target = "book", ignore = true)
    BookFileEntity toEntity(BookFile bookFile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "book", ignore = true)
    void updateEntity(BookFile bookFile, @MappingTarget BookFileEntity entity);
}
