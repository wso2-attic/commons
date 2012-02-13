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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.readwrite.DefaultRealmConstants;
import org.wso2.usermanager.readwrite.DefaultStrategy;

/**
 * Represents the user store. If you change this object it will change the way
 * the DefaultRealmBehaves.
 */
public class DefaultStrategyImpl implements DefaultStrategy {

    protected DataSource dataSource = null;

    public DefaultStrategyImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DefaultStrategyImpl() {

    }

    public String getColumnName(int columnID) {
        String sqlString = null;
        switch (columnID) {
        case DefaultRealmConstants.COLUMN_ID_ATTR_NAME:
            sqlString = DefaultRealmConstants.COLUMN_NAME_ATTR_NAME;
            break;
        case DefaultRealmConstants.COLUMN_ID_ATTR_VALUE:
            sqlString = DefaultRealmConstants.COLUMN_NAME_ATTR_VALUE;
            break;
        case DefaultRealmConstants.COLUMN_ID_ID:
            sqlString = DefaultRealmConstants.COLUMN_NAME_ID;
            break;
        case DefaultRealmConstants.COLUMN_ID_USER_NAME:
            sqlString = DefaultRealmConstants.COLUMN_NAME_USER_NAME;
            break;
        case DefaultRealmConstants.COLUMN_ID_ROLE_NAME:
            sqlString = DefaultRealmConstants.COLUMN_NAME_ROLE_NAME;
            break;
        case DefaultRealmConstants.COLUMN_ID_ROLE_ID:
            sqlString = DefaultRealmConstants.COLUMN_NAME_ROLE_ID;
            break;
        case DefaultRealmConstants.COLUMN_ID_USER_ID:
            sqlString = DefaultRealmConstants.COLUMN_NAME_USER_ID;
            break;
        case DefaultRealmConstants.COLUMN_ID_IS_ALLOWED:
            sqlString = DefaultRealmConstants.COLUMN_NAME_IS_ALLOWED;
            break;
        case DefaultRealmConstants.COLUMN_ID_CREDENTIAL:
            sqlString = DefaultRealmConstants.COLUMN_NAME_CREDENTIAL;
            break;
        case DefaultRealmConstants.COLUMN_ID_ACTION:
            sqlString = DefaultRealmConstants.COLUMN_NAME_ACTION;
            break;
        default:
            System.out.println("Null");
            break;
        }
        return sqlString;

    }

    public String getAuthenticatorSQL(int sqlID) {
        String sqlString = null;
        switch (sqlID) {
        case DefaultRealmConstants.GET_USER:
            sqlString = DefaultRealmConstants.GET_USER_SQL;
            break;
        default:
            System.out.println("Null");
            break;
        }
        return sqlString;
    }

    public String getUserStoreAdminSQL(int sqlID) {
        String sqlString = null;
        switch (sqlID) {
        case DefaultRealmConstants.ADD_USER:
            sqlString = DefaultRealmConstants.ADD_USER_SQL;
            break;
        case DefaultRealmConstants.ADD_ROLE:
            sqlString = DefaultRealmConstants.ADD_ROLE_SQL;
            break;
        case DefaultRealmConstants.ADD_USER_ROLE:
            sqlString = DefaultRealmConstants.ADD_USER_ROLE_SQL;
            break;
        case DefaultRealmConstants.ADD_USER_ATTRIBUTE:
            sqlString = DefaultRealmConstants.ADD_USER_ATTRIBUTE_SQL;
            break;
        case DefaultRealmConstants.ADD_ROLE_ATTRIBUTE:
            sqlString = DefaultRealmConstants.ADD_ROLE_ATTRIBUTE_SQL;
            break;
        case DefaultRealmConstants.UPDATE_USER:
            sqlString = DefaultRealmConstants.UPDATE_USER_SQL;
            break;
        case DefaultRealmConstants.DELETE_USER:
            sqlString = DefaultRealmConstants.DELETE_USER_SQL;
            break;
        case DefaultRealmConstants.DELETE_ROLE:
            sqlString = DefaultRealmConstants.DELETE_ROLE_SQL;
            break;
        case DefaultRealmConstants.DELETE_USER_ROLE:
            sqlString = DefaultRealmConstants.DELETE_USER_ROLE_SQL;
            break;
        case DefaultRealmConstants.DELETE_ROLE_ATTRIBUTE:
            sqlString = DefaultRealmConstants.DELETE_ROLE_ATTRIBUTE_SQL;
            break;
        case DefaultRealmConstants.DELETE_USER_ATTRIBUTE:
            sqlString = DefaultRealmConstants.DELETE_USER_ATTRIBUTE_SQL;
            break;
        case DefaultRealmConstants.GET_ROLE:
            sqlString = DefaultRealmConstants.GET_ROLE_SQL;
            break;
        default:
            System.out.println("Null");
            break;
        }
        return sqlString;
    }

