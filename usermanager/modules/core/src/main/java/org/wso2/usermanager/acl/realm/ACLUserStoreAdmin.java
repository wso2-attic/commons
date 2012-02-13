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
package org.wso2.usermanager.acl.realm;

import java.util.Map;

import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.UserManagerConstants;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;

/**
 * Manipulates data in the User Store
 * 
 * @see org.wso2.usermanager.UserStoreAdmin
 */
public class ACLUserStoreAdmin
        extends ACLUserStoreReader implements UserStoreAdmin {

    private Authorizer authorizer = null;

    private UserStoreAdmin usAdmin = null;

    public ACLUserStoreAdmin(Authorizer athzr, UserStoreAdmin admin,
            AuthorizingRealmConfig authConfig) {
        super(athzr, admin, authConfig);
        this.authorizer = athzr;
        this.usAdmin = admin;
    }

    /**
     * Add user to the user store
     */
    public void addUser(String userName, Object credential)
            throws UserManagerException {
        if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.USER_RESOURCE, UserManagerConstants.ADD)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.USER_RESOURCE,
                    UserManagerConstants.ADD });
        }
        usAdmin.addUser(userName, credential);
    }

    /**
     * Update user in the user store
     */
    public void updateUser(String userName, Object newCredential,
            Object oldCredential) throws UserManagerException {

        if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.USER_RESOURCE, UserManagerConstants.EDIT)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.USER_RESOURCE,
                    UserManagerConstants.EDIT });
        }
        usAdmin.updateUser(userName, newCredential, oldCredential);
    }

    public void updateUser(String userName, Object newCredential)
            throws UserManagerException {
        if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.USER_RESOURCE, UserManagerConstants.EDIT)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.USER_RESOURCE,
                    UserManagerConstants.EDIT });
        }
        usAdmin.updateUser(userName, newCredential);
    }

    /**
     * Delete user from user store
     */
    public void deleteUser(String userName) throws UserManagerException {
        if (config.getAuthenticatedUserName().equals(userName)) {
            return;
        } else if (ACLAdminChecker.isAdminUser(userName)) {
            /*
             * TODO : Is there a requirement to delete the user if the caller is
             * a Admin
             */
            return;
        } else if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer
                .isUserAuthorized(config.getAuthenticatedUserName(),
                        UserManagerConstants.USER_RESOURCE,
                        UserManagerConstants.DELETE)) {
            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.USER_RESOURCE,
                    UserManagerConstants.DELETE });
        }
        usAdmin.deleteUser(userName);
    }

    /**
     * Set user properties in the user store
     */
    public void setUserProperties(String userName, Map properties)
            throws UserManagerException {
        if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (config.getAuthenticatedUserName().equals(userName)
                && config.isCurrentUserEditable()) {
            // do nothing - authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.USER_RESOURCE, UserManagerConstants.EDIT)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.USER_RESOURCE,
                    UserManagerConstants.EDIT });
        }
        usAdmin.setUserProperties(userName, properties);
    }

    /**
     * Add role to user store
     */
    public void addRole(String roleName) throws UserManagerException {
        if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.ROLE_RESOURCE, UserManagerConstants.ADD)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.ROLE_RESOURCE,
                    UserManagerConstants.ADD });
        }
        usAdmin.addRole(roleName);
    }

    /**
     * Delete role from user store
     */
    public void deleteRole(String roleName) throws UserManagerException {
        if (config.isEnableAdminBehavior()
                && config.getAdminRoleName().equals(roleName)) {
            return;
        } else if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer
                .isUserAuthorized(config.getAuthenticatedUserName(),
                        UserManagerConstants.ROLE_RESOURCE,
                        UserManagerConstants.DELETE)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.ROLE_RESOURCE,
                    UserManagerConstants.DELETE });
        }
        usAdmin.deleteRole(roleName);
    }

    /**
     * Set role properties in the user store
     */
    public void setRoleProperties(String roleName, Map properties)
            throws UserManagerException {
        if (config.isEnableAdminBehavior()
                && config.getAdminRoleName().equals(roleName)) {
            return;
        } else if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.ROLE_RESOURCE, UserManagerConstants.EDIT)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.ROLE_RESOURCE,
                    UserManagerConstants.EDIT });
        }
        usAdmin.setRoleProperties(roleName, properties);
    }

    /**
     * Add user to role
     */
    public void addUserToRole(String userName, String roleName)
            throws UserManagerException {
        if (config.isEnableAdminBehavior()
                && config.getAdminRoleName().equals(roleName)) {
            /*
             * TODO : is there a requirement to perform this action if the
             * caller is a Admin
             */
            return;
        } else if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.ROLE_RESOURCE, UserManagerConstants.ADD)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.ROLE_RESOURCE,
                    UserManagerConstants.ADD });
        }
        usAdmin.addUserToRole(userName, roleName);
    }

    /**
     * Delete user from role
     */
    public void removeUserFromRole(String userName, String roleName)
            throws UserManagerException {
        if (config.isEnableAdminBehavior()
                && config.getAdminRoleName().equals(roleName)) {
            /*
             * TODO : is there a requirement to perform this action if the
             * caller is a Admin
             */
            return;
        } else if (config.isEnableAdminBehavior() && isAdmin) {
            // authorized
        } else if (!authorizer
                .isUserAuthorized(config.getAuthenticatedUserName(),
                        UserManagerConstants.ROLE_RESOURCE,
                        UserManagerConstants.DELETE)) {

            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.ROLE_RESOURCE,
                    UserManagerConstants.DELETE });
        }
        usAdmin.removeUserFromRole(userName, roleName);
    }

    
    

}
