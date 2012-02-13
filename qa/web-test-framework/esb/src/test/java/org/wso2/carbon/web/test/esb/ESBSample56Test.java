package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ESBRestClient;
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

public class ESBSample56Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample56Test(String text) {
        super(text);
    }

//    <sequence name="main">
//        <in>
//            <send>
//                <endpoint>
//                    <wsdl uri="file:repository/samples/resources/proxy/sample_proxy_1.wsdl" service="SimpleStockQuoteService" port="SimpleStockQuoteServiceSOAP11port_http"/>
//                </endpoint>
//            </send>
//        </in>
//        <out>
//            <send/>
//        </out>
//    </sequence>

    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        
        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addWsdlEprAnonSendMediator("0.0");
        esbSendMediatorTest.addUriMandInfoSendMediator(null,"file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl","SimpleStockQuoteService","SimpleStockQuoteServiceSOAP11port_http");
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
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);

        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/",null, "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();        
    }

    /*
    This method will create the seqence and invoke the client
     */
    public void testSample5Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }

        seleniumTestBase.loginToUI("admin","admin");
        createSequence("sample_56");
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_56");
        seleniumTestBase.logOutUI();
        invokeClient();
    }    
}
