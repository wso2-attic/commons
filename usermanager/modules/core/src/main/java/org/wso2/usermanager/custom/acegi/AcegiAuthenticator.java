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

import org.acegisecurity.Authentication;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.UserManagerException;

/**
 * @see org.wso2.usermanager.Authenticator
 */
public class AcegiAuthenticator implements Authenticator {

    private AuthenticationProvider authProvider = null;

    public AcegiAuthenticator(AuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }
    
    /**
     * {@link org.wso2.usermanager.Authenticator#authenticate(String, Object)}
     */
    public boolean authenticate(String userName, Object credentials)
            throws UserManagerException {
        if (!(credentials instanceof String)) {
            throw new UserManagerException("unsupportedCredential");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userName, (String) credentials);
        Authentication result = authProvider.authenticate(token);
        return result.isAuthenticated();
    }
}
