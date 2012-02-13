package org.wso2.stratos.automation.test.greg.registry_ws_api_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.registry.ws.client.resource.OnDemandContentResourceImpl;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;


public class OnDemandContentTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(OnDemandContentTest.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = OnDemandContentTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API OnDemandContentTest Test Cases............................ ");
        onDemandContent();
        log.info("Completed Running WS-API OnDemandContentTest Test Cases.................. ");
    }

    @Override
    public void cleanup() {

    }

     private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/ondemand");
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

    private void onDemandContent() {
        try {
            String testPath = "/ondemand/test";
            Resource r1 = registry.newResource();
            r1.setContent("This is test content. It should not be loaded unless getContent() is called.".getBytes());
            registry.put(testPath, r1);

            OnDemandContentResourceImpl r1_get = (OnDemandContentResourceImpl) registry.get(testPath);
            r1_get.setClient(null);
            Object content;
            try {
                content = r1_get.getContent();
                assertNull("Resource content should not exist", content);
                fail("Content has not been pre-fetched, not on demand");
            } catch (Exception ignored) {

            }

            Resource r1_get2 = registry.get(testPath);
            content = r1_get2.getContent();
            assertNotNull("Resource content should be fetched on demand", content);
            deleteResources("/ondemand");
            log.info("onDemandContent - Passed");
        } catch (Exception e) {
            log.error("onDemandContent Exception thrown:" + e.getMessage());
            Assert.fail("onDemandContent Exception thrown:" + e.getMessage());
        }
    }
}
