package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;
import java.io.*;

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

public class ESBScriptMediatorMainTest extends CommonSetup{

    public ESBScriptMediatorMainTest(String text) {
        super(text);
    }

    //Add a script mediator to the root level
    public void testAddScriptMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        addScriptMediator("test_script_mediator");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Script"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        esbCommon.addRootLevelChildren("Add Sibling","Filter","In");
        assertTrue(selenium.isElementPresent("//a[@id='mediator-0']"));
        assertTrue(selenium.isElementPresent("//a[@id='mediator-1']"));

        //verify Script TYpe & Language combo boxes
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Inline Registry Key"));
        assertTrue(selenium.isTextPresent("Javascript Ruby Groovy"));

        //Select the Script type as 'Registry Key'
        selenium.select("script_type", "label=Registry Key");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("exact:Function*"));
        assertTrue(selenium.isTextPresent("exact:Key*"));
        assertTrue(selenium.isTextPresent("Include keys"));
        assertEquals("Add include key", selenium.getText("link=Add include key"));

        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        //verify Select the combo box option 'Inline'
        selenium.select("script_type", "label=Inline");
        assertTrue(selenium.isTextPresent("Script Type"));
        assertTrue(selenium.isTextPresent("exact:Language*"));
        assertTrue(selenium.isTextPresent("exact:Source Script*"));


        //delete script mediator
        esbCommon.delMediator("0");
    }


    public void testClickOnScriptMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addScriptMediator("test_script_mediator");
        //verify click on script mediator
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Script Mediator"));
        assertTrue(selenium.isTextPresent("exact:Source Script*"));
        assertEquals("var sample", selenium.getText("mediator.script.source_script"));
        selenium.click("//a[@onclick='showSource()']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("<syn:script xmlns:syn=\"http://ws.apache.org/ns/synapse\" language=\"js\"> <![CDATA[var sample]]></syn:script>"));
        selenium.click("link=switch to design view");
    }

    public void testClickAddSiblingOnScriptMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        addScriptMediator("test_script_mediator");
        //verify add sibling
        esbCommon.verifyClickAddSibling();
    }

