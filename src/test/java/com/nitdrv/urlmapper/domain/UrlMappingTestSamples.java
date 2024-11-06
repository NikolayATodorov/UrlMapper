package com.nitdrv.urlmapper.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UrlMappingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UrlMapping getUrlMappingSample1() {
        return new UrlMapping().id(1L).originalUrl("originalUrl1").ttl(1L);
    }

    public static UrlMapping getUrlMappingSample2() {
        return new UrlMapping().id(2L).originalUrl("originalUrl2").ttl(2L);
    }

    public static UrlMapping getUrlMappingRandomSampleGenerator() {
        return new UrlMapping().id(longCount.incrementAndGet()).originalUrl(UUID.randomUUID().toString()).ttl(longCount.incrementAndGet());
    }
}
