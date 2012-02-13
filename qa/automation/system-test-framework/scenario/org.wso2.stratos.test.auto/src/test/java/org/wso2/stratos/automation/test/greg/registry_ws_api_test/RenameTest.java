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

public class RenameTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RenameTest.class);
    private static WSRegistryServiceClient registry = null;


    @Override
    public void init() {
        testClassName = RenameTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API RenameTest Test Cases............................ ");
        rootLevelResourceRename();
        generalResourceRename();
        rootLevelCollectionRename();
        generalCollectionRename();
        log.info("Completed Running WS-API RenameTest Test Cases................... ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/rename2");
        deleteResources("/rename4");
        deleteResources("/tests");
        deleteResources("/rename34k");
        deleteResources("/rename44k");
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

    private void rootLevelResourceRename() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "rename");

        try {
            r1.setContent("some text");
            registry.put("/rename2", r1);
            registry.rename("/rename2", "/rename4");

            assertFalse("Resource should not be accessible from the old path after renaming.", registry.resourceExists("/rename2"));

            Resource newR1 = registry.get("/rename4");

            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");

            deleteResources("/rename4");
            log.info("rootLevelResourceRename - Passed");
        } catch (RegistryException e) {
            log.error("rootLevelResourceRename RegistryException thrown :" + e.getMessage());
            Assert.fail("rootLevelResourceRename RegistryException thrown :" + e.getMessage());
        }

    }

    private void generalResourceRename() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "rename");

        try {
            r1.setContent("some text");
            registry.put("/tests/rename1", r1);
            registry.rename("/tests/rename1", "rename2");
            assertFalse("Resource should not be accessible from the old path after renaming.", registry.resourceExists("/test/rename1"));

            Resource newR1 = registry.get("/tests/rename2");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");
            deleteResources("/tests");
            log.info("generalResourceRename- Passed");
        } catch (RegistryException e) {
            log.error("generalResourceRename RegistryException thrown :" + e.getMessage());
            Assert.fail("generalResourceRename RegistryException thrown :" + e.getMessage());
        }
    }

    private void rootLevelCollectionRename() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "rename");

        try {
            r1.setContent("some text");
            registry.put("/rename34k/c1/dummy", r1);
            registry.rename("/rename34k", "/rename44k");

            assertFalse("Resource should not be " +
                    "accessible from the old path after renaming the parent.", registry.resourceExists("/rename34k/c1/dummy"));
            Resource newR1 = registry.get("/rename44k/c1/dummy");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");

            deleteResources("/rename44k");
            log.info("rootLevelCollectionRename - Passed");
        } catch (RegistryException e) {
            log.error("rootLevelCollectionRename RegistryException thrown :" + e.getMessage());
            Assert.fail("rootLevelCollectionRename RegistryException thrown :" + e.getMessage());
        }
    }

    private void generalCollectionRename() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "rename");

        try {
            r1.setContent("some text");
            registry.put("/c2/rename3/c1/dummy", r1);
            registry.rename("/c2/rename3", "rename4");

            assertFalse("Resource should not be " +
                    "accessible from the old path after renaming the parent.", registry.resourceExists("/c2/rename3/c1/dummy"));
            Resource newR1 = registry.get("/c2/rename4/c1/dummy");
            assertEquals("Resource should contain a property with name test and value rename.",
                    newR1.getProperty("test"), "rename");

            deleteResources("/c2");
            log.info("generalCollectionRename - Passed");
        } catch (RegistryException e) {
            log.error("generalCollectionRename RegistryException thrown :" + e.getMessage());
            Assert.fail("generalCollectionRename RegistryException thrown :" + e.getMessage());
        }
    }
}
