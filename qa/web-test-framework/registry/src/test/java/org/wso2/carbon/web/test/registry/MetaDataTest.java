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

package org.wso2.carbon.web.test.registry;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;


public class MetaDataTest extends TestCase {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed, NewSpeed;

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public MetaDataTest(String txt) {
        super(txt);
//        Curspeed =selenium.getSpeed();
//        NewSpeed="20";
    }

    ///////////////////////////////////////////// WSDL Test Methods Start ///////////////////////////////////////////////////////
    public void testWSDLContent() throws Exception {     // checks the add wsdl web page content
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoWSDLPage();
        assertTrue(selenium.isTextPresent("WSDL Operations"));
        assertTrue(selenium.isTextPresent("Add"));
        assertTrue(selenium.isTextPresent("WSDL URL"));
        assertTrue(selenium.isTextPresent("Name"));
        UmCommon.logOutUI();
    }

    public void testWSDL_IncUrl() throws Exception {
//        selenium.setSpeed(Curspeed);
        String falseUrl = "test";
        String falseName = "test";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        assertFalse(registryCommon.AddWSDL(falseUrl, ""));
        assertFalse(registryCommon.checkWSDL_Exsits(falseName));
        UmCommon.logOutUI();

    }

    public void testImportSimpleWsdl() throws Exception {
        String wsdlURL = "http://geocoder.us/dist/eg/clients/GeoCoder.wsdl";
        String name = "GeoCoder.wsdl";
        String serviceName = "GeoCode_Service";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, name);
        assertTrue(registryCommon.checkWSDL_Exsits(name));
        registryCommon.deleteWSDL(name);
        Thread.sleep(500);
        registryCommon.deleteService(serviceName);
        Thread.sleep(1000);

