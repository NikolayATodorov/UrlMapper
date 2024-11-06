package com.nitdrv.urlmapper.web.rest;

import com.nitdrv.urlmapper.repository.UrlMappingRepository;
import com.nitdrv.urlmapper.service.UrlMappingService;
import com.nitdrv.urlmapper.service.dto.UrlMappingDTO;
import com.nitdrv.urlmapper.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.nitdrv.urlmapper.domain.UrlMapping}.
 */
@RestController
@RequestMapping("/api/url-mappings")
public class UrlMappingResource {

    private final Logger log = LoggerFactory.getLogger(UrlMappingResource.class);

    private static final String ENTITY_NAME = "urlMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UrlMappingService urlMappingService;

    private final UrlMappingRepository urlMappingRepository;

    public UrlMappingResource(UrlMappingService urlMappingService, UrlMappingRepository urlMappingRepository) {
        this.urlMappingService = urlMappingService;
        this.urlMappingRepository = urlMappingRepository;
    }

    /**
     * {@code POST  /url-mappings} : Create a new urlMapping.
     *
     * @param urlMappingDTO the urlMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new urlMappingDTO, or with status {@code 400 (Bad Request)} if the urlMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UrlMappingDTO> createUrlMapping(@Valid @RequestBody UrlMappingDTO urlMappingDTO) throws URISyntaxException {
        log.debug("REST request to save UrlMapping : {}", urlMappingDTO);
        if (urlMappingDTO.getId() != null && urlMappingService.idExists(urlMappingDTO.getId())) {
            throw new BadRequestAlertException("A UrlMapping entry with the provided ID already exists!", ENTITY_NAME, "idexists");
        }

        urlMappingDTO = urlMappingService.save(urlMappingDTO);
        log.debug("The UrlMapping was successfully saved : {}", urlMappingDTO);

        return ResponseEntity.created(new URI("/api/url-mappings/" + urlMappingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, urlMappingDTO.getId().toString()))
            .body(urlMappingDTO);
    }

    /**
     * {@code PUT  /url-mappings/:id} : Updates an existing urlMapping.
     *
     * @param id the id of the urlMappingDTO to save.
     * @param urlMappingDTO the urlMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urlMappingDTO,
     * or with status {@code 400 (Bad Request)} if the urlMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the urlMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UrlMappingDTO> updateUrlMapping(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody UrlMappingDTO urlMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UrlMapping : {}, {}", id, urlMappingDTO);
        if (urlMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!urlMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (!Objects.equals(id, urlMappingDTO.getId())) {
            urlMappingService.delete(id);
            urlMappingDTO = urlMappingService.save(urlMappingDTO);
        } else {
            urlMappingDTO = urlMappingService.update(urlMappingDTO);
        }

        log.debug("The UrlMapping was successfully updated : {}", urlMappingDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, urlMappingDTO.getId().toString()))
            .body(urlMappingDTO);
    }

    /**
     * {@code PATCH  /url-mappings/:id} : Partial updates given fields of an existing urlMapping, field will ignore if it is null
     *
     * @param id the id of the urlMappingDTO to save.
     * @param urlMappingDTO the urlMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urlMappingDTO,
     * or with status {@code 400 (Bad Request)} if the urlMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the urlMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the urlMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UrlMappingDTO> partialUpdateUrlMapping(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody UrlMappingDTO urlMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UrlMapping partially : {}, {}", id, urlMappingDTO);
        if (urlMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urlMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urlMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UrlMappingDTO> result = urlMappingService.partialUpdate(urlMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, urlMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /url-mappings} : get all the urlMappings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of urlMappings in body.
     */
    @GetMapping("")
    public List<UrlMappingDTO> getAllUrlMappings() {
        log.debug("REST request to get all UrlMappings");
        return urlMappingService.findAll();
    }

    /**
     * {@code GET  /url-mappings/:id} : get the "id" urlMapping.
     *
     * @param id the id of the urlMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the urlMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UrlMappingDTO> getUrlMapping(@PathVariable("id") String id) {
        log.debug("REST request to get UrlMapping : {}", id);
        Optional<UrlMappingDTO> urlMappingDTO = urlMappingService.findOne(id);
        if (urlMappingDTO.isEmpty()) {
            log.debug("No UrlMapping was found with ID : {}", id);
        } else {
            log.debug("A UrlMapping with the provided ID was successfully retrieved : {}", id);
        }
        return ResponseUtil.wrapOrNotFound(urlMappingDTO);
    }

    /**
     * {@code DELETE  /url-mappings/:id} : delete the "id" urlMapping.
     *
     * @param id the id of the urlMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrlMapping(@PathVariable("id") String id) {
        log.debug("REST request to delete UrlMapping : {}", id);
        Optional<UrlMappingDTO> urlMappingDTO = urlMappingService.findOne(id);
        urlMappingService.delete(id);
        if (urlMappingDTO.isEmpty()) {
            log.debug("No UrlMapping with the provided ID was found to delete : {}", id);
        } else {
            log.debug("The UrlMapping with the provided ID was successfully deleted : {}", id);
        }

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
