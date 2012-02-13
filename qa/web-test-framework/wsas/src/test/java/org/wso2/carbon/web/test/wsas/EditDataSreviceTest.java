package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.GenericServiceClient;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Sep 23, 2009
 * Time: 11:37:44 AM
 * To change this template use File | Settings | File Templates.
 */

//in this class server is restarted
public class EditDataSreviceTest extends TestCase {

    static FileInputStream freader;
    Selenium browser;
    Connection connection = null;
    String DBName = "data_service_test";
    String hostname ;
    String DBPort ;
    String driverName ;
    String DBUserName ;
    String DBPassword ;
    String dburl;


    public EditDataSreviceTest (String s) {
        super(s);
        try {
            hostname= loadProperties().getProperty("mysql.hostname");
            DBPort=loadProperties().getProperty("mysql.DBPort");
            driverName=loadProperties().getProperty("mysql.driverName");
            DBUserName=loadProperties().getProperty("mysql.DBUserName");
            DBPassword=loadProperties().getProperty("mysql.DBPassword");
            dburl= "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        return properties;
    }

    public void testaddMySQLDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
        seleniumTestBase.loginToUI(username, password);

        Thread.sleep(1000);
        setUpDataBase();

        csvDataService.newDataService("EditedDataService" , "Sample data service");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");

        rdbmsDataService.addQueryName("salaryQuery","MySQLDataSource","select salary,firstName,lastRevisedDate from Salary where employeeNumber = ?");
        rdbmsDataService.addNewInputMapping("employeeNumber","STRING");
        rdbmsDataService.addResult("Salaries","Salary","");
        rdbmsDataService.addNewOutputMapping("element","Amount","salary","");
        rdbmsDataService.addNewOutputMapping("attribute","name","firstName","");
        rdbmsDataService.addNewOutputMapping("element","LastRevisedDate","lastRevisedDate","");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("employeeQuery","MySQLDataSource","select employeeNumber,firstName,lastName,email from Employees");
        rdbmsDataService.addResult("Employees","Employee","");
        rdbmsDataService.addNewOutputMapping("element","EmpNo","employeeNumber","");
        rdbmsDataService.addNewOutputMapping("attribute","name","firstName","");
        rdbmsDataService.addNewOutputMapping("element","LastName","lastName","");
        rdbmsDataService.addNewOutputMapping("element","Email","email","");
        rdbmsDataService.addNewOutputMapping("query","","","salaryQuery");
        rdbmsDataService.saveQuery();


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("getEmployees","employeeQuery");
        rdbmsDataService.addOperation("getSalary","salaryQuery");


        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("EditedDataService");



    }

    // test nested queries
    public void testInvokeServiceMySQL1() throws Exception{
        GenericServiceClient client = new GenericServiceClient();
        String serviceName = "EditedDataService";
        String csvExpectedResult = "Diane";

        OMElement result = client.twoWayAnonClient(serviceName, "urn:getSalary",  "http://ws.wso2.org/dataservice","getSalary","employeeNumber","1002");
        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));



    }

    public void testEditDataService() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        CSVDataService csvDataService = new CSVDataService(browser);

        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("EditedDataService"));

        instServiceManagement.accessServiceDashboard("EditedDataService");
        assertTrue(browser.isElementPresent("//table[@id='serviceOperationsTable']/tbody/tr[8]/td/strong"));
        browser.click("link=Edit Data Service (Wizard)");
        browser.waitForPageToLoad("30000");


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        browser.click("link=Edit Query");
        browser.waitForPageToLoad("30000");
        browser.click("link=Delete");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("newInputMapping");
        browser.waitForPageToLoad("30000");
        browser.type("inputMappingNameId", "firstName");
        browser.select("inputMappingSqlTypeId", "label=STRING");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.type("sql", "select salary,firstName,lastRevisedDate from Salary where firstName = ?");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        instServiceManagement.restartServer();
        csvDataService.checkService("EditedDataService");
    }

    public void testInvokeeditedService() throws Exception{
        GenericServiceClient client = new GenericServiceClient();

        String serviceName = "EditedDataService";
        String csvExpectedResult = "Diane";

        OMElement result = client.twoWayAnonClient(serviceName, "urn:getSalary",  "http://ws.wso2.org/dataservice","getSalary","firstName","Diane");
        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));



    }



    public void testRemoveEditedDataServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("EditedDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testLogoutUIEditedDataService()throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        freader.close();
        seleniumTestBase.logOutUI();


    }



    public void setUpDataBase(){
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {

            String url = "jdbc:mysql://" + hostname + ":" + DBPort;
            connection = DriverManager.getConnection(url, DBUserName, DBPassword);
            String check_database="DROP DATABASE IF EXISTS " + DBName;
            String create_database = "CREATE DATABASE " + DBName;
            Statement stmt = connection.createStatement();
            stmt.execute(check_database);
            stmt.execute(create_database);
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            Statement stmt = connect.createStatement();


            String insert_row_sql2 ="CREATE TABLE Employees(\n" +
                    "        employeeNumber VARCHAR(10),\n" +
                    "        lastName VARCHAR(50),\n" +
                    "        firstName VARCHAR(50),\n" +
                    "        extension VARCHAR(10),\n" +
                    "        email VARCHAR(100),\n" +
                    "        officeCode VARCHAR(10),\n" +
                    "        reportsTo INTEGER,\n" +
                    "        jobTitle VARCHAR(50)\n" +
                    ");";
            String insert_row_sql3 = "insert into Employees values ('1002','Murphy','Diane','x5800','dmurphy@classicmodelcars.com','1',null,'President')";
            String insert_row_sql4 = "insert into Employees values ('1056','Patterson','Mary','x4611','mpatterso@classicmodelcars.com','1',1002,'VP Sales')";
            String insert_row_sql5 = "insert into Employees values ('1076','Firrelli','Jeff','x9273','jfirrelli@classicmodelcars.com','1',1002,'VP Marketing')";

            String insert_row_sql6="CREATE TABLE Salary(\n" +
                    "        employeeNumber VARCHAR(10),\n" +
                    "        firstName VARCHAR(50),\n" +
                    "        salary DOUBLE,\n" +
                    "        lastRevisedDate DATE\n" +
                    ")";

            String insert_row_sql7="INSERT into Salary (employeeNumber,firstName,salary,lastRevisedDate) values ('1002','Diane',13000,'2007/11/30')";
            String insert_row_sql8="INSERT into Salary (employeeNumber,firstName,salary,lastRevisedDate) values ('1056','Mary',30000,'2007/01/20')";
            String insert_row_sql9="INSERT into Salary (employeeNumber,firstName,salary,lastRevisedDate) values ('1076','Jeff',17500,'2008/01/01')";

            String insert_row_sql10="USE "+DBName;
            stmt.executeUpdate(insert_row_sql10);
            stmt.executeUpdate(insert_row_sql2);
            stmt.executeUpdate(insert_row_sql3);
            stmt.executeUpdate(insert_row_sql4);
            stmt.executeUpdate(insert_row_sql5);
            stmt.executeUpdate(insert_row_sql6);
            stmt.executeUpdate(insert_row_sql7);
            stmt.executeUpdate(insert_row_sql8);
            stmt.executeUpdate(insert_row_sql9);

        } catch (SQLException e) {
        }
    }

}
