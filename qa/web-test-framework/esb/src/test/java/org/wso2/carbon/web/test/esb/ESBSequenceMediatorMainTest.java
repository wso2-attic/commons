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


public class ESBSequenceMediatorMainTest extends CommonSetup{
    public ESBSequenceMediatorMainTest(String text){
        super(text);
    }

    public void testAddSequenceMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_sequence_mediator");

        //verify adding a Sequence mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Core","Sequence");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Sequence"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Sequence mediator
        selenium.click("//a[@id='mediator-0']");
		assertEquals("Sequence Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
		assertTrue(selenium.isTextPresent("exact:Referring sequence*"));

        //Click on 'Add Sibling' of the Sequence mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Sequence");

        //verify Click on the 'Delete' icon of the 'Sequence mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Sequence"));
    }

    public void testClickOnRegistryBrowser() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        String localRegList=esbCommon.getLocalRegItems();
        String embeddedRegItem[]=new String[100];
        embeddedRegItem=esbCommon.getEmbeddedRegItems();

        esbCommon.addSequence("test_sequence_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Sequence");
        //verify Click on the 'Registry Key' link of the Script mediator
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('seq_ref')\"]");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Resource Paths \n Local Registry Select A Value"+localRegList+" \n \n Picked Path : \n /"));

        //verify embedded registry items
        selenium.click("plus_root");
        int pickedPath_no=0;
        while(selenium.isElementPresent("father_root_"+pickedPath_no)){
            assertEquals(embeddedRegItem[pickedPath_no], selenium.getText("father_root_"+pickedPath_no));
            pickedPath_no=pickedPath_no+1;
        }
        selenium.click("link=X");
    }

    public void testSelectSequenceFromRegistry() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_sequence_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Sequence");

        //Update the mediator without selecting any sequences from the Registry Browser
        esbCommon.mediatorUpdate();
        assertEquals("Please enter value to Referring sequence", selenium.getText("messagebox-warning"));
		selenium.click("//button[@type='button']");

        //Select a sequence through the 'Registry Browser'
        ESBSequenceMediatorTest esbSequenceMediatorTest=new ESBSequenceMediatorTest(selenium);
        esbSequenceMediatorTest.addSequenceMediator("fault");
        esbCommon.mediatorUpdate();
//        selenium.click("//a[@id='mediator-0']");
//        Thread.sleep(2000);
//        assertEquals("fault",selenium.getSelectedValue("seq_ref"));
        //verify the mediator syntax
        esbCommon.clickMediatorSource("0");
        Thread.sleep(4000);
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" key=\"fault\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
        //verify the sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_sequence_mediator\"> <syn:sequence key=\"fault\" /> </syn:sequence>", selenium.getText("sequence_source"));
        //Switch back to the design view
        esbCommon.clickDesignView();
        selenium.click("//a[@id='mediator-0']");
        selenium.isElementPresent("main");
    }


    public void testDeleteSequenceMediator() throws Exception{
         ESBCommon esbCommon=new ESBCommon(selenium);
         ESBSequenceMediatorTest esbSequenceMediatorTest = new ESBSequenceMediatorTest(selenium);
         esbCommon.logoutLogin();

         esbCommon.addSequence("test_sequence_mediator");
         esbCommon.addRootLevelChildren("Add Child","Core","Sequence");
         esbSequenceMediatorTest.addSequenceMediator("fault");
         esbCommon.mediatorUpdate();

         esbCommon.addMediators("Add Sibling","0","Core","Drop");
         esbCommon.sequenceSave();

         esbCommon.viewSequences();
         esbCommon.clickEditSeq("test_sequence_mediator");
         //verify sequence in edit mode
         assertEquals("RootAddChildSequenceDrop", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));

         //Click on the 'Delete' icon of the 'Sequence mediator'
         esbCommon.delMediator("0");
         //Save the sequence
         esbCommon.sequenceSave();
         assertTrue(esbCommon.searchSequenceInList("test_sequence_mediator"));

         //verify Sequence mediator has successfully deleted from the sequnce tree
         esbCommon.viewSequences();
         esbCommon.clickEditSeq("test_sequence_mediator");
         assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Sequence"));
         assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Drop"));

         //delete the sequnce from sequnce list
         esbCommon.deleteSequence("test_sequence_mediator");
     }

    /*
    Create a complete sequence with a Sequence mediator and save the sequence
    Set the above created sequence to the Main sequence and invoke the main sequence
     */
    //This scenario test through the sample3
}
