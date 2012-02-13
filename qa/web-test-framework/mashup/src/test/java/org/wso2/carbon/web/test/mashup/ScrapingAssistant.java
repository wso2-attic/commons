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

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

public class ScrapingAssistant extends CommonSetup{

     public ScrapingAssistant(String text) {
        super(text);
    }

    /*
        Test Scraping Assistant URL
     */
    public static void testScrapingAssistantURL() throws Exception {
        selenium.click("link=Scraping Assistant");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Scraping Assistant"));

        assertTrue(selenium.isTextPresent("Scraper Configuration"));
	    assertEquals("<config></config>", selenium.getValue("scraper-config"));
    }

    /*
        Test Scraper Configuration
     */
    public static void testScraperConfiguration() throws Exception {
        selenium.click("link=Scraping Assistant");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Scraping Assistant"));

        selenium.type("scraper-config", "<config>\n\n</config>");
		selenium.click("link=Scraper Configuration");
		selenium.click("link=Add HTTP request");
        assertEquals("<config>\n  <http url=\"url-to-fetch\" method=\"post\"/>\n</config>", selenium.getValue("scraper-config"));
//        selenium.addSelection("scraper-config","");
//        selenium.click("link=Scraper Configuration");
//		  selenium.click("yui-gen6");
//        Thread.sleep(1000);
    }

      /*
        Accessing tryit to test scraper service.
    */
    public static void testscraperConfig_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
        selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_scraperConfig");
        Thread.sleep(10000);
		String getText1=selenium.getText("//div[@id='console_scraperConfig']/div/div/div[2]/div/div[2]/div/div[1]/div/div[11]/div/div[3]/div/span[1]");
        MSSamplesServices.testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_scraperConfig");
        Thread.sleep(10000);
		String getText2=selenium.getText("//div[@id='console_scraperConfig']/div/div/div[2]/div/div[2]/div/div[1]/div/div[11]/div/div[3]/div/span[2]");
        MSSamplesServices.testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_scraperConfig");
        Thread.sleep(10000);
		String getText3=selenium.getText("//div[@id='console_scraperConfig']/div/div/div[2]/div/div[2]/div/div[1]/div/div[10]/div/div[2]/div/div[2]/span");
        MSSamplesServices.testCheckString(getText3);    
    }
}
