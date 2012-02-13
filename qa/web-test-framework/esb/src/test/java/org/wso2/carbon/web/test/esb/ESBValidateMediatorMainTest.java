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

public class ESBValidateMediatorMainTest  extends CommonSetup{

    public ESBValidateMediatorMainTest(String text) {
        super(text);
    }

    public void testApplyDebugMode() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.applyDebugMode();
    }

    public void testAddValidateMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_validate_mediator");
        //verify adding a Out mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Validate"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Validate mediator
        selenium.click("//a[@id='mediator-0']");
		assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
		assertEquals("Validate Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
		assertEquals("Schema keys defined for Validate Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/h3"));
		assertTrue(selenium.isTextPresent("exact:Key* Registry Keys Delete"));
		assertEquals("Add New Schema Key", selenium.getText("link=Add New Schema Key"));
		assertEquals("Source Namespace", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[4]/td"));
		assertEquals("Features defined for Validator Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[5]/td/h3"));
		assertEquals("Add feature", selenium.getText("link=Add feature"));

        //verify Click on 'Add Sibling' of the Router mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Validate");

        //verify Click on the 'Delete' icon of the 'Validate mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Validate"));
    }

    public void testClickOnRegistryKeys() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        String localRegList=esbCommon.getLocalRegItems();
        String embeddedRegItem[]=new String[100];
        embeddedRegItem=esbCommon.getEmbeddedRegItems();

        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        //verify the local entries and registry entries listed when click on 'Registry keys'
        selenium.click("link=Registry Keys");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Resource Paths \n Local Registry Select A Value"+localRegList+" \n \n Picked Path : \n /"));

        //verify embedded registry items
        /*
         If the  entry name has more than 13 characters this will not work!
        */
         selenium.click("plus_root");
         int pickedPath_no=0;
         while(selenium.isElementPresent("father_root_"+pickedPath_no)){
               assertEquals(embeddedRegItem[pickedPath_no], selenium.getText("father_root_"+pickedPath_no));
               pickedPath_no=pickedPath_no+1;
         }
         selenium.click("link=X");
    }

    public void testSelectKeyFromRegistry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        //verify Selecting a key from the 'Registry Browser'
        selenium.click("link=Registry Keys");
        Thread.sleep(2000);
        esbCommon.selectResource("Sequence", "main");

        //please do verification
    }

    public void testDeleteSchemaKey() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        //verify Selecting a key from the 'Registry Browser'
        selenium.click("link=Registry Keys");
        Thread.sleep(2000);
        esbCommon.selectResource("Sequence", "fault");
        //verify Click on the 'Delete' icon
        Thread.sleep(2000);
        selenium.click("//a[@onclick='deleteKey(0);return false;']");
        selenium.click("//button[@type='button']");

        //verify that schema had removed
        assertTrue(!selenium.isElementPresent("//tr[@id='keyRaw0']/td[1]"));
        assertTrue(!selenium.isElementPresent("//tr[@id='keyRaw0']/td[2]"));
        assertTrue(!selenium.isElementPresent("//tr[@id='keyRaw0']/td[3]"));
        assertTrue(!selenium.isElementPresent("//tr[@id='keyRaw0']/td[4]"));
    }

    public void testAddNewSchemaKey() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");

        //verify Click on the 'Add New Schema Key' link without specifying a Key
        selenium.click("link=Add New Schema Key");
	    selenium.click("link=Add New Schema Key");
	    assertEquals("Value of the key is empty, can not add futher keys", selenium.getText("messagebox-warning"));
	    selenium.click("//button[@type='button']");

        //verify Click on the 'Add New Schema Key' link after selecting a Key
        selenium.click("link=Registry Keys");
        Thread.sleep(2000);
        esbCommon.selectResource("Sequence", "main");
        selenium.click("link=Add New Schema Key");
        Thread.sleep(2000);
        assertTrue(selenium.isElementPresent("//tr[@id='keyRaw1']/td[1]"));
        assertTrue(selenium.isElementPresent("//a[@onclick=\"showInLinedRegistryBrowser('key1')\"]"));
		assertTrue(selenium.isElementPresent("//a[@onclick='deleteKey(1);return false;']"));
    }

    public void testClickOnNameSpaceLink() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");

        //verify Click on the 'Namespace' link
        selenium.click("link=Namespace");
        assertTrue(selenium.isTextPresent("Namespace EditorX"));
        selenium.click("link=X");
    }

    public void testAddFeature() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");

        //verify Click on the 'Add Feature' link
        selenium.click("link=Add feature");
        assertEquals("Features defined for Validator Mediator Feature Name Action Delete", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[5]/td"));

        //Click on the 'Add Feature' link without specifying a feature for the first item of the feature list
        selenium.click("link=Add feature");
        assertEquals("Name of a feature is empty, can not add further features", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //verify Click on the 'Delete' icon of a selected Feature
        selenium.selectFrame("relative=up");
        selenium.click("//a[@onclick='deletefeature(0);return false;']");
        selenium.click("//button[@type='button']");
        assertEquals("Add feature", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[6]/td"));
    }

    public void testValidateMediatorSyntax() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample7Test esbSample7Test=new ESBSample7Test("");
        esbSample7Test.addInlineLocalEntry();

        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");

        //Specify required information for the Validate mediator and view the source view of the mediator
        ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
        esbValidateMediatorTest.addValidateMediatorSchemaKey("0","validate_schema");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");
        esbValidateMediatorTest.addSource("source");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");
        esbValidateMediatorTest.addFeatures("feature");
        esbCommon.mediatorUpdate();

        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:validate xmlns:syn=\"http://ws.apache.org/ns/synapse\" source=\"source\"> <syn:schema key=\"validate_schema\" /> <syn:feature name=\"feature\" value=\"true\" /> <syn:on-fail /> </syn:validate>", selenium.getText("mediatorSrc"));


        //verify sequnce source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_validate_mediator\"> <syn:validate source=\"source\"> <syn:schema key=\"validate_schema\" /> <syn:feature name=\"feature\" value=\"true\" /> <syn:on-fail /> </syn:validate> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }


    /*
    Create a complete sequence with a Validate mediator without specifying the source and invoke using a client
     */
    //This scenario verifies through the sample_7


    /*
    Create a complete sequence with a Validate mediator without specifying the source and invoke using a client when the schema does not match with the message sent
     */
    public void testCreateSequnceWithValidateMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample7Test esbSample7Test=new ESBSample7Test("");
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbSample7Test.addInlineLocalEntry();
        esbSample7Test.createSequence("seq_validate");

        //edit the schema
        esbManageLocalEntriesTest.clickEditLocalEntry("validate_schema");
        String newEntry=selenium.getText("entry_value").replace("<xs:element name=\"symbol\"","<xs:element name=\"stocksymbol\"");
        selenium.type("entry_value",newEntry);
        selenium.click("//input[@value='Save']");
        selenium.waitForPageToLoad("30000");

        //setup the main sequence
        esbCommon.setSequenceToSequence("main","seq_validate");
        //Invoke client
        ESBSample5Test esbSample5Test=new ESBSample5Test("");
        int log_before1=esbCommon.checkLogFile("ValidateMediator Validation source : <ns:getQuote xmlns:ns=\"http://services.samples\"><ns:request><ns:symbol>IBM</ns:symbol></ns:request></ns:getQuote>");
        int log_before2=esbCommon.checkLogFile("ValidateMediator Validation of element returned by XPath : s11:Body/child::*[position()=1] | s12:Body/child::*[position()=1] failed against the given schema(s) [validate_schema]with error : cvc-complex-type.2.4.a: Invalid content was found starting with element 'ns:symbol'. One of '{\"http://services.samples\":stocksymbol}' is expected. Executing 'on-fail' sequence");
        boolean stockQuoteResponse=esbSample5Test.faultHandlingStockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        int log_after1=esbCommon.checkLogFile("ValidateMediator Validation source : <ns:getQuote xmlns:ns=\"http://services.samples\"><ns:request><ns:symbol>IBM</ns:symbol></ns:request></ns:getQuote>");
        int log_after2=esbCommon.checkLogFile("ValidateMediator Validation of element returned by XPath : s11:Body/child::*[position()=1] | s12:Body/child::*[position()=1] failed against the given schema(s) [validate_schema]with error : cvc-complex-type.2.4.a: Invalid content was found starting with element 'ns:symbol'. One of '{\"http://services.samples\":stocksymbol}' is expected. Executing 'on-fail' sequence");

        if((log_after1-log_before1)!=1)
            throw new MyCheckedException("Validate mediator hasn't specify the source..The xpath should be resolved to the full soap body..Error in validate source path!!! ");
        if((log_after2-log_before2)!=1)
            throw new MyCheckedException("since the schema is invalid,the message should NOT be validated..Error in validating the schema!!!");
        if(!stockQuoteResponse)
            throw new MyCheckedException("Fault should be received by the client.Error in validation..!");
     }

    /*
    Create a complete sequence with a Validate mediator specifying a valid source and invoke using a client
     */
    public void testCreateSequenceWithValidSouce() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
        esbCommon.logoutLogin();

        //edit the sequence
        esbCommon.editSeqMediator("seq_validate","In","Validate");
        esbValidateMediatorTest.addSource("//m0:getQuote/m0:request/m0:symbol");
        esbValidateMediatorTest.addValidateNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //invoke the client
        ESBSample5Test esbSample5Test=new ESBSample5Test("");
        int log_before1=esbCommon.checkLogFile("ValidateMediator Validation source : <ns:symbol xmlns:ns=\"http://services.samples\">IBM</ns:symbol>");
        boolean stockQuoteResponse=esbSample5Test.faultHandlingStockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        int log_after1=esbCommon.checkLogFile("ValidateMediator Validation source : <ns:symbol xmlns:ns=\"http://services.samples\">IBM</ns:symbol>");

        if((log_after1-log_before1)!=1)
            throw new MyCheckedException("Validate mediator has specify the valid source..The xpath should be resolved to the matching elements of soap body..Error in validate source path!!! ");
        if(!stockQuoteResponse)
            throw new MyCheckedException("Client failed..!");
    }

    public void testSequenceInformation() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(selenium.isTextPresent("seq_validate")){
            //verify Select relevant information and save the mediator and then save the sequence
            //verify information in edit mode
            esbCommon.viewSequences();
            esbCommon.clickEditSeq("seq_validate");
            //assertEquals("Root Add Child \n \n \n \n \nIn\n\n\nValidate\n\n\nFault\n\n\nProperty\n\n\nHeader\n \nSend", selenium.getText("treePane"));
            //verify information in 'Manage Synapse'
            boolean status=esbCommon.verifyManageSynapseConfig("sequence name=\"seq_validate\"");
            if(!status)
                throw new MyCheckedException("Sequence not found in the ManageSynapseConfig..Incorrect!");
        }
        else
            System.out.println("sequence \"seq_validate\" not found..!");
    }

    public void testEditSequnce() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(selenium.isTextPresent("seq_validate")){
            //Create a complete sequence with a Validate mediator specifying a valid source and invoke using a client
            ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
            esbCommon.editSeqMediator("seq_validate","In","Validate");
            esbValidateMediatorTest.addSource("m0:add");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
        }
        else
            System.out.println("sequence \"seq_validate\" not found..!");
    }


    /*
    Create a complete sequence with a Validate mediator specifying a feature/features and invoke using a client
     */
    //To do


    public void testDeleteValidateMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_validate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        esbValidateMediatorTest.addValidateMediatorSchemaKey("0","validate_schema");
        esbCommon.mediatorUpdate();

        esbCommon.addMediators("Add Sibling","0","Core","Drop");
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_validate_mediator");
        //Click on the 'Delete' icon of the 'Validate mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        esbCommon.viewSequences();
        assertTrue(esbCommon.searchSequenceInList("test_validate_mediator"));

        //verify Validate mediator has successfully deleted from the sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_validate_mediator");
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Validate"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Drop"));

        //delete the sequnce from sequnce list
        esbCommon.deleteSequence("test_validate_mediator");
    }
    
    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
