package org.wso2.carbon.web.test.is;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.KeyStoreManagement;


public class KeyStoreTest extends CommonSetup {
    //  Selenium browser;

    public KeyStoreTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests the UI in KS Management page. */
    public void testVerifyKeystorePage() throws Exception {
        selenium.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        Thread.sleep(2000);
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Key store Management"));
        assertTrue(selenium.isTextPresent("Name"));
        assertTrue(selenium.isTextPresent("Type"));
        assertTrue(selenium.isTextPresent("Actions"));
        assertTrue(selenium.isTextPresent("Add New Key store"));
        assertTrue(selenium.isTextPresent("Help"));
    }

    public void testAddKeyStore() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
        instKeyStoreManagement.AddKeystore("D:\\Testing\\Test_data\\service.jks", "testing");
    }

    public void testImportCertificate() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
        instKeyStoreManagement.ImportCertificate("D:\\Testing\\Test_data\\certs_IS testing", "is2.cert");
    }

    public void testDeleteKeystore() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
        instKeyStoreManagement.deleteKeyStore("service.jks");
    }

    /* Tests the UI validations in 'Import Certification' */
    public void testInvalidCertificate() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        Thread.sleep(2000);
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Import Cert");
        selenium.waitForPageToLoad("30000");

        //Certificate should be marked as a mandatory field
        assertTrue(selenium.isTextPresent("Certificate *"));

        //Submission without giving the certificate should be validated
        selenium.click("//input[@value='Import']");
        assertTrue(selenium.isElementPresent("messagebox-warning"));
        assertTrue(selenium.isTextPresent("Please specify a path before importing certificate"));
        selenium.click("//button[@type='button']");

        //Invalid certificate should be validated
        instSeleniumTestBase.SetFileBrowse("browseField", "D:\\Testing\\Test_data\\test-soapui-project.xml");
        selenium.click("//input[@value='Import']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-error"));
        assertTrue(selenium.isTextPresent("Could not import certificate"));
        selenium.click("//button[@type='button']");
        //Should be still in import certificate page
        assertTrue(selenium.isTextPresent("Import Certificates"));

        //Cancel butotn should take you back to the Key Store Management page
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Key store Management"));
    }

    public void testLogout() throws Exception {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(selenium);
        inSeleniumTestBase.logOutUI();
    }

}