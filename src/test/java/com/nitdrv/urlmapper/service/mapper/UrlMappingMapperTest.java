package com.nitdrv.urlmapper.service.mapper;

import static com.nitdrv.urlmapper.domain.UrlMappingAsserts.*;
import static com.nitdrv.urlmapper.domain.UrlMappingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlMappingMapperTest {

    private UrlMappingMapper urlMappingMapper;

    @BeforeEach
    void setUp() {
        urlMappingMapper = new UrlMappingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUrlMappingSample1();
        var actual = urlMappingMapper.toEntity(urlMappingMapper.toDto(expected));
        assertUrlMappingAllPropertiesEquals(expected, actual);
    }
}
