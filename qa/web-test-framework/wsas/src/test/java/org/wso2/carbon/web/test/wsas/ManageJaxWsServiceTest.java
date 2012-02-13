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


public class ManageJaxWsServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManageJaxWsServiceTest(String s){
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


    public void testLogin()throws Exception
    {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);
    }

    public void testUploadJaxWsService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File jaxwsPath = new File("." + File.separator + "lib" + File.separator + "JaxWSService.jar");
        String ServiceName = "JaxWSService.jar";

        //browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
        browser.click("link=JAX-WS Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("filename", jaxwsPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created JAXWS service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

    public void testJaxWsServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("JaxWSService.jar", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("JaxWSService.jar", "NewParam1");
    }

    public void testAddJaxWsServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("JaxWSService.jar", "savan-1.00");
        instServiceManagement.disengageServiceGroupModule("JaxWSService.jar", "savan-1.00");
    }

    public void testCheckThrottlingJaxWsService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("JaxWSTestService", "axis2");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "JaxWSTestService";
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://jaxws.carbon.wso2.org", "arg0");


    }


    public void testCheckCachingJaxWsService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("JaxWSTestService", "axis2");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "JaxWSTestService";
        instServiceManagement.checkCaching(serviceepr, "getTime", "urn:getTime", "http://jaxws.carbon.wso2.org");
    }

    public void testJaxWsServiceModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JaxWSTestService");
        instServiceManagement.engageServiceModule("JaxWSService.jar","JaxWSTestService", "sandesha2-"+moduleVersion);
        instServiceManagement.accessServiceDashboard("JaxWSTestService");
        instServiceManagement.disengageServiceModule("JaxWSTestService", "sandesha2-"+moduleVersion);
    }

    public void testJaxWsServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("JaxWSTestService", "Deactivate");
        instServiceManagement.serviceActivation("JaxWSTestService", "Activate");
    }

    public void testTransportJaxWsService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.checkTransport("JaxWSTestService");
    }

    public void testJaxWsServiceParameter() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JaxWSTestService");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("JaxWSTestService");
        mySeleniumTestBase.deleteParameter("testparam");
    }



    public void testParamModuleMgtJaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("JaxWSTestService", "echoString", "sandesha2-"+moduleVersion);
        instServiceManagement.disengageOperationModule("JaxWSTestService", "echoString", "sandesha2-"+moduleVersion);
    }

    public void testOperationalAddParametersJaxWs() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openOperationDashboard("JaxWSTestService", "echoString");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.openOperationDashboard("JaxWSTestService", "echoString");
        mySeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testParamFlowsJaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("JaxWSTestService");
        instServiceManagement.openOperationDashboard("JaxWSTestService", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingJaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.enableRM("JaxWSTestService");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "arg0");
        instServiceManagement.disableRM("JaxWSTestService");
    }

    public void testMTOMJaxWs() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("JaxWSTestService");

        insServiceManagement.changeOperationalMTOM("JaxWSTestService", "echoString", "True");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("JaxWSTestService", "echoString","Optional" );
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("JaxWSTestService", "echoString", "False");
    }

    public void testAddKeyStoreJaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");
    }

    public void testCheckSecurityScenario1JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario2JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");

    }

    public void testCheckSecurityScenario3JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario4JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario5JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario6JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario7JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario8JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario9JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario10JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");

    }

    public void testCheckSecurityScenario11JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario12JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario13JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario14JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testCheckSecurityScenario15JaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "JaxWSTestService";
        String nameSpace = "http://jaxws.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "arg0");
    }

    public void testDeleteKeystoreJaxWs() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("JaxWSTestService");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServicesJaxWs() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
        instSeleniumTestBase.deleteService("JaxWSService.jar");
    }

    public void logOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
