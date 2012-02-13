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

public class ESBThrottleMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBThrottleMediatorUITest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the properties of an Aggregate mediator
	 */
   public void testVerifyThrottleMediator(String level, String specifyAsPolicy, String onAcceptance, String onRejection) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Throttle Mediator"));
        assertTrue(selenium.isTextPresent("Throttle Group ID*"));
        assertTrue(selenium.isTextPresent("Throttle Policy"));
        assertTrue(selenium.isTextPresent("Specify as"));

        if (specifyAsPolicy.equals("policygroupValueId") && (selenium.getValue("policygroupValueId").equals("on"))) {
            assertEquals("on", selenium.getValue("policygroupValueId"));
            assertTrue(selenium.isTextPresent("In-Lined Policy"));
            assertEquals("Throttle Policy Editor", selenium.getText("link=Throttle Policy Editor"));
        } else if (specifyAsPolicy.equals("policygroupValueId") && (selenium.getValue("policygroupValueId").equals("off"))){
            assertEquals("off", selenium.getValue("policygroupValueId"));
        }

        if (specifyAsPolicy.equals("policygroupInlineId") && (selenium.getValue("policygroupInlineId").equals("on"))) {
            assertEquals("on", selenium.getValue("policygroupInlineId"));
            assertTrue(selenium.isTextPresent("Referring Policy"));
            assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.regPolicy')\"]"));
        } else if (specifyAsPolicy.equals("policygroupInlineId") && (selenium.getValue("policygroupInlineId").equals("off"))){
            assertEquals("off", selenium.getValue("policygroupInlineId"));
        }

        assertTrue(selenium.isTextPresent("On Acceptance"));
        assertTrue(selenium.isTextPresent("Specify as"));

        if (onAcceptance.equals("onacceptgroup") && (selenium.getValue("onacceptgroup").equals("on"))) {
            assertEquals("on", selenium.getValue("onacceptgroup"));
            assertTrue(selenium.isTextPresent("In-Lined Sequence"));
        } else if (onAcceptance.equals("onacceptgroup") && (selenium.getValue("onacceptgroup").equals("off"))) {
            assertEquals("off", selenium.getValue("onacceptgroup"));            
            assertEquals("on", selenium.getValue("//input[@name='onacceptgroup' and @value='onAcceptSequenceKey']"));
            assertTrue(selenium.isTextPresent("Referring Sequence"));
		    assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.acceptKey')\"]"));
        }

        assertTrue(selenium.isTextPresent("On Rejection"));
        assertTrue(selenium.isTextPresent("Specify as"));

        if (onRejection.equals("onrejectgroup") && (selenium.getValue("onrejectgroup").equals("on"))) {
            assertEquals("on", selenium.getValue("onrejectgroup"));
            assertTrue(selenium.isTextPresent("In-Lined Sequence"));
        } else if (onRejection.equals("onrejectgroup") && (selenium.getValue("onrejectgroup").equals("off"))){
            assertEquals("off", selenium.getValue("onrejectgroup"));
            assertEquals("on", selenium.getValue("//input[@name='onrejectgroup' and @value='onRejectSequenceKey']"));
            assertTrue(selenium.isTextPresent("Referring Sequence"));            
		    assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.throttle.rejectKey')\"]"));
        }

        assertEquals("Update", selenium.getValue("//input[@value='Update']"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Throttle");
    }
}
