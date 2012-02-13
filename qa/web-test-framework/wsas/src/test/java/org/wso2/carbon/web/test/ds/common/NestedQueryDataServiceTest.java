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

package org.wso2.carbon.web.test.ds.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Iterator;

import org.wso2.carbon.web.test.common.GenericServiceClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;

import javax.xml.namespace.QName;
import javax.xml.bind.Element;

/*
        this class tests nested queries generated through wizard and stored procedures with nested queries
        tests editing a nested query
        tests having a excel query nested in a mysql query
        
 */
public class NestedQueryDataServiceTest extends TestCase {

    Selenium browser;
    Connection connection = null;
    String DBName = "data_service_test";
    String hostname ;
    String DBPort ;
    String driverName ;
    String DBUserName ;
    String DBPassword ;
    String dburl;
    Properties property;
    String username;
    String password;


    public NestedQueryDataServiceTest(String s) {
        super(s);

    }


    public void setUp() throws Exception  {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        hostname= property.getProperty("mysql.hostname");
        DBPort=property.getProperty("mysql.DBPort");
        driverName=property.getProperty("mysql.driverName");
        DBUserName=property.getProperty("mysql.DBUserName");
        DBPassword=property.getProperty("mysql.DBPassword");
    }



    public void testaddMySQLDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
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
        rdbmsDataService.addNewOutputMapping("attribute","Amount","salary","");
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

    // test nested queries (both queries mysql)
    public void testInvokeServiceMySQL1() throws Exception{
        GenericServiceClient client = new GenericServiceClient();

        String serviceName = "NestedQueryDataService";
        String csvExpectedResult = "Diane";

        OMElement result = client.twoWayAnonClient(serviceName, "urn:getEmployees",  "http://ws.wso2.org/dataservice","getEmployees");
        System.out.println(result.toString());
        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));
        csvExpectedResult = "13000";


        result=result.getFirstElement();

        if(!result.toString().contains(csvExpectedResult)){

            assertTrue(csvExpectedResult,false);
        }



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

    public void testEditingNestedQuery() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("NestedQueryDataService"));

        instServiceManagement.accessServiceDashboard("NestedQueryDataService");
        assertTrue(browser.isElementPresent("//table[@id='serviceOperationsTable']/tbody/tr[8]/td/strong"));
        browser.click("link=Edit Data Service (Wizard)");
        browser.waitForPageToLoad("30000");


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        String query="salaryQuery";

        int i = 1;

        while (browser.isElementPresent("//table[@id='query-table']/tbody/tr[" + Integer.toString(i) + "]/td[1]")) {
            if(query.equals(browser.getText("//table[@id='query-table']/tbody/tr[" + Integer.toString(i) + "]/td[1]"))){
                browser.click("//table[@id='query-table']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[1]");
                browser.waitForPageToLoad("30000");
                break;
            }

            i = i + 1;
        }


        browser.click("//a[@onclick=\"deleteInputMappingsFromAddQuery(document.getElementById('employeeNumber').value,document.getElementById('STRING').value,document.getElementById('queryId').value);\"]");
        assertEquals("Do you want to delete the input mapping employeeNumber?",browser.getText("messagebox-confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue( browser.isTextPresent("salaryQuery has been used by another query nestedly. Please remove the relevant query to proceed."));
        browser.click("//button[@type='button']");
    }

    public void testaddingRDBMSExcelNestedQuery() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("NestedQueryDataService"));

        instServiceManagement.accessServiceDashboard("NestedQueryDataService");
        assertTrue(browser.isElementPresent("//table[@id='serviceOperationsTable']/tbody/tr[8]/td/strong"));
        browser.click("link=Edit Data Service (Wizard)");
        browser.waitForPageToLoad("30000");


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        csvDataService.newEXCELDataSource("EXCELDataSourse","NestedQueryExcelResource.xls");

        csvDataService.addEXCELQuery("excelSalaryQuery","EXCELDataSourse","Sheet1",2,100,"Salaries", "Salary");
        csvDataService.addOutputMapping("attribute","Amount","salary");
        csvDataService.addOutputMapping("attribute","name","firstName");
        csvDataService.addOutputMapping("element","LastRevisedDate","lastRevisedDate");
        csvDataService.gotoMainConfiguration();



        rdbmsDataService.addQueryName("excelEmployeeQuery","MySQLDataSource","select employeeNumber,firstName,lastName,email from Employees");
        rdbmsDataService.addResult("Employees","Employee","");
        rdbmsDataService.addNewOutputMapping("element","EmpNo","employeeNumber","");
        rdbmsDataService.addNewOutputMapping("attribute","name","firstName","");
        rdbmsDataService.addNewOutputMapping("element","LastName","lastName","");
        rdbmsDataService.addNewOutputMapping("element","Email","email","");
        rdbmsDataService.addNewOutputMapping("query","","","excelSalaryQuery");
        rdbmsDataService.saveQuery();

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("excelEmployeeQuery","excelEmployeeQuery");


        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        Thread.sleep(20000);

    }

    // test nested queries (1 mysql query 1 excel query)
    public void testInvokeServiceMySQL2() throws Exception{
        GenericServiceClient client = new GenericServiceClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
          instServiceManagement.restartServer();
        String serviceName = "NestedQueryDataService";
        String csvExpectedResult = "Diane";

        OMElement result = client.twoWayAnonClient(serviceName, "urn:excelEmployeeQuery",  "http://ws.wso2.org/dataservice","excelEmployeeQuery");
        System.out.println(result.toString());
        assertEquals(csvExpectedResult,result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice",  "name")));
        csvExpectedResult = "13000";


        result=result.getFirstElement();

        if(!result.toString().contains(csvExpectedResult)){
           
            assertTrue(csvExpectedResult,false);
        }



    }

    public void testRemoveNestedDataServices() throws Exception {
            SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
            RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

            seleniumTestBase.deleteService("NestedQueryDataService");
            rdbmsDataService.cleanDataBase();

        }

        public void testLogoutUINestedDataService()throws Exception{
            SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
            seleniumTestBase.logOutUI();

        }
    



    public void setUpDataBase(){
        try {

            dburl= "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
