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

import com.thoughtworks.selenium.*;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class LoggingTest extends CommonSetup {
//    Selenium selenium;

    public LoggingTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.checkSystemLogs();
    }

    public void testLogout() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        instSeleniumTestBase.logOutUI();
    }
}