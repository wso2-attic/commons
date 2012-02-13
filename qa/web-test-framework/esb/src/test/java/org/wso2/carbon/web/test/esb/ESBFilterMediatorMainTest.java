package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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


public class ESBFilterMediatorMainTest  extends CommonSetup{

    public ESBFilterMediatorMainTest(String text) {
        super(text);
    }

    //verify adding a filter mediator
    public void testAddFilterMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_Filter_mediator");

        //verify adding a Sequence mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Filter"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Delete"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Then"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Else"));

        //verify click on the Filter mediator
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
		assertEquals("Specify As XPath Source and Regular Expression", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td"));
        assertTrue(selenium.isTextPresent("exact:XPath *"));

        //Click on 'Add Sibling' of the Sequence mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Filter");

        //Select the radio button option 'Xpath'
        selenium.click("xpath");
        assertTrue(selenium.isTextPresent("exact:XPath *"));
		assertEquals("", selenium.getText("//tr[@id='mediator.filter.xpath']/td[2]"));
		assertEquals("Namspaces", selenium.getText("mediator.callout.target.xpath_nmsp_button"));

        //Click on the 'namespace' link
        selenium.click("mediator.callout.target.xpath_nmsp_button");
        assertTrue(selenium.isTextPresent("Namespace EditorX"));
        selenium.click("link=X");
    }

    public void testSpecifyXpathWithNs() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBFilterMediatorTest esbFilterMediatorTest=new ESBFilterMediatorTest(selenium);

        esbCommon.addSequence("test_Filter_mediator");