        UmCommon.logOutUI();
    }

    public void testWsdlImpAnotherWsdl() throws Exception {
        String wsdlURL = "http://131.107.72.15/Security_WsSecurity_Service_Indigo/WSSecureConversation.svc?wsdl";
        String name = "WSSecureConversation.svc.wsdl";
        String impWSDLname = "WSSecureConversation.wsdl";
        String serveiceName = "PingService";
        String schema = "WSSecureConversation.xsd";
        String schema1 = "WSSecureConversation1.xsd";
        String schema2 = "WSSecureConversation2.xsd";
        String schema3 = "WSSecureConversation3.xsd";

        String namSpschema3 = "";
        String namSpschema = "http://xmlsoap.org/Ping";
        String namSpschema2 = "http://schemas.microsoft.com/2003/10/Serialization/";
        String namSpschema1 = "http://InteropBaseAddress/interop";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, name);
        Thread.sleep(1000);
        assertTrue(registryCommon.checkWSDL_Exsits(name));
        assertTrue(registryCommon.checkWSDL_Exsits(impWSDLname));
        assertTrue(registryCommon.checkService_Exsits(serveiceName));
        assertTrue(registryCommon.checkSchema_Exsits(schema));
        assertTrue(registryCommon.checkSchema_Exsits(schema1));
        assertTrue(registryCommon.checkSchema_Exsits(schema2));
        assertTrue(registryCommon.checkSchema_Exsits(schema3));
        // check the integrity of the imported ones
        registryCommon.deleteService(serveiceName);
        Thread.sleep(1000);
        registryCommon.deleteWSDL(impWSDLname);
        Thread.sleep(1000);
        registryCommon.deleteWSDL(name);
        Thread.sleep(1000);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }


    public void testWsdlSchemaImport() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String name = "ComplexDataTypesDocLitW.svc.wsdl";
        String serviceName = "ComplexDataTypesDocLitWService";
        String schema = "ComplexDataTypesDocLitW.xsd";
        String schema1 = "ComplexDataTypesDocLitW1.xsd";
        String schema2 = "ComplexDataTypesDocLitW2.xsd";
        String schema3 = "ComplexDataTypesDocLitW3.xsd";
        String schema4 = "ComplexDataTypesDocLitW4.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, name);

        // selenium.click("link="+name);
        // registryCommon.checkDependencyTable()
        // check the dependency,association
        assertTrue(registryCommon.checkSchema_Exsits(schema));
        assertTrue(registryCommon.checkSchema_Exsits(schema1));
        assertTrue(registryCommon.checkSchema_Exsits(schema2));
        assertTrue(registryCommon.checkSchema_Exsits(schema3));
        assertTrue(registryCommon.checkSchema_Exsits(schema4));
        assertTrue(registryCommon.checkService_Exsits(serviceName));
        registryCommon.deleteService(serviceName);
        Thread.sleep(1000);
        registryCommon.deleteWSDL(name);
        Thread.sleep(2000);
        registryCommon.deleteSchema(schema3);
        Thread.sleep(1000);
        registryCommon.deleteSchema(schema);
        Thread.sleep(1000);
        registryCommon.deleteSchema(schema1);
        Thread.sleep(1000);
        registryCommon.deleteSchema(schema2);
        Thread.sleep(1000);
        registryCommon.deleteSchema(schema4);
        Thread.sleep(1000);
        UmCommon.logOutUI();

    }

    public void testWsdlCyclicImport() throws Exception {
        String wsdlURL = "http://131.107.72.15/Security_WsSecurity_Service_Indigo/WSSecureConversation.svc?wsdl";
        String name = "WSSecureConversation.svc.wsdl";
        String impWSDLname = "WSSecureConversation.wsdl";
        String serveiceName = "PingService";
        String schema = "WSSecureConversation.xsd";
        String schema1 = "WSSecureConversation1.xsd";
        String schema2 = "WSSecureConversation2.xsd";
        String schema3 = "WSSecureConversation3.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, name);
        registryCommon.gotoWSDLsPage();
        // selenium.click("link="+impWSDLname);
        // check the dependency

        UmCommon.logOutUI();
    }

    public void testWsdlBrokenLink() throws Exception {
        String wsdlURL = "http://wso2.org/HelloService.wsdl";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        assertFalse(registryCommon.AddWSDL(wsdlURL, ""));
        UmCommon.logOutUI();
    }

    public void testWsdlImportOffLine() throws Exception {
        // complete the function


    }

    public void testWsdlImportNullContent() throws Exception {      /// imports a null conntent and
        // check whats happening
        String wsdlURL = "http://ws.strikeiron.com/ypcom/yp1?WSDL";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        assertFalse(registryCommon.AddWSDL(wsdlURL, ""));
        UmCommon.logOutUI();
    }

    public void testWsdlEndsUpperCase() throws Exception {
        String wsdlURL = "http://ws.strikeiron.com/donotcall2_5?WSDL";
        String wsdlName = "donotcall2_5.WSDL";
        String serveiceName = "DoNotCallRegistry";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddWSDL(wsdlURL, "");
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        Thread.sleep(1000);
        registryCommon.deleteWSDL(wsdlName);
        Thread.sleep(1000);
        registryCommon.deleteService(serveiceName);
        Thread.sleep(1000);
        UmCommon.logOutUI();

    }

    public void testInvalidFile() throws Exception {
        String wsdlURL = "http://wordpress.org/extend/plugins/about/readme.txt";
        String wsdlName = "readme.txt";
        registryCommon.signOut();
//        selenium.setSpeed(Curspeed);
        UmCommon.loginToUI(adminUserName, adminPassword);
        assertFalse(registryCommon.AddWSDL(wsdlURL, wsdlName));
        assertFalse(registryCommon.checkWSDL_Exsits(wsdlName));
        UmCommon.logOutUI();
    }

    public void testDocLitBService() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitB.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitB.svc.wsdl";
        String serviceName = "ComplexDataTypesDocLitBService";
