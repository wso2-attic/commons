/* Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*  http://www.apache.org/licenses/LICENSE-2.0
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.stratos.tenant.mgt.ui.TenantMgtAdminServiceStub;
import org.wso2.stratos.tenant.mgt.ui.beans.xsd.TenantInfoBean;

import java.rmi.RemoteException;

public class TenantMgtAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(TenantMgtAdminCommand.class);
    TenantMgtAdminServiceStub tenantMgtAdminServiceStub;

    public TenantMgtAdminCommand(TenantMgtAdminServiceStub tenantMgtAdminServiceStub) {
        this.tenantMgtAdminServiceStub = tenantMgtAdminServiceStub;
    }

    public void addTenantCommand(TenantInfoBean tenantInfoBean)throws Exception{
        tenantMgtAdminServiceStub.addTenant(tenantInfoBean);
    }

    public void activateTenantCommand(String tenantDomain) throws Exception {
        tenantMgtAdminServiceStub.activateTenant(tenantDomain);
    }

    public void deactivateTenantCommand(String tenantDomain) throws Exception {
        tenantMgtAdminServiceStub.deactivateTenant(tenantDomain);
    }

    public TenantInfoBean getTenantCommand(String tenantDomain) throws Exception {
        TenantInfoBean tenantInfoBean = new TenantInfoBean();
        tenantInfoBean = tenantMgtAdminServiceStub.getTenant(tenantDomain);
        return tenantInfoBean;
    }

    public TenantInfoBean[] retirveTenantCommand(String tenantDomain) throws Exception {
        TenantInfoBean[] tenantInfoBean = tenantMgtAdminServiceStub.retrieveTenants();
        return tenantInfoBean;
    }
    public void updateTenant(TenantInfoBean tenantInfoBean )throws Exception {
        tenantMgtAdminServiceStub.updateTenant(tenantInfoBean);
    }
}