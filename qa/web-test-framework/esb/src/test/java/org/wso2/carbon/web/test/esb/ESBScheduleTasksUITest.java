package org.wso2.carbon.web.test.esb;

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

public class ESBScheduleTasksUITest  extends TestCase {
    Selenium selenium;

    public ESBScheduleTasksUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the properties of the New Schedule Tasks page
	 */
    public void testVerifyScheduleTasks() throws Exception {
		selenium.click("link=Scheduled Tasks");
        Thread.sleep(2000);
		selenium.click("link=Add Task");
        assertEquals("New Scheduled Task", selenium.getText("//div[@id='workArea']/table/thead/tr/th"));
		assertEquals("New Scheduled Task", selenium.getText("//div[@id='middle']/h2"));
        assertTrue(selenium.isTextPresent("New Scheduled Task"));
        assertTrue(selenium.isTextPresent("New Scheduled Task"));
		assertTrue(selenium.isTextPresent("Task Name*"));
		assertTrue(selenium.isTextPresent("Task implementation*"));
		assertTrue(selenium.isTextPresent("Trigger information of the Task"));
		assertTrue(selenium.isTextPresent("Trigger Type"));
		assertTrue(selenium.isTextPresent("Simple"));
		assertTrue(selenium.isTextPresent("Cron"));
		assertEquals("on", selenium.getValue("taskTrigger"));
		assertEquals("off", selenium.getValue("//input[@id='taskTrigger' and @name='taskTrigger' and @value='cron']"));
		assertTrue(selenium.isTextPresent("Count*"));
		assertTrue(selenium.isTextPresent("Interval*"));
		assertTrue(selenium.isTextPresent("Miscellaneous Information"));
		assertTrue(selenium.isTextPresent("Pinned servers"));
		assertTrue(selenium.isTextPresent("(separated by comma or space)"));
		assertTrue(selenium.isTextPresent("(separated by comma or space)"));
		assertEquals("Schedule", selenium.getValue("//input[@value='Schedule']"));
		assertEquals("Cancel", selenium.getValue("//input[@value='Cancel']"));

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.componentHelp("Scheduled Tasks");
    }
}
