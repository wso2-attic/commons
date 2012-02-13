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
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.GenericServiceClient;
import org.wso2.carbon.web.test.common.ServiceManagement;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

public class MonitorTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;

    public  MonitorTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    public void testLogin()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.loginToUI(username, password);

    }
    public void testLogs() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.checkSystemLogs();
    }

    public void testFlows() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.checkFlows();

    }
    public void testSystemStats()throws Exception
    {
        browser.click("link=System Statistics");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("System Statistics"));
        assertTrue(browser.isTextPresent("Service Summary"));
        assertTrue(browser.isTextPresent("Average Response Time"));
        assertTrue(browser.isTextPresent("Minimum Response Time"));
        assertTrue(browser.isTextPresent("Maximum Response Time"));
        assertTrue(browser.isTextPresent("Total Request Count"));
        assertTrue(browser.isTextPresent("Total Response Count"));
        assertTrue(browser.isTextPresent("Total Fault Count"));
        assertTrue(browser.isTextPresent("Active Services"));
        assertTrue(browser.isTextPresent("Server"));
        assertTrue(browser.isTextPresent("Host"));
        assertTrue(browser.isTextPresent("Server Start Time"));
        assertTrue(browser.isTextPresent("System Up Time"));
        assertTrue(browser.isTextPresent("Memory Allocated"));
        assertTrue(browser.isTextPresent("Memory Usage"));
        assertTrue(browser.isTextPresent("Average Response Time(ms) vs. Time(Units)"));
        assertTrue(browser.isTextPresent("Memory(MB) vs. Time(Units)"));
        assertTrue(browser.isTextPresent("Used"));
        assertTrue(browser.isTextPresent("Allocated"));
        assertTrue(browser.isTextPresent("Statistics Configuration"));
        assertTrue(browser.isTextPresent("Statistics Refresh Interval (ms)"));
        assertTrue(browser.isTextPresent("Response Time Graph"));
        assertTrue(browser.isTextPresent("X-Scale (units)"));
        assertTrue(browser.isTextPresent("X-Width (px)"));
        assertTrue(browser.isTextPresent("Memory Graph"));
        assertTrue(browser.isTextPresent("X-Scale (units)"));
        assertTrue(browser.isTextPresent("X-Width (px)"));
        browser.click("//input[@value='Reset']");
        browser.type("responseTimeGraphXScale", "60");
        browser.click("updateStats");
        browser.waitForPageToLoad("30000");
        assertEquals("6", browser.getText("//div[@id='responseTimeGraph']/div/div[9]"));
        browser.type("responseTimeGraphWidth", "600");
        browser.click("updateStats");
        browser.waitForPageToLoad("30000");
        browser.type("memoryGraphXScale", "50");
        browser.click("updateStats");
        browser.waitForPageToLoad("30000");
        assertEquals("5", browser.getText("//div[@id='memoryGraph']/div[1]/div[9]"));
        browser.type("memoryGraphWidth", "600");
        browser.click("updateStats");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Reset']");
    }

    public void testUploadAxis2ServiceForMonitorTest() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "Axis2Service.aar");
        String ServiceName = "Axis2Service";

        //browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename", aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));


    }

    public void testInvokeService() throws Exception{
        GenericServiceClient client=new GenericServiceClient();


        String serviceName = "Axis2Service";
        String nameSpace = "http://service.carbon.wso2.org";
        String soapAction = "urn:echoInt";
        String operation = "echoInt";

        for(int i=0;i<5;i++){
            client.twoWayAnonClient(serviceName,soapAction,nameSpace,operation,"x","1");
        }

        for(int i=0;i<5;i++){
            try{
                client.twoWayAnonClient(serviceName,soapAction,nameSpace,operation,"x","abc");
            }catch(Exception e){}
        }

    }

    public void testRequestCountAtServiceLevel() throws Exception{
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        checkServiceSatistics("Request Count","10");

    }

    public void testResponseCountAtServiceLevel() throws Exception{
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        checkServiceSatistics("Response Count","5");

    }

    public void testFaultCountAtServiceLevel() throws Exception{
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        checkServiceSatistics("Fault Count","5");

    }

    public void testRequestCountAtOperationalLevel() throws Exception{
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        insServiceManagement.openOperationDashboard("Axis2Service","echoInt");
        System.out.println("In Operational level");
        checkOperationalSatistics("Request Count","10");

    }

    public void testResponseCountAtOperationalLevel() throws Exception{
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        insServiceManagement.openOperationDashboard("Axis2Service","echoInt");
        System.out.println("In Operational level");
        checkOperationalSatistics("Request Count","5");

    }

    public void testFaultCountAtOperationalLevel() throws Exception{
        ServiceManagement insServiceManagement = new ServiceManagement(browser);
        insServiceManagement.Login();
        insServiceManagement.accessServiceDashboard("Axis2Service");
        insServiceManagement.openOperationDashboard("Axis2Service","echoInt");
        System.out.println("In Operational level");
        checkOperationalSatistics("Fault Count","5");

    }

    public void testRemoveServicesfromMonitorTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }


    public void testLogoutMonitorTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.logOutUI();
    }

    public void checkServiceSatistics(String countName,String count){
        int i = 1;

        while (browser.isElementPresent("//table[@id='serviceStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]") ) {

            if(countName.equals(browser.getText("//table[@id='serviceStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]"))){
                System.out.println(browser.getText("//table[@id='serviceStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]") + " "+browser.getText("//table[@id='serviceStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]"));
                assertEquals(count,browser.getText("//table[@id='serviceStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]"));
                break;
            }

            i = i + 1;
        }

    }

    public void checkOperationalSatistics(String countName,String count){
        int i = 1;

        while (browser.isElementPresent("//table[@id='opStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]") ) {

            if(countName.equals(browser.getText("//table[@id='opStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]"))){
                System.out.println(browser.getText("//table[@id='opStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]") + " "+browser.getText("//table[@id='opStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]"));
                assertEquals(count,browser.getText("//table[@id='opStatsTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]"));
                break;
            }

            i = i + 1;
        }

    }
}

