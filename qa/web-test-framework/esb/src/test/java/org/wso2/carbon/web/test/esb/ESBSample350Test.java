package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.FileInputStream;
import java.io.File;
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

public class ESBSample350Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample350Test(String text) {
        super(text);
    }

//    <localEntry key="stockquoteScript" src="file:repository/samples/resources/script/stockquoteTransform.js"/>
//    <in>
//        <script language="js" key="stockquoteScript" function="transformRequest"/>
//        <send>
//            <endpoint>
//                <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//            </endpoint>
//        </send>
//    </in>
//    <out>
//        <script language="js" key="stockquoteScript" function="transformResponse"/>
//        <send/>
//    </out>


    public void addScriptEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("stockquoteScript_js","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.js");
    }

    /*
    Creating the sequence
     */
    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBScriptMediatorTest esbScriptMediatorTest = new ESBScriptMediatorTest(selenium);

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the XSLT mediator
        esbCommon.addMediators("Add Child","0","Extension","Script");
        esbScriptMediatorTest.addRegistryScripts("0.0","Javascript","transformRequest","stockquoteScript_js");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.1");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();

        //Addung the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the XSLT mediator
        esbCommon.addMediators("Add Child","1","Extension","Script");
        esbScriptMediatorTest.addRegistryScripts("1.0","Javascript","transformResponse","stockquoteScript_js");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.1");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();

    }

    /*
    Invoking the client
     */
    public void invokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();

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
    addScriptEntry();
    createSequence("sample_350");
    //Setting the created sequence to the main sequence
    esbCommon.setSequenceToSequence("main","sample_350");
    seleniumTestBase.logOutUI();
    invokeClient();
    }    
}
