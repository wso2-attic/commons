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

public class ESBSwitchMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBSwitchMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Spring mediator
	 */
    public void testVerifySwitchMediator(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Switch Mediator"));
		assertTrue(selenium.isTextPresent("Source XPath *"));
		assertTrue(selenium.isTextPresent("Number of cases"));
		assertEquals("Namespaces", selenium.getText("mediator.switch.nmsp_button"));
		assertEquals("0 cases", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]"));
		assertEquals("Add case", selenium.getText("link=Add case"));
		assertEquals("Specify default case", selenium.getText("link=Specify default case"));
		assertEquals("Update", selenium.getValue("//input[@value='Update']"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Switch");
    }

    public void testVerifyCaseMediator() throws Exception{
		selenium.click("link=Case");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Switch-Case Mediator"));
		assertTrue(selenium.isTextPresent("Case Value (Regular Expression) *"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Switch Case");

		assertEquals("Update", selenium.getValue("//input[@value='Update']"));
    }

    public void testVerifySwitchDefaultmediator() throws Exception{
		selenium.click("link=Default");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Switch-Default Mediator"));
		assertTrue(selenium.isTextPresent("There are no Switch-Default mediator specific configuration."));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Switch Default");
    }
}
