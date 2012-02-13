package org.wso2.carbon.web.test.ds.common;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;
import java.util.Properties;
import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;

import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Aug 27, 2009
 * Time: 12:49:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class EXCELDataServiceManagementTest extends TestCase{

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;

    public EXCELDataServiceManagementTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
    }

    public void testCreatEXCELDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);

        seleniumTestBase.loginToUI(username, password);

        csvDataService.newDataService("EXCELDataService" , "Sample data service");
        csvDataService.newEXCELDataSource("EXCELDataSourse","DataServiceExcelResource.xls");
        csvDataService.addEXCELQuery("q1","EXCELDataSourse","Sheet1",2,100,"students", "student");
        csvDataService.addOutputMapping("element","student_id","Student_id");
        csvDataService.addOutputMapping("attribute","name","Name");
        csvDataService.addOutputMapping("element","grade","Grade");
        csvDataService.gotoMainConfiguration();
        csvDataService.addOperation("getAllStudents","q1");
        csvDataService.checkService("EXCELDataService");

    }

    public void testInvokeExcelDataService() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "EXCELDataService";
        String csvExpectedResult = "Mackie";

        instServiceManagement.Login();
        String result = csvDataService.tryEXCELDataService(serviceName, "getAllStudents", "urn:getAllStudents", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);
    }

    public void testExcelDataServiceGroupParameter() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.addServiceGroupParameter("EXCELDataService", "NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("EXCELDataService", "NewParam1");
    }


    public void testAddExcelDataServiceGroupModule() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageServiceGroupModule("EXCELDataService", "sandesha2-" + moduleVersion);
        instServiceManagement.disengageServiceGroupModule("EXCELDataService", "sandesha2-" + moduleVersion);
    }

    public void testCheckThrottlingExcelDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.openServiceDashboard("EXCELDataService", "data_service");

        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.port") + property.getProperty("context.root") + "/services/" + "EXCELDataService";
        instServiceManagement.checkThrottling(serviceepr, "getAllStudents", "urn:getAllStudents", "http://ws.wso2.org/dataservice", "s");


    }

    public void testExcelDataServiceModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        instServiceManagement.engageServiceModule("EXCELDataService","EXCELDataService", "sandesha2-" + moduleVersion);
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        instServiceManagement.disengageServiceModule("EXCELDataService", "sandesha2-" + moduleVersion);
    }

    public void testExcelDataServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.serviceActivation("EXCELDataService", "Deactivate");
        instServiceManagement.serviceActivation("EXCELDataService", "Activate");
    }

    public void testTransportExcelDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.checkTransport("EXCELDataService");
    }

    public void testExcelDataServiceParameter() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        mySeleniumTestBase.deleteParameter("testparam");
    }


    public void testOperationalModuleMgtExcelDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.engageOperationModule("EXCELDataService","getAllStudents", "savan-SNAPSHOT");
        instServiceManagement.disengageOperationModule("EXCELDataService","getAllStudents", "savan-SNAPSHOT");
    }

    public void testOperationalAddParametersExcelDataService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String operationName = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        instServiceManagement.openOperationDashboard("EXCELDataService", operationName);
        InstSeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        instServiceManagement.openOperationDashboard("EXCELDataService", operationName);
        InstSeleniumTestBase.deleteOperationalParameter("testparam");
    }

    public void testOperationalFlowsExcelDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("EXCELDataService");
        instServiceManagement.openOperationDashboard("EXCELDataService", "getAllStudents");
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessagingExcelDataService() throws Exception {

        String serviceName ="EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.enableRM("EXCELDataService");
        instServiceManagement.rmInvocations(serviceName, soapAction, nameSpace, operation, "s");
        instServiceManagement.disableRM("EXCELDataService");

    }


    public void testMTOMExcelDataService() throws Exception {
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();

        insServiceManagement.changeServiceMTOM("EXCELDataService", "True");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("EXCELDataService", "Optional");
        Thread.sleep(1000);
        insServiceManagement.changeServiceMTOM("EXCELDataService", "False");


    }


    public void testAddKeyStoreExcelDataService() throws Exception {

        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        File path = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaserver.jks");
        instKeyStoreManagement.AddKeystore(path.getCanonicalPath(), "qaserver");

    }

    public void testCheckSecurityScenario1ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario4");
        OMElement result = new SecurityClient().runSecurityClient("scenario4", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario2ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario1");
        OMElement result = new SecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);
        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario3ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario3");
        OMElement result = new SecurityClient().runSecurityClient("scenario3", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }

    public void testCheckSecurityScenario4ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario2");
        OMElement result = new SecurityClient().runSecurityClient("scenario2", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario5ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario13");
        OMElement result = new SecurityClient().runSecurityClient("scenario13", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario6ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario9");
        OMElement result = new SecurityClient().runSecurityClient("scenario9", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario7ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario10");
        OMElement result = new SecurityClient().runSecurityClient("scenario10", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario8ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement.enableSecurityScenario(serviceName, "scenario12");
        OMElement result = new SecurityClient().runSecurityClient("scenario12", serviceName, nameSpace, soapAction, operation, "s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario9ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario11");
        OMElement result = new SecurityClient().runSecurityClient("scenario11", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }


    public void testCheckSecurityScenario10ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario15");
        OMElement result = new SecurityClient().runSecurityClient("scenario15", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario11ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario7");
        OMElement result = new SecurityClient().runSecurityClient("scenario7", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario12ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario5");
        OMElement result = new SecurityClient().runSecurityClient("scenario5", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario13ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario8");
        OMElement result = new SecurityClient().runSecurityClient("scenario8", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario14ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario6");
        OMElement result = new SecurityClient().runSecurityClient("scenario6", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);
    }

    public void testCheckSecurityScenario15ExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "EXCELDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllStudents";
        String operation = "getAllStudents";

        instServiceManagement.Login();
        instServiceManagement. enableSecurityScenario(serviceName, "scenario14");
        OMElement result = new SecurityClient().runSecurityClient("scenario14", serviceName, nameSpace, soapAction, operation,"s");
        System.out.println(result);

        String csvExpectedResult = "Mackie";
        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        Thread.sleep(1000);
        instServiceManagement.disableSecurity(serviceName);


    }



    public void testDeleteKeystoreExcelData() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.Login();
        instServiceManagement.disableSecurity("EXCELDataService");
        KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
        instKeyStoreManagement.deleteKeyStore("qaserver.jks");
    }

    public void testRemoveExcelDataServices() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        instServiceManagement.Login();
        seleniumTestBase.deleteService("EXCELDataService");

    }

    public void testLogoutUiExcelData() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }

}
