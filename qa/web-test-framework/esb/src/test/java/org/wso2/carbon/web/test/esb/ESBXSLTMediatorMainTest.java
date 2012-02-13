package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
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

public class ESBXSLTMediatorMainTest extends CommonSetup {
	Properties properties = new Properties();

    public ESBXSLTMediatorMainTest(String text) {
        super(text);
    }


    public void testAddXsltMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_Xslt_mediator");
        //Add a XSLT mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Transform","XSLT");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("XSLT"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the Xslt mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("XSLT Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        assertTrue(selenium.isTextPresent("exact:Key* Registry Browser Source NameSpaces"));
        assertTrue(selenium.isElementPresent("mediator.xslt.source"));
        assertTrue(selenium.isElementPresent("link=Add Property"));
        assertTrue(selenium.isElementPresent("link=Add Feature"));

        //Click on 'Add Sibling' of the Xslt mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("XSLT");

        //Click on the 'Delete' icon of the 'Xslt mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("XSLT"));
    }

    public void testSelectKey() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addLocalEntry();
        
        esbCommon.addSequence("test_Xslt_mediator");
        //Add a XSLT mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Transform","XSLT");

        //Click on update without specifying a key
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);        
        selenium.click("//input[@value='Update']");
        assertEquals("Please specify the XSLT script key", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");
        
        //Select a key from the 'Registry Browser'
        ESBXSLTMediatorTest esbXSLTMediatorTest=new ESBXSLTMediatorTest(selenium);
        esbCommon.addSequence("test_Xslt_mediator");
        //Add a XSLT mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Transform","XSLT");
        esbXSLTMediatorTest.addXSLTMediator("0","source_url");
        esbCommon.mediatorUpdate();
        //verify mediator source view
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:xslt xmlns:syn=\"http://ws.apache.org/ns/synapse\" key=\"source_url\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
        //verify Sequnce source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_Xslt_mediator\"> <syn:xslt key=\"source_url\" /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }

    public void testAddProperty() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addLocalEntry();
        ESBXSLTMediatorTest esbXSLTMediatorTest=new ESBXSLTMediatorTest(selenium);
        esbCommon.addSequence("test_Xslt_mediator");
        esbCommon.addRootLevelChildren("Add Child","Transform","XSLT");
        //Select a key from the 'Registry Browser'
        esbXSLTMediatorTest.addXSLTMediator("0","source_url");
        esbCommon.mediatorUpdate();

        //Click on the 'Add Property' link
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.click("link=Add Property");
        Thread.sleep(2000);
        assertEquals("Properties of the XSLT mediator Property Name Property Type Value / Expression Action ValueExpressionDelete", selenium.getTable("//form[@id='mediator-editor-form']/div/table.2.0"));
		assertTrue(selenium.isElementPresent("propertyValue0"));

        //click on Update without giving Propety Name
        selenium.click("//input[@value='Update']");
        assertEquals("Name of a XSLT mediator property cannot be empty", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //Click on update without giving a value for property
        selenium.type("propertyName0", "UserName");
        selenium.click("//input[@value='Update']");
        assertEquals("Value of a XSLT mediator property cannot be empty", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //Delete the property
        selenium.click("//a[@onclick='deleteproperty(0)']");
    }

    public void testAddFeature() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addLocalEntry();
        ESBXSLTMediatorTest esbXSLTMediatorTest=new ESBXSLTMediatorTest(selenium);
        esbCommon.addSequence("test_Xslt_mediator");
        esbCommon.addRootLevelChildren("Add Child","Transform","XSLT");
        //Select a key from the 'Registry Browser'
        esbXSLTMediatorTest.addXSLTMediator("0","source_url");

        //Click on the 'Add Features' link
        selenium.click("link=Add Feature");
        assertEquals("Features of the XSLT mediator Feature Name Feature Value Action TrueFalseDelete", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[5]/td"));
        assertTrue(selenium.isElementPresent("featureName0"));

        //Click on update without spacifying Feature Name
        selenium.click("//input[@value='Update']");
		assertEquals("Names of an XSLT mediator features cannot be empty", selenium.getText("messagebox-warning"));
		selenium.click("//button[@type='button']");
    }

    //Create a complete sequence with an XSLT with a valid source path and invoke using a client
    public void testCreateSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBXSLTMediatorTest esbXSLTMediatorTest=new ESBXSLTMediatorTest(selenium);
        //Create a complete sequence with an XSLT with a valid source path and invoke using a client
        ESBSample8Test esbSample8Test=new ESBSample8Test("");
        esbSample8Test.addReqXsltLocalEntry();
        esbSample8Test.addResXsltLocalEntry();
        esbSample8Test.createSequence("XSLT_seq1");
        //Give source path
        esbCommon.editSeqMediator("XSLT_seq1","In","XSLT");
        esbXSLTMediatorTest.addXsltSource("//m0:CheckPriceRequest");
        esbXSLTMediatorTest.addSourceNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        esbCommon.setupMainSeq();
        esbCommon.setSequenceToSequence("main","XSLT_seq1");
        //Invoke the client & verify the transformation
        invokeClient();
    }

    public void testEditSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBXSLTMediatorTest esbXSLTMediatorTest=new ESBXSLTMediatorTest(selenium);
        esbCommon.viewSequences();
        if(selenium.isTextPresent("XSLT_seq1")){
            //Create a complete sequence with an XSLT with a invalid source path and invoke using a client
            esbCommon.editSeqMediator("XSLT_seq1","In","XSLT");
            esbXSLTMediatorTest.addXsltSource("//m0:Code");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            esbCommon.setupMainSeq();
            esbCommon.setSequenceToSequence("main","XSLT_seq1");
            //Invoke the client & verify the transformation
            invokeClient();
        }
        else
            throw new MyCheckedException("Sequence \"XSLT_seq1\" not found..!");
    }

    public void testVerifySequenceInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Select relevant information and save the mediator and then save the sequence
        //verify in edit mode
        esbCommon.viewSequences();
        if(selenium.isTextPresent("XSLT_seq1")){
            esbCommon.clickEditSeq("XSLT_seq1");
            //assertEquals("Root Add Child \n \n \n \n \nIn\n\n\nXSLT\n \nOut\n\n\nXSLT\n \nSend", selenium.getText("//div[@id='treePane']/div"));
            //verify information in 'Manage Synapse'
            boolean status=esbCommon.verifyManageSynapseConfig("sequence name=\"XSLT_seq1\"");
            if(!status)
                throw new MyCheckedException("Sequence not found in the ManageSynapseConfig..Incorrect!");
        }
        else
            throw new MyCheckedException("Sequence \"XSLT_seq1\" not found..!");
    }


    /*
    Invoking the client
     */
    public void invokeClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = esbSampleClient.customQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/","http://localhost:9000/services/SimpleStockQuoteService","IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            System.out.println("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    public void addLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Add a local entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("source_url","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.js");
    }
}

