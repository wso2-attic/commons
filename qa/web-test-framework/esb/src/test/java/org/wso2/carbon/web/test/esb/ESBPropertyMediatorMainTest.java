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


public class ESBPropertyMediatorMainTest extends CommonSetup{

    public ESBPropertyMediatorMainTest(String text){
        super(text);
    }

    public void testApplyDebugMode() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.applyDebugMode();
    }

    public void testAddPropertyMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_property_mediator");

        //verify adding a Property mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Core","Property");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Property"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Property mediator
        selenium.click("//a[@id='mediator-0']");
		assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
		assertEquals("Property Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));

        //verify Click on 'Add Sibling' of the Property mediator
        esbCommon.verifyClickAddSibling();

        //verify Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Property");

        //verify Click on the 'Delete' icon of the 'Property mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Property"));
    }


   public void testPropertyMediatorInfo() throws Exception{
        //Switch back to the design view
        //The information of the mediator should be displayed without the data being lost/unchanged
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Adding a property mediator
        addPropertyMediator("test_property_mediator","property","Set","Value","message/@type","Synapse");

        //get property mediator infomation
        String propertyName="",action="",setActionAs="",value="",scope="";
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(5000);

        propertyName=selenium.getValue("mediator.property.name");
        action=selenium.getValue("set");
        if(action.equals("on")){
            setActionAs=selenium.getValue("value");
            value=selenium.getValue("mediator.property.val_ex");
        }
        scope=selenium.getSelectedValue("mediator.property.scope");

        //Switch to source view
        esbCommon.clickMediatorSource("0");
        //Switch back to design view
        selenium.click("link=switch to design view");

        //The information of the mediator should be displayed without the data being lost/unchanged
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertEquals(propertyName, selenium.getValue("mediator.property.name"));
        assertEquals(action, selenium.getValue("set"));
        assertEquals(setActionAs, selenium.getValue("value"));
        assertEquals(value, selenium.getValue("mediator.property.val_ex"));
        assertEquals(scope, selenium.getValue("mediator.property.scope"));
}

//    public void testSelectActionSet() throws Exception{
//        //Select the 'Action' as 'Set' and update the mediator
//        verifySourceViewAction_Remove("test_Action_set","property1","set","Synapse");
//    }

    public void testSelectActionRemove() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Action' as 'Remove' and update the mediator
        selectActionRemove("test_Action_Remove","property1","remove","Synapse");
    }

    public void testSelectSetAsActionValue() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Set as action' as 'Value' and update the mediator
        selectSetAsAction("test_setAsAction_value","property2","Value","message/@type","Synapse");
    }

    public void testSelectSetAsActionExpression() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the 'Set as action' as 'Expression' and update the mediator
        selectSetAsAction("test_setAction_Expression","property3","Expression","message/@type","Synapse");
    }

    public void testSelectScope() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Select the different values of 'Scope' and update the mediator
        selectScope("test_scope","property4","Synapse");
        selectScope("test_scope","property5","Transport");
        selectScope("test_scope","property6","Axis2");
    }


