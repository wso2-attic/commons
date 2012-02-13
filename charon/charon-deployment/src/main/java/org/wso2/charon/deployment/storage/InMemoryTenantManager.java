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
package org.wso2.charon.deployment.storage;

import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.InternalServerException;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.schema.SCIMSchemaConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memroy tenant manager implementation for demo purpose only.
 */
public class InMemoryTenantManager {

    private static Map<Integer, String> tenantsMapping = new ConcurrentHashMap<Integer, String>();

    public static int getTenantId(String tenantDomain) throws InternalServerException {
        if (tenantsMapping.isEmpty()) {
            initTenantsMap();
        }
        //traverse through in-memory tenants map and obtain tenant id.
        for (Map.Entry<Integer, String> tenantMapping : tenantsMapping.entrySet()) {
            if (tenantDomain.equals(tenantMapping.getValue())) {
                return tenantMapping.getKey();
            }
        }
        throw new InternalServerException(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR,
                                          "Tenant not found.");

    }

    /**
     * ********For Demo purpose only***********************
     */
    private static void initTenantsMap() {
        tenantsMapping.put(1, "wso2.com");
        tenantsMapping.put(2, "wp.org");
    }

}
