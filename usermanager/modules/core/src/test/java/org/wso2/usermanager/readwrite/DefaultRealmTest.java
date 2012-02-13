/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.usermanager.readwrite;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;
import org.wso2.usermanager.readwrite.util.DefaultDatabaseUtil;

public class DefaultRealmTest extends TestCase {

    Realm realm = null;

    DefaultRealmConfig config = null;

    String connURL = "jdbc:derby:target/DefaultUserDatabase";

    
    protected void setUp() throws Exception {
        super.setUp();
        Class clazz = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Driver driver = (Driver) clazz.newInstance();
        Properties props = new Properties();
        Connection dbConnection = driver.connect(
                connURL + ";create=true", props);

        DefaultDatabaseUtil.createDatabase(dbConnection);
        
        dbConnection.close();
        
        
        dbConnection = null;
        
        realm = new DefaultRealm();
        config = (DefaultRealmConfig) realm.getRealmConfiguration();
        config.setConnectionURL(connURL);
        realm.init(config);
        
       
        

    }

    public void testRealm() throws Exception {
        userStuff();
        roleStuff();
        authorizationStuff();
        attributesStuff();
        roleUserStuff();
    }

    protected void userStuff() throws Exception {
        UserStoreAdmin admin = realm.getUserStoreAdmin();
        Authenticator authen = realm.getAuthenticator();
        UserStoreReader reader = realm.getUserStoreReader();
      
        // add
        admin.addUser("dimuthu", "credential");
        admin.addUser("vajira", "credential");
        TestCase.assertTrue(authen.authenticate("dimuthu", "credential"));
                
        TestCase.assertTrue(reader.isExistingUser("dimuthu"));
        TestCase.assertFalse(reader.isExistingUser("muhaha"));
        // update
        admin.updateUser("dimuthu", "password", "credential");
        TestCase.assertFalse(authen.authenticate("dimuthu", "credential"));
        // list
        String[] names = reader.getAllUserNames();
        TestCase.assertEquals(2, names.length);
        // delete
        admin.deleteUser("vajira");
        TestCase.assertFalse(authen.authenticate("vajira", "credential"));

        // restore stuff for further testing
        admin.addUser("vajira", "credential");
        admin.addUser("juhia", "jooo");
             
    }

    public void attributesStuff() throws Exception {
        UserStoreAdmin admin = realm.getUserStoreAdmin();
        UserStoreReader reader = realm.getUserStoreReader();

        Map props = new HashMap();
        props.put("address", "59C");
        props.put("telephone", "9870");
        admin.setUserProperties("dimuthu", props);
        
        String[] names = reader.getUserNamesWithPropertyValue("telephone", "9870");
        TestCase.assertEquals("dimuthu", names[0]);
        
        String[] nam = admin.getUserNamesWithPropertyValue(null, "9870");
        TestCase.assertEquals("dimuthu", nam[0]);

        Map stored = reader.getUserProperties("dimuthu");
        TestCase.assertEquals(props.size(), stored.size());
        TestCase.assertEquals("59C", stored.get("address"));

        props.put("address", "121/2");
        props.put("telephone", null);
        admin.setUserProperties("dimuthu", props);
        stored = reader.getUserProperties("dimuthu");
        TestCase.assertEquals(1, stored.size());
        TestCase.assertEquals("121/2", stored.get("address"));

        props.put("telephone", "34545");
        admin.setUserProperties("vajira", props);

        String[] attrNames = reader.getUserPropertyNames();
        TestCase.assertEquals(2, attrNames.length);
        
        

    }

    public void roleStuff() throws Exception {

        UserStoreAdmin admin = realm.getUserStoreAdmin();
        UserStoreReader reader = realm.getUserStoreReader();
        // add
        admin.addRole("admin");
        admin.addRole("hora");
        admin.addRole("normal");

        // list
        String[] names = reader.getAllRoleNames();
        TestCase.assertEquals(3, names.length);

        // addusertorole 
        admin.addUserToRole("dimuthu", "admin");
        admin.addUserToRole("dimuthu", "hora");
        admin.addUserToRole("vajira", "admin");

        String[] userRoles = reader.getUserRoles("dimuthu");
        TestCase.assertEquals(2, userRoles.length);
        
        String[] usersInRoles = reader.getUsersInRole("admin");
        TestCase.assertEquals(2, usersInRoles.length);

        // delete
        admin.deleteRole("normal");

        System.out
                .println("Now deleting a role attached to a user - role is hora user is dimuthu");
        System.out.println("**Total role count"
                + reader.getAllRoleNames().length);
        System.out.println("**Role count of Dimuthu "
                + reader.getUserRoles("dimuthu").length);
        admin.deleteRole("hora");
        System.out.println("**Total role count"
                + reader.getAllRoleNames().length);
        System.out.println("**Role count of dimuthu "
                + reader.getUserRoles("dimuthu").length);

        // restore stuff for further testing
        //dimuthu is a admin.
        admin.addRole("normal");
        admin.addRole("hora");
        admin.addUserToRole("dimuthu", "normal");
        
    }