        //Specify the Xpath, give the proper namespace and update the mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");
        esbFilterMediatorTest.addXpath("0","//m0:getQuote/m0:symbol");
        esbFilterMediatorTest.addFilterNamespace("m0","http://services/samples0");
        esbCommon.mediatorUpdate();
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_Filter_mediator\"> <syn:filter xmlns:m0=\"http://services/samples0\" xpath=\"//m0:getQuote/m0:symbol\"> <syn:then /> <syn:else /> </syn:filter> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:filter xmlns:syn=\"http://ws.apache.org/ns/synapse\" xmlns:m0=\"http://services/samples0\" xpath=\"//m0:getQuote/m0:symbol\"> <syn:then /> <syn:else /> </syn:filter>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //verify Switch back to the design view
        selenium.click("//a[@id='mediator-0']");
        assertEquals("//m0:getQuote/m0:symbol", selenium.getValue("filter_xpath"));
        Thread.sleep(2000);
        selenium.click("mediator.callout.target.xpath_nmsp_button");
        Thread.sleep(2000);
        assertEquals("m0", selenium.getValue("prefix0"));
        assertEquals("http://services/samples0", selenium.getValue("uri0"));
        selenium.click("link=X");
    }

    //*****************************************************************************************************************
    /*
    This method is used to check a customer issue..
    add a Filter mediator and insert some values with special characters
    E.g.:- <syn:filter xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xpath="not(doc("C:/Temp/wso2/Temp/soapenv.xml")/soapenv:Envelope/soapenv:Body/ soapenv:Fault)">

    When saving the sequence, it should not allow you to save since there is an issue with the Xpath given
     */


    public void testSpecifyInvalidXpath() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        specifyInvalidXpath("test_filter_xpath1","\"not(doc(\"C:/Temp/wso2/Temp/soapenv.xml\")/soapenv:Envelope/soapenv:Body/ soapenv:Fault)\"");
        specifyInvalidXpath("test_filter_xpath2","not(doc(\"C:/Temp/wso2/Temp/soapenv.xml\")/soapenv:Envelope/soapenv:Body/ soapenv:Fault)");

    }

    public void specifyInvalidXpath(String seqName,String xpath) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBFilterMediatorTest esbFilterMediatorTest=new ESBFilterMediatorTest(selenium);

        esbCommon.addSequence(seqName);

        //Specify the Xpath, give the proper namespace and update the mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");
        esbFilterMediatorTest.addXpath("0",xpath);
        esbCommon.mediatorUpdate();
        if(selenium.isElementPresent("messagebox-error")){
            assertEquals("Error while updating the Filter mediator", selenium.getText("messagebox-error"));
            selenium.click("//button[@type='button']");
            selenium.click("//a[@id='mediator-0']");
		    assertEquals("", selenium.getValue("filter_xpath"));
        }

        //If error message was not prompt when update the mediator,,then save the seqence and verify the info in edit mode.
        else{
            esbCommon.sequenceSave();
            esbCommon.clickEditSeq(seqName);
            selenium.click("//a[@id='mediator-0']");
            assertEquals(xpath, selenium.getValue("filter_xpath"));
        }
    }
    //******************************************************************************************************************

    public void testSelectRadioOption() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_Filter_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");

        //Select the radio button option 'Source and Regular Expression'
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.click("xpathRex");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("exact:Source *"));
        assertEquals("Namspaces", selenium.getText("//tr[@id='mediator.filter.source']/td[3]"));
        assertTrue(selenium.isTextPresent("exact:Regex *"));
    }

    public void testDeleteFilterMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_Filter_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");

        //verify Click on the 'Delete' icon of the 'Filter mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Filter"));
    }


    public void testCreateSeq() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Create a complete sequence with a Filter mediator with the 'Specify As' option set to 'Xpath'
        createSeqWithXpath("Filter_seq1");
        assertTrue(selenium.isTextPresent("Filter_seq1"));
    }

    public void testXpathWithOutNs() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.viewSequences();
        if(selenium.isTextPresent("Filter_seq1")){
            //Create a complete sequence with a Filter mediator with the 'Specify As' option set to 'Xpath' and save the sequence.NOT specify the namespace
            editSequenceRemoveNs("Filter_seq1");
            assertTrue(selenium.isTextPresent("Filter_seq1"));
            //Save the sequence and view options in the edit mode
            esbCommon.clickEditSeq("Filter_seq1");
            //assertEquals("Root Add Child \n \n \n \n \nFilter\n\n\nThen\n\n\nSend\n\n\nDrop\n\nElse\n \nSend", selenium.getText("treePane"));
        }
        else
            System.out.println("sequence \"Filter_seq1\" not found..!");
    }

    public void testCreateSeq2() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Create a complete sequence with a Filter mediator with the 'Specify As' option set to 'Source and Regular Expression' and save the sequence. specify the correct namespace
        ESBSample1Test esbSample1Test=new ESBSample1Test("");
        esbSample1Test.createSequence("Filter_seq2");
        assertTrue(selenium.isTextPresent("Filter_seq2"));
    }

    public void testEditSeq() throws Exception{
        //Create a complete sequence with a Filter mediator with the 'Specify As' option set to 'Source and Regular Expression' and save the sequence. NOT specify the namespace
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.viewSequences();
        if(selenium.isTextPresent("Filter_seq2")){
            editSequenceRemoveNs("Filter_seq2");
            assertTrue(selenium.isTextPresent("Filter_seq2"));
        }
        else
            System.out.println("sequence \"Filter_seq2\" not found..!");
    }

    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        seleniumTestBase.logOutUI();
    }


    //Create a complete sequence with a Filter mediator with the 'Specify As' option set to 'Xpath'..
    public void createSeqWithXpath(String seqName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbCommon.addSequence(seqName);

        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");
        esbFilterMediatorTest.addXpath("0","//m0:getQuote/m0:symbol");
        esbFilterMediatorTest.addFilterNamespace("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbFilterMediatorTest.addThenChildMediators("0.0","Core", "Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbFilterMediatorTest.addThenChildMediators("0.0","Core","Drop");

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("1");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();
    }

    //This method is used to remove the name spaces that has added to the Xpath of Filter mediator.
    public void editSequenceRemoveNs(String seqName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.editSeqMediator(seqName,"Filter",null);
        Thread.sleep(2000);
        selenium.click("mediator.callout.target.xpath_nmsp_button");
        int prefix_no=0;
        while(selenium.isElementPresent("prefix"+prefix_no)){
           selenium.click("//a[@onclick=\"deletensraw('"+prefix_no+"')\"]");
           selenium.click("//button[@type='button']");
           prefix_no=prefix_no+1;
        }
        selenium.click("saveNSButton");
        selenium.click("//input[@value='Update']");
        esbCommon.sequenceSave();
    }
}

