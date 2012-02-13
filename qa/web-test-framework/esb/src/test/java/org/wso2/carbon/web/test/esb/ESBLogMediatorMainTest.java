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
import org.wso2.carbon.web.test.client.ESBSampleClient;


public class ESBLogMediatorMainTest extends CommonSetup{

    public ESBLogMediatorMainTest(String text){
        super(text);
    }

    public void testAddLogMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_log_mediator");

        //verify adding a Log mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Core","Log");

        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Log"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Log mediator
        selenium.click("//a[@id='mediator-0']");
		assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
		assertEquals("Log Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
		assertEquals("Log level", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]"));
		assertEquals("Log Separator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[1]"));
		assertEquals("Properties of the Log mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[3]/td/h3"));
		assertEquals("Add Property", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[4]/td"));

        //verify Click on 'Add Sibling' of the Log mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Log");

        //verify Click on the 'Delete' icon of the 'Log mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Log"));
    }

    public void testDeleteLogMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_Log_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbCommon.mediatorUpdate();
        esbCommon.addMediators("Add Sibling","0","Core","Drop");
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_Log_mediator");
        //Click on the 'Delete' icon of the 'Log mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        esbCommon.viewSequences();
        assertTrue(selenium.isTextPresent("test_Log_mediator"));

        //verify Log mediator has successfully deleted from the sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_Log_mediator");
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Log"));

        //delete the sequnce from sequnce list
        esbCommon.deleteSequence("test_Log_mediator");
    }




    public void testLogLevelSimple() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Log Level' as 'Simple' and update the mediator
        verifyLogLevel("test_logLevel_Simple","Simple");
    }

    public void testLogLevelFull() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Log Level' as 'Full' and update the mediator
        verifyLogLevel("test_logLevel_Full","Full");
    }

    public void testlogLevelCustom() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Log Level' as 'Custom' and update the mediator
        verifyLogLevel("test_logLevel_Custom","Custom");
    }

    public void testLogLevelHeaders() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Log Level' as 'Headers' and update the mediator
        verifyLogLevel("test_logLevel_Headers","Headers");
    }

    public void testLogSeperatorComma() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Set  the 'Log Separator' to a different symbol other than a comma and update the mediator
        verifyChangeLogSeperator("test_logSeperator",",");
    }

    public void testAddLogSeperatorSymbol() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Set  the 'Log Separator' to a different symbol other than a comma and update the mediator
        verifyChangeLogSeperator("test_logSeperator","=/");
    }

    public void testAddLogProperty() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Add new properties and update the mediator
        verifyAddLogProperty("test_log_property","Text","Value","An unexcepted error occure");
    }

    public void testRemoveProperty() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);
        esbCommon.addSequence("test_log_property");
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest.addLogPropety("Text","Value","An unexcepted error occure");
        esbCommon.mediatorUpdate();

        //Remove properties from the mediator and update the mediator
        Thread.sleep(2000);
        removeProperty("0","Text");
        //verify Sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_log_property\"> <syn:log /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

        //verify Mediator source view
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:log xmlns:syn=\"http://ws.apache.org/ns/synapse\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
    }

    public void testCreateSequenceLogLevelSimple() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Create a complete sequence with a Log mediator with 'Log Level' set to 'Simple'
        ESBSampleLogSequence log_seq=new ESBSampleLogSequence("");
        log_seq.createSequence("sequnce_log_simple");

        assertTrue(selenium.isTextPresent("sequnce_log_simple"));

        esbCommon.setupMainSeq();
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sequnce_log_simple");
        //Invoke the client
          checkLogMediatorInvocation("LogMediator To: http://www.w3.org/2005/08/addressing/anonymous, WSAction: , SOAPAction: , MessageID:");
    }

    //Create a complete sequence with a Log mediator with 'Log Level' set to 'Full'
    public void testCreateSequenceWithLogLevelFull() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Create a complete sequence with a Log mediator with 'Log Level' set to 'Full'
        editSequnceLogLevel("Full");
        //Invoke the client
        checkLogMediatorInvocation("LogMediator To: http://www.w3.org/2005/08/addressing/anonymous, WSAction: , SOAPAction: , MessageID:");
    }

    public void testCreateSequenceWithLogLevelCustom() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Create a complete sequence with a Log mediator with 'Log Level' set to 'Custom'
        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("sequnce_log_simple");
        esbLogMediatorTest.addLogMediator("1.0","Custom");
        esbLogMediatorTest.setPropNo();
        esbLogMediatorTest.addLogPropety("Text","Value","Custom");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //Invoke the client
        checkLogMediatorInvocation("LogMediator Text = Custom");
    }

    public void testCreateSequenceWithLogLevelHeaders() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Create a complete sequence with a Log mediator with 'Log Level' set to 'Headers'
        editSequnceLogLevel("Headers");
    }

    public void testSequenceWithDiffrentLogseperator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Create a complete sequence with a Log mediator with the 'Log Separator' set to a different symbol other than a comma
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("sequnce_log_simple");

        selenium.click("link=Log");
        Thread.sleep(2000);
        selenium.type("mediator.log.log_separator","~");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
        assertTrue(selenium.isTextPresent("sequnce_log_simple"));

        //Invoke client
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        boolean stockQuoteResponse=esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot(), null, "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
    }

    public void testCreateSequenceWithLogPropertyValue() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.viewSequences();
        //Create a complete sequence with a Log mediator with properties added of type 'value'
        createSeqWithLogProperty("sequnce_log_simple","Text","Value","An unexpected error occure",null,null);

    }

    public void testCreateSequenceWithLogPropertyExpression() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.viewSequences();
        //Create a complete sequence with a Log mediator with properties added of type 'expression'
        createSeqWithLogProperty("sequnce_log_simple","MessageID","Expression","get-property('MessageID')",null,null);

        //Invoke client
        checkLogMediatorInvocation("LogMediator MessageID = urn:uuid:");
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

    
    public void verifyLogLevel(String seqName,String logLevel) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);

        esbCommon.addSequence(seqName);

        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest.addLogMediator("0",logLevel);
        esbCommon.mediatorUpdate();

        //verify sequence source view
        esbCommon.clickSequenceSource("0");
        if(logLevel.equalsIgnoreCase("Simple")){
            assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:log /> </syn:sequence>", selenium.getText("sequence_source"));
        }
        else{
            logLevel=logLevel.toLowerCase();
            assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:log level=\""+logLevel+"\" /> </syn:sequence>", selenium.getText("sequence_source"));
        }
        esbCommon.clickDesignView();


        //verify the mediator source view
        esbCommon.clickMediatorSource("0");
        if(logLevel.equalsIgnoreCase("Simple")){
            assertEquals("<syn:log xmlns:syn=\"http://ws.apache.org/ns/synapse\" />", selenium.getText("mediatorSrc"));
        }
        else{
            assertEquals("<syn:log xmlns:syn=\"http://ws.apache.org/ns/synapse\" level=\""+logLevel+"\" />", selenium.getText("mediatorSrc"));
        }
        selenium.click("link=switch to design view");    
    }

    //Set  the 'Log Separator' to a different symbol other than a comma and update the mediator
    public void verifyChangeLogSeperator(String seqName,String logSeperator) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);

        esbCommon.addSequence(seqName);
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        selenium.click("//a[@id='mediator-0']");
        selenium.type("mediator.log.log_separator", "");
        selenium.type("mediator.log.log_separator", logSeperator);
        esbCommon.mediatorUpdate();

        //verify the mediator source view
        esbCommon.clickMediatorSource("0");
		assertEquals("<syn:log xmlns:syn=\"http://ws.apache.org/ns/synapse\" separator=\""+logSeperator+"\" />", selenium.getValue("mediatorSrc"));
        selenium.click("link=switch to design view");

       //verify sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:log separator=\""+logSeperator+"\" /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }

    //Add new properties and update the mediator
    public void verifyAddLogProperty(String seqName,String propertyName, String propertyTypeSel, String propertyValue) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);

        esbCommon.addSequence(seqName);
        esbCommon.addRootLevelChildren("Add Child","Core","Log");
        esbLogMediatorTest.addLogPropety(propertyName,propertyTypeSel,propertyValue);
        esbCommon.mediatorUpdate();

        //verify sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:log> <syn:property name=\""+propertyName+"\" value=\""+propertyValue+"\" /> </syn:log> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

        //verify the mediator source view
        esbCommon.clickMediatorSource("0");
		assertEquals("<syn:log xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:property name=\""+propertyName+"\" value=\""+propertyValue+"\" /> </syn:log>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
    }

    //Remove properties from the mediator and update the mediator
    public void removeProperty(String logMediatorLevel,String propertyName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);

        selenium.click("//a[@id='mediator-"+logMediatorLevel+"']");
        Thread.sleep(2000);
        //search the property
        String propertyValue="";
        int p_no=0;
        while(!propertyName.equals(propertyValue)){
            propertyValue=selenium.getValue("propertyName"+p_no);
            p_no=p_no+1;
        }
        p_no=p_no-1;

        selenium.click("//a[@id='mediator-"+logMediatorLevel+"']");
        Thread.sleep(2000);
        //delete the property
        selenium.click("//a[@onclick='deleteproperty("+p_no+");return false;']");

        //update the mediator
        esbCommon.mediatorUpdate();   
    }

    public void editSequnceLogLevel(String logLevel) throws Exception{
        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon=new ESBCommon(selenium);

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("sequnce_log_simple");
        esbLogMediatorTest.addLogMediator("1.0",logLevel);
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
        assertTrue(selenium.isTextPresent("sequnce_log_simple"));
    }

    //Create a complete sequence with a Log mediator with properties added of type 'value' and save the sequence
    public void createSeqWithLogProperty(String seqName,String propertyName, String propertyTypeSel, String propertyValue,String prefix, String uri) throws Exception{
        ESBLogMediatorTest esbLogMediatorTest=new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon=new ESBCommon(selenium);

        esbCommon.viewSequences();
        esbCommon.clickEditSeq(seqName);

        selenium.click("link=Log");
        Thread.sleep(2000);
        int pro_no=0;
        while(selenium.isElementPresent("propertyName"+pro_no)){
            selenium.click("//a[@onclick='deleteproperty("+pro_no+");return false;']");
            pro_no=pro_no+1;
        }
        esbCommon.mediatorUpdate();
        
        selenium.click("link=Log");
        Thread.sleep(2000);
        esbLogMediatorTest.addLogPropety(propertyName,propertyTypeSel,propertyValue);
        if(propertyTypeSel.equals("Expression") && prefix!=null && uri!=null){
             esbCommon.setNsLevel();
             esbLogMediatorTest.addLogExpressionNameSpaces(prefix,uri);
        }
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
        assertTrue(selenium.isTextPresent(seqName));
    }

    public void checkLogMediatorInvocation(String lookInLog) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        //Invoke the client
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        int log_before=esbCommon.checkLogFile("INFO "+lookInLog);
        boolean stockQuoteResponse=esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot(), null, "IBM");
        int log_after=esbCommon.checkLogFile("INFO "+lookInLog);
        if((log_after-log_before)==1 && stockQuoteResponse==true)
            System.out.println("Message successfully sent through the log mediator ..");
        else
            throw new MyCheckedException("Log mediator has not invoked correctly..");
    }
}
