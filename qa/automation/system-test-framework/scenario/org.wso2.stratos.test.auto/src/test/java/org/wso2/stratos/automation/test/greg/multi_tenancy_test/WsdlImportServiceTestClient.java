package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.services.ServiceManager;
import org.wso2.carbon.governance.api.services.dataobjects.Service;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserCreator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import javax.xml.namespace.QName;


public class WsdlImportServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(WsdlImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static WSRegistryServiceClient registry_testUser = null;
    private static WSRegistryServiceClient registry_diffDomainUser1 = null;
    private static Registry governance = null;

    @Override
    public void init() {
        testClassName = WsdlImportServiceTestClient.class.getName();
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
        //delete wsdl
        deleteWsdl();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Multi Tenancy WsdlImportServiceTestClient Test Cases............................ ");
        addWSDL();
        log.info("Completed Running Multi Tenancy WsdlImportServiceTestClient Test Cases.................. ");
    }

    @Override
    public void cleanup() {

    }

    private void createWsdl(String wsdl_url) {
        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl;
        try {
            wsdl = wsdlManager.newWsdl(wsdl_url);
            wsdl.addAttribute("version", "1.0.0");
            wsdl.addAttribute("creator", "Aaaa");
            wsdlManager.addWsdl(wsdl);
            log.info("wsdl was successfully added");
        } catch (GovernanceException e) {
            log.error("createWsdl Exception thrown:" + e.getMessage());
            Assert.fail("createWsdl Exception thrown:" + e.getMessage());
        }
    }

    private void wsdlContentAssertion(String wsdl_path, String keyword1, String keyword2) {
        String content_adminUser;
        String content_testUser;
        String content_diffDomainUser = null;
        try {
            Resource r1;
            r1 = registry.get(wsdl_path);
            content_adminUser = new String((byte[]) r1.getContent());
            //Assert admin user -admin123@wso2manualQA0006.org
            assertTrue("Assert Content wsdl file - key word 1", content_adminUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content wsdl file - key word 2", content_adminUser.indexOf(keyword2) > 0);
            // Assert Test user - testuser1@wso2manualQA0006.org
            Resource r2;
            r2 = registry_testUser.get(wsdl_path);
            content_testUser = new String((byte[]) r2.getContent());
            assertTrue("Assert Content wsdl file - key word 1", content_testUser.indexOf(keyword1) > 0);
            assertTrue("Assert Content wsdl file - key word 2", content_testUser.indexOf(keyword2) > 0);
            //Assert admin user -admin123@wso2manualQA0004.org
            Resource r3 = registry.newResource();

            try {
                assertNotNull("Cannot get WSDL resource by different tenant", registry_diffDomainUser1.get(wsdl_path));

            } catch (RegistryException e) {
                log.debug("Cannot get WSDL resource by different tenant:" + e.getMessage());
            }


            try {
                content_diffDomainUser = new String((byte[]) r3.getContent());
            } catch (Exception e) {
                log.error("Registry Exception thrown from tenant because resource does not exists:" + e.getMessage());

            }
            assertEquals("Assert content does not exists", content_diffDomainUser, null);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    public void checkServiceExsist(String service_namespace, String service_name, String service_path) {
        ServiceManager serviceManager = new ServiceManager(governance);
        Service[] services;
        try {
            services = serviceManager.getAllServices();
            for (Service service : services) {
                if (service.getQName().equals(new QName(service_namespace, service_name))) {
                    //Assert service exisits admin user -admin123@wso2manualQA0006.org
                    assertTrue("Service Exist :", registry.resourceExists(service_path));
                    //Assert service exisits Test user - testuser1@wso2manualQA0006.org
                    assertTrue("Service Exist :", registry_testUser.resourceExists(service_path));
                    //Assert service does not exists admin user -admin123@wso2manualQA0004.org
                    assertFalse("Service does not Exist :", registry_diffDomainUser1.resourceExists(service_path));
                } else {
//                    System.out.println("Service path does not match");
                }
            }
        } catch (GovernanceException e) {
            log.error("checkServiceExsist Exception thrown:" + e.getMessage());
            Assert.fail("checkServiceExsist Exception thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("checkServiceExsist Exception thrown:" + e.getMessage());
            Assert.fail("checkServiceExsist Exception thrown:" + e.getMessage());
        }
    }

    private void deleteWsdl() {
        //delete wsdls
        try {
            registry.delete("/_system/governance/trunk/wsdls");
            registry_testUser.delete("/_system/governance/trunk/wsdls");
            registry_diffDomainUser1.delete("/_system/governance/trunk/wsdls");

            registry.delete("/_system/governance/trunk/services");
            registry_testUser.delete("/_system/governance/trunk/services");
            registry_diffDomainUser1.delete("/_system/governance/trunk/services");
        } catch (RegistryException e) {
            log.error("deleteWsdl RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteWsdl RegistryException thrown:" + e.getMessage());
        }
    }

    private void verifyResourceExists(String wsdl_path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertTrue("wsdl Exists :", registry.resourceExists(wsdl_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertTrue("wsdl exists:", registry_testUser.resourceExists(wsdl_path));
            // Assert differnt doamin user 1
            assertFalse("wsdl exists:", registry_diffDomainUser1.resourceExists(wsdl_path));
        } catch (RegistryException e) {
            log.error("verifyResourceExists RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourceExists RegistryException thrown:" + e.getMessage());
        }
    }

    private void verifyResourcesDeleted(String wsdl_path) {
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            assertFalse("wsdl Exists :", registry.resourceExists(wsdl_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            assertFalse("wsdl exists:", registry_testUser.resourceExists(wsdl_path));
            // Assert differnt doamin user 1
            assertFalse("wsdl exists:", registry_diffDomainUser1.resourceExists(wsdl_path));
        } catch (RegistryException e) {
            log.error("verifyResourcesDeleted( RegistryException thrown:" + e.getMessage());
            Assert.fail("verifyResourcesDeleted( RegistryException thrown:" + e.getMessage());
        }
    }

    public void getAssociationPath(String wsdl_path, String association_path) {
        Association[] associations;
        Association[] associations_testuser;
        Association[] associations_diffDomainUser;
        try {
            //Assert admin user -admin123@wso2manualQA0006.org
            associations = registry.getAssociations(wsdl_path, "usedBy");
            assertTrue("Association Path exsits :", associations[1].getDestinationPath().equalsIgnoreCase(association_path));
            // Assert Test user - testuser1@wso2manualQA0006.org
            associations_testuser = registry_testUser.getAssociations(wsdl_path, "usedBy");
            assertTrue("Association Path exsits :", associations_testuser[1].getDestinationPath().equalsIgnoreCase(association_path));
            //Assert admin user -admin123@wso2manualQA0004.org
            associations_diffDomainUser = registry_diffDomainUser1.getAssociations(wsdl_path, "usedBy");

            if (associations_diffDomainUser.length > 0) {

                assertTrue("Association Path exsits :", associations_diffDomainUser[1].getDestinationPath().equalsIgnoreCase(association_path));
            }
        } catch (RegistryException e) {
            log.error("getAssociationPath Exception thrown:" + e.getMessage());
            Assert.fail("getAssociationPath Exception thrown:" + e.getMessage());
        }
    }


    private void addWSDL() {
        String wsdl_url = "http://geocoder.us/dist/eg/clients/GeoCoder.wsdl";
        String wsdl_path = "/_system/governance/trunk/wsdls/us/geocoder/rpc/geo/coder/us/GeoCoder.wsdl";
        String association_path = "/_system/governance/trunk/services/us/geocoder/rpc/geo/coder/us/GeoCode_Service";
        String service_namespace = "http://rpc.geocoder.us/Geo/Coder/US/";
        String service_name = "GeoCode_Service";
        String service_path = "/_system/governance/trunk/services/us/geocoder/rpc/geo/coder/us/GeoCode_Service";
        String keyword1 = "?xml version=";
        String keyword2 = "ArrayOfGeocoderResult";

        //create wsdl
        createWsdl(wsdl_url);
        // Assert resource exists with differtn users
        verifyResourceExists(wsdl_path);
        //Assert Association path exsist with different users
        getAssociationPath(wsdl_path, association_path);
        //Assert wsdl content
        wsdlContentAssertion(wsdl_path, keyword1, keyword2);
        //Assert Service Exsist
        checkServiceExsist(service_namespace, service_name, service_path);
        //Delete Resources
        deleteWsdl();
        //Assert resources has been deleted properly
        verifyResourcesDeleted(wsdl_path);
        log.info("Multi Tenancy WsdlImportServiceTestClient- Passed");
    }
}
