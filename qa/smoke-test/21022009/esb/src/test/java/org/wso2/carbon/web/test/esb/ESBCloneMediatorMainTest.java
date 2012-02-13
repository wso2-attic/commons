package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBCloneMediatorMainTest  extends CommonSetup{

    public ESBCloneMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_clone");

        esbCommon.testAddRootLevelChildren("Add Child","Advanced","Clone");

        ESBCloneMediatorTest esbCloneMediatorTest = new ESBCloneMediatorTest(selenium);
        esbCloneMediatorTest.testAddCloneMediator("0","Yes");

        //The three radio button options available when setting sequences are mediator.target.seq.radio.none,mediator.target.seq.radio.anon and mediator.target.seq.radio.reg
        //The three radio button options available when setting endpoints are epOpNone,epOpAnon and epOpReg
        esbCloneMediatorTest.testAddTargetMediator("urn:add", "http://localhost:9000/services/SimpleStockQuoteService", "mediator.target.seq.radio.reg","epOpReg","fault","epr1");

        ESBCloneMediatorUITest esbCloneMediatorUITest = new ESBCloneMediatorUITest(selenium);
        esbCloneMediatorUITest.testVerifyTargetConfiguration();

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();
        
    }
}
