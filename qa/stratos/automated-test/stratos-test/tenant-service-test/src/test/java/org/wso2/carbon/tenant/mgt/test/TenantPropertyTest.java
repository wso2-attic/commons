package org.wso2.carbon.tenant.mgt.test;

import junit.framework.TestCase;
import org.wso2.carbon.tenant.mgt.stub.TenantMgtAdminServiceStub;
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Checking tenant parameters are set or not.
 */
public class TenantPropertyTest extends TestCase {

    public void testTenantParam() throws Exception {

        ServerLogin serverLogin = new ServerLogin();
        TenantMgtAdminServiceStub tenantMgtAdminServiceStub = new InitializeTenantMgtAdminService().executeAdminStub(serverLogin.login());
        TenantMgtAdmin tenantMgtAdmin = new TenantMgtAdmin(tenantMgtAdminServiceStub);
        Calendar calendar = new GregorianCalendar(2011, Calendar.JUNE, 20);
        TenantInfoBean setTenantInfoBean = new TenantInfoBean();
        TenantInfoBean getTenantInfoBean;
        setTenantInfoBean.setActive(true);
        setTenantInfoBean.setEmail("AutomatedTenant@paramtest.com");
        setTenantInfoBean.setAdminPassword("chamara123");
        setTenantInfoBean.setAdmin("AutomatedTenant");
        setTenantInfoBean.setTenantDomain("AutomatedTenant_test5.com");
        setTenantInfoBean.setCreatedDate(calendar);
        setTenantInfoBean.setFirstname("AutomatedTenantFName");
        setTenantInfoBean.setLastname("AutomatedTenantLName");
        setTenantInfoBean.setOriginatedService("originated");
        setTenantInfoBean.setTenantId(1);
        setTenantInfoBean.setUsagePlan("multitenancy-free");
        setTenantInfoBean.setSuccessKey("Key");

        tenantMgtAdmin.addTenant(setTenantInfoBean);
        tenantMgtAdmin.activateTenant("AutomatedTenant_test5.com");

        getTenantInfoBean = tenantMgtAdmin.getTenant("AutomatedTenant_test5.com");

        assertTrue("Tenant Active information doesn't match ", getTenantInfoBean.isActiveSpecified());
        assertTrue("Tenant email id information doesn't match ", getTenantInfoBean.isEmailSpecified());
        assertTrue("Tenant first name information doesn't match ", getTenantInfoBean.isFirstnameSpecified());
        assertTrue("Tenant last name information doesn't match ", getTenantInfoBean.isLastnameSpecified());
        assertTrue("Tenant domain information doesn't match ", getTenantInfoBean.isTenantDomainSpecified());
        assertTrue("Tenant ID information doesn't match ", getTenantInfoBean.isTenantIdSpecified());
        assertTrue("Tenant usage plan information doesn't match ", getTenantInfoBean.isUsagePlanSpecified());
        assertTrue("Tenant admin name information doesn't match ", getTenantInfoBean.isAdminSpecified());
        assertTrue("Tenant created date information doesn't match ", getTenantInfoBean.isCreatedDateSpecified());
        assertTrue("Tenant admin password information doesn't match ", getTenantInfoBean.isAdminPasswordSpecified());
        assertTrue("Tenant originated service information doesn't match ", getTenantInfoBean.isOriginatedServiceSpecified());
        assertTrue("Tenant success key information doesn't match ", getTenantInfoBean.isSuccessKeySpecified());


        assertTrue("Tenant email id information doesn't match ", getTenantInfoBean.getEmail().equals("AutomatedTenant@paramtest.com"));
        assertTrue("Tenant first name information doesn't match ", getTenantInfoBean.getFirstname().equals("AutomatedTenantFName"));
        assertTrue("Tenant last name information doesn't match ", getTenantInfoBean.getLastname().equals("AutomatedTenantLName"));
        assertTrue("Tenant domain information doesn't match ", getTenantInfoBean.getTenantDomain().equals("AutomatedTenant.com"));
        assertTrue("Tenant ID information doesn't match ", getTenantInfoBean.getTenantId() >= 1);
        assertTrue("Tenant usage plan information doesn't match ", getTenantInfoBean.getUsagePlan().equals("free"));
        assertTrue("Tenant Active information doesn't match ", getTenantInfoBean.getActive());
        assertTrue("Tenant admin name information doesn't match ", getTenantInfoBean.getAdmin().equals("AutomatedTenant"));
        assertTrue("Tenant created date information doesn't match ", getTenantInfoBean.getCreatedDate().equals(calendar));
        assertTrue("Tenant admin password information doesn't match ", getTenantInfoBean.getAdminPassword().equals("chamara123"));
        assertTrue("Tenant originated service information doesn't match ", getTenantInfoBean.getOriginatedService().equals("originated"));
        assertTrue("Tenant success key information doesn't match ", getTenantInfoBean.getSuccessKey().equals("Key"));

    }

    public void testTenantMGTServiceStub() throws Exception {
        ServerLogin serverLogin = new ServerLogin();
        TenantMgtAdminServiceStub tenantMgtAdminServiceStub = new InitializeTenantMgtAdminService().executeAdminStub(serverLogin.login());
        TenantMgtAdmin tenantMgtAdmin = new TenantMgtAdmin(tenantMgtAdminServiceStub);
        tenantMgtAdmin.deactivateTenant("AutomatedTenant.com");
        assertFalse("Tenant not deactivated", tenantMgtAdmin.getTenant("AutomatedTenant.com").isActiveSpecified());
    }

}
