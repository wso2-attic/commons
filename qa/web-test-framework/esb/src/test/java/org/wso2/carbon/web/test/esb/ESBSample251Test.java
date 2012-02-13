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


package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBJMSClient;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class ESBSample251Test extends CommonSetup{

    public ESBSample251Test(String text) {
        super(text);
    }

   /*
    Method to log into the system
     */
    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");
    }

    /*
    Set Transport Listener
     */
    public void testSetJmsTransportListner() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.configureTransportListner("JMS");
        esbCommon.configureTransportSender("JMS");
    }

    /*
    Method to set Proxy Service General settings
     */
    public void testSetGeneralSettings() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("StockQuoteProxy251", "Custom Proxy");
        esbAddProxyServiceTest.addSourceUrlWsdl("file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        System.out.println("Setting general settings");
    }

    /*
    Method to set Proxy Service Transport settings
     */
    public void testSetTransportSettings() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.removeTransport("jms");
        esbAddProxyServiceTest.removeTransport("https");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.clickNext();
        System.out.println("Setting transport settings");
    }

   /*
   This method will test setting up of the In Sequence of the Proxy Service wizard
    */
    public void testSetInSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0","OUT_ONLY","set");
        esbPropertyMediatorTest.addPropertyMediator("value","true","Synapse");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        System.out.println("The In sequence was created successfully");
    }

    /*
   This method will test setting up of the Endpoint of the Proxy Service wizard
    */
    public void testSetEndpoint() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpAnon", null);
        esbAddAddressEndpointTest.addAnonAddressEndpoint();
        esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,esbCommon.getServiceAddUrl("JmsSimpleStockQuoteService"));
        esbAddAddressEndpointTest.saveAddressEndpoint();
        esbAddProxyServiceTest.clickNext();
        System.out.println("The endpoints was created successfully");
    }

    /*
   This method will test setting up of the Out Sequence of the Proxy Service wizard
    */
    public void testSetOutSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("0");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        System.out.println("The Out sequence was created successfully");
    }

    /*
   This method will test saving the proxy Service
    */
    public void testSaveProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.saveProxyService();
        System.out.println("The Proxy Service was saved successfully");
    }

    /*
   This method will verify the proxy Service
    */
    public void testVerifyProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.verifyProxy("StockQuoteProxy251");
    }

    /*
    This method will invoke the client
     */
    public void testInvokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        OMElement stockQuoteResponse = null;
        ESBSampleClient sampleClient = new ESBSampleClient();

        if (esbCommon.getContextRoot().equals(null))
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/StockQuoteProxy251", null, "MSFT");
        }
        else
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/StockQuoteProxy251", null, "MSFT");
        }

        Iterator iterator =   stockQuoteResponse.getFirstElement().getChildrenWithName(new QName("http://services.samples/xsd", "name"));
        while (iterator.hasNext()) {
            OMElement element = (OMElement) iterator.next();
            System.out.println("The response is received : " + element.getText());
            assertEquals("MSFT Company", element.getText());
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    /*
    Method to logout from the Management Console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
