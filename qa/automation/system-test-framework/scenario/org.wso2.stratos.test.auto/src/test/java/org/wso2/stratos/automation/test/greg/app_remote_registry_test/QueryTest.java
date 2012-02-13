/*
* Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.stratos.automation.test.greg.app_remote_registry_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

import java.util.HashMap;
import java.util.Map;

public class QueryTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(QueryTest.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = QueryTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - QueryTest Test ..........................");
        putRegistryQueriesTest();
        executeQueriesTest();
        log.info("Completed Running Registry API - QueryTest Test .................");

    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/_system/Queries1");

    }

    public void deleteResources(String resourceName) {
        try {
            if (registry.resourceExists(resourceName)) {
                registry.delete(resourceName);
            }
        } catch (RegistryException e) {
            log.error("deleteResources RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteResources RegistryException thrown:" + e.getMessage());
        }

    }


    public void putRegistryQueriesTest() {
        String QUERY_EPR_BY_PATH = "/_system/Queries1/EPRByPath-new";

        try {
            storeSQLQuery(QUERY_EPR_BY_PATH);
            assertTrue("Resource doesn't exists", registry.resourceExists(QUERY_EPR_BY_PATH));

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("1", RegistryConstants.SQL_QUERY_MEDIA_TYPE); // media type
            Collection collection = registry.executeQuery(QUERY_EPR_BY_PATH, parameters);
            String[] children = collection.getChildren();

            boolean successful = false;
            for (String path : children) {
                if (path.contains(QUERY_EPR_BY_PATH)) successful = true;
            }
            deleteResources("/_system/Queries1");
            log.info("putRegistryQueriesTest - Passed");
            assertTrue(successful);
        } catch (Exception e) {
            log.error("Registry API -putRegistryQueriesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -putRegistryQueriesTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void executeQueriesTest() {
        String QUERY_EPR_BY_PATH = "/_system/Queries1/EPRByPath-new";
//        Resource resource1 = null;

        try {
            storeSQLQuery(QUERY_EPR_BY_PATH);
            assertTrue("Resource doesn't exists", registry.resourceExists(QUERY_EPR_BY_PATH));

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("1", RegistryConstants.SQL_QUERY_MEDIA_TYPE); // media type
            Collection collection = registry.executeQuery(QUERY_EPR_BY_PATH, parameters);
            String[] children = collection.getChildren();

            boolean successful = false;
            for (String path : children) {
                if (path.contains(QUERY_EPR_BY_PATH)) successful = true;
            }
            assertTrue(successful);
            deleteResources("/_system/Queries1");
            log.info("putRegistryQueriesTest - Passed");
        } catch (Exception e) {
            log.error("Registry API -executeQueriesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -executeQueriesTest RegistryException thrown :" + e.getMessage());
        }
    }


    private void storeSQLQuery(String path) throws RegistryException, Exception {
        String sql1 = "SELECT REG_PATH_ID, REG_NAME FROM REG_RESOURCE R WHERE R.REG_MEDIA_TYPE LIKE ?";
        Resource q1 = registry.newResource();
        q1.setContent(sql1);
        q1.setMediaType(RegistryConstants.SQL_QUERY_MEDIA_TYPE);
        q1.addProperty(RegistryConstants.RESULT_TYPE_PROPERTY_NAME,
                RegistryConstants.RESOURCES_RESULT_TYPE);
        registry.put(path, q1);
    }
}
