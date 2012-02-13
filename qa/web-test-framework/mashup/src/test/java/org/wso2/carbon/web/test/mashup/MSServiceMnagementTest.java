package org.wso2.carbon.web.test.mashup;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

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

public class MSServiceMnagementTest extends CommonSetup {

     public MSServiceMnagementTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


     public void testCheckThrottlingallCommonsService() throws Exception {
           ServiceManagement instServiceManagement = new ServiceManagement(selenium);
           instServiceManagement.openServiceDashboard("allCommons", "js_service");
           String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.port") + MSCommon.loadProperties().getProperty("context.root") + "/services/" + "allCommons";
           instServiceManagement.checkThrottling(serviceepr, "echoString", "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest", "http://services.mashup.wso2.org/allCommons", "param1");
       }

       public void testAddallCommonsServiceGroupModule() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        serviceManagement.engageServiceGroupModule("allCommons", "wso2mex-2.01");
        serviceManagement.engageServiceGroupModule("allCommons", "savan-SNAPSHOT");
        serviceManagement.engageServiceGroupModule("allCommons", "wso2xfer-2.01");
        serviceManagement.engageServiceGroupModule("allCommons", "sandesha2-2.01");
        serviceManagement.engageServiceGroupModule("allCommons", "rampart-2.01");
        serviceManagement.engageServiceGroupModule("allCommons", "rahas-2.01");

        serviceManagement.disengageServiceGroupModule("allCommons", "wso2mex-2.01");
        serviceManagement.disengageServiceGroupModule("allCommons", "savan-SNAPSHOT");
        serviceManagement.disengageServiceGroupModule("allCommons", "wso2xfer-2.01");
        serviceManagement.disengageServiceGroupModule("allCommons", "sandesha2-2.01");
        serviceManagement.disengageServiceGroupModule("allCommons", "rahas-2.01");
        serviceManagement.disengageServiceGroupModule("allCommons", "rampart-2.01");
    }

     public void testallCommonsServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("allCommons", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("allCommons", "NewParam1");
    }

     public void testCheckCachingallCommonsService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("allCommons");
        String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.port") + MSCommon.loadProperties().getProperty("context.root") + "/services/" + "allCommons";
        instServiceManagement.checkCaching(serviceepr, "getTime", "http://services.mashup.wso2.org/serviceClient/ServiceInterface/getTimeRequest","http://services.mashup.wso2.org/allCommons");
    }

      public void testCheckModuleMgtallCommonsService() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();

        serviceManagement.engageServiceModule("allCommons","allCommons", "wso2mex-2.01");
        serviceManagement.engageServiceModule("allCommons","allCommons", "savan-SNAPSHOT");
        serviceManagement.engageServiceModule("allCommons","allCommons", "wso2xfer-2.01");
        serviceManagement.engageServiceModule("allCommons","allCommons", "sandesha2-2.01");
        serviceManagement.engageServiceModule("allCommons","allCommons", "rampart-2.01");
        serviceManagement.engageServiceModule("allCommons","allCommons", "rahas-2.01");

        serviceManagement.disengageServiceModule("allCommons", "wso2mex-2.01");
        serviceManagement.disengageServiceModule("allCommons", "savan-SNAPSHOT");
        serviceManagement.disengageServiceModule("allCommons", "wso2xfer-2.01");
        serviceManagement.disengageServiceModule("allCommons", "sandesha2-2.01");
        serviceManagement.disengageServiceModule("allCommons", "rahas-2.01");
        serviceManagement.disengageServiceModule("allCommons", "rampart-2.01");

    }

    public void testServiceParameterallCommonsService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("allCommons");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("allCommons");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testServiceActivationallCommonsService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
        instServiceManagement.Login();
        instServiceManagement.serviceActivation("allCommons", "Deactivate");
        instServiceManagement.serviceActivation("allCommons", "Activate");
    }

     public void testOperationalThrottling() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
        String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.port") + MSCommon.loadProperties().getProperty("context.root") + "/services/" + "allCommons";
        instServiceManagement.accessServiceDashboard("allCommons");
        instServiceManagement.openOperationDashboard("allCommons", "echoString");
        instServiceManagement.checkThrottling(serviceepr, "echoString", "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest", "http://services.mashup.wso2.org/allCommons", "param1");
    }

    public void testOperationalCachingallCommonsService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
        String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.port") + MSCommon.loadProperties().getProperty("context.root") + "/services/" + "allCommons";
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("allCommons");
        instServiceManagement.openOperationDashboard("allCommons", "getTime");
        instServiceManagement.checkCaching(serviceepr, "getTime", "http://services.mashup.wso2.org/serviceClient/ServiceInterface/getTimeRequest", "http://services.mashup.wso2.org/allCommons");
    }

      public void testoperationalModuleMgtallCommonsService() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
        serviceManagement.engageOperationModule("allCommons", "echoString", "wso2mex-2.01");
        serviceManagement.engageOperationModule("allCommons", "echoString", "savan-SNAPSHOT");
        serviceManagement.engageOperationModule("allCommons", "echoString", "wso2xfer-2.01");
        serviceManagement.engageOperationModule("allCommons", "echoString", "sandesha2-2.01");
        serviceManagement.engageOperationModule("allCommons", "echoString", "rampart-2.01");
        serviceManagement.engageOperationModule("allCommons", "echoString", "rahas-2.01");

        serviceManagement.disengageOperationModule("allCommons", "echoString", "wso2mex-2.01");
        serviceManagement.disengageOperationModule("allCommons", "echoString", "savan-SNAPSHOT");
        serviceManagement.disengageOperationModule("allCommons", "echoString", "wso2xfer-2.01");
        serviceManagement.disengageOperationModule("allCommons", "echoString", "sandesha2-2.01");
        serviceManagement.disengageOperationModule("allCommons", "echoString", "rahas-2.01");
        serviceManagement.disengageOperationModule("allCommons", "echoString", "rampart-2.01");
    }

    public void testOperationalAddParametersallCommonsService() throws Exception {
            SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
            ServiceManagement instServiceManagement = new ServiceManagement(selenium);
            String operationName = "echoString";
            instServiceManagement.Login();
            instServiceManagement.accessServiceDashboard("allCommons");
            instServiceManagement.openOperationDashboard("allCommons", operationName);
            mySeleniumTestBase.addNewParameter("testparam");
            instServiceManagement.accessServiceDashboard("allCommons");
            instServiceManagement.openOperationDashboard("allCommons", operationName);
            mySeleniumTestBase.deleteOperationalParameter("testparam");
        }

        public void testOperationalFlowsallCommonsService() throws Exception {
            ServiceManagement instServiceManagement = new ServiceManagement(selenium);
            instServiceManagement.Login();
            instServiceManagement.accessServiceDashboard("allCommons");
            instServiceManagement.openOperationDashboard("allCommons", "echoString");
            instServiceManagement.checkParamFlows();
        }

        public void testReliableMessagingallCommonsService() throws Exception {

            String serviceName = "allCommons";
            String nameSpace = "http://services.mashup.wso2.org/allCommons";
            String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
            String operation = "echoString";

            ServiceManagement instServiceManagement = new ServiceManagement(selenium);
            instServiceManagement.Login();

            instServiceManagement.enableRM("allCommons");
         //   instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "param1");
            instServiceManagement.disableRM("allCommons");
        }


 //    public void testServiceManagement() throws Exception {

