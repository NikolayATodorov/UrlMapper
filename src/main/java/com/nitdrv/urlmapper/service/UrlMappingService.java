package com.nitdrv.urlmapper.service;

import com.nitdrv.urlmapper.service.dto.UrlMappingDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nitdrv.urlmapper.domain.UrlMapping}.
 */
public interface UrlMappingService {
    /**
     * Save a urlMapping.
     *
     * @param urlMappingDTO the entity to save.
     * @return the persisted entity.
     */
    UrlMappingDTO save(UrlMappingDTO urlMappingDTO);

    /**
     * Updates a urlMapping.
     *
     * @param urlMappingDTO the entity to update.
     * @return the persisted entity.
     */
    UrlMappingDTO update(UrlMappingDTO urlMappingDTO);

    /**
     * Partially updates a urlMapping.
     *
     * @param urlMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UrlMappingDTO> partialUpdate(UrlMappingDTO urlMappingDTO);

    /**
     * Get all the urlMappings.
     *
     * @return the list of entities.
     */
    List<UrlMappingDTO> findAll();

    /**
     * Get the "id" urlMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UrlMappingDTO> findOne(String id);

    /**
     * Delete the "id" urlMapping.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    public boolean idExists(String id);


}
