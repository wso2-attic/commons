package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

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


public class RDBMSDataServiceManagementTest extends TestCase {

    public RDBMSDataServiceManagementTest(String text) {
        super(text);
    }

    Selenium browser;
    private static final String moduleVersion = "2.01";

    public void setUp() throws Exception  {
        browser = BrowserInitializer.getBrowser();
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    public void testMySQLDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
        seleniumTestBase.loginToUI(username, password);

        rdbmsDataService.setUpDataBase();
        rdbmsDataService.newDataService("mysqlds");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");
        rdbmsDataService.addNewQuery("customers-query", "MySQLDataSource", "select * from customer");
        rdbmsDataService.addNewQueryWithInput("q1", "MySQLDataSource", "select * from customer where id = ?","id","STRING","customers","customer");
        csvDataService.addTwoOperations("getAllCustomers","customers-query", "selectCustomer","q1");
        csvDataService.checkService("mysqlds");
        //rdbmsDataService.cleanDataBase();
        // seleniumTestBase.logOutUI();

    }

    public void testInvokeServiceGetAllCustomers() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "mysqlds";
        String csvExpectedResult = "Charitha";

        String result = rdbmsDataService.tryRDBMSDataService(serviceName, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
        //rdbmsDataService.cleanDataBase();

    }

    public void testInvokeServiceSelectCustomer() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "mysqlds";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "selectCustomer","urn:selectCustomer",  "http://ws.wso2.org/dataservice","id","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
        //rdbmsDataService.cleanDataBase();

    }

    public void testRDBMSDataServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("mysqlds", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("mysqlds", "NewParam1");
    }


    public void testAddRDBMSDataServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("mysqlds", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("mysqlds", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("mysqlds", "data_service");
        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "mysqlds";
        instServiceManagement.checkThrottling(serviceepr, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice", "s");


    }

    public void testCheckCachingRDBMSDataService() throws Exception {
        RDBMSDataService instRDBMSDataService=new RDBMSDataService(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("mysqlds");
        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "mysqlds";
        instRDBMSDataService.checkCachingForRDBMSService(serviceepr, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");

    }

    public void testRDBMSDataServiceModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
      //  instServiceManagement.accessServiceDashboard("mysqlds");
        instServiceManagement.engageServiceModule("mysqlds","mysqlds", "sandesha2-" + moduleVersion);
      //  instServiceManagement.accessServiceDashboard("mysqlds");
        instServiceManagement.disengageServiceModule("mysqlds", "sandesha2-" + moduleVersion);
    }

    public void testRDBMSDataServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("mysqlds", "Deactivate");
        instServiceManagement.serviceActivation("mysqlds", "Activate");
    }

    public void testTransportRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.checkTransport("mysqlds");
    }

    public void testRDBMSDataServiceParameter() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("mysqlds");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("mysqlds");
        mySeleniumTestBase.deleteParameter("testparam");
    }

//
////        public void testOperationalThrottling() throws Exception {
////
////            ServiceManagement instServiceManagement = new ServiceManagement(browser);
////            String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") + loadProperties().getProperty("context.root") + "/services/" + "mysqlds";
////            instServiceManagement.accessServiceDashboard("mysqlds");
////            instServiceManagement.openOperationDashboard("mysqlds", "getAllCustomers");
////            instServiceManagement.checkThrottling(serviceepr, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice", "s");
////
////        }
//

    public void testOperationalCachingRDBMSDataService() throws Exception {
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "mysqlds";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("mysqlds");
        instServiceManagement.openOperationDashboard("mysqlds", "getAllCustomers");
        rdbmsDataService.checkCachingForRDBMSService(serviceepr, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");


    }

    public void testOperationalModuleMgtRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("mysqlds", "getAllCustomers", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("mysqlds", "getAllCustomers", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersRDBMSDataService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String operationName = "getAllCustomers";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("mysqlds");
        instServiceManagement.openOperationDashboard("mysqlds", operationName);
        InstSeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("mysqlds");
        instServiceManagement.openOperationDashboard("mysqlds", operationName);
        InstSeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("mysqlds");
        instServiceManagement.openOperationDashboard("mysqlds", "getAllCustomers");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";

        instServiceManagement.Login();
        instServiceManagement.enableRM("mysqlds");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("mysqlds");

    }


    public void testMTOMRDBMSDataService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
        insServiceManagement.changeOperationalMTOM("mysqlds", "getAllCustomers", "True");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("mysqlds", "getAllCustomers", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("mysqlds", "getAllCustomers", "False");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("mysqlds", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("mysqlds", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("mysqlds", "False");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("mysqlds", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("mysqlds", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("mysqlds", "False");


    }


    public void testAddKeyStoreRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");

        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario4");
        OMElement result = new SecurityClient().runSecurityClient("scenario4", serviceName, nameSpace, soapAction, operation,"id");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        System.out.println(output);
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario2RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario3RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario3");
        OMElement result = new SecurityClient().runSecurityClient("scenario3", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario4RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario2");
        OMElement result = new SecurityClient().runSecurityClient("scenario2", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario5RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario13");
        OMElement result = new SecurityClient().runSecurityClient("scenario13", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario6RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario9");
        OMElement result = new SecurityClient().runSecurityClient("scenario9", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario7RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario10");
        OMElement result = new SecurityClient().runSecurityClient("scenario10", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario8RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario12");
        OMElement result = new SecurityClient().runSecurityClient("scenario12", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario9RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario11");
        OMElement result = new SecurityClient().runSecurityClient("scenario11", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario10RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario15");
        OMElement result = new SecurityClient().runSecurityClient("scenario15", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario11RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario7");
        OMElement result = new SecurityClient().runSecurityClient("scenario7", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario12RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario5");
        OMElement result = new SecurityClient().runSecurityClient("scenario5", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario13RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario8");
        OMElement result = new SecurityClient().runSecurityClient("scenario8", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario14RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario6");
        OMElement result = new SecurityClient().runSecurityClient("scenario6", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario15RDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "mysqlds";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario14");
        OMElement result = new SecurityClient().runSecurityClient("scenario14", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }


    public void testDeleteKeystoreRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("mysqlds");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveRDBMSDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("mysqlds");
        rdbmsDataService.cleanDataBase();

    }


    public void testLogoutUiRDBMSDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }
}
