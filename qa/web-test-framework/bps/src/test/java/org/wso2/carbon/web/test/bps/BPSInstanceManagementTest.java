package org.wso2.carbon.web.test.bps;


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

public class BPSInstanceManagementTest extends CommonSetup {


    public BPSInstanceManagementTest(String text) {
        super(text);
    }

    public String getInstances() throws Exception {
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            BPSLogin.logOutConsole();
        }
        
        BPSLogin.loginToConsole("admin", "admin");
        browser.click("link=Instances");
        browser.waitForPageToLoad("30000");
        //assertTrue((browser.isElementPresent("//table[@id='InstanceListTable']/thead/th[1]")));
        Thread.sleep(5000);
        String iid = browser.getTable("instanceListTable.1.0");
        return iid;
    }


    public void verifyInstanceDetails(String instanceID) throws InterruptedException {
        browser.open("/carbon/bpel/instance_list.jsp?region=region2&item=instances_menu");
        browser.click("link=Instances");
        browser.waitForPageToLoad("30000");

        String processID = browser.getTable("instanceListTable.1.1");
        if (processID.indexOf("counter") > 0) {
            assertEquals("ACTIVE", browser.getText("//table[@id='instanceListTable']/tbody/tr/td[3]"));
            assertEquals("Suspend", browser.getText("link=Suspend"));
            assertEquals("Terminate", browser.getText("link=Terminate"));
            browser.click("link=" + instanceID);
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent(instanceID));
            assertEquals("Instance " + instanceID + " Details", browser.getText("//div[@id='middle']/h2"));
            assertEquals("Instance Details", browser.getTable("instanceStatic.0.0"));
            //assertTrue(browser.isTextPresent("Instance ID 	"+instanceID));
            assertTrue(browser.isTextPresent("ACTIVE [  Suspend  ]   [  Terminate  ]   [  Delete  ]"));
            assertEquals(instanceID, browser.getTable("instanceStatic.1.1"));
            browser.click("link=Sign-out");
            browser.waitForPageToLoad("300000");
        } else {

            browser.click("link=" + instanceID);
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent(instanceID));
            assertEquals("Instance " + instanceID + " Details", browser.getText("//div[@id='middle']/h2"));
            assertEquals("Instance Details", browser.getTable("instanceStatic.0.0"));
            //assertTrue(browser.isTextPresent("Instance ID 	"+instanceID));
            assertTrue(browser.isTextPresent("COMPLETED   [  Delete  ]"));
            assertEquals(instanceID, browser.getTable("instanceStatic.1.1"));
            browser.click("link=Sign-out");
            browser.waitForPageToLoad("300000");

        }
        Thread.sleep(1000);
    }

    public void testInstances() throws Exception {

        verifyInstanceDetails(getInstances());
        // verifyInstanceListPage();
        suspendInstances(getInstances());
        Thread.sleep(1000);
        terminateInstances(getInstances());
        deleteInstances(getInstances());
    }

    public void verifyInstanceListPage() throws Exception {
        BPSLogin.loginToConsole("admin", "admin");
        browser.click("link=Instances");
        browser.waitForPageToLoad("30000");

        //Verifying the presence of elements in instance listing page

        assertEquals("All", browser.getText("link=All"));
        assertEquals("Active", browser.getText("link=Active"));
        assertEquals("Completed", browser.getText("link=Completed"));
        assertEquals("Suspended", browser.getText("link=Suspended"));
        assertEquals("Terminated", browser.getText("link=Terminated"));
        assertEquals("Error", browser.getText("link=Error"));
        assertEquals("Failed", browser.getText("link=Failed"));
        assertEquals("Filter Instance List", browser.getText("filterToggleBtn"));
        assertTrue(browser.isElementPresent("resetInstanceFilter"));
        assertTrue(browser.isElementPresent("deleteInstances"));
        assertEquals("Help", browser.getText("link=Help"));
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");

    }

    public void deleteInstances(String InstanceID) throws Exception {

        browser.open("/carbon/bpel/instance_list.jsp?region=region2&item=instances_menu");
        browser.click("link=Delete");
        assertTrue(browser.isTextPresent("Do you want to delete the instance " + InstanceID + "?"));
        browser.click("//button[@type='button']");
        Thread.sleep(10000);
        assertTrue(browser.isTextPresent("Instances deleted: " + InstanceID));
        browser.click("//button[@type='button']");
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");

    }

    public void suspendInstances(String instanceID) throws Exception {
        browser.open("/carbon/bpel/instance_list.jsp?region=region2&item=instances_menu");
        browser.click("link=Instances");
        browser.waitForPageToLoad("30000");

        String processID = browser.getTable("instanceListTable.1.1");
        if (processID.indexOf("counter") > 0) {
            browser.click("link=Suspend");
            assertTrue(browser.isTextPresent("Do you want to suspend instance " + instanceID + "?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertEquals("Resume", browser.getText("link=Resume"));
            assertEquals("Terminate", browser.getText("link=Terminate"));
            assertEquals("Delete", browser.getText("link=Delete"));
            assertEquals("SUSPENDED", browser.getTable("instanceListTable.1.2"));
            browser.click("link=" + instanceID);
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("SUSPENDED [  Resume  ]   [  Terminate  ]   [  Delete  ]"));
            assertEquals(instanceID, browser.getTable("instanceStatic.1.1"));

            //Show suspended instance
            Thread.sleep(5000);
            //browser.open("/carbon/bpel/instance_list.jsp?operation=resume&iid="+instanceID+"#");
            browser.open("/carbon/bpel/instance_list.jsp?region=region2&item=instances_menu");
            browser.click("link=Instances");
            browser.click("link=Suspended");
            browser.waitForPageToLoad("30000");
            assertEquals(instanceID, browser.getTable("instanceListTable.1.0"));
            assertEquals("SUSPENDED", browser.getTable("instanceListTable.1.2"));
            browser.click("resetInstanceFilter");
            browser.waitForPageToLoad("30000");

            //Resume the suspended instance
            browser.click("link=Resume");
            assertTrue(browser.isTextPresent("Do you want to resume instance " + instanceID + "?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertEquals("ACTIVE", browser.getTable("instanceListTable.1.2"));
            assertEquals("Suspend", browser.getTable("instanceListTable.1.5"));
            assertEquals("Terminate", browser.getTable("instanceListTable.1.6"));
            assertEquals("Delete", browser.getTable("instanceListTable.1.7"));


        }
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");
    }

    public void terminateInstances(String instanceID) throws Exception{
        browser.open("/carbon/bpel/instance_list.jsp?region=region2&item=instances_menu");
        browser.click("link=Instances");
        browser.waitForPageToLoad("30000");
		String processID = browser.getTable("instanceListTable.1.1");
        if (processID.indexOf("counter") > 0) {
            if (browser.getTable("instanceListTable.1.2").equals("ACTIVE")||browser.getTable("instanceListTable.1.2").equals("SUSPENDED")){
		assertEquals("Terminate", browser.getText("link=Terminate"));
		browser.click("link=Terminate");
		assertTrue(browser.isTextPresent("Do you want to terminate instance " + instanceID + "?"));
		browser.click("//button[@type='button']");
		browser.waitForPageToLoad("30000");
		 browser.click("link=" + instanceID);
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("TERMINATED   [  Delete  ]"));
		assertEquals(instanceID, browser.getTable("instanceStatic.1.1"));

        //Show terminated instance

        browser.click("link=Instances");
		browser.waitForPageToLoad("30000");
		assertEquals("Delete", browser.getTable("instanceListTable.1.5"));
		browser.click("link=Terminated");
		browser.waitForPageToLoad("30000");
		assertEquals(instanceID, browser.getTable("instanceListTable.1.0"));
		assertEquals("TERMINATED", browser.getTable("instanceListTable.1.2"));
		browser.click("resetInstanceFilter");
		browser.waitForPageToLoad("30000");
	}
    }
    browser.click("link=Sign-out");
    browser.waitForPageToLoad("300000");
}
}