    public String getCommonSQL(int sqlID) {
        String sqlString = null;
        switch (sqlID) {
        case DefaultRealmConstants.GET_USER_ID:
            sqlString = DefaultRealmConstants.GET_USER_ID_SQL;
            break;
        case DefaultRealmConstants.GET_ROLE_ID:
            sqlString = DefaultRealmConstants.GET_ROLE_ID_SQL;
            break;
        case DefaultRealmConstants.GET_USER_ROLES:
            sqlString = DefaultRealmConstants.GET_USER_ROLES_SQL;
            break;
        default:
            System.out.println("Null");
            break;
        }
        return sqlString;
    }

    public String getAccessControlAdminSQL(int sqlID) {
        String sqlString = null;
        switch (sqlID) {
        case DefaultRealmConstants.ADD_PERMISSION:
            sqlString = DefaultRealmConstants.ADD_PERMISSION_SQL;
            break;
        case DefaultRealmConstants.ADD_ROLE_PERMISSION:
            sqlString = DefaultRealmConstants.ADD_ROLE_PERMISSION_SQL;
            break;
        case DefaultRealmConstants.ADD_USER_PERMISSION:
            sqlString = DefaultRealmConstants.ADD_USER_PERMISSION_SQL;
            break;

        case DefaultRealmConstants.DELETE_PERMISSION_ON_RESOURCE:
            sqlString = DefaultRealmConstants.DELETE_PERMISSION_ON_RESOURCE_SQL;
            break;
        case DefaultRealmConstants.DELETE_USER_PERMISSION:
            sqlString = DefaultRealmConstants.DELETE_USER_PERMISSION_SQL;
            break;
        case DefaultRealmConstants.DELETE_ROLE_PERMISSION:
            sqlString = DefaultRealmConstants.DELETE_ROLE_PERMISSION_SQL;
            break;

        default:
            break;
        }
        return sqlString;
    }

    public String getAuthorizerSQL(int sqlID) {
        String sqlString = null;
        switch (sqlID) {

        case DefaultRealmConstants.GET_PERMISSION:
            sqlString = DefaultRealmConstants.GET_PERMISSION_SQL;
            break;
        case DefaultRealmConstants.GET_ROLE_AUTHORIZED:
            sqlString = DefaultRealmConstants.GET_ROLE_AUTHORIZED_SQL;
            break;
        case DefaultRealmConstants.GET_USER_AUTHORIZED:
            sqlString = DefaultRealmConstants.GET_USER_AUTHORIZED_SQL;
            break;
        case DefaultRealmConstants.GET_ALLOWED_ROLES_FOR_RESOURCE:
            sqlString = DefaultRealmConstants.GET_ALLOWED_ROLES_FOR_RESOURCE_SQL;
            break;
        case DefaultRealmConstants.GET_DENIED_ROLES_FOR_RESOURCE:
            sqlString = DefaultRealmConstants.GET_DENIED_ROLES_FOR_RESOURCE_SQL;
            break;
        case DefaultRealmConstants.GET_ALLOWED_USERS_ON_RESOURCE:
            sqlString = DefaultRealmConstants.GET_ALLOWED_USERS_ON_RESOURCE_SQL;
            break;
        case DefaultRealmConstants.GET_DENIED_USERS_ON_RESOURCE:
            sqlString = DefaultRealmConstants.GET_DENIED_USERS_ON_RESOURCE_SQL;
            break;
        case DefaultRealmConstants.GET_ROLE_PERMISSION:
            sqlString = DefaultRealmConstants.GET_ROLE_PERMISSION_SQL;
            break;
        case DefaultRealmConstants.GET_USER_PERMISSION:
            sqlString = DefaultRealmConstants.GET_USER_PERMISSION_SQL;
            break;
        case DefaultRealmConstants.GET_RESOURCE_PERMISSION:
            sqlString = DefaultRealmConstants.GET_RESOURCE_PERMISSION_SQL;
            break;
        default:
            break;
        }
        return sqlString;
    }

