package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.*;

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

public class ESBEventMediatorTest extends TestCase  {
    Selenium selenium;

    public ESBEventMediatorTest(Selenium _browser){
        selenium=_browser;
    }

    /*
     * This method will be called when a person wants to add Event mediators
     */
    public void testAddEventMediator(String level, String eventSourceName) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        
        selenium.select("mediator.event.source", "label="+ eventSourceName);

        //Updating the mediator
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testMediatorUpdate();
    }

    /*
     * This method will be called when a person wants to edit Event mediators
     */
     public void testEditEventMediator(String level, String eventSourceName) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        selenium.waitForPageToLoad("30000");

        //Selecting the new value in the edit mode 
        selenium.select("mediator.event.source", "label="+ eventSourceName);

        //Updating the mediator
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testMediatorUpdate();
    }

    /*
     * The delete method of the Event mediator is invluded in the ESBCommon.java class since it is common to all mediators
     */

}
