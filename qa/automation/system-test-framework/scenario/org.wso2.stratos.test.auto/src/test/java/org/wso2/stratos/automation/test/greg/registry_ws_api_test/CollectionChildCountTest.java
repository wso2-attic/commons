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

public class CollectionChildCountTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(CollectionChildCountTest.class);
    private static WSRegistryServiceClient registry = null;


    @Override
    public void init() {
        testClassName = CollectionChildCountTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API CollectionChildCountTest Test Cases............................ ");
        getChildCountForCollection();
        log.info("Completed Running WS-API CollectionChildCountTest Test Cases....................");
    }

    @Override
    public void cleanup() {
    }


    public void getChildCountForCollection() {
        String path = "/";
        Resource resource = null;
        try {
            resource = registry.get(path);
            assertTrue("resource is not a collection", (resource instanceof Collection));
            Collection collection = (Collection) resource;
            log.info("Collection Child count is=" + collection.getChildCount());
            assertTrue("Child count is " + collection.getChildCount(), true);
            log.info("getChildCountForCollection  - Passed");
        } catch (RegistryException e) {
            log.error("getChildCountForCollection RegistryException thrown :" + e.getMessage());
            Assert.fail("getChildCountForCollection RegistryException thrown :" + e.getMessage());
        }

    }
}
