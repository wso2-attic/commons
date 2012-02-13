package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryBrowser;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


public class ManageRegistryBrowserTest extends TestCase {

    Selenium browser;
    RegistryBrowser registryBrowser;

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        registryBrowser = new RegistryBrowser(browser);
    }


    public void testRegistryBrowserTextResources() throws Exception {

        registryBrowser.testAddTextResource();
    }

    public void testRegistryBrowserURLResources() throws Exception {

        registryBrowser.testResourceImportUrl();
    }

    public void testRegistryBrowserFileSystemResources() throws Exception {

        registryBrowser.testAddResourceFromFileSystem();
    }

    /* These handlers are not present in WSAS. Therefore the following tests are not applicable w.r.t WSAS*/

//    public void testRegistryBrowserAxis2RepoCreation() throws Exception {
//
//        registryBrowser.testAxis2repoCreation();
//    }
//
//    public void testRegistryBrowserSynapseRepoCreation() throws Exception {
//
//        registryBrowser.testSyanpserepoCreation();
//    }
//
//    public void testRegistryBrowserESBRepoCreation() throws Exception {
//
//        registryBrowser.testEsbepoCreation();
//    }
//
//    public void testRegistryBrowserWSASRepoCreation() throws Exception {
//
//        registryBrowser.testwsasrepoCreation();
//    }

    public void testRegistryBrowserAddMultipleCollections() throws Exception {

        registryBrowser.testAddMulutipleCollections();
    }

    public void testRegistryBrowserResourceVersions() throws Exception {

        registryBrowser.testResoruceVersions();
    }

    public void testRegistryBrowserCollectionVersions() throws Exception {

        registryBrowser.testCollectionVersions();
    }

    public void testRegistryBrowserResourcesCopy() throws Exception {

        registryBrowser.testResourceCopy();
    }

    public void testRegistryBrowserCollectionCopy() throws Exception {

        registryBrowser.testCollectionCopy();
    }

    public void testRegistryBrowserResourceMove() throws Exception {

        registryBrowser.testResourceMove();
    }

    public void testRegistryBrowserCollectionMove() throws Exception {

        registryBrowser.testCollectionMove();
    }

    public void testRegistryBrowserResourcesEdit() throws Exception {

        registryBrowser.testResourceEdit();
    }

    public void testRegistryBrowserCollectionEdit() throws Exception {

        registryBrowser.testCollectionEdit();
    }

    public void testRegistryBrowserSymlinkCreationRoot() throws Exception {

        registryBrowser.testSymlinkcreationRoot();
    }

    public void testRegistryBrowserSymlinkCollectiontoRoot() throws Exception {

        registryBrowser.testSymlinkCollectiontoRoot();
    }

    public void testRegistryBrowserSymlinkcommunityFeatures() throws Exception {

        registryBrowser.testSymlinkcommunityFeatures();
    }

    //The following tests have been added to verify the recent everyone role read permission security fix

    public void testRegistryBrowserGuestUser() throws Exception {
        registryBrowser.testGuestUser();
    }

    public void testRegistryBrowserAnonymousAccessHttps() throws Exception {
        registryBrowser.testAnonymousAccessHttps();
    }

    public void testRegistryBrowserAnonymousAccessHttp() throws Exception {
        registryBrowser.testAnonymousAccessHttp();
    }

    public void testRegistryBrowserAnonymousAccessSystem() throws Exception {
        registryBrowser.testAnymousAccessSystem();
    }

    public void testRegistryBrowserAnonymousAccessSystemSetPermission() throws Exception {
        registryBrowser.testAnonymousAccessSystemSetPermission();
    }

    public void testRegistryBrowserAnonymousAccessSetPermissionForResource() throws Exception {
        registryBrowser.testAnonymousAccessSetPermissionForResource();
    }

    public void testRegistryBrowserAnonymousAccessSetPermissionDeny() throws Exception {
        registryBrowser.testAnonymousAccessSetPermissionDeny();
    }

    public void testRegistryBrowserAnonymousAccessSetPermissionDenyRole() throws Exception {
        registryBrowser.testAnonymousAccessSetPermissionDenyRole();
    }

    public void testRegistryBrowserAnonymousAccessSetPermissionToRootUser() throws Exception {
        registryBrowser.testAnonymousAccessSetPermissionToRootUser();
    }

    public void testRegistryBrowserAnonymousAccessSetPermissionToRoot() throws Exception {
        registryBrowser.testAnonymousAccessSetPermissionToRoot();
    }


    

}
