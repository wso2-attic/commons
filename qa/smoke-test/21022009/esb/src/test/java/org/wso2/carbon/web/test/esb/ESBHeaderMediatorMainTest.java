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

public class ESBHeaderMediatorMainTest  extends CommonSetup {

    public ESBHeaderMediatorMainTest(String text) {
        super(text);
    }

    //This method will test adding a new Fault mediator
    public void testAddHeaderMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_header");

        //Adding the first header mediator
        esbCommon.testAddRootLevelChildren("Add Child","Transform","Header");

        ESBHeaderMediatorUITest esbHeaderMediatorUITest = new ESBHeaderMediatorUITest(selenium);
        esbHeaderMediatorUITest.testVerifyHeaderMediator("0");

        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbHeaderMediatorTest.testAddHeaderMediator("0","RESPONSE",null,null);
        esbHeaderMediatorTest.testSetHeaderAction("set","expression","//m0:add/m0:x","m0","http://samples/services");

        //Adding the second header mediator
        esbCommon.testAddRootLevelChildren("Add Child","Transform","Header");

        esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbHeaderMediatorTest.testAddHeaderMediator("1","RESPONSE",null,null);
        esbHeaderMediatorTest.testSetHeaderAction("remove","expression","//m0:add/m0:x","m0","http://samples/services");

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();
    }
}
