package org.wso2.carbon.web.test.common;

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


import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

public class LDAPUSerStore extends TestCase {
    Selenium selenium;


    public LDAPUSerStore(Selenium _selenium) {
        selenium = _selenium;
    }

    /*  String lconnectionURL = "ldap://10.100.1.120:10389";
    String lconnectionName = "uid=admin,ou=system";
    String lconnectionPassword = "secret";
    String luserContextName = "ou=system";
    String luserPattern = "ou=system";
   */


    /*Checks LDAP userstore UI*/
    public void lDAPUserStoreUI() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add External User Store");
        selenium.waitForPageToLoad("30000");
        selenium.select("realmType", "label=LDAP");

        assertTrue(selenium.isTextPresent("Connection URL*"));
        assertTrue(selenium.isTextPresent("Connection user name*"));
        assertTrue(selenium.isTextPresent("Connection password*"));
        assertTrue(selenium.isTextPresent("Connection password repeat*"));
        assertTrue(selenium.isTextPresent("User Context Name*"));
        assertTrue(selenium.isTextPresent("User Pattern*"));
    }


    /*Create the LDAP User store*/
    public void createDatabase(String lconnectionURL, String lconnectionName, String lconnectionPassword, String luserContextName, String luserPattern) throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add External User Store");
        selenium.waitForPageToLoad("30000");
        selenium.select("realmType", "label=LDAP");
        selenium.type("lconnectionURL", lconnectionURL);
        selenium.type("lconnectionName", lconnectionName);
        selenium.type("lconnectionPassword", lconnectionPassword);
        selenium.type("lconnectionPasswordRetype", lconnectionPassword);
        selenium.type("luserContextName", luserContextName);
        selenium.type("luserPattern", luserPattern);
        selenium.click("//form[@id='ldap-form']/table/tbody/tr[2]/td/input[1]");
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

    /* Test Connection*/
    public void testTestConnection() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Test Connection");
        selenium.waitForPageToLoad("30000");
        assertEquals("Successfully connected to the user store", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }



    /* External Users */
    public void externalUsers() throws Exception{
        selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=External Users");
		selenium.waitForPageToLoad("30000");
		selenium.type("org.wso2.usermgt.external.filter", "*");
		selenium.click("//input[@value='Search']");
		selenium.waitForPageToLoad("30000");
        selenium.click("//input[@value='Finish']");
		selenium.waitForPageToLoad("30000");
    }


    /* External roles */
    public void externalRoles(String rolename,String permissions[],String[] selectedUsers) throws Exception{
        selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=External Roles");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add New Internal Role");
		selenium.waitForPageToLoad("30000");
		selenium.type("roleName",rolename);
		selenium.click("//input[@value='Next >']");
		selenium.waitForPageToLoad("30000");

        for(int i=0;i<permissions.length;i++){
            if (permissions[i].equals("Login to admin console")) {
                selenium.click("selectedPermissions");

            } else {
                selenium.click("//input[@name='selectedPermissions' and @value='" + permissions[i] + "']");
            }

        }
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");
        selenium.type("org.wso2.usermgt.role.add.filter", "*");
        selenium.click("//input[@value='Search']");
        selenium.waitForPageToLoad("30000");

        for(int j=0;j<selectedUsers.length;j++){
            if(selenium.getTable("//div[@id='workArea']/form[2]/table/tbody/tr[1]/td/table.1.0").equals(selectedUsers[j]))
                selenium.click("selectedUsers");
            else
                selenium.click("//input[@name='selectedUsers' and @value='" + selectedUsers[j] + "']");
        }
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(rolename));
        Thread.sleep(2000);
    }



    /* I have found a bug in the user management module of the wso2 carbon based ESB.
       From the admin console, when I assign permissions to one role, permissions of all other roles are lost. For example, if I have roles admin, manager and guest,
       if I assign permissions for admin, permissions for guest and manager are lost.*/

    /* Related to the above issue */
    public void permissionsOfExternalRoles(String rolename1,String permissions1[],String selectedUsers1[],String rolename2,String permissions2[],String selectedUsers2[]) throws Exception{
        externalRoles(rolename1,permissions1,selectedUsers1);
        externalRoles(rolename2,permissions2,selectedUsers2);

        selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=External Roles");
		selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent(rolename1));
		assertTrue(selenium.isTextPresent(rolename2));

        //Test permissions for rolename1 are available or not.
        if(selenium.getTable("//div[@id='workArea']/table[1].1.0").equals(rolename1)){
            selenium.click("link=Edit Permissions");
		    selenium.waitForPageToLoad("30000");
        }else{
            int k=2;
            while(selenium.isElementPresent("//div[@id='workArea']/table[1]/tbody/tr["+k+"]/td[1]")==true){
                if(selenium.getTable("//div[@id='workArea']/table[1]."+k+".0").equals(rolename1)){
                    selenium.click("//div[@id='workArea']/table[1]/tbody/tr["+k+"]/td[3]/a[2]");
		            selenium.waitForPageToLoad("30000");
                    k=20;
                }
                k=k+1;
            }
        }

        for(int i=0;i<permissions1.length;i++){
                if(permissions1[i].equals("Login to admin console"))
                    assertEquals("on", selenium.getValue("selectedPermissions"));
                else
                    assertEquals("on", selenium.getValue("//input[@name='selectedPermissions' and @value='"+permissions1[i]+"']"));
        }
        selenium.click("//input[@value='Cancel']");
		selenium.waitForPageToLoad("30000");
    }



    /* Delete a role */
    public void deleteRole(String roleName) throws Exception{
        selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=External Roles");
		selenium.waitForPageToLoad("30000");

        if(selenium.getTable("//div[@id='workArea']/table[1].1.0").equals(roleName)){
            selenium.click("link=Delete");
		    assertTrue(selenium.isTextPresent("exact:Do you wish to delete the role "+roleName+"?"));
		    selenium.click("//button[@type='button']");
		    selenium.waitForPageToLoad("30000");
        }

        int k=2;
        while(selenium.isElementPresent("//div[@id='workArea']/table[1]/tbody/tr["+k+"]/td[1]")==true){
            if(selenium.getTable("//div[@id='workArea']/table[1]."+k+".0").equals(roleName)){
                selenium.click("//a[@onclick=\"deleteUserGroup('"+roleName+"')\"]");
                assertTrue(selenium.isTextPresent("exact:Do you wish to delete the role "+roleName+"?"));
		        selenium.click("//button[@type='button']");
		        selenium.waitForPageToLoad("30000");
                k=20;
            }
            k=k+1;
       }
    }


    /* View external user store */
    public void viewExternalUserStore(String lconnectionURL, String lconnectionName, String luserContextName, String luserPattern) throws Exception{
        selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
        selenium.click("link=View External Store");
		selenium.waitForPageToLoad("30000");

		assertTrue(selenium.isTextPresent("External User Store Configuration"));

		assertTrue(selenium.isTextPresent("Connection URL"));
		assertTrue(selenium.isTextPresent("exact:"+lconnectionURL));

		assertTrue(selenium.isTextPresent("Connection user name"));
		assertTrue(selenium.isTextPresent(lconnectionName));

		assertTrue(selenium.isTextPresent("User Context Name"));
		assertTrue(selenium.isTextPresent(luserContextName));

		assertTrue(selenium.isTextPresent("User Pattern"));
		assertTrue(selenium.isTextPresent(luserPattern));

		selenium.click("//input[@value='Finish']");
		selenium.waitForPageToLoad("30000");
    }



    /* Delete external user store */
    public void deleteExternalUserStore() throws Exception{
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


    /* Edit external user store */
       public void editUserStores(String lconnectionURL, String lconnectionName, String lconnectionPassword, String luserContextName) throws Exception {
           selenium.click("link=User Management");
           selenium.waitForPageToLoad("30000");
           selenium.click("link=Edit External Store");
           selenium.waitForPageToLoad("30000");
           assertTrue(selenium.isTextPresent("External User Store Configuration"));
           assertTrue(selenium.isTextPresent("LDAP Store Properties"));

           /*Test Mandatorary Fields*/
           selenium.type("connectionURL", "");
		   selenium.click("//input[@value='Finish']");
		   assertTrue(selenium.isTextPresent("Please enter a valid Connection URL"));
		   selenium.click("//button[@type='button']");
		   selenium.type("connectionURL", lconnectionURL);

		   selenium.type("connectionName", "");
		   selenium.click("//input[@value='Finish']");
		   assertTrue(selenium.isTextPresent("Please enter a valid Connection Name"));
		   selenium.click("//button[@type='button']");
		   selenium.type("connectionName", lconnectionName);

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
		   selenium.type("connectionPassword", lconnectionPassword);
		   selenium.type("connectionPasswordRetype", lconnectionPassword);

		   selenium.type("userContextName", "");
		   selenium.click("//input[@value='Finish']");
		   selenium.waitForPageToLoad("30000");
		   assertTrue(selenium.isTextPresent("External user store updated successfully"));
		   selenium.click("//button[@type='button']");

		   selenium.click("//table[@id='external']/tbody/tr[4]/td/a");
		   selenium.waitForPageToLoad("30000");
           assertEquals("", selenium.getValue("userContextName"));
		   selenium.type("userContextName", luserContextName);
		   selenium.type("userPattern", "");
		   selenium.click("//input[@value='Finish']");
		   selenium.waitForPageToLoad("30000");
		   assertTrue(selenium.isTextPresent("External user store updated successfully"));
		   selenium.click("//button[@type='button']");
		
           selenium.click("//table[@id='external']/tbody/tr[4]/td/a");
		   selenium.waitForPageToLoad("30000");
		   assertEquals("", selenium.getValue("userPattern"));
           selenium.type("userContextName", "");
		   selenium.type("userPattern", "");
		   selenium.click("//input[@value='Finish']");
		   selenium.waitForPageToLoad("30000");
		   assertTrue(selenium.isTextPresent("External user store updated successfully"));
		   selenium.click("//button[@type='button']");

		   selenium.click("//table[@id='external']/tbody/tr[4]/td/a");
		   selenium.waitForPageToLoad("30000");
		   assertEquals("", selenium.getValue("userContextName"));
		   assertEquals("", selenium.getValue("userPattern"));
           selenium.click("//input[@value=' Cancel ']");
		   selenium.waitForPageToLoad("30000");
       }

       /* List Users */
       public String[] listUsers() throws Exception{
           selenium.click("link=User Management");
           selenium.waitForPageToLoad("30000");
           selenium.click("link=External Users");
           selenium.waitForPageToLoad("30000");
           selenium.type("org.wso2.usermgt.external.filter", "*");
           selenium.click("//input[@value='Search']");
           selenium.waitForPageToLoad("30000");

           int i=1;
           while(selenium.isElementPresent("//div[@id='workArea']/table[1]/tbody/tr["+i+"]/td[1]")==true){
                i=i+1;
           }
           int j=i-1;
           String users[]=new String[3];
           users[0]=selenium.getTable("//div[@id='workArea']/table[1]."+j+".0");
           users[1]=selenium.getTable("//div[@id='workArea']/table[1]."+(j-1)+".0");
           users[2]=selenium.getTable("//div[@id='workArea']/table[1]."+(j-2)+".0");

           selenium.click("//input[@value='Finish']");
           selenium.waitForPageToLoad("30000");

           return users;
       }

}
