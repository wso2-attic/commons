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


public class ESBEventMediatorMainTest extends CommonSetup{
     public ESBEventMediatorMainTest(String text){
        super(text);
    }

    public void testAddEventMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        String event_sources=getEventSources();
        esbCommon.addSequence("test_event_mediator");
        //verify adding a Event mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Core","Event");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Event"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Event mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
        assertEquals("Event Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        assertEquals("Event Source Select an Event Source "+event_sources, selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td"));

        //verify Click on 'Add Sibling' of the Event mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Event");

        //verify Click on the 'Delete' icon of the 'Log mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Event"));
    }

    public void testUpdateMediatorWithOutEventsorce() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_event_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Event");

        //Update the mediator without selecting any event sources
        selenium.click("//input[@value='Update']");
		assertEquals("Please select a valid Event Source", selenium.getText("messagebox-warning"));
		selenium.click("//button[@type='button']");
    }

    public void testSelectEventSource() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_event_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Event");

        //verify Selecting an Event source from the list and update
        addEventMediatorWithEventSource("test_select_event_source","test_EventSource");
    }

    public void testCreateSequnce() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        
        //Create a complete sequence with a Event mediator and save the sequence
        ESBEventSourceTest esbEventSourceTest=new ESBEventSourceTest("");
        esbEventSourceTest.addDefaultEventSource("SampleEventSource","Topic","http://apache.org/aip");

        esbCommon.addSequence("sequence_event");
        //Adding the Log mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);
        esbLogMediatorTest.addLogMediator("0","Full");
        esbCommon.mediatorUpdate();

        //Adding the Event mediator
        ESBEventMediatorTest esbEventMediatorTest=new ESBEventMediatorTest(selenium);
        esbCommon.addRootLevelChildren("Add Sibling","Core","Event");
        esbEventMediatorTest.addEventMediator("1","SampleEventSource");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
        assertTrue(selenium.isTextPresent("sequence_event"));
    }

    public void testEditsequence() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(selenium.isTextPresent("sequence_event")){
            //Do changes to the sequence, save the sequence and view in the edit mode
            esbCommon.editSeqMediator("sequence_event","Log",null);
            Thread.sleep(2000);
            selenium.select("mediator.log.log_level", "label=Custom");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            //verify changes
            esbCommon.clickEditSeq("sequence_event");
            selenium.click("link=Log");
            Thread.sleep(2000);
            selenium.getSelectedValue("mediator.log.log_level").equals("Custom");    
        }
        else
            System.out.println("sequnce \"sequence_event\" not found");
    }

    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }

    /*
    This method is used to take the names of all event sources currently available in Event source list
     */
    public String getEventSources() throws Exception{
        selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");

        String eventSource_name="";
        if(!selenium.isTextPresent("No Event Sources are currently defined.")){
            int row_no=1;
            while(selenium.isElementPresent("//table[@id='eventsourcestab']/tbody/tr["+row_no+"]/td[1]")){
                eventSource_name=eventSource_name+selenium.getText("//table[@id='eventsourcestab']/tbody/tr["+row_no+"]/td[1]")+" ";
                row_no=row_no+1;
            }
            eventSource_name=eventSource_name.substring(0,eventSource_name.length()-1);
            return  eventSource_name;
        }
        return  eventSource_name;
    }

     //Select an Event source from the list and update
    public void addEventMediatorWithEventSource(String seqName,String eventSourceName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBEventSourceTest esbEventSourceTest=new ESBEventSourceTest("");
        ESBEventMediatorTest esbEventMediatorTest=new ESBEventMediatorTest(selenium);

        //Select an Event source from the list and update
        esbEventSourceTest.addDefaultEventSource(eventSourceName,"Topic","http://apache.org/aip");
        esbCommon.addSequence(seqName);
        esbCommon.addRootLevelChildren("Add Child","Core","Event");
        esbEventMediatorTest.addEventMediator("0",eventSourceName);
        esbCommon.mediatorUpdate();

        //verify mediator source view
        esbCommon.clickMediatorSource("0");
		assertEquals("<syn:eventPublisher xmlns:syn=\"http://ws.apache.org/ns/synapse\" eventSourceName=\""+eventSourceName+"\" />", selenium.getText("mediatorSrc"));
		selenium.click("link=switch to design view");

        //verify sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:eventPublisher eventSourceName=\""+eventSourceName+"\" /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
        //verify Switch back to the design view
        selenium.click("link=Event");
        Thread.sleep(2000);
        selenium.getSelectedValue("mediator.event.source").equals(eventSourceName);
    }
}
