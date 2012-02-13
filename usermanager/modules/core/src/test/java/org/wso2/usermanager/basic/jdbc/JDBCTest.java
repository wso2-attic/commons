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

package org.wso2.usermanager.basic.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.custom.jdbc.JDBCRealm;
import org.wso2.usermanager.custom.jdbc.JDBCRealmConfig;

public class JDBCTest extends TestCase {

    public static final String PROTOCOL = "jdbc:derby:";

    protected JDBCRealm realm = null;

    protected void setUp() throws Exception {
        super.setUp();
        createDatabase();
        realm = new JDBCRealm();
        JDBCRealmConfig config = (JDBCRealmConfig) realm
                .getRealmConfiguration();
        config.setConnectionURL("jdbc:derby:target/UserDatabase");
        config.setUserNameColumn("username");
        config.setUserCredentialColumn("pass");
        config.setUserTable("users");
        config.setDriverName("org.apache.derby.jdbc.EmbeddedDriver");
        int[] intValues = new int[] { Types.DECIMAL, Types.VARCHAR,
                Types.VARCHAR };
        config.setColumnNames("status,username,pass");
   
        realm.init(config);
        
   
        
        
        
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJDBCDerbyService() throws UserManagerException {
        Authenticator authenticator = realm.getAuthenticator();
        
        boolean bool1 = authenticator.authenticate("dimuthu", "lee");
        boolean bool2 = authenticator.authenticate("hora", "hora");
        boolean bool3 = authenticator.authenticate("dimuthu", "hora");

        TestCase.assertTrue(bool1);
        TestCase.assertFalse(bool2);
        TestCase.assertFalse(bool3);

        Map map = realm.getUserStoreReader().getUserProperties("dimuthu");
        TestCase.assertNull("pass", map.get("lee"));
        TestCase.assertNull("1", map.get("status"));
    }

    private void createDatabase() throws Exception {

        Class clazz = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Driver driver = (Driver) clazz.newInstance();

        Properties props = new Properties();

        String connectionURL = PROTOCOL + "target/"
                + "UserDatabase;create=true";
        Connection dbConnection = driver.connect(connectionURL, props);

        Statement stmt = dbConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        DatabaseMetaData dbmd = dbConnection.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "USERS", null);
        if (rs.next() == false) {
            System.out.println("Creating a new table in the database.");
            stmt = dbConnection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt
                    .executeUpdate("create table USERS(status int, username varchar(20), pass varchar(40))");
            stmt
                    .executeUpdate("insert into USERS(status, username, pass) values(1, 'dimuthu', 'lee')");
            dbConnection.commit();
        } else {
            System.out.println("Database and table already found.");

        }
        dbConnection.commit();
        dbConnection.close();
    }

}
