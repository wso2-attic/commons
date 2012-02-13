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

public class ESBThrottleMediatorTest  extends TestCase {
    Selenium selenium;

    public ESBThrottleMediatorTest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add the basic Aggregate mediator properties
	 */
    public void testAddThrottleMediator(String level, String throttleID, String specifyAsPolicy, String throttlePolicy ) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
		selenium.type("throttle_id1", throttleID);
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (specifyAsPolicy.equals("policygroupInlineId")) {
		    selenium.click("policygroupInlineId");
            selenium.click("link=Throttle Policy Editor");
            selenium.type("maxAccess", "10");
            selenium.select("data15", "label=Control");
            selenium.type("data12", "10");
            selenium.type("data13", "10");
            selenium.type("data14", "10");
            selenium.click("//input[@value='Finish']");
        } else if (specifyAsPolicy.equals("policygroupValueId")){
            selenium.click("policygroupValueId");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.regPolicy')\"]");
    
            esbCommon.testSelectResource("Entry",throttlePolicy);
        }
    }

    /*
    This method will add onAccept information to the Throttle mediator
     */
    public void testOnAcceptInfo(String onAcceptGroup, String acceptKey) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        
        if (onAcceptGroup.equals("onacceptgroup")) {
           /*
           To do - Fill this section
            */
        } else {
            selenium.click("//input[@name='onacceptgroup' and @value='onAcceptSequenceKey']");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.acceptKey')\"]");

            esbCommon.testSelectResource("Sequence",acceptKey);
        }
    }

    /*
    This method will add onReject information to the Throttle mediator
     */
    public void testOnRejectInfo(String onRejectGroup, String rejectKey) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (onRejectGroup.equals("onrejectgroup")) {
           /*
           To do - Fill this section
            */
        }else{
            selenium.click("//input[@name='onrejectgroup' and @value='onRejectSequenceKey']");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.rejectKey')\"]");

            esbCommon.testSelectResource("Sequence",rejectKey);
        }
    }
}
