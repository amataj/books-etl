package com.example.books.service.mapper;

import static com.example.books.domain.BookPageTextAsserts.*;
import static com.example.books.domain.BookPageTextTestSamples.*;

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
