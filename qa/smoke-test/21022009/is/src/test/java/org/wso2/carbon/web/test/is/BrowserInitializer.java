package org.wso2.carbon.web.test.is;

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
            browser = new DefaultSelenium("localhost", 4444, "*iexplore", "https://localhost:9443");
            browser.start();

            //    browser.open("/carbon/admin/login.jsp");
            //    browser.type("txtUserName", "admin");
            //    browser.type("txtPassword", "admin");
            //    browser.click("//input[@value='Sign-in']");
            //    browser.waitForPageToLoad("300000");
            browser.setSpeed("500");
        }
    }
}
