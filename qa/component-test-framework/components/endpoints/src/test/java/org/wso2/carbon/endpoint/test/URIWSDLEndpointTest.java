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

public class URIWSDLEndpointTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(URIWSDLEndpointTest.class);

    @Override
    public void init() {
        log.info("Initializing URI WSDL Endpoint Tests");
        log.debug("URI WSDL Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running URI WSDL Endpoint Tests Success Case ");

        try {

            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("wsdlURITest")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("wsdlURITest");
            }
            int endpointCount_before = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();

            new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteSuccessCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                                      "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"wsdlURITest\">\n" +
                                                                                      "    <wsdl service=\"AWSECommerceService\" port=\"AWSECommerceServicePort\" uri=\"http://webservices.amazon.com/AWSECommerceService/JP/AWSECommerceService.wsdl\"/>\n" +
                                                                                      "</endpoint>");
            new EndpointAdminCommand(endpointAdminStub).enableStatSuccessCase("wsdlURITest");


            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();


            //getting number of endpoints available
            if (!(endpointCount_after - endpointCount_before == 1)) {
                log.error("URI-WSDL Endpoint has not been added successfully");
                Assert.fail("URI-WSDL Endpoint has not been added successfully");
            } else {
                log.info("URI-WSDL Endpoint added successfully");
                System.out.println("URI-WSDL Endpoint added successfully");

            }

            //enable statistics test for address endpoint
            if (!(new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("wsdlURITest").contains("statistics=\"enable"))) {
                log.error("URI-WSDL Endpoint statistics not enabled");
                Assert.fail("URI-WSDL Endpoint statistics not enabled");
            } else {
                log.info("URI-WSDL Endpoint statistics enabled successfully");
                System.out.println("URI-WSDL Endpoint statistics enabled successfully");

            }

            //getting endpoint details
            String ep = new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("wsdlURITest");
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("wsdlURITest")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("wsdlURITest");
                System.out.println("URI-WSDL Endpoint Deleted Successfully");
            }

            if (!(ep.contains("wsdlURITest"))) {
                log.error("URI-WSDL Endpoint has not been added successfully");
                Assert.fail("URI-WSDL Endpoint has not been added successfully");
            }

        } catch (Exception
                e) {
            e.printStackTrace();
            log.error("URI-WSDL Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}

