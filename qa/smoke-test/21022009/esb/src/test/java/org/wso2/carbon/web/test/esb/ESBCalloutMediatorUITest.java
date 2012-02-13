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

public class ESBCalloutMediatorUITest  extends TestCase {
    Selenium selenium;

    public ESBCalloutMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the Callout mediator properties
	 */
    public void testVerifyCalloutMediator(String level, String sourceSpecifyAs, String targetSpecifyAs) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Callout Mediator"));
        assertTrue(selenium.isTextPresent("ServiceURL *"));
        assertTrue(selenium.isTextPresent("Action"));
        assertTrue(selenium.isTextPresent("Axis2 Repository"));
        assertTrue(selenium.isTextPresent("Axis2 XML"));
        assertTrue(selenium.isTextPresent("Source *"));
        assertTrue(selenium.isTextPresent("Specify as :"));

        if (sourceSpecifyAs.equals("sourceGroupXPath")){
            selenium.click("sourceGroupXPath");
            assertEquals("on", selenium.getValue("sourceGroupXPath"));
            assertTrue(selenium.isTextPresent("XPath"));
            assertEquals("Namespaces", selenium.getText("mediator.callout.source.xpath_nmsp_button"));
        } else if (sourceSpecifyAs.equals("//input[@name='sourcegroup' and @value='Key']")){
            selenium.click("//input[@name='sourcegroup' and @value='Key']");
            assertEquals("on", selenium.getValue("//input[@name='sourcegroup' and @value='Key']"));
            assertTrue(selenium.isTextPresent("Key"));
		    assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.callout.source.key_val')\"]"));            
        }

        assertTrue(selenium.isTextPresent("Target *"));
        assertTrue(selenium.isTextPresent("Specify as :"));

        if (targetSpecifyAs.equals("targetGroupXPath")){
            selenium.click("targetGroupXPath");
            assertEquals("on", selenium.getValue("targetGroupXPath"));
            assertTrue(selenium.isTextPresent("XPath"));
            assertEquals("Namespaces", selenium.getText("mediator.callout.target.xpath_nmsp_button"));
        } else if (targetSpecifyAs.equals("//input[@name='targetgroup' and @value='Key']")){
            selenium.click("//input[@name='targetgroup' and @value='Key']");
            assertEquals("on", selenium.getValue("//input[@name='targetgroup' and @value='Key']"));
            assertTrue(selenium.isTextPresent("Key"));
            assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.callout.target.key_val')\"]"));
        }
        
        assertEquals("Update", selenium.getValue("//input[@value='Update']"));
        assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Callout");
    }
}
