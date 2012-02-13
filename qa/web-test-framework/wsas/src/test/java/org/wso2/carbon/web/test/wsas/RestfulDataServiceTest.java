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
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;


public class RestfulDataServiceTest extends TestCase {
    Selenium browser;

    Connection connection = null;
    String DBName = "DATASERVICE_SAMPLE";
    String hostname;
    String DBPort;
    String driverName;
    String DBUserName;
    String DBPassword;
    String dburl;
    private static final String moduleVersion = "2.01";

    public RestfulDataServiceTest(String s) {
        super(s);
        try {
            hostname = loadProperties().getProperty("mysql.hostname");
            DBPort = loadProperties().getProperty("mysql.DBPort");
            driverName = loadProperties().getProperty("mysql.driverName");
            DBUserName = loadProperties().getProperty("mysql.DBUserName");
            DBPassword = loadProperties().getProperty("mysql.DBPassword");
            dburl = "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
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
        csvDataService.newDataService("RestfulDataServiceTest", "Sample data service");
        addNewDataSource("MySQLDataSource", "RDBMS");
        addNewQuery("productQuery", "MySQLDataSource", "select productCode,productName,productLine,productScale,productVendor,productDescription,quantityInStock,buyPrice,MSRP \n" +
                "  from \n" +
                "    Products \n" +
                "  where \n" +
                "    productCode=?", "productCode", "STRING", "Product", "Details", "http://product.abc.com");

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        addResource("product/{productCode}", "GET", "productQuery");
        csvDataService.checkService("RestfulDataServiceTest");


    }

    public void testRestfulDataServiceInvoke() throws Exception {
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        rdbmsDataService.invokeRestFulService("RestfulDataServiceTest", "S10_1678");
        Thread.sleep(5000);
        rdbmsDataService.invokeRestFulService("RestfulDataServiceTest", "S12_1108");
    }


    public void testRemoveServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("RestfulDataServiceTest");
        cleanDataBase();

    }

