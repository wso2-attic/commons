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

public class DependencyTest extends TestCase {

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

    public void testDependencyUiValidation() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        assertTrue(selenium.isTextPresent("Dependencies"));
        selenium.click("dependenciesIconMinimized");
        assertTrue(selenium.isTextPresent("No dependencies on this entry yet."));
        assertTrue(selenium.isTextPresent("Add Dependency"));
        selenium.click("//a[contains(text(),'Add\n                Dependency')]");
        assertTrue(selenium.isTextPresent("Add Dependency"));
        assertTrue(selenium.isTextPresent("Path *"));
        selenium.click("//input[@value='..' and @type='button' and @onclick=\"showResourceTree('depPaths');\"]");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Resource Paths"));
        selenium.click("link=X");
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('depForm');\"]");
        assertTrue(selenium.isTextPresent("The required field Path has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.type("depPaths", "");
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('depForm');\"]");
        assertTrue(selenium.isTextPresent("The required field Path has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.click("//input[@value='Cancel' and @type='button' and @onclick=\"showHideCommon('dependenciesAddDiv');\"]");
        selenium.click("dependenciesIconExpanded");
        selenium.click("dependenciesIconMinimized");
        selenium.click("//a[contains(text(),'Add\n                Dependency')]");
        selenium.click("//a[contains(text(),'Add\n                Dependency')]");
        selenium.click("//a[contains(text(),'Add\n                Dependency')]");
        selenium.click("//a[contains(text(),'Add\n                Dependency')]");
        UmCommon.logOutUI();
    }

    public void testAddDependencyToRoot() throws Exception {

        registryCommon.signOut();
        String assoResourceName = "/testAddDependencyToRoot";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);

        registryCommon.addDependency(assoResourceName);

        registryCommon.clickRootIcon();

        registryCommon.navigateDependencyLinks(assoResourceName);

        registryCommon.clickRootIcon();

        registryCommon.clickDependencyTree();

        assertTrue(registryCommon.checkDependencyTree(assoResourceName));

        registryCommon.clickDependencyTree();

        registryCommon.clickDependencyTreelinks(assoResourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId1));

        UmCommon.logOutUI();
    }

    public void testAddDependencyToResource() throws Exception {

        registryCommon.signOut();
        String assoResourceName = "/999testAddDependencyToResource12";
        String resourceName = "/testResource123";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName);

        registryCommon.gotoResource(assoResourceName);
        registryCommon.addDependency(resourceName);

        registryCommon.navigateDependencyLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);

        registryCommon.clickDependencyTree();

        assertTrue(registryCommon.checkDependencyTree(resourceName));

        registryCommon.clickDependencyTree();

        registryCommon.clickDependencyTreelinks(resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAddMultipleDependenciesToResource() throws Exception {

        String assoResourceName = "/88testAddMultipleDependenciesToResource";
        String resourceName1 = "/1resourceName1";
        String resourceName2 = "/2resourceName2";
        String resourceName3 = "/3resourceName3";

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName1);
        registryCommon.addTextResourceToRoot(resourceName2);
        registryCommon.addTextResourceToRoot(resourceName3);

        registryCommon.gotoResource(assoResourceName);

        registryCommon.addDependency(resourceName1);
        registryCommon.addDependency(resourceName2);
        registryCommon.addDependency(resourceName3);

        registryCommon.navigateDependencyLinks(resourceName1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.navigateDependencyLinks(resourceName2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.navigateDependencyLinks(resourceName3);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickDependencyTree();
        assertTrue(registryCommon.checkDependencyTree(resourceName1));
        registryCommon.clickDependencyTree();
        assertTrue(registryCommon.checkDependencyTree(resourceName2));
        registryCommon.clickDependencyTree();
        assertTrue(registryCommon.checkDependencyTree(resourceName3));

        registryCommon.clickDependencyTree();
        registryCommon.clickDependencyTreelinks(resourceName1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickDependencyTree();
        registryCommon.clickDependencyTreelinks(resourceName2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickDependencyTree();
        registryCommon.clickDependencyTreelinks(resourceName3);
        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName1);
        assertTrue(registryCommon.deleteResource(resourceName1, actionId1));

        int actionId2 = registryCommon.getActionID(resourceName2);
        assertTrue(registryCommon.deleteResource(resourceName2, actionId2));

        int actionId3 = registryCommon.getActionID(resourceName3);
        assertTrue(registryCommon.deleteResource(resourceName3, actionId3));

        int actionId4 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId4));

        UmCommon.logOutUI();
    }

    public void testRemoveResourceDependency() throws Exception {

        registryCommon.signOut();
        String assoResourceName = "/testRemoveResourceDependency99";
        String resourceName = "/testResource99";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName);

        registryCommon.gotoResource(assoResourceName);
        registryCommon.addDependency(resourceName);

        registryCommon.navigateDependencyLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);

        registryCommon.clickDependencyTree();

        assertTrue(registryCommon.checkDependencyTree(resourceName));

        registryCommon.clickDependencyTree();

        registryCommon.clickDependencyTreelinks(resourceName);

        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.removeDependency(assoResourceName, resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testRemoveMultipleResourceDependency() throws Exception {

        String assoResourceName = "/testRemoveMultipleResourceDependency";
        String resourceName = "/12resourceName";
        String resourceName2 = "/13resourceName";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.addTextResourceToRoot(resourceName2);

        registryCommon.gotoResource(assoResourceName);
        registryCommon.addDependency(resourceName);
        registryCommon.addDependency(resourceName2);


        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);
        registryCommon.removeDependency(assoResourceName, resourceName);
        registryCommon.removeDependency(assoResourceName, resourceName2);

        assertFalse(registryCommon.checkDependencyTable(resourceName));
        assertFalse(registryCommon.checkDependencyTable(resourceName2));

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId1));

        int actionId3 = registryCommon.getActionID(resourceName2);
        assertTrue(registryCommon.deleteResource(resourceName2, actionId3));

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAddDependencyToCollection() throws Exception {

        registryCommon.signOut();
        String assoColName = "/testAddDependencyToCollection";
        String resourceName = "/testResourceCol";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceName, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addDependency(resourceName);

        registryCommon.navigateDependencyLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);

        registryCommon.clickDependencyTree();

        assertTrue(registryCommon.checkDependencyTree(resourceName));

        registryCommon.clickDependencyTree();

        registryCommon.clickDependencyTreelinks(resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAddMultipleDependenciesToCollection() throws Exception {

        String assoColName = "/123testAddMultipleDependenciesToCollection";
        String resourceName1 = "/1resourceName1";
        String resourceName2 = "/2resourceName2";
        String resourceName3 = "/3resourceName3";

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceName1, "", "");
        registryCommon.addCollectionToRoot(resourceName2, "", "");
        registryCommon.addCollectionToRoot(resourceName3, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addDependency(resourceName1);
        registryCommon.addDependency(resourceName2);
        registryCommon.addDependency(resourceName3);

        registryCommon.navigateDependencyLinks(resourceName1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.navigateDependencyLinks(resourceName2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.navigateDependencyLinks(resourceName3);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.clickDependencyTree();
        assertTrue(registryCommon.checkDependencyTree(resourceName1));
        registryCommon.clickDependencyTree();
        assertTrue(registryCommon.checkDependencyTree(resourceName2));
        registryCommon.clickDependencyTree();
        assertTrue(registryCommon.checkDependencyTree(resourceName3));

        registryCommon.clickDependencyTree();
        registryCommon.clickDependencyTreelinks(resourceName1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.clickDependencyTree();
        registryCommon.clickDependencyTreelinks(resourceName2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.clickDependencyTree();
        registryCommon.clickDependencyTreelinks(resourceName3);
        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName1);
        assertTrue(registryCommon.deleteCollection(resourceName1, actionId1));

        int actionId2 = registryCommon.getActionID(resourceName2);
        assertTrue(registryCommon.deleteCollection(resourceName2, actionId2));

        int actionId3 = registryCommon.getActionID(resourceName3);
        assertTrue(registryCommon.deleteCollection(resourceName3, actionId3));

        int actionId4 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId4));

        UmCommon.logOutUI();
    }

    public void testRemoveCollectionDependency() throws Exception {

        registryCommon.signOut();
        String assoColName = "/testRemoveCollectionDependency12";
        String resourceName = "/testResource99";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceName, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addDependency(resourceName);

        registryCommon.navigateDependencyLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);

        registryCommon.clickDependencyTree();

        assertTrue(registryCommon.checkDependencyTree(resourceName));

        registryCommon.clickDependencyTree();

        registryCommon.clickDependencyTreelinks(resourceName);

        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.removeDependency(assoColName, resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId2));

        UmCommon.logOutUI();
    }

    public void testRemoveMultipleCollectionDependency() throws Exception {

        String assoColName = "/testRemoveMultipleCollectionDependency123";
        String resourceName = "/12resourceName";
        String resourceName2 = "/13resourceName";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceName, "", "");
        registryCommon.addCollectionToRoot(resourceName2, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addDependency(resourceName);
        registryCommon.addDependency(resourceName2);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);
        registryCommon.removeDependency(assoColName, resourceName);
        registryCommon.removeDependency(assoColName, resourceName2);

        assertFalse(registryCommon.checkDependencyTable(resourceName));
        assertFalse(registryCommon.checkDependencyTable(resourceName2));

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId1));

        int actionId3 = registryCommon.getActionID(resourceName2);
        assertTrue(registryCommon.deleteCollection(resourceName2, actionId3));

        int actionId2 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId2));

        UmCommon.logOutUI();
    }
}