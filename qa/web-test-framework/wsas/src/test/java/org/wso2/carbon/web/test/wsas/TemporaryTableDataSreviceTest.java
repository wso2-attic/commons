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
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;

import javax.xml.namespace.QName;


//to do- complete the test for temporary tables
public class TemporaryTableDataSreviceTest extends TestCase {
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

    public TemporaryTableDataSreviceTest(String s) {
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
        freader.close();
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
        rdbmsDataService.addStoredProcedure("create function test(cusid VARCHAR(10)) returns INTEGER return cusid+10;");
        //rdbmsDataService.addStoredProcedure("delimiter :");
        //rdbmsDataService.addStoredProcedure("create procedure testTempTable2(cusid VARCHAR(10))  begin create temporary table tempTable select * from realCustomer where id=cusid;select * from tempTable;drop table tempTable;end :");
        // rdbmsDataService.addStoredProcedure("delimiter ;");
        csvDataService.newDataService("TemporaryTableDataService" , "Sample data service");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");

        rdbmsDataService.addQueryName("invokeStoredFunction","MySQLDataSource","select test(?) as nextId");
        rdbmsDataService.addNewInputMapping("id","STRING");
        rdbmsDataService.addResult("NextIds","NextId","");
        rdbmsDataService.addNewOutputMapping("attribute","name","nextId","");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("invokeStoredProcedure","MySQLDataSource","call testTempTable(?)");
        rdbmsDataService.addNewInputMapping("id","STRING");
        rdbmsDataService.addResult("customers","customer","");
        rdbmsDataService.addNewOutputMapping("element","id","id","");
        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
        rdbmsDataService.addNewOutputMapping("element","country","country","");
        rdbmsDataService.saveQuery();


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        //       rdbmsDataService.addOperation("getTempCustomer","getTempCustomer");
        rdbmsDataService.addOperation("invokeStoredFunction","invokeStoredFunction");
        rdbmsDataService.addOperation("invokeStoredProcedure","invokeStoredProcedure");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("TemporaryTableDataService");



    }


    public void testStoredFunction()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "TemporaryTableDataService";
        String csvExpectedResult = "11";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "invokeStoredFunction","urn:invokeStoredFunction","http://ws.wso2.org/dataservice","id","001");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
    }

    public void testStoredProcedure()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "TemporaryTableDataService";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "invokeStoredProcedure","urn:invokeStoredProcedure","http://ws.wso2.org/dataservice","id","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
    }

    public void testRemoveTemporaryTablesServices() throws Exception {
           SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
           RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

           seleniumTestBase.deleteService("TemporaryTableDataService");
           rdbmsDataService.cleanDataBase();

     }

    public void testLogoutUIStoredtemporarytablesService()throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.logOutUI();


    }

    public void setUpDataBase() {

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




            // Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            // stmt = connect.createStatement();
            String use_db="USE "+ DBName;

            String create_table_sql = "CREATE TABLE realCustomer(id VARCHAR(100), name VARCHAR(200), country VARCHAR(200))";
            String insert_row_sql1 = "INSERT INTO realCustomer(id, name, country) VALUES('001', 'Charitha', 'Sri lanka') ";
            String insert_row_sql2 = "INSERT INTO realCustomer(id, name, country) VALUES('002', 'Krishantha', 'India') ";
            String insert_row_sql3 = "INSERT INTO realCustomer(id, name, country) VALUES('003', 'Jayani', 'UK') ";
            String insert_row_sql4 = "INSERT INTO realCustomer(id, name, country) VALUES('004', 'Sarasi', 'USA') ";
            String insert_row_sql5 = "INSERT INTO realCustomer(id, name, country) VALUES('005', 'Dinusha', 'Japan') ";

            stmt.executeUpdate(use_db);
            stmt.executeUpdate(create_table_sql);
            stmt.executeUpdate(insert_row_sql1);
            stmt.executeUpdate(insert_row_sql2);
            stmt.executeUpdate(insert_row_sql3);
            stmt.executeUpdate(insert_row_sql4);
            stmt.executeUpdate(insert_row_sql5);

            // System.out.println("Hi");
//            String insert_row_sql6="create procedure testTempTable(cusid VARCHAR(10))  begin create temporary table tempTable select * from realCustomer where id=cusid".concat(";").concat("select * from tempTable").concat(";").concat(" drop table tempTable").concat(";").concat(" end //");
//            System.out.println(insert_row_sql6);
//            stmt.execute("DELIMITER //");
//           stmt.executeUpdate(insert_row_sql6);
            //  stmt.execute("DELIMITER ;");

            //stmt.executeUpdate("delimiter :");
            stmt.close();
            connection.close();
//            stmt.executeUpdate("delimiter ;");

        } catch (SQLException e) {

        }


    }





}
