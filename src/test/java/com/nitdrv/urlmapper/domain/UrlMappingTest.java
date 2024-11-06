package com.nitdrv.urlmapper.domain;

import static com.nitdrv.urlmapper.domain.UrlMappingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nitdrv.urlmapper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrlMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UrlMapping.class);
        UrlMapping urlMapping1 = getUrlMappingSample1();
        UrlMapping urlMapping2 = new UrlMapping();
        assertThat(urlMapping1).isNotEqualTo(urlMapping2);

        urlMapping2.setId(urlMapping1.getId());
        assertThat(urlMapping1).isEqualTo(urlMapping2);

        urlMapping2 = getUrlMappingSample2();
        assertThat(urlMapping1).isNotEqualTo(urlMapping2);
    }
}
