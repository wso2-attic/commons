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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;


public class KeyStoreManagement extends TestCase {
    Selenium browser;
    Properties properties = new Properties();

    public KeyStoreManagement(Selenium _browser) {
        browser = _browser;
    }

    public void AddKeystore(String keystorePath, String keystorePwd) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        FileInputStream freader= new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        String context_root = properties.getProperty("context.root");
        if (context_root.equals(null)) {
            browser.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        } else {
            browser.open(context_root + "/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        }
        freader.close();

        Thread.sleep(2000);
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");

        //ToDo - do this check for any keystore
        if(keystorePath.indexOf("qaserver")>0 && browser.isTextPresent("qaserver.jks")){     //Temp fix to avoid test failures when qaserver.jks already exists
            System.out.println("qaserver keystore already exists!!");
            deleteKeyStore("qaserver.jks");
        }
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

        if(browser.isTextPresent("Could not add the key store. Error is Key store qaserver.jks already available")){
            browser.click("//button[@type='button']");
        }
        assertTrue(browser.isTextPresent("Key store added successfully."));
        browser.click("//button[@type='button']");


    }

    public void ImportCertificate(String CertificatePath, String certName) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        FileInputStream freader=new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        String context_root = properties.getProperty("context.root");
        if (context_root.equals(null)) {
            browser.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        } else {
            browser.open(context_root + "/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        }

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
        freader.close();
    }

    public void deleteKeyStore(String keystoreName) throws Exception {
        FileInputStream freader=new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        String context_root = properties.getProperty("context.root");
        if (context_root.equals(null)) {
            browser.open("/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        } else {
            browser.open(context_root + "/carbon/keystoremgt/keystore-mgt.jsp?region=region1&item=keystores_menu");
        }
         freader.close();
        Thread.sleep(2000);
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");
        // browser.click("//a[@onclick=\"deleteKeystore('" + keystoreName + "')\"]");
        browser.click("link=Delete");

        assertTrue(browser.isTextPresent("Do you want to delete the key store " + keystoreName + "?"));
        assertTrue(browser.isElementPresent("messagebox-confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        if(browser.isTextPresent("Could not delete the key store. Error is deleteStoreUsed")){
            browser.click("//button[@type='button']");

        }
        assertTrue(browser.isTextPresent("Key store deleted successfully."));
        assertTrue(browser.isElementPresent("messagebox-info"));
        browser.click("//button[@type='button']");




    }
}
