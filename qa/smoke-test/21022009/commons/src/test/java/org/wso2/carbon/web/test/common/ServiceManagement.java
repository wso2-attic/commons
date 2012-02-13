/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

import java.awt.event.KeyEvent;

import org.apache.axiom.om.OMElement;


public class ServiceManagement extends TestCase

{
    Selenium browser;
    //   ReadXMLFile instReadXMLFile = new ReadXMLFile();


    public ServiceManagement(Selenium _browser) {
        browser = _browser;
    }

    public void serviceActivation(String serviceName, String Option) throws Exception     // Option should "Deactivate" or "Activate"
    {
        accessServiceDashboard(serviceName);
        if (Option == "Deactivate") {
            browser.click("link=Deactivate");
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Inactive"));
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Activate"));
        } else if (Option == "Activate") {
            browser.click("link=Activate");
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Active"));
        }
    }

    //Common method to access service dashboard
    public void accessServiceDashboard(String serviceName) throws InterruptedException {
        //Thread.sleep(5000);
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("link=" + serviceName);
        browser.waitForPageToLoad("30000");
        String pageTitle = browser.getText("//div[@id='middle']/h2");

        String[] tempString = null;
        tempString = pageTitle.split(" ");
        String newPageTitle = tempString[0].concat(tempString[1]);
        if ("ServiceGroup".equals(newPageTitle)) {
            browser.click("link=" + serviceName);
            browser.waitForPageToLoad("30000");
        }
        Thread.sleep(1000);
    }

    public void openServiceDashboard(String serviceName, String serviceType) throws Exception          // Traversing Service Management Page.
    {
        //  String serverIp = instReadXMLFile.ReadConfig("ServerIp","Genaral");

        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("link=" + serviceName);
        browser.waitForPageToLoad("30000");
        String pageTitle = browser.getText("//div[@id='middle']/h2");

        String[] tempString = null;
        tempString = pageTitle.split(" ");
        String newPageTitle = tempString[0].concat(tempString[1]);
        if ("ServiceGroup".equals(newPageTitle)) {
            browser.click("link=" + serviceName);
            browser.waitForPageToLoad("30000");
        }

        assertTrue(browser.isTextPresent(serviceName));
        String getService = browser.getText("//table[@id='serviceInfoTable']/tbody/tr[5]/td[2]");
        if (serviceType != null && !serviceType.equals(getService)) {
            assertTrue(browser.isTextPresent("Wrong ServiceType"));
        }
        assertTrue(browser.isTextPresent("Active"));
        assertTrue(browser.isTextPresent("Security"));
        assertTrue(browser.isTextPresent("Reliable Messaging"));
        assertTrue(browser.isTextPresent("Response Caching"));
        assertTrue(browser.isTextPresent("Access Throttling"));
        assertTrue(browser.isTextPresent("Policies"));
        assertTrue(browser.isTextPresent("Transports"));
        assertTrue(browser.isTextPresent("Modules"));
        assertTrue(browser.isTextPresent("Operations"));
        assertTrue(browser.isTextPresent("Parameters"));
        assertTrue(browser.isTextPresent("Client Operations"));
        assertTrue(browser.isTextPresent("Try this service"));
        assertTrue(browser.isTextPresent("Generate Client"));
        assertTrue(browser.isTextPresent("WSDL1.1"));
        assertTrue(browser.isTextPresent("WSDL2.0"));
        assertTrue(browser.isTextPresent("Endpoints"));
        assertTrue(browser.isTextPresent("Statistics"));
        //	assertTrue(browser.isTextPresent("exact:https://"+serverIp+":9443/services/"+serviceName));
        //	assertTrue(browser.isTextPresent("exact:http://"+serverIp+":9763/services/"+serviceName));


    }

