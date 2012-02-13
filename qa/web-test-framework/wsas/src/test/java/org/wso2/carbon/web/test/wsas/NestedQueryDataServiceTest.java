package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.GenericServiceClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Sep 8, 2009
 * Time: 11:37:28 AM
 * To change this template use File | Settings | File Templates.
 */

/*
        this class tests nested queries generated through wizard and stored procedures with nested queries
 */
public class NestedQueryDataServiceTest extends TestCase {
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

    public NestedQueryDataServiceTest(String s) {
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


    public void setUp() throws Exception  {
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

        String username = loadProperties().getProperty("admin.username");
        String password = loadProperties().getProperty("admin.password");
        seleniumTestBase.loginToUI(username, password);

        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
        Thread.sleep(1000);
        setUpDataBase();
        rdbmsDataService.addStoredProcedure("create procedure getSalaryFromProcedure(lName VARCHAR(20)) select salary,firstName,lastRevisedDate from Salary where employeeNumber in (select employeeNumber from Employees where lastName=lName);");
        csvDataService.newDataService("NestedQueryDataService" , "Sample data service");
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

        rdbmsDataService.addQueryName("storedProcedureQuery","MySQLDataSource","call getSalaryFromProcedure(?)");
        rdbmsDataService.addNewInputMapping("lastName","STRING");
        rdbmsDataService.addResult("Salaries","Salary","");
        rdbmsDataService.addNewOutputMapping("element","Amount","salary","");
        rdbmsDataService.addNewOutputMapping("attribute","name","firstName","");
        rdbmsDataService.addNewOutputMapping("element","LastRevisedDate","lastRevisedDate","");
        rdbmsDataService.saveQuery();


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("getEmployees","employeeQuery");
        rdbmsDataService.addOperation("getSalary","salaryQuery");
        rdbmsDataService.addOperation("getSalaryFromStoredProcedure","storedProcedureQuery");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("NestedQueryDataService");



    }

    // test nested queries
    public void testInvokeServiceMySQL1() throws Exception{
        GenericServiceClient client = new GenericServiceClient();

        String serviceName = "NestedQueryDataService";
        String csvExpectedResult = "Diane";

        OMElement result = client.twoWayAnonClient(serviceName, "urn:getSalary",  "http://ws.wso2.org/dataservice","getSalary","employeeNumber","1002");
        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));



    }

    public void testInvokeServiceMySQL2() throws Exception{
        GenericServiceClient client = new GenericServiceClient();

        String serviceName = "NestedQueryDataService";
        String csvExpectedResult = "Jeff";

        OMElement result = client.twoWayAnonClient(serviceName, "urn:getSalary",  "http://ws.wso2.org/dataservice","getSalary","employeeNumber","1076");
        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));


    }

    //test nestedQueries with a stored procedure
    public void testInvokegetSalaryFromNestedQuery1() throws Exception{

        GenericServiceClient client = new GenericServiceClient();
        String serviceName = "NestedQueryDataService";
        String csvExpectedResult = "Jeff";
        OMElement result = client.twoWayAnonClient(serviceName, "urn:getSalaryFromStoredProcedure",  "http://ws.wso2.org/dataservice","getSalaryFromStoredProcedure","lastName","Firrelli");

        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));


    }

    public void testInvokegetSalaryFromNestedQuery2() throws Exception{

        GenericServiceClient client = new GenericServiceClient();
        String serviceName = "NestedQueryDataService";
        String csvExpectedResult = "Mary";
        OMElement result = client.twoWayAnonClient(serviceName, "urn:getSalaryFromStoredProcedure",  "http://ws.wso2.org/dataservice","getSalaryFromStoredProcedure","lastName","Patterson");

        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));


    }

    public void testRemoveNestedDataServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("NestedQueryDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testLogoutUINestedDataService()throws Exception{
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
            String create_database = "CREATE DATABASE " + DBName;
            Statement stmt = connection.createStatement();
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
