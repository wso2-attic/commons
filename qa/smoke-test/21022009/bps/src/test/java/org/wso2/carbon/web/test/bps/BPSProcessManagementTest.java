package org.wso2.carbon.web.test.bps;

import com.thoughtworks.selenium.SeleneseTestCase;
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

public class BPSProcessManagementTest extends CommonSetup {


    public BPSProcessManagementTest(String text) {
        super(text);
    }


    public String getProcesses() throws InterruptedException {
        browser.open("https://10.100.1.60:9443/carbon/admin/login.jsp");
        browser.waitForPageToLoad("30000");
        browser.type("txtUserName", "admin");
        browser.type("txtPassword", "admin");
        browser.click("//input[@value='Sign-in']");
        browser.waitForPageToLoad("30000");
        browser.click("link=Processes");
        browser.waitForPageToLoad("30000");
        Thread.sleep(5000);
        String pid = browser.getTable("processListTable.2.0");
        return pid;
    }

    public void testVerifyProcesses() throws InterruptedException {
        verifyProcessList(getProcesses());
        verifyprocessDetails(getProcesses());
        verifyProcessFilter(getProcesses());

    }

    public void verifyProcessList(String processID) {
        browser.open("/carbon/bpel/process_list.jsp?region=region2&item=processes_menu");
        browser.waitForPageToLoad("30000");
        browser.click("link=Processes");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(processID));
        assertEquals("ACTIVE", browser.getText("//table[@id='processListTable']/tbody/tr[1]/td[3]"));
        assertEquals("Retire", browser.getText("//table[@id='processListTable']/tbody/tr[1]/td[5]"));
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("30000");

    }

    public void verifyprocessDetails(String processID) throws InterruptedException {
        browser.open("/carbon/bpel/process_list.jsp?region=region2&item=processes_menu");
        browser.click("link=Processes");
        browser.waitForPageToLoad("30000");
        browser.click("link=" + processID);
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(processID));
        assertTrue("processID is present", browser.isTextPresent(processID));
        Thread.sleep(10000);
        assertEquals("Process Details", browser.getTable("processDetailsList.0.0"));
        assertEquals("Instance Summary (Number of Instances vs. Status)", browser.getTable("//div[@id='process-details']/table/tbody/tr[1]/td[3]/table.0.0"));
        //  assertTrue(browser.isTextPresent("Process ID 	"+processID));
        //       Thread.sleep(10000);
//		verifyTrue(browser.isTextPresent("Status 	ACTIVE   [ Retire ]"));
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");

    }

    public void verifyProcessFilter(String processID) {
        browser.open("/carbon/bpel/process_list.jsp?region=region2&item=processes_menu");
        browser.waitForPageToLoad("30000");
        browser.click("link=Processes");
        browser.waitForPageToLoad("30000");
        browser.click("filterToggleBtn");
        assertTrue(browser.isTextPresent("Narrow down the list of processes by applying following selection criteria"));
        assertEquals("Name:", browser.getTable("//tbody[@id='filterBody']/tr[1]/td/table.0.0"));
        assertEquals("Namespace:", browser.getTable("//tbody[@id='filterBody']/tr[1]/td/table.1.0"));
        assertEquals("Deployed:", browser.getTable("//tbody[@id='filterBody']/tr[1]/td/table.2.0"));
        assertEquals("Before  On Or After", browser.getTable("//tbody[@id='filterBody']/tr[1]/td/table.2.1"));
        assertEquals("Order by:", browser.getTable("//tbody[@id='filterBody']/tr[1]/td/table.3.0"));
        //Retrieving process details from process ID
        int beginStringIndex = processID.indexOf("{");
        int endStringIndex = processID.indexOf("}");
        String processName = processID.substring(endStringIndex + 1);
        String processNameSpace = processID.substring(beginStringIndex + 1, endStringIndex);
        //Filter by process name
        browser.type("name", processName);
        browser.click("//input[@value='Filter']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isElementPresent("link=" + processID));
        browser.click("resetProcessFilter");
        browser.waitForPageToLoad("30000");
        //Filter by process namespace
        browser.click("filterToggleBtn");
        browser.type("namespace", processNameSpace);
        browser.click("//input[@value='Filter']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isElementPresent("link=" + processID));
        browser.click("resetProcessFilter");
        browser.waitForPageToLoad("30000");

        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");
    }


}
