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

package org.wso2.usermanager.custom.acegi;

/**
 * @see org.wso2.usermanager.Realm 
 */
import org.acegisecurity.providers.AuthenticationProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;

public class AcegiRealm implements Realm {

    private AcegiRealmConfig config = null;

    private AuthenticationProvider authProvider = null;

    public Object getRealmConfiguration() throws UserManagerException {
        AcegiRealmConfig retConfig = null;
        if (config == null) {
            retConfig = new AcegiRealmConfig();
        } else {
            retConfig = new AcegiRealmConfig(config);
        }
        return retConfig;
    }

    public void init(Object configBean) throws UserManagerException {
        if (!(configBean instanceof AcegiRealmConfig)) {
            return;
        }
        this.config = (AcegiRealmConfig) configBean;
        String fileName = this.config
                .getAuthenticationProviderBeanMappingFile();
        if (fileName == null) {
            throw new UserManagerException("beanMappingNotfound");
        }

        ApplicationContext context = new FileSystemXmlApplicationContext(
                fileName);
        String authProviderId = config.getAuthProviderId();
        if (authProviderId == null) {
            throw new UserManagerException("nullAuthProvider");
        }
        authProvider = (AuthenticationProvider) context.getBean(authProviderId);

    }

    public Authenticator getAuthenticator() throws UserManagerException {
        AcegiAuthenticator authenticator = new AcegiAuthenticator(authProvider);
        return authenticator;
    }

    public UserStoreReader getUserStoreReader() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public UserStoreAdmin getUserStoreAdmin() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public AccessControlAdmin getAccessControlAdmin()
            throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public Authorizer getAuthorizer() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

}
