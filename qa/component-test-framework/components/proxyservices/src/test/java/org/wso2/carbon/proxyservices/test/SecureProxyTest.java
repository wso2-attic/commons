/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/


package org.wso2.carbon.proxyservices.test;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.proxyservices.test.commands.InitializeProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.commands.ProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.util.ProxyReader;
import org.wso2.carbon.proxyservices.test.util.SecurityClient;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.security.mgt.test.commands.InitializeSecurityAdminCommand;
import org.wso2.carbon.security.mgt.test.commands.SecurityAdminCommand;
import org.wso2.carbon.security.ui.config.ApplySecurity;
import org.wso2.carbon.security.ui.config.SecurityAdminServiceStub;
import org.wso2.carbon.service.mgt.test.commands.InitializeServiceAdminCommand;
import org.wso2.carbon.service.mgt.test.commands.ServiceAdminCommand;
import org.wso2.carbon.service.mgt.ui.ServiceAdminStub;
import org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaDataWrapper;

import java.io.File;


public class SecureProxyTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(ProxyAdminServiceTest.class);
    String xmlPath = frameworkPath + File.separator + "components" + File.separator + "proxyservices"
                                 + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator +"SecureStockQuoteProxyTest.xml";
    

    @Override
    public void init() {

        log.info("Initializing Secure proxy class ");
        log.debug("PassThrough Test Initialised");

    }


    @Override
    public void runSuccessCase() {
        ProxyData proxyData;
        ProxyReader handler = new ProxyReader();
        log.debug("Running AddProxy SuccessCase ");
        String xmlPath = frameworkPath + File.separator + "components" + File.separator + "proxyservices"
                                         + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator +"SecureStockQuoteProxyTest.xml";
        
        try {
            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            ServiceAdminStub serviceAdminStub = new InitializeServiceAdminCommand().executeAdminStub(sessionCookie);
            SecurityAdminServiceStub securityAdminServiceStub = new InitializeSecurityAdminCommand().executeAdminStub(sessionCookie);
            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
            String[] group = {"admin"};
            String serviceName = "StockQuoteProxyTest";
            String privateKeyStore = "wso2carbon.jks";
            String[] trustedKeyStore = {"wso2carbon.jks"};
            OMElement testResult;

            ServiceGroupMetaDataWrapper serviceGroupMetaDataWrapper;
            SecurityClient securityClient = new SecurityClient();
            ApplySecurity applySecurity = new ApplySecurity();


            // Checking Security Scenario -07
//            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy","",0);
//            log.info("Checking SecureProxyTest Scenario - 07");
//            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
//                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
//                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
//                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
//                    log.info("SecurityScenarioTest deleted");
//                }
//            }

            new ServiceAdminCommand(serviceAdminStub).deleteNonAdminServiceGroupSuccessCase();

            proxyData = handler.getProxy(xmlPath);

          //  proxyData = handler.getProxy("SecureStockQuoteProxyTest.xml", null);
            proxyData.setName("SecurityScenarioTest");
            Thread.sleep(3000);
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);


            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            //    new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 7);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(7, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 7 failed.Response not matched.");

            }
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
            log.info(testResult.toString());


            // Checking Security Scenario -08

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 08");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                String currentServiceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (currentServiceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            Thread.sleep(3000);
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 8);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(8, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 8 failed.Response not matched.");
            }
            log.info(testResult.toString());

            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -09

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 09");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                String currentServiceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (currentServiceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            Thread.sleep(3000);
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 9);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(9, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 9 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -10

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 10");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 10);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(10, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 10 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -11

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 11");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 11);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(11, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 11 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -12

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 12");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 12);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(12, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 12 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -13

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 13");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 13);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(13, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 13 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -14

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 14");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 14);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(14, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 14 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");

            // Checking Security Scenario -15

            serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy", "", 0);
            log.info("Checking SecureProxyTest Scenario - 15");
            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("SecurityScenarioTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecurityScenarioTest");
                    log.info("SecurityScenarioTest deleted");
                }
            }

            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);

            applySecurity.setServiceName(serviceName);
            applySecurity.setUserGroupNames(group);
            applySecurity.setPrivateStore(privateKeyStore);
            applySecurity.setTrustedStores(trustedKeyStore);

            new SecurityAdminCommand(securityAdminServiceStub).disableSecuritySuccessCase(serviceName);
            applySecurity.setPolicyId("scenario" + 15);

            new SecurityAdminCommand(securityAdminServiceStub).applySecuritySuccessCase(applySecurity);


            Thread.sleep(2000);
            //  OMElement  testResult = securityClient.runSecurityClient(1,"StockQuoteProxyTest","urn:getQuote","<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
            testResult = securityClient.runSecurityClient(15, "SecurityScenarioTest", "urn:getQuote", "<xsd:getQuote xmlns:xsd=\"http://services.samples\"><xsd:request><xsd:symbol>IBM</xsd:symbol></xsd:request></xsd:getQuote>");

            Thread.sleep(2000);
            if (testResult.toString().contains("IBM")) {
            } else {
                Assert.fail("Security Scenario 15 failed.Response not matched.");
            }
            log.info(testResult.toString());
            new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/_system/config/PolicyFiles");


        }

        catch (Exception e) {
            log.error("Error occured while invoking registry services : " + e.toString());
            Assert.fail("Error occured while invoking registry services");
            e.printStackTrace();
        }
    }

    @Override
    public void runFailureCase() {
       /* ProxyData proxyData;
        ProxyReader handler = new ProxyReader();
        log.debug("Running Proxy FailureCase ");
        try {
            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            proxyData = handler.getProxy(xmlPath);
            proxyData.setName("SecurityScenarioTest");
            //checking add proxy method
            new ProxyAdminCommand(proxyServiceAdminStub).addProxyFailureCase(proxyData);
            //checking delete proxy method
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxyFailureCase("SecurityScenarioTest");

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("AddProxy failure case failed : " + e.getMessage());
        }*/

    }

    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();

    }
}


