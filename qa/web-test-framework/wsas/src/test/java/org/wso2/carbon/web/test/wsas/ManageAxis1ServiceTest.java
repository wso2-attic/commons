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
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ManageAxis1ServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManageAxis1ServiceTest(String s){
        super(s);
    }


    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        property = BrowserInitializer.getProperties();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");
    }



    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);
    }

    public void testUploadservice() throws Exception {
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

    public void testCheckThrottlingAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("Axis1Service", "axis1");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis1Service";
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://ws.apache.org/axis", "s");
    }


    public void testAddAxis1ServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("Axis1Service.wsdd", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("Axis1Service.wsdd", "sandesha2-" + moduleVersion);
    }


    public void testAxis1ServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("Axis1Service.wsdd", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("Axis1Service.wsdd", "NewParam1");
    }

   
    public void testCheckCachingAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis1Service");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis1Service";
        instServiceManagement.checkCaching(serviceepr, "getTime", "http://ws.apache.org/axis/Axis1Service/getTimeRequest", "http://ws.apache.org/axis");
    }

    public void testCheckModuleMgtAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        // instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.engageServiceModule("Axis1Service.wsdd","Axis1Service", "sandesha2-" + moduleVersion);
        // instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.disengageServiceModule("Axis1Service", "sandesha2-" + moduleVersion);
    }

    public void testServiceParameterAxis1Service() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis1Service");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("Axis1Service");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testServiceActivationAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.serviceActivation("Axis1Service", "Deactivate");
        instServiceManagement.serviceActivation("Axis1Service", "Activate");
    }

    public void testoperationalModuleMgtAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("Axis1Service", "echoString", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("Axis1Service", "echoString", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersAxis1Service() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String operationName = "getTime";
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.openOperationDashboard("Axis1Service", operationName);
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.openOperationDashboard("Axis1Service", operationName);
        mySeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.openOperationDashboard("Axis1Service", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingAxis1Service() throws Exception {

        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();

        instServiceManagement.enableRM("Axis1Service");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("Axis1Service");
    }

    public void testMTOMAxis1Service() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
//        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "True");
//        Thread.sleep(1000);
//        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "Optional");
//        Thread.sleep(1000);
//        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "False");
//        Thread.sleep(1000);
//        insServiceManagement.changeServiceGroupMTOM("Axis1Service.wsdd", "True");
//        Thread.sleep(1000);
//        insServiceManagement.changeServiceGroupMTOM("Axis1Service.wsdd", "Optional");
//        Thread.sleep(1000);
//        insServiceManagement.changeServiceGroupMTOM("Axis1Service.wsdd", "False");
//        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis1Service", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis1Service","Optional" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis1Service", "False");


    }

    public void testAddKeyStoreAxis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");
    }


    public void testCheckSecurityScenario1Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario2Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario3Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario4Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario5Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario6Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario7Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario8Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario9Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario10Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario11Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario12Axis1Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario13Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario14Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario15Axis1Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis1Service";
        String nameSpace = "http://ws.apache.org/axis";
        String soapAction = "http://ws.apache.org/axis/Axis1Service/echoStringRequest";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testDeleteKeystoreAxis1Service() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.disableSecurity("Axis1Service");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveAxis1Service() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instSeleniumTestBase.deleteService("Axis1Service.wsdd");

    }


    public void testlogOutAxis1Service() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }


}

