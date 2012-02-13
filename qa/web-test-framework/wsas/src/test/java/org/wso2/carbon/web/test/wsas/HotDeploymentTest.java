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

import java.util.Properties;
import java.io.File;

import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.EditAxis2XML;
import junit.framework.TestCase;

public class HotDeploymentTest extends TestCase {
    Selenium browser;
    Properties property;
    String username;
    String password;


    public HotDeploymentTest (String s) {
        super(s);

    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");

    }

    public void testDisableHoyDeployment() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        EditAxis2XML hu=new EditAxis2XML();
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        seleniumTestBase.loginToUI(username, password);
                
        hu.editXML("hotdeployment","false");
        Thread.sleep(5000) ;
        instServiceManagement.restartServer();
    }

    public void testAddService() throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

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
        browser.click("link=List");
        browser.waitForPageToLoad("120000");

        if(browser.isTextPresent(ServiceName)){
               assertTrue("ERROR",true);
        }

          }

    public void testHotDeployment() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
         String ServiceName = "Axis2Service";
        
        instServiceManagement.restartServer();
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(ServiceName));
    }

    public void testEnableHotDeployment() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        EditAxis2XML hu=new EditAxis2XML();
        hu.editXML("hotdeployment","true");
        Thread.sleep(5000) ;
        instServiceManagement.restartServer();
    }

     public void testRemoveServices() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }


    public void testlogOutAxis2Service() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

}

