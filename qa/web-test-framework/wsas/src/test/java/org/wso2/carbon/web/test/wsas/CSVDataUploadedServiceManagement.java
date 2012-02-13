package org.wso2.carbon.web.test.wsas;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;
import org.apache.axiom.om.OMElement;
import junit.framework.TestCase;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

import com.thoughtworks.selenium.Selenium;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Aug 26, 2009
 * Time: 11:38:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class CSVDataUploadedServiceManagement extends TestCase {
    Selenium browser;
    private static final String moduleVersion = "2.01";
    static FileInputStream freader;

    public void setUp() throws Exception  {
        browser = BrowserInitializer.getBrowser();
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
         String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
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
        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
        instServiceManagement.checkThrottling(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice", "s");


    }

//    public void testCheckCachingCSVDataService2() throws Exception {  //caching need not be checked in CSV
//
//
//        ServiceManagement instServiceManagement = new ServiceManagement(browser);
//        instServiceManagement.Login();
//        instServiceManagement.accessServiceDashboard("CSVDataService");
//        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
//        instServiceManagement.checkCaching(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice");
//
//    }

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


//        public void testOperationalThrottling() throws Exception {
//            Thread.sleep(10000);
//            ServiceManagement instServiceManagement = new ServiceManagement(browser);
//            String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
//            instServiceManagement.accessServiceDashboard("CSVDataService");
//            instServiceManagement.openOperationDashboard("CSVDataService", "getAllCustomer");
//            instServiceManagement.checkThrottling(serviceepr, "getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice", "s");
//
//        }

//    public void testOperationalCachingCSVDataService2() throws Exception {   //caching need not be checked in CSV
//
//
//        ServiceManagement instServiceManagement = new ServiceManagement(browser);
//        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") + loadProperties().getProperty("context.root") + "/services/" + "CSVDataService";
//        instServiceManagement.Login();
//        instServiceManagement.accessServiceDashboard("CSVDataService");
//        instServiceManagement.openOperationDashboard("CSVDataService","getAllCustomer");
//        instServiceManagement.checkCaching(serviceepr,"getAllCustomer", "urn:getAllCustomer", "http://ws.wso2.org/dataservice");
//
//
//    }

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
        instServiceManagement.Login();
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.deleteService("CSVDataService");
        seleniumTestBase.logOutUI();
    }

//    public void testLogoutUi() throws Exception{
//        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
//        seleniumTestBase.logOutUI();
//    }
}
