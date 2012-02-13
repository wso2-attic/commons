/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.user.mgt.admin.authenticate;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException;
import org.wso2.carbon.user.mgt.ui.UserAdminStub;

/**
 * Authenticating UserManagementAdmin Stub
 */
public class UserAdminServiceInitiator {
    private static final Log log = LogFactory.getLog(UserAdminServiceInitiator.class);

    /**
     * Method of authenticating UserAdminStub
     * @param sessionCookie valid session cookie
     * @param serviceURL test server url
     * @return authenticated UserAdminStub
     * @throws org.apache.axis2.AxisFault exception will throw if user add invalid session cookie
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException UserManager test module exception
     */

    public UserAdminStub authenticateUserAdminStub(String sessionCookie, String serviceURL)
            throws AxisFault, UserAdminTestException {
        UserAdminStub userAdminStub = null;
        try {
            userAdminStub = new UserAdminStub(serviceURL + "UserAdmin");
            ServiceClient client = userAdminStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
            log.debug(serviceURL + "/services/UserAdmin authenticated with valid session cookie : " + sessionCookie);
        } catch (AxisFault axisFault) {
            log.error("Exception thrown while authenticating UserAdmin service :" + axisFault);
            throw new UserAdminTestException("Exception thrown while authenticating UserAdmin service : " + axisFault);
        }
        return userAdminStub;
    }
}
