package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.registry.api.Collection;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.io.*;


public class GarfileUploadServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(GarfileUploadServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static WSRegistryServiceClient registry_testUser = null;
    private static WSRegistryServiceClient registry_diffDomainUser1 = null;
    String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;

    @Override
    public void init() {
        testClassName = GarfileUploadServiceTestClient.class.getName();
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

        //delete Resources
        deleteWsdl();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Multi Tenancy GarfileUploadServiceTestClient Test Cases............................ ");
        addAxis2ServiceGarFile();
        log.info("Completed Running Multi Tenancy GarfileUploadServiceTestClient Test Cases...................");
    }

    @Override
    public void cleanup() {

    }

    private void deleteWsdl() {
        try {
            //delete wsdls
            registry.delete("/_system/governance/trunk/wsdls");
            registry_testUser.delete("/_system/governance/trunk/wsdls");
            registry_diffDomainUser1.delete("/_system/governance/trunk/wsdls");
            //delete services
            registry.delete("/_system/governance/trunk/services");
            registry_testUser.delete("/_system/governance/trunk/services");
            registry_diffDomainUser1.delete("/_system/governance/trunk/services");
            //delete schemas
            registry.delete("/_system/governance/trunk/schemas");
            registry_testUser.delete("/_system/governance/trunk/schemas");
            registry_diffDomainUser1.delete("/_system/governance/trunk/schemas");
            registry.delete("/a1");
        } catch (RegistryException e) {
            log.error("deleteWsdl RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteWsdl RegistryException thrown:" + e.getMessage());
        }
    }

    private void uploadGarFile(String filePath) {
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
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("uploadGarFile RegistryException thrown:" + e.getMessage());
            Assert.fail("uploadGarFile RegistryException thrown:" + e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("uploadGarFile FileNotFoundException thrown:" + e.getMessage());
            Assert.fail("uploadGarFile FileNotFoundException thrown:" + e.getMessage());
        }
    }

    private void verifyResourceExists(String artifact) {
        try {
            //  admin user admin123@wso2manualQA0006.org
            assertTrue("WSDL Exists :", registry.resourceExists(artifact));
            //  test user testuser123@wso2manualQA0006.org
            assertTrue("WSDL Exists :", registry_testUser.resourceExists(artifact));
            // admin user - admin123@wso2autoQA0008.org
            assertFalse("WSDL Exists :", registry_diffDomainUser1.resourceExists(artifact));
        } catch (RegistryException e) {
            log.error("verifyResourceExists RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists RegistryException thrown:" + e.getMessage());
        }
    }

    private void verifyResourceDeleted(String artifact) {
        try {
            //  admin user admin123@wso2manualQA0006.org
            assertFalse("WSDL Exists :", registry.resourceExists(artifact));
            //  test user testuser123@wso2manualQA0006.org
            assertFalse("WSDL Exists :", registry_testUser.resourceExists(artifact));
            // admin user - admin123@wso2autoQA0008.org
            assertFalse("WSDL Exists :", registry_diffDomainUser1.resourceExists(artifact));
        } catch (RegistryException e) {
            log.error("verifyResourceDeleted RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourceDeleted RegistryException thrown:" + e.getMessage());
        }
    }


    private void addAxis2ServiceGarFile() {
        String filePath = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator + "Axis2Service.gar";
        String service_path = "/_system/governance/trunk/services/org/wso2/carbon/service/Axis2Service";
        String wsdl_path = "/_system/governance/trunk/wsdls/org/wso2/carbon/service/Axis2Service.wsdl";
        String schema_path = "/_system/governance/trunk/schemas/org/wso2/carbon/service/axis2serviceschema.xsd";

        //upload Gar file
        uploadGarFile(filePath);
        //assert wsdl exists
        verifyResourceExists(wsdl_path);
        // assert services exists
        verifyResourceExists(service_path);
        // assert schema exists
        verifyResourceExists(schema_path);
        //Delete resources
        deleteWsdl();
        //assert wsdl was deleted
        verifyResourceDeleted(wsdl_path);
        //assert services was deleted
        verifyResourceDeleted(service_path);
        //assert schema was deleted
        verifyResourceDeleted(schema_path);
        log.info("Multi Tenancy GarfileUploadServiceTestClient - Passed ");
    }
}
