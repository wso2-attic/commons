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
    public void addThrottleMediator(String level, String throttleID) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
		selenium.type("throttle_id1", throttleID);
        esbCommon.mediatorUpdate();
    }

    public void addThrottlePolicy(String level, String specifyAsPolicy, String throttlePolicy,String maxConcurrentAccess,String range,String type,String accessType,String maxRequest,String  unitTime,String prohibitTime) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);

        if (specifyAsPolicy.equals("policygroupInlineId")) {
		    selenium.click("policygroupInlineId");
            selenium.click("link=Throttle Policy Editor");
            Thread.sleep(2000);
            if(selenium.isElementPresent("data21")){
                selenium.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
            }

            if(maxConcurrentAccess!=null)
                selenium.type("maxAccess", maxConcurrentAccess);
            if(range!=null)
                selenium.type("data11", range);
            if(type!=null)
                selenium.select("data16", type);
            if(accessType!=null)
                selenium.select("data15", "label="+accessType);
            if(maxRequest!=null)
                selenium.type("data12", maxRequest);
            if( unitTime!=null)
                selenium.type("data13",  unitTime);
            if(prohibitTime!=null)
                selenium.type("data14", prohibitTime);
            selenium.click("//input[@value='Finish']");
        } else if (specifyAsPolicy.equals("policygroupValueId")){
            selenium.click("policygroupValueId");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.regPolicy')\"]");
    
            esbCommon.selectResource("Entry",throttlePolicy);
        }
    }

    /*
    This method will add onAccept information to the Throttle mediator
     */
    public void onAcceptInfo(String onAcceptGroup, String acceptKey) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        
        if (onAcceptGroup.equals("onacceptgroup")) {
           /*
           To do - Fill this section
            */
        } else {
            selenium.click("//input[@name='onacceptgroup' and @value='onAcceptSequenceKey']");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.acceptKey')\"]");

            esbCommon.selectResource("Sequence",acceptKey);
        }
    }

    /*
    This method will add onReject information to the Throttle mediator
     */
    public void onRejectInfo(String throttle_level,String onRejectGroup, String rejectKey) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        //selenium.click("link=OnReject");
        selenium.click("//div[@id='mediator-"+throttle_level+"']/div/div[1]/a");
        Thread.sleep(2000);
        
        if (onRejectGroup.equals("onrejectgroup")) {
            selenium.click("onrejectgroup");
           /*
           To do - Fill this section
            */
        }else{
            selenium.click("//input[@name='onrejectgroup' and @value='onRejectSequenceKey']");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.rejectKey')\"]");

            esbCommon.selectResource("Sequence",rejectKey);
        }
    }
}
