package org.wso2.carbon.web.test.mashup;

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


public class MSScheduledTasksTest extends CommonSetup{

    public MSScheduledTasksTest(String text) {
        super(text);
    }

    /*
      Sign-in to Mashup Server admin console
    */
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    /* Test basic scheduled task functionatily */
//    public void testBasicScheduledTasks() throws Exception{
//        MSScheduledTasks.scheduledTesksUI();
//        MSScheduledTasks.testCSHelp();
//    }
//
//    /*Upload the service*/
//    public void testDeployTaskService() throws Exception{
//        MSCommon.testServiceUpload("TaskSchedulerTest");
//    }

    /*Test StartupFunc*/
    public void testStartupFunc() throws Exception{
        String taskName="startupTask";
        String interval="2000";
        String intervalChangeTo="4000";

        //Check task name
        MSScheduledTasks.checkTaskName(taskName);
        //Delete task name
        MSScheduledTasks.deleteTask(taskName);
        //save service from th editor
        MSScheduledTasks.saveServiceFromEditor("TaskSchedulerTest");
        //Then check task name again
        MSScheduledTasks.checkTaskName(taskName);
        //check the interval
        MSScheduledTasks.intervalTest(taskName,interval);

        //Check the prints in the startup console --> The text "Startup Task Triggered" should be printed in evry 2 seconds.
        String arr1[]=MSScheduledTasks.readTaskLogFile("Startup");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
        }

        //Change the interval
        MSScheduledTasks.changeInterval(taskName,intervalChangeTo);

        //check in the backend if the text "Startup Task Triggered" is printed according to the new 'interval'
//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("Startup");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }
    }


    /*Test setIntervalCodeTest*/
    public void testSetIntervalCodeTest() throws Exception{
        String taskName="setIntervalCodeTest";
        String taskNameChangeTo="setIntervalCodeTest1";
        String interval="2000";
        String intervalChangeTo="4000";
        String operation="setIntervalCodeTest";

        // Tryit the service
        // Access setIntervalCodeTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskName);
        selenium.close();
        selenium.selectWindow("");

        //Check the task name
        MSScheduledTasks.checkTaskName(taskName);

        //Check if the text "setIntervalCodeTest Triggered" is printed in the backend within the given interval.
        String arr1[]=MSScheduledTasks.readTaskLogFile("setIntervalCodeTest Triggered");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("setIntervalCodeTest Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("setIntervalCodeTest Triggered is NOT printed in the backend within the given interval...");
        }

        //Change the task name
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskNameChangeTo);
        selenium.close();
        selenium.selectWindow("");

        //Then chack the task name again
        MSScheduledTasks.checkTaskName(taskNameChangeTo);

        //Test the interval
        MSScheduledTasks.intervalTest(taskNameChangeTo,interval);

        //Change the interval
        MSScheduledTasks.changeInterval(taskNameChangeTo,intervalChangeTo);

        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setIntervalCodeTest Triggered");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask(taskNameChangeTo);
    }

    /*Test setIntervalArgsTest*/
    public void testSetIntervalArgsTest() throws Exception{
        String taskName="setIntervalArgsTest";
        String taskNameChangeTo="setIntervalArgsTest1";
        String interval="2000";
        String intervalChangeTo="4000";
        String operation="setIntervalArgsTest";

        // Tryit the service
        // Access setIntervalArgsTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskName);
        selenium.close();
        selenium.selectWindow("");

        //check the task name
        MSScheduledTasks.checkTaskName(taskName);

        //Check if the text "setIntervalArgsTest Triggered" is printed in the backend within the given interval.
        String arr1[]=MSScheduledTasks.readTaskLogFile("setIntervalArgsTest Triggered");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("setIntervalArgsTest Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("setIntervalArgsTest Triggered is NOT printed in the backend within the given interval...");
        }

        //Change the task name
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskNameChangeTo);
        selenium.close();
        selenium.selectWindow("");

        //Check the task name again
        MSScheduledTasks.checkTaskName(taskNameChangeTo);

        //Test the interval
        MSScheduledTasks.intervalTest(taskNameChangeTo,interval);

        //Change the interval
        MSScheduledTasks.changeInterval(taskNameChangeTo,intervalChangeTo);

        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setIntervalArgsTest Triggered");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask(taskNameChangeTo);
    }

    /*Test setIntervalStartTimeTest*/

    /*......Due to bug........
    testSetIntervalStartTimeTest(org.wso2.carbon.web.test.mashup.MSScheduledTasksTest)  Time elapsed: 24.505 sec  <<< ERROR!
    java.lang.NullPointerException
	    at org.wso2.carbon.web.test.mashup.MSScheduledTasks.getInterval(MSScheduledTasks.java:200)
	    at org.wso2.carbon.web.test.mashup.MSScheduledTasksTest.testSetIntervalStartTimeTest(MSScheduledTasksTest.java:231)
     */
    public void testSetIntervalStartTimeTest() throws Exception{
        String taskName="setIntervalStartTimeTest";
        String taskNameChangeTo="setIntervalStartTimeTest1";
        String interval="2000";
        String intervalChangeTo="4000";
        String operation="setIntervalStartTimeTest";

        // Tryit the service
        // Access setIntervalStartTimeTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskName);
        selenium.close();
        selenium.selectWindow("");

        //It should start at the given start time
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:20");

        //It should have the specified interval
        String arr1[]=MSScheduledTasks.readTaskLogFile("setIntervalStartTimeTest Triggered");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("setIntervalStartTimeTest Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("setIntervalStartTimeTest Triggered is NOT printed in the backend within the given interval...");
        }

        //check the task name
        MSScheduledTasks.checkTaskName(taskName);

        //Change the task name
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskNameChangeTo);
        selenium.close();
        selenium.selectWindow("");

        //Then check the task name again
        MSScheduledTasks.checkTaskName(taskNameChangeTo);

        //Test the interval
        MSScheduledTasks.intervalTest(taskNameChangeTo,interval);

        //Change the interval
        MSScheduledTasks.changeInterval(taskNameChangeTo,intervalChangeTo);

        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:20");

