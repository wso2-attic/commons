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

package org.wso2.carbon.web.test.registry;

public class UserManagerCommon extends CommonSetup {
    public UserManagerCommon(String text) {
        super(text);
    }

    public static boolean addNewrole(String roleName) {

        boolean value = false;

        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", "admin");
        selenium.type("txtPassword", "admin");
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Roles");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Roles"));
        selenium.click("link=Add New Role");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add Role"));
        assertTrue(selenium.isTextPresent("Enter role details"));
        assertTrue(selenium.isTextPresent("Role Name"));
        selenium.type("roleName", roleName);
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Select All");
        assertTrue(selenium.isTextPresent("Select permissions"));
        assertTrue(selenium.isTextPresent("Select permissions to add to Role"));
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");

        if (selenium.isTextPresent("successfully.")) {
            value = true;
        }

        assertTrue(selenium.isTextPresent(roleName));
        selenium.click("//button[@type='button']");
        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");
        return value;
    }


    public static boolean addNewUser(String userName, String password) {

        boolean value = false;
        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", "admin");
        selenium.type("txtPassword", "admin");
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Users"));
        selenium.click("link=Add New User");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Enter user name"));
        selenium.type("username", userName);
        selenium.type("password", password);
        selenium.type("retype", password);

        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("added successfully."));
        selenium.click("//button[@type='button']");

        if (selenium.isTextPresent(userName)) {
            value = true;
        }

        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");
        return value;
    }

    public static boolean assignUserToAdminRole(String userName) {

        boolean value = false;

        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", "admin");
        selenium.type("txtPassword", "admin");
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Roles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Edit Users");
        selenium.waitForPageToLoad("30000");
        selenium.type("org.wso2.usermgt.role.edit.filter", "*");
        selenium.click("//input[@value='Search']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(userName));
        selenium.click("//input[@name='selectedUsers' and @value='" + userName + "']");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Role admin updated successfully."));


        if (selenium.isTextPresent("Role admin updated successfully.")) {
            value = true;
        }

        selenium.click("//button[@type='button']");
        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");

        return value;
    }

    public static boolean assignPriviledgeToRoot(String userName, String roleName) {

        boolean value = false;

        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", "admin");
        selenium.type("txtPassword", "admin");
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        selenium.click("perIconMinimized");
        selenium.select("roleToAuthorize", "label=testrole123");
        selenium.select("roleActionToAuthorize", "label=Write");
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('/')\"]");
        assertTrue(selenium.isTextPresent("Permission applied successfully"));
        selenium.click("//button[@type='button']");
        assertTrue(selenium.isTextPresent("Role Permissions"));
        assertTrue(selenium.isTextPresent("testrole"));
        assertTrue(selenium.isTextPresent("testrole2"));
        selenium.click("testrole2^rda");
        selenium.click("testrole2^rra");
        selenium.click("//div[@id='rolePermisionsDiv']/form[2]/table/tbody/tr[6]/td/input");
        assertTrue(selenium.isTextPresent("Permission applied successfully"));
        selenium.click("//button[@type='button']");

        if (selenium.isTextPresent("successfully.")) {
            value = true;
        }

        selenium.click("//button[@type='button']");

        return value;
    }

    public static void loginToUi(String UserName, String Password) throws Exception   // Login to UI Console
    {
        selenium.open("/carbon/admin/login.jsp");
        Thread.sleep(1000);
        selenium.type("txtUserName", UserName);
        selenium.type("txtPassword", Password);
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
    }

    public static void logOutUi() {
        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");
    }

    public void addNewRole(String RoleName, String AsignUser, String Permission1) throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Roles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Role");
        selenium.waitForPageToLoad("30000");
        selenium.type("roleName", RoleName);
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");

        if (Permission1 == "Login to admin console") {
            selenium.click("selectedPermissions");
            selenium.click("//input[@value='Next >']");
            selenium.waitForPageToLoad("30000");
            selenium.type("org.wso2.usermgt.role.add.filter", "*");
            selenium.click("//input[@value='Search']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@name='selectedUsers' and @value='" + AsignUser + "']");
            selenium.click("//input[@value='Finish']");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("Role " + RoleName + " added successfully."));
            selenium.click("//button[@type='button']");
            assertTrue(selenium.isTextPresent(RoleName));
            Thread.sleep(2000);
        } else {
            selenium.click("selectedPermissions");
            selenium.click("//input[@name='selectedPermissions' and @value='" + Permission1 + "']");
            selenium.click("//input[@value='Next >']");
            selenium.waitForPageToLoad("30000");
            selenium.type("org.wso2.usermgt.role.add.filter", "*");
            selenium.click("//input[@value='Search']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@name='selectedUsers' and @value='" + AsignUser + "']");
            selenium.click("//input[@value='Finish']");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("Role " + RoleName + " added successfully."));
            selenium.click("//button[@type='button']");
            assertTrue(selenium.isTextPresent(RoleName));
            Thread.sleep(2000);
        }

    }

    public static void deleteUser(String Uname) throws Exception {

        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", "admin");
        selenium.type("txtPassword", "admin");
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");

        String tmp = "//a[@onclick=\"deleteUser('" + Uname + "')\"]";
        selenium.click(tmp);
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
    }

    public static void deleteRole(String RoleName) {

        selenium.open("/carbon/admin/login.jsp");
        selenium.type("txtUserName", "admin");
        selenium.type("txtPassword", "admin");
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=User Management");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Roles");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"deleteUserGroup('" + RoleName + "')\"]");
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Role " + RoleName + " deleted successfully."));
        selenium.click("//button[@type='button']");
        selenium.click("link=Sign-out");
        selenium.waitForPageToLoad("30000");
    }
}
