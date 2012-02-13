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

import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.UserManagerException;

/**
 * @see org.wso2.usermanager.Authorizer
 */
public class ACLAuthorizer implements Authorizer {

    private Authorizer authorizer = null;

    protected AuthorizingRealmConfig config = null;

    protected boolean isAdmin = false;

    public ACLAuthorizer(Authorizer athzr, AuthorizingRealmConfig authConfig) {
        this.authorizer = athzr;
        config = authConfig;
        if (config.isEnableAdminBehavior()
                && ACLAdminChecker.isAdminUser(config
                        .getAuthenticatedUserName())) {
            isAdmin = true;
        }
    }

    public String[] getAllowedRolesForResource(String resourceId, String action)
            throws UserManagerException {
        return authorizer.getAllowedRolesForResource(resourceId, action);
    }

    public String[] getDeniedRolesForResource(String resourceId, String action)
            throws UserManagerException {
        return authorizer.getDeniedRolesForResource(resourceId, action);
    }

    public String[] getAllowedUsersForResource(String resourceId, String action)
            throws UserManagerException {
        return authorizer.getAllowedUsersForResource(resourceId, action);
    }

    public String[] getDeniedUsersForResource(String resourceId, String action)
            throws UserManagerException {
        return authorizer.getDeniedUsersForResource(resourceId, action);
    }

    public boolean isRoleAuthorized(String roleName, String resourceId,
            String action) throws UserManagerException {
        return authorizer.isRoleAuthorized(roleName, resourceId, action);
    }

    public boolean isUserAuthorized(String userName, String resourceId,
            String action) throws UserManagerException {
        return authorizer.isUserAuthorized(userName, resourceId, action);
    }

}
