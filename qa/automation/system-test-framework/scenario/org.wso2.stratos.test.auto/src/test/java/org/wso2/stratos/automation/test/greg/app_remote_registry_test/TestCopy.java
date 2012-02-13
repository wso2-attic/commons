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


public class TestCopy extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestCopy.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = CommentTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - TestCopy Test ..........................");
        ResourceCopyTest();
        CollectionCopyTest();
        log.info("Completed Running Registry API - TestCopy Test .................");
    }

    @Override
    public void cleanup() {
    }

    private void removeResource() {
        deleteResources("/test1");
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

    public void ResourceCopyTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "copy");
            r1.setContent("c");
            registry.put("/test1/copy/c1/copy1", r1);

            Collection c1 = registry.newCollection();
            registry.put("/test1/move", c1);
            registry.copy("/test1/copy/c1/copy1", "/test1/copy/c2/copy2");

            Resource newR1 = registry.get("/test1/copy/c2/copy2");
            assertEquals("Copied resource should have a property named 'test' with value 'copy'.",
                    newR1.getProperty("test"), "copy");

            Resource oldR1 = registry.get("/test1/copy/c1/copy1");
            assertEquals("Original resource should have a property named 'test' with value 'copy'.",
                    oldR1.getProperty("test"), "copy");

            String newContent = new String((byte[]) newR1.getContent());
            String oldContent = new String((byte[]) oldR1.getContent());
            assertEquals("Contents are not equal in copied resources", newContent, oldContent);
            deleteResources("/test1");
            log.info("ResourceCopyTest");
        } catch (RegistryException e) {
            log.error("Registry API -ResourceCopyTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -ResourceCopyTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void CollectionCopyTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setProperty("test", "copy");
            r1.setContent("c");
            registry.put("/test1/copy/copy3/c3/resource1", r1);

            Collection c1 = registry.newCollection();
            registry.put("/test1/move", c1);
            registry.copy("/test1/copy/copy3", "/test1/newc/copy3");

            Resource newR1 = registry.get("/test1/newc/copy3/c3/resource1");
            assertEquals("Copied resource should have a property named 'test' with value 'copy'.",
                    newR1.getProperty("test"), "copy");

            Resource oldR1 = registry.get("/test1/copy/copy3/c3/resource1");
            assertEquals("Original resource should have a property named 'test' with value 'copy'.",
                    oldR1.getProperty("test"), "copy");
            deleteResources("/test1");
            log.info("CollectionCopyTest");
        } catch (RegistryException e) {
            log.error("Registry API -CollectionCopyTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -CollectionCopyTest RegistryException thrown :" + e.getMessage());
        }
    }
}
