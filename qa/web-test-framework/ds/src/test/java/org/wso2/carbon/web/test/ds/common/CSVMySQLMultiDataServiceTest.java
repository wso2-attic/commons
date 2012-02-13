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

package org.wso2.carbon.web.test.ds.common;


import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.apache.axiom.om.OMElement;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;

import javax.xml.namespace.QName;

public class CSVMySQLMultiDataServiceTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;

    public CSVMySQLMultiDataServiceTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }

    public void testCreatCSVMySQLMultiDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.loginToUI(username, password);

        rdbmsDataService.setUpDataBase();
        csvDataService.newDataService("CSVMySQLMultiDataService" , "Sample data service");
        rdbmsDataService.addNewCSVMySQLDataSource("CSVDataSource", "DataServiceCSVResource.csv", "id,name,address", "1,2,3", 2,"MySQLDataSource","RDBMS");
        csvDataService.addCSVQuery("q1", "CSVDataSource", "customers", "customer");
        csvDataService.addOutputMapping("element", "id", "1");
        csvDataService.addOutputMapping("element", "name", "2");
        csvDataService.addOutputMapping("attribute", "address","3" );
        csvDataService.gotoMainConfiguration();

        rdbmsDataService.addQueryName("customers-query","MySQLDataSource","select * from customer");
        rdbmsDataService.addResult("customers","customer","");
        rdbmsDataService.addNewOutputMapping("element","id","id","");
        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
        rdbmsDataService.addNewOutputMapping("element","country","country","");
        rdbmsDataService.saveQuery();

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("getAllCustomers","q1");
        rdbmsDataService.addOperation("getCustomers","customers-query");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("CSVMySQLMultiDataService");

    }

    public void testInvokeServiceMySQL() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "CSVMySQLMultiDataService";
        String csvExpectedResult = "Charitha";

        String result = rdbmsDataService.tryRDBMSDataService(serviceName, "getCustomers", "urn:getCustomers", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
        //rdbmsDataService.cleanDataBase();

    }


    public void testInvokeServiceCSV() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName = "CSVMySQLMultiDataService";
        String csvExpectedResult = "Boston";

        String result = csvDataService.tryCSVDataService(serviceName, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);
    }

    public void testRemoveCSVMySQLServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("CSVMySQLMultiDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testLogoutUi() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }
}
