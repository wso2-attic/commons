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

package org.wso2.carbon.web.test.registry;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;


public class
        RegistryTags extends TestCase {

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

    public RegistryTags(String text) {
        super(text);
    }

    public void testaddTag() throws Exception {

        registryCommon.signOut();
        String tagName = "tag1";
        String resourcePath = "/";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTags(tagName, "tfTag");
        Thread.sleep(1000);
        registryCommon.gotoResourcePage();
        registryCommon.basicTagSearch(resourcePath, tagName);
        assertTrue(registryCommon.searchSimpleTag(resourcePath, tagName));
        UmCommon.logOutUI();
    }

    public void testaddMultipleResourceTags() throws Exception {
        registryCommon.signOut();
        String resourcePath = "/testaddMultipleResourceTags";

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(2000);
        registryCommon.gotoResource(resourcePath);

        registryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, "tfTag");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        registryCommon.tagLinkNavigation(tag1Value, 1, resourcePath);

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.basicTagSearch(resourcePath, tag1Value);
        assertTrue(registryCommon.searchSimpleTag(resourcePath, tag1Value));

        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);

        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        registryCommon.tagLinkNavigation(tag2Value, 1, resourcePath);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.basicTagSearch(resourcePath, tag2Value);


        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);

        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        registryCommon.tagLinkNavigation(tag3Value, 1, resourcePath);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.basicTagSearch(resourcePath, tag3Value);

        assertTrue(registryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertTrue(registryCommon.searchSimpleTag(resourcePath, tag3Value));

        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteResource(resourcePath, actionId));

        assertFalse(registryCommon.searchSimpleTag(resourcePath, tag1Value));
        assertFalse(registryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertFalse(registryCommon.searchSimpleTag(resourcePath, tag3Value));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.basicTagSearch(resourcePath, tag1Value));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.basicTagSearch(resourcePath, tag2Value));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.basicTagSearch(resourcePath, tag3Value));

        UmCommon.logOutUI();
    }

    public void testaddMultipleCollectionTags() throws Exception {
        registryCommon.signOut();
        String resourcePath = "/testaddMultipleCollectionTags";

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourcePath, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        registryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, "tfTag");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        registryCommon.tagLinkNavigation(tag1Value, 1, resourcePath);

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.basicTagSearch(resourcePath, tag1Value);
        assertTrue(registryCommon.searchSimpleTag(resourcePath, tag1Value));

        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        registryCommon.tagLinkNavigation(tag2Value, 1, resourcePath);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.basicTagSearch(resourcePath, tag2Value);


        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        registryCommon.tagLinkNavigation(tag3Value, 1, resourcePath);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.basicTagSearch(resourcePath, tag3Value);

        assertTrue(registryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertTrue(registryCommon.searchSimpleTag(resourcePath, tag3Value));

        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteCollection(resourcePath, actionId));

        assertFalse(registryCommon.searchSimpleTag(resourcePath, tag1Value));
        assertFalse(registryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertFalse(registryCommon.searchSimpleTag(resourcePath, tag3Value));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.basicTagSearch(resourcePath, tag1Value));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.basicTagSearch(resourcePath, tag2Value));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.basicTagSearch(resourcePath, tag3Value));

        UmCommon.logOutUI();
    }

    public void testaddLongResourceTags() throws Exception {

        registryCommon.signOut();
        String resourceName = "/testaddLongResourceTags";
        String tagValue = "This is long tag";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        registryCommon.addTags(tagValue, "tfTag");
        registryCommon.tagLinkNavigation(tagValue, 1, resourceName);

        assertTrue(registryCommon.searchSimpleTag(resourceName, "This is long tag"));

        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));

        assertFalse(registryCommon.searchSimpleTag(resourceName, "This is long tag"));

        UmCommon.logOutUI();
    }

    public void testaddLongCollectionTags() throws Exception {
        registryCommon.signOut();
        String resourceName = "/testaddLongCollectionTags";
        String tagValue = "This is long tag";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourceName, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        registryCommon.addTags(tagValue, "tfTag");
        registryCommon.tagLinkNavigation(tagValue, 1, resourceName);

        assertTrue(registryCommon.searchSimpleTag(resourceName, "This is long tag"));

        registryCommon.gotoResourcePage();
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId));

        assertFalse(registryCommon.searchSimpleTag(resourceName, "This is long tag"));

        UmCommon.logOutUI();
    }

    public void testaddSubResourceTags() throws Exception {
        registryCommon.signOut();
        String collName1 = "/testaddSubResourceTags";
        String collName2 = "test";
        String fullCollectionName = collName1 + "/" + collName2;
        String resourceName = "testaddSubResourceTags";

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(fullCollectionName, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(collName1);
        Thread.sleep(1000);

        registryCommon.gotoResource(collName2);
        assertTrue(selenium.isTextPresent(collName2));
        Thread.sleep(1000);

        registryCommon.addTextResource(resourceName);

        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        registryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, "tfTag");

        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        registryCommon.tagLinkNavigation(tag1Value, 1, fullCollectionName + "/" + resourceName);

        assertTrue(registryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag2Value));
        assertTrue(registryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag3Value));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();

        int actionId = registryCommon.getActionID(collName1);
        assertTrue(registryCommon.deleteCollection(collName1, actionId));

        assertFalse(registryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag1Value));
        assertFalse(registryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag2Value));
        assertFalse(registryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag3Value));

        UmCommon.logOutUI();
    }

    public void testaddSubCollectionTags() throws Exception {
        registryCommon.signOut();
        String collName1 = "/testaddSubCollectionTags";
        String collName2 = "/test2";
        String collName3 = "/test3";
        String resourceName = collName1 + collName2 + collName3;

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";
        String tagId = "tfTag";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourceName, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(collName1);
        Thread.sleep(1000);

        registryCommon.gotoResource(collName2);
        assertTrue(selenium.isTextPresent(collName2));
        Thread.sleep(1000);

        Thread.sleep(1000);
        registryCommon.gotoResource(collName3);
        assertTrue(selenium.isTextPresent(collName3));
        Thread.sleep(1000);

        registryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, tagId);

        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        registryCommon.tagLinkNavigation(tag1Value, 1, resourceName);

        assertTrue(registryCommon.searchSimpleTag(resourceName, tag2Value));
        assertTrue(registryCommon.searchSimpleTag(resourceName, tag3Value));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();

        int actionId = registryCommon.getActionID(collName1);
        assertTrue(registryCommon.deleteCollection(collName1, actionId));

        assertFalse(registryCommon.searchSimpleTag(resourceName, tag1Value));
        assertFalse(registryCommon.searchSimpleTag(resourceName, tag2Value));
        assertFalse(registryCommon.searchSimpleTag(resourceName, tag3Value));

        UmCommon.logOutUI();
    }

    public void testResourceTagCount() throws Exception {
        registryCommon.signOut();
        String userName = "newUser1";
        String password = "test1231";
        String resourceName = "/testResourceTagCount";
        String tagValue = "This is a long tag";
        String tagId = "tfTag";

        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);

        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        registryCommon.addTags(tagValue, tagId);
        UmCommon.logOutUI();

        UmCommon.loginToUI(userName, password);
        registryCommon.gotoResourcePage();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);
        registryCommon.addTags(tagValue, tagId);
        registryCommon.tagLinkNavigation(tagValue, 2, resourceName);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();

        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));

        Thread.sleep(1000);
        assertFalse(registryCommon.searchSimpleTag(resourceName, tagValue));
        UmCommon.logOutUI();
        Thread.sleep(1000);
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.deleteUser(userName);
        UmCommon.logOutUI();
    }
}
