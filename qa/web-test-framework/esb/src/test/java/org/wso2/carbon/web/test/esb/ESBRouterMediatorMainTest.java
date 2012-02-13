package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class ESBRouterMediatorMainTest  extends CommonSetup{

    public ESBRouterMediatorMainTest(String text) {
        super(text);
    }

    public void testAddRouterMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_router_mediator");
        //verify adding a Router mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Router"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify click on the Router mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
		assertEquals("Router Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
		selenium.getSelectedValue("mediator.router.continue").equals("No");
        assertEquals("Add Route", selenium.getText("link=Add Route"));

        //verify Click on 'Add Sibling' of the Router mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Router");

        //verify Click on the 'Delete' icon of the 'Router mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Router"));
    }

    public void testAddSiblingToRouter() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_router_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");

        //Add a Sibling to the Router mediator
        esbCommon.addMediators("Add Sibling","0","Filter","Out");
        assertEquals("Router", selenium.getText("//a[@id='mediator-0']"));
		assertEquals("Out", selenium.getText("//a[@id='mediator-1']"));
        selenium.click("//a[@id='mediator-0']");
        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertTrue(selenium.isTextPresent("<syn:router xmlns:syn=\"http://ws.apache.org/ns/synapse\" />"));
        selenium.click("link=switch to design view");
        //verify Sequence source
        esbCommon.clickSequenceSource("0");
        assertTrue(selenium.isTextPresent("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_router_mediator\"> <syn:router /> <syn:out /> </syn:sequence>"));
        esbCommon.clickDesignView();
    }

    public void testContinueAfterRouting() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_router_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");

        //Select the 'Continue after routing' to 'Yes' and view the mediator source
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(1000);
        selenium.select("mediator.router.continue", "label=Yes");
        Thread.sleep(1000);
        esbCommon.mediatorUpdate();
        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertTrue(selenium.isTextPresent("<syn:router xmlns:syn=\"http://ws.apache.org/ns/synapse\" continueAfter=\"true\" />"));
        selenium.click("link=switch to design view");

        //Select the 'Continue after routing' to 'No' and view the mediator source
        Thread.sleep(2000);
        selenium.select("mediator.router.continue", "label=No");
        Thread.sleep(1000);
        esbCommon.mediatorUpdate();
        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:router xmlns:syn=\"http://ws.apache.org/ns/synapse\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
    }

    public void testAddRoute() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_router_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");

        //verify click on the 'Add Route' link
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.click("link=Add Route");
        selenium.waitForPageToLoad("30000");
        //Thread.sleep(4000);
        //assertEquals("RouterAdd Sibling\n \nDelete\n \n\n\n\n\n\nRoute\n\n\nTarget", selenium.getText("//ul[@id='sequenceTree']/li/ul/li[1]"));
        //assertEquals("1", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]"));

        //verify Click on the 'Router' node
        selenium.click("link=Route");
        Thread.sleep(2000);
		assertEquals("Route Configuration", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
		selenium.getSelectedValue("mediator.route.break").equals("No");
        assertTrue(selenium.isTextPresent("exact:Route Expression* Namespaces"));
        assertEquals("Route Pattern", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[3]/td[1]"));

        //Click on the 'Help' link of the 'Route' node
        //esbCommon.mediatorHelp("Route");

        //verify Click on the 'Target' link
        selenium.click("link=Target");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Target Configuration"));
        assertEquals("on", selenium.getValue("mediator.target.seq.radio.none"));
        assertEquals("on", selenium.getValue("epOpNone"));
    }

    public void testRouterMediatorInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBRouterMediatorTest esbRouterMediatorTest=new ESBRouterMediatorTest(selenium);
        esbCommon.addSequence("test_router_mediator");

        //Adding the Router mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");
        esbRouterMediatorTest.clickAddRoute();
        esbRouterMediatorTest.addRouterMediator("0","Yes");
        esbCommon.mediatorUpdate();

        //Adding Route information
        esbRouterMediatorTest.addRoute("0","No", "//m0:getQuote/m0:request/m0:symbol" );
        esbRouterMediatorTest.addRoutePattern("MSFT");
        esbRouterMediatorTest.addRouterNamespace("ns","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("ns2","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        //Adding target information
        esbRouterMediatorTest.addTarget("0.0.0");
        esbRouterMediatorTest.addTargetAnonEndpoint("http://localhost:9000/services/SimpleStockQuoteService");
        Thread.sleep(2000);
        esbRouterMediatorTest.addTargetSoapAction("urn:getQuote");
        esbRouterMediatorTest.addTargetToAddress("http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        
        //verify sequence source
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_router_mediator");
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_router_mediator\"> <syn:router continueAfter=\"true\"> <syn:route xmlns:ns2=\"http://org.apache.synapse/xsd\" xmlns:ns=\"http://org.apache.synapse/xsd\" xmlns:m0=\"http://services.samples\" expression=\"//m0:getQuote/m0:request/m0:symbol\" match=\"MSFT\" breakRouter=\"false\"> <syn:target to=\"http://localhost:9000/services/SimpleStockQuoteService\" soapAction=\"urn:getQuote\"> <syn:endpoint> <syn:address uri=\"http://localhost:9000/services/SimpleStockQuoteService\" /> </syn:endpoint> </syn:target> </syn:route> </syn:router> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

//        //verify in manage synapse
//        esbCommon.clickSequenceSource("0");
//        String seq_source = selenium.getText("sequence_source");
//        String temp1=seq_source.substring(seq_source.indexOf("name"));
//        String temp2="<syn:sequence "+temp1;
//        temp2=temp2.replaceAll(" />","/>");
//        esbCommon.clickSynapse();
//        boolean status=esbCommon.verifyManageSynapseConfig(temp2);
//        if(!status)
//           throw new MyCheckedException("Sequence information incorrect in the  ManageSynapseConfig...!");
//        else
//            System.out.println("sequence \"test_router_mediator\" not found..!");
}

    public void testCreateSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Create a complete sequence with a Route mediator with 'Continue after routing set to 'Yes'
        ESBSampleSeqForRouterMediator esbSampleSeqForRouterMediator=new ESBSampleSeqForRouterMediator("");
        esbSampleSeqForRouterMediator.createSequence("router_sequence");
        assertTrue(selenium.isTextPresent("router_sequence"));
    }

    public void testEditSequenceContinueAfterRouting() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(selenium.isTextPresent("router_sequence")){
            //Create a complete sequence with a Route mediator with 'Continue after routing set to 'No'
            ESBRouterMediatorTest esbRouterMediatorTest=new ESBRouterMediatorTest(selenium);
            esbCommon.editSeqMediator("router_sequence","In","Router");
            esbRouterMediatorTest.addRouterMediator("0.1","No");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            assertTrue(selenium.isTextPresent("router_sequence"));

        }
        else
            System.out.println("sequence \"router_sequence\" not found..!");
    }

    public void testEditsequenceBreakAfterRoute() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(selenium.isTextPresent("router_sequence")){
            esbCommon.clickEditSeq("router_sequence");
            selenium.click("//a[@id='mediator-0.1.0']");
            selenium.select("mediator.route.break", "label=Yes");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            assertTrue(selenium.isTextPresent("router_sequence"));
        }
        else
            System.out.println("sequence \"router_sequence\" not found..!");
    }

    public void testUpdateSynpse() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}


