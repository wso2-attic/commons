package org.wso2.stratos.automation.test.greg.registry_ws_api_test;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.util.ArrayList;
import java.util.List;

public class ResourceHandling extends TestTemplate {
    private static final Log log = LogFactory.getLog(ResourceHandling.class);
    private static WSRegistryServiceClient registry = null;
    String password = null;

    @Override
    public void init() {
        testClassName = ResourceHandling.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        password = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId)).getTenantPassword();
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API ResourceHandling Test Cases............................ ");
        newCollection();
        resourceCopy();
        collectionCopy();
        collectionCopy();
        getResourceoperation();
        getCollectionoperation();
        log.info("Completed Running WS-API ResourceHandling Test Cases....................");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/f1012");
        deleteResources("/f95");
        deleteResources("/c9011");
        deleteResources("/c9011");
        deleteResources("/testk");
        deleteResources("/testk2");
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


    private void newCollection() {
        Collection collection = registry.newCollection();

        try {

            assertEquals("Invalid Child Count for new collection", 0, collection.getChildCount());

            assertNotNull("The children for a new collection cannot be null", collection.getChildren());
            assertEquals("Invalid Child Count for new collection", 0, collection.getChildren().length);

            registry.put("/f1012/col", collection);
            Collection collection_new = (Collection) registry.get("/f1012/col");

            assertEquals("Invalid Child Count for new collection", 0, collection_new.getChildCount());
            assertNotNull("The children for a new collection cannot be null",
                    collection_new.getChildren());
            assertEquals("Invalid Child Count for new collection", 0,
                    collection_new.getChildren().length);

            deleteResources("/f1012");
            log.info("newCollection -Passed");
        } catch (RegistryException e) {
            log.error("newCollection RegistryException thrown:" + e.getMessage());
            Assert.fail("newCollection RegistryException thrown:" + e.getMessage());
        }
    }


    private void resourceCopy() {

        try {
            String path = "/f95/f2/r1";
            String commentPath = path + RegistryConstants.URL_SEPARATOR + "comments";
            String new_path = "/f96/f2/r1";
            String commentPathNew = new_path + RegistryConstants.URL_SEPARATOR + "comments";
            Resource r1 = registry.newResource();
            r1.setDescription("This is a file to be renamed");
            byte[] r1content = "R2 content".getBytes();
            r1.setContent(r1content);
            r1.setMediaType("txt");

            Comment c1 = new Comment();
            c1.setResourcePath(path);
            c1.setText("This is a test comment1");

            Comment c2 = new Comment();
            c2.setResourcePath(path);
            c2.setText("This is a test comment2");

            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");

            registry.put(path, r1);
            registry.addComment(path, c1);
            registry.addComment(path, c2);
            registry.applyTag(path, "tag1");
            registry.applyTag(path, "tag2");
            registry.applyTag(path, "tag3");
            registry.rateResource(path, 4);

            Resource r2 = registry.get(path);

            assertEquals("Properties are not equal", r1.getProperty("key1"), r2.getProperty("key1"));
            assertEquals("Properties are not equal", r1.getProperty("key2"), r2.getProperty("key2"));
            assertEquals("File content is not matching", new String((byte[]) r1.getContent()),
                    new String((byte[]) r2.getContent()));
            assertTrue(c1.getText() + " is not associated for resource" + path,
                    containsComment(commentPath, c1.getText()));
            assertTrue(c2.getText() + " is not associated for resource" + path,
                    containsComment(commentPath, c2.getText()));
            assertTrue("Tag1 is not exist", containsTag(path, "tag1"));
            assertTrue("Tag2 is not exist", containsTag(path, "tag2"));
            assertTrue("Tag3 is not exist", containsTag(path, "tag3"));

            float rating = registry.getAverageRating(path);
            assertEquals("Rating is not mathching", rating, (float) 4.0, (float) 0.01);
            assertEquals("Media type not exist", r1.getMediaType(), r2.getMediaType());
//
            assertEquals("Description is not exist", r1.getDescription(), r2.getDescription());

            String new_path_returned;
            new_path_returned = registry.rename(path, new_path);

            assertEquals("New resource path is not equal", new_path, new_path_returned);


            Resource r1Renamed = registry.get(new_path);

            assertEquals("File content is not matching", new String((byte[]) r2.getContent()),
                    new String((byte[]) r1Renamed.getContent()));
            assertEquals("Properties are not equal", r2.getProperty("key1"),
                    r1Renamed.getProperty("key1"));
            assertEquals("Properties are not equal", r2.getProperty("key2"),
                    r1Renamed.getProperty("key2"));
            assertTrue(c1.getText() + " is not associated for resource" + new_path,
                    containsComment(commentPathNew, c1.getText()));
            assertTrue(c2.getText() + " is not associated for resource" + new_path,
                    containsComment(commentPathNew, c2.getText()));
            assertTrue("Tag1 is not copied", containsTag(new_path, "tag1"));
            assertTrue("Tag2 is not copied", containsTag(new_path, "tag2"));
            assertTrue("Tag3 is not copied", containsTag(new_path, "tag3"));

            float rating1 = registry.getAverageRating(new_path);
            assertEquals("Rating is not copied", rating1, (float) 4.0, (float) 0.01);
            assertEquals("Media type not copied", r2.getMediaType(), r1Renamed.getMediaType());
            assertEquals("Authour Name is not copied", r2.getAuthorUserName(),
                    r1Renamed.getAuthorUserName());
            assertEquals("Description is not exist", r2.getDescription(), r1Renamed.getDescription());

            deleteResources("/f95");
            deleteResources("/f96");
            log.info("resourceCopy -Passed");
        } catch (RegistryException e) {
            log.error("resourceCopy RegistryException thrown:" + e.getMessage());
            Assert.fail("resourceCopy RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("resourceCopy Exception thrown:" + e.getMessage());
            Assert.fail("resourceCopy Exception thrown:" + e.getMessage());
        }
    }

    private void collectionCopy() {

        try {

            String path = "/c9011/c1/c2";
            String commentPath = path + RegistryConstants.URL_SEPARATOR + "comments";
            String new_path = "/c9111/c1/c3";
            String commentPathNew = new_path + RegistryConstants.URL_SEPARATOR + "comments";
            Resource r1 = registry.newCollection();
            r1.setDescription("This is a file to be renamed");

            Comment c1 = new Comment();
            c1.setResourcePath(path);
            c1.setText("This is first test comment");

            Comment c2 = new Comment();
            c2.setResourcePath(path);
            c2.setText("This is secound test comment");

            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");

            registry.put(path, r1);
            registry.addComment(path, c1);
            registry.addComment(path, c2);
            registry.applyTag(path, "tag1");
            registry.applyTag(path, "tag2");
            registry.applyTag(path, "tag3");
            registry.rateResource(path, 4);

            Resource r2 = registry.get(path);

            assertEquals("Properties are not equal", r1.getProperty("key1"),
                    r2.getProperty("key1"));
            assertEquals("Properties are not equal", r1.getProperty("key2"),
                    r2.getProperty("key2"));
            assertTrue(c1.getText() + " is not associated for resource" + path,
                    containsComment(commentPath, c1.getText()));
            assertTrue(c2.getText() + " is not associated for resource" + path,
                    containsComment(commentPath, c2.getText()));
            assertTrue("Tag1 is not copied", containsTag(path, "tag1"));
            assertTrue("Tag2 is not copied", containsTag(path, "tag2"));
            assertTrue("Tag3 is not copied", containsTag(path, "tag3"));

            float rating = registry.getAverageRating(path);
            assertEquals("Rating is not mathching", rating, (float) 4.0, (float) 0.01);

            String new_path_returned;
            new_path_returned = registry.rename(path, new_path);

            assertEquals("New resource path is not equal", new_path, new_path_returned);

            /*get renamed resource details*/

            Resource r1Renamed = registry.get(new_path);

            assertEquals("Properties are not equal", r2.getProperty("key1"),
                    r1Renamed.getProperty("key1"));
            assertEquals("Properties are not equal", r2.getProperty("key2"),
                    r1Renamed.getProperty("key2"));
            assertTrue(c1.getText() + " is not associated for resource" + new_path,
                    containsComment(commentPathNew, c1.getText()));
            assertTrue(c2.getText() + " is not associated for resource" + new_path,
                    containsComment(commentPathNew, c2.getText()));
            assertTrue("Tag1 is not copied", containsTag(new_path, "tag1"));
            assertTrue("Tag2 is not copied", containsTag(new_path, "tag2"));
            assertTrue("Tag3 is not copied", containsTag(new_path, "tag3"));

            float rating1 = registry.getAverageRating(new_path);
            assertEquals("Rating is not copied", rating1, (float) 4.0, (float) 0.01);
            deleteResources("/c9011");
            deleteResources("/c9111");
            log.info("collectionCopy -Passed");
        } catch (RegistryException e) {
            log.error("collectionCopy RegistryException thrown:" + e.getMessage());
            Assert.fail("collectionCopy RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("collectionCopy Exception thrown:" + e.getMessage());
            Assert.fail("collectionCopy Exception thrown:" + e.getMessage());
        }
    }

    private void getResourceoperation() {

        Resource r2 = registry.newResource();
        String path = "/testk/testa/derby.log";
        try {
            r2.setContent(new String("this is the content").getBytes());

            r2.setDescription("this is test desc this is test desc this is test desc this is test desc this is test desc " +
                    "this is test desc this is test desc this is test desc this is test descthis is test desc ");
            r2.setMediaType("plain/text");
            registry.put(path, r2);

            r2.discard();

            Resource r3 = registry.newResource();
            r3 = registry.get(path);

            assertEquals("Author User names are not Equal", password, r3.getAuthorUserName());
            assertNotNull("Created time is null", r3.getCreatedTime());
            assertEquals("Author User names are not Equal", password, r3.getAuthorUserName());
            assertEquals("Description is not Equal", "this is test desc this is test desc this is test desc this is test" +
                    " desc this is test desc this is test desc this is test desc this is test desc this is test descthis is " +
                    "test desc ", r3.getDescription());
            assertNotNull("Get Id is null", r3.getId());
            assertNotNull("LastModifiedDate is null", r3.getLastModified());
            assertEquals("Last Updated names are not Equal", password, r3.getLastUpdaterUserName());
            //System.out.println(r3.getMediaType());
            assertEquals("Media Type is not equal", "plain/text", r3.getMediaType());
            assertEquals("parent Path is not equal", "/testk/testa", r3.getParentPath());
            assertEquals("parent Path is not equal", path, r3.getPath());
            assertEquals("Get stated wrong", 0, r3.getState());

            String st = r3.getPermanentPath();

            deleteResources("/testk");
            log.info("getResourceoperation -Passed");

        } catch (RegistryException e) {

            log.error("getResourceoperation RegistryException thrown:" + e.getMessage());
            Assert.fail("getResourceoperation RegistryException thrown:" + e.getMessage());


        }


    }

    private void getCollectionoperation() {

        Resource r2 = registry.newCollection();
        String path = "/testk2/testa/testc";
        r2.setDescription("this is test desc");
        r2.setProperty("test2", "value2");
        try {
            registry.put(path, r2);

            r2.discard();

            Resource r3 = registry.get(path);
            assertNotNull("Get Id is null", r3.getId());
            assertNotNull("LastModifiedDate is null", r3.getLastModified());
            assertEquals("Last Updated names are not Equal", password, r3.getLastUpdaterUserName());
            assertEquals("parent Path is not equal", "/testk2/testa", r3.getParentPath());
            assertEquals("Get stated wrong", 0, r3.getState());

            registry.createVersion(path);
            assertEquals("Permenent path doesn't contanin the string", "/testk2/testa", r3.getParentPath());
            assertEquals("Path doesn't contanin the string", path, r3.getPath());

            deleteResources("/testk2");
            log.info("getCollectionoperation - Passed");
        } catch (RegistryException e) {
            log.error("getCollectionoperation RegistryException thrown:" + e.getMessage());
            Assert.fail("getCollectionoperation RegistryException thrown:" + e.getMessage());
        }
    }


    private boolean containsComment(String pathValue, String commentText) throws Exception {

        Comment[] commentsArray = null;
        List commentTexts = new ArrayList();

        try {
            Resource commentsResource = registry.get(pathValue);
            commentsArray = (Comment[]) commentsResource.getContent();
            for (Comment comment : commentsArray) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(commentResource.getContent());
            }
        } catch (RegistryException e) {
            e.printStackTrace();
        }

        boolean found = false;
        if (commentTexts.contains(commentText)) {
            found = true;
        }

        return found;
    }

    private boolean containsTag(String tagPath, String tagText) throws Exception {

        Tag[] tags = null;

        try {
            tags = registry.getTags(tagPath);
        } catch (RegistryException e) {
            e.printStackTrace();
        }

        boolean tagFound = false;
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].getTagName().equals(tagText)) {
                tagFound = true;
                break;
            }
        }

        return tagFound;
    }

    private boolean containsString(String[] array, String value) {

        boolean found = false;
        for (String anArray : array) {
            if (anArray.startsWith(value)) {
                found = true;
                break;
            }
        }

        return found;
    }
}
