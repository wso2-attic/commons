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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the cache management class and this holds the list of CachedObjects in the
 * cache. An instance of this will be stored in the appropriate place to retireve when
 * a request is processed (Note: CacheManager does not hold the actual cached responses
 * in it, rather it keeps a list of references to the cached responses stored in a
 * place which is accessible and replicateable through the whole cluster, mostly in one
 * of the AbstractContext)
 */
public class CacheManager {

    private Map<ServiceName, Map<RequestHash, CachableResponse>> cache;

    /**
     * This default constructor instantiates the CacheManager with defafult parameters
     */
    public CacheManager() {
        cache = new ConcurrentHashMap<ServiceName, Map<RequestHash, CachableResponse>>();
    }

    /**
     * This method will remove the expired cached responses from the provided context and
     * remove the keys from the reference list in the CacheManager
     *
     * @param serviceName Name of the serviceName
     * @param cacheReplicationCommand The StateReplicationCommand used for replicating the cache changes
     */
    public synchronized void removeExpiredResponses(ServiceName serviceName,
                                                    CacheReplicationCommand cacheReplicationCommand) {
        Map<RequestHash, CachableResponse> responseMap = cache.get(serviceName);
        for (Map.Entry<RequestHash, CachableResponse> entry : responseMap.entrySet()) {
            if (entry.getValue().isExpired() && !entry.getValue().isInUse()) {
                responseMap.remove(entry.getKey());
                cacheReplicationCommand.removeCachedResponse(serviceName, entry.getKey());
            }
        }
        if (responseMap.isEmpty()) {
            cache.remove(serviceName);
        }
    }

    public synchronized void removeExpiredResponse(ServiceName serviceName,
                                                   RequestHash requestHash) {
        Map<RequestHash, CachableResponse> responseMap = cache.get(serviceName);
        if (responseMap != null) {
            responseMap.remove(requestHash);
            if (responseMap.isEmpty()) {
                cache.remove(serviceName);
            }
        }
    }

    /**
     * This will return the response associated with the given requestHash if there is a cached
     * response or null otherwise
     *
     * @param serviceName - Name of the service
     * @param requestHash - Object representing the requestHash to be searched
     * @return cached response associated with the given requestHash
     */
    public CachableResponse getCachedResponse(ServiceName serviceName, RequestHash requestHash) {
        Map<RequestHash, CachableResponse> responseMap = cache.get(serviceName);
        if (responseMap != null) {
            return responseMap.get(requestHash);
        }
        return null;
    }

    public long getCacheSize(ServiceName serviceName) {
        Map<RequestHash, CachableResponse> responseMap = cache.get(serviceName);
        if (responseMap != null) {
            return responseMap.size();
        }
        return 0;
    }

    /**
     * This will be used to add a response to the cache associated with the given requestHash
     *
     * @param serviceName - Name of the service
     * @param requestHash - String representing the requestHash to be associated with the response
     * @param response    - CachableResponse representing the response cache
     */
    public void cacheResponse(ServiceName serviceName,
                              RequestHash requestHash,
                              CachableResponse response) {
        Map<RequestHash, CachableResponse> responseMap = cache.get(serviceName);
        if (responseMap == null) {
            responseMap = new ConcurrentHashMap<RequestHash, CachableResponse>();
            cache.put(serviceName, responseMap);
        }
        responseMap.put(requestHash, response);
    }

    /**
     * This will be used to add a response to the cache associated with the given requestHash
     *
     * @param serviceName - Name of the service
     * @param requestHash - String representing the requestHash to be associated with the response
     * @param response    - CachableResponse representing the response cache
     * @param cacheReplicationCommand The StateReplicationCommand used for replicating the cache changes
     */
    public void cacheResponse(ServiceName serviceName,
                              RequestHash requestHash,
                              CachableResponse response,
                              CacheReplicationCommand cacheReplicationCommand) {
        cacheResponse(serviceName, requestHash, response);
        cacheReplicationCommand.addCachedReponse(serviceName, requestHash, response);
    }
}
