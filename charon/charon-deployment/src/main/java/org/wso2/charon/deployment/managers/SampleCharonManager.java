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

import org.wso2.charon.core.exceptions.InternalServerException;
import org.wso2.charon.core.extensions.AuthenticationHandler;
import org.wso2.charon.core.extensions.AuthenticationInfo;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.deployment.storage.InMemoryTenantManager;
import org.wso2.charon.deployment.storage.InMemroyUserManager;
import org.wso2.charon.utils.authentication.BasicAuthHandler;
import org.wso2.charon.utils.authentication.BasicAuthInfo;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This illustrate what are the core tasks an implementation should take care of,
 * according to their specific implementation, and how the extension points and utils
 * implementation provided by charon can be utilized here.
 */
public class SampleCharonManager {

    private static AuthenticationHandler authenticationHandler;
    private static AuthenticationInfo authenticationInfo;

    private static Map<Integer, UserManager> userManagers = new ConcurrentHashMap<Integer, UserManager>();

    private static void initAuthenticationHandler() {
        //read from the config and initialize relevant authentication handler.
        authenticationHandler = new BasicAuthHandler();
        authenticationInfo = new BasicAuthInfo();
    }

    /**
     * Handle authentication using implementation specific authentication mechanism.
     *
     * @param userName
     * @param password
     * @param authorization
     */
    public static void handleAuthentication(String userName, String password,
                                            String authorization) {
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

    /**
     * Obtain the tenant specific user manager. This should be called only after authenticating the
     * API invoker.
     * TODO:handle concurrency/multi-threading issues.
     *
     * @param userName
     * @return
     */
    public static UserManager getUserManager(String userName) throws InternalServerException {
        UserManager userManager;

        String tenantDomain = userName.split("@")[1];
        int tenantId = InMemoryTenantManager.getTenantId(tenantDomain);

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
}

