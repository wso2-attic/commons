package org.wso2.carbon.web.test.esb;

import org.apache.commons.io.FileUtils;
import org.wso2.carbon.web.test.client.ESBSecurityClient;
import org.wso2.carbon.web.test.client.ESBRMClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

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

public class ESBSample201Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample201Test(String text) {
        super(text);
    }

    /*
    This method will log into the management console
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
   This method will test adding of a Proxy Service with inline WSDL but with anonymous sequences
    */
    public void testCreateProxyService() throws Exception {
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("StockQuoteProxy201", "Custom Proxy");
        esbAddProxyServiceTest.addSourceUrlWsdl("file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.removeTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon",null);
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0","SequenceAcknowledgement");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsrm","http://schemas.xmlsoap.org/ws/2005/02/rm");
        esbHeaderMediatorTest.removeHeader();
        esbHeaderMediatorTest.setHeaderNSVal();
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("1","Sequence");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsrm","http://schemas.xmlsoap.org/ws/2005/02/rm");
        esbHeaderMediatorTest.removeHeader();
        esbHeaderMediatorTest.setHeaderNSVal();
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("2","AckRequested");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsrm","http://schemas.xmlsoap.org/ws/2005/02/rm");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("3");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        Thread.sleep(2000);

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpNone", null);
        esbAddProxyServiceTest.clickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("0");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpNone", null, null, null, null);
        esbAddProxyServiceTest.saveProxyService();
        selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
		selenium.click("//div[@id='workArea']/table[4]/tbody/tr/td[2]/strong/a");
		selenium.waitForPageToLoad("30000");
        esbAddProxyServiceTest.verifyProxy("StockQuoteProxy201");
    }

    /*
    This method will enabled security and apply the proper security to the Proxy Service
     */
    public void testApplyRM() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.enableRM("StockQuoteProxy201");
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void testInvokeClient() throws Exception{
        ESBRMClient esbrmClient = new ESBRMClient();
        boolean stockQuoteResponse=false;
        int stockQuoteResponseInt=0;
        
        /*
        Invoking NonRM Service with RM Client  - RMRequestReplyAddressableSOAP11Client (This is the default client that would run for sample 201)
         */
//       stockQuoteResponseInt =  esbrmClient.RMRequestReplyAddressableClient("StockQuoteProxy201","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking NonRM Service with RM Client  - RMRequestReplyAddressableSOAP12Client
         */
//        stockQuoteResponseInt =  esbrmClient.RMRequestReplyAddressableClient("NonRMProxy","soap12","getQuote","http://services.samples","getQuote","request","symbol");

        /*
        Invoking NonRM Service with RM Client  - RMRequestReplyAnonSOAP11Client
         */
//        stockQuoteResponse =  esbrmClient.RMRequestReplyAnonClient("NonRMProxy","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking NonRM Service with RM Client  - RMRequestReplyAnonSOAP12Client
         */
//        stockQuoteResponse =  esbrmClient.RMRequestReplyAnonClient("NonRMProxy","soap12","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking NonRM Service with RM Client  - RMOnewayAnonSOAP11Client
         */
//        esbrmClient.NonRMOnewayAnonClient("NonRMProxy","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking NonRM Service with RM Client  - RMOnewayAnonSOAP12Client 
         */
//        esbrmClient.NonRMOnewayAnonClient("NonRMProxy","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking RM Service with Non RM Client  - NonRMRequestReplyAddressableSOAP11Client
         */
//       stockQuoteResponseInt =  esbrmClient.NonRMRequestReplyAddressableClient("RMProxy","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking RM Service with Non RM Client  - NonRMRequestReplyAddressableSOAP12Client
         */
//        stockQuoteResponseInt =  esbrmClient.NonRMRequestReplyAddressableClient("RMProxy","soap12","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking RM Service with Non RM Client  - NonRMRequestReplyAnonSOAP11Client
         */
//        stockQuoteResponse =  esbrmClient.NonRMRequestReplyAnonClient("RMProxy","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking RM Service with Non RM Client  - NonRMRequestReplyAnonSOAP12Client 
         */
//        stockQuoteResponse =  esbrmClient.NonRMRequestReplyAnonClient("RMProxy","soap12","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking RM Service with Non RM Client  - NonRMOnewayAnonSOAP11Client
         */
//        esbrmClient.NonRMOnewayAnonClient("RMProxy","soap11","getQuote","http://services.samples","getQuote","request","symbol");


        /*
        Invoking RM Service with Non RM Client  - NonRMOnewayAnonSOAP12Client 
         */
//        esbrmClient.NonRMOnewayAnonClient("RMProxy","soap12","getQuote","http://services.samples","getQuote","request","symbol");
        
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
            System.out.println(stockQuoteResponse);
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }

        if (stockQuoteResponseInt>0){
            System.out.println("The response received!!!!");
            System.out.println(stockQuoteResponseInt);
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }

        Thread.sleep(5000);
    }

    /*
    This method will log out from the management console
     */
    public void testLogout() throws Exception{
       SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        Thread.sleep(5000);
        mySeleniumTestBase.logOutUI();
    }

}
