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
import org.wso2.carbon.account.mgt.stub.beans.xsd.AccountInfoBean;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceStratosAccountMgt;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

public class UpdateFullNameTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(UpdateFullNameTest.class);
    private AdminServiceStratosAccountMgt adminServiceStratosAccountMgt;
    private String sessionCookie;
    private AccountInfoBean oldAccountInfoBean;


    @Override
    public void init() {
        testClassName = UpdateFullNameTest.class.getName();
        adminServiceStratosAccountMgt =
                new AdminServiceStratosAccountMgt(FrameworkSettings.MANAGER_BACKEND_URL);
        TenantDetails tenantAdminDetails = TenantListCsvReader.getTenantDetails(13);
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
        oldAccountInfoBean = adminServiceStratosAccountMgt.getTenantFullName(sessionCookie);
    }

    @Override
    public void runSuccessCase() {
        //First name and last name to be updated
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";

        AccountInfoBean setAccountInfoBean = new AccountInfoBean();

        //set first name and last name to info bean
        setAccountInfoBean.setFirstname(newFirstName);
        setAccountInfoBean.setLastname(newLastName);

        //update tenant full name
        log.debug("Update tenant full name");
        adminServiceStratosAccountMgt.updateTenantFullName(sessionCookie,setAccountInfoBean);
        log.info("Tenant full name updated successfully");


        assertEquals("Tenant first Name does not match ", newFirstName,
                adminServiceStratosAccountMgt.getTenantFullName(sessionCookie).getFirstname());
        assertEquals("Tenant last Name does not match ", newLastName,
                adminServiceStratosAccountMgt.getTenantFullName(sessionCookie).getLastname());
    }

    @Override
    public void cleanup() {
        //reset to older info bean - First and last names will be rest to previous
        adminServiceStratosAccountMgt.updateTenantFullName(sessionCookie, oldAccountInfoBean);
        log.info("Tenant full name reset to previous one");

        assertEquals("Tenant first Name does not match ", oldAccountInfoBean.getFirstname(),
                adminServiceStratosAccountMgt.getTenantFullName(sessionCookie).getFirstname());
        assertEquals("Tenant last Name does not match ", oldAccountInfoBean.getLastname(),
                adminServiceStratosAccountMgt.getTenantFullName(sessionCookie).getLastname());
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
