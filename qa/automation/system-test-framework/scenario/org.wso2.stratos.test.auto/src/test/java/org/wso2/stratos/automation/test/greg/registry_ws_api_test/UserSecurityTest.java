package org.wso2.stratos.automation.test.greg.registry_ws_api_test;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceUserMgtService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;
import org.wso2.carbon.user.mgt.common.UserAdminException;

import java.io.File;

public class UserSecurityTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(UserSecurityTest.class);
    private static WSRegistryServiceClient registry = null;
    private Registry everyOneRegistry = null;
    private AdminServiceUserMgtService userAdminStub;
    private TenantDetails tenantAdminDetails;
    private String sessionCookie;
    private String roleName;
    private String userName;
    private String userPassword;
    String serverURL;


    @Override
    public void runSuccessCase() {
        log.info("Running WS-API UserSecurityTest Test Cases............................ ");
        deleteRoleAndUsers();
        addRoleWithUser();
        checkEveryoneRoleDeniedPermissions();
        checkEveryoneRoleAllowedPermissions();
        checkAdminRolePermissions();
        log.info("Completed Running WS-API UserSecurityTest Test Cases....................");
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void init() {
        testClassName = UserSecurityTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);

        //Tenant Details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));

        if (FrameworkSettings.getStratosTestStatus()) {
            serverURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain() + File.separator + "services" + File.separator;
        } else {
            serverURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + "services" + File.separator;

        }

        String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;
        String axis2Repo = resourcePath + File.separator + "moduleClient" + File.separator + "client";
        String axis2Conf = resourcePath + File.separator + "productConfigFiles" + File.separator + "axis2_client.xml";

        userAdminStub = new AdminServiceUserMgtService(FrameworkSettings.IS_BACKEND_URL);
        tenantAdminDetails = TenantListCsvReader.getTenantDetails(Integer.parseInt(tenantId));
        sessionCookie = login(tenantAdminDetails.getTenantName(), tenantAdminDetails.getTenantPassword(),
                FrameworkSettings.IS_BACKEND_URL);
        userName = "testuser1";
        roleName = "test";
        userPassword = "test123";

        ConfigurationContext configContext = null;
        try {
            configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(axis2Repo, axis2Conf);
            //test user login
            everyOneRegistry = new WSRegistryServiceClient(serverURL, userName, userPassword, configContext);

        } catch (AxisFault axisFault) {
            log.error("Axis2 fault thrown :" + axisFault.getMessage());
            Assert.fail("Axis2 fault thrown :" + axisFault.getMessage());
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }

    }


    private void checkEveryoneRoleDeniedPermissions() {
        try {
            //Admin Resource creation
            Resource onlyAdminAcessResource = registry.newResource();
            registry.put("/testuser1/adminresource", onlyAdminAcessResource);
        } catch (Exception e) {
            log.error("checkEveryoneRoleDeniedPermissions Exception thrown:" + e.getMessage());
            Assert.fail("checkEveryoneRoleDeniedPermissions Exception thrown:" + e.getMessage());

        }

        try {
            Resource everyoneResource = everyOneRegistry.newResource();
            everyoneResource.setContent("this is a test resource");
            everyOneRegistry.put("/testuser1/everyoneresource", everyoneResource);
            fail("Everyone should not be able to add a resource");
        } catch (Exception e) {
            log.error("everyOneRegistry user Exception thrown:" + e.getMessage());
        }


        try {
            everyOneRegistry.move("/testuser1/adminresource", "/newtestuser1");
            fail("Everyone should not be able to move resource");
        } catch (Exception e) {
            log.error("everyOneRegistry trying to move resource Exception thrown:" + e.getMessage());
        }

        try {
            registry.move("/testuser1/adminresource", "/newtestuser1");
            registry.delete("/testuser1");
            registry.delete("/newtestuser1");
        } catch (Exception e) {
            log.error("checkEveryoneRoleDeniedPermissions Admin user move resource Exception thrown:" + e.getMessage());
            Assert.fail("checkEveryoneRoleDeniedPermissions Admin user move resource Exception thrown:" + e.getMessage());
        }
    }

    private void checkEveryoneRoleAllowedPermissions() {
        try {
            Resource everoneAccessAccessResource = registry.newResource();
            registry.put("/testuser1/testuser1/everyoneAccessResource", everoneAccessAccessResource);
        } catch (Exception e) {
            log.error("checkEveryoneRoleAllowedPermissions Exception thrown:" + e.getMessage());
            Assert.fail("checkEveryoneRoleAllowedPermissionsException thrown:" + e.getMessage());
        }

        try {
            Resource everyoneResource = everyOneRegistry.newResource();
            everyoneResource.setContent("this is a test resource");
            everyOneRegistry.put("/testuser1/testuser1/everyoneresource", everyoneResource);
            fail("Everyone should not be able to add a resource");
        } catch (Exception e) {
            log.error("checkEveryoneRoleAllowedPermissions everyOneRegistry Exception thrown when moving registry:" + e.getMessage());
        }

        try {
            everyOneRegistry.move("/testuser1/testuser1/adminresource", "/newtestuser2");
            fail("Everyone should not be able to move resource");
            registry.delete("/testuser1");
            log.info("checkEveryoneRoleAllowedPermissions - Passed");
        } catch (Exception e) {
            log.error("checkEveryoneRoleAllowedPermissions everyOneRegistry Exception thrown when moving registry:" + e.getMessage());
        }
    }

    private void checkAdminRolePermissions() {
        try {
            Resource everyoneResource = registry.newResource();
            registry.put("/testuser1/testuser1/everyoneresource", everyoneResource);
        } catch (Exception e) {
            log.error("checkAdminRolePermissions  admin add resource Exception thrown:" + e.getMessage());
            Assert.fail("checkAdminRolePermissions Admin add resource thrown:" + e.getMessage());
        }

        try {
            Resource adminResource = registry.newResource();
            adminResource.setContent("this is a test resource");
            registry.put("/testuser1/everyoneresource", adminResource);
        } catch (Exception e) {
            log.error("checkAdminRolePermissions admin set content Exception thrown:" + e.getMessage());
            Assert.fail("checkAdminRolePermissions admin set conetent thrown:" + e.getMessage());
        }

        try {
            registry.move("/testuser1/everyoneresource", "/newtestuser3");
            registry.delete("/testuser1");
            registry.delete("/newtestuser3");
        } catch (Exception e) {
            log.error("checkAdminRolePermissions admin move resource Exception thrown:" + e.getMessage());
            Assert.fail("checkAdminRolePermissions admin move resource thrown:" + e.getMessage());
        }


    }

    private void addRoleWithUser() {
        String permission[] = {"/permission/admin/login"};
        String userList[] = {"admin123"};

        try {
            userAdminStub.addRole(roleName, userList, permission, sessionCookie); //add role with login permission before adding user list
            log.info("Role added successfully");
            String roles[] = {roleName};
            userAdminStub.addUser(sessionCookie, userName, userPassword, roles, null);
        } catch (UserAdminException e) {
            log.error("Add user fail" + e.getMessage());
            Assert.fail("Add user fail" + e.getMessage());
        }
    }

    private void deleteRoleAndUsers() {
        userAdminStub.deleteRole(sessionCookie, roleName);
        log.info("Role " + roleName + " deleted successfully");
        userAdminStub.deleteUser(sessionCookie, userName);
    }


    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }

}
