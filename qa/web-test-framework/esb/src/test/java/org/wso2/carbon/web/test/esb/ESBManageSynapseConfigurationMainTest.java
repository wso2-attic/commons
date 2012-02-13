package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

import java.util.Properties;

import java.io.*;

import junit.framework.Test;
import junit.framework.TestSuite;

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

public class ESBManageSynapseConfigurationMainTest   extends CommonSetup{

    public ESBManageSynapseConfigurationMainTest(String text) {
        super(text);
    }

    /*
    Method to login to the system
     */
    public void testLogin() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");
        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");
    }
    
    //Click on synapse link & verify it
    public void testVerifySynapseConfig1() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);

        serviceManagement.Login();
        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        esbManageSynapseConfigurationTest.clickSave();
        assertTrue(selenium.isTextPresent("Manage Synapse Configuration"));
        assertTrue(selenium.isTextPresent("'Update' the configuration if any changes have been made manually. Then 'Save' to commit the current operational configuration to disk."));
        //verify the 'Manage Synapse Configuration' in a fresh ESB
        String defaultSyn=esbManageSynapseConfigurationTest.readSynapseFile();
        assertTrue(selenium.isTextPresent(defaultSyn));
   }

   //verify the click on Reset
    public void testVerifyReset() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\nTestReset\n        <syn:in>\nTestRest....\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n</syn:definitions>");
        esbManageSynapseConfigurationTest.clickReset();
        //verify the 'Manage Synapse Configuration'
        String defaultSyn=esbManageSynapseConfigurationTest.readSynapseFile();
        assertTrue(selenium.isTextPresent(defaultSyn));
    }

   /*
   Method to verify addition of synapse config to the Manage Synapse Configuration
    */
   public void testPutEprConfigToSynapse() throws Exception{
        putSampleToSynapse("<syn:endpoint name=\"epr3\">\n<syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\"/>\n</syn:endpoint>");
   }

    //verify the click on Update
   public void testClickUpdate() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.clickUpdate();
   }

    //restart the server
//   public void restartServer() throws Exception{
//       selenium.click("link=Shutdown/Restart");
//       selenium.waitForPageToLoad("30000");
//       selenium.click("link=Graceful Restart");
//       selenium.click("//button[@type='button']");
//       Thread.sleep(2000);
//       selenium.click("//button[@type='button']");
//       Thread.sleep(60000);
//   }

   //verify click on save button
   public void testClickSave() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.clickSave();
   }

   /*
   Method to verify the Management Console
    */
