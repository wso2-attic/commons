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

public class DynamicLoadBalanceEpTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(DynamicLoadBalanceEpTest.class);

    @Override
    public void init() {
        log.info("Initializing Dynamic Load-balance Endpoint Tests");
        log.debug("Dynamic Load-balance Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Dynamic Load-balance Endpoint Tests Success Case ");
        try {


            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointsSuccessCase(0, 100));

            if (endpointList.contains("conf:/DynamicLbconf")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("conf:/DynamicLbconf");

            }
            if (endpointList.contains("gov:/DynamicLbgov")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("gov:/DynamicLbgov");
            }
            int endpointCount_before = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            String epXML = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"anonymous\">\n" +
                           "   <loadbalance algorithm=\"org.apache.synapse.endpoints.algorithms.RoundRobin\">\n" +
                           "      <endpoint>\n" +
                           "         <address uri=\"http://webservices.amazon.com/AWSECommerceService/UK/AWSECommerceService.wsdl\">\n" +
                           "            <suspendOnFailure>\n" +
                           "               <progressionFactor>1.0</progressionFactor>\n" +
                           "            </suspendOnFailure>\n" +
                           "            <markForSuspension>\n" +
                           "               <retriesBeforeSuspension>0</retriesBeforeSuspension>\n" +
                           "               <retryDelay>0</retryDelay>\n" +
                           "            </markForSuspension>\n" +
                           "         </address>\n" +
                           "      </endpoint>\n" +
                           "   </loadbalance>\n" +
                           "</endpoint>";

            new EndpointAdminCommand(endpointAdminStub).addDynamicEndpointSuccessCase("conf:/DynamicLbconf", epXML);

            new EndpointAdminCommand(endpointAdminStub).addDynamicEndpointSuccessCase("gov:/DynamicLbgov", epXML);

            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            //getting number of dynamic endpoints +2 to than before
            if (!(endpointCount_after - endpointCount_before == 2)) {
                log.error("Dynamic Load-balance Endpoints have not been added successfully");
                Assert.fail("Dynamic Load-balance Endpoints have not been added successfully");
            } else {
                log.info("Dynamic Load-balance Endpoints added successfully");
                System.out.println("Dynamic Load-balance Endpoints added successfully");

            }

            //getting dynamic endpoints
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointsSuccessCase(0, 100));

            if (endpointList.contains("conf:/DynamicLbconf")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("conf:/DynamicLbconf");
            }
            if (endpointList.contains("gov:/DynamicLbgov")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("gov:/DynamicLbgov");
            }

            endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            //getting number of dynamic endpoints: endpointCount_after=  endpointCount_before
            if (!(endpointCount_after - endpointCount_before == 0)) {
                log.error("Dynamic Load-balance Endpoints have not been deleted successfully");
                Assert.fail("Dynamic Load-balance Endpoints have not been deleted successfully");
            } else {
                log.info("Dynamic Load-balance Endpoints deleted successfully");
                System.out.println("Dynamic Load-balance Endpoints deleted successfully");
            }

        } catch (Exception e) {
            log.error("Dynamic Load-balance Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}

