package org.wso2.carbon.web.test.ds.common;

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

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Sep 14, 2009
 * Time: 11:48:39 AM
 * To change this template use File | Settings | File Templates.
 */

//to do- complete the test for temporary tables
public class TemporaryTableDataSreviceTest extends TestCase {

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

    public TemporaryTableDataSreviceTest(String s) {
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
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

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

//        rdbmsDataService.addQueryName("invokeStoredProcedure","MySQLDataSource","call testTempTable(?)");
//        rdbmsDataService.addNewInputMapping("id","STRING");
//        rdbmsDataService.addResult("customers","customer","");
//        rdbmsDataService.addNewOutputMapping("element","id","id","");
//        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
//        rdbmsDataService.addNewOutputMapping("element","country","country","");
//        rdbmsDataService.saveQuery();


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        //       rdbmsDataService.addOperation("getTempCustomer","getTempCustomer");
        rdbmsDataService.addOperation("invokeStoredFunction","invokeStoredFunction");
//        rdbmsDataService.addOperation("invokeStoredProcedure","invokeStoredProcedure");

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

//    public void testTemporaryTable()throws Exception{
//        GenericServiceClient client=new GenericServiceClient();
//        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
//
//        String serviceName = "TemporaryTableDataService";
//        String csvExpectedResult = "Krishantha";
//
//        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "invokeStoredProcedure","urn:invokeStoredProcedure","http://ws.wso2.org/dataservice","id","002");
//        System.out.println(result);
//        assertEquals(csvExpectedResult, result);
//    }

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
