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


public class ESBFilterMediatorMainTest  extends CommonSetup{

    public ESBFilterMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
                
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_filter");

        esbCommon.testAddRootLevelChildren("Add Child","Filter","Filter");

        //The parameters which can be passed are String xPath, String src, String regex, String namespacePrefix, String namespaceURI, String selectionType
        //Options available for selectionType are xpath, xpathRex
        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);
        esbFilterMediatorTest.testAddFilterMediator("0","//m0:add","get-property('To')",".*/StockQuote.*","m0","http://services/samples","xpathRex");

        esbFilterMediatorTest.testAddThenChildMediators("Core", "Log");
        esbFilterMediatorTest.testAddElseChildMediators("Core", "Send");

        ESBFilterMediatorUITest esbFilterMediatorUITest = new ESBFilterMediatorUITest(selenium);
        esbFilterMediatorUITest.testVerifyFilterMediator("0");

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
    }
}

