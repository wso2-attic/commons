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

public class TestPaths extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestPaths.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = TestPaths.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API TestPaths Test Cases............................ ");
        getOnPaths();
        putOnPaths();
        log.info("Completed Running WS-API TestPaths Test Cases....................");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/testkrishantha");
        deleteResources("/testkrishantha1");
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


    private void getOnPaths() {
        Resource r1 = registry.newResource();
        try {
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
            log.info("getOnPaths - Passed");
        } catch (RegistryException e) {
            log.error("getOnPaths RegistryException thrown:" + e.getMessage());
            Assert.fail("getOnPaths RegistryException thrown:" + e.getMessage());
        }
    }

    private void putOnPaths() {
        Resource r1 = registry.newResource();

        try {
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
            log.info("putOnPaths - Passed");
        } catch (RegistryException e) {
            log.error("putOnPaths RegistryException thrown:" + e.getMessage());
            Assert.fail("putOnPaths RegistryException thrown:" + e.getMessage());
        }
    }
}
