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


public class RegistryProperties extends CommonSetup {

    public RegistryProperties(String text) {
        super(text);
    }

    public void testaddPropertyToResource() throws Exception {
        String propertyKey = "key";
        String propertyValue = "value";
        String resourcePath = "/addPropertyToResource";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        RegistryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        RegistryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteResource(resourcePath, 1);
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin"));
        UserManagerCommon.logOutUi();
    }

    public void testaddPropertyToRoot() throws Exception {

        String propertyKey = "key";
        String propertyValue = "value";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        Thread.sleep(1000);
        RegistryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        RegistryCommon.searchProproties(propertyKey, propertyValue, "/", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        RegistryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin");
        assertFalse(RegistryCommon.searchProproties(propertyKey, propertyValue, "/", "admin"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteProperty(propertyKey, propertyValue);
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin"));
        UserManagerCommon.logOutUi();
    }


    public void testaddPropertyToCollection() throws Exception {

        String collectionName = "/testaddPropertyToCollection";
        String propertyKey = "key";
        String propertyValue = "value";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(collectionName, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        RegistryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        RegistryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin");
        assertFalse(RegistryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        RegistryCommon.deleteCollection(collectionName, 3);
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin"));
        UserManagerCommon.logOutUi();
    }

    public void testaddPropertyUrlToCollection() throws Exception {

        String collectionName = "/testaddPropertyUrlToCollection";
        String propertyKey = "URL";
        String propertyValue = "https://localhost:9443/carbon/resources/resource.jsp";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "https://localhost:9443/carbon/resources/resource123.jsp";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(collectionName, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addProperty(propertyKey, propertyValue);

        Thread.sleep(1000);
        RegistryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        RegistryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin");
        assertFalse(RegistryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        RegistryCommon.deleteCollection(collectionName, 3);
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin"));
        UserManagerCommon.logOutUi();
    }

    public void testaddPropertiesWithSpaces() throws Exception {

        String collectionName = "/testaddPropertiesWithSpaces";
        String propertyKey = "Key space";
        String propertyValue = "Propery value space";

        String updatePropertyKey = "key Updated";
        String updatepropertyValue = "value Updated";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(collectionName, "", "");
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        RegistryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        Thread.sleep(1000);
        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin");
        assertFalse(RegistryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        RegistryCommon.deleteCollection(collectionName, 3);
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin"));
        UserManagerCommon.logOutUi();
    }

    public void testVerifyPropertyBoxUi() throws Exception {

        String resourcePath = "/testVerifyPropertyBoxUi";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Properties"));
        boolean tableName = selenium.isTextPresent("Properties");
        Thread.sleep(1000);
        selenium.click("propertiesIconMinimized");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Properties"));
        assertTrue(selenium.isTextPresent("No properties defined yet"));
        Thread.sleep(1000);
        selenium.click("propertiesIconExpanded");
        selenium.click("propertiesIconMinimized");
        selenium.click("link=Add New Property");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Add New Property"));
        assertTrue(selenium.isTextPresent("Name*"));
        assertTrue(selenium.isTextPresent("Value*"));
        assertTrue(selenium.isTextPresent("No properties defined yet"));
        Thread.sleep(1000);
        selenium.click("//input[@value='Add']");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("The required field Property Name has not been filled in."));
        selenium.click("//button[@type='button']");
        Thread.sleep(1000);
        selenium.type("propName", "");
        selenium.click("//input[@value='Add']");
        Thread.sleep(1000);
        selenium.click("link=X");
        Thread.sleep(1000);
        selenium.type("propValue", "");
        selenium.click("//input[@value='Add']");
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
        Thread.sleep(1000);
        selenium.type("propName", "test");
        selenium.click("//input[@value='Add']");
        boolean value = selenium.isTextPresent("The required field Property Value has not been filled in");
        assertTrue(selenium.isTextPresent("The required field Property Value has not been filled in"));
        selenium.click("//button[@type='button']");
        selenium.type("propValue", "");
        selenium.click("//input[@value='Add']");
        assertTrue(selenium.isTextPresent("The required field Property Value has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.type("propValue", "testvalue");
        selenium.click("//input[@value='Add']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("1 Property"));
        assertTrue(selenium.isTextPresent("Name"));
        assertTrue(selenium.isTextPresent("Value"));
        assertTrue(selenium.isTextPresent("Action"));
        assertTrue(selenium.isTextPresent("test"));
        assertTrue(selenium.isTextPresent("testvalue"));
        assertTrue(selenium.isTextPresent("testvalue"));
        assertTrue(selenium.isTextPresent("Delete"));
        assertTrue(selenium.isTextPresent("Edit"));
        Thread.sleep(1000);
        selenium.click("propertiesIconExpanded");
        selenium.click("propertiesIconMinimized");
        selenium.click("link=Delete");
        selenium.click("//button[@type='button']");
        Thread.sleep(1000);
        RegistryCommon.clickRootIcon();
        RegistryCommon.deleteResource(resourcePath, 3);
        UserManagerCommon.logOutUi();
    }

    public void testProperyCount() throws Exception {

        String propertyKey = "key1";
        String propertyValue = "value1";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        String resourcePath = "/testProperyCount";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addProperty(propertyKey, propertyValue);
        RegistryCommon.addProperty(propertyKey + 1, propertyValue + 1);
        RegistryCommon.addProperty(propertyKey + 2, propertyValue + 2);

        RegistryCommon.verifyPropertyCount(3, "Properties");
        RegistryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin");
        RegistryCommon.searchProproties(propertyKey + 1, propertyValue + 1, resourcePath, "admin");
        RegistryCommon.searchProproties(propertyKey + 2, propertyValue + 2, resourcePath, "admin");
        RegistryCommon.gotoResourcePage();

        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");

        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        RegistryCommon.editProperty(updatePropertyKey + 1, updatepropertyValue + 1, 1);
        RegistryCommon.editProperty(updatePropertyKey + 2, updatepropertyValue + 2, 2);
        Thread.sleep(1000);

        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin");
        RegistryCommon.searchProproties(updatePropertyKey + 1, updatepropertyValue + 1, resourcePath, "admin");
        RegistryCommon.searchProproties(updatePropertyKey + 2, updatepropertyValue + 2, resourcePath, "admin");

        assertFalse(RegistryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin"));
        assertFalse(RegistryCommon.searchProproties(propertyKey + 1, propertyValue + 1, resourcePath, "admin"));
        assertFalse(RegistryCommon.searchProproties(propertyKey + 2, propertyValue + 2, resourcePath, "admin"));

        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);


        RegistryCommon.deleteResource(resourcePath, 3);

        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin");
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey + 1, updatepropertyValue + 1, resourcePath, "admin"));
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey + 2, updatepropertyValue + 2, resourcePath, "admin"));

        UserManagerCommon.logOutUi();
    }


    public void testProperySingleCount() throws Exception {

        String propertyKey = "key1";
        String propertyValue = "value1";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        String resourcePath = "/testProperySingleCount";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.addProperty(propertyKey, propertyValue);

        RegistryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        RegistryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        RegistryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        RegistryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin");
        assertFalse(RegistryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        Thread.sleep(1000);
        RegistryCommon.deleteResource(resourcePath, 3);
        Thread.sleep(1000);
        assertFalse(RegistryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin"));
        UserManagerCommon.logOutUi();
    }
}
