package org.wso2.stratos.automation.test.greg.registry_ws_api_test;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.util.List;

public class PropertiesTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(PropertiesTest.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = PropertiesTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API PropertiesTest Test Cases............................ ");
        rootLevelProperties();
        singleValuedProperties();
        multiValuedProperties();
        removingProperties();
        removingMultivaluedProperties();
        editingMultivaluedProperties();
        log.info("Completed Running WS-API PropertiesTest Test Cases.................. ");
    }

    @Override
    public void cleanup() {
    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/propTest");
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


    private void rootLevelProperties() {
        Resource root;
        try {
            root = registry.get("/");
            root.addProperty("p1", "v1");
            registry.put("/", root);

            Resource rootb = registry.get("/");
            assertEquals("Root should have a property named p1 with value v1", rootb.getProperty("p1"), "v1");
            log.info("rootLevelProperties() -Passed");

        } catch (RegistryException e) {
            log.error("rootLevelProperties RegistryException thrown :" + e.getMessage());
            Assert.fail("rootLevelProperties RegistryException thrown :" + e.getMessage());
        }
    }

    private void singleValuedProperties() {
        String path = "/propTest/r2";
        Resource r2 = registry.newResource();
        try {
            r2.setContent("Some content for r2");

            r2.addProperty("p1", "p1v1");
            registry.put(path, r2);

            Resource r2b = registry.get(path);
            String p1Value = r2b.getProperty("p1");

            assertEquals("Property p1 of /propTest/r2 should contain the value p1v1",
                    p1Value, "p1v1");

            deleteResources("/propTest");
            log.info("singleValuedProperties - Passed");
        } catch (RegistryException e) {

            log.error("singleValuedProperties RegistryException thrown :" + e.getMessage());
            Assert.fail("singleValuedProperties RegistryException thrown :" + e.getMessage());
        }


    }

    private void multiValuedProperties() {
        String path = "/propTest/r1";
        Resource r1 = registry.newResource();

        try {
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
            log.info("multiValuedProperties - Passed");
        } catch (RegistryException e) {

            log.error("multiValuedProperties RegistryException thrown :" + e.getMessage());
            Assert.fail("multiValuedProperties RegistryException thrown :" + e.getMessage());
        }

    }

    private void removingProperties() {
        String path = "/props/t1/r1";
        Resource r1 = registry.newResource();
        try {
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
            log.info("removingProperties - Passed");
        } catch (RegistryException e) {

            log.error("removingProperties RegistryException thrown :" + e.getMessage());
            Assert.fail("removingProperties RegistryException thrown :" + e.getMessage());
        }
    }

    private void removingMultivaluedProperties() {
        String path = "/props/t2/r1";
        Resource r1 = registry.newResource();
        try {
            r1.setContent("r1 content");

            r1.addProperty("p1", "v1");
            r1.addProperty("p1", "v2");
            registry.put(path, r1);

            Resource r1e1 = registry.get(path);
            r1e1.setContent("r1 content updated");
            r1e1.removePropertyValue("p1", "v1");
            registry.put(path, r1e1);

            Resource r1e2 = registry.get(path);
            assertFalse("Property is not removed.", r1e2.getPropertyValues("p1").contains("v1"));
            assertTrue("Wrong property is removed.", r1e2.getPropertyValues("p1").contains("v2"));

            deleteResources("/props");
            log.info("removingMultivaluedProperties - Passed");
        } catch (RegistryException e) {

            log.error("removingMultivaluedProperties RegistryException thrown :" + e.getMessage());
            Assert.fail("removingMultivaluedProperties RegistryException thrown :" + e.getMessage());

        }

    }

    private void editingMultivaluedProperties() {
        String path = "/props/t3/r1";
        Resource r1 = registry.newResource();
        try {
            r1.setContent("r1 content");

            r1.addProperty("p1", "v1");
            r1.addProperty("p1", "v2");
            r1.setProperty("test", "value2");
            r1.setProperty("test2", "value2");
            registry.put(path, r1);

            Resource r1e1 = registry.get(path);
            r1e1.setContent("r1 content");
            r1e1.editPropertyValue("p1", "v1", "v3");

            registry.put(path, r1e1);

            Resource r1e2 = registry.get(path);
            assertFalse("Property is not edited.", r1e2.getPropertyValues("p1").contains("v1"));
            assertTrue("Property is not edited.", r1e2.getPropertyValues("p1").contains("v3"));
            assertTrue("Wrong property is removed.", r1e2.getPropertyValues("p1").contains("v2"));

            deleteResources("/props");
            log.info("editingMultivaluedProperties- Passed");
        } catch (RegistryException e) {

            log.error("editingMultivaluedProperties RegistryException thrown :" + e.getMessage());
            Assert.fail("editingMultivaluedProperties RegistryException thrown :" + e.getMessage());
        }
    }
}
