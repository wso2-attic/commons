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
import org.wso2.charon.utils.authentication.BasicAuthInfo;

/**
 * AuthenticationHandler for validating API access through basic auth - authentication mechanism.
 */
public class BasicAuthHandler implements AuthenticationHandler {

    private static CharonManager charonManager;

    @Override
    public boolean isAuthenticated(AuthenticationInfo authInfo) throws CharonException {

        TenantManager tenantManager = charonManager.getTenantManager();
        //TODO: is it ok that the following methods called by any class?
        int tenantID = tenantManager.getTenantID(((BasicAuthInfo) authInfo).getUserName());
        TenantDTO tenantInfo = tenantManager.getTenantInfo(tenantID);
        if (((BasicAuthInfo) authInfo).getPassword().equals(tenantInfo.getTenantAdminPassword())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public AuthenticationInfo getAuthenticationToken(AuthenticationInfo authInfo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
