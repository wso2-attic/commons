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

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;


public class BrowserInitializer {

    private static Selenium browser;
    private static Properties property;

    public synchronized static void setBrowser(Selenium _browser) {
        browser = _browser;
    }

    public synchronized static void setProperty(Properties _property) {
        property = _property;
    }

    public synchronized static Selenium getBrowser() {
        return browser;
    }

    public synchronized static Properties getProperties() {
        return property;
    }

    public synchronized static void stopBrowser() {
        browser.stop();
    }

    public synchronized static void initProperty() throws Exception {
        if (property == null) {
            Properties p1 = new Properties();
            System.out.println(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
            p1.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
            setProperty(p1);
        }
    }

    public synchronized static void initBrowser() throws Exception {
        if (browser == null) {
            System.out.println("https://" + property.getProperty("host.name") + ":" + property.getProperty("https.port"));
            browser = new DefaultSelenium("localhost", 4444, "*firefox", "https://" + property.getProperty("host.name") + ":" + property.getProperty("https.port"));
//            browser = new DefaultSelenium("localhost", 4444, "*firefox", "https://localhost:9443/");
            browser.start();
            browser.setSpeed("200");
            //browser.setSpeed(EXECUTION_SPEED_NORMAL);
        }
    }
}