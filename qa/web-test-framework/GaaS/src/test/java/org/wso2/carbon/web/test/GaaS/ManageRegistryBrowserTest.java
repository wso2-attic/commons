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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryBrowser;

public class ManageRegistryBrowserTest extends TestCase {

    Selenium browser;
    RegistryBrowser registryBrowser;

    public ManageRegistryBrowserTest(String s) {
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        registryBrowser = new RegistryBrowser(browser);
    }

    public void testAddResourceFromFileSystem() throws Exception {
        registryBrowser.testAddResourceFromFileSystem();
    }

    public void testAddMulutipleCollections() throws Exception {
        registryBrowser.testAddMulutipleCollections();
    }

    public void testAddTextResource() throws Exception {
        registryBrowser.testAddTextResource();
    }

    public void testAxis2repoCreation() throws Exception {
        registryBrowser.testAxis2repoCreation();
    }

    public void testCollectionCopy() throws Exception {
        registryBrowser.testCollectionCopy();
    }

    public void testCollectionEdit() throws Exception {
        registryBrowser.testCollectionEdit();
    }

    public void testCollectionMove() throws Exception {
        registryBrowser.testCollectionMove();
    }

    public void testCollectionVersions() throws Exception {
        registryBrowser.testCollectionVersions();
    }

    public void testEsbepoCreation() throws Exception {
        registryBrowser.testEsbepoCreation();
    }

    public void testSynapseepoCreation() throws Exception {
        registryBrowser.testSyanpserepoCreation();
    }

    public void testResoruceVersions() throws Exception {
        registryBrowser.testResoruceVersions();
    }

    public void testResourceCopy() throws Exception {
        registryBrowser.testResourceCopy();

    }

    public void testResourceEdit() throws Exception {
        registryBrowser.testResourceEdit();
    }

    public void testResourceImportUrl() throws Exception {
        registryBrowser.testResourceImportUrl();
    }

    public void testResourceMove() throws Exception {
        registryBrowser.testResourceMove();
    }

    public void testSyanpserepoCreation() throws Exception {
        registryBrowser.testSyanpserepoCreation();
    }

    public void testSymlinkCollectiontoRoot() throws Exception {
        registryBrowser.testSymlinkCollectiontoRoot();
    }

    public void testSymlinkcommunityFeatures() throws Exception {
        registryBrowser.testSymlinkcommunityFeatures();
    }

    public void testSymlinkcreationRoot() throws Exception {
        registryBrowser.testSymlinkcreationRoot();
    }

    public void testwsasrepoCreation() throws Exception {
        registryBrowser.testwsasrepoCreation();
    }
}


