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


public class RegistryRating extends CommonSetup {
    public RegistryRating(String text) {
        super(text);
    }

    public void testRateRoot() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        assertTrue(RegistryCommon.rateResource("/", 5, 5));
        assertTrue(selenium.isTextPresent("root"));
        UserManagerCommon.logOutUi();
    }

    public void testChangeRootRating() throws Exception {

        UserManagerCommon.loginToUi("admin", "admin");
        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        assertTrue(RegistryCommon.rateResource("/", 3, 3));
        assertTrue(selenium.isTextPresent("root"));
        assertTrue(RegistryCommon.rateResource("/", 4, 4));
        UserManagerCommon.logOutUi();
    }

    public void testRatingTable() throws Exception {

        String resourceName = "/testRatingTable";
        UserManagerCommon.loginToUi("admin", "admin");
        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Ratings"));
        assertTrue(selenium.isTextPresent("My Rating:"));
        assertTrue(selenium.isTextPresent("Rating:"));
        assertTrue(selenium.isTextPresent("(0.0)"));
        selenium.click("ratingIconExpanded");
        assertTrue(selenium.isTextPresent("Ratings"));
        selenium.click("ratingIconMinimized");
        assertTrue(selenium.isTextPresent("Rating:"));
        String ratingTable = selenium.getTable("//div[@id='ratingDiv']/table.0.1");


        assertTrue(RegistryCommon.rateResource(resourceName, 5, 5));
        assertTrue(RegistryCommon.rateResource(resourceName, 3, 3));
        assertTrue(RegistryCommon.clickRootIcon());
        assertTrue(RegistryCommon.deleteResource(resourceName, 3));

        UserManagerCommon.logOutUi();


    }

    public void testAverageRating() throws Exception {

        String resourceName = "/testAverageRating";
        String UserName = "AverageRatingUser";
        String password = "test123";
        UserManagerCommon.addNewUser(UserName, password);
        UserManagerCommon.assignUserToAdminRole(UserName);

        UserManagerCommon.loginToUi(UserName, password);
        RegistryCommon.addTextResourceToRoot(resourceName);
        Thread.sleep(1000);
        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Ratings"));
        assertTrue(selenium.isTextPresent("My Rating:"));
        assertTrue(selenium.isTextPresent("Rating:"));

        selenium.click("ratingIconExpanded");
        assertTrue(selenium.isTextPresent("Ratings"));
        selenium.click("ratingIconMinimized");
        assertTrue(selenium.isTextPresent("Rating:"));
        Thread.sleep(1000);
        assertTrue(RegistryCommon.rateResource(resourceName, 5, 5));
        assertTrue(RegistryCommon.clickRootIcon());

        UserManagerCommon.logOutUi();

        UserManagerCommon.loginToUi("admin", "admin");
        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");

        selenium.click("resourceView3");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Ratings"));
        assertTrue(selenium.isTextPresent("My Rating:"));
        assertTrue(selenium.isTextPresent("Rating:"));

        selenium.click("ratingIconExpanded");
        assertTrue(selenium.isTextPresent("Ratings"));
        selenium.click("ratingIconMinimized");
        assertTrue(selenium.isTextPresent("Rating:"));

        assertTrue(RegistryCommon.rateResource(resourceName, 2, 3.5));
        assertTrue(RegistryCommon.clickRootIcon());
        assertTrue(RegistryCommon.deleteResource(resourceName, 3));
        UserManagerCommon.logOutUi();
        UserManagerCommon.deleteUser(UserName);
        UserManagerCommon.logOutUi();
    }

}
