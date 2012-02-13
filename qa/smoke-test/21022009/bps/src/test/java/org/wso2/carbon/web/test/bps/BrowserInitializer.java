package org.wso2.carbon.web.test.bps;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

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

public class BrowserInitializer {
private static Selenium browser;

private static String SELENIUM_SERVER_HOST="selenium.server.host";
private static String SELENIUM_SERVER_PORT="selenium.server.port";
private static String SELENIUM_BROWSER_STARTCOMMAND="selenium.browser.startCommand";
private static String SELENIUM_BROWSER_URL="selenium.browser.url";

public synchronized static void setBrowser(Selenium _browser) {
browser = _browser;
}

public synchronized static Selenium getBrowser() {
return browser;
}

public synchronized static void stopBrowser() {
browser.stop();
}

public synchronized static void initBrowser() {

if (browser == null) {
browser = new DefaultSelenium("localhost", 4444, "*iexplore", "https://localhost:9443");
//browser = new DefaultSelenium(System.getProperty(SELENIUM_SERVER_HOST), Integer.parseInt(System.getProperty(SELENIUM_SERVER_PORT)), System.getProperty(SELENIUM_BROWSER_STARTCOMMAND), System.getProperty(SELENIUM_BROWSER_URL));
browser.start();

//    browser.open("/carbon/admin/login.jsp");
//    browser.type("txtUserName", "admin");
//    browser.type("txtPassword", "admin");
//    browser.click("//input[@value='Sign-in']");
//    browser.waitForPageToLoad("300000");
      browser.setSpeed("500");
}
}
}
