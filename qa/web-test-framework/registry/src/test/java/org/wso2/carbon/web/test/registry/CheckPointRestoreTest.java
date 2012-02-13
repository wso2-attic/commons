package org.wso2.carbon.web.test.registry;

import com.thoughtworks.selenium.Selenium;

import java.util.Properties;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class CheckPointRestoreTest extends CommonSetup{
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    public CheckPointRestoreTest(String txt) {
        super(txt);
    }
    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }
    public void testRoot() throws Exception {
        int version1,version2 ;
        String collectionName="CheckPoint";
        String[] resources1={"carbon","governance","system"};
        String[] resources2={"carbon","governance","system",collectionName};

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        assertEquals(-1,registryCommon.getVersion());
        registryCommon.gotoBrowsePage();
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        registryCommon.gotoBrowsePage();
        System.out.println("current version "+version1);
        assertTrue(registryCommon.checkVersionDetails(version1,resources1));
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        registryCommon.gotoBrowsePage();
        assertTrue(registryCommon.checkVersionDetails(version2,resources2));
        registryCommon.gotoBrowsePage();
        registryCommon.restoreVersion(version1);
        assertFalse(registryCommon.gotopath("/",collectionName));
        registryCommon.deleteColletion("/",collectionName);
        UmCommon.logOutUI();
    }
    public void testCollection() throws Exception {
        int version1,version2 ;
        String collectionName="CheckPointTestCollection";
        String resourceName="TestRes";
        String[] resources1={"carbon","governance","system"};
        String[] resources2={"TestRes"};
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry();
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        assertTrue(registryCommon.gotopath("/",collectionName));
        assertEquals(-1,registryCommon.getVersion());
        assertTrue(registryCommon.gotopath("/",collectionName));
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        assertTrue(registryCommon.gotopath("/",collectionName));
        System.out.println("current version "+version1);
        assertFalse(registryCommon.checkVersionDetails(version1,resources1));
        assertTrue(registryCommon.gotopath("/",collectionName));
        registryCommon.addTextResource(resourceName) ;
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        assertTrue(registryCommon.gotopath("/",collectionName));
        assertTrue(registryCommon.checkVersionDetails(version2,resources2));
        assertTrue(registryCommon.gotopath("/",collectionName));
        registryCommon.restoreVersion(version1);
        assertFalse(registryCommon.gotopath("/"+collectionName,resourceName));
        registryCommon.deleteColletion("/",collectionName);
        UmCommon.logOutUI();
    }
    public void testResource() throws Exception {
        int version1,version2 ;
        String resourceName="CheckPointTestResource";
        String propertyName="testPropertyName";
        String propertyValue="testPropertyValue";
        String[] resources1={"carbon","governance","system"};
        String content="this is a sample content";
//        String[] resources2={resourceName};

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry();
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResource(resourceName);
        assertTrue(registryCommon.gotopath("/",resourceName));
        assertEquals(-1,registryCommon.getVersion());
        assertTrue(registryCommon.gotopath("/",resourceName));
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        assertTrue(registryCommon.gotopath("/",resourceName));
        System.out.println("current version "+version1);
        assertFalse(registryCommon.checkVersionResourceProperties(version1,"",propertyName,propertyValue));
        assertTrue(registryCommon.gotopath("/",resourceName));
        assertFalse(registryCommon.checkVersionResourceContent(version1,"",content));
        assertTrue(registryCommon.gotopath("/",resourceName));
        registryCommon.addProperty(propertyName,propertyValue);
        registryCommon.editResourceContent(content);
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        assertTrue(registryCommon.gotopath("/",resourceName));
        assertTrue(registryCommon.checkVersionResourceProperties(version2,"",propertyName,propertyValue));
        assertTrue(registryCommon.gotopath("/",resourceName));
        assertTrue(registryCommon.checkVersionResourceContent(version2,"",content));
        assertTrue(registryCommon.gotopath("/",resourceName));
        registryCommon.restoreVersion(version1);
        assertTrue(registryCommon.gotopath("/",resourceName));
        assertFalse(registryCommon.checkProperties(propertyName,propertyValue));
        assertTrue(registryCommon.gotopath("/",resourceName));
        assertFalse(registryCommon.checkResourceContent(content));
        registryCommon.deleteColletion("/",resourceName);
        UmCommon.logOutUI();
    }
    /* public void testServiceProperty() throws Exception {           // when the services module is fixed
                                                                    // enabl this test
        int version1,version2 ;
        String collectionName="CheckPointTestResource";
        String propertyName="testPropertyName";
        String propertyValue="testPropertyValue";
        String[] resources1={"carbon","governance","system"};
        String serviceName="test serice";
//        String[] resources2={resourceName};

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllServices();
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName,"test NS","","");
        registryCommon.saveService();
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        assertEquals(-1,registryCommon.getVersion());
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        System.out.println("current version "+version1);
        assertFalse(registryCommon.checkVersionResourceProperties(version1,"",propertyName,propertyValue));
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        registryCommon.addProperty(propertyName,propertyValue);
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        assertTrue(registryCommon.checkVersionResourceProperties(version2,"",propertyName,propertyValue));
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        registryCommon.restoreVersion(version1);
        registryCommon.gotoServicesPage();
        registryCommon.openResourceInMetaData(serviceName);
        assertFalse(registryCommon.checkProperties(propertyName,propertyValue));
         registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        UmCommon.logOutUI();
    }*/
    public void testWSDLProperty() throws Exception {
        int version1,version2 ;
        String collectionName="CheckPointTestResource";
        String propertyName="testPropertyName";
        String propertyValue="testPropertyValue";
        String wsdlURL = "http://geocoder.us/dist/eg/clients/GeoCoder.wsdl";
        String name = "GeoCoder.wsdl";
        String user=adminUserName;
        String comment="test comment WSDLProperty";
        String tags="test tags WSDLProperty";
        int rating=5;
        double averageRatin=5.0;

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        registryCommon.AddWSDL(wsdlURL,name);
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
//        assertEquals(-1,registryCommon.getVersion());
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        System.out.println("current version "+version1);
        assertFalse(registryCommon.checkVersionResourceProperties(version1,"",propertyName,propertyValue));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceComments(version1,"",comment,user));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceTags(version1,"",tags));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceRating(version1,"",rating,averageRatin));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.addProperty(propertyName,propertyValue);
        registryCommon.addComment(comment,user,selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n",""));
        registryCommon.addTags(tags, "tfTag");
        registryCommon.rateResource(selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n",""),rating,averageRatin);
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceProperties(version2,"",propertyName,propertyValue));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceComments(version2,"",comment,user));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceTags(version2,"",tags));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceRating(version2,"",rating,averageRatin));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.restoreVersion(version1);
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkProperties(propertyName,propertyValue));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkComments(comment,user));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkTags(tags));
        registryCommon.gotoWSDLsPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkRating(rating,averageRatin));
        registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        UmCommon.logOutUI();
    }
    public void testPolicyProperty() throws Exception {
        int version1,version2 ;
        String collectionName="CheckPointTestResource";
        String propertyName="testPropertyName";
        String propertyValue="testPropertyValue";
        String name = "RMpolicy3.xml";
        String policyUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String user=adminUserName;
        String comment="test comment PolicyProperty";
        String tags="test tags PolicyProperty";
        int rating=5;
        double averageRatin=5.0;

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        registryCommon.AddPolicy(policyUrl,name);
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
//        assertEquals(-1,registryCommon.getVersion());
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        System.out.println("current version "+version1);
        assertFalse(registryCommon.checkVersionResourceProperties(version1,"",propertyName,propertyValue));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceComments(version1,"",comment,user));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceTags(version1,"",tags));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
