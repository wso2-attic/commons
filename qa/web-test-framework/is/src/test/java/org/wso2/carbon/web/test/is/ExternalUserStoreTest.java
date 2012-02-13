package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.*;

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


public class ExternalUserStoreTest extends CommonSetup {
    //selenium selenium;

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
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/userstore/docs/userguide.html";
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
            LDAPUserStore
         */
    public void testCreateLDAP() throws Exception {
        LDAPUSerStore instLDAPUserStore = new LDAPUSerStore(selenium);

        instLDAPUserStore.lDAPUserStoreUI();

        /*Create the LDAP User store*/
        instLDAPUserStore.createDatabase("ldap://10.100.1.120:10389","uid=admin,ou=system","secret","ou=system","ou=system");
        instLDAPUserStore.testTestConnection();

        instLDAPUserStore.externalUsers();

        /*Add different permissions for the different roles.*/
        String permissions1[]={"Login to admin console","manage-configuration","upload-services"};
        String permissions2[]={"manage-services","monitor-system"};
        String selectedUser1[]={"ashadi"};
        String selectedUser2[]={"yumani"};

        instLDAPUserStore.permissionsOfExternalRoles("role1",permissions1,selectedUser1,"role2",permissions2,selectedUser2);

        /*Delete roles*/
        instLDAPUserStore.deleteRole("role1");
        instLDAPUserStore.deleteRole("role2");

        /*View external user store*/
        instLDAPUserStore.viewExternalUserStore("ldap://10.100.1.120:10389","uid=admin,ou=system","ou=system","ou=system");
        instLDAPUserStore.listUsers();

        /*Edit external user store */
        instLDAPUserStore.editUserStores("ldap://10.100.1.120:10389","uid=admin,ou=system","secret","ou=system");

        /*Delete external user store*/
        instLDAPUserStore.deleteExternalUserStore();
    }

    /*
        ADUserStore
     */
    public void testCreateAD() throws Exception {
        ADUserStore instADUserStore = new ADUserStore(selenium);
        instADUserStore.testADUserStoreUI();
        instADUserStore.testCreateADUserStore();
        instADUserStore.testTestConnection();
        instADUserStore.testUsers("yumani","roshanthi");
        instADUserStore.testPermission();
        instADUserStore.testViewUserStore();
        instADUserStore.testEditUserStores();
        instADUserStore.testDeleteUserStores();
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



    //Sign-out from admin console.
     public void testSignout() throws Exception {
           SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
           instseleniumTestBase.logOutUI();
     }


}