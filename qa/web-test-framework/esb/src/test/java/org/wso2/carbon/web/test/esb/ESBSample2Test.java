package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
//        <property name="epr" expression="get-property('To')"/>
//    </log>
//    <send/>

    public void createSequence(String seqName) throws Exception{
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Switch mediator
        esbCommon.addMediators("Add Child","0","Filter","Switch");
        esbSwitchMediatorTest.addSwitchMediator("0.0","//m0:getQuote/m0:request/m0:symbol");
        esbSwitchMediatorTest.addSwitchNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();

        //Adding the first level Case mediator
        esbSwitchMediatorTest.addCase("0.0","IBM","0.0.0");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.0", "Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.0.0","symbol","set");
        esbPropertyMediatorTest.addPropertyMediator("value","Great stock - IBM","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the second level Case mediator
        esbSwitchMediatorTest.addCase("0.0","MSFT","0.0.1");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.1", "Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.1.0","symbol","set");
        esbPropertyMediatorTest.addPropertyMediator("value","Are you sure? - MSFT","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the Default Case
        esbSwitchMediatorTest.addDefaultCase("0.0");
        esbSwitchMediatorTest.addDefaultChildMediator("0.0.2","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.2.0","symbol","set");
        esbPropertyMediatorTest.addPropertyMediator("expression","fn:concat('Normal Stock - ', //m0:getQuote/m0:request/m0:symbol)","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the Log mediator
        esbCommon.addMediators("Add Child","0","Core","Log");
        esbLogMediatorTest.addLogMediator("0.1","Custom");
        esbLogMediatorTest.addLogPropety("symbol","Expression","get-property('symbol')");
        esbLogMediatorTest.addLogPropety("epr","Expression","get-property('To')");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.2");
        //esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Addung the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.0");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void invokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();

        //boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", "http://localhost:9000/services/SimpleStockQuoteService");
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", esbCommon.getServiceAddUrl("SimpleStockQuoteService"), "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    public void testSampleSequence() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        esbCommon.setupMainSeq();
        createSequence("sample_2");
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_2");
        seleniumTestBase.logOutUI();
        invokeClient();
    }
}
