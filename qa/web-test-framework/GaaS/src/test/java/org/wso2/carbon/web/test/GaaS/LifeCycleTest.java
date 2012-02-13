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

package org.wso2.carbon.web.test.GaaS;

import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;


public class LifeCycleTest extends CommonSetup {
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;

    public LifeCycleTest(String msg) {
        super(msg);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public void testContent() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        assertTrue(selenium.isTextPresent("Lifecycle Name"));
        assertTrue(selenium.isTextPresent("Actions"));
        assertTrue(selenium.isTextPresent("Lifecycles"));
        UmCommon.logOutUI();
    }

    public void testContentOftheServiceLC() throws Exception {
        String serviceLC = "<aspect name=\"ServiceLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\" location=\"/environment/init\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\" location=\"/environment/design\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\" location=\"/environment/development\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\" location=\"/environment/qa\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\" location=\"/environment/prod\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\" location=\"/environment/support\"> </state> </lifecycle> </configuration> </aspect>";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.viewLC("ServiceLifeCycle");
//        System.out.println(selenium.getText("payload"));
        assertEquals(selenium.getText("payload"), serviceLC);
        UmCommon.logOutUI();
    }

    public void testAddDuplicantServiceLC() throws Exception {
        String serviceLC = "<aspect name=\"ServiceLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\" location=\"/environment/init\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\" location=\"/environment/design\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\" location=\"/environment/development\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\" location=\"/environment/qa\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\" location=\"/environment/prod\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\" location=\"/environment/support\"> </state> </lifecycle> </configuration> </aspect>";
        String lcName = "ServiceLifeCycle";
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        lcCount = registryCommon.countLCs();
        registryCommon.addNewLC(lcName, serviceLC);
        assertEquals(lcCount, registryCommon.countLCs());
    }

    public void testEditContentServiceLC() throws Exception {
        String serviceNewLC = "<aspect name=\"ServiceLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\" location=\"/environment/init\"> <checkitem>Requirements Gathered test</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\" location=\"/environment/design\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\" location=\"/environment/development\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\" location=\"/environment/qa\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\" location=\"/environment/prod\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\" location=\"/environment/support\"> </state> </lifecycle> </configuration> </aspect>";
        String service_name = "testService";
        String namSp = "testNS";

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.editLC("ServiceLifeCycle", serviceNewLC);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(service_name, namSp, "", "");
        registryCommon.saveService();
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("lifecycleIconMinimized")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        registryCommon.addLifeCycle("ServiceLifeCycle");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Checklist:".equals(selenium.getText("//div[@id='aspectList']/table/tbody/tr[1]/td/div[2]/table/tbody/tr[3]/th")))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
//       assertTrue(selenium.isTextPresent("Requirements Gathered test"));
        UmCommon.logOutUI();
    }

    public void testEditUsingLC() throws Exception {
        String lcname = "ServiceLifeCycle";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        assertFalse(registryCommon.editLC(lcname, ""));
        UmCommon.logOutUI();
    }

    public void testContentSampleLC() throws Exception {
        String sampleLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        selenium.click("link=Add New Lifecycle");
        selenium.waitForPageToLoad("30000");
        assertEquals(selenium.getText("payload"), sampleLc);
        UmCommon.logOutUI();
    }

    public void testEditSampleLC() throws Exception {
        String sampleLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"sample Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        lcCount = registryCommon.countLCs();
        registryCommon.addNewLC("SampleLifeCycle", sampleLc);
        assertEquals(lcCount + 1, registryCommon.countLCs());
        UmCommon.logOutUI();
    }

