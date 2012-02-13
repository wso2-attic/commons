package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.File;

import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.SamplesCommon;
import org.wso2.carbon.web.test.common.ServiceManagement;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Nov 27, 2009
 * Time: 11:59:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataServiceSampleTest extends TestCase {
    Selenium browser,client;
    Properties property;
    String username;
    String password;

    public DataServiceSampleTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");

    }

    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);
    }

    public void testAntBuildForDataService() throws Exception{
        SamplesCommon wc=new SamplesCommon();

        boolean result=wc.runAnt(property.getProperty("carbon.home") + File.separator + "samples" + File.separator + "DataService"  + File.separator + "build.xml","deploy");
        assertTrue("BUILD SUCCESSFUL!!!",result);
        Thread.sleep(10000);
    }

    public void testDataServiceSample1() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("DataServiceSample1"));

    }

    public void testDataServiceSample2() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("DataServiceSample2"));

    }

    public void testRunClient1() throws Exception{
        SamplesCommon wc=new SamplesCommon();

        boolean result=wc.runAnt(property.getProperty("carbon.home") + File.separator + "samples" + File.separator + "DataService"  + File.separator + "build.xml","run-client-1");
        assertTrue("BUILD SUCCESSFUL!!!",result);
        Thread.sleep(10000);
    }

    public void testRunClient2() throws Exception{
        SamplesCommon wc=new SamplesCommon();

        boolean result=wc.runAnt(property.getProperty("carbon.home") + File.separator + "samples" + File.separator + "DataService"  + File.separator + "build.xml","run-client-2");
        assertTrue("BUILD SUCCESSFUL!!!",result);
        Thread.sleep(10000);
    }

    public void testRemoveDataServiceSample1() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("DataServiceSample1");
    }

    public void testRemoveDataServiceSample2() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("DataServiceSample2");
    }


    public void testLogoutUI() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }

}
