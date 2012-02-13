package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.util.Random;
import java.io.File;

import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.ds.common.InstanceCreationSetup;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.SamplesCommon;


/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Nov 25, 2009
 * Time: 2:47:36 PM
 * To change this template use File | Settings | File Templates.
 */

/*
* tests chad sample
* to do assert  whether the poll is successfully created
* */
public class ChadSampleTest extends TestCase {

    Selenium browser,client;
    Properties property;
    String username;
    String password;
    String moduleVersion ;

    public ChadSampleTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
    }

    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);

    }
    public void testAntBuild() throws Exception{
       SamplesCommon wc=new SamplesCommon();


        boolean result=wc.runAnt(property.getProperty("carbon.home") + File.separator + "samples" + File.separator + "Chad"  + File.separator + "build.xml","ant");
        assertTrue("BUILD SUCCESSFUL!!!",result);
        Thread.sleep(10000);
    }

    public void testChadSample() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("Chad"));

    }


    public void testChadClientUI() throws Exception{
        InstanceCreationSetup browser2 = new InstanceCreationSetup("browser2");

        String strURL = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/wservices/Chad" ;
        client= browser2.openBrowser(strURL);

        assertTrue(client.isTextPresent("WSO2 Web Services Application Server 1.1"));
        assertTrue(client.isTextPresent("Chad"));

        client.click("link=Adminstrator Login");
        client.type("txtUserName", "admin");
        client.type("txtPassword", "admin");

        client.click("signin");
        assertEquals("Active Polls", client.getText("menu_active_polls2"));
        assertEquals("Create Poll", client.getText("menu_create_poll"));
        assertEquals("All Polls", client.getText("menu_list_poll"));
        assertEquals("Stopped Polls", client.getText("menu_stopped_polls"));
        assertEquals("Administrators", client.getText("menu_adminstrators"));
        Thread.sleep(10000);

        int m = (int)(100*Math.random());

        client.click("menu_create_poll");
        assertEquals("Create Poll", client.getText("//div[@id='divCreatPoll']/h2"));
        client.type("txtPollTitle", "MyPoll"+m);
        client.type("txtPollDescription", "Select the most popular soccer team to win 2006 world");
        client.type("txtPollChoices1", "Brazil");
        client.type("txtPollChoices0", "France");
        client.click("//input[@value='Create']");
        // assertTrue(client.isTextPresent("MyPoll"));
        client.click("alertBoxButton");

        Thread.sleep(10000);
        client.stop();
    }

    public void testRemoveChadSample() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("Chad");
    }

    public void testLogoutUI() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }


}
