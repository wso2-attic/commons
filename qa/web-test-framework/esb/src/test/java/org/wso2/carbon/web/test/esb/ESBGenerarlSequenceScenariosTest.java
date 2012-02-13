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


public class ESBGenerarlSequenceScenariosTest extends CommonSetup{

    public ESBGenerarlSequenceScenariosTest(String text) {
        super(text);
    }

    //Click on sequences and verify the UI
    public void testSequencesOpeningPage() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();

        assertTrue(selenium.isTextPresent("Available defined Sequences in the Synapse Configuration"));
		assertEquals("Sequence Name", selenium.getTable("sequencesTable.0.0"));
		assertEquals("Actions", selenium.getTable("sequencesTable.0.1"));


        String readService = "";
        String s1="main";
        String s2="fault";

        int i=1,j=0;
        while(!s1.equals(readService)){
            readService=selenium.getText("//table[@id='sequencesTable']/tbody/tr["+i+"]/td[1]");
            if(s2.equals(readService)){
                j=i;
            }
             i=i+1;
        }
        i=i-1;

        //verify Actions in "main" sequence
            assertEquals("Enable Statistics", selenium.getTable("sequencesTable."+i+".1"));
		    assertEquals("Enable Tracing", selenium.getTable("sequencesTable."+i+".2"));
		    assertEquals("Edit", selenium.getTable("sequencesTable."+i+".3"));
		    assertEquals("Delete", selenium.getTable("sequencesTable."+i+".4"));
        //verify Actions in "fault" sequence
            assertEquals("Enable Statistics", selenium.getTable("sequencesTable."+j+".1"));
		    assertEquals("Enable Tracing", selenium.getTable("sequencesTable."+j+".2"));
		    assertEquals("Edit", selenium.getTable("sequencesTable."+j+".3"));
		    assertEquals("Delete", selenium.getTable("sequencesTable."+j+".4"));


        //verify the help page
        //esbCommon.componentHelp("Mediation Sequences");
    }

    public void testAddSequence() throws Exception{
       ESBCommon esbCommon = new ESBCommon(selenium);
       esbCommon.logoutLogin();

       esbCommon.addSequence("");
       assertTrue(selenium.isTextPresent("Design Sequence"));
       assertEquals("", selenium.getValue("sequence.name"));
       assertEquals("Root", selenium.getText("link=Root"));
	   assertEquals("Add Child", selenium.getText("link=Add Child"));

       // verify the blank sequence tree area
        selenium.click("link=switch to source view");
	    selenium.waitForPageToLoad("30000");
		assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"\" />", selenium.getText("sequence_source"));
        selenium.click("link=switch to design view");
		selenium.waitForPageToLoad("30000");

       //click on save button with out specifying Sequence name
       selenium.click("saveButton");
	   assertTrue(selenium.isElementPresent("messagebox-warning"));
	   assertTrue(selenium.isTextPresent("Please specify the sequence name"));
	   selenium.click("//button[@type='button']");

       //click on apply button without specifying Sequence name
       selenium.click("applyButton");
	   assertTrue(selenium.isElementPresent("messagebox-warning"));
		assertTrue(selenium.isTextPresent("Please specify the sequence name"));
       selenium.click("//button[@type='button']");

       //click on cancel button
       esbCommon.sequenceCancel();

    }

    //Add mediators to the sequence tree and click on 'Save'
    public void testSaveSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addMediatorsToSequenceTree("test_save_sequence");
        esbCommon.sequenceSave();
        //verify in the sequence list
        assertTrue(selenium.isTextPresent("test_save_sequence"));

        //verify in the manage synapse
        esbCommon.clickSynapse();
        boolean status=esbCommon.verifyManageSynapseConfig("test_save_sequence");
        if(!status)
            throw new MyCheckedException("Sequence not found in the manage synapse..!!");
    }

    //Create a new sequence, add mediators to the sequence tree and click on 'Save As'
    public void testSaveAsNewSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //verify Save As sequence
        addMediatorsToSequenceTree("test_sequence_saveAs");
        saveAsSequence("test_sequence_saveAs","/Cabon/test");
        esbCommon.viewSequences();
        //verify in mediation sequence page
        assertTrue(!selenium.isTextPresent("test_sequence_saveAs"));
    }

    //Select an existing sequence from the sequence list, go to the edit mode and click 'Save As' and give proper registry path
    public void testsaveAsExistingSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();


        addMediatorsToSequenceTree("test_saveAs_Existing_seq");
        esbCommon.sequenceSave();
        //Select an existing sequence from the sequence list, go to the edit mode and click 'Save As' and give proper registry path
        esbCommon.clickEditSeq("test_saveAs_Existing_seq");
        Thread.sleep(2000);
        saveAsSequence("test_saveAs_Existing_seq","/Test/w");
    }

    //Create a new sequence, specify a name other than the default name given and save to the registry
    public void testSaveAsNewSequenceWithOutDefaultName() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //create a sequence
        addMediatorsToSequenceTree("test_saveAs_withOut_default");
        saveAsSequence("test_saveAs_withOut_default","test_not_D");
    }

    //Edit an existing sequence, specify a name other than the default name given and save to the registry
    public void testSaveAsExistingSequenceWithOutDefaultName() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addMediatorsToSequenceTree("test_saveAs_Existing_seq_withOut_default");
        esbCommon.sequenceSave();
        //Edit an existing sequence, specify a name other than the default name given and save to the registry
        esbCommon.clickEditSeq("test_saveAs_Existing_seq_withOut_default");
        saveAsSequence("test_saveAs_Existing_seq_withOut_default","saveAs_existing");
    }

    //Create a new sequence and click on 'Apply'
    public void testApplyNewSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        addMediatorsToSequenceTree("test_apply");
        esbCommon.sequenceApply();
        esbCommon.viewSequences();
        assertTrue(selenium.isTextPresent("test_apply"));
        esbCommon.clickEditSeq("test_apply");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("In"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Log"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Out"));

        //Add new send mediator to sequence tree and click on cancel without clicking the Apply
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceCancel();
        assertTrue(selenium.isTextPresent("test_apply"));
        esbCommon.clickEditSeq("test_apply");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("In"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Log"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Out"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Send"));
    }

    public void testAddRootChild() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addSequence("Test_Add_child");
        selenium.click("link=Add Child");
        Thread.sleep(2000);
        assertTrue(selenium.isElementPresent("link=Extension"));
		assertTrue(selenium.isTextPresent("Filter"));
		assertTrue(selenium.isTextPresent("Advanced"));
		assertTrue(selenium.isTextPresent("Transform"));
		assertTrue(selenium.isTextPresent("Core"));
    }

