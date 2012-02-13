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

public class ESBSwitchMediatorTest   extends TestCase {
    Selenium selenium;

    public ESBSwitchMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Switch mediator
	 */
    public void testAddSwitchMediator(String level, String sourceXpath, String namespacePrefix, String namespaceURI) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(5000);
        selenium.type("sourceXPath", sourceXpath);
		selenium.click("mediator.switch.nmsp_button");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddNamespace(namespacePrefix, namespaceURI);

        esbCommon.testMediatorUpdate();
    }

    /*
	 * This method will add a Case mediator
	 */
    public void testAddCase(String level, String caseVal, String childLevel) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("link=Add case");
        Thread.sleep(2000);
		selenium.click("//a[@id='mediator-"+childLevel+"']");
        Thread.sleep(2000);
		selenium.type("caseValue", caseVal);

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testMediatorUpdate();
    }

    /*
	 * This method will add a Default mediator
	 */
    public void testAddDefaultCase(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);
        selenium.click("link=Specify default case");
        selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will add a Default child mediators
	 */
    public void testAddDefaultChildMediator(String mediatorCategory, String mediatorName, String childLevel) throws Exception{
        selenium.click("link=Default");
        Thread.sleep(2000);        
        selenium.click("//div[@id='mediator-"+childLevel+"']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
    }

    /*
	 * This method will add a Case child mediators
	 */
    public void testAddCaseChildMediator(String mediatorCategory, String mediatorName, String childLevel) throws Exception{
        selenium.click("link=Case");
        Thread.sleep(2000);
        selenium.click("//div[@id='mediator-"+childLevel+"']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
    }
}
