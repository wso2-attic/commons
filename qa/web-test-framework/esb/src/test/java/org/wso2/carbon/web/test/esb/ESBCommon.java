package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;
import java.nio.channels.FileChannel;

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

public class ESBCommon extends TestCase {
    Selenium selenium;
    int nsLevel =0;
    FileInputStream freader;

    public ESBCommon(Selenium _browser){
        selenium=_browser;
    }

    /*
	 * This method will be used when updating mediators
	 */
    public void mediatorUpdate() throws Exception {
        Thread.sleep(2000);
        selenium.click("//input[@value='Update']");
        Thread.sleep(5000);
    }

    /*
	 * This method will be used when clicking on the Save button of the Sequence editor
	 */
    public void sequenceSave() throws Exception {
        Thread.sleep(2000);
        selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method is used to save as the sequence
     */
    public void sequenceSaveAs(String regPath) throws Exception{
        selenium.click("saveAsButton");
        Thread.sleep(2000);
        selenium.type("synRegKey", regPath);
        selenium.click("saveSynRegButton");
        selenium.waitForPageToLoad("30000");
    }
    

    /*
	 * This method will be used when clicking on the Apply button of the Sequence editor
	 */
    public void sequenceApply() throws Exception {
        Thread.sleep(2000);        
        selenium.click("applyButton");
		selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will be used when clicking on the Cancel button of the Sequence editor
	 */
    public void sequenceCancel() throws Exception {
        Thread.sleep(2000);        
        selenium.click("//input[@value='Cancel']");
		selenium.waitForPageToLoad("30000");        
    }

    /*
	 * This method will be used when adding a new Sequence
	 */
    public void addSequence(String sequenceName){
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add Sequence");
		selenium.waitForPageToLoad("30000");
		selenium.type("sequence.name", sequenceName);
    }

    /*
	 * This method will verify the Help links of the mediators
	 */
     public void mediatorHelp(String mediatorName) throws Exception {
        //Commenting out the method till the issue is resolved

        //Verifying the Help link
        assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));
		selenium.click("//div[@id='mediatorDesign']/div/div/a");
        Thread.sleep(1000);

        String popupidhelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupidhelp);

        String a = selenium.getText("//h1");

        assertEquals(mediatorName+" Mediator", selenium.getText("//h1"));
        selenium.close();

        // Bringing control back to main window.
        selenium.selectWindow("");
    }

    /*
	 * This method will verify the docs links of the Management Console
	 */
     public void docLinks(String linkName, String verifyText, String headerLevel) throws Exception {
        //verifying the document links
        assertEquals(linkName, selenium.getText("link="+linkName));
		assertTrue(selenium.isElementPresent("link="+linkName));
		selenium.click("link="+linkName);
        Thread.sleep(25000);

        String popupidaboute = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupidaboute);

        if (verifyText!=null && headerLevel!=null){
            assertEquals(verifyText, selenium.getText(headerLevel));
        }
        selenium.close();

        // Bringing control back to main window.
        selenium.selectWindow("");
    }

    /*
	 * This method will select the resources from the Registry Browser
	 */
    public void selectResource(String resourceType, String resourceName) throws Exception {
        Thread.sleep(5000);
        String popupid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupid);

        selenium.select("local-registry-keys-selection", "label=["+resourceType+"]-"+resourceName);
