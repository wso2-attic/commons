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

package org.wso2.carbon.web.test.csg;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.csg.BrowserInitializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class SetupClientService extends TestCase {
    /*Selenium browser;
    FileInputStream freader;

    public SetupClientService(String s){
        super(s);
    }*/

    Selenium browser;
    Properties properties = new Properties();

    public SetupClientService(Selenium _browser) {
        browser = _browser;
    }

  /*  public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }
*/
    public Properties loadProperties() throws IOException {
       Properties properties = new Properties();
       FileInputStream  freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
       properties.load(freader);
       freader.close();
       return properties;
    }

    public void Login() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
        InstSeleniumTestBase.loginToUI(username, password);
    }

    public void AddServer(String domainName,String uName,String pwd,String port)throws Exception
    {
        browser.open("/carbon/admin/index.jsp");
		assertTrue(browser.isTextPresent("Cloud Services Gateway"));
		browser.click("link=Cloud Services Gateway");
		browser.waitForPageToLoad("30000");
		browser.click("//a[@onclick='add()']");
		browser.waitForPageToLoad("30000");
		browser.type("name", "Instance1");
		browser.select("type", "label=Existing");
		browser.type("ip", domainName);
		browser.type("userName", uName);
		browser.type("password", pwd);
		browser.type("transportPort", port);
		browser.click("connectStartUp");
		browser.click("cancel");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Instance1"));
		assertTrue(browser.isTextPresent("Not Connected"));
		assertTrue(browser.isTextPresent("Edit"));
		assertTrue(browser.isTextPresent("Remove"));
		assertTrue(browser.isTextPresent("Connect"));
		browser.click("link=Connect");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Connected"));
    }
    public void PublishService(String serviceName,String instName)throws Exception
    {
        browser.click("link=Cloud Services");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent(instName));
		browser.click("link=Instance1");
		browser.waitForPageToLoad("30000");
		browser.click("//a[@onclick=\"publishForm('"+serviceName+"')\"]");
		browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Cloud Service published successfully"));
		browser.click("//button[@type='button']");
    }

     public void RemoveInst(String instName)throws Exception
     {
        browser.click("link=Cloud Services Gateway");
		browser.waitForPageToLoad("30000"); 
        browser.click("//a[@onclick=\"disconnect('"+instName+"')\"]");
		browser.waitForPageToLoad("30000");
		browser.click("link=Remove");
		browser.waitForPageToLoad("30000");
     }
    


    public void logOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }


}