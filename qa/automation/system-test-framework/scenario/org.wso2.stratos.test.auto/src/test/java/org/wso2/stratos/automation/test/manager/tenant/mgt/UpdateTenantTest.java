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
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UpdateTenantTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(UpdateTenantTest.class);
    private static TenantInfoBean tenantInfoBeanGet;
    private static AdminServiceTenantMgtServiceAdmin tenantStub;
    private String sessionCookie;
    private TenantDetails tenantAdminDetails;

    @Override
    public void init() {
        testClassName = UpdateTenantTest.class.getName();
        tenantStub = new AdminServiceTenantMgtServiceAdmin(FrameworkSettings.MANAGER_BACKEND_URL);
        TenantDetails superTenantDetails = TenantListCsvReader.getTenantDetails(0); //get super tenant credential
        sessionCookie = login(superTenantDetails.getTenantName(), superTenantDetails.getTenantPassword(),
                FrameworkSettings.MANAGER_BACKEND_URL);
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(13);
    }

    @Override
    public void runSuccessCase() {
        //get user credentials
        tenantInfoBeanGet = tenantStub.getTenant(sessionCookie, tenantAdminDetails.getTenantDomain());
        log.debug("TenantID " + tenantInfoBeanGet.getTenantId());
        log.debug("Usage plan before update" + tenantInfoBeanGet.getUsagePlan());

        //create calendar object to set tenant created time
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        //tenant update properties
        String password = "admin123Updated";
        String firstName = "automatedTenantUpdated";
        String updatedLastName = firstName + "wso2automationUpdated";
        String updateEmail = "wso2automation.test.update@wso2.com";
        String usagePlan = "SMB";

        //get tenant admin name
        String adminName = tenantAdminDetails.getTenantName().substring(0, tenantAdminDetails.getTenantName().indexOf('@'));

        //set updated values to tenantInfoBean
        TenantInfoBean setTenantInfoBean = new TenantInfoBean();
        setTenantInfoBean.setActive(true);
        setTenantInfoBean.setAdminPassword(password);
        setTenantInfoBean.setAdmin(adminName);
        setTenantInfoBean.setEmail(updateEmail);
        setTenantInfoBean.setFirstname(firstName);
        setTenantInfoBean.setLastname(updatedLastName);
        setTenantInfoBean.setUsagePlan(usagePlan);
        setTenantInfoBean.setSuccessKey("true");
        setTenantInfoBean.setCreatedDate(calendar);
        setTenantInfoBean.setTenantDomain(tenantAdminDetails.getTenantDomain());
        setTenantInfoBean.setTenantId(tenantInfoBeanGet.getTenantId());

        tenantStub.updateTenant(sessionCookie, setTenantInfoBean);
        log.info("Tenant info updated");


        TenantInfoBean getUpdatedTenantInfoBean = tenantStub.getTenant(sessionCookie, tenantAdminDetails.getTenantDomain());

        log.debug("Assert tenant info values");
        assertTrue("Tenant is not active after update", getUpdatedTenantInfoBean.getActive());
//        assertEquals("Password has not been updated", tenantInfoBeanGet.getAdminPassword(), getUpdatedTenantInfoBean.getAdminPassword());
        assertEquals("Admin user name has changed after update", tenantInfoBeanGet.getAdmin(),
                getUpdatedTenantInfoBean.getAdmin());
        assertEquals("Email address hasn't been updated", updateEmail, getUpdatedTenantInfoBean.
                getEmail());
        assertEquals("Tenant first name hasn't been updated", tenantInfoBeanGet.getFirstname(),
                getUpdatedTenantInfoBean.getFirstname());
        assertEquals("Tenant last name hasn't been updated", tenantInfoBeanGet.getLastname(),
                getUpdatedTenantInfoBean.getLastname());
        assertEquals("Tenant usage plan hasn't been updated", usagePlan, getUpdatedTenantInfoBean.
                getUsagePlan());
        assertEquals("Tenant Ids are not matching after updation", tenantInfoBeanGet.getTenantId(),
                getUpdatedTenantInfoBean.getTenantId());
        assertEquals("Tenant Domain are not matching updation", tenantInfoBeanGet.getTenantDomain(),
                getUpdatedTenantInfoBean.getTenantDomain());
        assertNotNull("Created date is null", getUpdatedTenantInfoBean.getCreatedDate().getTime());

        //tenant login check after password update

        login(tenantAdminDetails.getTenantName(), password, FrameworkSettings.MANAGER_BACKEND_URL);
        log.info("Login successful with updated tenant credentials");
    }

    @Override                                                                   
    public void cleanup() {
        //Reset tenant info to older values
        TenantInfoBean setTenantInfoBean = new TenantInfoBean();
        setTenantInfoBean.setActive(true);
        setTenantInfoBean.setAdminPassword(tenantAdminDetails.getTenantPassword());
        setTenantInfoBean.setAdmin(tenantInfoBeanGet.getAdmin());
        setTenantInfoBean.setEmail(tenantInfoBeanGet.getEmail());
        setTenantInfoBean.setFirstname(tenantInfoBeanGet.getFirstname());
        setTenantInfoBean.setLastname(tenantInfoBeanGet.getLastname());
        setTenantInfoBean.setUsagePlan(tenantInfoBeanGet.getUsagePlan());
        setTenantInfoBean.setSuccessKey("true");
        setTenantInfoBean.setTenantDomain(tenantInfoBeanGet.getTenantDomain());
        setTenantInfoBean.setTenantId(tenantInfoBeanGet.getTenantId());
        log.info("Tenant info reset to default begins");
        tenantStub.updateTenant(sessionCookie, setTenantInfoBean);
        log.info("Tenant inf reset to default");

        //tenant login check after tenant info reset
        login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(), FrameworkSettings.MANAGER_BACKEND_URL);
        log.info("Tenant login successful with default credentials");
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
