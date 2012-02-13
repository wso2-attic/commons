package org.wso2.carbon.web.test.esb;

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


import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

public class ESBIterateMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBIterateMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    public void verifyIterateMediator() throws Exception{
         ESBCommon esbCommon=new ESBCommon(selenium);
        
        assertTrue(selenium.isTextPresent("Iterate Mediator"));
        assertEquals("True False", selenium.getText("continueParent"));
        assertEquals("True False", selenium.getText("preservePayload"));
		assertTrue(selenium.isTextPresent("exact:Iterate Expression*"));
		assertTrue(selenium.isTextPresent("Attach Path"));
		assertTrue(selenium.isElementPresent("link=Namespaces"));
		assertTrue(selenium.isElementPresent("//a[@onclick=\"showNameSpaceEditor('attach_path')\"]"));
        assertEquals("Update", selenium.getValue("//input[@value='Update']"));
        assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));
        //esbCommon.componentHelp("//div[@id='mediatorDesign']/div/div/a");

    }

     public void testVerifyTargetConfiguration() throws Exception{
        selenium.click("link=Target");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Target Configuration"));
		assertTrue(selenium.isTextPresent("SOAP Action"));
		assertTrue(selenium.isTextPresent("To Address"));
		assertTrue(selenium.isTextPresent("Sequence"));
		assertTrue(selenium.isTextPresent("None"));
		assertTrue(selenium.isTextPresent("Anonymous"));
		assertTrue(selenium.isTextPresent("Pick From Registry"));
		assertTrue(selenium.isTextPresent("Endpoint"));
		assertTrue(selenium.isTextPresent("None"));
		assertTrue(selenium.isTextPresent("Anonymous"));
		assertTrue(selenium.isTextPresent("Pick From Registry"));
		assertEquals("Pick From Registry", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[10]/td[1]"));
		assertEquals("Anonymous", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[9]/td[1]"));
		assertEquals("None", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[8]/td"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));
		assertEquals("Update", selenium.getValue("//input[@value='Update']"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Target");
    }
}
