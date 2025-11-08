package com.example.books.infrastructure.database.jpa.mapper;

import static com.example.books.infrastructure.database.jpa.entity.BookAsserts.*;
import static com.example.books.infrastructure.database.jpa.entity.BookTestSamples.*;

import com.example.books.infrastructure.database.jpa.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookSample1();
        var actual = bookMapper.toEntity(bookMapper.toDto(expected));
        assertBookAllPropertiesEquals(expected, actual);
    }
}
