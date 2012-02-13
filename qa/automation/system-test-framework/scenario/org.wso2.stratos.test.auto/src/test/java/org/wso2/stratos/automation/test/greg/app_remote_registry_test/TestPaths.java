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
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

public class TestPaths extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestPaths.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = TestMove.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - TestPaths Test ..........................");
        GetOnPathsTest();
        PutOnPathsTest();
        log.info("Completed Running Registry API - TestPaths Test ..........................");
    }


    @Override
    public void cleanup() {
    }

    private void removeResource() {
        deleteResources("/testkrishantha");
        deleteResources("/testkrishantha1");
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


    public void GetOnPathsTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            registry.put("/testkrishantha/paths/r1", r1);
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha"));
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha/"));
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha/paths/r1"));
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha/paths/r1/"));

            registry.get("/testkrishantha");
            registry.get("/testkrishantha/");
            registry.get("/testkrishantha/paths/r1");
            registry.get("/testkrishantha/paths/r1/");
            deleteResources("/testkrishantha");
            log.info("GetOnPathsTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GetOnPathsTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GetOnPathsTest RegistryException thrown :" + e.getMessage());
        }


    }

    public void PutOnPathsTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("some content");
            registry.put("/testkrishantha1/paths2/r1", r1);

            Resource r2 = registry.newResource();
            r2.setContent("another content");
            registry.put("/testkrishantha1/paths2/r2", r2);

            Collection c1 = registry.newCollection();
            registry.put("/testkrishantha1/paths2/c1", c1);

            Collection c2 = registry.newCollection();
            registry.put("/testkrishantha1/paths2/c2", c2);

            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/r1"));
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/r2"));
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/c1"));
            assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/c2"));
            deleteResources("/testkrishantha1");
            log.info("PutOnPathsTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -PutOnPathsTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -PutOnPathsTest RegistryException thrown :" + e.getMessage());
        }
    }
}
