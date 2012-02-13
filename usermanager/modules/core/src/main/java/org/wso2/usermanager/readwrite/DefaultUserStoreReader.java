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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreReader;
import org.wso2.usermanager.readwrite.util.DefaultStrategyImpl;

/**
 * Manipulates data in the User Store
 * 
 * @see org.wso2.usermanager.UserStoreReader
 */
public class DefaultUserStoreReader implements UserStoreReader {

    private static Log log = LogFactory.getLog(DefaultUserStoreReader.class);

    protected DataSource dataSource = null;
    protected DefaultStrategy data = null;

    public DefaultUserStoreReader(DataSource dataSource) {
        this.dataSource = dataSource;
        this.data = new DefaultStrategyImpl(dataSource);
    }

    public DefaultUserStoreReader(DataSource dataSource, DefaultStrategy store) {
        this.dataSource = dataSource;
        if (store != null) {
            this.data = store;
        } else {
            this.data = new DefaultStrategyImpl(dataSource);
        }
    }

    public String[] getAllRoleNames() throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getAllRoleNamesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreReaderSQL(DefaultRealmConstants.GET_ROLES_ALL));
            ResultSet rs = getAllRoleNamesStmt.executeQuery();
            List lst = new LinkedList();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ROLE_NAME);
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getAllRoleNamesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
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

    public String[] getAllUserNames() throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getAllUserNamesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreReaderSQL(DefaultRealmConstants.GET_USERS_ALL));
            ResultSet rs = getAllUserNamesStmt.executeQuery();
            List lst = new LinkedList();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_USER_NAME);
            while (rs.next()) {
                String name = rs.getString(colName);
                lst.add(name);
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getAllUserNamesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
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

    public Map getRoleProperties(String roleName) throws UserManagerException {

        Map props = new HashMap();
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getRolePropertiesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreReaderSQL(DefaultRealmConstants.GET_ROLE_ATTRIBUTES));
            getRolePropertiesStmt.setString(1, roleName);
            ResultSet rs = getRolePropertiesStmt.executeQuery();
            String colAttrName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ATTR_NAME);
            String colAttrValue = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ATTR_VALUE);
            while (rs.next()) {
                String name = rs.getString(colAttrName);
                String value = rs.getString(colAttrValue);
                props.put(name, value);
            }
            getRolePropertiesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
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
        return props;

    }

    public Map getUserProperties(String userName) throws UserManagerException {
        Map props = new HashMap();
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserPropertiesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreReaderSQL(DefaultRealmConstants.GET_USER_ATTRIBUTES));
            getUserPropertiesStmt.setString(1, userName);
            ResultSet rs = getUserPropertiesStmt.executeQuery();
            String colAttrName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ATTR_NAME);
            String colAttrValue = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ATTR_VALUE);
            while (rs.next()) {
                String name = rs.getString(colAttrName);
                String value = rs.getString(colAttrValue);
                props.put(name, value);
            }
            getUserPropertiesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
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
        return props;

    }

    public String[] getUserPropertyNames() throws UserManagerException {
        String[] propNames = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserAttributeNamesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreReaderSQL(DefaultRealmConstants.GET_ATTRIBUTE_NAMES));
            ResultSet rs = getUserAttributeNamesStmt.executeQuery();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ATTR_NAME);
            List lst = new ArrayList();
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            propNames = (String[]) lst.toArray(new String[lst.size()]);
            getUserAttributeNamesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
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
        return propNames;
    }

    public String[] getUserRoles(String userName) throws UserManagerException {
        return data.getUserRoles(userName);
    }

    public boolean isExistingUser(String userName) throws UserManagerException {
        boolean isExisting = false;
        if (data.getUserId(userName) != null) {
            isExisting = true;
        }
        return isExisting;
    }
    
    public boolean isExistingRole(String roleName) throws UserManagerException{
        boolean isExisting = false;
        if (data.getRoleId(roleName) != null) {
            isExisting = true;
        }
        return isExisting;
    }

    public String[] getUserNamesWithPropertyValue(String propertyName,
            String propetyValue) throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getPropValue = null;

            if (propertyName != null && propertyName.length() != 0) {
                getPropValue = dbConnection
                        .prepareStatement(data
                                .getUserStoreReaderSQL(DefaultRealmConstants.GET_USERS_WITH_PROPERTY));
                getPropValue.setString(1, propertyName);
                getPropValue.setString(2, propetyValue);
            } else {
                getPropValue = dbConnection
                        .prepareStatement(data
                                .getUserStoreReaderSQL(DefaultRealmConstants.GET_USERS_WITH_PROPERTY_VALUE));
                getPropValue.setString(1, propetyValue);
            }

            ResultSet rs = getPropValue.executeQuery();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_USER_NAME);
            List lst = new ArrayList();
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            names = (String[]) lst.toArray(new String[lst.size()]);
            getPropValue.close();
        } catch (SQLException e) {
            log.debug(e);
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

    public String[] getUsersInRole(String roleName) throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserInRole = dbConnection
                    .prepareStatement(data
                            .getUserStoreReaderSQL(DefaultRealmConstants.GET_USERS_IN_ROLE));
            getUserInRole.setString(1, roleName);
            ResultSet rs = getUserInRole.executeQuery();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_USER_NAME);
            List lst = new ArrayList();
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            names = (String[]) lst.toArray(new String[lst.size()]);
            getUserInRole.close();
        } catch (SQLException e) {
            log.debug(e);
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

    public Map getUserProfileProperties(String username, String profileName)
            throws UserManagerException {
        return null;
    }

    
}
