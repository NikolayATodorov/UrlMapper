package com.nitdrv.urlmapper.repository;

import com.nitdrv.urlmapper.domain.UrlMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the UrlMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, String> {
    List<UrlMapping> findByTtlNotNull ();
}
