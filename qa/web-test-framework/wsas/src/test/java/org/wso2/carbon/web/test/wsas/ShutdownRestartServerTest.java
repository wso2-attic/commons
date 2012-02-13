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

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
//import org.wso2.carbon.web.test.common.MexModuleClient;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.apache.axiom.om.OMElement;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;



public class ShutdownRestartServerTest extends TestCase{

    Selenium browser;
    Properties property;
    String username;
    String password;

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }

    public  ShutdownRestartServerTest(String s){
        super(s);
    }


    public void testRun() throws Exception                 //Add new system user and login to wsas console
    {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);

    }

    public void testGracefulRestart() throws Exception{
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        accessShutdownrestartServerPage();
        browser.click("link=Graceful Restart");
        assertTrue(browser.isTextPresent("Do you really want to gracefully restart the server?"));
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent("Server is being gracefully restarted. This Management Console will not be accessible for a few minutes."));
        browser.click("//button[@type='button']");
        Thread.sleep(15000);
        //instSeleniumTestBase.logOutUI();

        instSeleniumTestBase.loginToUI(username, password);

    }

    public void testForcedRestart() throws Exception{
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        accessShutdownrestartServerPage();
        browser.click("link=Forced Restart");
        assertTrue(browser.isTextPresent("Do you really want to restart the server?"));
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent("Server is being restarted. This Management Console will not be accessible for a few minutes."));
        browser.click("//button[@type='button']");
        Thread.sleep(20000);
        //instSeleniumTestBase.logOutUI();


        instSeleniumTestBase.loginToUI(username, password);

    }

    public void testLogoutSystem() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();

    }

    public void accessShutdownrestartServerPage() throws Exception{
        browser.click("link=Shutdown/Restart");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Shutdown/Restart Server"));
        assertTrue(browser.isTextPresent("Graceful Shutdown"));
        assertTrue(browser.isTextPresent("Forced Shutdown"));
        assertTrue(browser.isTextPresent("Graceful Restart"));
        assertTrue(browser.isTextPresent("Forced Restart"));
        assertTrue(browser.isElementPresent("link=Graceful Shutdown"));
        assertTrue(browser.isElementPresent("link=Forced Shutdown"));
        assertTrue(browser.isElementPresent("link=Graceful Restart"));
        assertTrue(browser.isElementPresent("link=Forced Restart"));
        assertTrue(browser.isTextPresent("Stop accepting new requests, continue to process already received requests, and then shutdown the server."));
        assertTrue(browser.isTextPresent("Discard any requests currently being processed and immediately shutdown the server. "));
        assertTrue(browser.isTextPresent("Stop accepting new requests, continue to process already received requests, and then restart the server. "));
        assertTrue(browser.isTextPresent("Discard any requests currently being processed and immediately restart the server. "));

    }


}
