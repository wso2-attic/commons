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

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class  RDBMSDataService extends SeleneseTestCase {

    Properties property;
    Selenium browser;
    Connection connection = null;
    String DBName = "data_service_test";
    String hostname;
    String DBPort;
    String driverName;
    String DBUserName;
    String DBPassword;
    String dburl;
    String contextroot;
    String username;
    String clientIp;
    String clientpwd;

    public RDBMSDataService(Selenium _browser) {
        browser = _browser;
        property = BrowserInitializer.getProperties();
        contextroot=property.getProperty("context.root");
        try {
            hostname = property.getProperty("mysql.hostname");
            DBPort = property.getProperty("mysql.DBPort");
            driverName = property.getProperty("mysql.driverName");
            DBUserName = property.getProperty("mysql.DBUserName");
            DBPassword = property.getProperty("mysql.DBPassword");
            username = property.getProperty("mysql.username");
            clientIp = property.getProperty("mysql.clientIp");
            clientpwd = property.getProperty("mysql.clientpwd");
            dburl = "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
           // String grant_permission="GRANT ALL ON " + DBName + ".* TO " + username + "@" + clientIp + " IDENTIFIED BY '" + clientpwd + "'";
            Statement stmt = connection.createStatement();
            stmt.execute(check_database);
            stmt.execute(create_database);
           // stmt.execute(grant_permission);
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

        //browser.open(contextroot + "/carbon/admin/index.jsp?loginStatus=true");
        assertTrue(browser.isTextPresent("Data Service"));
        browser.click("link=Create");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Create Data Service"));
        assertTrue(browser.isElementPresent("//table[@id='dataSources']/thead/tr/th"));

        //check for no data service name
        browser.click("//input[@value='Next >']");
        assertEquals("Data Service Name is mandatory", browser.getText("messagebox-warning"));
        browser.click("//button[@type='button']");

        //give service name
        browser.type("serviceName", serviceName);
        browser.type("description", "This is a test data service");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Data Sources"));

    }

    public void addNewDataSource(String dataSourceName, String dataSourceType) throws IOException, InterruptedException {
        if (browser.isTextPresent("Data Sources")) {
            // browser.open(contextroot + "/carbon/ds/dataSources.jsp");
            assertEquals("Add New Data Source", browser.getText("link=Add New Data Source"));
            browser.click("link=Add New Data Source");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Add New Data Source"));
            verifyTrue(browser.isTextPresent("DataSource Id*"));
            verifyTrue(browser.isTextPresent("Data Source Type*"));

            //check for no datasourceId
            browser.click("//input[@value='Save']");
            verifyEquals("Data Source Id is mandatory", browser.getText("messagebox-warning"));
            browser.click("//button[@type='button']");

            //give datasourceId
            browser.type("datasourceId", dataSourceName);

            if (dataSourceType.equals("RDBMS")) {
                //check for no daatsourceType
                browser.click("//input[@value='Save']");
                assertEquals("Select the data source type", browser.getText("messagebox-warning"));
                browser.click("//button[@type='button']");

                //give datasourceType
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

//                //check for no database engine
//                browser.click("//input[@value='Save']");
//                assertEquals("Select the Database engine", browser.getText("messagebox-warning"));
//                browser.click("//button[@type='button']");

                //give database engi
                browser.select("databaseEngine", "label=MySQL");
                assertEquals("com.mysql.jdbc.Driver", browser.getValue("org.wso2.ws.dataservice.driver"));
                assertEquals("jdbc:mysql://[machine-name/ip]:[port]/[database-name]", browser.getValue("org.wso2.ws.dataservice.protocol"));
                browser.type("org.wso2.ws.dataservice.protocol", "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName);
                browser.type("org.wso2.ws.dataservice.user", DBUserName);
                browser.type("org.wso2.ws.dataservice.password", DBPassword);
                browser.click("//input[@value='Test Connection']");
                Thread.sleep(1000);
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
        browser.select("csv_hasheader", "label=false");


        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

        // browser.open(contextroot + "/carbon/ds/dataSources.jsp");
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

    public String tryRDBMSDataService(String ServiceName, String operationName, String SoapAction, String namespace) throws Exception {
        RDBMSDataServiceClient RDBMSClient = new RDBMSDataServiceClient();

        String serviceepr = "http://" +property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + ServiceName;
        String result = RDBMSClient.RDBMSClient(serviceepr, operationName, SoapAction, namespace);
        return result;       


    }

    public String tryRDBMSDataServiceWithInput(String ServiceName, String operationName, String SoapAction, String namespace, String param, String input) throws Exception {
        RDBMSDataServiceClient RDBMSClient = new RDBMSDataServiceClient();

        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + ServiceName;
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

        //check  no queryId
        browser.click("//input[@value='Save']");
        assertEquals("Query Id is mandatory", browser.getText("messagebox-warning"));
        browser.click("//button[@type='button']");

        //give queryId
        browser.type("queryId", queryName);

        //check no datasource
        browser.click("//input[@value='Save']");
        assertEquals("Select the data source",  browser.getText("messagebox-warning"));
        browser.click("//button[@type='button']");

        //give  datasource
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
            Thread.sleep(1000);
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
        String strURL = "http://localhost:" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + serviceName + ".HTTPEndpoint/product/" + productCode;
         Selenium newBrowser=browser2.openBrowser(strURL);

        URL url=new URL(strURL);
        URLConnection ucon=url.openConnection();
        StringBuffer sb=new StringBuffer();
        String data="";
        InputStream is=ucon.getInputStream();
        int ch;
        while((ch=is.read()) != -1){
            sb.append((char)ch);
        }

        data=sb.toString();
        System.out.println(data);
        if(data.contains(productCode)){
            System.out.println("Element found");
        }else{
            System.out.println("Element not found");
            is.close();
            assertTrue(browser.isTextPresent("Element found"));
        }


        // System.out.println(data);
        is.close();
        Thread.sleep(10000);
        newBrowser.stop();


    }

    public void accessEditFaultyService(String serviceName) throws Exception{
           browser.click("link=List");
           browser.waitForPageToLoad("30000");
           assertTrue(browser.isTextPresent("Faulty Service Group(s)."));
           browser.click("//div[@id='workArea']/form[1]/table/tbody/tr[1]/td/nobr/u/a/font");
           browser.waitForPageToLoad("30000");

           assertTrue(browser.isTextPresent("Faulty Service Group(s)."));
           assertEquals("Delete", browser.getText("delete1"));
           assertEquals("Delete", browser.getText("delete2"));
           assertTrue(browser.isTextPresent("Select all in this page"));
           assertTrue(browser.isTextPresent("Select none"));

           int i=1;
           String pageTitle;
           String[] tempString = null;

           String newPageTitle = "";


           while (browser.isElementPresent("//div[@id='workArea']/form/table/tbody/tr[" + Integer.toString(i) + "]/td[2]") && !newPageTitle.equals(serviceName)) {
               pageTitle = browser.getText("//div[@id='workArea']/form/table/tbody/tr[" + Integer.toString(i) + "]/td[2]") ;
               tempString = pageTitle.split("\n");
               System.out.println(tempString[0]);
               newPageTitle = tempString[0];
               if (serviceName.equals(newPageTitle)) {
                   browser.click("//div[@id='workArea']/form/table/tbody/tr[" + Integer.toString(i) + "]/td[3]/nobr/a");
                   browser.waitForPageToLoad("30000");
                   assertTrue(browser.isTextPresent("Data Service XML Editor("));
               }

               i = i + 1;
           }

           Thread.sleep(1000);
       }

}
