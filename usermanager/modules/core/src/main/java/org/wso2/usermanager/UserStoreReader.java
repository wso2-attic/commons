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
 * Reads data from the user store
 */
public interface UserStoreReader {

    /**
     * Retrieves all user names
     */
    public String[] getAllUserNames() throws UserManagerException;

    /**
     * Checks whether an user exist in the user store
     * 
     * @param userName
     *            TODO
     */
    public boolean isExistingUser(String userName) throws UserManagerException;

    /**
     * Checks whether a role exist in the user store
     * 
     * @param roleName RoleName
     */
    public boolean isExistingRole(String roleName) throws UserManagerException;

    /**
     * Retrieves user properties given the user name
     */
    public Map getUserProperties(String userName) throws UserManagerException;

    /**
     * Retrieves user properties given the user name
     */
    public String[] getUserPropertyNames() throws UserManagerException;

    /**
     * Retrieves user names with the given property values
     */
    public String[] getUserNamesWithPropertyValue(String propertyName,
            String propetyValue) throws UserManagerException;

    /**
     * Retrieves all role names
     */
    public String[] getAllRoleNames() throws UserManagerException;

    /**
     * Retrieves role properties given the role name
     */
    public Map getRoleProperties(String roleName) throws UserManagerException;

    /**
     * Gets all user roles
     */
    public String[] getUserRoles(String userName) throws UserManagerException;

    /**
     * Retrieves users in a role
     */
    public String[] getUsersInRole(String roleName) throws UserManagerException;

     

}
