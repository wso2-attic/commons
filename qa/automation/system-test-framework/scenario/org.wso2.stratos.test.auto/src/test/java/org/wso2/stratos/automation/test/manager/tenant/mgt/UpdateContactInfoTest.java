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
import org.wso2.carbon.admin.service.AdminServiceStratosAccountMgt;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

public class UpdateContactInfoTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(UpdateContactInfoTest.class);
    private String tenantOldContactInfo;
    private AdminServiceStratosAccountMgt adminServiceStratosAccountMgt;
    private String sessionCookie;

    @Override
    public void init() {
        testClassName = UpdateContactInfoTest.class.getName();
        adminServiceStratosAccountMgt =
                new AdminServiceStratosAccountMgt(FrameworkSettings.MANAGER_BACKEND_URL);
        TenantDetails tenantAdminDetails = TenantListCsvReader.getTenantDetails(13);
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
    }

    @Override
    public void runSuccessCase() {
        String updatedEmailAddress = "tenantupdatedemail.test@wso2.com";
        tenantOldContactInfo = adminServiceStratosAccountMgt.getTenantcontact(sessionCookie);
        //update tenant email address
        adminServiceStratosAccountMgt.updateTenantContact(sessionCookie, updatedEmailAddress);
        assertEquals("Contact info doesn't get updated", updatedEmailAddress,
                adminServiceStratosAccountMgt.getTenantcontact(sessionCookie));
    }

    @Override
    public void cleanup() {
        //reset contact email
        adminServiceStratosAccountMgt.updateTenantContact(sessionCookie, tenantOldContactInfo);
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
