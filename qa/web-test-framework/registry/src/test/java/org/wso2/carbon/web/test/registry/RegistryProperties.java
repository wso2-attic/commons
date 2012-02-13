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
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;


public class RegistryProperties extends TestCase {

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

    public void testaddPropertyToResource() throws Exception {
        registryCommon.signOut();
        String propertyKey = "key";
        String propertyValue = "value";
        String resourcePath = "/addPropertyToResource";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        registryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        registryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        registryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteResource(resourcePath, actionId));
        assertFalse(registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin"));
        UmCommon.logOutUI();
    }

    public void testaddPropertyToRoot() throws Exception {
        registryCommon.signOut();
        String propertyKey = "key";
        String propertyValue = "value";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        Thread.sleep(1000);
        registryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        registryCommon.searchProproties(propertyKey, propertyValue, "/", "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        registryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin");
        assertFalse(registryCommon.searchProproties(propertyKey, propertyValue, "/", "admin"));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.deleteProperty(propertyKey, propertyValue);
        assertFalse(registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, "/", "admin"));
        UmCommon.logOutUI();
    }


    public void testaddPropertyToCollection() throws Exception {
        registryCommon.signOut();
        String collectionName = "/testaddPropertyToCollection";
        String propertyKey = "key";
        String propertyValue = "value";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(collectionName);
        registryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        registryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(collectionName);
        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        registryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin");
        assertFalse(registryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin"));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        int actionId = registryCommon.getActionID(collectionName);
        assertTrue(registryCommon.deleteCollection(collectionName, actionId));
        assertFalse(registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin"));
        UmCommon.logOutUI();
    }

    public void testaddPropertyUrlToCollection() throws Exception {
        registryCommon.signOut();
        String collectionName = "/testaddPropertyUrlToCollection";
        String propertyKey = "URL";
        String propertyValue = "https://localhost:9443/carbon/resources/resource.jsp";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "https://localhost:9443/carbon/resources/resource123.jsp";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(collectionName);
        registryCommon.addProperty(propertyKey, propertyValue);

        Thread.sleep(1000);
        registryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(collectionName);
        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        registryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin");
        assertFalse(registryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin"));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        int actionId = registryCommon.getActionID(collectionName);
        assertTrue(registryCommon.deleteCollection(collectionName, actionId));
        assertFalse(registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin"));
        UmCommon.logOutUI();
    }

    public void testaddPropertiesWithSpaces() throws Exception {
        registryCommon.signOut();
        String collectionName = "/testaddPropertiesWithSpaces";
        String propertyKey = "Key space";
        String propertyValue = "Propery value space";

        String updatePropertyKey = "key Updated";
        String updatepropertyValue = "value Updated";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");
        Thread.sleep(1000);
        registryCommon.gotoResource(collectionName);
        registryCommon.addProperty(propertyKey, propertyValue);
        Thread.sleep(1000);
        registryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(collectionName);
        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        Thread.sleep(1000);
        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin");
        assertFalse(registryCommon.searchProproties(propertyKey, propertyValue, collectionName, "admin"));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        int actionId = registryCommon.getActionID(collectionName);
        assertTrue(registryCommon.deleteCollection(collectionName, actionId));
        assertFalse(registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, collectionName, "admin"));
        UmCommon.logOutUI();
    }

    public void testVerifyPropertyBoxUi() throws Exception {
        registryCommon.signOut();
        String resourcePath = "/testVerifyPropertyBoxUi";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
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
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteResource(resourcePath, actionId));
        UmCommon.logOutUI();
    }

    public void testPropertyCount() throws Exception {
        registryCommon.signOut();
        String propertyKey = "key1";
        String propertyValue = "value1";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        String resourcePath = "/testPropertyCount";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        registryCommon.addProperty(propertyKey, propertyValue);
        registryCommon.addProperty(propertyKey + 1, propertyValue + 1);
        registryCommon.addProperty(propertyKey + 2, propertyValue + 2);

        registryCommon.verifyPropertyCount(3, "Properties");
        registryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin");
        registryCommon.searchProproties(propertyKey + 1, propertyValue + 1, resourcePath, "admin");
        registryCommon.searchProproties(propertyKey + 2, propertyValue + 2, resourcePath, "admin");
        registryCommon.gotoResourcePage();

        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);

        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        selenium.click("propertiesIconMinimized");
        registryCommon.editProperty(updatePropertyKey + 1, updatepropertyValue + 1, 1);
        selenium.click("propertiesIconMinimized");
        registryCommon.editProperty(updatePropertyKey + 2, updatepropertyValue + 2, 2);
        Thread.sleep(1000);

        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin");
        registryCommon.searchProproties(updatePropertyKey + 1, updatepropertyValue + 1, resourcePath, "admin");
        registryCommon.searchProproties(updatePropertyKey + 2, updatepropertyValue + 2, resourcePath, "admin");

        assertFalse(registryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin"));
        assertFalse(registryCommon.searchProproties(propertyKey + 1, propertyValue + 1, resourcePath, "admin"));
        assertFalse(registryCommon.searchProproties(propertyKey + 2, propertyValue + 2, resourcePath, "admin"));

        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);


        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteResource(resourcePath, actionId));

        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin");
        assertFalse(registryCommon.searchProproties(updatePropertyKey + 1, updatepropertyValue + 1, resourcePath, "admin"));
        assertFalse(registryCommon.searchProproties(updatePropertyKey + 2, updatepropertyValue + 2, resourcePath, "admin"));

        UmCommon.logOutUI();
    }


    public void testProperySingleCount() throws Exception {
        registryCommon.signOut();
        String propertyKey = "key1";
        String propertyValue = "value1";

        String updatePropertyKey = "keyUpdated";
        String updatepropertyValue = "valueUpdated";

        String resourcePath = "/testProperySingleCount";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourcePath);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        registryCommon.addProperty(propertyKey, propertyValue);

        registryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        registryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin");
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        registryCommon.gotoResource(resourcePath);
        registryCommon.editProperty(updatePropertyKey, updatepropertyValue, 0);
        registryCommon.verifyPropertyCount(1, "Property");
        Thread.sleep(1000);
        registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin");
        assertFalse(registryCommon.searchProproties(propertyKey, propertyValue, resourcePath, "admin"));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        Thread.sleep(1000);
        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteResource(resourcePath, actionId));
        Thread.sleep(1000);
        assertFalse(registryCommon.searchProproties(updatePropertyKey, updatepropertyValue, resourcePath, "admin"));
        UmCommon.logOutUI();
    }
}
