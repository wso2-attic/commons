package org.wso2.carbon.tenant.mgt.test;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.tenant.mgt.stub.TenantMgtAdminServiceStub;
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.PaginatedTenantInfoBean;
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;

public class TenantMgtAdmin extends TestCase {

    private static final Log log = LogFactory.getLog(TenantMgtAdmin.class);
    TenantMgtAdminServiceStub tenantMgtAdminServiceStub;

    public TenantMgtAdmin(TenantMgtAdminServiceStub tenantMgtAdminServiceStub) {
        this.tenantMgtAdminServiceStub = tenantMgtAdminServiceStub;
    }

    public void addTenant(TenantInfoBean tenantInfoBean) throws Exception {
        tenantMgtAdminServiceStub.addTenant(tenantInfoBean);
    }

    public void activateTenant(String tenantDomain) throws Exception {
        tenantMgtAdminServiceStub.activateTenant(tenantDomain);
    }

    public void deactivateTenant(String tenantDomain) throws Exception {
        tenantMgtAdminServiceStub.deactivateTenant(tenantDomain);
    }

    public TenantInfoBean getTenant(String tenantDomain) throws Exception {
        TenantInfoBean tenantInfoBean = new TenantInfoBean();
        tenantInfoBean = tenantMgtAdminServiceStub.getTenant(tenantDomain);
        return tenantInfoBean;
    }

    public void updateTenant(TenantInfoBean tenantInfoBean) throws Exception {
        tenantMgtAdminServiceStub.updateTenant(tenantInfoBean);
    }

    public void restartTenant(String tenantDomain) throws Exception {
        tenantMgtAdminServiceStub.restartTenant(tenantDomain);
    }

    public TenantInfoBean[] retrieveTenant() throws Exception {
        return tenantMgtAdminServiceStub.retrieveTenants();
    }

    public PaginatedTenantInfoBean retrievePaginatedTenant(int pageNumber) throws Exception {
        return tenantMgtAdminServiceStub.retrievePaginatedTenants(pageNumber);
    }

    public void updateTenantPassword(TenantInfoBean tenantInfoBean) throws Exception {
        tenantMgtAdminServiceStub.updateTenantPassword(tenantInfoBean);
    }
}