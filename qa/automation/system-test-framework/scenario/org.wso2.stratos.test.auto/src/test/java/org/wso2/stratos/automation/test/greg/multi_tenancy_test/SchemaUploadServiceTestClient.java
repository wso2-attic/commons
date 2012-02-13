package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.schema.SchemaManager;
import org.wso2.carbon.governance.api.schema.dataobjects.Schema;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;


public class SchemaUploadServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(SchemaUploadServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static WSRegistryServiceClient registry_testUser = null;
    private static WSRegistryServiceClient registry_diffDomainUser1 = null;
    private static Registry governance = null;

    @Override
    public void init() {
        testClassName = SchemaUploadServiceTestClient.class.getName();
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
        //Delete Schemas
        deleteSchema();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Multi Tenancy SchemaUploadServiceTestClient Test Cases............................ ");
        addSchema();
        log.info("Completed Running Multi Tenancy SchemaUploadServiceTestClient Test Cases.................. ");
    }

    @Override
    public void cleanup() {

    }

    private void deleteSchema() {
        try {
            registry.delete("/_system/governance/trunk/schemas");
            registry_diffDomainUser1.delete("/_system/governance/trunk/schemas");
        } catch (RegistryException e) {
            log.error("deleteSchema Registry Exception thrown:" + e.getMessage());
            Assert.fail("deleteSchema Registry Exception thrown:" + e.getMessage());
        }
    }

    public void createSchema(Registry governance, String schema_url) {
        SchemaManager schemaManager = new SchemaManager(governance);
        Schema schema;
        try {
            schema = schemaManager.newSchema(schema_url);
            schema.addAttribute("creator", "Aaaa");
            schema.addAttribute("version", "1.0.0");
            schemaManager.addSchema(schema);
            log.info("Schema was added successfully");
        } catch (GovernanceException e) {
            log.error("createSchema Exception thrown:" + e.getMessage());
            Assert.fail("createSchema Exception thrown:" + e.getMessage());
        }
    }

    public void propertyAssertion(String schema_path, String property1, String property2, String property3) {
        Resource resource_adminUser;
        Resource resource_testUser;
        Resource resource_diffDomainUser = null;
        try {
            resource_adminUser = registry.get(schema_path);
            assertEquals("Schema Property - Schema Validation", resource_adminUser.getProperty("Schema Validation"), property1);
            assertEquals("Schema Property - targetNamespace", resource_adminUser.getProperty("targetNamespace"), property2);
            assertEquals("Schema Property - Creator", resource_adminUser.getProperty("creator"), property3);
        } catch (RegistryException e) {
            log.error("propertyAssertion adminUser Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion adminUser Exception thrown:" + e.getMessage());
        }

        try {
            resource_testUser = registry_testUser.get(schema_path);
            assertEquals("Schema Property - Schema Validation", resource_testUser.getProperty("Schema Validation"), property1);
            assertEquals("Schema Property - targetNamespace", resource_testUser.getProperty("targetNamespace"), property2);
            assertEquals("Schema Property - Creator", resource_testUser.getProperty("creator"), property3);
        } catch (RegistryException e) {
            log.error("propertyAssertion testUser Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion testUser Exception thrown:" + e.getMessage());
        }

        try {
            resource_diffDomainUser = registry_diffDomainUser1.get(schema_path);
        } catch (RegistryException e) {
            log.info("propertyAssertion diffDomainUser Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert resource does not exists:
            Assert.assertNull(resource_diffDomainUser);
        }
    }

    private void verifyResourceExists(String schema_path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertTrue("wsdl Exists :", registry.resourceExists(schema_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertTrue("wsdl exists:", registry_testUser.resourceExists(schema_path));
            // Assert differnt doamin user 1
            assertFalse("wsdl exists:", registry_diffDomainUser1.resourceExists(schema_path));
        } catch (RegistryException e) {
            log.error("verifyResourceExists Exception thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists Exception thrown:" + e.getMessage());
        }
    }

    private void verifyResourceDelete(String schema_path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertFalse("wsdl Exists :", registry.resourceExists(schema_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertFalse("wsdl exists:", registry_testUser.resourceExists(schema_path));
            // Assert differnt doamin user 1
            assertFalse("wsdl exists:", registry_diffDomainUser1.resourceExists(schema_path));
        } catch (RegistryException e) {
            log.error("verifyResourceExists Exception thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists Exception thrown:" + e.getMessage());
        }
    }


    public void schemaContentAssertion(String schema_path, String keyword1, String keyword2) {
        String content_adminUser = null;
        String content_testUser;
        String content_diffDomainUser = null;

        try {
            Resource r1;
            r1 = registry.get(schema_path);
            content_adminUser = new String((byte[]) r1.getContent());
            assertTrue("Assert Content Schema file - key word 1", content_adminUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content Schema file - key word 2", content_adminUser.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("schemaContentAssertion adminUser Registry Exception thrown:" + e.getMessage());
            Assert.fail("schemaContentAssertion adminUser Registry Exception thrown:" + e.getMessage());
        }

        try {
            Resource r2;
            r2 = registry_testUser.get(schema_path);
            content_testUser = new String((byte[]) r2.getContent());
            assertTrue("Assert Content Schema file - key word 1", content_testUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content Schema file - key word 2", content_testUser.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("schemaContentAssertion testUser Registry Exception thrown:" + e.getMessage());
            Assert.fail("schemaContentAssertion testUser Registry Exception thrown:" + e.getMessage());
        }

        try {
            Resource r3;
            r3 = registry_diffDomainUser1.get(schema_path);
            content_diffDomainUser = new String((byte[]) r3.getContent());
            assertTrue("Assert Content Schema file - key word 1", content_adminUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content Schema file - key word 2", content_adminUser.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.info("propertyAssertion diffDomainUser Exception thrown:" + e.getMessage());
            //registry null exception is caught to assert resource does not exists:
            Assert.assertNull(content_diffDomainUser);
        }
    }

    private void addSchema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/Patient.xsd";
        String schema_path = "/_system/governance/trunk/schemas/org/ihc/xsd/Patient.xsd";
        String property1 = "Valid";
        String property2 = "http://ihc.org/xsd?patient";
        String property3 = "Aaaa";
        String keyword1 = "attributeFormDefault";
        String keyword2 = "languageCommunication";

        //Add Schema
        createSchema(governance, schema_url);
        //assert resource exists
        verifyResourceExists(schema_path);
        //Assert Properties
        propertyAssertion(schema_path, property1, property2, property3);
        //Assert Schema content
        schemaContentAssertion(schema_path, keyword1, keyword2);
        //delete schemasa
        deleteSchema();
        //assert resources have been proprly deleted
        verifyResourceDelete(schema_path);
        log.info("Multi Tenancy SchemaUploadServiceTestClient - Passed ");
    }
}
