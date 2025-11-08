package com.example.books.infrastructure.database.jpa.mapper;

import static com.example.books.infrastructure.database.jpa.entity.BookPageTextAsserts.*;
import static com.example.books.infrastructure.database.jpa.entity.BookPageTextTestSamples.*;

import com.example.books.infrastructure.database.jpa.mapper.BookPageTextMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookPageTextMapperTest {

    private BookPageTextMapper bookPageTextMapper;

    @BeforeEach
    void setUp() {
        bookPageTextMapper = new BookPageTextMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookPageTextSample1();
        var actual = bookPageTextMapper.toEntity(bookPageTextMapper.toDto(expected));
        assertBookPageTextAllPropertiesEquals(expected, actual);
    }
}
