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
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
/*
* tests editing a data service through wizard and XML
* tests editing a faulty service
* */
//in this class server is restarted
public class EditDataSreviceTest extends TestCase {

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


    public EditDataSreviceTest (String s) {
        super(s);

    }

    public void setUp() throws Exception  {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        hostname = property.getProperty("host.name");

    }


    public void testMySQLDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.loginToUI(username, password);

        rdbmsDataService.setUpDataBase();
        rdbmsDataService.newDataService("EditDataService");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");

        rdbmsDataService.addQueryName("q1","MySQLDataSource","select * from customer where id = ?");
        rdbmsDataService.addNewInputMapping("id","STRING");
        rdbmsDataService.addResult("customers","customer","");
        rdbmsDataService.addNewOutputMapping("element","id","id","");
        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
        rdbmsDataService.addNewOutputMapping("element","country","country","");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("q2","MySQLDataSource","select * from customer where id=?  ");
        rdbmsDataService.addNewInputMapping("id","STRING");
        rdbmsDataService.addResult("customers","customer","");
        rdbmsDataService.addNewOutputMapping("element","id","id","");
        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
        rdbmsDataService.addNewOutputMapping("element","country","country","");
        rdbmsDataService.saveQuery();

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("selectCustomer1","q1");
        rdbmsDataService.addOperation("selectCustomer2","q2");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("EditDataService");

    }

    public void testInvokeOperation1() throws Exception{
        GenericServiceClient client = new GenericServiceClient();

        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);


        String serviceName = "EditDataService";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "selectCustomer1","urn:selectCustomer1",  "http://ws.wso2.org/dataservice","id","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);

    }

    public void testInvokeOperation2() throws Exception{
        GenericServiceClient client = new GenericServiceClient();

        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "EditDataService";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "selectCustomer2","urn:selectCustomer2",  "http://ws.wso2.org/dataservice","id","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);

    }


    public void testEditThroughWizard() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        instServiceManagement.accessServiceDashboard("EditDataService");

        browser.click("link=Edit Data Service (Wizard)");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        String query="q1";

        int i = 1;

        while (browser.isElementPresent("//table[@id='query-table']/tbody/tr[" + Integer.toString(i) + "]/td[1]")) {
            if(query.equals(browser.getText("//table[@id='query-table']/tbody/tr[" + Integer.toString(i) + "]/td[1]"))){
                browser.click("//table[@id='query-table']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[1]");
                browser.waitForPageToLoad("30000");
                break;
            }

            i = i + 1;
        }

        browser.click("link=Delete");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertEquals("q1 has been used by selectCustomer1 operation. Please remove the relevant operation(s) to proceed.", browser.getText("messagebox-error"));
        browser.click("//button[@type='button']");
        browser.click("//input[@value='Cancel']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        String operation= "selectCustomer1";

        browser.click("//a[@onclick=\"deleteOperations(document.getElementById('" +  operation + "').value);\"]");
        assertTrue(browser.isTextPresent("exact:Do you want to delete operation " + operation + "?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");



        browser.click("//input[@value='< Back']");
        browser.waitForPageToLoad("30000");
        browser.click("link=Edit Query");
        browser.waitForPageToLoad("30000");
        browser.click("link=Delete");
        assertTrue(browser.isTextPresent("exact:Do you want to delete the input mapping id?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addNewInputMapping("name","STRING");
        browser.type("sql", "select * from customer where name = ?");
        browser.click("//input[@value='Save']");
        browser.waitForPageToLoad("30000");

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("selectCustomer1","q1");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

    }

    //edit through wizard
    public void testInvokeEditedOperation1() throws Exception{
        GenericServiceClient client = new GenericServiceClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
        instServiceManagement.restartServer();

        String serviceName = "EditDataService";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "selectCustomer1","urn:selectCustomer1",  "http://ws.wso2.org/dataservice","name","Krishantha");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);

    }


    public void testEditOperation2() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.accessServiceDashboard("EditDataService");
        browser.click("link=Edit Data Service (XML Edit)");
        browser.waitForPageToLoad("30000");
        String xml =  browser.getText("dsConfig");

        xml=xml.replaceAll("selectCustomer2","selectCustomer2New");
        System.out.println(xml);
        browser.type("dsConfig",xml);
        browser.click("save");
        browser.waitForPageToLoad("30000");

    }

    //edit through xml
    public void testInvokeEditedOperation2() throws Exception{
        GenericServiceClient client = new GenericServiceClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
        instServiceManagement.restartServer();

        String serviceName = "EditDataService";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "selectCustomer2New","urn:selectCustomer2New",  "http://ws.wso2.org/dataservice","id","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);

    }

    public void testAddFaultyService() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.accessServiceDashboard("EditDataService");
        browser.click("link=Edit Data Service (XML Edit)");
        browser.waitForPageToLoad("30000");
        String xml =  browser.getText("dsConfig");

        xml=xml.replaceFirst("<param name=\"name\" sqlType=\"STRING\" type=\"\"/>", "  <param name=\"abc\" sqlType=\"STRING\" type=\"\"/>");
        System.out.println(xml);
        browser.type("dsConfig",xml);
        browser.click("save");
        browser.waitForPageToLoad("30000");

    }

    public void testFaultyServiceUI() throws Exception{
        GenericServiceClient client = new GenericServiceClient();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);
        instServiceManagement.restartServer();

        rdbmsDataService.accessEditFaultyService("EditDataService");

         String xml =  browser.getText("dsConfig");

        xml=xml.replaceFirst("<param name=\"abc\" sqlType=\"STRING\" type=\"\"/>", "  <param name=\"name\" sqlType=\"STRING\" type=\"\"/>");
        System.out.println(xml);
        browser.type("dsConfig",xml);
        browser.click("save");
        browser.waitForPageToLoad("30000");


    }

    public void testEditedFaultyService() throws Exception{

        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.restartServer();
          instServiceManagement.accessServiceDashboard("EditDataService");


    }



     public void testRemoveEditXMLDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("EditDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testLogoutUIEditDataService()throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.logOutUI();


    }


}