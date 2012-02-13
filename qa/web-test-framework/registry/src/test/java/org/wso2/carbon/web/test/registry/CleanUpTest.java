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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;


public class CleanUpTest extends TestCase {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public CleanUpTest(String s) {
        super(s);
    }

    public void testFinalizeUsers() throws Exception {

        String role = "nonadminrole";
        String userName5 = "admin5";
        String userName1 = "NonAdmin";
        String userName2 = "admin2";
        String userName3 = "admin3";
        String userName4 = "admin4";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteRole(role);
        registryCommon.deleteUser(userName5);
        registryCommon.deleteUser(userName1);
        registryCommon.deleteUser(userName2);
        registryCommon.deleteUser(userName3);
        registryCommon.deleteUser(userName4);
        UmCommon.logOutUI();
    }

    public void testCleanRegistry() throws Exception {  // if u need  anything not to delete put that
        // name to the "folder" array
        String[] folders = {"carbon", "governance", "system"};
        boolean shouldNotDelete = false;
        String name = "";
        int i = 1;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[2]")) {
            name = selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").replace(" ..", "").replace("..", "");
            System.out.println(name);
            for (int j = 0; j < folders.length; j++) {
                if (name.equals(folders[j]))
                    shouldNotDelete = true;
            }
            if (shouldNotDelete) {
                shouldNotDelete = false;
                i++;
                continue;
            }
            selenium.click("actionLink" + i);
            Thread.sleep(1000);
            if (selenium.isElementPresent("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/');\"]"))
                selenium.click("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/');\"]");
            else
            if (selenium.isElementPresent("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/')\"]"))
                selenium.click("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/')\"]");
            else if (selenium.isElementPresent("link=Delete"))
                selenium.click("link=Delete");
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if (selenium.isElementPresent("messagebox-confirm")) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);

        }
        UmCommon.logOutUI();
    }
    public void testFinalizeLifeCycle() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();

    }
    public void testfinalizeNotificationTest() throws Exception {
        String role = "nonadminrole";
        String userName = "NonAdmin";
        String collectionName = "testProfile";
        String userName2 = "admin2";
        String userName3 = "admin3";
        String userName4 = "admin4";
        String profileName = "test profile";
        // selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI("admin", "admin");
        registryCommon.deleteColletion("/", collectionName);
        registryCommon.deleteRole(role);
        registryCommon.deleteUser(userName);
        registryCommon.deleteUser(userName2);
        registryCommon.deleteUser(userName3);
        registryCommon.deleteUser(userName4);
        Thread.sleep(1000);
        registryCommon.deleteAllNotifications();
        registryCommon.deleteProfile(profileName, "admin");
        UmCommon.logOutUI();
        // selenium.setSpeed(Curspeed);
    }
    public void testCleanUpLCs() throws Exception {
        String[] folders = {"ServiceLifeCycle"};        // this wont be deleted
        boolean shouldNotDelete = false;
        String name = "";
        int i = 1;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        while (selenium.isElementPresent("//table[@id='lcmTable']/tbody/tr["+i+"]/td[1]")) {
            name = selenium.getText("//table[@id='lcmTable']/tbody/tr["+i+"]/td[1]");
//            System.out.println(name);
            for (int j = 0; j < folders.length; j++) {
                if (name.equals(folders[j]))
                    shouldNotDelete = true;
            }
            if (shouldNotDelete) {
                shouldNotDelete = false;
                i++;
                continue;
            }
            if(i==1){
                selenium.click("link=Delete");
              }
            else {
               selenium.click("//a[@onclick=\"deleteLC('"+name+"')\"]");
            }
            for (int second = 0;; second++) {
                if (second >= 60) fail("timeout");
                try { if (selenium.isElementPresent("//div[@id='messagebox-confirm']/p")) break; } catch (Exception e) {}
                Thread.sleep(1000);
            }
            selenium.click("//button[@type='button']");
            selenium.waitForPageToLoad("30000");
        }
        UmCommon.logOutUI();
    }
}
