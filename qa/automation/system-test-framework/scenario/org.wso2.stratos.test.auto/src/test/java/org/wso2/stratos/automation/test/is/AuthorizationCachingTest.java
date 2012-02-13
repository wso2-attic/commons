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
package org.wso2.stratos.automation.test.is;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceUserMgtService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.user.mgt.common.UserAdminException;

public class AuthorizationCachingTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(AuthorizationCachingTest.class);
    private AdminServiceUserMgtService userAdminStub;
    private TenantDetails tenantAdminDetails;
    private String sessionCookie;
    private String roleName;
    private String userName;
    private String userPassword;

    @Override
    public void init() {
        testClassName = AuthorizationCachingTest.class.getName();
        userAdminStub = new AdminServiceUserMgtService(FrameworkSettings.IS_BACKEND_URL);
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(10);
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.IS_BACKEND_URL);
        userName = "wso2automationUser103";
        roleName = "admin";
        userPassword = "wso2automationPassword";
    }

    @Override
    public void runSuccessCase() {
        deleteUsers(); //delete the user if exists
        log.debug("Delete user if exists");
        try {
            //Create user and add him to admin role
            userAdminStub.addUser(sessionCookie, userName, userPassword, new String[]{roleName}, null);
            userAdminStub.updateUserListOfRole(roleName, new String[]{userName}, null); //Assign admin role
            login(userName + "@" + tenantAdminDetails.getTenantDomain(), userPassword, FrameworkSettings.IS_BACKEND_URL);
            log.debug("User " + userName + " logged in successfully");

            //remove user from admin role
            userAdminStub.updateUserListOfRole(roleName, null, new String[]{userName});
            loginAfterRoleRemoval();

            //Assign admin role to the user again
            userAdminStub.updateUserListOfRole(roleName, new String[]{userName}, null);
            //login after role update
            login(userName + "@" + tenantAdminDetails.getTenantDomain(), userPassword, FrameworkSettings.IS_BACKEND_URL);
            log.debug("User " + userName + " logged in successfully after role update");

        } catch (UserAdminException e) {
            log.error("Add user fail" + e.getMessage());
            Assert.fail("Add user fail" + e.getMessage());
        }
    }

    private void loginAfterRoleRemoval() {
        try {
            //login after role deletion
            log.debug("Try login after role deletion");
            login(userName + "@" + tenantAdminDetails.getTenantDomain(), userPassword, FrameworkSettings.IS_BACKEND_URL);
        } catch (AssertionFailedError e) {
            log.info("User cannot login");
            System.out.println("User cannot login");
        }
    }

    @Override
    public void cleanup() {
        deleteUsers();
    }

    private void deleteUsers() {
        userAdminStub.deleteUser(sessionCookie, userName);
        try {
            //login after user deletion
            log.debug("Try login after user deletion");
            login(userName + "@" + tenantAdminDetails.getTenantDomain(), userPassword, FrameworkSettings.IS_BACKEND_URL);
        } catch (AssertionFailedError e) {
            log.info("Deleted user cannot login");
            System.out.println("Deleted users cannot login");
        }
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
