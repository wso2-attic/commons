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

package org.wso2.carbon.web.test.wsas;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.Properties;
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

public class UserManagementTest extends TestCase {
   

    Selenium browser;
    Properties property;
    String username;
    String password;

    public  UserManagementTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    public void testLogin() throws Exception                 //Add new system user and login to wsas console
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.loginToUI(username, password);

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
        browser.click("link=WSDL2Java");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("WSDL2Java"));
        browser.click("link=Java2WSDL");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Java2WSDL"));
        browser.click("link=WSDL Converter");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("WSDL Converter"));
        browser.click("link=Service Validator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service Archive Validator"));
        browser.click("link=Module Validator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module Archive Validator"));


    }

    public void testCheckPermission2() throws Exception    // manage-configuration
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user2", "usertwo");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=Logging");
        browser.waitForPageToLoad("90000");
        Thread.sleep(3000);
        assertTrue(browser.isTextPresent("Logging Configuration"));
        browser.click("link=Shutdown/Restart");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Shutdown/Restart Server"));
        browser.click("link=Browse");                   //changed resourses to Browse
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Browse"));       //changed Resources to Browse
        browser.click("link=Search");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Search"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        // browser.open("/carbon/admin/login.jsp");


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
        browser.click("link=HelloService");
        browser.waitForPageToLoad("30000");


    }

    public void testCheckPermission4() throws Exception  //upload-services
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user4", "userfour");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=POJO Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add POJO Services"));
        browser.click("link=JAX-WS Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add JAX-WS Service"));
        browser.click("link=Axis1 Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("New Axis1 Service"));
        browser.click("link=Create");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Create Data Service"));
        browser.click("link=Upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Upload Data Service"));
        browser.click("link=Spring Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add Spring Service"));
        browser.click("link=EJB Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("EJB Service"));
        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add a Service Archive"));


    }

    public void testCheckPermission5() throws Exception  //manage-services
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("user5", "userfive");
        assertTrue(browser.isElementPresent("menu-panel"));
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("link=HelloService");
        browser.waitForPageToLoad("30000");
        Thread.sleep(3000);
        assertTrue(browser.isTextPresent("Service Dashboard (HelloService)"));
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");
        browser.click("link=Service Dashboard");
        browser.waitForPageToLoad("30000");
        //browser.click("link=Modules");
        // browser.waitForPageToLoad("30000");
        //    assertTrue(browser.isTextPresent("Engage Modules to HelloService Service"));
        //   browser.click("//div[@id='menu']/ul/li[3]/ul/li[4]/ul/li[1]/a");
        //  browser.waitForPageToLoad("30000");
        //   assertTrue(browser.isTextPresent("Deployed Modules"));
        // browser.click("link=Add");
        //   browser.waitForPageToLoad("30000");
        //   assertTrue(browser.isTextPresent("Add a module"));


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
        mySeleniumTestBase.logOutUI();

    }

    public void testrenamePassword() throws Exception {
//      try
//        {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);

        // mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI(username, password);
        mySeleniumTestBase.addNewUser("Chamara", "Chamara");
        mySeleniumTestBase.addNewRole("wso3", "Chamara", "Login to admin console");

        mySeleniumTestBase.logOutUI();
        Thread.sleep(3000);
        mySeleniumTestBase.loginToUI("Chamara", "Chamara");
        Thread.sleep(3000);
        assertTrue(browser.isTextPresent("Chamara@"));
        mySeleniumTestBase.logOutUI();
        browser.waitForPageToLoad("30000");
        mySeleniumTestBase.loginToUI(username, password);
        browser.waitForPageToLoad("30000");
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Users");
        browser.waitForPageToLoad("30000");
        String readUserName = "";
        String user = "Chamara";
        int i = 1;
        while (!user.equals(readUserName)) {
            readUserName = browser.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]/a[1]");
        browser.waitForPageToLoad("30000");
        browser.type("newPassword", "Chamara123");
        browser.type("checkPassword", "Chamara123");
        browser.click("//input[@value='Change']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Password of user " + user + " successfully changed"));
        browser.click("//button[@type='button']");
        mySeleniumTestBase.logOutUI();
        browser.waitForPageToLoad("30000");
        mySeleniumTestBase.loginToUI("Chamara", "Chamara123");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Chamara@"));
//        Thread.sleep(1000);
//            mySeleniumTestBase.logOutUI();
//        }
//      catch (Exception e)
//          {
//            SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
//            mySeleniumTestBase.logOutUI();
//          }
    }

    public void testDeleteUsers() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI(username, password);
        mySeleniumTestBase.deleteUser("user1");
        mySeleniumTestBase.deleteUser("user2");
        mySeleniumTestBase.deleteUser("user3");
        mySeleniumTestBase.deleteUser("user4");
        mySeleniumTestBase.deleteUser("user5");
        mySeleniumTestBase.deleteUser("user6");
        mySeleniumTestBase.deleteUser("Chamara");
    }

    public void testDeleteRole() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI(username, password);
        mySeleniumTestBase.deleteRole("Role1");
        mySeleniumTestBase.deleteRole("Role2");
        mySeleniumTestBase.deleteRole("Role3");
        mySeleniumTestBase.deleteRole("Role4");
        mySeleniumTestBase.deleteRole("Role5");
        mySeleniumTestBase.deleteRole("Role6");
        mySeleniumTestBase.deleteRole("wso3");

    }
  
    public void testLogout()throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        Thread.sleep(5000);
        mySeleniumTestBase.logOutUI();
    }
}
