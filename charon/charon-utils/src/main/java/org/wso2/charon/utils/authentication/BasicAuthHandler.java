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
package org.wso2.charon.utils.authentication;

import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.extensions.AuthenticationHandler;
import org.wso2.charon.core.extensions.AuthenticationInfo;
import org.wso2.charon.core.extensions.CharonManager;
import org.wso2.charon.core.extensions.TenantDTO;
import org.wso2.charon.core.extensions.TenantManager;
import org.apache.axiom.om.util.Base64;

/**
 * AuthenticationHandler for validating API access through basic auth - authentication mechanism.
 */
public class BasicAuthHandler implements AuthenticationHandler {

    private static CharonManager charonManager;
    //default credentials
    private static final String USER_NAME = "charonAdmin";
    private static final String PASSWORD = "charonAdmin";


    @Override
    public boolean isAuthenticated(AuthenticationInfo authInfo) throws CharonException {
        //get authorization header from auth info
        String authorizationHeader = ((BasicAuthInfo) authInfo).getAuthorizationHeader();
        BasicAuthInfo decodedInfo = decodeBasicAuthHeader(authorizationHeader);
        if (USER_NAME.equals(decodedInfo.getUserName()) && (PASSWORD).equals(decodedInfo.getPassword())) {
            return true;
        } else {
            return false;
        }
        //decode it
        //check with tenant manager and return
        /*TenantManager tenantManager = charonManager.getTenantManager();
        //TODO: is it ok that the following methods called by any class?
        int tenantID = tenantManager.getTenantID(((BasicAuthInfo) authInfo).getUserName());
        TenantDTO tenantInfo = tenantManager.getTenantInfo(tenantID);
        if (((BasicAuthInfo) authInfo).getPassword().equals(tenantInfo.getTenantAdminPassword())) {
            return true;
        } else {
            return false;
        }*/
    }

    @Override
    public AuthenticationInfo getAuthenticationToken(AuthenticationInfo authInfo) {
        String userName = ((BasicAuthInfo) authInfo).getUserName();
        String password = ((BasicAuthInfo) authInfo).getPassword();
        ((BasicAuthInfo) authInfo).setAuthorizationHeader(getBase64EncodedBasicAuthHeader(userName, password));
        return authInfo;
    }

    /**
     * Get the Base64 encoded basic auth header.
     *
     * @param userName
     * @param password
     * @return
     */
    public String getBase64EncodedBasicAuthHeader(String userName, String password) {
        String concatenatedCredential = userName + ":" + password;
        byte[] byteValue = concatenatedCredential.getBytes();
        String encodedAuthHeader = Base64.encode(byteValue);
        encodedAuthHeader = "Basic " + encodedAuthHeader;
        return encodedAuthHeader;
    }

    public BasicAuthInfo decodeBasicAuthHeader(String authorizationHeader) {
        byte[] decodedAuthHeader = Base64.decode(authorizationHeader.split(" ")[1]);
        String authHeader = new String(decodedAuthHeader);
        String userName = authHeader.split(":")[0];
        String password = authHeader.split(":")[1];
        BasicAuthInfo basicAuthInfo = new BasicAuthInfo();
        basicAuthInfo.setUserName(userName);
        basicAuthInfo.setPassword(password);
        return basicAuthInfo;

    }

    /**
     * Pass a handler of Charon Manager - who knows about other extensions such as TenantManager,UserManagers etc,
     * so that authentication handler can utilize them if needed.
     *
     * @param charonManager
     */
    @Override
    public void setCharonManager(CharonManager charonManager) {
        if (this.charonManager == null) {
            this.charonManager = charonManager;
        }
    }

}
