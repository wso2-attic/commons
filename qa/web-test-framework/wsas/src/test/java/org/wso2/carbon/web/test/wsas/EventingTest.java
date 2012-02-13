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

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.savan.eventing.client.EventingClientBean;
import org.apache.savan.eventing.client.EventingClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ModuleManagement;
import org.wso2.carbon.web.test.common.ServiceManagement;
import junit.framework.TestCase;
import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

import com.thoughtworks.selenium.Selenium;

public class EventingTest extends TestCase {
       //
    final static String SUBSCRIBER_ID = "wso2-savan-subscriber";
    final static String EVENTING_PUBLISHER = "StockQuoteService";
    final static String SAVAN_MODULE = "savan-SNAPSHOT";
    Selenium browser;
    Properties property;
    String username;
    String password;
    String hostname;
    String httpport;
    String contextroot;

    public EventingTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");
    }





    public void testDeployEventingServices() throws Exception {
        File publisheraarPath = new File("." + File.separator + "lib" + File.separator + "StockQuoteService.aar");
        File consumeraarPath = new File("." + File.separator + "lib" + File.separator + "Consumer.aar");

        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);

        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename", publisheraarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
        Thread.sleep(12000);

        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename", consumeraarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
    }

    public void testEngageSavan() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);

        moduleManagement.engageServiceLevelModules(EVENTING_PUBLISHER,EVENTING_PUBLISHER, SAVAN_MODULE);
        Thread.sleep(5000);

    }

    public void testEventing() throws Exception {

        ServiceManagement serviceManagement = new ServiceManagement(browser);
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(property.getProperty("carbon.home") + File.separator + "repository");
        ServiceClient client = new ServiceClient(cc, null);
        Options options = new Options();

        EventingClient eventingClient = null;

        options.setTo(new EndpointReference("http://" + hostname + ":" + httpport + contextroot + "/services/" + "StockQuoteService"));
        client.setOptions(options);
        client.engageModule("addressing");
        EventingClientBean bean = new EventingClientBean();
        bean.setDeliveryEPR(new EndpointReference("http://" + hostname + ":" + httpport + contextroot  + "/services/" + "StockQuoteConsumer"));
        eventingClient = new EventingClient(client);
        eventingClient.subscribe(bean, SUBSCRIBER_ID);
        serviceManagement.accessServiceDashboard(EVENTING_PUBLISHER);
        assertEquals("Manage Eventing Subscriptions", browser.getText("link=Manage Eventing Subscriptions"));
        browser.click("link=Manage Eventing Subscriptions");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Eventing Service Subscriptions"));
        assertTrue(browser.isTextPresent("Valid Subscriptions"));
        assertTrue(browser.isTextPresent("Expired Subscriptions"));
        assertTrue(browser.isElementPresent("//table[@id='validsubscriptions']/tbody/tr[1]/td"));

    }

    public void testRemoveService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.deleteService("StockQuoteService");
        instSeleniumTestBase.deleteService("Consumer");
    }

    public void testLogout() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        instSeleniumTestBase.logOutUI();
    }

}


