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


public class RegistryRating extends TestCase {

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

    public void testRateRoot() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        assertTrue(registryCommon.rateResource("/", 5, 5));
        assertTrue(selenium.isTextPresent("root"));
        UmCommon.logOutUI();
    }

    public void testChangeRootRating() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        assertTrue(registryCommon.rateResource("/", 3, 3));
        assertTrue(selenium.isTextPresent("root"));
        assertTrue(registryCommon.rateResource("/", 4, 4));
        UmCommon.logOutUI();
    }

    public void testRatingTable() throws Exception {
        registryCommon.signOut();
        String resourceName = "/testRatingTable";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent("Ratings"));
        assertTrue(selenium.isTextPresent("My Rating:"));
        assertTrue(selenium.isTextPresent("Rating:"));
        assertTrue(selenium.isTextPresent("(0.0)"));
        selenium.click("ratingIconExpanded");
        assertTrue(selenium.isTextPresent("Ratings"));
        selenium.click("ratingIconMinimized");
        assertTrue(selenium.isTextPresent("Rating:"));
        String ratingTable = selenium.getTable("//div[@id='ratingDiv']/table.0.1");


        assertTrue(registryCommon.rateResource(resourceName, 5, 5));
        assertTrue(registryCommon.rateResource(resourceName, 3, 3));
        assertTrue(registryCommon.clickRootIcon());
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));

        UmCommon.logOutUI();


    }

    public void testAverageRating() throws Exception {
        registryCommon.signOut();
        String resourceName = "/testAverageRating";
        String UserName = "AverageRatingUser";
        String password = "test123";
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(UserName, password);
        UmCommon.assignUserToAdminRole(UserName);
        UmCommon.logOutUI();

        UmCommon.loginToUI(UserName, password);
        registryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent("Ratings"));
        assertTrue(selenium.isTextPresent("My Rating:"));
        assertTrue(selenium.isTextPresent("Rating:"));

        selenium.click("ratingIconExpanded");
        assertTrue(selenium.isTextPresent("Ratings"));
        selenium.click("ratingIconMinimized");
        assertTrue(selenium.isTextPresent("Rating:"));
        Thread.sleep(1000);
        assertTrue(registryCommon.rateResource(resourceName, 5, 5));
        assertTrue(registryCommon.clickRootIcon());

        UmCommon.logOutUI();

        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();

        registryCommon.gotoResource(resourceName);
        assertTrue(selenium.isTextPresent("Ratings"));
        assertTrue(selenium.isTextPresent("My Rating:"));
        assertTrue(selenium.isTextPresent("Rating:"));

        selenium.click("ratingIconExpanded");
        assertTrue(selenium.isTextPresent("Ratings"));
        selenium.click("ratingIconMinimized");
        assertTrue(selenium.isTextPresent("Rating:"));

        assertTrue(registryCommon.rateResource(resourceName, 2, 3.5));
        assertTrue(registryCommon.clickRootIcon());
        int actionId = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId));

        UmCommon.deleteUser(UserName);
        UmCommon.logOutUI();
    }

}