    public void testBrowserRootNoIcon() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        assertFalse(selenium.isElementPresent("lifecycleIconMinimized"));
        assertFalse(selenium.isElementPresent("lifecycleIconExpanded"));
        UmCommon.logOutUI();
    }

    public void testPromoteDemoteCollection() throws Exception {
        String newLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"Test SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        String collectionName = "testLC";
        String lcName = "Test SampleLifeCycle";
        int id;
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName, "", "");
        lcCount = registryCommon.countLCs();
        registryCommon.addNewLC(lcName, newLc);
        assertEquals(lcCount + 1, registryCommon.countLCs());
        registryCommon.gotoBrowsePage();
        id = registryCommon.getId(collectionName);
        if (id == -1)
            assertFalse(true);
        else
            selenium.click("resourceView" + id);
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("lifecycleIconMinimized")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        registryCommon.addLifeCycle(lcName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        selenium.click("lifecycleIconMinimized");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Code Completed"));
        Thread.sleep(1000);
        assertTrue(registryCommon.demoteLC());
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("High Level Design Completed"));
        registryCommon.deleteLCFromBrowser();
        registryCommon.deleteColletion("/", collectionName);
        UmCommon.logOutUI();
    }

    public void testPromoteDemoteResource() throws Exception {
        String newLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"Test SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        String resourceName = "testLCText";
        String lcName = "Test SampleLifeCycle";
        int id;
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.gotoLifeCyclePage();
        if (!(selenium.isTextPresent("Test SampleLifeCycle"))) {
            lcCount = registryCommon.countLCs();
            registryCommon.addNewLC(lcName, newLc);
            assertEquals(lcCount + 1, registryCommon.countLCs());
        }
        registryCommon.gotoBrowsePage();
        id = registryCommon.getId(resourceName);
        if (id == -1)
            assertFalse(true);
        else
            selenium.click("resourceView" + id);
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("lifecycleIconMinimized")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        registryCommon.addLifeCycle(lcName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        selenium.click("lifecycleIconMinimized");
        assertTrue(selenium.isTextPresent("Code Completed"));
        assertTrue(registryCommon.demoteLC());
        assertTrue(selenium.isTextPresent("High Level Design Completed"));
        registryCommon.deleteLCFromBrowser();
        registryCommon.deleteColletion("/", resourceName);
        UmCommon.logOutUI();
    }

    public void testPromoteDemoteService() throws Exception {
        String newLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"Test SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        String serviceName = "testLCService";
        String lcName = "Test SampleLifeCycle";
        String lcNameSpace = "testLCNameSpace";
        String lcDescrip = "this is a test service to test service";
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName, lcNameSpace, "", lcDescrip);
        registryCommon.saveService();
//        registryCommon.fillServiceContacts();
        Thread.sleep(10000);
        registryCommon.gotoLifeCyclePage();
        if (!(selenium.isTextPresent("Test SampleLifeCycle"))) {
            lcCount = registryCommon.countLCs();
            registryCommon.addNewLC(lcName, newLc);
            assertEquals(lcCount + 1, registryCommon.countLCs());
        }
        registryCommon.gotoServicesPage();
        selenium.click("link=" + serviceName);
        Thread.sleep(5000);
        registryCommon.addLifeCycle(lcName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        Thread.sleep(1000);
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");
        Thread.sleep(1000);
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//table[@id='myTable']/tbody/tr[1]/td")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
//        assertTrue(selenium.isTextPresent("Code Completed"));
        assertTrue(selenium.getText("//table[@id='myTable']/tbody/tr[1]/td").equals("Code Completed"));
        assertTrue(registryCommon.demoteLC());
        assertTrue(selenium.isTextPresent("High Level Design Completed"));
        registryCommon.deleteLCFromBrowser();
        UmCommon.logOutUI();
    }

    public void testPromoteDemotePolicy() throws Exception {
        String newLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"Test SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        String lcName = "Test SampleLifeCycle";
        String policyUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String policyName = "RMpolicy3.xml";
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        if (!(selenium.isTextPresent("Test SampleLifeCycle"))) {
            lcCount = registryCommon.countLCs();
            registryCommon.addNewLC(lcName, newLc);
            assertEquals(lcCount + 1, registryCommon.countLCs());
        }
        registryCommon.AddPolicy(policyUrl, policyName);
        registryCommon.gotoPoliciesPage();
        selenium.click("link=" + policyName);
        selenium.waitForPageToLoad("30000");
        registryCommon.addLifeCycle(lcName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        selenium.click("lifecycleIconMinimized");
        assertTrue(selenium.isTextPresent("Code Completed"));
        assertTrue(registryCommon.demoteLC());
        assertTrue(selenium.isTextPresent("High Level Design Completed"));
        registryCommon.deleteLCFromBrowser();
        UmCommon.logOutUI();
    }

    public void testPromoteDemoteWsdl() throws Exception {
        String newLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"Test SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        String lcName = "Test SampleLifeCycle";
        String wsdlUrl = "http://ww2.wso2.org/~charitha/wsdls/calculator_import_schema.wsdl";
        String wsdlName = "calculator_import_schema.wsdl";
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        if (!(selenium.isTextPresent("Test SampleLifeCycle"))) {
            lcCount = registryCommon.countLCs();
            registryCommon.addNewLC(lcName, newLc);
            assertEquals(lcCount + 1, registryCommon.countLCs());
        }
        registryCommon.AddWSDL(wsdlUrl, wsdlName);
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        registryCommon.gotoWSDLsPage();
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        registryCommon.addLifeCycle(lcName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        selenium.click("lifecycleIconMinimized");
        assertTrue(selenium.isTextPresent("Code Completed"));
        assertTrue(registryCommon.demoteLC());
        assertTrue(selenium.isTextPresent("High Level Design Completed"));
        registryCommon.deleteLCFromBrowser();
        UmCommon.logOutUI();
    }

    public void testPromoteDemoteSchema() throws Exception {
        String newLc = "<!-- Following is a sample Life Cycle. Please edit it according to your requirements --> <aspect name=\"Test SampleLifeCycle\" class=\"org.wso2.carbon.governance.registry.extensions.aspects.ChecklistLifeCycle\"> <configuration type=\"literal\"> <lifecycle> <state name=\"Initialize\"> <checkitem>Requirements Gathered</checkitem> <checkitem>Architecture Finalized</checkitem> <checkitem>High Level Design Completed</checkitem> </state> <state name=\"Designed\"> <checkitem>Code Completed</checkitem> <checkitem>WSDL, Schema Created</checkitem> <checkitem>QoS Created</checkitem> </state> <state name=\"Created\"> <checkitem>Effective Inspection Completed</checkitem> <checkitem>Test Cases Passed</checkitem> <checkitem>Smoke Test Passed</checkitem> </state> <state name=\"Tested\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deployed\"> <checkitem>Service Configuration</checkitem> </state> <state name=\"Deprecated\"> </state> </lifecycle> </configuration> </aspect>";
        String lcName = "Test SampleLifeCycle";
        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator.xsd";
        String schemaName = "calculator.xsd";
        int lcCount;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoLifeCyclePage();
        if (!(selenium.isTextPresent("Test SampleLifeCycle"))) {
            lcCount = registryCommon.countLCs();
            registryCommon.addNewLC(lcName, newLc);
            assertEquals(lcCount + 1, registryCommon.countLCs());
        }
        registryCommon.addSchema(schemaUrl, schemaName);
        assertTrue(registryCommon.checkSchema_Exsits(schemaName));
        registryCommon.gotoSchemasPage();
        selenium.click("link=" + schemaName);
        selenium.waitForPageToLoad("30000");
        registryCommon.addLifeCycle(lcName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        selenium.click("lifecycleIconMinimized");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Code Completed"));
        Thread.sleep(1000);
        assertTrue(registryCommon.demoteLC());
        assertTrue(selenium.isTextPresent("High Level Design Completed"));
        registryCommon.deleteLCFromBrowser();
        UmCommon.logOutUI();
    }

    public void testFinalize() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();

    }
}