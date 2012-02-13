package org.wso2.carbon.web.test.wsas;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 20, 2009
 * Time: 9:44:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserInitializer {
    private static Selenium browser;

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

    public synchronized static void initBrowser()throws Exception{
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String ServerIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpsPort = instReadXMLFileTest.ReadConfig("https", "ServerConfig");

        if (browser == null) {
            browser = new DefaultSelenium("localhost", 4444, "*iexplore", "https://"+ServerIP+":"+httpsPort);
//browser = new DefaultSelenium(System.getProperty(SELENIUM_SERVER_HOST), Integer.parseInt(System.getProperty(SELENIUM_SERVER_PORT)), System.getProperty(SELENIUM_BROWSER_STARTCOMMAND), System.getProperty(SELENIUM_BROWSER_URL));
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
