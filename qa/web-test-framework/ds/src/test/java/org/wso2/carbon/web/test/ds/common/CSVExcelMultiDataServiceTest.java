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

public class CSVExcelMultiDataServiceTest extends TestCase{

    Selenium browser;
    Properties property;
    String username;
    String password;

    public CSVExcelMultiDataServiceTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
         property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    public void testCreatCSVExcelMultiDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);

        seleniumTestBase.loginToUI(username, password);

        csvDataService.newDataService("CSVExcelMultiDataService" , "Sample data service");
        csvDataService.newCSVEXCELDataSource("CSVDataSource", "DataServiceCSVResource.csv", "id,name,address", "1,2,3", 2,"EXCELDataSourse","DataServiceExcelResource.xls");
        csvDataService.addCSVQuery("q1", "CSVDataSource", "customers", "customer");
        csvDataService.addOutputMapping("element", "id", "1");
        csvDataService.addOutputMapping("element", "name", "2");
        csvDataService.addOutputMapping("attribute", "address","3" );
        csvDataService.gotoMainConfiguration();
        csvDataService.addEXCELQuery("q2","EXCELDataSourse","Sheet1",2,100,"students", "student");
        csvDataService.addOutputMapping("element","student_id","Student_id");
        csvDataService.addOutputMapping("attribute","name","Name");
        csvDataService.addOutputMapping("element","grade","Grade");
        csvDataService.gotoMainConfiguration();
        csvDataService.addTwoOperations("getAllCustomers","q1","getAllStudents","q2");
        csvDataService.checkService("CSVExcelMultiDataService");

    }

    public void testInvokeServiceEXCEL() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName ="CSVExcelMultiDataService";
        String csvExpectedResult = "Mackie";

        String result = csvDataService.tryEXCELDataService(serviceName, "getAllStudents", "urn:getAllStudents", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);


    }

    public void testInvokeServiceCSV() throws Exception{
        CSVDataService csvDataService = new CSVDataService(browser);

        String serviceName = "CSVExcelMultiDataService";
        String csvExpectedResult = "Boston";

        String result = csvDataService.tryCSVDataService(serviceName, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");
        assertEquals(csvExpectedResult, result);
    }

    public void testRemoveCSVExcelServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.deleteService("CSVExcelMultiDataService");

    }

    public void testLogoutUiCSVExcel() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.logOutUI();
    }
}
