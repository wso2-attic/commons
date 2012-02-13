package org.wso2.stratos.automation.test.greg.registry_ws_api_test;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Comment;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;


import java.util.ArrayList;
import java.util.List;

public class CommentTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(CommentTest.class);
    private static WSRegistryServiceClient registry = null;
    private String username = null;
    private String password = null;
    String tenantId;

    @Override
    public void init() {
        testClassName = CommentTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);

        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        username = tenantDetails.getTenantName();
        password = tenantDetails.getTenantPassword();
        //delete existing resources
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API CommentTest Test Cases............................ ");
        try {
            AddComment();
            AddCommentToResource();
            AddCommentToCollection();
            AddCommenttoRoot();
            EditComment();
            CommentDelete();
            log.info("Completed Running WS-API CommentTest Test Cases............................ ");
        }
        catch (Exception e) {
            e.printStackTrace();
            fail("Comment Test Failed");
        }
    }


    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/d112");
        deleteResources("/d1");
        deleteResources("/d11");
        deleteResources("/c101");
        deleteResources("/c1d1");
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



    public void AddComment() throws Exception {
        Resource r1 = registry.newResource();
        String path = "/d112/r3";
        byte[] r1content = "R1 content".getBytes();
        r1.setContent(r1content);
        registry.put(path, r1);

        String comment1 = "this is qa comment 4";
        String comment2 = "this is qa comment 5";
        Comment c1 = new Comment();
        c1.setResourcePath(path);
        c1.setText("This is default comment");

        c1.setUser(username);

        registry.addComment(path, c1);
        registry.addComment(path, new Comment(comment1));
        registry.addComment(path, new Comment(comment2));

        Comment[] comments = registry.getComments(path);

        boolean commentFound = false;

        for (Comment comment : comments) {
            if (comment.getText().equals(comment1)) {
                commentFound = true;

                assertEquals(comment1, comment.getText());
                assertEquals(password, comment.getUser());

                assertEquals(path, comment.getResourcePath());

            }

            if (comment.getText().equals(comment2)) {
                commentFound = true;
                assertEquals(comment2, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals(path, comment.getResourcePath());


            }

            if (comment.getText().equals("This is default comment")) {
                commentFound = true;
                assertEquals("This is default comment", comment.getText());
            }
        }


        assertTrue("No comment is associated with the resource" + path, commentFound);

        Resource commentsResource = registry.get("/d112/r3;comments");

        assertTrue("Comment collection resource should be a directory.",
                commentsResource instanceof Collection);

        comments = (Comment[]) commentsResource.getContent();


        List<String> commentTexts = new ArrayList<String>();
        for (Comment comment : comments) {

            Resource commentResource = registry.get(comment.getPath());

            commentTexts.add(commentResource.getContent().toString());
        }


        assertTrue(comment1 + " is not associated with the resource /d112/r3.",
                commentTexts.contains(comment1));
        assertTrue(comment2 + " is not associated with the resource /d112/r3.",
                commentTexts.contains(comment2));


        //delete Resource
        deleteResources("/d112");

        //assert resource has been properly deleted
        assertFalse(path + "has not been deleted properly", registry.resourceExists(path));

//        System.out.println("AddComment - Passed");

        log.info("AddComment - Passed");

    }

    public void AddCommentToResource() throws Exception {
        String path = "/d1/r3";
        Resource r1 = registry.newResource();
        byte[] r1content = "R1 content".getBytes();
        r1.setContent(r1content);
        registry.put(path, r1);

        String comment1 = "this is qa comment 4";
        String comment2 = "this is qa comment 5";
        Comment c1 = new Comment();
        c1.setResourcePath(path);
        c1.setText("This is default comment");
        c1.setUser(username);

        registry.addComment(path, c1);
        registry.addComment(path, new Comment(comment1));
        registry.addComment(path, new Comment(comment2));

        Comment[] comments = registry.getComments("/d1/r3");

        boolean commentFound = false;

        for (Comment comment : comments) {
            if (comment.getText().equals(comment1)) {
                commentFound = true;

                assertEquals(comment1, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals(path, comment.getResourcePath());

            }

            if (comment.getText().equals(comment2)) {
                commentFound = true;

                assertEquals(comment2, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals(path, comment.getResourcePath());

            }

            if (comment.getText().equals("This is default comment")) {
                commentFound = true;

                assertEquals("This is default comment", comment.getText());
            }
        }


        assertTrue("comment '" + comment1 +
                " is not associated with the artifact /d1/r3", commentFound);

        Resource commentsResource = registry.get("/d1/r3;comments");
        assertTrue("Comment collection resource should be a directory.",
                commentsResource instanceof Collection);
        comments = (Comment[]) commentsResource.getContent();

        List<Object> commentTexts = new ArrayList<Object>();
        for (Comment comment : comments) {
            Resource commentResource = registry.get(comment.getPath());
            commentTexts.add(commentResource.getContent());
        }

        assertTrue(comment1 + " is not associated for resource /d1/r3.",
                commentTexts.contains(comment1));
        assertTrue(comment2 + " is not associated for resource /d1/r3.",
                commentTexts.contains(comment2));


        //delete Resource
        deleteResources("/d1");

        //assert resource has been properly deleted
        assertFalse(path + "has not been deleted properly", registry.resourceExists(path));

        log.info("AddCommentToResource - passed");
    }

    public void AddCommentToCollection() throws Exception {
        String path = "/d11/d12";

        Resource r1 = registry.newCollection();
        r1.setDescription("this is a collection to add comment");

        registry.put(path, r1);

        String comment1 = "this is qa comment 1 for collection d12";
        String comment2 = "this is qa comment 2 for collection d12";

        Comment c1 = new Comment();
        c1.setResourcePath(path);
        c1.setText("This is default comment for d12");
        c1.setUser(username);

        try {
            registry.addComment(path, c1);
            registry.addComment(path, new Comment(comment1));
            registry.addComment(path, new Comment(comment2));
        } catch (RegistryException e) {
            fail("Valid commenting for resources scenario failed");
        }

        Comment[] comments = null;

        try {

            comments = registry.getComments(path);
        } catch (RegistryException e) {
            fail("Failed to get comments for the resource /d11/d12");
        }

        boolean commentFound = false;

        for (Comment comment : comments) {
            if (comment.getText().equals(comment1)) {
                commentFound = true;

                assertEquals(comment1, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals(path, comment.getResourcePath());
            }

            if (comment.getText().equals(comment2)) {
                commentFound = true;

                assertEquals(comment2, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals(path, comment.getResourcePath());
            }

            if (comment.getText().equals(c1.getText())) {
                commentFound = true;

                assertEquals("This is default comment for d12", comment.getText());
            }
        }

        assertTrue("comment '" + comment1 +
                " is not associated with the artifact /d11/d12", commentFound);

        try {

            Resource commentsResource = registry.get("/d11/d12;comments");
            assertTrue("Comment collection resource should be a directory.",
                    commentsResource instanceof Collection);
            comments = (Comment[]) commentsResource.getContent();

            List commentTexts = new ArrayList();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(commentResource.getContent());
            }

            assertTrue(comment1 + " is not associated for resource /d11/d12.",
                    commentTexts.contains(comment1));
            assertTrue(comment2 + " is not associated for resource /d11/d12.",
                    commentTexts.contains(comment2));

        } catch (RegistryException e) {
            e.printStackTrace();
            fail("Failed to get comments form URL: /d11/d12;comments");
        }


        //Delete Resource
        deleteResources("/d11");

        //Assert resource has been properly deleted
        assertFalse(path + "Resource Deleted", registry.resourceExists(path));

        log.info("AddCommentToCollection - Passed");
    }

    public void AddCommenttoRoot() {
        String comment1 = "this is qa comment 1 for root";
        String comment2 = "this is qa comment 2 for root";

        Comment c1 = new Comment();
        c1.setResourcePath("/");
        c1.setText("This is default comment for root");
        c1.setUser(username);

        try {
            registry.addComment("/", c1);
            registry.addComment("/", new Comment(comment1));
            registry.addComment("/", new Comment(comment2));
        } catch (RegistryException e) {
            fail("Valid commenting for resources scenario failed");
        }

        Comment[] comments = null;

        try {

            comments = registry.getComments("/");
        } catch (RegistryException e) {
            fail("Failed to get comments for the resource /");
        }

        boolean commentFound = false;

        for (Comment comment : comments) {
            if (comment.getText().equals(comment1)) {
                commentFound = true;

                assertEquals(comment1, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals("/", comment.getResourcePath());

            }

            if (comment.getText().equals(comment2)) {
                commentFound = true;

                assertEquals(comment2, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals("/", comment.getResourcePath());
            }

            if (comment.getText().equals(c1.getText())) {
                commentFound = true;

                assertEquals("This is default comment for root", comment.getText());


            }
        }

        assertTrue("comment '" + comment1 +
                " is not associated with the artifact /", commentFound);

        try {

            Resource commentsResource = registry.get("/;comments");
            assertTrue("Comment collection resource should be a directory.",
                    commentsResource instanceof Collection);
            comments = (Comment[]) commentsResource.getContent();

            List commentTexts = new ArrayList();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(commentResource.getContent());
            }

            assertTrue(comment1 + " is not associated for resource /.",
                    commentTexts.contains(comment1));
            assertTrue(comment2 + " is not associated for resource /.",
                    commentTexts.contains(comment2));

            //Remove comments added to root
            for (int i = 0; i < comments.length; i++) {
                registry.removeComment(comments[i].getPath());
            }
//            registry.removeComment(comments[0].getPath());
//            registry.removeComment(comments[1].getPath());
//            registry.removeComment(comments[2].getPath());


        } catch (RegistryException e) {
            fail("Failed to get comments form URL: /;comments");
        }

        log.info("AddCommenttoRoot - passed");
    }

    public void EditComment() throws Exception {
        String path = "/c101/c11/r1";
        Resource r1 = registry.newResource();
        byte[] r1content = "R1 content".getBytes();
        r1.setContent(r1content);
        r1.setDescription("this is a resource to edit comment");
        registry.put(path, r1);

        Comment c1 = new Comment();
        c1.setResourcePath(path);
        c1.setText("This is default comment " + path);
        c1.setUser(username);

        registry.addComment(path, c1);

        Comment[] comments = registry.getComments(path);

        boolean commentFound = false;

        for (Comment comment : comments) {
            if (comment.getText().equals(c1.getText())) {
                commentFound = true;

                assertEquals("This is default comment " + path, comment.getText());
                assertEquals(password, comment.getUser());
                assertEquals(path, comment.getResourcePath());
            }
        }

        assertTrue("comment:" + c1.getText() +
                " is not associated with the artifact /c101/c11/r1", commentFound);

        try {

            Resource commentsResource = registry.get("/c101/c11/r1;comments");

            assertTrue("Comment resource should be a directory.",
                    commentsResource instanceof Collection);
            comments = (Comment[]) commentsResource.getContent();

            List<Object> commentTexts = new ArrayList<Object>();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(commentResource.getContent());
            }

            assertTrue(c1.getText() + " is not associated for resource /c101/c11/r1.",
                    commentTexts.contains(c1.getText()));
            registry.editComment(comments[0].getPath(), "This is the edited comment");
            comments = registry.getComments(path);

            Resource resource = registry.get(comments[0].getPath());
            assertEquals("This is the edited comment", resource.getContent());

            /*Edit comment goes here*/

            registry.editComment(path, "This is the edited comment");

            //Delete resource
            deleteResources("/c101");

            //Assert resource has been properly deleted
            assertFalse("Resource Deleted:", registry.resourceExists(path));

            log.info("EditComment -Passed");
        } catch (RegistryException e) {
            e.printStackTrace();
            fail("Failed to get comments form URL:/c101/c11/r1;comments");
        }


    }

    public void CommentDelete() {
        String r1Path = "/c1d1/c1";
        Collection r1 = registry.newCollection();
        try {
            registry.put(r1Path, r1);

            String c1Path = registry.addComment(r1Path, new Comment("test comment1"));
            registry.addComment(r1Path, new Comment("test comment2"));

            Comment[] comments1 = registry.getComments(r1Path);

            assertEquals("There should be two comments.", comments1.length, 2);

            String[] cTexts1 = {comments1[0].getText(), comments1[1].getText()};

            assertTrue("comment is missing", containsString(cTexts1, "test comment1"));
            assertTrue("comment is missing", containsString(cTexts1, "test comment2"));

            registry.delete(c1Path);

            Comment[] comments2 = registry.getComments(r1Path);

            assertEquals("There should be one comment.", 1, comments2.length);

            String[] cTexts2 = {comments2[0].getText()};

            assertTrue("comment is missing", containsString(cTexts2, "test comment2"));
            assertTrue("deleted comment still exists", !containsString(cTexts2, "test comment1"));

            //Delete Resource
            registry.delete("/c1d1");

            // assert Resource properly deleted
            assertFalse("Resource has been properly deleted:", registry.resourceExists(r1Path));

            log.info("CommentDelete _ Passed");

        } catch (RegistryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


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