    public void openOperationDashboard(String serviceName, String operationName)   // traversing to Operation dashboard
    {
        assertTrue(browser.isTextPresent("Operations"));
        browser.click("link=Operations");
        browser.waitForPageToLoad("30000");
        browser.click("link=" + operationName);
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(operationName));
        assertTrue(browser.isTextPresent(serviceName));
        assertTrue(browser.isTextPresent("Operation Details"));
        assertTrue(browser.isTextPresent("Quality of Service Configuration"));
        assertTrue(browser.isTextPresent("Response Caching"));
        assertTrue(browser.isTextPresent("Access Throttling"));
        assertTrue(browser.isTextPresent("Flows"));
        assertTrue(browser.isTextPresent("Modules"));
        assertTrue(browser.isTextPresent("Parameters"));
        assertTrue(browser.isTextPresent("Statistics"));
        assertTrue(browser.isTextPresent("Request Count"));
        assertTrue(browser.isTextPresent("Response Count"));
        assertTrue(browser.isTextPresent("Fault Count"));
        assertTrue(browser.isTextPresent("Maximum Response Time"));
        assertTrue(browser.isTextPresent("Minimum Response Time"));
        assertTrue(browser.isTextPresent("Average Response Time"));
        assertTrue(browser.isTextPresent("Average Response Time(ms) vs. Time(Units)"));
    }

    public void checkThrottling(String ServiceEpr, String operationName, String SoapAction, String namespace, String firstChild) throws Exception      // Throttle checking (Throttling configuration hardcoded)
    {
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
        assertTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(1000);
        int iThrottle = instThrottleClientCall.throttleClient(ServiceEpr, operationName, SoapAction, namespace, firstChild);
        Thread.sleep(1000);
        if (iThrottle == 7) {
            System.out.println("Throttling done.");
        } else {
            System.out.println("Throtlling failed.        Response count - " + iThrottle);
            assertTrue(browser.isTextPresent("Undefined"));
        }
        Thread.sleep(1000);
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");

        browser.select("enableThrottle", "label=No");
        assertTrue(browser.isTextPresent("This will disengage throttling from the service. Click Yes to confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
    }

    public void checkCaching(String ServiceEpr, String operationName, String SoapAction, String Namespace) throws Exception              // matching output of the client call because better to use system time output service.
    {
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
        String sCachData = instCachingClient.cachClient(ServiceEpr, operationName, SoapAction, Namespace);
        for (int i = 0; i <= 5; i++) {
            String sCachingNow = instCachingClient.cachClient(ServiceEpr, operationName, SoapAction, Namespace);
            if (sCachData != null && !sCachData.equals(sCachingNow)) {
                assertTrue(browser.isTextPresent("caching false"));
            } else if (sCachData != null && sCachData.equals(sCachingNow)) {
                System.out.println("Caching Done..");
            }
        }
        Thread.sleep(40000);

        String sCachingCurrent = instCachingClient.cachClient(ServiceEpr, operationName, SoapAction, Namespace);
        if (sCachData != null && !sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Done..");
        } else if (sCachData != null && sCachData.equals(sCachingCurrent)) {
            assertTrue(browser.isTextPresent("caching false"));
        }
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
    }


    public void verifyBindingPolicy(String serviceName, String policyid, String scenarioname) throws InterruptedException {

        enableSecurityScenario(serviceName, scenarioname);
        accessServiceDashboard(serviceName);
        browser.click("link=Policies");
        browser.waitForPageToLoad("30000");
        assertEquals("Service Hierarchy", browser.getTable("service-hierarchy-table.0.0"));
        assertEquals("Binding Hierarchy", browser.getTable("binding-hierarchy-table.0.0"));
        Thread.sleep(5000);
        browser.focus("//input[@value='Edit Policy']");
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_TAB);
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_TAB);
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_TAB);
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_TAB);
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_TAB);
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_TAB);
        Thread.sleep(1000);
        browser.keyDownNative("" + KeyEvent.VK_ENTER);
        browser.waitForPageToLoad("30000");
        assertTrue(browser.getValue("raw-policy").indexOf(policyid) > 0);
        browser.click("go-back");
        browser.waitForPageToLoad("30000");
        disableSecurity(serviceName);
    }

    public void assignConfidentialitySecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario5");
        OMElement result = new SecurityClient().runSecurityClient("scenario5", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignUTSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {


        enableSecurityScenario(serviceName, "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    //
    public void assignIntegritySecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario4");
        OMElement result = new SecurityClient().runSecurityClient("scenario4", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignNonRepudiationSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario2");
        OMElement result = new SecurityClient().runSecurityClient("scenario2", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConEncrOnlyAnonymousSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario13");
        OMElement result = new SecurityClient().runSecurityClient("scenario13", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConSignOnlySecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario9");
        OMElement result = new SecurityClient().runSecurityClient("scenario9", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConEncrOnlySecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario10");
        OMElement result = new SecurityClient().runSecurityClient("scenario10", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConSignOnlyAnonymousSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario12");
        OMElement result = new SecurityClient().runSecurityClient("scenario12", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConSgnEncrSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario11");
        OMElement result = new SecurityClient().runSecurityClient("scenario11", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConSgnEncrUsernameSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario15");
        OMElement result = new SecurityClient().runSecurityClient("scenario15", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignEncrOnlyUsernameSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario7");
        OMElement result = new SecurityClient().runSecurityClient("scenario7", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSigEncrSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario3");
        OMElement result = new SecurityClient().runSecurityClient("scenario3", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSgnEncrUsernameSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario8");
        OMElement result = new SecurityClient().runSecurityClient("scenario8", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }


    public void assignSgnEncrAnonymousScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario6");
        OMElement result = new SecurityClient().runSecurityClient("scenario6", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }

    public void assignSecConEncrUsernameSecurityScenario(String serviceName, String nameSpace, String SoapAction, String operation, String param) throws Exception {

        enableSecurityScenario(serviceName, "scenario14");
        OMElement result = new SecurityClient().runSecurityClient("scenario14", serviceName, nameSpace, SoapAction, operation, param);
        System.out.println(result);
        assertEquals("Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        disableSecurity(serviceName);
    }


    public void enableSecurityScenario(String serviceName, String scenarioid) throws InterruptedException {
        Thread.sleep(1000);
        accessServiceDashboard(serviceName);
        Thread.sleep(1000);
        browser.click("link=Security");
        browser.waitForPageToLoad("30000");

        if ((browser.getSelectedValue("securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            assertTrue(browser.isTextPresent("This will disable security from the service. Click OK to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Security disabled successfully."));
            browser.click("//button[@type='button']");
        }
        assertEquals("Security for the service", browser.getText("//div[@id='middle']/h2"));
        assertTrue(browser.isTextPresent("The service \"" + serviceName + "\" is not secured."));
        browser.select("securityConfigAction", "label=Yes");
        assertEquals("Security Scenario", browser.getTable("//form[@id='secConfigForm']/table.0.0"));
        assertEquals("Advanced Scenarios", browser.getTable("//form[@id='secConfigForm']/table.5.0"));
        Thread.sleep(1000);
        browser.click("//input[@name='scenarioId' and @value='" + scenarioid + "']");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        if (scenarioid.equals("scenario1")) {
            browser.click("userGroups");
            browser.click("//input[@value='Finish']");
            browser.waitForPageToLoad("30000");
        } else if (scenarioid.equals("scenario15") || scenarioid.equals("scenario7") || scenarioid.equals("scenario8") || scenarioid.equals("scenario14")) {
            browser.click("userGroups");
            browser.click("//input[@name='trustStore' and @value='qaserver.jks']");
            browser.click("//input[@value='Finish']");
            browser.waitForPageToLoad("30000");
        } else {
            browser.click("//input[@name='trustStore' and @value='qaserver.jks']");
            browser.click("//input[@value='Finish']");
            browser.waitForPageToLoad("30000");
        }


//        browser.click("//input[@name='trustStore' and @value='qaserver.jks']");
//        browser.click("//input[@value='Finish']");
//        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Security applied successfully."));
        browser.click("//button[@type='button']");


    }

    public void disableSecurity(String serviceName) throws InterruptedException {
        Thread.sleep(1000);
        accessServiceDashboard(serviceName);
        Thread.sleep(1000);
        browser.click("link=Security");
        browser.waitForPageToLoad("30000");
        if ((browser.getSelectedValue("id=securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
        } else {
            System.out.println("Security is already disabled");
        }
        assertTrue(browser.isTextPresent("The service \"" + serviceName + "\" is not secured."));
        Thread.sleep(3000);

    }

    public void addServiceGroupParameter(String serviceGroupName, String parameterName) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        accessServiceGroupPage(serviceGroupName);
        instSeleniumTestBase.addNewGroupParameter(parameterName);
    }

    public void deleteServiceGroupParameter(String serviceGroupName, String parameterName) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        accessServiceGroupPage(serviceGroupName);
        instSeleniumTestBase.deleteGroupParameter(parameterName);
    }

    public void engageServiceModule(String serviceName, String moduleName) throws Exception     // Service level module engage method
    {
        accessServiceDashboard(serviceName);
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module"));
        assertTrue(browser.isTextPresent("Currently Engaged Modules"));
        assertTrue(browser.isTextPresent("Service Level"));
        assertTrue(browser.isTextPresent("Service Group Level"));
        assertTrue(browser.isTextPresent("Global level"));
        Thread.sleep(1000);
        browser.select("moduleSelector", "label=" + moduleName);
        Thread.sleep(1000);
        browser.click("//input[@value=' Engage ']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully engaged."));
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(moduleName));
    }

    public void engageServiceGroupModule(String serviceGroupName, String moduleName) throws Exception {
        Thread.sleep(2000);

        accessServiceGroupPage(serviceGroupName);

        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        Thread.sleep(1000);
        browser.select("moduleSelector", "label=" + moduleName);
        Thread.sleep(1000);
        browser.click("//input[@value=' Engage ']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully engaged."));
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(moduleName));
    }

    public void disengageServiceModule(String serviceName, String moduleName) throws Exception {
        accessServiceDashboard(serviceName);
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule)) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
        assertTrue(browser.isTextPresent("exact:Do you really want to disengage " + moduleName + " module from " + serviceName + " service ?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully disengaged."));
        browser.click("//button[@type='button']");
    }

    public void disengageServiceGroupModule(String serviceGroupName, String moduleName) throws Exception {
        Thread.sleep(2000);
        accessServiceGroupPage(serviceGroupName);

        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule)) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
        assertTrue(browser.isTextPresent("exact:Do you really want to disengage " + moduleName + " module from " + serviceGroupName + " service group"));

        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully disengaged."));
        browser.click("//button[@type='button']");
    }

    public void checkTransport(String ServiceName) throws Exception {
        accessServiceDashboard(ServiceName);
        browser.click("//table[@id='serviceOperationsTable']/tbody/tr[3]/td[2]/a");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Transport Management"));
        assertTrue(browser.isTextPresent("Add Transports"));
        assertTrue(browser.isTextPresent("Protocol"));
        assertTrue(browser.isTextPresent("Exposed Transports"));
        assertTrue(browser.isTextPresent("Protocol"));
        assertTrue(browser.isTextPresent("https"));
        assertTrue(browser.isTextPresent("http"));
        String protocol = browser.getText("//table[@id='exposedTransports']/tbody/tr[2]/td[1]");
        String protocol2 = browser.getText("//table[@id='exposedTransports']/tbody/tr[1]/td[1]");

        browser.click("//table[@id='exposedTransports']/tbody/tr[2]/td[2]/a[2]/img");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("exact:Protocol: " + protocol));
        assertTrue(browser.isTextPresent("Parameter Name"));
        assertTrue(browser.isTextPresent("port"));
        assertTrue(browser.isTextPresent("Parameter Value"));
        if ("http".equals(protocol)) {
            assertTrue(browser.isTextPresent("9763"));
        }
        if ("https".equals(protocol)) {
            assertTrue(browser.isTextPresent("9443"));
        }
        browser.click("//input[@value='Back']");
        browser.waitForPageToLoad("30000");
        browser.click("//table[@id='exposedTransports']/tbody/tr[2]/td[2]/a[1]/img");
        assertTrue(browser.isTextPresent("exact:Do you want to remove " + protocol + " binding for this service ?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        if ("http".equals(protocol)) {
            browser.select("protocol", "label=http");
        }
        if ("https".equals(protocol)) {
            browser.select("protocol", "label=https");
        }
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(protocol));
        //     }
        //      if ("https".equals(protocol2)) {
        browser.click("//table[@id='exposedTransports']/tbody/tr[1]/td[2]/a[2]/img");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("exact:Protocol: " + protocol2));
        assertTrue(browser.isTextPresent("Parameter Name"));
        assertTrue(browser.isTextPresent("port"));
        assertTrue(browser.isTextPresent("Parameter Value"));
        if ("http".equals(protocol2)) {
            assertTrue(browser.isTextPresent("9763"));
        }
        if ("https".equals(protocol2)) {
            assertTrue(browser.isTextPresent("9443"));
        }
        browser.click("//input[@value='Back']");
        browser.waitForPageToLoad("30000");
        browser.click("//table[@id='exposedTransports']/tbody/tr[1]/td[2]/a[1]/img");
//            assertTrue(browser.isTextPresent("exact:Do you want to remove "+protocol2+" binding for this service ?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        if ("http".equals(protocol2)) {
            browser.select("protocol", "label=http");
        }
        if ("https".equals(protocol2)) {
            browser.select("protocol", "label=https");
        }
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(protocol2));
        //     }
    }

    public void engageOperationModule(String serviceName, String operationName, String moduleName) throws Exception {
        accessServiceDashboard(serviceName);
        openOperationDashboard(serviceName, operationName);
        browser.click("module_view_link");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module"));
        assertTrue(browser.isTextPresent("Currently Engaged Modules"));
        assertTrue(browser.isTextPresent("Service Level"));
        assertTrue(browser.isTextPresent("Service Group Level"));
        assertTrue(browser.isTextPresent("Global level"));
        Thread.sleep(1000);
        browser.select("moduleSelector", "label=" + moduleName);
        Thread.sleep(1000);
        browser.click("//input[@value=' Engage ']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully engaged."));
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(moduleName));
    }

    public void disengageOperationModule(String serviceName, String operationName, String moduleName) throws Exception {
        accessServiceDashboard(serviceName);
        openOperationDashboard(serviceName, operationName);
        browser.click("module_view_link");
        browser.waitForPageToLoad("30000");
        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule)) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
        assertTrue(browser.isTextPresent("exact:Do you really want to disengage " + moduleName + " module from " + operationName + " operation"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully disengaged."));
        browser.click("//button[@type='button']");
    }

    public void checkParamFlows() throws Exception {
        browser.click("handler_view_link");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Show Text View"));
        assertTrue(browser.isTextPresent("Message Flows (Graphical View)"));
        assertTrue(browser.isTextPresent("In Flow"));
        assertTrue(browser.isTextPresent("Out Flow"));
        assertTrue(browser.isTextPresent("In Fault Flow"));
        assertTrue(browser.isTextPresent("Out Fault Flow"));
        browser.click("link=Show Text View");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Show Graphical View"));
        assertTrue(browser.isTextPresent("In Flow"));
        assertTrue(browser.isTextPresent("Out Flow"));
        assertTrue(browser.isTextPresent("In Fault Flow"));
        assertTrue(browser.isTextPresent("Out Fault Flow"));
        browser.click("link=Show Graphical View");
        browser.waitForPageToLoad("30000");
    }

    public void enableRM(String serviceName) throws Exception {
        accessServiceDashboard(serviceName);
        browser.click("link=Reliable Messaging");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Enable Reliable Messaging for " + serviceName));
        browser.select("isRMEngaged", "label=Yes");
        browser.click("Done");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied reliable messaging configuration"));
        browser.click("//button[@type='button']");
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Currently Engaged Modules"));
        assertTrue(browser.isTextPresent("sandesha2"));
    }

    public void disableRM(String serviceName) throws InterruptedException {
        accessServiceDashboard(serviceName);
        browser.click("link=Reliable Messaging");
        browser.waitForPageToLoad("30000");
        if ((browser.getSelectedValue("name=isRMEngaged")).equals("true")) {
            browser.select("isRMEngaged", "label=No");
            assertTrue(browser.isTextPresent("This will disengage Reliable Messaging from your service. Click OK to confirm."));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Successfully applied reliable messaging configuration"));
            browser.click("//button[@type='button']");
        } else {
            System.out.println("RM is already disabled");
        }
        Thread.sleep(500);

    }

    public void rmInvocations(String serviceName, String SoapAction, String NameSpace, String operation, String param) throws Exception {
        RMClient rmClient = new RMClient();

        //Request-Reply invocation
        int soap11_response_count = rmClient.RMRequestReplyAnonClient(serviceName, "soap11", SoapAction, NameSpace, operation, param);
        int soap12_response_count = rmClient.RMRequestReplyAnonClient(serviceName, "soap12", SoapAction, NameSpace, operation, param);
        assertEquals(11, soap11_response_count);
        assertEquals(11, soap12_response_count);

        //Oneway invocation
        rmClient.RMOnewayAnonClient(serviceName, "soap11", SoapAction, NameSpace, operation, param);

        //Dual Channel Twoway invocation
        int soap11_addressable_response_count = rmClient.RMRequestReplyAddressableClient(serviceName, "soap11", SoapAction, NameSpace, operation, param);
        int soap12_addressable_response_count = rmClient.RMRequestReplyAddressableClient(serviceName, "soap12", SoapAction, NameSpace, operation, param);
        assertEquals(11, soap11_addressable_response_count);
        assertEquals(11, soap12_addressable_response_count);
    }

    public void accessServiceGroupPage(String serviceGroupName) throws Exception {
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        String readGroupName = "";
        int i = 1;

        while (!serviceGroupName.equals(readGroupName)) {
            Thread.sleep(1000);
            readGroupName = browser.getText("//table[@id='sgTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]");

            i = i + 1;


        }
        i = i - 1;
        browser.click("//table[@id='sgTable']/tbody/tr[" + i + "]/td[2]/nobr/a");
        browser.waitForPageToLoad("30000");
    }

    public void changeServiceGroupMTOM(String serviceGroupName, String mtomState) throws Exception {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        accessServiceGroupPage(serviceGroupName);
        inSeleniumTestBase.changeMTOMState(mtomState);

    }

    public void changeServiceMTOM(String serviceName, String motmOption) throws Exception {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        accessServiceDashboard(serviceName);
        inSeleniumTestBase.changeMTOMState(motmOption);

    }

    public void changeOperationalMTOM(String serviceName, String operationName, String mtomState) throws Exception {
        SeleniumTestBase insSeleniumTestBase = new SeleniumTestBase(browser);
        accessServiceDashboard(serviceName);
        openOperationDashboard(serviceName, operationName);
        insSeleniumTestBase.changeMTOMState(mtomState);
    }
}