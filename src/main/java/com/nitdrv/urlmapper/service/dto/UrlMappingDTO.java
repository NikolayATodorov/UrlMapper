package com.nitdrv.urlmapper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nitdrv.urlmapper.domain.UrlMapping} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UrlMappingDTO implements Serializable {

    private String id;

    @NotNull
    private String originalUrl;

    private Long ttl;

//    @NotNull
    private Instant creationTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrlMappingDTO)) {
            return false;
        }

        UrlMappingDTO urlMappingDTO = (UrlMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, urlMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UrlMappingDTO{" +
            "id=" + getId() +
            ", originalUrl='" + getOriginalUrl() + "'" +
            ", ttl=" + getTtl() +
            ", creationTimestamp='" + getCreationTimestamp() + "'" +
            "}";
    }
}
