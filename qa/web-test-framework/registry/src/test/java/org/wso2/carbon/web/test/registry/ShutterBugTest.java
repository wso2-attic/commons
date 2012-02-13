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

package org.wso2.carbon.web.test.registry;

import com.thoughtworks.selenium.Selenium;

import java.util.Properties;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;


public class ShutterBugTest extends CommonSetup {
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String url;
    String httpsPort;
    String contextRoot;

    public void setUp() throws Exception {

        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
        url = "https://" + property.getProperty("host.name");
        httpsPort = property.getProperty("https.port");
        contextRoot = property.getProperty("context.root");
    }

    public ShutterBugTest(String s) {
        super(s);
    }

    public void testContent() throws Exception {
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
//        selenium.open(url+":"+httpsPort+contextRoot+"/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
//      System.out.println(url+":"+httpsPort+contextRoot+"/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        assertEquals("Upload Image", selenium.getText("//form[@id='resourceUploadForm']/table/tbody/tr[1]/td"));
        assertTrue(selenium.isTextPresent("File *"));
        assertTrue(selenium.isTextPresent("Name *"));
        assertEquals("Description", selenium.getText("//form[@id='resourceUploadForm']/table/tbody/tr[4]/td[1]"));
        assertTrue(selenium.isElementPresent("//input[@value='Add']"));
        assertTrue(selenium.isElementPresent("//input[@value='Cancel']"));
        assertTrue(selenium.isElementPresent("uResourceFile"));
        assertTrue(selenium.isElementPresent("link=Browse"));
        assertTrue(selenium.isElementPresent("link=Upload"));
        assertTrue(selenium.isElementPresent("link=Sign-out"));
        UmCommon.logOutUI();
    }
//    public void testUploadNonExistingFile() throws Exception {    // this method is not working sht bg cannot handle

//        String fileName="NonExistingFile.jpg";
//        registryCommon.signOut();

    //        UmCommon.loginToUI(adminUserName, adminPassword);
    //        selenium.open(url+":"+httpsPort+contextRoot+"/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
    //        registryCommon.uploadPicShtBg(fileName,"","");
    //        selenium.open(url+":"+httpsPort+contextRoot+"/carbon/admin/index.jsp");
    //        UmCommon.logOutUI();
    //    }
    public void testUploadFileNameOnly() throws Exception {
        String fileName = "sampleResource.jpg";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
//        selenium.open(url+":"+httpsPort+contextRoot+"/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        selenium.type("uResourceName", fileName);
        selenium.click("//input[@value='Add']");
        selenium.waitForPageToLoad("120000");
//        selenium.open(url+":"+httpsPort+contextRoot+"/carbon/admin/index.jsp");
        assertEquals("File uploading failed. Content is not set properly.", selenium.getText("//div[@id='workArea']/table/tbody/tr/td/b"));
        UmCommon.logOutUI();
    }

    public void testUploadSampleByAdmin() throws Exception {
        String fileName = "1.jpg";
        String user = "tenant0.admin";
        String folderName = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        registryCommon.uploadPicShtBg(fileName, "", "");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        registryCommon.managePopupInShutterbugBrowser();
        assertTrue(registryCommon.gotopath("/shutterbug", ""));
        folderName = registryCommon.getProperty(user);
        assertTrue(registryCommon.gotopath("/shutterbug/" + folderName, fileName));
        UmCommon.logOutUI();
    }

    public void testUploadSample2ByAdmin() throws Exception {
        String fileName = "2.jpg";
        String user = "tenant0.admin";
        String folderName = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        registryCommon.uploadPicShtBg(fileName, "", "");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        registryCommon.managePopupInShutterbugBrowser();
        assertTrue(registryCommon.gotopath("/shutterbug", ""));
        folderName = registryCommon.getProperty(user);
        assertTrue(registryCommon.gotopath("/shutterbug/" + folderName, fileName));
        UmCommon.logOutUI();
    }

    public void testUploadSample3ByAdmin() throws Exception {
        String fileName = "3.jpg";
        String user = "tenant0.admin";
        String folderName = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        assertFalse(registryCommon.uploadPicShtBg(fileName, "", ""));
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        registryCommon.managePopupInShutterbugBrowser();
        assertTrue(registryCommon.gotopath("/shutterbug", ""));
        folderName = registryCommon.getProperty(user);
        assertFalse(registryCommon.gotopath("/shutterbug/" + folderName, fileName));
        UmCommon.logOutUI();
    }

    public void testUploadSampleByNonDefAdmin() throws Exception {
        String fileName = "1.jpg";
        String user = "tenant0.NonDefAdmin";
        String folderName = "";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        UmCommon.addNewUser("NonDefAdmin", "NonDefAdmin");
        UmCommon.assignUserToAdminRole("NonDefAdmin");
        UmCommon.logOutUI();
        UmCommon.loginToUI("NonDefAdmin", "NonDefAdmin");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        registryCommon.uploadPicShtBg(fileName, "", "");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        registryCommon.managePopupInShutterbugBrowser();
        assertTrue(registryCommon.gotopath("/shutterbug", ""));
        folderName = registryCommon.getProperty(user);
        assertTrue(registryCommon.gotopath("/shutterbug/" + folderName, fileName));
        UmCommon.logOutUI();
    }

    public void testUploadSample2ByNonDefAdmin() throws Exception {
        String fileName = "2.jpg";
        String user = "tenant0.NonDefAdmin";
        String folderName = "";
        registryCommon.signOut();
        UmCommon.loginToUI("NonDefAdmin", "NonDefAdmin");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        registryCommon.uploadPicShtBg(fileName, "", "");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        registryCommon.managePopupInShutterbugBrowser();
        assertTrue(registryCommon.gotopath("/shutterbug", ""));
        folderName = registryCommon.getProperty(user);
        assertTrue(registryCommon.gotopath("/shutterbug/" + folderName, fileName));
        UmCommon.logOutUI();
    }

    public void testUploadSample3ByNonDefAdmin() throws Exception {
        String fileName = "3.jpg";
        String user = "tenant0.NonDefAdmin";
        String folderName = "";
        registryCommon.signOut();
        UmCommon.loginToUI("NonDefAdmin", "NonDefAdmin");
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/shutterbug/shutterbug-ajaxprocessor.jsp");
        assertFalse(registryCommon.uploadPicShtBg(fileName, "", ""));
        selenium.open(url + ":" + httpsPort + contextRoot + "/carbon/admin/index.jsp");
        registryCommon.managePopupInShutterbugBrowser();
        assertTrue(registryCommon.gotopath("/shutterbug", ""));
        folderName = registryCommon.getProperty(user);
        assertFalse(registryCommon.gotopath("/shutterbug/" + folderName, fileName));
        UmCommon.logOutUI();
    }

}
