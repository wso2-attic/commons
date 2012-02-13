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

public class TestCopy extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestCopy.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = TestCopy.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API TestCopy Test Cases............................ ");
        resourceCopy();
        collectionCopy();
        log.info("Completed Running WS-API TestCopy Test Cases............................ ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
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


    private void resourceCopy() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "copy");
        try {
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
            log.info("resourceCopy - Passed");
        } catch (RegistryException e) {
            log.error("resourceCopy fault thrown :" + e.getMessage());
            Assert.fail("resourceCopy fault thrown :" + e.getMessage());
        }
    }

    private void collectionCopy() {
        Resource r1 = registry.newResource();
        r1.setProperty("test", "copy");

        try {
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
            log.info("collectionCopy - Passed");
        } catch (RegistryException e) {
            log.error("collectionCopy fault thrown :" + e.getMessage());
            Assert.fail("collectionCopy fault thrown :" + e.getMessage());
        }
    }
}
