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

public class ESBValidateMediatorUITest   extends TestCase {
    Selenium selenium;

    public ESBValidateMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Validate mediator
	 */
    public void testVerifyValidateMediator(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
		Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Validate Mediator"));
		assertTrue(selenium.isTextPresent("Schema keys defined for Validate Mediator"));
		assertTrue(selenium.isTextPresent("Key*"));
		assertEquals("Registry Keys", selenium.getText("link=Registry Keys"));
		assertEquals("Delete", selenium.getText("//a[@onclick='deleteKey(0);return false;']"));
		assertEquals("Add New Schema Key", selenium.getText("link=Add New Schema Key"));
		assertEquals("Source", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[4]/td/table/tbody/tr/td[1]"));
		assertTrue(selenium.isTextPresent("Source"));
		assertEquals("Namespace", selenium.getText("link=Namespace"));
		assertTrue(selenium.isTextPresent("Features defined for Validator Mediator"));
		assertEquals("Add feature", selenium.getText("link=Add feature"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Validate");
    }
}
