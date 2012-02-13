package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
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

public class ESBSample390Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample390Test(String text) {
        super(text);
    }

    /*
    This method adds a xquery Local Entry
     */
    public void createRequestKey() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("xquery-key-req","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/xquery/xquery_req.xq");
    }

    /*
    This method adds a xquery Local Entry
     */
    public void createResponseKey() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("xquery-key-res","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/xquery/xquery_res.xq");
    }

    /*
    This method will create the proxy wsdl Local Entry
     */
    public void createProxy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Creating a local entry to store the WSDL file
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("proxy_wsdl","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
    }

    /*
    This method will create  createStockQuoteProxy153
     */
    public void createStockQuoteProxy390() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBXqueryMediatorTest esbXqueryMediatorTest = new ESBXqueryMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        //Creating a local entry to store the WSDL file

        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("StockQuoteProxy390", "Custom Proxy");
        esbAddProxyServiceTest.addRegistryLookupWsdl("proxy_wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.removeTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Transform","XQuery");
        esbXqueryMediatorTest.addXqueryKey("0","xquery-key-req");
        esbXqueryMediatorTest.addVariables("ELEMENT","payload",null);
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("1");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpNone", null);
        esbAddProxyServiceTest.clickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Transform","XQuery");
        esbXqueryMediatorTest.addXqueryKey("0","xquery-key-res");
        esbXqueryMediatorTest.addVariables("ELEMENT","payload",null);
        esbXqueryMediatorTest.addExpressionVariable("ELEMENT","code","self::node()//m0:return/m0:symbol/child::text()",null);
        esbXqueryMediatorTest.addVariableNamespace("m0","http://services.samples/xsd");
        esbXqueryMediatorTest.addExpressionVariable("DOUBLE","price","self::node()//m0:return/m0:last/child::text()",null);
        esbXqueryMediatorTest.addVariableNamespace("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpNone", null,null,null,null);
        esbAddProxyServiceTest.saveProxyService();
    }

    /*
    Executing client for invokeSunClient Proxy Service
     */
    public void invokeClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);

//        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://localhost:8280/",null);
        boolean stockQuoteResponse = esbSampleClient.customQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/StockQuoteProxy390",null,"IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            System.out.println("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
        
    }
    
    public void testSample4Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }

        seleniumTestBase.loginToUI("admin","admin");
        createRequestKey();
        createResponseKey();
        createProxy();
        createStockQuoteProxy390();
        seleniumTestBase.logOutUI();
        invokeClient();
    }    
}
