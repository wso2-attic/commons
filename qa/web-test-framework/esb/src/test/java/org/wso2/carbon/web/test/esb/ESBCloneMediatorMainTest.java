package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBIterateClient;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.axiom.om.OMElement;

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

public class ESBCloneMediatorMainTest  extends CommonSetup{

    public ESBCloneMediatorMainTest(String text) {
        super(text);
    }

    public void testAddCloneMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_clone_mediator");
        //Add a Clone mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Clone");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Clone"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the Clone mediator
        selenium.click("//a[@id='mediator-0']");
        assertTrue(selenium.isTextPresent("Clone Mediator"));
		selenium.getSelectedValue("mediator.clone.continue").equals("No");
		assertEquals("Add Clone Target", selenium.getText("link=Add Clone Target"));
		assertTrue(selenium.isTextPresent("0 Clone Targets"));

        //Click on 'Add Sibling' of the Clone mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Clone");

        //Click on the link 'Add Clone Target'
        selenium.click("link=Add Clone Target");
		selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
		assertTrue(selenium.isTextPresent("1 Clone Target"));
        assertTrue(selenium.getText("//a[@id='mediator-0']").equals("Clone"));
        assertTrue(selenium.getText("//a[@id='mediator-0.0']").equals("Target"));

        //Click on the 'Delete' icon of the 'Clone mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Clone"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Target"));
    }

    public void testCreateProxy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSampleCloneProxy esbSampleCloneProxy=new ESBSampleCloneProxy("");
        esbSampleCloneProxy.createProxyService("test_clone_proxy");
    }

    public void testEditProxy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCloneMediatorTest esbCloneMediatorTest=new ESBCloneMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);

        esbAddProxyServiceTest.editProxyService("test_clone_proxy");

        selenium.click("nextBtn");
        selenium.click("inAnonAddEdit");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Clone");
        //Add 2nd Target to clone mediator
        esbCloneMediatorTest.addCloneTarget("0");
        //Adding a log mediator to 2nd target
        esbLogMediatorTest.setPropNo();
        esbCommon.addMediators("Add Child","0.1","Core","Log");
        esbLogMediatorTest.addLogMediator("0.1.0","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","This is the 2nd message");
        esbCommon.mediatorUpdate();
        //Adding the send mediator to 2nd target
        esbCommon.addMediators("Add Sibling","0.1.0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.1.1");
        esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,"http://localhost:9000/services/SimpleStockQuoteService");
        esbAddAddressEndpointTest.saveAddressEndpoint();
        esbCommon.mediatorUpdate();

        //Add Log mediator to Clone mediator
        esbLogMediatorTest.setPropNo();
        esbCommon.addMediators("Add Sibling","0","Core","Log");
        esbLogMediatorTest.addLogMediator("1","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","**Continue Parent Accept in Clone**");
        esbCommon.mediatorUpdate();
        //Adding a Drop mediator
        esbCommon.addMediators("Add Sibling","1","Core","Drop");
        esbCommon.sequenceSave();
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");
    }

    //Create a sequence with the Continue Parent set to No and invoke using a client
    public void testContinueParent_No() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
          esbCommon.logoutLogin();
        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("test_clone_proxy");
        //set continue parent=No
        selenium.click("nextBtn");
		selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Clone");
        Thread.sleep(2000);
		selenium.select("mediator.clone.continue", "label=No");
		selenium.click("//input[@value='Update']");
        Thread.sleep(2000);
        esbCommon.sequenceSave();
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");

        int log_before=esbCommon.checkLogFile("text = **Continue Parent Accept in Clone**");
        //Invoke the client
        ESBIterateClient esbIterateClient=new ESBIterateClient();
        if (esbCommon.getContextRoot().equals(null))
           esbIterateClient.iterateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/test_clone_proxy",null,"getQuote",1);
        else
            esbIterateClient.iterateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/test_clone_proxy", null,"getQuote",1);

        int log_after=esbCommon.checkLogFile("text = **Continue Parent Accept in Clone**");
        int log_staus=log_after-log_before;

        if(log_staus==0)
            System.out.println("Continue parent=False works..");
        else
            throw new MyCheckedException("Continue parent=False not working..");
    }

    public void testContinueParent_Yes() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("test_clone_proxy");
        //set continue parent=true
        selenium.click("nextBtn");
		selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Clone");
        Thread.sleep(2000);
		selenium.select("mediator.clone.continue", "label=Yes");
		selenium.click("//input[@value='Update']");
        Thread.sleep(2000);
        esbCommon.sequenceSave();
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");

        int log_before=esbCommon.checkLogFile("text = **Continue Parent Accept in Clone**");
        //Invoke the client
        ESBIterateClient esbIterateClient=new ESBIterateClient();
        if (esbCommon.getContextRoot().equals(null))
           esbIterateClient.iterateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/test_clone_proxy",null,"getQuote",1);
        else
            esbIterateClient.iterateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/test_clone_proxy", null,"getQuote",1);

        int log_after=esbCommon.checkLogFile("text = **Continue Parent Accept in Clone**");
        int log_staus=log_after-log_before;

        if(log_staus==1)
            System.out.println("Continue parent=true works..");
        else
            throw new MyCheckedException("Continue parent=true not working..");

    }

    public void testAddTwoCloneTargets() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        String res="";
        //Invoke the client
        ESBIterateClient esbIterateClient=new ESBIterateClient();
        int log_firstTarget_before=esbCommon.checkLogFile("text = This is the aggregated message");
        int log_secondTarget_before=esbCommon.checkLogFile("text = This is the 2nd message");

        if (esbCommon.getContextRoot().equals(null))
           res=esbIterateClient.iterateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/test_clone_proxy",null,"getQuote",1);
        else
            res=esbIterateClient.iterateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/test_clone_proxy", null,"getQuote",1);

        int log_firstTarget_after=esbCommon.checkLogFile("text = This is the aggregated message");
        int log_secondTarget_after=esbCommon.checkLogFile("text = This is the 2nd message");

        int log_staus_firstTarget=log_firstTarget_after-log_firstTarget_before;
        int log_staus_secondTarget=log_secondTarget_after-log_secondTarget_before;

        if(log_staus_firstTarget==1 && log_staus_secondTarget==1)
            System.out.println("The message cloned correctly and one message directed to the specified two targets..");
        else
            throw new MyCheckedException("The message not directed to the two specified targets...");

        //verify response aggregated corectly
        ESBIterateMediatorMainTest esbIterateMediatorMainTest=new ESBIterateMediatorMainTest("");
        esbIterateMediatorMainTest.checkResponse(res,2,"Target sequence picked from the registry");

    }

    public void testUpdateTargetMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBCloneMediatorTest esbCloneMediatorTest=new ESBCloneMediatorTest(selenium);
        esbCommon.addSequence("test_Clone");
        //Adding the Clone mediator
        esbCommon.addRootLevelChildren("Add Child","Advanced","Clone");
        esbCloneMediatorTest.addCloneMediator("0","No");
        esbCloneMediatorTest.addCloneTarget("0");
        selenium.click("//a[@id='mediator-0.0']");
        esbCommon.mediatorUpdate();

        assertEquals("Please select a valid sequence or a valid endpoint", selenium.getText("messagebox-warning"));
		selenium.click("//button[@type='button']");
    }

    public void testCloneMediatorInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBCloneMediatorTest esbCloneMediatorTest=new ESBCloneMediatorTest(selenium);
        esbCommon.addSequence("test_clone");
        //Adding the Clone mediator
        esbCommon.addRootLevelChildren("Add Child","Advanced","Clone");
        esbCloneMediatorTest.addCloneMediator("0","Yes");
        esbCloneMediatorTest.addCloneTarget("0");
       // esbCommon.mediatorUpdate();

        //Adding the target mediator infomation
        esbCloneMediatorTest.addTargetMediator("0.0","urn:getQuote","", "anonymous", "none", null, null);
        esbCommon.mediatorUpdate();
        selenium.click("link=Target");
        Thread.sleep(2000);
        //Adding the send mediator
        esbCommon.addMediators("Add Child","0.0","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //verify sequence infomation in edit mode
        esbCommon.clickEditSeq("test_clone");
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_clone\"> <syn:clone> <syn:target to=\"\" soapAction=\"urn:getQuote\"> <syn:sequence> <syn:send /> </syn:sequence> </syn:target> </syn:clone> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }
}
