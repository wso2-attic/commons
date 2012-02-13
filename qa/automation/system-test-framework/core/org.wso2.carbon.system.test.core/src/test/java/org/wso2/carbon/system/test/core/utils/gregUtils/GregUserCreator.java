package org.wso2.carbon.system.test.core.utils.gregUtils;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceUserMgtService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.user.mgt.common.UserAdminException;

import java.io.File;


public class GregUserCreator {
    private static final Log log = LogFactory.getLog(GregUserCreator.class);
    private static WSRegistryServiceClient registry = null;
    private AdminServiceUserMgtService userAdminStub;
    private TenantDetails tenantAdminDetails;
    private String sessionCookie;

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }

    public void deleteUsers(int tenantId, String userID) {
        setInfoRolesAndUsers(tenantId);
        userAdminStub.deleteUser(sessionCookie, userID);
    }

    public void addUser(int tenantId, String userID, String userPassword, String roleName) {
        setInfoRolesAndUsers(tenantId);
        try {
            String roles[] = {roleName};
            userAdminStub.addUser(sessionCookie, userID, userPassword, roles, null);
        } catch (UserAdminException e) {
            log.error("Add user fail" + e.getMessage());
            Assert.fail("Add user fail" + e.getMessage());
        }
    }

    public void setInfoRolesAndUsers(int tenantID) {
        userAdminStub = new AdminServiceUserMgtService(FrameworkSettings.IS_BACKEND_URL);
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(tenantID);
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.IS_BACKEND_URL);
    }

    public WSRegistryServiceClient getRegistry(int tenantId, String userID, String userPassword) {
        setInfoRolesAndUsers(tenantId);
        //Tenant details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(Integer.toString(tenantId)));
        String userName = userID + "@" + tenantDetails.getTenantDomain();

        String serverURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain() + File.separator + "services" + File.separator;
        String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;
        String axis2Repo = resourcePath + File.separator + "moduleClient" + File.separator + "client";
        String axis2Conf = resourcePath + File.separator + "productConfigFiles" + File.separator + "axis2_client.xml";

        ConfigurationContext configContext = null;
        try {
            configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(axis2Repo, axis2Conf);

            String serviceEPR = serverURL + "AuthenticationAdmin";
            AuthenticationAdminStub stub = new AuthenticationAdminStub(configContext, serviceEPR);
            ServiceClient client = stub._getServiceClient();
            Options options = client.getOptions();
            options.setManageSession(true);
            String cookie = null;
            try {
                boolean result = stub.login(userName, userPassword, serviceEPR);
                if (result) {
                    cookie = ((String) stub._getServiceClient().getServiceContext().
                            getProperty(HTTPConstants.COOKIE_STRING));
                }
            } catch (Exception e) {
            }
            registry = new WSRegistryServiceClient(serverURL, cookie);

        } catch (AxisFault axisFault) {
            log.error("Axis2 fault thrown :" + axisFault.getMessage());
            Assert.fail("Axis2 fault thrown :" + axisFault.getMessage());
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
        log.info("GReg Registry -Login Success");
        return registry;
    }


}
