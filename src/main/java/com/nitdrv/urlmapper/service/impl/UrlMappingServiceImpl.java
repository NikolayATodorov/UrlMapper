package com.nitdrv.urlmapper.service.impl;

import com.nitdrv.urlmapper.domain.UrlMapping;
import com.nitdrv.urlmapper.repository.UrlMappingRepository;
import com.nitdrv.urlmapper.service.UrlMappingService;
import com.nitdrv.urlmapper.service.dto.UrlMappingDTO;
import com.nitdrv.urlmapper.service.mapper.UrlMappingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link UrlMapping}.
 */
@Service
@Transactional
public class UrlMappingServiceImpl implements UrlMappingService {

    private final Logger log = LoggerFactory.getLogger(UrlMappingServiceImpl.class);

    private static final String ID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final UrlMappingRepository urlMappingRepository;

    private final UrlMappingMapper urlMappingMapper;

    private int length = 1;

    public UrlMappingServiceImpl(UrlMappingRepository urlMappingRepository, UrlMappingMapper urlMappingMapper) {
        this.urlMappingRepository = urlMappingRepository;
        this.urlMappingMapper = urlMappingMapper;
    }

    @Override
    public UrlMappingDTO save(UrlMappingDTO urlMappingDTO) {
        log.debug("Request to save UrlMapping : {}", urlMappingDTO);
        UrlMapping urlMapping = urlMappingMapper.toEntity(urlMappingDTO);
        while (urlMapping.getId() == null) {
            String id = "0";
            for (int i = 0; i < Math.pow(36, length); i++) {
                id = generateShortId(length);
                if (!urlMappingRepository.existsById(id)) {
                    urlMapping.setId(id);
                    break;
                }
            }
            if (urlMapping.getId() == null) {
                length++;
            }
        }
        urlMapping.setCreationTimestamp(Instant.now());
        urlMapping = urlMappingRepository.save(urlMapping);
        return urlMappingMapper.toDto(urlMapping);
    }

    @Override
    public UrlMappingDTO update(UrlMappingDTO urlMappingDTO) {
        log.debug("Request to update UrlMapping : {}", urlMappingDTO);
        UrlMapping urlMapping = urlMappingMapper.toEntity(urlMappingDTO);
        urlMapping = urlMappingRepository.save(urlMapping);
        return urlMappingMapper.toDto(urlMapping);
    }

    @Override
    public Optional<UrlMappingDTO> partialUpdate(UrlMappingDTO urlMappingDTO) {
        log.debug("Request to partially update UrlMapping : {}", urlMappingDTO);

        return urlMappingRepository
            .findById(urlMappingDTO.getId())
            .map(existingUrlMapping -> {
                urlMappingMapper.partialUpdate(existingUrlMapping, urlMappingDTO);

                return existingUrlMapping;
            })
            .map(urlMappingRepository::save)
            .map(urlMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UrlMappingDTO> findAll() {
        log.debug("Request to get all UrlMappings");
        return urlMappingRepository.findAll().stream().map(urlMappingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UrlMappingDTO> findOne(String id) {
        log.debug("Request to get UrlMapping : {}", id);
        return urlMappingRepository.findById(id).map(urlMappingMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete UrlMapping : {}", id);
        urlMappingRepository.deleteById(id);
    }

    public String generateShortId(int length) {
        StringBuilder shortId = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * ID_CHARACTERS.length());
            shortId.append(ID_CHARACTERS.charAt(index));
        }
        return shortId.toString();
    }

    @Override
    public boolean idExists(String id) {
        return Boolean.TRUE.equals(urlMappingRepository.existsById(id));
    }

    @Scheduled(fixedRateString = "${application.expired-urlmappings-cleanup-interval}" + "000")
    protected void cleanExpiredUrlMappings () {
        Instant now = Instant.now();
        List<UrlMapping> urlMappingsWithTtl = urlMappingRepository.findByTtlNotNull();
        if (urlMappingsWithTtl != null) {
            for (UrlMapping urlMapping : urlMappingsWithTtl) {
                if (urlMapping.getCreationTimestamp().plusMillis(urlMapping.getTtl()).isBefore(now)) {
                    delete(urlMapping.getId());
                }
            }
        }
    }

}
