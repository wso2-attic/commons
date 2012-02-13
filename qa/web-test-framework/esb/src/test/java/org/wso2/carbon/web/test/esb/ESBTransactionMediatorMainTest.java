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

public class ESBTransactionMediatorMainTest  extends CommonSetup{

    public ESBTransactionMediatorMainTest(String text) {
        super(text);
    }

    public void testAll() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBXSLTMediatorTest esbXSLTMediatorTest=new ESBXSLTMediatorTest(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon=new ESBCommon(selenium);

        verifyAddTransactionMediator("test_transaction_mediator");

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        seleniumTestBase.logOutUI();
    }

    public void verifyAddTransactionMediator(String seqName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);

        esbCommon.addSequence(seqName);
        //Add a Cache mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Transaction"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));


        //Click on the Transaction mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Transaction Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        selenium.getSelectedValue("mediator.transaction.transaction_action").equals("Select Transaction Action");

        //View the combo box options of 'Action'
        assertTrue(selenium.isTextPresent("Select Transaction Action Commit Transaction Fault if no Transaction Initiate new Transaction Resume Transaction Rollback Transaction Suspend Transaction Use existing or Initiate Transaction"));


        //Click on 'Add Sibling' of the Fault mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Transaction");

        //Click on the 'Delete' icon of the 'Cache mediator'
        esbCommon.delMediator("0");
        assertEquals("Root Add Child", selenium.getText("treePane"));

        esbCommon.addSequence(seqName);
        //Add a Cache mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        //Select an option from the combo box and click on 'Update'
        ESBTransactionMediatorTest esbTransactionMediatorTest = new ESBTransactionMediatorTest(selenium);
        esbTransactionMediatorTest.addTransactionMediator("0","Commit Transaction");
        esbCommon.sequenceSave();
        //verify in edit mode
        esbCommon.viewSequences();
        esbCommon.editSeqMediator(seqName,"Transaction",null);
        selenium.click("//a[@id='mediator-0']");
        selenium.getSelectedValue("mediator.transaction.transaction_action").equals("Commit Transaction");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Transaction"));
        
        //Add a mediator, specify all values and switch to the source view
        selenium.click("link=Transaction");
        String action=selenium.getSelectedValue("mediator.transaction.transaction_action");
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:transaction xmlns:syn=\"http://ws.apache.org/ns/synapse\" action=\""+action+"\" />", selenium.getText("mediatorSrc"));
        //Switch back to the design view
        selenium.click("link=switch to design view");
        Thread.sleep(2000);
        assertEquals(action,selenium.getSelectedValue("mediator.transaction.transaction_action"));

        //verify Manage Synapse
        esbCommon.clickSequenceSource("0");
        String seq_source = selenium.getText("sequence_source");
        String temp1=seq_source.substring(seq_source.indexOf("name"));
        String temp2="<syn:sequence "+temp1;
        temp2=temp2.replaceAll(" />","/>");
        esbCommon.clickSynapse();
        boolean status=esbCommon.verifyManageSynapseConfig(temp2);
        if(!status)
            throw new MyCheckedException("Sequence infomations are incorrect in synapse..!");

        //Do changes to the sequence, save the sequence and view in the edit mode
        esbCommon.editSeqMediator("test_transaction_mediator","Transaction",null);
        esbTransactionMediatorTest.addTransactionMediator("0","Fault if no Transaction");
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_transaction_mediator");
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:transaction xmlns:syn=\"http://ws.apache.org/ns/synapse\" action=\"fault-if-no-tx\" />", selenium.getText("mediatorSrc"));

    }

}