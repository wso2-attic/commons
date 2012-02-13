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
    int switchNsLevel = 0;

    public ESBSwitchMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Switch mediator
	 */
    public void addSwitchMediator(String level, String sourceXpath) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(5000);
        selenium.type("sourceXPath", sourceXpath);
    }

    /*
	 * This method will add a Case mediator
	 */
    public void addCase(String level, String caseVal, String childLevel) throws Exception{
        Thread.sleep(2000);        
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("link=Add case");
        Thread.sleep(5000);
		selenium.click("//a[@id='mediator-"+childLevel+"']");
        Thread.sleep(2000);
		selenium.type("caseValue", caseVal);
    }

    /*
	 * This method will add a Default mediator
	 */
    public void addDefaultCase(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);
        selenium.click("link=Specify default case");
        selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will add a Default child mediators
	 */
    public void addDefaultChildMediator(String childLevel, String mediatorCategory, String mediatorName) throws Exception{
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
    public void addCaseChildMediator( String childLevel, String mediatorCategory, String mediatorName) throws Exception{
        selenium.click("link=Case");
        Thread.sleep(3000);
        selenium.click("//div[@id='mediator-"+childLevel+"']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
        Thread.sleep(3000);        
    }

    /*
    This method will add namespaces to the Switch mediator
     */
    public void addSwitchNamespace(String switchNSPrefix, String switchNSUri) throws Exception {
        boolean pref = selenium.isTextPresent(switchNSPrefix);

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(1000);
            selenium.click("mediator.switch.nmsp_button");
            Thread.sleep(2000);
            selenium.click("addNSButton");
            selenium.type("prefix"+switchNsLevel, switchNSPrefix);
            selenium.type("uri"+switchNsLevel, switchNSUri);
            switchNsLevel++;
            selenium.click("saveNSButton");
        }
    }

}
