package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;
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

public class ESBSwitchMediatorMainTest  extends CommonSetup{

    public ESBSwitchMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.loginToUI("admin","admin");


        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_switch");

        esbCommon.testAddRootLevelChildren("Add Child","Filter","Switch");

        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        esbSwitchMediatorTest.testAddSwitchMediator("0","//m0:add", "m0", "http://services/samples");

        Thread.sleep(2000);
        ESBSwitchMediatorUITest esbSwitchMediatorUITest = new ESBSwitchMediatorUITest(selenium);
        esbSwitchMediatorUITest.testVerifySwitchMediator("0");

        esbSwitchMediatorTest.testAddCase("0","IBM","0.0");
        Thread.sleep(2000);
        esbSwitchMediatorUITest.testVerifyCaseMediator();
        esbCommon.testAddChildMediators("0.0","Core","Log");

        esbSwitchMediatorTest.testAddCase("0","MSFT","0.1");
        esbCommon.testAddChildMediators("0.1","Core","Send");

        esbSwitchMediatorTest.testAddCase("0","SUN","0.2");
        esbCommon.testAddChildMediators("0.2","Core","Drop");
        
        esbSwitchMediatorTest.testAddDefaultCase("0");
        Thread.sleep(2000);
        esbSwitchMediatorUITest.testVerifySwitchDefaultmediator();

        esbSwitchMediatorTest.testAddDefaultChildMediator("Core", "Drop", "0.3");
        esbSwitchMediatorTest.testAddDefaultChildMediator("Core", "Log", "0.3");

        esbCommon.testSequenceSave();
    }

}
