package org.wso2.carbon.web.test.mashup;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.Selenium;


public class MSaccessSecServiceTest extends CommonSetup {

    public MSaccessSecServiceTest(String text) {
        super(text);
    }

     /*
    *  Sign-in to Mashup Server admin console
     */
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //Accessing tryit to test invokeSecService service.
    public void testInvokeSecService() throws Exception{
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario1");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario2");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario3");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario4");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario5");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario6");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario7");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario8");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario9");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario10");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario11");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario12");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario13");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario14");
        MSaccessSecService.testInvokeGetVersionForScenarios("version","invokeSecService","scenario15");
    }

      /*
         Sign-out from Mashup
       */
     public void testSignout() throws Exception {
          SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
          instseleniumTestBase.logOutUI();
     }
}
