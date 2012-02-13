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

public class ModulesTest extends SeleneseTestCase {
    //Selenium selenium;

    //Initialize the browser
    public void setUp() throws Exception {
        selenium = BrowserInitializer.getbrowser();
    }


    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests the UI in 'Modules -List' page */
    public void testModulesUI() throws Exception {
        assertTrue(selenium.isTextPresent("Modules"));
        selenium.click("//div[@id='menu']/ul/li[5]/ul/li[4]/ul/li[1]/a");  //Clicking the 'List' link under'Modules'
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Modules"));
        assertEquals("Name", selenium.getText("//table[@id='moduleTable']/thead/tr/th[1]"));
        assertEquals("Version", selenium.getText("//table[@id='moduleTable']/thead/tr/th[2]"));
        assertEquals("Description", selenium.getText("//table[@id='moduleTable']/thead/tr/th[3]"));
        assertEquals("Actions", selenium.getText("//table[@id='moduleTable']/thead/tr/th[4]"));
        assertTrue(selenium.isElementPresent("link=wso2mex"));
        assertTrue(selenium.isElementPresent("link=savan"));
        assertTrue(selenium.isElementPresent("link=wso2caching"));
        assertTrue(selenium.isElementPresent("link=wso2xfer"));
        assertTrue(selenium.isElementPresent("link=addressing"));
        assertTrue(selenium.isElementPresent("link=sandesha2"));
        assertTrue(selenium.isElementPresent("link=rampart"));
        assertTrue(selenium.isElementPresent("link=rahas"));
        assertEquals("Globally Engaged Modules", selenium.getText("//table[@id='globalModules']/thead/tr/th"));
        assertEquals("addressing-1.41", selenium.getText("//table[@id='globalModules']/tbody/tr/td"));
    }


   /* Test engaging\disengaging wso2mex-1.00 */
    public void testEngageWso2Mex() throws Exception {
        selenium.open("/carbon/modulemgt/index.jsp");
        selenium.click("//div[@id='menu']/ul/li[5]/ul/li[4]/ul/li[1]/a");
        selenium.waitForPageToLoad("30000");

        //Engaging the wso2mex module
        assertTrue(selenium.isTextPresent("Engage"));
        selenium.click("link=Engage");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Module was successfully engaged globally."));
        selenium.click("//button[@type='button']");

        //In successful engaging the moodule should appear in the global modules table.
        String mex_en_table1 = selenium.getText("//table[@id='globalModules']/tbody/tr[1]/td");
        boolean mex_en_link = selenium.isElementPresent("link=Disengage");
        if ((mex_en_table1 == "savan-1.00") && (mex_en_link)) {
            System.out.println("savan was successfully engaged ----------- Test PASSED");
        } else {
            System.out.println("savan was NOT successfully engaged-------- Test FAILED");
        }

        //Dis-engaging the wso2mex module
        assertTrue(selenium.isTextPresent("Disengage"));
        selenium.click("link=Disengage");
        assertTrue(selenium.isElementPresent("messagebox-confirm"));
        assertTrue(selenium.isTextPresent("Do you want to globally disengage wso2mex-1.00 module ?"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Module was successfully disengaged globally."));
        selenium.click("//button[@type='button']");

        //In successful dis-engaging the moodule should disappear from the global modules table.
        String mex_en_table2 = selenium.getText("//table[@id='globalModules']/tbody/tr/td");
        boolean mex_dis_link = selenium.isElementPresent("link=Engage");
        if ((mex_en_table2 != "wso2mex-1.00") && (mex_dis_link)) {
            System.out.println("wso2mex-1.00 was successfully dis-engaged ----------- Test PASSED");
        } else {
            System.out.println("wso2mex-1.00 was NOT successfully dis-engaged-------- Test FAILED");
        }
    }


   /* Test engaging\disengaging savan */
    public void testEngageSavan() throws Exception {
        selenium.click("//div[@id='menu']/ul/li[5]/ul/li[4]/ul/li[1]/a");
        selenium.waitForPageToLoad("30000");

        //Check if 'engage' link is available under 'savan' and click it
        assertTrue(selenium.isElementPresent("link=Engage"));
        selenium.click("link=Engage");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Module was successfully engaged globally."));
        selenium.click("//button[@type='button']");

        //Checks if the savan appears in the global modules table and the link changes to 'Disengage'.
        String savan_en_table1 = selenium.getText("//table[@id='globalModules']/tbody/tr[1]/td");
        boolean savan_en_link = selenium.isElementPresent("link=Disengage");
        if ((savan_en_table1 == "savan-1.00") && (savan_en_link)) {
            System.out.println("savan was successfully engaged ----------- Test PASSED");
        } else {
            System.out.println("savan was NOT successfully engaged-------- Test FAILED");
        }

        //Diengage the module
        selenium.click("link=Disengage");
        assertTrue(selenium.isElementPresent("messagebox-confirm"));
        assertTrue(selenium.isTextPresent("Do you want to globally disengage savan-1.00 module ?"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("messagebox-info"));
        assertTrue(selenium.isTextPresent("Module was successfully disengaged globally."));
        selenium.click("//button[@type='button']");

        // Action link should be chanaged back to 'Engage'
        String savan_en_table2 = selenium.getText("//table[@id='globalModules']/tbody/tr[1]/td");
        boolean savan_dis_link = selenium.isElementPresent("link=Engage");
        if ((savan_en_table2 != "savan-1.00") && (savan_en_link)) {
            System.out.println("savan was successfully diengaged ----------- Test PASSED");
        } else {
            System.out.println("savan was NOT successfully disengaged-------- Test FAILED");
        }
    }


    public void testCaching() throws Exception {
        /* set the caching values
         go to service listing
         take abt 2 services and check if service level caching is enabled and all operation level caching are enabled.
         call caching client and see if the caching works (client asks for system time)
         disable caching and check in two services/their operations whether its disabled
        */
		assertTrue(selenium.isElementPresent("link=Configure"));
		selenium.click("link=Configure");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Global Caching Configuration"));
    }
}