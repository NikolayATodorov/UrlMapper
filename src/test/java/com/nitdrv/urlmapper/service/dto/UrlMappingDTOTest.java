package com.nitdrv.urlmapper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitdrv.urlmapper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrlMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UrlMappingDTO.class);
        UrlMappingDTO urlMappingDTO1 = new UrlMappingDTO();
        urlMappingDTO1.setId(1L);
        UrlMappingDTO urlMappingDTO2 = new UrlMappingDTO();
        assertThat(urlMappingDTO1).isNotEqualTo(urlMappingDTO2);
        urlMappingDTO2.setId(urlMappingDTO1.getId());
        assertThat(urlMappingDTO1).isEqualTo(urlMappingDTO2);
        urlMappingDTO2.setId(2L);
        assertThat(urlMappingDTO1).isNotEqualTo(urlMappingDTO2);
        urlMappingDTO1.setId(null);
        assertThat(urlMappingDTO1).isNotEqualTo(urlMappingDTO2);
    }
}
