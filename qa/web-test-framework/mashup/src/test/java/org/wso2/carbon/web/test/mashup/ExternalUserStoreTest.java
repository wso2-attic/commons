package org.wso2.carbon.web.test.mashup;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.JDBCUserStore;
import org.wso2.carbon.web.test.common.ADUserStore;
import org.wso2.carbon.web.test.common.LDAPUSerStore;

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

public class ExternalUserStoreTest extends CommonSetup {

    public ExternalUserStoreTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    /*
       CSHelp
     */
    public void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/userstore/docs/userguide.html";
        selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add External User Store");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Help"));
        selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("User Management"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    } 


    /*
        JDBCUserStore
     */
    public void testCreateJDBC() throws Exception {
        JDBCUserStore instJDBCUserStore = new JDBCUserStore(selenium);
        instJDBCUserStore.CreateJDBCUserStore();
        instJDBCUserStore.testConnection();
        instJDBCUserStore.testUsers();
        instJDBCUserStore.testRoles();
        instJDBCUserStore.testPermission();
        instJDBCUserStore.testViewUserStore();
        instJDBCUserStore.testDelete();
    }

    /*
        LDAPUserStore
     */
    public void testCreateLDAP() throws Exception {
          LDAPUSerStore instLDAPUserStore = new LDAPUSerStore(selenium);
        /*Create the LDAP User store*/
          instLDAPUserStore.testCreateDatabase("ldap://10.100.1.120:10389","uid=admin,ou=system","secret","ou=system","ou=system");

          /* Delete external user store */
                selenium.click("link=User Management");
                selenium.waitForPageToLoad("30000");
                selenium.click("link=Delete External Store");
                assertTrue(selenium.isTextPresent("exact:Do you wish to delete the external userstore ?"));
                selenium.click("//button[@type='button']");
                selenium.waitForPageToLoad("30000");
                assertEquals("User store deleted successfully.", selenium.getText("messagebox-info"));
                selenium.click("//button[@type='button']");

                //After the deletion 'Add Externak User store' link should appear enabled.
                assertTrue(selenium.isElementPresent("link=Add External User Store"));

                //After the deletion all external user store functionalities should be disabled. Links should turn to texts.
                assertTrue(selenium.isTextPresent("External Users"));
                assertTrue(selenium.isTextPresent("External Roles"));
                assertTrue(selenium.isTextPresent("View External Store"));
                assertTrue(selenium.isTextPresent("Edit External Store"));
                assertTrue(selenium.isTextPresent("Delete External Store"));
                assertTrue(selenium.isTextPresent("Test Connection"));
     }

    //Sign-out from Mashup
     public void testSignout() throws Exception {
           SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
           instseleniumTestBase.logOutUI();
     }

}
