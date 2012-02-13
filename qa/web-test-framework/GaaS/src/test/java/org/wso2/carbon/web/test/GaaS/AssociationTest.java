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


public class AssociationTest extends TestCase {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    ;

    public void setUp() throws Exception {

        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public AssociationTest(String s) {
        super(s);
    }

    public void testAddAssociationToRoot() throws Exception {


        registryCommon.signOut();
        String assoResourceName = "/199testAddAssociationToRoot";

        UmCommon.loginToUI(adminUserName, adminPassword);
        Thread.sleep(3000);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);

        registryCommon.addAssociationResource("TestType", assoResourceName);

        registryCommon.navigateAssociationLinks(assoResourceName);

        registryCommon.clickRootIcon();

        registryCommon.clickAssociationTree();

        assertTrue(registryCommon.checkAssociationTree(assoResourceName));

        registryCommon.clickAssocitionTreelinks(assoResourceName);

        registryCommon.clickRootIcon();

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAssociationUI() throws Exception {

        String resourceName = "/testAssociationUI";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.gotoResource(resourceName);

        selenium.click("associationsIconMinimized");
        assertTrue(selenium.isTextPresent("Associations"));
        assertTrue(selenium.isTextPresent("Add Association"));
        assertTrue(selenium.isTextPresent("No associations on this entry yet."));
        selenium.click("//a[contains(text(),'Add\n                Association')]");
        assertTrue(selenium.isTextPresent("Type *"));
        assertTrue(selenium.isTextPresent("Path *"));
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('assoForm');\"]");
        assertTrue(selenium.isTextPresent("The required field Type has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.type("type", "TestType");
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('assoForm');\"]");
        assertTrue(selenium.isTextPresent("The required field Path has not been filled in."));
        selenium.click("//button[@type='button']");
        selenium.click("//input[@value='Cancel' and @type='button' and @onclick=\"showHideCommon('associationsAddDiv');\"]");
        selenium.click("//a[contains(text(),'Add\n                Association')]");
        selenium.click("associationsIconExpanded");
        registryCommon.gotoResourcePage();

        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));

