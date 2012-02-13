package org.wso2.carbon.web.test.esb;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;


public class ESBSendMediatorMainTest extends CommonSetup{
    public ESBSendMediatorMainTest(String text) {
        super(text);
    }

    public void testAddSendMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);

        esbCommon.logoutLogin();
        //verify Adding a Send mediator to the 'Root' level
        esbCommon.addSequence("Test_Send_Mediator");
        esbSendMediatorTest.addNormalSendMediator("0");

        selenium.click("//a[@id='mediator-0']");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Send"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Send mediator
        assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
        assertEquals("Select Endpoint Type", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr/td/label"));
		assertEquals("None", selenium.getText("//table[@id='epOptionTable']/tbody/tr[1]/td"));
		assertEquals("Anonymous", selenium.getText("//table[@id='epOptionTable']/tbody/tr[2]/td[1]"));
		assertEquals("Pick From Registry", selenium.getText("//table[@id='epOptionTable']/tbody/tr[3]/td[1]"));

        //verify Click on 'Add Sibling' of the Send mediator
        selenium.click("//a[@id='mediator-0']");
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Send");
    }

    public void testDeleteSendMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_send_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbCommon.mediatorUpdate();

        esbCommon.addMediators("Add Sibling","0","Core","Drop");
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_send_mediator");
        assertEquals("RootAddChildSendDrop", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));
        //Click on the 'Delete' icon of the 'Send mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        assertTrue(esbCommon.searchSequenceInList("test_send_mediator"));

        //verify Send mediator has successfully deleted from the sequence tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_send_mediator");
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Send"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Drop"));

        //verify sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_send_mediator\"> <syn:drop /> </syn:sequence>", selenium.getText("sequence_source"));

        //verify in manage synapse
        esbCommon.checkSequenceInfoInSynapse("test_send_mediator");

        //delete the sequence from sequnce list
        esbCommon.deleteSequence("test_send_mediator");
    }

    public void testSelectEndPointAnonymous() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_Send_Mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Send");

        //verify Select the endpoint type 'Anonymous'
        selenium.click("epOpAnon");
        assertEquals("Add", selenium.getText("//table[@id='epOptionTable']/tbody/tr[2]/td[2]"));

        //Click on the 'Add' link
        selenium.click("epAnonAdd");
		selenium.waitForPageToLoad("30000");
        assertEquals("Manage Endpoints", selenium.getText("//div[@id='middle']/h2"));
        assertEquals("Add Address Endpoint Add WSDL Endpoint Add Failover Group Add Load-balance Group", selenium.getText("workArea"));

        //Click on the 'Back' button of the 'Manage Endpoints' page
        selenium.click("cancelBtn");
        selenium.waitForPageToLoad("30000");
        assertEquals("Design Sequence", selenium.getText("//div[@id='middle']/h2"));
    }

    //verify Selecting the endpoint type as 'None' and update the mediator
    public void testAddNormalSendMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);

        esbCommon.addSequence("test_epNone");
        esbSendMediatorTest.addNormalSendMediator("0");
        esbCommon.mediatorUpdate();

        //verify sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_epNone\">\n   <syn:send />\n</syn:sequence>", selenium.getValue("sequence_source"));
        esbCommon.clickDesignView();

        //verify mediator soure view
        esbCommon.clickMediatorSource("0");
		assertEquals("<syn:send xmlns:syn=\"http://ws.apache.org/ns/synapse\" />", selenium.getText("mediatorSrc"));
		selenium.click("link=switch to design view");
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(1000);
        assertEquals("on", selenium.getValue("epOpNone"));
    }

    //verify Selecting the endpoint type as 'Anonymous', specify an endpoint and update the mediator
    public void testAddAnonymousWSDLepointSendMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);

        esbCommon.addSequence("test_epAnonymous");
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addWsdlEprAnonSendMediator("0");
        esbSendMediatorTest.addUriMandInfoSendMediator(null,"wsdl_url","wsdl_service","wsdl_port");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //verify sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_epAnonymous\"> <syn:send /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

        //verify mediator source
        esbCommon.clickMediatorSource("0");
        selenium.click("link=switch to design view");
        /*
        bug--CARBON-5213
         */
        //verify switch back to the design view
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.click("epAnonEdit");
        selenium.waitForPageToLoad("30000");
        assertEquals("wsdl_url", selenium.getValue("uriWSDLVal"));
		assertEquals("wsdl_service", selenium.getValue("wsdlendpointService"));
		assertEquals("wsdl_port", selenium.getValue("wsdlendpointPort"));

        selenium.click("cancel");
        selenium.waitForPageToLoad("30000");
    }

    public void testAddAnonFailoverGroupSendMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);

        esbCommon.addSequence("test_epAnonymous_failover");
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.clickAddAnonSendMediator("0","failover");
        ESBLoadbalanceFailoverTest esbLoadbalanceFailoverTest=new ESBLoadbalanceFailoverTest(selenium);
        //Adding the first address endpoint to FailoverGroup
        esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
        esbLoadbalanceFailoverTest.clickAddressEndpoint("0.1");
        esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.1","http://localhost:9001/services/LBService1");
        esbLoadbalanceFailoverTest.saveAddressEp("0.1");
        //Adding the second address endpoint to FailoverGroup
        esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
        esbLoadbalanceFailoverTest.clickAddressEndpoint("0.2");
        esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.2","http://localhost:9002/services/LBService1");
        esbLoadbalanceFailoverTest.saveAddressEp("0.2");
        //save the failover group
        esbLoadbalanceFailoverTest.saveGroup();
        //save the sequence
        Thread.sleep(1000);
        esbCommon.sequenceSave();
        
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_epAnonymous_failover");
        Thread.sleep(2000);
        selenium.click("//a[@id='mediator-0']");
        selenium.click("epAnonEdit");
        selenium.waitForPageToLoad("30000");
        assertEquals("rootAddEndpointAddressDeleteAddressDelete", selenium.getText("failoverTree").replaceAll("\n","").replaceAll(" ",""));

        //verify source view
        selenium.click("link=Switch to source view");
        selenium.waitForPageToLoad("30000");        
        assertEquals("<syn:endpoint xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"anonymous\"> <syn:failover> <syn:endpoint> <syn:address uri=\"http://localhost:9001/services/LBService1\"/> </syn:endpoint> <syn:endpoint> <syn:address uri=\"http://localhost:9002/services/LBService1\"/> </syn:endpoint> </syn:failover> </syn:endpoint>", selenium.getText("xmlPay"));        
        selenium.click("link=Switch to design view");
        selenium.waitForPageToLoad("30000");        
    }

    public void testAddRegistrySendMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        addRegistrySendMediator("test_epRegistry","0","Sequence","fault");
    }

    /*
    Create a complete sequence with a Send mediator with the endpoint type set to 'Pick From Registry' and save the sequence
     */
    //This scenario verifies through the "ESBEndPointsInRegistryTest"


    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }

    //verify Selecting the endpoint type as 'Pick From Registry' and update the mediator
    public void addRegistrySendMediator(String seqName,String level,String resourceType,String resourceName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);

        esbCommon.addSequence(seqName);
        esbSendMediatorTest.addRegistrySendMediator(level,resourceType,resourceName);
        esbCommon.mediatorUpdate();

        //verify sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\">\n   <syn:send>\n      <syn:endpoint key=\""+resourceName+"\" />\n   </syn:send>\n</syn:sequence>", selenium.getValue("sequence_source"));
        esbCommon.clickDesignView();

        //verify mediator soure view
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:send xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:endpoint key=\""+resourceName+"\" /> </syn:send>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //verify switch to design view
        esbCommon.clickMediatorSource("0");
        Thread.sleep(2000);
        assertEquals(resourceName, selenium.getValue("registryKey"));
    }
}
