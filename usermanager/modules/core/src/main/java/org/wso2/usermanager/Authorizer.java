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
 * This perfroms authorization checks on Users and Roles
 */
public interface Authorizer {
    /**
     * Checks wheather an user is authorized to perfrom an action on a resource
     */
    public boolean isUserAuthorized(String userName, String resourceId,
            String action) throws UserManagerException;

    /**
     * Checks wheather an role is authorized to perfrom an action on a resource
     */
    public boolean isRoleAuthorized(String roleName, String resourceId,
            String action) throws UserManagerException;

    /**
     * Returns all users who are authorized to perform an action on a resource
     */
    public String[] getAllowedUsersForResource(String resourceId, String action)
            throws UserManagerException;

    /**
     * Returns all users who are authorized to perform an action on a resource
     */
    public String[] getDeniedUsersForResource(String resourceId, String action)
            throws UserManagerException;

    /**
     * Returns all roles who are authorized to perform an action on a resource
     */
    public String[] getAllowedRolesForResource(String resourceId, String action)
            throws UserManagerException;

    /**
     * Returns all roles who are authorized to perform an action on a resource
     */
    public String[] getDeniedRolesForResource(String resourceId, String action)
            throws UserManagerException;

}
