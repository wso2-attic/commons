package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Iterator;

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

public class ESBAddRegistrySourceWsdlProxyServiceTest extends CommonSetup {
    Properties properties = new Properties();

    public ESBAddRegistrySourceWsdlProxyServiceTest(String text) {
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
    Method to create in sequence
     */
    public void testCreateInSeq() throws Exception{
        //Adding a sequence which will be refered through the wizard
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
        boolean insequence = selenium.isTextPresent("in_seq");
        System.out.println(insequence);
        if (insequence) {
            //Do nothing
        } else {
            esbCommon.addSequence("in_seq");
            esbCommon.addRootLevelChildren("Add Child","Core","Log");
            ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
            esbLogMediatorTest.addLogMediator("0","Full");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
        }
    }

    /*
    Method to create out sequence
     */
    public void testCreateOutSeq() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding a sequence which will be refered through the wizard
        esbCommon = new ESBCommon(selenium);
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
        boolean outsequence = selenium.isTextPresent("out_seq");
        System.out.println(outsequence);
        if (outsequence) {
            //Do nothing
        } else {
            esbCommon.addSequence("out_seq");
            ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
            esbSendMediatorTest.addNormalSendMediator("0");
            esbCommon.sequenceSave();
        }
    }

    /*
    Method to create endpoint
     */
    public void testCreateEndpoint() throws Exception{
        //Adding an endpoint which will be refered through the proxy wizard
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("epr1");
        System.out.println(endpoint);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("epr1",esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }

    /*
    Method to add local entry
     */
    public void testAddLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Creating a local entry to store the WSDL file
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("wsdl_file","file:"+ esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
    }

    /*
    Method to set Proxy Service General settings
     */
    public void testSetGeneralSettings() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("proxy_registry_wsdl", "Custom Proxy");
        esbAddProxyServiceTest.addRegistryLookupWsdl("wsdl_file");
        esbAddProxyServiceTest.specifyStartOnload("true");
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
    }

    /*
   This method will test setting up of the In Sequence of the Proxy Service wizard
    */
    public void testSetInSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpImp", "in_seq");
    }

    /*
   This method will test setting up of the Endpoint of the Proxy Service wizard
    */
    public void testSetEndpoint() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpImp", "epr1");
        esbAddProxyServiceTest.clickNext();
    }

    /*
   This method will test setting up of the Out Sequence of the Proxy Service wizard
    */
    public void testSetOutSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, outSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpImp", "out_seq");
    }

    /*
   This method will test setting up of the Out Sequence of the Proxy Service wizard
    */
    public void testSetFaultSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpImp", "fault",null,null,null);
    }

    /*
   This method will test saving the proxy Service
    */
    public void testSaveProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.saveProxyService();
    }

    /*
   This method will verify the proxy Service
    */
    public void testVerifyProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.verifyProxy("proxy_registry_wsdl");
    }

    /*
   This method will save the Synapse configuration after the Proxy is created
    */
    public void testSaveSynapseConfig() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();
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
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_registry_wsdl", null, "IBM");
        }
        else
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/proxy_registry_wsdl", null, "IBM");
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
        esbAddProxyServiceTest.deleteProxyService("proxy_registry_wsdl");
    }

    /*
    Verify whether the Proxy Service is deleted successfully
     */
    public void testVerifyProxyDeleted() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.verifyProxyDelete("proxy_registry_wsdl");
    }

    /*
    Method to logout from the Management Console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
