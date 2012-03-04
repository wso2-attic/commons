/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.charon.deployment.managers;

import org.wso2.charon.core.encoder.Decoder;
import org.wso2.charon.core.encoder.Encoder;
import org.wso2.charon.core.encoder.json.JSONDecoder;
import org.wso2.charon.core.encoder.json.JSONEncoder;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.FormatNotSupportedException;
import org.wso2.charon.core.extensions.AuthenticationHandler;
import org.wso2.charon.core.extensions.AuthenticationInfo;
import org.wso2.charon.core.extensions.CharonManager;
import org.wso2.charon.core.extensions.TenantDTO;
import org.wso2.charon.core.extensions.TenantManager;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.deployment.storage.InMemoryTenantManager;
import org.wso2.charon.deployment.storage.InMemroyUserManager;
import org.wso2.charon.utils.authentication.BasicAuthHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This illustrates what are the core tasks an implementation should take care of,
 * according to their specific implementation, and how the extension points and utils
 * implementation provided by charon can be initialized/utilized here.
 */
public class DefaultCharonManager implements CharonManager {

    /*private static AuthenticationHandler authenticationHandler;
    private static AuthenticationInfo authenticationInfo;*/
    private static TenantManager tenantManager;
    private static DefaultCharonManager defaultCharonManager;
    private static Map<String, Encoder> encoderMap = new HashMap<String, Encoder>();
    private static Map<String, Decoder> decoderMap = new HashMap<String, Decoder>();
    private static Map<String, Map> authenticators = new HashMap<String, Map>();

    private static Map<Integer, UserManager> userManagers = new ConcurrentHashMap<Integer, UserManager>();
    private static final String INSTANCE = "instance";

    /**
     * Perform initialization.
     */
    private void init() {
        //TODO:read config and init stuff 
        tenantManager = new InMemoryTenantManager();
        encoderMap.put(SCIMConstants.JSON, new JSONEncoder());
        decoderMap.put(SCIMConstants.JSON, new JSONDecoder());
        //create basic auth - authenticator property
        Map<String, Object> basicAuthAuthenticator = new HashMap<String, Object>();
        basicAuthAuthenticator.put("", new BasicAuthHandler());
        basicAuthAuthenticator.put(SCIMConstants.AUTH_PROPERTY_PRIMARY, true);
        //add basic auth authenticator properties to authenticators list.
        authenticators.put(SCIMConstants.AUTH_TYPE_BASIC, basicAuthAuthenticator);
    }

    private DefaultCharonManager() {
        init();
    }

    /**
     * Should return the static instance of CharonManager implementation.
     * Read the config and initialize extensions as specified in the config.
     *
     * @return
     */
    public static DefaultCharonManager getInstance() {
        if (defaultCharonManager == null) {
            synchronized (DefaultCharonManager.class) {
                if (defaultCharonManager == null) {
                    defaultCharonManager = new DefaultCharonManager();
                    return defaultCharonManager;
                } else {
                    return defaultCharonManager;
                }
            }
        } else {
            return defaultCharonManager;
        }
    }


    /**
     * Handle authentication using implementation specific authentication mechanism.
     *
     * @param userName
     * @param password
     * @param authorization
     */
    /*public static void handleAuthentication(String userName, String password,
                                            String authorization) {
        //get the authenticator, according to the authenticator,
        if (authenticationHandler == null && authenticationInfo == null) {
            initAuthenticationHandler();
        }
        if (userName != null && password != null) {
            ((BasicAuthInfo) authenticationInfo).setUserName(userName);
            ((BasicAuthInfo) authenticationInfo).setPassword(password);
            if (!authenticationHandler.isAuthenticated(authenticationInfo)) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
*/

    /**
     * Obtain the encoder for the given format.
     *
     * @return
     */
    @Override
    public Encoder getEncoder(String format) throws FormatNotSupportedException {
        //if the requested format not supported, return an error.
        if (!encoderMap.containsKey(format)) {
            //Error is logged by the caller.
            throw new FormatNotSupportedException(ResponseCodeConstants.CODE_FORMAT_NOT_SUPPORTED,
                                                  ResponseCodeConstants.DESC_FORMAT_NOT_SUPPORTED);
        }
        return encoderMap.get(format);
    }

    /**
     * Obtain the decoder for the given format.
     *
     * @return
     */
    @Override
    public Decoder getDecoder(String format) throws FormatNotSupportedException {
        //if the requested format not supported, return an error.
        if (!decoderMap.containsKey(format)) {
            //Error is logged by the caller.
            throw new FormatNotSupportedException(ResponseCodeConstants.CODE_FORMAT_NOT_SUPPORTED,
                                                  ResponseCodeConstants.DESC_FORMAT_NOT_SUPPORTED);
        }
        return decoderMap.get(format);
    }

