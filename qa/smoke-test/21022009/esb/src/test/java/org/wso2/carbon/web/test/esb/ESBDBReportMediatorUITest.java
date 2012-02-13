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

public class ESBDBReportMediatorUITest extends TestCase {

    Selenium selenium;

    public ESBDBReportMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method will verify the properties of the DBreport mediator
     */
    public void testVerifyDBreportMediator(String level) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(4000);
        assertTrue(selenium.isTextPresent("DB Report Mediator"));
		assertTrue(selenium.isTextPresent("Connection Information"));
		assertTrue(selenium.isTextPresent("Pool"));
		assertTrue(selenium.isTextPresent("Data Source"));
		assertTrue(selenium.isTextPresent("Driver  *"));
		assertTrue(selenium.isTextPresent("Url  *"));
		assertTrue(selenium.isTextPresent("User  *"));
		assertTrue(selenium.isTextPresent("Password  *"));
		assertTrue(selenium.isTextPresent("Properties"));
		selenium.click("link=Add Property");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Name"));
		assertTrue(selenium.isTextPresent("Value"));
		assertTrue(selenium.isTextPresent("Action"));
		assertTrue(selenium.isElementPresent("link=Add Property"));
		assertTrue(selenium.isTextPresent("SQL Statements"));
		assertTrue(selenium.isElementPresent("link=Add Statement"));
		selenium.click("link=Add Statement");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("SQL *"));
		assertTrue(selenium.isTextPresent("Parameters"));
		assertTrue(selenium.isElementPresent("link=Add Parameter"));
		selenium.click("link=Add Parameter");
        Thread.sleep(2000);		
        assertTrue(selenium.isTextPresent("Parameter Type"));
		assertTrue(selenium.isTextPresent("Property Type"));
		assertTrue(selenium.isTextPresent("Value/Expression"));
		assertTrue(selenium.isTextPresent("Namespace"));
		assertTrue(selenium.isElementPresent("paramNS1.1"));
		assertTrue(selenium.isTextPresent("Action"));
		selenium.click("radio_datasource");
		assertTrue(selenium.isTextPresent("Datasource Type"));
		assertTrue(selenium.isTextPresent("Inline"));
		assertTrue(selenium.isTextPresent("Existing"));
		assertTrue(selenium.isTextPresent("Initial Context  *"));
		assertTrue(selenium.isTextPresent("Data Source Name  *"));
		assertTrue(selenium.isTextPresent("Url  *"));
		assertTrue(selenium.isTextPresent("User  *"));
		assertTrue(selenium.isTextPresent("Password  *"));
		selenium.click("sourceTypeExisting");
		assertTrue(selenium.isTextPresent("Data Source Name"));
		assertTrue(selenium.isElementPresent("link=Load Data Sources"));
    }
}