//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setIntervalStartTimeTest Triggered");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask(taskNameChangeTo);
    }


    //Test setIntervalEndTimeTest

    /*
    testSetIntervalEndTimeTest(org.wso2.carbon.web.test.mashup.MSScheduledTasksTest)  Time elapsed: 24.808 sec  <<< ERROR!
    java.lang.NullPointerException
	    at org.wso2.carbon.web.test.mashup.MSScheduledTasks.getInterval(MSScheduledTasks.java:200)
	    at org.wso2.carbon.web.test.mashup.MSScheduledTasksTest.testSetIntervalEndTimeTest(MSScheduledTasksTest.java:296)
     */
    public void testSetIntervalEndTimeTest() throws Exception{
        String taskName="setIntervalEndTimeTest";
        String taskNameChangeTo="setIntervalEndTimeTest1";
        String interval="2000";
        String intervalChangeTo="4000";
        String operation="setIntervalEndTimeTest";

        // Tryit the service
        // Access setIntervalEndTimeTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskName);
        selenium.close();
        selenium.selectWindow("");

        //It should end at the given end time (should be 20 seconds after the 'endtime' that is picked in the service)
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:20");

        //It should have the specified interval
        String arr1[]=MSScheduledTasks.readTaskLogFile("setIntervalEndTimeTest Triggered");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("setIntervalEndTimeTest Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("setIntervalEndTimeTest Triggered is NOT printed in the backend within the given interval...");
        }

        //Check the task name
        MSScheduledTasks.checkTaskName(taskName);

        //Change the task name
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskNameChangeTo);
        selenium.close();
        selenium.selectWindow("");

        //Then chack the task name again
        MSScheduledTasks.checkTaskName(taskNameChangeTo);

        //Test the interval
        MSScheduledTasks.intervalTest(taskNameChangeTo,interval);

        //Change the interval
        MSScheduledTasks.changeInterval(taskNameChangeTo,intervalChangeTo);

        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:20");

