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


public class MSHostObjects extends CommonSetup {

    public MSHostObjects(String text) {
        super(text);
    }


    /*
        Feed host object
     */

    /*
     Accessing tryit to test raceNewsRss service.
    */
    public static void testReadFeedtEntry() throws Exception {
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        MSCommon.testAccessTryit("raceNewsRss");
        selenium.click("button_raceNewsRss");
        Thread.sleep(6000);
        String actual = selenium.getText("console_raceNewsRss");

        selenium.open("http://www.formula1.com/rss/news/headlines.rss");
        selenium.waitForPageToLoad("6000");
        String expected = selenium.getText("//x:div[@id='feedContent']/x:div[1]/x:h3/x:a");
        Thread.sleep(1000);
        selenium.close();
        selenium.selectWindow("");
       
        System.out.println(actual);
        System.out.println(expected);

        if (actual.equals(expected)) {
            System.out.println("ReadFeedtEntry - test passed");
        } else {
            System.out.println("ReadFeedtEntry - test failed");
        }
    }

    /*
     Accessing tryit to test feedReader2 service.
    */
    public static void testFeedGetEntriesLink() throws Exception {
       
        MSCommon.testAccessTryit("feedReader2");
        selenium.click("button_test");
        Thread.sleep(6000);
        String actual = selenium.getText("//div[@id='console_test']/div/div/span[2]");
        selenium.close();
        selenium.selectWindow("");

        String signinwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.openWindow("http://www.formula1.com/rss/news/headlines.rss",signinwinid);
		selenium.selectWindow(signinwinid);
        selenium.click("//x:div[@id='feedContent']/x:div[1]/x:h3/x:a");
        selenium.waitForPageToLoad("60000");
        String expected = selenium.getLocation();
        selenium.close();
        selenium.selectWindow("");
        System.out.println(actual);
        System.out.println(expected);

        if (expected.equals(actual)) {
            System.out.println("ReadFeedtEntry - test passed");
        } else {
            System.out.println("ReadFeedtEntry - test failed");
        }
    }

    /*
          	   EmailAllScenarios  Service
     */


    /*
     Accessing tryit to test EmailAllScenarios service.
    */
    public static void testEmailAllScenarios_Service(String fromParam,String linkName) throws Exception {
        selenium.click("link="+linkName);
		selenium.type("input_"+linkName+"_param_0", fromParam);
		selenium.click("button_"+linkName);
        Thread.sleep(40000);
    }

    /*
        Login in to hotmail account and read a Unread message.
     */
    public static void testLogginToInbox(String linkName) throws Exception{
        MSCommon.testAccessTryit("EmailAllScenarios");

        MSHostObjects.testEmailAllScenarios_Service("sarasi@wso2.com",linkName);
        assertTrue(selenium.isTextPresent("(null)"));
        selenium.close();
        selenium.selectWindow("");

        MSCommon.testLogginToAccounts("wso2mashupserver1@hotmail.com","password123");
        Thread.sleep(10000);

        selenium.click("//img[@alt='Unread']");
        Thread.sleep(10000); 
    }

    /*
        Delete that read meesage from inbox.
     */
    public static void testDeleteMsgfromInbox() throws Exception{
        selenium.click("//li[@id='00000000-0000-0000-0000-000000000001']/a/span");
        Thread.sleep(1000);
        selenium.click("msgChk");
		selenium.click("//a[@id='DeleteMessages']/span");
        MSCommon.testlogOutFromAccounts();
        selenium.close();
        selenium.selectWindow("");
    }

    /*
        request service
     */

    public static void testRequest_Service(String userName,String password) throws Exception{
       //Enable Security Scenario
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.enableSecurityScenario("admin/request","scenario1");
       //Accessing Tryit to request service
        MSCommon.testAccessTryit("quest");
        selenium.type("username", userName);
		selenium.type("password", password);
		selenium.click("link=returnAuthUser");
		selenium.click("button_returnAuthUser");
        String authUser=MSCommon.loadProperties().getProperty("requestAuthUser");
        if(authUser.equals(selenium.getText("console_returnAuthUser")))
            System.out.println("Passed");
        else
            System.out.println("Failed");

		selenium.click("link=returnIP");
		selenium.click("button_returnIP");
		String IP=MSCommon.loadProperties().getProperty("host.ip");
        if(IP.equals(selenium.getText("console_returnIP")))
            System.out.println("Passed");
        else
            System.out.println("Failed");

		selenium.click("link=returnURL");
		selenium.click("button_returnURL");
        String url="https://"+ IP +":"+MSCommon.loadProperties().getProperty("https.be.port")+MSCommon.loadProperties().getProperty("context.root")+"/services/admin/request.SecureSOAP12Endpoint/";
        if(url.equals(selenium.getText("console_returnURL")))
            System.out.println("Passed");
        else
            System.out.println("Failed");

        selenium.close();
        selenium.selectWindow("");
      //Disable Security Scenario
        serviceManagement.disableSecurity("admin/request");
    }

}
