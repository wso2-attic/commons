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

import java.util.Properties;
import java.io.File;

import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;
import org.apache.axiom.om.OMElement;
import junit.framework.TestCase;

import javax.xml.namespace.QName;


public class ManageEJBServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public ManageEJBServiceTest(String s){
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

    public void testUploadEJBService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "ejb-provider-adaptor.jar");
        String ServiceName = "TimeServiceRemote";



        browser.click("link=EJB Service");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("EJB Service"));

        if( browser.isTextPresent("No existing EJB Configurations")){
            browser.click("appServerConfiguration2");
            browser.select("serverType", "label=JBoss AS");
            browser.type("confirmPassword", "admin");
            browser.click("addApplicationServerButton");
            browser.waitForPageToLoad("30000");
        }
        
        browser.click("ejbStep0NextButton");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("filename", aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue( browser.isTextPresent("EJB Service"));
        assertTrue( browser.isTextPresent("Select EJB Remote/Home Interfaces"));
        browser.click("chkRemoteInterface");
        browser.click("//input[@name='chkHomeInterface' and @value='org.apache.axis2.samples.ejb.session.interfaces.TimeServiceRemoteHome']");
        browser.type("beanJNDIName", "org.apache.axis2.samples.ejb.session.interfaces.TimeServiceLocalHome");
        browser.click("//input[@value='Deploy Service']");
        browser.waitForPageToLoad("30000");
        assertTrue( browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created EJB service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

    public void testCheckThrottlingEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "TimeServiceRemote";
        instServiceManagement.checkThrottling(serviceepr, "getGreetings", "urn:getGreetings", "http://interfaces.session.ejb.samples.axis2.apache.org", "s");
    }


    public void testAddEJBServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("ejb-provider-adaptor", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("ejb-provider-adaptor", "sandesha2-" + moduleVersion);
    }


    public void testEJBServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("ejb-provider-adaptor", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("ejb-provider-adaptor", "NewParam1");
    }


    public void testCheckCachingEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "TimeServiceRemote";
        instServiceManagement.checkCaching(serviceepr, "getServerTime","urn:getServerTime", "http://interfaces.session.ejb.samples.axis2.apache.org");
    }

    public void testCheckModuleMgtEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        // instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.engageServiceModule("ejb-provider-adaptor","TimeServiceRemote", "sandesha2-" + moduleVersion);
        // instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.disengageServiceModule("TimeServiceRemote", "sandesha2-" + moduleVersion);
    }

    public void testServiceParameterEJBService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testServiceActivationEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.serviceActivation("TimeServiceRemote", "Deactivate");
        instServiceManagement.serviceActivation("TimeServiceRemote", "Activate");
    }

    public void testOperationalModuleMgtEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("TimeServiceRemote", "getGreetings", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("TimeServiceRemote", "getGreetings", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersEJBService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String operationName = "getServerTime";
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        instServiceManagement.openOperationDashboard("TimeServiceRemote", operationName);
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        instServiceManagement.openOperationDashboard("TimeServiceRemote", operationName);
        mySeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("TimeServiceRemote");
        instServiceManagement.openOperationDashboard("TimeServiceRemote", "getGreetings");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingEJBService() throws Exception {

        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();

        instServiceManagement.enableRM("TimeServiceRemote");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("TimeServiceRemote");
    }

    public void testMTOMEJBService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
//        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "True");
//        Thread.sleep(1000);
//        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "Optional");
//        Thread.sleep(1000);
//        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "False");
//        Thread.sleep(1000);
//        insServiceManagement.changeServiceGroupMTOM("ejb-provider-adaptor ", "True");
//        Thread.sleep(1000);
//        insServiceManagement.changeServiceGroupMTOM("ejb-provider-adaptor ", "Optional");
//        Thread.sleep(1000);
//        insServiceManagement.changeServiceGroupMTOM("ejb-provider-adaptor ", "False");
//        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("TimeServiceRemote", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("TimeServiceRemote","Optional" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("TimeServiceRemote", "False");


    }

    public void testAddKeyStoreEJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");
    }


    public void testCheckSecurityScenario1EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario4");
        OMElement result = new SecurityClient().runSecurityClient("scenario4", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario2EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario3EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario3");
        OMElement result = new SecurityClient().runSecurityClient("scenario3", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario4EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario2");
        OMElement result = new SecurityClient().runSecurityClient("scenario2", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario5EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario13");
        OMElement result = new SecurityClient().runSecurityClient("scenario13", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario6EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario9");
        OMElement result = new SecurityClient().runSecurityClient("scenario9", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario7EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario10");
        OMElement result = new SecurityClient().runSecurityClient("scenario10", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario8EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario12");
        OMElement result = new SecurityClient().runSecurityClient("scenario12", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario9EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario11");
        OMElement result = new SecurityClient().runSecurityClient("scenario11", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario10EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario15");
        OMElement result = new SecurityClient().runSecurityClient("scenario15", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario11EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario7");
        OMElement result = new SecurityClient().runSecurityClient("scenario7", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario12EJBService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario5");
        OMElement result = new SecurityClient().runSecurityClient("scenario5", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario13EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario8");
        OMElement result = new SecurityClient().runSecurityClient("scenario8", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario14EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario6");
        OMElement result = new SecurityClient().runSecurityClient("scenario6", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testCheckSecurityScenario15EJBService() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "TimeServiceRemote";
        String nameSpace = "http://interfaces.session.ejb.samples.axis2.apache.org";
        String soapAction = "urn:getGreetings";
        String operation = "getGreetings";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario14");
        OMElement result = new SecurityClient().runSecurityClient("scenario14", serviceName, nameSpace, soapAction, operation,"s");
        assertEquals("Hello Invoking Security Scenario!!!!", result.getFirstElement().getText());
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);

    }

    public void testDeleteKeystoreEJBService() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.disableSecurity("TimeServiceRemote");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveEJBService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instSeleniumTestBase.deleteService("ejb-provider-adaptor");

    }


    public void testlogOutEJBService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
