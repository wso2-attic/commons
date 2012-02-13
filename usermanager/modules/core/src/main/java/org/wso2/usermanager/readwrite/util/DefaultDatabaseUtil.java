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
package org.wso2.usermanager.readwrite.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.wso2.usermanager.UserManagerException;

public class DefaultDatabaseUtil {

    public static final String USER_TABLE = "CREATE TABLE UM_USERS (ID VARCHAR(255),"
            + " USER_NAME VARCHAR(255) NOT NULL,"
            + " PASSWORD VARCHAR(255) NOT NULL," + " UNIQUE (USER_NAME),"
            + " PRIMARY KEY (ID))";

    public static final String USER_ATTRIBUTE_TABLE = "CREATE TABLE UM_USER_ATTRIBUTES (ID VARCHAR(255),"
            + " ATTR_NAME VARCHAR(255) NOT NULL,"
            + " ATTR_VALUE VARCHAR(255),"
            + " USER_ID VARCHAR(255),"
            + " FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE,"
            + " PRIMARY KEY (ID))";

    public static final String ROLE_TABLE = "CREATE TABLE UM_ROLES (ID VARCHAR(255),"
            + " ROLE_NAME VARCHAR(255) NOT NULL," + " UNIQUE (ROLE_NAME),"
            + " PRIMARY KEY (ID))";

    public static final String ROLE_ATTRIBUTE_TABLE = "CREATE TABLE UM_ROLE_ATTRIBUTES (ID VARCHAR(255),"
            + " ATTR_NAME VARCHAR(255) NOT NULL,"
            + " ATTR_VALUE VARCHAR(255),"
            + " ROLE_ID VARCHAR(255),"
            + " FOREIGN KEY (ROLE_ID) REFERENCES UM_ROLES(ID) ON DELETE CASCADE,"
            + " PRIMARY KEY (ID))";

    public static final String PERMISSION_TABLE = "CREATE TABLE UM_PERMISSIONS (ID VARCHAR(255),"
            + " RESOURCE_ID VARCHAR(255) NOT NULL,"
            + " ACTION VARCHAR(255) NOT NULL," + " PRIMARY KEY (ID))";

    public static final String ROLE_PERMISSION_TABLE = "CREATE TABLE UM_ROLE_PERMISSIONS (ID VARCHAR(255),"
            + " PERMISSION_ID VARCHAR(255) NOT NULL,"
            + " ROLE_ID VARCHAR(255) NOT NULL,"
            + " IS_ALLOWED SMALLINT NOT NULL,"
            + " FOREIGN KEY (PERMISSION_ID) REFERENCES UM_PERMISSIONS(ID) ON DELETE  CASCADE,"
            + " FOREIGN KEY (ROLE_ID) REFERENCES UM_ROLES(ID) ON DELETE CASCADE,"
            + " UNIQUE (PERMISSION_ID, ROLE_ID)," + " PRIMARY KEY (ID))";

    public static final String USER_PERMISSON_TABLE = "CREATE TABLE UM_USER_PERMISSIONS (ID VARCHAR(255),"
            + " IS_ALLOWED SMALLINT NOT NULL,"
            + " PERMISSION_ID VARCHAR(255) NOT NULL,"
            + " USER_ID VARCHAR(255) NOT NULL,"
            + " FOREIGN KEY (PERMISSION_ID) REFERENCES UM_PERMISSIONS(ID) ON DELETE CASCADE,"
            + " FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE,"
            + " UNIQUE (PERMISSION_ID, USER_ID)," + " PRIMARY KEY (ID))";

    public static final String USER_ROLES_TABLE = "CREATE TABLE UM_USER_ROLES (ID VARCHAR(255),"
            + " ROLE_ID VARCHAR(255) NOT NULL,"
            + " USER_ID VARCHAR(255) NOT NULL,"
            + " FOREIGN KEY (ROLE_ID) REFERENCES UM_ROLES(ID) ON DELETE CASCADE,"
            + " FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE,"
            + " UNIQUE (USER_ID, ROLE_ID)," + " PRIMARY KEY (ID))";

    public static final String USER_PROFILE_TABLE = "CREATE TABLE USER_PROFILES (ID VARCHAR(255),"
            + " USER_ID VARCHAR(255), PROFILE_NAME VARCHAR(255), IS_DEFAULT SMALLINT,"
            + " FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE, PRIMARY KEY (ID))";
    
    public static final String PROFILE_ATTRIBUTE_TABLE = "CREATE TABLE PROFILE_ATTRIBUTES (ID VARCHAR(255),"
            + " USER_PROFILE_ID VARCHAR(255), ATTRIBUTE_NAME VARCHAR(255), ATTRIBUTE_VALUE VARCHAR(255),"
            + " FOREIGN KEY (USER_PROFILE_ID) REFERENCES USER_PROFILES(ID) ON DELETE CASCADE,"
            + " PRIMARY KEY (ID))";

    public static void createDatabase(Connection conn) throws SQLException,
            UserManagerException {

        Statement s = conn.createStatement();
        s.executeUpdate(USER_TABLE);
        s.executeUpdate(ROLE_TABLE);
        s.executeUpdate(USER_ATTRIBUTE_TABLE);
        s.executeUpdate(ROLE_ATTRIBUTE_TABLE);
        s.executeUpdate(PERMISSION_TABLE);
        s.executeUpdate(ROLE_PERMISSION_TABLE);
        s.executeUpdate(USER_PERMISSON_TABLE);
        s.executeUpdate(USER_ROLES_TABLE);
        s.executeUpdate(USER_PROFILE_TABLE);
        s.executeUpdate(PROFILE_ATTRIBUTE_TABLE);
        conn.commit();

    }
}
