package org.wso2.carbon.tenant.mgt.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.junit.Test;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.proxy.AuthenticationExceptionException;
import org.wso2.carbon.tenant.mgt.stub.TenantMgtAdminServiceStub;
import org.wso2.carbon.tenant.mgt.stub.beans.xsd.TenantInfoBean;
import org.wso2.carbon.utils.NetworkUtils;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TenantLoadTest extends TestCase {

    public int TENANT_COUNT;

    @Test
    public void testAddTenant() {
        try {
            ServerLogin serverLogin = new ServerLogin();
            TenantMgtAdminServiceStub tenantMgtAdminServiceStub = new InitializeTenantMgtAdminService().executeAdminStub(serverLogin.login());
            TenantMgtAdmin tenantMgtAdmin = new TenantMgtAdmin(tenantMgtAdminServiceStub);
            PropertyLoader.loadProperty();
            TENANT_COUNT = PropertyLoader.TENANT_COUNT;
            Calendar calendar = new GregorianCalendar(2011, Calendar.JUNE, 20);

            for (int i = 0; i <= TENANT_COUNT; i++) {
                TenantInfoBean tenantInfoBean = new TenantInfoBean();
                tenantInfoBean.setActive(true);
                tenantInfoBean.setEmail("AutomatedTenant" + i + "@wso2.com");
                tenantInfoBean.setAdminPassword("abc1231");
                tenantInfoBean.setAdmin("AutomatedTenant" + i);
                tenantInfoBean.setTenantDomain("AutomatedTenant" + i + ".com");
                tenantInfoBean.setCreatedDate(calendar);
                tenantInfoBean.setFirstname("chamara-AutomatedTenantFName:" + i);
                tenantInfoBean.setLastname("AutomatedTenantLName:" + i);
                //tenantInfoBean.setOriginatedService();
                tenantInfoBean.setTenantId(i);
                tenantInfoBean.setUsagePlan("free");
                tenantMgtAdmin.addTenant(tenantInfoBean);
                tenantMgtAdmin.activateTenant("AutomatedTenant" + i + ".com");

            }
        } catch (Exception e) {
            Assert.fail("Exception thrown while creating tenant : " + e);

        }
    }

    @Test
    public void testGetTenant() throws Exception {
        ServerLogin serverLogin = new ServerLogin();
        TenantMgtAdminServiceStub tenantMgtAdminServiceStub = new InitializeTenantMgtAdminService().executeAdminStub(serverLogin.login());
        TenantMgtAdmin tenantMgtAdmin = new TenantMgtAdmin(tenantMgtAdminServiceStub);
        for (int i = 0; i <= TENANT_COUNT; i++) {
            TenantInfoBean tenantInfoBean = tenantMgtAdmin.getTenant("AutomatedTenant" + i + ".com");
            if (!tenantInfoBean.getEmail().equals("AutomatedTenant" + i + "@wso2.com")) {
                Assert.fail("Tenant : " + "AutomatedTenant" + 1 + "   not found ");
            }
        }
    }

    @Test
    public void testLogin()
            throws RemoteException, SocketException, AuthenticationExceptionException {
        for (int i = 0; i <= TENANT_COUNT; i++) {
            String authenticationServiceURL = "https://cloud.private.wso2.com/services/AuthenticationAdmin";
            AuthenticationAdminStub authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);
            ServiceClient client = authenticationAdminStub._getServiceClient();
            Options options = client.getOptions();
            options.setManageSession(true);
            String userName = "AutomatedTenant" + i;
            String password = "abc1231";
            String hostName = NetworkUtils.getLocalHostname();
            authenticationAdminStub.login(userName, password, hostName);
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            String sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
            if (sessionCookie == null) {
                Assert.fail("Unable to login using Tenant : ");
            }
        }
    }


}
