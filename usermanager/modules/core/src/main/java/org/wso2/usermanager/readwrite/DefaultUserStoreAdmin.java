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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.readwrite.util.UUIDGenerator;
import org.wso2.usermanager.util.Base64;

/**
 * Manipulates data in the User Store
 * 
 * @see org.wso2.usermanager.UserStoreAdmin
 */
public class DefaultUserStoreAdmin
        extends DefaultUserStoreReader implements UserStoreAdmin {

    private static Log log = LogFactory.getLog(DefaultUserStoreAdmin.class);

    public DefaultUserStoreAdmin(DataSource dataSource) {
        super(dataSource);
    }

    public DefaultUserStoreAdmin(DataSource dataSource, DefaultStrategy store) {
        super(dataSource, store);
    }

    public void addUser(String userName, Object credential)
            throws UserManagerException {

        if (userName == null || credential == null) {
            throw new UserManagerException("nullData");
        }

        if (!(credential instanceof String)) {
            throw new UserManagerException("credentialTypeNotSupport");
        }

        if (userName.trim().length() == 0) {
            throw new UserManagerException("nullUser");
        }

        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement addUserStmt = dbConnection.prepareStatement(data
                    .getUserStoreAdminSQL(DefaultRealmConstants.ADD_USER));

            addUserStmt.setString(1, userName);
            MessageDigest dgst = MessageDigest.getInstance("MD5");
            dgst.update(((String) credential).getBytes());
            addUserStmt.setString(2, Base64.encode(dgst.digest()));
            String idUser = UUIDGenerator.getUUID();
            addUserStmt.setString(3, idUser);
            addUserStmt.executeUpdate();
            dbConnection.commit();
            addUserStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } catch (NoSuchAlgorithmException e) {
            log.debug(e);
            throw new UserManagerException("errorCreatingPasswordDigest", e);
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

    public void updateUser(String userName, Object newCredential,
            Object oldCredential) throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserStmt = dbConnection.prepareStatement(data
                    .getAuthenticatorSQL(DefaultRealmConstants.GET_USER));
            getUserStmt.setString(1, userName);

            ResultSet rs = getUserStmt.executeQuery();
            if (rs.next()) {
                if (oldCredential != null) {
                    MessageDigest dgst = MessageDigest.getInstance("MD5");
                    dgst.update(((String) oldCredential).getBytes());
                    String dbCred = rs
                            .getString(data
                                    .getColumnName(DefaultRealmConstants.COLUMN_ID_CREDENTIAL));
                    if (!Base64.encode(dgst.digest()).equals(dbCred)) {
                        throw new UserManagerException("oldPasswordIncorrect");
                    }
                }
            } else {
                throw new UserManagerException("oldPasswordIncorrect");
            }

            PreparedStatement updateUserStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.UPDATE_USER));
            MessageDigest dgst = MessageDigest.getInstance("MD5");
            dgst.update(((String) newCredential).getBytes());
            updateUserStmt.setString(1, Base64.encode(dgst.digest()));
            updateUserStmt.setString(2, userName);
            updateUserStmt.executeUpdate();
            dbConnection.commit();
            updateUserStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } catch (NoSuchAlgorithmException e) {
            log.debug(e);
            throw new UserManagerException("errorCreatingPasswordDigest", e);
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

    public void updateUser(String userName, Object newCredential)
            throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement updateUserStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.UPDATE_USER));
            MessageDigest dgst = MessageDigest.getInstance("MD5");
            dgst.update(((String) newCredential).getBytes());
            updateUserStmt.setString(1, Base64.encode(dgst.digest()));
            updateUserStmt.setString(2, userName);
            updateUserStmt.executeUpdate();
            dbConnection.commit();
            updateUserStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorModifyingUserStore", e);
        } catch (NoSuchAlgorithmException e) {
            log.debug(e);
            throw new UserManagerException("errorCreatingPasswordDigest", e);
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

    public void deleteUser(String userName) throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement deleteUserStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.DELETE_USER));
            deleteUserStmt.setString(1, userName);
            deleteUserStmt.executeUpdate();
            dbConnection.commit();
            deleteUserStmt.close();
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

    public void setUserProperties(String userName, Map properties)
            throws UserManagerException {

        String userid = data.getUserId(userName);
        if (userid == null) {
            throw new UserManagerException("nullUser");
        }

        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement deleteUserPropsStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.DELETE_USER_ATTRIBUTE));
            deleteUserPropsStmt.setString(1, userid);
            deleteUserPropsStmt.executeUpdate();

            PreparedStatement setUserPropertiesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.ADD_USER_ATTRIBUTE));

            if (properties != null) {
                Iterator ite = properties.entrySet().iterator();
                while (ite.hasNext()) {
                    Entry entry = (Entry) ite.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (value != null) {
                        String idUserAttribute = UUIDGenerator.getUUID();
                        setUserPropertiesStmt.setString(1, key);
                        setUserPropertiesStmt.setString(2, value);
                        setUserPropertiesStmt.setString(3, userid);
                        setUserPropertiesStmt.setString(4, idUserAttribute);
                        setUserPropertiesStmt.executeUpdate();
                    }
                }
            }
            dbConnection.commit();

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

    public void setRoleProperties(String roleName, Map properties)
            throws UserManagerException {

        String roleid = data.getRoleId(roleName);
        if (roleid == null) {
            throw new UserManagerException("nullRole");
        }
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement deleteRolePropsStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.DELETE_ROLE_ATTRIBUTE));
            deleteRolePropsStmt.setString(1, roleid);
            deleteRolePropsStmt.executeUpdate();

            PreparedStatement setRolePropertiesStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.ADD_ROLE_ATTRIBUTE));

            Iterator ite = properties.entrySet().iterator();
            while (ite.hasNext()) {
                Entry entry = (Entry) ite.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (value != null) {
                    String idRoleAttribute = UUIDGenerator.getUUID();
                    setRolePropertiesStmt.setString(1, key);
                    setRolePropertiesStmt.setString(2, value);
                    setRolePropertiesStmt.setString(3, roleid);
                    setRolePropertiesStmt.setString(4, idRoleAttribute);
                    setRolePropertiesStmt.executeUpdate();
                }
            }
            dbConnection.commit();

            deleteRolePropsStmt.close();
            setRolePropertiesStmt.close();

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

    public void addRole(String roleName) throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement addRoleStmt = dbConnection.prepareStatement(data
                    .getUserStoreAdminSQL(DefaultRealmConstants.ADD_ROLE));
            String idRole = UUIDGenerator.getUUID();
            addRoleStmt.setString(1, roleName);
            addRoleStmt.setString(2, idRole);
            addRoleStmt.execute();
            dbConnection.commit();
            addRoleStmt.close();
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

    public void deleteRole(String roleName) throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement deleteRoleStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.DELETE_ROLE));
            deleteRoleStmt.setString(1, roleName);
            deleteRoleStmt.execute();
            dbConnection.commit();
            deleteRoleStmt.close();
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

    public void addUserToRole(String userName, String roleName)
            throws UserManagerException {

        String userid = data.getUserId(userName);
        String roleid = data.getRoleId(roleName);

        if (roleid == null || userid == null) {
            throw new UserManagerException("nullData");
        }
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement addUserToRoleStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.ADD_USER_ROLE));
            String id = UUIDGenerator.getUUID();
            addUserToRoleStmt.setString(1, id);
            addUserToRoleStmt.setString(2, userid);
            addUserToRoleStmt.setString(3, roleid);
            addUserToRoleStmt.executeUpdate();
            dbConnection.commit();
            addUserToRoleStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorAddingUserToRole", e);
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

    public void removeUserFromRole(String userName, String roleName)
            throws UserManagerException {
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement removeUserFromRoleStmt = dbConnection
                    .prepareStatement(data
                            .getUserStoreAdminSQL(DefaultRealmConstants.DELETE_USER_ROLE));
            removeUserFromRoleStmt.setString(1, userName);
            removeUserFromRoleStmt.setString(2, roleName);
            removeUserFromRoleStmt.executeUpdate();
            dbConnection.commit();
            removeUserFromRoleStmt.close();
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

    public void addUserProfile(String username, String profileName,
            boolean isDefault) throws UserManagerException {
        // TODO Auto-generated method stub
        
    }

    public void deleteUserProfile(String username, String profileName)
            throws UserManagerException {
        // TODO Auto-generated method stub
        
    }

    public void setUserProfileProperties(String username, String profileName,
            Map properties) throws UserManagerException {
        // TODO Auto-generated method stub
        
    }
    
    
    

}
