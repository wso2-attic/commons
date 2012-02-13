/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.system.test.core;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceTenantMgtServiceAdmin;
import org.wso2.carbon.admin.service.AdminServiceUserMgtService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.user.mgt.common.UserAdminException;

public class UserPopulator {
    private static final Log log = LogFactory.getLog(UserPopulator.class);
    private AdminServiceUserMgtService userMgtAdmin;
    private static boolean isUsersPopulated = true;

    public void populateUsers() {

        FrameworkSettings.getFrameworkProperties();
        if (isUsersPopulated) {
            if (FrameworkSettings.getStratosTestStatus()) {
                AdminServiceTenantMgtServiceAdmin tenantStub = new AdminServiceTenantMgtServiceAdmin(FrameworkSettings.MANAGER_BACKEND_URL);
                TenantDetails superTenantDetails = TenantListCsvReader.getTenantDetails(0);
                int userCount = TenantListCsvReader.getTenantCount();
                createStratosUsers(tenantStub, superTenantDetails, userCount);

            } else {
                int adminUserId = 0;
                TenantDetails adminDetails = TenantListCsvReader.getTenantDetails(adminUserId);
                String[] permissions = {"/permission/"};
                String[] userList = null;

                String[] productList = FrameworkSettings.PRODUCT_LIST_TO_START.split(",");

                for (String product : productList) {
                    String backendURL = ProductConstant.getBackendURL(product);
                    userMgtAdmin = new AdminServiceUserMgtService(backendURL);
                    log.info("Populate user to " + product + " server");
                    createProductUsers(adminDetails, permissions, userList, backendURL);
                }
            }
            //disable user population after first run
            isUsersPopulated = false;
        }
    }

    private void createProductUsers(TenantDetails adminDetails, String[] permissions, String[] userList, String backendURL) {
        String sessionCookieUser = login(adminDetails.getTenantName(), adminDetails.getTenantPassword(), backendURL);
        String[] roleName = {"testRole"};
        int roleNameIndex = 0;

        try {
            if (!userMgtAdmin.roleNameExists(roleName[roleNameIndex], sessionCookieUser)) {
                userMgtAdmin.addRole(roleName[roleNameIndex], userList, permissions, sessionCookieUser);
                log.info("Role " + roleName[roleNameIndex] + " was created successfully");
            }
        } catch (UserAdminException e) {
            log.error("Unable to add role :" + e.getMessage());
            Assert.fail("Unable to add role :" + e.getMessage());
        }

        for (int userId = 0; userId < TenantListCsvReader.getTenantCount(); userId++) {
            if (userId != 0) {
                String userId_str = Integer.toString(userId);
                int userIdValue = TenantListCsvReader.getTenantId(userId_str);
                TenantDetails userDetails = TenantListCsvReader.getTenantDetails(userIdValue);
                try {
                    if (!userMgtAdmin.userNameExists(roleName[roleNameIndex], sessionCookieUser, userDetails.getTenantName())) {
                        userMgtAdmin.addUser(sessionCookieUser, userDetails.getTenantName(), userDetails.getTenantName(), roleName, null);
                        log.info("User " + userDetails.getTenantName() + " was created successfully");
                    }
                } catch (UserAdminException e) {
                    log.error("Unable to add users :" + e.getMessage());
                    Assert.fail("Unable to add users :" + e.getMessage());
                }
            }
        }
    }

    private void createStratosUsers(AdminServiceTenantMgtServiceAdmin tenantStub, TenantDetails superTenantDetails, int userCount) {
        String sessionCookie = login(superTenantDetails.getTenantName(), superTenantDetails.getTenantPassword(), FrameworkSettings.MANAGER_BACKEND_URL);

        for (int userId = 0; userId < userCount; userId++) {
            if (userId != 0) {
                String userId_str = Integer.toString(userId);
                int tenantId = TenantListCsvReader.getTenantId(userId_str);
                TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(tenantId);
                tenantStub.addTenant(sessionCookie, tenantDetails.getTenantDomain(), tenantDetails.getTenantPassword(), "admin123", "free");
            }
        }
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }


}
