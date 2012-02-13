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

public class TestMove extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestMove.class);
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
        log.info("Running Registry API - TestMove Test ..........................");
        ResourceMoveFromRootTest();
        ResourceMoveToRootTest();
        GeneralResourceMoveTest();
        GeneralCollectionMoveTest();
        log.info("Completed Running Registry API - TestMove Test ...................");
    }


    @Override
    public void cleanup() {
    }

    private void removeResource() {
        deleteResources("/test");
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

    public void ResourceMoveFromRootTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "move");
            r1.setContent("c");
            registry.put("/move1", r1);

            Collection c1 = registry.newCollection();
            registry.put("/test/move", c1);
            registry.move("/move1", "/test/move/move1");

            Resource newR1 = registry.get("/test/move/move1");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            boolean failed = false;
            try {
                registry.get("/move1");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Moved resource should not be accessible from the old path.", failed);
            deleteResources("/test");
            log.info("ResourceMoveFromRootTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddResourceRatingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddResourceRatingTestt RegistryException thrown :" + e.getMessage());
        }

    }

    public void ResourceMoveToRootTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "move");
            r1.setContent("c");
            registry.put("/test/move/move2", r1);
            registry.move("/test/move/move2", "/move2");

            Resource newR1 = registry.get("/move2");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            boolean failed = false;
            try {
                 registry.get("/test/move/move2");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Moved resource should not be accessible from the old path.", failed);
            deleteResources("/test");
            deleteResources("/move2");
            log.info("ResourceMoveToRootTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -ResourceMoveToRootTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -ResourceMoveToRootTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void GeneralResourceMoveTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "move");
            r1.setContent("c");
            registry.put("/test/c1/move/move3", r1);

            Collection c2 = registry.newCollection();
            registry.put("/test/c2/move", c2);
            registry.move("/test/c1/move/move3", "/test/c2/move/move3");

            Resource newR1 = registry.get("/test/c2/move/move3");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            boolean failed = false;
            try {
                registry.get("/test/c1/move/move3");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Moved resource should not be accessible from the old path.", failed);
            deleteResources("/test");
            log.info("GeneralResourceMoveTest -Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GeneralResourceMoveTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GeneralResourceMoveTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void GeneralCollectionMoveTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "move");
            r1.setContent("c");
            registry.put("/test/c1/move5/move/dummy", r1);

            Collection c2 = registry.newCollection();
            registry.put("/test/c3", c2);

            registry.move("/test/c1/move5", "/test/c3/move5");

            Resource newR1 = registry.get("/test/c3/move5/move/dummy");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            boolean failed = false;
            try {
                registry.get("/test/c1/move5/move/dummy");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Moved resource should not be accessible from the old path.", failed);
            deleteResources("/test");
            log.info("GeneralCollectionMoveTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GeneralCollectionMoveTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GeneralCollectionMoveTest RegistryException thrown :" + e.getMessage());
        }

    }


}
