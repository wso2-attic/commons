/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.ds.common;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;
import org.apache.axiom.om.OMElement;
import junit.framework.TestCase;
import java.io.File;
import java.util.Properties;

import com.thoughtworks.selenium.Selenium;

import javax.xml.namespace.QName;

public class CSVDataUploadedServiceManagement extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;

    public void setUp() throws Exception  {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
    }


    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.loginToUI(username, password);
        
        Thread.sleep(200);
    }

    public void testUploadServiceCSVUploDataService2() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
//
//      seleniumTestBase.loginToUI("admin","admin");
        CSVDataService csvDataService = new CSVDataService(browser);

        File resourcePath = new File("." + File.separator + "lib" + File.separator + "CSVDataService.dbs");
        browser.click("link=Upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Upload Data Service"));
        assertTrue(browser.isElementPresent("//input[@value=' Cancel ']"));
        seleniumTestBase.SetFileBrowse("dbsFilename",resourcePath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Data Service configuration file uploaded successfully."));
        browser.click("//button[@type='button']");

        csvDataService.checkService("CSVDataService");

    }


    public void testInvokeUploadedServiceCSVDataService2() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);
        String serviceName = "CSVDataService";
        String csvExpectedResult = "Boston";

        String result = csvDataService.tryCSVDataService(serviceName, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);
    }

    public void testServiceGroupParameterCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("CSVDataService", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("CSVDataService", "NewParam1");
    }


    public void testAddServiceGroupModuleCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("CSVDataService", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("CSVDataService", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingCSVDataService2() throws Exception {

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("CSVDataService", "data_service");
        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.port") + property.getProperty("context.root") + "/services/" + "CSVDataService";
        instServiceManagement.checkThrottling(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice", "s");


    }

    public void testServiceModuleMgtCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.engageServiceModule("CSVDataService","CSVDataService", "sandesha2-" + moduleVersion);
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.disengageServiceModule("CSVDataService", "sandesha2-" + moduleVersion);
    }

    public void testServiceActivationCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.serviceActivation("CSVDataService", "Deactivate");
        instServiceManagement.serviceActivation("CSVDataService", "Activate");
    }

    public void testTransportCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.checkTransport("CSVDataService");
    }

    public void testServiceParameterCSVDataService2() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("CSVDataService");
        mySeleniumTestBase.deleteParameter("testparam");
    }

    public void testOperationalModuleMgtCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("CSVDataService","getAllCustomer", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("CSVDataService","getAllCustomer", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersCSVDataService2() throws Exception {
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

    public void testOperationalFlowsCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("CSVDataService");
        instServiceManagement.openOperationDashboard("CSVDataService", "getAllCustomer");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingCSVDataService2() throws Exception {
        String serviceName ="CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.enableRM("CSVDataService");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("CSVDataService");

    }


    public void testMTOMCSVDataService2() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();

        insServiceManagement.changeServiceMTOM("CSVDataService","True" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("CSVDataService","Optional" );
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("CSVDataService", "False");


    }


    public void testAddKeyStoreCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instServiceManagement.Login();
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario4");
        OMElement result = new SecurityClient().runSecurityClient("scenario4", serviceName, nameSpace, soapAction, operation,"id");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario2CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario3CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario3");
        OMElement result = new SecurityClient().runSecurityClient("scenario3", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario4CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario2");
        OMElement result = new SecurityClient().runSecurityClient("scenario2", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario5CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario13");
        OMElement result = new SecurityClient().runSecurityClient("scenario13", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario6CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario9");
        OMElement result = new SecurityClient().runSecurityClient("scenario9", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario7CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario10");
        OMElement result = new SecurityClient().runSecurityClient("scenario10", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario8CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario12");
        OMElement result = new SecurityClient().runSecurityClient("scenario12", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario9CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario11");
        OMElement result = new SecurityClient().runSecurityClient("scenario11", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario10CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario15");
        OMElement result = new SecurityClient().runSecurityClient("scenario15", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario11CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario7");
        OMElement result = new SecurityClient().runSecurityClient("scenario7", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario12CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario5");
        OMElement result = new SecurityClient().runSecurityClient("scenario5", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario13CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario8");
        OMElement result = new SecurityClient().runSecurityClient("scenario8", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario14CSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "CSVDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomer";
        String operation = "getAllCustomer";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario6");
        OMElement result = new SecurityClient().runSecurityClient("scenario6", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Boston";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "address"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario15CSVDataService2() throws Exception {
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

    public void testDeleteKeystoreCSVDataService2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.disableSecurity("CSVDataService");
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveCSVDataService2() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        File resourcePath = new File("." + File.separator + "lib" + File.separator + "CSVDataService.dbs");

        instServiceManagement.Login();
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.deleteService("CSVDataService");
        resourcePath.delete();
        seleniumTestBase.logOutUI();
    }

//    public void testLogoutUi() throws Exception{
//        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
//        seleniumTestBase.logOutUI();
//    }
}
