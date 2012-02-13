package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
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


public class ManagePojoServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManagePojoServiceTest(String s){
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


    public void testLogin() throws Exception
    {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);
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

    public void testAddPojoServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("PojoService", "sandesha2-"+moduleVersion);
        instServiceManagement.disengageServiceGroupModule("PojoService", "sandesha2-"+moduleVersion);
    }

    public void testServiceGroupParameterPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("PojoService", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("PojoService", "NewParam1");
    }


    public void testCheckThrottlingPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("PojoService", "axis2");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "PojoService";
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://ws.apache.org/axis2", "s");

    }

    public void testCheckCachingPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("PojoService");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "PojoService";
        instServiceManagement.checkCaching(serviceepr, "getTime", "urn:getTime", "http://ws.apache.org/axis2");

    }

    public void testCheckModuleMgtPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        // instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.engageServiceModule("PojoService","PojoService", "sandesha2-"+moduleVersion);
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.disengageServiceModule("PojoService", "sandesha2-"+moduleVersion);
    }

    public void testServiceParameterPojoService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("PojoService");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("PojoService");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testServiceActivationPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("PojoService", "Deactivate");
        instServiceManagement.serviceActivation("PojoService", "Activate");
    }

    public void testTransportPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.checkTransport("PojoService");

    }


    public void testoperationalModuleMgtPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("PojoService", "echoString", "sandesha2-"+moduleVersion);
        instServiceManagement.disengageOperationModule("PojoService", "echoString", "sandesha2-"+moduleVersion);
    }

    public void testOperationalAddParametersPojoService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String operationName = "getTime";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.openOperationDashboard("PojoService", operationName);
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.openOperationDashboard("PojoService", operationName);
        mySeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.openOperationDashboard("PojoService", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.enableRM("PojoService");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("PojoService");
    }


    public void testMTOMPojoService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();

        insServiceManagement.changeOperationalMTOM("PojoService", "echoString", "True");
       
        insServiceManagement.changeServiceMTOM("PojoService","True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("PojoService", "Optional" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("PojoService", "False");

    }

    public void testAddKeyStorePojoService() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");
    }

    public void testCheckSecurityScenario1PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario2PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario3PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario4PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario5PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario6PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario7PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario8PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario9PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario10PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario11PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario12PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario13PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario14PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario15PojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "PojoService";
        String nameSpace = "http://ws.apache.org/axis2";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testDeleteKeystorePojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("PojoService");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServicesPojoService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instServiceManagement.Login();
        instSeleniumTestBase.deleteService("PojoService");

    }

    public void testlogOutPojoService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

}
