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

public class ESBXSLTMediatorUITest extends TestCase {

    Selenium selenium;

    public ESBXSLTMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method will verify the properties of the XSLT mediator
     */
    public void testAddXSLTMediator(String level) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("XSLT Mediator"));
		assertTrue(selenium.isTextPresent("Key*"));
		assertTrue(selenium.isElementPresent("//a[@onclick=\"showInLinedRegistryBrowser('mediator.xslt.key')\"]"));
		assertTrue(selenium.isTextPresent("Source"));
        assertTrue(selenium.isTextPresent("Properties of the XSLT mediator"));
        selenium.click("link=Add Property");
		assertTrue(selenium.isTextPresent("Property Name"));
		assertTrue(selenium.isTextPresent("Property Type"));
		assertTrue(selenium.isTextPresent("Value / Expression"));
		assertTrue(selenium.isTextPresent("Action"));
		assertEquals("ValueExpression", selenium.getText("propertyTypeSelection0"));
		assertTrue(selenium.isElementPresent("//a[@onclick='deleteproperty(0)']"));
        assertTrue(selenium.isTextPresent("Features of the XSLT mediator"));
        selenium.click("link=Add Feature");
		assertTrue(selenium.isTextPresent("Feature Name"));
		assertTrue(selenium.isTextPresent("Feature Value"));
		assertTrue(selenium.isTextPresent("Action"));
		assertEquals("TrueFalse", selenium.getText("featureValue0"));
		assertTrue(selenium.isElementPresent("//a[@onclick='deletefeature(0);return false;']"));
    }
}
