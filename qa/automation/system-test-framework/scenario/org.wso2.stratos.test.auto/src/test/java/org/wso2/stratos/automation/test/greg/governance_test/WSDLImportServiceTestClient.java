package org.wso2.stratos.automation.test.greg.governance_test;

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
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import javax.xml.namespace.QName;


public class WSDLImportServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(WSDLImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;

    @Override
    public void init() {
        testClassName = WSDLImportServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        //Delete WSDL which exists
        deleteWSDL();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WSDLImportServiceTestClient Test Cases............................ ");
        simpleWsdl_Import();
        uppercase_WSDL_extension();
//        largeWSDL_import();
        log.info("Completed Running WSDLImportServiceTestClient Test Cases...................");
    }

    @Override
    public void cleanup() {
    }


    public void createWsdl(String wsdl_url) {
        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl = null;
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

    public void deleteWSDL() {
        try {
            if (registry.resourceExists("/_system/governance/trunk/wsdls")) {
                registry.delete("/_system/governance/trunk/wsdls");
            }
        } catch (RegistryException e) {
            log.error("deleteWSDL Exception thrown:" + e.getMessage());
        }
    }

    public void propertyAssertion(String wsdl_path, String property1, String property2, String property3) {
        //Assert Property
        Resource resource = null;
        try {
            resource = registry.get(wsdl_path);
            assertEquals("WSDL Property - WSDL Validation", resource.getProperty("WSDL Validation"), property1);
            assertEquals("WSDL Property - WSI Validation", resource.getProperty("WSI Validation"), property2);
            assertEquals("WSDL Property - WSI creator", resource.getProperty("creator"), property3);
        } catch (RegistryException e) {
            log.error("propertyAssertion Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion Exception thrown:" + e.getMessage());
        }
    }

    public void wsdlContentAssertion(String wsdl_path, String keyword1, String keyword2) {
        String content = null;
        try {
            Resource r1 = registry.newResource();
            r1 = registry.get(wsdl_path);
            content = new String((byte[]) r1.getContent());

            assertTrue("Assert Content wsdl file - key word 1", content.indexOf(keyword1) > 0);
            assertTrue("Assert Content wsdl file - key word 2", content.indexOf(keyword2) > 0);
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("Registry Exception thrown:" + e.getMessage());
            Assert.fail("Registry Exception thrown:" + e.getMessage());
        }
    }

    public void getAssociationPath(String wsdl_path, String association_path) {
        Association[] associations = new Association[0];
        try {
            associations = registry.getAssociations(wsdl_path, "usedBy");
            assertTrue("Association Path exsits :", associations[1].getDestinationPath().equalsIgnoreCase(association_path));
        } catch (RegistryException e) {
            log.error("getAssociationPath Exception thrown:" + e.getMessage());
            Assert.fail("getAssociationPath Exception thrown:" + e.getMessage());
        }
    }

    public void checkServiceExsist(String service_namespace, String service_name, String service_path) {
        ServiceManager serviceManager = new ServiceManager(governance);
        Service[] services = new Service[0];
        try {
            services = serviceManager.getAllServices();
            for (Service service : services) {
                if (service.getQName().equals(new QName(service_namespace, service_name))) {
                    assertTrue("Service Exist :", registry.resourceExists(service_path));
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

    private void simpleWsdl_Import() {
        String wsdl_url = "http://geocoder.us/dist/eg/clients/GeoCoder.wsdl";
        String wsdl_path = "/_system/governance/trunk/wsdls/us/geocoder/rpc/geo/coder/us/GeoCoder.wsdl";
        String association_path = "/_system/governance/trunk/services/us/geocoder/rpc/geo/coder/us/GeoCode_Service";
        String service_namespace = "http://rpc.geocoder.us/Geo/Coder/US/";
        String service_name = "GeoCode_Service";
        String service_path = "/_system/governance/trunk/services/us/geocoder/rpc/geo/coder/us/GeoCode_Service";
        String property1 = "Invalid";
        String property2 = "Invalid";
        String property3 = "Aaaa";
        String keyword1 = "?xml version=";
        String keyword2 = "ArrayOfGeocoderResult";

        // Import wsdl Geocoder
        createWsdl(wsdl_url);
        try {
            // Assert Resource exsists
            assertTrue("Simple WSDL file exists", registry.resourceExists(wsdl_path));
            //Assert Properties
            propertyAssertion(wsdl_path, property1, property2, property3);
            //Assert Association path exsist
            getAssociationPath(wsdl_path, association_path);
            //Assert wsdl content
            wsdlContentAssertion(wsdl_path, keyword1, keyword2);
            //Assert Service Exsist
            checkServiceExsist(service_namespace, service_name, service_path);
            //Remove wsdl
            registry.delete(wsdl_path);
            //Remove service
            registry.delete(service_path);
            //Assert wsdl was Removed successfully
            assertFalse("Simple WSDL file exists", registry.resourceExists(wsdl_path));
        } catch (RegistryException e) {
            log.error("simpleWsdl_Import Exception thrown:" + e.getMessage());
            Assert.fail("simpleWsdl_Import Exception thrown:" + e.getMessage());
        }


    }

    private void uppercase_WSDL_extension() {
        String wsdl_url = "http://ws.strikeiron.com/donotcall2_5?WSDL";
        String wsdl_path = "/_system/governance/trunk/wsdls/com/strikeiron/www/donotcall2_5.wsdl";
        String association_path = "/_system/governance/trunk/services/com/strikeiron/www/DoNotCallRegistry";
        String service_namespace = "http://www.strikeiron.com";
        String service_name = "DoNotCallRegistry";
        String service_path = "/_system/governance/trunk/services/com/strikeiron/www/DoNotCallRegistry";
        String property1 = "Valid";
        String property2 = "Invalid";
        String property3 = "Aaaa";
        String keyword1 = "?xml version=";
        String keyword2 = "wsdl:definitions targetNamespace";

        //import wsdl file
        createWsdl(wsdl_url);

        try {
            // Assert Resource exsists
            assertTrue("WSDL which ends with uppercase WSDL extension exists", registry.resourceExists(wsdl_path));
            //Assert Properties
            propertyAssertion(wsdl_path, property1, property2, property3);
            //Assert Association path exsist
            getAssociationPath(wsdl_path, association_path);
            //Assert wsdl content
            wsdlContentAssertion(wsdl_path, keyword1, keyword2);
            //Assert Service Exsist
            checkServiceExsist(service_namespace, service_name, service_path);
            //Remove wsdl
            registry.delete(wsdl_path);
            //Remove service
            registry.delete(service_path);
            //Assert wsdl was Removed successfully
            assertFalse("WSDL which ends with uppercase WSDL extension exists", registry.resourceExists(wsdl_path));
        } catch (RegistryException e) {
            log.error("uppercase_WSDL_extension Exception thrown:" + e.getMessage());
            Assert.fail("uppercase_WSDL_extension Exception thrown:" + e.getMessage());
        }
    }

    private void largeWSDL_import() {
        String wsdl_url = "http://developer.ebay.com/webservices/latest/eBaySvc.wsdl";
        String wsdl_path = "/_system/governance/trunk/wsdls/eblbasecomponents/apis/ebay/eBaySvc.wsdl";
        String association_path = "/_system/governance/trunk/services/eblbasecomponents/apis/ebay/eBayAPIInterfaceService";
        String service_namespace = "urn:ebay:apis:eBLBaseComponents";
        String service_name = "eBayAPIInterfaceService";
        String service_path = "/_system/governance/trunk/services/eblbasecomponents/apis/ebay/eBayAPIInterfaceService";
        String property1 = "Valid";
        String property2 = "Valid";
        String property3 = "Aaaa";
        String keyword1 = "?xml version=";
        String keyword2 = "wsdl:definitions targetNamespace";


        //Create wsdl
        createWsdl(wsdl_url);

        try {
            // Assert Resource exsists
            assertTrue("largest WSDL exists", registry.resourceExists(wsdl_path));
            //Assert Properties
            propertyAssertion(wsdl_path, property1, property2, property3);
            //Assert Association path exsist
            getAssociationPath(wsdl_path, association_path);
            //Assert wsdl content
            wsdlContentAssertion(wsdl_path, keyword1, keyword2);
            //Assert Service Exsist
            checkServiceExsist(service_namespace, service_name, service_path);
            //Remove wsdl
            registry.delete(wsdl_path);
            //Remove service
            registry.delete(service_path);
            //Assert wsdl was Removed successfully
            assertFalse("largest WSDL exists", registry.resourceExists(wsdl_path));
        } catch (RegistryException e) {
            log.error("largeWSDL_import Exception thrown:" + e.getMessage());
            Assert.fail("largeWSDL_import Exception thrown:" + e.getMessage());
        }
    }
}



