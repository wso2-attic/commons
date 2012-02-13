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
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;


public class ManageJarServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManageJarServiceTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");
    }


    public void testLoginJarService()throws Exception
    {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);

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


    public void testJarServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("JarService", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("JarService", "NewParam1");
    }


    public void testAddJarServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("JarService", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("JarService", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("JarService1", "jarservice");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "JarService1";
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s");


    }

    public void testCheckCachingJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JarService1");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "JarService1";
        instServiceManagement.checkCaching(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

    }

    public void testJarServiceModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceModule("JarService","JarService1", "sandesha2-" + moduleVersion);
        instServiceManagement.accessServiceDashboard("JarService1");
        instServiceManagement.disengageServiceModule("JarService1", "sandesha2-" + moduleVersion);
    }

    public void testJarServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("JarService1", "Deactivate");
        instServiceManagement.serviceActivation("JarService1", "Activate");
    }

    public void testTransportJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.checkTransport("JarService1");
    }

    public void testServiceParameterJarService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JarService1");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("JarService1");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testOperationalModuleMgtJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("JarService1", "echoString", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("JarService1", "echoString", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersJarSrevice() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String operationName = "getTime";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JarService1");
        instServiceManagement.openOperationDashboard("JarService1", operationName);
        InstSeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("JarService1");
        instServiceManagement.openOperationDashboard("JarService1", operationName);
        InstSeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JarService1");
        instServiceManagement.openOperationDashboard("JarService1", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingJarSrevice() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";


        instServiceManagement.Login();
        instServiceManagement.enableRM("JarService1");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("JarService1");

    }

    public void testMTOMJarService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
       
        insServiceManagement.changeServiceMTOM("JarService1", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("JarService1", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("JarService1", "False");


    }


    public void testAddKeyStoreJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");

        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");

    }

    public void testCheckSecurityScenario2JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario3JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario4JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario5JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario6JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario7JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario8JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario9JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario10JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario11JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario12JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario13JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario14JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario15JarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JarService1";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }


    public void testDeleteKeystoreJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("JarService1");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServicesJarService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instServiceManagement.Login();
        instSeleniumTestBase.deleteService("JarService");

    }

    public void testlogOutJarService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.logOutUI();
    }
}
