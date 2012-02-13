/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

package org.wso2.carbon.web.test.registry;

public class RegistryComment extends CommonSetup {
    public RegistryComment(String text) {
        super(text);
    }

    public void testCommentBoxUivalidation() throws Exception {

        String resourceName = "/test";
        String commentValue = "testCommentValue";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourceName, " ", " ");
        Thread.sleep(1000);

        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("test"));
        assertTrue(selenium.isTextPresent("Comments"));
        selenium.click("commentsIconMinimized");
        assertTrue(selenium.isTextPresent("No comments on this entry yet"));
        assertTrue(selenium.isTextPresent("Add Comment"));
        assertTrue(selenium.isTextPresent("Feed"));

        assertTrue(selenium.isVisible("id=commentsIconExpanded"));
        assertTrue(selenium.isVisible("id=commentsExpanded"));

        selenium.click("commentsIconExpanded");
        selenium.click("commentsIconMinimized");
        selenium.click("link=Add Comment");
        assertTrue(selenium.isTextPresent("No comments on this entry yet"));
        assertTrue(selenium.isTextPresent("Add New Comment"));
        selenium.click("//input[@value='Cancel' and @type='button' and @onclick=\"showHideCommon('add-comment-div');\"]");
        assertTrue(selenium.isTextPresent("No comments on this entry yet"));
        selenium.click("link=Add Comment");
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment('/test');\"]");
        assertTrue(selenium.isTextPresent("The required field Comment Body has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.type("comment", "");
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment('/test');\"]");
        assertTrue(selenium.isTextPresent("The required field Comment Body has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.click("//input[@value='Cancel' and @type='button' and @onclick=\"showHideCommon('add-comment-div');\"]");

        RegistryCommon.addComment(commentValue, "admin", resourceName);
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteCollection(resourceName, 3);
        UserManagerCommon.logOutUi();
    }


    public void testaddComment() throws Exception {

        String userName = "admin";
        String password = "admin";
        String resourceName = "/";
        String commentValue = "Test Comment";
        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        assertTrue(selenium.isTextPresent("Resources"));
        assertTrue(selenium.isTextPresent("Comments"));
        RegistryCommon.addComment(commentValue, userName, resourceName);
        assertTrue(RegistryCommon.searchSimpleCommet("\"", commentValue));
        UserManagerCommon.logOutUi();
    }

    public void testaddLongComment() throws Exception {

        String userName = "admin";
        String password = "admin";
        String resourceName = "/testaddLongComment";
        String LongCommentValue = "This is a long comment This is a long comment This is a long comment This is a long " +
                "comment This is a long commentThis is a long commentThis is a long commentThis is a long commentThis " +
                "is a long commentThis is a long commentThis is a long comment ";
        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addComment(LongCommentValue, userName, resourceName);
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("This is a long comment"));
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, LongCommentValue));
        RegistryCommon.deleteResource(resourceName, 3);
        UserManagerCommon.logOutUi();
    }

    public void testaddCommentToResource() throws Exception {

        String userName = "admin";
        String password = "admin";
        String resourceName = "/testaddCommentToResource";
        String commentValue = "This is test comment on resource";

        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addComment(commentValue, userName, resourceName);
        Thread.sleep(1000);
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteResource(resourceName, 3);
        Thread.sleep(1000);

        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue));
        UserManagerCommon.logOutUi();
    }

    public void testaddCommentOnCollection() throws Exception {

        String userName = "admin";
        String password = "admin";
        String resourceName = "/testaddCommentToResource";
        String commentValue = "This is test comment on Collection";

        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourceName, " ", " ");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addComment(commentValue, userName, resourceName);
        Thread.sleep(1000);
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteCollection(resourceName, 3);
        Thread.sleep(1000);
        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue));
        UserManagerCommon.logOutUi();
    }

    public void testcollectioncommentCount() throws Exception {

        String userName = "admin";
        String password = "admin";
        String resourceName = "/testcollectioncommentCount";
        String commentValue1 = "This is test comment1 on Collection";
        String commentValue2 = "This is test comment2 on Collection";
        String commentValue3 = "This is test comment3 on Collection";

        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourceName, "", "");

        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addComment(commentValue1, userName, resourceName);
        RegistryCommon.addComment(commentValue2, userName, resourceName);
        RegistryCommon.addComment(commentValue3, userName, resourceName);

        assertTrue(selenium.isTextPresent("3 Comments"));

        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue3));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteCollection(resourceName, 3);
        Thread.sleep(1000);

        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue3));
        UserManagerCommon.logOutUi();
    }

    public void testresourceCommentCount() throws Exception {

        String userName = "admin";
        String password = "admin";
        String resourceName = "/testresourceCommentCount";
        String commentValue1 = "This is test comment1 on Collection";
        String commentValue2 = "This is test comment2 on Collection";
        String commentValue3 = "This is test comment3 on Collection";

        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourceName);

        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addComment(commentValue1, userName, resourceName);
        RegistryCommon.addComment(commentValue2, userName, resourceName);
        RegistryCommon.addComment(commentValue3, userName, resourceName);

        assertTrue(selenium.isTextPresent("3 Comments"));

        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertTrue(RegistryCommon.searchSimpleCommet(resourceName, commentValue3));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteResource(resourceName, 3);
        Thread.sleep(1000);

        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertFalse(RegistryCommon.searchSimpleCommet(resourceName, commentValue3));
        UserManagerCommon.logOutUi();
    }
}
