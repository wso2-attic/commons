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
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;


public class ManageAxis2ServicesTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManageAxis2ServicesTest(String s){
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
        contextroot = property.getProperty("context.root");
    }



    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);

    }

    public void testUploadAxis2Service() throws Exception {
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

    public void testAddAxis2ServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("Axis2Service", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("Axis2Service", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingAxis2Service() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("Axis2Service", "axis2");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s");


    }

    public void testCheckCachingAxis2Service() throws Exception {


        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instServiceManagement.checkCaching(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

    }

    public void testServiceModuleMgtAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        // instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.engageServiceModule("Axis2Service","Axis2Service", "sandesha2-" + moduleVersion);
        // instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.disengageServiceModule("Axis2Service", "sandesha2-" + moduleVersion);
    }

    public void testServiceActivationAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.serviceActivation("Axis2Service", "Deactivate");
        instServiceManagement.serviceActivation("Axis2Service", "Activate");
    }

    public void testTransportAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.checkTransport("Axis2Service");
    }

    public void testServiceParameterAxis2Service() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("Axis2Service");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testServiceGroupParameterAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("Axis2Service", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("Axis2Service", "NewParam1");
    }


    public void testOperationalModuleMgtAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("Axis2Service", "echoString", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("Axis2Service", "echoString", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersAxis2Service() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String operationName = "getTime";
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service", operationName);
        InstSeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service", operationName);
        InstSeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingAxis2Service() throws Exception {

        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.enableRM("Axis2Service");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("Axis2Service");

    }

    public void testMTOMAxis2Service() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");

        insServiceManagement.changeServiceMTOM("Axis2Service", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis2Service", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis2Service", "False");


    }


    public void testAddKeyStoreAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1Axis2Service() throws Exception {


        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");

    }

    public void testCheckSecurityScenario2Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario3Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario4Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario5Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario6Axis2Service() throws Exception

    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario7Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario8Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario9Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario10Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario11Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario12Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario13Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";


        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario14Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario15Axis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testDeleteKeystoreAxis2Service() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.disableSecurity("Axis2Service");
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServices() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }


    public void testlogOutAxis2Service() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
