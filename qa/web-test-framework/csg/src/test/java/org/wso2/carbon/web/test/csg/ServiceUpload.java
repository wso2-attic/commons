package org.wso2.carbon.web.test.csg;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.Properties;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ServiceUpload extends TestCase
{
      Selenium browser;
    Properties properties = new Properties();

    public ServiceUpload(Selenium _browser) {
        browser = _browser;
    }

    public Properties loadProperties() throws IOException {
       Properties properties = new Properties();
       FileInputStream  freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
       properties.load(freader);
       freader.close();
       return properties;
    }
    public void testLogin() throws Exception {
           SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
           String username = loadProperties().getProperty("admin.username");
           String password = loadProperties().getProperty("admin.password");
           InstSeleniumTestBase.loginToUI(username, password);
       }


       public void testUploadAxis1Service() throws Exception {
           SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

           File resourcePath = new File("." + File.separator + "lib" + File.separator + "Axis1Service.jar");
           File wsddPath = new File("." + File.separator + "lib" + File.separator + "Axis1Service.wsdd");
           String ServiceName = "Axis1Service";

           //browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
           browser.click("link=Axis1 Service");
           browser.waitForPageToLoad("30000");
           InstSeleniumTestBase.SetFileBrowse("wsddFilename", wsddPath.getCanonicalPath());
           InstSeleniumTestBase.SetFileBrowse("jarResource", resourcePath.getCanonicalPath());
           browser.click("upload");
           browser.waitForPageToLoad("30000");
           assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis1 service"));
           Thread.sleep(2000);
           browser.click("//button[@type='button']");
           Thread.sleep(12000);
           browser.click("link=List");
           browser.waitForPageToLoad("120000");
           assertTrue(browser.isTextPresent(ServiceName));

       }

     public void testUploadAxis2Service() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "Axis2Service.aar");
        String ServiceName = "Axis2Service";

        //browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        System.out.println(aarPath.getCanonicalPath());
        InstSeleniumTestBase.SetFileBrowse("aarFilename", aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));


    }

     public void testUploadJarService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File artifactPath = new File("." + File.separator + "lib" + File.separator + "JarService.jar");
        String ServiceName = "JarService";


        browser.click("link=Jar Service");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add Jar Service"));
        assertTrue(browser.isTextPresent("Step 1 of 3: Upload a new archive"));
        assertTrue(browser.isTextPresent("Service Group Name*"));
        assertTrue(browser.isTextPresent("WSDL Document "));
        assertTrue(browser.isTextPresent("Artifact (.jar/.zip)*"));
        assertTrue(browser.isElementPresent("//input[@value=' Cancel ']"));
        assertTrue(browser.isElementPresent("//input[@value=' Next> ']"));

        browser.type("serviceGroupName", ServiceName);
        InstSeleniumTestBase.SetFileBrowse("resourceFileName", artifactPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add Jar Service"));
        assertTrue(browser.isTextPresent("Step 2 of 3: Select Classes to Expose as Web Services"));
        assertTrue(browser.isTextPresent("Class"));
        assertTrue(browser.isTextPresent("Service Name"));
        assertTrue(browser.isTextPresent("Deployment Scope"));
        //assertTrue(browser.isElementPresent("Use Original WSDL"));
        assertTrue(browser.isElementPresent("//input[@value=' Next> ']"));
        assertTrue(browser.isElementPresent("//input[@value=' <Back ']"));
        assertTrue(browser.isElementPresent("//input[@value=' Cancel ']"));

        browser.click("org.wso2.carbon.jarservice.JarService2Chk");
        browser.click("org.wso2.carbon.service.JarService1Chk");

        browser.click("//input[@value=' Next> ']");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add Jar Service"));
        assertTrue(browser.isTextPresent("Step 3 of 3: Select Methods to be Exposed as Web Service Operations"));
        assertTrue(browser.isTextPresent("Class: org.wso2.carbon.jarservice.JarService2 "));
        assertTrue(browser.isTextPresent("Class: org.wso2.carbon.service.JarService1"));
        assertTrue(browser.isElementPresent("//input[@value=' Finish ']"));
        assertTrue(browser.isElementPresent("//input[@value=' <Back ']"));
        assertTrue(browser.isElementPresent("//input[@value=' Cancel ']"));


        browser.click("//input[@value=' Finish ']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service archive successfully created. Please refresh this page in a while to see the status of the created Axis2 services"));
        Thread.sleep(2000);
        browser.click("//button[@type='button']");
        Thread.sleep(12000);

        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

     public void testUploadPojoService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File pojoPath = new File("." + File.separator + "lib" + File.separator + "PojoService.class");
        String ServiceName = "PojoService";

        //browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=POJO Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("jarZipFilename", pojoPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created POJO service"));

        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

     public void testUploadSpringService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File springContextPath = new File("." + File.separator + "lib" + File.separator + "context.xml");
        File springBeansPath = new File("." + File.separator + "lib" + File.separator + "SpringService.jar");
        String ServiceName = "SpringBean";


        browser.click("link=Spring Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add Spring Service"));
        assertEquals("Upload a New Spring Service (.xml and .jar)", browser.getText("//div[@id='workArea']/form/table/thead/tr/th"));
        InstSeleniumTestBase.SetFileBrowse("springContext", springContextPath.getCanonicalPath());
        InstSeleniumTestBase.SetFileBrowse("springBeans", springBeansPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Select Beans to Deploy"));
        assertEquals("Select Spring Beans to be exposed as Web services", browser.getText("//div[@id='workArea']/form/table/thead/tr/th"));
        assertTrue(browser.isElementPresent("//input[@value=' Select All ']"));
        assertTrue(browser.isElementPresent("//input[@value='Select None']"));
        browser.click("chkBeans");
        browser.click("//div[@id='workArea']/form/table/tbody/tr[3]/td/div/input[1]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page after a while to see the created service"));
        browser.click("//button[@type='button']");

        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

}