    /**
     * Obtain the authentication handler, given the authentication mechanism.
     *
     * @return
     */
    @Override
    public AuthenticationHandler getAuthenticationHandler(String authMechanism)
            throws CharonException {
        if (authenticators.size() != 0) {
            Map authenticatorProperties = authenticators.get(authMechanism);
            if (authenticatorProperties != null && authenticatorProperties.size() != 0) {
                return (AuthenticationHandler) authenticatorProperties.get(INSTANCE);
            }
        }
        String error = "Requested authentication mechanism is not supported.";
        throw new CharonException(error);
    }

    /**
     * Obtain the user manager, after identifying the tenantId of the tenantAdminUser
     * who invokes the SCIM API exposed by the service provider.
     *
     * @param tenantAdminUserName
     * @return
     */
    @Override
    public UserManager getUserManager(String tenantAdminUserName) throws CharonException {
        //identify tenant id and domain
        int tenantId = tenantManager.getTenantID(tenantAdminUserName);
        String tenantDomain = tenantManager.getTenantDomain(tenantAdminUserName);
        UserManager userManager;
        if ((userManagers != null) && (userManagers.size() != 0)) {
            if (userManagers.get(tenantId) != null) {
                return userManagers.get(tenantId);
            } else {
                userManager = new InMemroyUserManager(tenantId, tenantDomain);
                userManagers.put(tenantId, userManager);
                return userManager;
            }
        } else {
            userManager = new InMemroyUserManager(tenantId, tenantDomain);
            userManagers.put(tenantId, userManager);
            return userManager;
        }
    }

    /**
     * Obtain the the instance of registered tenant manager implementation.
     *
     * @return
     */
    @Override
    public TenantManager getTenantManager() {
        return tenantManager;
    }

    /**
     * Create the tenant in the particular tenant manager, given the tenant info.
     * Purpose of registering is, creating a tenant specific storage space in the service provider
     * as well as obtaining credentials to access SCIM SP API for subsequent provisioning activities.
     * Therefore. proper credentials should be returned according to the authentication handler.
     *
     * @param tenantInfo
     * @return
     * @throws org.wso2.charon.core.exceptions.CharonException
     *
     */
    @Override
    public AuthenticationInfo registerTenant(TenantDTO tenantInfo) throws CharonException {

        //check whether the requested authentication mechanism supported.
        if (this.isAuthenticationSupported(tenantInfo.getAuthenticationMechanism())) {

            this.getTenantManager().createTenant(tenantInfo);
            //if auth mechanism is http basic auth, we skip the step of obtaining and returning auth token.
            if (SCIMConstants.AUTH_TYPE_BASIC.equals(tenantInfo.getAuthenticationMechanism())) {
                return null;
            } else {
                //if auth type is oauth, obtain the oauth bearer token and return.
                return this.getAuthenticationHandler(
                        tenantInfo.getAuthenticationMechanism()).getAuthenticationToken(
                        this.getAuthInfo(tenantInfo));
            }

        } else {
            String errorMessage = "Requested authentication mechanism not supported.";
            throw new CharonException(errorMessage);
        }
    }

    /**
     * Returns true if the registered authenticators support the given authentication mechanism.
     *
     * @param authmechanism
     * @return
     */
    @Override
    public boolean isAuthenticationSupported(String authmechanism) {
        return authenticators.containsKey(authmechanism);
    }

    @Override
    public void handleAuthentication(Map<String, String> httpAuthHeaders) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String identifyAuthMechanism(Map<String, String> authHeaders) throws CharonException {
        if (authHeaders.containsKey(SCIMConstants.AUTH_HEADER_USERNAME) &&
            authHeaders.containsKey(SCIMConstants.AUTH_HEADER_PASSWORD)) {
            return SCIMConstants.AUTH_TYPE_BASIC;
        } else if (authHeaders.containsKey(SCIMConstants.AUTH_TYPE_OAUTH)) {
            return SCIMConstants.AUTH_TYPE_OAUTH;
        } else {
            String error = "Provided authentication headers do not contain supported authentication headers.";
            throw new CharonException(error);
        }
    }

    /**
     * Create the AuthInfo according to the requested authentication mechanism.
     * Now, only used in the case of OAUTH.
     *
     * @param tenantDTO
     * @return
     */
    private AuthenticationInfo getAuthInfo(TenantDTO tenantDTO) {
        AuthenticationInfo authInfo = null;

        if (SCIMConstants.AUTH_TYPE_OAUTH.equals(tenantDTO.getAuthenticationMechanism())) {
            //TODO:create OAUTHInfo out of tenant info submitted, and return.
        }
        return authInfo;

    }
}

