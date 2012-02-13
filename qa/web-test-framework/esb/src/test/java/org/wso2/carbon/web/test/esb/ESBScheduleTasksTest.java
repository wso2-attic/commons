package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class ESBScheduleTasksTest  extends TestCase {
    Selenium selenium;
    public static int schelude_count=0;
    public static int triggerCount=0;
    public static int triggerInterval=0;
    public ESBScheduleTasksTest(Selenium _browser){
		selenium = _browser;
    }


    /*
    Click on the schedued task link
     */
    public void clickScheduledTask() throws Exception{
       selenium.click("link=Scheduled Tasks");
       selenium.waitForPageToLoad("30000");
   }

    /*
	 * This method will add schedule a new task
	 */
    public void addMandInfo(String taskName) throws Exception{
		selenium.click("link=Scheduled Tasks");
        Thread.sleep(5000);
		selenium.click("link=Add Task");
        Thread.sleep(5000);
        selenium.type("taskName", taskName);
    }

    /*
    This method will add implementation details to the task
     */
    public void addImplInfo(String taskClass, String msgPropertyType, String msgPropVal,String toPropType ,String toPropVal,String actionPropType, String actionPropVal,String formatPropType, String formatPropVal) throws Exception{

		selenium.type("taskClass", taskClass);
		selenium.click("loadClassButton");
        Thread.sleep(5000);

        int msgIndex=getPropertyIndex("message");
        if (msgPropertyType.equals("XML")){
        selenium.select("propertyTypeSelection"+msgIndex, "label="+msgPropertyType);
        selenium.type("textArea"+msgIndex, msgPropVal);
        }else if (msgPropertyType.equals("Literal")){
            selenium.select("propertyTypeSelection"+msgIndex, "label="+msgPropertyType);
            selenium.type("textField"+msgIndex, msgPropVal);
        }

        int toIndex=getPropertyIndex("to");
        if (toPropType.equals("XML")){
            selenium.select("propertyTypeSelection"+toIndex, "label="+toPropType);
            selenium.type("textArea"+toIndex, toPropVal);
        }else if (toPropType.equals("Literal")){
            selenium.select("propertyTypeSelection"+toIndex, "label="+toPropType);
            selenium.type("textField"+toIndex, toPropVal);
        }

        int actionIndex=getPropertyIndex("soapAction");
        if (actionPropType.equals("XML")){
            selenium.select("propertyTypeSelection"+actionIndex, "label="+actionPropType);
            selenium.type("textArea"+actionIndex, actionPropVal);
        }else if(actionPropType.equals("Literal")){
            selenium.select("propertyTypeSelection"+actionIndex, "label="+actionPropType);
            selenium.type("textField"+actionIndex, actionPropVal);
        }

        int formatIndex=getPropertyIndex("format");
        if (formatPropType.equals("XML")){
            selenium.select("propertyTypeSelection"+formatIndex, "label="+formatPropType);
            selenium.type("textArea"+formatIndex, formatPropVal);
        } else if (formatPropType.equals("Literal")){
            selenium.select("propertyTypeSelection"+formatIndex, "label="+formatPropType);
            selenium.type("textField"+formatIndex, formatPropVal);
        }
    }

    public int getPropertyIndex(String propertyName) throws Exception{
        int count=0;
        String property;
        do{
            property=selenium.getValue("property_name"+count);
            count=count+1;
        }while(!(property.equals(propertyName)));
         return count-1;
    }


    /*
    This method will add trigger information
     */
    public void addTriggerInfo(String triggerCount, String triggerInterval) throws Exception{
		selenium.type("triggerCount", triggerCount);
		selenium.type("triggerInterval", triggerInterval);
        this.triggerCount=Integer.parseInt(triggerCount);
		this.triggerInterval=Integer.parseInt(triggerInterval);
    }

       /*
     This method is used to get the trigger information "trigger count"
     */
     public int getTriggerCount() throws Exception{
     	return triggerCount;
     }

     /*
      This metthod is used to get the trigger interval
      */
      public int getTriggerInterval() throws Exception{
      	return triggerInterval;
      }

    /*
    This method will add Miscellaneous Information
     */
    public void addMiscInfo(String serverList) throws Exception{
        selenium.type("pinnedServers", serverList);
    }

    /*
    This method will schedule the task
     */
    public void scheduleTask() throws Exception{
		selenium.click("//input[@value='Schedule']");
        selenium.waitForPageToLoad("30000");
        schelude_count++;
    }

    /*
    This method will be used to reschedule tasks once they are being created
     */
    public void rescheduleTask() throws Exception{
		selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");
		selenium.click("config_link");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Schedule']");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method is used to set the value of schedule_count variable
     */
    public void setScheduleCount() throws Exception{
        schelude_count=0;
    }

    /*
    This method is used to get the value of schedule_count variable
     */
    public int getScheduleCount() throws Exception{
        return schelude_count;
    }
}