//         MSCommon.testSpecificConfigurationLink("exchangeRate");
//         MSCommon.testAPIDocumentation("exchangeRate",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("http.port"),MSCommon.loadProperties().getProperty("https.port"));
//         MSCommon.testConfiguration("exchangeRate","Source Code",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?source");
//         MSCommon.testConfiguration("exchangeRate","Source code template [HTML interface]",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?template&flavor=html");
//         MSCommon.testConfiguration("exchangeRate","Source code template [Gadget interface]",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?template&flavor=googlegadget");
//         MSCommon.testConfiguration("exchangeRate","XML Schema",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?xsd");
//         MSCommon.testConfiguration("exchangeRate","Javascript (DOM) stub",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?stub");
//         MSCommon.testConfiguration("exchangeRate","Javascript (E4X) stub",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?stub&lang=e4x");
//         MSCommon.testConfiguration("exchangeRate","Javascript (E4X) stub [localhost endpoints]",MSCommon.loadProperties().getProperty("host.name"),MSCommon.loadProperties().getProperty("https.port"),"?stub&lang=e4x&localhost=true");

//        ServiceManagement serviceManagement = new ServiceManagement(selenium);
 //    }

    public void testMTOMallCommonsService() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();

        serviceManagement.changeOperationalMTOM("allCommons", "echoString", "True");
        serviceManagement.changeOperationalMTOM("allCommons", "echoString", "False");
        serviceManagement.changeOperationalMTOM("allCommons", "echoString", "Optional");

        serviceManagement.changeServiceGroupMTOM("allCommons","True");
        serviceManagement.changeServiceGroupMTOM("allCommons","False");
        serviceManagement.changeServiceGroupMTOM("allCommons","Optional");

        serviceManagement.changeServiceMTOM("allCommons", "True");
        serviceManagement.changeServiceMTOM("allCommons", "False");
        serviceManagement.changeServiceMTOM("allCommons", "Optional");
    }

//    public void testAddKeyStoreAxis1Service() throws Exception {
//        ServiceManagement instServiceManagement = new ServiceManagement(browser);
//        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
//        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
//        instServiceManagement.Login();
//        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");
//    }


//    public void testCheckSecurityScenario1allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignConfidentialitySecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario2allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignUTSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario3allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignIntegritySecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario4allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario5allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario6allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario7allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario8allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario9allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario10allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario11allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario12allCommonsService() throws Exception {
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSigEncrSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario13allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario14allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }
//
//    public void testCheckSecurityScenario15allCommonsService() throws Exception {
//
//        ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//        String serviceName = "allCommons";
//        String nameSpace = "http://services.mashup.wso2.org/allCommons";
//        String soapAction = "http://services.mashup.wso2.org/serviceClient/ServiceInterface/echoStringRequest";
//        String operation = "echoString";
//
//        instServiceManagement.Login();
//        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName, nameSpace, soapAction, operation, "param1");
//    }

//    public void testDeleteKeystoreAxis1Service() throws Exception {
//        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
//        ServiceManagement instServiceManagement = new ServiceManagement(browser);
//        instServiceManagement.Login();
//         instServiceManagement.disableSecurity("Axis1Service");
//        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
//    }


       public void testlogOutallCommonsService() throws Exception {
           SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
           instSeleniumTestBase.logOutUI();
       }


}
