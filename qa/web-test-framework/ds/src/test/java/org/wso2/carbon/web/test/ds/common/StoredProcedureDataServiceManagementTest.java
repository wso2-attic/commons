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
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.*;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

public class StoredProcedureDataServiceManagementTest extends TestCase{

    Selenium browser;
    Properties property;
    String username;
    String password;

    public StoredProcedureDataServiceManagementTest(String text) {
        super(text);
    }


    public void setUp() throws Exception  {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    public void testMySQLDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.loginToUI(username, password);

        rdbmsDataService.setUpDataBase();
        rdbmsDataService.addStoredProcedure("CREATE PROCEDURE getCustomer(cusid INTEGER) select id, name, country from customer where id=cusid;");
        rdbmsDataService.addStoredProcedure("CREATE PROCEDURE addCustomer(cusid VARCHAR(100),cusname VARCHAR(100),cuscountry VARCHAR(200)) INSERT INTO customer(id,name,country) VALUES(cusid,cusname,cuscountry)");
        rdbmsDataService.addStoredProcedure("CREATE PROCEDURE updateCustomer(oldName VARCHAR(100),newName VARCHAR(100)) UPDATE customer SET name=newName WHERE name=oldName");
        rdbmsDataService.newDataService("StoredProcedureDataService");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");
//        rdbmsDataService.addNewQueryWithInput("customers-query", "MySQLDataSource", "call getCustomer(?)","customerid","INTEGER","customers","customer");
//        csvDataService. addOperation("getCustomer","customers-query") ;
//        csvDataService.checkService("StoredProcedureDataService");

        rdbmsDataService.addQueryName("getCustomer","MySQLDataSource","call getCustomer(?)");
        rdbmsDataService.addNewInputMapping("customerid","INTEGER");
        rdbmsDataService.addResult("customers","customer","");
        rdbmsDataService.addNewOutputMapping("element","id","id","");
        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
        rdbmsDataService.addNewOutputMapping("element","country","country","");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("getAllCustomers","MySQLDataSource","select * from customer");
        rdbmsDataService.addResult("customers","customer","");
        rdbmsDataService.addNewOutputMapping("element","id","id","");
        rdbmsDataService.addNewOutputMapping("attribute","name","name","");
        rdbmsDataService.addNewOutputMapping("element","country","country","");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("updateCustomer","MySQLDataSource","call updateCustomer(?,?)");
        rdbmsDataService.addNewInputMapping("oldName","STRING");
        rdbmsDataService.addNewInputMapping("newName","STRING");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("addCustomer","MySQLDataSource","call addCustomer(?,?,?)");
        rdbmsDataService.addNewInputMapping("id","STRING");
        rdbmsDataService.addNewInputMapping("name","STRING");
        rdbmsDataService.addNewInputMapping("country","STRING");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("addCustomerQuery","MySQLDataSource","INSERT INTO customer(id, name, country) VALUES(?,?,?)");
        rdbmsDataService.addNewInputMapping("id","STRING");
        rdbmsDataService.addNewInputMapping("name","STRING");
        rdbmsDataService.addNewInputMapping("country","STRING");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("updateCustomerQuery","MySQLDataSource","call updateCustomer(?,?)");
        rdbmsDataService.addNewInputMapping("oldName","STRING");
        rdbmsDataService.addNewInputMapping("newName","STRING");
        rdbmsDataService.saveQuery();

        rdbmsDataService.addQueryName("deleteCustomerQuery","MySQLDataSource","delete from customer where id=?");
        rdbmsDataService.addNewInputMapping("id","INTEGER");
        rdbmsDataService.saveQuery();


        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("getAllCustomers","getAllCustomers");
        rdbmsDataService.addOperation("addCustomer","addCustomer");
        rdbmsDataService.addOperation("updateCustomer","updateCustomer");
        rdbmsDataService.addOperation("getCustomer","getCustomer");
        rdbmsDataService.addOperation("addCustomerQuery","addCustomerQuery");
        rdbmsDataService.addOperation("updateCustomerQuery","updateCustomerQuery");
        rdbmsDataService.addOperation("deleteCustomerQuery","deleteCustomerQuery");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("StoredProcedureDataService");
    }

    public void testInvokeServiceGetCustomer() throws Exception{
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "StoredProcedureDataService";
        String csvExpectedResult = "Krishantha";

        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "getCustomer","urn:getCustomer","http://ws.wso2.org/dataservice","customerid","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);

    }

    public void testInvokeAddCustomer()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "StoredProcedureDataService";
        String csvExpectedResult = "Dinusha";

        client.onewayClientThreeInputs(serviceName,"urn:addCustomer","http://ws.wso2.org/dataservice" , "addCustomer","id","003","name","Dinusha","country","SriLanka");
        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "getCustomer","urn:getCustomer","http://ws.wso2.org/dataservice","customerid","003");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
    }

    public void testInvokeUpdateCustomer()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "StoredProcedureDataService";
        String csvExpectedResult = "Sarasi";

        client.oneWayClientTwoInputs(serviceName,"urn:updateCustomer","http://ws.wso2.org/dataservice" , "updateCustomer","oldName","Krishantha","newName",csvExpectedResult);
        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "getCustomer","urn:getCustomer","http://ws.wso2.org/dataservice","customerid","002");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
    }

    public void testInvokeAddCustomerQuery()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "StoredProcedureDataService";
        String csvExpectedResult = "Dinusha";

        client.onewayClientThreeInputs(serviceName,"urn:addCustomerQuery","http://ws.wso2.org/dataservice" , "addCustomerQuery","id","004","name",csvExpectedResult,"country","UK");
        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "getCustomer","urn:getCustomer","http://ws.wso2.org/dataservice","customerid","004");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);
    }

    public void testInvokeUpdateCustomerQuery()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "StoredProcedureDataService";
        String csvExpectedResult = "Jayani";

        client.oneWayClientTwoInputs(serviceName,"urn:updateCustomer","http://ws.wso2.org/dataservice" , "updateCustomer","oldName","Sarasi","newName",csvExpectedResult);
        String result = rdbmsDataService.tryRDBMSDataServiceWithInput(serviceName, "getCustomer","urn:getCustomer","http://ws.wso2.org/dataservice","customerid","002");

        System.out.println(result);
        assertEquals(csvExpectedResult, result);
    }

    public void testInvokeDeleteCustomer()throws Exception{
        GenericServiceClient client=new GenericServiceClient();
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        String serviceName = "StoredProcedureDataService";
        String csvExpectedResult = "Jayani";

        client.oneWayClientOneInput(serviceName,"urn:deleteCustomerQuery","http://ws.wso2.org/dataservice" , "deleteCustomerQuery","id","001");
        Thread.sleep(1000);

        String result = rdbmsDataService.tryRDBMSDataService(serviceName, "getAllCustomers", "urn:getAllCustomers", "http://ws.wso2.org/dataservice");
        System.out.println(result);
        assertEquals(csvExpectedResult, result);

    }

    public void testRemoveStoredProcedureServices() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        seleniumTestBase.deleteService("StoredProcedureDataService");
        rdbmsDataService.cleanDataBase();

    }

    public void testLogoutUIStoredProcedureDataService()throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);

        seleniumTestBase.logOutUI();


    }



}
