package org.wso2.carbon.web.test.is;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

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


public class BrowserInitializer {
    private static Selenium selenium;

    public synchronized static void setbrowser(Selenium _browser) {
        selenium = _browser;
    }

    public synchronized static Selenium getbrowser() {
        return selenium;
    }

    public synchronized static void stopbrowser() {
        selenium.stop();
    }

     public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        return properties;
    }

    public synchronized static void initbrowser() throws IOException {

        if (selenium == null) {
            selenium = new DefaultSelenium("localhost", 4444, "*firefox", "https://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("https.port"));
            selenium.start();

            //    selenium.open("/carbon/admin/login.jsp");
            //    selenium.type("txtUserName", "admin");
            //    selenium.type("txtPassword", "admin");
            //    selenium.click("//input[@value='Sign-in']");
            //    selenium.waitForPageToLoad("300000");
            selenium.setSpeed("500");
        }
    }
}