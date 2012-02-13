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

public class InlinedWSDLEndpointTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(InlinedWSDLEndpointTest.class);

    @Override
    public void init() {
        log.info("Initializing WSDL Endpoint Tests");
        log.debug("WSDL Endpoint Tests Initialized");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running WSDL Endpoint Tests Success Case ");

        try {

            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "endpoints"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "wsdlTestEp.xml";

            OMElement omElement = ConfigHelper.createOMElement(xmlPath);
            String wsdlTestEp = omElement.toStringWithConsume();

            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("wsdlTestEp")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("wsdlTestEp");
            }
            int endpointCount_before = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();

            new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteSuccessCase(wsdlTestEp);
            new EndpointAdminCommand(endpointAdminStub).enableStatSuccessCase("wsdlTestEp");

            int endpointCount_after = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();

            //getting number of endpoints available
            if (!(endpointCount_after - endpointCount_before == 1)) {
                log.error("Inlined-WSDL Endpoint has not been added successfully");
                Assert.fail("Inlined-WSDL Endpoint has not been added successfully");
            } else {
                log.info("Inlined-WSDL Endpoint added successfully");
                System.out.println("Inlined-WSDL Endpoint added successfully");

            }

            //enable statistics test for address endpoint
            if (!(new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("wsdlTestEp").contains("statistics=\"enable"))) {
                log.error("Inlined-WSDL Endpoint statistics not enabled");
                Assert.fail("Inlined-WSDL Endpoint statistics not enabled");
            } else {
                log.info("Inlined-WSDL Endpoint statistics enabled successfully");
                System.out.println("Inlined-WSDL Endpoint statistics enabled successfully");

            }

            //getting endpoint details
            String ep = new EndpointAdminCommand(endpointAdminStub).getEndpointSuccessCase("wsdlTestEp");
            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());

            if (endpointList.contains("wsdlTestEp")) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase("wsdlTestEp");
                System.out.println("Inlined-WSDL Endpoint Deleted Successfully");
            }

            if (!(ep.contains("wsdlTestEp"))) {
                log.error("Inlined-WSDL Endpoint has not been added successfully");
                Assert.fail("Inlined-WSDL Endpoint has not been added successfully");
            }

        } catch (Exception
                e) {
            e.printStackTrace();
            log.error("Inlined-WSDL Endpoint Failed " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
