package org.wso2.carbon.web.test.esb;
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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

public class ESBEntitlementMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBEntitlementMediatorUITest(Selenium _browser){
		selenium = _browser;
    }


    public void testverifyEntitlementMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);


	    assertTrue(selenium.isTextPresent("Entitlement Mediator"));
        assertTrue(selenium.isTextPresent("Entitlement Server"));
        assertTrue(selenium.isTextPresent("User Name"));
        assertTrue(selenium.isTextPresent("Password"));
        assertTrue(selenium.isElementPresent("remoteServiceUrl"));
        assertTrue(selenium.isElementPresent("remoteServicePassword"));
        assertTrue(selenium.isElementPresent("remoteServiceUserName"));
        assertTrue(selenium.isElementPresent("//div[@id='mediatorDesign']/div/div/a"));
        assertTrue(selenium.isElementPresent("//a[@onclick='showSource()']"));

        //ESBEntitlementMediatorTest esbEntitlementTest=new ESBEntitlementMediatorTest(selenium);
        //esbEntitlementTest.addMediator("UItest","UItest","UItest");

        Thread.sleep(2000);
        


        //verify the  help
       // esbCommon.mediatorHelp("Entitlement");

    }
}
