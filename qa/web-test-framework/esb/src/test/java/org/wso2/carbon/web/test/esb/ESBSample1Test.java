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

public class ESBSample1Test  extends CommonSetup {

    public ESBSample1Test(String text) {
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

    public void testSample1Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

        esbCommon.logoutLogin();
        esbCommon.setupMainSeq();
        createSequence("sample_1");
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_1");
        seleniumTestBase.logOutUI();
        invokeClient();
    }
    
    public void createSequence(String seqName) throws Exception {
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
        esbFilterMediatorTest.addThenChildMediators("0.0","Core", "Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.0");
        //esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbFilterMediatorTest.addThenChildMediators("0.0","Core","Drop");

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("1");
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

        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/StockQuote",null, "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }
}

