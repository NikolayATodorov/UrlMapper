package com.nitdrv.urlmapper.service.mapper;

import com.nitdrv.urlmapper.domain.UrlMapping;
import com.nitdrv.urlmapper.service.dto.UrlMappingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UrlMapping} and its DTO {@link UrlMappingDTO}.
 */
@Mapper(componentModel = "spring")
public interface UrlMappingMapper extends EntityMapper<UrlMappingDTO, UrlMapping> {}
