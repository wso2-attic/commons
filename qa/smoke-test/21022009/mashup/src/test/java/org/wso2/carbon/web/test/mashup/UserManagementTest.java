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

public class UserManagementTest extends SeleneseTestCase {
    Selenium selenium;

    //Initialize the browser
    public void setUp() throws Exception {
        selenium = BrowserInitializer.getBrowser();
    }


    //Login to admin console and check the environment
    public void testRun() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        instSeleniumTestBase.loginToUI("admin", "admin");
    }


    //Add new system user and login to Mashup server console
    public void testPrepareUsers() throws Exception {
        LoginToUI("admin", "admin");
        CreateUsers();
        CreateRoles();
        LogOutUI();
    }


    // Login to MS UI Console
    public void LoginToUI(String UserName, String Password) throws Exception {
        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", UserName);
        selenium.type("txtPassword", Password);
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
    }


    //Logout from UI console
    public void LogOutUI() {
        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");
    }


    // Create users to check user permissions
    public void CreateUsers() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user1", "userone");
        mySeleniumTestBase.addNewUser("user2", "usertwo");
        mySeleniumTestBase.addNewUser("user3", "userthree");
        mySeleniumTestBase.addNewUser("user4", "userfour");
        mySeleniumTestBase.addNewUser("user5", "userfive");
        mySeleniumTestBase.addNewUser("user6", "usersix");

    }


    //Create roles to check user permissions
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


    //Login as user1 and check the permissions (Login to admin console).
    public void testCheckPermission1() throws Exception {
        LoginToUI("user1", "userone");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));
        selenium.click("link=WSDL2Java");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL2Java"));
        selenium.click("link=Java2WSDL");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Java2WSDL"));
        selenium.click("link=WSDL Converter");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL Converter"));
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        selenium.click("link=Service Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Service Archive Validator"));
        selenium.click("link=JavaScript Stub Generator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
        selenium.click("link=Module Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Module Archive Validator"));
    }


    //Login as user2 and check the permissions (manage-configuration).
    public void testCheckPermission2() throws Exception {
        LogOutUI();
        LoginToUI("user2", "usertwo");
        selenium.click("link=Logging");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Logging Configuration"));
        assertTrue(selenium.isTextPresent("Service"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");  // add a test to validate the accessibility to services dashboard, service grps.
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
        selenium.click("link=WSDL2Java");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL2Java"));
        selenium.click("link=Java2WSDL");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Java2WSDL"));
        selenium.click("link=WSDL Converter");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL Converter"));
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        selenium.click("link=Service Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Service Archive Validator"));
        selenium.click("link=JavaScript Stub Generator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
        selenium.click("link=Module Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Module Archive Validator"));
    }


    //Login as user3 and check the permissions (manage-security).
    public void testCheckPermission3() throws Exception {
        LogOutUI();
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
        selenium.click("link=digit2image");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=WSDL2Java");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL2Java"));
        selenium.click("link=Java2WSDL");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Java2WSDL"));
        selenium.click("link=WSDL Converter");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL Converter"));
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        selenium.click("link=Service Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Service Archive Validator"));
        selenium.click("link=JavaScript Stub Generator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
        selenium.click("link=Module Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Module Archive Validator"));
    }


    //Login as user4 and check the permissions (upload-services).
    public void testCheckPermission4() throws Exception {
        LogOutUI();
        LoginToUI("user4", "userfour");
        assertTrue(selenium.isElementPresent("menu-panel"));
        assertTrue(selenium.isTextPresent("Service"));
        assertTrue(selenium.isElementPresent("link=List"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add"));
        assertTrue(selenium.isTextPresent("JavaScript Service"));
        assertTrue(selenium.isElementPresent("link=Create"));
        selenium.click("link=Create");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("New Mashup"));
        selenium.waitForPageToLoad("30000");
        selenium.click("link=WSDL2Java");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL2Java"));
        selenium.click("link=Java2WSDL");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Java2WSDL"));
        selenium.click("link=WSDL Converter");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL Converter"));
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        selenium.click("link=Service Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Service Archive Validator"));
        selenium.click("link=JavaScript Stub Generator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
        selenium.click("link=Module Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Module Archive Validator"));
    }


    //Login as user5 and check the permissions (manage-services).
    public void testCheckPermission5() throws Exception {
        LogOutUI();
        LoginToUI("user5", "userfive");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=digit2image");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(3000);
        assertTrue(selenium.isTextPresent("Service Dashboard (digit2image)"));
        selenium.click("link=Access Throttling");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Service Dashboard");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Modules");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Engage Modules to digit2image Service"));
        selenium.click("//div[@id='menu']/ul/li[3]/ul/li[4]/ul/li[1]/a");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Modules"));
        selenium.click("link=Add");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add a module"));
        selenium.click("link=WSDL2Java");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL2Java"));
        selenium.click("link=Java2WSDL");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Java2WSDL"));
        selenium.click("link=WSDL Converter");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL Converter"));
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        selenium.click("link=Service Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Service Archive Validator"));
        selenium.click("link=JavaScript Stub Generator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
        selenium.click("link=Module Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Module Archive Validator"));
    }


    //Login as user6 and check the permissions (monitor-system).
    public void testCheckPermission6() throws Exception {
        LogOutUI();
        LoginToUI("user6", "usersix");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=System Statistics");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(3000);
        assertTrue(selenium.isTextPresent("System Statistics"));
        assertTrue(selenium.isTextPresent("Average Response Time(ms) vs. Time(Units)"));
        selenium.click("link=System Logs");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(8000);
        assertTrue(selenium.isTextPresent("System Logs"));
        selenium.click("link=SOAP Tracer");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("SOAP Message Tracer"));
        selenium.click("link=Message Flows");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Message Flows (Graphical View)"));
        selenium.click("link=WSDL2Java");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL2Java"));
        selenium.click("link=Java2WSDL");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Java2WSDL"));
        selenium.click("link=WSDL Converter");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("WSDL Converter"));
        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        selenium.click("link=Service Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Service Archive Validator"));
        selenium.click("link=JavaScript Stub Generator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
        selenium.click("link=Module Validator");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Module Archive Validator"));
        LogOutUI();
    }

    //Rename the user password and test login
    public void testRenamePassword() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.addNewUser("tester", "tester");
        mySeleniumTestBase.addNewRole("tester", "tester", "Login to admin console");

        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("tester", "tester");
        assertTrue(selenium.isTextPresent("Signed-in as tester"));
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("admin", "admin");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");
        String readUserName = "";
        String user = "tester";
        int i = 1;
        while (!user.equals(readUserName)) {
            readUserName = selenium.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        selenium.click("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]/a[1]");
        selenium.waitForPageToLoad("30000");
        selenium.type("newPassword", "tester1");
        selenium.type("checkPassword", "tester1");
        selenium.click("//input[@value='Change']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Password of user " + user + " successfully changed"));
        selenium.click("//button[@type='button']");
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("tester", "tester1");
        assertTrue(selenium.isTextPresent("Signed-in as tester"));
    }


    //DElete all users
    public void testDeleteUsers() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.deleteUser("user1");
        mySeleniumTestBase.deleteUser("user2");
        mySeleniumTestBase.deleteUser("user3");
        mySeleniumTestBase.deleteUser("user4");
        mySeleniumTestBase.deleteUser("user5");
        mySeleniumTestBase.deleteUser("user6");
        mySeleniumTestBase.deleteUser("tester");
    }


    //Delete all roles
    public void testDeleteRole() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.deleteRole("Role1");
        mySeleniumTestBase.deleteRole("Role2");
        mySeleniumTestBase.deleteRole("Role3");
        mySeleniumTestBase.deleteRole("Role4");
        mySeleniumTestBase.deleteRole("Role5");
        mySeleniumTestBase.deleteRole("Role6");
        mySeleniumTestBase.deleteRole("tester");
    }


    //Logout
    public void testLogout() {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.logOutUI();
    }


    //Try to login from a deleted user account
    public void testLoginInvalid() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("tester", "tester1");
        assertTrue(selenium.isTextPresent("Login failed! Please recheck the username and password and try again."));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
    }


}