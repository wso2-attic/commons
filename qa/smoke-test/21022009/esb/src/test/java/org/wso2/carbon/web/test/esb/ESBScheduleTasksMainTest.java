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

public class ESBScheduleTasksMainTest  extends CommonSetup{

    public ESBScheduleTasksMainTest(String text) {
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

//        ESBScheduleTasksUITest esbScheduleTasksUITest = new ESBScheduleTasksUITest(selenium);
//        esbScheduleTasksUITest.testVerifyScheduleTasks();

        // Parameters that could be passed are String taskName, String taskClass, String propertyType0, String propertyValue0,
        // String propertyType1, String propertyValue1, String propertyType2, String propertyValue2, String propertyType3,
        // String propertyValue3,String triggerCount, String triggerInterval
        //Option which could be provided for propertyType=XML, Literal
        ESBScheduleTasksTest esbScheduleTasksTest = new ESBScheduleTasksTest(selenium);
        esbScheduleTasksTest.testAddNewTask("CheckPrice","org.apache.synapse.startup.tasks.MessageInjector","XML","<m0:getQuote xmlns:m0=\"http://services.samples/xsd\"><m0:request><m0:symbol>IBM</m0:symbol></m0:request></m0:getQuote>",
                "Literal", "","Literal","http://localhost:9000/services/SimpleStockQuoteService","Literal","urn:getQuote","2","2000");
        seleniumTestBase.logOutUI();        
    }
}
