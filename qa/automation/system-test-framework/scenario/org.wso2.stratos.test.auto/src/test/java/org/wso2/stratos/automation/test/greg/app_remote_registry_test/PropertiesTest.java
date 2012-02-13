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

import java.util.List;

public class PropertiesTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(PropertiesTest.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = PropertiesTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();

    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - PropertiesTest Test ..........................");
        RootLevelPropertiesTest();
        SingleValuedPropertiesTest();
        MultiValuedPropertiesTest();
        NullValuedPropertiesTest();
        NullMultiValuedPropertiesTest();
        RemovingMultivaluedPropertiesTest();
        RemovingPropertiesTest();
        EditingMultivaluedPropertiesTest();
        log.info("Completed Running Registry API - PropertiesTest Test ..........................");

    }


    @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/propTest");
        deleteResources("/propTest3");
        deleteResources("/propTest4");
        deleteResources("/props");

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


    public void RootLevelPropertiesTest() {
        Resource root;
        try {
            root = registry.get("/");
            root.addProperty("p1", "v1");
            registry.put("/", root);
            Resource rootb = registry.get("/");
            assertEquals("Root should have a property named p1 with value v1", rootb.getProperty("p1"), "v1");
            Resource r1e1 = registry.get("/");
            r1e1.removeProperty("p1");
            log.info("RootLevelPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RootLevelPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RootLevelPropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void SingleValuedPropertiesTest() {
        Resource r2;
        try {
            r2 = registry.newResource();
            r2.setContent("Some content for r2");
            r2.addProperty("p1", "p1v1");
            registry.put("/propTest/r2", r2);
            Resource r2b = registry.get("/propTest/r2");
            String p1Value = r2b.getProperty("p1");

            assertEquals("Property p1 of /propTest/r2 should contain the value p1v1",
                    p1Value, "p1v1");
            deleteResources("/propTest");
            log.info("SingleValuedPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -SingleValuedPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -SingleValuedPropertiesTest RegistryException thrown :" + e.getMessage());
        }


    }

    public void MultiValuedPropertiesTest() {
        String path = "/propTest/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("Some content for r1");
            r1.addProperty("p1", "p1v1");
            r1.addProperty("p1", "p1v2");
            registry.put(path, r1);

            Resource r1b = registry.get(path);
            List propValues = r1b.getPropertyValues("p1");

            assertTrue("Property p1 of /propTest/r1 should contain the value p1v1",
                    propValues.contains("p1v1"));
            assertTrue("Property p1 of /propTest/r1 should contain the value p1v2",
                    propValues.contains("p1v2"));
            deleteResources("/propTest");
            log.info("MultiValuedPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -MultiValuedPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -MultiValuedPropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void NullValuedPropertiesTest() {
        String path = "/propTest3/r2";
        Resource r2;
        try {
            r2 = registry.newResource();
            r2.setContent("Some content for r2");
            r2.addProperty("p1", null);
            registry.put(path, r2);
            Resource r2b = registry.get(path);
            String p1Value = r2b.getProperty("p1");
            assertEquals("Property p1 of /propTest3/r2 should contain the value null",
                    p1Value, null);
            deleteResources("/propTest3");
            log.info("NullValuedPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -NullValuedPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -NullValuedPropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void NullMultiValuedPropertiesTest() {
        String path = "/propTest4/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("Some content for r1");
            r1.addProperty("p1", null);
            r1.addProperty("p1", null);
            registry.put(path, r1);
            Resource r1b = registry.get(path);
            List propValues = r1b.getPropertyValues("p1");
            String value = "";
            try {
                value = (String) propValues.get(0);
            } catch (NullPointerException e) {
                assertTrue("Property p1 of /propTest4/r1 should contain the value null", true);
            }
            deleteResources("/propTest4");
            log.info("NullMultiValuedPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -NullMultiValuedPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -NullMultiValuedPropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void RemovingPropertiesTest() {
        String path = "/props/t1/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("r1 content");
            r1.setProperty("p1", "v1");
            r1.setProperty("p2", "v2");
            registry.put(path, r1);

            Resource r1e1 = registry.get(path);
            r1e1.setContent("r1 content");
            r1e1.removeProperty("p1");
            registry.put(path, r1e1);
            Resource r1e2 = registry.get(path);
            assertEquals("Property is not removed.", r1e2.getProperty("p1"), null);
            assertNotNull("Wrong property is removed.", r1e2.getProperty("p2"));
            deleteResources("/props");
            log.info("RemovingPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemovingPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemovingPropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void RemovingMultivaluedPropertiesTest() {
        String path = "/props/t2/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("r1 content");
            r1.addProperty("p1", "v1");
            r1.addProperty("p1", "v2");
            registry.put(path, r1);
            Resource r1e1 = registry.get("/props/t2/r1");
            r1e1.setContent("r1 content updated");
            r1e1.removePropertyValue("p1", "v1");
            registry.put(path, r1e1);
            Resource r1e2 = registry.get(path);
            assertFalse("Property is not removed.", r1e2.getPropertyValues("p1").contains("v1"));
            assertTrue("Wrong property is removed.", r1e2.getPropertyValues("p1").contains("v2"));
            deleteResources("/props");
            log.info("RemovingMultivaluedPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemovingMultivaluedPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemovingMultivaluedPropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void EditingMultivaluedPropertiesTest() {
        String path = "/props";
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("r1 content");
            r1.addProperty("p1", "v1");
            r1.addProperty("p1", "v2");
            r1.setProperty("test", "value2");
            r1.setProperty("test2", "value2");
            registry.put(path, r1);

            Resource r1e1 = registry.get(path);
            r1e1.setContent("r1 content");
            r1e1.editPropertyValue("p1", "v1", "v3");
//            List list = r1e1.getPropertyValues(path);
            registry.put(path, r1e1);
            Resource r1e2 = registry.get(path);
            assertFalse("Property is not edited.", r1e2.getPropertyValues("p1").contains("v1"));
            assertTrue("Property is not edited.", r1e2.getPropertyValues("p1").contains("v3"));
            assertTrue("Wrong property is removed.", r1e2.getPropertyValues("p1").contains("v2"));
            deleteResources("/props");
            log.info("EditingMultivaluedPropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -EditingMultivaluedPropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -EditingMultivaluedPropertiesTest RegistryException thrown :" + e.getMessage());
        }
    }
}
