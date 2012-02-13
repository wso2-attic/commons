/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.system.test.core;

import com.opera.core.systems.OperaDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserManager {
    private static final Log log = LogFactory.getLog(BrowserManager.class);
    public static WebDriver driver;

    public static WebDriver getWebDriver() {
        String driverSelection = FrameworkSettings.BROWSER_NAME;
        if (FrameworkSettings.REMOTE_SELENIUM_WEBDRIVER_STATUS.equals("true")) {
            log.info("Test runs on remote browser");
            getRemoteWebDriver();
            return driver;
        } else {
            log.info("Test runs on " + driverSelection + "browser");
            getDriver(driverSelection);
            return driver;
        }
    }

    private static void getDriver(String driverSelection) {
        if (driverSelection.equalsIgnoreCase(ProductConstant.FIREFOX_BROWSER)) {
            driver = new FirefoxDriver();
        } else if (driverSelection.equalsIgnoreCase(ProductConstant.CHROME_BROWSER)) {
            driver = new ChromeDriver();
            System.setProperty("webdriver.chrome.driver", FrameworkSettings.CHROME_DIRVER_PATH);
        } else if (driverSelection.equalsIgnoreCase(ProductConstant.IE_BROWSER)) {
            driver = new InternetExplorerDriver();
        } else if (driverSelection.equalsIgnoreCase(ProductConstant.HTML_UNIT_DRIVER)) {
            driver = new HtmlUnitDriver(true);
            System.setProperty("webdriver.chrome.driver", FrameworkSettings.CHROME_DIRVER_PATH);
        } else {
            driver = new OperaDriver();
        }
    }

    private static void getRemoteWebDriver() {
        URL url = null;
        String browserName = FrameworkSettings.BROWSER_NAME;
        String remoteWebDriverURL = FrameworkSettings.REMOTE_SELENIUM_WEBDRIVER_URL;
        log.debug("Browser selection " + browserName);
        log.debug("Remote WebDriverURL " + remoteWebDriverURL);
        try {
            url = new URL(remoteWebDriverURL);
        } catch (MalformedURLException e) {
            log.error("Malformed URL " + e.getMessage());
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setBrowserName(browserName);
        driver = new RemoteWebDriver(url, capabilities);
    }
}
