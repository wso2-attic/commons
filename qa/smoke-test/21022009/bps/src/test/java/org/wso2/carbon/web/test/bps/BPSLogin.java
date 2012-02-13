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

public class BPSLogin extends CommonSetup {


    public BPSLogin(String text) {
        super(text);
    }


    public static void loginToConsole(String UserName, String Password) throws Exception   // Login to management console
    {
        browser.open("/carbon/admin/login.jsp");
        browser.type("txtUserName", "admin");
        browser.type("txtPassword", "admin");
        browser.click("//input[@value='Sign-in']");
        browser.waitForPageToLoad("300000");
        //Verify the left menu contents
        assertTrue(browser.isElementPresent("menu-panel"));
        assertTrue(browser.isTextPresent("Welcome to the WSO2 BPS Management Console"));
        browser.click("link=Summary");
        browser.waitForPageToLoad("300000");
        assertTrue(browser.isTextPresent("Business Process Server Summary"));
    }

    public static void logOutConsole() {
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("30000");
    }

}
