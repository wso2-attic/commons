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
import org.wso2.usermanager.UserStoreReader;

/**
 * @see org.wso2.usermanager.UserStoreReader
 */
public class ACLUserStoreReader implements UserStoreReader {

    private Authorizer authorizer = null;

    private UserStoreReader usReader = null;

    protected AuthorizingRealmConfig config = null;

    protected boolean isAdmin = false;

    public ACLUserStoreReader(Authorizer athzr, UserStoreReader reader,
            AuthorizingRealmConfig authConfig) {
        this.authorizer = athzr;
        this.config = authConfig;
        this.usReader = reader;

        if (config.isEnableAdminBehavior()
                && ACLAdminChecker.isAdminUser(config
                        .getAuthenticatedUserName())) {
            isAdmin = true;
        }

    }

    public String[] getAllUserNames() throws UserManagerException {
        doAuthorizationToReadUser();
        return usReader.getAllUserNames();
    }

    public String[] getUserNamesWithPropertyValue(String propertyName,
            String propetyValue) throws UserManagerException {
        doAuthorizationToReadUser();
        return usReader.getUserNamesWithPropertyValue(propertyName,
                propetyValue);
    }

    public Map getUserProperties(String userName) throws UserManagerException {
        if (config.getAuthenticatedUserName().equals(userName)
                && config.isCurrentUserReadable()) {
            // do nothing authorized
        } else {
            doAuthorizationToReadUser();
        }
        return usReader.getUserProperties(userName);
    }

    public String[] getUserPropertyNames() throws UserManagerException {
        doAuthorizationToReadUser();
        return usReader.getUserPropertyNames();
    }

    public boolean isExistingUser(String userName) throws UserManagerException {
        doAuthorizationToReadUser();
        return usReader.isExistingUser(userName);
    }
    
    public boolean isExistingRole(String roleName) throws UserManagerException{
        doAuthorizationToReadRole();
        return usReader.isExistingUser(roleName);
    }

    public String[] getAllRoleNames() throws UserManagerException {
        doAuthorizationToReadRole();
        return usReader.getAllRoleNames();
    }
    
    public Map getRoleProperties(String roleName) throws UserManagerException {
        doAuthorizationToReadRole();
        return usReader.getRoleProperties(roleName);
    }

    public String[] getUserRoles(String userName) throws UserManagerException {
        doAuthorizationToReadRole();
        return usReader.getUserRoles(userName);
    }

    public String[] getUsersInRole(String roleName) throws UserManagerException {
        doAuthorizationToReadRole();
        return usReader.getUsersInRole(roleName);
    }
    
    

    protected void doAuthorizationToReadRole() throws UserManagerException {

        if (config.isEnableAdminBehavior() && isAdmin) {
            // do nothing user is authenticated
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.ROLE_RESOURCE, UserManagerConstants.READ)) {
            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.ROLE_RESOURCE,
                    UserManagerConstants.READ });
        }
    }

    protected void doAuthorizationToReadUser() throws UserManagerException {

        if (config.isEnableAdminBehavior() && isAdmin) {
            // do nothing user is authenticated
        } else if (!authorizer.isUserAuthorized(config
                .getAuthenticatedUserName(),
                UserManagerConstants.USER_RESOURCE, UserManagerConstants.READ)) {
            throw new UnauthorizedException("unAuthorized", new String[] {
                    UserManagerConstants.USER_RESOURCE,
                    UserManagerConstants.READ });
        }
    }

}
