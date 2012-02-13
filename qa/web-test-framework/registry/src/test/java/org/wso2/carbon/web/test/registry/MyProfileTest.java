/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.registry;

import org.wso2.carbon.web.test.common.*;
import org.wso2.carbon.web.test.common.RegistryCommon;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;


public class MyProfileTest extends CommonSetup {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed;

    public MyProfileTest(String msg) {
        super(msg);

    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public void testAddProfile() throws Exception {

        String name = "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoMyprofile();
        selenium.click("link=Add New Profile");
        // selenium.click("link=default");

        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        assertTrue(registryCommon.registerProfile("admin", "application/vnd.wso2-profiles+xml"));
        selenium.refresh();
        Thread.sleep(5000);
        registryCommon.gotoMyprofile();
        registryCommon.checkProfileRegister("/system/users/admin/", name + " Profile", fName, lName, email);
        registryCommon.deleteProfile(name, "admin");
        registryCommon.deleteColletion("/system/users", "admin");
        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testDefaultProfile() throws Exception {

        String name = "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);

        // add a test  check the notifiactions

        assertTrue(registryCommon.registerProfile("admin", "application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();
        registryCommon.checkProfileRegister("/system/users/admin/", "default Profile", fName, lName, email);
        registryCommon.gotoMyprofile();     // this redirection is just to eliminate the conflicts beteen
        // adjecent methods
        registryCommon.deleteColletion("/system/users", "admin");

//        Thread.sleep (10000);
        UmCommon.logOutUI();

    }

    public void testNewProfile() throws Exception {
        String name = "admin 2" + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        String userName = "admin2";
        String password = "admin2";

        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
//        registryCommon.gotoResourcePage();

        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(userName, password);
        registryCommon.gotoMyprofile();
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.gotoMyprofile();
        assertTrue(registryCommon.registerProfile(userName, "application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
        registryCommon.checkProfileRegister("/system/users/admin2/", name + " Profile", fName, lName, email);
        registryCommon.gotoMyprofile();
        registryCommon.deleteProfile(name, userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteColletion("/system/users", userName);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();

    }

    public void testRegisterNewProfile() throws Exception {
        String name = "admin 2 " + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";


        String userName = "admin2";
        String password = "admin2";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
//        registryCommon.gotoResourcePage();
        UmCommon.addNewUser(userName, password);
        Thread.sleep(1000);
        UmCommon.assignUserToAdminRole(userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(userName, password);
        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        assertTrue(registryCommon.registerProfile(userName, "application/vnd.wso2-profiles+xml"));
        Thread.sleep(3000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
        registryCommon.checkProfileRegister("/system/users/admin2/", "default Profile", fName, lName, email);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        Thread.sleep(1000);
        registryCommon.deleteColletion("/system/users", userName);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();
    }

    public void testNewAdmin3NewProfile() throws Exception {
        String name = "admin 3 " + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        String userName = "admin3";
        String password = "admin3";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.gotoMyprofile();
        assertTrue(registryCommon.registerProfile(userName, "application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
        registryCommon.checkProfileRegister("/system/users/admin3/", name + " Profile", fName, lName, email);
        registryCommon.deleteProfile(name, userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteColletion("/system/users", userName);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();

    }

    public void testNewAdmin3DefaultProfile() throws Exception {
        String name = "admin 3" + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        String userName = "admin3";
        String password = "admin3";

        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.gotoMyprofile();
        assertTrue(registryCommon.registerProfile(userName, "application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
        registryCommon.checkProfileRegister("/system/users/admin3/", "default Profile", fName, lName, email);
        registryCommon.deleteProfile(name, userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();

    }

    public void testCheckNonAdminDefaultProfileRegiseredByAdmin() throws Exception {

        String name = "Nonadmin " + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        String userName = "Nonadmin";
        String password = "Nonadmin";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.gotoMyprofile();
        assertTrue(registryCommon.registerProfile(userName, "application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
        registryCommon.checkProfileRegister("/system/users/Nonadmin/", "default Profile", fName, lName, email);

        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();
    }

    public void testCheckNonAdminNewProfileRegiseredByAdmin() throws Exception {
        String name = "Nonadmin " + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        String userName = "Nonadmin";
        String password = "Nonadmin";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.gotoMyprofile();
        assertTrue(registryCommon.registerProfile(userName, "application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
        registryCommon.checkProfileRegister("/system/users/Nonadmin/", name + " Profile", fName, lName, email);
        registryCommon.deleteProfile(name, userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();
    }

    public void testCheckNonAdminProfileRegisterByhimself() throws Exception {
        String name = "Nonadmin " + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        String userName = "Nonadmin";
        String password = "Nonadmin";
        String roleName = "NonAdminRole";
        String permissions = "110110000";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        registryCommon.addNewRole(roleName, permissions);
        registryCommon.addUserToRole(roleName, userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(userName, password);
        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.gotoMyprofile();
//        assertTrue(registryCommon.registerProfile(userName,"application/vnd.wso2-profiles+xml"));
        Thread.sleep(1000);
        registryCommon.gotoMyprofile();     // just to make adjecent commands compatible
//        registryCommon.checkProfileRegister("/system/users/Nonadmin/","default Profile",fName,lName,email);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(userName);
        registryCommon.gotoMyprofile();
        registryCommon.deleteRole(roleName);
        UmCommon.logOutUI();
    }

    public void testNotificationsDefaultAdmin() throws Exception {
        String collectionName = "testProfile";
        String mediaType = "not specified";
        String description = "this collection  is to test subscribe from user profile";
        String event = "Check LC Item";
        String profileName = "admin";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.addCollectionToRoot(collectionName, mediaType, description);
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, "User Profile", profileName));
        registryCommon.gotoMyprofile();
        registryCommon.deleteColletion("/", collectionName);
        UmCommon.logOutUI();

    }

    public void testNotificationsNonDefaultAdminRegByAdmin() throws Exception {
        String collectionName = "testProfile";
        String mediaType = "not specified";
        String description = "this collection  is to test subscribe from user profile";
        String event = "Delete Child";
        String userName = "admin3";
        String password = "admin3";
        String name = "admin 3" + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.gotoBrowsePage();    // dummy method just to eliminate the error
        registryCommon.addCollectionToRoot(collectionName, mediaType, description);
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, "User Profile", userName));
        Thread.sleep(1000);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();

    }

    public void testNotificationsNonDefaultAdmin() throws Exception {

        String collectionName = "testProfile";
        String mediaType = "not specified";
        String description = "this collection  is to test subscribe from user profile";
        String event = "Delete Child";
        String userName = "admin4";
        String password = "admin4";
        String name = "admin 4" + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(userName, password);
        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.addCollectionToRoot(collectionName, mediaType, description);
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, "User Profile", userName));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(userName);
        registryCommon.deleteColletion("/", collectionName);
        UmCommon.logOutUI();
    }

    public void testNotificationsNondefProfile() throws Exception {

        String collectionName = "testProfile";
        String mediaType = "not specified";
        String description = "this collection  is to test subscribe from user profile";
        String event = "Create Child";
        String userName = "admin5";
        String password = "admin5";
        String name = "admin 5" + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.addCollectionToRoot(collectionName, mediaType, description);
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, "User Profile", userName));
        Thread.sleep(1000);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();

    }

    public void testNotificationsNondefProfileForNonAdminUser() throws Exception {

        String collectionName = "testProfile";
        String mediaType = "not specified";
        String description = "this collection  is to test subscribe from user profile";
        String event = "Create Child";
        String userName = "admin5";
        String password = "admin5";
        String name = "admin 5" + "test profile";
        String fName = "test fname";
        String lName = "test lname";
        String org = "WSO2";
        String add = "59,Flower rd,Col 7";
        String country = "Sri lanka";
        String email = "test@wso2.com";
        String tp = "0112000000";
        String mobNo = "0999000000";
        String im = "test IM";
        String url = "";
        String tempUrl = "http://wso2.org";
        //  selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, password);
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.addCollectionToRoot(collectionName, mediaType, description);
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, "User Profile", userName));
        Thread.sleep(1000);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();
    }
}