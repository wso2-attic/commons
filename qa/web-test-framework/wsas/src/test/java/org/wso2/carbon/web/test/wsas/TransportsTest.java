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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;


public class TransportsTest extends TestCase
{
    Selenium browser;
    Properties property;
    String username;
    String password;

    public  TransportsTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    public void testUploadAxis2Service() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "Axis2Service.aar");
        String ServiceName = "Axis2Service";

        InstSeleniumTestBase.loginToUI(username, password);
        //browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
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

    public void testHttpTransports() throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.removeTransportProtocol("http");//change this
        instSeleniumTestBase.addTransportProtocol("http");

        instSeleniumTestBase.removeTransportProtocol("https");
        instSeleniumTestBase.addTransportProtocol("https");

        instSeleniumTestBase.removeTransportProtocol("http");
        instSeleniumTestBase.removeTransportProtocol("https");

        instSeleniumTestBase.addTransportProtocol("http");
    }

    public void testHttpsSecurityScenario() throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario("Axis2Service", "scenario1");
        browser.click("//table[@id='serviceOperationsTable']/tbody/tr[3]/td[2]/a");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("All enabled transport listeners are already added "));
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        instServiceManagement.disableSecurity("Axis2Service");
    }
    /*
 public void testMailToTransport() throws Exception{
     SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
     browser.click("link=Transports");
     browser.waitForPageToLoad("30000");
     assertTrue(browser.isTextPresent("Transport Management"));
     assertTrue(browser.isTextPresent("Transport Implementation"));
     assertTrue(browser.isTextPresent("Receiver/Sender"));
     assertTrue(browser.isTextPresent("MAILTO"));
     assertTrue(browser.isTextPresent("HTTPS"));
     assertTrue(browser.isTextPresent("HTTP"));
     assertTrue(browser.isTextPresent("JMS"));

}       *
    */
    public void testRemoveServicesfromTransportTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }

    public void logOut()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
