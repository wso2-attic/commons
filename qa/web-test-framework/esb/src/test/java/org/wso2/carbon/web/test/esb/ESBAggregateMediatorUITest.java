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

public class ESBAggregateMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBAggregateMediatorUITest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify Aggregate mediator properties
	 */
    public void addAggregateMediator(String level, String sequenceOption) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
		assertTrue(selenium.isTextPresent("Aggregate Mediator"));
		assertTrue(selenium.isTextPresent("Aggregation Expression *"));
		assertEquals("NameSpaces", selenium.getText("link=NameSpaces"));
		assertTrue(selenium.isTextPresent("Completion Timeout"));
		assertTrue(selenium.isTextPresent("Completion Max-messages"));
		assertTrue(selenium.isTextPresent("Completion Min-messages"));
		assertEquals("NameSpaces", selenium.getText("//a[@onclick=\"showNameSpaceEditor('correlate_expr')\"]"));
		assertTrue(selenium.isTextPresent("Correlation Expression"));
		assertTrue(selenium.isTextPresent("On Complete"));
		assertTrue(selenium.isTextPresent("Anonymous"));

        if (sequenceOption.equals("sequenceOptionAnon") && selenium.getValue("sequenceOptionAnon").equals("on")){
            assertEquals("on", selenium.getValue("sequenceOptionAnon"));
        } else {
            assertEquals("off", selenium.getValue("sequenceOptionAnon"));
        }

        if (sequenceOption.equals("sequenceOptionReference") && selenium.getValue("sequenceOptionReference").equals("on")){
            assertEquals("on", selenium.getValue("sequenceOptionReference"));
            assertTrue(selenium.isTextPresent("Pick From Registry"));
            selenium.click("sequenceOptionReference");
            assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.sequence')\"]"));
        } else {
            assertEquals("off", selenium.getValue("sequenceOptionReference"));
        }

		assertEquals("Update", selenium.getValue("//input[@value='Update']"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Aggregate");
    }




}
