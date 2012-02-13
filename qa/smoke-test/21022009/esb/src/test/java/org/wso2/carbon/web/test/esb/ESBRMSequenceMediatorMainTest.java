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


public class ESBRMSequenceMediatorMainTest  extends CommonSetup{

    public ESBRMSequenceMediatorMainTest(String text) {
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
        esbCommon.testAddSequence("sequence_rmsequence");

        esbCommon.testAddRootLevelChildren("Add Child","Advanced","RMSequence");

        ESBRMSequenceMediatorTest esbrmSequenceMediatorTest = new ESBRMSequenceMediatorTest(selenium);
        esbrmSequenceMediatorTest.testAddRmSequenceMediator("0","1.1");
        esbrmSequenceMediatorTest.testSetMessageSeqInfo("correlationRadio","m0:add","m0", "http://samples/services", "//m1:div","m1", "http://services/samples");
        esbCommon.testMediatorUpdate();

        ESBRMSequenceMediatorUITest esbrmSequenceMediatorUITest = new ESBRMSequenceMediatorUITest(selenium);
        esbrmSequenceMediatorUITest.testVerifyRouterMediator("0","correlationRadio");

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
    }

}

