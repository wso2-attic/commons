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

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.GenericServiceClient;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.CachingClient;


public class CSVDataService extends SeleneseTestCase {
    Selenium browser;
    Properties property;


    public CSVDataService(Selenium _browser) {
        browser = _browser;
        property = BrowserInitializer.getProperties();
    }


    public void newDataService(String serviceName, String description) throws Exception {
        browser.click("link=Create");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Create Data Service "));
        assertTrue(browser.isElementPresent("//table[@id='dataSources']/thead/tr/th"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));

        //check for no serviceName
        browser.click("//input[@value='Next >']");
        assertEquals("Data Service Name is mandatory", browser.getText("//div[@id='messagebox-warning']/p"));
		browser.click("//button[@type='button']");

        //give a service name
        browser.type("serviceName", serviceName);
        browser.type("description", description);

        // String name=browser.getText("serviceName");
        browser.click("//input[@value='Next >']");

//        if(name.equals("")){
//            assertTrue(browser.isTextPresent("Data Service Name is mandatory"));
//             browser.click("//button[@type='button']");
//		     browser.click("//input[@value='Cancel']");
//        }
        browser.waitForPageToLoad("30000");

    }

    public void newCSVDataSource(String datasourceId, String path, String columns, String columnordinal, int startingrow) throws Exception {
        File resourcePath = new File("." + File.separator + "lib" + File.separator + path);

        assertTrue(browser.isTextPresent("Data Sources"));
        assertTrue(browser.isElementPresent("//input[@value='Next >']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.click("link=Add New Data Source");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Save']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));

        //checks for no datasourceId
        browser.click("//input[@value='Save']");
		assertEquals("Data Source Id is mandatory", browser.getText("//div[@id='messagebox-warning']/p"));
		browser.click("//button[@type='button']");

        //checks for no datasourceType
        browser.type("datasourceId", datasourceId);
        browser.click("//input[@value='Save']");
		assertEquals("Select the data source type", browser.getText("//div[@id='messagebox-warning']/p"));
		browser.click("//button[@type='button']");

        //give  datasourceId & datasourcetype
        browser.type("datasourceId", datasourceId);
        browser.select("datasourceType", "label=CSV");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.type("csv_datasource", resourcePath.getCanonicalPath());
        browser.type("csv_columns", columns);
        browser.type("csv_columnordinal", columnordinal);
        browser.type("csv_startingrow", Integer.toString(startingrow));

        //check without csv_hasheader
        browser.click("//input[@value='Save']");
		assertEquals("Contains Column Header Row is mandatory", browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give the csv_hasheader
        browser.select("csv_hasheader", "label=false");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Data Sources"));
        assertTrue(browser.isElementPresent("//table[@id='datasource-table']/thead/tr/th[1]"));
        assertTrue(browser.isElementPresent("link=Edit Datasource"));
        assertTrue(browser.isElementPresent("link=Delete Datasource"));
        assertTrue(browser.isElementPresent("link=Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        assertTrue(browser.isElementPresent("//input[@value='Finish']"));

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

    }

    public void newEXCELDataSource (String datasourceId,String path) throws Exception{
        File resourcePath = new File("." + File.separator + "lib" + File.separator + path);

        assertTrue(browser.isTextPresent("Data Sources"));
        assertTrue(browser.isElementPresent("//input[@value='Next >']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.click("link=Add New Data Source");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Save']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));

        //check for no datasourceId
        browser.click("//input[@value='Save']");
		assertEquals("Data Source Id is mandatory", browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

         //checks for no datasourceType
        browser.type("datasourceId", datasourceId);
        browser.click("//input[@value='Save']");
		assertEquals("Select the data source type", browser.getText("//div[@id='messagebox-warning']/p"));
		browser.click("//button[@type='button']");

        //give datasourceType and Id
        browser.type("datasourceId", datasourceId);
        browser.select("datasourceType",  "label=EXCEL");
        browser.waitForPageToLoad("30000");

        //check for no location
       	browser.click("//input[@value='Save']");
		assertEquals("Excel File Location is mandatory",  browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give the location
        browser.type("excel_datasource",  resourcePath.getCanonicalPath());
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Data Sources"));
        assertTrue(browser.isElementPresent("//table[@id='datasource-table']/thead/tr/th[1]"));

        assertTrue(browser.isElementPresent("link=Edit Datasource"));
        assertTrue(browser.isElementPresent("link=Delete Datasource"));
        assertTrue(browser.isElementPresent("link=Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        assertTrue(browser.isElementPresent("//input[@value='Finish']"));

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

    }

    public void newCSVEXCELDataSource(String CSVdatasourceId, String CSVpath, String columns, String columnordinal, int startingrow,String EXCELdatasourceId,String EXCELpath) throws Exception{
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

        resourcePath = new File("." + File.separator + "lib" + File.separator + EXCELpath);

        browser.click("link=Add New Data Source");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Data Source "));
        assertTrue(browser.isElementPresent("//input[@value='Save']"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        browser.type("datasourceId", EXCELdatasourceId);
        browser.select("datasourceType",  "label=EXCEL");
        browser.waitForPageToLoad("30000");
        browser.type("excel_datasource",  resourcePath.getCanonicalPath());
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Data Sources"));
        assertTrue(browser.isTextPresent("actions"));
        assertTrue(browser.isElementPresent("link=Edit Datasource"));
        assertTrue(browser.isElementPresent("link=Delete Datasource"));
        assertTrue(browser.isElementPresent("link=Add New Data Source"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        assertTrue(browser.isElementPresent("//input[@value='Finish']"));

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");


    }



    public void addCSVQuery(String queryId, String datasource, String wrapElement, String rowName) throws Exception {
        browser.click("link=Add New Query");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Query"));
        verifyTrue(browser.isElementPresent("//form[@id='dataForm']/table/tbody/tr[1]/td/table/tbody/tr[7]/td/table/tbody/tr[1]/td/label/b"));

        //check  no queryId
        browser.click("//input[@value='Save']");
		assertEquals("Query Id is mandatory", browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give queryId
        browser.type("queryId", queryId);

        //check no datasource
        browser.click("//input[@value='Save']");
		assertEquals("Select the data source",  browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give  datasource
        browser.select("datasource", "label=" + datasource);

        browser.type("txtDataServiceWrapElement", wrapElement);
        browser.type("txtDataServiceRowName", rowName);

        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");
    }

    public void addEXCELQuery(String queryId,String dataSource,String workbookName,int StartingRow,int MaxRowCount,String WrapElement,String RowName) throws Exception{
        browser.click("link=Add New Query");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Add New Query"));
        verifyTrue(browser.isElementPresent("//form[@id='dataForm']/table/tbody/tr[1]/td/table/tbody/tr[7]/td/table/tbody/tr[1]/td/label/b"));

        //check fo no queryId
        browser.click("//input[@value='Save']");
		assertEquals("Query Id is mandatory",  browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give queryId
        browser.type("queryId", queryId);

        //check for no datasource
        browser.click("//input[@value='Save']");
		assertEquals("Select the data source",  browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give data source
        browser.select("datasource", "label="+dataSource);
        browser.type("txtExcelWorkbookName", workbookName);
        browser.type("txtExcelStartingRow", Integer.toString( StartingRow));
        browser.type("txtExcelMaxRowCount", Integer.toString( MaxRowCount));
        browser.select("txtExcelHeaderColumns", "label=true");
        browser.type("txtDataServiceWrapElement",WrapElement);
        browser.type("txtDataServiceRowName", RowName);
        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");

    }

    public void addOutputMapping(String mappingType, String feildName,String columnName) throws Exception {
        assertTrue(browser.isTextPresent("Add/Edit Output Mapping"));
        browser.select("cmbDataServiceOMType", "label=" + mappingType);
        browser.type("txtDataServiceOMElementName", feildName);
        browser.type("txtDataServiceOMColumnName", columnName);
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
    }

    public void gotoMainConfiguration() throws Exception {
        browser.click("//input[@value='Main Configuration']");
        assertTrue(browser.isTextPresent("Do you want to go back to the main configuration?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Edit Query"));
        assertTrue(browser.isElementPresent("//form[@id='dataForm']/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]"));
        assertTrue(browser.isElementPresent("//form[@id='dataForm']/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[1]"));
        assertTrue(browser.isElementPresent("//table[@id='existingOutputMappingsTable']/tbody/tr[1]/td[1]/b"));
        assertTrue(browser.isElementPresent("//input[@value='Cancel']"));
        assertTrue(browser.isElementPresent("newOutputMapping"));
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

    }

    public void addOperation(String operationName, String queryId) throws Exception {
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Operations"));
        browser.click("link=Add New Operation");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Operation"));

        //check for no operation name
        browser.click("//input[@value='Save']");
		assertEquals("Operation name is mandatory", browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give operation name
        browser.type("operationName", operationName);

        //check for no queryId
        browser.click("//input[@value='Save']");
		assertEquals("Select the query Id",  browser.getText("messagebox-warning"));
		browser.click("//button[@type='button']");

        //give query Id
        browser.select("queryId", "label=" + queryId);
        browser.waitForPageToLoad("30000");

        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

    }

    public void addTwoOperations(String operationName1, String queryId1,String operationName2, String queryId2)throws Exception {
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Operations"));
        browser.click("link=Add New Operation");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Operation"));
        browser.type("operationName", operationName1);
        browser.select("queryId", "label=" + queryId1);
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        browser.click("link=Add New Operation");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New Operation"));
        browser.type("operationName", operationName2);
        browser.select("queryId", "label=" + queryId2);
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

    }

    public void checkService(String serviceName) throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(serviceName));

        instServiceManagement.accessServiceDashboard(serviceName);
        assertTrue(browser.isElementPresent("//table[@id='serviceOperationsTable']/tbody/tr[8]/td/strong"));
        browser.click("link=Edit Data Service (Wizard)");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(" Edit Data Service(" + serviceName + ")"));
        browser.click("//input[@value='Cancel']");
        browser.waitForPageToLoad("30000");

        instServiceManagement.accessServiceDashboard(serviceName);
        browser.click("link=Edit Data Service (XML Edit)");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(" Data Service XML Editor(" + serviceName + ")"));
        browser.click("cancel");
        browser.waitForPageToLoad("30000");
    }

    public String tryCSVDataService(String ServiceName, String operationName, String SoapAction, String namespace) throws Exception {
        CSVDataServiceClient CSVClient = new CSVDataServiceClient();

        String serviceepr = "http://" +property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + ServiceName;
        String result = CSVClient.CSVClient(serviceepr, operationName, SoapAction, namespace);
        return result;


    }

    public String tryEXCELDataService(String ServiceName, String operationName, String SoapAction, String namespace) throws Exception {
        EXCELDataServiceClient EXCELClient = new EXCELDataServiceClient();

        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + ServiceName;
        String result = EXCELClient.EXCELClient(serviceepr, operationName, SoapAction, namespace);
        return result;


    }

    public void checkCaching(String ServiceEpr, String operationName, String SoapAction, String Namespace) throws Exception  {
        CachingClient instCachingClient = new CachingClient();
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
        String sCachData = instCachingClient.cachClient(ServiceEpr, operationName, SoapAction, Namespace);
        for (int i = 0; i <= 5; i++) {
            String sCachingNow = instCachingClient.cachClient(ServiceEpr, operationName, SoapAction, Namespace);

            if (sCachData != null && !sCachData.equals(sCachingNow)) {
                System.out.println("Caching Not Done..");
                browser.click("link=Response Caching");
                browser.waitForPageToLoad("30000");
                browser.select("enable", "label=No");
                browser.click("//button[@type='button']");
                browser.waitForPageToLoad("30000");
                assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
                browser.click("//button[@type='button']");
                assertTrue(browser.isTextPresent("Caching Done.."));
            } else if (sCachData != null && sCachData.equals(sCachingNow)) {
                System.out.println("Caching Done..");
            }
        }
        Thread.sleep(40000);

        String sCachingCurrent = instCachingClient.cachClient(ServiceEpr, operationName, SoapAction, Namespace);
        if (sCachData != null && !sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Done..");
        } else if (sCachData != null && sCachData.equals(sCachingCurrent)) {
            System.out.println("Caching Not Done..");
            browser.click("link=Response Caching");
            browser.waitForPageToLoad("30000");
            browser.select("enable", "label=No");
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
            browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent("caching false"));

        }
        browser.click("link=Response Caching");
        browser.waitForPageToLoad("30000");
        browser.select("enable", "label=No");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
    }

}