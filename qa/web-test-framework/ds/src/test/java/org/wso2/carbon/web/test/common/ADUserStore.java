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

package org.wso2.carbon.web.test.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

public class ADUserStore extends TestCase {
    Selenium selenium;
    String connectionURL = "ldap://10.100.1.211:389";
    String connectionName = "cn=Administrator,cn=users,dc=wso2,dc=lk";
    String connectionPassword = "admin123";
    String searchBase = "dc=wso2,dc=lk";

//    String user = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[4]/td[1]");
//    String view = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[4]/td[3]/a[1]");
//    String permission = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[4]/td[3]/a[2]");


    public ADUserStore(Selenium _selenium) {
        selenium = _selenium;
    }

    public void testADUserStoreUI() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User Management"));
        selenium.click("link=Add External User Store");
        selenium.waitForPageToLoad("30000");
        selenium.select("realmType", "label=Active Directory");
        assertTrue(selenium.isTextPresent("Active Directory Store Properties"));
        assertTrue(selenium.isTextPresent("Connection URL*"));
        assertTrue(selenium.isTextPresent("User DN*"));
        assertTrue(selenium.isTextPresent("Connection password*"));
        assertTrue(selenium.isTextPresent("Connection password repeat*"));
        assertTrue(selenium.isTextPresent("Base DN*"));
        selenium.click("//form[@id='actdir-form']/table/tbody/tr[2]/td/input[2]");
        selenium.waitForPageToLoad("30000");
    }


    public void testCreateADUserStore() throws Exception {

        /* Create an AD user store */
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User Management"));
        selenium.click("link=Add External User Store");
        selenium.waitForPageToLoad("30000");
        selenium.select("realmType", "label=Active Directory");
        selenium.type("aconnectionURL", connectionURL);
        selenium.type("aconnectionName", connectionName);
        selenium.type("aconnectionPassword", connectionPassword);
        selenium.type("aconnectionPasswordRetype", connectionPassword);
        selenium.type("asearchBase", searchBase);
        selenium.click("//form[@id='actdir-form']/table/tbody/tr[2]/td/input[1]");
        selenium.waitForPageToLoad("30000");
        assertEquals("User store added successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");

        // The following should be turned to links after the user store is created.
        assertTrue(selenium.isElementPresent("link=External Users"));
        assertTrue(selenium.isElementPresent("link=External Roles"));
        assertTrue(selenium.isElementPresent("link=View External Store"));
        assertTrue(selenium.isElementPresent("link=Edit External Store"));
        assertTrue(selenium.isElementPresent("link=Delete External Store"));
        assertTrue(selenium.isElementPresent("link=Test Connection"));
        assertTrue(selenium.isTextPresent("Add External User Store"));
    }

    public void testTestConnection() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Test Connection");
        selenium.waitForPageToLoad("30000");
        assertEquals("Successfully connected to the user store", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

    public void testUsers(String user1, String user2) throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=External Users");
        selenium.waitForPageToLoad("30000");
        selenium.type("org.wso2.usermgt.external.filter", "*");
        selenium.click("//input[@value='Search']");
        selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent("External Users"));
        //Following users must be listed in the user store
        assertTrue(selenium.isTextPresent(user1));
        assertTrue(selenium.isTextPresent(user2));
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");

    }


    public void testPermission() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=External Roles");
        selenium.waitForPageToLoad("30000");

        String user;
        int i;

        user = "null";
        i = 1;
        while (!user.equals("Users")) {
                user = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[" + i + "]/td[1]");
                i=i+1;
        }
        int j = i-1;

        //Change permission
        selenium.click("//div[@id='workArea']/table[1]/tbody/tr[" + j + "]/td[3]/a[2]");
        selenium.waitForPageToLoad("30000");
        selenium.click("selectedPermissions");
        selenium.click("//input[@name='selectedPermissions' and @value='manage-configuration']");
        selenium.click("//input[@name='selectedPermissions' and @value='manage-security']");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
        assertEquals("Role Users updated successfully.", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");

        //Check the changes
        selenium.click("//div[@id='workArea']/table[1]/tbody/tr[" + j + "]/td[3]/a[1]");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Assigned Permissions"));
        assertTrue(selenium.isTextPresent("login, manage-configuration, manage-security"));        
        selenium.click("//input[@value='Back to Roles List']");
        selenium.waitForPageToLoad("30000");

        //Reset the environment
        selenium.click("//div[@id='workArea']/table[1]/tbody/tr[" + j + "]/td[3]/a[2]");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Unselect All");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");


    }


    public void testViewUserStore() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=View External Store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(connectionURL));
        assertTrue(selenium.isTextPresent(connectionName));
        assertTrue(selenium.isTextPresent(searchBase));
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");


    }

    public void testEditUserStores() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Edit External Store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("External User Store Configuration"));
		assertTrue(selenium.isTextPresent("Active Directory Store Properties"));

        selenium.type("connectionURL", "");
		selenium.click("//input[@value='Finish']");
		assertTrue(selenium.isTextPresent("Please enter a valid Connection URL"));
		selenium.click("//button[@type='button']");

		selenium.type("connectionURL", connectionURL);
		selenium.type("connectionName", "");
		selenium.click("//input[@value='Finish']");
		assertTrue(selenium.isTextPresent("Please enter a valid Connection Name"));
		selenium.click("//button[@type='button']");

		selenium.type("connectionName", connectionName);
		selenium.type("connectionPassword", "");
		selenium.click("//input[@value='Finish']");
		assertTrue(selenium.isTextPresent("Please enter a valid Connection Password"));
		selenium.click("//button[@type='button']");

		selenium.type("connectionPassword", "mismatch");
		selenium.click("//input[@value='Finish']");
		assertTrue(selenium.isTextPresent("Password and Password Repeat do not match. Please re-enter."));
		selenium.click("//button[@type='button']");

		selenium.type("connectionPasswordRetype", "");
		selenium.click("//input[@value='Finish']");
		assertTrue(selenium.isTextPresent("Password and Password Repeat do not match. Please re-enter."));
		selenium.click("//button[@type='button']");

		selenium.type("connectionPassword", connectionPassword);
		selenium.type("connectionPasswordRetype", connectionPassword);
		selenium.type("searchBase", "");
		selenium.click("//input[@value='Finish']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("External user store updated successfully"));
		selenium.click("//button[@type='button']");

		selenium.click("//table[@id='external']/tbody/tr[4]/td/a");
		selenium.waitForPageToLoad("30000");
		assertEquals("", selenium.getValue("searchBase"));
		selenium.click("//input[@value=' Cancel ']");
		selenium.waitForPageToLoad("30000");
    }


    public void testDeleteUserStores() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Delete External Store");
        assertTrue(selenium.isTextPresent("exact:Do you wish to delete the external userstore ?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("User store deleted successfully."));
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
}
