package org.wso2.carbon.web.test.wsas;

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

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

public class KeyStoreTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;

    public KeyStoreTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    //Login to admin console and test Logging.
    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);
    }

    /* Tests the UI in KS Management page. */
    public void testVerifyKeystorePage() throws Exception {
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Key Store Management"));
        assertTrue(browser.isTextPresent("Name"));
        assertTrue(browser.isTextPresent("Type"));
        assertTrue(browser.isTextPresent("Actions"));
        assertTrue(browser.isTextPresent("Add New Key store"));
        assertTrue(browser.isTextPresent("Help"));
    }


    public void testAddKeyStore()throws Exception
    {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        // instKeyStoreManagement.AddKeystore(".." + File.separator + "wsas" + File.separator + "lib" + File.separator + "service.jks","testing");
        File path = new File(".." + File.separator + "wsas" + File.separator + "lib" + File.separator + "service.jks");

        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "testing");
    }

    public void testImportCertificate()throws Exception
    {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        // instKeyStoreManagement.ImportCertificate(".." + File.separator + "wsas" + File.separator + "lib" + File.separator + "chamara.cert","chamara.cert");
        File path = new File(".." + File.separator + "wsas" + File.separator + "lib" + File.separator + "chamara.cert");

        instKeyStoreManagement.ImportCertificate(path.getCanonicalPath(), "chamara.cert");
    }

    public void testDeleteKeystore()throws Exception
    {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        instKeyStoreManagement.deleteKeyStore("service.jks");
    }

    /* Tests the UI validations in 'Import Certification' */
    public void testInvalidCertificate() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");
        browser.click("link=Import Cert");
        browser.waitForPageToLoad("30000");

        //Certificate should be marked as a mandatory field
        assertTrue(browser.isTextPresent("Certificate *"));

        //Submission without giving the certificate should be validated
        browser.click("//input[@value='Import']");
        assertTrue(browser.isElementPresent("messagebox-warning"));
        assertTrue(browser.isTextPresent("Please specify a path before importing certificate"));
        browser.click("//button[@type='button']");

        //Invalid certificate should be validated
        File path = new File(".." + File.separator + "wsas" + File.separator + "lib" + File.separator + "PojoService.class");
        instSeleniumTestBase.SetFileBrowse("browseField",path.getCanonicalPath());
        browser.click("//input[@value='Import']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isElementPresent("messagebox-error"));
        assertTrue(browser.isTextPresent("Could not import certificate"));
        browser.click("//button[@type='button']");
        //Should be still in import certificate page
        assertTrue(browser.isTextPresent("Import Certificates"));

        //Cancel butotn should take you back to the Key Store Management page
        browser.click("//input[@value='Cancel']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Key Store Management"));
    }

    public void testLogoutKeyStoreTest()throws Exception
    {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        inSeleniumTestBase.logOutUI();
    }

}