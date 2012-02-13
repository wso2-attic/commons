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

public class RegistryBrowser extends CommonSetup {

    public RegistryBrowser(String text) {
        super(text);
    }

    public void testAddResourceFrom() throws Exception {

        String path = PropertyReader.loadRegistryProperties().getProperty("localFileSystemResourcePath");
        String resourceName = PropertyReader.loadRegistryProperties().getProperty("localFileSystemResourceName");
        String resourcePath = "/" + resourceName;

        int rowCount = (Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("ResourceBrowserTableRowCount")));

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        Thread.sleep(1000);
        RegistryCommon.addtResourceFromFileSystem(path, resourceName, rowCount);

        assertTrue(RegistryCommon.searchResources(resourcePath));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(resourceName);
        assertTrue(RegistryCommon.deleteResource(resourcePath, actionId));
        assertFalse(RegistryCommon.searchResources(resourcePath));

        UserManagerCommon.logOutUi();
    }

//    public void testResourceImportUrl() throws Exception {
//
//        String pathUrl = PropertyReader.loadRegistryProperties().getProperty("resourceURL");
//        String resourceName = PropertyReader.loadRegistryProperties().getProperty("resourceURLName");
//        int rowCount = (Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("ResourceBrowserTableRowCount")));
//        String resourcePath = "/" + resourceName;
//
//        UserManagerCommon.loginToUi("admin", "admin");
//        RegistryCommon.gotoResourcePage();
//        Thread.sleep(1000);
//        RegistryCommon.addtResourceFromUrl(pathUrl, resourceName, rowCount);
//        assertTrue(RegistryCommon.searchResources(resourcePath));
//        RegistryCommon.gotoResourcePage();
//        RegistryCommon.clickRootIcon();
//        assertTrue(RegistryCommon.deleteResource(resourcePath, 3));
//        assertFalse(RegistryCommon.searchResources(resourcePath));
//        UserManagerCommon.logOutUi();
//    }

    public void testAddTextResource() throws Exception {

        String resourceName = "/testAddTextResource";
        UserManagerCommon.loginToUi("admin", "admin");

        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        assertTrue(RegistryCommon.searchResources(resourceName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(resourceName);
        assertTrue(RegistryCommon.deleteResource(resourceName, actionId));
        assertFalse(RegistryCommon.searchResources(resourceName));
        UserManagerCommon.logOutUi();
    }

    public void testAddCutomMediaResource() throws Exception {

        String mediatype = "application/vnd.wso2-mex+xml";
        String wsdlURL = PropertyReader.loadRegistryProperties().getProperty("wsdlURL");
        String wsdlName = PropertyReader.loadRegistryProperties().getProperty("wsdlName");
        String owner = "Owner";
        String telephone = "1234567";
        String email = "wsdl@wso2.com";
        String wsdlDescription = "Test Wsdl description";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();

        RegistryCommon.importWsldMexTool(mediatype, wsdlURL, wsdlName, owner, telephone, email, wsdlDescription);

        assertTrue(RegistryCommon.searchResources(wsdlName + "Service"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(wsdlName + "Service");
        assertTrue(RegistryCommon.deleteCollection(wsdlName + "Service", actionId));
        assertFalse(RegistryCommon.searchResources("/" + wsdlName + "Service"));
        UserManagerCommon.logOutUi();
    }

    public void testAxis2repoCreation() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();

        String collectionName = "axis2collection";
        String mediaType = "application/vnd.apache.axis2";
        String colDesc = "axis2repo";

        RegistryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        selenium.click("resourceView1");
        selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(RegistryCommon.resourceExists("conf"));
        assertTrue(RegistryCommon.resourceExists("modules"));
        assertTrue(RegistryCommon.resourceExists("services"));

        assertTrue(RegistryCommon.searchResources(collectionName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(collectionName);
        RegistryCommon.deleteCollection(collectionName, actionId);
        assertFalse(RegistryCommon.searchResources(collectionName));
        UserManagerCommon.logOutUi();
    }

    public void testSyanpserepoCreation() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();

        String collectionName = "synapsecollection";
        String mediaType = "application/vnd.apache.synapse";
        String colDesc = "synapserepo";

        RegistryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);
        RegistryCommon.gotoResource(collectionName);


        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(RegistryCommon.resourceExists("conf"));
        assertTrue(RegistryCommon.resourceExists("endpoints"));
        assertTrue(RegistryCommon.resourceExists("entries"));
        assertTrue(RegistryCommon.resourceExists("proxy-services"));
        assertTrue(RegistryCommon.resourceExists("sequences"));
        assertTrue(RegistryCommon.resourceExists("tasks"));

        assertTrue(RegistryCommon.searchResources(collectionName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(collectionName);
        RegistryCommon.deleteCollection(collectionName, actionId);
        assertFalse(RegistryCommon.searchResources(collectionName));
        UserManagerCommon.logOutUi();
    }

    public void testEsbepoCreation() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();

        String collectionName = "esbcollection";
        String mediaType = "application/vnd.apache.esb";
        String colDesc = "synapserepo";

        RegistryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);
        RegistryCommon.gotoResource(collectionName);


        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(RegistryCommon.resourceExists("conf"));
        assertTrue(RegistryCommon.resourceExists("endpoints"));
        assertTrue(RegistryCommon.resourceExists("entries"));
        assertTrue(RegistryCommon.resourceExists("proxy-services"));
        assertTrue(RegistryCommon.resourceExists("sequences"));
        assertTrue(RegistryCommon.resourceExists("tasks"));

        assertTrue(RegistryCommon.searchResources(collectionName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(collectionName);
        RegistryCommon.deleteCollection(collectionName, actionId);
        assertFalse(RegistryCommon.searchResources(collectionName));
        UserManagerCommon.logOutUi();
    }

    public void testwsasrepoCreation() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();

        String collectionName = "wsascollection";
        String mediaType = "application/vnd.apache.wsas";
        String colDesc = "axis2repo";

        RegistryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        RegistryCommon.gotoResource(collectionName);

        assertTrue(selenium.isTextPresent(collectionName));
        assertTrue(selenium.isTextPresent(mediaType));
        assertTrue(selenium.isTextPresent(collectionName));

        Thread.sleep(1000);
        assertTrue(RegistryCommon.resourceExists("conf"));
        assertTrue(RegistryCommon.resourceExists("modules"));
        assertTrue(RegistryCommon.resourceExists("services"));

        assertTrue(RegistryCommon.searchResources(collectionName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(collectionName);
        RegistryCommon.deleteCollection(collectionName, actionId);
        assertFalse(RegistryCommon.searchResources(collectionName));
        UserManagerCommon.logOutUi();
    }

    public void testAddMulutipleCollections() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();

        String collectionName = "/col1/col2/col3/col4";
        String mediaType = "";
        String colDesc = "axis2repo";

        RegistryCommon.addCollectionToRoot(collectionName, mediaType, colDesc);

        RegistryCommon.gotoResource("col1");
        assertTrue(selenium.isTextPresent("col1"));
        RegistryCommon.gotoResource("col2");
        assertTrue(selenium.isTextPresent("col2"));
        RegistryCommon.gotoResource("col3");
        assertTrue(selenium.isTextPresent("col3"));
        RegistryCommon.gotoResource("col4");
        assertTrue(selenium.isTextPresent("col4"));

        Thread.sleep(1000);

        assertTrue(RegistryCommon.searchResources("col1"));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID("col1");
        RegistryCommon.deleteCollection("col1", actionId);
        assertFalse(RegistryCommon.searchResources("col1"));
        UserManagerCommon.logOutUi();
    }

    public void testResoruceVersions() throws Exception {

        String resourceName = "/testResoruceVersions";
        String userName = "admin";
        String password = "admin";
        UserManagerCommon.loginToUi(userName, password);

        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);

        RegistryCommon.gotoResource(resourceName);
        RegistryCommon.checkResourceVersions(resourceName, userName);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.gotoResource(resourceName);
        RegistryCommon.createCheckpoint(resourceName);
        Thread.sleep(1000);
        RegistryCommon.checkResourceVersions(resourceName, userName);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();

        assertTrue(RegistryCommon.searchResources(resourceName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(resourceName);
        assertTrue(RegistryCommon.deleteResource(resourceName, actionId));
        assertFalse(RegistryCommon.searchResources(resourceName));
        UserManagerCommon.logOutUi();
    }

    public void testCollectionVersions() throws Exception {

        RegistryCommon.signOut();
        String resourceName = "/testCollectionVersions";
        String userName = "admin";
        String password = "admin";
        UserManagerCommon.loginToUi(userName, password);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(resourceName, " ", " ");
        Thread.sleep(1000);

        RegistryCommon.gotoResource(resourceName);
        RegistryCommon.checkCollectionVersions(resourceName, userName);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        RegistryCommon.gotoResource(resourceName);
        RegistryCommon.createCheckpoint(resourceName);
        Thread.sleep(1000);
        RegistryCommon.checkResourceVersions(resourceName, userName);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();

        assertTrue(RegistryCommon.searchResources(resourceName));
        RegistryCommon.gotoResourcePage();
        RegistryCommon.clickRootIcon();
        int actionId = RegistryCommon.getActionID(resourceName);
        assertTrue(RegistryCommon.deleteCollection(resourceName, actionId));
        assertFalse(RegistryCommon.searchResources(resourceName));
        UserManagerCommon.logOutUi();
    }


    public void testResourceCopy() throws Exception {

        RegistryCommon.signOut();
        String resourceName = "/testResourceCopy";
        String destinationPath = "/testcopyDestination";

        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addTextResourceToRoot(resourceName);

        int actionId = RegistryCommon.getActionID(resourceName);
        RegistryCommon.copyResource(actionId, resourceName, destinationPath);

        int actionIdDestination = RegistryCommon.getActionID(destinationPath);

        selenium.click("resourceView" + actionIdDestination);
		selenium.waitForPageToLoad("30000");
        
        RegistryCommon.resourceExists(resourceName);
        RegistryCommon.clickRootIcon();
        assertTrue(RegistryCommon.searchResources(resourceName));
        assertTrue(RegistryCommon.searchResources(destinationPath));

        RegistryCommon.deleteCollection(destinationPath, actionIdDestination);
        RegistryCommon.deleteResource(resourceName, actionId);

        assertFalse(RegistryCommon.searchResources(resourceName));
        assertFalse(RegistryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UserManagerCommon.logOutUi();
	}

    public void testCollectionCopy() throws Exception {

        RegistryCommon.signOut();
        String collectionName = "/testResourceCopy";
        String destinationPath = "/testcopyDestination";

        UserManagerCommon.loginToUi("admin", "admin");
        Thread.sleep(1000);
        RegistryCommon.gotoResourcePage();
        RegistryCommon.addCollectionToRoot(collectionName ,"", "");

        int actionId = RegistryCommon.getActionID(collectionName);
        RegistryCommon.copyResource(actionId, collectionName, destinationPath);

        int actionIdDestination = RegistryCommon.getActionID(destinationPath);

        selenium.click("resourceView" + actionIdDestination);
		selenium.waitForPageToLoad("30000");

        RegistryCommon.resourceExists(collectionName);
        RegistryCommon.clickRootIcon();

        assertTrue(RegistryCommon.searchResources(collectionName));
        assertTrue(RegistryCommon.searchResources(destinationPath));

        RegistryCommon.deleteCollection(destinationPath, actionIdDestination);
        RegistryCommon.deleteCollection(collectionName, actionId);

        assertFalse(RegistryCommon.searchResources(collectionName));
        assertFalse(RegistryCommon.searchResources(destinationPath));

        Thread.sleep(1000);
        UserManagerCommon.logOutUi();
	}
}

