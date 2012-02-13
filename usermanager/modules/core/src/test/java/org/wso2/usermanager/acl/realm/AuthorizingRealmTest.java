package org.wso2.usermanager.acl.realm;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import junit.framework.TestCase;

import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerConstants;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;
import org.wso2.usermanager.readwrite.DefaultRealm;
import org.wso2.usermanager.readwrite.DefaultRealmConfig;
import org.wso2.usermanager.readwrite.util.DefaultDatabaseUtil;

public class AuthorizingRealmTest extends TestCase {

    Realm realm = null;

   String connectionURL = "jdbc:derby:target/ACLDatabase";

    protected void setUp() throws Exception {
        super.setUp();
        Class clazz = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Driver driver = (Driver) clazz.newInstance();
        Properties props = new Properties();
        Connection dbConnection = driver.connect(
                connectionURL + ";create=true", props);

        DefaultDatabaseUtil.createDatabase(dbConnection);
        
        dbConnection = null;
        
          
        Realm drealm = new DefaultRealm();
        DefaultRealmConfig dconfig = (DefaultRealmConfig) drealm.getRealmConfiguration();
        dconfig.setConnectionURL(connectionURL);
        drealm.init(dconfig);
    
        // Adding the admin to the realm
        UserStoreAdmin usadmin = drealm.getUserStoreAdmin();
        usadmin.addUser("muthulee", "wsas123");
        usadmin.addRole("admin");
        usadmin.addUserToRole("muthulee", "admin");

        // add permission to admin
        AccessControlAdmin acladmin = drealm.getAccessControlAdmin();
        acladmin.authorizeRole("admin", UserManagerConstants.USER_RESOURCE,
                UserManagerConstants.ADD);
        acladmin.authorizeRole("admin", UserManagerConstants.USER_RESOURCE,
                UserManagerConstants.READ);
        acladmin.authorizeRole("admin", UserManagerConstants.ROLE_RESOURCE,
                UserManagerConstants.ADD);

        acladmin.authorizeRole("admin",
                "server room",
                UserManagerConstants.AUTHORIZE);
        acladmin.authorizeUser("muthulee",
                "picture",
                UserManagerConstants.AUTHORIZE);
       
        // now create the realm
        realm = new AuthorizingRealm();
        AuthorizingRealmConfig config = (AuthorizingRealmConfig) realm
                .getRealmConfiguration();
        config.setRealm(drealm);
        config.setAuthenticatedUserName("muthulee");
        realm.init(config);
       

    }

    public void testAuthorizingRealm() throws UserManagerException {
        UserStoreAdmin admin = realm.getUserStoreAdmin();
        UserStoreReader reader = realm.getUserStoreReader();

        admin.addUser("abc", "abc123");
        TestCase.assertTrue(reader.isExistingUser("abc"));

        try {
            admin.deleteUser("abc");
            // if it comes here problem - Exception expected
            TestCase.assertFalse(true);
        } catch (UserManagerException e) {
            // do nothing - expected behavior
        }

       admin.addRole("normal-user");
       try{
           reader.getAllRoleNames();
            // if it comes here problem - exception expected
            TestCase.assertFalse(true);
        } catch (UserManagerException e) {
            // do nothing - expected behavior
        }
        AccessControlAdmin aclAdmin = realm.getAccessControlAdmin();
        aclAdmin.authorizeUser("abc", "picture", "comment");
        TestCase.assertTrue(aclAdmin.isUserAuthorized("abc", "picture", "comment"));
        TestCase.assertFalse(aclAdmin.isUserAuthorized("abc", "picture2", "comment2"));
        admin.addRole("abc2");
        aclAdmin.authorizeRole("abc2", "picture", "comment");
        TestCase.assertTrue(aclAdmin.isRoleAuthorized("abc2", "picture", "comment"));
      
    }
    
    
    
    
}
