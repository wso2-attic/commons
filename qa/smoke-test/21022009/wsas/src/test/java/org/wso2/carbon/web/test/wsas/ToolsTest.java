package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.Tryit;
import org.wso2.carbon.web.test.common.SeleniumTestBase; /*
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
 * Time: 5:52:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToolsTest extends TestCase
{
    Selenium browser;

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public void testUploadservice() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI("admin", "admin");

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("PojoService.xml");
        String resourcefile = instReadXMLFileTest.ReadConfig("PojoPath","PojoService");
        String ServiceName = instReadXMLFileTest.ReadConfig("ServiceName", "PojoService");

        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=POJO Service");
        browser.waitForPageToLoad("30000");
      //  browser.attachFile("jarZipFilename","file:///D:\\Projects\\Idea\\TestFramework\\web-test-framework\\wsas\\lib\\PojoService.class");
        InstSeleniumTestBase.SetFileBrowse("jarZipFilename", resourcefile);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created POJO service"));

        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }
        public void testAbout()throws Exception
    {
        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        Thread.sleep(10000);
		browser.click("link=About");
        String tryitwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(tryitwinid);
		assertTrue(browser.isTextPresent("Version 1.1"));
        browser.close();
        browser.selectWindow("");
    }
       public void testLogout()throws Exception
    {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        inSeleniumTestBase.logOutUI();
    }
   public void testInvokeExtrTryit()throws Exception
    {

        Tryit instTryit = new Tryit(browser);
      //  instTryit.invokeTryit("external","HelloService", "greet","name","Hello", 1, "Hello");
        instTryit.invokeTryit("external","PojoService", "echoString","s","Hello",4, "Hello");
    }
    public void testLogin()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.loginToUI("admin","admin");
    }
    public void testRemoveService()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("PojoService");
    }
        public void testLogout2()throws Exception
    {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        inSeleniumTestBase.logOutUI();
    }

}