//    public void testScriptMediatorHelp() throws Exception{
//        ESBCommon esbCommon=new ESBCommon(selenium);
//        esbCommon.logoutLogin();
//        addScriptMediator("test_script_mediator");
//        //verify click on help
//        esbCommon.mediatorHelp("Script");
//    }

    public void testClickOnRegistryKey() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //verify Click on the 'Registry Key' link of the Script mediator
        String localRegList=esbCommon.getLocalRegItems();
        String embeddedRegItem[]=new String[100];
        embeddedRegItem=esbCommon.getEmbeddedRegItems();

        addScriptMediator("test_script_mediator");
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.select("script_type", "label=Registry Key");
        selenium.click("link=Registry Key");
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

    public void testSelectResourceFromRegistry() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBScriptMediatorTest esbScriptMediatorTest=new ESBScriptMediatorTest(selenium);
        //Adding the local entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("stockquoteScript_js","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.js");

        addScriptMediator("test_script_mediator");
        //Add reqistry key
        esbScriptMediatorTest.addRegistryScripts("0","Javascript","mediate","stockquoteScript_js");
        esbCommon.mediatorUpdate();
    }

    public void testAddIncludeKey() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBScriptMediatorTest esbScriptMediatorTest=new ESBScriptMediatorTest(selenium);
        addScriptMediator("test_script_mediator");
        //Add reqistry key
        esbScriptMediatorTest.addRegistryScripts("0","Javascript","transformRequest","stockquoteScript_js");
        esbCommon.mediatorUpdate();

        selenium.click("//a[@id='mediator-0']");
        //verify Click on the 'Add Include Keys' link
        selenium.click("link=Add include key");
        assertEquals("Key", selenium.getTable("includeKeytable.0.0"));
        assertEquals("", selenium.getTable("includeKeytable.0.1"));
        assertEquals("Registry Keys", selenium.getTable("includeKeytable.0.2"));
        assertEquals("Delete", selenium.getTable("includeKeytable.0.3"));

        selenium.click("//a[@onclick='deleteIncludeKey(0)']");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");
        //Select a key from the registry as the include key and update the mediator
        esbScriptMediatorTest.addIncludeKeys("stockquoteScript_js");
        esbCommon.mediatorUpdate();

        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:script xmlns:syn=\"http://ws.apache.org/ns/synapse\" language=\"js\" key=\"stockquoteScript_js\" function=\"transformRequest\"> <syn:include key=\"stockquoteScript_js\" /> </syn:script>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
        Thread.sleep(2000);

        //verify the sequence source view
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_script_mediator\"> <syn:script language=\"js\" key=\"stockquoteScript_js\" function=\"transformRequest\"> <syn:include key=\"stockquoteScript_js\" /> </syn:script> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

        //Click on 'Delete' of an include key
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        //selenium.click("//a[@onclick='deleteIncludeKey(1)']");
        selenium.click("//a[@onclick=\"deleteIncludeKey('0')\"]");
        esbCommon.mediatorUpdate();
        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:script xmlns:syn=\"http://ws.apache.org/ns/synapse\" language=\"js\" key=\"stockquoteScript_js\" function=\"transformRequest\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
        Thread.sleep(2000);
    }


    public void testAddJsInlineScriptMediator() throws Exception{
        // Verify adding a inline type Script mediator by specifying a Java script in the Source script text box
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("TestJsSoruce");
        esbCommon.addRootLevelChildren("Add Child","Extension","Script");
        verifyAddSourceScript("TestJsSoruce","Javascript",".."+File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"requestJavaScript.js");
    }

    public void testAddRbInlineScriptMediator() throws Exception{
        //verify adding a inline type Script mediator by specifying a Ruby script in the Source script text box
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("TestRbSoruce");
        esbCommon.addRootLevelChildren("Add Child","Extension","Script");
        verifyAddSourceScript("TestRbSoruce","Ruby",".."+File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"requestRubyScript.rb");
    }

    public void testAddGvInlineScriptMediator() throws Exception{
        //verify adding a inline type Script mediator by specifying a Groovy script in the Source script text box
             /*
             script for groovy
              */
    }

    public void testAddJsRegistryScriptMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //verify adding a Script mediator with the script selected as a registry key and also by specifying a Java script in the Source script text box
        verifyAddRegKey("Test_script","Javascript","mediator","Sequence","main");
    }

    public void testAddRbregistryScriptMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Add a Script mediator with the script selected as a registry key and also by specifying a Ruby script in the Source script text box
        verifyAddRegKey("Test_script_rb","Ruby","mediation","Sequence","fault");
    }

    public void testAddGvRegistryScriptMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Add a Script mediator with the script selected as a registry key and also by specifying a Groovy script in the Source script text box
        verifyAddRegKey("Test_script_groovy","Groovy","mediation","Sequence","main");
    }

    public void testCreateSeq() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //verify Create a complete sequence with a Script mediator and save the sequence
        ESBSample350Test esbSample350Test=new ESBSample350Test("");
        esbSample350Test.addScriptEntry();
        esbSample350Test.createSequence("seq_Script");

        selenium.click("link=Sequences");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("seq_Script"));
    }

    public void testEditSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Do changes to the sequence, save the sequence and view in the edit mode
        esbCommon.viewSequences();
        if(selenium.isTextPresent("seq_Script")){
            esbCommon.editSeqMediator("seq_Script","Out",null);
            esbCommon.addRootLevelChildren("Add Sibling","Core","Drop");
            esbCommon.sequenceSave();

    //           selenium.click("link=Sequences");
    //           selenium.waitForPageToLoad("30000");
    //           selenium.click("//a[@onclick=\"editSequence('sample_350_')\"]");
    //           selenium.waitForPageToLoad("30000");
    //           assertEquals("Root Add Child \n \n \n \n \nIn\n\n\nScript\n\n\nSend\n \nOut\n\n\nScript\n\n\nSend\n \nDrop", selenium.getText("treePane"));
        }
        else
            System.out.println("sequence \"seq_Script\" not found..!");
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


    //Add source script
    public void verifyAddSourceScript(String seqName,String language,String scriptFilePath) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        String srcScript=readFile(scriptFilePath);

        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.select("mediator.script.language", "label="+language);
        selenium.type("mediator.script.source_script", srcScript);
        Thread.sleep(2000);
        esbCommon.mediatorUpdate();
        
        if(language.equalsIgnoreCase("Javascript"))
            language="js";
        else if(language.equalsIgnoreCase("Ruby"))
             language="rb";
        else if(language.equalsIgnoreCase("Groovy"))
             language="groovy";
        else
             //do nothing


        //verify "Sequence source"
        selenium.click("//a[@id='mediator-0']");
        esbCommon.clickSequenceSource("0");
        assertTrue(selenium.isTextPresent("exact:<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:script language=\""+language+"\"> <![CDATA["+srcScript+"]]> </syn:script> </syn:sequence>"));
        esbCommon.clickDesignView();

        //verify "Mediator source"
        esbCommon.clickMediatorSource("0");
        assertTrue(selenium.isTextPresent("exact:<syn:script xmlns:syn=\"http://ws.apache.org/ns/synapse\" language=\""+language+"\"> <![CDATA["+srcScript+"]]> </syn:script>"));
      }


        /*
       This method is used to read the script file
        */
       public String readFile(String filePath) throws Exception{
          String conCatLines="";
          try{
             // Open the file
             // FileInputStream fstream = new FileInputStream("/home/dinusha/web-test-framework/esb/src/test/resources/defaultSynapse.xml");
             FileInputStream fstream = new FileInputStream(filePath);
             // Get the object of DataInputStream
             DataInputStream in = new DataInputStream(fstream);
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             String strLine;


             //Read File Line By Line
             while ((strLine = br.readLine()) != null){
               strLine=strLine.trim();
               conCatLines=conCatLines+strLine;
             }
             conCatLines=conCatLines.replaceAll("<"," <");
             //Close the input stream
             in.close();
             //return conCatLines;
           }
          catch (Exception e){
           System.err.println("Error: " + e.getMessage());
           }
          return conCatLines;
       }

       public void verifyAddRegKey(String seqName,String language,String function,String resourceType, String resourceName) throws Exception{
           ESBCommon esbCommon = new ESBCommon(selenium);

           esbCommon.addSequence(seqName);
           esbCommon.addRootLevelChildren("Add Child","Extension","Script");
           selenium.click("//a[@id='mediator-0']");
           Thread.sleep(2000);
           selenium.select("script_type", "label=Registry Key");
           selenium.select("mediator.script.language",language);
           selenium.type("mediator.script.function",function);
           //esbCommon.selectResource(resourceType,resourceName);
           selenium.click("link=Registry Key");
           Thread.sleep(3000);
           selenium.select("local-registry-keys-selection", "label=["+resourceType+"]-"+resourceName);
           selenium.click("link=X");
           selenium.click("//input[@value='Update']");
           selenium.click("//a[@id='mediator-0']");
           selenium.click("//a[@onclick='showSource()']");
           Thread.sleep(4000);

            if(language.equalsIgnoreCase("Javascript"))
               language="js";
            else if(language.equalsIgnoreCase("Ruby"))
               language="rb";
            else if(language.equalsIgnoreCase("Groovy"))
               language="groovy";
            else
               //do nothing

            //verify the sequence source
           assertTrue(selenium.isTextPresent("<syn:script xmlns:syn=\"http://ws.apache.org/ns/synapse\" language=\""+language+"\" key=\""+resourceName+"\" />"));
           selenium.click("link=switch to design view");

           //verify the Mediator source
           selenium.click("//a[@id='mediator-0']");
           selenium.click("link=switch to source view");
           selenium.waitForPageToLoad("30000");
           assertTrue(selenium.isTextPresent("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\""+seqName+"\"> <syn:script language=\""+language+"\" key=\""+resourceName+"\" function=\""+function+"\" /> </syn:sequence>"));
           selenium.click("link=switch to design view");
           selenium.waitForPageToLoad("30000");
       }


      //verify Click on the up-down arrow keys of the mediator
       public void moveMediator() throws Exception{

       }

       public void addScriptMediator(String seqName) throws Exception{
           ESBCommon esbCommon = new ESBCommon(selenium);
           esbCommon.addSequence(seqName);
           esbCommon.addRootLevelChildren("Add Child","Extension","Script");
       }

   }

