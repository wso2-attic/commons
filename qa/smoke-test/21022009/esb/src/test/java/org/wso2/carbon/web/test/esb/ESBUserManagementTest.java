package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBUserManagementTest extends TestCase {
    Selenium selenium;

    public ESBUserManagementTest(Selenium _browser){
		selenium = _browser;
    }

    public void testPrepareUsers() throws Exception                 //Add new system user and login to wsas console
    {
        CreateUsers();
        CreateRoles();
        LogOutUI();
    }

    public void LoginToUI(String UserName, String Password) throws Exception   // Login to WSAS UI Console
    {
        selenium.open("/carbon/admin/login.jsp");
        selenium.waitForPageToLoad("30000");
        selenium.type("txtUserName", UserName);
        selenium.type("txtPassword", Password);
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
    }

    public void LogOutUI() {
        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");
    }

    public void CreateUsers() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user1", "userone");
        mySeleniumTestBase.addNewUser("user2", "usertwo");
        mySeleniumTestBase.addNewUser("user3", "userthree");
        mySeleniumTestBase.addNewUser("user4", "userfour");
        mySeleniumTestBase.addNewUser("user5", "userfive");
        mySeleniumTestBase.addNewUser("user6", "usersix");

    }


    public void CreateRoles() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role1", "user1", "Login to admin console");
        mySeleniumTestBase.addNewRole("Role2", "user2", "manage-configuration");
        mySeleniumTestBase.addNewRole("Role3", "user3", "manage-security");
        mySeleniumTestBase.addNewRole("Role4", "user4", "upload-services");
        mySeleniumTestBase.addNewRole("Role5", "user5", "manage-services");
        mySeleniumTestBase.addNewRole("Role6", "user6", "monitor-system");

    }


    public void testCheckPermission1() throws Exception    //Login to admin console
    {

        LoginToUI("user1", "userone");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));
        
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission2() throws Exception    // manage-configuration
    {
        LoginToUI("user2", "usertwo");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=Logging");
        selenium.waitForPageToLoad("90000");
        Thread.sleep(3000);
        assertTrue(selenium.isTextPresent("Logging Configuration"));
        selenium.click("link=Event Sources");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Event Sources"));

        selenium.click("link=Data Sources");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Data Sources"));

        selenium.click("link=Scheduled Tasks");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Scheduled Tasks"));

        selenium.click("link=Synapse");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Manage Synapse Configuration"));

        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));

        selenium.click("link=Transports");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Transport Management"));

        selenium.click("link=Shutdown/Restart");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Shutdown/Restart Server"));

        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Resources"));

        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Search"));

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission3() throws Exception  //manage-security
    {
        LoginToUI("user3", "userthree");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User Management"));

        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Key store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add New Key store"));
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=echo");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission4() throws Exception  //upload-services
    {
        LoginToUI("user4", "userfour");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        LogOutUI();
    }

    public void testCheckPermission5() throws Exception  //manage-services
    {
        LoginToUI("user5", "userfive");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=echo");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(5000);
        selenium.click("link=Modules");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Engage Modules to echo Service"));
        selenium.click("//div[@id='menu']/ul/li[3]/ul/li[4]/ul/li[1]/a");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Modules"));
        selenium.click("link=Add");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add a module"));

        LogOutUI();
    }

    public void testCheckPermission6() throws Exception  //monitor-system
    {
        LoginToUI("user6", "usersix");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));

        selenium.click("link=System Statistics");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(5000);
        assertTrue(selenium.isTextPresent("System Statistics"));
        assertTrue(selenium.isTextPresent("Average Response Time(ms) vs. Time(Units)"));

        selenium.click("link=Mediation Statistics");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Mediation Statistics"));

        selenium.click("link=System Logs");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("System Logs"));

        selenium.click("link=SOAP Tracer");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("SOAP Message Tracer"));

        selenium.click("link=Mediation Tracer");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Mediation Message Tracer"));

        selenium.click("link=Message Flows");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Message Flows (Graphical View)"));

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        
        LogOutUI();

    }

    public void testrenamePassword() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.addNewUser("Chamara", "Chamara");
        mySeleniumTestBase.addNewRole("wso2", "Chamara", "Login to admin console");

        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("Chamara", "Chamara");
        assertTrue(selenium.isTextPresent("Chamara@localhost:9443"));
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("admin", "admin");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");
        String readUserName = "";
        String user = "Chamara";
        int i = 1;
        while (!user.equals(readUserName)) {
            readUserName = selenium.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        selenium.click("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]/a[1]");
        selenium.waitForPageToLoad("30000");
        selenium.type("newPassword", "Chamara123");
        selenium.type("checkPassword", "Chamara123");
        selenium.click("//input[@value='Change']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Password of user " + user + " successfully changed"));
        selenium.click("//button[@type='button']");
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("Chamara", "Chamara123");
        assertTrue(selenium.isTextPresent("Signed-in as Chamara"));
        mySeleniumTestBase.logOutUI();
    }

    public void testDeleteUsers() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.deleteUser("user1");
        mySeleniumTestBase.deleteUser("user2");
        mySeleniumTestBase.deleteUser("user3");
        mySeleniumTestBase.deleteUser("user4");
        mySeleniumTestBase.deleteUser("user5");
        mySeleniumTestBase.deleteUser("user6");
        mySeleniumTestBase.deleteUser("Chamara");
    }

    public void testDeleteRole() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.deleteRole("Role1");
        mySeleniumTestBase.deleteRole("Role2");
        mySeleniumTestBase.deleteRole("Role3");
        mySeleniumTestBase.deleteRole("Role4");
        mySeleniumTestBase.deleteRole("Role5");
        mySeleniumTestBase.deleteRole("Role6");
        mySeleniumTestBase.deleteRole("wso2");

    }

    public void testLogout() {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.logOutUI();
    }
}


