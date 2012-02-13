package org.wso2.carbon.web.test.wsas;

import com.thoughtworks.selenium.Selenium;
//import org.wso2.carbon.web.test.common.MexModuleClient;
import org.wso2.carbon.web.test.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

import java.util.Properties;
import java.io.File;

import junit.framework.TestCase;
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


public class ModuleManagementTest extends TestCase {
    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    String sandesha_version = "sandesha2-2.01";
    String wso2mex_version = "wso2mex-2.01";
    String wso2xfer_version = "wso2xfer-2.01";
    String wso2caching_version = "wso2caching-2.01";
    String rahas_version = "rahas-2.01";
    String wso2throttle_version = "wso2throttle-2.01";
    String rampart_version = "rampart-2.01";
    String savan_version = "savan-SNAPSHOT";
    String addressing_version = "addressing-1.5";


    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        property = BrowserInitializer.getProperties();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");

        sandesha_version = "sandesha2-" + moduleVersion;
        wso2mex_version = "wso2mex-" + moduleVersion;
        wso2xfer_version = "wso2xfer-" + moduleVersion;
        wso2caching_version = "wso2caching-" + moduleVersion;
        rahas_version = "rahas-" + moduleVersion;
        wso2throttle_version = "wso2throttle-" + moduleVersion;
        rampart_version = "rampart-" + moduleVersion;
        savan_version = "savan-SNAPSHOT";
        addressing_version = "addressing-1.5";
    }


    public ModuleManagementTest(String text) {
        super(text);
    }


    //test whether the modules sre present in the module index page
    public void testModuleIndexPage() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        InstSeleniumTestBase.loginToUI(username, password);

        instModuleManagement.checkModules(wso2mex_version);
        instModuleManagement.checkModules(sandesha_version);
        instModuleManagement.checkModules(wso2xfer_version);
        instModuleManagement.checkModules(wso2caching_version);
        instModuleManagement.checkModules(rahas_version);
        instModuleManagement.checkModules(savan_version);
        instModuleManagement.checkModules(wso2throttle_version);
        instModuleManagement.checkModules(addressing_version);
        instModuleManagement.checkModules(rampart_version);
    }

    public void testUploadAxis2ServiceForModule() throws Exception {
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
    //test engaging and disengaging modules from the module index page
    public void testEngageWso2mex() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModuleIndexPage();
        instModuleManagement.engageGlobalLevelModules(wso2mex_version);
        instModuleManagement.engagedAtServiceGroupLevel("Axis2Service", wso2mex_version);
        instModuleManagement.engagedAtServiceLevel("Axis2Service", wso2mex_version);
        instModuleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", wso2mex_version);
        instModuleManagement.disengageGlobalLevelModules(wso2mex_version);
    }

    public void testEngageSandesha2() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.engageGlobalLevelModules(sandesha_version);
        instModuleManagement.engagedAtServiceGroupLevel("Axis2Service", sandesha_version);
        instModuleManagement.engagedAtServiceLevel("Axis2Service", sandesha_version);
        instModuleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", sandesha_version);
        instModuleManagement.disengageGlobalLevelModules(sandesha_version);
    }

    public void testEngageWso2xfer() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.engageGlobalLevelModules(wso2xfer_version);
        instModuleManagement.engagedAtServiceGroupLevel("Axis2Service", wso2xfer_version);
        instModuleManagement.engagedAtServiceLevel("Axis2Service", wso2xfer_version);
        instModuleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", wso2xfer_version);
        instModuleManagement.disengageGlobalLevelModules(wso2xfer_version);
    }

    public void testEngageSavan() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.engageGlobalLevelModules(savan_version);
        instModuleManagement.engagedAtServiceGroupLevel("Axis2Service", savan_version);
        instModuleManagement.engagedAtServiceLevel("Axis2Service", savan_version);
        instModuleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", savan_version);
        instModuleManagement.disengageGlobalLevelModules(savan_version);
    }

    public void testEngageRahas() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.engageGlobalLevelModules(rahas_version);
        instModuleManagement.engagedAtServiceGroupLevel("Axis2Service", rahas_version);
        instModuleManagement.engagedAtServiceLevel("Axis2Service", rahas_version);
        instModuleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", rahas_version);
        instModuleManagement.engagedAtServiceGroupLevel("Axis2Service", rampart_version);
        instModuleManagement.engagedAtServiceLevel("Axis2Service", rampart_version);
        instModuleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", rampart_version);
        instModuleManagement.disengageGlobalLevelModules(rahas_version);
        instModuleManagement.disengageGlobalLevelModules(rampart_version);
    }



    public void testUploadModule() throws Exception{
        ModuleManagement instModuleManagement= new ModuleManagement(browser);

        instModuleManagement.uploadModule("counter-module-SNAPSHOT");
        instModuleManagement.checkModules("counter-module-SNAPSHOT");
        instModuleManagement.deleteModule("counterModule-SNAPSHOT");
        assertFalse(browser.isTextPresent("counterModule"));
    }

    //test adding ane removing module parameters in the module index page
    public void testSandesha2Parameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(sandesha_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        assertTrue(browser.isTextPresent("propertiesToCopyFromReferenceMessage"));
        assertTrue(browser.isTextPresent("propertiesToCopyFromReferenceRequestMessage"));
        instModuleManagement.accessModules(sandesha_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testWso2cachingParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(wso2caching_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(wso2caching_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testRahasParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(rahas_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(rahas_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testRampartParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(rampart_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(rampart_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testSavanParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(savan_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(savan_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testWso2ThrottleParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(wso2throttle_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(wso2throttle_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testAddressingParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(addressing_version);
        instModuleManagement.addNewModuleParameter("testparam");
        instModuleManagement.accessModules(addressing_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testWso2mexParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(wso2mex_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(wso2mex_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();
    }

    public void testWso2xferParameters() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModules(wso2xfer_version);
        instModuleManagement.addNewModuleParameter("testparam");
        assertTrue(browser.isTextPresent("managedModule"));
        instModuleManagement.accessModules(wso2xfer_version);
        instModuleManagement.deleteModuleParameter("testparam");
        instModuleManagement.accessModuleIndexPage();

    }
    ////
    //test engaging and disengaging modules at service group level and service level
    public void testSGModules() throws Exception {

        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ModuleManagement moduleManagement = new ModuleManagement(browser);

        moduleManagement.engageServiceGroupModules("HelloWorld", "HelloService", "greet", wso2xfer_version);
        moduleManagement.disengageServiceGroupModules("HelloWorld", "HelloService", "greet", wso2xfer_version);

        moduleManagement.engageServiceGroupModules("HelloWorld", "HelloService", "greet", savan_version);
        moduleManagement.disengageServiceGroupModules("HelloWorld", "HelloService", "greet", savan_version);

        moduleManagement.engageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", sandesha_version);

        new RMClient().RMRequestReplyAnonClient("Axis2Service", "soap11", "urn:echoString", "http://service.carbon.wso2.org", "echoString", "s");
        moduleManagement.disengageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", sandesha_version);

        moduleManagement.engageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", wso2mex_version);
        new MexModuleClient().getServiceSchema("http://"+ hostname + ":" + httpport + contextroot + "/services/Axis2Service");     //read epr from property file
        moduleManagement.disengageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", wso2mex_version);


    }  //engage and disengage wso2mex at service group level


    public void testWso2mexAtServiceLevel() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);

        moduleManagement.engageServiceLevelModules("Axis2Service","Axis2Service", wso2mex_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", wso2mex_version);

        moduleManagement.disengageServiceLevelModules("Axis2Service", wso2mex_version);
        moduleManagement.disengagedAtServiceLevel("Axis2Service", wso2mex_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", wso2mex_version);
    }

    public void testSandesha2AtServiceLevel() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);

        moduleManagement.engageServiceLevelModules("Axis2Service","Axis2Service", sandesha_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", sandesha_version);

        browser.click("link=Service Dashboard");
        browser.waitForPageToLoad("30000");
        browser.click("link=Reliable Messaging");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Yes"));

        moduleManagement.disengageServiceLevelModules("Axis2Service",sandesha_version);
        moduleManagement.disengagedAtServiceLevel("Axis2Service", sandesha_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", sandesha_version);
    }

    public void testWso2xfer2AtServiceLevel() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);

        moduleManagement.engageServiceLevelModules("Axis2Service","Axis2Service", wso2xfer_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", wso2xfer_version);

        moduleManagement.disengageServiceLevelModules("Axis2Service",wso2xfer_version);
        moduleManagement.disengagedAtServiceLevel("Axis2Service", wso2xfer_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", wso2xfer_version);
    }

    public void testSavanAtServiceLevel() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);

        moduleManagement.engageServiceLevelModules("Axis2Service","Axis2Service", savan_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", savan_version);

        moduleManagement.disengageServiceLevelModules("Axis2Service",savan_version);
        moduleManagement.disengagedAtServiceLevel("Axis2Service", savan_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", savan_version);
    }

    public void testRahasAtServiceLevel() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);
        moduleManagement.engageServiceLevelModules("Axis2Service","Axis2Service", rahas_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", rahas_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", rampart_version);
        moduleManagement.engagedAtServiceLevel("Axis2Service", rampart_version);

        moduleManagement.disengageServiceLevelModules("Axis2Service",rahas_version);
        moduleManagement.disengagedAtServiceLevel("Axis2Service", rahas_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", rahas_version);

        moduleManagement.disengageServiceLevelModules("Axis2Service",rampart_version);
        moduleManagement.disengagedAtServiceLevel("Axis2Service",rampart_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", rampart_version);
    }


    public void testSecurityeAtServiceLevel() throws Exception {
        ModuleManagement moduleManagement = new ModuleManagement(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        moduleManagement.engageServiceLevelModules("Axis2Service","Axis2Service", rampart_version);
        instServiceManagement.enableSecurityScenario("Axis2Service", "scenario1");
        moduleManagement.disengageServiceLevelModules("Axis2Service", rampart_version);
        moduleManagement.engagedAtServiceLevel("Axis2Service", rampart_version);
        moduleManagement.engagedAtOperationalLevel("Axis2Service", "echoString", rampart_version);
        instServiceManagement.disableSecurity("Axis2Service");
        moduleManagement.disengagedAtServiceLevel("Axis2Service", rampart_version);
        moduleManagement.disengagedAtOperationalLevel("Axis2Service", "echoString", rampart_version);
        moduleManagement.disengageServiceLevelModules("Axis2Service", rampart_version);

        instServiceManagement.enableSecurityScenario("Axis2Service", "scenario1");
        moduleManagement.engageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", rampart_version);
        moduleManagement.engagedAtServiceLevel("Axis2Service", rampart_version);
        instServiceManagement.disableSecurity("Axis2Service");
        moduleManagement.disengageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", rampart_version);

        moduleManagement.engageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", rampart_version);
        instServiceManagement.enableSecurityScenario("Axis2Service", "scenario1");
        moduleManagement.engagedAtServiceLevel("Axis2Service", rampart_version);
        instServiceManagement.disableSecurity("Axis2Service");
        moduleManagement.disengageServiceGroupModules("Axis2Service", "Axis2Service", "echoString", rampart_version);

// seleniumTestBase.logOutUI();

    }

    public void testDisableSecurity() throws Exception{
         ServiceManagement instServiceManagement = new ServiceManagement(browser);
         instServiceManagement.disableSecurity("Axis2Service");
    }



    public void testAccessDenyThrottling() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModuleIndexPage();

        instModuleManagement.deleteEntries();
        // instModuleManagement.checkAccessDenyAtGlobalThottling("", "HelloService", "greet", "name", "jayani", 0, "Fault: Access deny for a caller ");
        instModuleManagement.checkAccessControlAtGlobalThottling("IP", 10, 100000, 10000, "Deny");
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "HelloService";
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 0);

        instModuleManagement.globallyDisableThrottling();
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 10);

    }

    public void testAccessControlThrottling1() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModuleIndexPage();
        instModuleManagement.checkAccessControlAtGlobalThottling("IP", 10, 100000, 10000, "Control");


        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 10);

        Thread.sleep(10000);
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 10);

        Thread.sleep(10000);
        serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/HelloService.HelloServiceHttpSoap11Endpoint/";
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 10);

        Thread.sleep(10000);
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 10);

    }


    public void testAccessControlThrottling2() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        Thread.sleep(10000);
        instModuleManagement.accessModuleIndexPage();
        instModuleManagement.checkAccessControlAtGlobalThottling("DOMAIN", 5, 100000, 10000, "Control");

        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 5);

        Thread.sleep(10000);
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 5);

        Thread.sleep(10000);
        serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/HelloService.HelloServiceHttpSoap11Endpoint/";
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 5);

        Thread.sleep(10000);
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 5);

        instModuleManagement.globallyDisableThrottling();
        serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 16);


        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 16);
    }

    public void  testAccessControlThrottling3() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);
        Thread.sleep(10000);
        instModuleManagement.accessModuleIndexPage();

        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";

        instModuleManagement.setMaximumConcurrentAccesses("7");
        instModuleManagement.checkAccessControlAtGlobalThottling("DOMAIN", 5, 100000, 10000, "Control");
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 5);

        Thread.sleep(10000);
        instModuleManagement.setMaximumConcurrentAccesses("");
        instModuleManagement.checkAccessControlAtGlobalThottling("IP", 0, 100000, 10000, "Allow");
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 16);
    }

    public void testAccessControlThrottling4() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModuleIndexPage();

        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        Thread.sleep(10000);
        instModuleManagement.setMaximumConcurrentAccesses("");
        instModuleManagement.checkAccessControlAtGlobalThottling("IP", 10, 100000, 10000, "Control");
        instModuleManagement.enableThrottlingAtServiceLevel("Axis2Service", "IP", 3, 100000, 10000, "Control");
        /*instModuleManagement.enableThrottlingAtOperationalLevel("Axis2Service", "echoString", "IP", 2, 100000, 10000, "Control");

        instModuleManagement.invokeService(serviceepr, "echoInt", "urn:echoInt", "http://service.carbon.wso2.org", "x", 3);
       Thread.sleep(10000);

        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 2);
        Thread.sleep(10000);
        instModuleManagement.invokeService(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org", "return", 3);
       Thread.sleep(10000);  */

        serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/HelloService.HelloServiceHttpSoap11Endpoint/";
        instModuleManagement.invokeService(serviceepr, "greet", "urn:greet", "http://www.wso2.org/types", "name", 10);

        Thread.sleep(10000);

        serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 3);

        instModuleManagement.globallyDisableThrottling();
    }

    public void testAddEntry() throws Exception{
        ModuleManagement instModuleManagement = new ModuleManagement(browser);

        instModuleManagement.accessModuleIndexPage();
        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        browser.click("link=Add New Entry");
        browser.type("data21", "other");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
        instModuleManagement.globallyDisableThrottling();
    }

    public void testWso2mexModule() throws Exception {
        ModuleManagement instModuleManagement = new ModuleManagement(browser);
        MexModuleClient instMexModuleClient = new MexModuleClient();

        instModuleManagement.accessModuleIndexPage();
        instModuleManagement.engageGlobalLevelModules(wso2mex_version);
        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        instMexModuleClient.getServiceSchema(serviceepr);
        instModuleManagement.disengageGlobalLevelModules(wso2mex_version);
    }

    public void testSandeshaModule() throws Exception {
        Thread.sleep(10000);
        ModuleManagement instModuleManagement = new ModuleManagement(browser);
        RMClient rmClient = new RMClient();

        instModuleManagement.accessModuleIndexPage();
        instModuleManagement.engageGlobalLevelModules(sandesha_version);
        instModuleManagement.invokeRMClient("Axis2Service","urn:echoString", "http://service.carbon.wso2.org", "echoString", "s");
        //Request-Reply invocation
//        int soap11_response_count = rmClient.RMRequestReplyAnonClient("Axis2Service", "soap11", "urn:echoString", "http://service.carbon.wso2.org", "echoString", "s");
//        //assertEquals(11, soap11_response_count);
//        if(soap11_response_count==11){
//            System.out.println("RM Done");
//        }else{
//            System.out.println("RM Not Done");
//            assertEquals(11, soap11_response_count);;
//        }
        instModuleManagement.disengageGlobalLevelModules(sandesha_version);

    }

    public void testCaching() throws Exception {
        Thread.sleep(10000);
        ModuleManagement instModuleManagement = new ModuleManagement(browser);
        CachingClient instCachingClient = new CachingClient();
        //SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        instModuleManagement.engageCachingAtGlobalLevel();

        String serviceepr = "http://" + hostname + ":" + httpport + contextroot + "/services/" + "Axis2Service";
        String time1 = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

        int i = 1;
        String time2;
        while (i < 5) {
            Thread.sleep(5000);
            time2 = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

            i = i + 1;
            assertEquals("Caching not done!", time1, time2);

        }
//
//        InstSeleniumTestBase.logOutUI();
//        InstSeleniumTestBase.loginToUI("admin", "admin");
//
//        time2 = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");
//        assertEquals("Caching not done!", time1, time2);
        instModuleManagement.disengagecachingatGlobalLevel();
    }

    public void testRemoveServicesfromModuleManagement() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }


    public void testlogOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }


}

                                                    