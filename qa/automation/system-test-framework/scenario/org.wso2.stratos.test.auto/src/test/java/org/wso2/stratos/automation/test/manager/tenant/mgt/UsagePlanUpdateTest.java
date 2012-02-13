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

package org.wso2.stratos.automation.test.manager.tenant.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceBillingDataAccessService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

public class UsagePlanUpdateTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(UsagePlanUpdateTest.class);
    private TenantDetails tenantAdminDetails;
    private String sessionCookie;
    private AdminServiceBillingDataAccessService billingDataAccessStub;

    @Override
    public void init() {
        testClassName = UsagePlanUpdateTest.class.getName();
        billingDataAccessStub = new AdminServiceBillingDataAccessService(FrameworkSettings.MANAGER_BACKEND_URL);
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(13);
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
    }

    @Override
    public void runSuccessCase() {
        //upgrade usage plan as SMB
        assertTrue("Usage plan updation fail", billingDataAccessStub.updateUsagePlan(sessionCookie,
                tenantAdminDetails.getTenantDomain(), ProductConstant.MULTITENANCY_SMALL_PLAN));
        assertEquals("Usage plan doesn't get updated", ProductConstant.MULTITENANCY_SMALL_PLAN,
                billingDataAccessStub.getUsagePlanName(sessionCookie, tenantAdminDetails.getTenantDomain()));
        log.info("Usage plan has been updated to SMB");


        //upgrade usage plan as Professional
        assertTrue("Usage plan updation fail",
                billingDataAccessStub.updateUsagePlan(sessionCookie, tenantAdminDetails.getTenantDomain(),
                        ProductConstant.MULTITENANCY_MEDIUM_PLAN));
        assertEquals("Usage plan doesn't get updated", ProductConstant.MULTITENANCY_MEDIUM_PLAN,
                billingDataAccessStub.getUsagePlanName(sessionCookie, tenantAdminDetails.getTenantDomain()));
        log.info("Usage plan has been updated to professional");


        //upgrade usage plan as Enterprise
        assertTrue("Usage plan updation fail",
                billingDataAccessStub.updateUsagePlan(sessionCookie, tenantAdminDetails.getTenantDomain(),
                        ProductConstant.MULTITENANCY_LARGE_PLAN));
        assertEquals("Usage plan doesn't get updated", ProductConstant.MULTITENANCY_LARGE_PLAN,
                billingDataAccessStub.getUsagePlanName(sessionCookie, tenantAdminDetails.getTenantDomain()));
        log.info("Usage plan has been updated to Enterprise");
    }

    @Override
    public void cleanup() {
        //Reset usage plan
        assertTrue("Usage plan updation fail",
                billingDataAccessStub.updateUsagePlan(sessionCookie, tenantAdminDetails.getTenantDomain(),
                        ProductConstant.MULTITENANCY_FREE_PLAN));
        assertEquals("Usage plan doesn't get updated", ProductConstant.MULTITENANCY_FREE_PLAN, billingDataAccessStub.getUsagePlanName(sessionCookie,
                tenantAdminDetails.getTenantDomain()));
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
