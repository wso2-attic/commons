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

package org.wso2.carbon.web.test.common;


import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class RegistryBrowser extends TestCase {

    Selenium selenium;
    Properties properties = new Properties();
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    FileInputStream freader;
    String adminUserName;
    String adminPassword;


    public RegistryBrowser(Selenium _browser) throws IOException {
        selenium = _browser;
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        adminUserName = properties.getProperty("admin.username");
        adminPassword = properties.getProperty("admin.password");
    }

    public void testAddResourceFromFileSystem() throws Exception {

        registryCommon.signOut();


        String resourceName = "svn_info.txt";

        File resourcePath1 = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + resourceName);
        System.out.println(resourcePath1.getCanonicalPath());

        String resourcePath = resourcePath1.getCanonicalPath();
        String registryResourcePath = "/" + resourceName;

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        Thread.sleep(1000);
        registryCommon.addtResourceFromFileSystem(resourcePath, resourceName);

        assertTrue(registryCommon.searchResources(registryResourcePath));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(registryResourcePath, actionId));
        assertFalse(registryCommon.searchResources(registryResourcePath));
        freader.close();
        UmCommon.logOutUI();
    }

    public void testResourceImportUrl() throws Exception {
        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        String pathUrl = properties.getProperty("resourceURL");
        String resourceName = properties.getProperty("resourceURLName");
        int rowCount = (Integer.parseInt(properties.getProperty("ResourceBrowserTableRowCount")));
        String resourcePath = "/" + resourceName;

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        Thread.sleep(1000);
        registryCommon.addtResourceFromUrl(pathUrl, resourceName, rowCount);
        assertTrue(registryCommon.searchResources(resourcePath));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourcePath);
        assertTrue(registryCommon.deleteResource(resourcePath, actionId));
        assertFalse(registryCommon.searchResources(resourcePath));
        freader.close();
        UmCommon.logOutUI();
    }

    public void testAddTextResource() throws Exception {
        registryCommon.signOut();

//        SeleniumTestBase stb = new SeleniumTestBase(selenium);
        String resourceName = "/testAddTextResource";
//        stb.loginToUI("admin", "admin");
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        assertTrue(registryCommon.searchResources(resourceName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));
        assertFalse(registryCommon.searchResources(resourceName));
        UmCommon.logOutUI();
    }


    public void testAxis2repoCreation() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        String collectionName = "axis2collection";
        String mediaType = "application/vnd.apache.axis2";
        String colDesc = "axis2repo";

        registryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        registryCommon.gotoResource(collectionName);


        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(registryCommon.resourceExists("conf"));
        assertTrue(registryCommon.resourceExists("modules"));
        assertTrue(registryCommon.resourceExists("services"));

        assertTrue(registryCommon.searchResources(collectionName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionId);
        assertFalse(registryCommon.searchResources(collectionName));
        UmCommon.logOutUI();
    }

    public void testSyanpserepoCreation() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        String collectionName = "synapsecollection";
        String mediaType = "application/vnd.apache.synapse";
        String colDesc = "synapserepo";

        registryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        registryCommon.gotoResource(collectionName);


        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(registryCommon.resourceExists("conf"));
        assertTrue(registryCommon.resourceExists("endpoints"));
        assertTrue(registryCommon.resourceExists("entries"));
        assertTrue(registryCommon.resourceExists("proxy-services"));
        assertTrue(registryCommon.resourceExists("sequences"));
        assertTrue(registryCommon.resourceExists("tasks"));

        assertTrue(registryCommon.searchResources(collectionName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionId);
        assertFalse(registryCommon.searchResources(collectionName));
        UmCommon.logOutUI();
    }

    public void testEsbepoCreation() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        String collectionName = "esbcollection";
        String mediaType = "application/vnd.wso2.esb";
        String colDesc = "esbrepo";

        registryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        registryCommon.gotoResource(collectionName);


        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(registryCommon.resourceExists("conf"));
        assertTrue(registryCommon.resourceExists("endpoints"));
        assertTrue(registryCommon.resourceExists("entries"));
        assertTrue(registryCommon.resourceExists("proxy-services"));
        assertTrue(registryCommon.resourceExists("sequences"));
        assertTrue(registryCommon.resourceExists("tasks"));

        assertTrue(registryCommon.searchResources(collectionName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionId);
        assertFalse(registryCommon.searchResources(collectionName));
        UmCommon.logOutUI();
    }

    public void testwsasrepoCreation() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        String collectionName = "wsascollection";
        String mediaType = "application/vnd.wso2.wsas";
        String colDesc = "wsasrepo";

        registryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        registryCommon.gotoResource(collectionName);


        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(registryCommon.resourceExists("conf"));
        assertTrue(registryCommon.resourceExists("modules"));
        assertTrue(registryCommon.resourceExists("services"));

        assertTrue(registryCommon.searchResources(collectionName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionId);
        assertFalse(registryCommon.searchResources(collectionName));
        UmCommon.logOutUI();
    }

    public void testAddMulutipleCollections() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        String collectionName = "/col1/col2/col3/col4";
        String mediaType = "";
        String colDesc = "repo";

        registryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        registryCommon.gotoResource("col1");
        assertTrue(selenium.isTextPresent("col1"));
        registryCommon.gotoResource("col2");
        assertTrue(selenium.isTextPresent("col2"));
        registryCommon.gotoResource("col3");
        assertTrue(selenium.isTextPresent("col3"));
        registryCommon.gotoResource("col4");
        assertTrue(selenium.isTextPresent("col4"));

        Thread.sleep(1000);

        assertTrue(registryCommon.searchResources("col1"));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID("col1");
        registryCommon.deleteCollection("col1", actionId);
        assertFalse(registryCommon.searchResources("col1"));
        UmCommon.logOutUI();
    }

    public void testResoruceVersions() throws Exception {
        registryCommon.signOut();
        String resourceName = "/3testResoruceVersions123";
        String userName = "admin";
        String password = "admin";
        UmCommon.loginToUI(userName, password);

        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);

        registryCommon.gotoResource(resourceName);
        registryCommon.checkCollectionVersions(resourceName, userName);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(resourceName);
        registryCommon.createCheckpoint();
        Thread.sleep(1000);
        registryCommon.checkResourceVersions(resourceName, userName);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();

        assertTrue(registryCommon.searchResources(resourceName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));
        assertFalse(registryCommon.searchResources(resourceName));
        UmCommon.logOutUI();
    }

    public void testCollectionVersions() throws Exception {
        registryCommon.signOut();
        registryCommon.signOut();
        String resourceName = "/testCollectionVersions21";
        String userName = "admin";
        String password = "admin";
        UmCommon.loginToUI(userName, password);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(resourceName, " ", " ");
        Thread.sleep(2000);

        registryCommon.gotoResource(resourceName);
        Thread.sleep(1000);
        registryCommon.checkCollectionVersions(resourceName, userName);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.gotoResource(resourceName);
        registryCommon.createCheckpoint();
        Thread.sleep(1000);
        registryCommon.checkResourceVersions(resourceName, userName);
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();

        assertTrue(registryCommon.searchResources(resourceName));
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteCollection(resourceName, actionId));
        assertFalse(registryCommon.searchResources(resourceName));
        UmCommon.logOutUI();
    }


    public void testResourceCopy() throws Exception {
        registryCommon.signOut();
        registryCommon.signOut();
        String resourceName = "/testResourceCopy";
        String destinationPath = "/testcopyDestination";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);

        int actionId = registryCommon.getActionID(resourceName);
        registryCommon.copyResource(actionId, resourceName, destinationPath);

        int actionIdDestination = registryCommon.getActionID(destinationPath);

        registryCommon.gotoResource(destinationPath);

        registryCommon.resourceExists(resourceName);
        registryCommon.clickRootIcon();
        assertTrue(registryCommon.searchResources(resourceName));
        assertTrue(registryCommon.searchResources(destinationPath));

        registryCommon.deleteCollection(destinationPath, actionIdDestination);
        registryCommon.deleteResource(resourceName, actionId);

        assertFalse(registryCommon.searchResources(resourceName));
        assertFalse(registryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testCollectionCopy() throws Exception {
        registryCommon.signOut();
        registryCommon.signOut();
        String collectionName = "/testResourceCopy";
        String destinationPath = "/testcopyDestination";

        UmCommon.loginToUI(adminUserName, adminPassword);
        Thread.sleep(1000);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");

        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.copyResource(actionId, collectionName, destinationPath);

        int actionIdDestination = registryCommon.getActionID(destinationPath);

        registryCommon.gotoResource(destinationPath);

        registryCommon.resourceExists(collectionName);
        registryCommon.clickRootIcon();

        assertTrue(registryCommon.searchResources(collectionName));
        assertTrue(registryCommon.searchResources(destinationPath));

        registryCommon.deleteCollection(destinationPath, actionIdDestination);
        registryCommon.deleteCollection(collectionName, actionId);

        assertFalse(registryCommon.searchResources(collectionName));
        assertFalse(registryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testResourceMove() throws Exception {
        registryCommon.signOut();
        String resourceName = "/testResourceMove";
        String destinationPath = "/testResourceMoveDestination";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);

        int actionId = registryCommon.getActionID(resourceName);
        registryCommon.moveResource(actionId, resourceName, destinationPath);
        registryCommon.resourceExists(resourceName);

        int actionIdDestination = registryCommon.getActionID(destinationPath);

        registryCommon.gotoResource(destinationPath);

        registryCommon.resourceExists(resourceName);
        registryCommon.clickRootIcon();
        assertTrue(registryCommon.searchResources(resourceName));
        assertTrue(registryCommon.searchResources(destinationPath));

        registryCommon.deleteCollection(destinationPath, actionIdDestination);


        assertFalse(registryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testCollectionMove() throws Exception {
        registryCommon.signOut();
        String collectionName = "/testCollectionMove";
        String destinationPath = "/testCollectionMoveDestination";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");

        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.moveResource(actionId, collectionName, destinationPath);
        registryCommon.resourceExists(collectionName);

        int actionIdDestination = registryCommon.getActionID(destinationPath);

        registryCommon.gotoResource(destinationPath);

        registryCommon.resourceExists(collectionName);
        registryCommon.clickRootIcon();
        assertTrue(registryCommon.searchResources(collectionName));
        assertTrue(registryCommon.searchResources(destinationPath));

        registryCommon.deleteCollection(destinationPath, actionIdDestination);


        assertFalse(registryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testResourceEdit() throws Exception {

        registryCommon.signOut();
        String resourceName = "/aaatestResourceEdi12t";
        String destinationPath = "/DestinationResource12";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);

        int actionId = registryCommon.getActionID(resourceName);
        registryCommon.editResource(actionId, resourceName, destinationPath);
        registryCommon.clickRootIcon();
        assertFalse(registryCommon.resourceExists(resourceName));
        assertTrue(registryCommon.resourceExists(destinationPath));

        registryCommon.gotoResource(destinationPath);

        assertFalse(registryCommon.searchResources(resourceName));
        assertTrue(registryCommon.searchResources(destinationPath));

        registryCommon.gotoResourcePage();
        int actionIdDestination = registryCommon.getActionID(destinationPath);
        registryCommon.deleteResource(destinationPath, actionIdDestination);

        assertFalse(registryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testCollectionEdit() throws Exception {

        registryCommon.signOut();
        String collectionName = "/aaatestCollectionEdit";
        String destinationPath = "DestinationCollection";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");

        int actionId = registryCommon.getActionID(collectionName);
        registryCommon.editCollection(actionId, destinationPath, collectionName);
        Thread.sleep(1000);
        registryCommon.clickRootIcon();

        assertFalse(registryCommon.resourceExists(collectionName));
        assertTrue(registryCommon.resourceExists(destinationPath));

        registryCommon.gotoResource(destinationPath);

        assertFalse(registryCommon.searchResources(collectionName));
        assertTrue(registryCommon.searchResources(destinationPath));

        registryCommon.gotoResourcePage();

        int actionIdDestination = registryCommon.getActionID(destinationPath);
        registryCommon.deleteCollection(destinationPath, actionIdDestination);

        assertFalse(registryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }


    /* add a symlink to registry root*/
    public void testSymlinkcreationRoot() throws Exception {

        registryCommon.signOut();
        String resourceName = "/testSymlinkcreation";
        String symlinkName = "/symlinkName";
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.createSymlinktoRootResource(symlinkName, resourceName);
        registryCommon.gotoResource(symlinkName);
        registryCommon.clickRootIcon();
        registryCommon.searchResources(resourceName);
        registryCommon.searchResources(symlinkName);

        registryCommon.gotoResourcePage();

        int actionIdResource = registryCommon.getActionID(resourceName);
        registryCommon.deleteResource(resourceName, actionIdResource);

        int actionIdSym = registryCommon.getActionID(symlinkName);
        registryCommon.deleteResource(symlinkName, actionIdSym);

        assertFalse(registryCommon.searchResources(resourceName));
        assertFalse(registryCommon.searchResources(symlinkName));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testSymlinkCollectiontoRoot() throws Exception {

        registryCommon.signOut();
        String collectionName = "/testSymlinkCollectiontoRoot";
        String symlinkName = "/symlinkCollection";
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");
        registryCommon.createSymlinktoRootResource(symlinkName, collectionName);
        registryCommon.gotoResource(symlinkName);
        registryCommon.clickRootIcon();
        registryCommon.searchResources(collectionName);
        registryCommon.searchResources(symlinkName);

        registryCommon.gotoResourcePage();
        int actionIdSym = registryCommon.getActionID(symlinkName);
        registryCommon.deleteCollection(symlinkName, actionIdSym);
        int actionIdResource = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdResource);


        assertFalse(registryCommon.searchResources(collectionName));
        assertFalse(registryCommon.searchResources(symlinkName));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testSymlinkcommunityFeatures() throws Exception {

        registryCommon.signOut();
        String resourceName = "/testSymlinkcommunityFeatures";
        String symlinkName = "/symlinkName";
        String tag1Value = "tag1Sym";
        String tag2Value = "tag2Sym";
        String propertyKey1 = "key1";
        String propertyKey2 = "key2";
        String propertyValue1 = "value1";
        String propertyValue2 = "value2";
        String comment1Value = "Test sym link1";
        String comment2Value = "Test sym link2";
        String userName = "admin";
        int ratingValue = 5;
        int averageRating = 5;
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.gotoResource(resourceName);
        registryCommon.addProperty(propertyKey1, propertyValue1);
        registryCommon.addProperty(propertyKey2, propertyValue2);
        registryCommon.addComment(comment1Value, userName, resourceName);
        selenium.click("commentsIconExpanded");
        registryCommon.addComment(comment2Value, userName, resourceName);
        registryCommon.addTags(tag1Value + "," + tag2Value + ",", "tfTag");
        registryCommon.rateResource(resourceName, ratingValue, averageRating);

        registryCommon.clickRootIcon();

        registryCommon.createSymlinktoRootResource(symlinkName, resourceName);
        registryCommon.gotoResource(symlinkName);

        registryCommon.checkProperties(propertyKey1, propertyValue1);
        registryCommon.checkProperties(propertyKey2, propertyValue2);
        registryCommon.checkComments(comment1Value, userName);
        registryCommon.checkComments(comment2Value, userName);
        registryCommon.checkRating(ratingValue, averageRating);
        registryCommon.checkTags(tag1Value);
        registryCommon.checkTags(tag2Value);

        registryCommon.clickRootIcon();
        registryCommon.searchResources(resourceName);
        registryCommon.searchResources(symlinkName);

        registryCommon.searchSimpleTag(resourceName, tag1Value);
        registryCommon.searchSimpleTag(resourceName, tag2Value);

        registryCommon.searchSimpleCommet(resourceName, comment1Value);
        registryCommon.searchSimpleCommet(resourceName, comment2Value);

        registryCommon.searchProproties(propertyKey1, propertyValue1, resourceName, userName);
        registryCommon.searchProproties(propertyKey2, propertyValue2, resourceName, userName);

        registryCommon.gotoResourcePage();
        int actionIdSym = registryCommon.getActionID(symlinkName);
        registryCommon.deleteResource(symlinkName, actionIdSym);
        int actionIdResource = registryCommon.getActionID(resourceName);
        registryCommon.deleteResource(resourceName, actionIdResource);


        assertFalse(registryCommon.searchResources(resourceName));
        assertFalse(registryCommon.searchResources(symlinkName));

        assertFalse(registryCommon.searchSimpleCommet(resourceName, comment1Value));
        assertFalse(registryCommon.searchSimpleCommet(resourceName, comment2Value));

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testGuestUser() throws Exception {

        registryCommon.signOut();
        String collectionName = "/testGuestUserCollection";

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        String resourceName = "/testGuestUserResource";

        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.gotoResource(resourceName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.clickRootIcon();

        registryCommon.addCollectionToRoot(collectionName, "", "");
        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.clickRootIcon();

        String symlinkName = "/testSymLinkName";

        registryCommon.createSymlinktoRootResource(symlinkName, resourceName);
        registryCommon.gotoResource(symlinkName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.gotoResourcePage();

        int actionIdResource = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdResource);

        int actionIdSym = registryCommon.getActionID(symlinkName);
        registryCommon.deleteResource(symlinkName, actionIdSym);

        int actionIdRes = registryCommon.getActionID(resourceName);
        registryCommon.deleteResource(resourceName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessHttps() throws Exception {


        String constructedUrl = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";
        System.out.println("XXX" + url);

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrl = url + "registry/resource";
        } else {
            constructedUrl = url + "/" + context_root + "/registry/resource";
        }

        System.out.println("XXX- Constructeted " + constructedUrl);


        String collectionName = "/testGuestUserCollection";


        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        String resourceName = "/testGuestUserResource";
        String resourceName2 = "/testGuestUserResource2";


        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.gotoResource(resourceName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        selenium.open(constructedUrl + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        System.out.println("the URL is " + constructedUrl + resourceName);
        registryCommon.clickRootIcon();

        registryCommon.addCollectionToRoot(collectionName, "", "");
        registryCommon.gotoResource(collectionName);
        registryCommon.addTextResource(resourceName2);
        registryCommon.gotoResource(resourceName2);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        selenium.open(constructedUrl + collectionName + resourceName2);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.clickRootIcon();

        String symlinkName = "/testSymLinkName";

        registryCommon.createSymlinktoRootResource(symlinkName, resourceName);
        registryCommon.gotoResource(symlinkName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        selenium.open(constructedUrl + symlinkName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.gotoResourcePage();

        int actionIdResource = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdResource);

        int actionIdSym = registryCommon.getActionID(symlinkName);
        registryCommon.deleteResource(symlinkName, actionIdSym);

        int actionIdRes = registryCommon.getActionID(resourceName);
        registryCommon.deleteResource(resourceName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessHttp() throws Exception {


        String constructedUrl = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        System.out.println("XXX" + url);

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrl = url + "registry/resource";
        } else {
            constructedUrl = url + "/" + context_root + "/registry/resource";
        }

        System.out.println("XXX- Constructeted " + constructedUrl);


        String collectionName = "/testGuestUserCollection";


        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        String resourceName = "/testGuestUserResource";
        String resourceName2 = "/testGuestUserResource2";


        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.gotoResource(resourceName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        selenium.open(constructedUrl + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        System.out.println("the URL is " + constructedUrl + resourceName);

        registryCommon.addCollectionToRoot(collectionName, "", "");
        registryCommon.gotoResource(collectionName);
        registryCommon.addTextResource(resourceName2);
        registryCommon.gotoResource(resourceName2);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        selenium.open(constructedUrl + collectionName + resourceName2);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        String symlinkName = "/testSymLinkName";

        registryCommon.createSymlinktoRootResource(symlinkName, resourceName);
        registryCommon.gotoResource(symlinkName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        selenium.open(constructedUrl + symlinkName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        int actionIdResource = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdResource);

        int actionIdSym = registryCommon.getActionID(symlinkName);
        registryCommon.deleteResource(symlinkName, actionIdSym);

        int actionIdRes = registryCommon.getActionID(resourceName);
        registryCommon.deleteResource(resourceName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnymousAccessSystem() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" + context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/SystemResource";
        registryCommon.gotoResourcePage();
        registryCommon.gotoResource("/system");

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.addTextResource(resourceName);
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.open(constructedUrlhttps + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        registryCommon.gotoResource("/system");

//        int actionIdRes = registryCommon.getActionID(resourceName);
//        registryCommon.deleteResource(resourceName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessSystemSetPermission() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        String content1 = "Mycontent1";
        String content2 = "Mycontent2";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" + context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/testAnoumousAccessSystemSetPermission";
        registryCommon.gotoResourcePage();
        registryCommon.gotoResource("/system");

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.addTextResourceWithContent(resourceName, content1, "");
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + "/system" + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        registryCommon.gotoResource("/system");

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + "/system" + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + "/system" + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        selenium.windowFocus();

        registryCommon.signOut();
        Thread.sleep(2000);
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.gotoResource("/system");

        String myNewResource = "/myNewResource";
        registryCommon.addTextResourceWithContent(myNewResource, content2, "");

        selenium.open(constructedUrlhttp + "/system" + myNewResource);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + "/system" + myNewResource);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.gotoResource("/system");

        int actionIdRes = registryCommon.getActionID(resourceName);
        //registryCommon.deleteResource(resourceName, actionIdRes);
        registryCommon.deleteResource("/system", resourceName);

        int actionIdRes1 = registryCommon.getActionID(myNewResource);
        //registryCommon.deleteResource(myNewResource, actionIdRes1);
        registryCommon.deleteResource("/system", myNewResource);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessSetPermissionForResource() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        String content1 = "CollectionContent1";
        String content2 = "CollectionContent2";
        String collectionName = "/permissionCollection";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" + context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/testAnoumousAccessSystemSetPermission";
        registryCommon.gotoResourcePage();

        registryCommon.addCollectionToRoot(collectionName, "", "");

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.addTextResourceWithContent(resourceName, content1, "");
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        selenium.windowFocus();

        registryCommon.signOut();
        Thread.sleep(2000);
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.gotoResource(collectionName);

        String myNewResource = "/myNewResource";
        registryCommon.addTextResourceWithContent(myNewResource, content2, "");

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        int actionIdRes = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessSetPermissionDeny() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        String content1 = "CollectionContent11";
        String content2 = "CollectionContent12";
        String collectionName = "/testAnonymousAccessSetPermissionDeny123";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" + context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/testAnoumousAccessSetPermissionDeny";
        registryCommon.gotoResourcePage();

        registryCommon.addCollectionToRoot(collectionName, "", "");

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.addTextResourceWithContent(resourceName, content1, "");
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        selenium.windowFocus();

        registryCommon.signOut();
        Thread.sleep(2000);
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        String myNewResource = "/myNewResource";
        registryCommon.addTextResourceWithContent(myNewResource, content2, "");

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        int actionIdRes = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessSetPermissionDenyRole() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        String content1 = "CollectionContent113";
        String content2 = "CollectionContent124";
        String collectionName = "/permissionCollectionDenyRole";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" +  context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/testAnoumousAccessSetPermissionDeny";
        registryCommon.gotoResourcePage();

        registryCommon.addCollectionToRoot(collectionName, "", "");

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.addTextResourceWithContent(resourceName, content1, "");
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('" + collectionName + "')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        selenium.windowFocus();

        registryCommon.signOut();
        Thread.sleep(2000);
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();
        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('" + collectionName + "')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        String myNewResource = "/myNewResource";
        registryCommon.addTextResourceWithContent(myNewResource, content2, "");

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        int actionIdRes = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdRes);

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessSetPermissionToRoot() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        String content1 = "CollectionContent113";
        String content2 = "CollectionContent124";
        String collectionName = "/testAnoumousAccessSetPermissionToRoot";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" + context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/testAnoumousAccessSetPermissionDeny";
        registryCommon.gotoResourcePage();

        registryCommon.addCollectionToRoot(collectionName, "", "");

         selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('/')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);


        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        registryCommon.gotoResource(collectionName);

        registryCommon.addTextResourceWithContent(resourceName, content1, "");
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('/')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        selenium.windowFocus();

        registryCommon.signOut();
        Thread.sleep(2000);
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('/')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        registryCommon.gotoResource(collectionName);

        String myNewResource = "/myNewResource";
        registryCommon.addTextResourceWithContent(myNewResource, content2, "");

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        int actionIdRes = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdRes);

        Thread.sleep(1000);

//        selenium.click("perIconMinimized");
//        Thread.sleep(2000);
//        selenium.click("guests^rra");
//        Thread.sleep(2000);
//        selenium.click("//div[@id='rolePermisionsDiv']/form[2]/table/tbody/tr[5]/td/input");
//        Thread.sleep(2000);
//        selenium.click("//button[@type='button']");


        Thread.sleep(2000);
        UmCommon.logOutUI();
    }

    public void testAnonymousAccessSetPermissionToRootUser() throws Exception {


        String constructedUrlhttp = "";
        String constructedUrlhttps = "";
        String content1 = "CollectionContent1134";
        String content2 = "CollectionContent1244";
        String collectionName = "/testAnoumousAccessSetPermissionToRootUser";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();


        FileInputStream freaderIo = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freaderIo);

        String http_url = "http://" + properties.getProperty("host.name") + ":" + properties.getProperty("http.port") + "";
        String https_url = "https://" + properties.getProperty("host.name") + ":" + properties.getProperty("https.port") + "";

        String context_root = properties.getProperty("context.root");


        if (context_root.equals(null)) {
            constructedUrlhttp = http_url + "registry/resource";
            constructedUrlhttps = https_url + "registry/resource";
        } else {
            constructedUrlhttp = http_url + "/" + context_root + "/registry/resource";
            constructedUrlhttps = https_url + "/" + context_root + "/registry/resource";
        }

        String resourceName = "/testAnoumousAccessSetPermissionDeny";
        registryCommon.gotoResourcePage();

        registryCommon.addCollectionToRoot(collectionName, "", "");

        registryCommon.gotoResource(collectionName);

        selenium.click("perIconMinimized");
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("guests"));

        registryCommon.addTextResourceWithContent(resourceName, content1, "");
        registryCommon.gotoResource(resourceName);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.open(constructedUrlhttps + collectionName + resourceName);
        assertFalse(selenium.isTextPresent("HTTP Status 500"));
        assertTrue(selenium.isTextPresent(content1));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");
        selenium.windowFocus();

        registryCommon.signOut();
        Thread.sleep(2000);
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        registryCommon.gotoResource(collectionName);

        String myNewResource = "/myNewResource";
        registryCommon.addTextResourceWithContent(myNewResource, content2, "");

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        registryCommon.gotoResource(collectionName);
        registryCommon.gotoResource(myNewResource);

        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionDeny");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('" + collectionName + myNewResource + "')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=guests");
        selenium.select("roleActionToAuthorize", "label=Read");
        selenium.click("rolePermissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('/')\"]");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);


        selenium.select("userToAuthorize", "label=guest");
        selenium.select("actionToAuthorize", "label=Read");
        selenium.click("permissionAllow");
        Thread.sleep(2000);
        selenium.click("//input[@value='Add Permission']");
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);

        selenium.open(constructedUrlhttp + collectionName + myNewResource);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.open(constructedUrlhttps + collectionName + myNewResource);
        assertTrue(selenium.isTextPresent("HTTP Status 500"));
        assertFalse(selenium.isTextPresent(content2));

        selenium.goBack();
        selenium.goBack();
        selenium.waitForPageToLoad("30000");

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        int actionIdRes = registryCommon.getActionID(collectionName);
        registryCommon.deleteCollection(collectionName, actionIdRes);

        Thread.sleep(1000);

//        selenium.click("perIconMinimized");
//        Thread.sleep(2000);
//        selenium.click("guests^rra");
//        Thread.sleep(2000);
//        selenium.click("//div[@id='rolePermisionsDiv']/form[2]/table/tbody/tr[5]/td/input");
//        Thread.sleep(2000);
//        selenium.click("//button[@type='button']");


        Thread.sleep(2000);
        UmCommon.logOutUI();
    }
}

