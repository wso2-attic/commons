package org.wso2.carbon.web.test.bps;

import junit.framework.TestCase;

import com.thoughtworks.selenium.Selenium;

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

public class BPSLoginPageTest extends CommonSetup {


    public BPSLoginPageTest(String text) {
        super(text);
    }

    public void testBPSLoginPageUI() throws InterruptedException {
        browser.open("/carbon/admin/login.jsp");
		assertEquals("", browser.getText("//div[@id='header-div']/div[2]/a/img"));
		assertEquals("WSO2 Management Console", browser.getTitle());
		assertEquals("Management Console", browser.getText("//div[@id='header-div']/div[1]"));
		//assertEquals("HomeManageService List", browser.getTable("menu-table.0.0"));
        assertTrue(browser.isElementPresent("menu-panel"));
		assertEquals("Sign-in", browser.getText("//div[@id='loginbox']/h2"));

        //Check sign-in help
        assertEquals("Sign-in Help", browser.getText("link=Sign-in Help"));
       	browser.click("link=Sign-in Help");
        String signinwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(signinwinid);
        assertEquals("Sign-In", browser.getText("//h1"));
        browser.close();
        browser.selectWindow("");
        assertTrue(browser.isElementPresent("link=User Guide"));
        //browser.click("link=User Guide");

        //Check forums
        assertEquals("Forum", browser.getText("link=Forum"));
        browser.click("link=Forum");
        String forumwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(forumwinid);
        browser.waitForPageToLoad("30000");
        assertEquals("Project: WSO2 BPS", browser.getText("//div[@id='left']/h1"));
        browser.close();
        browser.selectWindow("");

        //Check Issue tracker
//        assertEquals("Issue Tracker", browser.getText("link=Issue Tracker"));
//		browser.click("link=Issue Tracker");
//        String issuetrackerwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
//        browser.selectWindow(issuetrackerwinid);
//        //This test will fail on IE due to a security alert appears when opening JIRA home page. ToDo: Find a soultion to avoid security alert popup
////        //browser.answerOnNextPrompt("Yes");
////        browser.chooseCancelOnNextConfirmation();
//        browser.waitForPageToLoad("30000");
//		assertEquals("All Projects : WSO2 Business Process Server (Key: BPS)", browser.getText("//h3"));
//        browser.close();
//        browser.selectWindow("");


        //Check Mailing lists
		assertEquals("Mailing Lists", browser.getText("link=Mailing Lists"));
		browser.click("link=Mailing Lists");
        String mailinglistswinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(mailinglistswinid);
        browser.waitForPageToLoad("30000");
		assertEquals("WSO2 Project Mailing Lists", browser.getText("//div[@id='middle-noright']/h1"));
		browser.close();
        browser.selectWindow("");

        //Check the doc links

		assertEquals("About", browser.getText("link=About"));
		browser.click("link=About");
        Thread.sleep(2000);
        String aboutwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(aboutwinid);
        assertEquals("About WSO2 Carbon", browser.getText("//h2[1]"));
		browser.close();
        browser.selectWindow("");
		assertEquals("Docs", browser.getText("link=Docs"));
		assertEquals("Help", browser.getText("link=Help"));
		browser.click("link=Help");
        Thread.sleep(2000);
        String helpwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(helpwinid);
		assertEquals("WSO2 Carbon Server Home Page", browser.getText("//h1"));
		browser.close();
        browser.selectWindow("");
		assertEquals("© 2008 - 2009 WSO2 Inc. All Rights Reserved.", browser.getText("//div[@id='footer-div']/div/div"));
    }
    
    public void testExternalServiceList(){
        browser.open("/carbon/admin/login.jsp");
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));
        assertEquals("Service Groups", browser.getTable("sgTable.0.0"));

    }

}
