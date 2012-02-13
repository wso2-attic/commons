package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;


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

public synchronized static void initBrowser() {

if (browser == null) {
browser = new DefaultSelenium("localhost", 4444, "*chrome", "https://localhost:9443");
//browser = new DefaultSelenium(System.getProperty(SELENIUM_SERVER_HOST), Integer.parseInt(System.getProperty(SELENIUM_SERVER_PORT)), System.getProperty(SELENIUM_BROWSER_STARTCOMMAND), System.getProperty(SELENIUM_BROWSER_URL));
browser.start();

//    browser.open("/carbon/admin/login.jsp");
//    browser.type("txtUserName", "admin");
//    browser.type("txtPassword", "admin");
//    browser.click("//input[@value='Sign-in']");
//    browser.waitForPageToLoad("300000");
browser.setSpeed("200");
}
}
}
