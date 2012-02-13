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
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.readwrite.util.DefaultStrategyImpl;

/**
 * @see org.wso2.usermanager.Authorizer
 */
public class DefaultAuthorizer implements Authorizer {

    /** DBCP datasource */
    protected DataSource dataSource = null;

    /** The object that pumps sqls */
    protected DefaultStrategy data = null;

    /** logger */
    private static Log log = LogFactory.getLog(DefaultAuthorizer.class);

    /** Algorithm to authorize users */
    private String algorithm = null;

    public DefaultAuthorizer(DataSource dataSource, String algo) {
        this.dataSource = dataSource;
        this.data = new DefaultStrategyImpl(dataSource);
        this.algorithm = algo;
    }

    public DefaultAuthorizer(DataSource dataSource, String algo,
            DefaultStrategy strategy) {
        this.dataSource = dataSource;
        if (strategy != null) {
            this.data = strategy;
        } else {
            this.data = new DefaultStrategyImpl(dataSource);
        }
        this.algorithm = algo;
    }

    public boolean isRoleAuthorized(String roleName, String resourceId,
            String action) throws UserManagerException {

        Boolean isAuthorized = getRoleAuthorized(roleName, resourceId, action);
        if (isAuthorized == null) {
            return false;
        } else {
            return isAuthorized.booleanValue();
        }
    }

    public boolean isUserAuthorized(String userName, String resourceId,
            String action) throws UserManagerException {
        Boolean isAuthorized = null;

        if (algorithm.equals(DefaultRealmConfig.PERMISSION_USER_ONLY)) {
            isAuthorized = getUserAuthorized(userName, resourceId, action);
        } else if (algorithm.equals(DefaultRealmConfig.PERMISSION_BLOCK_FIRST)) {
            isAuthorized = getUserAuthorizationConsideringRoles(userName,
                    resourceId, action);
        }

        if (isAuthorized == null) {
            return false;
        } else {
            return isAuthorized.booleanValue();
        }
    }

    public String[] getAllowedUsersForResource(String resourceId, String action)
            throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getAuthorizedUsersForResourceStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_ALLOWED_USERS_ON_RESOURCE));
            getAuthorizedUsersForResourceStmt.setString(1, resourceId);
            getAuthorizedUsersForResourceStmt.setString(2, action);
            ResultSet rs = getAuthorizedUsersForResourceStmt.executeQuery();
            List lst = new LinkedList();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_USER_NAME);
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getAuthorizedUsersForResourceStmt.close();
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

    public String[] getDeniedUsersForResource(String resourceId, String action)
            throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getAuthorizedUsersForResourceStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_DENIED_USERS_ON_RESOURCE));
            getAuthorizedUsersForResourceStmt.setString(1, resourceId);
            getAuthorizedUsersForResourceStmt.setString(2, action);
            ResultSet rs = getAuthorizedUsersForResourceStmt.executeQuery();
            List lst = new LinkedList();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_USER_NAME);
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getAuthorizedUsersForResourceStmt.close();
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

    public String[] getAllowedRolesForResource(String resourceId, String action)
            throws UserManagerException {
        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getAuthorizedRolesForResourceStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_ALLOWED_ROLES_FOR_RESOURCE));
            getAuthorizedRolesForResourceStmt.setString(1, resourceId);
            getAuthorizedRolesForResourceStmt.setString(2, action);
            ResultSet rs = getAuthorizedRolesForResourceStmt.executeQuery();
            List lst = new LinkedList();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ROLE_NAME);
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getAuthorizedRolesForResourceStmt.close();
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

    public String[] getDeniedRolesForResource(String resourceId, String action)
            throws UserManagerException {

        String[] names = new String[0];
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getAuthorizedRolesForResourceStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_DENIED_ROLES_FOR_RESOURCE));
            getAuthorizedRolesForResourceStmt.setString(1, resourceId);
            getAuthorizedRolesForResourceStmt.setString(2, action);
            ResultSet rs = getAuthorizedRolesForResourceStmt.executeQuery();
            List lst = new LinkedList();
            String colName = data
                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ROLE_NAME);
            while (rs.next()) {
                lst.add(rs.getString(colName));
            }
            if (lst.size() > 0) {
                names = (String[]) lst.toArray(new String[lst.size()]);
            }
            getAuthorizedRolesForResourceStmt.close();
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

    private Boolean getUserAuthorizationConsideringRoles(String userName,
            String resourceId, String action) throws UserManagerException {
        Boolean isAuthorized = getUserAuthorized(userName, resourceId, action);

        boolean onceAllowed = false;
        if (isAuthorized == null) {
            String[] roles = data.getUserRoles(userName);

            for (int i = 0; i < roles.length; i++) {
                String roleName = roles[i];
                Boolean roleAuth = getRoleAuthorized(roleName, resourceId,
                        action);
                if (roleAuth != null) {
                    if (roleAuth.booleanValue() == false) {
                        isAuthorized = roleAuth;
                        break;
                    } else {
                        onceAllowed = true;
                    }
                }
            }
        }

        if (isAuthorized == null && onceAllowed == true) {
            isAuthorized = new Boolean(true);
        }

        return isAuthorized;
    }

    private Boolean getRoleAuthorized(String roleName, String resourceId,
            String action) throws UserManagerException {
        Boolean isAuthorized = null;
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement isRoleAuthorizedStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_ROLE_AUTHORIZED));
            isRoleAuthorizedStmt.setString(1, resourceId);
            isRoleAuthorizedStmt.setString(2, action);
            isRoleAuthorizedStmt.setString(3, roleName);
            ResultSet rs = isRoleAuthorizedStmt.executeQuery();
            if (rs.next()) {
                boolean isAuth = rs
                        .getBoolean(data
                                .getColumnName(DefaultRealmConstants.COLUMN_ID_IS_ALLOWED));
                isAuthorized = Boolean.valueOf(isAuth);
            }
            isRoleAuthorizedStmt.close();
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
        return isAuthorized;
    }

    private Boolean getUserAuthorized(String userName, String resourceId,
            String action) throws UserManagerException {
        Boolean result = null;
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement isUserAuthorizedStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_USER_AUTHORIZED));
            isUserAuthorizedStmt.setString(1, resourceId);
            isUserAuthorizedStmt.setString(2, action);
            isUserAuthorizedStmt.setString(3, userName);
            ResultSet rs = isUserAuthorizedStmt.executeQuery();
            if (rs.next()) {
                result = Boolean
                        .valueOf(rs
                                .getBoolean(data
                                        .getColumnName(DefaultRealmConstants.COLUMN_ID_IS_ALLOWED)));
            }
            isUserAuthorizedStmt.close();
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
        return result;
    }

    public DefaultStrategy getData() {
        return data;
    }

    public void setData(DefaultStrategy data) {
        this.data = data;
    }

}
