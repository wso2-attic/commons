package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;


public class BrowserInitializer {
    private static Selenium browser;

    private static String SELENIUM_SERVER_HOST="selenium.server.host";
    private static String SELENIUM_SERVER_PORT="selenium.server.port";
    private static String SELENIUM_BROWSER_STARTCOMMAND="selenium.browser.startCommand";
    private static String SELENIUM_BROWSER_URL="selenium.browser.url";

    public synchronized static void setBrowser(Selenium _browser) {
        browser = _browser;
    }

    public synchronized static Selenium getBrowser() {
        return browser;
    }

    public synchronized static void stopBrowser() {
        browser.stop();
    }

    public synchronized static void initBrowser()throws Exception{
        ESBCommon esbCommon = new ESBCommon(browser);
        if (browser == null) {
            browser = new DefaultSelenium("localhost", 4444, esbCommon.getBrowserVersion(), "https://" + esbCommon.getHostName() + ":" + esbCommon.getHttpsPort());
            browser.start();
            browser.setSpeed("500");
        }
    }
}
