package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.policies.PolicyManager;
import org.wso2.carbon.governance.api.policies.dataobjects.Policy;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;


public class PolicyUploadServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(PolicyUploadServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static WSRegistryServiceClient registry_testUser = null;
    private static WSRegistryServiceClient registry_diffDomainUser1 = null;
    private static Registry governance = null;

    @Override
    public void init() {
        testClassName = PolicyUploadServiceTestClient.class.getName();
        String tenantId = "3";
        String diff_Domainuser = "6";
        int tenantID_testUser = 3;
        String userID = "testuser1";
        String userPassword = "test123";
        String roleName = "admin";

        registry = new RegistryProvider().getRegistry(tenantId);
        registry_diffDomainUser1 = new RegistryProvider().getRegistry(diff_Domainuser);

        GregUserCreator GregUserCreator = new GregUserCreator();
        GregUserCreator.deleteUsers(tenantID_testUser, userID);
        GregUserCreator.addUser(tenantID_testUser, userID, userPassword, roleName);
        registry_testUser = GregUserCreator.getRegistry(tenantID_testUser, userID, userPassword);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        //delete policies
        deletePolicy();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Multi Tenancy PolicyUploadServiceTestClient Test Cases............................ ");
        addPolicy();
        log.info("Completed Running Multi Tenancy PolicyUploadServiceTestClient Test Cases...................");
    }

    @Override
    public void cleanup() {

    }

    private void deletePolicy() {
        try {
            registry.delete("/_system/governance/trunk/policies");
        } catch (RegistryException e) {
            log.error("deletePolicy Registry Exception thrown:" + e.getMessage());
            Assert.fail("deletePolicy Registry Exception thrown:" + e.getMessage());
        }
    }

    public void propertyAssertion(String policy_path, String property1, String property2) {
        Resource resource_adminUser = null;

        try {
            resource_adminUser = registry.get(policy_path);
            assertEquals("Policy Property - targetNamespace", resource_adminUser.getProperty("version"), property1);
            assertEquals("Policy Property - Creator", resource_adminUser.getProperty("creator"), property2);
        } catch (RegistryException e) {
            log.error("propertyAssertion adminUser Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion adminUser Exception thrown:" + e.getMessage());
        }

        try {
            registry_testUser.get(policy_path);
            assertEquals("Policy Property - targetNamespace", resource_adminUser.getProperty("version"), property1);
            assertEquals("Policy Property - Creator", resource_adminUser.getProperty("creator"), property2);
        } catch (RegistryException e) {
            log.error("propertyAssertion testUser Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion testUser Exception thrown:" + e.getMessage());
        }

        try {
            assertNotNull("Resource object shouldn't be null", registry_diffDomainUser1.get(policy_path));
        } catch (RegistryException e) {
            log.info("Can not get registry resource by different tenant");
        }
    }


    public void createPolicy(String policy_url) {
        PolicyManager policyManager = new PolicyManager(governance);
        Policy policy;
        try {
            policy = policyManager.newPolicy(policy_url);
            policy.addAttribute("creator", "Aaaa");
            policy.addAttribute("version", "1.0.0");
            policyManager.addPolicy(policy);
            log.info("Policy was added successfully");
        } catch (GovernanceException e) {
            log.error("createPolicy Exception thrown:" + e.getMessage());
            Assert.fail("createPolicy Exception thrown:" + e.getMessage());
        }
    }

    private void verifyResourceExists(String policy_path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertTrue("wsdl Exists :", registry.resourceExists(policy_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertTrue("wsdl exists:", registry_testUser.resourceExists(policy_path));
            // Assert differnt doamin user 1
            assertFalse("wsdl exists:", registry_diffDomainUser1.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("verifyResourceExists RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists RegistryException thrown:" + e.getMessage());
        }
    }

    public void policyContentAssertion(String policy_path, String keyword1, String keyword2) {
        String content_adminUser;
        String content_testUser;

        try {
            Resource r1;
            r1 = registry.get(policy_path);
            content_adminUser = new String((byte[]) r1.getContent());
            assertTrue("Assert Content Schema file - key word 1", content_adminUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content Schema file - key word 2", content_adminUser.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("schemaContentAssertion adminUser Registry Exception thrown:" + e.getMessage());
            Assert.fail("schemaContentAssertion adminUser Registry Exception thrown:" + e.getMessage());
        }


        try {
            Resource r2;
            r2 = registry_testUser.get(policy_path);
            content_testUser = new String((byte[]) r2.getContent());
            assertTrue("Assert Content Schema file - key word 1", content_testUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content Schema file - key word 2", content_testUser.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("schemaContentAssertion testUser Registry Exception thrown:" + e.getMessage());
            Assert.fail("schemaContentAssertion testUser Registry Exception thrown:" + e.getMessage());
        }

        try {
            Assert.assertNotNull(registry_diffDomainUser1.get(policy_path));

        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.info("Cannot read the policy by different tenant");
        }
    }

    private void verifyResourceDelete(String policy_path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertFalse("wsdl Exists :", registry.resourceExists(policy_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertFalse("wsdl exists:", registry_testUser.resourceExists(policy_path));
            // Assert differnt doamin user 1
            assertFalse("wsdl exists:", registry_diffDomainUser1.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("verifyResourceExists Exception thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists Exception thrown:" + e.getMessage());
        }
    }


    private void addPolicy() {
        String policy_url = "https://wso2.org/repos/wso2/trunk/commons/qa/qa-artifacts/greg/policies/policy.xml";
        String policy_path = "/_system/governance/trunk/policies/policy.xml";
        String property1 = "1.0.0";
        String property2 = "Aaaa";
        String keyword1 = "UTOverTransport";
        String keyword2 = "Basic256";

        //Add Policy
        createPolicy(policy_url);
        //assert policy exists
        verifyResourceExists(policy_path);
        //Assert Properties
        propertyAssertion(policy_path, property1, property2);
        //Assert Schema content
        policyContentAssertion(policy_path, keyword1, keyword2);
        //delete resources
        deletePolicy();
        //assert resouces deleted propely
        verifyResourceDelete(policy_path);
        log.info("Multi Tenancy PolicyUploadServiceTestClient - Passed");
    }
}
