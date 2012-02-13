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

public class ESBSendMediatorMainTest  extends CommonSetup{

    public ESBSendMediatorMainTest(String text) {
        super(text);
    }

    public void testAddMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
                
        seleniumTestBase.loginToUI("admin","admin");
        Thread.sleep(2000);

        //Paramters which could be passed are String endpointType, String radioSelection, String resourceName, String endpointURI, String format,
        // String optimize, String errCode, String durSec, String maxDur, String factor, String timoutErr, String retry, String action, String actDur
        // Options available for paramter endpointType=Add Address Endpoint,Add WSDL Endpoint,Add Failover Group,Add Load-balance Group && radioSelection=epOpNone,epOpAnon,epOpReg

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_send");

        esbCommon.testAddRootLevelChildren("Add Child","Core","Send");

        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbSendMediatorTest.testAddSendMediator("0","epOpAnon","", "http://localhost:9000/services/SimpleStockQuoteService","Leave As-Is", "Leave As-Is","","","","","","","","Never timeout","",null,null,null,null,null,null);

        ESBSendMediatorUITest esbSendMediatorUITest = new ESBSendMediatorUITest(selenium);
        esbSendMediatorUITest.testVerifySendMediator("0", "epOpAnon");
        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
    }

}
