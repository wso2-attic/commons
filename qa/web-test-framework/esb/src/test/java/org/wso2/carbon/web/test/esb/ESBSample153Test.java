package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ESBSecurityClient;
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

public class ESBSample153Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample153Test(String text) {
        super(text);
    }

//    <proxy name="StockQuoteProxy">
//        <target>
//            <inSequence>
//                <send>
//                    <endpoint>
//                        <address uri="http://localhost:9000/services/SecureStockQuoteService"/>
//                    </endpoint>
//                </send>
//            </inSequence>
//            <outSequence>
//                <send/>
//            </outSequence>
//        </target>
//        <publishWSDL uri="file:repository/samples/resources/proxy/sample_proxy_1.wsdl"/>
//    </proxy>

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
    This method will create  createStockQuoteProxy153
     */
    public void createStockQuoteProxy153() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Creating a local entry to store the WSDL file

        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("StockQuoteProxy153", "Custom Proxy");
        esbAddProxyServiceTest.addRegistryLookupWsdl("proxy_wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.removeTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbSendMediatorTest.addAnonSendMediator("0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SecureStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        
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
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpNone", null,null,null,null);
        esbAddProxyServiceTest.saveProxyService();

    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void invokeClient() throws Exception{
	ESBCommon esbCommon = new ESBCommon(selenium);        
        boolean stockQuoteResponse = false;
	ESBSecurityClient esbSecurityClient = new ESBSecurityClient();

        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));

        if (esbCommon.getContextRoot().equals(null))
        {
        	stockQuoteResponse = esbSecurityClient.runSecurityClient("StockQuoteProxy153","http://services.samples","getQuote","getQuote","request","symbol");
        }
        else
        {
        	stockQuoteResponse = esbSecurityClient.runSecurityClient("StockQuoteProxy153","http://services.samples","getQuote","getQuote","request","symbol");
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
    public void testSample153Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
         seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        createLocalEntry();
        createStockQuoteProxy153();
        seleniumTestBase.logOutUI();
        invokeClient();
    }    
}
