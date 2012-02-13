package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.*;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBEventSourceTest extends TestCase {
    Selenium selenium;

    public ESBEventSourceTest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method will add a new event source
     */
    public void testAddEventSource(String eventSourceType, String eventSourceName, String headerName, String namespace, String registryUrl, String username, String password) throws Exception{
        selenium.click("link=Event Sources");
        Thread.sleep(4000);

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean eventSource = selenium.isTextPresent(eventSourceName);

        if (eventSource){
            //Do nothing
        } else {
            selenium.click("link=Add Event Source");
            Thread.sleep(2000);

            if (eventSourceType.equals("RemoteRegistry (topic filter)")){
                Thread.sleep(2000);
                selenium.select("eventsourcetype", "label=RemoteRegistry (topic filter)");
                selenium.type("eventsourcename", eventSourceName);
                selenium.type("headerName", headerName);
                selenium.type("namespace", namespace);
                selenium.type("registryUrl", registryUrl);
                selenium.type("user", username);
                selenium.type("pwd", password);
            } else if (eventSourceType.equals("DefaultInMemory (topic filter)")){
                Thread.sleep(2000);
                selenium.select("eventsourcetype", "label=DefaultInMemory (topic filter)");
                selenium.type("eventsourcename", eventSourceName);
                selenium.type("headerName", headerName);
                selenium.type("namespace", namespace);
            }

            selenium.click("//input[@value='Add']");
        }
    }
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
//
	
	
