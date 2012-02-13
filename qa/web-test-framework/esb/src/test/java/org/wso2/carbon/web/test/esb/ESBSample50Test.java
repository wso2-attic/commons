package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ESBRestClient;
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

public class ESBSample50Test extends CommonSetup{

    public ESBSample50Test(String text) {
        super(text);
    }

//    <filter source="get-property('To')" regex=".*/StockQuote.*">
//        <send>
//            <endpoint>
//                <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//            </endpoint>
//        </send>
//        <drop/>
//    </filter>
//    <send/>

    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the Filter mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "Filter");
        esbFilterMediatorTest.addSourceRegex("0","get-property('To')",".*/StockQuote.*");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0.0","Core", "Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.addFormatOptimizeInfoSendMediator("SOAP 1.1",null);
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Child","0.0","Core","Drop");

        //Adding the Send mediator
        esbCommon.addMediators("Add Sibling","0", "Core", "Send");
        //esbCommon.addRootLevelChildren("Add Sibling", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("1");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void invokeClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBRestClient esbRestClient = new ESBRestClient();
        ESBCommon esbCommon = new ESBCommon(selenium);

        boolean stockQuoteResponse = esbRestClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/StockQuote");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();        
    }

    public void testSample5Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }

        seleniumTestBase.loginToUI("admin","admin");
        createSequence("sample_50");
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_50");
        seleniumTestBase.logOutUI();
        invokeClient();
    }
}