//    public void testExtension() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.addSequence("Test_Add_child");
//        selenium.click("link=Add Child");
//        Thread.sleep(2000);
//        selenium.click("link=Extension");
//        Thread.sleep(6000);
//        assertTrue(selenium.isTextPresent("Class"));
//        assertTrue(selenium.isTextPresent("Command"));
//        assertTrue(selenium.isTextPresent("Script"));
//        assertTrue(selenium.isTextPresent("Spring"));
//    }
//
//    public void testCore() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.addSequence("Test_Add_child");
//        selenium.click("link=Add Child");
//        Thread.sleep(2000);
//        selenium.click("link=Core");
//        Thread.sleep(2000);
//        assertTrue(selenium.isTextPresent("Drop"));
//        assertTrue(selenium.isTextPresent("Send"));
//        assertTrue(selenium.isTextPresent("Log"));
//        assertTrue(selenium.isTextPresent("Property"));
//        assertTrue(selenium.isTextPresent("Event"));
//        assertTrue(selenium.isTextPresent("Sequence"));
//    }
//
//    public void testFilter() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.addSequence("Test_Add_child");
//        selenium.click("link=Add Child");
//        Thread.sleep(2000);
//        selenium.click("link=Filter");
//        Thread.sleep(2000);
//        assertTrue(selenium.isTextPresent(" Filter"));
//        assertTrue(selenium.isTextPresent("Out"));
//        assertTrue(selenium.isTextPresent("Router"));
//        assertTrue(selenium.isTextPresent("Validate"));
//        assertTrue(selenium.isTextPresent("In"));
//        assertTrue(selenium.isTextPresent("Switch"));
//      }
//
//    public void testAdvanced() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.addSequence("Test_Add_child");
//        selenium.click("link=Add Child");
//        Thread.sleep(2000);
//        selenium.click("link=Advanced");
//        Thread.sleep(4000);
//        assertTrue(selenium.isTextPresent("Cache"));
//        assertTrue(selenium.isTextPresent("Transaction"));
//        assertTrue(selenium.isTextPresent("DBReportr"));
//        assertTrue(selenium.isTextPresent("Throttle"));
//        assertTrue(selenium.isTextPresent("Rule"));
//        assertTrue(selenium.isTextPresent("Aggregate"));
//        assertTrue(selenium.isTextPresent("Entitlement"));
//        assertTrue(selenium.isTextPresent("Iterate"));
//        assertTrue(selenium.isTextPresent("DBLookup"));
//        assertTrue(selenium.isTextPresent("Clone"));
//        assertTrue(selenium.isTextPresent("RMSequence"));
//        assertTrue(selenium.isTextPresent("Callout"));
//    }
//
//    public void testTransform() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.addSequence("Test_Add_child");
//        selenium.click("link=Add Child");
//        Thread.sleep(2000);
//        selenium.click("link=Transform");
//        Thread.sleep(2000);
//        assertTrue(selenium.isTextPresent("XQuery"));
//        assertTrue(selenium.isTextPresent("Header"));
//        assertTrue(selenium.isTextPresent("XSLT"));
//        assertTrue(selenium.isTextPresent("Fault"));
//    }
   
    public void addMediatorsToSequenceTree(String  seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addSequence(seqName);
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbCommon.addMediators("Add Child","0","Core","Log");
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
    }



    public void saveAsSequence(String  seqName,String regPath) throws Exception{
        //addMediators("test_addMediators_saveAs");
        selenium.click("saveAsButton");
		assertTrue(selenium.isTextPresent("Save As Synapse Registry Entry"));
		assertEquals(seqName, selenium.getValue("synRegKey"));
        selenium.type("synRegKey", regPath);
        selenium.click("saveSynRegButton");

        //Extract the file name from the path
        String fileName;
        int p=regPath.lastIndexOf('/');
        if(p<0)
           fileName=regPath;
        else
           fileName=regPath.substring(p+1,regPath.length());
        
            //Extract the folders from the path
            int seperatorIndex[]=new int[8];
            int x=0;
            for(int index=0;index<regPath.length();index++){
                if(regPath.charAt(index)=='/'){
                    seperatorIndex[x]=index;
                    x=x+1;
                }
            }
        int size=x-1;

        //path given to root
        if(size==-1){
            Thread.sleep(2000);
            selenium.click("link=Browse");
		    selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent(regPath));
        }
        //path is specify to folder
        else{
            String[] folder_name=new String[size];
            for(int i=0;i<size;i++){
		        int k=i;
                folder_name[i]=regPath.substring((seperatorIndex[k]+1),seperatorIndex[k+1]);
            }

            Thread.sleep(2000);
            //verify in the remote registry
            selenium.click("link=Browse");
		    selenium.waitForPageToLoad("40000");

            for(int j=0;j<folder_name.length;j++){
                String readService = "";
                int row_no = 1;

                String fName=folder_name[j];
                while (!fName.equals(readService)) {
                    readService=selenium.getText("//td[@id='actionPaneHelper"+row_no+"']/table/tbody/tr/td[1]");
                    row_no = row_no+1;
                }
                row_no = row_no - 1;
		        selenium.click("resourceView"+row_no);
                selenium.waitForPageToLoad("50000");
            }//End_for
        //verify the file in registry
        assertTrue(selenium.isTextPresent(fileName));
       }//End_else

    }

    public boolean searchRegistryResourceFromBrowse(String regPath) throws Exception{
         boolean resouce_found=false;

         //Extract the file name from the path
         String fileName;
         int p=regPath.lastIndexOf('/');
         if(p<0)
            fileName=regPath;
         else
            fileName=regPath.substring(p+1,regPath.length());

             //Extract the folders from the path
             int seperatorIndex[]=new int[10];
             int x=0;
             for(int index=0;index<regPath.length();index++){
                 if(regPath.charAt(index)=='/'){
                     seperatorIndex[x]=index;
                     x=x+1;
                 }
             }
         int size=x-1;

         //path given to root
         if(size==-1){
             Thread.sleep(2000);
             selenium.click("link=Browse");
             selenium.waitForPageToLoad("30000");
             assertTrue(selenium.isTextPresent(regPath));
         }
         //path is specify to folder
         else{
             String[] folder_name=new String[size];
             for(int i=0;i<size;i++){
                 int k=i;
                 folder_name[i]=regPath.substring((seperatorIndex[k]+1),seperatorIndex[k+1]);
             }

             Thread.sleep(2000);
             //verify in the remote registry
             selenium.click("link=Browse");
             selenium.waitForPageToLoad("50000");

             for(int j=0;j<folder_name.length;j++){
                 String readService = "";
                 int row_no = 1;

                 String fName=folder_name[j];
                 while (!fName.equals(readService)) {
                     readService=selenium.getText("//td[@id='actionPaneHelper"+row_no+"']/table/tbody/tr/td[1]");
                     row_no = row_no+1;
                 }
                 row_no = row_no - 1;
                 selenium.click("resourceView"+row_no);
                 selenium.waitForPageToLoad("50000");
             }
         //verify the file in registry
         if(selenium.isTextPresent(fileName))
             resouce_found=true;

        }

      return resouce_found;
     }


    /*
    This method is used to delete resource from  registry
     */
    public void clickActionDelete(String regPath,String rName) throws Exception{
        int i=1;
        String resourceName="";
        String path=regPath.substring(0,regPath.indexOf(rName)-1);

        if(searchRegistryResourceFromBrowse(regPath)){
            while(selenium.isElementPresent("resourceView"+i)){
                resourceName=selenium.getText("resourceView"+i);
                if(resourceName.equals(rName)){
                    break;
                }
                i=i+1;
            }

            selenium.click("actionLink"+i);
        }
        Thread.sleep(2000);
        if(i==1)
           selenium.click("link=Delete");
        else
            selenium.click("//a[@onclick=\"hideOthers("+i+",'del');deleteResource('"+regPath+"', '"+path+"')\"]");

        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
    }
}