//        //Create a complete sequence with a Property mediator with the
//          "'Action' set to 'Set'" and
//          "'Set Action as' set to 'Value'" and
//          "'Scope' set to 'Synapse'"
    public void testCreateSequenceWithActionSet() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBSample3Test esbSample3Test=new ESBSample3Test("");
        esbSample3Test.createEndpoint();
        esbSample3Test.createLocalEntry();
        esbSample3Test.createSequence("sequence_property1");
        assertTrue(esbCommon.searchSequenceInList("sequence_property1"));

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sequence_property1");
        //Invoke the client & verify Property mediator has invoked correcty..
        checkPropertyMediatorInvocation("PropertyMediator Setting property : direction at scope : default to : incoming");
    }

    //Create a complete sequence with a Property mediator with the 'Set Action as' set to 'Expression'.
    public void testEditSequenceSetAsActionToExpression() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("sequence_property1");
        selenium.click("link=Property");
        Thread.sleep(2000);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        esbPropertyMediatorTest.addBasicPropInfo("0.0","Format","set");
        esbPropertyMediatorTest.addPropertyMediator("expression","get-property('MESSAGE_FORMAT')","Synapse");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
        assertTrue(esbCommon.searchSequenceInList("sequence_property1"));

        //Invoke the client & verify Property mediator has invoked correcty..
        checkPropertyMediatorInvocation("PropertyMediator Setting property : Format at scope : default to : soap11 (i.e. result of expression : get-property('MESSAGE_FORMAT'))");
    }

    public void testCreateSequenceWithActionRemove() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        //Create a complete sequence with a Property mediator with the 'Action' set to 'Remove' and save the sequence
        esbCommon.clickEditSeq("sequence_property1");
        selenium.click("link=Property");
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        esbPropertyMediatorTest.addBasicPropInfo("0.0","direction","Remove");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();

        assertTrue(esbCommon.searchSequenceInList("sequence_property1"));
        //verify changes done to the mediator
        esbCommon.clickEditSeq("sequence_property1");
        selenium.click("link=Property");
        esbCommon.clickMediatorSource("0.0");
        assertEquals("<syn:property xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"direction\" scope=\"default\" action=\"remove\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //Invoke the client & verify Property mediator has invoked correcty..
        checkPropertyMediatorInvocation("PropertyMediator Removing property : direction (scope:default)");
    }


    public void testEditSequenceScopeToTransport() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
       //Create a complete sequence with a Property mediator with the 'Scope' set to 'Transport' and save the sequence
       editSequenceScope("sequence_property1","Transport");

       //edit the "stockquote" sequence log mediator
       esbCommon.editSeqMediator("stockquote","Log",null);
       Thread.sleep(2000);
       selenium.type("propertyName2", "direction");
       selenium.type("propertyValue2", "get-property('direction')");
       esbCommon.mediatorUpdate();
       esbCommon.sequenceSave();

       //verify proprties has applied to message correctly
       checkPropertyMediatorInvocation("LogMediator Text = Sending, version = 0.1, direction = null");

    }

    public void testEditSequenceScopeToAxis2() throws Exception{
       ESBCommon esbCommon=new ESBCommon(selenium);
       esbCommon.logoutLogin();

      //Create a complete sequence with a Property mediator with the 'Scope' set to 'Axis2' and save the sequence
      editSequenceScope("sequence_property1","Axis2");

      //verify proprties has applied to message correctly
       checkPropertyMediatorInvocation("LogMediator Text = Sending, version = 0.1, direction = null");    
   }


    public void addPropertyMediator(String seqName,String propertyName,String action,String actionAsVal, String propertyValue, String scope) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);

        esbCommon.addSequence(seqName);
        esbCommon.addRootLevelChildren("Add Child","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0",propertyName,action);
        esbPropertyMediatorTest.addPropertyMediator(actionAsVal,propertyValue,scope);
        esbCommon.mediatorUpdate();
    }

    //This method is used to select the 'Action' type Remove and verify the mediator & sequence source views
    public void selectActionRemove(String seqName,String propertyName,String action,String scope) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        addPropertyMediator(seqName,propertyName,action,null,null,scope);

        //verify Mediator source view
        action=action.toLowerCase();
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:property xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+propertyName+"\" scope=\"default\" action=\""+action+"\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //verify Sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:property name=\""+propertyName+"\" scope=\"default\" action=\""+action+"\" /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }

    public void selectSetAsAction(String seqName,String propertyName,String actionAsVal, String propertyValue, String scope) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);

        addPropertyMediator(seqName,propertyName,"Set",actionAsVal,propertyValue,scope);
        //verify Mediator source view
        esbCommon.clickMediatorSource("0");
        //verify source view when 'Set as action' as 'Value' or 'Expression'
        if(actionAsVal!=null){
            if (actionAsVal.equalsIgnoreCase("value")){
                assertEquals("<syn:property xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+propertyName+"\" value=\""+propertyValue+"\" scope=\"default\" />", selenium.getText("mediatorSrc"));
            }
            else if(actionAsVal.equalsIgnoreCase("Expression")){
                assertEquals("<syn:property xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+propertyName+"\" expression=\""+propertyValue+"\" scope=\"default\" />", selenium.getText("mediatorSrc"));
            }
            selenium.click("link=switch to design view");

            //verify Sequence source view
            esbCommon.clickSequenceSource("0");
             if(actionAsVal.equalsIgnoreCase("value")){
                assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:property name=\""+propertyName+"\" value=\""+propertyValue+"\" scope=\"default\" /> </syn:sequence>", selenium.getText("sequence_source"));
            }
            else if(actionAsVal.equalsIgnoreCase("Expression")){
                assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:property name=\""+propertyName+"\" expression=\""+propertyValue+"\" scope=\"default\" /> </syn:sequence>", selenium.getText("sequence_source"));
            }
            esbCommon.clickDesignView();
       }
    }

    public void selectScope(String seqName,String propertyName,String scope) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        addPropertyMediator(seqName,propertyName,"Remove",null,null,scope);

        //verify Mediator source view
        scope=scope.toLowerCase();
        esbCommon.clickMediatorSource("0");
        if(scope.equalsIgnoreCase("Synapse"))
            assertEquals("<syn:property xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+propertyName+"\" scope=\"default\" action=\"remove\" />", selenium.getText("mediatorSrc"));
        else
            assertEquals("<syn:property xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+propertyName+"\" scope=\""+scope+"\" action=\"remove\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        //verify Sequence source view
        esbCommon.clickSequenceSource("0");
        if(scope.equalsIgnoreCase("Synapse"))
            assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:property name=\""+propertyName+"\" scope=\"default\" action=\"remove\" /> </syn:sequence>", selenium.getText("sequence_source"));
        else
            assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:property name=\""+propertyName+"\" scope=\""+scope+"\" action=\"remove\" /> </syn:sequence>", selenium.getText("sequence_source"));
    }

    public void editSequenceScope(String seqName,String scope) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.viewSequences();
        esbCommon.clickEditSeq(seqName);

        selenium.click("link=Property");
        Thread.sleep(2000);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        if(scope.equalsIgnoreCase("Transport"))
            esbPropertyMediatorTest.addPropertyMediator("","","Transport");
        else if(scope.equalsIgnoreCase("Axis2"))
            esbPropertyMediatorTest.addPropertyMediator("","","Axis2");
        esbPropertyMediatorTest.addBasicPropInfo("0.0","direction","Remove");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
        assertTrue(esbCommon.searchSequenceInList(seqName));
    }

    public void checkPropertyMediatorInvocation(String lookInLog) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();

        int log_before=esbCommon.checkLogFile(lookInLog);
        boolean stockQuoteResponse=esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot(), null, "IBM");
        int log_after=esbCommon.checkLogFile(lookInLog);
        if((log_after-log_before)==1 && stockQuoteResponse==true)
            System.out.println("Message successfully sent through the log mediator ..");
        else
            throw new MyCheckedException("Log mediator has not invoked correctly..");
    }
 }
    

