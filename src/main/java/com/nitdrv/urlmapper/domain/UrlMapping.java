package com.nitdrv.urlmapper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UrlMapping.
 */
@Entity
@Table(name = "url_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UrlMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private String id;

    @NotNull
    @Column(name = "original_url", nullable = false, unique = true)
    private String originalUrl;

    @Column(name = "ttl")
    private Long ttl;

    @NotNull
    @Column(name = "creation_timestamp", nullable = false)
    private Instant creationTimestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public UrlMapping id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public UrlMapping originalUrl(String originalUrl) {
        this.setOriginalUrl(originalUrl);
        return this;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getTtl() {
        return this.ttl;
    }

    public UrlMapping ttl(Long ttl) {
        this.setTtl(ttl);
        return this;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Instant getCreationTimestamp() {
        return this.creationTimestamp;
    }

    public UrlMapping creationTimestamp(Instant creationTimestamp) {
        this.setCreationTimestamp(creationTimestamp);
        return this;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrlMapping)) {
            return false;
        }
        return getId() != null && getId().equals(((UrlMapping) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UrlMapping{" +
            "id=" + getId() +
            ", originalUrl='" + getOriginalUrl() + "'" +
            ", ttl=" + getTtl() +
            ", creationTimestamp='" + getCreationTimestamp() + "'" +
            "}";
    }
}
