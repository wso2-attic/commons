package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
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

public class ManageSpringServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManageSpringServiceTest(String s){
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

    public void testUploadSpringService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File springContextPath = new File("." + File.separator + "lib" + File.separator + "context.xml");
        File springBeansPath = new File("." + File.separator + "lib" + File.separator + "SpringService.jar");
        String ServiceName = "SpringBean";


        browser.click("link=Spring Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add Spring Service"));
        assertEquals("Upload a New Spring Service (.xml and .jar)", browser.getText("//div[@id='workArea']/form/table/thead/tr/th"));
        InstSeleniumTestBase.SetFileBrowse("springContext", springContextPath.getCanonicalPath());
        InstSeleniumTestBase.SetFileBrowse("springBeans", springBeansPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Select Beans to Deploy"));
        assertEquals("Select Spring Beans to be exposed as Web services", browser.getText("//div[@id='workArea']/form/table/thead/tr/th"));
        assertTrue(browser.isElementPresent("//input[@value=' Select All ']"));
        assertTrue(browser.isElementPresent("//input[@value='Select None']"));
        browser.click("chkBeans");
        browser.click("//div[@id='workArea']/form/table/tbody/tr[3]/td/div/input[1]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page after a while to see the created service"));
        browser.click("//button[@type='button']");

        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }


    public void testAddSpringServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("SpringService", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("SpringService", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("SpringBean", "axis2");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "SpringBean";
        instServiceManagement.checkThrottling(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "args0");


    }

    public void testCheckCachingSpringservice() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("SpringBean");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "SpringBean";
        instServiceManagement.checkCaching(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

    }

    public void testServiceModuleMgtSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        // instServiceManagement.accessServiceDashboard("SpringBean");
        instServiceManagement.engageServiceModule("SpringService","SpringBean", "sandesha2-" + moduleVersion);
        // instServiceManagement.accessServiceDashboard("SpringBean");
        instServiceManagement.disengageServiceModule("SpringBean", "sandesha2-" + moduleVersion);
    }

    public void testSpringServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("SpringBean", "Deactivate");
        instServiceManagement.serviceActivation("SpringBean", "Activate");
    }

    public void testTransportSpringsrevice() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.checkTransport("SpringBean");
    }

    public void testSpringServiceParameter() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("SpringBean");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("SpringBean");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testSpringServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("SpringService", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("SpringService", "NewParam1");
    }


    public void testOperationalModuleMgtSpringservice() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("SpringBean", "echoString", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("SpringBean", "echoString", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersSpringService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String operationName = "getTime";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("SpringBean");
        instServiceManagement.openOperationDashboard("SpringBean", operationName);
        InstSeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("SpringBean");
        instServiceManagement.openOperationDashboard("SpringBean", operationName);
        InstSeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("SpringBean");
        instServiceManagement.openOperationDashboard("SpringBean", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.enableRM("SpringBean");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "args0");
        instServiceManagement.disableRM("SpringBean");

    }

    public void testMTOMSpringService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("SpringBean");

        insServiceManagement.changeServiceMTOM("SpringBean","True" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("SpringBean","Optional" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("SpringBean", "False");


    }


    public void testAddKeyStoreSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");

        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");

    }

    public void testCheckSecurityScenario2SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario3SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario4SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario5SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario6SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario7SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario8SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario9SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario10SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario11SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario12SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario13SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario14SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.Login();
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testCheckSecurityScenario15SpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "SpringBean";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoString";
        String operation = "echoString";

        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "args0");
    }

    public void testDeleteKeystoreSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("SpringBean");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServicesSpringService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instServiceManagement.Login();
        instSeleniumTestBase.deleteService("SpringService");
    }



    public void testlogOutSpringService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

}
