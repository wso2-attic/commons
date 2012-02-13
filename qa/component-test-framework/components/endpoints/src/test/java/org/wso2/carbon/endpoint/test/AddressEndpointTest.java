package org.wso2.carbon.endpoint.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.endpoint.test.commands.EndpointAdminCommand;
import org.wso2.carbon.endpoint.test.commands.InitializeEndpointAdminCommand;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright 2004,2005 The Apache Software Foundation.                         
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

public class AddressEndpointTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(AddressEndpointTest.class);

    @Override
    public void init() {
        log.info("Initializing Address Endpoint Tests");
        log.debug("Address Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Address Endpoint Tests Success Case ");
        try {


            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("addEpTest")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("addEpTest");
            }
            int endpointCount_before = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();

            new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteSuccessCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                                      "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"addEpTest\">\n" +
                                                                                      "    <address uri=\"http://webservices.amazon.com/AWSECommerceService/UK/AWSECommerceService.wsdl\"/>\n" +
                                                                                      "</endpoint>");
            new EndpointAdminCommand(endpointAdminStub).enableStatSuccessCase("addEpTest");


            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();


            //getting number of endpoints available
            if (!(endpointCount_after - endpointCount_before == 1)) {
                log.error("Address Endpoint has not been added successfully");
                Assert.fail("Address Endpoint has not been added successfully");
            } else {
                log.info("Address Endpoint added successfully");
                System.out.println("Address Endpoint added successfully");

            }

            //enable statistics test for address endpoint
            if (!(new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("addEpTest").contains("statistics=\"enable"))) {
                log.error("Address Endpoint statistics not enabled");
                Assert.fail("Address Endpoint statistics not enabled");
            } else {
                log.info("Address Endpoint statistics enabled successfully");
                System.out.println("Address Endpoint statistics enabled successfully");

            }

            //getting endpoint details
            String ep = new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("addEpTest");
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("addEpTest")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("addEpTest");
                System.out.println("Address Endpoint Deleted Successfully");
            }

            if (!(ep.contains("addEpTest"))) {
                log.error("Address Endpoint has not been added successfully");
                Assert.fail("Address Endpoint has not been added successfully");
            }

        } catch (Exception
                e) {
            e.printStackTrace();
            log.error("Address Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase
            () {

    }

    @Override
    public void cleanup
            () {

    }
}
