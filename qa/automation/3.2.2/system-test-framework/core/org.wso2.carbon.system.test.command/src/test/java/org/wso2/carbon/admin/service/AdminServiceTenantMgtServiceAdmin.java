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

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.tenant.mgt.stub.*;
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AdminServiceTenantMgtServiceAdmin {

    private static final Log log = LogFactory.getLog(AdminServiceTenantMgtServiceAdmin.class);

    private final String serviceName = "TenantMgtAdminService";
    private TenantMgtAdminServiceStub tenantMgtAdminServiceStub;
    private String endPoint;

    public AdminServiceTenantMgtServiceAdmin(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        tenantMgtAdminServiceStub = new TenantMgtAdminServiceStub(endPoint);
    }

    public void addTenant(String sessionCookie) throws ActivateTenantExceptionException, RemoteException, AddTenantExceptionException, GetTenantExceptionException {

        new AuthenticateStub().authenticateStub(sessionCookie, tenantMgtAdminServiceStub);
        Calendar calendar = new GregorianCalendar(2011, Calendar.SEPTEMBER, 20);

        TenantInfoBean tenantInfoBean = new TenantInfoBean();
        tenantInfoBean.setActive(true);
        tenantInfoBean.setEmail("wso2automation@wso2.com");
        tenantInfoBean.setAdminPassword("admin123");
        tenantInfoBean.setAdmin("admin123");
        tenantInfoBean.setTenantDomain("wso2autoQA00008.org");
        tenantInfoBean.setCreatedDate(calendar);
        tenantInfoBean.setFirstname("wso2Automation");
        tenantInfoBean.setLastname("wso2AutomationLastName");
        tenantInfoBean.setSuccessKey("true");
        //tenantInfoBean.setOriginatedService();
        tenantInfoBean.setTenantId(1);
        tenantInfoBean.setUsagePlan("free");

        tenantMgtAdminServiceStub.getTenant("manualQA0001.org");
        tenantMgtAdminServiceStub.addTenant(tenantInfoBean);
        tenantMgtAdminServiceStub.activateTenant("AutomatedTenant" + 1 + ".com");

    }
}
