package org.wso2.carbon.web.test.mashup;

import org.wso2.carbon.web.test.mashup.CommonSetup;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
import java.awt.event.KeyEvent;


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

public class MSHostObjectsTest extends CommonSetup {

     public MSHostObjectsTest(String text) {
        super(text);
    }

     /*
    *  Sign-in to Mashup Server admin console
     */
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    /*
          	   Feed  Service
     */
    public void testFeedHostObject() throws Exception {
        MSHostObjects.testReadFeedtEntry();
        MSHostObjects.testFeedGetEntriesLink();

    }

    /*
          	   EmailAllScenarios  Service

     */

     /*
         sendEmailwithJPG
      */
     public void testsendEmailwithJPG‏() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmailwithJPG");
        assertEquals("Mashup server - sendEmailwithJPG‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
  	  assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver2@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
		assertEquals("text.jpg", selenium.getText("link=text.jpg"));
        MSHostObjects.testDeleteMsgfromInbox();
     }

     /*
         sendEmail_ArrayOfTo
      */
     public void testsendEmail_ArrayOfTo‏() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmail_ArrayOfTo");
		assertEquals("Mashup server - sendEmail_ArrayOfTo‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
		assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@gmail.com; wso2mashupserver2@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver1@hotmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
        MSHostObjects.testDeleteMsgfromInbox();
     }

    /*
         sendEmailwithManyFiles‏‏
      */
    public void testsendEmailwithManyFiles‏‏() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmailwithManyFiles");
		assertEquals("Mashup server - sendEmailwithManyFiles‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
		assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver2@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
		assertEquals("text.jpg", selenium.getText("link=text.jpg"));
		assertEquals("temp.txt", selenium.getText("link=temp.txt"));
        MSHostObjects.testDeleteMsgfromInbox();
     }

    /*
        sendEmail_HostUserPass
     */
    public void testsendEmail_HostUserPass‏‏() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmail_HostUserPass");
		assertEquals("Mashup server - sendEmail_HostUserPass‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
		assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver2@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
        MSHostObjects.testDeleteMsgfromInbox();
     }

    /*
       sendEmail_ArrayOfCC‏
     */
    public void testsendEmail_ArrayOfCC‏() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmail_ArrayOfCC");
        assertEquals("Mashup server - sendEmail_ArrayOfCC‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
		assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@hotmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver1@gmail.com; wso2mashupserver2@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
        MSHostObjects.testDeleteMsgfromInbox();
     }
   /*
        sendEmail_HostPortUserPass
     */
    public void testsendEmail_HostPortUserPass() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmail_HostPortUserPass");
        assertEquals("Mashup server - sendEmail_HostPortUserPass‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
		assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver2@gmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
        MSHostObjects.testDeleteMsgfromInbox();
    }

    /*
        sendEmail_ArrayOfBCC
     */
    public void testsendEmail_ArrayOfBCC() throws Exception {
        MSHostObjects.testLogginToInbox("sendEmail_ArrayOfBCC");
        assertEquals("Mashup server - sendEmail_ArrayOfBCC‏", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/div"));
		assertEquals("To:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[1]"));
		assertEquals("wso2mashupserver1@hotmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[3]/td[2]"));
		assertEquals("Cc:", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[1]"));
		assertEquals("wso2mashupserver2@hotmail.com", selenium.getText("//div[@id='readingPaneContentContainer']/div/div[2]/table/tbody/tr[4]/td[2]"));
        MSHostObjects.testDeleteMsgfromInbox();
    }

    /*
        request service
     */
    public void testRequestService() throws Exception{
        MSHostObjects.testRequest_Service("admin","admin");
    }

    /*
        Sign-out from Mashup server
     */
    public void testSignout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

}
