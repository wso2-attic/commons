/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.File;

import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.common.*;
import org.apache.axiom.om.OMElement;


/**
 * tests operational caching
 * tests operational throttling
 */

public class RandomTest extends TestCase {
    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String httpsport;
    String contextroot;

    public RandomTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        httpsport = property.getProperty("https.be.port");
        contextroot = property.getProperty("context.root");
    }



    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);

    }

    public void testUploadAxis2ServiceForRandomTests() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "Axis2Service.aar");
        String ServiceName = "Axis2Service";

        //browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
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


    public void testOperationalCachingAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();

        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";

        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service", "getTime");
        instServiceManagement.checkCaching(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");


    }

    public void testMTOMAtServiceGroup() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.changeServiceGroupMTOM("Axis2Service", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("Axis2Service", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("Axis2Service", "False");
        Thread.sleep(1000);

    }

    public void testMTOMAtOperationalLevel() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        insServiceManagement.changeOperationalMTOM("Axis2Service", "echoString", "True");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("Axis2Service", "echoString", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("Axis2Service", "echoString", "False");
        Thread.sleep(1000);

    }

    public void testOperationalThrottlingAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();

        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";

        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service", "echoString");
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s");

    }

    public void testAddKeyStoreAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityWithThrottling() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        OMElement result;

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        Thread.sleep(1000);
        assertTrue(browser.isTextPresent("Access Throttling"));
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");
        if ((browser.getSelectedValue("enableThrottle")).equals("Yes")) {
            browser.select("enableThrottle", "label=No");
        }
        browser.select("enableThrottle", "label=Yes");
        browser.click("//input[@value='Default']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Delete"));
        browser.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
        browser.select("data15", "label=Control");
        browser.type("data12", "3");
        browser.type("data13", "50000");
        browser.type("data14", "10000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        //assertTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");
       

        instServiceManagement. enableSecurityScenario(serviceName, "scenario1");
        for(int i=0;i<3;i++){
            result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
            System.out.println(i+ " =i  " + result.getFirstElement().getText());
            assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        }

        try{
            result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
            System.out.println(result.getFirstElement().getText());
            if(result.getFirstElement().getText().equals("Invoking Security Scenario!!!!")){
                assertTrue("Throttling failed",false);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        Thread.sleep(15000);

        for(int i=0;i<3;i++){
            result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
            System.out.println(i + "    " +result.getFirstElement().getText());
            assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        }


    }

    public void testDisableThrottling() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis2Service");
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");
        browser.click("link=Add New Entry");
        browser.type("data21", "other");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");

        browser.select("enableThrottle", "label=No");
        assertTrue(browser.isTextPresent("This will disengage throttling. Click Yes to confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");


        instServiceManagement.disableSecurity("Axis2Service");

    }

    public void testCheckSecurityWithCaching() throws Exception {
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:getTime";
        String operation = "getTime";


        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        if ((browser.getSelectedValue("enable")).equals("Yes")) {
            browser.select("enable", "label=No");
        }

        browser.select("enable", "label=Yes");
        browser.type("timeoutField", "30000");
        browser.click("Submit");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(500);

        instServiceManagement. enableSecurityScenario("Axis2Service", "scenario1");

        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
        String sCachData = result.getFirstElement().getText();
        System.out.println("sCachData :"+sCachData);
        for (int i = 0; i <= 5; i++) {
            result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");

            if (sCachData != null && !sCachData.equals(result.getFirstElement().getText())) {
                System.out.println("Caching Not Done..");
                assertTrue(browser.isTextPresent("Caching Done.."));
            } else if (sCachData != null && sCachData.equals(result.getFirstElement().getText())) {
                System.out.println("Caching Done..");
            }
        }

        Thread.sleep(40000);
        result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
        String sCachingCurrent = result.getFirstElement().getText();
        System.out.println("sCachingCurrent :"+sCachingCurrent);
        if (sCachData != null && !sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Done..");
        } else if (sCachData != null && sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Not Done..");
            assertTrue(browser.isTextPresent("Caching Done.."));
        }

    }

    public void testDisableCaching() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");

        instServiceManagement.disableSecurity("Axis2Service");
    }



    public void testRemoveServicesFromRandomTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }


    public void testlogOutRandomTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
