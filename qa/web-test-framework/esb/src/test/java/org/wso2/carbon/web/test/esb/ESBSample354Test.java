package org.wso2.carbon.web.test.esb;

import org.apache.commons.io.FileUtils;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
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

public class ESBSample354Test extends CommonSetup{

    public ESBSample354Test(String text) {
        super(text);
    }

    /*
    Creating the sequence
     */
    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBScriptMediatorTest esbScriptMediatorTest = new ESBScriptMediatorTest(selenium);
        String requestContent = null;
        String responseContent = null;

        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "requestRubyScript.rb");
            requestContent = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }

        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "responseRubyScript.rb");
            responseContent = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the XSLT mediator
        esbCommon.addMediators("Add Child","0","Extension","Script");
        esbScriptMediatorTest.addInlineScripts ("0.0","Ruby",requestContent);
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
        esbScriptMediatorTest.addInlineScripts ("1.0","Ruby",responseContent);
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.1");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_354");
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
    This method will add create the sample 354 sequence
     */
    public void testAddSample354Sequence() throws Exception{
      SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

     boolean login = selenium.isTextPresent("Sign-out");

     if (login){
         seleniumTestBase.logOutUI();
     }

    seleniumTestBase.loginToUI("admin","admin");
    createSequence("sample_354");
    seleniumTestBase.logOutUI();
    invokeClient();
    }
}
