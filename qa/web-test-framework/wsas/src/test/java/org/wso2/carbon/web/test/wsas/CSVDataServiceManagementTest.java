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
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;
import org.apache.axiom.om.OMElement;
import junit.framework.TestCase;

import javax.xml.namespace.QName;
import java.io.*;
import java.util.Properties;

public class CSVDataServiceManagementTest extends TestCase {
    Selenium browser;
    private static final String moduleVersion = "2.01";
    static FileInputStream freader;

    public CSVDataServiceManagementTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
           browser = BrowserInitializer.getBrowser();
    }


    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }



    public void testCreatCSVDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);

        seleniumTestBase.loginToUI("admin", "admin");

        csvDataService.newDataService("CSVDataService", "Sample data service");
        csvDataService.newCSVDataSource("CSVDataSource", "DataServiceCSVResource.csv", "id,name,address", "1,2,3", 2);
        csvDataService.addCSVQuery("q1", "CSVDataSource", "customers", "customer");
        csvDataService.addOutputMapping("element", "id", "1");
        csvDataService.addOutputMapping("element", "name", "2");
        csvDataService.addOutputMapping("attribute", "address","3" );
        csvDataService.gotoMainConfiguration();
        csvDataService.addOperation("getAllCustomer", "q1");
        csvDataService.checkService("CSVDataService");

    }

    public void testInvokeServiceCSVDataService() throws Exception {
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName = "CSVDataService";
        String csvExpectedResult = "Boston";

        String result = csvDataService.tryCSVDataService(serviceName, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);

    }

    public void testCopyFromRepositorytoLib() throws Exception{
        String carbonHome= loadProperties().getProperty("carbon.home");
        String src=carbonHome+"/repository/dataservices/CSVDataService.dbs";

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream("." + File.separator + "lib" + File.separator + "CSVDataService.dbs");

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

    }

    public void testServiceGroupParameterCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("CSVDataService", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("CSVDataService", "NewParam1");
    }


    public void testAddServiceGroupModuleCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("CSVDataService", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("CSVDataService", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("CSVDataService", "data_service");
        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
        instServiceManagement.checkThrottling(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice", "s");


    }

//    public void testCheckCachingCSVDataService() throws Exception {   //caching need not to be checked in CSV
//
//
//        ServiceManagement instServiceManagement = new ServiceManagement(browser);
//        instServiceManagement.Login();
//        instServiceManagement.accessServiceDashboard("CSVDataService");
//        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
//        instServiceManagement.checkCaching(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice");
//
//    }

    public void testServiceModuleMgtCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.engageServiceModule("CSVDataService","CSVDataService", "sandesha2-" + moduleVersion);
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.disengageServiceModule("CSVDataService", "sandesha2-" + moduleVersion);
    }

    public void testServiceActivationCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("CSVDataService", "Deactivate");
        instServiceManagement.serviceActivation("CSVDataService", "Activate");
    }

    public void testTransportCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.checkTransport("CSVDataService");
    }

    public void testServiceParameterCSVDataService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("CSVDataService");
        mySeleniumTestBase.deleteParameter("testparam");
    }


//        public void testOperationalThrottlingCSVDataService() throws Exception {
//
//            ServiceManagement instServiceManagement = new ServiceManagement(browser);
//            String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
//            instServiceManagement.accessServiceDashboard("CSVDataService");
//            instServiceManagement.openOperationDashboard("CSVDataService", "getAllCustomer");
//            instServiceManagement.checkThrottling(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice", "s");
//
//        }


//    public void testOperationalCachingCSVDataService() throws Exception {    //caching need not be checked in CSV
//
//
//        ServiceManagement instServiceManagement = new ServiceManagement(browser);
//        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
//        instServiceManagement.Login();
//        instServiceManagement.accessServiceDashboard("CSVDataService");
//        instServiceManagement.openOperationDashboard("CSVDataService", "getAllCustomer");
//        instServiceManagement.checkCaching(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice");
//
//
//    }

    public void testOperationalModuleMgtCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("CSVDataService", "getAllCustomer", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("CSVDataService", "getAllCustomer", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersCSVDataService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String operationName = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.openOperationDashboard("CSVDataService", operationName);
        InstSeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.openOperationDashboard("CSVDataService", operationName);
        InstSeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.openOperationDashboard("CSVDataService", "getAllCustomer");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableRM("CSVDataService");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("CSVDataService");

    }


    public void testMTOMCSVDataServiceCSVDataService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);

        insServiceManagement.Login();
        insServiceManagement.changeOperationalMTOM("CSVDataService", "getAllCustomer", "True");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("CSVDataService", "getAllCustomer", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeOperationalMTOM("CSVDataService", "getAllCustomer", "False");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("CSVDataService", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("CSVDataService", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceGroupMTOM("CSVDataService", "False");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("CSVDataService", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("CSVDataService", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("CSVDataService", "False");


    }


    public void testAddKeyStoreCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");

        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario4");
        OMElement result = new SecurityClient().runSecurityClient("scenario4", serviceName, nameSpace, soapAction, operation,"id");
        System.out.println(result);
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario2CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario3CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario3");
        OMElement result = new SecurityClient().runSecurityClient("scenario3", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario4CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario2");
        OMElement result = new SecurityClient().runSecurityClient("scenario2", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario5CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario13");
        OMElement result = new SecurityClient().runSecurityClient("scenario13", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario6CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario9");
        OMElement result = new SecurityClient().runSecurityClient("scenario9", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);


        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario7CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario10");
        OMElement result = new SecurityClient().runSecurityClient("scenario10", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario8CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario12");
        OMElement result = new SecurityClient().runSecurityClient("scenario12", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario9CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario11");
        OMElement result = new SecurityClient().runSecurityClient("scenario11", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario10CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario15");
        OMElement result = new SecurityClient().runSecurityClient("scenario15", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario11CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario7");
        OMElement result = new SecurityClient().runSecurityClient("scenario7", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario12CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario5");
        OMElement result = new SecurityClient().runSecurityClient("scenario5", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario13CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario8");
        OMElement result = new SecurityClient().runSecurityClient("scenario8", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario14CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";
        String csvExpectedResult = "Boston";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario6");
        OMElement result = new SecurityClient().runSecurityClient("scenario6", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario15CSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario14");
        OMElement result = new SecurityClient().runSecurityClient("scenario14", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }


    public void testDeleteKeystoreCSVDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.disableSecurity("CSVDataService");
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveServicesCSVDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("CSVDataService");
        seleniumTestBase.logOutUI();

    }


}
