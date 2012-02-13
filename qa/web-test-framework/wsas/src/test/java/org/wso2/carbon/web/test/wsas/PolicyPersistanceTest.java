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
import org.wso2.carbon.web.test.common.*;
import org.apache.axiom.om.OMElement;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

public class PolicyPersistanceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String hostname;
    String httpport;
    String contextroot;

    public PolicyPersistanceTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
       property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");
    }


    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);
    }

    public void testUploadServiceforPlicyCheck() throws Exception {
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

    public void testCheckThrottling() throws Exception {
        throttleClient instThrottleClientCall = new throttleClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.openServiceDashboard("Axis2Service", "axis2");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";


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
        browser.type("data13", "60000");
        browser.type("data14", "60000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        //assertTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");

        instServiceManagement.restartServer();

        int iThrottle = instThrottleClientCall.throttleClient(serviceepr,  "echoString", "urn:echoString","http://service.carbon.wso2.org", "s");
        Thread.sleep(1000);
        if (iThrottle == 7) {
            System.out.println("Throttling done.");
        } else {
            System.out.println("Throtlling failed.        Response count - " + iThrottle);
            instServiceManagement.openServiceDashboard("Axis2Service", "axis2");
            browser.click("link=Access Throttling");
            browser.waitForPageToLoad("30000");

            browser.select("enableThrottle", "label=No");
            assertTrue(browser.isTextPresent("This will disengage throttling. Click Yes to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent("Throtlling failed."));

        }
        Thread.sleep(1000);
        instServiceManagement.openServiceDashboard("Axis2Service", "axis2");
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");

        browser.select("enableThrottle", "label=No");
        assertTrue(browser.isTextPresent("This will disengage throttling. Click Yes to confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");

    }

    public void testCheckCaching() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        CachingClient instCachingClient = new CachingClient();

        instServiceManagement.accessServiceDashboard("Axis2Service");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";

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

        instServiceManagement.restartServer();

        String sCachData = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");
        for (int i = 0; i <= 5; i++) {
            String sCachingNow = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

            if (sCachData != null && !sCachData.equals(sCachingNow)) {
                 instServiceManagement.accessServiceDashboard("Axis2Service");
                browser.click("link=Response Caching");
                browser.waitForPageToLoad("30000");
             browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
                assertTrue(browser.isTextPresent("caching false"));
            } else if (sCachData != null && sCachData.equals(sCachingNow)) {
                System.out.println("Caching Done..");
            }
        }
        Thread.sleep(40000);

        String sCachingCurrent = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");
        if (sCachData != null && !sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Done..");
        } else if (sCachData != null && sCachData.equals(sCachingCurrent)) {
             instServiceManagement.accessServiceDashboard("Axis2Service");
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent("caching false"));

        }
        instServiceManagement.accessServiceDashboard("Axis2Service");
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
    }

    public void testReliableMessaging() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.enableRM("Axis2Service");
        instServiceManagement.restartServer();
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("Axis2Service");

    }

    public void testAddKeyStore() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");
    }

    public void testCheckSecurityScenario1() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario4",serviceName, nameSpace, soapAction, operation, "s");

    }

    public void testCheckSecurityScenario2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario1",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario3() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario3",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario4() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario2",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario5() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario13",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario6() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario10",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario7() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario12",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario8() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario4",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario9() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario11",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario10() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario15",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario11() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario7",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario12() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario5",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario13() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario8",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario14() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        checkPolicyPersistance("scenario6",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario15() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";
        checkPolicyPersistance("scenario14",serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testDeleteKeystore() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServicesfromPolicyTest() throws Exception {
           SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
           instSeleniumTestBase.deleteService("Axis2Service");
      }


    public void testlogOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

    public void checkPolicyPersistance(String scenario,String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.enableSecurityScenario(serviceName, scenario);
        instServiceManagement.restartServer();

        OMElement result = new SecurityClient().runSecurityClient(scenario, serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

}
