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

import com.thoughtworks.selenium.Selenium;
//import org.wso2.carbon.web.test.common.MexModuleClient;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.apache.axiom.om.OMElement;

import java.util.Properties;
import java.io.File;

import junit.framework.TestCase;

public class SOAPTracerTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;

    public SOAPTracerTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
           property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }


    public void testRun() throws Exception                 //Add new system user and login to wsas console
    {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);

    }

    public void testUploadServiceforSOAPTest() throws Exception {
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

    public void testSoapTracerConfigurationUi() throws Exception {
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("SOAP Message Tracer"));
        assertTrue(browser.isTextPresent("Configuration"));
        assertTrue(browser.isTextPresent("Status"));
        assertTrue(browser.isTextPresent("Tracer Persister"));
        assertTrue(browser.isTextPresent("Filter"));
        assertEquals("OFF", browser.getSelectedValue("monitorSetting"));

    }


    public void testEnableSOAPTracer() throws Exception {
        GenericServiceClient instGenericServiceClient = new GenericServiceClient();

        enableSOAPTracer();
        instGenericServiceClient.twoWayAnonClient("Axis2Service", "urn:echoString", "http://service.carbon.wso2.org", "echoString", "s", "jayani");
        //GenericServiceClient.onewayAnonClient("Axis2Service","urn:Ping","http://service.carbon.wso2.org", "Ping", "s");
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Axis2Service.echoString"));
        assertTrue(browser.isTextPresent("Envelope"));
        assertTrue(browser.isTextPresent("Body"));
        disableSOAPTracer();
    }

    public void testSearchQuery() throws Exception {
        GenericServiceClient instGenericServiceClient = new GenericServiceClient();

        enableSOAPTracer();

        String[] animals = {"Alligator", "Bat", "Cat", "Dog", "Elephant", "Fly", "Goat", "Hen", "Iguana", "Jaguar"};
        int i;
        for (i = 0; i < animals.length; i++) {
            instGenericServiceClient.twoWayAnonClient("Axis2Service", "urn:echoString", "http://service.carbon.wso2.org", "echoString", "s", "Hi_" + animals[i]);
        }

        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Axis2Service.echoString"));
        assertTrue(browser.isTextPresent("Envelope"));
        assertTrue(browser.isTextPresent("Body"));


        for (i = 0; i < animals.length; i++) {
            searchQuery(animals[i]);
            System.out.println(animals[i]);
            assertTrue(browser.isTextPresent("Axis2Service.echoString"));
            System.out.println("Hi_" + animals[i]);
            assertTrue(browser.isTextPresent("Hi_" + animals[i]));

        }

    }

    public void testClearQuery() throws Exception {
        browser.type("filterText", "");
        browser.click("filterClear");
        assertEquals(browser.getText("filterText"), "");
        assertTrue(browser.isTextPresent("Axis2Service.echoString"));
        assertTrue(browser.isTextPresent("Envelope"));
        assertTrue(browser.isTextPresent("Body"));
        disableSOAPTracer();
    }

    public void testOneWayMessage() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        GenericServiceClient instGenericServiceClient = new GenericServiceClient();

        instSeleniumTestBase.logOutUI();
             instSeleniumTestBase.loginToUI(username, password);

        enableSOAPTracer();
        instGenericServiceClient.onewayAnonClient("Axis2Service", "urn:Ping", "http://service.carbon.wso2.org", "Ping", "s");

        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Axis2Service.Ping"));
        assertTrue(browser.isTextPresent("Envelope"));
        assertTrue(browser.isTextPresent("Body"));
        assertFalse(browser.isTextPresent("RelatesTo"));

    }

    public void testSecureService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SecurityClient instSecurityClient = new SecurityClient();

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.enableSecurityScenario("Axis2Service", "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");

        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Security"));
        assertTrue(browser.isTextPresent("Timestamp"));
        assertTrue(browser.isTextPresent("Created"));
        assertTrue(browser.isTextPresent("Expires"));
        assertTrue(browser.isTextPresent("UsernameToken"));
        assertTrue(browser.isTextPresent("Username"));
        assertTrue(browser.isTextPresent("Password"));

        instServiceManagement.disableSecurity("Axis2Service");

    }

    public void testCaching() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root")+ "/services/" + "Axis2Service";

        instServiceManagement.accessServiceDashboard("Axis2Service");

        CachingClient instCachingClient = new CachingClient();
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
        String sCachData = instCachingClient.cachClient(serviceepr,"getTime", "urn:getTime", "http://service.carbon.wso2.org");
        System.out.println("sCachData :"+sCachData);
         browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");

        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("urn:getTime"));
        assertTrue(browser.isTextPresent("RelatesTo"));
        assertTrue(browser.isTextPresent("To"));
        assertTrue(browser.isTextPresent("RelatesTo"));
        assertTrue(browser.isTextPresent("getTimeResponse"));
        assertTrue(browser.isTextPresent("return"));

        disableSOAPTracer();
        //instSeleniumTestBase.logOutUI();


    }

    public void testThrottling() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        InstSeleniumTestBase.logOutUI();
        InstSeleniumTestBase.loginToUI(username, password);
       
        enableSOAPTracer();
        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + "Axis2Service";
        instServiceManagement.accessServiceDashboard("Axis2Service");

        throttleClient instThrottleClientCall = new throttleClient();

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
        browser.type("data12", "7");
        browser.type("data13", "50000");
        browser.type("data14", "50000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        //assertTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(1000);
        int iThrottle = instThrottleClientCall.throttleClient(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s");
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");

        browser.select("enableThrottle", "label=No");
        assertTrue(browser.isTextPresent("This will disengage throttling. Click Yes to confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
        
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Envelope"));
        assertTrue(browser.isTextPresent("Body"));
        assertTrue(browser.isTextPresent("Fault"));
        assertTrue(browser.isTextPresent("Reason"));
        assertTrue(browser.isTextPresent("faultcode"));


        disableSOAPTracer();
        //instServiceManagement.disableSecurity("Axis2Service");
    }

//    public void testHelp() throws Exception{
//        String tryitwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
//        browser.selectWindow(tryitwinid);
//        Thread.sleep(1000);
//        assertTrue(browser.isTextPresent("SOAP Monitor Tracer"));
//        assertTrue(browser.isTextPresent("Turning on the Tracer feature will have a drag on the performance."));
//        assertTrue(browser.isTextPresent("The Tracer shows the SOAP messages, the respective SOAP message requests and responses "));
//        browser.close();
//        browser.selectWindow("");
//    }

    public void enableSOAPTracer() throws Exception {
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("SOAP Message Tracer"));
        assertTrue(browser.isTextPresent("Configuration"));
        assertTrue(browser.isTextPresent("Status"));
        assertTrue(browser.isTextPresent("Tracer Persister"));
        assertTrue(browser.isTextPresent("Filter"));
        browser.select("monitorSetting", "label=ON");
        assertEquals("ON", browser.getSelectedValue("monitorSetting"));
        assertTrue(browser.isTextPresent("Messages"));
        assertTrue(browser.isTextPresent("Request"));
        assertTrue(browser.isTextPresent("Response"));
    }

    public void disableSOAPTracer() throws Exception {
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("SOAP Message Tracer"));
        assertTrue(browser.isTextPresent("Configuration"));
        assertTrue(browser.isTextPresent("Status"));
        assertTrue(browser.isTextPresent("Tracer Persister"));
        assertTrue(browser.isTextPresent("Filter"));

        browser.select("monitorSetting", "label=OFF");
        assertEquals("OFF", browser.getSelectedValue("monitorSetting"));
        //assertFalse(browser.isTextPresent("Messages"));
        //assertFalse(browser.isTextPresent("Request"));
        //assertFalse(browser.isTextPresent("Response"));


    }

    public void searchQuery(String query) throws Exception {
        browser.click("link=SOAP Tracer");
        browser.waitForPageToLoad("30000");
        browser.type("filterText", query);
        browser.click("filterSearch");

    }

    public void testRemoveServicesfromSOAPTests() throws Exception {
           SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
           instSeleniumTestBase.deleteService("Axis2Service");
      }

    public void testLogoutSystem() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();

    }


}
