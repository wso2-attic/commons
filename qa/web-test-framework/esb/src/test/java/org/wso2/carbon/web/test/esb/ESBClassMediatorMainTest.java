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

public class ESBClassMediatorMainTest  extends CommonSetup{

    public ESBClassMediatorMainTest(String text) {
        super(text);
    }

    public void testAddClassMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        verifyAddClassMediator("Test_addClassMediator","samples.mediators.DiscountQuoteMediator","5","10");    
    }

    public void testUpdateWithOutRequiredFields() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //verify Click on the 'Update' button without filling the required fields
        esbCommon.addSequence("Test_incorrect_updt");
        esbCommon.addRootLevelChildren("Add Child","Extension","Class");
        Thread.sleep(2000);
        selenium.click("//input[@value='Update']");
        assertTrue(selenium.isElementPresent("messagebox-error"));
        assertTrue(selenium.isTextPresent("Required fields can not be left blank"));
        selenium.click("link=X");
    }

    public void testCreateSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("sequence_class");
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbCommon.addMediators("Add Child","0","Extension","Class");
        specifyClassPath("samples.mediators.DiscountQuoteMediator","5","10");
        esbCommon.mediatorUpdate();
        esbCommon.addMediators("Add Sibling","0.0","Core","Send");
        esbCommon.addMediators("Add Sibling","0","Filter","Out");
        esbCommon.sequenceSave();
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("sequence_class"));
    }

//    public void testSequenceInfo() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.logoutLogin();
//        //verify click edit sequence
//        esbCommon.viewSequences();
//        if(selenium.isTextPresent("sequence_class")){
//            esbCommon.clickEditSeq("sequence_class");
//            //assertEquals("Root Add Child \n \n \n \n \nClass\n\n \nIn\n\n\nSend\n \nOut", selenium.getText("treePane"));
//
//            //verify Manage Synapse
//            selenium.click("link=Class");
//            esbCommon.clickSequenceSource("0");
//            String seq_source = selenium.getText("sequence_source");
//            String temp1=seq_source.substring(seq_source.indexOf("name"));
//            String temp2="<syn:sequence "+temp1;
//            temp2=temp2.replaceAll(" />","/>");
//            esbCommon.clickSynapse();
//            boolean status=esbCommon.verifyManageSynapseConfig(temp2);
//            if(!status)
//               throw new MyCheckedException("Sequence information incorrect in the  ManageSynapseConfig...!");
//        }
//        else
//            System.out.println("sequence \"sequence_class\" not found..!");
//    }

    public void testEditSequence() throws Exception{
        //Do changes to the sequence, save the sequence and view in the edit mode
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.viewSequences();
        if(selenium.isTextPresent("sequence_class")){
            esbCommon.editSeqMediator("sequence_class","In",null);
            esbCommon.addMediators("Add Sibling","1","Core","Send");
            esbCommon.sequenceSave();

            esbCommon.clickEditSeq("sequence_class");
            //assertEquals("Root Add Child \n \n \n \n \nIn\n\n\nClass\n\n\nSend\n \nOut\n\n \nLog", selenium.getText("treePane"));
        }
        else
            System.out.println("sequence \"sequence_class\" not found..!");
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

    public void verifyAddClassMediator(String seqName,String className,String param1, String param2) throws Exception{
         ESBCommon esbCommon = new ESBCommon(selenium);
         //verify adding a class mediator to root level
         esbCommon.addSequence(seqName);
         esbCommon.addRootLevelChildren("Add Child","Extension","Class");

         assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Class"));
         assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
         assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
         assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

         //verify click on class mediator
         selenium.click("//a[@id='mediator-0']");
         assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
         assertEquals("Class Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
         assertTrue(selenium.isTextPresent("exact:Class Name*"));
         assertEquals("", selenium.getValue("mediatorInputId"));

         //verify click on add sibling
         esbCommon.verifyClickAddSibling();

         //verify click on help
         //esbCommon.mediatorHelp("Class");

         //verify Click on the 'Load Class' button without the jar placed in the proper place
         selenium.click("actionID");
         Thread.sleep(2000);
         assertTrue(selenium.isElementPresent("messagebox-error"));
         assertEquals("Class not found in the path", selenium.getText("//div[@id='messagebox-error']/p"));
         //selenium.click("//button[@type='button']");
         selenium.click("link=X");

         //verify Place the jars in the classpath, specify the proper class name and click on 'Load Class'
         specifyClassPath(className,param1,param2);

         //Specify correct values and switch to the source view of the mediator
         selenium.click("//a[@onclick='showSource()']");
         Thread.sleep(2000);
         //assertEquals("<syn:class xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"samples.mediators.DiscountQuoteMediator\"> <axis2ns15:property xmlns:axis2ns15=\"http://ws.apache.org/ns/synapse\" name=\"bonusFor\" value=\""+param2+"\" /> <axis2ns16:property xmlns:axis2ns16=\"http://ws.apache.org/ns/synapse\" name=\"discountFactor\" value=\""+param1+"\" /> </syn:class>", selenium.getText("mediatorSrc"));
         selenium.click("link=switch to design view");

         //verify click on delete
         esbCommon.delMediator("0");
       }

       public void specifyClassPath(String className,String param1, String param2) throws Exception{
         selenium.type("mediatorInputId",className);
         selenium.click("actionID");
         Thread.sleep(4000);
         assertEquals("Class Loaded Successfully", selenium.getText("//div[@id='messagebox-info']/p"));
         selenium.click("//button[@type='button']");

         assertEquals("Properties defined for Class mediator", selenium.getText("propertyLabel"));
         assertEquals("discountFactor", selenium.getText("//table[@id='propertytable']/tbody/tr[1]/td[1]"));
         assertEquals("bonusFor", selenium.getText("//table[@id='propertytable']/tbody/tr[2]/td[1]"));

         selenium.type("propertyValue0",param1);
         selenium.type("propertyValue1",param2);
       }
}