//   public void testVerifyManagementConsole(String manageType,String name){
//        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
////        esbManageSynapseConfigurationTest.verifyManagementConsole();
//   }

    //configuration of a task and click on 'Update'
    public void testAddScheduledTask() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        putSampleToSynapse("<syn:task class=\"org.apache.synapse.startup.tasks.MessageInjector\" name=\"CheckPrice\">\n<syn:property name=\"to\" value=\"http://localhost:9000/services/SimpleStockQuoteService\"/>\n<syn:property name=\"soapAction\" value=\"urn:getQuote\"/>\n<syn:property name=\"message\">\n<m0:getQuote xmlns:m0=\"http://services.samples\">\n<m0:request>\n<m0:symbol>IBM</m0:symbol>\n</m0:request>\n</m0:getQuote>\n</syn:property>\n<syn:trigger interval=\"5\"/>\n </syn:task>\n");
        esbManageSynapseConfigurationTest.clickUpdate();
        esbManageSynapseConfigurationTest.verifyManagementConsole("Scheduled Tasks","CheckPrice");
   }

    //configuration of a proxi services and click on 'Update'
    public void testAddProxyService() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        putSampleToSynapse("<syn:proxy name=\"StockQuoteProxy\">\n<syn:target>\n<syn:endpoint>\n<syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\"/>\n</syn:endpoint>\n<syn:outSequence>\n<syn:send/>\n </syn:outSequence>\n</syn:target>\n<syn:publishWSDL uri=\"file:repository/samples/resources/proxy/sample_proxy_1.wsdl\"/>\n</syn:proxy>\n");
        esbManageSynapseConfigurationTest.clickUpdate();
        esbManageSynapseConfigurationTest.verifyManagementConsole("List","StockQuoteProxy");
    }

   //configuration of a sequence and click on 'update'
    public void testAddSequence() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        //putSampleToSynaps("<syn:sequence name=\"test_seq\">\n<syn:log level=\"full\"/>\n<syn:send/>\n<syn:in>\n<syn:out>\n</syn:log level=\"full\"/>\n</syn:send>\n</syn:out>\n</syn:sequence>");
        putSampleToSynapse("<syn:sequence name=\"test_seq\">\n<syn:in>\n<syn:log level=\"full\"/>\n<syn:send/>\n</syn:in>\n<syn:out>\n<syn:log level=\"full\"/>\n<syn:send/>\n</syn:out>\n</syn:sequence>\n");
        esbManageSynapseConfigurationTest.clickUpdate();
        esbManageSynapseConfigurationTest.verifyManagementConsole("Sequences","test_seq");
    }

    //configuration of a end point and click on 'update'
     public void testAddEndPoint() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        putSampleToSynapse("<syn:endpoint name=\"epr2\">\n<syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\"/>\n</syn:endpoint>");
        esbManageSynapseConfigurationTest.clickUpdate();
        esbManageSynapseConfigurationTest.verifyManagementConsole("Endpoints","epr2");
     }

    //configuration of a local entry and click on 'update'
    public void testAddLocalEntry() throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        putSampleToSynapse("<syn:localEntry key=\"version\">0.1</syn:localEntry>");
        esbManageSynapseConfigurationTest.clickUpdate();
        esbManageSynapseConfigurationTest.verifyManagementConsole("Local Entries","version");
    }

    //Change the configuration of a Proxy Service
    public void testChangeProxyConfig(String proxyName,String action) throws Exception{
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
         ServiceManagement serviceManagement = new ServiceManagement(selenium);
         serviceManagement.Login();
        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        if(action.equalsIgnoreCase("statistics"))
            selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    <syn:proxy name=\"StockQuoteProxy\" startOnLoad=\"true\" statistics=\"enable\">\n        <syn:target>\n            <syn:endpoint>\n                <syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\"/>\n            </syn:endpoint>\n            <syn:outSequence>\n                <syn:send/>\n            </syn:outSequence>\n        </syn:target>\n        <syn:publishWSDL uri=\"file:repository/samples/resources/proxy/sample_proxy_1.wsdl\"/>\n    </syn:proxy>\n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n</syn:definitions>");
        else if(action.equalsIgnoreCase("trace"))
            selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    <syn:proxy name=\"StockQuoteProxy\" startOnLoad=\"true\" trace=\"enable\">\n        <syn:target>\n            <syn:endpoint>\n                <syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\"/>\n            </syn:endpoint>\n            <syn:outSequence>\n                <syn:send/>\n            </syn:outSequence>\n        </syn:target>\n        <syn:publishWSDL uri=\"file:repository/samples/resources/proxy/sample_proxy_1.wsdl\"/>\n    </syn:proxy>\n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n</syn:definitions>");
        esbManageSynapseConfigurationTest.clickUpdate();

        //verify  the service dashboard of the Proxy Service
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        String readService = "";
        int row_no = 1;

        while (!proxyName.equals(readService)) {
            readService = selenium.getText("//table[@id='sgTable']/tbody/tr[" + Integer.toString(row_no) + "]/td[3]/a");
            row_no = row_no+1;
        }
        row_no = row_no - 1;

        selenium.click("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]/a");
        selenium.waitForPageToLoad("30000");
         if(action.equalsIgnoreCase("statistics"))
            assertTrue(selenium.isElementPresent("link=Disable Statistics"));
        else if(action.equalsIgnoreCase("trace"))
            assertTrue(selenium.isElementPresent("link=Disable Statistics"));
    }

      //Change the configuration of a Sequence
    public void testChangeSeqConfig(String seqName,String action) throws Exception{
         ServiceManagement serviceManagement = new ServiceManagement(selenium);
         serviceManagement.Login();
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        if(action.equalsIgnoreCase("statistics"))
            selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n    <syn:sequence name=\"test_seq\" statistics=\"enable\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:send/>\n        </syn:in>\n        <syn:out>\n            <syn:log level=\"full\"/>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n</syn:definitions>");
        else if(action.equalsIgnoreCase("trace"))
            selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n    <syn:sequence name=\"test_seq\" trace=\"enable\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:send/>\n        </syn:in>\n        <syn:out>\n            <syn:log level=\"full\"/>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n</syn:definitions>");
        esbManageSynapseConfigurationTest.clickUpdate();

        selenium.click("link=Sequences");
        selenium.waitForPageToLoad("30000");
        if(action.equalsIgnoreCase("statistics"))
            assertTrue(selenium.isElementPresent("//a[@onclick=\"disableStat('test_seq')\"]"));
        else if(action.equalsIgnoreCase("trace"))
            assertTrue(selenium.isElementPresent("//a[@onclick=\"disableTracing('test_seq')\"]"));
    }

     //Change the configuration of a Endpoints
    public void testChangeEndPointConfig(String seqName,String action) throws Exception{
         ServiceManagement serviceManagement = new ServiceManagement(selenium);
         serviceManagement.Login();
       ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        if(action.equalsIgnoreCase("statistics"))
            selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    <syn:endpoint name=\"epr2\">\n        <syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\" statistics=\"enable\"/>\n    </syn:endpoint>\n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n</syn:definitions>");

        esbManageSynapseConfigurationTest.clickUpdate();

        selenium.click("link=Endpoints");
        selenium.waitForPageToLoad("30000");

       String readService = "";
        int row_no = 1;

        while (!seqName.equals(readService)) {
            readService=selenium.getText("//table[@id='endpointListTable']/tbody/tr["+Integer.toString(row_no)+"]/td[1]");
            row_no = row_no+1;
        }
        row_no = row_no - 1;

        selenium.click("link=Endpoints");
        selenium.waitForPageToLoad("30000");
        assertEquals("Disable Statistics Enable Statistics", selenium.getText("//table[@id='endpointListTable']/tbody/tr["+row_no+"]/td[3]"));
   }

    /*
    This mthod will be used to log out from the management console
    */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }

    /*
    Method to verify addition of synapse config to the Manage Synapse Configuration
     */
    public void putSampleToSynapse(String sampleSynapse) throws Exception{
         ServiceManagement serviceManagement = new ServiceManagement(selenium);
         serviceManagement.Login();
        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        selenium.type("rawConfig", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<syn:definitions xmlns:syn=\"http://ws.apache.org/ns/synapse\">\n    <syn:registry provider=\"org.wso2.carbon.mediation.registry.WSO2Registry\">\n        <syn:parameter name=\"cachableDuration\">15000</syn:parameter>\n    </syn:registry>\n    \n    <syn:sequence name=\"fault\">\n        <syn:log/>\n    </syn:sequence>\n    <syn:sequence name=\"main\">\n        <syn:in>\n            <syn:log level=\"full\"/>\n            <syn:filter source=\"get-property('To')\" regex=\"http://localhost:9000.*\">\n                <syn:send/>\n            </syn:filter>\n        </syn:in>\n        <syn:out>\n            <syn:send/>\n        </syn:out>\n    </syn:sequence>\n"+sampleSynapse+"\n</syn:definitions>");

    }
 }