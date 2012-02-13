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
package org.wso2.authenticator.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.wso2.authenticator.Authenticator;
import org.wso2.authenticator.AuthenticatorException;
import org.wso2.authenticator.help.UserHelpInfo;

/**
 * @see org.wso2.usermanager.Authenticator
 */
public class AcegiAuthenticator implements Authenticator {

    private AuthenticationProvider authProvider = null;
    
    public String authenticationProviderBeanMappingFile = null;

    public String authProviderId = null;
    
    public AcegiAuthenticator() {
    }
    
    /**
     * {@link org.wso2.usermanager.Authenticator#authenticate(String, Object)}
     */
    public boolean authenticate(String userName, Object credentials)
            throws AuthenticatorException {
        if (!(credentials instanceof String)) {
            throw new AuthenticatorException("unsupportedCredential");
        }
        open();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userName, (String) credentials);
        Authentication result = authProvider.authenticate(token);
        return result.isAuthenticated();
    }

    @UserHelpInfo(isRequired = true, getHelpText = "BeanMapping file",
        getInputType = "text", getLabel= "Bean Mapping File")
    public void setAuthenticationProviderBeanMappingFile(
            String authenticationProviderBeanMappingFile) {
        this.authenticationProviderBeanMappingFile = authenticationProviderBeanMappingFile;
    }

    @UserHelpInfo(isRequired = true, getHelpText = "Authentication provider id",
            getInputType = "text", getLabel= "AuthProvider Id")
    public void setAuthProviderId(String authProviderId) {
        this.authProviderId = authProviderId;
    }
    
    protected void open() throws AuthenticatorException{
        
        if(authProvider != null){
            return;
        }
        
        ApplicationContext context = new FileSystemXmlApplicationContext(
                authenticationProviderBeanMappingFile);
     
        if (authProviderId == null) {
            throw new AuthenticatorException("nullAuthProvider");
        }
        authProvider = (AuthenticationProvider) context.getBean(authProviderId);
     
    }
    
    
}
