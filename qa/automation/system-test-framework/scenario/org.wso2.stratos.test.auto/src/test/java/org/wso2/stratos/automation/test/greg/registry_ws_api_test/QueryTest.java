package org.wso2.stratos.automation.test.greg.registry_ws_api_test;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.util.HashMap;
import java.util.Map;

public class QueryTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(QueryTest.class);
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
        log.info("Running WS-API QueryTest Test Cases............................ ");
        putRegistryQueries();
        executeQueries();
        log.info("Completed Running WS-API QueryTest Test Cases.................. ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/Queries1");
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

    private void putRegistryQueries() {
        String QUERY_EPR_BY_PATH = "/Queries1/EPRByPath";
        Resource resource1 = null;
        Resource r1 = null;
        String sql = "";

        try {
            resource1 = registry.newResource();
            sql = "SELECT PATH FROM REG_RESOURCE WHERE  REG_PATH LIKE ?";
            resource1.setContent(sql);
            resource1.setMediaType(RegistryConstants.SQL_QUERY_MEDIA_TYPE);
            resource1.addProperty(RegistryConstants.RESULT_TYPE_PROPERTY_NAME,
                    RegistryConstants.RESOURCES_RESULT_TYPE);

            boolean exists = registry.resourceExists(QUERY_EPR_BY_PATH);

            if (!exists)
                registry.put(QUERY_EPR_BY_PATH, resource1);

            assertTrue("Resource doesn't exists", registry.resourceExists(QUERY_EPR_BY_PATH));

            r1 = registry.get(QUERY_EPR_BY_PATH);

            assertEquals("File content is not matching", sql, new String((byte[]) r1.getContent()));
            assertEquals("Media type doesn't match", RegistryConstants.SQL_QUERY_MEDIA_TYPE, r1.getMediaType());
            assertEquals("Media type doesn't match", RegistryConstants.SQL_QUERY_MEDIA_TYPE, "application/vnd.sql.query");

            deleteResources(QUERY_EPR_BY_PATH);
            log.info("putRegistryQueries - Passed");
        } catch (Exception e) {
            log.error("putRegistryQueries Exception thrown :" + e.getMessage());
            Assert.fail("putRegistryQueries Exception thrown :" + e.getMessage());
        }

    }

    private void storeSQLQuery(String path) {
        String sql1 = "SELECT REG_PATH_ID, REG_NAME FROM REG_RESOURCE R WHERE R.REG_MEDIA_TYPE LIKE ?";
        Resource q1 = registry.newResource();
        try {
            q1.setContent(sql1);
            q1.setMediaType(RegistryConstants.SQL_QUERY_MEDIA_TYPE);
            q1.addProperty(RegistryConstants.RESULT_TYPE_PROPERTY_NAME,
                    RegistryConstants.RESOURCES_RESULT_TYPE);
            registry.put(path, q1);

        } catch (RegistryException e) {
            log.error("storeSQLQuery RegistryException thrown :" + e.getMessage());
            Assert.fail("storeSQLQuery RegistryException thrown :" + e.getMessage());
        }

    }

    private void executeQueries() {
        String QUERY_EPR_BY_PATH = "/Queries1/EPRByPath-new";
        Resource resource1 = null;

        try {
            storeSQLQuery(QUERY_EPR_BY_PATH);
            assertTrue("Resource doesn't exists", registry.resourceExists(QUERY_EPR_BY_PATH));
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("1", RegistryConstants.SQL_QUERY_MEDIA_TYPE); // media type
            Collection collection = registry.executeQuery(QUERY_EPR_BY_PATH, parameters);
            String[] children = collection.getChildren();

            boolean successful = false;
            for (String path : children) {
                if (path.contains(QUERY_EPR_BY_PATH)) successful = true;
            }
            assertTrue(successful);

            deleteResources("/Queries1");
            log.info("executeQueries - Passed");
        } catch (Exception e) {
            log.error("executeQueries Exception thrown :" + e.getMessage());
            Assert.fail("executeQueries Exception thrown :" + e.getMessage());
        }

    }
}
