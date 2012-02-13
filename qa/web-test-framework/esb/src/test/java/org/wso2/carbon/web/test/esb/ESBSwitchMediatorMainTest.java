package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;
import org.wso2.carbon.web.test.client.ESBSampleClient;
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

public class ESBSwitchMediatorMainTest  extends CommonSetup{

    public ESBSwitchMediatorMainTest(String text) {
        super(text);
    }

    public void testAddSwithMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_switch_mediator");
        //Add a Switch mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Switch"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Switch mediator
        selenium.click("//a[@id='mediator-0']");
        assertTrue(selenium.isTextPresent("exact:Source XPath * Namespaces Number of cases 0 cases Add case Specify default case"));

        //verify Click on 'Add Sibling' of the Switch mediator
        esbCommon.verifyClickAddSibling();

//        //verify Click on the 'Help' link of the mediator
//        selenium.click("//a[@id='mediator-0']");
//        //esbCommon.mediatorHelp("Switch");

        //verify Click on the 'Delete' icon of the 'Switch mediator'
        esbCommon.delMediator("0");
        assertEquals("Root Add Child", selenium.getText("treePane"));
    }

    //This method will fail in version 2.1.2(BUG)
    public void testDeleteSwitchMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_switch_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");
        esbSwitchMediatorTest.addSwitchMediator("0","//m0:getQuote/m0:request/m0:symbol");
        esbSwitchMediatorTest.addSwitchNamespace("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();

        esbCommon.addMediators("Add Sibling","0","Core","Drop");
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_switch_mediator");
        //Click on the 'Delete' icon of the 'Switch mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        assertTrue(esbCommon.searchSequenceInList("test_switch_mediator"));

        //verify Switch mediator has successfully deleted from the sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_switch_mediator");
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Switch"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Drop"));

        //delete the sequnce from sequnce list
        esbCommon.deleteSequence("test_switch_mediator");
    }


    public void testSpecifySourceXpath() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_switch_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");

        //Specify the source Xpath, add a namespace and view the source view of the mediator and the sequence
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        esbSwitchMediatorTest.addSwitchMediator("0","//m0:getQuote/m0:request/m0:symbol");
        esbSwitchMediatorTest.addSwitchNamespace("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:switch xmlns:syn=\"http://ws.apache.org/ns/synapse\" xmlns:m0=\"http://services.samples/xsd\" source=\"//m0:getQuote/m0:request/m0:symbol\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

    }

    public void testAddCase() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_switch_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");

        //Click on the 'Add Case' link without specifying a value for the 'Source Xpath' field
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        esbSwitchMediatorTest.addSwitchMediator("0","");
        selenium.click("link=Add case");
		assertEquals("You must specify a Source XPath", selenium.getText("messagebox-error"));
		selenium.click("//button[@type='button']");

        //Click on the link 'Add Case'
        Thread.sleep(2000);
        esbSwitchMediatorTest.addSwitchMediator("0","//m0:getQuote/m0:request/m0:symbol");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCase("0","IBM","0.0");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("1 case"));
        selenium.click("link=Case");
        assertTrue(selenium.getText("//*[@id='mediator-0.0']").contains("Case"));
        assertTrue(selenium.getText("//*[@id='mediator-0.0']").contains("Add Child"));
        assertTrue(!selenium.getText("//*[@id='mediator-0.0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0.0']").contains("Delete"));

        //Click on the 'Help' link of the 'Switch-Case Mediator' mediator
        //esbCommon.mediatorHelp("Switch Case");
    }

    //Specify a value for the field 'Case Value', update the mediator and view the source view of the Switch-Case mediator
    public void testSwitchCaseMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_switch_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");

         //Specify a value for the fields & update the mediator
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        esbSwitchMediatorTest.addSwitchMediator("0","//m0:getQuote/m0:request/m0:symbol");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCase("0","IBM","0.0");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");

        //view the source view of the Switch-Case mediator
        selenium.click("link=Case");
        esbCommon.clickMediatorSource("0.0");
        assertEquals("<syn:case xmlns:syn=\"http://ws.apache.org/ns/synapse\" regex=\"IBM\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //view the source view of the Switch mediator
        Thread.sleep(2000);
        esbCommon.clickMediatorSource("0");
        Thread.sleep(2000);
        assertEquals("<syn:switch xmlns:syn=\"http://ws.apache.org/ns/synapse\" source=\"//m0:getQuote/m0:request/m0:symbol\"> <syn:case regex=\"IBM\" /> </syn:switch>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //Click on the 'Add Child' link of the Switch-Case Mediator
        esbCommon.addMediators("Add Child","0.0","Core","Send");
        assertEquals("Case", selenium.getText("//a[@id='mediator-0.0']"));
        assertEquals("Send", selenium.getText("//a[@id='mediator-0.0.0']"));
        esbCommon.clickMediatorSource("0.0");
		assertEquals("<syn:case xmlns:syn=\"http://ws.apache.org/ns/synapse\" regex=\"IBM\"> <syn:send /> </syn:case>", selenium.getText("mediatorSrc"));
		selenium.click("link=switch to design view");

        //Click on the 'Delete' icon of the 'Switch-Case Mediator'
        esbCommon.delMediator("0.0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Switch"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Case"));

    }

    public void testAddDefaultCase() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_switch_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");

        //Specify a value for the fields & update the mediator
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        esbSwitchMediatorTest.addSwitchMediator("0","//m0:getQuote/m0:request/m0:symbol");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCase("0","IBM","0.0");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");

        //Go to the Switch mediator and click on 'Specify default case'
        esbSwitchMediatorTest.addDefaultCase("0");
        //verify that Defaul case has added to the end of the sequence tree
        assertTrue(selenium.getText("treePane").endsWith("Default"));
        selenium.click("link=Switch");
        assertTrue(!selenium.isElementPresent("link=Specify default case"));

        //Click on the 'Default' node
        selenium.click("link=Default");
        Thread.sleep(2000);
		assertEquals("Switch-Default Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
		assertEquals("There are no Switch-Default mediator specific configuration.", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td"));

        //Click on the 'Help' link of the 'Defaultr' mediator
        //esbCommon.mediatorHelp("Switch Default");

        //Add a child mediators to the Default mediator
        esbCommon.addMediators("Add Child","0.1","Core","Log");
        esbCommon.mediatorUpdate();
        assertEquals("Log",selenium.getText("//div[@id='mediator-0.1.0']"));

        //View the source view of the Default mediator
        esbCommon.clickMediatorSource("0.1");
        assertEquals("<syn:default xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:log /> </syn:default>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //View the source view of the Switch mediator
        selenium.click("//a[@id='mediator-0']");
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:switch xmlns:syn=\"http://ws.apache.org/ns/synapse\" source=\"//m0:getQuote/m0:request/m0:symbol\"> <syn:case regex=\"IBM\" /> <syn:default> <syn:log /> </syn:default> </syn:switch>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //View the source view of the sequence
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_switch_mediator\"> <syn:switch source=\"//m0:getQuote/m0:request/m0:symbol\"> <syn:case regex=\"IBM\" /> <syn:default> <syn:log /> </syn:default> </syn:switch> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

        //Click on 'Delete' of the 'Default' mediator
        esbCommon.delMediator("0.1");
        selenium.click("link=Switch");
        Thread.sleep(2000);
        assertEquals("Specify default case", selenium.getText("link=Specify default case"));
    }

    //Create a complete sequence with a Switch mediator with several cases and a default case.
    // Send a message using a client which will match with at least one case specified in the configuration
    public void testCreatesequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample2Test esbSample2Test=new ESBSample2Test("");
        esbSample2Test.createSequence("sequence_switch");
        assertTrue(esbCommon.searchSequenceInList("sequence_switch"));

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sequence_switch");

        //Invoke client & verify that mediators under the matched case has executed
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        int log_before=esbCommon.checkLogFile("INFO LogMediator symbol = Great stock - IBM, epr = "+esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        int log_after=esbCommon.checkLogFile("INFO LogMediator symbol = Great stock - IBM, epr = "+esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        if (stockQuoteResponse && (log_after-log_before)==1){
            System.out.println("The response received!!!!\nSwitch-case invoked successfully");
        }else{
            throw new MyCheckedException("Error in Switch-case!!!!");
        }
    }

    //Create a complete sequence with a Switch mediator with several cases and a default case.
    // Send a message that does not match with any of the cases
    public void testInvokeDefaultCase() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);

        int log_before=esbCommon.checkLogFile("INFO LogMediator symbol = Normal Stock - SUN, epr = "+esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"SUN");
        int log_after=esbCommon.checkLogFile("INFO LogMediator symbol = Normal Stock - SUN, epr = "+esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        System.out.println(stockQuoteResponse);
        if (stockQuoteResponse && (log_after-log_before)==1)
            System.out.println("The response received!!!!\nDefault-case invoked successfully");
        else
            throw new MyCheckedException("Error in Default-case!!!!");
    }

    public void testSequenceInfomation() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        //verify the information in edit mode
        esbCommon.clickEditSeq("sequence_switch");
        assertEquals("RootAddChildInSwitchCasePropertyCasePropertyDefaultPropertyLogSendOutSend", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));

        //verify information in 'Manage Synapse'
        esbCommon.checkSequenceInfoInSynapse("sequence_switch");
    }

    /*
    To do
     */
    //Create a complete sequence with a Switch mediator and send a message which will not match with the given source Xpath
    //the switch mediator should not be invoked
}