    public void authorizationStuff() throws Exception {

        UserStoreAdmin admin = realm.getUserStoreAdmin();

        AccessControlAdmin acAdmin = realm.getAccessControlAdmin();
        Authorizer athzr = realm.getAuthorizer();
        Authenticator authen = realm.getAuthenticator();

        acAdmin.authorizeUser("dimuthu", "floor", "dance");
        acAdmin.authorizeRole("admin", "server", "login");
        acAdmin.authorizeUser("vajira", "floor", "dance");
        acAdmin.denyUser("dimuthu", "denyResource", "deny");
        acAdmin.authorizeRole("admin", "denyResource", "deny");
        acAdmin.denyRole("admin", "denyResource", "deny");

        TestCase
                .assertTrue(athzr.isUserAuthorized("dimuthu", "floor", "dance"));
        TestCase.assertTrue(athzr.isRoleAuthorized("admin", "server", "login"));
        TestCase.assertFalse(athzr.isUserAuthorized("dimuthu", "denyResource",
                "deny"));
        TestCase.assertFalse(athzr.isRoleAuthorized("admin", "denyResource",
                "deny"));

        String[] authorizedUsers = athzr.getAllowedUsersForResource("floor",
                "dance");
        String au = Arrays.toString(authorizedUsers);
        TestCase.assertTrue(au.contains("vajira"));
        TestCase.assertTrue(au.contains("dimuthu"));
        
        String[] authorizedRoles = athzr.getAllowedRolesForResource(
                "server", "login");
        String ar = Arrays.toString(authorizedRoles);
        TestCase.assertTrue(ar.contains("admin"));
    
        String[] deniedRoles = athzr.getDeniedRolesForResource(
                "denyResource", "deny");
        TestCase.assertEquals(deniedRoles[0], "admin");
    
        acAdmin.clearUserAuthorization("dimuthu", "floor", "dance");

        String[] deniedUsers = athzr.getDeniedUsersForResource("denyResource",
        "deny");
        TestCase.assertEquals(1,deniedUsers.length);
        
        TestCase.assertTrue(athzr
                .isRoleAuthorized("admin",  "server", "login"));
       
        acAdmin.clearRoleAuthorization("admin", "server", "login");
        
        TestCase.assertFalse(athzr
                .isRoleAuthorized("admin",  "server", "login"));
        
        TestCase.assertTrue(athzr.isUserAuthorized("vajira", "floor", "dance"));
        
        acAdmin.authorizeUser("juhia", "floor", "dance");
        TestCase.assertTrue(authen.authenticate("dimuthu", "password"));
        TestCase
                .assertFalse(athzr.isUserAuthorized("admin", "server", "login"));
        acAdmin.authorizeRole("admin", "server", "login");
        System.out.println("Basic done");

        acAdmin.copyAuthorizations("floor", "classroom");
        TestCase.assertTrue(athzr.isUserAuthorized("juhia", "classroom",
                "dance"));

        acAdmin.copyAuthorizations("server", "desktop");
        TestCase
                .assertTrue(athzr.isRoleAuthorized("admin", "desktop", "login"));
        
        

    }

    public void roleUserStuff() throws UserManagerException {

        UserStoreAdmin admin = realm.getUserStoreAdmin();
        UserStoreReader reader = realm.getUserStoreReader();
        AccessControlAdmin ac = realm.getAccessControlAdmin();
        Authorizer athzr = realm.getAuthorizer();

        admin.addUser("sam", "boo");
        admin.addUser("jennifer", "boo");
        admin.addUser("david", "boo");

        admin.addRole("cook");
        admin.addRole("waiter");
        admin.addRole("hora2");

        ac.authorizeRole("cook", "new cooker", "cook stuff");
        ac.authorizeRole("waiter", "new apron", "wear");

        ac.denyRole("hora2", "new cooker", "cook stuff");
        ac.denyRole("hora2", "new apron", "wear");

        admin.addUserToRole("jennifer", "waiter");
        admin.addUserToRole("sam", "cook");
        
        //david is a cook and a theif
        admin.addUserToRole("david", "cook");
        admin.addUserToRole("david", "hora2");

        config.setPermissionAlgo(DefaultRealmConfig.PERMISSION_BLOCK_FIRST);
        TestCase.assertTrue(athzr.isUserAuthorized("jennifer", "new apron",
                "wear"));
        TestCase.assertTrue(athzr.isUserAuthorized("sam", "new cooker",
                "cook stuff"));

        TestCase.assertFalse(athzr.isUserAuthorized("david", "new cooker",
                "cook stuff"));
        
        //david is good now
        admin.removeUserFromRole("david", "hora2");
        TestCase.assertTrue(athzr.isUserAuthorized("david", "new cooker",
        "cook stuff"));
        
        
        
    }

}
