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

package org.wso2.carbon.web.test.wsas;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.GenericServiceClient;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.ds.common.*;
import org.apache.axiom.om.OMElement;

public class FaultHandlingTest  extends TestCase {
    Selenium browser;
    Properties property;
    String username;
    String password;
    
    public FaultHandlingTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }


    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);
    }

    public void testUploadBankService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "BankService.aar");
        String ServiceName = "BankService";

        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename", aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));


    }

    public void testService() throws Exception{
        FaultHandlingClient client=new FaultHandlingClient();

        String ServiceName = "BankService";

        OMElement result= client.twoWayClientWithHandledExceptions(ServiceName,"http://example/Bank/withdrawRequest","http://example" , "withdraw","account","amount","88","400");
        String msg=result.getFirstElement().getText();

        if( !msg.equals("Account does not exist!") && !msg.equals("Insufficient funds")){
            System.out.println("service invoked successfully!!");

        }else{
            System.out.println("service failed!!!");
            assertTrue("service failed!!!",false);
        }
    }

    public void testFaultyAccountService() throws Exception{
        FaultHandlingClient client=new FaultHandlingClient();

        String ServiceName = "BankService";

        OMElement result= client.twoWayClientWithHandledExceptions(ServiceName,"http://example/Bank/withdrawRequest","http://example" , "withdraw","account","amount","13","400");

    }

    public void testFaultyAmount() throws Exception{
        FaultHandlingClient client=new FaultHandlingClient();

        String ServiceName = "BankService";

        OMElement result= client.twoWayClientWithHandledExceptions(ServiceName,"http://example/Bank/withdrawRequest","http://example" , "withdraw","account","amount","88","1400");

    }

    public void testAddKeyStoreBankService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testFaultyAccountWithSecurity() throws Exception{
        FaultHandlingClient client=new FaultHandlingClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "BankService";
        String nameSpace = "http://example";
        String soapAction = "http://example/Bank/withdrawRequest";
        String operation = "withdraw";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario("BankService", "scenario5");

        OMElement result= client.runSecurityClient("scenario5",serviceName,nameSpace,soapAction,operation ,"account","amount","13","200");

    }

    public void testFaultyAmountWithSecurity() throws Exception{
        FaultHandlingClient client=new FaultHandlingClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "BankService";
        String nameSpace = "http://example";
        String soapAction = "http://example/Bank/withdrawRequest";
        String operation = "withdraw";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario("BankService", "scenario5");

        OMElement result= client.runSecurityClient("scenario5",serviceName,nameSpace,soapAction,operation ,"account","amount","88","1400");

    }


    public void testDeleteKeystoreBankService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("BankService");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveBankService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instSeleniumTestBase.deleteService("BankService");

    }

    public void testlogOutBankService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.logOutUI();
    }

}
