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

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.readwrite.util.UUIDGenerator;

/**
 * @see org.wso2.usermanager.AccessControlAdmin
 */
public class DefaultAccessControlAdmin
        extends DefaultAuthorizer implements AccessControlAdmin {

    private static Log log = LogFactory.getLog(DefaultAccessControlAdmin.class);

    public DefaultAccessControlAdmin(DataSource dataSource, String algo) {
        super(dataSource, algo);
    }

    public DefaultAccessControlAdmin(DataSource dataSource, String algo,
            DefaultStrategy store) {
        super(dataSource, algo, store);
    }

    public DefaultAccessControlAdmin(DataSource dataSource,
            DefaultStrategy store) {
        super(dataSource, DefaultRealmConfig.PERMISSION_BLOCK_FIRST, store);
    }

    public void clearUserAuthorization(String userName, String resourceId,
            String action) throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getPermission = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_PERMISSION));
            getPermission.setString(1, resourceId);
            getPermission.setString(2, action);

            ResultSet rs = getPermission.executeQuery();
            String pid = null;
            if (rs.next()) {
                pid = rs.getString(data
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ID));
            } else {
                throw new UserManagerException("nullData");
            }
            String uid = data.getUserId(userName);

            PreparedStatement clearUPStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_USER_PERMISSION));
            clearUPStmt.setString(1, uid);
            clearUPStmt.setString(2, pid);
            clearUPStmt.executeUpdate();
            dbConnection.commit();
            getPermission.close();
            clearUPStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
    }

    public void denyUser(String userName, String resourceId, String action)
            throws UserManagerException {
        String permissionId = this.getOrAddPermissionId(resourceId, action);

        String userId = data.getUserId(userName);

        if (userId == null) {
            throw new UserManagerException("nullUser");
        }

        Connection dbConnection = null;
        String idUserPermission = UUIDGenerator.getUUID();
        short allow = 0;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement clearUPStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_USER_PERMISSION));
            clearUPStmt.setString(1, userId);
            clearUPStmt.setString(2, permissionId);
            clearUPStmt.executeUpdate();

            PreparedStatement addUserPermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.ADD_USER_PERMISSION));
            addUserPermissionStmt.setString(1, permissionId);
            addUserPermissionStmt.setShort(2, allow);
            addUserPermissionStmt.setString(3, userId);
            addUserPermissionStmt.setString(4, idUserPermission);
            addUserPermissionStmt.executeUpdate();
            dbConnection.commit();
            addUserPermissionStmt.close();
            clearUPStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }

    }

    public void authorizeRole(String roleName, String resourceId, String action)
            throws UserManagerException {
        String roleId = data.getRoleId(roleName);

        if (roleId == null) {
            throw new UserManagerException("nullRole");
        }

        String permissionId = this.getOrAddPermissionId(resourceId, action);
        Connection dbConnection = null;
        String idRolePermission = UUIDGenerator.getUUID();
        short allow = 1;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement deleteRolesStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_ROLE_PERMISSION));
            deleteRolesStmt.setString(1, roleId);
            deleteRolesStmt.setString(2, permissionId);
            deleteRolesStmt.executeUpdate();

            PreparedStatement addRolePermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.ADD_ROLE_PERMISSION));
            addRolePermissionStmt.setString(1, permissionId);
            addRolePermissionStmt.setShort(2, allow);
            addRolePermissionStmt.setString(3, roleId);
            addRolePermissionStmt.setString(4, idRolePermission);
            addRolePermissionStmt.executeUpdate();
            dbConnection.commit();
            addRolePermissionStmt.close();
            deleteRolesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
    }

    public void denyRole(String roleName, String resourceId, String action)
            throws UserManagerException {

        String roleId = data.getRoleId(roleName);

        if (roleId == null) {
            throw new UserManagerException("nullRole");
        }

        String permissionId = this.getOrAddPermissionId(resourceId, action);
        Connection dbConnection = null;
        String idRolePermission = UUIDGenerator.getUUID();
        short allow = 0;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            // There can be only one permission for a role and a permission
            PreparedStatement deleteRolesStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_ROLE_PERMISSION));
            deleteRolesStmt.setString(1, roleId);
            deleteRolesStmt.setString(2, permissionId);
            deleteRolesStmt.executeUpdate();

            PreparedStatement addRolePermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.ADD_ROLE_PERMISSION));
            addRolePermissionStmt.setString(1, permissionId);
            addRolePermissionStmt.setShort(2, allow);
            addRolePermissionStmt.setString(3, roleId);
            addRolePermissionStmt.setString(4, idRolePermission);
            addRolePermissionStmt.executeUpdate();
            dbConnection.commit();
            addRolePermissionStmt.close();
            deleteRolesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
    }

    public void clearRoleAuthorization(String roleName, String resourceId,
            String action) throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getPermission = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_PERMISSION));
            getPermission.setString(1, resourceId);
            getPermission.setString(2, action);

            ResultSet rs = getPermission.executeQuery();
            String pid = null;
            if (rs.next()) {
                pid = rs.getString(data
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ID));
            } else {
                throw new UserManagerException("nullData");
            }

            String rid = data.getRoleId(roleName);

            PreparedStatement deleteRolesStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_ROLE_PERMISSION));
            deleteRolesStmt.setString(1, rid);
            deleteRolesStmt.setString(2, pid);
            deleteRolesStmt.executeUpdate();

            dbConnection.commit();
            getPermission.close();
            deleteRolesStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }

    }

    public void clearResourceAuthorizations(String resourceId)
            throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement clearResourceAuthorizationsStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_PERMISSION_ON_RESOURCE));
            clearResourceAuthorizationsStmt.setString(1, resourceId);
            clearResourceAuthorizationsStmt.executeUpdate();
            dbConnection.commit();
            clearResourceAuthorizationsStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
    }

    public void copyAuthorizations(String fromResourceId, String toResourceId)
            throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getResourcePermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_RESOURCE_PERMISSION));
            getResourcePermissionStmt.setString(1, toResourceId);
            ResultSet rs = getResourcePermissionStmt.executeQuery();
            if (rs.next()) {
                throw new UserManagerException(
                        "copyAuthorizationFailedPermissionExist");
            }

            getResourcePermissionStmt.setString(1, fromResourceId);
            rs = getResourcePermissionStmt.executeQuery();
            PreparedStatement addPermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.ADD_PERMISSION));
            while (rs.next()) {
                String action = rs.getString(data
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ACTION));
                String oldPermissionId = rs.getString(data
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ID));
                String idPermission = UUIDGenerator.getUUID();
                addPermissionStmt.setString(1, toResourceId);
                addPermissionStmt.setString(2, action);
                addPermissionStmt.setString(3, idPermission);
                addPermissionStmt.executeUpdate();

                PreparedStatement getRolePermissionStmt = dbConnection
                        .prepareStatement(data
                                .getAuthorizerSQL(DefaultRealmConstants.GET_ROLE_PERMISSION));

                getRolePermissionStmt.setString(1, oldPermissionId);
                ResultSet rsr = getRolePermissionStmt.executeQuery();
                PreparedStatement addRolePermissionStmt = dbConnection
                        .prepareStatement(data
                                .getAccessControlAdminSQL(DefaultRealmConstants.ADD_ROLE_PERMISSION));

                while (rsr.next()) {
                    String idRolePermission = UUIDGenerator.getUUID();
                    String roleId = rsr
                            .getString(data
                                    .getColumnName(DefaultRealmConstants.COLUMN_ID_ROLE_ID));
                    short allow = rsr
                            .getShort(data
                                    .getColumnName(DefaultRealmConstants.COLUMN_ID_IS_ALLOWED));

                    addRolePermissionStmt.setString(1, idPermission);
                    addRolePermissionStmt.setShort(2, allow);
                    addRolePermissionStmt.setString(3, roleId);
                    addRolePermissionStmt.setString(4, idRolePermission);
                    addRolePermissionStmt.executeUpdate();
                }

                PreparedStatement getUserPermissionStmt = dbConnection
                        .prepareStatement(data
                                .getAuthorizerSQL(DefaultRealmConstants.GET_USER_PERMISSION));

                getUserPermissionStmt.setString(1, oldPermissionId);
                ResultSet rsu = getUserPermissionStmt.executeQuery();

                PreparedStatement addUserPermissionStmt = dbConnection
                        .prepareStatement(data
                                .getAccessControlAdminSQL(DefaultRealmConstants.ADD_USER_PERMISSION));

                while (rsu.next()) {
                    String idUserPermission = UUIDGenerator.getUUID();
                    String userId = rsu
                            .getString(data
                                    .getColumnName(DefaultRealmConstants.COLUMN_ID_USER_ID));
                    short allow = rsu
                            .getShort(data
                                    .getColumnName(DefaultRealmConstants.COLUMN_ID_IS_ALLOWED));

                    addUserPermissionStmt.setString(1, idPermission);
                    addUserPermissionStmt.setShort(2, allow);
                    addUserPermissionStmt.setString(3, userId);
                    addUserPermissionStmt.setString(4, idUserPermission);
                    addUserPermissionStmt.executeUpdate();
                }

                getRolePermissionStmt.close();
                addRolePermissionStmt.close();
                getUserPermissionStmt.close();
                addUserPermissionStmt.close();

            }

            getResourcePermissionStmt.close();
            addPermissionStmt.close();

            dbConnection.commit();

        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorCopyingAuthorizations", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }

    }

    public void authorizeUser(String userName, String resourceId, String action)
            throws UserManagerException {

        String userId = data.getUserId(userName);

        if (userId == null) {
            throw new UserManagerException("nullUser");
        }

        String permissionId = this.getOrAddPermissionId(resourceId, action);
        Connection dbConnection = null;
        String idUserPermission = UUIDGenerator.getUUID();
        short allow = 1;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement clearUPStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.DELETE_USER_PERMISSION));
            clearUPStmt.setString(1, userId);
            clearUPStmt.setString(2, permissionId);
            clearUPStmt.executeUpdate();

            PreparedStatement addUserPermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.ADD_USER_PERMISSION));
            addUserPermissionStmt.setString(1, permissionId);
            addUserPermissionStmt.setShort(2, allow);
            addUserPermissionStmt.setString(3, userId);
            addUserPermissionStmt.setString(4, idUserPermission);
            addUserPermissionStmt.executeUpdate();
            dbConnection.commit();
            addUserPermissionStmt.close();
            clearUPStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }

    }

    /**
     * Gets the permission if it exists, else create the permission and returns
     * the id.
     * 
     * @return
     */
    protected String getOrAddPermissionId(String resourceId, String action)
            throws UserManagerException {
        String permissionId = null;
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getPermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAuthorizerSQL(DefaultRealmConstants.GET_PERMISSION));
            getPermissionStmt.setString(1, resourceId);
            getPermissionStmt.setString(2, action);

            ResultSet rs = getPermissionStmt.executeQuery();

            if (rs.next()) {
                permissionId = rs.getString(data
                        .getColumnName(DefaultRealmConstants.COLUMN_ID_ID));
            }

            PreparedStatement addPermissionStmt = dbConnection
                    .prepareStatement(data
                            .getAccessControlAdminSQL(DefaultRealmConstants.ADD_PERMISSION));

            if (permissionId == null) {
                // no permission so add it
                String idPermission = UUIDGenerator.getUUID();
                addPermissionStmt.setString(1, resourceId);
                addPermissionStmt.setString(2, action);
                addPermissionStmt.setString(3, idPermission);
                addPermissionStmt.executeUpdate();
                dbConnection.commit();
                permissionId = idPermission;
            }

        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }

        return permissionId;

    }

}
