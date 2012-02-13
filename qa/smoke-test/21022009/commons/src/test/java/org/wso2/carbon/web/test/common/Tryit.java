package org.wso2.carbon.web.test.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;/*
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

public class Tryit extends TestCase {

    Selenium browser;


    public Tryit(Selenium _browser) {
        browser = _browser;
    }


    /* This method is used to invoke tryit for a particular operation
     * You must specify the following arguments when calling this method.
     *
     * type = "external" or "internal" (do you want to invoke tryit without logging in or after logging in?)
     * serviceName = name of the service you want to try
     * opName = name of the operation you want to invoke
     * opParam = name of the parameter passing
     * input = the input string
     * operationCount = no.of operations included in your service
     * result = the string you want to assert
     *
     */

    public void invokeTryit(String type, String serviceName, String opName, String opParam, String input, int operationCount, String result) throws InterruptedException {
        browser.open("/carbon/admin/login.jsp");
        Thread.sleep(10000);
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        String readService = "";
        int i = 1;

        while (!serviceName.equals(readService)) {
            readService = browser.getText("//table[@id='sgTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]");
            i = i + 1;
        }
        i = i - 1;
        if (type.equals("external")) {
            browser.click("//table[@id='sgTable']/tbody/tr[" + Integer.toString(i) + "]/td[6]/nobr/a");
        } else {
            browser.click("//table[@id='sgTable']/tbody/tr[" + Integer.toString(i) + "]/td[7]/nobr/a");
        }
        String tryitwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(tryitwinid);
        Thread.sleep(1000);
        assertTrue(browser.isTextPresent(serviceName));
        Thread.sleep(1000);
        assertEquals("Try the " + serviceName + " service", browser.getTitle());
        if (operationCount > 0) {
            browser.click("link=" + opName);
        }
        browser.type("input_" + opName + "_" + opParam + "_0", input);
        browser.click("button_" + opName);
        assertTrue(browser.isTextPresent(result));

        browser.close();
        browser.selectWindow("");
    }
}

