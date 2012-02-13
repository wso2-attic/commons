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

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class RDBMSDataService extends SeleneseTestCase {
    Selenium browser;
    Connection connection = null;
    String DBName = "data_service_test";
    String hostname;
    String DBPort;
    String driverName;
    String DBUserName;
    String DBPassword;
    String dburl;


    public RDBMSDataService(Selenium _browser) {
        browser = _browser;
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

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
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
            String check_database = "DROP DATABASE IF EXISTS " + DBName;
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

            String create_table_sql = "CREATE TABLE customer(id VARCHAR(100), name VARCHAR(200), country VARCHAR(200))";
            String insert_row_sql1 = "INSERT INTO customer(id, name, country) VALUES('001', 'Charitha', 'Sri lanka') ";
            String insert_row_sql2 = "INSERT INTO customer(id, name, country) VALUES('002', 'Krishantha', 'India') ";

            stmt.executeUpdate(create_table_sql);
            stmt.executeUpdate(insert_row_sql1);
            stmt.executeUpdate(insert_row_sql2);

        } catch (SQLException e) {
        }
    }

    public void newDataService(String serviceName) throws IOException {

        browser.open(loadProperties().getProperty("context.root") + "/carbon/admin/index.jsp?loginStatus=true");
        assertTrue(browser.isTextPresent("Data Service"));
        browser.click("link=Create");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Create Data Service"));
        assertTrue(browser.isElementPresent("//table[@id='dataSources']/thead/tr/th"));
        browser.type("serviceName", serviceName);
        browser.type("description", "This is a test data service");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Data Sources"));

    }

    public void addNewDataSource(String dataSourceName, String dataSourceType) throws IOException, InterruptedException {
        if (browser.isTextPresent("Data Sources")) {
            browser.open(loadProperties().getProperty("context.root") + "/carbon/ds/dataSources.jsp");
            assertEquals("Add New Data Source", browser.getText("link=Add New Data Source"));
            browser.click("link=Add New Data Source");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Add New Data Source"));
            verifyTrue(browser.isTextPresent("DataSource Id*"));
            verifyTrue(browser.isTextPresent("Data Source Type*"));
            browser.type("datasourceId", dataSourceName);
            if (dataSourceType.equals("RDBMS")) {
                browser.select("datasourceType", "label=RDBMS");
                browser.waitForPageToLoad("30000");
                verifyTrue(browser.isTextPresent("Database Engine*"));
                verifyTrue(browser.isTextPresent("Driver Class"));
                verifyTrue(browser.isTextPresent("JDBC URL"));
                verifyTrue(browser.isTextPresent("User Name"));
                verifyTrue(browser.isTextPresent("Password"));
                verifyTrue(browser.isTextPresent("Min. Pool Size"));
                verifyTrue(browser.isTextPresent("Max. Pool Size"));
                verifyTrue(browser.isTextPresent("AutoCommit"));

                browser.select("databaseEngine", "label=MySQL");
                assertEquals("com.mysql.jdbc.Driver", browser.getValue("org.wso2.ws.dataservice.driver"));
                assertEquals("jdbc:mysql://[machine-name/ip]:[port]/[database-name]", browser.getValue("org.wso2.ws.dataservice.protocol"));
                browser.type("org.wso2.ws.dataservice.protocol", "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName);
                browser.type("org.wso2.ws.dataservice.user", DBUserName);
                browser.type("org.wso2.ws.dataservice.password", DBPassword);
                browser.click("//input[@value='Test Connection']");
                assertEquals("Database connection is successfull with driver class com.mysql.jdbc.Driver , jdbc url jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName + " and user name " + DBUserName, browser.getText("//div[@id='messagebox-info']/p"));
                Thread.sleep(1000);
                browser.click("//button[@type='button']");
                browser.click("//input[@value='Save']");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent(dataSourceName));
                assertEquals("Edit Datasource", browser.getText("link=Edit Datasource"));
                verifyEquals("Delete Datasource", browser.getText("link=Delete Datasource"));

                browser.click("//input[@value='Next >']");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent("Queries"));


            } else {
                //logout UI
            }

        }
    }


    public void addNewCSVMySQLDataSource(String CSVdatasourceId, String CSVpath, String columns, String columnordinal, int startingrow, String dataSourceName, String dataSourceType) throws Exception {
        File resourcePath = new File("." + File.separator + "lib" + File.separator + CSVpath);

        assertTrue(browser.isTextPresent("Data Sources"));
        assertTrue(browser.isElementPresent("//input[@value='Next >']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.click("link=Add New Data Source");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isElementPresent("//input[@value='Save']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.type("datasourceId", CSVdatasourceId);
        browser.select("datasourceType", "label=CSV");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.type("csv_datasource", resourcePath.getCanonicalPath());
        browser.type("csv_columns", columns);
        browser.type("csv_columnordinal", columnordinal);
        browser.type("csv_startingrow", Integer.toString(startingrow));
        browser.select("csv_hasheader", "label=true");


        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

        browser.open(loadProperties().getProperty("context.root") + "/carbon/ds/dataSources.jsp");
        assertEquals("Add New Data Source", browser.getText("link=Add New Data Source"));
        browser.click("link=Add New Data Source");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Data Source"));
        verifyTrue(browser.isTextPresent("DataSource Id*"));
        verifyTrue(browser.isTextPresent("Data Source Type*"));
        browser.type("datasourceId", dataSourceName);
        if (dataSourceType.equals("RDBMS")) {
            browser.select("datasourceType", "label=RDBMS");
            browser.waitForPageToLoad("30000");
            verifyTrue(browser.isTextPresent("Database Engine*"));
            verifyTrue(browser.isTextPresent("Driver Class"));
            verifyTrue(browser.isTextPresent("JDBC URL"));
            verifyTrue(browser.isTextPresent("User Name"));
            verifyTrue(browser.isTextPresent("Password"));
            verifyTrue(browser.isTextPresent("Min. Pool Size"));
            verifyTrue(browser.isTextPresent("Max. Pool Size"));
            verifyTrue(browser.isTextPresent("AutoCommit"));

            browser.select("databaseEngine", "label=MySQL");
            assertEquals("com.mysql.jdbc.Driver", browser.getValue("org.wso2.ws.dataservice.driver"));
            assertEquals("jdbc:mysql://[machine-name/ip]:[port]/[database-name]", browser.getValue("org.wso2.ws.dataservice.protocol"));
            browser.type("org.wso2.ws.dataservice.protocol", "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName);
            browser.type("org.wso2.ws.dataservice.user", DBUserName);
            //browser.type("org.wso2.ws.dataservice.password", "");
            browser.click("//input[@value='Test Connection']");
            assertEquals("Database connection is successfull with driver class com.mysql.jdbc.Driver , jdbc url jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName + " and user name " + DBUserName, browser.getText("//div[@id='messagebox-info']/p"));
            browser.click("//button[@type='button']");
            browser.click("//input[@value='Save']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent(dataSourceName));
            assertEquals("Edit Datasource", browser.getText("link=Edit Datasource"));
            verifyEquals("Delete Datasource", browser.getText("link=Delete Datasource"));
            browser.click("//input[@value='Next >']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Queries"));


        }

    }

    public void addNewQuery(String queryName, String dataSourceName, String sqlstmt) throws IOException {
        browser.open(loadProperties().getProperty("context.root") + "/carbon/ds/queries.jsp");
        assertEquals("Add New Query", browser.getText("link=Add New Query"));
        browser.click("link=Add New Query");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Query"));
        verifyTrue(browser.isTextPresent("Query ID*"));
        verifyTrue(browser.isTextPresent("Data Source*"));
        assertTrue(browser.isTextPresent("Currently there are no output mappings present for this query"));
        verifyTrue(browser.isTextPresent("Grouped by element"));
        verifyTrue(browser.isTextPresent("Row name"));
        verifyTrue(browser.isTextPresent("Row namespace"));
        browser.type("queryId", queryName);
        browser.select("datasource", "label=" + dataSourceName);
        browser.type("sql", sqlstmt);
        browser.type("txtDataServiceWrapElement", "customers");
        browser.type("txtDataServiceRowName", "customer");
        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add/Edit Output Mapping"));
        verifyTrue(browser.isTextPresent("Mapping Type*"));
        verifyTrue(browser.isTextPresent("Output field name"));
        browser.type("txtDataServiceOMColumnName", "id");
        verifyTrue(browser.isTextPresent("Data source column Name"));
        browser.select("cmbDataServiceOMType", "label=element");
        browser.type("txtDataServiceOMElementName", "id");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=attribute");
        browser.type("txtDataServiceOMElementName", "name");
        browser.type("txtDataServiceOMColumnName", "name");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=element");
        browser.type("txtDataServiceOMElementName", "country");
        browser.type("txtDataServiceOMColumnName", "country");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        verifyTrue(browser.isTextPresent("Do you want to go back to the main configuration?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        // assertEquals(queryName, browser.getTable("query-table.1.0"));


    }

    public void addNewQueryWithInput(String queryId, String datasource, String sql, String inputMappingNameId, String inputMappingSqlTypeId, String DataServiceWrapElement, String DataServiceRowName) throws Exception {
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
        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add/Edit Output Mapping"));
        verifyTrue(browser.isTextPresent("Mapping Type*"));
        verifyTrue(browser.isTextPresent("Output field name"));
        browser.type("txtDataServiceOMColumnName", "id");
        verifyTrue(browser.isTextPresent("Data source column Name"));
        browser.select("cmbDataServiceOMType", "label=element");
        browser.type("txtDataServiceOMElementName", "id");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=attribute");
        browser.type("txtDataServiceOMElementName", "name");
        browser.type("txtDataServiceOMColumnName", "name");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=element");
        browser.type("txtDataServiceOMElementName", "country");
        browser.type("txtDataServiceOMColumnName", "country");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        verifyTrue(browser.isElementPresent("//table[@id='existingInputMappingsTable']/tbody/tr/th[1]"));
        verifyTrue(browser.isTextPresent("Do you want to go back to the main configuration?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
    }

    public void addNewOperation(String queryName, String operationName) throws IOException {

        browser.open(loadProperties().getProperty("context.root") + "/carbon/ds/queries.jsp");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Operations"));
        assertEquals("Add New Operation", browser.getText("link=Add New Operation"));
        browser.click("link=Add New Operation");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Operation"));
        verifyTrue(browser.isTextPresent("Operation Name*"));
        verifyTrue(browser.isTextPresent("Query ID*"));
        browser.type("operationName", operationName);
        browser.select("queryId", "label=" + queryName);
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        assertEquals(operationName, browser.getTable("operation-table.2.0"));
        assertEquals("Edit Operation", browser.getText("link=Edit Operation"));
        assertEquals("Delete Operation", browser.getText("link=Delete Operation"));
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");


    }

    public String tryRDBMSDataService(String ServiceName, String operationName, String SoapAction, String namespace) throws Exception {
        RDBMSDataServiceClient RDBMSClient = new RDBMSDataServiceClient();

        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + ServiceName;
        String result = RDBMSClient.RDBMSClient(serviceepr, operationName, SoapAction, namespace);
        return result;


    }

    public String tryRDBMSDataServiceWithInput(String ServiceName, String operationName, String SoapAction, String namespace, String param, String input) throws Exception {
        RDBMSDataServiceClient RDBMSClient = new RDBMSDataServiceClient();

        String serviceepr = "http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + ServiceName;
        String result = RDBMSClient.twoWayAnonClient(serviceepr, operationName, SoapAction, namespace, param, input);
        return result;


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
            // stmt.executeUpdate("DROP TABLE customer");
            stmt.executeUpdate("DROP DATABASE  " + DBName);
        } catch (SQLException e) {
            System.out.println(e);
        }


    }

    public void updateData(String oldName, String newName) {
        try {
            Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            Statement stmt = connect.createStatement();
            String create_table_sql = "UPDATE customer SET name='" + newName + "' WHERE name='" + oldName + "'";

            stmt.executeUpdate(create_table_sql);


        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void addStoredProcedure(String sqlStatement) {
        try {
            Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(sqlStatement);


        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public void checkCachingForRDBMSService(String ServiceEpr, String operationName, String SoapAction, String Namespace) throws Exception {
        RDBMSDataServiceClient instCachingClient = new RDBMSDataServiceClient();
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        if ((browser.getSelectedValue("enable")).equals("Yes")) {
            browser.select("enable", "label=No");
        }

        browser.select("enable", "label=Yes");
        browser.type("timeoutField", "30000");
        browser.click("Submit");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(500);
        String sCachData = instCachingClient.RDBMSClient(ServiceEpr, operationName, SoapAction, Namespace);

        updateData("Charitha", "Jayani");
        for (int i = 0; i <= 5; i++) {
            String sCachingNow = instCachingClient.RDBMSClient(ServiceEpr, operationName, SoapAction, Namespace);

            if (sCachData != null && !sCachData.equals(sCachingNow)) {
                System.out.println("Caching Not Done..");
                updateData("Jayani", "Charitha");
                browser.click("link=Response Caching");
                browser.waitForPageToLoad("30000");
                browser.select("enable", "label=No");
                browser.click("//button[@type='button']");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
                browser.click("//button[@type='button']");
                assertTrue(browser.isTextPresent("caching false"));
            } else if (sCachData != null && sCachData.equals(sCachingNow)) {
                System.out.println("Caching Done..");
            }
        }
        Thread.sleep(40000);

        String sCachingCurrent = instCachingClient.RDBMSClient(ServiceEpr, operationName, SoapAction, Namespace);
        if (sCachData != null && !sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Done..");
        } else if (sCachData != null && sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Not Done..");
            updateData("Jayani", "Charitha");
            browser.click("link=Response Caching");
            browser.waitForPageToLoad("30000");
            browser.select("enable", "label=No");
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
            browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent("caching false"));

        }
        updateData("Jayani", "Charitha");
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
    }


    public void addQueryName(String queryName, String dataSourceName, String sqlstmt) throws Exception {
        browser.click("link=Add New Query");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Query"));
        verifyTrue(browser.isTextPresent("Query ID*"));
        verifyTrue(browser.isTextPresent("Data Source*"));
        assertTrue(browser.isTextPresent("Currently there are no output mappings present for this query"));
        verifyTrue(browser.isTextPresent("Grouped by element"));
        verifyTrue(browser.isTextPresent("Row name"));
        verifyTrue(browser.isTextPresent("Row namespace"));
        browser.type("queryId", queryName);
        browser.select("datasource", "label=" + dataSourceName);
        browser.type("sql", sqlstmt);
    }

    public void addNewInputMapping(String inputMappingNameId, String inputMappingSqlTypeId) throws Exception {
        browser.click("newInputMapping");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Add Input Mapping"));
        browser.type("inputMappingNameId", inputMappingNameId);
        browser.select("inputMappingSqlTypeId", "label=" + inputMappingSqlTypeId);
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
    }

    public void addResult(String DataServiceWrapElement, String DataServiceRowName, String RowNamespace) throws Exception {
        verifyTrue(browser.isElementPresent("//form[@id='dataForm']/table/tbody/tr[1]/td/table/tbody/tr[6]/td/table/tbody/tr[1]/td/label/b"));
        browser.type("txtDataServiceWrapElement", DataServiceWrapElement);
        browser.type("txtDataServiceRowName", DataServiceRowName);
        browser.type("txtDataServiceRowNamespace", RowNamespace);
    }

    public void addNewOutputMapping(String mappingType, String feildName, String columnName, String queryId) throws Exception {
        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add/Edit Output Mapping"));
        browser.select("cmbDataServiceOMType", "label=" + mappingType);

        if (browser.getSelectedValue("cmbDataServiceOMType").equals("element") || browser.getSelectedValue("cmbDataServiceOMType").equals("attribute")) {
            browser.type("txtDataServiceOMElementName", feildName);
            browser.type("txtDataServiceOMColumnName", columnName);
            browser.click("//input[@value='Add']");
        } else if (browser.getSelectedValue("cmbDataServiceOMType").equals("query")) {
            browser.select("cmbDataServiceQueryId", "label=" + queryId);
            browser.click("//input[@value='Save']");
        }

        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
    }

    public void saveQuery() throws Exception {
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
    }

    public void addOperation(String operationName, String queryName) throws Exception {
        browser.click("link=Add New Operation");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Operation"));
        verifyTrue(browser.isTextPresent("Operation Name*"));
        verifyTrue(browser.isTextPresent("Query ID*"));
        browser.type("operationName", operationName);
        browser.select("queryId", "label=" + queryName);
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(operationName));
        assertEquals("Edit Operation", browser.getText("link=Edit Operation"));
        assertEquals("Delete Operation", browser.getText("link=Delete Operation"));
    }

    public void invokeRestFulService(String serviceName, String productCode) throws Exception {
        InstanceCreationSetup browser2 = new InstanceCreationSetup("browser2");
        String strURL = "http://localhost:" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName + ".HTTPEndpoint/product/" + productCode;
        browser2.openBrowser(strURL, productCode);
    }
}
