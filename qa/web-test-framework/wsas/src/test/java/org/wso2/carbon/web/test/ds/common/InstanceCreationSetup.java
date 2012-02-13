package org.wso2.carbon.web.test.ds.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Oct 8, 2009
 * Time: 3:24:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceCreationSetup extends TestCase {
     static Selenium browser;

     public InstanceCreationSetup(String text) {
      super(text);
    }

     public  Properties loadProperties() throws IOException {
       Properties properties = new Properties();
        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
       properties.load(freader);
         freader.close();
       return properties;
    }

    public Selenium openBrowser(String strUrl) throws Exception{

        browser = new DefaultSelenium("localhost", 4444, "*"+loadProperties().getProperty("browser.version"), "https://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port"));
        browser.start();
        browser.open(strUrl);


        return browser;

     }
}