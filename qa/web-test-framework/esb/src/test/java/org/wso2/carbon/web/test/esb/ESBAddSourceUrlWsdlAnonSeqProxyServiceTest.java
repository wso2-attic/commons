package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.Properties;
import java.util.Iterator;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

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

public class ESBAddSourceUrlWsdlAnonSeqProxyServiceTest extends CommonSetup {
    Properties properties = new Properties();

    public ESBAddSourceUrlWsdlAnonSeqProxyServiceTest(String text) {
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
    Method to set Proxy Service General settings
     */
    public void testSetGeneralSettings() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("proxy_sourceurl_wsdl_anon_seq", "Custom Proxy");
        esbAddProxyServiceTest.addSourceUrlWsdl("file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        System.out.println("Setting general settings");        
    }

    /*
    Method to set Proxy Service Transport settings
     */
    public void testSetTransportSettings() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.addParameters("param1", "value1");
        esbAddProxyServiceTest.addParameters("param2", "value2");
        esbAddProxyServiceTest.clickNext();
        System.out.println("Setting transport settings");
    }

    /*
   This method will test setting up of the In Sequence of the Proxy Service wizard
    */
    public void testSetInSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest.addLogMediator("0","Full");
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
        esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
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
   This method will test setting up of the Out Sequence of the Proxy Service wizard
    */
    public void testSetFaultSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpAnon", null,"Add Child","Core","Drop");
        esbCommon.sequenceSave();
        System.out.println("The Fault sequence was set successfully");
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
        esbAddProxyServiceTest.verifyProxy("proxy_sourceurl_wsdl_anon_seq");
    }

    /*
   This method will save the Synapse configuration after the Proxy is created
    */
    public void testSaveSynapseConfig() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();
        System.out.println("The Synapse configuration was saved successfully after the Proxy was created!!!");
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void testInvokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        OMElement stockQuoteResponse = null;
        ESBSampleClient sampleClient = new ESBSampleClient();

        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));

        if (esbCommon.getContextRoot().equals(null))
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_sourceurl_wsdl_anon_seq", null, "IBM");
        }
        else
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/proxy_sourceurl_wsdl_anon_seq", null, "IBM");
        }

        Iterator iterator =   stockQuoteResponse.getFirstElement().getChildrenWithName(new QName("http://services.samples/xsd", "name"));
        while (iterator.hasNext()) {
            OMElement element = (OMElement) iterator.next();
            System.out.println("The response is received : " + element.getText());
            assertEquals("IBM Company", element.getText());
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    /*
    Method to delete the Proxy Service
     */
    public void testDeleteProxyService()throws Exception{
		ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.deleteProxyService("proxy_sourceurl_wsdl_anon_seq");
    }

    /*
    Verifying whether the Proxy Service is deleted successfully
    */
    public void testVerifyDeletedProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.verifyProxyDelete("proxy_sourceurl_wsdl_anon_seq");
    }

    /*
    Method to logout from the Management Console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
