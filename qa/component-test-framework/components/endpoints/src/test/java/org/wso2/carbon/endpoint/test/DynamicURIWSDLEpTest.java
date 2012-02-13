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

public class DynamicURIWSDLEpTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(DynamicURIWSDLEpTest.class);

    @Override
    public void init() {
        log.info("Initializing Dynamic URI WSDL Endpoint Tests");
        log.debug("Dynamic URI WSDL Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Dynamic URI WSDL Endpoint Tests Success Case ");
        try {


            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointsSuccessCase(0, 100));

            if (endpointList.contains("conf:/DynamicURIconf")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("conf:/DynamicURIconf");

            }
            if (endpointList.contains("gov:/DynamicURIgov")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("gov:/DynamicURIgov");
            }
            int endpointCount_before = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            String epXML = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\">\n" +
                           "   <wsdl uri=\"http://webservices.amazon.com/AWSECommerceService/JP/AWSECommerceService.wsdl\" service=\"AWSECommerceService\" port=\"AWSECommerceServicePort\" >\n" +
                           "      <suspendOnFailure>\n" +
                           "         <progressionFactor>1.0</progressionFactor>\n" +
                           "      </suspendOnFailure>\n" +
                           "      <markForSuspension>\n" +
                           "         <retriesBeforeSuspension>0</retriesBeforeSuspension>\n" +
                           "         <retryDelay>0</retryDelay>\n" +
                           "      </markForSuspension>\n" +
                           "   </wsdl>\n" +
                           "</endpoint>";

            new EndpointAdminCommand(endpointAdminStub).addDynamicEndpointSuccessCase("conf:/DynamicURIconf", epXML);

            new EndpointAdminCommand(endpointAdminStub).addDynamicEndpointSuccessCase("gov:/DynamicURIgov", epXML);

            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            //getting number of dynamic endpoints +2 to than before
            if (!(endpointCount_after - endpointCount_before == 2)) {
                log.error("Dynamic URI WSDL Endpoints have not been added successfully");
                Assert.fail("Dynamic URI WSDL Endpoints have not been added successfully");
            } else {
                log.info("Dynamic URI WSDL Endpoints added successfully");
                System.out.println("Dynamic URI WSDL Endpoints added successfully");

            }

            //getting dynamic endpoints
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointsSuccessCase(0, 100));

            if (endpointList.contains("conf:/DynamicURIconf")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("conf:/DynamicURIconf");
            }
            if (endpointList.contains("gov:/DynamicURIgov")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("gov:/DynamicURIgov");
            }

            endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            //getting number of dynamic endpoints: endpointCount_after=  endpointCount_before
            if (!(endpointCount_after - endpointCount_before == 0)) {
                log.error("Dynamic URI WSDL Endpoints have not been deleted successfully");
                Assert.fail("Dynamic URI WSDL Endpoints have not been deleted successfully");
            } else {
                log.info("Dynamic URI WSDL Endpoints deleted successfully");
                System.out.println("Dynamic URI WSDL Endpoints deleted successfully");
            }

        } catch (Exception e) {
            log.error("Dynamic URI WSDL Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}

