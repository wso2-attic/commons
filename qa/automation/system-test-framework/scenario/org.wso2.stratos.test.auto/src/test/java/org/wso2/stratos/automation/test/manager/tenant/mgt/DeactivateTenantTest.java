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

import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceStratosAccountMgt;
import org.wso2.carbon.admin.service.AdminServiceTenantMgtServiceAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;


public class DeactivateTenantTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(DeactivateTenantTest.class);
    private TenantDetails tenantAdminDetails;

    @Override
    public void init() {
        testClassName = DeactivateTenantTest.class.getName();
    }

    @Override
    public void runSuccessCase() {
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(13);
        String sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
        AdminServiceStratosAccountMgt adminServiceStratosAccountMgt =
                new AdminServiceStratosAccountMgt(FrameworkSettings.MANAGER_BACKEND_URL);
        adminServiceStratosAccountMgt.deactivateTenant(sessionCookie);

        try {
            //login after tenant deativation
            log.debug("Try login after tenant deactivation");
            login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                    FrameworkSettings.MANAGER_BACKEND_URL);
        } catch (AssertionFailedError e) {
            log.info("Deactivated tenant cannot login");
        }
    }

    @Override
    public void cleanup() {
        //Activate the tenant again
        TenantDetails superTenantDetails = TenantListCsvReader.getTenantDetails(0); //get super tenant credential
        String sessionCookie = login(superTenantDetails.getTenantName(), superTenantDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
        AdminServiceTenantMgtServiceAdmin tenantStub =
                new AdminServiceTenantMgtServiceAdmin(FrameworkSettings.MANAGER_BACKEND_URL);
        //activate tenant again and login
        tenantStub.activateTenant(sessionCookie, tenantAdminDetails.getTenantDomain());
        login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
        log.info("Tenant activated after test execution");
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
