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

import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;

public class ActivityTest extends CommonSetup{
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;

    public ActivityTest(String txt) {
        super(txt);
    }
    public void setUp() throws Exception {
        selenium = BrowserInitializer.getBrowser();
        property = BrowserInitializer.getProperties();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }
    public void  testcheckContent() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoActivityPage();
        assertTrue(selenium.isTextPresent("Search Activities"));
        assertTrue(selenium.isTextPresent("Username"));
        assertTrue(selenium.isTextPresent("Path"));
        assertTrue(selenium.isTextPresent("Date"));
        assertTrue(selenium.isTextPresent("Filter by"));
        assertTrue(selenium.isElementPresent("//option[@value='resourceAdd']"));
        assertTrue(selenium.isElementPresent("//option[@value='resourceUpdate']"));
        assertTrue(selenium.isElementPresent("//option[@value='delete']"));
        assertTrue(selenium.isElementPresent("//option[@value='restore']"));
        assertTrue(selenium.isElementPresent("//option[@value='commentings']"));
        assertTrue(selenium.isElementPresent("//option[@value='taggings']"));
        assertTrue(selenium.isElementPresent("//option[@value='ratings']"));
        assertTrue(selenium.isElementPresent("//option[@value='createSymbolicLink']"));
        assertTrue(selenium.isElementPresent("//option[@value='createRemoteLink']"));
        assertTrue(selenium.isElementPresent("//option[@value='removeLink']"));
        assertTrue(selenium.isElementPresent("//option[@value='all']"));
        UmCommon.logOutUI();
    }
    public void  testAddCollectionByDefAdmin() throws Exception {
        String user="admin";
        String collectionName="TestAddCollection";
        String path="/";
        String searchTag=user+" has added the resource /"+collectionName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.gotoBrowsePage();
        registryCommon.deleteColletion(path,collectionName);
        UmCommon.logOutUI();
    }
    public void testRenameCollectionByDefAdmin() throws Exception {
        String user="admin";
        String collectionName="TestRenameCollection";
        String collectionNewName="TestRenameCollection2";
        String path="/";
        String searchTag=user+" has renamed the resource "+path+collectionName+" to new Name: "+path+collectionNewName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.editCollection(registryCommon.getId(collectionName),"/"+collectionNewName,collectionName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.gotoBrowsePage();
        registryCommon.deleteColletion(path,collectionNewName);
        UmCommon.logOutUI();
    }
    public void testMoveCollectionByDefAdmin() throws Exception {
        String user="admin";
        String collectionName="TestMoveCollection";
        String path="/";
        String newpath="/testpath";
        String searchTag=user+" has moved the resource "+path+collectionName+" to the Location "+newpath +path+collectionName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.moveCollection(collectionName,path,newpath);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteColletion(path,newpath.replace("/",""));
        UmCommon.logOutUI();
    }
    public void testCopyCollectionByDefAdmin() throws Exception {
        String user="admin";
        String collectionName="TestCopyCollection";
        String copiedPath="new directory/temp" ;
        String path="/";
        String searchTag=user+" has copied the resource "+path+collectionName+" to the Location "+copiedPath+"/"+collectionName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.copyCollecion(collectionName,path,copiedPath);
        System.out.println(searchTag);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteColletion(path,copiedPath);
        UmCommon.logOutUI();
    }
    public void testDeleteCollectionByDefAdmin() throws Exception {
        String user="admin";
        String collectionName="TesDeletetCollection";
        String path="/";
        String searchTag=user+" has deleted the resource "+path+collectionName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.deleteColletion(path,collectionName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        UmCommon.logOutUI();
    }
    public void testAddResourceByDefAdmin() throws Exception {
        String user="admin";
        String resourceName="TestAddResource";
        String searchTag=user+" has added the resource /"+resourceName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteResource("/",resourceName) ;
        UmCommon.logOutUI();
    }
    public void testRenameResourceByDefAdmin() throws Exception {
        String user="admin";
        String resourceName="TestRenameResource";
        String resourceNewName="TestRenameResource2";
        String path="/";
        String searchTag=user+" has renamed the resource "+path+resourceName+" to new Name: "+path+resourceNewName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.editResource(registryCommon.getId(resourceName),path,resourceNewName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteResource("/",resourceNewName);
        UmCommon.logOutUI();
    }
    public void testMoveResourceByDefAdmin() throws Exception {
        String user="admin";
        String resourceName="TestMoveResource";
        String path="/";
        String newpath="/move res";
        String searchTag=user+" has moved the resource "+path+resourceName +" to the Location "+newpath+path+resourceName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.moveResource(registryCommon.getId(resourceName),path,newpath);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteColletion(path,newpath.replace("/",""));
        UmCommon.logOutUI();
    }
    public void testCopyResourceByDefAdmin() throws Exception {
        String user="admin";
        String resourceName="TestCopyResource";
        String path="/";
        String copyCollection="copyResCollect";
        String searchTag=user+" has copied the resource "+path+resourceName +" to the Location "+copyCollection+"/"+resourceName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.copyResource(registryCommon.getId(resourceName),resourceName,copyCollection);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.gotoBrowsePage();
        registryCommon.deleteColletion(path,copyCollection);
        registryCommon.deleteResource("/",resourceName) ;
        UmCommon.logOutUI();
    }
    public void testAddServiceByDefAdmin() throws Exception {
        String user="admin";
        String path="/";
        String serviceName="TestActivity";
        String namespace="test.activity.namespace";
        String status="created";
        String searchTag=user+" has added the resource /governance/services/"+registryCommon.convertNamespaceToFolderStructure(namespace)+serviceName;
        String description="this is a test service made for activity test";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName,namespace,"","");
        registryCommon.saveService();
        registryCommon.checkService_Exsits(serviceName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
    public void testAddPolicyByDefAdmin() throws Exception {
        String user="admin";
        String policyName = "RMpolicy3.xml";
        String policyUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String searchTag=user+" has added the resource /governance/policies/"+policyName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddPolicy(policyUrl,policyName);
        registryCommon.checkPolicy_Exsits(policyName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
    public void testAddWsdlByDefAdmin() throws Exception {
        String user="admin";
        String nameSpace="http://charitha.org/";
        String wsdlName = "calculator_import_schema.wsdl";
        String serviceName="calculatorImportSchema";
        String schemaName="calculator.xsd";
        String wsdlUrl = "http://ww2.wso2.org/~charitha/wsdls/calculator_import_schema.wsdl";
        String searchTag=user+" has added the resource /governance/wsdls/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+wsdlName;
        String searchTag1=user+" has added the resource /governance/services/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+serviceName;
        String searchTag2=user+" has added the resource /governance/schemas/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+schemaName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddWSDL(wsdlUrl,wsdlName);
        registryCommon.checkWSDL_Exsits(wsdlName);
        assertTrue(registryCommon.searchActivity("admin","","","","",searchTag));
        assertTrue(registryCommon.searchActivity("admin","","","","",searchTag1));
        assertTrue(registryCommon.searchActivity("admin","","","","",searchTag2));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
    public void testAddSchemaByDefAdmin() throws Exception {
        String user="admin";
        String schemaName = "calculator.xsd";
        String namespace="http://charitha.org/";
        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator.xsd";
        String searchTag=user+" has added the resource /governance/schemas/"+registryCommon.convertNamespaceToFolderStructure(namespace)+schemaName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addSchema(schemaUrl, schemaName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
    public void  testAddCollectionByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String collectionName="TestAddCollection";
        String path="/";
        String searchTag=user+" has added the resource /"+collectionName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        registryCommon.cleanRegistry() ;
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.gotoBrowsePage();
        registryCommon.deleteColletion(path,collectionName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testRenameCollectionByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String collectionName="TestRenameCollection";
        String collectionNewName="TestRenameCollection2";
        String path="/";
        String searchTag=user+" has renamed the resource "+path+collectionName+" to new Name: "+path+collectionNewName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.editCollection(registryCommon.getId(collectionName),"/"+collectionNewName,collectionName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.gotoBrowsePage();
        registryCommon.deleteColletion(path,collectionNewName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testMoveCollectionByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String collectionName="TestMoveCollection";
        String path="/";
        String newpath="/testpath";
        String searchTag=user+" has moved the resource "+path+collectionName+" to the Location "+newpath +path+collectionName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.moveCollection(collectionName,path,newpath);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteColletion(path,newpath.replace("/",""));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testCopyCollectionByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String collectionName="TestCopyCollection";
        String copiedPath="new directory/temp" ;
        String path="/";
        String searchTag=user+" has copied the resource "+path+collectionName+" to the Location "+copiedPath+"/"+collectionName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.copyCollecion(collectionName,path,copiedPath);
        System.out.println(searchTag);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteColletion(path,copiedPath);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testDeleteCollectionNonByDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String collectionName="TesDeletetCollection";
        String path="/";
        String searchTag=user+" has deleted the resource "+path+collectionName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.deleteColletion(path,collectionName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testAddResourceByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String resourceName="TestAddResource";
        String searchTag=user+" has added the resource /"+resourceName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteResource("/",resourceName) ;
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testRenameResourceByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String resourceName="TestRenameResource";
        String resourceNewName="TestRenameResource2";
        String path="/";
        String searchTag=user+" has renamed the resource "+path+resourceName+" to new Name: "+path+resourceNewName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.editResource(registryCommon.getId(resourceName),path,resourceNewName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteResource("/",resourceNewName);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testMoveResourceByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String resourceName="TestMoveResource";
        String path="/";
        String newpath="/move res";
        String searchTag=user+" has moved the resource "+path+resourceName +" to the Location "+newpath+path+resourceName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.moveResource(registryCommon.getId(resourceName),path,newpath);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteColletion(path,newpath.replace("/",""));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testCopyResourceByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String resourceName="TestCopyResource";
        String path="/";
        String copyCollection="copyResCollect";
        String searchTag=user+" has copied the resource "+path+resourceName +" to the Location "+copyCollection+"/"+resourceName ;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry() ;
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(resourceName);
        registryCommon.copyResource(registryCommon.getId(resourceName),resourceName,copyCollection);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.gotoBrowsePage();
        registryCommon.deleteColletion(path,copyCollection);
        registryCommon.deleteResource("/",resourceName) ;
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testAddServiceByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String path="/";
        String serviceName="TestActivity";
        String namespace="test.activity.namespace";
        String status="created";
        String searchTag=user+" has added the resource /governance/services/"+registryCommon.convertNamespaceToFolderStructure(namespace)+serviceName;
        String description="this is a test service made for activity test";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.gotoServicePage();
        registryCommon.fillServiceOverview(serviceName,namespace,"","");
        registryCommon.saveService();
        registryCommon.checkService_Exsits(serviceName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        registryCommon.deleteAllServices();
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.logOutUI();
    }
    public void testAddPolicyByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String policyName = "RMpolicy3.xml";
        String policyUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String searchTag=user+" has added the resource /governance/policies/"+policyName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.AddPolicy(policyUrl,policyName);
        registryCommon.checkPolicy_Exsits(policyName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
    public void testAddWsdlByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String nameSpace="http://charitha.org/";
        String wsdlName = "calculator_import_schema.wsdl";
        String serviceName="calculatorImportSchema";
        String schemaName="calculator.xsd";
        String wsdlUrl = "http://ww2.wso2.org/~charitha/wsdls/calculator_import_schema.wsdl";
        String searchTag=user+" has added the resource /governance/wsdls/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+wsdlName;
        String searchTag1=user+" has added the resource /governance/services/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+serviceName;
        String searchTag2=user+" has added the resource /governance/schemas/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+schemaName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.AddWSDL(wsdlUrl,wsdlName);
        registryCommon.checkWSDL_Exsits(wsdlName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag));
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag1));
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag2));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
    public void testAddSchemaByNonDefAdmin() throws Exception {
        String user="NonDefAdminActivity";
        String passwd="NonDefAdminActivity";
        String schemaName = "calculator.xsd";
        String namespace="http://charitha.org/";
        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator.xsd";
        String searchTag=user+" has added the resource /governance/schemas/"+registryCommon.convertNamespaceToFolderStructure(namespace)+schemaName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        UmCommon.addNewUser(user,passwd);
        UmCommon.assignUserToAdminRole(user);
        UmCommon.logOutUI();
        UmCommon.loginToUI(user,passwd);
        registryCommon.addSchema(schemaUrl, schemaName);
        assertTrue(registryCommon.searchActivity(user,"","","","",searchTag ));
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(user);
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }
}