    public String getUserStoreReaderSQL(int sqlID) {
        String sqlString = null;
        switch (sqlID) {
        case DefaultRealmConstants.GET_USER_ROLES:
            sqlString = DefaultRealmConstants.GET_USER_ROLES_SQL;
            break;
        case DefaultRealmConstants.GET_ROLE_ATTRIBUTES:
            sqlString = DefaultRealmConstants.GET_ROLE_ATTRIBUTES_SQL;
            break;
        case DefaultRealmConstants.GET_USER_ATTRIBUTES:
            sqlString = DefaultRealmConstants.GET_USER_ATTRIBUTES_SQL;
            break;
        case DefaultRealmConstants.GET_ATTRIBUTE_NAMES:
            sqlString = DefaultRealmConstants.GET_ATTRIBUTE_NAMES_SQL;
            break;
        case DefaultRealmConstants.GET_USERS_IN_ROLE:
            sqlString = DefaultRealmConstants.GET_USERS_IN_ROLE_SQL;
            break;
        case DefaultRealmConstants.GET_USERS_WITH_PROPERTY:
            sqlString = DefaultRealmConstants.GET_USERS_WITH_PROPERTY_SQL;
            break;
        case DefaultRealmConstants.GET_USERS_WITH_PROPERTY_VALUE:
            sqlString = DefaultRealmConstants.GET_USERS_WITH_PROPERTY_VALUE_SQL;
            break;
        case DefaultRealmConstants.GET_USERS_ALL:
            sqlString = DefaultRealmConstants.GET_USERS_ALL_SQL;
            break;
        case DefaultRealmConstants.GET_ROLES_ALL:
            sqlString = DefaultRealmConstants.GET_ROLES_ALL_SQL;
            break;
        default:
            break;
        }
        return sqlString;
    }

    public String getUserId(String userName) throws UserManagerException {
        String id = null;
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserIdStmt = dbConnection
                    .prepareStatement(this
                            .getCommonSQL(DefaultRealmConstants.GET_USER_ID));
            getUserIdStmt.setString(1, userName);
            ResultSet rs = getUserIdStmt.executeQuery();
            if (rs.next()) {
                id = rs.getString(this
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ID));
            }
            getUserIdStmt.close();
        } catch (SQLException e) {
            throw new UserManagerException("errorReadingFromUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
        return id;
    }

    public String getRoleId(String roleName) throws UserManagerException {
        String id = null;
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getRolIdStmt = dbConnection.prepareStatement(this
                    .getCommonSQL(DefaultRealmConstants.GET_ROLE_ID));
            getRolIdStmt.setString(1, roleName);
            ResultSet rs = getRolIdStmt.executeQuery();
            if (rs.next()) {
                id = rs.getString(this
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ID));
            }

            getRolIdStmt.close();
        } catch (SQLException e) {
            throw new UserManagerException("errorReadingFromUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
        return id;
    }

    public String[] getUserRoles(String userName) throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserRolesStmt = dbConnection
                    .prepareStatement(this
                            .getCommonSQL(DefaultRealmConstants.GET_USER_ROLES));
            getUserRolesStmt.setString(1, userName);
            ResultSet rs = getUserRolesStmt.executeQuery();
            List lst = new LinkedList();
            String colName = this
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ROLE_NAME);
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getUserRolesStmt.close();
        } catch (SQLException e) {
            throw new UserManagerException("errorReadingFromUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
        return names;
    }
}