//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setIntervalEndTimeTest Triggered");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask(taskNameChangeTo);
    }

    //Test setIntervalStartEndTimesTest

    /*
    testSetIntervalStartEndTimesTest(org.wso2.carbon.web.test.mashup.MSScheduledTasksTest)  Time elapsed: 24.179 sec  <<< ERROR!
    java.lang.NullPointerException
	    at org.wso2.carbon.web.test.mashup.MSScheduledTasks.getInterval(MSScheduledTasks.java:200)
	    at org.wso2.carbon.web.test.mashup.MSScheduledTasksTest.testSetIntervalStartEndTimesTest(MSScheduledTasksTest.java:362)
     */
    public void testSetIntervalStartEndTimesTest() throws Exception{
        String taskName="setIntervalStartEndTimesTest";
        String taskNameChangeTo="setIntervalStartEndTimesTest1";
        String interval="2000";
        String intervalChangeTo="4000";
        String operation="setIntervalStartEndTimesTest";

        // Tryit the service
        // Access setIntervalStartEndTimesTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskName);
        selenium.close();
        selenium.selectWindow("");

        //It should start at the given start time (should be 20 seconds after the 'startTime' that is picked in the service)
        //It should end at the given end time (should be 40 seconds after the 'endTime' that is picked in the service)
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:20");  //start time
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:40");  //end time

        //It should have the specified interval
        String arr1[]=MSScheduledTasks.readTaskLogFile("setIntervalStartEndTimesTest Triggered");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("setIntervalStartEndTimesTest Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("setIntervalStartEndTimesTest Triggered is NOT printed in the backend within the given interval...");
        }

        //check the task name
        MSScheduledTasks.checkTaskName(taskName);

        //Change the task name
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskNameChangeTo);
        selenium.close();
        selenium.selectWindow("");

        //Then chack the task name again
        MSScheduledTasks.checkTaskName(taskNameChangeTo);

        //test the inerval
        MSScheduledTasks.intervalTest(taskNameChangeTo,interval);

        //Change the interval
        MSScheduledTasks.changeInterval(taskNameChangeTo,intervalChangeTo);

        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:20");  //start time
        MSScheduledTasks.getGivenStart_endTimes(MSScheduledTasks.getDateTime(),"00:00:40");  //end time


//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setIntervalStartEndTimesTest Triggered");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask(taskNameChangeTo);
    }

    /*
      Task name is changed.
     */
