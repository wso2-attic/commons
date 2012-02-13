/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.GaaS;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;

public class RegistryComment extends TestCase {
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public void testCommentBoxUivalidation() throws Exception {

        registryCommon.signOut();
        String resourceName = "/testCommentBoxUivalidation";
        String commentValue = "testCommentValue";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourceName, " ", " ");
        Thread.sleep(1000);

        registryCommon.gotoResource(resourceName);

        assertTrue(selenium.isTextPresent(resourceName));
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
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment('" + resourceName + "');\"]");
        assertTrue(selenium.isTextPresent("The required field Comment Body has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.type("comment", "");
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment('" + resourceName + "');\"]");
        assertTrue(selenium.isTextPresent("The required field Comment Body has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.click("//input[@value='Cancel' and @type='button' and @onclick=\"showHideCommon('add-comment-div');\"]");
        selenium.click("commentsIconExpanded");
        registryCommon.addComment(commentValue, "admin", resourceName);
        registryCommon.clickRootIcon();

        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId));
        UmCommon.logOutUI();
    }

    public void testaddComment() throws Exception {
        registryCommon.signOut();
        String userName = "admin";
        String password = "admin";
        String resourceName = "/";
        String commentValue = "Test Comment";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        assertTrue(selenium.isTextPresent("Browse"));
        assertTrue(selenium.isTextPresent("Comments"));
        registryCommon.addComment(commentValue, userName, resourceName);
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue));
        UmCommon.logOutUI();
    }

    public void testaddLongComment() throws Exception {
        registryCommon.signOut();
        String userName = "admin";
        String password = "admin";
        String resourceName = "/testaddLongComment12";
        String LongCommentValue = "This is a long comment This is a long comment This is a long comment This is a long " +
                "comment This is a long commentThis is a long commentThis is a long commentThis is a long commentThis " +
                "is a long commentThis is a long commentThis is a long comment ";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        registryCommon.addComment(LongCommentValue, userName, resourceName);
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("This is a long comment"));
        assertTrue(registryCommon.searchSimpleCommet(resourceName, LongCommentValue));
        registryCommon.gotoResourcePage();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));
        UmCommon.logOutUI();
    }

    public void testaddCommentToResource() throws Exception {
        registryCommon.signOut();
        String userName = "admin";
        String password = "admin";
        String resourceName = "/testaddCommentToResource1";
        String commentValue = "comment123";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        registryCommon.addComment(commentValue, userName, resourceName);
        Thread.sleep(1000);
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));
        Thread.sleep(1000);

        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue));
        UmCommon.logOutUI();
    }

    public void testaddCommentOnCollection() throws Exception {
        registryCommon.signOut();
        String userName = "admin";
        String password = "admin";
        String resourceName = "/testaddCommentOnCollection";
        String commentValue = "This is test comment on Collection";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourceName, " ", " ");
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        registryCommon.addComment(commentValue, userName, resourceName);
        Thread.sleep(1000);
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId));
        Thread.sleep(1000);
        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue));
        UmCommon.logOutUI();
    }

    public void testcollectioncommentCount() throws Exception {
        registryCommon.signOut();
        String userName = "admin";
        String password = "admin";
        String resourceName = "/testcollectioncommentCount";
        String commentValue1 = "This is test comment1 on Collection";
        String commentValue2 = "This is test comment2 on Collection";
        String commentValue3 = "This is test comment3 on Collection";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourceName, "", "");

        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        registryCommon.addComment(commentValue1, userName, resourceName);
        selenium.click("commentsIconExpanded");
        registryCommon.addComment(commentValue2, userName, resourceName);
        selenium.click("commentsIconExpanded");
        registryCommon.addComment(commentValue3, userName, resourceName);

        assertTrue(selenium.isTextPresent("3 Comments"));

        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue3));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId));
        Thread.sleep(1000);

        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue3));
        UmCommon.logOutUI();
    }

    public void testresourceCommentCount() throws Exception {
        registryCommon.signOut();
        String userName = "admin";
        String password = "admin";
        String resourceName = "/testresourceCommentCount";
        String commentValue1 = "This is test comment1 on Collection";
        String commentValue2 = "This is test comment2 on Collection";
        String commentValue3 = "This is test comment3 on Collection";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);

        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        registryCommon.addComment(commentValue1, userName, resourceName);
        selenium.click("commentsIconExpanded");
        registryCommon.addComment(commentValue2, userName, resourceName);
        selenium.click("commentsIconExpanded");
        registryCommon.addComment(commentValue3, userName, resourceName);

        assertTrue(selenium.isTextPresent("3 Comments"));

        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertTrue(registryCommon.searchSimpleCommet(resourceName, commentValue3));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));
        Thread.sleep(1000);

        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue1));
        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue2));
        assertFalse(registryCommon.searchSimpleCommet(resourceName, commentValue3));
        UmCommon.logOutUI();
    }
}
