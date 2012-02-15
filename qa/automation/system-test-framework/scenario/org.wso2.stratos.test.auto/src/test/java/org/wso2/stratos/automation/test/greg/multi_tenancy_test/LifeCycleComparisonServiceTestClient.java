package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;


public class LifeCycleComparisonServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(LifeCycleComparisonServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static WSRegistryServiceClient registry_diffDomainUser1 = null;
    private static Registry governance_admin1 = null;
    private static Registry governance_admin2 = null;
    private static final String StateProperty_admin1 = "registry.lifecycle.ServiceLifeCycle.state";
    private static final String StateProperty_admin2 = "registry.lifecycle.ServiceLifeCycle.state";

    @Override
    public void init() {
        testClassName = LifeCycleComparisonServiceTestClient.class.getName();
        String tenantId = "3";
        String diff_Domainuser = "2";
        int tenantID_testUser = 3;
        String userID = "testuser1";
        String userPassword = "test123";
        String roleName = "admin";

        registry = new RegistryProvider().getRegistry(tenantId);
        registry_diffDomainUser1 = new RegistryProvider().getRegistry(diff_Domainuser);

        GregUserCreator GregUserCreator = new GregUserCreator();
        GregUserCreator.deleteUsers(tenantID_testUser, userID);
        GregUserCreator.addUser(tenantID_testUser, userID, userPassword, roleName);

        governance_admin1 = new RegistryProvider().getGovernance(registry, tenantId);
        governance_admin2 = new RegistryProvider().getGovernance(registry_diffDomainUser1, tenantId);
        //delete artifacts
        deleteResources();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Multi Tenancy LifeCycleComparisonServiceTestClient Test Cases............................ ");
        checklifeCycleComparison();
        log.info("Completed Running Multi Tenancy LifeCycleComparisonServiceTestClient Test Cases...................");
    }

    @Override
    public void cleanup() {
    }

    private void deleteResources() {
        try {
            registry.delete("/_system/governance/trunk/wsdls");
            registry_diffDomainUser1.delete("/_system/governance/trunk/wsdls");

            registry.delete("/_system/governance/trunk/services");
            registry_diffDomainUser1.delete("/_system/governance/trunk/services");
        } catch (RegistryException e) {
            log.error("deleteResources Registry Exception thrown:" + e.getMessage());
            Assert.fail("deleteResources Registry Exception thrown:" + e.getMessage());
        }
    }

    public void addWSDL(Registry governance, String wsdl_url) {
        //        String wsdl_url = "http://svn.wso2.org/repos/wso2/trunk/carbon/components/governance/org.wso2.carbon.governance.api/src/test/resources/test-resources/wsdl/BizService.wsdl";
        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl;
        try {
            wsdl = wsdlManager.newWsdl(wsdl_url);
            wsdl.addAttribute("creator2", "it is me");
            wsdl.addAttribute("version2", "0.01");
            wsdlManager.addWsdl(wsdl);
            log.info("Add wsdl");
        } catch (GovernanceException e) {
            log.error("addWSDL GovernanceException thrown:" + e.getMessage());
            Assert.fail("addWSDL GovernanceException thrown thrown:" + e.getMessage());
        }
    }

    private void verifyResourceExists(String wsdl_path_admin1, String wsdl_path_admin2) {
        try {
            assertTrue("admin1 resource exist:", registry.resourceExists(wsdl_path_admin1));
            assertFalse("admin2 resource exist:", registry_diffDomainUser1.resourceExists(wsdl_path_admin1));
            assertFalse("admin1 resource exist:", registry.resourceExists(wsdl_path_admin2));
            assertTrue("admin2 resource exist:", registry_diffDomainUser1.resourceExists(wsdl_path_admin2));
        } catch (RegistryException e) {
            log.error("verifyResourceExists GovernanceException thrown:" + e.getMessage());
            Assert.fail("verifyResourceExistsL GovernanceException thrown thrown:" + e.getMessage());
        }
    }


    private void verifyResourceDeleted(String wsdl_path_admin1, String wsdl_path_admin2) {
        try {
            assertFalse("admin1 resource exist:", registry.resourceExists(wsdl_path_admin1));
            assertFalse("admin2 resource exist:", registry_diffDomainUser1.resourceExists(wsdl_path_admin1));
            assertFalse("admin1 resource exist:", registry.resourceExists(wsdl_path_admin2));
            assertFalse("admin2 resource exist:", registry_diffDomainUser1.resourceExists(wsdl_path_admin2));
        } catch (RegistryException e) {
            log.error("verifyResourceDeleted GovernanceException thrown:" + e.getMessage());
            Assert.fail("verifyResourceDeleted GovernanceException thrown thrown:" + e.getMessage());
        }
    }

    private void promoteLifeCycle(String wsdl_path_admin1, String wsdl_path_admin2) {
        String admin1_state = null;
        String admin2_state = null;

        try {
            registry.associateAspect(wsdl_path_admin1, "ServiceLifeCycle");
            registry_diffDomainUser1.associateAspect(wsdl_path_admin2, "ServiceLifeCycle");

            // assert admin 1 & admin 2 life cycle states
            assertEquals(registry.get(wsdl_path_admin1).getProperty(StateProperty_admin1), registry_diffDomainUser1.get(wsdl_path_admin2).getProperty(StateProperty_admin2));

//            //promote two steps - admin 1 life cycle
            registry.invokeAspect(wsdl_path_admin1, "ServiceLifeCycle", "Promote");
            registry.invokeAspect(wsdl_path_admin1, "ServiceLifeCycle", "Promote");

            //promote one step -- admin 2 life cycle
            registry_diffDomainUser1.invokeAspect(wsdl_path_admin2, "ServiceLifeCycle", "Promote");

            // assert admin 1 & admin 2 life cycle states are different
            assertNotSame(registry.get(wsdl_path_admin1).getProperty(StateProperty_admin1), registry_diffDomainUser1.get(wsdl_path_admin2).getProperty(StateProperty_admin2));

            //promote from anothe two steps - admin 2 life cycle
            registry_diffDomainUser1.invokeAspect(wsdl_path_admin2, "ServiceLifeCycle", "Promote");

            // assert admin 1 & admin 2 life cycle states are the same
            assertEquals(registry.get(wsdl_path_admin1).getProperty(StateProperty_admin1), registry_diffDomainUser1.get(wsdl_path_admin2).getProperty(StateProperty_admin2));
        } catch (RegistryException e) {
            log.error("promoteLifeCycle RegistryExceptio thrown:" + e.getMessage());
            Assert.fail("promoteLifeCycle RegistryExceptio thrown thrown:" + e.getMessage());
        }


        try {
            // assert admin 2 life cycle state is visible from admin 1
            admin1_state = registry.get(wsdl_path_admin2).getProperty(StateProperty_admin2);
        } catch (RegistryException e) {

            log.info("promoteLifeCycle RegistryExceptio Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert life cycle does not appear to admin1:
            Assert.assertNull(admin1_state);
            System.out.println("admin 1 is unable to view admin2 life cycle state");
        }

        try {
            // assert admin 2 life cycle state is visible from admin 1
            admin2_state = registry_diffDomainUser1.get(wsdl_path_admin1).getProperty(StateProperty_admin1);
        } catch (RegistryException e) {
            log.info("promoteLifeCycle RegistryExceptio Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert life cycle does not appear to admin1:
            Assert.assertNull(admin2_state);
            System.out.println("admin 2 is unable to view admin1 life cycle state");
        }
    }

    private void demoteLifeCycle(String wsdl_path_admin1, String wsdl_path_admin2) {
        String admin1_state = null;
        String admin2_state = null;

        try {
            registry.associateAspect(wsdl_path_admin1, "ServiceLifeCycle");
            registry_diffDomainUser1.associateAspect(wsdl_path_admin2, "ServiceLifeCycle");

            // assert admin 1 & admin 2 life cycle states
            assertEquals(registry.get(wsdl_path_admin1).getProperty(StateProperty_admin1), registry_diffDomainUser1.get(wsdl_path_admin2).getProperty(StateProperty_admin2));
//            //demote two steps - admin 1 life cycle
            registry.invokeAspect(wsdl_path_admin1, "ServiceLifeCycle", "Demote");
            registry.invokeAspect(wsdl_path_admin1, "ServiceLifeCycle", "Demote");
            //demote one step -- admin 2 life cycle
            registry_diffDomainUser1.invokeAspect(wsdl_path_admin2, "ServiceLifeCycle", "Demote");
            // assert admin 1 & admin 2 life cycle states are different
            assertNotSame(registry.get(wsdl_path_admin1).getProperty(StateProperty_admin1), registry_diffDomainUser1.get(wsdl_path_admin2).getProperty(StateProperty_admin2));
            //demote from anothe two steps - admin 2 life cycle
            registry_diffDomainUser1.invokeAspect(wsdl_path_admin2, "ServiceLifeCycle", "Demote");
//
        } catch (RegistryException e) {
            log.error("promoteLifeCycle RegistryExceptio thrown:" + e.getMessage());
            Assert.fail("promoteLifeCycle RegistryExceptio thrown thrown:" + e.getMessage());
        }

        try {
            // assert admin 2 life cycle state is visible from admin 1
            admin1_state = registry.get(wsdl_path_admin2).getProperty(StateProperty_admin2);
        } catch (RegistryException e) {
            log.info("promoteLifeCycle RegistryExceptio Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert life cycle does not appear to admin1:
            Assert.assertNull(admin1_state);
            log.info("Demote - admin 1 is unable to view admin2 life cycle state");
        }

        try {
            // assert admin 2 life cycle state is visible from admin 1
            admin2_state = registry_diffDomainUser1.get(wsdl_path_admin1).getProperty(StateProperty_admin1);

        } catch (RegistryException e) {
            log.info("promoteLifeCycle RegistryExceptio Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert life cycle does not appear to admin1:
            Assert.assertNull(admin2_state);
            log.info("Demote -admin 2 is unable to view admin1 life cycle state");
        }
    }

    private void checklifeCycleComparison() {
        String wsdl_url_admin1 = "http://people.wso2.com/~evanthika/wsdls/BizService.wsdl";
        String wsdl_url_admin2 = "http://geocoder.us/dist/eg/clients/GeoCoder.wsdl";
        String wsdl_path_admin1 = "/_system/governance/trunk/wsdls/com/foo/BizService.wsdl";
        String wsdl_path_admin2 = "/_system/governance/trunk/wsdls/us/geocoder/rpc/geo/coder/us/GeoCoder.wsdl";

        //add Wsdl -admin 1
        addWSDL(governance_admin1, wsdl_url_admin1);
        //add wsdl - admin2
        addWSDL(governance_admin2, wsdl_url_admin2);
        //assert resource exists:
        verifyResourceExists(wsdl_path_admin1, wsdl_path_admin2);
        // assert promote life
        promoteLifeCycle(wsdl_path_admin1, wsdl_path_admin2);
        // assert demote life
        demoteLifeCycle(wsdl_path_admin1, wsdl_path_admin2);
        //delete life cycle
        deleteResources();
        //assert resource deleted successfully
        verifyResourceDeleted(wsdl_path_admin1, wsdl_path_admin2);
        log.info("Multi Tenancy LifeCycleComparisonServiceTestClient - Passed ");
    }
}