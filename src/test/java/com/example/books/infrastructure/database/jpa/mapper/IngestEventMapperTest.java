package com.example.books.infrastructure.database.jpa.mapper;

import static com.example.books.infrastructure.database.jpa.entity.IngestEventAsserts.*;
import static com.example.books.infrastructure.database.jpa.entity.IngestEventTestSamples.*;

import com.example.books.infrastructure.database.jpa.mapper.IngestEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngestEventMapperTest {

    private IngestEventMapper ingestEventMapper;

    @BeforeEach
    void setUp() {
        ingestEventMapper = new IngestEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIngestEventSample1();
        var actual = ingestEventMapper.toEntity(ingestEventMapper.toDto(expected));
        assertIngestEventAllPropertiesEquals(expected, actual);
    }
}
