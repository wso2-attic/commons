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
import org.wso2.charon.core.schema.SCIMSchemaConstants;
import org.wso2.charon.deployment.storage.InMemoryTenantManager;
import org.wso2.charon.deployment.storage.InMemroyUserManager;
import org.wso2.charon.deployment.storage.SampleUser;
import org.wso2.charon.utils.authentication.BasicAuthHandler;
import org.wso2.charon.utils.authentication.BasicAuthInfo;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * This illustrate what are the core tasks an implementation should take care of,
 * according to their specific implementation, and how the extension points and utils
 * implementation provided by charon can be utilized here.
 */
public class SampleCharonManager {

    private static AuthenticationHandler authenticationHandler;
    private static AuthenticationInfo authenticationInfo;

    /**
     * **************For the demo purpose only*********************************
     */
    /*In memory users list of tenant 1*/
    private static List<SampleUser> tenant1UserList;

    /*In memory users list of tenant 2*/
    private static List<SampleUser> tenant2UserList;

    private static List<SampleUser> createTenant1UserList() {
        tenant1UserList = new ArrayList<SampleUser>();
        //create some dummy users and add to the list.
        SampleUser user1 = new SampleUser();
        user1.setId("wso2001");
        user1.setUserName("peter");
        user1.setEmails(new String[]{"peter@wso2.com", "pet@gmail.com"});
        tenant1UserList.add(user1);

        SampleUser user2 = new SampleUser();
        user2.setId("wso2002");
        user2.setUserName("jane");
        user2.setEmails(new String[]{"jane@wso2.com", "jane@gmail.com"});
        tenant1UserList.add(user2);

        return tenant1UserList;
    }

    private static List<SampleUser> createTenant2UserList() {
        tenant2UserList = new ArrayList<SampleUser>();
        //create some dummy users and add to the list.
        SampleUser user1 = new SampleUser();
        user1.setId("wp001");
        user1.setUserName("peter");
        user1.setEmails(new String[]{"peter@wp.org", "pet@gmail.com"});
        tenant2UserList.add(user1);

        SampleUser user2 = new SampleUser();
        user2.setId("wp002");
        user2.setUserName("jane");
        user2.setEmails(new String[]{"ann@wp.org", "ann@gmail.com"});
        tenant2UserList.add(user2);

        return tenant2UserList;
    }


    /**
     * ***********************************************************************
     */

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

    public static String identifyResponseFormat(String format) {
        if (format.equals("application/json")) {
            format = SCIMSchemaConstants.JSON;
        } else if (format.equals("application/xml")) {
            format = SCIMSchemaConstants.XML;
        }
        return format;
    }

    /**
     * Obtain the tenant specific user manager. This should be called only after authenticating the
     * API invoker.
     *
     * @param userName
     * @return
     */
    public static UserManager getUserManager(String userName) throws InternalServerException {
        UserManager userManager;

        String tenantDomain = userName.split("@")[1];
        int tenantId = InMemoryTenantManager.getTenantId(tenantDomain);

        /*********For the demo purpose only - because we keep a in memory user list.***************/
        if (tenantId == 1) {
            userManager = new InMemroyUserManager(createTenant1UserList(), tenantId,
                                                  tenantDomain);
        } else if (tenantId == 2) {
            userManager = new InMemroyUserManager(createTenant2UserList(), tenantId, tenantDomain);

        } else {
            throw new InternalServerException(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR,
                                              "Error in obtaining tenant specific user manager.");
        }

        /*****************************************************************************************/
        return userManager;

    }

}

