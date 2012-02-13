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

    public ESBScheduleTasksTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add schedule a new task
	 */
    public void testAddNewTask(String taskName, String taskClass, String propertyType0, String propertyValue0,
                               String propertyType1, String propertyValue1, String propertyType2 ,
                               String propertyValue2, String propertyType3, String propertyValue3,
                               String triggerCount, String triggerInterval) throws Exception{
		selenium.click("link=Scheduled Tasks");
        Thread.sleep(2000);
		selenium.click("link=Add Task");
        Thread.sleep(2000);
        selenium.type("taskName", taskName);
		selenium.type("taskClass", taskClass);
		selenium.click("loadClassButton");
        Thread.sleep(5000);

        if (propertyType0.equals("XML")){
            selenium.select("propertyTypeSelection0", "label="+propertyType0);
            selenium.type("textArea0", propertyValue0);
        } else if (propertyType0.equals("Literal")){
            selenium.select("propertyTypeSelection0", "label="+propertyType0);
            selenium.type("textField0", propertyValue0);
        }

        if (propertyType1.equals("XML")){
            selenium.select("propertyTypeSelection1", "label="+propertyType1);
            selenium.type("textArea1", propertyValue1);
        } else if (propertyType1.equals("Literal")){
            selenium.select("propertyTypeSelection1", "label="+propertyType1);
            selenium.type("textField1", propertyValue1);
        }

        if (propertyType2.equals("XML")){
            selenium.select("propertyTypeSelection2", "label="+propertyType2);
            selenium.type("textArea2", propertyValue2);
        } else if (propertyType2.equals("Literal")){
            selenium.select("propertyTypeSelection2", "label="+propertyType2);
            selenium.type("textField2", propertyValue2);
        }

        if (propertyType3.equals("XML")){
            selenium.select("propertyTypeSelection3", "label="+propertyType3);
            selenium.type("textArea3", propertyValue3);
        } else if (propertyType3.equals("Literal")){
            selenium.select("propertyTypeSelection3", "label="+propertyType3);            
            selenium.type("textField3", propertyValue3);
        }

		selenium.type("triggerCount", triggerCount);
		selenium.type("triggerInterval", triggerInterval);
		selenium.click("//input[@value='Schedule']");
	}
}
