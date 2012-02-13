package org.wso2.carbon.web.test.esb;

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

public class ESBSample3Test  extends CommonSetup{

    public ESBSample3Test(String text) {
        super(text);
    }

//    <localEntry key="version">0.1</localEntry>
//    <endpoint name="simple">
//        <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//    </endpoint>
//
//    <sequence name="stockquote">
//        <log level="custom">
//            <property name="Text" value="Sending quote request"/>
//            <property name="version" expression="get-property('version')"/>
//            <property name="direction" expression="get-property('direction')"/>
//        </log>
//        <send>
//            <endpoint key="simple"/>
//        </send>
//    </sequence>
//
//    <sequence name="main">
//        <in>
//            <property name="direction" value="incoming"/>
//            <sequence key="stockquote"/>
//        </in>
//        <out>
//            <send/>
//        </out>
//    </sequence>

    public void createLocalEntry() throws Exception{
        //Adding the local entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addInlineTextEntry("version","0.1");
    }

    public void createEndpoint() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding an address endpoint
        esbCommon.viewEndpoints();

        boolean addEprName = selenium.isTextPresent("simple");
        if (addEprName){
            //Do nothing
        } else{
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("simple",esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }

    public void createSequence(String seqName) throws Exception{
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);

        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        ESBSequenceMediatorTest esbSequenceMediatorTest = new ESBSequenceMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);


        esbCommon.viewSequences();
        boolean addSeqName = selenium.isTextPresent("stockquote");
        if (addSeqName){
            //Do nothing
        } else{
            //Creating sequence - stockquote
            esbCommon.addSequence("stockquote");

            //Adding the Log mediator
            esbCommon.addRootLevelChildren("Add Child", "Core", "Log");
            esbLogMediatorTest.addLogMediator("0","Custom");
            esbLogMediatorTest.addLogPropety("Text","Value","Sending");
            esbLogMediatorTest.addLogPropety("version","Expression","get-property('version')");
            esbLogMediatorTest.addLogPropety("direction","Expression","get-property('direction')");
            esbCommon.mediatorUpdate();

            esbCommon.addRootLevelChildren("Add Child", "Core", "Send");
            esbSendMediatorTest.addRegSendMediator("1","simple");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
        }

        //Creating sequence - sample_3
        esbCommon.addSequence(seqName);

         //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "In");
        esbSequenceTreePopulatorTest.clickMediator("0");
        esbCommon.addMediators("Add Child","0", "Core", "Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0","direction","set");
        esbPropertyMediatorTest.addPropertyMediator("value","incoming","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the Sequence mediator
        esbCommon.addMediators("Add Child","0", "Core", "Sequence");
        esbSequenceMediatorTest.addSequenceMediator("stockquote");
        esbCommon.mediatorUpdate();

        //Adding an Out mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "Out");
        esbSequenceTreePopulatorTest.clickMediator("1");
        esbCommon.addMediators("Add Child","1", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("1.0");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
    }

        /*
        Executing client for proxy_inline_wsdl_anon_seq Proxy Service
         */
    public void invokeClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", esbCommon.getServiceAddUrl("SimpleStockQuoteService"), "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();            
    }        

     public void testSample3Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
         seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        createLocalEntry();
        createEndpoint();
        createSequence("sample_3");
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_3");
        seleniumTestBase.logOutUI();
        invokeClient();
     }

}
