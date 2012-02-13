package org.wso2.carbon.web.test.mashup;

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

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

public class MSaccessSecService extends CommonSetup {

    public MSaccessSecService(String text) {
        super(text);
    }

    //Enable security scenarios.
    public static void testEnableSecurity(String serviceName,String scenarioid) throws Exception{
        int i = 1,k=1;
        boolean next=selenium.isTextPresent("next >");
        boolean search=true;

        while(next){
               selenium.click("link=next >");
               Thread.sleep(6000);
               next = selenium.isTextPresent("next >");
               i=i+1;
        }
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        for(k=1;k<=i;k++){
           int j=1,x=1;
           while(j<k){
              selenium.click("link=next >");
               j=j+1;
           }
           selenium.type("serviceGroupSearchString", serviceName);
		   selenium.click("//a[@onclick='javascript:searchServices(); return false;']");
		   selenium.waitForPageToLoad("30000");
           search = selenium.isTextPresent("No Deployed Services Found");
           if(search){
             selenium.click("link=List");
             selenium.waitForPageToLoad("30000");
           }else{
               selenium.click("link="+serviceName);
               selenium.waitForPageToLoad("30000");
               selenium.click("link="+serviceName);
               selenium.waitForPageToLoad("30000");
               selenium.click("link=Security");
               selenium.waitForPageToLoad("30000");
               if ((selenium.getSelectedValue("securityConfigAction")).equals("Yes")) {
                   selenium.select("securityConfigAction", "label=No");
                   assertTrue(selenium.isTextPresent("This will disable security from the service. Click OK to confirm"));
                   selenium.click("//button[@type='button']");
                   selenium.waitForPageToLoad("30000");
                   assertTrue(selenium.isTextPresent("Security disabled successfully."));
                   selenium.click("//button[@type='button']");
                }
                selenium.select("securityConfigAction", "label=Yes");
                Thread.sleep(1000);
                selenium.click("//input[@name='scenarioId' and @value='" + scenarioid + "']");
                selenium.click("//input[@value='Next >']");
                selenium.waitForPageToLoad("30000");
                //For scenario 1
                if (scenarioid.equals("scenario1")) {
                    selenium.click("//input[@name='userGroups' and @value='admin']");
                    selenium.click("//input[@value='Finish']");
                    selenium.waitForPageToLoad("30000");
                } else if (scenarioid.equals("scenario15") || scenarioid.equals("scenario7") || scenarioid.equals("scenario8") || scenarioid.equals("scenario14")) {
                    selenium.click("userGroups");
                    selenium.click("trustStore");
		            selenium.click("//input[@value='Finish']");
                    selenium.waitForPageToLoad("30000");
                } else {
                    selenium.click("trustStore");
		            selenium.click("//input[@value='Finish']");
                    selenium.waitForPageToLoad("30000");
                }
                assertTrue(selenium.isTextPresent("Security applied successfully."));
                selenium.click("//button[@type='button']");
                k=i+1;
           }
        }
    }

    //Disable security scenarios.
    public static void testDisableSecurity(String serviceName) throws Exception{
            int i = 1,k=1;
            boolean next=selenium.isTextPresent("next >");
            boolean search=true;

            while(next){
                   selenium.click("link=next >");
                   Thread.sleep(6000);
                   next = selenium.isTextPresent("next >");
                   i=i+1;
            }
            selenium.click("link=List");
            selenium.waitForPageToLoad("30000");
            for(k=1;k<=i;k++){
               int j=1,x=1;
               while(j<k){
                  selenium.click("link=next >");
                   j=j+1;
               }
               selenium.type("serviceGroupSearchString", serviceName);
               selenium.click("//a[@onclick='javascript:searchServices(); return false;']");
               selenium.waitForPageToLoad("30000");
               search = selenium.isTextPresent("No Deployed Services Found");
               if(search){
                 selenium.click("link=List");
                 selenium.waitForPageToLoad("30000");
               }else{
                   selenium.click("link="+serviceName);
                   selenium.waitForPageToLoad("30000");
                   selenium.click("link="+serviceName);
                   selenium.waitForPageToLoad("30000");
                   selenium.click("link=Security");
                   selenium.waitForPageToLoad("30000");
                   if ((selenium.getSelectedValue("securityConfigAction")).equals("Yes")) {
                       selenium.select("securityConfigAction", "label=No");
                       assertTrue(selenium.isTextPresent("This will disable security from the service. Click OK to confirm"));
                       selenium.click("//button[@type='button']");
                       selenium.waitForPageToLoad("30000");
                       assertTrue(selenium.isTextPresent("Security disabled successfully."));
                       selenium.click("//button[@type='button']");
                   }
                }
             }
    }

    //Accessing tryit to test  invokeSecService service.
    public static void testInvokeGetVersionForScenarios(String serviceName,String InvokeService,String scenarioid) throws Exception {
           testEnableSecurity(serviceName,scenarioid);
           MSCommon.testAccessTryit(InvokeService);
           Thread.sleep(20000);
           selenium.type("input_invokeGetVersion_keystore_path_0", MSCommon.loadProperties().getProperty("carbon.home")+"/resources/security/wso2carbon.jks");
		   selenium.click("button_invokeGetVersion");
           Thread.sleep(10000);
           String unexpected="Fault:";
           String temp=selenium.getText("console_invokeGetVersion");
           System.out.println(temp);
           String expected=temp.substring(0,6);
           System.out.println(expected);
           if (expected.equals(unexpected)) {
             System.out.println("assert is failed");
           } else{
             System.out.println("assert is passed");
           }
           selenium.close();
           selenium.selectWindow("");
           testDisableSecurity(serviceName);
       
    }
}
