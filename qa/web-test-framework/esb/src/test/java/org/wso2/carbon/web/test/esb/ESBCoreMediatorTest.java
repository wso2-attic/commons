package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;/*
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

public class ESBCoreMediatorTest extends CommonSetup{
    Properties properties = new Properties();

    public ESBCoreMediatorTest(String text) {
        super(text);
    }

    /*
    This method will add Event sources which would be needed when creating the sequence
     */
    public void addEventSources() throws Exception{
        ESBEventSourceTest esbEventSourceTest = new ESBEventSourceTest("");
        //Adding a default event source
        esbEventSourceTest.addDefaultEventSource("sampleEventSource","Topic","TopicNamespace");
//        esbEventSourceTest.testAddRegistryEventSource("SampleRegEventSource","Topic","TopicNamespace","http://localhost:9444/registry","esb","esb");
    }

    /*
    Adding local entries to be used for the Send mediator
     */
    public void addLocalEntries() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Adding the security policy and rm policy as local entries
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("sec_policy","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/policy/policy_3.xml");
        esbManageLocalEntriesTest.addSourceUrlEntry("rm_policy","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/policy/sample_rm_policy_1.xml");
    }

    /*
    Creating an endpoint to be used when creating the send mediator
     */
    public void addEndpoint() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.viewEndpoints();

        boolean addEprName = selenium.isTextPresent("epr1");
        if (addEprName){
            //Do nothing
        } else{
            //Adding an Address endpoint
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("epr1","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }


    /*
    This method will add a Drop medaitor to the sequence tree
     */
    public void addDropMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Core","Drop");

        ESBDropMediatorTest esbDropMediatorTest = new ESBDropMediatorTest(selenium);
        esbDropMediatorTest.addDropMediator("0");
    }

    /*
    This method will add a Log mediator
     */
    public void addLogMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        esbLogMediatorTest.addLogMediator("1","Custom");
        esbLogMediatorTest.addLogPropety("text_value","Value","This is a custom property");
        esbLogMediatorTest.addLogPropety("text_expression","Expression","//m0:add");
        esbCommon.setNsLevel();
        esbLogMediatorTest.addLogExpressionNameSpaces("m0","http://services/samples0");
        esbLogMediatorTest.addLogExpressionNameSpaces("m1","http://services/samples1");
        esbLogMediatorTest.addLogExpressionNameSpaces("m2","http://services/samples2");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        esbLogMediatorTest.addLogMediator("2","Simple");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        esbLogMediatorTest.addLogMediator("3","Headers");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        esbLogMediatorTest.addLogMediator("4","Full");
        esbCommon.mediatorUpdate();
    }

    /*
    This method will add a Property mediator
     */
    public void addPropertyMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Core","Property");

        //Parameters that could be passed are String propertyName, String action, String actionAsVal, String propertyValue, String scope, String nsPrefix, String nsUri
        //Options available are action=set/remove, actionAsVal=value/expression, scope=Axis2,Transport,Synapse
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        esbPropertyMediatorTest.addBasicPropInfo("5","RESPONSE","set");
        esbPropertyMediatorTest.addPropertyMediator("value","true","Axis2");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("6","To","remove");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("7","Reply-To","set");
        esbPropertyMediatorTest.addPropertyMediator("expression","//m0:getQuote/m1:request/m2:symbol","Synapse");
        esbPropertyMediatorTest.addPropNamespace("m0","http://services/samples0");
        esbPropertyMediatorTest.addPropNamespace("m1","http://services/samples1");
        esbPropertyMediatorTest.addPropNamespace("m2","http://services/samples2");
        esbCommon.mediatorUpdate();
    }

    /*
    This method will add an Event mediator
     */
    public void addEventMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBEventMediatorTest esbEventMediatorTest = new ESBEventMediatorTest(selenium);

        esbCommon.addRootLevelChildren("Add Child","Core","Event");
        esbEventMediatorTest.addEventMediator("8","sampleEventSource");

//        esbCommon.addRootLevelChildren("Add Child","Core","Event");
//        esbEventMediatorTest.addEventMediator("9","SampleRegEventSource");
        esbCommon.mediatorUpdate();
    }

    /*
    This method will add a Sequence mediator
     */
    public void addSequenceMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceMediatorTest esbSequenceMediatorTest = new ESBSequenceMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);

        esbCommon.addRootLevelChildren("Add Child","Core","Sequence");
        esbSequenceTreePopulatorTest.setFocusMediator("9");
        esbSequenceMediatorTest.addSequenceMediator("main");
        esbCommon.mediatorUpdate();
    }

    /*
    This method will add a Send mediator
     */
    public void addSendMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        //Adding a send mediator without endpoints
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbSendMediatorTest.addNormalSendMediator("10");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        //Adding a send mediator with an anonymous endpoint
        esbSendMediatorTest.addAnonSendMediator("11");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.addFormatOptimizeInfoSendMediator("Leave As-Is", "Leave As-Is");
        esbSendMediatorTest.addSuspendInfoSendMediator("101504","10000","1000","1.0");
        esbSendMediatorTest.retryTimeoutInfoSendMediator("101503","2","10000");
        esbSendMediatorTest.timeoutInfoSendMediator("Never timeout","30");
        esbSendMediatorTest.eprQosInfoSendMediator("wsAddressing","sepListener","wsSecurity","sec_policy","wsRM","rm_policy");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        //Adding a send mediator with an refered endpoint
        esbSendMediatorTest.addRegSendMediator("12","epr1");
        esbCommon.mediatorUpdate();
    }
    
    /*
    This method will add all the mediators which are under the 'Core' category to a sequence and update the Synapse confoguration
     */
    public void testAddCoreMediators() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");

        addEventSources();
        addLocalEntries();
        addEndpoint();
        esbCommon.addSequence("sequence_core");
        addDropMediator();
        addLogMediator();
        addPropertyMediator();
        addEventMediator();
        addSequenceMediator();
        addSendMediator();

        esbCommon.sequenceSave();
        seleniumTestBase.logOutUI();
    }

}
