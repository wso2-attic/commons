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

public class ESBRMSequenceMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBRMSequenceMediatorUITest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the Router mediator properties
	 */
    public void verifyRMSequenceMediator(String level, String messageSequence) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
		assertTrue(selenium.isTextPresent("RM Sequence Mediator"));
		assertTrue(selenium.isTextPresent("WS-RM Spec Version"));
		assertEquals("on", selenium.getValue("v1.0"));
		assertTrue(selenium.isTextPresent("1.0"));
		assertEquals("off", selenium.getValue("v1.1"));
		assertTrue(selenium.isTextPresent("1.1"));
		assertTrue(selenium.isTextPresent("Message sequence"));
		assertTrue(selenium.isTextPresent("Single message"));
        assertTrue(selenium.isTextPresent("Correlated sequence"));
		assertEquals("Update", selenium.getValue("//input[@value='Update']"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));
        
        if (messageSequence.equals("singleRadio")){
		    selenium.click("singleRadio");
        } else if (messageSequence.equals("correlationRadio")){
		    selenium.click("correlationRadio");
            assertTrue(selenium.isTextPresent("Sequence XPath*"));
            assertEquals("NameSpaces", selenium.getText("link=NameSpaces"));
            assertTrue(selenium.isTextPresent("Last message XPath"));
            assertEquals("NameSpaces", selenium.getText("//a[@onclick=\"showNameSpaceEditor('last-message')\"]"));
        }
    }
}
