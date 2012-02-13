package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
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

public class ESBRouterMediatorMainTest  extends CommonSetup{

    public ESBRouterMediatorMainTest(String text) {
        super(text);
    }

    /*
     * This method will be called to verify the mediator properties of the Sequence mediator
     */ 
    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_router");

        esbCommon.testAddRootLevelChildren("Add Child","Filter","Router");

        ESBRouterMediatorTest esbRouterMediatorTest = new ESBRouterMediatorTest(selenium);
        esbRouterMediatorTest.testAddRouterMediator("0","Yes");

        ESBRouterMediatorUITest esbRouterMediatorUITest = new ESBRouterMediatorUITest(selenium);
        esbRouterMediatorUITest.testVerifyRouterMediator("0");

        esbRouterMediatorTest.testAddRoute("//m0:add/m0:x", "m0", "http://services/samples", "//m0:add");
        esbRouterMediatorUITest.testVerifyRouterConfiguration();

        //The available options for radio button options for endpoints are epOpAnon, epOpNone and epOpReg
        //The available options for radio button options for sequences are mediator.target.seq.radio.anon, mediator.target.seq.radio.none and mediator.target.seq.radio.reg
        esbRouterMediatorTest.testAddTarget("urn:add", "http://localhost:9000/services/SimpleStockQuoteService", "mediator.target.seq.radio.none", "epOpAnon", "main", "epr1","Add Child","Core","Log");
        esbRouterMediatorUITest.testVerifyTargetConfiguration();

        esbCommon.testSequenceSave();
    }
}


