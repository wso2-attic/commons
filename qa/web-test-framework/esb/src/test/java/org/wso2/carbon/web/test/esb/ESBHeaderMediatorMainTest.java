package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ESBAggregateClient;

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

public class ESBHeaderMediatorMainTest  extends CommonSetup {

    public ESBHeaderMediatorMainTest(String text) {
        super(text);
    }

    public void testAddHeaderMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_header_mediator");
        //Add a Header mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Header"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the Header mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Header Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        assertEquals("on", selenium.getValue("set"));
        assertEquals("off", selenium.getValue("remove"));
        assertEquals("on", selenium.getValue("value"));
        assertEquals("off", selenium.getValue("expression"));

        //Click on 'Add Sibling' of the Header mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Header");

        //Update mediator without specifying header name
        selenium.click("//input[@value='Update']");
        assertEquals("Header name is required", selenium.getText("messagebox-error"));
        selenium.click("//button[@type='button']");

        //Update without specifying header value
        selenium.type("mediator.header.name", "name");
        selenium.click("//input[@value='Update']");
		assertEquals("Value or Expression is required", selenium.getText("messagebox-error"));
		selenium.click("//button[@type='button']");

        //Click on the radio button option 'Set'
        selenium.click("set");
        assertTrue(selenium.isTextPresent("exact:Name  *"));
		assertTrue(selenium.isTextPresent("Set Remove"));
		assertTrue(selenium.isTextPresent("exact:Value Expression *"));

        //Click on the radio button option 'Remove'
        selenium.click("remove");
        assertTrue(selenium.isTextPresent("exact:Name  *"));
        assertTrue(selenium.isTextPresent("Set Remove"));

        //Click on the 'Delete' icon of the 'Header mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Header"));
        assertTrue(!selenium.isTextPresent("exact:Value Expression *"));
    }

    public void testDeleteHeaderMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_Header_mediator");
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0","To");
        esbHeaderMediatorTest.setHeaderAction("value","http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();
        esbCommon.addMediators("Add Sibling","0","Core","Log");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_Header_mediator");
        //Click on the 'Delete' icon of the 'Header mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        esbCommon.viewSequences();
        assertTrue(selenium.isTextPresent("test_Header_mediator"));

        //verify Header mediator has successfully deleted from the sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_Header_mediator");
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Header"));

        //delete the sequnce from sequnce list
        esbCommon.deleteSequence("test_Header_mediator");
    }


    public void testHeaderMediatorSourceSyntax() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Adding a Header mediator
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbCommon.addSequence("test_header_mediator");
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0","To");
        esbHeaderMediatorTest.setHeaderAction("value","http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();

        //        <header name="qname" (value="literal" | expression="xpath") [action="set"]/>
        //        <header name="qname" action="remove"/>

        //selenium.click("//div[@id='mediator-"+header_mediator_level+"']/div/div[1]/a");
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        String h_name = selenium.getValue("mediator.header.name");
        String h_set = selenium.getValue("set");
        String h_remove = selenium.getValue("remove");
        String h_value = selenium.getValue("value");
        String h_expression = selenium.getValue("expression");
        String h_val_ex = selenium.getValue("mediator.header.val_ex");
        selenium.click("mediator.header.name.namespace_button");
        Thread.sleep(2000);
        String ns_prefix=selenium.getValue("prefix0");
        String ns_uri=selenium.getValue("uri0");
        selenium.click("link=X");

        esbCommon.clickMediatorSource("0");
        if(h_set.equals("on")){
            if(ns_prefix.equals("") && h_value.equals("on"))
                assertEquals("<syn:header xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+h_name+"\" value=\""+h_val_ex+"\" />", selenium.getText("mediatorSrc"));
            else if(ns_prefix.equals("") && h_expression.equals("on"))
                assertEquals("<syn:header xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+h_name+"\" expression=\""+h_val_ex+"\" />", selenium.getText("mediatorSrc"));
            else if(!ns_prefix.equals("") && h_value.equals("on"))
                assertEquals("<syn:header xmlns:syn=\"http://ws.apache.org/ns/synapse\" xmlns:"+ns_prefix+"=\""+ns_uri+"\" name=\""+ns_prefix+":"+h_name+"\" value=\""+h_val_ex+"\" />", selenium.getText("mediatorSrc"));
            else if(!ns_prefix.equals("") && h_expression.equals("on"))
                assertEquals("<syn:header xmlns:syn=\"http://ws.apache.org/ns/synapse\" xmlns:"+ns_prefix+"=\""+ns_uri+"\" name=\""+ns_prefix+":"+h_name+"\" expression=\""+h_val_ex+"\" />", selenium.getText("mediatorSrc"));

        }
        else if(h_remove.equals("on")){
            if(ns_prefix.equals(""))
                assertEquals("<syn:header xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+h_name+"\" action=\"remove\" />", selenium.getText("mediatorSrc"));
            else
                assertEquals("<syn:header xmlns:syn=\"http://ws.apache.org/ns/synapse\" xmlns:"+ns_prefix+"=\""+ns_uri+"\" name=\""+ns_prefix+":"+h_name+"\" action=\"remove\" />", selenium.getText("mediatorSrc"));
        }

        //verify Switch back to the design view
        //The information of the mediator should be displayed without the data being lost/unchanged
        selenium.click("link=switch to design view");
        Thread.sleep(2000);
        selenium.click("//a[@id='mediator-0']");
        assertEquals(h_name,selenium.getValue("mediator.header.name"));
        assertEquals(h_set,selenium.getValue("set"));
        assertEquals(h_remove,selenium.getValue("remove"));
        assertEquals(h_value,selenium.getValue("value"));
        assertEquals(h_expression,selenium.getValue("expression"));
        assertEquals(h_val_ex,selenium.getValue("mediator.header.val_ex"));
        selenium.click("mediator.header.name.namespace_button");
        assertEquals(ns_prefix,selenium.getValue("prefix0"));
        assertEquals(ns_uri,selenium.getValue("uri0"));
        selenium.click("link=X");
    }

    //Create a complete sequence with the radio button 'Set', specify a Header name along with the correct namespace and select 'Value' and specify the name.
    // Then invoke using a client
    /*
    This scenario verifies through the Sample_6
     */

    //Create a complete sequence with the radio button 'Remove', specify a Header name along with the correct namespace and invoke using a client
    /*
    This scenario verifies through the Sample_200
     */

    public void testCreateSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(!esbCommon.searchSequenceInList("sample_6")){
            ESBSample6Test esbSample6Test=new ESBSample6Test("");
            esbSample6Test.createSequence("Header_seq1");
            selenium.isTextPresent("Header_seq1");
        }
    }

    public void testSequenceInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Select relevant information and save the mediator and then save the sequence
        esbCommon.viewSequences();
        if(selenium.isTextPresent("sample_6"))
            esbCommon.clickEditSeq("sample_6");
        else
            esbCommon.clickEditSeq("Header_seq1");
        assertEquals("RootAddChildInHeaderSend", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));

        //verify the information  available on the the Manage Synapse Configuration
        esbCommon.clickSequenceSource("0");
        String seq_source = selenium.getText("sequence_source");
        String temp1=seq_source.substring(seq_source.indexOf("name"));
        String temp2="<syn:sequence "+temp1;
        temp2=temp2.replaceAll(" />","/>");
        esbCommon.clickSynapse();
        boolean status=esbCommon.verifyManageSynapseConfig(temp2);
        if(!status)
            throw new MyCheckedException("Sequence information incorrect in the manage synapse..!!");
    }

    public void testUpdateManageSynapse() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();

    }
}
