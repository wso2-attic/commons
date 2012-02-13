package org.wso2.carbon.web.test.is;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class ISDefaultUI extends CommonSetup{

    public ISDefaultUI(String text) {
        super(text);
    }

    /* Check the default UI */
    public static void testMainUI() throws Exception {
        ISCommon.forChangedContext();
        assertEquals("Management Console", selenium.getText("//div[@id='header-div']/div[1]"));
		assertEquals("", selenium.getText("//div[@id='header-div']/div[2]/a/img"));
        assertEquals("WSO2 Management Console", selenium.getTitle());

        assertTrue(selenium.isTextPresent("Sign-up"));
        assertTrue(selenium.isTextPresent("InfoCard/OpenID Sign-in"));
        assertTrue(selenium.isTextPresent("Identity"));

        selenium.click("link=Sign-up");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign-up"));
        assertEquals("", selenium.getText("//div[@id='loginbox']/a/img"));
        assertEquals("", selenium.getText("//td[2]/div/a/img"));
        selenium.click("link=Sign-in");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=InfoCard/OpenID Sign-in");
		selenium.waitForPageToLoad("30000");
		assertEquals("Sign-in", selenium.getText("//div[@id='middle']/h2"));
		assertTrue(selenium.isTextPresent("Sign-in with Information Card"));
		assertTrue(selenium.isTextPresent("Sign-in with OpenID"));
		assertEquals("", selenium.getText("//div[@id='loginbox']/a/img"));
		selenium.click("link=Sign-in");
		selenium.waitForPageToLoad("30000");
    }

    //User Guide test
    public static void userGuideTest() throws Exception{
        String expectedForUserGuide="http://wso2.org/project/solutions/identity/2.0.0/docs/";

        assertEquals("User Guide", selenium.getText("link=User Guide"));
        selenium.click("link=User Guide");
        String userguidewinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(userguidewinid);
        Thread.sleep(20000);
        assertTrue(selenium.isTextPresent("WSO2 Identity Server - Distribution"));
        String actualForUserGuide = selenium.getLocation();
        if(actualForUserGuide.equals(expectedForUserGuide))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

    //Forum test
    public static void forumTest() throws Exception{
        String expectedForForum="http://wso2.org/forum/308";

        assertEquals("Forum", selenium.getText("link=Forum"));
        selenium.click("link=Forum");
        String forumwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(forumwinid);
        Thread.sleep(20000);
		assertTrue(selenium.isTextPresent("exact:Project: WSO2 Identity Server (WSO2 IS)"));
        String actualForForum = selenium.getLocation();
        if(actualForForum.equals(expectedForForum))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

    //Issue Tracker test
    public static void issueTrackerTest() throws Exception{
        String expectedForIssueTracker="https://wso2.org/jira/browse/IDENTITY";

        assertEquals("Issue Tracker", selenium.getText("link=Issue Tracker"));
        selenium.click("link=Issue Tracker");
        String issuetrackerwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(issuetrackerwinid);
        Thread.sleep(20000);
		assertTrue(selenium.isTextPresent("All Projects : WSO2 Identity Server (Key: IDENTITY)"));
        String actualForIssueTracker = selenium.getLocation();
        if(actualForIssueTracker.equals(expectedForIssueTracker))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

    //Mailing list test
    public static void mailingListsTest() throws Exception{
        String expectedForMailingLists="http://wso2.org/mail";

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
    }

    //Sign In Help test
    public static void signInHelpTest() throws Exception{
        String expectedForSignInHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/docs/signin_userguide.html";

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
    }

    //Docs test
    public static void docsTest() throws Exception{
        String expectedForDocs="http://wso2.org/project/solutions/identity/2.0.0/docs/";

        assertEquals("Docs", selenium.getText("link=Docs"));
        selenium.click("link=Docs");
        String docswinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(docswinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("WSO2 Identity Server - Distribution"));
        String actualForDocs = selenium.getLocation();
        if(actualForDocs.equals(expectedForDocs))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

    //About test
    public static void aboutTest() throws Exception{
        String expectedForAbout="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/docs/about.html";

        assertEquals("About", selenium.getText("link=About"));
        selenium.click("link=About");
        String aboutwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(aboutwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("About WSO2 Carbon"));
        String actualForAbout = selenium.getLocation();
        if(actualForAbout.equals(expectedForAbout))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

    //CSHelp test
    public static void csHelpTest() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/admin/docs/userguide.html";

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