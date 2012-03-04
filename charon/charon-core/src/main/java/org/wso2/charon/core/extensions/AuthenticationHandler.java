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
package org.wso2.charon.core.extensions;

/**
 * Interface for handling authentication into SCIM Service Provider API.
 * Implementers can plugin their own implementation for handling authentication.
 */
public interface AuthenticationHandler {

    /**
     * Validate authentication information according to the auth handling implementation.
     *
     * @param authInfo
     * @return
     */
    public boolean isAuthenticated(AuthenticationInfo authInfo);

    public AuthenticationInfo getAuthenticationToken(AuthenticationInfo authInfo);



}
