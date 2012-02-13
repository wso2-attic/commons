package org.wso2.carbon.web.test.esb;

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

public class ESBLoginPage extends CommonSetup{

    public ESBLoginPage(String text) {
        super(text);
    }

    /*
	 * This method will verify the ESB Login page
	 */
    public void testESBLoginVeryfier() throws Exception {
        selenium.open("/carbon/admin/login.jsp");
		assertEquals("", selenium.getText("//div[@id='header-div']/div[2]/a/img"));
		assertTrue(selenium.isElementPresent("link=Home"));
//		assertEquals("Manage", selenium.getText("//div[@id='menu']/ul/li[2]"));
        assertEquals("User Management", selenium.getText("//div[@id='menu']/ul/li[3]/ul/li[1]"));
		assertTrue(selenium.isElementPresent("link=List"));
		assertEquals("List", selenium.getText("link=List"));
		assertEquals("Tools", selenium.getText("//tr[5]/td/div/ul/li[1]"));
		assertTrue(selenium.isElementPresent("//tr[5]/td/div/ul/li[1]"));
		assertEquals("Try It", selenium.getText("link=Try It"));
		assertTrue(selenium.isElementPresent("link=Try It"));
		assertEquals("Management Console", selenium.getText("//div[@id='header-div']/div[1]"));

        //Verifying the link User Guide
        assertEquals("WSO2 Carbon user guide.", selenium.getText("//div[@id='features']/table/tbody/tr[1]/td[2]/p"));
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.docLinks("User Guide",null,null);

        //Verifying the link Forum
        assertEquals("The interactive message board for sharing information, questions and comments about WSO2 products.", selenium.getText("//div[@id='features']/table/tbody/tr[2]/td[2]/p"));
        esbCommon.docLinks("Forum","Project: WSO2 ESB","//div[@id='left']/h1");

        //Verifying the link Issue Tracker
		assertEquals("Users are encouraged use the issue tracker to report issues & suggest improvements using the JIRA issue tracker. In addition users will be able to observe the status of the items in progress.", selenium.getText("//div[@id='features']/table/tbody/tr[3]/td[2]/p"));
        esbCommon.docLinks("Issue Tracker","All Projects : WSO2 ESB (Key: ESBJAVA)", "//h3");

        //Verifying the link Mailing Lists
        assertEquals("Report issues, provide feedback & get help from our mailing lists.", selenium.getText("//div[@id='features']/table/tbody/tr[4]/td[2]/p"));
        esbCommon.docLinks("Mailing Lists","WSO2 Project Mailing Lists", "//div[@id='middle-noright']/h1");

        //Verifying the link About
        esbCommon.docLinks("About","About WSO2 Carbon","//h2[1]");

        //Verifying the link Docs
        esbCommon.docLinks("Docs",null,null);

        //Verifying the link Sign-in
        assertEquals("Sign-in", selenium.getText("link=Sign-in"));
		selenium.click("link=Sign-in");
        selenium.waitForPageToLoad("30000");

        //Verifying the link Help
        esbCommon.docLinks("Help","WSO2 Carbon Server Home Page", "//h1");

        //Verifying the properties of the Sign-in box
        assertEquals("Sign-in", selenium.getText("//div[@id='loginbox']/h2"));
		assertEquals("Server URL", selenium.getText("//div[@id='loginbox']/form/table/tbody/tr[1]/td[1]/label"));
		assertEquals("Username", selenium.getText("//div[@id='loginbox']/form/table/tbody/tr[2]/td[1]/label"));
		assertEquals("Password", selenium.getText("//div[@id='loginbox']/form/table/tbody/tr[3]/td[1]/label"));
		assertEquals("Sign-in", selenium.getValue("//input[@value='Sign-in']"));

        //Verifying the link Sign-in Help
        esbCommon.docLinks("Sign-in Help","In the Sign-In panel, provide the user name and the password of your account and the server you want to sign in. Your user name and password will be authenticated, and you will authorized to perform functions according to your role.","//p[1]");
    }
}
