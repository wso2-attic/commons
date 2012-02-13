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

public class LoadBalanceEndpointTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(LoadBalanceEndpointTest.class);

    @Override
    public void init() {
        log.info("Initializing Load-balance Endpoint Tests");
        log.debug("Load-balance Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Load-balance Endpoint Tests Success Case ");
        try {


            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("loadbalEp")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("loadbalEp");
            }
            int endpointCount_before = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();

            new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteSuccessCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                                      "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"loadbalEp\">\n" +
                                                                                      "    <loadbalance algorithm=\"org.apache.synapse.endpoints.algorithms.RoundRobin\">\n" +
                                                                                      "        <endpoint name=\"endpoint_urn_uuid_7E71CCD625D839E55A25548428059450-1123062013\">\n" +
                                                                                      "            <address uri=\"http://webservices.amazon.com/AWSECommerceService/UK/AWSECommerceService.wsdl\"/>\n" +
                                                                                      "        </endpoint>\n" +
                                                                                      "    </loadbalance>\n" +
                                                                                      "</endpoint>");

            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();


            //getting number of endpoints available
            if (!(endpointCount_after - endpointCount_before == 1)) {
                log.error("Load-balance Endpoint has not been added successfully");
                Assert.fail("Load-balance Endpoint has not been added successfully");
            } else {
                log.info("Load-balance Endpoint added successfully");
                System.out.println("Load-balance Endpoint added successfully");
            }

            //getting endpoint details
            String ep = new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("loadbalEp");
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("loadbalEp")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("loadbalEp");
                System.out.println("Load-balance Endpoint Deleted Successfully");
            }

            if (!(ep.contains("loadbalEp"))) {
                log.error("Load-balance Endpoint has not been added successfully");
                Assert.fail("Load-balance Endpoint has not been added successfully");
            }

        } catch (Exception
                e) {
            e.printStackTrace();
            log.error("Load-balance Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
