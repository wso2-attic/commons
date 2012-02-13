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

package org.wso2.carbon.web.test.wsas;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;


public class BrowserInitializer {
    private static Selenium browser;
    static FileInputStream freader;
    private static String SELENIUM_SERVER_HOST = "selenium.server.host";
    private static String SELENIUM_SERVER_PORT = "selenium.server.port";
    private static String SELENIUM_BROWSER_STARTCOMMAND = "selenium.browser.startCommand";
    private static String SELENIUM_BROWSER_URL = "selenium.browser.url";

    public synchronized static void setBrowser(Selenium _browser) {
        browser = _browser;
    }

    public synchronized static Selenium getBrowser() {
        return browser;
    }

    public synchronized static void stopBrowser() {
        browser.stop();
    }

    public static Properties loadProperties() throws IOException {
       Properties properties = new Properties();
       freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
       properties.load(freader);
       return properties;
    }

    public synchronized static void initBrowser()throws Exception{
       

        if (browser == null) {
            browser = new DefaultSelenium("localhost", 4444, "*"+loadProperties().getProperty("browser.version"), "https://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("https.fe.port"));
            browser.start();
            browser.setSpeed("500");
            freader.close();
        }
    }
}
