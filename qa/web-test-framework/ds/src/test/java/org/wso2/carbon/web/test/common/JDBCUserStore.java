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

// =========http://www.kitebird.com/articles/jdbc.html, http://www.roseindia.net/jdbc/jdbc-mysql/CreateMySqlTable.shtml=============TO BE DELETED. USE THIS TO ENHANCE THE DB CODE.

import com.thoughtworks.selenium.*;
import com.sun.tools.xjc.AbortException;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;

public class JDBCUserStore extends TestCase {
    Selenium selenium;
    Connection con = null;
    String hostname = "localhost";
    String DBPort = "3306";
    String DBName = "usermgmt";
    String driverName = "com.mysql.jdbc.Driver";
    String DBUserName = "root";
    String DBPassword = "sa";
    String dburl = "jdbc:mysql://" + hostname + ":" + DBPort;
    String dburl1 = "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;
    String juserPasswordSQL = "select password from users where username=?";
    String juserFilterSQL = "select username from users where username like ?";
    String jisUserExistingSQL = "select username from users where username=?";
    String juserListSQL = "select username from users";
    String juserRoleSQL = "select rolename from userroles where username=?";
    String jusersInRoleSQL = "select username from userroles where rolename=?";
    String jroleListSQL = "select rolename from userroles";
    //String attrSQL = "Select username from userprofiles where username=?";
    String attrsSQL = "Select * from userprofiles where username=? and profilename=default";
    String attrProfileSQL = "select distinct profilename from userprofiles where username=?";
    String attrsProfileSQL = "select * from userprofiles where username=? and profilename=?";


    public JDBCUserStore(Selenium _selenium) {
        selenium = _selenium;
    }


