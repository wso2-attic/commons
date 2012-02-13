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

public class ESBScriptMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBScriptMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the properties of the Script mediator
	 */
    public void testVerifyScriptMediator(String level) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(1000);
		assertTrue(selenium.isTextPresent("Script Mediator"));
		assertTrue(selenium.isTextPresent("Script Type"));
		assertTrue(selenium.isTextPresent("Language*"));
		assertTrue(selenium.isTextPresent("Source Script*"));
		assertEquals("Inline Registry Key", selenium.getText("script_type"));
		assertEquals("Javascript Ruby Groovy", selenium.getText("mediator.script.language"));
		assertEquals("var sample", selenium.getText("mediator.script.source_script"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Script");
    }
}
