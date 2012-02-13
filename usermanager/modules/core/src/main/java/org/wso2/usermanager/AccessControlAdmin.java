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

/**
 * Manipulates access control data in the user store
 */
public interface AccessControlAdmin extends Authorizer {

    /**
     * Grant access to user.
     */
    public void authorizeUser(String userName, String resourceId, String action)
            throws UserManagerException;

    /**
     * Deny access to user
     */
    public void denyUser(String userName, String resourceId, String action)
            throws UserManagerException;

    /**
     * Grant access to role
     */
    public void authorizeRole(String roleName, String resourceId, String action)
            throws UserManagerException;

    /**
     * Deny access to role
     */
    public void denyRole(String roleName, String resourceId, String action)
            throws UserManagerException;

    /**
     * Removes granted access from user
     */
    public void clearUserAuthorization(String userName, String resourceId,
            String action) throws UserManagerException;

    /**
     * Removes granted access from Role
     */
    public void clearRoleAuthorization(String roleName, String resourceId,
            String action) throws UserManagerException;

    /**
     * Clears all authorizations on a Role
     */
    public void clearResourceAuthorizations(String resourceId)
            throws UserManagerException;

    /**
     * Duplicates granted/denied access
     */
    public void copyAuthorizations(String fromResourceId, String toResourceId)
            throws UserManagerException;
}
