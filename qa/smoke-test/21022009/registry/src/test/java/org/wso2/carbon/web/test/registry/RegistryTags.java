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

import java.awt.event.KeyEvent;


public class RegistryTags extends CommonSetup {


    public RegistryTags(String text) {
        super(text);
    }

    public void testaddTag() throws Exception {

        String tagName = "tag1";
        String resourcePath = "/";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTags(tagName, "tfTag");
        Thread.sleep(1000);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.basicTagSearch(resourcePath, tagName);
        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tagName));
        UserManagerCommon.logOutUi();
    }

    public void testaddMultipleResourceTags() throws Exception {

        String resourcePath = "/testResource1";

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(2000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, "tfTag");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        RegistryCommon.tagLinkNavigation(tag1Value, 1, resourcePath);

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.basicTagSearch(resourcePath, tag1Value);
        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tag1Value));

        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        RegistryCommon.tagLinkNavigation(tag2Value, 1, resourcePath);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.basicTagSearch(resourcePath, tag2Value);


        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        RegistryCommon.tagLinkNavigation(tag3Value, 1, resourcePath);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.basicTagSearch(resourcePath, tag3Value);

        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tag3Value));

        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.deleteResource(resourcePath, 3);


        assertFalse(RegistryCommon.searchSimpleTag(resourcePath, tag1Value));
        assertFalse(RegistryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertFalse(RegistryCommon.searchSimpleTag(resourcePath, tag3Value));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        assertFalse(RegistryCommon.basicTagSearch(resourcePath, tag1Value));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        assertFalse(RegistryCommon.basicTagSearch(resourcePath, tag2Value));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        assertFalse(RegistryCommon.basicTagSearch(resourcePath, tag3Value));

        UserManagerCommon.logOutUi();
    }

    public void testaddMultipleCollectionTags() throws Exception {

        String resourcePath = "/testaddMultipleCollectionTags";

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourcePath, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, "tfTag");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        RegistryCommon.tagLinkNavigation(tag1Value, 1, resourcePath);

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.basicTagSearch(resourcePath, tag1Value);
        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tag1Value));

        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        RegistryCommon.tagLinkNavigation(tag2Value, 1, resourcePath);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.basicTagSearch(resourcePath, tag2Value);


        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourcePath));
        Thread.sleep(1000);
        RegistryCommon.tagLinkNavigation(tag3Value, 1, resourcePath);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.basicTagSearch(resourcePath, tag3Value);

        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertTrue(RegistryCommon.searchSimpleTag(resourcePath, tag3Value));

        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.deleteCollection(resourcePath, 3);


        assertFalse(RegistryCommon.searchSimpleTag(resourcePath, tag1Value));
        assertFalse(RegistryCommon.searchSimpleTag(resourcePath, tag2Value));
        assertFalse(RegistryCommon.searchSimpleTag(resourcePath, tag3Value));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        assertFalse(RegistryCommon.basicTagSearch(resourcePath, tag1Value));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        assertFalse(RegistryCommon.basicTagSearch(resourcePath, tag2Value));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        assertFalse(RegistryCommon.basicTagSearch(resourcePath, tag3Value));

        UserManagerCommon.logOutUi();
    }

    public void testaddLongResourceTags() throws Exception {


        String resourceName = "/testaddLongResourceTags";
        String tagValue = "This is long tag";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        RegistryCommon.addTags(tagValue, "tfTag");
        RegistryCommon.tagLinkNavigation(tagValue, 1, resourceName);

        assertTrue(RegistryCommon.searchSimpleTag(resourceName, "This is long tag"));

        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteResource(resourceName, 3);

        assertFalse(RegistryCommon.searchSimpleTag(resourceName, "This is long tag"));

        UserManagerCommon.logOutUi();
    }

    public void testaddLongCollectionTags() throws Exception {

        String resourceName = "/testaddLongCollectionTags";
        String tagValue = "This is long tag";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourceName, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        RegistryCommon.addTags(tagValue, "tfTag");
        RegistryCommon.tagLinkNavigation(tagValue, 1, resourceName);

        assertTrue(RegistryCommon.searchSimpleTag(resourceName, "This is long tag"));

        RegistryCommon.gotoResourcePage();
        Thread.sleep(2000);
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteCollection(resourceName, 3);

        assertFalse(RegistryCommon.searchSimpleTag(resourceName, "This is long tag"));

        UserManagerCommon.logOutUi();
    }

    public void testaddSubResourceTags() throws Exception {

        String collName1 = "/test";
        String collName2 = "test";
        String fullCollectionName = collName1 + "/" + collName2;
        String resourceName = "testaddSubResourceTags";

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(fullCollectionName, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1000);

        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(collName1));
        Thread.sleep(1000);

        RegistryCommon.addTextResourceToRoot(resourceName);

        Thread.sleep(1000);
        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        RegistryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, "tfTag");

        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        RegistryCommon.tagLinkNavigation(tag1Value, 1, fullCollectionName + "/" + resourceName);

        assertTrue(RegistryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag2Value));
        assertTrue(RegistryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag3Value));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();

        RegistryCommon.deleteCollection(collName1, 3);

        assertFalse(RegistryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag1Value));
        assertFalse(RegistryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag2Value));
        assertFalse(RegistryCommon.searchSimpleTag(fullCollectionName + "/" + resourceName, tag3Value));

        UserManagerCommon.logOutUi();
    }

    public void testaddSubCollectionTags() throws Exception {

        String collName1 = "/test1";
        String collName2 = "/test2";
        String collName3 = "/test3";
        String resourceName = collName1 + collName2 + collName3;

        String tag1Value = "test123";
        String tag2Value = "test1234";
        String tag3Value = "test12345";
        String tagId = "tfTag";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourceName, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1000);

        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(collName1));
        Thread.sleep(1000);

        Thread.sleep(1000);
        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(collName3));
        Thread.sleep(1000);

        RegistryCommon.addTags(tag1Value + "," + tag2Value + "," + tag3Value, tagId);

        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Tags"));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag1Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag2Value));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(tag3Value));

        RegistryCommon.tagLinkNavigation(tag1Value, 1, resourceName);

        assertTrue(RegistryCommon.searchSimpleTag(resourceName, tag2Value));
        assertTrue(RegistryCommon.searchSimpleTag(resourceName, tag3Value));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();

        RegistryCommon.deleteCollection(collName1, 3);

        assertFalse(RegistryCommon.searchSimpleTag(resourceName, tag1Value));
        assertFalse(RegistryCommon.searchSimpleTag(resourceName, tag2Value));
        assertFalse(RegistryCommon.searchSimpleTag(resourceName, tag3Value));

        UserManagerCommon.logOutUi();
    }

    public static void testResourceTagCount() throws Exception {

        String userName = "newUser1";
        String password = "test1231";
        String resourceName = "/testResourceTagCount";
        String tagValue = "This is a long tag";
        String tagId = "tfTag";

        UserManagerCommon.addNewUser(userName, password);
        UserManagerCommon.assignUserToAdminRole(userName);

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);

        RegistryCommon.addTags(tagValue, tagId);
        UserManagerCommon.logOutUi();

        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);
        RegistryCommon.addTags(tagValue, tagId);
        RegistryCommon.tagLinkNavigation(tagValue, 2, resourceName);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteResource(resourceName, 3);
        Thread.sleep(1000);
        assertFalse(RegistryCommon.searchSimpleTag(resourceName, tagValue));
        UserManagerCommon.logOutUi();
        UserManagerCommon.deleteUser(userName);
        UserManagerCommon.logOutUi();
    }
}
