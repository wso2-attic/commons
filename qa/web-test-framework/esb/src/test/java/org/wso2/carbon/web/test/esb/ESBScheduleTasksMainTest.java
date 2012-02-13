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

public class ESBScheduleTasksMainTest  extends CommonSetup{

    public ESBScheduleTasksMainTest(String text) {
        super(text);
    }

    public void testScheduledTaskUI() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBScheduleTasksTest esbScheduleTasksTest=new ESBScheduleTasksTest(selenium);
        esbScheduleTasksTest.clickScheduledTask();
        assertTrue(selenium.isTextPresent("Scheduled Tasks"));
        assertTrue(selenium.isElementPresent("link=Help"));
        //esbCommon.componentHelp("Scheduled Tasks");

        if(selenium.isElementPresent("//table[@id='myTable']/thead/tr/th[1]") && selenium.isElementPresent("//table[@id='myTable']/thead/tr/th[2]")){
           assertTrue(selenium.isTextPresent("Available Defined Scheduled Tasks"));
           assertTrue(selenium.isTextPresent("Task Name"));
           assertTrue(selenium.isTextPresent("Action"));
           assertEquals("Edit", selenium.getText("config_link"));
           assertEquals("Delete", selenium.getText("delete_link"));
        }
        else{
            assertTrue(selenium.isTextPresent("No tasks are currently defined"));
            assertEquals("Add Task", selenium.getText("link=Add Task"));
        }
    }

    public void testAddScheduledTask() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBScheduleTasksTest esbScheduleTasksTest=new ESBScheduleTasksTest(selenium);
        esbScheduleTasksTest.clickScheduledTask();
       //verify Click on the link 'Add Task'
        selenium.click("link=Add Task");
        selenium.waitForPageToLoad("30000");
        selenium.getSelectedValue("taskGroup").equals("synapse.simple.quartz");
        //verify 'Trigger Type' set to 'Simple'
        assertEquals("on", selenium.getValue("taskTrigger"));
        assertTrue(selenium.isTextPresent("Count"));
        assertTrue(selenium.isTextPresent("exact:Interval (in milliseconds)*"));
        //esbCommon.componentHelp("Scheduled Tasks");

        //Submitting without specifying a task name
		selenium.click("//input[@value='Schedule']");
		assertEquals("The value for the name must not be empty and must not contain any characters other than alphanumeric , underscore , hyphen or dash.", selenium.getText("//div[@id='messagebox-warning']/p"));
		selenium.click("//button[@type='button']");
        selenium.type("taskName", "e05");

        //Submitting with out specifying a Task Implementation
        selenium.type("taskClass", "");
        selenium.click("//input[@value='Schedule']");
        assertEquals("Task Implementation class cannot be empty.", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //Specify correct value for Task implementation
        selenium.type("taskClass", "org.apache.synapse.startup.tasks.MessageInjector");
        selenium.click("loadClassButton");
        Thread.sleep(6000);
        //verify the property table
        for(int i=1;i<5;i++){
           assertTrue(selenium.isElementPresent("//table[@id='property_table']/thead/tr/th["+i+"]"));
           selenium.select("propertyTypeSelection"+(i-1), "label=XML");
           selenium.select("propertyTypeSelection"+(i-1), "label=Literal");
           assertEquals("Delete", selenium.getText("link=Delete"));
        }

        //click on shedule button with out specifyinf Interval
        selenium.click("//input[@value='Schedule']");
        assertEquals("Interval must be a positive integer if the trigger type is simple.", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //verify Click on the 'Cancel' button
        esbCommon.sequenceCancel();
    }

    public void testCreateSimpleTask() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample300Test esbSample300Test=new ESBSample300Test("");
        esbSample300Test.createSequence("test_sample300");
        esbCommon.logoutLogin();
        int beforeSchedule=esbCommon.checkLogFile("LogMediator Stock_Quote_on = , For_the_organization = , Last_Value =");
        esbSample300Test.createTask("CheckPrice");
        int afterSchedule=esbCommon.checkLogFile("LogMediator Stock_Quote_on = , For_the_organization = , Last_Value =");
        int taskExecution=afterSchedule-beforeSchedule;

        ESBScheduleTasksTest esbScheduleTasksTest = new ESBScheduleTasksTest(selenium);
        int scheduled_count=esbScheduleTasksTest.getScheduleCount();
        int trigger_count=esbScheduleTasksTest.getTriggerCount();
        int task_executedNo=scheduled_count*trigger_count;
        System.out.println("******task_executedNo*****== "+task_executedNo);

        assertTrue(selenium.isTextPresent("CheckPrice"));
        assertTrue(esbCommon.verifyManageSynapseConfig("task name=\"CheckPrice\""));
        veryfyRegitry("CheckPrice");

        //verify task execution
        if(task_executedNo==taskExecution){
        	System.out.println("Task executes the number of times specified in the 'Count' column..");
        }
        else
         	throw new MyCheckedException("The task did not execute the number of times specified in the 'Count' column once the 'Schedule' button is clicked...!");
    }


    public void testEditTask() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

       //Edit the task, do required changes and save
       editTask("CheckPrice",null,"Simple",null,"1","1000",null);
       //verify changes in manage synapse page
       String expectedText="trigger count=\"1\" interval=\"1\"";
       selenium.click("link=Synapse");
	   selenium.waitForPageToLoad("30000");
       assertTrue(selenium.getValue("rawConfig").indexOf(expectedText)!=-1);
    }

    public void testDeleteTask() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

       //Delete an existing task
       deleteTask("CheckPrice");
       verifydeteleTask("CheckPrice");
    }

    //*************************************************************************************
    public void createCronTask() throws Exception{
            //Bug  CARBON-4992
    }

    //***************************************************************************************
    
    //verify the registry
    public void veryfyRegitry(String taskName) throws Exception{
        selenium.click("link=Browse");
        selenium.waitForPageToLoad("30000");

        //String regPath="/carbon/synapse-config/synapse-startups";
        String[] folder_name={"carbon","synapse-config","synapse-startups"};

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
                selenium.waitForPageToLoad("30000");
            }
        //verify the Task in registry
        assertTrue(selenium.isTextPresent(taskName));

        selenium.click("link=Scheduled Tasks");
        selenium.waitForPageToLoad("30000");
    }

      /*
       *This method is used to delete the content files in Log directory
       */
       public void deleteLogDirContent() throws Exception{
          ESBCommon esbCommon = new ESBCommon(selenium);
          File path=new File(esbCommon.getCarbonHome()+"/logs");
          if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
               if(files[i].isDirectory()) {
                 deleteLogDirContent();
               }
               else {
                 files[i].delete();
               }
            }
          }
       }

        //Edit Task
        public void editTask(String taskName,String taskClass,String triggerType,String triggerCron,String triggerCount,String triggerInterval,String pinnedServers) throws Exception{
            ESBScheduleTasksTest esbScheduleTasksTest=new ESBScheduleTasksTest(selenium);
            esbScheduleTasksTest.clickScheduledTask();

            if(selenium.isElementPresent("//tr[@id='tr_"+taskName+"']/td[1]")){
                selenium.click("//tr[@id='tr_"+taskName+"']/td[2]/a[1]");
                selenium.waitForPageToLoad("30000");

                if(taskClass!=null){
                 selenium.type("taskClass", taskClass);
                }
                if(triggerType.equalsIgnoreCase("Simple")){
                    if(triggerCount!=null){
                        selenium.type("triggerCount", triggerCount);
                    }
                    if(triggerInterval!=null){
                        selenium.type("triggerInterval", triggerInterval);
                    }
                }
                else if(triggerType.equalsIgnoreCase("Cron") && triggerCron!=null) {
                    selenium.type("triggerCron", triggerCron);
                }
                if(pinnedServers!=null)
                    selenium.type("pinnedServers", pinnedServers);

                Thread.sleep(2000);
                selenium.click("//input[@value='Schedule']");
                selenium.waitForPageToLoad("30000");
            }
        }

        // Delete an existing task
        public void deleteTask(String taskName) throws Exception{
            ESBScheduleTasksTest esbScheduleTasksTest=new ESBScheduleTasksTest(selenium);
            esbScheduleTasksTest.clickScheduledTask();

            if(selenium.isElementPresent("//tr[@id='tr_"+taskName+"']/td[1]")){
                selenium.click("//tr[@id='tr_"+taskName+"']/td[2]/a[2]");
                //verify the msg box
                assertTrue(selenium.isElementPresent("messagebox-confirm"));
                assertTrue(selenium.isTextPresent("exact:Do you want to delete the task ' "+taskName+" ' ?"));
                selenium.click("//button[@type='button']");
                selenium.waitForPageToLoad("30000");
            }
        }

        //verify deleting a existing task
        public void verifydeteleTask(String taskName) throws Exception{
            ESBCommon esbCommon = new ESBCommon(selenium);
            ESBScheduleTasksTest esbScheduleTasksTest=new ESBScheduleTasksTest(selenium);
            esbScheduleTasksTest.clickScheduledTask();

            //verify in sequence list
            assertTrue(!selenium.isElementPresent("//tr[@id='tr_"+taskName+"']/td[1]"));

            //verify in "Manage synapse"
            boolean tsk_in_synapse=esbCommon.verifyManageSynapseConfig("task name=\""+taskName+"\"");
            if(tsk_in_synapse){
                throw new MyCheckedException("Oops, Task not deleted from the ManageSynapseConfig..Incorrect!");
            }
      }
}


