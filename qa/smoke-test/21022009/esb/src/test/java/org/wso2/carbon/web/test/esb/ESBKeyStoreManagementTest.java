package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.KeyStoreManagement;

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


public class ESBKeyStoreManagementTest extends TestCase {
    Selenium selenium;

    public ESBKeyStoreManagementTest(Selenium _browser){
		selenium = _browser;
    }


    //Login to admin console and test Logging.
    public void testLogin() throws Exception {
   SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(selenium);
        InstSeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests the UI in KS Management page. */
    public void testVerifyKeystorePage() throws Exception {
        selenium.click("link=Key Stores");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Key store Management"));
		assertTrue(selenium.isTextPresent("Name"));
		assertTrue(selenium.isTextPresent("Type"));
		assertTrue(selenium.isTextPresent("Actions"));
		assertTrue(selenium.isTextPresent("Add New Key store"));
        assertTrue(selenium.isTextPresent("Help"));
    }

  public void testAddKeyStore()throws Exception
  {

      Properties properties = new Properties();
      properties.load(new FileInputStream(".."+File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"esb.properties"));
      String keystorePath = properties.getProperty("keystorePath");
      
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
      instKeyStoreManagement.AddKeystore(keystorePath,"qaserver");
  }
  public void testImportCertificate()throws Exception
  {
      Properties properties = new Properties();
      properties.load(new FileInputStream(".."+File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"esb.properties"));
      String certPath = properties.getProperty("certPath");

      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
      instKeyStoreManagement.ImportCertificate(certPath,"chamara.cert");
  }
  public void testDeleteKeystore()throws Exception
  {
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
      instKeyStoreManagement.deleteKeyStore("qaserver.jks");
  }

    /* Tests the UI validations in 'Import Certification' */
    public void testInvalidCertificate() throws Exception {
       SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
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

        Properties properties = new Properties();
        properties.load(new FileInputStream(".."+File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"esb.properties"));
        String browseField = properties.getProperty("browseField");

        //Invalid certificate should be validated
        instSeleniumTestBase.SetFileBrowse("browseField",browseField);
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

     public void testLogout()throws Exception
     {
         SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(selenium);
         inSeleniumTestBase.logOutUI();
     }
}