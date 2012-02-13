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

public class SearchTest extends CommonSetup {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed;

    public SearchTest(String txt) {
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

    //    public static boolean setDate(String newdate) {
    //        Runtime r = Runtime.getRuntime();
    //       try {
    //            r.exec("sudo bash;date --set=\"20100304\"");
    //       }
    //       catch (IOException e){
    //
    //       }
    //       return true;
    //    }
    public void testContent() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Resource Name"));
        assertTrue(selenium.isTextPresent("Created"));
        assertTrue(selenium.isTextPresent("Updated"));
        assertTrue(selenium.isTextPresent("Created by"));
        assertTrue(selenium.isTextPresent("Updated by"));
        assertTrue(selenium.isTextPresent("Tags"));
        assertTrue(selenium.isTextPresent("Comments"));
        assertTrue(selenium.isTextPresent("Property Name"));
        assertTrue(selenium.isTextPresent("Property Value"));
        UmCommon.logOutUI();
    }

    public void testErrorNoInput() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        selenium.click("#_0");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-warning")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertEquals("Please fill at least one field.", selenium.getText("//div[@id='messagebox-warning']/p"));
        selenium.click("//button[@type='button']");
        UmCommon.logOutUI();
    }

    public void testSearchDefaultCollection() throws Exception {    // here i check the properties of the default collection
        // so i didnt add any properties and comments too..
        //
        String resName = "carbon";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = "system";
        String updatedBy = "system";
        String tags = "";
        String comments = "";
        String propertyName = "";
        String propVal = "";
        String path = "/carbon";
        String author = "system";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoSearchPage();
        assertTrue(registryCommon.search(resName, "", "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "admin", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "admin", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));

        UmCommon.logOutUI();
    }

    public void testSearchNewCollection() throws Exception {
//         assertTrue(registryCommon.search(resName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
        String resName = "SearchNewCollection";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = "admin";
        String updatedBy = "admin";
        String tags = "this is a sample tag";
        String comments = "sample comment";
        String propertyName = "sample property name";
        String propVal = "sample property val";
        String path = "/SearchNewCollection";
        String author = "admin";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(resName, "", "");
        registryCommon.gotopath("/" + resName, "");
        registryCommon.addComment(comments, author, "/" + resName);
        registryCommon.addProperty(propertyName, propVal);
        registryCommon.addTags(tags, "tfTag");
        registryCommon.gotoSearchPage();
        assertTrue(registryCommon.search(resName, "", "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, yesterday, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "system", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "system", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", updatedBy, "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, propVal, path, author));
        registryCommon.deleteColletion("/", resName);
        UmCommon.logOutUI();
    }

    public void testSearchNewCollectionByNewUser() throws Exception {
//         assertTrue(registryCommon.search(resName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
        String resName = "SearchNewCollectionByNewUser";
        String userName = "Newadmin";
        String passwd = "Newadmin";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = userName;
        String updatedBy = userName;
        String tags = "this is a sample tag";
        String comments = "sample comment";
        String propertyName = "sample property name";
        String propVal = "sample property val";
        String path = "/SearchNewCollectionByNewUser";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        String author = userName;
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.addNewUser(userName, passwd);
        UmCommon.assignUserToAdminRole(userName);
        registryCommon.signOut();
        UmCommon.loginToUI(userName, passwd);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(resName, "", "");
        registryCommon.gotopath("/" + resName, "");
        registryCommon.addComment(comments, author, "/" + resName);
        registryCommon.addProperty(propertyName, propVal);
        registryCommon.addTags(tags, "tfTag");
        registryCommon.gotoSearchPage();
        assertTrue(registryCommon.search(resName, "", "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, yesterday, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "system", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "system", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", updatedBy, "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, propVal, path, author));
        registryCommon.deleteResource("/", resName);
        Thread.sleep(1000);
        UmCommon.logOutUI();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.deleteUser(userName);
        UmCommon.logOutUI();
    }

    public void testSearchNewResource() throws Exception {
//         assertTrue(registryCommon.search(resName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
        String resName = "SearchNewresource";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = "admin";
        String updatedBy = "admin";
        String tags = "this is a sample tag";
        String comments = "sample comment";
        String propertyName = "sample property name";
        String propVal = "sample property val";
        String path = "/SearchNewresource";
        String author = "admin";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addTextResourceToRoot(resName);
        registryCommon.gotopath("/" + resName, "");
        registryCommon.addComment(comments, author, "/" + resName);
        registryCommon.addProperty(propertyName, propVal);
        registryCommon.addTags(tags, "tfTag");
        registryCommon.gotoSearchPage();
        assertTrue(registryCommon.search(resName, "", "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, yesterday, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "system", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", "system", "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, "", updatedBy, "", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "", "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "", "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "", path, author));
        assertFalse(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));
        assertTrue(registryCommon.search(resName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, propVal, path, author));
        registryCommon.deleteResource("/", resName);
        UmCommon.logOutUI();
    }
//    public void testSearchNewService() throws Exception {
////         assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
//        String serviceName ="SearchNewSampleService";
//        String nameSpace="SearchNewNameSpace";
//        String state="Created" ;
//        String description ="this is a test service to check the search facility";
//        String  createdFrom=registryCommon.getDate();
//        String createdTo = "";
//        String updatedFrom= registryCommon.getDate();
//        String updatedTo  = "";
//        String createdBy =  "admin";
//        String updatedBy =  "admin";
//        String tags =   "this is a sample tag";
//        String comments =   "sample comment";
//        String propertyName = "sample property name";
//        String propVal = "sample property val";
//        String path = "/SearchNewresource";
//        String author = "admin";
//        String yesterday=registryCommon.getYesterdayDate();
//        String tomorrow =registryCommon.getTomorrowDate();
//        registryCommon.signOut();
//        UmCommon.loginToUI(adminUserName, adminPassword);
//        registryCommon.gotoServicePage();
//        registryCommon.fillServiceOverview(serviceName,nameSpace,state,description);
//        // fill other part also
//        registryCommon.saveService();
//        selenium.waitForPageToLoad("90000");
//        Thread.sleep(1000);
//        System.out.println("/governance/services/"+registryCommon.convertNamespaceToFolderStructure(nameSpace)+serviceName);
////        registryCommon.addComment(comments,author,"/governance/services/SearchNewNameSpace/SearchNewSampleService");
////        registryCommon.addcomment(comments,"governance/services/SearchNewNameSpace/SearchNewSampleService") ;
//        selenium.click("link=Add Comment");
//		selenium.type("comment", comments);
//		selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment('/governance/services/SearchNewNameSpace/SearchNewSampleService');\"]");
//        registryCommon.addProperty(propertyName,propVal);
//        registryCommon.addTags(tags, "tfTag");
//        registryCommon.gotoSearchPage();
//        assertTrue(registryCommon.search(serviceName,"","","","","" ,"" ,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,tomorrow,"","","","" ,"" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,"","","","" ,"" ,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,yesterday,"","","" ,"" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,"","","" ,"" ,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,tomorrow,"","" ,"" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,yesterday,"","" ,"" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,"","" ,"" ,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,yesterday,"" ,"" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,"" ,"" ,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,"system" ,"" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,"" ,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,"","system" ,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,"",updatedBy,"","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,"","","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,"wrong tag","","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,"","","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,"wrong comment","","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,comments,"","",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,comments,"wrong property name","",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,comments,propertyName,"",path,author));
//        assertFalse(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,comments,propertyName,"wrong property value",path,author));
//        assertTrue(registryCommon.search(serviceName,createdFrom,createdTo,updatedFrom,updatedTo,createdBy,updatedBy,tags,comments,propertyName,propVal,path,author));
//        registryCommon.deleteAllPolicies();
//        registryCommon.deleteAllSchemas();
//        registryCommon.deleteAllServices();
//        registryCommon.deleteAllWsdls();

    //        UmCommon.logOutUI();
    //        }

    public void testSearchNewPolicy() throws Exception {
//         assertTrue(registryCommon.search(policyName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
        String policyName = "RMpolicy3.xml";
        String policyUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = "admin";
        String updatedBy = "admin";
        String tags = "this is a sample tag";
        String comments = "sample comment";
        String propertyName = "sample property name";
        String propVal = "sample property val";
        String path = "/governance/policies/" + policyName;
        String author = "admin";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddPolicy(policyUrl, policyName);
        Thread.sleep(3000);
        selenium.click("link=" + policyName);
        selenium.waitForPageToLoad("90000");
        Thread.sleep(1000);
//        System.out.println("/governance/services/" + registryCommon.convertNamespaceToFolderStructure(policyUrl) + policyName);
        registryCommon.addComment(comments, author, "/governance/policies/" + policyName);
//        registryCommon.addcomment(comments,"governance/services/SearchNewNameSpace/SearchNewSampleService") ;
        registryCommon.addProperty(propertyName, propVal);
        registryCommon.addTags(tags, "tfTag");
        registryCommon.gotoSearchPage();
//        assertTrue(registryCommon.search(policyName, "", "", "", "", "", "", "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, yesterday, "", "", "", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, "system", "", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, "", "system", "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, "", updatedBy, "", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "", "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "", "", path, author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "", path,author));
//        assertFalse(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));
//        assertTrue(registryCommon.search(policyName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, propVal, path, author));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }

    public void testSearchNewWsdl() throws Exception {
//         assertTrue(registryCommon.search(wsdlName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
        String wsdlName = "GeoCoder.wsdl";
        String wsdlUrl = "http://geocoder.us/dist/eg/clients/GeoCoder.wsdl";
        String nameSpace = "http://rpc.geocoder.us/Geo/Coder/US/";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = "admin";
        String updatedBy = "admin";
        String tags = "this is a sample tag";
        String comments = "sample comment";
        String propertyName = "sample property name";
        String propVal = "sample property val";
        String path = "/governance/wsdls/" + registryCommon.convertNamespaceToFolderStructure(nameSpace) + wsdlName;
        String author = "admin";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.AddWSDL(wsdlUrl, wsdlName);
        selenium.click("link=" + wsdlName);
        selenium.waitForPageToLoad("90000");
        Thread.sleep(1000);

        registryCommon.addComment(comments, author, "/governance/wsdls/" + registryCommon.convertNamespaceToFolderStructure(nameSpace) + wsdlName);
//        registryCommon.addcomment(comments,"governance/services/SearchNewNameSpace/SearchNewSampleService") ;
        registryCommon.addProperty(propertyName, propVal);
        registryCommon.addTags(tags, "tfTag");
        registryCommon.gotoSearchPage();
        assertTrue(registryCommon.search(wsdlName, "", "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, yesterday, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, "system", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, "", "system", "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, "", updatedBy, "", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "", "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "", "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "", path, author));
        assertFalse(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));
        assertTrue(registryCommon.search(wsdlName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, propVal, path, author));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }

    public void testSearchNewSchema() throws Exception {
//         assertTrue(registryCommon.search(schemaName,createdFrom,createdTo,updatedFrom,updatedTo, createdBy ,updatedBy ,tags,comments,propertyName,propVal,path,author));
        String schemaName = "calculator.xsd";
        String schemaUrl = "http://ww2.wso2.org/~charitha/xsds/calculator.xsd";
        String nameSpace = "http://charitha.org/";
        String createdFrom = registryCommon.getDate();
        String createdTo = registryCommon.getTomorrowDate();
        String updatedFrom = registryCommon.getDate();
        String updatedTo = registryCommon.getTomorrowDate();
        String createdBy = "admin";
        String updatedBy = "admin";
        String tags = "this is a sample tag";
        String comments = "sample comment";
        String propertyName = "sample property name";
        String propVal = "sample property val";
        String path = "/governance/schemas/" + registryCommon.convertNamespaceToFolderStructure(nameSpace) + schemaName;
        String author = "admin";
        String yesterday = registryCommon.getYesterdayDate();
        String tomorrow = registryCommon.getTomorrowDate();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addSchema(schemaUrl, schemaName);
        selenium.click("link=" + schemaName);
        selenium.waitForPageToLoad("90000");
        Thread.sleep(1000);

        registryCommon.addComment(comments, author, "/governance/schemas/" + registryCommon.convertNamespaceToFolderStructure(nameSpace) + schemaName);
        registryCommon.addProperty(propertyName, propVal);
        registryCommon.addTags(tags, "tfTag");
        registryCommon.gotoSearchPage();
        assertTrue(registryCommon.search(schemaName, "", "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, tomorrow, "", "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, "", "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, yesterday, "", "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, "", "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, tomorrow, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, yesterday, "", "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, "", "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, yesterday, "", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, "", "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, "system", "", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, "", "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, "", "system", "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, "", updatedBy, "", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "", "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, "wrong tag", "", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "", "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, "wrong comment", "", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "", "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, "wrong property name", "", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "", path, author));
        assertFalse(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, "wrong property value", path, author));
        assertTrue(registryCommon.search(schemaName, createdFrom, createdTo, updatedFrom, updatedTo, createdBy, updatedBy, tags, comments, propertyName, propVal, path, author));
        registryCommon.deleteAllPolicies();
        registryCommon.deleteAllSchemas();
        registryCommon.deleteAllServices();
        registryCommon.deleteAllWsdls();
        UmCommon.logOutUI();
    }

}
