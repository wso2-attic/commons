package org.wso2.caching;

import org.wso2.caching.digest.DigestGenerator;

/**
 * 
 */
public class CacheConfiguration {

    /**
     * This variable will hold the DigestGenerator implementation to be used for
     * the request/response identifier generation
     */
    private DigestGenerator digestGenerator = CachingConstants.DEFAULT_XML_IDENTIFIER;

    /**
     * This holds the maximum message size that can be used in caching. i.e. if a particular
     * message exceeds this specified size, then that message will not be cached. This is
     * because hash generation can be slower in big messages and might take much longer time
     * than normal serve
     */
    private int maxMessageSize = 0;

    /**
     * This holds the maximum number of response messages that will be cached in the
     * CacheManager. If this is not specified by the policy then, it will be taken as the
     * default value specified in the CachingConstants.
     */
    private int maxCacheSize = CachingConstants.DEFAULT_CACHE_SIZE;

    /**
     * This will hold the cache expiration interval in milliseconds. From the cache
     * implementation point of view each and every cached response will be cached
     * for this time period enless there is a explicit value set by the response
     * from the server. (For example through http cache-control headers)
     */
    private long timeout = 0L;

    public DigestGenerator getDigestGenerator() {
        return digestGenerator;
    }

    public void setDigestGenerator(DigestGenerator digestGenerator) {
        this.digestGenerator = digestGenerator;
    }

    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
