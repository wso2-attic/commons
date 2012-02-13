package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
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

public class ESBFaultMediatorMainTest extends CommonSetup {

    public ESBFaultMediatorMainTest(String text) {
        super(text);
    }

    //This method will test adding a new Fault mediator
    public void testAddFaultMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_fault");

        esbCommon.testAddRootLevelChildren("Add Child","Transform","Fault");

        ESBFaultMediatorUITest esbFaultMediatorUITest = new ESBFaultMediatorUITest(selenium);
        esbFaultMediatorUITest.testVerifyFaultMediatorUI("0");

        ESBFaultMediatorTest esbFaultMediatorTest = new ESBFaultMediatorTest(selenium);
        //soapVersion options available are - soap_version and //input[@id='soap_version' and @name='soap_version' and @value='2']
        //faultCode options available are
        // - soap11 - mustUnderstand, versionMismatch, Client,Server
        // - soap12 - versionMismatch, mustUnderstand, dataEncodingUnknown, Sender,Receiver 
        esbFaultMediatorTest.testSetFaultMediatorSoapVersion("0","soap_version","Server");

        //Options available for fault string are fault_string and //input[@name='fault_string' and @value='expression
        esbFaultMediatorTest.testSetFaultCode("//input[@name='fault_string' and @value='expression","test","m0","http://services/samples");
        esbFaultMediatorTest.testSetFaultGeneralInfo("soap_version","actor","node","detail");
        esbCommon.testMediatorUpdate();

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();
    }
}
