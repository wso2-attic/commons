/*
* Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifact;
import org.wso2.carbon.governance.api.endpoints.EndpointManager;
import org.wso2.carbon.governance.api.endpoints.dataobjects.Endpoint;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.services.ServiceManager;
import org.wso2.carbon.governance.api.services.dataobjects.Service;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import javax.xml.namespace.QName;


public class EndpointServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(EndpointServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;

    @Override
    public void init() {
        testClassName = EndpointServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
	log.info("Running EndpointServiceTestClient Test Cases............................ ");
        addEndpoint();
        addWsdlWithEndpoints();
        addServiceWithEndpoints();
        attachEndpointsToService();
	log.info("Completed Running EndpointServiceTestClient Test Cases............................ ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/_system/governance/trunk/services");
        deleteResources("/_system/governance/trunk/wsdls");
        deleteResources("/_system/governance/trunk/endpoints");
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


    private void createEndpoint(String endpoint_url) {
        EndpointManager endpointManager = new EndpointManager(registry);
        Endpoint endpoint1;
        try {
            endpoint1 = endpointManager.newEndpoint(endpoint_url);
            endpoint1.addAttribute("status1", "QA");
            endpoint1.addAttribute("status2", "Dev");
            endpointManager.addEndpoint(endpoint1);
            log.info("Endpoint was successfully added");
        } catch (GovernanceException e) {
            log.error("createEndpoint Exception thrown:" + e.getMessage());
            Assert.fail("createEndpoint Exception thrown:" + e.getMessage());
        }
    }

    public void propertyAssertion(String endpoint_path, String property1, String property2) {
        //Assert Property
        Resource resource;
        try {
            resource = registry.get(endpoint_path);
            assertEquals("Endpoint Property - Status1 does not Exists :", resource.getProperty("status1"), property1);
            assertEquals("Endpoint Property - Status2 does not Exists :", resource.getProperty("status2"), property2);
        } catch (RegistryException e) {
            log.error("propertyAssertion Exception thrown:" + e.getMessage());
            Assert.fail("propertyAssertion Exception thrown:" + e.getMessage());
        }
    }

    private void addEndpoint() {
        String endpoint_url = "http://ws.strikeiron.com/StrikeIron/donotcall2_5/DoNotCallRegistry";
        String endpoint_path = "/_system/governance/trunk/endpoints/com/strikeiron/ws/strikeiron/donotcall2_5/ep-DoNotCallRegistry";
        String property1 = "QA";
        String property2 = "Dev";

        //Create Endpoint
        createEndpoint(endpoint_url);

        try {
            assertTrue("Endpoint Resource Does not exists :", registry.resourceExists(endpoint_path));
        } catch (RegistryException e) {
            log.error("addEndpoint Resource Does not Exists Registry Exception thrown:" + e.getMessage());
            Assert.fail("addEndpoint Resource Does not Exists Registry Exception thrown:" + e.getMessage());
        }

        propertyAssertion(endpoint_path, property1, property2);
        deleteResources(endpoint_path);
        log.info("EndpointServiceTestClient -addEndpoint() Passed");
    }

    private void addWsdlWithEndpoints() {
        String wsdl_url = "http://people.wso2.com/~evanthika/wsdls/BizService.wsdl";
        String endpoint_path = "http://people.wso2.com:9763/services/BizService";

        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl;
        try {
            wsdl = wsdlManager.newWsdl(wsdl_url);
            wsdlManager.addWsdl(wsdl);
            log.info("EndpointServiceTestClient - WSDL was successfully added");

            Endpoint[] endpoints = wsdl.getAttachedEndpoints();
            assertEquals(1, endpoints.length);
            assertEquals("Endpoint Path does not exsits :", endpoint_path, endpoints[0].getUrl());
            assertEquals(1, endpoints[0].getAttributeKeys().length);

            // now we are trying to remove the endpoint
            EndpointManager endpointManager = new EndpointManager(governance);
            try {
                endpointManager.removeEndpoint(endpoints[0].getId());
                assertTrue(false);
            } catch (Exception e) {
                assertTrue(true);
                log.info("Can't remove Endpoint yet because of serive & wsdl Exists");
            }

            GovernanceArtifact[] artifacts = wsdl.getDependents();
            // delete the wsdl
            wsdlManager.removeWsdl(wsdl.getId());

            ServiceManager serviceManager = new ServiceManager(governance);

            for (GovernanceArtifact artifact : artifacts) {
                if (artifact instanceof Service) {
                    // getting the service.
                    Service service2 = (Service) artifact;
                    serviceManager.removeService(service2.getId());
                }
            }

            // now try to remove the endpoint
            endpointManager.removeEndpoint(endpoints[0].getId());
            assertTrue(true);
            deleteResources(endpoint_path);
            log.info("EndpointServiceTestClient addWsdlWithEndpoints()- Passed");
        } catch (GovernanceException e) {
            log.error("addWsdlWithEndpoints Exception thrown:" + e.getMessage());
            Assert.fail("addWsdlWithEndpoints Exception thrown:" + e.getMessage());
        }
    }

    private void addServiceWithEndpoints() {
        String service_namespace = "http://wso2.com/test/examples";
        String service_name = "myService";
        String service_path = "/_system/governance/trunk/services/com/wso2/test/examples/myService";
        String endpoint_path1 = "/_system/governance/trunk/endpoints/ep-endpoint1";
        String endpoint_path2 = "/_system/governance/trunk/endpoints/ep-endpoint2";


        ServiceManager serviceManager = new ServiceManager(governance);
        try {
            Service service = serviceManager.newService(new QName(service_namespace, service_name));
            service.addAttribute("endpoints_entry", ":http://endpoint1");
            service.addAttribute("endpoints_entry", "QA:http://endpoint2");
            serviceManager.addService(service);

            Endpoint[] endpoints = service.getAttachedEndpoints();
            assertEquals(2, endpoints.length);

            assertEquals("http://endpoint1", endpoints[0].getUrl());
            assertEquals(0, endpoints[0].getAttributeKeys().length);

            assertEquals("http://endpoint2", endpoints[1].getUrl());
            assertEquals(1, endpoints[1].getAttributeKeys().length);
            deleteResources(service_path);
            deleteResources(endpoint_path1);
            deleteResources(endpoint_path2);
            log.info("EndpointServiceTestClient addServiceWithEndpoints()- Passed");
        } catch (GovernanceException e) {
            log.error("addServiceWithEndpoints GovernanceException Exception thrown:" + e.getMessage());
            Assert.fail("addServiceWithEndpoints GovernanceException Exception thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("addServiceWithEndpoints RegistryException Exception thrown:" + e.getMessage());
            Assert.fail("addServiceWithEndpoints RegistryException Exception thrown:" + e.getMessage());
        }

    }

    private void attachEndpointsToService() {
        String service_namespace = "http://wso2.com/test234/xxxxx";
        String service_name = "myServicxcde";
        String service_path = "/_system/governance/trunk/services/com/wso2/test234/xxxxx/myServicxcde";
        String endpoint_path1 = "/_system/governance/trunk/endpoints/ep-endpoint1xx";
        String endpoint_path2 = "/_system/governance/trunk/endpoints/ep-endpoint2xx";

        ServiceManager serviceManager = new ServiceManager(governance);
        Service service;
        try {
            service = serviceManager.newService(new QName(service_namespace, service_name));
            serviceManager.addService(service);

            EndpointManager endpointManager = new EndpointManager(governance);
            Endpoint ep1 = endpointManager.newEndpoint("http://endpoint1xx");
            endpointManager.addEndpoint(ep1);

            Endpoint ep2 = endpointManager.newEndpoint("http://endpoint2xx");
            endpointManager.addEndpoint(ep2);

            service.attachEndpoint(ep1);
            service.attachEndpoint(ep2);

            Endpoint[] endpoints = service.getAttachedEndpoints();
            assertEquals(2, endpoints.length);
            assertEquals("http://endpoint1xx", endpoints[0].getUrl());
            assertEquals("http://endpoint2xx", endpoints[1].getUrl());

            //Detach Endpoint one
            service.detachEndpoint(ep1.getId());
            endpoints = service.getAttachedEndpoints();
            assertEquals(1, endpoints.length);
            deleteResources(service_path);
            deleteResources(endpoint_path1);
            deleteResources(endpoint_path2);
            log.info("EndpointServiceTestClient attachEndpointsToService()- Passed");
        } catch (GovernanceException e) {
            log.error("attachEndpointsToService GovernanceException Exception thrown:" + e.getMessage());
            Assert.fail("attachEndpointsToService GovernanceException Exception thrown:" + e.getMessage());
        }
    }

}
