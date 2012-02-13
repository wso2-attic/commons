package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.apache.axiom.om.OMElement;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;

import javax.xml.namespace.QName;
/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Aug 27, 2009
 * Time: 11:49:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class CSVExcelMultiDataServiceTest extends TestCase{
    Selenium browser;
    private static final String moduleVersion = "2.01";

    public CSVExcelMultiDataServiceTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        FileInputStream  freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    public void testCreatCSVExcelMultiDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);

        String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
        seleniumTestBase.loginToUI(username, password);

        csvDataService.newDataService("CSVExcelMultiDataService" , "Sample data service");
        csvDataService.newCSVEXCELDataSource("CSVDataSource", "DataServiceCSVResource.csv", "id,name,address", "1,2,3", 2,"EXCELDataSourse","DataServiceExcelResource.xls");
        csvDataService.addCSVQuery("q1", "CSVDataSource", "customers", "customer");
        csvDataService.addOutputMapping("element", "id", "1");
        csvDataService.addOutputMapping("element", "name", "2");
        csvDataService.addOutputMapping("attribute", "address","3" );
        csvDataService.gotoMainConfiguration();
        csvDataService.addEXCELQuery("q2","EXCELDataSourse","Sheet1",2,100,"students", "student");
        csvDataService.addOutputMapping("element","student_id","Student_id");
        csvDataService.addOutputMapping("attribute","name","Name");
        csvDataService.addOutputMapping("element","grade","Grade");
        csvDataService.gotoMainConfiguration();
        csvDataService.addTwoOperations("getAllCustomers","q1","getAllStudents","q2");
        csvDataService.checkService("CSVExcelMultiDataService");

    }

    public void testInvokeServiceEXCEL() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName ="CSVExcelMultiDataService";
        String csvExpectedResult = "Mackie";

        String result = csvDataService.tryEXCELDataService(serviceName, "getAllStudents", "urn:getAllStudents", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);


    }

    public void testInvokeServiceCSV() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName = "CSVExcelMultiDataService";
        String csvExpectedResult = "Boston";

        String result = csvDataService.tryCSVDataService(serviceName, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);
    }

    public void testRemoveCSVExcelServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.deleteService("CSVExcelMultiDataService");

    }

    public void testLogoutUiCSVExcel() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.logOutUI();
    }
}
