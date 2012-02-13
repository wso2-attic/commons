package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.policies.PolicyManager;
import org.wso2.carbon.governance.api.policies.dataobjects.Policy;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

public class PolicyImportServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(PolicyImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;

    @Override
    public void init() {
        testClassName = PolicyImportServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        //Delete already exising policies
        deletePolicy();

    }

    @Override
    public void runSuccessCase() {
        log.info("Running PolicyImportServiceTestClient Test Cases............................ ");
        addEncrOnlyAnonymousPolicy();
        addPolicySecurePartnerService01();
        addPolicySecurePartnerService02();
        addPolicySecurePartnerService03();
        addPolicySecurePartnerService04();
        addPolicySecurePartnerService05();
        addPolicySecurePartnerService06();
        addPolicySecurePartnerService07();
        addPolicySecurePartnerService08();
        addPolicySecurePartnerService09();
        addPolicySecurePartnerService10();
        addPolicySecurePartnerService11();
        addPolicySecurePartnerService12();
        addPolicySecurePartnerService13();
        addPolicySecurePartnerService14();
        addPolicySecurePartnerService15();
        addSecConSignOnly();
        addSgnEncrAnonymous();
        addSigEncr();
        addSigOnly();
        addSignOnlyAnonymous();
        addUTOverTransport();
        log.info("Completed Running PolicyImportServiceTestClient Test Cases............................ ");
    }


    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }


    public void createPolicy(String policy_url) {
        PolicyManager policyManager = new PolicyManager(governance);
        Policy policy = null;
        try {
            policy = policyManager.newPolicy(policy_url);
            policy.addAttribute("creator", "Aaaa");
            policy.addAttribute("version", "1.0.0");
            policyManager.addPolicy(policy);
        } catch (GovernanceException e) {
            log.error("createPolicy Exception thrown:" + e.getMessage());
            Assert.fail("createPolicy Exception thrown:" + e.getMessage());
        }
    }

    public void deletePolicy() {
        try {
            if (registry.resourceExists("/_system/governance/trunk/policies")) {
                registry.delete("/_system/governance/trunk/policies");
            }
        } catch (RegistryException e) {
            log.error("deletePolicy Exception thrown:" + e.getMessage());
        }
    }


    public void propertyAssertion(String policy_path) {
        Resource resource = null;
        try {
            resource = registry.get(policy_path);
            assertEquals("WSDL Property - WSI creator", resource.getProperty("creator"), "Aaaa");
            assertEquals("WSDL Property - WSI version", resource.getProperty("version"), "1.0.0");
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }


    public void policyContentAssertion(String keyword1, String keyword2, String policy_path) {
        Resource r1 = registry.newResource();
        String content = null;
        try {
            r1 = registry.get(policy_path);
            content = new String((byte[]) r1.getContent());
            assertTrue("Assert Content Policy file - keyword1", content.indexOf(keyword1) > 0);
            assertTrue("Assert Content Policy filel - keyword2", content.indexOf(keyword2) > 0);
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addEncrOnlyAnonymousPolicy() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/EncrOnlyAnonymous.xml";
        String policy_path = "/_system/governance/trunk/policies/EncrOnlyAnonymous.xml";
        String keyword1 = "EncrOnlyAnonymous";
        String keyword2 = "WssX509V3Token10";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("EncrOnlyAnonymous Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService01() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService01.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService01.xml";
        String keyword1 = "UTOverTransport";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);


        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService01 Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService02() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService02.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService02.xml";
        String keyword1 = "SecurePartnerService02";
        String keyword2 = "SigOnly";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService02 Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService03() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService03.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService03.xml";
        String keyword1 = "SgnOnlyAnonymous";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService03 Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService04() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService04.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService04.xml";
        String keyword1 = "EncrOnlyAnonymous";
        String keyword2 = "WssX509V3Token10";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService04 Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService05() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService05.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService05.xml";
        String keyword1 = "SigEncr";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService05Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService06() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService06.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService06.xml";
        String keyword1 = "SgnEncrAnonymous";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService06Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService07() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService07.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService07.xml";
        String keyword1 = "EncrOnlyUsername";
        String keyword2 = "service";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService07Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService08() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService08.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService08.xml";
        String keyword1 = "SgnEncrUsername";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService08Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }

    }

    private void addPolicySecurePartnerService09() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService09.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService09.xml";
        String keyword1 = "SecConSignOnly";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService09Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService10() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService10.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService10.xml";
        String keyword1 = "SecConEncrOnly";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService10Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService11() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService11.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService11.xml";
        String keyword1 = "SecConSgnEncr";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService11Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService12() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService12.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService12.xml";
        String keyword1 = "SecConSignOnlyAnonymous";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService12Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService13() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService13.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService13.xml";
        String keyword1 = "SecConEncrOnlyAnonymous";
        String keyword2 = "service";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService13Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService14() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService14.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService14.xml";
        String keyword1 = "SecConEncrUsername";
        String keyword2 = "service";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService14Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPolicySecurePartnerService15() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/PolicySecurePartnerService15.xml";
        String policy_path = "/_system/governance/trunk/policies/PolicySecurePartnerService15.xml";
        String keyword1 = "SecConSgnEncrUsername";
        String keyword2 = "service";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("PolicySecurePartnerService14Policy Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addSecConSignOnly() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/SecConSignOnly.xml";
        String policy_path = "/_system/governance/trunk/policies/SecConSignOnly.xml";
        String keyword1 = "SecConSignOnly";
        String keyword2 = "sp:SymmetricBinding";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("SecConSignOnly Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }

    }

    private void addSgnEncrAnonymous() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/SgnEncrAnonymous.xml";
        String policy_path = "/_system/governance/trunk/policies/SgnEncrAnonymous.xml";
        String keyword1 = "SgnEncrAnonymous";
        String keyword2 = "sp:X509Token";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("SgnEncrAnonymous Exists:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addSigEncr() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/SigEncr.xml";
        String policy_path = "/_system/governance/trunk/policies/SigEncr.xml";
        String keyword1 = "SigEncr";
        String keyword2 = "WssX509V3Token10";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("SigEncr:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addSigOnly() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/SignOnly.xml";
        String policy_path = "/_system/governance/trunk/policies/SignOnly.xml";
        String keyword1 = "SigOnly";
        String keyword2 = "WssX509V3Token10";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("SigOnly:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {

            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addSignOnlyAnonymous() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/SignOnlyAnonymous.xml";
        String policy_path = "/_system/governance/trunk/policies/SignOnlyAnonymous.xml";
        String keyword1 = "SgnOnlyAnonymous";
        String keyword2 = "WssX509V3Token10";
        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("SignOnlyAnonymous:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addUTOverTransport() {
        String policy_url = "http://people.wso2.com/~evanthika/policies/UTPolicy.xml";
        String policy_path = "/_system/governance/trunk/policies/UTPolicy.xml";
        String keyword1 = "UTOverTransport";
        String keyword2 = "Basic256";

        //Add Policy
        createPolicy(policy_url);

        try {
            //Assert Policy exists
            assertTrue("UTOverTransport:", registry.resourceExists(policy_path));
            //Assert Property
            propertyAssertion(policy_path);
            //Assert content of Policy
            policyContentAssertion(keyword1, keyword2, policy_path);
            //Remove Policy
            registry.delete(policy_path);
            //Assert Resource was deleted successfully
            assertFalse("Policy exists at " + policy_path, registry.resourceExists(policy_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }
}
