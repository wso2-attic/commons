package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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


public class ISMultifactorAuthenticationTest extends CommonSetup{

     public ISMultifactorAuthenticationTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    /* CSHelp */
    public void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/multi-factor/docs/userguide.html";
        selenium.click("link=Multifactor Authentication");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("Multifactor Authentication Configurations"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

    //Test Multifactor Authentication Functionality.
    public void testMultifacAuthentication() throws Exception{
        selenium.click("link=Multifactor Authentication");
		selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent("Enable XMPP based multi-factor authentication."));
		selenium.click("enablexmppmultifact");

		selenium.click("addbutton");
		assertTrue(selenium.isTextPresent("User name is required."));
		selenium.click("//button[@type='button']");
		selenium.type("username", "wso2isserver@gmail.com");
		selenium.click("addbutton");

		assertTrue(selenium.isTextPresent("PIN Number is required."));
		selenium.click("//button[@type='button']");
		selenium.type("usercode", "123456");
		selenium.click("addbutton");

		assertTrue(selenium.isTextPresent("Please retype the PIN number."));
		selenium.click("//button[@type='button']");
		selenium.type("reusercode", "123456");
		selenium.click("enablePIN");
		selenium.click("addbutton");
		selenium.waitForPageToLoad("30000");

		assertTrue(selenium.isTextPresent("XMPP configurations added successfully"));
		selenium.click("//button[@type='button']");
    }

    //Log out from the admin console.
    public void testLogout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }
}