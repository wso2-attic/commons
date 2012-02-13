/*
*  Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.caching;

import org.apache.axis2.clustering.ClusteringFault;
import org.apache.axis2.clustering.state.StateClusteringCommand;
import org.apache.axis2.context.ConfigurationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles replication of the Cache. Handles both addition of new entries as well
 * as removal of entries
 */
public class CacheReplicationCommand extends StateClusteringCommand {

    private List<AddedCachedResponse>   addedItems   = new ArrayList<AddedCachedResponse>();
    private List<RemovedCachedResponse> removedItems = new ArrayList<RemovedCachedResponse>();

    public void addCachedReponse(ServiceName serviceName, RequestHash requestHash,
                                 CachableResponse response) {
        addedItems.add(new AddedCachedResponse(serviceName, requestHash, response));
    }

    public void removeCachedResponse(ServiceName serviceName, RequestHash requestHash) {
        removedItems.add(new RemovedCachedResponse(serviceName, requestHash));
    }

    @Override
    public void execute(ConfigurationContext configurationContext) throws ClusteringFault {
        CacheManager cacheManager =
                (CacheManager) configurationContext.getPropertyNonReplicable(CachingConstants.CACHE_MANAGER);
        for (AddedCachedResponse addedItem : addedItems) {
            cacheManager.cacheResponse(addedItem.getServiceName(),
                                       addedItem.getRequestHash(),
                                       addedItem.getResponse());
        }
        for (RemovedCachedResponse removedItem : removedItems) {
            cacheManager.removeExpiredResponse(removedItem.getServiceName(),
                                               removedItem.getRequestHash());
        }
    }

    private static class RemovedCachedResponse implements Serializable {
        private ServiceName serviceName;
        private RequestHash requestHash;

        private RemovedCachedResponse(ServiceName serviceName, RequestHash requestHash) {
            this.serviceName = serviceName;
            this.requestHash = requestHash;
        }

        public ServiceName getServiceName() {
            return serviceName;
        }

        public RequestHash getRequestHash() {
            return requestHash;
        }
    }

    private static class AddedCachedResponse implements Serializable {
        private ServiceName      serviceName;
        private RequestHash      requestHash;
        private CachableResponse response;

        private AddedCachedResponse(ServiceName serviceName,
                                    RequestHash requestHash,
                                    CachableResponse response) {
            this.serviceName = serviceName;
            this.requestHash = requestHash;
            this.response = response;
        }

        public ServiceName getServiceName() {
            return serviceName;
        }

        public RequestHash getRequestHash() {
            return requestHash;
        }

        public CachableResponse getResponse() {
            return response;
        }
    }

    @Override
    public String toString() {
        return "CacheReplicationCommand";
    }
}
