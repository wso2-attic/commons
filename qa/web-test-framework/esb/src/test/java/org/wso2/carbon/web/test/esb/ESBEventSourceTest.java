package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import junit.framework.Test;
import junit.framework.TestSuite;

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

public class ESBEventSourceTest extends CommonSetup{

    public ESBEventSourceTest(String text) {
        super(text);
    }

    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
    }

    /*
    Method to verify the Event Sources page
     */
    public void testVerifyEventSourcePage() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");

        boolean eventSource = selenium.isTextPresent("SampleEventSource");
        if (eventSource){
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            selenium.waitForPageToLoad("30000");
        }

        assertTrue(selenium.isTextPresent("Event Sources"));
        assertTrue(selenium.isTextPresent("No Event Sources are currently defined."));
        assertTrue(selenium.isElementPresent("link=Add Event Source"));
    }

    /*
    Method to verify the New Event Source page
     */
    public void testVerifyNewEventSourcePage() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add Event Source");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("New Event Source"));
		assertTrue(selenium.isElementPresent("//table[@id='eventsources']/thead/tr/th"));
		assertTrue(selenium.isElementPresent("//table[@id='eventsources']/tbody/tr[1]/td[1]"));
		assertTrue(selenium.isTextPresent("DefaultInMemory (topic filter) RemoteRegistry (topic filter)"));
		assertTrue(selenium.isTextPresent("Name*"));
		assertTrue(selenium.isTextPresent("Topic Header Name*"));
		assertTrue(selenium.isTextPresent("Topic Header Namespace*"));
    }

    /*
    Method to add event sources
     */
    public void testAddSimpleEventSource() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");
        //Options available are RemoteRegistry (topic filter) and DefaultInMemory (topic filter)
        addDefaultEventSource("SampleEventSource","Topic","http://apache.org/aip");
//        esbEventSourceTest.testAddRegistryEventSource("SampleEventSource5","TopicHeader","TopicNamespace","http://localhost:9444/registry","admin","admin");
        assertTrue(selenium.isTextPresent("SampleEventSource"));
        System.out.println("SUCCESS !!! - The event source was added successfully");
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();
    }

    /*
    Verify the Event source info in the edit mode
     */
    public void testVerifyEventSourceEditPage() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");        
//        selenium.click("link=Edit");
		selenium.click("//a[@onclick=\"editRow('SampleEventSource')\"]");        
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Event Source Details"));
        assertTrue(selenium.isTextPresent("Event Source Details :SampleEventSource"));
        assertEquals("Type", selenium.getText("//table[@id='eventsources']/tbody/tr[1]/td[1]"));
        assertEquals("DefaultInMemory", selenium.getText("//table[@id='eventsources']/tbody/tr[1]/td[2]"));
        assertEquals("Class Name", selenium.getText("//table[@id='eventsources']/tbody/tr[2]/td[1]"));
        assertEquals("org.apache.synapse.eventing.managers.DefaultInMemorySubscriptionManager", selenium.getText("//table[@id='eventsources']/tbody/tr[2]/td[2]"));
        assertEquals("Topic Header Name", selenium.getText("//table[@id='eventsources']/tbody/tr[3]/td[1]"));
        assertEquals("Topic", selenium.getValue("headerName"));
        assertEquals("Topic Header Namespace", selenium.getText("//table[@id='eventsources']/tbody/tr[4]/td[1]"));
		assertEquals("http://apache.org/aip", selenium.getValue("namespace"));
        System.out.println("SUCCESS !!! - The event source information has been added successfully");
    }

    /*
    Method to edit the Event source
     */
    public void testEditEventSource() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"editRow('SampleEventSource')\"]");
        selenium.waitForPageToLoad("30000");
		selenium.type("headerName", "Topic1");
		selenium.type("namespace", "http://apache.org/aip1");
		selenium.click("//input[@value='Save']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Edit");
		selenium.waitForPageToLoad("30000");
		assertEquals("Topic1", selenium.getValue("headerName"));
		assertEquals("http://apache.org/aip1", selenium.getValue("namespace"));
        System.out.println("SUCCESS !!! - The event source information has been edited successfully");
    }

    /*
    Verify the Event source info in the edit mode
     */
    public void testVerifyEditedEventSourceEditPage() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");
