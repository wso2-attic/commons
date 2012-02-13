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

package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.tenant.mgt.stub.*;
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AdminServiceTenantMgtServiceAdmin {

    private static final Log log = LogFactory.getLog(AdminServiceTenantMgtServiceAdmin.class);

    private TenantMgtAdminServiceStub tenantMgtAdminServiceStub;

    public AdminServiceTenantMgtServiceAdmin(String backEndUrl) {
        String serviceName = "TenantMgtAdminService";
        String endPoint = backEndUrl + serviceName;
        try {
            tenantMgtAdminServiceStub = new TenantMgtAdminServiceStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing tenantMgtAdminServiceStub failed : " + axisFault.getMessage());
            Assert.fail("Initializing tenantMgtAdminServiceStub failed : " + axisFault.getMessage());
        }
    }

    public void addTenant(String sessionCookie, String domainName, String password, String firstName, String usagePlan) {

        new AuthenticateStub().authenticateStub(sessionCookie, tenantMgtAdminServiceStub);

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        TenantInfoBean tenantInfoBean = new TenantInfoBean();
        tenantInfoBean.setActive(true);
        tenantInfoBean.setEmail("wso2automation.test@wso2.com");
        tenantInfoBean.setAdminPassword(password);
        tenantInfoBean.setAdmin(firstName);
        tenantInfoBean.setTenantDomain(domainName);
        tenantInfoBean.setCreatedDate(calendar);
        tenantInfoBean.setFirstname(firstName);
        tenantInfoBean.setLastname(firstName + "wso2automation");
        tenantInfoBean.setSuccessKey("true");
        tenantInfoBean.setUsagePlan(usagePlan);

        TenantInfoBean tenantInfoBeanGet;
        try {
            tenantInfoBeanGet = tenantMgtAdminServiceStub.getTenant(domainName);

            if (!tenantInfoBeanGet.getActive() && tenantInfoBeanGet.getTenantId() != 0) {
                tenantMgtAdminServiceStub.activateTenant(domainName);
                log.info("Tenant domain " + domainName + " Activated successfully");

            } else if (!tenantInfoBeanGet.getActive() && tenantInfoBeanGet.getTenantId() == 0) {
                tenantMgtAdminServiceStub.addTenant(tenantInfoBean);
                tenantMgtAdminServiceStub.activateTenant(domainName);
                log.info("Tenant domain " + domainName + " created and activated successfully");
            } else {
                log.info("Tenant domain " + domainName + " already registered");
            }
        } catch (RemoteException e) {
            log.error("RemoteException thrown while adding user/tenants : " + e.getMessage());
            Assert.fail("RemoteException thrown while adding user/tenants : " + e.getMessage());
        } catch (GetTenantExceptionException e) {
            log.error("GetTenantExceptionException thrown when getting user list : " + e.getMessage());
            Assert.fail("GetTenantExceptionException thrown when getting user list : " + e.getMessage());
        } catch (ActivateTenantExceptionException e) {
            log.error("Tenant activation was unsucessfull : " + e.getMessage());
            Assert.fail("Tenant activation was unsucessfull : " + e.getMessage());
        } catch (AddTenantExceptionException e) {
            log.error("Add new tenants fail : " + e.getMessage());
            Assert.fail("Add new tenants fail : " + e.getMessage());
        }
    }

    public TenantInfoBean getTenant(String sessionCookie, String tenantDomain) {
        new AuthenticateStub().authenticateStub(sessionCookie, tenantMgtAdminServiceStub);
        TenantInfoBean getTenantBean = null;
        try {
            getTenantBean = tenantMgtAdminServiceStub.getTenant(tenantDomain);
            Assert.assertNotNull("Domain Name not found", getTenantBean);
        } catch (RemoteException e) {
            log.error("RemoteException thrown while retrieving user/tenants : " + e.getMessage());
            Assert.fail("RemoteException thrown while retrieving user/tenants : " + e.getMessage());
        } catch (GetTenantExceptionException e) {
            log.error("GetTenantExceptionException thrown when getting user/tenant list : " + e.getMessage());
            Assert.fail("GetTenantExceptionException thrown when getting user/tenant list : " + e.getMessage());
        }
        return getTenantBean;
    }

    public void updateTenant(String sessionCookie, TenantInfoBean infoBean) {
        new AuthenticateStub().authenticateStub(sessionCookie, tenantMgtAdminServiceStub);
        try {
            tenantMgtAdminServiceStub.updateTenant(infoBean);
        } catch (RemoteException e) {
            log.error("RemoteException thrown while retrieving user/tenants : " + e.getMessage());
            Assert.fail("RemoteException thrown while retrieving user/tenants : " + e.getMessage());
        } catch (UpdateTenantExceptionException e) {
            log.error("UpdateTenantExceptionException thrown while updating tenant info : " + e.getMessage());
            Assert.fail("UpdateTenantExceptionException thrown while updating tenant info : " + e.getMessage());
        }
    }

    public void activateTenant(String sessionCookie, String domainName) {
        new AuthenticateStub().authenticateStub(sessionCookie, tenantMgtAdminServiceStub);
        try {
            tenantMgtAdminServiceStub.activateTenant(domainName);
        } catch (RemoteException e) {
            log.error("RemoteException thrown while retrieving user/tenants : " + e.getMessage());
            Assert.fail("RemoteException thrown while retrieving user/tenants : " + e.getMessage());
        } catch (ActivateTenantExceptionException e) {
            log.error("Tenant domain" + domainName + " activation fail" + e.getMessage());
            Assert.fail("Tenant domain" + domainName + " activation fail" + e.getMessage());

        }

    }

}
