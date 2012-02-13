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

public class ESBMessageMediationStatisticsUITest extends TestCase {
    Selenium selenium;

    public ESBMessageMediationStatisticsUITest(Selenium _browser){
        selenium=_browser;
    }

    /*
    This method will verify the Message Mediation UI properties
     */
    public void testMessageMediationUI() throws Exception{
		ESBCommon esbCommon = new ESBCommon(selenium);

        selenium.click("link=Mediation Statistics");
        Thread.sleep(5000);
        assertTrue(selenium.isTextPresent("Mediation Statistics"));
		assertEquals("Server Statistics", selenium.getText("link=Server Statistics"));
        assertEquals("Help", selenium.getText("link=Help"));
        esbCommon.testComponentHelp("Mediation Statistics");

        //Verifying the Server Statistics page
        selenium.click("link=Server Statistics");
        Thread.sleep(5000);
        assertTrue(selenium.isTextPresent("Server Statistics"));
		assertTrue(selenium.isTextPresent("Statistics"));
		assertTrue(selenium.isTextPresent("Total count"));
		assertTrue(selenium.isTextPresent("Fault count"));
		assertTrue(selenium.isTextPresent("Maximum response time"));
		assertTrue(selenium.isTextPresent("Minimum response time"));
		assertTrue(selenium.isTextPresent("Average time"));
		assertTrue(selenium.isTextPresent("Servers"));
		assertTrue(selenium.isTextPresent("Average time Vs Time"));

        //Verifying the Proxy Services Statistics page        
        selenium.click("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]/a");
        Thread.sleep(5000);
        assertEquals("Proxy service Statistics", selenium.getText("link=Proxy service Statistics"));
		selenium.click("link=Proxy service Statistics");
		assertTrue(selenium.isTextPresent("Proxy service Statistics"));
		assertTrue(selenium.isTextPresent("Statistics"));
		assertTrue(selenium.isTextPresent("Total count"));
		assertTrue(selenium.isTextPresent("Fault count"));
		assertTrue(selenium.isTextPresent("Maximum response time"));
		assertTrue(selenium.isTextPresent("Minimum response time"));
		assertTrue(selenium.isTextPresent("Average time"));
		assertTrue(selenium.isTextPresent("Proxy Services"));
		assertTrue(selenium.isTextPresent("Average time Vs Time"));

        //Verifying the Sequence Statistics page
        selenium.click("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]/a");
        Thread.sleep(5000);
		assertEquals("Sequence Statistics", selenium.getText("link=Sequence Statistics"));
		selenium.click("link=Sequence Statistics");
        Thread.sleep(5000);
        assertTrue(selenium.isTextPresent("Sequence Statistics"));
		assertTrue(selenium.isTextPresent("Statistics"));
		assertTrue(selenium.isTextPresent("Total count"));
		assertTrue(selenium.isTextPresent("Fault count"));
		assertTrue(selenium.isTextPresent("Maximum response time"));
		assertTrue(selenium.isTextPresent("Minimum response time"));
		assertTrue(selenium.isTextPresent("Average time"));
		assertTrue(selenium.isTextPresent("Sequences"));
		assertTrue(selenium.isTextPresent("Average time Vs Time"));

        //Verifying the Endpoint Statistics page        
        selenium.click("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]/a");
        Thread.sleep(5000);
		assertEquals("Endpoint Statistics", selenium.getText("link=Endpoint Statistics"));
		selenium.click("link=Endpoint Statistics");
        Thread.sleep(5000);
        assertTrue(selenium.isTextPresent("Endpoint Statistics"));
		assertTrue(selenium.isTextPresent("Statistics"));
		assertTrue(selenium.isTextPresent("Total count"));
		assertTrue(selenium.isTextPresent("Fault count"));
		assertTrue(selenium.isTextPresent("Maximum response time"));
		assertTrue(selenium.isTextPresent("Minimum response time"));
		assertTrue(selenium.isTextPresent("Average time"));
		assertTrue(selenium.isTextPresent("Sequences"));
		assertTrue(selenium.isTextPresent("Average time Vs Time"));
    }
}
