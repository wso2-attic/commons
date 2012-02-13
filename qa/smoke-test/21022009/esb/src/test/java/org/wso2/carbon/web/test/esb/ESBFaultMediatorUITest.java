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

public class ESBFaultMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBFaultMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    public void testVerifyFaultMediatorUI(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Fault Mediator"));
		assertTrue(selenium.isTextPresent("Version"));
		assertTrue(selenium.isTextPresent("SOAP 1.1"));
		assertTrue(selenium.isTextPresent("SOAP 1.2"));
		assertTrue(selenium.isTextPresent("Code*"));
		assertEquals("versionMismatch mustUnderstand dataEncodingUnknown Sender Receiver", selenium.getText("fault_code2"));
		assertTrue(selenium.isTextPresent("value"));
		assertTrue(selenium.isTextPresent("expression"));
		assertTrue(selenium.isTextPresent("Fault String*"));
		assertTrue(selenium.isTextPresent("Fault Actor"));
		assertTrue(selenium.isTextPresent("Node"));
		assertTrue(selenium.isTextPresent("Detail"));
		assertTrue(selenium.isElementPresent("nmsp_button"));
		assertEquals("Update", selenium.getValue("//input[@value='Update']"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Fault");
    }
}
