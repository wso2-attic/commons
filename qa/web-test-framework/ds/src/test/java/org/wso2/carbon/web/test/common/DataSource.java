/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;


public class DataSource extends TestCase {

    Selenium browser;
    Connection connection = null;
    String hostname ;
    String DBPort;
    String DBName ;
    String driverName ;
    String DBUserName ;
    String DBPassword ;
    String dburl ;
    Properties properties = new Properties();

    public DataSource(Selenium _browser) {
        browser = _browser;
    }

    public void setUpDB() {
        try{
         FileInputStream file= new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
         properties.load(file);
         file.close();

        String hostname = properties.getProperty("mysql.hostname");
        String DBPort =properties.getProperty("mysql.DBPort");
        String driverName = properties.getProperty("mysql.driverName");
        String DBUserName = properties.getProperty("mysql.DBUserName");
        String DBPassword = properties.getProperty("mysql.DBPassword");

      
            dburl = "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;
        }catch(Exception e){
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
            String create_table_sql = "CREATE TABLE customer(id VARCHAR(100), name VARCHAR(200), country VARCHAR(200))";
            String insert_row_sql1 = "INSERT INTO customer(id, name, country) VALUES('001', 'Charitha', 'Sri lanka') ";
            String insert_row_sql2 = "INSERT INTO customer(id, name, country) VALUES('001', 'Krishantha', 'India') ";
            stmt.executeUpdate(create_table_sql);
            stmt.executeUpdate(insert_row_sql1);
            stmt.executeUpdate(insert_row_sql2);
        } catch (SQLException e) {
        }

    }

    public void testNewMySQLDataSource(String alias, String dsName) throws Exception {
        Thread.sleep(10000);
        browser.open("/carbon/admin/index.jsp?loginStatus=true");
        browser.click("link=Data Sources");
        browser.waitForPageToLoad("30000");
        assertEquals("Data Sources", browser.getText("//div[@id='middle']/h2"));
        assertEquals("Add Data Source", browser.getText("link=Add Data Source"));
        browser.click("link=Add Data Source");
        browser.waitForPageToLoad("30000");
        assertEquals("New Data Source", browser.getText("//div[@id='middle']/h2"));
        assertEquals("New Data Source", browser.getTable("//div[@id='workArea']/table.0.0"));
        browser.type("alias", alias);
        browser.type("dsName", dsName);
        browser.type("driver", driverName);
        browser.type("url", dburl);
        browser.type("user", DBUserName);
        browser.type("password", DBPassword);
        browser.type("validationquery", "select * from customer");
        browser.click("testConnectionButton");
        Thread.sleep(2000);
        assertTrue(browser.isTextPresent("Connection is healthy."));
        browser.click("//button[@type='button']");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        assertEquals(alias, browser.getTable("myTable.1.0"));
    }

    public void cleanDB() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(dburl, DBUserName, DBPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE customer");
            stmt.executeUpdate("DROP DATABASE " + DBName);
        } catch (SQLException e) {
        }


    }

    public void deleteDataSource(String datasourceAlias) {
        browser.click("link=Data Sources");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(datasourceAlias));
        browser.click("link=Delete");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

    }


}
