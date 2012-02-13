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

public class ESBSecuredAddressEndpointTest extends CommonSetup{
	Properties properties = new Properties();

    public ESBSecuredAddressEndpointTest(String text) {
        super(text);
    }


    /*
    This method will add a new address endpoint, assign it to a sequence and invoke using a client
     */
//        <in>
//            <send>
//                <endpoint name="secure">
//                    <address uri="http://localhost:9000/services/SecureStockQuoteService">
//                        <enableSec policy="sec_policy"/>
//                        <enableAddressing/>
//                    </address>
//                </endpoint>
//            </send>
//        </in>
//        <out>
//            <header name="wsse:Security" action="remove"
//                    xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"/>
//            <send/>
//        </out>

    public void createEndpoint() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding an endpoint which will be refered through the proxy wizard
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("add_sec_epr1");
        System.out.println(endpoint);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("add_sec_epr1","http://localhost:9000/services/SecureStockQuoteService");
            esbAddAddressEndpointTest.addAddressEprQosInfo("wsAddressing",null,"wsSecurity","sec_policy",null,null);
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }

    public void createLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Adding the security policy and rm policy as local entries
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("sec_policy","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/policy/policy_3.xml");
    }

    public void createSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);

        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence("sec_add_epr_test_seq");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addRegSendMediator("0.0","add_sec_epr1");
        esbCommon.mediatorUpdate();

        //Adding the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Header mediator
        esbCommon.addMediators("Add Child","1","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("1.0","Security");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsse","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.1");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
    }

    public void invokeClient() throws Exception{
        ESBSampleClient sampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Invoking the client
        sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", null, "IBM");
       esbCommon.closeFiles();        
    }

    public void testVerifyAddressEndpoints() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        createLocalEntry();
        createEndpoint();
        createSequence();

        //Assigning the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sec_add_epr_test_seq");

        //Loging out from the console
        seleniumTestBase.logOutUI();
        invokeClient();
    }    
}