    /* Creating a database and Establishing a connection. */
    public void testCreateDatabase() throws Exception {
        System.out.println("Creating the database");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            try {
                Statement st = con.createStatement();
                st.executeUpdate("CREATE DATABASE usermgmt");
                con.close();
                System.out.println("Created the database");
            }
            catch (SQLException s) {
                System.out.println("SQL statement is not executed!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    /* Creating 'user' table  */
    public void testCreateUserTable() throws Exception {
        Connection con = DriverManager.getConnection(dburl, DBUserName, DBPassword);
        try {
            Statement s = con.createStatement();
            int count;
            s.executeUpdate("USE usermgmt");
            s.executeUpdate("CREATE TABLE users ("
                    + "username varchar(10), "
                    + "PRIMARY KEY (username),"
                    + "password varchar(10))"
                    + "engine=innodb;");
            count = s.executeUpdate("INSERT INTO users (username, password)"
                    + " VALUES"
                    + "('tamara', 'tamara'),"
                    + "('yumani', 'yumani'),"
                    + "('alice', 'alice'),"
                    + "('saman', 'saman')");
            s.close();
            System.out.println(count + " rows were inserted");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* Creating 'roles' table */
    public void testCreateRolesTable() throws Exception {
        Connection con = DriverManager.getConnection(dburl, DBUserName, DBPassword);
        Statement s = con.createStatement();
        int count;
        s.executeUpdate("USE usermgmt");
        s.executeUpdate("CREATE TABLE roles ("
                + "rolename varchar(10), "
                + "PRIMARY KEY (rolename),"
                + "descrition varchar(15))"
                + "engine=innodb;");
        count = s.executeUpdate("INSERT INTO roles (rolename, descrition)"
                + " VALUES"
                + "('admin', 'Administrator'),"
                + "('tester', 'Tester'),"
                + "('guest', 'Guest'),"
                + "('dev', 'Developer')");
        s.close();
        con.close();
        System.out.println(count + " rows were inserted");
    }


    /*Creating 'userroles' table */
    public void testCreateUserRolesTable() throws Exception {
        Connection con = DriverManager.getConnection(dburl1, DBUserName, DBPassword);
        Statement s = con.createStatement();
        int count;
        s.executeUpdate("USE usermgmt");
        s.executeUpdate("CREATE TABLE userroles ("
                + "username varchar(10), "
                + "rolename varchar(10),"
                + "constraint un foreign key (username) references usermgmt.users(username),"
                + "constraint rn foreign key (rolename) references usermgmt.roles(rolename))"
                + "engine=innodb;");
        count = s.executeUpdate("INSERT INTO userroles (username, rolename)"
                + " VALUES"
                + "('tamara', 'admin'),"
                + "('yumani', 'tester'),"
                + "('alice', 'guest'),"
                + "('saman', 'dev')");
        s.close();
        con.close();
        System.out.println(count + " rows were inserted");
    }


    /*Creating 'userroles' table */
    public void testCreateUserProfilesTable() throws Exception {
        Connection con = DriverManager.getConnection(dburl, DBUserName, DBPassword);
        Statement s = con.createStatement();
        int count;
        s.executeUpdate("USE usermgmt");
        s.executeUpdate("CREATE TABLE userprofiles ("
                + "username varchar(10), "
                + "profilename varchar(10),"
                + "email varchar(25),"
                + "postalcode varchar(5),"
                + "country varchar(10),"
                + "firstname varchar(10),"
                + "lastname varchar(10),"
                + "homephone varchar(10),"
                + "constraint unp foreign key (username) references usermgmt.users(username))"
                + "engine=innodb;");
        count = s.executeUpdate("INSERT INTO userprofiles (username, profilename, email, postalcode, country, firstname, lastname, homephone)"
                + " VALUES"
                + "('tamara', 'default', 'tamara@yahoo.com', '123', 'SL', 'Tamara', 'Cuttilan', '1111111111'),"
                + "('saman', 'home', 'saman@yahoo.com', '234', 'SL', 'Saman', 'Peries', '2222222222'),"
                + "('yumani', 'default', 'yumani@yahoo.com', '345', 'USA', 'Yumani', 'Ranaweera', '3333333333'),"
                + "('yumani', 'home', 'yumani@gmail.com', '333', 'USA', 'Yumani', 'Ranaweera', '4444444444'),"
                + "('alice', 'default', 'alice@yahoo.com', '555', 'IND', 'Alice', 'Peter', '6666666666'),"
                + "('tamara', 'home', 'tamara@gmail.com', '444', 'SL', 'Tamara', 'Cuttilan', '5555555555')");
        s.close();
        con.close();
        System.out.println(count + " rows were inserted");
    }


    /* Checking JDBC user store UI */
    public void JDBCUserStoreUI() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);


        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        assertEquals("External User Store", selenium.getText("//table[@id='external']/thead/tr/th"));
        assertTrue(selenium.isTextPresent("External Users"));
        assertTrue(selenium.isTextPresent("External Roles"));
        assertTrue(selenium.isTextPresent("View External Store"));
        assertTrue(selenium.isTextPresent("Edit External Store"));
        assertTrue(selenium.isTextPresent("Delete External Store"));
        assertTrue(selenium.isTextPresent("Test Connection"));
        assertTrue(selenium.isElementPresent("link=Add External User Store"));

        selenium.click("link=Add External User Store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Add External User Store"));
        assertTrue(selenium.isTextPresent("JDBC Store Properties"));
        assertTrue(selenium.isTextPresent("Connection URL*"));
        assertTrue(selenium.isTextPresent("Connection user name"));
        assertTrue(selenium.isTextPresent("Connection password"));
        assertTrue(selenium.isTextPresent("Connection password repeat"));
        assertTrue(selenium.isTextPresent("Driver name*"));
        assertTrue(selenium.isTextPresent("User Information"));
        assertTrue(selenium.isTextPresent("SQL for retrieving a given user's password*"));
        assertTrue(selenium.isTextPresent("SQL for listing users using \"like\"*"));
        assertTrue(selenium.isTextPresent("SQL for selecting a single user given the user name*"));
        assertTrue(selenium.isTextPresent("SQL for retrieving a user list*"));
        assertTrue(selenium.isTextPresent("Role Information"));
        assertTrue(selenium.isTextPresent("Read roles from the database"));
        assertTrue(selenium.isTextPresent("SQL for retrieving a given user's role list"));
        assertTrue(selenium.isTextPresent("SQL for retrieving users in a Role"));
        assertTrue(selenium.isTextPresent("SQL for listing the role list"));
        assertTrue(selenium.isTextPresent("SQL for retrieving an user attribute"));
        assertTrue(selenium.isTextPresent("SQL for retrieving multiple attributes"));
        assertTrue(selenium.isTextPresent("SQL for retrieving an user profile attribute"));
        assertTrue(selenium.isTextPresent("SQL for retrieving a multiple attributes in profile"));

        // Following should become mandatory inputs when 'Read roles from the database' check box is clicked.
        assertTrue(selenium.isTextPresent("SQL for retrieving multiple attributes"));
        selenium.click("importRoles");
        assertTrue(selenium.isTextPresent("SQL for retrieving a given user's role list*"));
        assertTrue(selenium.isTextPresent("SQL for retrieving users in a Role*"));
        assertTrue(selenium.isTextPresent("SQL for listing the role list*"));

        // Following should become mandatory inputs when "Import user attributes from database" is clicked.
        selenium.click("importAttrs");
        assertTrue(selenium.isTextPresent("SQL for retrieving an user attribute*"));
        assertTrue(selenium.isTextPresent("SQL for retrieving multiple attributes*"));

        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("60000");
    }


    /* Creating JDBC Exernal user store */
    public void CreateJDBCUserStore() throws Exception {
        selenium.open("/carbon/userstore/index.jsp?region=region1&item=userstores_menu");
        selenium.click("link=Add External User Store");
        selenium.waitForPageToLoad("30000");
        selenium.type("jconnectionURL", dburl1);
        selenium.type("jconnectionName", DBUserName);
        selenium.type("jconnectionPassword", DBPassword);
        selenium.type("jconnectionPasswordRetype", DBPassword);
        selenium.type("jdriverName", driverName);
        selenium.type("juserPasswordSQL", juserPasswordSQL);
        selenium.type("juserFilterSQL", juserFilterSQL);
        selenium.type("jisUserExistingSQL", jisUserExistingSQL);
        selenium.type("juserListSQL", juserListSQL);
        selenium.click("importRoles");
        selenium.type("juserRoleSQL", juserRoleSQL);
        selenium.type("jusersInRoleSQL", jusersInRoleSQL);
        selenium.type("jroleListSQL", jroleListSQL);
        selenium.click("importAttrs");
        selenium.type("jattrsSQL", attrsSQL);
        selenium.click("importMultipleProfiles");

        selenium.type("jprofileNamesSQL", attrProfileSQL);
        selenium.type("jattrsForProfileSQL", attrsProfileSQL);
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
    }


    /* Testing connections to JDBC Exernal user store */
    public void testConnection() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Test Connection");
        selenium.waitForPageToLoad("30000");
        //assertEquals("Successfully connected to the user store", selenium.getText("messagebox-info"));


        boolean x = selenium.isTextPresent("Successfully connected to the user store");
        boolean y = selenium.isTextPresent("Connecting to the user store was unsuccessful");
        if (x) {
            System.out.println("Test cannection to JDBC user store passed !");
        } else {
            if (y) {
                System.out.println("Test cannection to JDBC user store failed !");
            }
        }
        selenium.click("//button[@type='button']");
    }


    /* Testing if all users in the database are visible from UI */
    public void testUsers() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=External Users");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("External Users"));
        selenium.type("org.wso2.usermgt.external.filter", "*");
        selenium.click("//input[@value='Search']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("saman"));
        assertTrue(selenium.isTextPresent("tamara"));
        assertTrue(selenium.isTextPresent("yumani"));
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
    }


    /* Testing if all user roles in the database are visible from UI */
    public void testRoles() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=External Roles");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Roles of External Users"));
        assertTrue(selenium.isTextPresent("admin"));
        assertTrue(selenium.isTextPresent("guest"));
        assertTrue(selenium.isTextPresent("tester"));
    }


    /* Viewing the user store configuration*/
    public void testViewUserStore() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=View External Store");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("External User Store Configuration"));
        assertTrue(selenium.isTextPresent(dburl));
        assertTrue(selenium.isTextPresent(DBUserName));
        assertTrue(selenium.isTextPresent(driverName));
        assertTrue(selenium.isTextPresent(juserPasswordSQL));
        assertTrue(selenium.isTextPresent(juserFilterSQL));
        assertTrue(selenium.isTextPresent(jisUserExistingSQL));
        assertTrue(selenium.isTextPresent(juserListSQL));
        assertTrue(selenium.isTextPresent(juserRoleSQL));
        assertTrue(selenium.isTextPresent(jusersInRoleSQL));
        assertTrue(selenium.isTextPresent(jroleListSQL));
        //  assertTrue(selenium.isTextPresent(attrSQL));
        assertTrue(selenium.isTextPresent(attrsSQL));
        assertTrue(selenium.isTextPresent(attrProfileSQL));
        assertTrue(selenium.isTextPresent(attrsProfileSQL));
        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
    }


    /* Setting the user permissions to external users */
    public void testPermission() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=External Roles");
        selenium.waitForPageToLoad("30000");
        /*    String[] test = {"admin", "guest", "tester"};
               for(int i=0; i< test.length; i++){
                   if(test[i].equals("tester")){
                                    }
        }*/

        int j = 0;
        int i = 1;
        String role = "";
        while (i <= 3) {
            role = selenium.getText("//div[@id='workArea']/table[1]/tbody/tr[" + i + "]/td[1]");
            if (role.equals("tester")) {
                j = i;
            }
            i++;
        }
        //Change permission
        selenium.click("//div[@id='workArea']/table[1]/tbody/tr[" + j + "]/td[3]/a[2]");
        selenium.waitForPageToLoad("30000");
        selenium.click("selectedPermissions");
        selenium.click("//input[@name='selectedPermissions' and @value='manage-configuration']");
        selenium.click("//input[@name='selectedPermissions' and @value='manage-security']");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");

        assertEquals("Role " + role + " updated successfully.", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");

        //Check the changes
        selenium.click("//div[@id='workArea']/table[1]/tbody/tr[" + j + "]/td[3]/a[1]");
        selenium.waitForPageToLoad("30000");
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


    /* Deleting the external user store */

    public void testDelete() throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Delete External Store");
        // assertTrue(selenium.getText("messagebox-confirm").matches("^exact:Do you wish to delete the external userstore [\\s\\S]$"));
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


    /* Deleting the database and the tables */
    public void testcleanDB() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection(dburl1, DBUserName, DBPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement s = con.createStatement();
            s.executeUpdate("USE usermgmt");
            s.executeUpdate("DROP TABLE users");
            s.executeUpdate("DROP TABLE roles");
            s.executeUpdate("DROP TABLE userroles");
            s.executeUpdate("DROP TABLE userprofiles");
            s.executeUpdate("DROP DATABASE " + DBName);
        } catch (SQLException e) {
        }


    }
}