//        selenium.click("//input[@value='OK']");
		selenium.click("link=X");        
    }

    /*
     * This method will click on the Namespace link of mediators
     */
    public void clickNamespaceLink() throws Exception{
		selenium.click("link=NameSpaces");
        Thread.sleep(3000);
    }

    /*
    This method will click on the "Synapse" link
     */
    public void clickSynapse() throws Exception{
        selenium.click("link=Synapse");
		selenium.waitForPageToLoad("30000");
    }

    /*
     * This method will be used to add namespaces through the namespace editor
     */

    public void addNamespace(String prefix, String uri) throws Exception{
        //boolean pref = selenium.isTextPresent(prefix);
        int prefix_no=0;
        String prefix_available="";
        boolean pref=false;
        while(selenium.isElementPresent("prefix"+prefix_no)){
             prefix_available=selenium.getValue("prefix"+prefix_no);
             if(prefix_available.equals(prefix)){
                 pref=true;
                 break;
             }
             else
                pref=false;

             prefix_no=prefix_no+1;
       } 

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(1000);
            selenium.click("addNSButton");
            selenium.type("prefix"+nsLevel, prefix);
            selenium.type("uri"+nsLevel, uri);
            //nsLevel++;
            selenium.click("saveNSButton");
        }
    }

    /*
    Setting the nsLevel to 0
     */
    public void setNsLevel() throws Exception{
        nsLevel = 0;
    }

    /*
     * A method used to delete mediators from the Sequence tree
     */
    public void deleteMediator(String mediatorName) throws Exception{
		//Deleting the relevant mediator
        selenium.click("link="+mediatorName);
		selenium.click("link=Delete");
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");

        //Verifying the confirmation message
        assertTrue(selenium.isTextPresent("Do you want to delete the selected mediator?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
    }

//    public void deleteUser(String Uname) throws Exception {
//        browser.click("link=User Management");
//        browser.waitForPageToLoad("30000");
//        browser.click("link=Users");
//        browser.waitForPageToLoad("30000");
//
//        String tmp = "//a[@onclick=\"deleteUser('" + Uname + "')\"]";
//       // System.out.println(tmp);
//        browser.click(tmp);
//        browser.click("//button[@type='button']");
//        browser.waitForPageToLoad("30000");
//
//     //   Thread.sleep(3000);
//
////        browser.click("//button[@type='button']");
////        browser.waitForPageToLoad("30000");
//    }


    /*
    This method will be used to delete a mediator from sequence
    */
    public void delMediator(String mediatorLevel) throws Exception{
        if (mediatorLevel!=null){
                selenium.click("//a[@id='mediator-"+mediatorLevel+"']");
                int option_count=1;
                String option="";
                while(!option.equals("Delete")){
                     option=selenium.getText("//div[@id='mediator-"+mediatorLevel+"']/div/div["+option_count+"]/a");
                     option_count=option_count+2;
                }
                if(option_count>1)
                    option_count=option_count-2;
                selenium.click("//div[@id='mediator-"+mediatorLevel+"']/div/div["+option_count+"]/a");
                Thread.sleep(5000);
                //Verifying the confirmation message
                if(selenium.getText("//a[@id='mediator-"+mediatorLevel+"']").equals("Default"))
                    assertTrue(selenium.isTextPresent("undefined"));
                else
                    assertTrue(selenium.isTextPresent("Do you want to delete the selected mediator?"));
                selenium.click("//button[@type='button']");
            }
    }

    
    /*
	 * This method will be used to go to the Manage Endpoints page
	 */
    public void viewEndpoints() throws Exception{
        selenium.click("link=Endpoints");
        selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will be used to go to the Manage Local Entry page
	 */
    public void viewLocalEntries() throws Exception{
        selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");
    }


    /*
	 * This method will be used to go to the Manage Sequence page
	 */
    public void viewSequences() throws Exception{
        selenium.click("link=Sequences");
        selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will verify the Help links of each component
	 */
     public void componentHelp(String compName) throws Exception {

        //Verifying the Help link
		assertEquals("Help", selenium.getText("link=Help"));
		selenium.click("link=Help");
        Thread.sleep(1000);

        String popupidhelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupidhelp);

        String a = selenium.getText("//h1");

		assertEquals(compName, selenium.getText("//h1"));
        selenium.close();

        // Bringing control back to main window.
        selenium.selectWindow("");
    }


    /*
    This method will add child mediators to the root level of the sequence tree
    nodeType will always be wither Add Child or Add Sibling
     */
    public void addRootLevelChildren(String nodeType, String mediatorCategory, String mediatorName) throws Exception{
        Thread.sleep(1000);
		if (nodeType!=null){
            selenium.click("link="+nodeType);
        }
        Thread.sleep(1000);
        if (mediatorCategory!=null){
            selenium.click("link="+mediatorCategory);
        }

        if (mediatorName.equals("Filter")){
            Thread.sleep(2000);
            selenium.click("//li[@id='filter']/a");
            Thread.sleep(2000);
        } else {
            Thread.sleep(1000);
            selenium.click("link="+mediatorName);
            Thread.sleep(2000);
        }
    }

    /*
     * This method will be used to add mediators to the second level of the sequence tree
     */
    public void addMediators(String nodeType,String mediatorLevel, String mediatorCategory, String mediatorName) throws Exception{
        Thread.sleep(1000);
            if (mediatorLevel!=null){
                selenium.click("//a[@id='mediator-"+mediatorLevel+"']");
                int option_count=1;
                String option="";
                while(!option.equals(nodeType)){
                     option=selenium.getText("//div[@id='mediator-"+mediatorLevel+"']/div/div["+option_count+"]/a");
                     option_count=option_count+2;
                }
                if(option_count>1)
                    option_count=option_count-2;
                selenium.click("//div[@id='mediator-"+mediatorLevel+"']/div/div["+option_count+"]/a");
            }

        Thread.sleep(2000);
        if (mediatorCategory!=null){
             selenium.click("link="+mediatorCategory);
        }

        if (mediatorName.equals("Filter")){
            Thread.sleep(2000);
            selenium.click("//li[@id='filter']/a");
            Thread.sleep(2000);
        } else {
            Thread.sleep(2000);
            selenium.click("link="+mediatorName);
            Thread.sleep(2000);
         }
         Thread.sleep(4000);
    }


    /*
    This method will be used to Logout and login again if tests fail
     */

    public void logoutLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        if(selenium.isPromptPresent()){
            selenium.close();
        }
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");        
    }

    /*
    This method will set existing sequences to the main sequence
     */
    public void setSequenceToSequence(String parentSequence, String childSequenceName) throws Exception{
		selenium.click("link=Sequences");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"editSequence('"+parentSequence+"')\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Sequence");
        Thread.sleep(3000);
        
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.openRegistryBrowser();
        esbCommon.selectResource("Sequence",childSequenceName);
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
    }

    /*
    This method will open the registry browser
     */
    public void openRegistryBrowser() throws Exception{
		selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('seq_ref')\"]");        
    }

    /*
    Removing the default mediators and setting up the Sequence mediator for testing purposes
     */
    public void setupMainSeq() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
		ESBSequenceMediatorTest esbSequenceMediatorTest = new ESBSequenceMediatorTest(selenium);

        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"editSequence('main')\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='mediator-0']");
        boolean nodeName = selenium.isElementPresent("link=In");
        
        if (nodeName){
            selenium.click("link=In");
            selenium.click("link=Delete");
            Thread.sleep(2000);
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);
            selenium.click("link=Out");
            selenium.click("link=Delete");
            Thread.sleep(2000);
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);
            esbCommon.addRootLevelChildren("Add Child","Core","Sequence");
            Thread.sleep(2000);
            esbSequenceMediatorTest.addSequenceMediator("fault");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
        }else{
            esbCommon.sequenceCancel();
        }
    }

    /*
    This method will add the onError sequence to a Sequence
     */
    public void setOnErrorSeq(String onErrorSeq) throws Exception{
		ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("link=Registry Browser");
        esbCommon.selectResource("Sequence",onErrorSeq);
    }

    /*
    This method will delete endpoints from the Manage Endpoints page
     */
    public void deleteEndpoint(String endpointName) throws Exception {
		selenium.click("link=Endpoints");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"deleteEndpoint('"+endpointName+"')\"]");
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method will delete sequences from the Manage Sequence list
     */
    public void deleteSequence(String sequenceName) throws Exception{
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
        if(sequenceName.charAt(0)=='/')
            selenium.click("//a[@onclick=\"deleteRegistrySequence('"+sequenceName+"')\"]");
        else
		    selenium.click("//a[@onclick=\"deleteSequence('"+sequenceName+"')\"]");
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
    }
    /*
    This method will apply security for Proxy services
     */
    public void applySecurity(String serviceName, String policyXml, String scenarioId) throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.enableSecurityScenario(serviceName,scenarioId);
        serviceManagement.accessServiceDashboard(serviceName);
		selenium.click("link=Policies");
		selenium.waitForPageToLoad("30000");
		selenium.click("//table[@id='binding-hierarchy-table']/tbody/tr[1]/td[2]/input");
		selenium.waitForPageToLoad("30000");
		selenium.type("raw-policy", "}");
		selenium.type("raw-policy", policyXml);
		selenium.click("save-policy");
		selenium.waitForPageToLoad("30000");
		selenium.click("//table[@id='binding-hierarchy-table']/tbody/tr[4]/td[2]/input");
		selenium.waitForPageToLoad("30000");
		selenium.type("raw-policy", policyXml);
		selenium.click("save-policy");
		selenium.waitForPageToLoad("30000");        
    }

    /*
    This method verify the add sibling
     */
    public void verifyClickAddSibling() throws Exception{
        selenium.click("link=Add Sibling");
        Thread.sleep(2000);
		assertEquals("Extension", selenium.getText("link=Extension"));
		assertEquals("Filter", selenium.getText("link=Filter"));
		assertEquals("Advanced", selenium.getText("link=Advanced"));
		assertEquals("Transform", selenium.getText("link=Transform"));
		assertEquals("Core", selenium.getText("link=Core"));
    }

    /*
    The method will click on edit sequnce
     */
    public void clickEditSeq(String seqName) throws Exception{
        selenium.click("//a[@onclick=\"editSequence('"+seqName+"')\"]");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method is used to edit a sequnce that has saved in registry ..
    To use this the patch should be applied..display the sequnces that are saved in the registry in sequnce list UI
     */
    public void clickEditDynamicSequence(String seqName) throws Exception{
        selenium.click("//a[@onclick=\"editRegistrySequence('"+seqName+"')\"]");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method is used to select a mediator from the sequence tree in the edit mode
     */
    public void editSeqMediator(String seqName,String root_level_m_Nm,String child_m_Nm) throws Exception{
       ESBCommon esbCommon = new ESBCommon(selenium);
       esbCommon.viewSequences();
       if(seqName.charAt(0)=='/'){
            clickEditDynamicSequence(seqName);
       }
       else{
            clickEditSeq(seqName);
       }

       int m_level=0;
       String m_name="";

       if(root_level_m_Nm!=null){
           while(!root_level_m_Nm.equals(m_name)){
                m_name=selenium.getText("//a[@id='mediator-"+m_level+"']");
                m_level=m_level+1;
           }
        m_level=m_level-1;
        selenium.click("//a[@id='mediator-"+m_level+"']");
       }

      if(child_m_Nm!=null){
        m_name="";
        int sub_m_level=0;
        while(!child_m_Nm.equals(m_name)){
                m_name=selenium.getText("//a[@id='mediator-"+m_level+"."+sub_m_level+"']");
                sub_m_level=sub_m_level+1;
        }
        sub_m_level=sub_m_level-1;
        selenium.click("//a[@id='mediator-"+m_level+"."+sub_m_level+"']");
      }
   }

    /*
    This method is used to verify some String in the "Manage synapse configuration"
     */
    public boolean verifyManageSynapseConfig(String taskName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
       selenium.click("link=Synapse");
	   selenium.waitForPageToLoad("30000");
       selenium.click("saveConfig");
	   Thread.sleep(20000);
	   selenium.click("//button[@type='button']");
        
       Properties properties = new Properties();
       boolean status=true;
       try{
           // Open the file
           FileInputStream fstream = new FileInputStream(esbCommon.getCarbonHome()+"/conf/synapse.xml");

           // Get the object of DataInputStream
           DataInputStream in = new DataInputStream(fstream);
           BufferedReader br = new BufferedReader(new InputStreamReader(in));
           String strLine;
           String concat_lines="";

           //Read File Line By Line
           while ((strLine = br.readLine()) != null){
               strLine=strLine.trim();
               concat_lines=concat_lines+" "+strLine;
               if(concat_lines.contains(taskName)){
                   status=true;
                   break;
               }
               else
                   status=false;
            }
          //Close the input stream
            in.close();
        }
       catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        }
       return status;
    }


    /*
    This method is used to restart the sever
     */
    public void restartSever() throws Exception{
        selenium.click("link=Shutdown/Restart");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Graceful Restart");
		selenium.click("//button[@type='button']");
        Thread.sleep(2000);
		selenium.click("//button[@type='button']");
        Thread.sleep(60000);
    }

    /*
    This method is used to click the 'swithch to source view' of a mediator
     */
   public void clickMediatorSource(String level) throws Exception{
        if(level!=null)
            selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("//a[@onclick='showSource()']");
        Thread.sleep(4000);
    }

    /*
    This method is used to click the 'swithch to source view' of a sequence
     */
    public void clickSequenceSource(String level) throws Exception{
       if(level!=null)
            selenium.click("//a[@id='mediator-"+level+"']");
        selenium.click("link=switch to source view");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method is used to click the 'Switch to design view' of a sequence
     */
    public void clickDesignView() throws Exception{
        selenium.click("link=switch to design view");
        selenium.waitForPageToLoad("30000");
    }

    /*
    This method os used to get local registry items to a string
     */
    public String getLocalRegItems() throws Exception{
              String localRegItems="";
              String localRegItemName[]=new String[100];
              String readService = "";

             //Get "Endpoints" to localRegItemName[] array
              selenium.click("link=Endpoints");
              selenium.waitForPageToLoad("30000");

              int index_endPoint=0;
              int end_point_row_no =1;
              while (selenium.isElementPresent("//table[@id='endpointListTable']/tbody/tr["+end_point_row_no+"]/td[1]")) {
                          readService=selenium.getText("//table[@id='endpointListTable']/tbody/tr["+end_point_row_no+"]/td[1]");
                          localRegItemName[index_endPoint]=" [Enpoint]-"+readService;
                          index_endPoint=index_endPoint+1;
                          end_point_row_no=end_point_row_no+1;
               }

              //Get "Local Entries" to localRegItemName[] array
              selenium.click("link=Local Entries");
              selenium.waitForPageToLoad("30000");

              int index_localEntry=index_endPoint;
              readService = "";
              int local_row_no =1;
              while (selenium.isElementPresent("//table[@id='myTable']/tbody/tr["+local_row_no+"]/td[1]")) {
                          readService=selenium.getText("//table[@id='myTable']/tbody/tr["+local_row_no+"]/td[1]");
                          localRegItemName[index_localEntry]=" [Entry]-"+readService;
                          index_localEntry=index_localEntry+1;
                          local_row_no = local_row_no+1;
               }

              //Get "Sequences" to localRegItemName[] array
              selenium.click("link=Sequences");
              selenium.waitForPageToLoad("30000");

              int index_sequence=index_localEntry;
              readService = "";
              int seq_row_no =1;
              while (selenium.isElementPresent("//table[@id='sequencesTable']/tbody/tr["+seq_row_no+"]/td[1]")) {
                          readService=selenium.getText("//table[@id='sequencesTable']/tbody/tr["+seq_row_no+"]/td[1]");
                          localRegItemName[index_sequence]=" [Sequence]-"+readService;
                          index_sequence=index_sequence+1;
                          seq_row_no = seq_row_no+1;
               }

              for(int i=0;i<index_sequence;i++){
                  localRegItems=localRegItems+localRegItemName[i];
              }
              return localRegItems;
    }

         /*
         This method is used to get embbeded registry items to a string
          */
          public String[] getEmbeddedRegItems() throws Exception{
              String embeddedRegItemName[]=new String[100];

              selenium.click("link=Browse");
              selenium.waitForPageToLoad("50000");

              String readService = "";
              int row_no = 1;
              while(selenium.isElementPresent("//td[@id='actionPaneHelper"+row_no+"']/table/tbody/tr/td[1]")){
                    readService=selenium.getText("//td[@id='actionPaneHelper"+row_no+"']/table/tbody/tr/td[1]");
                    embeddedRegItemName[row_no-1]=readService;
                    //System.out.println(embeddedRegItemName[row_no-1]);
                    row_no = row_no+1;
              }

          return embeddedRegItemName;

          }

//   public int checkLogFile(String log_expression) throws Exception{
//       ESBCommon esbCommon = new ESBCommon(selenium);
//
//       int count_log=0;
//       try{
//           // Open the file
//           FileInputStream fstream = new FileInputStream(esbCommon.getCarbonHome()+"/logs/wso2-esb.log");
//
//           // Get the object of DataInputStream
//           DataInputStream in = new DataInputStream(fstream);
//           BufferedReader br = new BufferedReader(new InputStreamReader(in));
//           String strLine=null,tmp;
//
//           String concat_lines="";
//           //Read File Line By Line
//           while ((tmp = br.readLine()) != null){
//               strLine = tmp.trim();
//               concat_lines=concat_lines+strLine;
//           }
//
//           int index =0;
//           for(int searchIndex=0;searchIndex<concat_lines.length();) {
//               index=concat_lines.indexOf(log_expression,searchIndex);
//               if( index != -1){
//                   count_log=count_log+1;
//                   searchIndex=index+log_expression.length();
//              }
//               else
//                   break;
//            }
//
//          //Close the input stream
//          in.close();
//        }
//       catch (Exception e){//Catch exception if any
//        System.err.println("Error: " + e.getMessage());
//        }
//       return count_log;
//    }


    /*
    This method is used to copy a  file  from one directory to another directory
     */
//    public  void copyfile(String srFile, String dtFile) throws IOException{
//    try{
//          File f1 = new File(srFile);
//          File f2 = new File(dtFile);
//          InputStream in = new FileInputStream(f1);
//
//          //For Overwrite the file.
//          OutputStream out = new FileOutputStream(f2);
//
//          byte[] buf = new byte[1024];
//          int len;
//          while ((len = in.read(buf)) > 0){
//            out.write(buf, 0, len);
//          }
//          in.close();
//          out.close();
//          System.out.println("File copied.");
//        }
//        catch(FileNotFoundException ex){
//          System.out.println(ex.getMessage() + " in the specified directory.");
//          System.exit(0);
//        }
//        catch(IOException e){
//          System.out.println(e.getMessage());
//        }
//    }


    public Properties loadfameworkProperties() throws IOException {
        Properties properties = new Properties();
        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    } 

    public Properties loadEsbProperties() throws IOException {
        Properties properties = new Properties();
        freader = new FileInputStream(".."+File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"esb.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    /*
    This method is used to get the host_name from the "framework.properties" file
     */
    public String getHostName() throws Exception{
        return loadfameworkProperties().getProperty("host.name");
    }

    /*
    This method is used to get the http port from the "framework.properties" file
     */
    public String getHttpPort() throws Exception{
        return loadfameworkProperties().getProperty("http.fe.port");
    }

    /*
    This method is used to get the https port from the "framework.properties" file
     */
    public String getHttpsPort() throws Exception{
        return loadfameworkProperties().getProperty("https.fe.port");
    }

    /*
    This method is used to get the nio http port from the "framework.properties" file
     */
    public String getNioHttpPort() throws Exception{
        return loadfameworkProperties().getProperty("http.be.port");
    }

    /*
    This method is used to get the nio https port from the "framework.properties" file
     */
    public String getNioHttpsPort() throws Exception{
        return loadfameworkProperties().getProperty("https.be.port");
    }

    /*
    This method is used to get the context root from the "framework.properties" file
     */
    public String getContextRoot() throws Exception{
        return loadfameworkProperties().getProperty("context.root");
    }

    /*
    This method is used to get the carbon home from the "framework.properties" file
     */
    public String getCarbonHome() throws Exception{
        return loadfameworkProperties().getProperty("carbon.home");
    }

    /*
    This method is used to get the carbon home from the "framework.properties" file
     */
    public String getBrowserVersion() throws Exception{
        return loadfameworkProperties().getProperty("browser.version");
    }

    public String getServiceAddUrl(String serviceName) throws Exception{
            return loadfameworkProperties().getProperty("addUrl."+serviceName);
    }

    /*
    This method will close opened files
     */
    public void closeFiles() throws IOException {
        freader.close();
    }

    /*
    Setting Transports through Transport Management
     */
    public void configureTransportListner(String transport) throws Exception{
		selenium.click("link=Transports");
		selenium.waitForPageToLoad("30000");

        boolean transportName = selenium.isTextPresent(transport);

        if (transportName){
            String readTransport = "";
            String transportType = transport;
            int i = 1;
            while (!transportType.equals(readTransport)) {
                readTransport = selenium.getText("//table[@id='availableTransports']/tbody/tr["+ Integer.toString(i) +"]/td[1]");
                i = i + 1;
            }
            i = i - 1;

            String transportEnabled = selenium.getText("//table[@id='availableTransports']/tbody/tr[" + Integer.toString(i) +"]/td[3]/a");
            System.out.println(transportEnabled);

             if (transportEnabled.equals("Enable")) {
                selenium.click("//table[@id='availableTransports']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a");
                selenium.waitForPageToLoad("30000");
                selenium.click("//input[@value='Enable']");
                assertTrue(selenium.isTextPresent("If any of the required jars are missing in the classpath, this transport listener will not be enabled. Do you want to proceed?"));
                selenium.click("//button[@type='button']");
                selenium.waitForPageToLoad("30000");
            } else {
                System.out.println("The transport is already enabled!!!");
            }

        } else {
            System.out.println("The transport does not exist!!!");
        }
    }

    /*
    Setting Transports through Transport Management
     */
    public void configureTransportSender(String transport) throws Exception{
		selenium.click("link=Transports");
		selenium.waitForPageToLoad("30000");

        boolean transportName = selenium.isTextPresent(transport);

        if (transportName){
            String readTransport = "";
            String transportType = transport;
            int i = 1;
            while (!transportType.equals(readTransport)) {
                readTransport = selenium.getText("//table[@id='availableTransports']/tbody/tr["+ Integer.toString(i) +"]/td[1]");
                i = i + 1;
            }
            i = i - 1;

            String transportEnabled = selenium.getText("//table[@id='availableTransports']/tbody/tr[" + Integer.toString(i+1) +"]/td[2]/a");
            System.out.println(transportEnabled);

             if (transportEnabled.equals("Enable")) {
                selenium.click("//table[@id='availableTransports']/tbody/tr[" + Integer.toString(i+1) + "]/td[2]/a");
                selenium.waitForPageToLoad("30000");
                selenium.click("//input[@value='Enable']");
                assertTrue(selenium.isTextPresent("If any of the required jars are missing in the classpath, this transport sender will not be enabled. Do you want to proceed?"));
                selenium.click("//button[@type='button']");
                selenium.waitForPageToLoad("30000");
            } else {
                System.out.println("The transport is already enabled!!!");
            }
        } else {
            System.out.println("The transport does not exist!!!");
        }
    }

 /*
    This method is used to search the resource in registry
     */
    //If u want to search the resource in a perticular place,then specify the both parameters
    //If u wants to serch the resource availability any where in the registry,then specify only resorceName parameter
    public boolean searchResources(String path,String resorceName) throws Exception {
        boolean resource_available=false;
        if(path!=null && path.charAt(0)=='/')
                path=path.substring(1);

        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        if(path!=null)
           selenium.type("#_resourceName", path);
        else
           selenium.type("#_resourceName", resorceName);

        Thread.sleep(1000);
        selenium.click("#_0");
        Thread.sleep(1000);

        if (!selenium.isTextPresent("Your search did not match any resources")){
            Thread.sleep(2000);
            if(path==null){
                resource_available=true;
            }
            else{
                selenium.click("link=/"+path);
                Thread.sleep(15000);
                //selenium.waitForPageToLoad("30000");
                if(selenium.isTextPresent(resorceName)){
                    resource_available=true;
                }
            }
        }
        return resource_available;
    }

/*
    This method is used to saveAs the Endpoint
     */
    public void saveAsAddressEndpoint(String registryPath) throws Exception{
        selenium.click("//input[@name='save' and @value='Save As']");
		selenium.type("synRegKey", registryPath);
		selenium.click("saveSynRegButton");
		selenium.waitForPageToLoad("30000");
        Thread.sleep(5000);
    }

    /*
    This method is used to check the availability of a sequence in sequence list.
     */
    public boolean searchSequenceInList(String seqName) throws Exception{
        boolean seqInList=false;
        int row_no=1;
        String s1="";
        viewSequences();
        Thread.sleep(2000);
        while (selenium.isElementPresent("//table[@id='sequencesTable']/tbody/tr["+row_no+"]/td[1]")){
            s1=selenium.getText("//table[@id='sequencesTable']/tbody/tr["+row_no+"]/td[1]");
            if(s1.equals(seqName)){
                seqInList=true;
                break;
            }
            row_no++;
         }

        return seqInList;
    }

    /*
    This method is used to switch on  the DEBUG mode
     */
    public void applyDebugMode() throws Exception{
        selenium.click("link=Logging");
		Thread.sleep(15000);
        if(!selenium.getSelectedValue("org.apache.synapseLogLevel").equals("DEBUG")){
            selenium.select("org.apache.synapseLogLevel", "label=DEBUG");
            Thread.sleep(2000);
            selenium.click("//button[@type='button']");
        }
    }

    public int checkLogFile(String log_expression) throws Exception{
        int count_log=0;
        try{
        File file = new File(getCarbonHome()+File.separator+"logs"+File.separator+"wso2-esb.log");
        String logContent = FileUtils.readFileToString(file);
        freader.close();

        int index =0;
        for(int searchIndex=0;searchIndex<logContent.length();) {
            index=logContent.indexOf(log_expression,searchIndex);
            if( index != -1){
                count_log=count_log+1;
                searchIndex=index+log_expression.length();
           }
            else
                break;
         }
        }
       catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
       }

        return count_log;
    }

    /*
    This method is used to check the sequence infomation in manage synapse.
     */
    public void checkSequenceInfoInSynapse(String seqName) throws Exception{
        viewSequences();
        //verify the information in edit mode
        clickEditSeq(seqName);

        //verify information in 'Manage Synapse'
        String defaultNs1="xmlns:ns=\"http://org.apache.synapse/xsd\"";
        String defaultNs2="xmlns:ns2=\"http://org.apache.synapse/xsd\"";
        clickSequenceSource("0");
        String seq_source = selenium.getText("sequence_source");
        String temp1=seq_source.substring(seq_source.indexOf("name"));
        String temp2="<syn:sequence "+temp1;
        temp2=temp2.replaceAll(" />","/>");
        if(temp2.contains(defaultNs1) && temp2.contains(defaultNs2)){
            temp2=temp2.replace(defaultNs1,"");
            temp2=temp2.replace("  "," ");
        }
        boolean status=verifyManageSynapseConfig(temp2);
        if(!status)
           throw new MyCheckedException("Sequence information incorrect in the  ManageSynapseConfig...!");
    }

}