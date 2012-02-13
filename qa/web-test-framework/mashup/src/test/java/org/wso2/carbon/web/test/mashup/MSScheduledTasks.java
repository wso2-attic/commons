package org.wso2.carbon.web.test.mashup;

import org.apache.tools.ant.taskdefs.Exit;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

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


public class MSScheduledTasks extends CommonSetup{

    public MSScheduledTasks(String text) {
            super(text);
    }

    /*Scheduled tasks UI Test */
    public static void scheduledTesksUI() throws Exception{
        selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Scheduled Tasks"));
		assertTrue(selenium.isTextPresent("Available Defined Scheduled Tasks"));
    }

    /* Test task name*/
    public static void checkTaskName(String taskName) throws Exception{
        selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent(taskName));
    }

    /* Check if the task is cleared */
    public static void afterTaskIsCleared() throws Exception{
        selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");

        boolean test=selenium.isTextPresent(selenium.getTable("myTable.2.0"));

        if(test==false){
            System.out.println("After accessing clearIntervalTest,task is cleared....");
        }
        else{
            System.out.println("After accessing clearIntervalTest, still task is there....");
        }
    }


    /*Delete the Task from the tasks list*/
    public static void deleteTask(String taskName) throws Exception{
        selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");

		selenium.click("//tr[@id='tr_"+taskName+"']/td[2]/a[2]");
		assertTrue(selenium.isTextPresent("exact:Do you want to delete the task ' "+taskName+" ' ?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
    }


    /*Open the service from Mashup editor.
      Save the service from the editor (so that its deply ed again)*/
    public static void saveServiceFromEditor(String serviceName) throws Exception{
        selenium.click("link=List");
		selenium.waitForPageToLoad("30000");

		int k=1;
        boolean next=selenium.isTextPresent("next >");
        while(next==true){
               selenium.click("link=next >");
               Thread.sleep(6000);
               next = selenium.isTextPresent("next >");
               k=k+1;
        }

        int i;
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        for(i=1;i<=k;i++){
            if(selenium.isElementPresent("link="+serviceName)==true){
                i=k+1;
                selenium.click("link="+serviceName);
		        selenium.waitForPageToLoad("30000");
		        selenium.click("link="+serviceName);
                selenium.waitForPageToLoad("30000");
                selenium.click("link=Edit Mashup");
                selenium.waitForPageToLoad("30000");
                selenium.click("//input[@value='Save changes']");
		        selenium.waitForPageToLoad("30000");
                Thread.sleep(10000);
            }
            else{
                int temp=i+1;
                if(temp<=k)
                    selenium.click("link="+temp);
             }
        }

    }

    /*Verifiy the 'Interval' given in the UI is the same as the value given in the service's source.*/
    public static void intervalTest(String taskName,String interval) throws Exception{
        selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");
        selenium.click("//tr[@id='tr_"+taskName+"']/td[2]/a[1]");
		selenium.waitForPageToLoad("30000");
		assertEquals(interval, selenium.getValue("triggerInterval"));
		selenium.click("//input[@value='Cancel']");
		selenium.waitForPageToLoad("30000");
    }


    /*Change the interval*/
    public static void changeInterval(String taskName,String interval) throws Exception{
        selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");
        selenium.click("//tr[@id='tr_"+taskName+"']/td[2]/a[1]");
		selenium.waitForPageToLoad("30000");
		selenium.type("triggerInterval", interval);
		selenium.click("//input[@value='Schedule']");
		selenium.waitForPageToLoad("30000");
    }


    /*Access operations with signature*/
    public static void operationCallWithSignature(String operationName,String taskName) throws Exception{
        selenium.click("link="+operationName);
		selenium.type("input_"+operationName+"_"+operationName+"_0", taskName);
		selenium.click("button_"+operationName);
    }


    /*Access operations without signature*/
    public static void operationCallWithoutSignature(String operationName) throws Exception{
        selenium.click("link="+operationName);
		selenium.click("button_"+operationName);
    }


    /*Read Task Log file*/
    public static String[] readTaskLogFile(String operation) throws Exception{
        File file = new File(MSCommon.loadProperties().getProperty("carbon.home")+File.separator+"repository" + File.separator + "scripts" + File.separator + "TaskSchedulerTest.resources" + File.separator + "taskLog.txt");
        FileReader freader = null;
        LineNumberReader lnreader = null;
        String arr[]=new String[2];
        String temp1=null;
        String temp2=null;

               try
               {
               freader = new FileReader(file);
               lnreader = new LineNumberReader(freader);
               String line = "";
               while ((line = lnreader.readLine()) != null){
                   if(line.startsWith(operation)){
                       temp1=line;
                       temp2=lnreader.readLine();
                       while(!temp2.startsWith(operation)){
                           temp2=lnreader.readLine();
                       }
                       break;

                   }

                   }
               }
                finally{
                    freader.close();
                    lnreader.close();
                }

        arr[0]=temp1;
        arr[1]=temp2;
        return arr;
    }


     /*Get the interval between two printed statement in the back-end*/
     public static long getInterval(String date1,String date2) throws ParseException {
           int i=date1.indexOf("+");
           int j=date2.indexOf("+");

           String temp1=date1.substring((i-12),(i-4));
           String temp2=date2.substring((j-12),(j-4));

           SimpleDateFormat df=new SimpleDateFormat("hh:mm:ss");

           Date d1=df.parse(temp1);
           Date d2=df.parse(temp2);
           long d1Ms=d1.getTime();
           long d2Ms=d2.getTime();

           return (d1Ms-d2Ms);
     }


      //After changing the interval of the operation,difficult to test the interval.This code for readTaskLogFile after changing the interval.

//     public static String[] readTaskLogFile_newTimeInterval(String operation) throws Exception{
//        File file = new File(MSCommon.loadProperties().getProperty("carbon.home")+File.separator+"repository" + File.separator + "scripts" + File.separator + "TaskSchedulerTest.resources" + File.separator + "taskLog.txt");
//        FileReader freader = null;
//        LineNumberReader lnreader = null;
//        LineNumberReader nlnreader = null;
//        String arr[]=new String[2];
//
//        int count=0;
//        int temp=1;
//
//        String printLine[]=new String[6];
//
//               try
//               {
//               freader = new FileReader(file);
//               lnreader = new LineNumberReader(freader);
//               nlnreader = new LineNumberReader(freader);
//               String line = "";
//
//               while ((line = lnreader.readLine()) != null){
//                   count=count+1;
//               }
//
//                while ((line = nlnreader.readLine()) != null){
//                    System.out.println("line : "+line);
//                    if(temp==count-5)
//                        printLine[0]=line;
//                    if(temp==count-4)
//                        printLine[1]=line;
//                    if(temp==count-3)
//                        printLine[2]=line;
//                    if(temp==count-2)
//                        printLine[3]=line;
//                    if(temp==count-1)
//                        printLine[4]=line;
//                    if(temp==count)
//                        printLine[5]=line;
//
//                    temp=temp+1;
//                }
//
//                for(int i=0;i<printLine.length;i++){
//                    System.out.println("*******  "+printLine[i]);
//                }
//
//                for(int j=0;j<printLine.length;j++){
//                    if(printLine[printLine.length-1-j].startsWith(operation) && printLine[printLine.length-1-j-1].startsWith(operation)){
//                        arr[1]=printLine[printLine.length-1-j];
//                        arr[0]=printLine[printLine.length-1-j-1];
//                    }
//
//                    if(printLine[printLine.length-1-j].startsWith(operation) && printLine[printLine.length-1-j-2].startsWith(operation)){
//                        arr[1]=printLine[printLine.length-1-j];
//                        arr[0]=printLine[printLine.length-1-j-2];
//                    }
//                }
//
//
//               }
//                finally{
//                    freader.close();
//                    lnreader.close();
//                }
//        return arr;
//    }
    


    /*Check "true" and "false" in the back-end for "isTaskActiveTest" operation*/
    public static void check_true_false_inback_end(String true_false) throws Exception{
        File file = new File(MSCommon.loadProperties().getProperty("carbon.home")+File.separator+"repository" + File.separator + "scripts" + File.separator + "TaskSchedulerTest.resources" + File.separator + "a.txt");
        FileReader freader = null;
        LineNumberReader lnreader = null;

               try
               {
               freader = new FileReader(file);
               lnreader = new LineNumberReader(freader);
               String line = "";
               while ((line = lnreader.readLine()) != null){
                    if(line.equals(true_false)){
                        System.out.println("print "+true_false+" in the back-end");
                        break;
                    }
               }
               }
                finally{
                    freader.close();
                    lnreader.close();
                }
    }

    /*Get current time*/
    public static String getDateTime() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*Add current time and given start time or end time*/
    public static long getGivenStart_endTimes(String currentTime,String timeToAdd) throws ParseException {

           SimpleDateFormat df=new SimpleDateFormat("hh:mm:ss");

           Date d1=df.parse(currentTime);
           Date d2=df.parse(timeToAdd);
           long d1Ms=d1.getTime();
           long d2Ms=d2.getTime();

           return (d1Ms+d2Ms);
       }



    /*CSHelp */
    public static void testCSHelp() throws Exception{
       String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/task/docs/userguide.html";
       selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");
       assertTrue(selenium.isTextPresent("Help"));
       selenium.click("link=Help");
       String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
       selenium.selectWindow(helpwinid);
       Thread.sleep(10000);
       assertTrue(selenium.isTextPresent("Scheduled Tasks"));
       String actualForCSHelp = selenium.getLocation();
       if(actualForCSHelp.equals(expectedForCSHelp))
           System.out.println("Actual location & expected location are matched");
       else
           System.out.println("Actual location & expected location are not matched");
       selenium.close();
       selenium.selectWindow("");
    }
}
