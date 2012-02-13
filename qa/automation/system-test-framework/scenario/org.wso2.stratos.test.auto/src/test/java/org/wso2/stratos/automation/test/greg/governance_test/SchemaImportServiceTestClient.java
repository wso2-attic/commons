package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.schema.SchemaManager;
import org.wso2.carbon.governance.api.schema.dataobjects.Schema;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.utils.RegistryClientUtils;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class SchemaImportServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(SchemaImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;
    private static Registry remote_registry = null;
    String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;

    @Override
    public void init() {
        testClassName = SchemaImportServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        String registryURL;

        //Tenant Details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        String username = tenantDetails.getTenantName();
        String password = tenantDetails.getTenantPassword();

        if (FrameworkSettings.getStratosTestStatus()) {
           registryURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain() + File.separator + "registry" + File.separator;
       } else {
           if (FrameworkSettings.GREG_SERVER_WEB_CONTEXT_ROOT != null) {
               registryURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + FrameworkSettings.GREG_SERVER_WEB_CONTEXT_ROOT + File.separator + "registry" + File.separator;
               
           } else {
               registryURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + "registry" + File.separator;
              
           }

       }


        try {
            remote_registry = new RemoteRegistry(new URL(registryURL), username, password);
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        } catch (MalformedURLException e) {
            log.error("MalformedURLException e thrown:" + e.getMessage());
            Assert.fail("MalformedURLException e thrown:" + e.getMessage());
        }
        //  Delete Schemas already existing
        deleteSchema();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running SchemaImportServiceTestClient Test Cases............................ ");
        addPatientSchema();
        addBookSchema();
        addErrorResolutionSchema();
        addListing3Schema();
        addListing4Schema();
        addPurchasingSchema();
//        //Upload schema from a file
        addSchemafromFile();
        log.info("Completed Running SchemaImportServiceTestClient Test Cases................... ");
    }

    //    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }


    public void createSchema(Registry governance, String schema_url) {
        SchemaManager schemaManager = new SchemaManager(governance);
        Schema schema = null;
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

    public void deleteSchema() {
        try {
            if (registry.resourceExists("/_system/governance/trunk/schemas")) {
                registry.delete("/_system/governance/trunk/schemas");
            }
        } catch (RegistryException e) {
            log.error("deleteSchema Exception thrown:" + e.getMessage());
        }
    }


    public void propertyAssertion(String schema_path, String property1, String property2, String property3) {
        Resource resource = null;
        try {
            resource = registry.get(schema_path);
            assertEquals("Schema Property - Schema Validation", resource.getProperty("Schema Validation"), property1);
            assertEquals("Schema Property - targetNamespace", resource.getProperty("targetNamespace"), property2);
            assertEquals("Schema Property - Creator", resource.getProperty("creator"), property3);
        } catch (RegistryException e) {
            log.error("propertyAssertion Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion Exception thrown:" + e.getMessage());
        }
    }

    public void schemaContentAssertion(String schema_path, String keyword1, String keyword2) {
        String content = null;
        try {
            Resource r1 = registry.newResource();
            r1 = registry.get(schema_path);
            content = new String((byte[]) r1.getContent());
            assertTrue("Assert Content Schema file - key word 1", content.indexOf(keyword1) > 0);
            assertTrue("Assert Content Schema file - key word 2", content.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addPatientSchema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/Patient.xsd";
        String schema_path = "/_system/governance/trunk/schemas/org/ihc/xsd/Patient.xsd";
        String property1 = "Valid";
        String property2 = "http://ihc.org/xsd?patient";
        String property3 = "Aaaa";
        String keyword1 = "attributeFormDefault";
        String keyword2 = "languageCommunication";


        try {
            //Add Schema
            createSchema(governance, schema_url);
            //Assert Schema exist
            assertTrue("Patient Schema exist ", registry.resourceExists(schema_path));
            //Assert Properties
            propertyAssertion(schema_path, property1, property2, property3);
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Remove Schema
            registry.delete(schema_path);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, registry.resourceExists(schema_path));
        } catch (RegistryException e) {
            log.error("Add patient schema failed :" + e.getMessage());
            Assert.fail("Add patient Exception thrown:" + e.getMessage());
        }
    }

    private void addBookSchema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/books.xsd";
        String schema_path = "/_system/governance/trunk/schemas/books/books.xsd";
        String property1 = "Valid";
        String property2 = "urn:books";
        String property3 = "Aaaa";
        String keyword1 = "bks:BookForm";
        String keyword2 = "author";
        try {
            //Add Schema
            createSchema(governance, schema_url);
            //Assert Schema exist
            assertTrue("Book Schema exist ", registry.resourceExists(schema_path));
            //Assert Properties
            propertyAssertion(schema_path, property1, property2, property3);
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Remove Schema
            registry.delete(schema_path);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, registry.resourceExists(schema_path));
        } catch (RegistryException e) {
            log.error("Add Book schema failed :" + e.getMessage());
            Assert.fail("Add Book Exception thrown:" + e.getMessage());
        }
    }

    private void addErrorResolutionSchema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/ErrorResolution.xsd";
        String schema_path = "/_system/governance/trunk/schemas/org/ihc/xsd/ErrorResolution.xsd";
        String property1 = "Valid";
        String property2 = "http://ihc.org/xsd?ErrorResolution";
        String property3 = "Aaaa";
        String keyword1 = "WARNING";
        String keyword2 = "CRITICAL";

        try {
            //Add Schema
            createSchema(governance, schema_url);
            //Assert Schema exist
            assertTrue("ErrorResolution Schema exist ", registry.resourceExists(schema_path));
            //Assert Properties
            propertyAssertion(schema_path, property1, property2, property3);
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Remove Schema
            registry.delete(schema_path);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, registry.resourceExists(schema_path));

        } catch (GovernanceException e) {
            log.error("Add Error Resolution schema failed :" + e.getMessage());
            Assert.fail("Add Error Resolution Exception thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("Add Error Resolution schema failed :" + e.getMessage());
            Assert.fail("Add Error Resolution Exception thrown:" + e.getMessage());
        }
    }

    private void addListing3Schema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/listing3.xsd";
        String schema_path = "/_system/governance/trunk/schemas/listing3/listing3.xsd";
        String schema_path2 = "/_system/governance/trunk/schemas/listing4/listing4.xsd";
        String property1 = "Valid";
        String property2 = "urn:listing3";
        String property3 = "Aaaa";
        String keyword1 = "areaCode";
        String keyword2 = "exchange";

        try {
            //Add Schema
            createSchema(governance, schema_url);
            //Assert Schema exist
            assertTrue("Listing3 Schema exist ", registry.resourceExists(schema_path));
            assertTrue("Listing4 Schema exist ", registry.resourceExists(schema_path2));
            //Assert Properties
            propertyAssertion(schema_path, property1, property2, property3);
            //Assert Association
            Association[] associations = registry.getAllAssociations(schema_path2);
            assertTrue("Association Exsists", associations[1].getDestinationPath().equalsIgnoreCase(schema_path));
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Remove Registry
            registry.delete(schema_path);
            registry.delete(schema_path2);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, registry.resourceExists(schema_path));
        } catch (GovernanceException e) {
            log.error("Add Listing3 schema failed :" + e.getMessage());
            Assert.fail("Add Listing3 Exception thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("Add Listing3 schema failed :" + e.getMessage());
            Assert.fail("Add Listing3 Exception thrown:" + e.getMessage());
        }
    }

    private void addListing4Schema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/listing4.xsd";
        String schema_path = "/_system/governance/trunk/schemas/listing4/listing4.xsd";
        String property1 = "Valid";
        String property2 = "urn:listing4";
        String property3 = "Aaaa";
        String keyword1 = "areaCode2";
        String keyword2 = "exchange2";

        try {
            //Add Schema
            createSchema(governance, schema_url);
            //Assert Schema exist
            assertTrue("Listing4 Schema exist ", registry.resourceExists(schema_path));
            //Assert Properties
            propertyAssertion(schema_path, property1, property2, property3);
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Remove Registry
            registry.delete(schema_path);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, registry.resourceExists(schema_path));
        } catch (GovernanceException e) {
            log.error("Add Listing4 schema failed :" + e.getMessage());
            Assert.fail("Add Listing4 Exception thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("Add Listing4 schema failed :" + e.getMessage());
            Assert.fail("Add Listing4 Exception thrown:" + e.getMessage());
        }
    }

    private void addPurchasingSchema() {
        String schema_url = "http://people.wso2.com/~evanthika/schemas/purchasing.xsd";
        String schema_path = "/_system/governance/trunk/schemas/org/bar/purchasing/purchasing.xsd";
        String property1 = "Valid";
        String property2 = "http://bar.org/purchasing";
        String property3 = "Aaaa";
        String keyword1 = "productQueryResult";
        String keyword2 = "invalidProductId";

        try {
            //Add Schema
            createSchema(governance, schema_url);
            //Assert Schema exist
            assertTrue("Purchasing Schema exist ", registry.resourceExists(schema_path));
            //Assert Properties
            propertyAssertion(schema_path, property1, property2, property3);
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Remove Registry
            registry.delete(schema_path);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, registry.resourceExists(schema_path));
        } catch (GovernanceException e) {
            log.error("Add Purchasing schema failed :" + e.getMessage());
            Assert.fail("Add Purchasing Exception thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("Add Purchasing schema failed :" + e.getMessage());
            Assert.fail("Add Purchasing Exception thrown:" + e.getMessage());
        }
    }

    private void addSchemafromFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "Person.xsd";
        String toPath = "/_system/governance/trunk/schemas/";
        String schema_path = "/_system/governance/trunk/schemas/Person.xsd";
        String keyword1 = "Name";
        String keyword2 = "SSN";
        File file = new File(filePath);

        try {
            //Upload Schema from file
            RegistryClientUtils.importToRegistry(file, toPath, remote_registry);
            //Assert Resource Exists
            assertTrue("Person Schema exist ", remote_registry.resourceExists(schema_path));
            //Assert Schema content
            schemaContentAssertion(schema_path, keyword1, keyword2);
            //Delete Schema
            remote_registry.delete(schema_path);
            //Assert Resource was deleted successfully
            assertFalse("Schema exists at " + schema_path, remote_registry.resourceExists(schema_path));
        } catch (RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }
}
