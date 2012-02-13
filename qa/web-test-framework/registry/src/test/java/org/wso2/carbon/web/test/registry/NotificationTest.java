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

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;


public class NotificationTest extends TestCase {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed;

    public NotificationTest(String txt) {
        super(txt);

    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }


    private void initilize() throws Exception {
        String collectionName = "testProfile";
        String mediaType = "not specified";
        String description = "this collection  is to test subscribe from user profile";

//        selenium.setSpeed(Curspeed);
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.addCollectionToRoot(collectionName, mediaType, description);

    }

    public void testCheckContent() throws Exception {
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        initilize();
        registryCommon.gotoNotificationPage();
        assertEquals("Add Subscription to Resource/Collection", selenium.getText("link=Add Subscription to Resource/Collection"));
        selenium.click("link=Add Subscription to Resource/Collection");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Registry Subscription".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertTrue(selenium.isTextPresent("Subscription Path"));
        assertTrue(selenium.isTextPresent("Event"));
        assertTrue(selenium.isTextPresent("Notification"));

//        assertTrue(selenium.getText("//td[@id='subscription-area-div']/table/tbody/tr[1]/td[1]").matches("^exact:Event [\\s\\S]*$"));
//        assertTrue(selenium.getText("//td[@id='subscription-area-div']/table/tbody/tr[2]/td[1]").matches("^exact:Notification [\\s\\S]*$"));
//        assertTrue(selenium.getText("//form[@id='subscriptionForm']/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]").matches("^exact:Subscription Path [\\s\\S]*$"));
        UmCommon.logOutUI();
    }

    public void testNoEmailAdd() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String notification = "E-mail";
        String email = "";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertTrue(selenium.isTextPresent("You didn't enter an email address."));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testIncEmailAdd() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String notification = "E-mail";
        String email = "aabccdd";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertTrue(selenium.isTextPresent("Please enter a valid email address."));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testNoWebAdd() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String notification = "HTML/Plain-Text";
        String html = "";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertTrue(selenium.isTextPresent("The Web Service URL is not a valid URL."));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testIncWebAdd() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String notification = "HTML/Plain-Text";
        String html = "aabbccdd";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertTrue(selenium.isTextPresent("The Web Service URL is not a valid URL."));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testIncSOAPAdd() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String notification = "SOAP";
        String soapAdd = "test";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, soapAdd));
        assertTrue(selenium.isTextPresent("The Web Service URL is not a valid URL."));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testNoUsrName() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String profileName = "";
        String notification = "User Profile";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        assertTrue(selenium.isTextPresent("The required field User Name has not been filled in."));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testInvUsrName() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String profileName = "test";
        String notification = "User Profile";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("No valid user exists by the name:"));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_LCI_Email() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";

        String notification = "E-mail";
