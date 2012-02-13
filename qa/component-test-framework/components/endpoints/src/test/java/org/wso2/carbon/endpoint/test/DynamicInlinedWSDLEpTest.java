package org.wso2.carbon.endpoint.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.endpoint.test.commands.EndpointAdminCommand;
import org.wso2.carbon.endpoint.test.commands.InitializeEndpointAdminCommand;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;

import java.io.File;
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

public class DynamicInlinedWSDLEpTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(DynamicInlinedWSDLEpTest.class);

    @Override
    public void init() {
        log.info("Initializing Dynamic Inlined-WSDL Endpoint Tests");
        log.debug("Dynamic Inlined-WSDL Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Dynamic Inlined-WSDL Endpoint Tests Success Case ");

        try {
            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "endpoints"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "dynamicEp.xml";

            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointsSuccessCase(0, 100));

            if (endpointList.contains("conf:/DynamicInWSDLconf")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("conf:/DynamicInWSDLconf");

            }
            if (endpointList.contains("gov:/DynamicInWSDLgov")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("gov:/DynamicInWSDLgov");
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

            new EndpointAdminCommand(endpointAdminStub).addDynamicEndpointSuccessCase("conf:/DynamicInWSDLconf", epXML);

            new EndpointAdminCommand(endpointAdminStub).addDynamicEndpointSuccessCase("gov:/DynamicInWSDLgov", epXML);


            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            //getting number of dynamic endpoints +2 to than before
            if (!(endpointCount_after - endpointCount_before == 2)) {
                log.error("Dynamic Inlined-WSDL Endpoints have not been added successfully");
                Assert.fail("Dynamic Inlined-WSDL Endpoints have not been added successfully");
            } else {
                log.info("Dynamic Inlined-WSDL Endpoints added successfully");
                System.out.println("Dynamic Inlined-WSDL Endpoints added successfully");

            }

            //getting dynamic endpoints
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointsSuccessCase(0, 100));

            if (endpointList.contains("conf:/DynamicInWSDLconf")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("conf:/DynamicInWSDLconf");
            }
            if (endpointList.contains("gov:/DynamicInWSDLgov")) {
                new EndpointAdminCommand(endpointAdminStub).deleteDynamicEpSuccessCase("gov:/DynamicInWSDLgov");
            }

            endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getDynamicEndpointCountSuccessCase();

            //getting number of dynamic endpoints: endpointCount_after=  endpointCount_before
            if (!(endpointCount_after - endpointCount_before == 0)) {
                log.error("Dynamic Inlined-WSDL Endpoints have not been deleted successfully");
                Assert.fail("Dynamic Inlined-WSDL Endpoints have not been deleted successfully");
            } else {
                log.info("Dynamic Inlined-WSDL Endpoints deleted successfully");
                System.out.println("Dynamic Inlined-WSDL Endpoints deleted successfully");

            }
        } catch (Exception e) {
            log.error("Dynamic Inlined-WSDL Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}

