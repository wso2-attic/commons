package org.wso2.carbon.web.test.gs;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

public class UserManagementTest extends TestCase {

    Selenium browser;

    public void setUp() throws Exception {
        browser = BrowserInitializer.getbrowser();
    }

    //CSHelp
     public void testCSHelp() throws Exception{
           SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
           mySeleniumTestBase.loginToUI("admin", "admin");
           browser.click("link=User Management");
           browser.waitForPageToLoad("30000");
           String expectedForCSHelp="https://"+GSCommon.loadProperties().getProperty("host.name")+":"+GSCommon.loadProperties().getProperty("https.port")+"/carbon/userstore/docs/userguide.html";
           browser.click("link=User Management");
           browser.waitForPageToLoad("30000");
           assertTrue(browser.isTextPresent("Help"));
           browser.click("link=Help");
           String helpwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
           browser.selectWindow(helpwinid);
           Thread.sleep(10000);
           assertTrue(browser.isTextPresent("User Management"));
           String actualForCSHelp = browser.getLocation();
           if(actualForCSHelp.equals(expectedForCSHelp))
               System.out.println("Actual location & expected location are matched");
           else
               System.out.println("Actual location & expected location are not matched");
           browser.close();
           browser.selectWindow("");
           mySeleniumTestBase.logOutUI();
       }