//        String email="test@wso2.com";
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
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_LCI_HTML() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String html = "http://google.lk/wso2";
        String notification = "HTML/Plain-Text";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();


    }

    public void testAddNotificationAdmin_LCI_Soap() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String soapAddress = "http://google.lk/wso2";
        String notification = "SOAP";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, soapAddress));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();

    }

    public void testAddNotificationAdmin_LCI_UserProfile() throws Exception {
        String collectionName = "testProfile";
        String event = "Check LC Item";
        String profileName = "admin";
        String notification = "User Profile";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();

    }

    ///////////////////////////////////////////////////   non default admin  //////////////////////
    public void testAddNotificationAdmin_CC_Email() throws Exception {
        String collectionName = "testProfile";
        String event = "Create Child";

        String notification = "E-mail";
//        String email="test@wso2.com";
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
        String userName = "admin2";
        String password = "admin2";

        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        UmCommon.addNewUser(userName, password);
        //        UmCommon.assignUserToAdminRole(userName);
        registryCommon.addUserToRole("admin", userName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(userName, password);
        //initilize();
        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();


    }

    public void testAddNotificationAdmin_CC_HTML() throws Exception {
        String collectionName = "testProfile";
        String event = "Create Child";
        String html = "http://google.lk/wso2";
        String notification = "HTML/Plain-Text";
        String userName = "admin2";
        String password = "admin2";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, password);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();


    }

    public void testAddNotificationAdmin_CC_Soap() throws Exception {
        String collectionName = "testProfile";
        String event = "Create Child";
        String soapAddress = "http://soap.lk/wso2";
        String notification = "SOAP";
        String userName = "admin2";
        String password = "admin2";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, password);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, soapAddress));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();

    }

    public void testAddNotificationAdmin_CC_UserProfile() throws Exception {
        String collectionName = "testProfile";
        String event = "Create Child";
        String profileName = "admin";
        String notification = "User Profile";
        String userName = "admin2";
        String password = "admin2";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, password);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    ////////////////////////////////// non admin add a notifivation him self /////////////////////////////////
    public void testAddNotificationAdmin_DC_Email() throws Exception {
        String collectionName = "testProfile";
        String event = "Delete Child";

        String notification = "E-mail";
//        String email="test@wso2.com";
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
        String userName = "NonAdmin";
        String password = "NonAdmin";
        String configuration = "110110000";
        String role = "nonadminrole";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        UmCommon.addNewUser(userName, password);
        registryCommon.addNewRole(role, configuration);

        registryCommon.addUserToRole(role, userName);
        registryCommon.setRolePermissions(role, "Write", true);
        UmCommon.logOutUI();
        UmCommon.loginToUI(userName, password);
        //initilize();
        registryCommon.gotoMyprofile();
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_DC_HTML() throws Exception {
        String collectionName = "testProfile";
        String event = "Delete Child";
        String html = "http://google.lk/wso2";
        String notification = "HTML/Plain-Text";
        String userName = "NonAdmin";
        String password = "NonAdmin";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, password);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();


    }

    public void testAddNotificationAdmin_DC_Soap() throws Exception {
        String collectionName = "testProfile";
        String event = "Delete Child";
        String soapAddress = "http://soap.lk/wso2";
        String notification = "SOAP";
        String userName = "NonAdmin";
        String password = "NonAdmin";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, password);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, soapAddress));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();

    }

    public void testAddNotificationAdmin_DC_UserProfile() throws Exception {
        String collectionName = "testProfile";
        String event = "Delete Child";
        String profileName = "admin";
        String notification = "User Profile";
        String userName = "NonAdmin";
        String password = "NonAdmin";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, password);
        assertFalse(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, userName));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    //////////////////////// default admin profile subscribed by non def admin  /////////////////////////////
    public void testAddNotificationAdmin_CLCStat_Email() throws Exception {
        String collectionName = "testProfile";
        String event = "Change LC State";

        String notification = "E-mail";
//        String email="test@wso2.com";
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
        String userName = "admin3";
        String password = "admin3";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        UmCommon.addNewUser(userName, password);
        UmCommon.assignUserToAdminRole(userName);
        //initilize();
        registryCommon.gotoMyprofile();
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_CLCStat_HTML() throws Exception {
        String collectionName = "testProfile";
        String event = "Change LC State";
        String html = "http://google.lk/wso2";
        String notification = "HTML/Plain-Text";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();


    }

    public void testAddNotificationAdmin_CLCStat_Soap() throws Exception {
        String collectionName = "testProfile";
        String event = "Change LC State";

        String soapAddress = "http://soap.lk/wso2";
        String notification = "SOAP";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, soapAddress));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();

    }

    public void testAddNotificationAdmin_CLCStat_UserProfile() throws Exception {
        String collectionName = "testProfile";
        String event = "Change LC State";
        String profileName = "admin3";
        String notification = "User Profile";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public void testAddNotificationAdmin_Updat_Email() throws Exception {
        String collectionName = "testProfile";
        String event = "Update";

        String notification = "E-mail";
//        String email="test@wso2.com";
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
        String userName = "admin4";
        String password = "admin4";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        UmCommon.addNewUser(userName, password);

        //initilize();
        registryCommon.gotoOtherUsersProfiles(userName);
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        registryCommon.fillProfile(name, fName, lName, org, add, country, email, tp, mobNo, im, url);
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, email));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_Updat_HTML() throws Exception {
        String collectionName = "testProfile";
        String event = "Update";
        String html = "http://google.lk/wso2";
        String notification = "HTML/Plain-Text";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, html));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_Updat_Soap() throws Exception {
        String collectionName = "testProfile";
        String event = "Update";
        String soapAddress = "http://soap.lk/wso2";
        String notification = "SOAP";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, soapAddress));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testAddNotificationAdmin_Updat_UserProfile() throws Exception {
        String collectionName = "testProfile";
        String event = "Update";
        String profileName = "admin4";
        String notification = "User Profile";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        int noOfNot = 0;
        noOfNot = registryCommon.countNotifications();
        assertTrue(registryCommon.addNotificationFromAdminConsole(collectionName, event, notification, profileName));
        assertEquals(noOfNot + 1, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

    public void testEditProfile() throws Exception {
        String path = "/testProfile";
        String event = "Change LC State";
        String notification = "E-mail";
        String email = "test@wso2.com";
        int noOfNot = 0;
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        noOfNot = registryCommon.countNotifications();
        registryCommon.editNotification(path, event, notification, email, event, notification, "testwee@bb.ccv");
        assertEquals(noOfNot, registryCommon.countNotifications());
        UmCommon.logOutUI();
    }

   

}
