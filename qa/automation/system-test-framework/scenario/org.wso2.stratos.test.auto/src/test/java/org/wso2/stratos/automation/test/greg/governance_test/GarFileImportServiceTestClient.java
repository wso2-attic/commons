package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.registry.api.Collection;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.io.*;

public class GarFileImportServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(SchemaImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;

    @Override
    public void init() {
        testClassName = GarFileImportServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);

        //Delete existing resources
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running GarFileImportServiceTestClient Test Cases............................ ");
        addAxis2ServiceGarFile();
        addSimpleStockQuoteServiceGarFile();
        addEnterprisePersonServiceGarFile();
        addRegistryGarFile();
//        addOOpsGarFile();
        addXSDAllGarFile();
        addHeterogeneousNamespaceGarFile();
        addArtistRegistryGarFile();
        addOriginalWSDLGarFile();
        addHomogeneousNamespaceGarFile();
        log.info("Completed Running GarFileImportServiceTestClient Test Cases...................");
    }


    @Override
    public void cleanup() {

    }


    private void removeResource() {
        deleteResources("/_system/governance/trunk/services");
        deleteResources("/_system/governance/trunk/wsdls");
        deleteResources("/_system/governance/trunk/schemas");
        deleteResources("/a1");

    }

    public void deleteResources(String resourceName) {
        try {
            if (registry.resourceExists(resourceName)) {
                registry.delete(resourceName);
            }
        } catch (RegistryException e) {
            log.error("deleteResources RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteResources RegistryException thrown:" + e.getMessage());
        }

    }

    private void addAxis2ServiceGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "Axis2Service.gar";
        String service_path = "/_system/governance/trunk/services/org/wso2/carbon/service/Axis2Service";
        String wsdl_path = "/_system/governance/trunk/wsdls/org/wso2/carbon/service/Axis2Service.wsdl";
        String schema_path = "/_system/governance/trunk/schemas/org/wso2/carbon/service/axis2serviceschema.xsd";

        try {
            // Create Collection
            Collection collection = registry.newCollection();
            registry.put("/a1/a2/a3", collection);
            //Create Resource
            Resource r1 = registry.newResource();
            //create an Input Stream
            InputStream is = new BufferedInputStream(new FileInputStream(filePath));
            r1.setContentStream(is);
            r1.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r1", r1);
            //Assert Service exists
            assertTrue("Service Exists :", registry.resourceExists(service_path));
            //Assert WSDL exists
            assertTrue("WSDL Exists :", registry.resourceExists(wsdl_path));
            //Assert SCHEMA exists
            assertTrue("Schema Exists :", registry.resourceExists(schema_path));
            //delete resources
            deleteResources(service_path);
            deleteResources(wsdl_path);
            deleteResources(schema_path);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("Service Deleted :", registry.resourceExists(service_path));
            assertFalse("WSDL Deleted :", registry.resourceExists(wsdl_path));
            assertFalse("Schema Deleted :", registry.resourceExists(schema_path));
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addAxis2ServiceGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addAxis2ServiceGarFile RegistryException thrown:" + e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("addAxis2ServiceGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addAxis2ServiceGarFile FileNotFoundException  thrown:" + e.getMessage());
        }
    }

    private void addSimpleStockQuoteServiceGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "SimpleStockQuoteService.gar";
        String service_path = "/_system/governance/trunk/services/samples/services/SimpleStockQuoteService";
        String wsdl_path = "/_system/governance/trunk/wsdls/samples/services/SimpleStockQuoteService.wsdl";

        try {
            //Create Resource
            Resource r2 = registry.newResource();
            //create an Input Stream                                               
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r2.setContentStream(is1);
            r2.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r2", r2);
            //Assert Service exists
            assertTrue("Service Exists :", registry.resourceExists(service_path));
            //Assert WSDL exists
            assertTrue("WSDL Exists :", registry.resourceExists(wsdl_path));
            //Delete Resources
            deleteResources(service_path);
            deleteResources(wsdl_path);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("Service Deleted :", registry.resourceExists(service_path));
            assertFalse("WSDL Deleted :", registry.resourceExists(wsdl_path));
        } catch (FileNotFoundException e) {
            log.error("addSimpleStockQuoteServiceGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addSimpleStockQuoteServiceGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addSimpleStockQuoteServiceGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addSimpleStockQuoteServiceGarFile RegistryException thrown:" + e.getMessage());
        }
    }

    private void addEnterprisePersonServiceGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "wsdl_arch_with_imports_folder.gar";
        String service_path = "/_system/governance/trunk/services/org/ihc/services/enterprisepersonservice/EnterprisePersonService";
        String wsdl_path = "/_system/governance/trunk/wsdls/org/ihc/services/enterprisepersonservice/EnterprisePersonService.wsdl";

        try {
            //Create Resource
            Resource r3 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r3.setContentStream(is1);
            r3.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r3", r3);
            //Assert Service exists
            assertTrue("Service Exists :", registry.resourceExists(service_path));
            //Assert WSDL exists
            assertTrue("WSDL Exists :", registry.resourceExists(wsdl_path));
            //Delete Resources
            deleteResources(service_path);
            deleteResources(wsdl_path);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("Service Deleted :", registry.resourceExists(service_path));
            assertFalse("WSDL Deleted :", registry.resourceExists(wsdl_path));
        } catch (FileNotFoundException e) {
            log.error("addEnterprisePersonServiceGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addEnterprisePersonServiceGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addEnterprisePersonServiceGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addEnterprisePersonServiceGarFile RegistryException thrown:" + e.getMessage());
        }
    }

    private void addRegistryGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "registry.gar";
        String schema_path1 = "/_system/governance/trunk/schemas/dk/dr/www/namespaces/schemas/common/types/types.xsd";
        String schema_path2 = "/_system/governance/trunk/schemas/org/ihc/xsd/ErrorResolution.xsd";
        String schema_path3 = "/_system/governance/trunk/schemas/org/ihc/xsd/Patient.xsd";
        try {
            //Create Resource
            Resource r4 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r4.setContentStream(is1);
            r4.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r4", r4);
            //Assert Schema exists
            assertTrue("Schema Exists1 :", registry.resourceExists(schema_path1));
            assertTrue("Schema Exists2 :", registry.resourceExists(schema_path2));
            assertTrue("Schema Exists3 :", registry.resourceExists(schema_path3));
            //Delete Resources
            deleteResources(schema_path1);
            deleteResources(schema_path2);
            deleteResources(schema_path3);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("schema_path1 Deleted :", registry.resourceExists(schema_path1));
            assertFalse("schema_path2 Deleted :", registry.resourceExists(schema_path2));
            assertFalse("schema_path3 Deleted :", registry.resourceExists(schema_path3));
        } catch (FileNotFoundException e) {
            log.error("addRegistryGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addRegistryGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addRegistryGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addRegistryGarFile RegistryException thrown:" + e.getMessage());
        }
    }

    private void addOOpsGarFile() {

        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GReg" + File.separator + "opps.gar";


        String wsdl_path = "/_system/governance/trunk/wsdls/org/epo/ops/wsdl/ops.wsdl";
        String schema_path1 = "/_system/governance/trunk/schemas/org/epo/www/exchange/exchange-documents-v2.0.xsd";
        String schema_path2 = "/_system/governance/trunk/schemas/org/epo/www/fulltext/fulltext-documents.xsd";
        String schema_path3 = "/_system/governance/trunk/schemas/org/epo/ops/ops.xsd";
        String schema_path4 = "/_system/governance/trunk/schemas/org/epo/ops/ops_legal.xsd";
        String schema_path5 = "/_system/governance/trunk/schemas/org/w3/www/xml/_1998/namespace/xml.xsd";
        String service_path1 = "/_system/governance/trunk/services/org/epo/ops/wsdl/OPSBiblioRetrievalService";
        String service_path2 = "/_system/governance/trunk/services/org/epo/ops/wsdl/OPSNumberService";

        try {
            //Create Resource
            Resource r5 = registry.newResource();

            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));

            r5.setContentStream(is1);
            r5.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r5", r5);

            Thread.sleep(250000);

            //Assert Schema exists
            assertTrue("ServiceL Exists1 :", registry.resourceExists(service_path1));
            assertTrue("Service Exists1 :", registry.resourceExists(service_path2));
            assertTrue("WSDL Exists1 :", registry.resourceExists(wsdl_path));
            assertTrue("Schema Exists2 :", registry.resourceExists(schema_path1));
            assertTrue("Schema Exists3 :", registry.resourceExists(schema_path2));
            assertTrue("Schema Exists1 :", registry.resourceExists(schema_path3));
            assertTrue("Schema Exists2 :", registry.resourceExists(schema_path4));
            assertTrue("Schema Exists3 :", registry.resourceExists(schema_path5));

            //Delete Resources
            deleteResources(schema_path1);
            deleteResources(schema_path2);
            deleteResources(schema_path3);
            deleteResources(schema_path4);
            deleteResources(schema_path5);
            deleteResources(wsdl_path);
            deleteResources("/a1");

            //Assert Resources have been properly deleted
            assertFalse("schema_path1 Deleted :", registry.resourceExists(schema_path1));
            assertFalse("schema_path2 Deleted :", registry.resourceExists(schema_path2));
            assertFalse("schema_path3 Deleted :", registry.resourceExists(schema_path3));
            assertFalse("schema_path4 Deleted :", registry.resourceExists(schema_path4));
            assertFalse("schema_path5 Deleted :", registry.resourceExists(schema_path5));
            assertFalse("WSDL Deleted :", registry.resourceExists(wsdl_path));


        } catch (FileNotFoundException e) {
            log.error("addRegistryGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addRegistryGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addRegistryGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addRegistryGarFile RegistryException thrown:" + e.getMessage());
        } catch (InterruptedException e) {
            log.error("addRegistryGarFile InterruptedException thrown:" + e.getMessage());
            Assert.fail("addRegistryGarFile InterruptedException thrown:" + e.getMessage());
        }

    }


    private void addXSDAllGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "xsdAll.gar";
        String schema_path1 = "/_system/governance/trunk/schemas/org/datacontract/schemas/_2004/_07/system/test1.xsd";
        String schema_path2 = "/_system/governance/trunk/schemas/com/microsoft/schemas/_2003/_10/serialization/test2.xsd";
        String schema_path3 = "/_system/governance/trunk/schemas/org/tempuri/test3.xsd";
        String schema_path4 = "/_system/governance/trunk/schemas/com/microsoft/schemas/_2003/_10/serialization/test4.xsd";

        try {
            //Create Resource
            Resource r6 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r6.setContentStream(is1);
            r6.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r6", r6);
            //Assert Schema exists
            assertTrue("Schema Exists1 :", registry.resourceExists(schema_path1));
            assertTrue("Schema Exists2 :", registry.resourceExists(schema_path2));
            assertTrue("Schema Exists3 :", registry.resourceExists(schema_path3));
            assertTrue("Schema Exists4 :", registry.resourceExists(schema_path4));
            //Delete Resources
            deleteResources(schema_path1);
            deleteResources(schema_path2);
            deleteResources(schema_path3);
            deleteResources(schema_path4);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("schema_path1 Deleted :", registry.resourceExists(schema_path1));
            assertFalse("schema_path2 Deleted :", registry.resourceExists(schema_path2));
            assertFalse("schema_path3 Deleted :", registry.resourceExists(schema_path3));
            assertFalse("schema_path3 Deleted :", registry.resourceExists(schema_path4));
        } catch (FileNotFoundException e) {
            log.error("addXSDAllGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addXSDAllGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addXSDAllGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addXSDAllGarFile RegistryException thrown:" + e.getMessage());
        }
    }

    private void addHeterogeneousNamespaceGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "Heterogeneous_Namespace.gar";
        String schema_path1 = "/_system/governance/trunk/schemas/org/company/www/Company.xsd";
        String schema_path2 = "/_system/governance/trunk/schemas/org/person/www/Person.xsd";
        String schema_path3 = "/_system/governance/trunk/schemas/org/product/www/Product.xsd";

        try {
            //Create Resource
            Resource r7 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r7.setContentStream(is1);
            r7.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r7", r7);
            //Assert Schema exists
            assertTrue("Schema Exists1 :", registry.resourceExists(schema_path1));
            assertTrue("Schema Exists2 :", registry.resourceExists(schema_path2));
            assertTrue("Schema Exists3 :", registry.resourceExists(schema_path3));
            //Delete Resources
            deleteResources(schema_path1);
            deleteResources(schema_path2);
            deleteResources(schema_path3);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("schema_path1 Deleted :", registry.resourceExists(schema_path1));
            assertFalse("schema_path2 Deleted :", registry.resourceExists(schema_path2));
            assertFalse("schema_path3 Deleted :", registry.resourceExists(schema_path3));
        } catch (FileNotFoundException e) {
            log.error("addHeterogeneousNamespaceGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addHeterogeneousNamespaceGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addHeterogeneousNamespaceGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addHeterogeneousNamespaceGarFile RegistryException thrown:" + e.getMessage());
        }
    }

    private void addArtistRegistryGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "artistRegistry.gar";
        String service_path = "/_system/governance/trunk/services/eu/yesso/soamoa/samples/ArtistRegistryService";
        String wsdl_path = "/_system/governance/trunk/wsdls/eu/yesso/soamoa/samples/artistRegistry.wsdl";
        String schema_path = "/_system/governance/trunk/schemas/eu/yesso/soamoa/samples/artistRegistry.xsd";

        try {
            //Create Resource
            Resource r8 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r8.setContentStream(is1);
            r8.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r8", r8);
            //Assert Schema exists
            assertTrue("service_path Exists1 :", registry.resourceExists(service_path));
            assertTrue("wsdl_path  Exists2 :", registry.resourceExists(wsdl_path));
            assertTrue("Schema Exists3 :", registry.resourceExists(schema_path));
            //Delete Resources
            deleteResources(service_path);
            deleteResources(wsdl_path);
            deleteResources(schema_path);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("service_path Deleted :", registry.resourceExists(service_path));
            assertFalse("wsdl_path  Deleted :", registry.resourceExists(wsdl_path));
            assertFalse("schema_path Deleted :", registry.resourceExists(schema_path));
        } catch (FileNotFoundException e) {
            log.error("addArtistRegistry FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addArtistRegistry FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addArtistRegistry RegistryException thrown:" + e.getMessage());
            Assert.fail("addArtistRegistry RegistryException thrown:" + e.getMessage());
        }
    }

    private void addOriginalWSDLGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "original-wsdl.gar";
        String service_path1 = "/_system/governance/trunk/services/com/konakart/ws/KKWSEngIfService";
        String service_path2 = "/_system/governance/trunk/services/net/ecubicle/www/YouTubeDownloader";
        String wsdl_path1 = "/_system/governance/trunk/wsdls/com/konakart/ws/KKWebServiceEng.wsdl";
        String wsdl_path2 = "/_system/governance/trunk/wsdls/net/ecubicle/www/youtubedownloader.asmx.wsdl";

        try {
            //Create Resource
            Resource r9 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r9.setContentStream(is1);
            r9.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r9", r9);
            //Assert Schema exists
            assertTrue("Service1 Exists1 :", registry.resourceExists(service_path1));
            assertTrue("Service2 Exists1 :", registry.resourceExists(service_path2));
            assertTrue("WSDL1 Exists2 :", registry.resourceExists(wsdl_path1));
            assertTrue("WSDL2 Exists2 :", registry.resourceExists(wsdl_path2));
            //Delete Resources
            deleteResources(service_path1);
            deleteResources(service_path2);
            deleteResources(wsdl_path1);
            deleteResources(wsdl_path2);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("Service1 Deleted :", registry.resourceExists(service_path1));
            assertFalse("Service2 Deleted :", registry.resourceExists(service_path2));
            assertFalse("WSDL1 Deleted :", registry.resourceExists(wsdl_path1));
            assertFalse("WSDL2 Deleted :", registry.resourceExists(wsdl_path2));
        } catch (FileNotFoundException e) {
            log.error("addOriginalWSDLGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addOriginalWSDLGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addOriginalWSDLGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addOriginalWSDLGarFile RegistryException thrown:" + e.getMessage());
        }
    }

    private void addHomogeneousNamespaceGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "Homogeneous-Namespace.gar";
        String schema_path1 = "/_system/governance/trunk/schemas/org/company/www/Company.xsd";
        String schema_path2 = "/_system/governance/trunk/schemas/org/company/www/Person.xsd";
        String schema_path3 = "/_system/governance/trunk/schemas/org/company/www/Product.xsd";

        try {
            //Create Resource
            Resource r10 = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            r10.setContentStream(is1);
            r10.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/a1/a2/a3/r10", r10);
            //Assert Schema exists
            assertTrue("Schema1 Exists :", registry.resourceExists(schema_path1));
            assertTrue("Schema1 Exists :", registry.resourceExists(schema_path2));
            assertTrue("Schema1 Exists :", registry.resourceExists(schema_path3));
            //Delete Resources
            deleteResources(schema_path1);
            deleteResources(schema_path2);
            deleteResources(schema_path3);
            deleteResources("/a1");
            //Assert Resources have been properly deleted
            assertFalse("Schema1 Deleted :", registry.resourceExists(schema_path1));
            assertFalse("Schema2 Deleted :", registry.resourceExists(schema_path2));
            assertFalse("Schema3 Deleted :", registry.resourceExists(schema_path3));
        } catch (FileNotFoundException e) {
            log.error("addHomogeneousNamespaceGarFile FileNotFoundException  thrown:" + e.getMessage());
            Assert.fail("addHomogeneousNamespaceGarFile FileNotFoundException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("addHomogeneousNamespaceGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("addHomogeneousNamespaceGarFile RegistryException thrown:" + e.getMessage());
        }
    }
}
