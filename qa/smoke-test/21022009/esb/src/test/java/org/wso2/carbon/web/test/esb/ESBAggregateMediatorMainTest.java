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

public class ESBAggregateMediatorMainTest  extends CommonSetup{

    public ESBAggregateMediatorMainTest(String text) {
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
        esbCommon.testAddSequence("sequence_aggregate");

        esbCommon.testAddRootLevelChildren("Add Child","Advanced","Aggregate");

        ESBAggregateMediatorTest esbAggregateMediatorTest = new ESBAggregateMediatorTest(selenium);

        //The two radio button options available are sequenceOptionAnon and sequenceOptionReference
        esbAggregateMediatorTest.testAddAggregateMediator("0");
        esbAggregateMediatorTest.testAddAggregateExpression("//m0:add","m0","http://services/samples");
        esbAggregateMediatorTest.testAddCompletionInfo("1000","2","2");
        esbAggregateMediatorTest.testAddCorelationExpression("//m0:div","m0","http://samples/services");
        esbAggregateMediatorTest.testAddOnCompleteMediator("sequenceOptionReference","Sequence","fault");
        esbCommon.testMediatorUpdate();

        ESBAggregateMediatorUITest esbAggregateMediatorUITest = new ESBAggregateMediatorUITest(selenium);
        esbAggregateMediatorUITest.testAddAggregateMediator("0","sequenceOptionReference");

        esbCommon.testSequenceSave();

        seleniumTestBase.logOutUI();
    }
}
