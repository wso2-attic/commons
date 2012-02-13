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
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo;
import org.wso2.carbon.proxyservices.test.commands.InitializeProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.commands.ProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.util.ProxyReader;
import org.wso2.carbon.proxyservices.test.util.SecurityClient;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.service.mgt.test.commands.InitializeServiceAdminCommand;
import org.wso2.carbon.service.mgt.test.commands.ServiceAdminCommand;
import org.wso2.carbon.service.mgt.ui.ServiceAdminStub;
import org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaDataWrapper;

import javax.activation.DataHandler;
import java.io.File;
import java.net.URL;

public class SecurityScenarioTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(ProxyAdminServiceTest.class);

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
        try {
            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            ServiceAdminStub serviceAdminStub = new InitializeServiceAdminCommand().executeAdminStub(sessionCookie);
            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
            String serviceName;
            createResources(resourceAdminServiceStub);
            for (int scenario = 2; scenario <= 5; scenario++) {
                log.info("Checking security scenario : " + scenario);
                /*  ServiceGroupMetaDataWrapper serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy","",0);
                for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                    serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                    if (serviceName.equalsIgnoreCase("SecureProxy")) {
                        new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("SecureProxy");
                        log.info("SecureProxy deleted");
                    }
                }*/
                new ServiceAdminCommand(serviceAdminStub).deleteNonAdminServiceGroupSuccessCase();
                proxyData = handler.getProxy("secureproxy.xml", null);
                proxyData.setEnableSecurity(true);
                ProxyServicePolicyInfo proxyServicePolicyInfo = new ProxyServicePolicyInfo();
                proxyServicePolicyInfo.setKey("conf:/PolicyFiles/policy" + scenario + ".xml");
                proxyData.setPolicies(new ProxyServicePolicyInfo[]{proxyServicePolicyInfo});
                Thread.sleep(2000);
                new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);


                SecurityClient securityClient = new SecurityClient();
                Thread.sleep(2000);
                OMElement testResult = securityClient.runSecurityClient(scenario, "SecureProxy", "urn:getQuote", "<ser:getQuote xmlns:ser=\"http://services.samples\"><ser:request><xsd:symbol xmlns:xsd=\"http://services.samples/xsd\">IBM</xsd:symbol></ser:request></ser:getQuote>");
                Thread.sleep(2000);
                if (testResult.toString().contains("IBM")) {
                }
                else {
                    Assert.fail("Security Scenario 2 failed.Response not matched.");
                }
                log.info(testResult.toString());
            }
        }
        catch (Exception e) {
            log.error("Unknown exception in Security Scenario test class : " + e.toString());
            e.printStackTrace();
            Assert.fail("Unknown exception in Security Scenario test class");
        }
    }

    private void createResources(ResourceAdminServiceStub resourceAdminServiceStub) throws Exception {
        new ResourceAdminCommand(resourceAdminServiceStub).deleteCollectionSuccessCase("/_system/config/PolicyFiles");
        new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/_system/config/", "PolicyFiles", "", "contain test policy files");

        /*File filePath = new File("./");
        String relativePath = filePath.getCanonicalPath();
        File findFile = new File(relativePath + File.separator + "config" + File.separator + "framework.properties");
        if (!findFile.isFile()) {
            filePath = new File("./../");
            relativePath = filePath.getCanonicalPath();
        }*/
        String securityPolicy = frameworkPath + File.separator + "proxyservices" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "ServerSecurityFiles";

        // Adding policy xml      (Thread sleep added until sql Exception fixed)
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy2.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy2.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy3.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy3.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy4.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy4.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy5.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy5.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy6.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy6.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy9.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy9.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy10.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy10.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy11.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy11.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy12.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy12.xml")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/PolicyFiles/policy13.xml", "application/xml", "policy files", new DataHandler(new URL("file:///" + securityPolicy + File.separator + "policy13.xml")), null);
        Thread.sleep(1000);
    }

    @Override
    public void runFailureCase() {
//        ProxyData proxyData;
//        ProxyReader handler = new ProxyReader();
//        log.debug("Running Proxy FailureCase ");
//        try {
//            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
//            proxyData = handler.getProxy("StockQuoteProxyTest.xml", null);
//
//            //checking add proxy method
//            new ProxyAdminCommand(proxyServiceAdminStub).addProxyFailureCase(proxyData);
//            //checking delete proxy method
//            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxyFailureCase("StockQuoteProxyTest");
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.error("AddProxy failure case failed : " + e.getMessage());
//        }

    }

    @Override
    public void cleanup() {
        loadDefaultConfig();
    }
}