//        String schema="ComplexDataTypesDocLitB.xsd";
//        String schema1="ComplexDataTypesDocLitB1.xsd";
//        String schema2="ComplexDataTypesDocLitB2.xsd";
//        String schema3="ComplexDataTypesDocLitB3.xsd";
//        String schema4="ComplexDataTypesDocLitB4.xsd";

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, wsdlName);
        Thread.sleep(1000);
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        Thread.sleep(1000);
        registryCommon.deleteService(serviceName);
        Thread.sleep(1000);
        registryCommon.deleteWSDL(wsdlName);
        Thread.sleep(1000);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();

    }

    public void testDocLitWService() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        String serviceName = "ComplexDataTypesDocLitBService";
//        String schema="ComplexDataTypesDocLitW.xsd";
//        String schema1="ComplexDataTypesDocLitW1.xsd";
//        String schema2="ComplexDataTypesDocLitW2.xsd";
//        String schema3="ComplexDataTypesDocLitW3.xsd";
//        String schema4="ComplexDataTypesDocLitW4.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, wsdlName);
        registryCommon.checkWSDL_Exsits(wsdlName);
        Thread.sleep(1000);
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        registryCommon.deleteService(serviceName);
        Thread.sleep(1000);
        registryCommon.deleteWSDL(wsdlName);
        Thread.sleep(1000);

        UmCommon.logOutUI();

    }

    public void testDocRpcLitService() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesRpcLit.svc?wsdl";
        String wsdlName = "ComplexDataTypesRpcLit.svc.wsdl";
//        String serviceName="ComplexDataTypesDocLitBService";
//        String schema="ComplexDataTypesRpcLit.xsd";
//        String schema1="ComplexDataTypesRpcLit1.xsd";
//        String schema2="ComplexDataTypesRpcLit2.xsd";
//        String schema3="ComplexDataTypesRpcLit3.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, wsdlName);
        Thread.sleep(1000);
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        UmCommon.logOutUI();
    }

    public void testImportWsdlAssociations() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        String serviceName = "ComplexDataTypesDocLitBService";
//        String schema="ComplexDataTypesDocLitW.xsd";
//        String schema1="ComplexDataTypesDocLitW1.xsd";
//        String schema2="ComplexDataTypesDocLitW2.xsd";
//        String schema3="ComplexDataTypesDocLitW3.xsd";
//        String schema4="ComplexDataTypesDocLitW4.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, wsdlName);
        Thread.sleep(1000);
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkAssociationTable("/governance/services/http/tempuri/org/ComplexDataTypesDocLitWService"));
        registryCommon.clickAssocitionTreelinks("/governance/services/http/tempuri/org/ComplexDataTypesDocLitWService");
        registryCommon.deleteService(serviceName);
        Thread.sleep(1000);
        registryCommon.deleteWSDL(wsdlName);
        Thread.sleep(1000);

        UmCommon.logOutUI();
    }

    public void testDeleteDependency() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        int depCount = 0;
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, "");
//        Thread.sleep(1000);
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        depCount = registryCommon.countDependencies();
        registryCommon.removeDependency("/governance/wsdls/http/tempuri/org/ComplexDataTypesDocLitW.svc.wsdl", "/governance/schemas/http/tempuri/org/ComplexDataTypesDocLitW4.xsd");
        assertEquals(depCount - 1, registryCommon.countDependencies());

        UmCommon.logOutUI();

    }

    public void testAddNewDepenToWsdl() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        String resourceName = "testDependency";
        String sourceResounce = "/governance/wsdls/http/tempuri/org/ComplexDataTypesDocLitW.svc.wsdl";
        int depCount = 0;
        String mediaType = "not specified";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();

        registryCommon.deleteColletion("/", resourceName);
        registryCommon.gotoWSDLsPage();     // dummy method
        registryCommon.gotoBrowsePage();
        Thread.sleep(1000);
        registryCommon.addCollectionToRoot(resourceName, mediaType, "");
        registryCommon.AddWSDL(wsdlURL, "");
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        depCount = registryCommon.countDependencies();
        registryCommon.addDependency("/" + resourceName);
        assertEquals(depCount + 1, registryCommon.countDependencies());
        assertTrue(registryCommon.checkDependencyTable(resourceName));
        registryCommon.removeDependency(sourceResounce, "/" + resourceName);
        registryCommon.deleteColletion("/", resourceName); // replace is used to remove the
        UmCommon.logOutUI();
    }

    public void testDelAssociation() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        String resourceName = "/governance/wsdls/http/tempuri/org/ComplexDataTypesDocLitW.svc.wsdl";
        String type = "usedby";
        String associationLink = "/governance/services/http/tempuri/org/ComplexDataTypesDocLitWService";
        int assoCount = 0;
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.AddWSDL(wsdlURL, "");
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assoCount = registryCommon.countAssociations();
        registryCommon.removeAssociation(resourceName, type, associationLink);
        assertEquals(assoCount - 1, registryCommon.countAssociations());
        UmCommon.logOutUI();

    }

    public void testAddAssociation() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        String associationPath = "/testassociation";
        String associationName = "testassociation";
        int assoCount = 0;
