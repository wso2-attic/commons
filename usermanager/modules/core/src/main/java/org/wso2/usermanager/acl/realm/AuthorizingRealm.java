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
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;

/**
 * ACL aware Realm. Contains an authorized user and a realm. Always checks
 * whether an user is authorized to perform an action before actually executing
 * the action.
 * 
 * @see org.wso2.usermanager.Realm 
 */

public class AuthorizingRealm implements Realm {

    protected String username = null;

    protected Realm realm = null;

    protected Authenticator authenticator = null;

    protected Authorizer authorizer = null;

    protected AccessControlAdmin aclAdmin = null;

    protected UserStoreAdmin usAdmin = null;

    protected UserStoreReader usReader = null;

    AuthorizingRealmConfig config = null;

    public AccessControlAdmin getAccessControlAdmin()
            throws UserManagerException {
        if (aclAdmin == null) {
            throw new UserManagerException("actionNotSupportedByRealm");
        }
        return aclAdmin;
    }

    public Authenticator getAuthenticator() throws UserManagerException {
        if (authenticator == null) {
            throw new UserManagerException("actionNotSupportedByRealm");
        }
        return authenticator;
    }

    public Authorizer getAuthorizer() throws UserManagerException {
        if (authorizer == null) {
            throw new UserManagerException("actionNotSupportedByRealm");
        }
        return authorizer;
    }

    public UserStoreAdmin getUserStoreAdmin() throws UserManagerException {
        if (usAdmin == null) {
            throw new UserManagerException("actionNotSupportedByRealm");
        }
        return usAdmin;
    }

    public UserStoreReader getUserStoreReader() throws UserManagerException {
        if (usReader == null) {
            throw new UserManagerException("actionNotSupportedByRealm");
        }
        return usReader;
    }

    public Object getRealmConfiguration() throws UserManagerException {
        AuthorizingRealmConfig retConfig = null;
        if (config == null) {
            retConfig = new AuthorizingRealmConfig();
        } else {
            retConfig = new AuthorizingRealmConfig(config);
        }
        return retConfig;
    }

    public void init(Object configBean) throws UserManagerException {
        AuthorizingRealmConfig config = (AuthorizingRealmConfig) configBean;
        this.realm = config.getRealm();
        this.username = config.getAuthenticatedUserName();

        if (realm == null) {
            throw new UserManagerException("nullRealm");
        }

        Authorizer authorizer = realm.getAuthorizer();
        if (authorizer == null) {
            throw new UserManagerException("authorizerNullatAuthorizer");
        }

        this.authenticator = realm.getAuthenticator();
        this.authorizer = new ACLAuthorizer(authorizer, config);

        if (realm.getAccessControlAdmin() != null) {
            this.aclAdmin = new ACLAccessControlAdmin(authorizer, realm
                    .getAccessControlAdmin(), config);
        }

        if (realm.getUserStoreAdmin() != null) {
            this.usAdmin = new ACLUserStoreAdmin(authorizer, realm
                    .getUserStoreAdmin(), config);
        }

        if (realm.getUserStoreReader() != null) {
            this.usReader = new ACLUserStoreReader(authorizer, realm
                    .getUserStoreReader(), config);
            if (config.isEnableAdminBehavior()) {
                ACLAdminChecker
                        .loadAdminUsers(config.getAdminRoleName(), realm);
            }

        }

    }

}
