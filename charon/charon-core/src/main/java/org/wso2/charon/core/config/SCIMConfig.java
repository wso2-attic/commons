/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.charon.core.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SCIMConfig {

    private Map<String, SCIMProvider> providersMap;
    private Map<String, SCIMConsumer> consumersMap;

    public Map<String, SCIMProvider> getProvidersMap() {
        return providersMap;
    }

    public void setProvidersMap(Map<String, SCIMProvider> providersMap) {
        providersMap = providersMap;
    }

    public Map<String, SCIMConsumer> getConsumersMap() {
        return consumersMap;
    }

    public void setConsumersMap(Map<String, SCIMConsumer> consumersMap) {
        consumersMap = consumersMap;
    }

    public SCIMConsumer getConsumer(String consumerId) {
        if (consumersMap != null && consumersMap.containsKey(consumerId)) {
            //when returning the consumer, go through the consumer's providers map and fill
            //their properties map if no property map is in the consumer, and if there is any properties
            //in consumer's provider, skip those attributes and inherit other attributes
            SCIMConsumer scimConsumer = consumersMap.get(consumerId);
            Map<String, SCIMProvider> providers = scimConsumer.getScimProviders();
            if ((providers != null) && (!providers.isEmpty())) {
                for (Map.Entry<String, SCIMProvider> scimProviderEntry : providers.entrySet()) {
                    String providerId = scimProviderEntry.getKey();
                    SCIMProvider scimProvider = scimProviderEntry.getValue();
                    if ((scimProvider.getProperties() != null) &&
                        (!scimProvider.getProperties().isEmpty())) {
                        //inherit properties from provider list other than the ones in the consumer's provider
                        Map<String, String> providerPropMap = scimProvider.getProperties();
                        if ((providersMap != null) && (!providersMap.isEmpty())) {
                            SCIMProvider provider = providersMap.get(providerId);
                            Map<String, String> providerProperties = provider.getProperties();
                            for (Map.Entry<String, String> entry : providerProperties.entrySet()) {
                                if (providerPropMap.containsKey(entry.getKey())) {
                                    continue;
                                }
                                providerPropMap.put(entry.getKey(), entry.getValue());
                            }
                            scimProvider.setProperties(providerProperties);
                        }
                    } else {
                        //go through the providers map, get the relevant provider's map,
                        if ((providersMap != null) && (!providersMap.isEmpty())) {
                            SCIMProvider provider = providersMap.get(providerId);
                            Map<String, String> providerProperties = provider.getProperties();
                            //TODO:verify whether the properties are updated in the provider of the map
                            scimProvider.setProperties(providerProperties);
                        }

                    }
                }
            }
            return consumersMap.get(consumerId);
        } else {
            return null;
        }
    }
}