//        selenium.setSpeed(Curspeed);
        String associationType = "testAssociation";
        String mediaType = "not specified";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteColletion("/", associationName);
        registryCommon.gotoWSDLsPage();
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(associationName, mediaType, "");
        registryCommon.AddWSDL(wsdlURL, "");
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assoCount = registryCommon.countAssociations();
        registryCommon.addAssociationResource(associationType, associationPath);
        assoCount += 1;
        assertEquals(assoCount, registryCommon.countAssociations());
//   assertTrue(registryCommon.checkAssociationTable(associationPath));
//        registryCommon.removeAssociation()
        registryCommon.gotoResourcePage();
        registryCommon.clickRootIcon();
        registryCommon.deleteColletion("/", associationName);

        UmCommon.logOutUI();
    }

    public void testAddLifeCycle() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
        String wsdlName = "ComplexDataTypesDocLitW.svc.wsdl";
        String lifeCycleName = "ServiceLifeCycle";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddWSDL(wsdlURL, "");
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.addLifeCycle(lifeCycleName));
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertTrue(registryCommon.deleteLifeCycle());
        UmCommon.logOutUI();
    }

    public void setPermissions() throws Exception {
        String wsdlURL = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitW.svc?wsdl";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddWSDL(wsdlURL, "");

    }

    ////////////////////////////////////////////////WSDL finish ///////////////////////////
    ////////////////////////////////////////////////Policy Start ///////////////////////////
    public void checkPolicyContent() throws Exception {
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoPolicyPage();
        assertTrue(selenium.isTextPresent("Policy Operations"));
        assertTrue(selenium.isTextPresent("Policy URL"));
        assertTrue(selenium.isTextPresent("Name"));
        registryCommon.gotoPoliciesPage();
        assertTrue(selenium.isTextPresent("Policy List"));
        UmCommon.logOutUI();
    }

    public void testIncPolicy() throws Exception {
        String policyName = "test";
        String policyUrl = "test policy url";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        assertFalse(registryCommon.AddPolicy(policyUrl, policyName));
        UmCommon.logOutUI();

    }

    public void testImportSimplePolicy() throws Exception {
        String policyName = "RMpolicy3.xml";
        String policyUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String resourceName = "/testDependencypolicy1";
        String sourceResounce = "testDependencypolicy1";
        String lifeCycleName = "ServiceLifeCycle";
        int depCount = 0;
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        assertTrue(registryCommon.AddPolicy(policyUrl, policyName));
        assertTrue(registryCommon.checkPolicy_Exsits(policyName));
        selenium.click("link=" + policyName);
        selenium.waitForPageToLoad("30000");
        depCount = registryCommon.countDependencies();
        registryCommon.addDependency(resourceName);
        assertEquals(depCount + 1, registryCommon.countDependencies());
        assertTrue(registryCommon.checkDependencyTable(resourceName));
        //registryCommon.removeDependency(sourceResounce,resourceName);
        assertTrue(registryCommon.addLifeCycle(lifeCycleName));
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertTrue(registryCommon.deleteLifeCycle());
        registryCommon.deletePolicy(policyName);

        registryCommon.gotoResourcePage();
        int actionId2 = registryCommon.getActionID(resourceName);
        assertTrue(registryCommon.deleteResource(resourceName, actionId2));

        Thread.sleep(1000);
        UmCommon.logOutUI();

    }
    ///////////////////////////////////////////////////////////////Policy finish //////////////////////////

    ///////////////////////////////////////////////////////////////WSDL Validation ////////////////////////

    public void testWsdlValidation() throws Exception {
        String wsdlName = "calculator_import_schema.wsdl";
        String wsdlUrl = "http://ww2.wso2.org/~charitha/wsdls/calculator_import_schema.wsdl";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        assertTrue(registryCommon.AddWSDL(wsdlUrl, wsdlName));
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Valid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Valid"));
        UmCommon.logOutUI();
    }
