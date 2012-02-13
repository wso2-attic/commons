package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;/*
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

public class ESBSample8Test extends CommonSetup{
	Properties properties = new Properties();

    public ESBSample8Test(String text) {
        super(text);
    }

//    <registry provider="org.wso2.carbon.mediation.registry.ESBRegistry">
//        <parameter name="root">file:./repository/samples/resources/</parameter>
//        <parameter name="cachableDuration">15000</parameter>
//    </registry>
//
//    <localEntry key="xslt-key-req" src="file:repository/samples/resources/transform/transform.xslt"/>
//
//    <in>
//        <xslt key="xslt-key-req"/>
//    </in>
//    <out>
//        <xslt key="transform/transform_back.xslt"/>
//    </out>
//    <send/>

    /*
    Creating the local entry key for the incoming transformation
     */
    public void addReqXsltLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("xslt-key-req","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/transform/transform.xslt");
    }

    /*
    Creating the local entry key for the outgoing transformation
     */
    public void addResXsltLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("xslt-key-res","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/transform/transform_back.xslt");
    }

    /*
    Creating the sequence
     */
    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        ESBRMSequenceMediatorTest esbrmSequenceMediatorTest = new ESBRMSequenceMediatorTest(selenium);
        ESBSampleClient sampleClient = new ESBSampleClient();
        ESBXSLTMediatorTest esbxsltMediatorTest = new ESBXSLTMediatorTest(selenium);
        

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the XSLT mediator
        esbCommon.addMediators("Add Child","0","Transform","XSLT");
        esbxsltMediatorTest.addXSLTMediator("0.0","xslt-key-req");
        esbCommon.mediatorUpdate();

        //Addung the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the XSLT mediator
        esbCommon.addMediators("Add Child","1","Transform","XSLT");
        esbxsltMediatorTest.addXSLTMediator("1.0","xslt-key-res");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("2");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_8");
    }

    /*
    Invoking the client
     */
    public void invokeClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = esbSampleClient.customQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();        
    }
        
    /*
    This method will add create the sample 8 sequence
     */
    public void testAddSample8Sequence() throws Exception{
      SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
      ESBCommon esbCommon = new ESBCommon(selenium);

     boolean login = selenium.isTextPresent("Sign-out");

     if (login){
         seleniumTestBase.logOutUI();
     }

    seleniumTestBase.loginToUI("admin","admin");
    addReqXsltLocalEntry();
    addResXsltLocalEntry();
    createSequence("sample_8");
    //Setting the created sequence to the main sequence
    esbCommon.setSequenceToSequence("main","sample_8");
    seleniumTestBase.logOutUI();
    invokeClient();
    }
}
