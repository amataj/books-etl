package com.example.books.domain.service.mapper;

import static com.example.books.infrastructure.database.jpa.entity.BookFileAsserts.*;
import static com.example.books.infrastructure.database.jpa.entity.BookFileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookFileMapperTest {

    private BookFileMapper bookFileMapper;

    @BeforeEach
    void setUp() {
        bookFileMapper = new BookFileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookFileSample1();
        var actual = bookFileMapper.toEntity(bookFileMapper.toDto(expected));
        assertBookFileAllPropertiesEquals(expected, actual);
    }
}
