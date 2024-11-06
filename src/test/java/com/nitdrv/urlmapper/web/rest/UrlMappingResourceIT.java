package com.nitdrv.urlmapper.web.rest;

import static com.nitdrv.urlmapper.domain.UrlMappingAsserts.*;
import static com.nitdrv.urlmapper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nitdrv.urlmapper.IntegrationTest;
import com.nitdrv.urlmapper.domain.UrlMapping;
import com.nitdrv.urlmapper.repository.UrlMappingRepository;
import com.nitdrv.urlmapper.service.dto.UrlMappingDTO;
import com.nitdrv.urlmapper.service.mapper.UrlMappingMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UrlMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UrlMappingResourceIT {

    private static final String DEFAULT_ORIGINAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_TTL = 1L;
    private static final Long UPDATED_TTL = 2L;

    private static final Instant DEFAULT_CREATION_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/url-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private UrlMappingMapper urlMappingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUrlMappingMockMvc;

    private UrlMapping urlMapping;

    private UrlMapping insertedUrlMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UrlMapping createEntity(EntityManager em) {
        UrlMapping urlMapping = new UrlMapping()
            .originalUrl(DEFAULT_ORIGINAL_URL)
            .ttl(DEFAULT_TTL)
            .creationTimestamp(DEFAULT_CREATION_TIMESTAMP);
        return urlMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UrlMapping createUpdatedEntity(EntityManager em) {
        UrlMapping urlMapping = new UrlMapping()
            .originalUrl(UPDATED_ORIGINAL_URL)
            .ttl(UPDATED_TTL)
            .creationTimestamp(UPDATED_CREATION_TIMESTAMP);
        return urlMapping;
    }

    @BeforeEach
    public void initTest() {
        urlMapping = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUrlMapping != null) {
            urlMappingRepository.delete(insertedUrlMapping);
            insertedUrlMapping = null;
        }
    }

    @Test
    @Transactional
    void createUrlMapping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);
        var returnedUrlMappingDTO = om.readValue(
            restUrlMappingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urlMappingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UrlMappingDTO.class
        );

        // Validate the UrlMapping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUrlMapping = urlMappingMapper.toEntity(returnedUrlMappingDTO);
        assertUrlMappingUpdatableFieldsEquals(returnedUrlMapping, getPersistedUrlMapping(returnedUrlMapping));

        insertedUrlMapping = returnedUrlMapping;
    }

    @Test
    @Transactional
    void createUrlMappingWithExistingId() throws Exception {
        // Create the UrlMapping with an existing ID
        urlMapping.setId(1L);
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUrlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urlMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOriginalUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        urlMapping.setOriginalUrl(null);

        // Create the UrlMapping, which fails.
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        restUrlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urlMappingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        urlMapping.setCreationTimestamp(null);

        // Create the UrlMapping, which fails.
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        restUrlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urlMappingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUrlMappings() throws Exception {
        // Initialize the database
        insertedUrlMapping = urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList
        restUrlMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urlMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalUrl").value(hasItem(DEFAULT_ORIGINAL_URL)))
            .andExpect(jsonPath("$.[*].ttl").value(hasItem(DEFAULT_TTL.intValue())))
            .andExpect(jsonPath("$.[*].creationTimestamp").value(hasItem(DEFAULT_CREATION_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getUrlMapping() throws Exception {
        // Initialize the database
        insertedUrlMapping = urlMappingRepository.saveAndFlush(urlMapping);

        // Get the urlMapping
        restUrlMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, urlMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(urlMapping.getId().intValue()))
            .andExpect(jsonPath("$.originalUrl").value(DEFAULT_ORIGINAL_URL))
            .andExpect(jsonPath("$.ttl").value(DEFAULT_TTL.intValue()))
            .andExpect(jsonPath("$.creationTimestamp").value(DEFAULT_CREATION_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUrlMapping() throws Exception {
        // Get the urlMapping
        restUrlMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUrlMapping() throws Exception {
        // Initialize the database
        insertedUrlMapping = urlMappingRepository.saveAndFlush(urlMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the urlMapping
        UrlMapping updatedUrlMapping = urlMappingRepository.findById(urlMapping.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUrlMapping are not directly saved in db
        em.detach(updatedUrlMapping);
        updatedUrlMapping.originalUrl(UPDATED_ORIGINAL_URL).ttl(UPDATED_TTL).creationTimestamp(UPDATED_CREATION_TIMESTAMP);
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(updatedUrlMapping);

        restUrlMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, urlMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(urlMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUrlMappingToMatchAllProperties(updatedUrlMapping);
    }

    @Test
    @Transactional
    void putNonExistingUrlMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urlMapping.setId(longCount.incrementAndGet());

        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, urlMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(urlMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUrlMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urlMapping.setId(longCount.incrementAndGet());

        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(urlMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUrlMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urlMapping.setId(longCount.incrementAndGet());

        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urlMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUrlMappingWithPatch() throws Exception {
        // Initialize the database
        insertedUrlMapping = urlMappingRepository.saveAndFlush(urlMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the urlMapping using partial update
        UrlMapping partialUpdatedUrlMapping = new UrlMapping();
        partialUpdatedUrlMapping.setId(urlMapping.getId());

        partialUpdatedUrlMapping.originalUrl(UPDATED_ORIGINAL_URL).ttl(UPDATED_TTL).creationTimestamp(UPDATED_CREATION_TIMESTAMP);

        restUrlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrlMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUrlMapping))
            )
            .andExpect(status().isOk());

        // Validate the UrlMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUrlMappingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUrlMapping, urlMapping),
            getPersistedUrlMapping(urlMapping)
        );
    }

    @Test
    @Transactional
    void fullUpdateUrlMappingWithPatch() throws Exception {
        // Initialize the database
        insertedUrlMapping = urlMappingRepository.saveAndFlush(urlMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the urlMapping using partial update
        UrlMapping partialUpdatedUrlMapping = new UrlMapping();
        partialUpdatedUrlMapping.setId(urlMapping.getId());

        partialUpdatedUrlMapping.originalUrl(UPDATED_ORIGINAL_URL).ttl(UPDATED_TTL).creationTimestamp(UPDATED_CREATION_TIMESTAMP);

        restUrlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrlMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUrlMapping))
            )
            .andExpect(status().isOk());

        // Validate the UrlMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUrlMappingUpdatableFieldsEquals(partialUpdatedUrlMapping, getPersistedUrlMapping(partialUpdatedUrlMapping));
    }

    @Test
    @Transactional
    void patchNonExistingUrlMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urlMapping.setId(longCount.incrementAndGet());

        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, urlMappingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(urlMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUrlMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urlMapping.setId(longCount.incrementAndGet());

        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(urlMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUrlMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urlMapping.setId(longCount.incrementAndGet());

        // Create the UrlMapping
        UrlMappingDTO urlMappingDTO = urlMappingMapper.toDto(urlMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(urlMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UrlMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUrlMapping() throws Exception {
        // Initialize the database
        insertedUrlMapping = urlMappingRepository.saveAndFlush(urlMapping);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the urlMapping
        restUrlMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, urlMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return urlMappingRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UrlMapping getPersistedUrlMapping(UrlMapping urlMapping) {
        return urlMappingRepository.findById(urlMapping.getId()).orElseThrow();
    }

    protected void assertPersistedUrlMappingToMatchAllProperties(UrlMapping expectedUrlMapping) {
        assertUrlMappingAllPropertiesEquals(expectedUrlMapping, getPersistedUrlMapping(expectedUrlMapping));
    }

    protected void assertPersistedUrlMappingToMatchUpdatableProperties(UrlMapping expectedUrlMapping) {
        assertUrlMappingAllUpdatablePropertiesEquals(expectedUrlMapping, getPersistedUrlMapping(expectedUrlMapping));
    }
}
