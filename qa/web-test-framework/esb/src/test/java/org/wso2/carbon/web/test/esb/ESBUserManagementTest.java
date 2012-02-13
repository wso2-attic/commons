package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.Test;
import junit.framework.TestSuite;

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

public class ESBUserManagementTest extends CommonSetup{


    public ESBUserManagementTest(String text) {
        super(text);
    }

    /*
    This mthod will be used to log into the management console
     */
    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
    }

    /*
    These mthods will be used to test preparing users
     */

    public void testCreateUsers1() throws Exception{           // Create users for check user permissions
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user1", "userone");
     }

    public void testCreateUsers2() throws Exception{           // Create users for check user permissions
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user2", "usertwo");
     }

    public void testCreateUsers3() throws Exception{           // Create users for check user permissions
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user3", "userthree");
     }

    public void testCreateUsers4() throws Exception{           // Create users for check user permissions
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user4", "userfour");
     }

    public void testCreateUsers5() throws Exception{           // Create users for check user permissions
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user5", "userfive");
     }

    public void testCreateUsers6() throws Exception{           // Create users for check user permissions
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewUser("user6", "usersix");
     }

    /*
    Methods creating roles
     */
    public void testCreateRoles1() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role1", "user1", "Login to admin console");
  }
    public void testCreateRoles2() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role2", "user2", "manage-configuration");

    }
    public void testCreateRoles3() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role3", "user3", "manage-security");

    }
    public void testCreateRoles4() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role4", "user4", "upload-services");

    }
    public void testCreateRoles5() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role5", "user5", "manage-services");

    }
    public void testCreateRoles6() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.addNewRole("Role6", "user6", "monitor-system");
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testCheckPermission1() throws Exception    //Login to admin console
    {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("user1","userone");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        seleniumTestBase.logOutUI();
    }    

    /*
    This mthod will be used to test check permissions
     */
    public void testCheckPermission2() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("user2","usertwo");
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

        selenium.click("link=Browse");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Browse"));

        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Search"));

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        seleniumTestBase.logOutUI();
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testCheckPermission3() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("user3","userthree");
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
        seleniumTestBase.logOutUI();
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testCheckPermission4() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("user4","userfour");
        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Deployed Services"));

        selenium.click("link=Try It");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Try It"));
        seleniumTestBase.logOutUI();
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testCheckPermission5() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("user5","userfive");
        boolean result = false;

        assertTrue(selenium.isElementPresent("menu-panel"));
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=echo");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(5000);
        selenium.click("link=Modules");
        selenium.waitForPageToLoad("30000");

        if (selenium.isTextPresent("Engage Modules to echo Service")) {
            result = true;
        }

        if (result == true) {
            assertTrue(selenium.isTextPresent("Engage Modules to echo Service"));
            selenium.click("//div[@id='menu']/ul/li[3]/ul/li[4]/ul/li[1]/a");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("Deployed Modules"));
            selenium.click("link=Add");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("Add a module"));

            seleniumTestBase.logOutUI();
        }

        else {
            assertFalse ("Engage Module Echo service not found", result);
            seleniumTestBase.logOutUI();
        }
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testCheckPermission6() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("user6","usersix");
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

        seleniumTestBase.logOutUI();
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testrenamePassword() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin", "admin");
        seleniumTestBase.addNewUser("EsbUser", "EsbUser");
        seleniumTestBase.addNewRole("EsbRole", "EsbUser", "Login to admin console");

        seleniumTestBase.logOutUI();
        seleniumTestBase.loginToUI("EsbUser", "EsbUser");
        assertTrue(selenium.isTextPresent("EsbUser"));
        seleniumTestBase.logOutUI();
        seleniumTestBase.loginToUI("admin", "admin");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");
        String readUserName = "";
        String user = "EsbUser";
        int i = 1;
        while (!user.equals(readUserName)) {
            readUserName = selenium.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        selenium.click("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]/a[1]");
        selenium.waitForPageToLoad("30000");
        selenium.type("newPassword", "EsbUser123");
        selenium.type("checkPassword", "EsbUser123");
        selenium.click("//input[@value='Change']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Password of user " + user + " successfully changed"));
        selenium.click("//button[@type='button']");
        seleniumTestBase.logOutUI();
        seleniumTestBase.loginToUI("EsbUser", "EsbUser123");
        assertTrue(selenium.isTextPresent("Signed-in as: EsbUser"));
        seleniumTestBase.logOutUI();
    }

    /*
    This mthod will be used to test check permissions
     */
    public void testDeleteUsers() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin", "admin");
        seleniumTestBase.deleteUser("user1");
        seleniumTestBase.deleteUser("user2");
        seleniumTestBase.deleteUser("user3");
        seleniumTestBase.deleteUser("user4");
        seleniumTestBase.deleteUser("user5");
        seleniumTestBase.deleteUser("user6");
        seleniumTestBase.deleteUser("EsbUser");
    }

    /*
    This mthod will be used to test check permissions
     */      
    public void testDeleteRole() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.deleteRole("Role1");
        seleniumTestBase.deleteRole("Role2");
        seleniumTestBase.deleteRole("Role3");
        seleniumTestBase.deleteRole("Role4");
        seleniumTestBase.deleteRole("Role5");
        seleniumTestBase.deleteRole("Role6");
        seleniumTestBase.deleteRole("EsbRole");
    }

    /*
    This mthod will be used to log out from the management console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}


