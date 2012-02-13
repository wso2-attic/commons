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
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

public class RenameTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RenameTest.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = RenameTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();

    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - RenameTest Test ..........................");
        RootLevelResourceRenameTest();
        GeneralResourceRenameTest();
        RootLevelCollectionRenameTest();
        GeneralCollectionRenameTest();
        log.info("Completed Running Registry API - RenameTest Test ..........................");
    }

      @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/tests");
        deleteResources("/c2");

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

    public void RootLevelResourceRenameTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "rename");
            r1.setContent("some text");
            registry.put("/rename2", r1);
            registry.rename("/rename2", "/rename4");

            boolean failed = false;
            try {
                registry.get("/rename2");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Resource should not be accessible from the old path after renaming.", failed);
            Resource newR1 = registry.get("/rename4");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");
            deleteResources("/rename4");
            log.info("RootLevelResourceRenameTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RootLevelResourceRenameTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RootLevelResourceRenameTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void GeneralResourceRenameTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "rename");
            r1.setContent("some text");
            registry.put("/tests/rename1", r1);
            registry.rename("/tests/rename1", "rename2");

            boolean failed = false;
            try {
                registry.get("/tests/rename1");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Resource should not be accessible from the old path after renaming.", failed);
            Resource newR1 = registry.get("/tests/rename2");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");
            deleteResources("/tests");
            log.info("GeneralResourceRenameTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GeneralResourceRenameTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GeneralResourceRenameTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void RootLevelCollectionRenameTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "rename");
            r1.setContent("some text");
            registry.put("/rename34k/c1/dummy", r1);
            registry.rename("/rename34k", "/rename44k");

            boolean failed = false;
            try {
                registry.get("/rename34k/c1/dummy");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Resource should not be " +
                    "accessible from the old path after renaming the parent.", failed);
            Resource newR1 = registry.get("/rename44k/c1/dummy");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");
            deleteResources("/rename44k");
            log.info("RootLevelCollectionRenameTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RootLevelCollectionRenameTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RootLevelCollectionRenameTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void GeneralCollectionRenameTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "rename");
            r1.setContent("some text");
            registry.put("/c2/rename3/c1/dummy", r1);

            registry.rename("/c2/rename3", "rename4");

            boolean failed = false;
            try {
               registry.get("/c2/rename3/c1/dummy");
            } catch (RegistryException e) {
                failed = true;
            }
            assertTrue("Resource should not be " +
                    "accessible from the old path after renaming the parent.", failed);

            Resource newR1 = registry.get("/c2/rename4/c1/dummy");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");
            deleteResources("/c2");
            log.info("GeneralCollectionRenameTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GeneralCollectionRenameTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GeneralCollectionRenameTest RegistryException thrown :" + e.getMessage());
        }

    }
}