    public void testLogin() throws Exception                 //Add new system user and login to wsas console
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.loginToUI("admin", "admin");

    }

    public void testCreateUsers1() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user1", "userone");
     }
     public void testCreateUsers2() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user2", "usertwo");
    }
     public void testCreateUsers3() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user3", "userthree");
    }
     public void testCreateUsers4() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user4", "userfour");
    }
     public void testCreateUsers5() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user5", "userfive");
    }
     public void testCreateUsers6() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user6", "usersix");
    }

     public void testCreateRoles1() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role1", "user1", "Login to admin console");
  }
    public void testCreateRoles2() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role2", "user2", "manage-configuration");

    }
    public void testCreateRoles3() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role3", "user3", "manage-security");

    }
    public void testCreateRoles4() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role4", "user4", "upload-services");

    }
    public void testCreateRoles5() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role5", "user5", "manage-services");

    }
    public void testCreateRoles6() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role6", "user6", "monitor-system");
    }

    public void testCheckPermission1() throws Exception    //Login to admin console
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user1", "userone");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));
        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        browser.click("link=JavaScript Stub Generator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("JavaScript Stub Generator"));
        browser.click("link=Scraping Assistant");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Scraping Assistant"));
    }

    public void testCheckPermission2() throws Exception    // manage-configuration
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user2", "usertwo");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=Logging");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Logging Configuration"));
        assertTrue(browser.isTextPresent("Service"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");  // add a test to validate the accessibility to services dashboard, service grps.
        assertTrue(browser.isTextPresent("Deployed Services"));
        browser.click("link=Transports");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Transport Management"));
        browser.click("link=Shutdown/Restart");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Shutdown/Restart Server"));
        browser.click("link=Search");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Search"));
        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        browser.click("link=JavaScript Stub Generator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("JavaScript Stub Generator"));
        browser.click("link=Scraping Assistant");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Scraping Assistant"));
    }

    public void testCheckPermission3() throws Exception  //manage-security
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user3", "userthree");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("User Management"));
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");
        browser.click("link=Add New Key store");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Key store"));
        browser.click("//input[@value='Cancel']");
        browser.waitForPageToLoad("30000");
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        browser.click("link=JavaScript Stub Generator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("JavaScript Stub Generator"));
        browser.click("link=Scraping Assistant");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Scraping Assistant"));
    }

    public void testCheckPermission4() throws Exception  //upload-services
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user4", "userfour");
        assertTrue(browser.isElementPresent("menu-panel"));
        assertTrue(browser.isTextPresent("Service"));
        assertTrue(browser.isElementPresent("link=List"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));
 //       assertTrue(selenium.isElementPresent("link=Create"));
//        selenium.click("link=Create");
//        selenium.waitForPageToLoad("30000");
//        assertTrue(selenium.isTextPresent("New Mashup"));
        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        browser.click("link=JavaScript Stub Generator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("JavaScript Stub Generator"));
        browser.click("link=Scraping Assistant");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Scraping Assistant"));
    }

    public void testCheckPermission5() throws Exception  //manage-services
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user5", "userfive");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        browser.click("link=JavaScript Stub Generator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("JavaScript Stub Generator"));
        browser.click("link=Scraping Assistant");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Scraping Assistant"));
    }

    public void testCheckPermission6() throws Exception  //monitor-system
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user6", "usersix");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=System Statistics");
        browser.waitForPageToLoad("30000");
        Thread.sleep(3000);
        assertTrue(browser.isTextPresent("System Statistics"));
        assertTrue(browser.isTextPresent("Average Response Time(ms) vs. Time(Units)"));
        browser.click("link=System Logs");
        browser.waitForPageToLoad("30000");
        Thread.sleep(8000);
        assertTrue(browser.isTextPresent("System Logs"));
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("SOAP Message Tracer"));
        browser.click("link=Message Flows");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Message Flows (Graphical View)"));
        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        browser.click("link=JavaScript Stub Generator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("JavaScript Stub Generator"));
        browser.click("link=Scraping Assistant");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Scraping Assistant"));
        mySeleniumTestBase.logOutUI();
    }

    public void testrenamePassword() throws Exception {
      try
        {
            SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);

        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.addNewUser("Yumani", "Yumani");
        mySeleniumTestBase.addNewRole("wso2", "Yumani", "Login to admin console");

        mySeleniumTestBase.logOutUI();
        Thread.sleep(3000);
        mySeleniumTestBase.loginToUI("Yumani", "Yumani");
        Thread.sleep(3000);
        assertTrue(browser.isTextPresent("Yumani@"));
        mySeleniumTestBase.logOutUI();
        browser.waitForPageToLoad("30000");
        mySeleniumTestBase.loginToUI("admin", "admin");
        browser.waitForPageToLoad("30000");
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Users");
        browser.waitForPageToLoad("30000");
        String readUserName = "";
        String user = "Yumani";
        int i = 1;
        while (!user.equals(readUserName)) {
            readUserName = browser.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]/a[1]");
        browser.waitForPageToLoad("30000");
        browser.type("newPassword", "Yumani123");
        browser.type("checkPassword", "Yumani123");
        browser.click("//input[@value='Change']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Password of user " + user + " successfully changed"));
        browser.click("//button[@type='button']");
        mySeleniumTestBase.logOutUI();
        browser.waitForPageToLoad("30000");
       mySeleniumTestBase.loginToUI("Yumani", "Yumani123");
       browser.waitForPageToLoad("30000");
       assertTrue(browser.isTextPresent("Yumani@"));
        Thread.sleep(1000);
            mySeleniumTestBase.logOutUI();
        }
      catch (Exception e)
          {
            SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
            mySeleniumTestBase.logOutUI();
          }
    }

    public void testDeleteUsers() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.deleteUser("user1");
        mySeleniumTestBase.deleteUser("user2");
        mySeleniumTestBase.deleteUser("user3");
        mySeleniumTestBase.deleteUser("user4");
        mySeleniumTestBase.deleteUser("user5");
        mySeleniumTestBase.deleteUser("user6");
        mySeleniumTestBase.deleteUser("Yumani");
    }

    public void testDeleteRole() throws Exception {
      SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.deleteRole("Role1");
        mySeleniumTestBase.deleteRole("Role2");
        mySeleniumTestBase.deleteRole("Role3");
        mySeleniumTestBase.deleteRole("Role4");
        mySeleniumTestBase.deleteRole("Role5");
        mySeleniumTestBase.deleteRole("Role6");
        mySeleniumTestBase.deleteRole("wso2");

    }

    public void testLogout()throws Exception{
       SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        Thread.sleep(5000);
        mySeleniumTestBase.logOutUI();
    }
}