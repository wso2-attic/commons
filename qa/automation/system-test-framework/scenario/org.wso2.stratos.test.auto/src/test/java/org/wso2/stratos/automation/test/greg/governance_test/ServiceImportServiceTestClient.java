package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.services.ServiceManager;
import org.wso2.carbon.governance.api.services.dataobjects.Service;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import javax.xml.namespace.QName;


public class ServiceImportServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(SchemaImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;


    @Override
    public void init() {
        testClassName = ServiceImportServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        //Delete Service already exists
        deleteService();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running ServiceImportServiceTestClient Test Cases............................ ");
        addService1();
        log.info("Completed Running ServiceImportServiceTestClient Test Cases...................");
    }

    //    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }


    public Service createService(String service_namespace, String service_name) {
        ServiceManager serviceManager = new ServiceManager(governance);
        Service service = null;
        try {
            service = serviceManager.newService(new QName(service_namespace, service_name));
            service.addAttribute("creator", "Aaaa");
            service.addAttribute("version", "1.0.0");
            serviceManager.addService(service);
            log.info("Service Added Successfully");
        } catch (GovernanceException e) {
            log.error("createService Exception thrown:" + e.getMessage());
            Assert.fail("createService Exception thrown:" + e.getMessage());
        }
        return service;
    }

    public void deleteService() {
        try {
            if (registry.resourceExists("/_system/governance/trunk/services/com/example/demo/services/ExampleService")) {
                registry.delete("/_system/governance/trunk/services/com/example/demo/services/ExampleService");
            }
        } catch (RegistryException e) {
            log.error("deleteService Exception thrown:" + e.getMessage());
        }
    }


    private void addService1() {
        String service_namespace = "http://example.com/demo/services";
        String service_name = "ExampleService";
        String service_path = "/_system/governance/trunk/services/com/example/demo/services/ExampleService";

        createService(service_namespace, service_name);

        try {
            //Assert Service exists
            assertTrue("Service Exists", registry.resourceExists(service_path));
            //Remove Service
            registry.delete(service_path);
            //Assert Service removed successfully
            assertFalse("Service Exists", registry.resourceExists(service_path));
        } catch (RegistryException e) {
            log.error("addService1 Exception thrown:" + e.getMessage());
            Assert.fail("addService1 Exception thrown:" + e.getMessage());
        }
    }
}
