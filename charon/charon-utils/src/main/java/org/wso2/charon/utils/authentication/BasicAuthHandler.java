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

import org.wso2.charon.core.extensions.AuthenticationHandler;
import org.wso2.charon.core.extensions.AuthenticationInfo;

/**
 * AuthenticationHandler for validating API access through basic auth - authentication mechanism.
 */
public class BasicAuthHandler implements AuthenticationHandler {

    //for demo purpose, use hard coded credentials
    private String userName = "hasini@wso2.com";
    private String password = "hasini";

    @Override
    public boolean isAuthenticated(AuthenticationInfo authInfo) {
        
        return ((userName.equals(((BasicAuthInfo) authInfo).getUserName()) &&
             (password.equals(((BasicAuthInfo) authInfo).getPassword()))));
    }
}
