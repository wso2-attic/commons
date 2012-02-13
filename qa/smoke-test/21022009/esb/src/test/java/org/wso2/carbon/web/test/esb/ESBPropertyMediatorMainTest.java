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

public class ESBPropertyMediatorMainTest extends CommonSetup{

    public ESBPropertyMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        
        seleniumTestBase.loginToUI("admin","admin");
        Thread.sleep(2000);

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_property");

        esbCommon.testAddRootLevelChildren("Add Child","Core","Property");
        
        //Parameters that could be passed are String propertyName, String action, String actionAsVal, String propertyValue, String scope, String nsPrefix, String nsUri
        //Options available are action=set/remove, actionAsVal=value/expression, scope=Axis2,Transport,Synapse
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        esbPropertyMediatorTest.testAddPropertyMediator("0","RESPONSE","set","value","true","Axis2",null,null);

        ESBPropertyMediatorUITest esbPropertyMediatorUITest = new ESBPropertyMediatorUITest(selenium);
        esbPropertyMediatorUITest.testAddPropertyMediator("0","set","value");

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
    }
}
