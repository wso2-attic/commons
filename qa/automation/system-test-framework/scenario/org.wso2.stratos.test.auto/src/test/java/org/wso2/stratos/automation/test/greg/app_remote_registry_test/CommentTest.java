/*
* Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.stratos.automation.test.greg.app_remote_registry_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Comment;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(CommentTest.class);
    public RemoteRegistry registry;
    String username;


    @Override
    public void init() {
        testClassName = CommentTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        //Tenant Details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        username = tenantDetails.getTenantName();
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - CommentTest Test ..........................");
        AddComment();
        AddCommentToResource();
        AddCommentToCollection();
        AddCommenttoRoot();
        EditComment();
        CommentDelete();
        log.info("Completed Running Registry API - CommentTest Test .................");
    }


    @Override
    public void cleanup() {

    }

    private void removeResource() {
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
//            assertTrue("If Resource does not exsits : ", e.getMessage().indexOf("Response Status: 404, Response Type: CLIENT_ERROR") >= 0);
            log.error("deleteResources RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteResources RegistryException thrown:" + e.getMessage());
        }

    }

    public void AddComment() {
        Resource r1;
        try {
            r1 = registry.newResource();
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
                    assertEquals(username, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }

                if (comment.getText().equals(comment2)) {
                    commentFound = true;
                    assertEquals(comment2, comment.getText());
                    assertEquals(username, comment.getUser());
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

            //delete Resource
            deleteResources("/d112");
            //assert resource has been properly deleted
            assertFalse(path + "has not been deleted properly", registry.resourceExists(path));
            log.info("AddComment - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddComment RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddComment RegistryException thrown :" + e.getMessage());
        }

    }

    public void AddCommentToResource() {
        String path = "/d1/r3";
        Resource r1;
        try {
            r1 = registry.newResource();
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
                    assertEquals(username, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }

                if (comment.getText().equals(comment2)) {
                    commentFound = true;
                    assertEquals(comment2, comment.getText());
                    assertEquals(username, comment.getUser());
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

            List commentTexts = new ArrayList();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(new String((byte[]) commentResource.getContent()));
            }

            assertTrue(comment1 + " is not associated for resource /d1/r3.",
                    commentTexts.contains(comment1));

            assertTrue(comment2 + " is not associated for resource /d1/r3.",
                    commentTexts.contains(comment2));
            deleteResources("/d1");
            log.info("AddCommentToResource - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddCommentToResource RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddCommentToResource RegistryException thrown :" + e.getMessage());
        }


    }

    public void AddCommentToCollection() {
        String path = "/d11/d12";
        Resource r1;
        try {
            r1 = registry.newCollection();
            r1.setDescription("this is a collection to add comment");
            registry.put(path, r1);

            String comment1 = "this is qa comment 1 for collection d12";
            String comment2 = "this is qa comment 2 for collection d12";

            Comment c1 = new Comment();
            c1.setResourcePath(path);
            c1.setText("This is default comment for d12");
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
                    assertEquals(username, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }

                if (comment.getText().equals(comment2)) {
                    commentFound = true;
                    assertEquals(comment2, comment.getText());
                    assertEquals(username, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }

                if (comment.getText().equals(c1.getText())) {
                    commentFound = true;
                    assertEquals("This is default comment for d12", comment.getText());
                }
            }
            assertTrue("comment '" + comment1 +
                    " is not associated with the artifact /d11/d12", commentFound);

            Resource commentsResource = registry.get("/d11/d12;comments");
            assertTrue("Comment collection resource should be a directory.",
                    commentsResource instanceof Collection);
            comments = (Comment[]) commentsResource.getContent();

            List commentTexts = new ArrayList();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(new String((byte[]) commentResource.getContent()));
            }

            assertTrue(comment1 + " is not associated for resource /d11/d12.",
                    commentTexts.contains(comment1));
            assertTrue(comment2 + " is not associated for resource /d11/d12.",
                    commentTexts.contains(comment2));
            deleteResources("/d11");
            log.info("AddCommentToCollection - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddCommentToCollection RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddCommentToCollection RegistryException thrown :" + e.getMessage());
        }
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

            Comment[] comments;
            comments = registry.getComments("/");
            boolean commentFound = false;

            for (Comment comment : comments) {
                if (comment.getText().equals(comment1)) {
                    commentFound = true;
                    assertEquals(comment1, comment.getText());
                    assertEquals(username, comment.getUser());
                    assertEquals("/", comment.getResourcePath());
                }

                if (comment.getText().equals(comment2)) {
                    commentFound = true;
                    assertEquals(comment2, comment.getText());
                    assertEquals(username, comment.getUser());
                    assertEquals("/", comment.getResourcePath());
                }

                if (comment.getText().equals(c1.getText())) {
                    commentFound = true;
                    assertEquals("This is default comment for root", comment.getText());
                }
            }
            assertTrue("comment '" + comment1 +
                    " is not associated with the artifact /", commentFound);


            Resource commentsResource = registry.get("/;comments");
            assertTrue("Comment collection resource should be a directory.",
                    commentsResource instanceof Collection);
            comments = (Comment[]) commentsResource.getContent();

            List commentTexts = new ArrayList();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(new String((byte[]) commentResource.getContent()));
            }

            assertTrue(comment1 + " is not associated for resource /.",
                    commentTexts.contains(comment1));
            assertTrue(comment2 + " is not associated for resource /.",
                    commentTexts.contains(comment2));

            for (int i = 0; i < comments.length; i++) {
                registry.removeComment(comments[i].getPath());
            }
            log.info("AddCommenttoRoot - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddCommenttoRoot RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddCommenttoRoot RegistryException thrown :" + e.getMessage());
        }

    }

    public void EditComment() {
        String path = "/c101/c11/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "R1 content".getBytes();
            r1.setContent(r1content);
            r1.setDescription("this is a resource to edit comment");
            registry.put(path, r1);

            Comment c1 = new Comment();
            c1.setResourcePath(path);
            c1.setText("This is default comment");
            c1.setUser(username);

            registry.addComment(path, c1);
            Comment[] comments = registry.getComments(path);
            boolean commentFound = false;

            for (Comment comment : comments) {
                if (comment.getText().equals(c1.getText())) {
                    commentFound = true;
                    assertEquals("This is default comment", comment.getText());
                }
            }

            assertTrue("comment:" + c1.getText() +
                    " is not associated with the artifact /c101/c11/r1", commentFound);

            Resource commentsResource = registry.get("/c101/c11/r1;comments");
            assertTrue("Comment resource should be a directory.",
                    commentsResource instanceof Collection);
            comments = (Comment[]) commentsResource.getContent();

            List commentTexts = new ArrayList();
            for (Comment comment : comments) {
                Resource commentResource = registry.get(comment.getPath());
                commentTexts.add(new String((byte[]) commentResource.getContent()));
            }

            assertTrue(c1.getText() + " is not associated for resource /c101/c11/r1.",
                    commentTexts.contains(c1.getText()));
            registry.editComment(comments[0].getPath(), "This is the edited comment");
            comments = registry.getComments(path);
            Resource resource = registry.get(comments[0].getPath());
            assertEquals("This is the edited comment", new String((byte[]) resource.getContent()));
            registry.editComment("/c101/c11/r1", "This is the edited comment");
            deleteResources("/c101");
            log.info("EditComment - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -EditComment RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -EditComment RegistryException thrown :" + e.getMessage());
        }
    }

    public void CommentDelete() {
        String r1Path = "/c1d1/c1";
        Collection r1;
        try {
            r1 = registry.newCollection();
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
            deleteResources("/c1d1");
            log.info("CommentDelete - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -CommentDelete RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -CommentDelete RegistryException thrown :" + e.getMessage());
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
