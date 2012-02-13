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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.ds.common.CSVDataService;
import org.wso2.carbon.web.test.ds.common.RDBMSDataService;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.EditAxis2XML;

public class HotUpdateTest extends TestCase {
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


    public HotUpdateTest (String s) {
        super(s);

    }

    public void setUp() throws Exception {
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

    public void testEditAxis2XML() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        seleniumTestBase.loginToUI(username, password);
        EditAxis2XML hu=new EditAxis2XML();
        hu.editXML("hotupdate","true");
        Thread.sleep(5000) ;
        instServiceManagement.restartServer();
    }

    public void testaddMySQLDataService() throws Exception {

        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);


        Thread.sleep(1000);
        setUpDataBase();

        csvDataService.newDataService("EditedDataService" , "Sample data service");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");

        rdbmsDataService.addQueryName("employeeQuery","MySQLDataSource","select employeeNumber,firstName,lastName,email from Employees");
        rdbmsDataService.addResult("Employees","Employee","");
        rdbmsDataService.addNewOutputMapping("element","EmpNo","employeeNumber","");
        rdbmsDataService.addNewOutputMapping("attribute","name","firstName","");
        rdbmsDataService.addNewOutputMapping("element","LastName","lastName","");
        rdbmsDataService.addNewOutputMapping("element","Email","email","");

        rdbmsDataService.saveQuery();


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("getEmployees","employeeQuery");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("EditedDataService");



    }

    // test nested queries
    public void testInvokeServiceMySQL1() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
        String serviceName = "EditedDataService";
        String csvExpectedResult = "Diane";

        String result = rdbmsDataService.tryRDBMSDataService(serviceName, "getEmployees", "urn:getEmployees", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);


    }

    public void testEditDataService() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
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

        rdbmsDataService.addQueryName("selectEmployee","MySQLDataSource","select firstName from Employees where employeeNumber=?");
        rdbmsDataService.addNewInputMapping("employeeNumber","INTEGER");
        rdbmsDataService.addResult("Employees","Employee","");
        rdbmsDataService.addNewOutputMapping("attribute","name","firstName","");
        rdbmsDataService.saveQuery();

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("selectEmployee","selectEmployee");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("EditedDataService");
    }

    public void testInvokeServiceSelectCustomer() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "EditedDataService";
        String csvExpectedResult = "Mary";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "selectEmployee","urn:selectEmployee",  "http://ws.wso2.org/dataservice","employeeNumber","1056");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
        //rdbmsDataService.cleanDataBase();

    }



    public void testRemoveEditedDataServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("EditedDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testResetAxis2XML() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        EditAxis2XML hu=new EditAxis2XML();
        hu.editXML("hotupdate","false");
        Thread.sleep(5000) ;
        instServiceManagement.restartServer();
    }

    public void testLogoutUIEditedDataService()throws Exception{
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


            String insert_row_sql10="USE "+DBName;
            stmt.executeUpdate(insert_row_sql10);
            stmt.executeUpdate(insert_row_sql2);
            stmt.executeUpdate(insert_row_sql3);
            stmt.executeUpdate(insert_row_sql4);
            stmt.executeUpdate(insert_row_sql5);


        } catch (SQLException e) {
        }
    }

}
