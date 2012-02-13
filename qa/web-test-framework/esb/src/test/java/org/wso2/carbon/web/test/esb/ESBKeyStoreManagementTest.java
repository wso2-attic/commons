package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.ServiceManagement;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

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

public class ESBKeyStoreManagementTest extends CommonSetup{


    public ESBKeyStoreManagementTest(String text) {
        super(text);
    }

    /*
    This method is used to login to the management console
     */
    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
    }

    /* Tests the UI in KS Management page. */
    public void testVerifyKeystorePage() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();        
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Key Store Management"));
        assertTrue(selenium.isTextPresent("Name"));
        assertTrue(selenium.isTextPresent("Type"));
        assertTrue(selenium.isTextPresent("Actions"));
        assertTrue(selenium.isTextPresent("Add New Key store"));
        assertTrue(selenium.isTextPresent("Help"));
    }

    /*
    Method to add keystores
    */
    public void testAddKeyStore()throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
        File keyStorePath = new File(".."+File.separator+"esb"+File.separator+"lib"+File.separator+"qaserver.jks");
        instKeyStoreManagement.AddKeystore(keyStorePath.getCanonicalPath(),"qaserver");
        System.out.println("SUCCESS !!! - The keystore was added successfully");
    }

    /*
    Method to import certificate
    */
    public void testImportCertificate()throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
        File certificatePath = new File(".."+File.separator+"esb"+File.separator+"lib"+File.separator+"esb.cert");
        instKeyStoreManagement.ImportCertificate(certificatePath.getCanonicalPath(),"esb.cert");
        System.out.println("SUCCESS !!! - The certificate was imported succcessfully");
    }

    /*
    Method to delete keystore
    */
    public void testDeleteKeystore()throws Exception{
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(selenium);
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
        System.out.println("SUCCESS !!! - The keystore was deleted successfully");        
    }

    /*
    Method to test invalid certificates
    */
    public void testInvalidCertificate() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
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
        File caCertPath = new File(".."+File.separator+"esb"+File.separator+"lib"+File.separator+"ca.cer");
        instSeleniumTestBase.SetFileBrowse("browseField",caCertPath.getCanonicalPath());
        selenium.click("//input[@value='Import']");
        selenium.waitForPageToLoad("30000");
        //        assertTrue(selenium.isElementPresent("messagebox-error"));
        //        assertTrue(selenium.isTextPresent("Could not import certificate"));
        selenium.click("//button[@type='button']");
        //Should be still in import certificate page
        assertTrue(selenium.isTextPresent("Import Certificates"));

        //Cancel butotn should take you back to the Key Store Management page
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Key Store Management"));
    }

    /*
    This mthod will be used to log out from the management console
    */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }

}
