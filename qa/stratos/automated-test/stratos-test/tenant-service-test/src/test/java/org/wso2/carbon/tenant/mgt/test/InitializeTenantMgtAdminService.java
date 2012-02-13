package org.wso2.carbon.tenant.mgt.test;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.tenant.mgt.stub.TenantMgtAdminServiceStub;

import java.io.IOException;


public class InitializeTenantMgtAdminService {
    private static final Log log = LogFactory.getLog(InitializeTenantMgtAdminService.class);

    public TenantMgtAdminServiceStub executeAdminStub(String sessionCookie) throws IOException {
        PropertyLoader.loadProperty();

        String serviceURL = "https://" + PropertyLoader.CLOUD_NAME + "/services/TenantMgtAdminService";
        TenantMgtAdminServiceStub tenantMgtAdminServiceStub = null;
        try {
            tenantMgtAdminServiceStub = new TenantMgtAdminServiceStub(serviceURL);

            ServiceClient client = tenantMgtAdminServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
        } catch (AxisFault axisFault) {
            log.error("AxisFault in InitializeProxyAdminCommand class : " + axisFault.toString());
            Assert.fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        }
        log.info("InitializeProxyAdminCommand : ProxyAdminStub created with session " + sessionCookie);
        return tenantMgtAdminServiceStub;

    }
}
