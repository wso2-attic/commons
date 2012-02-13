package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
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

public class ESBSample2Test extends CommonSetup{

    public ESBSample2Test(String text) {
        super(text);
    }


//    <switch source="//m0:getQuote/m0:request/m0:symbol" xmlns:m0="http://services.samples/xsd">
//        <case regex="IBM">
//            <property name="symbol" value="Great stock - IBM"/>
//        </case>
//        <case regex="MSFT">
//            <property name="symbol" value="Are you sure? - MSFT"/>
//        </case>
//        <default>
//            <property name="symbol"
//                  expression="fn:concat('Normal Stock - ', //m0:getQuote/m0:request/m0:symbol)"
//                  xmlns:m0="http://services.samples/xsd"/>
//        </default>
//    </switch>
//
//    <log level="custom">
//        <property name="symbol" expression="get-property('symbol')"/>
//
//        <property name="epr" expression="get-property('To')"/>
//    </log>
//    <send/>
    
    public void testSample2Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        
        seleniumTestBase.loginToUI("admin","admin");

        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.testAddSequence("Sample_2_Sequence");

        //Adding the Switch mediator
        esbCommon.testAddRootLevelChildren("Add Child", "Filter", "Switch");
        esbSwitchMediatorTest.testAddSwitchMediator("0","//m0:getQuote/m0:request/m0:symbol","m0","http://services.samples/xsd");
        esbSwitchMediatorTest.testAddCase("0","IBM","0.0");
        esbSwitchMediatorTest.testAddCaseChildMediator("Core","Property","0.0");
        esbPropertyMediatorTest.testAddPropertyMediator("0.0","symbol","set","value","Great stock - IBM","Axis2",null,null);

        esbSwitchMediatorTest.testAddCase("0","MSFT","0.1");
        esbSwitchMediatorTest.testAddCaseChildMediator("Core","Property","0.1");
        esbPropertyMediatorTest.testAddPropertyMediator("0.1","symbol","set","value","Are you sure? - MSFT","Axis2",null,null);

        esbSwitchMediatorTest.testAddDefaultCase("0");
        esbSwitchMediatorTest.testAddDefaultChildMediator("Core","Property","0.2");
        esbPropertyMediatorTest.testAddPropertyMediator("0.2","symbol","set","value","fn:concat('Normal Stock - ', //m0:getQuote/m0:request/m0:symbol)","Axis2",null,null);

        //Adding a Log mediator
        esbSequenceTreePopulatorTest.testClickMediator("0");
        esbCommon.testAddChildMediators("0","Core","Log");

        //String logLevel, String propertyName0, String propertyTypeSel0, String propertyValue0,
        //String propertyName1, String propertyTypeSel1, String propertyValue1, String propertyTypeSel2,
        //String propertyName2, String propertyValue2
        esbLogMediatorTest.testAddLogMediator("0","Custom");
        esbLogMediatorTest.testAddLogPropeties("symbol","Expression","get-property('symbol')","epr","Expression","get-property('To')",null,null,null);
        esbLogMediatorTest.testUpdateLogMediator();

        esbSequenceTreePopulatorTest.testClickMediator("1");
        esbCommon.testAddChildMediators("1","Core","Send");
        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
     }
}
