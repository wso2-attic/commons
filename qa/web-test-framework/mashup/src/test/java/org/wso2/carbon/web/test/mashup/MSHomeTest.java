package org.wso2.carbon.web.test.mashup;

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


public class MSHomeTest extends CommonSetup {

    public MSHomeTest(String text) {
        super(text);
    }

    public void testMSHomePageUI() throws Exception {
        selenium.open(MSCommon.loadProperties().getProperty("context.root")+"/carbon/admin/login.jsp");
        Thread.sleep(15000);
		assertEquals("Management Console", selenium.getText("//div[@id='header-div']/div[1]"));
		assertEquals("", selenium.getText("//div[@id='header-div']/div[2]/a/img"));
        assertEquals("WSO2 Management Console", selenium.getTitle());
        
        assertEquals("Home", selenium.getText("link=Home"));
		assertEquals("Manage", selenium.getText("//div[@id='menu']/ul/li[2]"));
		assertEquals("List", selenium.getText("link=List"));
		assertEquals("Try It", selenium.getText("link=Try It"));
		assertEquals("Scraping Assistant", selenium.getText("link=Scraping Assistant"));
		assertEquals("JavaScript Stub Generator", selenium.getText("link=JavaScript Stub Generator"));

        assertEquals("Sign-in", selenium.getText("link=Sign-in"));
        assertEquals("Sign-in", selenium.getText("//div[@id='loginbox']/h2"));

        String expectedForUserGuide="http://wso2.org/project/mashup/2.0/docs/index.html";
        String expectedForForum="http://wso2.org/forum/226";
        String expectedForIssueTracker="https://wso2.org/jira/browse/MASHUP";
        String expectedForMailingLists="http://wso2.org/mail";
        String expectedForSignInHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/docs/signin_userguide.html";
        String expectedForDocs="http://wso2.org/project/mashup/2.0/docs/index.html";
        String expectedForAbout="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/docs/about.html";
        String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/admin/docs/userguide.html";

        assertEquals("User Guide", selenium.getText("link=User Guide"));
        selenium.click("link=User Guide");
        String userguidewinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(userguidewinid);
        Thread.sleep(20000);
        String actualForUserGuide = selenium.getLocation();
        if(actualForUserGuide.equals(expectedForUserGuide))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");


		assertEquals("Forum", selenium.getText("link=Forum"));
        selenium.click("link=Forum");
        String forumwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(forumwinid);
        selenium.waitForPageToLoad("60000");
		assertTrue(selenium.isTextPresent("exact:Project: WSO2 Mashup Server"));
        String actualForForum = selenium.getLocation();
        if(actualForForum.equals(expectedForForum))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");


		assertEquals("Issue Tracker", selenium.getText("link=Issue Tracker"));
        selenium.click("link=Issue Tracker");
        String issuetrackerwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(issuetrackerwinid);
        selenium.waitForPageToLoad("60000");
		assertTrue(selenium.isTextPresent("home"));
		assertTrue(selenium.isTextPresent("browse project"));
		assertTrue(selenium.isTextPresent("find issues"));
		assertTrue(selenium.isTextPresent("All Projects : WSO2 Mashup Server (Key: MASHUP)"));
        String actualForIssueTracker = selenium.getLocation();
        if(actualForIssueTracker.equals(expectedForIssueTracker))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");


		assertEquals("Mailing Lists", selenium.getText("link=Mailing Lists"));
        selenium.click("link=Mailing Lists");
        String mailinglistwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(mailinglistwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("WSO2 Project Mailing Lists"));
        String actualForMailingLists = selenium.getLocation();
        if(actualForMailingLists.equals(expectedForMailingLists))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");

        
        assertEquals("Sign-in Help", selenium.getText("link=Sign-in Help"));
        selenium.click("link=Sign-in Help");
        String signinhelpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(signinhelpwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("Sign-In"));
        String actualForSignInHelp = selenium.getLocation();
        if(actualForSignInHelp.equals(expectedForSignInHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");


        assertEquals("Docs", selenium.getText("link=Docs"));
        selenium.click("link=Docs");
        String docswinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(docswinid);
        Thread.sleep(10000);
        String actualForDocs = selenium.getLocation();
        if(actualForDocs.equals(expectedForDocs))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");


        assertEquals("About", selenium.getText("link=About"));
        selenium.click("link=About");
        String aboutwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(aboutwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("Version 2.0"));
		assertTrue(selenium.isTextPresent("About WSO2 Carbon"));
        String actualForAbout = selenium.getLocation();
        if(actualForAbout.equals(expectedForAbout))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");


        assertEquals("Help", selenium.getText("link=Help"));
        selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("WSO2 Carbon Server Home Page"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

}
