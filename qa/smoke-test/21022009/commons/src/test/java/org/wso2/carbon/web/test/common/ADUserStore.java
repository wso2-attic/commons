package org.wso2.carbon.web.test.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 19, 2009
 * Time: 2:01:29 PM
 * To change this template use File | Settings | File Templates.
 */

public class ADUserStore extends TestCase {
    Selenium selenium;
    String connectionURL = "ldap://10.100.1.122:389";
    String connectionName = "cn=Administrator,cn=users,dc=wso2,dc=lk";
    String connectionPassword = "admin123";
    String searchBase = "dc=wso2,dc=lk";

    String user = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[4]/td[1]");
    String view = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[4]/td[3]/a[1]");
    String permission = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[4]/td[3]/a[2]");


    public ADUserStore(Selenium _browser) {
        selenium = _browser;
    }

    public void testADUserStoreUI() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

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
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

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
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Test Connection");
        selenium.waitForPageToLoad("30000");
        assertEquals("Successfully connected to the user store", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

    public void testUsers(String user1, String user2) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

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
        //   SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

           selenium.click("link=User Management");
           selenium.waitForPageToLoad("30000");
           selenium.click("link=External Roles");
           selenium.waitForPageToLoad("30000");

           String user;
           int i;

           user = "null";
           i = 0;
           while (user == "User") {
               for (i = 0; i <= 16; i++) {
                   user = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[" + i + "]/td[1]");
               }
           }
           int j = i;

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
           assertTrue(selenium.isTextPresent("Assigned Permissions 	login, manage-configuration, manage-security"));
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
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
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
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Edit External Store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("External User Store Configuration"));
        assertTrue(selenium.isTextPresent("Active Directory Store Properties"));
        selenium.type("connectionPassword", "admin123q");
        selenium.type("connectionPasswordRetype", "admin123q");
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        assertEquals("External user store updated successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");

    }


    public void testDeleteUserStores() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Delete External Store");
        assertTrue(selenium.getText("messagebox-confirm").matches("^exact:Do you wish to delete the external userstore [\\s\\S]$"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertEquals("User store deleted successfully.", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");

    }


}
