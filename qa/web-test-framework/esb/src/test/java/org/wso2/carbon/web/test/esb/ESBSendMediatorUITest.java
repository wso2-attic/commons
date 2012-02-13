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

public class ESBSendMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBSendMediatorUITest(Selenium _browser){
        selenium=_browser;
    }

    /*
     * This method will be called to verify the mediator properties of the Sequence mediator
     */
    public void testVerifySendMediator(String level, String radioSelection) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Send Mediator"));
		assertTrue(selenium.isTextPresent("Select Endpoint Type"));

        if (radioSelection.equals("epOpNone")){
            assertEquals("on", selenium.getValue("epOpNone"));
        }
        else if (radioSelection.equals("epOpAnon")){
		    assertEquals("on", selenium.getValue("epOpAnon"));
            assertEquals("Add", selenium.getText("epAnonAdd"));

        } else if (radioSelection.equals("epOpReg")){
		    assertEquals("on", selenium.getValue("epOpReg"));
            assertEquals("Registry Browser", selenium.getText("regEpLink"));
        }

        //Verifying the Help link on the mediator and its content
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Send");
    }
}
