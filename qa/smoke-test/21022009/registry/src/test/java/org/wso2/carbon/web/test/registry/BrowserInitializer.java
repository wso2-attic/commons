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

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


public class BrowserInitializer {

    private static Selenium browser;

    public synchronized static void setBrowser(Selenium _browser) {
        browser = _browser;
    }

    public synchronized static Selenium getBrowser() {
        return browser;
    }

    public synchronized static void stopBrowser() {
        browser.stop();
    }

    public synchronized static void initBrowser() {
        if (browser == null) {
            browser = new DefaultSelenium("localhost", 4444, "*chrome", "https://localhost:9443/");
            browser.start();
            browser.setSpeed("100");
            //browser.setSpeed(EXECUTION_SPEED_NORMAL);
        }
    }
}