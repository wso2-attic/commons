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

package org.wso2.stratos.automation.test.manager.tenant.throttling;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceBillingDataAccessService;
import org.wso2.carbon.admin.service.AdminServiceUserMgtService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.user.mgt.common.UserAdminException;

public class UserCountTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(UserCountTest.class);
    private AdminServiceUserMgtService userAdminStub;
    private AdminServiceBillingDataAccessService billingDataAccessStub;
    private TenantDetails tenantAdminDetails;
    private String sessionCookie;
    private String sessionCookie_billingStub;
    private String roleName;
    private String userName;
    int tenantUserCount = 20;
    private String userPassword;

    @Override
    public void init() {
        testClassName = UserCountTest.class.getName();
        userAdminStub = new AdminServiceUserMgtService(FrameworkSettings.IS_BACKEND_URL);
        billingDataAccessStub = new AdminServiceBillingDataAccessService(FrameworkSettings.MANAGER_BACKEND_URL);
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(13);
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.IS_BACKEND_URL);
        sessionCookie_billingStub = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
        userName = "wso2automationUser";
        roleName = "wso2automationRole3";
        userPassword = "wso2automationPassword";
    }

    @Override
    public void runSuccessCase() {
        deleteRoleAndUsers(); //delete user and roles, if exists
        usagePlanUpdate(ProductConstant.MULTITENANCY_FREE_PLAN); //update usage plan to demo and add 20 users
        addRoleWithUser();
        deleteRoleAndUsers();
        usagePlanUpdate(ProductConstant.MULTITENANCY_SMALL_PLAN); //update usage plan to SMB and add 20 users
        addRoleWithUser();
        deleteRoleAndUsers();
        usagePlanUpdate(ProductConstant.MULTITENANCY_MEDIUM_PLAN); //update usage plan to Professional and add 20 users
        addRoleWithUser();
        deleteRoleAndUsers();
        usagePlanUpdate(ProductConstant.MULTITENANCY_LARGE_PLAN); //update usage plan to Enterprise and add 20 users
        addRoleWithUser();
        deleteRoleAndUsers();
    }

    private void usagePlanUpdate(String usagePlan) {
        assertTrue("Usage plan updation fail", billingDataAccessStub.updateUsagePlan(sessionCookie_billingStub,
                tenantAdminDetails.getTenantDomain(), usagePlan));
        assertEquals("Usage plan doesn't get updated", usagePlan,
                billingDataAccessStub.getUsagePlanName(sessionCookie_billingStub, tenantAdminDetails.getTenantDomain()));
        log.info("Usage plan has been updated to " + usagePlan);
    }

    private void addRoleWithUser() {
        String permission[] = {"/permission/admin/login"};
        String userList[] = {"admin123"};
        int tenantUserCount = 20;


        try {
            userAdminStub.addRole(roleName, userList, permission, sessionCookie); //add role with login permission before adding user list
            log.info("Role added successfully");
            String roles[] = {roleName};

            for (int userCount = 0; userCount < tenantUserCount; userCount++) {
                userAdminStub.addUser(sessionCookie, userName + userCount, userPassword + userCount, roles, null);
                login(userName + userCount + "@" + tenantAdminDetails.getTenantDomain(), userPassword + userCount, FrameworkSettings.IS_BACKEND_URL);
                log.debug("User " + userName + userCount + " logged in successfully");
            }

        } catch (UserAdminException e) {
            log.error("Add user fail" + e.getMessage());
            Assert.fail("Add user fail" + e.getMessage());
        }
    }

    @Override
    public void cleanup() {
        //Reset usage plan to demo
        usagePlanUpdate(ProductConstant.MULTITENANCY_FREE_PLAN);
    }

    private void deleteRoleAndUsers() {
        userAdminStub.deleteRole(sessionCookie, roleName);
        log.info("Role " + roleName + " deleted successfully");
        for (int userCount = 0; userCount < tenantUserCount; userCount++) {
            userAdminStub.deleteUser(sessionCookie, userName + userCount);
            log.debug("User " + userName + userCount + " deleted successfully");
            try {
                //login after user deletion
                log.debug("Try login after user deletion");
                login(userName + userCount + "@" + tenantAdminDetails.getTenantDomain(), userPassword + userCount, FrameworkSettings.IS_BACKEND_URL);
            } catch (AssertionFailedError e) {
                log.info("Deleted user cannot login");
                System.out.println("Deleted users cannot login");
            }
        }
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }


}