//     public void testSetIntervalUUIDTest() throws Exception{
//        String taskName="setIntervalUUIDTest";
//        String interval="2000";
//        String intervalChangeTo="4000";
//        String operation="setIntervalUUIDTest";
//
//        // Tryit the service
//        // Access setIntervalUUIDTest
//        MSCommon.testAccessTryit("TaskSchedulerTest");
//        Thread.sleep(10000);
//        MSScheduledTasks.operationCallWithoutSignature(operation);
//        selenium.close();
//        selenium.selectWindow("");
//
//         //Check the task name
//        MSScheduledTasks.checkTaskName(taskName);
//
//        //It should have the specified interval
//        String arr1[]=MSScheduledTasks.readTaskLogFile("setIntervalUUIDTest Triggered");
//        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
//        if(i==Long.parseLong(interval)){
//           System.out.println("setIntervalUUIDTest Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("setIntervalUUIDTest Triggered is NOT printed in the backend within the given interval...");
//        }
//
//        //Test the interval
//        MSScheduledTasks.intervalTest(taskName,interval);
//
//        //Change the interval
//        MSScheduledTasks.changeInterval(taskName,intervalChangeTo);
//
//        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
////        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setIntervalUUIDTest Triggered");
////        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
////        if(j==Long.parseLong(intervalChangeTo)){
////           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
////        }
////        else{
////            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
////        }
//
//        MSScheduledTasks.deleteTask(taskName);
//    }

    public void testIsTaskActiveTest() throws Exception{
        String operation="isTaskActiveTest";
        String taskName="isTaskActiveTest";

        //Delete all tasks from the UI
        MSScheduledTasks.deleteTask("startupTask");

        // Tryit the service
        // Access isTaskActiveTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithoutSignature(operation);
        selenium.close();
        selenium.selectWindow("");

        //it should print "false" in the back-end
        MSScheduledTasks.check_true_false_inback_end("false");

        // Tryit the service
        // Access setIntervalCodeTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature("setIntervalCodeTest","setIntervalCodeTest");
        selenium.close();
        selenium.selectWindow("");

        // Tryit the service
        // Access isTaskActiveTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithoutSignature(operation);
        selenium.close();
        selenium.selectWindow("");

        //It should print 'true' in the back-end console.
        MSScheduledTasks.check_true_false_inback_end("true");

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask("setIntervalCodeTest");

    }


    public void testclearIntervalTest() throws Exception{
        String operation="clearIntervalTest";
        String taskName="clearIntervalTest";

        //Tasks are already deleted from previous methods,then no need to delete.

        //save service from editor
        MSScheduledTasks.saveServiceFromEditor("TaskSchedulerTest");

        //Check the task name of startupFunc
        MSScheduledTasks.checkTaskName(taskName);

        // Tryit the service
        // Access clearIntervalTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithoutSignature(operation);
        selenium.close();
        selenium.selectWindow("");

        //Check if the task is cleared.
        MSScheduledTasks.afterTaskIsCleared();

        //Invoke all the tasks from the tryit
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature("setIntervalCodeTest","setIntervalCodeTest");
        MSScheduledTasks.operationCallWithSignature("setIntervalArgsTest","setIntervalArgsTest");
        MSScheduledTasks.operationCallWithSignature("setIntervalStartTimeTest","setIntervalStartTimeTest");
        MSScheduledTasks.operationCallWithSignature("setIntervalEndTimeTest","setIntervalEndTimeTest");
        MSScheduledTasks.operationCallWithSignature("setIntervalStartEndTimesTest","setIntervalStartEndTimesTest");
        MSScheduledTasks.operationCallWithSignature("setIntervalUUIDTest","setIntervalUUIDTest");
        selenium.close();
        selenium.selectWindow("");

        //access "clearIntervalTest"
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithoutSignature(operation);
        selenium.close();
        selenium.selectWindow("");

        //Check if the tasks are cleared.
        MSScheduledTasks.afterTaskIsCleared();

        MSScheduledTasks.deleteTask(taskName);
    }



      public void testSetTimeoutTest() throws Exception{
        String taskName="setTimeoutTest";
        String taskNameChangeTo="setTimeoutTest1";
        String interval="2000";
        String intervalChangeTo="4000";
        String operation="setTimeoutTest";

        // Tryit the service
        // Access setTimeoutTest
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskName);
        selenium.close();
        selenium.selectWindow("");

        //Check the task name
        MSScheduledTasks.checkTaskName(taskName);

        //It should have the specified interval
        String arr1[]=MSScheduledTasks.readTaskLogFile("setTimeoutTest Triggered");
        long i=MSScheduledTasks.getInterval(arr1[1],arr1[0]);
        if(i==Long.parseLong(interval)){
           System.out.println("setTimeoutTest Triggered is printed in the backend within the given interval...");
        }
        else{
            System.out.println("setTimeoutTest Triggered is NOT printed in the backend within the given interval...");
        }

        //Change the task name
        MSCommon.testAccessTryit("TaskSchedulerTest");
        Thread.sleep(10000);
        MSScheduledTasks.operationCallWithSignature(operation,taskNameChangeTo);
        selenium.close();
        selenium.selectWindow("");

        //Then test the task name again
        MSScheduledTasks.checkTaskName(taskNameChangeTo);

        //Test the interval
        MSScheduledTasks.intervalTest(taskNameChangeTo,interval);

        //Change the interval
        MSScheduledTasks.changeInterval(taskNameChangeTo,intervalChangeTo);

        //check in the backend if the text "setIntervalCodeTest Triggered" is printed according to the new 'interval'
//        String arr2[]=MSScheduledTasks.readTaskLogFile_newTimeInterval("setTimeoutTest Triggered");
//        long j=MSScheduledTasks.getInterval(arr2[1],arr2[0]);
//        if(j==Long.parseLong(intervalChangeTo)){
//           System.out.println("Startup Task Triggered is printed in the backend within the given interval...");
//        }
//        else{
//            System.out.println("Startup Task Triggered is NOT printed in the backend within the given interval...");
//        }

        MSScheduledTasks.deleteTask(taskName);
        MSScheduledTasks.deleteTask(taskNameChangeTo);
    }

    /*Delete the service*/
    public void testDeleteService() throws Exception{
        MSCommon.testDelete("TaskSchedulerTest");
    }

     /*
        Sign out from the admin console
      */
     public void testSignout() throws Exception {
          SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
          instseleniumTestBase.logOutUI();
     }
    
}