    public void testLogoutUI() throws Exception {
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
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            Statement stmt = connect.createStatement();


            String insert_row_sql2 = "CREATE TABLE Products(productCode VARCHAR(15),productName VARCHAR(70),productLine VARCHAR(50),productScale VARCHAR(10),productVendor VARCHAR(50),productDescription LONG VARCHAR,quantityInStock INTEGER,buyPrice DOUBLE,MSRP DOUBLE);";
            String insert_row_sql3 = "CREATE UNIQUE INDEX products_pk ON Products( productCode )";
            String insert_row_sql4 = "insert into Products values ('S10_1678','1969 Harley Davidson Ultimate Chopper','Motorcycles','1:10','Min Lin Diecast','This replica features working kickstand, front suspension, gear-shift lever, footbrake lever, drive chain, wheels and steering. All parts are particularly delicate due to their precise scale and require special care and attention.',7933,48.81,95.7)";
            String insert_row_sql5 = "insert into Products values ('S10_1949','1952 Alpine Renault 1300','Classic Cars','1:10','Classic Metal Creations','Turnable front wheels; steering function; detailed interior; detailed engine; opening hood; opening trunk; opening doors; and detailed chassis.',7305,98.58,214.3)";
            String insert_row_sql6 = "insert into Products values ('S12_1108','2001 Ferrari Enzo','Classic Cars','1:12','Second Gear Diecast','Turnable front wheels; steering function; detailed interior; detailed engine; opening hood; opening trunk; opening doors; and detailed chassis.',3619,95.59,207.8)";

            stmt.executeUpdate("USE DATASERVICE_SAMPLE");
            stmt.executeUpdate(insert_row_sql2);
            stmt.executeUpdate(insert_row_sql3);
            stmt.executeUpdate(insert_row_sql4);
            stmt.executeUpdate(insert_row_sql5);
            stmt.executeUpdate(insert_row_sql6);


        } catch (SQLException e) {
        }
    }

    public void addNewQuery(String queryId, String datasource, String sql, String inputMappingNameId, String inputMappingSqlTypeId, String DataServiceWrapElement, String DataServiceRowName, String RowNamespace) throws Exception {
        browser.click("link=Add New Query");
        browser.waitForPageToLoad("30000");
        browser.type("queryId", queryId);
        browser.select("datasource", "label=" + datasource);
        browser.type("sql", sql);
        browser.click("newInputMapping");
        browser.waitForPageToLoad("30000");
        browser.type("inputMappingNameId", inputMappingNameId);
        browser.select("inputMappingSqlTypeId", "label=" + inputMappingSqlTypeId);
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.type("txtDataServiceWrapElement", DataServiceWrapElement);
        browser.type("txtDataServiceRowName", DataServiceRowName);
        browser.type("txtDataServiceRowNamespace", RowNamespace);
        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add/Edit Output Mapping"));
        assertTrue(browser.isTextPresent("Mapping Type*"));
        assertTrue(browser.isTextPresent("Output field name"));
        browser.type("txtDataServiceOMColumnName", "productCode");
        assertTrue(browser.isTextPresent("Data source column Name"));
        browser.select("cmbDataServiceOMType", "label=element");
        browser.type("txtDataServiceOMElementName", "ProductCode");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=attribute");
        browser.type("txtDataServiceOMElementName", "name");
        browser.type("txtDataServiceOMColumnName", "productName");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=element");
        browser.type("txtDataServiceOMElementName", "ProductLine");
        browser.type("txtDataServiceOMColumnName", "productLine");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        assertTrue(browser.isTextPresent("Do you want to go back to the main configuration?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
    }

    public void addResource(String resourcePath, String resourceMethod, String queryId) throws Exception {
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Resources"));
        assertTrue(browser.isElementPresent("//input[@value='Finish']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.click("link=Add New Resource");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add Resources"));
        assertTrue(browser.isElementPresent("//div[@id='workArea']/form/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]"));
        browser.type("resourcePath", resourcePath);
        browser.select("resourceMethod", "label=" + resourceMethod);
        browser.select("queryId", "label=" + queryId);
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
    }

    public void addNewDataSource(String dataSourceName, String dataSourceType) throws IOException {
        if (browser.isTextPresent("Data Sources")) {
            browser.open(loadProperties().getProperty("context.root") + "/carbon/ds/dataSources.jsp");
            assertEquals("Add New Data Source", browser.getText("link=Add New Data Source"));
            browser.click("link=Add New Data Source");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Add New Data Source"));
            assertTrue(browser.isTextPresent("DataSource Id*"));
            assertTrue(browser.isTextPresent("Data Source Type*"));
            browser.type("datasourceId", dataSourceName);
            if (dataSourceType.equals("RDBMS")) {
                browser.select("datasourceType", "label=RDBMS");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent("Database Engine*"));
                assertTrue(browser.isTextPresent("Driver Class"));
                assertTrue(browser.isTextPresent("JDBC URL"));
                assertTrue(browser.isTextPresent("User Name"));
                assertTrue(browser.isTextPresent("Password"));
                assertTrue(browser.isTextPresent("Min. Pool Size"));
                assertTrue(browser.isTextPresent("Max. Pool Size"));
                assertTrue(browser.isTextPresent("AutoCommit"));

                browser.select("databaseEngine", "label=MySQL");
                assertEquals("com.mysql.jdbc.Driver", browser.getValue("org.wso2.ws.dataservice.driver"));
                assertEquals("jdbc:mysql://[machine-name/ip]:[port]/[database-name]", browser.getValue("org.wso2.ws.dataservice.protocol"));
                browser.type("org.wso2.ws.dataservice.protocol", "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName);
                browser.type("org.wso2.ws.dataservice.user", DBUserName);
                browser.type("org.wso2.ws.dataservice.password", DBPassword);
                browser.click("//input[@value='Test Connection']");
                assertEquals("Database connection is successfull with driver class com.mysql.jdbc.Driver , jdbc url jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName + " and user name " + DBUserName, browser.getText("//div[@id='messagebox-info']/p"));
                browser.click("//button[@type='button']");
                browser.click("//input[@value='Save']");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent(dataSourceName));
                assertEquals("Edit Datasource", browser.getText("link=Edit Datasource"));
                assertEquals("Delete Datasource", browser.getText("link=Delete Datasource"));

                browser.click("//input[@value='Next >']");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent("Queries"));


            } else {
                //logout UI
            }

        }
    }

    public void cleanDataBase() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//           Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
//           Statement stmt= connect.createStatement();
//        } catch (SQLException e) {
//           e.printStackTrace();
//        }

        try {
            Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            Statement stmt = connect.createStatement();
            //stmt.executeUpdate("USE "+ DBName);
            stmt.executeUpdate("DROP TABLE Products");
            stmt.executeUpdate("DROP DATABASE  " + DBName);
        } catch (SQLException e) {
            System.out.println(e);
        }


    }

}
