package org.wso2.carbon.web.test.bps;

import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
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

public class BPSUserManagementTest extends CommonSetup{
    public BPSUserManagementTest(String text) {
        super(text);
    }


    public void testPrepareUsers() throws Exception
    {
        CreateUsers();
        CreateRoles();
       // LogOutUI();
    }

    public void LoginToUI(String UserName, String Password) throws Exception
    {
        browser.open("/carbon/admin/login.jsp");
        browser.waitForPageToLoad("30000");
        browser.type("txtUserName", UserName);
        browser.type("txtPassword", Password);
        browser.click("//input[@value='Sign-in']");
        browser.waitForPageToLoad("30000");
    }

    public void LogOutUI() {
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("30000");
    }

    public void CreateUsers() throws Exception
    {
        SeleniumTestBase stb = new SeleniumTestBase(browser);
        LoginToUI("admin", "admin");
        stb.addNewUser("user1", "userone");
        stb.addNewUser("user2", "usertwo");
        stb.addNewUser("user3", "userthree");
        stb.addNewUser("user4", "userfour");
        stb.addNewUser("user5", "userfive");
        stb.addNewUser("user6", "usersix");
        LogOutUI();

    }


    public void CreateRoles() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase stb = new SeleniumTestBase(browser);
        LoginToUI("admin", "admin");
        stb.addNewRole("Role1", "user1", "Login to admin console");
        stb.addNewRole("Role2", "user2", "manage-configuration");
        stb.addNewRole("Role3", "user3", "manage-security");
        stb.addNewRole("Role4", "user4", "upload-services");
        stb.addNewRole("Role5", "user5", "manage-services");
        stb.addNewRole("Role6", "user6", "monitor-system");
        LogOutUI();

    }


    public void testCheckPermission1() throws Exception
    {

        LoginToUI("user1", "userone");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));

        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission2() throws Exception
    {

         boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        LoginToUI("user2", "usertwo");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=Logging");
        browser.waitForPageToLoad("90000");
        Thread.sleep(3000);
        assertTrue(browser.isTextPresent("Logging Configuration"));
        browser.click("link=Data Sources");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Data Sources"));

        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));

        browser.click("link=Transports");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Transport Management"));

        browser.click("link=Shutdown/Restart");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Shutdown/Restart Server"));

     //   browser.click("link=Browse");
     //   browser.waitForPageToLoad("30000");
      //  assertTrue(browser.isTextPresent("Browse"));

     //   browser.click("link=Search");
     //   browser.waitForPageToLoad("30000");
      //  assertTrue(browser.isTextPresent("Search"));

        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission3() throws Exception
    {
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        LoginToUI("user3", "userthree");
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
        browser.click("link=LoanService");
        browser.waitForPageToLoad("30000");

        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission4() throws Exception
    {
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        LoginToUI("user4", "userfour");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));

        browser.click("link=Try It");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission5() throws Exception
    {
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }

        boolean result = false;

        LoginToUI("user5", "userfive");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("link=LoanService");
        browser.waitForPageToLoad("30000");
        Thread.sleep(5000);
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");

        if (browser.isTextPresent("Engage Modules to LoanService")) {
            result = true;
        }

        if (result == true) {
            assertTrue(browser.isTextPresent("Engage Modules to LoanService"));
            browser.click("//div[@id='menu']/ul/li[3]/ul/li[4]/ul/li[1]/a");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Deployed Modules"));
            browser.click("link=Add");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Add a module"));

            LogOutUI();
        }

        else {
            assertFalse ("Engage Modules to LoanService not found", result);
            LogOutUI();
        }
    }

    public void testCheckPermission6() throws Exception
    {
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        LoginToUI("user6", "usersix");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Services"));

        browser.click("link=System Statistics");
        browser.waitForPageToLoad("30000");
        Thread.sleep(5000);
        assertTrue(browser.isTextPresent("System Statistics"));
        assertTrue(browser.isTextPresent("Average Response Time(ms) vs. Time(Units)"));

        browser.click("link=System Logs");
        browser.waitForPageToLoad("30000");
        Thread.sleep(10000);
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

        LogOutUI();

    }

    public void testrenamePassword() throws Exception {
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        SeleniumTestBase stb = new SeleniumTestBase(browser);
        stb.loginToUI("admin", "admin");
        stb.addNewUser("wso2user", "wso2user");
        stb.addNewRole("wso2", "wso2user", "Login to admin console");

        stb.logOutUI();
        stb.loginToUI("wso2user", "wso2user");
        assertTrue(browser.isTextPresent("wso2user"));
        stb.logOutUI();
        stb.loginToUI("admin", "admin");
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Users");
        browser.waitForPageToLoad("30000");
        String readUserName = "";
        String user = "wso2user";
        int i = 1;
        while (!user.equals(readUserName)) {
            readUserName = browser.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]/a[1]");
        browser.waitForPageToLoad("30000");
        browser.type("newPassword", "wso2123");
        browser.type("checkPassword", "wso2123");
        browser.click("//input[@value='Change']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Password of user " + user + " successfully changed"));
        browser.click("//button[@type='button']");
        stb.logOutUI();
        stb.loginToUI("wso2user", "wso2123");
        assertTrue(browser.isTextPresent("Signed-in as wso2user"));
        stb.logOutUI();
    }

    public void testDeleteUsers() throws Exception {
        SeleniumTestBase stb = new SeleniumTestBase(browser);
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        stb.loginToUI("admin", "admin");
        stb.deleteUser("user1");
        stb.deleteUser("user2");
        stb.deleteUser("user3");
        stb.deleteUser("user4");
        stb.deleteUser("user5");
        stb.deleteUser("user6");
        stb.deleteUser("wso2user");
        stb.logOutUI();
    }

    public void testDeleteRole() throws Exception {
        SeleniumTestBase stb = new SeleniumTestBase(browser);
        boolean login = browser.isTextPresent("Sign-out");

        if (login){
            LogOutUI();
        }
        stb.loginToUI("admin", "admin");
        stb.deleteRole("Role1");
        stb.deleteRole("Role2");
        stb.deleteRole("Role3");
        stb.deleteRole("Role4");
        stb.deleteRole("Role5");
        stb.deleteRole("Role6");
        stb.deleteRole("wso2");
        stb.logOutUI();

    }

//    public void testLogout() {
//        SeleniumTestBase stb = new SeleniumTestBase(browser);
//        stb.logOutUI();
//    }
}
