package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBRestClient;
import org.wso2.carbon.web.test.client.ESBSampleClient;

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

public class ESBGeneralAddressEndpointTest extends CommonSetup{
    Properties properties = new Properties();

    public ESBGeneralAddressEndpointTest(String text) {
        super(text);
    }

    /*
    This method is used to login to the management console
     */
    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
    }

    /*
    This method will create the endpoint
     */
    public void testCreateEndpoint() throws Exception{
        //Adding an endpoint which will be refered through the proxy wizard
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("add_epr1");
        System.out.println(endpoint);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("add_epr1","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.addAddressEprFormatOptimizeInfo("SOAP 1.1",null);
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }

    /*
    This method will create the sequence
     */
    public void testCreateSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBSampleClient sampleClient = new ESBSampleClient();

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence("address_epr_test_seq");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Filter mediator
        esbCommon.addMediators("Add Child","0","Filter","Filter");
        esbFilterMediatorTest.addSourceRegex("0.0","get-property('To')",".*/StockQuote.*");
        esbCommon.mediatorUpdate();

        esbFilterMediatorTest.addThenChildMediators("0.0.0","Core","Send");
        esbSendMediatorTest.addRegSendMediator("0.0.0.0","add_epr1");
        esbCommon.mediatorUpdate();

        //Adding the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.0");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
    }

    /*
    This method will add a new address endpoint, assign it to a sequence and invoke using a client
     */
    public void testVerifyAddressEndpoints() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.setSequenceToSequence("main","address_epr_test_seq");
    }

    /*
    This method will invoke the client
     */
    public void testInvokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = false;

        ESBSampleClient sampleClient = new ESBSampleClient();
        stockQuoteResponse = sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/StockQuote", null, "IBM");
       
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            System.out.println("Client Failed!!!!");
        }
       esbCommon.closeFiles();
    }

    /*
    This mthod will be used to log out from the management console
    */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
