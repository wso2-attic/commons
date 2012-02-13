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

import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.UserManagerConstants;
import org.wso2.usermanager.UserManagerException;

/**
 * Manipulates access control data in the user store Cannot change admin
 * privileges through this interface
 * 
 * @see org.wso2.usermanager.AccessControlAdmin
 * 
 */
public class ACLAccessControlAdmin
        extends ACLAuthorizer implements AccessControlAdmin {

    private Authorizer authorizer = null;

    private AccessControlAdmin admin = null;

    /** Constructor */
    public ACLAccessControlAdmin(Authorizer athzr, AccessControlAdmin admin,
            AuthorizingRealmConfig authConfig) {
        super(athzr, authConfig);
        this.authorizer = athzr;
        this.admin = admin;
    }

    /**
     * Authorize user
     */
    public void authorizeUser(String userName, String resourceId, String action)
            throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.authorizeUser(userName, resourceId, action);

    }

    /**
     * Deny access to user
     */
    public void denyUser(String userName, String resourceId, String action)
            throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.denyUser(userName, resourceId, action);
    }

    /**
     * Grant access to role
     */
    public void authorizeRole(String roleName, String resourceId, String action)
            throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.authorizeRole(roleName, resourceId, action);
    }

    /**
     * Deny access to role
     */
    public void denyRole(String roleName, String resourceId, String action)
            throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.denyRole(roleName, resourceId, action);
    }

    /**
     * Removes granted access from user
     */
    public void clearUserAuthorization(String userName, String resourceId,
            String action) throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.clearUserAuthorization(userName, resourceId, action);
    }

    /**
     * Removes granted access from Role
     */
    public void clearRoleAuthorization(String roleName, String resourceId,
            String action) throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.clearRoleAuthorization(roleName, resourceId, action);
    }

    /**
     * Clears all authorizations on a Role
     */
    public void clearResourceAuthorizations(String resourceId)
            throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.clearResourceAuthorizations(resourceId);
    }

    /**
     * Duplicates granted/denied access
     */
    public void copyAuthorizations(String fromResourceId, String toResourceId)
            throws UserManagerException {
        doAuthorizationToAuthorize();
        admin.copyAuthorizations(fromResourceId, toResourceId);
    }

    protected void doAuthorizationToAuthorize() throws UserManagerException {

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
