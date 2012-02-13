package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Comment;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.TaggedResourcePath;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;
//import org.wso2.carbon.system.test.core.utils.gregUtils.GregSystemProperties;

public class ResourceHandlingServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(ResourceHandlingServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static WSRegistryServiceClient registry_testUser = null;
    private static WSRegistryServiceClient registry_diffDomainUser1 = null;
    String admin_username;
    String diffDomain_username1;
    String admin_password;
    String comment1 = "check multitenancy feature 1";
    String comment2 = "check multitenancy feature 2";
    String tag1 = "multi tenancy tag 1";
    String tag2 = "multi tenancy tag 2";

    @Override
    public void init() {
        testClassName = ResourceHandlingServiceTestClient.class.getName();
        String tenantId = "3";
        String diff_Domainuser = "6";
        int tenantID_testUser = 3;
        String userID = "testuser1";
        String userPassword = "test123";
        String roleName = "admin";

        registry = new RegistryProvider().getRegistry(tenantId);
        registry_diffDomainUser1 = new RegistryProvider().getRegistry(diff_Domainuser);

        GregUserCreator GregUserCreator = new GregUserCreator();
        GregUserCreator.deleteUsers(tenantID_testUser, userID);
        GregUserCreator.addUser(tenantID_testUser, userID, userPassword, roleName);
        registry_testUser = GregUserCreator.getRegistry(tenantID_testUser, userID, userPassword);

        //Admin Tenant Details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        admin_username = tenantDetails.getTenantName();
        admin_password = tenantDetails.getTenantPassword();

        //different domain user1
        TenantDetails tenantDetails_diffDomainUser1 = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(diff_Domainuser));
        diffDomain_username1 = tenantDetails_diffDomainUser1.getTenantName();
        // Delete Resource
        deleteResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Multi Tenancy ResourceHandlingServiceTestClientt Test Cases............................ ");
        addResource();
        log.info("Completed Running Multi Tenancy ResourceHandlingServiceTestClientt Test Cases...................");
    }



    @Override
    public void cleanup() {

    }

    private void uploadResource(String path) {
        Resource r1 = registry.newResource();
        String content = "this is my content1";
        try {
            r1.setContent(content.getBytes());
            r1.setDescription("This is r1 file description");
            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");
            registry.put(path, r1);
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void deleteResource() {
        try {
            registry.delete("/d1");
            registry_diffDomainUser1.delete("/d1");
        } catch (RegistryException e) {
            log.error("deleteResource Registry Exception thrown:" + e.getMessage());
            Assert.fail("deleteResource Registry Exception thrown:" + e.getMessage());
        }
    }

    private void verifyResourceExists(String path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertTrue("Resource Exists :", registry.resourceExists(path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertTrue("Resource exists:", registry_testUser.resourceExists(path));
            // Assert differnt doamin user 1
            assertFalse("Resource exists:", registry_diffDomainUser1.resourceExists(path));
        } catch (RegistryException e) {
            log.error("verifyResourceExists RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists RegistryException thrown:" + e.getMessage());
        }
    }

    private void verifyResourceDelete(String path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertFalse("Resource Exists :", registry.resourceExists(path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertFalse("Resource exists:", registry_testUser.resourceExists(path));
            // Assert differnt doamin user 1
            assertFalse("Resource exists:", registry_diffDomainUser1.resourceExists(path));
        } catch (RegistryException e) {
            log.error("verifyResourceDelete RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourceDelete RegistryException thrown:" + e.getMessage());
        }
    }

    private void addComment(String path) {
        Comment c1 = new Comment();
        c1.setResourcePath(path);
        c1.setText("This is default comment");
        c1.setUser(admin_username);
        try {
            registry.addComment(path, c1);
            registry.addComment(path, new Comment(comment1));
            registry.addComment(path, new Comment(comment2));
        } catch (RegistryException e) {
            log.error("addComment RegistryException thrown:" + e.getMessage());
            Assert.fail("addComment RegistryException thrown:" + e.getMessage());
        }
    }

    private void verifyComments(String path) {
        Comment[] comments;
        try {
            comments = registry.getComments(path);
            boolean commentFound = false;
            for (Comment comment : comments) {
                if (comment.getText().equals(comment1)) {
                    commentFound = true;
                    assertEquals(comment1, comment.getText());
                    assertEquals(admin_password, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }
                if (comment.getText().equals(comment2)) {
                    commentFound = true;
                    assertEquals(comment2, comment.getText());
                    assertEquals(admin_password, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }

                if (comment.getText().equals("This is default comment")) {
                    commentFound = true;
                    assertEquals("This is default comment", comment.getText());
                }
            }
            assertTrue("Comment not found", commentFound);
        } catch (RegistryException e) {
            log.error("verifyComments by admin123@wso2manualQA0006.org RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyComments by admin123@wso2manualQA0006.org RegistryException thrown:" + e.getMessage());
        }
        // verify admin user comments : testuser1@wso2manualQA0006.org
        Comment[] comments_testUser;
        try {
            comments_testUser = registry_testUser.getComments(path);
            boolean commentFound = false;
            for (Comment comment : comments_testUser) {
                if (comment.getText().equals(comment1)) {
                    commentFound = true;
                    assertEquals(comment1, comment.getText());
                    assertEquals(admin_password, comment.getUser());
                    assertEquals(path, comment.getResourcePath());

                }

                if (comment.getText().equals(comment2)) {
                    commentFound = true;
                    assertEquals(comment2, comment.getText());
                    assertEquals(admin_password, comment.getUser());
                    assertEquals(path, comment.getResourcePath());
                }

                if (comment.getText().equals("This is default comment")) {
                    commentFound = true;
                    assertEquals("This is default comment", comment.getText());
                }
            }
            assertTrue("Comment not found", commentFound);
        } catch (RegistryException e) {
            log.error("verifyComments by testuser1@wso2manualQA0006.org RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyComments by testuser1@wso2manualQA0006.org RegistryException thrown:" + e.getMessage());
        }
        // verify by different domain user
        Comment[] comments_diffDomainUser = new Comment[0];
        try {
            comments_diffDomainUser = registry_diffDomainUser1.getComments(path);
            // assert array lenght zero
            assertEquals("Comments Array lenght 0 :", comments_diffDomainUser.length, 0);
            comments_diffDomainUser[0].getText();
        } catch (Exception e) {
            log.info("verifyComments by diffDomainUser Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert resource does not exists:
            Assert.assertNotNull(comments_diffDomainUser);
        }
    }

    private void addTag(String path) {
        try {
            registry.applyTag(path, tag1);
            registry.applyTag(path, tag2);
        } catch (RegistryException e) {
            log.error("addTag RegistryException thrown:" + e.getMessage());
            Assert.fail("addTag RegistryException thrown:" + e.getMessage());
        }
    }

    private void verifyTag(String path) {
        try {
            TaggedResourcePath[] tagPath_admin1 = registry.getResourcePathsWithTag(tag1);
            TaggedResourcePath[] tagPath_admin2 = registry.getResourcePathsWithTag(tag2);
            //assert admin user :admin123@wso2manualQA0006.org
            assertEquals("Tag path 1", tagPath_admin1[0].getResourcePath(), path);
            assertEquals("Tag path 1", tagPath_admin2[0].getResourcePath(), path);
            // assrt test user :testuser1@wso2manualQA0006.org
            TaggedResourcePath[] tagPath_testuser1 = registry_testUser.getResourcePathsWithTag(tag1);
            TaggedResourcePath[] tagPath_testUser2 = registry_testUser.getResourcePathsWithTag(tag2);
            assertEquals("Tag path 1", tagPath_testuser1[0].getResourcePath(), path);
            assertEquals("Tag path 1", tagPath_testUser2[0].getResourcePath(), path);
        } catch (RegistryException e) {
            log.error("verifyTag RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyTag RegistryException thrown:" + e.getMessage());
        }
        TaggedResourcePath[] tagPath_diffDomainUser1 = new TaggedResourcePath[0];
        try {
            tagPath_diffDomainUser1 = registry_diffDomainUser1.getResourcePathsWithTag(tag1);
            tagPath_diffDomainUser1[0].getResourcePath();
        } catch (Exception e) {
            log.info("verifyTag by diffDomainUser Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert resource does not exists:
            Assert.assertNull(tagPath_diffDomainUser1);
        }

    }


    private void addResource() {
        String path = "/d1/d2/d3/d4/r1";

        //add Resource
        uploadResource(path);
        //assert resource exists
        verifyResourceExists(path);
        //add comments
        addComment(path);
        // Assert comments
        verifyComments(path);
        // add Tag
        addTag(path);
        //Assert Tag paths
        verifyTag(path);
        //Delete resource
        deleteResource();
        //assert Resources have been deleted properly
        verifyResourceDelete(path);
        log.info("Multi Tenancy ResourceHandlingServiceTestClient Test - Passed");
    }
}
