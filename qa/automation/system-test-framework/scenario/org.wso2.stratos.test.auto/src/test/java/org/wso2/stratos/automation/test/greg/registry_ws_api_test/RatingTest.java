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

public class RatingTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RatingTest.class);
    private static WSRegistryServiceClient registry = null;


    @Override
    public void init() {
        testClassName = RatingTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API RatingTest Test Cases............................ ");
        addResourceRating();
        addCollectionRating();
        editResourceRating();
        ratingsPath();
        log.info("Completed Running WS-API RatingTest Test Cases................... ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
       deleteResources("/d16");
       deleteResources("/d61");
       deleteResources("/c1");

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


    private void addResourceRating() {
        String path = "/d16/d17/r1";
        Resource r1 = registry.newResource();
        byte[] r1content = "R1 content".getBytes();

        try {
            r1.setContent(r1content);
            registry.put(path, r1);
            registry.rateResource(path, 5);

            float rating = registry.getAverageRating(path);
            assertEquals("Rating of the resource /d16/d17/r1 should be 5.", rating, (float) 5.0,
                    (float) 0.01);
            deleteResources("/d16");
            log.info("addResourceRating - Passed");
        } catch (RegistryException e) {
            log.error("addResourceRating RegistryExceptio thrown:" + e.getMessage());
            Assert.fail("addResourceRating RegistryException thrown:" + e.getMessage());
        }

    }

    private void addCollectionRating() {
        String path = "/d16/d18";
        Resource r1 = registry.newCollection();

        try {
            registry.put(path, r1);
            registry.rateResource(path, 4);
            float rating = registry.getAverageRating(path);

            assertEquals("Rating of the resource /d16/d18 should be 5.", rating, (float) 4.0,
                    (float) 0.01);
            deleteResources("/d16");
            log.info("addCollectionRating - Passed");
        } catch (RegistryException e) {
            log.error("addCollectionRating RegistryExceptio thrown:" + e.getMessage());
            Assert.fail("addCollectionRating RegistryException thrown:" + e.getMessage());
        }

    }

    private void editResourceRating() {
        String path = "/d61/d17/d18/r1";
        Resource r1 = registry.newResource();
        byte[] r1content = "R1 content".getBytes();

        try {
            r1.setContent(r1content);
            registry.put(path, r1);
            registry.rateResource(path, 5);
            float rating = registry.getAverageRating(path);

            assertEquals("Rating of the resource /d61/d17/d18/r1 should be 5.", (float) 5.0, rating,
                    (float) 0.01);

            registry.rateResource(path, 3);
            float rating_edit = registry.getAverageRating(path);
            assertEquals("Rating of the resource /d61/d17/d18/r1 should be 3.", (float) 3.0, rating_edit,
                    (float) 0.01);

            deleteResources("/d61");
            log.info("editResourceRating - Passed");

        } catch (RegistryException e) {
            log.error("editResourceRating RegistryExceptio thrown:" + e.getMessage());
            Assert.fail("editResourceRating RegistryException thrown:" + e.getMessage());
        }
    }

    private void ratingsPath() {
        Resource r5 = registry.newResource();
        String r5Content = "this is r5 content";

        try {
            r5.setContent(r5Content.getBytes());
            r5.setDescription("production ready.");
            String r5Path = "/c1/r5";
            registry.put(r5Path, r5);
            registry.rateResource("/c1/r5", 3);
            String[] ratingPaths;
            Resource ratings = registry.get("/c1/r5;ratings");
            ratingPaths = (String[]) ratings.getContent();
            int rating;
            Resource c1 = registry.get(ratingPaths[0]);

            Object o = c1.getContent();
            if (o instanceof Integer) {
                rating = (Integer) o;
            } else {
                rating = Integer.parseInt(o.toString());
            }

            assertEquals("Ratings are not retrieved properly as resources.", rating, 3);
            deleteResources("/c1");
            log.info("ratingsPath - Passed");
        } catch (RegistryException e) {
            log.error("ratingsPath RegistryExceptio thrown:" + e.getMessage());
            Assert.fail("ratingsPath RegistryException thrown:" + e.getMessage());
        }
    }
}
