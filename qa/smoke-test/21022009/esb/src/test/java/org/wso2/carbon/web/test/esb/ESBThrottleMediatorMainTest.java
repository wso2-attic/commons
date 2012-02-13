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

public class ESBThrottleMediatorMainTest  extends CommonSetup{

    public ESBThrottleMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_throttle");

        esbCommon.testAddRootLevelChildren("Add Child","Advanced","Throttle");

        ESBThrottleMediatorTest esbThrottleMediatorTest = new ESBThrottleMediatorTest(selenium);

        //The radio button options available are policygroupInlineId,policygroupValueId && onacceptgroup, //input[@name='onacceptgroup' and @value='onAcceptSequenceKey']
        //  &&  onrejectgroup, //input[@name='onrejectgroup' and @value='onRejectSequenceKey']

        esbThrottleMediatorTest.testAddThrottleMediator("0","A", "policygroupValueId","throttle_policy");
        esbThrottleMediatorTest.testOnAcceptInfo("//input[@name='onacceptgroup' and @value='onAcceptSequenceKey'","fault");
        esbThrottleMediatorTest.testOnRejectInfo("//input[@name='onrejectgroup' and @value='onRejectSequenceKey']","main");
        esbCommon.testMediatorUpdate();

        ESBThrottleMediatorUITest esbThrottleMediatorUITest = new ESBThrottleMediatorUITest(selenium);
        esbThrottleMediatorUITest.testVerifyAggregateMediator("0","policygroupValueId","//input[@name='onacceptgroup' and @value='onAcceptSequenceKey'", "//input[@name='onrejectgroup' and @value='onRejectSequenceKey']");

        esbCommon.testSequenceSave();
    }
}