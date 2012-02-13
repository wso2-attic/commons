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

package org.wso2.carbon.web.test.GaaS;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;

public class FeedsTest extends TestCase {

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

    public FeedsTest(String text) {
        super(text);
    }

    public void testRootLevelFeeds() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        String resource1 = "/carbon";
        String resource2 = "/governance";
        String resource3 = "/system";

        registryCommon.gotoResourcePage();

        assertTrue(selenium.isTextPresent("Browse"));

        selenium.click("link=Feed");

        String feedWinId = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(feedWinId);

        selenium.windowFocus();

        String resources[] = {resource1, resource2, resource3};
        registryCommon.verifyFeedResources(resources);

        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();

        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();

    }

}
