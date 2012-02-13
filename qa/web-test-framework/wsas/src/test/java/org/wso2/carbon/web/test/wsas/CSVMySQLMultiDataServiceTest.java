package org.wso2.carbon.web.test.wsas;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Aug 31, 2009
 * Time: 9:29:03 AM
 * To change this template use File | Settings | File Templates.
 */

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

public class CSVMySQLMultiDataServiceTest extends TestCase {
    Selenium browser;
    private static final String moduleVersion = "2.01";

    public CSVMySQLMultiDataServiceTest(String s){
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

    public void testCreatCSVMySQLMultiDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

         String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
        seleniumTestBase.loginToUI(username, password);

        rdbmsDataService.setUpDataBase();
        csvDataService.newDataService("CSVMySQLMultiDataService" , "Sample data service");
        rdbmsDataService.addNewCSVMySQLDataSource("CSVDataSource", "DataServiceCSVResource.csv", "id,name,address", "1,2,3", 2,"MySQLDataSource","RDBMS");
        csvDataService.addCSVQuery("q1", "CSVDataSource", "customers", "customer");
        csvDataService.addOutputMapping("element", "id", "1");
        csvDataService.addOutputMapping("element", "name", "2");
        csvDataService.addOutputMapping("attribute", "address","3" );
        csvDataService.gotoMainConfiguration();
        rdbmsDataService.addNewQuery("customers-query", "MySQLDataSource", "select * from customer");
        //csvDataService.addNewQuery("customers-query", "MySQLDataSource", "select * from customer");
        csvDataService.addTwoOperations("getAllCustomers","q1","getCustomers","customers-query");
        csvDataService.checkService("CSVMySQLMultiDataService");

    }

    public void testInvokeServiceMySQL() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "CSVMySQLMultiDataService";
        String csvExpectedResult = "Charitha";

        String result = rdbmsDataService.tryRDBMSDataService(serviceName, "getCustomers", "urn:getCustomers", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
        //rdbmsDataService.cleanDataBase();

    }


    public void testInvokeServiceCSV() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName = "CSVMySQLMultiDataService";
        String csvExpectedResult = "Boston";

        String result = csvDataService.tryCSVDataService(serviceName, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);
    }

    public void testRemoveServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("CSVMySQLMultiDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testLogoutUi() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }
}
