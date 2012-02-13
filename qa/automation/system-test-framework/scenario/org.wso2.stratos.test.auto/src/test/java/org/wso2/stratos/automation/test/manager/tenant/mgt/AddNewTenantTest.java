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

package org.wso2.stratos.automation.test.manager.tenant.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceTenantMgtServiceAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.util.Random;

import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;

public class AddNewTenantTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(AddNewTenantTest.class);

    @Override
    public void init() {
        testClassName = AddNewTenantTest.class.getName();
    }

    @Override
    public void runSuccessCase() {
        Random rand = new Random();
        AdminServiceTenantMgtServiceAdmin tenantStub = new AdminServiceTenantMgtServiceAdmin(FrameworkSettings.MANAGER_BACKEND_URL);
        TenantDetails superTenantDetails = TenantListCsvReader.getTenantDetails(0);
        String sessionCookie = login(superTenantDetails.getTenantName(), superTenantDetails.getTenantPassword(), FrameworkSettings.MANAGER_BACKEND_URL);
        String domainName = "wso2TestAutomationDomain" + System.currentTimeMillis() + rand.nextInt() + ".org";
        String password = "admin123";
        String firstName = "automatedTenant";
        tenantStub.addTenant(sessionCookie, domainName, password, "automatedTenant", "Demo");

        TenantInfoBean getTenantInfoBean;
        getTenantInfoBean = tenantStub.getTenant(sessionCookie, domainName);
        assertEquals("Tenant domainName does not match", domainName, getTenantInfoBean.getTenantDomain());
        assertTrue("Tenant is not active", getTenantInfoBean.getActive());
        assertEquals("Tenant email does not match", "wso2automation.test@wso2.com", getTenantInfoBean.getEmail());
        assertEquals("Tenant firstName does not match", firstName, getTenantInfoBean.getFirstname());
        assertEquals("Tenant lastName does not match", "automatedTenantwso2automation", getTenantInfoBean.getLastname());
        assertEquals("Tenant usage plan does not match", "Demo", getTenantInfoBean.getUsagePlan());
        assertNotNull("Tenant Id not found", getTenantInfoBean.getTenantId());

        login(firstName + "@" + domainName, password, FrameworkSettings.MANAGER_BACKEND_URL);
    }


    @Override
    public void cleanup() {
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
