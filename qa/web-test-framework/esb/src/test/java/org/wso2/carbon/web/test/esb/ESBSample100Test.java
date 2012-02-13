package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;

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

public class ESBSample100Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample100Test(String text) {
        super(text);
    }

//    <localEntry key="sec_policy" src="file:repository/samples/resources/policy/policy_3.xml"/>
//    <in>
//        <send>
//            <endpoint name="secure">
//                <address uri="http://localhost:9000/services/SecureStockQuoteService">
//                    <enableSec policy="sec_policy"/>
//                    <enableAddressing/>
//                </address>
//            </endpoint>
//        </send>
//    </in>
//    <out>
//        <header name="wsse:Security" action="remove"
//                xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"/>
//        <send/>
//    </out>

    /*
    Adding local entries to be used for the Send mediator
     */
    public void addLocalEntries() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Adding the security policy and rm policy as local entries
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("security_policy","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/policy/policy_3.xml");
    }

    public void testCreateSecuritySequence() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");

        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBInMediatorTest esbInMediatorTest = new ESBInMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBOutMediatorTest esbOutMediatorTest = new ESBOutMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        ESBSampleClient sampleClient = new ESBSampleClient();

        addLocalEntries();

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence("sample_100");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");

        //Adding a send mediator with an anonymous endpoint
        esbSendMediatorTest.addAnonSendMediator("0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SecureStockQuoteService"));
        esbSendMediatorTest.eprQosInfoSendMediator("wsAddressing",null,"wsSecurity","security_policy",null,null);
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Addung the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Header mediator
        esbCommon.addMediators("Add Child","1","Core","Header");
        esbHeaderMediatorTest.addHeaderMediator("1.0","Security");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsse","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.1");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_100");

        seleniumTestBase.logOutUI();

        /*
        Executing client for proxy_inline_wsdl_anon_seq Proxy Service
         */

        boolean stockQuoteResponse = sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", null, "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
        }

}
