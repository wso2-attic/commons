package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement; /*
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

/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: May 6, 2009
 * Time: 11:07:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManageSpringServiceTest extends TestCase {
    Selenium browser;

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public void testUploadservice() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI("admin", "admin");

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String resourcefile = instReadXMLFileTest.ReadConfig("Axis1ResourcePath", "Axis1Service");
        String wsdlfile = instReadXMLFileTest.ReadConfig("Axis1WsddPath", "Axis1Service");
        String ServiceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");

        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=Axis1 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("wsddFilename", wsdlfile);
        InstSeleniumTestBase.SetFileBrowse("jarResource", resourcefile);
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

    public void testAddServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageServiceGroupModule("Axis1Service.wsdd", "savan-SNAPSHOT");
        instServiceManagement.disengageServiceGroupModule("Axis1Service.wsdd", "savan-SNAPSHOT");
    }

    public void testServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.addServiceGroupParameter("Axis1Service.wsdd", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("Axis1Service.wsdd", "NewParam1");
    }

    public void testCheckThrottling() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");

        //------ Throttle Test XML readings -------
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "Axis1ThrottleTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1ThrottleTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "Axis1ThrottleTest");
        String NameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1ThrottleTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");
        instServiceManagement.openServiceDashboard("Axis1Service", "axis1");
        instServiceManagement.checkThrottling("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR, NameSpace, "Text");
    }

    public void testCheckCaching() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        instServiceManagement.accessServiceDashboard("Axis1Service");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "Axis1CachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1CachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "Axis1CachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace", "Axis1CachingTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");

        instServiceManagement.checkCaching("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR, NameSpaceTR);
    }

    public void testCheckModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.engageServiceModule("Axis1Service", "sandesha2-SNAPSHOT");
        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.disengageServiceModule("Axis1Service", "sandesha2-SNAPSHOT");
    }

    public void testServiceParameter() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis1Service");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("Axis1Service");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.serviceActivation("Axis1Service", "Deactivate");
        instServiceManagement.serviceActivation("Axis1Service", "Activate");
    }

    public void testTransport() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.checkTransport("Axis1Service");
    }

    public void testAddKeyStore() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String keystorePath = instReadXMLFileTest.ReadConfig("SecurityKeyStore", "KeystoreMGT");
        String keystorePassword = instReadXMLFileTest.ReadConfig("SecurityKeyStorePwd", "KeystoreMGT");
        instKeyStoreManagement.AddKeystore(keystorePath, keystorePassword);
    }

    public void testCheckSecurityScenario1() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario2() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario3() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario4() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    /* public void testCheckSecurityScenario5() throws Exception {

            ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
            String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
            String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
            String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
            String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

            ServiceManagement instServiceManagement = new ServiceManagement(browser);
            instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
        }

        public void testCheckSecurityScenario6() throws Exception {

            ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
            String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
            String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
            String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
            String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

            ServiceManagement instServiceManagement = new ServiceManagement(browser);
            instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
        }

        public void testCheckSecurityScenario7() throws Exception {

            ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
            String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
            String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
            String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
            String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

            ServiceManagement instServiceManagement = new ServiceManagement(browser);
            instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
        }

        public void testCheckSecurityScenario8() throws Exception {

            ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
            String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
            String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
            String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
            String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

            ServiceManagement instServiceManagement = new ServiceManagement(browser);
            instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
        }

        public void testCheckSecurityScenario9() throws Exception {

            ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
            String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
            String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
            String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
            String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

            ServiceManagement instServiceManagement = new ServiceManagement(browser);
            instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
        }

        public void testCheckSecurityScenario10() throws Exception {

            ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
            String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
            String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
            String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
            String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

            ServiceManagement instServiceManagement = new ServiceManagement(browser);
            instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
        }
    */
    public void testCheckSecurityScenario11() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario12() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario13() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    public void testCheckSecurityScenario14() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "s");
    }

    /*  public void testCheckSecurityScenario15() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }*/
    public void testDeleteKeystore() throws Exception {
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testOperationalThrottling() throws Exception {
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "Axis1ThrottleTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1ThrottleTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "Axis1ThrottleTest");
        String NameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1ThrottleTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");

        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.openOperationDashboard("Axis1Service", operationNameTR);
        instServiceManagement.checkThrottling("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR, NameSpace, "Text");
    }

    public void testOperationalCaching() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "Axis1CachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1CachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "Axis1CachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace", "Axis1CachingTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");

        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.openOperationDashboard("Axis1Service", operationNameTR);
        instServiceManagement.checkCaching("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR, NameSpaceTR);
    }

    public void testoperationalModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageOperationModule("Axis1Service", "echoString", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("Axis1Service", "echoString", "savan-SNAPSHOT");
    }

    public void testOperationalAddParameters() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String operationName = instReadXMLFileTest.ReadConfig("OperationName", "Axis1CachingTest");
        instServiceManagement.openOperationDashboard("Axis1Service", operationName);
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.openOperationDashboard("Axis1Service", operationName);
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testOperationalFlows() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis1Service");
        instServiceManagement.openOperationDashboard("Axis1Service", "echoString");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessaging() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName", "Axis1Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace", "Axis1Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction", "Axis1Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName", "Axis1Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.enableRM("Axis1Service");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("Axis1Service");
    }

    public void testMTOM() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);


        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "True");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("Axis1Service", "echoString", "False");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("Axis1Service.wsdd", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("Axis1Service.wsdd", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("Axis1Service.wsdd", "False");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis1Service", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis1Service", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("Axis1Service", "False");


    }

    public void testRemoveServices() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis1Service.wsdd");

    }

    public void logOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