        UmCommon.logOutUI();
    }

    public void testAddAssociationToResource() throws Exception {

        String assoResourceName = "/testAddAssociationToResource8";
        String resourceName = "/resourceName8";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName);

        registryCommon.gotoResource(assoResourceName);
        registryCommon.addAssociationResource("TestType", resourceName);

        registryCommon.navigateAssociationLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);

        registryCommon.clickAssociationTree();

        assertTrue(registryCommon.checkAssociationTree(resourceName));

        registryCommon.clickAssocitionTreelinks(resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAddMultipleAssociationToResource() throws Exception {

        String assoResourceName = "/testAddAssociationToResource5";
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

        registryCommon.addAssociationResource("TestType", resourceName1);
        registryCommon.addAssociationResource("TestType", resourceName2);
        registryCommon.addAssociationResource("TestType", resourceName3);

        registryCommon.navigateAssociationLinks(resourceName1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.navigateAssociationLinks(resourceName2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.navigateAssociationLinks(resourceName3);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickAssociationTree();
        assertTrue(registryCommon.checkAssociationTree(resourceName1));
        registryCommon.clickAssociationTree();
        assertTrue(registryCommon.checkAssociationTree(resourceName2));
        registryCommon.clickAssociationTree();
        assertTrue(registryCommon.checkAssociationTree(resourceName3));

        registryCommon.clickAssocitionTreelinks(resourceName1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickAssociationTree();
        registryCommon.clickAssocitionTreelinks(resourceName2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickAssociationTree();
        registryCommon.clickAssocitionTreelinks(resourceName3);
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

    public void testAddExternalLinkAssociation() throws Exception {

        String assoResourceName = "/12345testAddExternalLinkAssociation";
        String externalLink = "http://www.google.com";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);


        registryCommon.gotoResource(assoResourceName);
        registryCommon.addExtenalLinkAssociation(externalLink, "ExtenalLink");

        registryCommon.clickAssociationTree();
        registryCommon.checkAssociationTree(externalLink);


        registryCommon.navigateAssociationUrls(externalLink, "Google");
        assertTrue(selenium.isTextPresent("Google"));
        Thread.sleep(1000);
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);
        registryCommon.clickAssociationTree();
        registryCommon.clickAssocitionTreelinks(externalLink);
        assertTrue(selenium.isTextPresent("Google"));
        Thread.sleep(1000);
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId1));

        UmCommon.logOutUI();
    }

    public void testRemoveResourceAssociation() throws Exception {

        String assoResourceName = "/testRemoveResourceAssociation1236";
        String resourceName = "/resourceName";
        String assoType = "assoType";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName);

        registryCommon.gotoResource(assoResourceName);
        registryCommon.addAssociationResource(assoType, resourceName);

        registryCommon.navigateAssociationLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);

        registryCommon.clickAssociationTree();

        assertTrue(registryCommon.checkAssociationTree(resourceName));

        registryCommon.clickAssocitionTreelinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);
        registryCommon.removeAssociation(assoResourceName, assoType, resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testRemoveMultipleResourceAssociation() throws Exception {

        String assoResourceName = "/122testRemoveMultipleResourceAssociation12";
        String resourceName = "/12resourceName";
        String resourceName2 = "/13resourceName";
        String assoType = "assoType";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(assoResourceName);
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.addTextResourceToRoot(resourceName2);

        registryCommon.gotoResource(assoResourceName);
        registryCommon.addAssociationResource(assoType, resourceName);
        registryCommon.addAssociationResource(assoType, resourceName2);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoResourceName);
        registryCommon.removeAssociation(assoResourceName, assoType, resourceName);
        registryCommon.removeAssociation(assoResourceName, assoType, resourceName2);

        assertFalse(registryCommon.checkAssociationTable(resourceName));
        assertFalse(registryCommon.checkAssociationTable(resourceName2));

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId1));

        int actionId3 = registryCommon.getActionID(resourceName2);
        assertTrue(registryCommon.deleteResource(resourceName2, actionId3));

        int actionId2 = registryCommon.getActionID(assoResourceName);
        assertTrue(registryCommon.deleteResource(assoResourceName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAddAssociationToCollection() throws Exception {

        String assoColName = "/testAddAssociationToResource899";
        String assotestColName = "/resourceName8";
        String assotype = "assoType";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(assotestColName, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addAssociationResource(assotype, assotestColName);

        registryCommon.navigateAssociationLinks(assotestColName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);

        registryCommon.clickAssociationTree();

        assertTrue(registryCommon.checkAssociationTree(assotestColName));

        registryCommon.clickAssocitionTreelinks(assotestColName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(assotestColName);
        assertTrue(registryCommon.deleteCollection(assotestColName, actionId1));

        int actionId2 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId2));

        UmCommon.logOutUI();
    }

    public void testAddMultipleAssociationToCollection() throws Exception {

        String assoColName = "/99testAddMultipleAssociationToCollection";
        String resourceNameCol1 = "/1resourceName1";
        String resourceNameCol2 = "/2resourceName2";
        String resourceNameCol3 = "/3resourceName3";

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceNameCol1, "", "");
        registryCommon.addCollectionToRoot(resourceNameCol2, "", "");
        registryCommon.addCollectionToRoot(resourceNameCol3, "", "");

        registryCommon.gotoResource(assoColName);

        registryCommon.addAssociationResource("TestType", resourceNameCol1);
        registryCommon.addAssociationResource("TestType", resourceNameCol2);
        registryCommon.addAssociationResource("TestType", resourceNameCol3);

        registryCommon.navigateAssociationLinks(resourceNameCol1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.navigateAssociationLinks(resourceNameCol2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.navigateAssociationLinks(resourceNameCol3);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.clickAssociationTree();
        assertTrue(registryCommon.checkAssociationTree(resourceNameCol1));
        registryCommon.clickAssociationTree();
        assertTrue(registryCommon.checkAssociationTree(resourceNameCol2));
        registryCommon.clickAssociationTree();
        assertTrue(registryCommon.checkAssociationTree(resourceNameCol3));

        registryCommon.clickAssocitionTreelinks(resourceNameCol1);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.clickAssociationTree();
        registryCommon.clickAssocitionTreelinks(resourceNameCol2);
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(assoColName);
        registryCommon.clickAssociationTree();
        registryCommon.clickAssocitionTreelinks(resourceNameCol3);
        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceNameCol1);
        assertTrue(registryCommon.deleteCollection(resourceNameCol1, actionId1));

        int actionId2 = registryCommon.getActionID(resourceNameCol2);
        assertTrue(registryCommon.deleteCollection(resourceNameCol2, actionId2));

        int actionId3 = registryCommon.getActionID(resourceNameCol3);
        assertTrue(registryCommon.deleteCollection(resourceNameCol3, actionId3));

        int actionId4 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId4));

        UmCommon.logOutUI();
    }

    public void testAddExternalColLinkAssociation() throws Exception {

        String assoColName = "/12testAddExternalColLinkAssociation";
        String externalLink = "http://www.google.com";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");


        registryCommon.gotoResource(assoColName);
        registryCommon.addExtenalLinkAssociation(externalLink, "ExtenalLink");

        registryCommon.clickAssociationTree();
        registryCommon.checkAssociationTree(externalLink);


        registryCommon.navigateAssociationUrls(externalLink, "Google");
        assertTrue(selenium.isTextPresent("Google"));
        Thread.sleep(1000);
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);
        registryCommon.clickAssociationTree();
        registryCommon.clickAssocitionTreelinks(externalLink);
        assertTrue(selenium.isTextPresent("Google"));
        Thread.sleep(1000);
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId1));

        UmCommon.logOutUI();
    }

    public void testRemoveCollectionAssociation() throws Exception {

        String assoColName = "/testRemoveCollectionAssociation1";
        String resourceName = "/resourceName";
        String assoType = "assoType";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceName, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addAssociationResource(assoType, resourceName);

        registryCommon.navigateAssociationLinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);

        registryCommon.clickAssociationTree();

        assertTrue(registryCommon.checkAssociationTree(resourceName));

        registryCommon.clickAssocitionTreelinks(resourceName);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);
        registryCommon.removeAssociation(assoColName, assoType, resourceName);

        registryCommon.clickRootIcon();

        int actionId1 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId1));

        int actionId2 = registryCommon.getActionID(assoColName);
        assertTrue(registryCommon.deleteCollection(assoColName, actionId2));

        UmCommon.logOutUI();
    }

    public void testRemoveMultipleCollectionAssociation() throws Exception {

        String assoColName = "/testRemoveMultipleCollectionAssociation";
        String resourceName = "/12resourceName";
        String resourceName2 = "/13resourceName";
        String assoType = "assoType";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(assoColName, "", "");
        registryCommon.addCollectionToRoot(resourceName, "", "");
        registryCommon.addCollectionToRoot(resourceName2, "", "");

        registryCommon.gotoResource(assoColName);
        registryCommon.addAssociationResource(assoType, resourceName);
        registryCommon.addAssociationResource(assoType, resourceName2);

        registryCommon.clickRootIcon();

        registryCommon.gotoResource(assoColName);
        registryCommon.removeAssociation(assoColName, assoType, resourceName);
        registryCommon.removeAssociation(assoColName, assoType, resourceName2);

        assertFalse(registryCommon.checkAssociationTable(resourceName));
        assertFalse(registryCommon.checkAssociationTable(resourceName2));

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