//        selenium.click("link=Edit");
		selenium.click("//a[@onclick=\"editRow('SampleEventSource')\"]");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Event Source Details"));
        assertTrue(selenium.isTextPresent("Event Source Details :SampleEventSource"));
        assertEquals("Type", selenium.getText("//table[@id='eventsources']/tbody/tr[1]/td[1]"));
        assertEquals("DefaultInMemory", selenium.getText("//table[@id='eventsources']/tbody/tr[1]/td[2]"));
        assertEquals("Class Name", selenium.getText("//table[@id='eventsources']/tbody/tr[2]/td[1]"));
        assertEquals("org.apache.synapse.eventing.managers.DefaultInMemorySubscriptionManager", selenium.getText("//table[@id='eventsources']/tbody/tr[2]/td[2]"));
        assertEquals("Topic Header Name", selenium.getText("//table[@id='eventsources']/tbody/tr[3]/td[1]"));
        assertEquals("Topic1", selenium.getValue("headerName"));
        assertEquals("Topic Header Namespace", selenium.getText("//table[@id='eventsources']/tbody/tr[4]/td[1]"));
		assertEquals("http://apache.org/aip1", selenium.getValue("namespace"));
        System.out.println("SUCCESS !!! - The edited event source information has been saved successfully");
    }

    /*
    Method to delete the Event Source
     */
    public void testDeleteEventSource() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"deleteRow('SampleEventSource')\"]");
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");

        boolean isPresent = (selenium.isTextPresent("SampleEventSource"));
        if (isPresent){
            System.out.println("ERROR !!! - The event source has not been deleted");
        }
        System.out.println("SUCCESS !!! - The event source was deleted successfully");
    }

    /*
    This mthod will be used to log out from the management console
    */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }

    /*
    This method will add a new event source
     */
    public void addDefaultEventSource(String eventSourceName, String headerName, String namespace) throws Exception{
        selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");

        boolean eventSource = selenium.isTextPresent(eventSourceName);

        if (eventSource){
            //Do nothing
        } else {
            selenium.click("link=Add Event Source");
		    selenium.waitForPageToLoad("30000");
            selenium.select("eventsourcetype", "label=DefaultInMemory (topic filter)");
            addCommonEventInfo(eventSourceName, headerName, namespace);
            selenium.click("//input[@value='Add']");
            selenium.waitForPageToLoad("30000");
        }
    }

    /*
    This method will add Registry Event sources
     */
    public void addRegistryEventSource(String eventSourceName, String headerName, String namespace, String registryUrl, String username, String password) throws Exception{
        selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean eventSource = selenium.isTextPresent(eventSourceName);

        if (eventSource){
            //Do nothing
        } else {
            selenium.click("link=Add Event Source");
		    selenium.waitForPageToLoad("30000");
            selenium.select("eventsourcetype", "label=RemoteRegistry (topic filter)");
            addCommonEventInfo(eventSourceName, headerName, namespace);
            selenium.type("registryUrl", registryUrl);
            selenium.type("user", username);
            selenium.type("pwd", password);
            selenium.click("//input[@value='Add']");
            selenium.waitForPageToLoad("30000");
        }
    }

    /*
    This method will add common Event source information
     */
    public void addCommonEventInfo(String eventSourceName, String headerName, String namespace) throws Exception {
        selenium.type("eventsourcename", eventSourceName);
        selenium.type("headerName", headerName);
        selenium.type("namespace", namespace);
    }

//    public void testAddDefaultInMemoryEventSource(String eventsourcetype, String eventsourcename, String headerName, String namespace) throws Exception {
//        //Signing in
//        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
//        seleniumTestBase.loginToUI("admin","admin");
//
//		selenium.open("/carbon/admin/index.jsp?loginStatus=true");
//		selenium.click("link=Event Sources");
//		selenium.waitForPageToLoad("30000");
//
//		//Add event source
//		selenium.click("link=Add Event Source");
//		selenium.waitForPageToLoad("30000");
//		selenium.select("eventsourcetype", "label="+eventsourcetype);
//		selenium.type("eventsourcename", eventsourcename);
//		selenium.type("headerName", headerName);
//		selenium.type("namespace", namespace);
//		selenium.click("//input[@value='Add']");
//		selenium.waitForPageToLoad("30000");
//		Boolean add = selenium.isTextPresent(eventsourcename);
//		if (add) {
//			System.out.print("Test PASSED - The event source was successfully added");
//		}
//		else {
//			System.out.print("Test FAILED - The event source was not added");
//		}
//
//    }
//
//    public void testEditDefaultInMemoryEventSource(String eventsourcetype, String eventsourcename, String headerName, String namespace) throws Exception {
//        //Edit event source
//		selenium.click("link=Edit");
//		selenium.waitForPageToLoad("30000");
//		selenium.type("headerName", headerName);
//		String edit = selenium.getValue("headerName");
//		System.out.println(edit);
//		selenium.click("//input[@value='Save']");
//		selenium.waitForPageToLoad("30000");
//		selenium.click("link=Edit");
//		selenium.waitForPageToLoad("30000");
//		String editCheck = selenium.getValue("headerName");
//		System.out.println(editCheck);
//		if(edit.equals(editCheck)){
//			System.out.print("Test PASSED - The eventsource was successfully edited");
//		}
//		else {
//			System.out.print("Test FAILED - The eventsource editing failed");
//		}
//		selenium.click("//input[@value='Cancel']");
//		selenium.waitForPageToLoad("30000");
//    }
//
//	public void testDeleteDefaultInMemoryEventSource(String eventsourcetype, String eventsourcename, String headerName, String namespace) throws Exception {
//        //Delete event source
//		selenium.click("link=Delete");
//        assertTrue(selenium.isTextPresent("Do you want to remove "+eventsourcename+ "?"));
//		selenium.click("//button[@type='button']");
//		selenium.waitForPageToLoad("30000");
//
//		Boolean del = selenium.isTextPresent(eventsourcename);
//		System.out.println(del);
//		if (del) {
//			System.out.print("Test FAILED - The eventsource was not deleted");
//		}
//		else if (!del){
//			System.out.print("Test PASSED - The eventsource was successfully deleted");
//		}
//	}
//}
    
    
}


