package org.wso2.carbon.web.test.mashup;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;


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
            selenium = new DefaultSelenium("localhost", 4444, "*firefox", "https://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("https.be.port"));
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
