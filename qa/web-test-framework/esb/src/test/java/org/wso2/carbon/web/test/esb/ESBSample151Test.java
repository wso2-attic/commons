package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
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

public class ESBSample151Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample151Test(String text) {
        super(text);
    }

//    <sequence name="proxy_1">
//        <send>
//            <endpoint><address uri="http://localhost:9000/services/SimpleStockQuoteService"/></endpoint>
//        </send>
//    </sequence>
//    <sequence name="out">
//        <send/>
//    </sequence>
//    <endpoint name="proxy_2_endpoint">
//        <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//    </endpoint>
//    <localEntry key="proxy_wsdl" src="file:repository/samples/resources/proxy/sample_proxy_1.wsdl"/>
//    <proxy name="StockQuoteProxy1">
//        <publishWSDL key="proxy_wsdl"/>
//        <target inSequence="proxy_1" outSequence="out"/>
//    </proxy>
//    <proxy name="StockQuoteProxy2">
//        <publishWSDL key="proxy_wsdl"/>
//        <target endpoint="proxy_2_endpoint" outSequence="out"/>
//    </proxy>

    /*
    This method will create sequence proxy_1
     */
    public void createSeqProxy1() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
//        esbCommon.viewSequences();
//        boolean sequence = selenium.isTextPresent("proxy_1");
//        System.out.println(sequence);
//
//        if (sequence) {
//            esbCommon.deleteSequence("proxy_1");
//        } else {
//            //Do nothing
//        }
        //Loging in and creating the sequence
        esbCommon.addSequence("proxy_1");

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();
    }

    /*
    This method will create sequence out
     */
    public void createSeqOut() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
//        esbCommon.viewSequences();
//        boolean sequence = selenium.isTextPresent("out");
//        System.out.println(sequence);
//
//        if (sequence) {
//            esbCommon.deleteSequence("out");
//        } else {
//            //Do nothing
//        }
        //Loging in and creating the sequence
        esbCommon.addSequence("out");

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("0");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();
    }

    /*
    This method will add the endpoint required for the Proxy Service
     */
   public void createEndpoint() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Adding an endpoint which will be refered through the proxy wizard
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("proxy_2_endpoint");
        System.out.println(endpoint);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("proxy_2_endpoint",esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }

    /*
    This method will create the Local Entry
     */
    public void createLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Creating a local entry to store the WSDL file
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("proxy_wsdl","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
    }

    /*
    This method will create  StockQuoteProxy1
     */
    public void createStockQuoteProxy1() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Creating a local entry to store the WSDL file

        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("StockQuoteProxy1", "Custom Proxy");
        esbAddProxyServiceTest.addRegistryLookupWsdl("proxy_wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpImp", "proxy_1");

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpNone", null);
        esbAddProxyServiceTest.clickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpImp", "out");

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpNone", null,null,null,null);
        esbAddProxyServiceTest.saveProxyService();
    }

    /*
    This method will create  StockQuoteProxy1
     */
    public void createStockQuoteProxy2() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Creating a local entry to store the WSDL file

        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("StockQuoteProxy2", "Custom Proxy");
        esbAddProxyServiceTest.addRegistryLookupWsdl("proxy_wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpNone", null);

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpImp", "proxy_2_endpoint");
        esbAddProxyServiceTest.clickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpImp", "out");

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpNone", null,null,null,null);
        esbAddProxyServiceTest.saveProxyService();
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void invokeClientProxy1() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = false;
        ESBSampleClient sampleClient = new ESBSampleClient();

        if (esbCommon.getContextRoot().equals(null))
        {
            stockQuoteResponse = sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/StockQuoteProxy1", null, "IBM");
        }
        else
        {
            stockQuoteResponse = sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/StockQuoteProxy1", null, "IBM");
        }

        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void invokeClientProxy2() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = false;
        ESBSampleClient sampleClient = new ESBSampleClient();

        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        String context_root =  properties.getProperty("context.root");

        if (context_root.equals(null))
        {
            stockQuoteResponse = sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/StockQuoteProxy2", null, "IBM");
        }
        else
        {
            stockQuoteResponse = sampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+context_root+"/services/StockQuoteProxy2", null, "IBM");
        }

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
    public void testSample151Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
         seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        createEndpoint();
        createLocalEntry();
        createSeqProxy1();
        createSeqOut();
        createStockQuoteProxy1();
        createStockQuoteProxy2();
        seleniumTestBase.logOutUI();
        invokeClientProxy1();
        invokeClientProxy2();
    }
}

