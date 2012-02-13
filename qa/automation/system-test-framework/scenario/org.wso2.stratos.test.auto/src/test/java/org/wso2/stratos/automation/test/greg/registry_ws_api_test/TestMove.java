package org.wso2.stratos.automation.test.greg.registry_ws_api_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

public class TestMove extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestMove.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = TestMove.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API TestMove Test Cases............................ ");
        resourceMoveFromRoot();
        resourceMoveToRoot();
        generalResourceMove();
        generalCollectionMove();
        log.info("Completed Running WS-API TestMove Test Cases............................ ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/move1");
        deleteResources("/test");
        deleteResources("/move2");
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

    private void resourceMoveFromRoot() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "move");
        try {
            r1.setContent("c");

            registry.put("/move1", r1);

            Collection c1 = registry.newCollection();
            registry.put("/test/move", c1);
            registry.move("/move1", "/test/move/move1");

            Resource newR1 = registry.get("/test/move/move1");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            assertFalse("Moved resource should not be accessible from the old path.", registry.resourceExists("/move1"));
            deleteResources("/test");
            log.info("resourceMoveFromRoot - Passed");
        } catch (RegistryException e) {
            log.error("resourceMoveFromRoot RegistryException thrown:" + e.getMessage());
            Assert.fail("resourceMoveFromRoot RegistryException thrown:" + e.getMessage());
        }
    }

    private void resourceMoveToRoot() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "move");
        try {
            r1.setContent("c");

            registry.put("/test/move/move2", r1);
            registry.move("/test/move/move2", "/move2");

            Resource newR1 = registry.get("/move2");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            assertFalse("Moved resource should not be accessible from the old path.", registry.resourceExists("/test/move/move2"));
            deleteResources("/move2");
            log.info("resourceMoveToRoot - Passed");
        } catch (RegistryException e) {
            log.error("resourceMoveToRoot RegistryException thrown:" + e.getMessage());
            Assert.fail("resourceMoveToRoott RegistryException thrown:" + e.getMessage());
        }
    }

    private void generalResourceMove() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "move");

        try {
            r1.setContent("c");
            registry.put("/test/c1/move/move3", r1);
            Collection c2 = registry.newCollection();
            registry.put("/test/c2/move", c2);
            registry.move("/test/c1/move/move3", "/test/c2/move/move3");

            Resource newR1 = registry.get("/test/c2/move/move3");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            assertFalse("Moved resource should not be accessible from the old path.", registry.resourceExists("/test/c1/move/move3"));
            deleteResources("/test");
            log.info("generalResourceMove - Passed");
        } catch (RegistryException e) {
            log.error("generalResourceMove RegistryException thrown:" + e.getMessage());
            Assert.fail("generalResourceMove RegistryException thrown:" + e.getMessage());
        }
    }

    private void generalCollectionMove() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "move");

        try {
            r1.setContent("c");
            registry.put("/test/c1/move5/move/dummy", r1);
            Collection c2 = registry.newCollection();
            registry.put("/test/c3", c2);
            registry.move("/test/c1/move5", "/test/c3/move5");
            Resource newR1 = registry.get("/test/c3/move5/move/dummy");
            assertEquals("Moved resource should have a property named 'test' with value 'move'.",
                    newR1.getProperty("test"), "move");

            assertFalse("Moved resource should not be accessible from the old path.", registry.resourceExists("/test/c1/move5/move/dummy"));
            deleteResources("/test");
            log.info("generalCollectionMove - Passed");
        } catch (RegistryException e) {
            log.error("generalCollectionMove RegistryException thrown:" + e.getMessage());
            Assert.fail("generalCollectionMove RegistryException thrown:" + e.getMessage());
        }
    }
}
