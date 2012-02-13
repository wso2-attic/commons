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

public class ESBSample300Test extends CommonSetup{

    public ESBSample300Test(String text) {
        super(text);
    }

    /*
        <task class="org.apache.synapse.startup.tasks.MessageInjector" name="CheckPrice">
            <property name="to" value="http://localhost:9000/services/SimpleStockQuoteService"/>
            <property name="soapAction" value="urn:getQuote"/>
            <property name="message">
                <m0:getQuote xmlns:m0="http://services.samples">
                    <m0:request>
                        <m0:symbol>IBM</m0:symbol>
                    </m0:request>
                </m0:getQuote>
            </property>
            <trigger interval="5"/>
        </task>

    <in>
        <send/>
    </in>
    <out>
        <log level="custom">
            <property name="Stock_Quote_on" expression="//ns:return/ns:lastTradeTimestamp/child::text()" xmlns:ns="http://services.samples/xsd"/>
            <property name="For_the_organization" expression="//ns:return/ns:name/child::text()" xmlns:ns="http://services.samples/xsd"/>
            <property name="Last_Value" expression="//ns:return/ns:last/child::text()" xmlns:ns="http://services.samples/xsd"/>
        </log>
    </out>
    */


    public void createTask(String taskName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBScheduleTasksTest esbScheduleTasksTest = new ESBScheduleTasksTest(selenium);
        esbScheduleTasksTest.addMandInfo(taskName);
        esbScheduleTasksTest.addImplInfo("org.apache.synapse.startup.tasks.MessageInjector","XML","<m0:getQuote xmlns:m0=\"http://services.samples\"><m0:request><m0:symbol>IBM</m0:symbol></m0:request></m0:getQuote>","Literal",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"Literal","urn:getQuote","Literal","");
        esbScheduleTasksTest.addTriggerInfo("2","2000");
        esbScheduleTasksTest.scheduleTask();
    }

    public void createSequence(String seqName) throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");

        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");

        //Addung the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Log mediator
        esbCommon.addMediators("Add Child","1","Core","Log");
        esbLogMediatorTest.addLogMediator("1.0","Custom");
        esbLogMediatorTest.addLogPropety("Stock_Quote_on","Expression","//m0:return/m0:lastTradeTimestamp/child::text()");
        esbLogMediatorTest.addLogExpressionNameSpaces("m0","http://services.samples/xsd");
        esbLogMediatorTest.setNsLevel_log();
        esbLogMediatorTest.addLogPropety("For_the_organization","Expression","//m0:return/m0:name/child::text()");
        esbLogMediatorTest.addLogExpressionNameSpaces("m0","http://services.samples/xsd");
        esbLogMediatorTest.setNsLevel_log();
        esbLogMediatorTest.addLogPropety("Last_Value","Expression","//m0:return/m0:last/child::text()");
        esbLogMediatorTest.addLogExpressionNameSpaces("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();

        //setup the main sequence
        esbCommon.setupMainSeq();
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main",seqName);

//        ESBScheduleTasksTest esbScheduleTasksTest = new ESBScheduleTasksTest(selenium);
//        esbScheduleTasksTest.rescheduleTask();
        seleniumTestBase.logOutUI();        
        }

    public void testAll() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        createSequence("sample_300");
        createTask("CheckPrice");
    }
}

