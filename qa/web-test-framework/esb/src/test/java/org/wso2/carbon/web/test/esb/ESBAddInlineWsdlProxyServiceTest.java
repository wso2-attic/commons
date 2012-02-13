package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.commons.io.FileUtils;
import org.apache.axiom.om.OMElement;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import javax.xml.namespace.QName;

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

public class ESBAddInlineWsdlProxyServiceTest  extends CommonSetup{

    public ESBAddInlineWsdlProxyServiceTest(String text) {
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
    Method to create the in sequence
     */
    public void testCreateInSeq() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding a sequence which will be refered through the wizard
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
        boolean insequence = selenium.isTextPresent("in_seq");
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
        System.out.println("The Sequence used for In Sequence was created successfully");
    }


    /*
    Method to add the out sequence
     */
    public void testCreateOutSeq() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding a sequence which will be refered through the wizard
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
        boolean outsequence = selenium.isTextPresent("out_seq");
        if (outsequence) {
            //Do nothing
        } else {
            esbCommon.addSequence("out_seq");
            ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
            esbSendMediatorTest.addNormalSendMediator("0");
            esbCommon.sequenceSave();
        }
        System.out.println("The Sequence used for Out Sequence was created successfully");
    }

    /*
    Method to create an endpoint
     */
    public void testCreateEndpoint() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding an endpoint which will be refered through the proxy wizard
        esbCommon = new ESBCommon(selenium);
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("epr1");

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("epr1",esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
        System.out.println("The Endpoint used for Proxy endpoint was created successfully");
    }

    /*
    Method to set Proxy Service General settings
     */
    public void testSetGeneralSettings() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();         
        String content = null;
        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "SimpleStockQuoteProxy.wsdl");
            content = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("proxy_inline_wsdl", "Custom Proxy");
        esbAddProxyServiceTest.addInlineWsdl(content);
        esbAddProxyServiceTest.specifyStartOnload("true");
    }

    /*
    Method to set Proxy Service Transport settings
     */
    public void testSetTransportSettings() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();
    }

    /*
   This method will test setting up of the In Sequence of the Proxy Service wizard
    */
    public void testSetInSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpImp", "in_seq");
        System.out.println("The In Sequence was selected successfully");
    }

    /*
   This method will test setting up of the Endpoint of the Proxy Service wizard
    */
    public void testSetEndpoint() throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpImp", "epr1");
        esbAddProxyServiceTest.clickNext();
        System.out.println("The Endpoint was selected successfully");
    }

    /*
   This method will test setting up of the Out Sequence of the Proxy Service wizard
    */
    public void testSetOutSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpImp", "out_seq");
        System.out.println("The Out Sequence was selected successfully");
    }

    /*
   This method will test setting up of the Out Sequence of the Proxy Service wizard
    */
    public void testSetFaultSequence() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpImp", "fault",null,null,null);
        System.out.println("The Fault Sequence was selected successfully");        
    }

    /*
   This method will test saving the proxy Service
    */
    public void testSaveProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.saveProxyService();
        System.out.println("The Proxy service was created successfully");        
    }

    /*
   This method will verify the proxy Service
    */
    public void testVerifyProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.verifyProxy("proxy_inline_wsdl");
        System.out.println("The Proxy service exists in the Services Listing page");
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
    This method will invoke the client
     */
    public void testInvokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        OMElement stockQuoteResponse = null;

        ESBSampleClient sampleClient = new ESBSampleClient();

        if (esbCommon.getContextRoot().equals(null))
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_inline_wsdl", null, "IBM");
        }
        else
        {
            stockQuoteResponse = sampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/proxy_inline_wsdl", null, "IBM");
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
        esbAddProxyServiceTest.deleteProxyService("proxy_inline_wsdl");
        System.out.println("The Proxy Service was deleted successfully");
    }

    /*
    Verifying whether the Proxy Service is deleted successfully
    */
    public void testVerifyDeletedProxy() throws Exception{
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.verifyProxyDelete("proxy_inline_wsdl");
        System.out.println("The Proxy Service has beeen deleted");
    }

    /*
    Method to logout from the Management Console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
