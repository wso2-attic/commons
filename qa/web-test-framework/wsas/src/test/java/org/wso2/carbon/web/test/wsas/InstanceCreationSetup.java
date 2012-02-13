package org.wso2.carbon.web.test.wsas;

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

    public void openBrowser(String strUrl,String str) throws Exception{

        browser = new DefaultSelenium("localhost", 4444, "*"+loadProperties().getProperty("browser.version"), "https://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port"));
        browser.start();
        browser.open(strUrl);

        URL url=new URL(strUrl);
        URLConnection ucon=url.openConnection();
        StringBuffer sb=new StringBuffer();
        String data="";
        InputStream is=ucon.getInputStream();
        int ch;
        while((ch=is.read()) != -1){
            sb.append((char)ch);
        }

        data=sb.toString();
        System.out.println(data);
        if(data.contains(str)){
            System.out.println("Element found");
        }else{
            System.out.println("Element not found");
            is.close();
            assertTrue(browser.isTextPresent("Element found"));
        }


        // System.out.println(data);
        is.close();
        Thread.sleep(500);
        browser.stop();
       

     }
}