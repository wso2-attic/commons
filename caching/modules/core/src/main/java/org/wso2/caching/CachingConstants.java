/*
 * Copyright (c) 2006, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.caching;

import org.wso2.caching.digest.DigestGenerator;
import org.wso2.caching.digest.DOMHASHGenerator;

import javax.xml.namespace.QName;

/**
 * This class holds the caching related constants 
 */
public final class CachingConstants {

    /** QName of the cached operation which will be used by the module */
    public static QName CACHED_OPERATION_QNAME = new QName("CachedOperation");

    /** String key to store the the request hash in the message contetx */
    public static final String REQUEST_HASH = "requestHash";

    /** String key to store the cached response in the message context */
    public static final String CACHED_OBJECT = "CachableResponse";

    /**
     * String key to store the clustering state replication object
     */
    public static final String STATE_REPLICATION_OBJECT = "StateReplicationObject";

    /** String key to store the cache object */
    public static final String CACHE_MANAGER = "cacheManager";

    /** String key to store the cache object */
    public static final String CACHE_CONFIGURATION = "cacheConfiguration";

    /** Caching namespace string value */
    public static final String CACHING_NS = "http://www.wso2.org/ns/2007/06/commons/caching";

    /** Caching namespace prefix */
    public static final String CACHING_NS_PREFIX = "wsch";

    /** Caching assertion QName for the caching policy */
    public static final QName CACHING_ASSERTION_QNAME
            = new QName(CACHING_NS, "CachingAssertion", CACHING_NS_PREFIX);

    /** XMLIdentifier QName in the caching policy */
    public static final QName CACHING_XML_IDENTIFIER_QNAME
            = new QName(CACHING_NS, "XMLIdentifier", CACHING_NS_PREFIX);

    /** Expire time QName for the cache in the caching policy */
    public static final QName CACHE_EXPIRATION_TIME_QNAME
            = new QName(CACHING_NS, "ExpireTime", CACHING_NS_PREFIX);

    /** Expire time QName for the cache in the caching policy */
    public static final QName MAX_CACHE_SIZE_QNAME
            = new QName(CACHING_NS, "MaxCacheSize", CACHING_NS_PREFIX);

    /** Expire time QName for the cache in the caching policy */
    public static final QName MAX_MESSAGE_SIZE_QNAME
            = new QName(CACHING_NS, "MaxMessageSize", CACHING_NS_PREFIX);

    /** Default DigestGenerator for the caching impl */
    public static final DigestGenerator DEFAULT_XML_IDENTIFIER = new DOMHASHGenerator();

    /** per-mediator cache scope attribute value */
    public static final String SCOPE_PER_MEDIATOR = "per-mediator";

    /** per-host cache scope attribute value */
    public static final String SCOPE_PER_HOST = "per-host";

    /** distributed cache scope attribute value */
    public static final String SCOPE_DISTRIBUTED = "distributed";

    /** in memory cache scope attribute value */    
    public static final String TYPE_MEMORY = "memory";

    /** disk based cache scope attribute value */    
    public static final String TYPE_DISK = "disk";

    /** Default cache size (in-memory) */
    public static final int DEFAULT_CACHE_SIZE = 1000;
}
