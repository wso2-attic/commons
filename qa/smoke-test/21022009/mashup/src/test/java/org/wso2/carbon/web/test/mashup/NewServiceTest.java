package org.wso2.carbon.web.test.mashup;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

public class NewServiceTest extends SeleneseTestCase {
    Selenium selenium;
    public static String SERVICE_NAME = "automation";
    public static String OPERATION_NAME = "toString";

    //Initialize the browser
    public void setUp() throws Exception {
        selenium = BrowserInitializer.getBrowser();
    }


    //Login to admin console and check the environment
    public void testRun() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        instSeleniumTestBase.loginToUI("admin", "admin");

        //Delete any existing services with the name "automation"
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        Boolean x = selenium.isTextPresent(SERVICE_NAME);
        if (x) {
            selenium.click("serviceGroups");
            selenium.click("delete1");
            assertTrue(selenium.isElementPresent("messagebox-confirm"));
            selenium.click("//button[@type='button']");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isElementPresent("messagebox-info"));
            assertTrue(selenium.isTextPresent("Successfully deleted selected service groups"));
            selenium.click("//button[@type='button']");
        }

    }


    //Create a new .js service
    public void testNew() throws Exception {
        selenium.click("link=Create");
        selenium.waitForPageToLoad("30000");
        selenium.type("serviceName", SERVICE_NAME);
        selenium.click("next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Editing the mashup "+SERVICE_NAME));
        assertTrue(selenium.isElementPresent("//body"));
        selenium.click("//input[@value='Save changes']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(SERVICE_NAME));
    }


    //Access and test service dashboard
    public void testServiceManagement() throws Exception {
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.accessServiceDashboard(SERVICE_NAME);
        serviceManagement.openServiceDashboard(SERVICE_NAME, "js_service");

        //Checking if Mashup specific configs are available
        assertTrue(selenium.isTextPresent("Specific Configuration"));
        assertTrue(selenium.isElementPresent("link=Edit Mashup"));
    }


    //Access and test security scenarios
    public void testSecurityManagement() throws Exception {
        ServiceManagement securityServiceManagement = new ServiceManagement(selenium);

        //Access and enable security
        securityServiceManagement.accessSecurityScenarioPage(SERVICE_NAME);

        //Assign secutiry senarios
        securityServiceManagement.assignUTSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignNonRepudiationSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignConfidentialitySecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignIntegritySecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConSignOnlySecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSgnEncrUsernameSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConEncrUsernameSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConEncrUsernameSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSecConEncrOnlySecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSigEncrSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignEncrOnlyUsernameSecurityScenario(SERVICE_NAME);
        securityServiceManagement.assignSgnEncrUsernameSecurityScenario(SERVICE_NAME);
    }


    //Access and test caching
    public void testResponseCaching() throws Exception {
        ServiceManagement cachingServiceManagement = new ServiceManagement(selenium);
        cachingServiceManagement.checkCaching("http://localhost:9763/services/"+SERVICE_NAME, OPERATION_NAME, "http://services.mashup.wso2.org/"+SERVICE_NAME+"/ServiceInterface/"+OPERATION_NAME+"Request");
    }


    //Access and test RM
    public void testRM() throws Exception {
        ServiceManagement rmServiceManagement = new ServiceManagement(selenium);
        rmServiceManagement.enableRM(SERVICE_NAME);
        rmServiceManagement.rmInvocations(SERVICE_NAME);
        rmServiceManagement.disableRM(SERVICE_NAME);
    }


    //Access and test throttling
    public void testThrottling() throws Exception {
        ServiceManagement throttleServiceManagement = new ServiceManagement(selenium);
        throttleServiceManagement.checkThrottling("http://localhost:9763/services/"+SERVICE_NAME, OPERATION_NAME, "http://services.mashup.wso2.org/"+SERVICE_NAME+"/ServiceInterface/"+OPERATION_NAME+"Request");
    }


/*    //Access and test MTOM
    public void testMTOM() throws Exception {
        ServiceManagement mtomServiceManagement = new ServiceManagement(selenium);

        mtomServiceManagement
    }*/


    /*   public void testPolicies() throws Exception {
        ServiceManagement policyServiceManagement = new ServiceManagement(selenium);
        policyServiceManagement.

    }*/


    //Test engaging/desengaging a module at service level
    public void testServiceModule() throws Exception {
        ServiceManagement smServiceManagement = new ServiceManagement(selenium);
        smServiceManagement.engageServiceModule(SERVICE_NAME, "wso2mex-1.00");
        smServiceManagement.engageServiceModule(SERVICE_NAME, "savan-1.00");
        smServiceManagement.engageServiceModule(SERVICE_NAME, "wso2xfer-1.00");
        smServiceManagement.engageServiceModule(SERVICE_NAME, "sandesha2-1.00");
        smServiceManagement.engageServiceModule(SERVICE_NAME, "rampart-1.00");
        smServiceManagement.engageServiceModule(SERVICE_NAME, "rahas-1.00");

        smServiceManagement.disengageServiceModule(SERVICE_NAME, "wso2mex-1.00");
        smServiceManagement.disengageServiceModule(SERVICE_NAME, "savan-1.00");
        smServiceManagement.disengageServiceModule(SERVICE_NAME, "wso2xfer-1.00");
        smServiceManagement.disengageServiceModule(SERVICE_NAME, "sandesha2-1.00");
        smServiceManagement.disengageServiceModule(SERVICE_NAME, "rampart-1.00");
        smServiceManagement.disengageServiceModule(SERVICE_NAME, "rahas-1.00");
    }


    // Test transport
    public void testTransport() throws Exception {
        ServiceManagement trServiceManagement = new ServiceManagement(selenium);
        trServiceManagement.checkTransport(SERVICE_NAME);
    }


    //Test engaging/disengaging a module at operation level
    public void testOperations() throws Exception {
        ServiceManagement opServiceManagement = new ServiceManagement(selenium);
        opServiceManagement.engageParamModule(SERVICE_NAME, OPERATION_NAME, "wso2mex-1.00");
        opServiceManagement.engageParamModule(SERVICE_NAME, OPERATION_NAME, "savan-1.00");
        opServiceManagement.engageParamModule(SERVICE_NAME, OPERATION_NAME, "wso2xfer-1.00");
        opServiceManagement.engageParamModule(SERVICE_NAME, OPERATION_NAME, "sandesha2-1.00");
        opServiceManagement.engageParamModule(SERVICE_NAME, OPERATION_NAME, "rampart-1.00");
        opServiceManagement.engageParamModule(SERVICE_NAME, OPERATION_NAME, "rahas-1.00");

        opServiceManagement.disengageParamModule(SERVICE_NAME, OPERATION_NAME, "wso2mex-1.00");
        opServiceManagement.disengageParamModule(SERVICE_NAME, OPERATION_NAME, "savan-1.00");
        opServiceManagement.disengageParamModule(SERVICE_NAME, OPERATION_NAME, "wso2xfer-1.00");
        opServiceManagement.disengageParamModule(SERVICE_NAME, OPERATION_NAME, "sandesha2-1.00");
        opServiceManagement.disengageParamModule(SERVICE_NAME, OPERATION_NAME, "rampart-1.00");
        opServiceManagement.disengageParamModule(SERVICE_NAME, OPERATION_NAME, "rahas-1.00");
    }


    //Test message flows
    public void testMessageFlows() throws Exception {
        ServiceManagement msgFlowsServiceManagement = new ServiceManagement(selenium);
        msgFlowsServiceManagement.checkParamFlows();
    }

}