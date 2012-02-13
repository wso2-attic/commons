package org.wso2.carbon.web.test.mashup;

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

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

public class KeyStoreTest extends SeleneseTestCase {
    Selenium selenium;

    //Initialize the browser
    public void setUp() throws Exception {
        selenium = BrowserInitializer.getBrowser();
    }


    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests the UI in KS Management page. */
    public void testKSUICheck() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");

        //Checking the page heading and other page elements
        assertTrue(selenium.isTextPresent("Key store Management"));
        assertTrue(selenium.isElementPresent("link=Add New Key store"));
        assertTrue(selenium.isElementPresent("//table[@id='keymgtTable']/thead/tr/th[1]"));
        assertTrue(selenium.isTextPresent("Name"));
        assertTrue(selenium.isElementPresent("//table[@id='keymgtTable']/thead/tr/th[2]"));
        assertTrue(selenium.isTextPresent("Type"));
        assertTrue(selenium.isElementPresent("//table[@id='keymgtTable']/thead/tr/th[3]"));
        assertTrue(selenium.isTextPresent("Actions"));
        assertTrue(selenium.isElementPresent("link=Help"));
    }


    /* Tests the UI validations in KS Management.
    NOTE: The validations are checked within a single KS creation cycle */
    public void testKSUIValidations() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Add New Key store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add New Key store"));

        assertTrue(selenium.isTextPresent("Step 1: Upload Key store File"));
        assertTrue(selenium.isTextPresent("Key Store File*"));
        assertTrue(selenium.isTextPresent("Key store Password*"));

        //Submitting without entering values to mandatory fields (KS file, password)
        selenium.click("//input[@value='Next >']");
        //Checking for the KS file
        assertTrue(selenium.isElementPresent("messagebox-warning"));
        assertTrue(selenium.isTextPresent("Please enter a key store File"));
        selenium.click("//button[@type='button']");
        //Checking for the KS password
        selenium.type("keystoreFile", "D:\\Testing\\Test data\\wso2is.jks");
        selenium.click("//input[@value='Next >']");
        assertTrue(selenium.isElementPresent("messagebox-warning"));
        assertTrue(selenium.isTextPresent("Please enter key store Password"));
        selenium.click("//button[@type='button']");
        selenium.type("ksPassword", "wso2is");

        //Checking the validation when a mismatching KS type is selected
        selenium.select("keystoreType", "label=PKCS12");
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-error"));
        assertTrue(selenium.isTextPresent("DerInputStream.getLength(): lengthTag=109, too big."));   //--- update the error message once the defect is fixed
        selenium.click("//button[@type='button']");

        //Checking for private key password
        assertTrue(selenium.isTextPresent("Step 2: Specify Private Key Password"));
        assertTrue(selenium.isTextPresent("Private Key Password*"));
        selenium.click("//input[@value='Finish']");
        assertTrue(selenium.isTextPresent("Enter a private key password"));
        selenium.click("//button[@type='button']");
        selenium.type("keyPass", "wso2is");
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Key store added successfully."));
        selenium.click("//button[@type='button']");
    }

    
    /* Tests the UI validations in 'Import Certification' */
    public void testUIValidationsImportCert() throws Exception {
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
        selenium.type("browseField", "D:\\Testing\\Test data\\test-soapui-project.xml");
        selenium.click("//input[@value='Import']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-error"));
        assertTrue(selenium.isTextPresent("Could not import certificate. Error is Error importing certificate to store. Error is invalid DER-encoded certificate data"));
        selenium.click("//button[@type='button']");
        //Should be still in import certificate page
        assertTrue(selenium.isTextPresent("Import Certificates"));

        //Cancel butotn should take you back to the Key Store Management page
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Key store Management"));
    }

    /* Tests default KS with all basic KS features  */
    public void testDefaultKeyStore() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");

        //Checking the default keystore
        assertTrue(selenium.isTextPresent("wso2carbon.jks"));
        assertTrue(selenium.isTextPresent("JKS"));
        assertTrue(selenium.isElementPresent("link=Import Cert"));
        assertTrue(selenium.isElementPresent("link=View"));
        assertTrue(selenium.isElementPresent("link=Delete"));

        //Importing a cert for the default keystore
        selenium.click("link=Import Cert");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Import Certificates"));
        selenium.type("browseField", "D:\\Testing\\Test data\\relying party cert for IS\\ca.cer");
        selenium.click("//input[@value='Import']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("X\r\n\r\nCertificate imported successfully"));
        selenium.click("//button[@type='button']");

        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=View");
        selenium.waitForPageToLoad("30000");
        //--- there was a bug here MASHIP-1188. Need to validate the next page once the bug is fixed.

        //Delete the default keystore
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Delete");
        assertTrue(selenium.isTextPresent("Do you want to delete the key store wso2carbon.jks?"));
        assertTrue(selenium.isElementPresent("messagebox-confirm"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Could not delete the key store. Error is Primary key store cannot be deleted."));
        assertTrue(selenium.isElementPresent("messagebox-error"));
        selenium.click("//button[@type='button']");
    }


    /* Tests the 'Add new KS' feature. */
    public void testAddKS() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Key store");
        selenium.waitForPageToLoad("30000");
        selenium.type("keystoreFile", "D:\\Testing\\Test data\\wso2is.jks");
        selenium.type("ksPassword", "wso2is");
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");
        selenium.type("keyPass", "wso2is");
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Key store added successfully."));
        selenium.click("//button[@type='button']");

        //Check if the added KS is listed in the KS Management page.
        assertTrue(selenium.isTextPresent("Key store Management"));
        assertTrue(selenium.isTextPresent("wso2is.jks"));
    }


    /* Tests a successful certificate import operation */
    public void testImportCertificate() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.type("browseField", "D:\\Testing\\Test data\\relying party cert for IS\\test.cer");
        selenium.click("//input[@value='Import']");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Certificate imported successfully."));
        selenium.click("//button[@type='button']");
        assertTrue(selenium.isTextPresent("test.cer"));
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
    }


    /*Tests the Key Store 'view' option */
    public void testViewKS() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=View");
        selenium.waitForPageToLoad("30000");
        //--- add validations for the next page after the defect fix for MASHUP-1188
    }


    /* Test the KS deletion */
    public void testDeleteKS() throws Exception {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Delete");
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Key store deleted successfully."));
        selenium.click("//button[@type='button']");
    }
}