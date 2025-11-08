package com.example.books.service.mapper;

import static com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRunAsserts.*;
import static com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRunTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngestRunMapperTest {

    private IngestRunMapper ingestRunMapper;

    @BeforeEach
    void setUp() {
        ingestRunMapper = new IngestRunMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIngestRunSample1();
        var actual = ingestRunMapper.toEntity(ingestRunMapper.toDto(expected));
        assertIngestRunAllPropertiesEquals(expected, actual);
    }
}