//    public void testCorrectnessValidWsdl() throws Exception {
//        String wsdlName="calculator_import_schema.wsdl";
//        String wsdlUrl="http://ww2.wso2.org/~charitha/wsdls/calculator_import_schema.wsdl";
//        registryCommon.signOut();
//        UmCommon.loginToUI("admin","admin");
//        registryCommon.deleteAllPolicies();
//        registryCommon.deleteAllSchemas() ;
//        registryCommon.deleteAllServices();
//        registryCommon.deleteAllWsdls();
//        assertTrue(registryCommon.AddWSDL(wsdlUrl,wsdlName) );
//        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
//        selenium.click("link="+wsdlName );
//        selenium.waitForPageToLoad("30000");
//        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation","Valid"));
//        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation","Valid"));
//        UmCommon.logOutUI();
//    }

    public void testCorrectnessInvalidWsdl() throws Exception {
        String wsdlName = "invalid-policy-ref-wsdl.wsdl";
        String wsdlUrl = "http://ww2.wso2.org/~charitha/wsdls/invalid-policy-ref-wsdl.wsdl";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        assertTrue(registryCommon.AddWSDL(wsdlUrl, wsdlName));
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Valid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));
        UmCommon.logOutUI();
    }

    public void testCorrectnessWsdlImportSchema() throws Exception {
        String wsdlName = "BizService.wsdl";
        String wsdlUrl = "http://ww2.wso2.org/~prabath/BizService.wsdl";
        String schemaName = "purchasing.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        assertTrue(registryCommon.AddWSDL(wsdlUrl, wsdlName));
        assertTrue(registryCommon.checkWSDL_Exsits(wsdlName));
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Valid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Valid"));
        registryCommon.gotoSchemasPage();
        selenium.click("link=" + schemaName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("targetNamespace", "http://bar.org/purchasing"));
        UmCommon.logOutUI();
    }

    public void testCorrectnessSchema() throws Exception {

        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator.xsd";
        String schemaName = "calculator.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        assertTrue(registryCommon.addSchema(schemaUrl, schemaName));
        assertTrue(registryCommon.checkSchema_Exsits(schemaName));
        selenium.click("link=" + schemaName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("Schema Validation", "Valid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("targetNamespace", "http://charitha.org/"));
        UmCommon.logOutUI();
    }

    public void testIncSchema() throws Exception {

        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator-no-element-name-invalid.xsd";
        String schemaName = "calculator-no-element-name-invalid.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        assertTrue(registryCommon.addSchema(schemaUrl, schemaName));
        assertTrue(registryCommon.checkSchema_Exsits(schemaName));
        selenium.click("link=" + schemaName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("Schema Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("targetNamespace", "http://charitha.org/"));
        UmCommon.logOutUI();
    }

    /* public void testCyclicImportSchema() throws Exception {

            String schemaUrl = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitB.svc?xsd=xsd2";
            String schemaName = "ComplexDataTypesDocLitB.svc.xsd=xsd2";
            String importSchemaName = "ComplexDataTypesDocLitB1.xsd";
    //        selenium.setSpeed(Curspeed);
            registryCommon.signOut();
            UmCommon.loginToUI(adminUserName, adminPassword);
            registryCommon.deleteAllPolicies();
            registryCommon.deleteAllSchemas();
            registryCommon.deleteAllServices();
            registryCommon.deleteAllWsdls();
            registryCommon.addSchema(schemaUrl, schemaName);
            assertTrue(registryCommon.checkSchema_Exsits(schemaName));
            assertTrue(registryCommon.checkSchema_Exsits(importSchemaName));
            selenium.click("link=" + schemaName);
            selenium.waitForPageToLoad("30000");
            assertTrue(registryCommon.checkPropertiesOfWSDL("Schema Validation", "Valid"));
            assertTrue(registryCommon.checkPropertiesOfWSDL("targetNamespace", "http://tempuri.org/"));
            registryCommon.gotoSchemasPage();
            selenium.click("link=" + importSchemaName);
            selenium.waitForPageToLoad("30000");
            assertTrue(registryCommon.checkPropertiesOfWSDL("targetNamespace", "http://schemas.datacontract.org/2004/07/System"));
            registryCommon.deleteAllPolicies();
            registryCommon.deleteAllSchemas();
            registryCommon.deleteAllServices();
            registryCommon.deleteAllWsdls();
            UmCommon.logOutUI();
        }
    */
    public void testImportWsdlFromLocalFileSystem() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
//        System.out.println(path);
        String wsdlPath = "/lib/wsdl/use-encoded-wsdl.wsdl";
        String wsdlName = "use-encoded-wsdl.wsdl";
        String wsdlPathName = "/" + wsdlName;
        int id = 4;
//        selenium.setSpeed(Curspeed);
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        Thread.sleep(1000);
        registryCommon.addResFromLocalFS(path + wsdlPath, wsdlName);
        assertTrue(registryCommon.searchResources(wsdlPathName));
        registryCommon.checkWSDL_Exsits(wsdlName);
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));
        registryCommon.gotoBrowsePage();
        id = registryCommon.getId(wsdlName);
        selenium.click("resourceView" + id);
        selenium.waitForPageToLoad("80000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));
        registryCommon.gotoResourcePage();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteResource("/", wsdlName);
        UmCommon.logOutUI();
    }

    public void testImportWsdlWithQuestionMarkNamespacesFromLocalFileSystem() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
//        System.out.println(path);
        String wsdlPath = "/lib/wsdl/use-encoded-wsdl-qmark.wsdl";
        String wsdlName = "use-encoded-wsdl.wsdl";
        String wsdlPathName = "/" + wsdlName;
        int id = 4;
//        selenium.setSpeed(Curspeed);
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        Thread.sleep(1000);
        registryCommon.addResFromLocalFS(path + wsdlPath, wsdlName);
        assertTrue(registryCommon.searchResources(wsdlPathName));
        registryCommon.checkWSDL_Exsits(wsdlName);
        /*selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));
        registryCommon.gotoBrowsePage();
        id = registryCommon.getId(wsdlName);
        selenium.click("resourceView" + id);
        selenium.waitForPageToLoad("80000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));*/
        registryCommon.gotoResourcePage();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteResource("/", wsdlName);
        UmCommon.logOutUI();
    }

    public void testImportWsdlFromGWA() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
//        System.out.println(path);
        String wsdlPath = "/lib/gwa/authenticate.gwa";
        String wsdlName = "authenticate_.wsdl";
        String wsdlPathName = "/" + wsdlName;
        int id = 4;
//        selenium.setSpeed(Curspeed);
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        Thread.sleep(1000);
        registryCommon.addResFromLocalFS(path + wsdlPath, wsdlName);
        assertTrue(registryCommon.searchResources(wsdlPathName));
        registryCommon.checkWSDL_Exsits(wsdlName);
        /*selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("30000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));
        registryCommon.gotoBrowsePage();
        id = registryCommon.getId(wsdlName);
        selenium.click("resourceView" + id);
        selenium.waitForPageToLoad("80000");
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSDL Validation", "Invalid"));
        assertTrue(registryCommon.checkPropertiesOfWSDL("WSI Validation", "Invalid"));*/
        registryCommon.gotoResourcePage();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteResource("/", wsdlName);
        UmCommon.logOutUI();
    }

    public void testAddMetaDataFromFS() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
        String wsdlURL = "file://" + path + "/lib/wsdl/use-encoded-wsdl-qmark.wsdl";
        String schemaURL = "file://" + path + "/lib/schema/calculator.xsd";
        String policyURL = "file://" + path + "/lib/resources/sts.policy.xml";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoWSDLPage();
        assertTrue(addMetaDataFromFSFailure(wsdlURL, "Please wait until the WSDL is validated " +
                "and added..."));
        registryCommon.gotoPolicyPage();
        assertTrue(addMetaDataFromFSFailure(policyURL, "Please wait until the Policy is " +
                "added..."));
        registryCommon.gotoSchemaPage();
        assertTrue(addMetaDataFromFSFailure(schemaURL, "Please wait until the Schema is " +
                "validated and added..."));
        UmCommon.logOutUI();
    }

    public void testImportMetaDataFromFS() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
        String wsdlURL = "file://" + path + "/lib/wsdl/use-encoded-wsdl-qmark.wsdl";
        String schemaURL = "file://" + path + "/lib/schema/calculator.xsd";
        String policyURL = "file://" + path + "/lib/resources/sts.policy.xml";
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        Thread.sleep(1000);
        selenium.click("stdView");
        Thread.sleep(1000);
        importMetaDataFromFSFailure(wsdlURL, null);
        importMetaDataFromFSFailure(policyURL, null);
        importMetaDataFromFSFailure(schemaURL, null);
        UmCommon.logOutUI();
    }

    public void testImportFakeSchemaFromFS() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
        String schemaURL = "file://" + path + "/lib/resources/sts.policy.xml";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoResourcePage();
        Thread.sleep(1000);
        selenium.click("stdView");
        Thread.sleep(1000);
        importMetaDataFromFSFailure(schemaURL, "application/x-xsd+xml");
        UmCommon.logOutUI();
    }

    private void importMetaDataFromFSFailure(String url, String mediaType)
            throws InterruptedException {
        selenium.click("stdView");
        Thread.sleep(500);
        selenium.click("link=Add Resource");
        selenium.select("addMethodSelector", "label=Import content from URL");
        selenium.type("irFetchURL", url);
        if (mediaType != null) {
            selenium.type("irMediaType", mediaType);
        }
        selenium.type("irDescription", "test");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitImportContentForm();']");
        Thread.sleep(10000);
        registryCommon.setWindowfocus();
        assertTrue(selenium.isTextPresent("The source URL must not be file in the server's " +
                        "local file system"));
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
    }

    private boolean addMetaDataFromFSFailure(String url, String message)
            throws InterruptedException {
        selenium.type("irFetchURL", url);
        selenium.click("//input[@value='Add']");
//        while(selenium.isTextPresent("Please wait until the WSDL is validated and added..."))
//            Thread.sleep(2000);
//
//        Thread.sleep(1000);
        for (int second = 0; ; second++) {
            if (second >= 240) {
                fail("timeout");
            }
            try {
                if (!selenium
                        .isTextPresent(message)) {
                    break;
                }
                // Sometimes the 'please wait' text may be hidden from the user, but present in
                // the html source.
                else if (selenium.isTextPresent("The source URL must not be file in the server's " +
                        "local file system") ) {
                    selenium.click("//button[@type='button']");
                    return true;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        return false;
    }

    public void testInvalidSchemaScyclicImportLocalFileSystem() throws Exception {
        String schemaPath = "http://ww2.wso2.org/~qa/greg/calculator.xsd";
        String schemaName = "calculator.xsd";
        String schemaImportedName = "calculator-no-element-name-invalid.xsd";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllSchemas();
        registryCommon.addSchema(schemaPath, schemaName);
        registryCommon.checkSchema_Exsits(schemaName);
        selenium.click("link=" + schemaName);
        selenium.waitForPageToLoad("30000");
        registryCommon.checkPropertiesOfWSDL("Schema Validation", "Invalid");
        registryCommon.gotoPolicyPage();
        registryCommon.checkSchema_Exsits(schemaImportedName);
        selenium.click("link=" + schemaImportedName);
        selenium.waitForPageToLoad("30000");
        registryCommon.checkPropertiesOfWSDL("targetNamespace", "http://charitha.org1/");
        registryCommon.deleteAllSchemas();
        UmCommon.logOutUI();

    }

    ///////////////////////////////////////////////// Service  Management ////////////////////
    public void testCheckServiceContent() throws Exception {
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
//        assertTrue(selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]").matches("^exact:Name[\\s\\S]*$"));
//        assertTrue(selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[2]/td[1]").matches("^exact:Namespace[\\s\\S]*$"));
        assertEquals("States", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[3]/td[1]"));
        assertEquals("Description", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[4]/td[1]"));
        assertEquals("WSDL URL", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[11]/td[1]"));
        assertEquals("Transport Protocols", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[12]/td[1]"));
        assertEquals("Message Formats", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[13]/td[1]"));
        assertEquals("Message Exchange Pattern", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[14]/td[1]"));
        assertEquals("Authentication Platform", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[16]/td[1]"));
        assertEquals("Authentication Mechanism", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[17]/td[1]"));
        assertEquals("Authorization Platform", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[18]/td[1]"));
        assertEquals("Message Integrity", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[19]/td[1]"));
        assertEquals("Message Encryption", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[20]/td[1]"));
        assertEquals("Comments", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[21]/td[1]"));
        assertEquals("Document Type", selenium.getText("//form[@id='CustomUIForm']/table/tbody/tr[1]/td/table/tbody/tr[26]/td[1]"));
        UmCommon.logOutUI();

    }

    public void testInvServiceNoName_NoNS() throws Exception {
        String serviceName = "";
        String wsdl = "";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName, wsdl, "", "");
        registryCommon.saveService();
        assertEquals("The required field Name has not been filled in.\nThe required field Namespace has not been filled in.",
                selenium.getText("//div[@id='messagebox-warning']/p"));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testInvServiceNoName() throws Exception {
        String serviceName = "";
        String wsdl = "http://ww2.wso2.org/~prabath/BizService.wsdl";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName, wsdl, "", "");
        registryCommon.saveService();
        assertEquals("The required field Name has not been filled in.",
                selenium.getText("//div[@id='messagebox-warning']/p"));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testInvServiceNoNS() throws Exception {
        String serviceName = "test Service";
        String wsdl = "";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName, wsdl, "", "");
        registryCommon.saveService();
        assertEquals("The required field Namespace has not been filled in.",
                selenium.getText("//div[@id='messagebox-warning']/p"));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();

    }

    public void testValidService() throws Exception {
        String serviceName = "test Service";
	String nameSpace="http://foo.com";
        String wsdl = "http://ww2.wso2.org/~prabath/BizService.wsdl";
//        selenium.setSpeed(Curspeed);
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName,nameSpace, "", "");
        registryCommon.saveService();
        UmCommon.logOutUI();
    }

}
