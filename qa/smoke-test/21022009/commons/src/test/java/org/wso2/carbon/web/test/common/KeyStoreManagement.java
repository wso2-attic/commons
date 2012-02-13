package org.wso2.carbon.web.test.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium; /*
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

/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Apr 23, 2009
 * Time: 12:46:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyStoreManagement extends TestCase {
    Selenium browser;

    public KeyStoreManagement(Selenium _browser) {
        browser = _browser;
    }

    public void AddKeystore(String keystorePath, String keystorePwd) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        browser.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        Thread.sleep(2000);
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");

        browser.click("link=Add New Key store");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Key store"));

        assertTrue(browser.isTextPresent("Step 1: Upload Key store File"));
        assertTrue(browser.isTextPresent("Key Store File*"));
        assertTrue(browser.isTextPresent("Key store Password*"));

        //Submitting without entering values to mandatory fields (KS file, password)
        browser.click("//input[@value='Next >']");
        //Checking for the KS file
        assertTrue(browser.isElementPresent("messagebox-warning"));
        assertTrue(browser.isTextPresent("Please enter a key store File"));
        browser.click("//button[@type='button']");
        //Checking for the KS password
        instSeleniumTestBase.SetFileBrowse("keystoreFile", keystorePath);
        browser.click("//input[@value='Next >']");
        assertTrue(browser.isElementPresent("messagebox-warning"));
        assertTrue(browser.isTextPresent("Please enter key store Password"));
        browser.click("//button[@type='button']");
        browser.type("ksPassword", keystorePwd);

        //Checking the validation when a mismatching KS type is selected
        browser.select("keystoreType", "label=JKS");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        //Checking for private key password
        assertTrue(browser.isTextPresent("Step 2: Specify Private Key Password"));
        assertTrue(browser.isTextPresent("Private Key Password*"));
        browser.click("//input[@value='Finish']");
        assertTrue(browser.isTextPresent("Enter a private key password"));
        browser.click("//button[@type='button']");
        browser.type("keyPass", keystorePwd);
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Key store added successfully."));
        browser.click("//button[@type='button']");
    }

    public void ImportCertificate(String CertificatePath, String certName) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        browser.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        Thread.sleep(2000);
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");

        //Checking the default keystore
        assertTrue(browser.isTextPresent("wso2carbon.jks"));
        assertTrue(browser.isTextPresent("JKS"));
        assertTrue(browser.isElementPresent("link=Import Cert"));
        assertTrue(browser.isElementPresent("link=View"));
        assertTrue(browser.isElementPresent("link=Delete"));

        //Importing a cert for the default keystore
        browser.click("link=Import Cert");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Import Certificates"));
        instSeleniumTestBase.SetFileBrowse("browseField", CertificatePath);

        browser.click("//input[@value='Import']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isElementPresent("messagebox-info"));
        assertTrue(browser.isTextPresent("Certificate imported successfully."));
        browser.click("//button[@type='button']");

        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");
        browser.click("link=View");
        browser.waitForPageToLoad("30000");
        Thread.sleep(4000);
        assertTrue(browser.isTextPresent(certName));
        //Delete the default keystore
    }

    public void deleteKeyStore(String keystoreName) throws Exception {
        browser.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        Thread.sleep(2000);
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");
        browser.click("//a[@onclick=\"deleteKeystore('" + keystoreName + "')\"]");
        //browser.click("link=Delete");
        assertTrue(browser.isTextPresent("Do you want to delete the key store " + keystoreName + "?"));
        assertTrue(browser.isElementPresent("messagebox-confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Key store deleted successfully."));
        assertTrue(browser.isElementPresent("messagebox-info"));
        browser.click("//button[@type='button']");
    }
}