//        assertFalse(registryCommon.checkVersionResourceRating(version1,"",rating,averageRatin));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.addProperty(propertyName,propertyValue);
        registryCommon.addComment(comment,user,selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n",""));
        registryCommon.addTags(tags, "tfTag");
        registryCommon.rateResource(selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n",""),rating,averageRatin);
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceProperties(version2,"",propertyName,propertyValue));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceComments(version2,"",comment,user));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceTags(version2,"",tags));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceRating(version2,"",rating,averageRatin));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.restoreVersion(version1);
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkProperties(propertyName,propertyValue));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkComments(comment,user));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkTags(tags));
        registryCommon.gotoPoliciesPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkRating(rating,averageRatin));
        registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        UmCommon.logOutUI();
    }
    public void testSchemaProperty() throws Exception {

        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator-no-element-name-invalid.xsd";
        String name = "calculator-no-element-name-invalid.xsd";
//        String[] resources2={resourceName};
        int version1,version2 ;
        String collectionName="CheckPointTestResource";
        String propertyName="testPropertyName";
        String propertyValue="testPropertyValue";
        String user=adminUserName;
        String comment="test comment Schemaroperty";
        String tags="test tags SchemaProperty";
        int rating=5;
        double averageRatin=5.0;

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        registryCommon.addSchema(schemaUrl,name);
        //        assertEquals(-1,registryCommon.getVersion());
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.createCheckPoint();
        version1=registryCommon.getVersion();
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        System.out.println("current version "+version1);
        assertFalse(registryCommon.checkVersionResourceProperties(version1,"",propertyName,propertyValue));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceComments(version1,"",comment,user));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceTags(version1,"",tags));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkVersionResourceRating(version1,"",rating,averageRatin));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.addProperty(propertyName,propertyValue);
        registryCommon.addComment(comment,user,selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n",""));
        registryCommon.addTags(tags, "tfTag");
        registryCommon.rateResource(selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n",""),rating,averageRatin);
        registryCommon.createCheckPoint();
        version2=registryCommon.getVersion();
        System.out.println("current version "+version2);
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceProperties(version2,"",propertyName,propertyValue));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceComments(version2,"",comment,user));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceTags(version2,"",tags));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertTrue(registryCommon.checkVersionResourceRating(version2,"",rating,averageRatin));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        registryCommon.restoreVersion(version1);
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkProperties(propertyName,propertyValue));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkComments(comment,user));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkTags(tags));
        registryCommon.gotoSchemasPage();
        registryCommon.openResourceInMetaData(name);
        assertFalse(registryCommon.checkRating(rating,averageRatin));
        registryCommon.deleteAllServices();
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllWsdls();
        registryCommon.deleteAllSchemas();
        UmCommon.logOutUI();
    }

}
