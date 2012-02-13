package org.wso2.carbon.web.test.mashup;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;


public class MSNewServiceTest extends CommonSetup {

    public MSNewServiceTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


     /*
       CSHelp
     */
    public void testCSHelp() throws Exception{
       String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/js_service/docs/userguide.html";
       selenium.click("link=Create");
       selenium.waitForPageToLoad("30000");
       assertTrue(selenium.isTextPresent("Help"));
       selenium.click("link=Help");
       String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
       selenium.selectWindow(helpwinid);
       Thread.sleep(10000);
       assertTrue(selenium.isTextPresent("Javascript service creation"));
       String actualForCSHelp = selenium.getLocation();
       if(actualForCSHelp.equals(expectedForCSHelp))
           System.out.println("Actual location & expected location are matched");
       else
           System.out.println("Actual location & expected location are not matched");
       selenium.close();
       selenium.selectWindow("");
    }
    
    public void testService() throws Exception {

       //Add new service and delete that service from the list.
        MSCommon.testCreateNewService("newService");
        MSCommon.testDelete("newService");

       //Test custom UI and gadget UI for "allCommons" service. 
        MSCommon.testCustom_UI_Gadget_UICode_ForService("allCommons");

      //Upload new service and delete that service from the list.
        MSCommon.testNewService_Upload("newService_upload");
        MSCommon.testDelete("newService_upload");

      //Upload faulty service.
        MSCommon.testFaultyService_Upload("faulty_upload");

       //Search and delete faulty service.
        MSCommon.testFaultyService();
        MSCommon.testDeleteFaultyService();
    }


    //Logout from Mashup Server.
    public void testSignout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

}



