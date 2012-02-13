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
package org.wso2.usermanager;

import java.util.Map;

/**
 * Manipulates data in the User Store
 */
public interface UserStoreAdmin extends UserStoreReader {

    /**
     * Add user to the user store
     */
    public void addUser(String userName, Object credential)
            throws UserManagerException;

    /**
     * Update user in the user store
     */
    public void updateUser(String userName, Object newCredential,
            Object oldCredential) throws UserManagerException;

    /**
     * Update method for admin
     */
    public void updateUser(String userName, Object newCredential)
            throws UserManagerException;

    /**
     * Delete user from user store
     */
    public void deleteUser(String userName) throws UserManagerException;

    /**
     * Set user properties in the user store
     */
    public void setUserProperties(String userName, Map properties)
            throws UserManagerException;

    /**
     * Add role to user store
     */
    public void addRole(String roleName) throws UserManagerException;

    /**
     * Delete role from user store
     */
    public void deleteRole(String roleName) throws UserManagerException;

    /**
     * Set role properties in the user store
     */
    public void setRoleProperties(String roleName, Map properties)
            throws UserManagerException;

    /**
     * Add user to role
     */
    public void addUserToRole(String userName, String roleName)
            throws UserManagerException;

    /**
     * Delete user from role
     */
    public void removeUserFromRole(String userName, String roleName)
            throws UserManagerException;

   
}
