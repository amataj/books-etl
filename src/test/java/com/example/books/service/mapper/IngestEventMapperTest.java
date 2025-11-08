package com.example.books.service.mapper;

import static com.example.books.domain.IngestEventAsserts.*;
import static com.example.books.domain.IngestEventTestSamples.*;

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
