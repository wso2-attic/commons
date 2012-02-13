package org.wso2.carbon.web.test.mashup;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.Selenium;


public class ScrapingAssistantTest extends CommonSetup {

    public ScrapingAssistantTest(String text) {
        super(text);
    }

    /*
    *  Sign-in to Mashup Server admin console
     */
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    /*
       CSHelp
     */
    public void testCSHelp() throws Exception{
       String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.be.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/js_scraper/docs/userguide.html";
       selenium.click("link=Scraping Assistant");
       selenium.waitForPageToLoad("30000");
       assertTrue(selenium.isTextPresent("Help"));
       selenium.click("link=Help");
       String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
       selenium.selectWindow(helpwinid);
       Thread.sleep(10000);
       assertTrue(selenium.isTextPresent("The Scraper Assistant"));
       String actualForCSHelp = selenium.getLocation();
       if(actualForCSHelp.equals(expectedForCSHelp))
           System.out.println("Actual location & expected location are matched");
       else
           System.out.println("Actual location & expected location are not matched");
       selenium.close();
       selenium.selectWindow("");
    }


     public void testScrapingAssistant() throws Exception {
        ScrapingAssistant.testScrapingAssistantURL();
        ScrapingAssistant.testScraperConfiguration();

     }

     /*
           Testing scraperConfig service.
     */

    public void testscraperConfig() throws Exception {

            MSCommon.testAccessTryit("scraper");
            Thread.sleep(10000);
            ScrapingAssistant.testscraperConfig_Service();

            selenium.close();
            selenium.selectWindow("");
    }

    /*
        Sign-out from Mashup Server.
     */
    public void testSignout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

}
