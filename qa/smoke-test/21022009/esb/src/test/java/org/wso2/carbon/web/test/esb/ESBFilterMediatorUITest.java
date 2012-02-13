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


public class ESBFilterMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBFilterMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Switch mediator
	 */

    public void testVerifyFilterMediator(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Filter Mediator"));
		assertTrue(selenium.isTextPresent("Specify As"));
		assertTrue(selenium.isTextPresent("XPath"));
		assertTrue(selenium.isTextPresent("Source and Regular Expression"));
		assertTrue(selenium.isTextPresent("XPath *"));
		assertTrue(selenium.isElementPresent("mediator.callout.target.xpath_nmsp_button"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));
        assertEquals("Update", selenium.getValue("//input[@value='Update']"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Filter");
    }
}
