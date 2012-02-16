/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.stratos.automation.test.as;

import junit.framework.Assert;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.utils.RegistryClientUtils;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.io.*;

public class StratosAsReadWSDLFromRepo extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosAsReadWSDLFromRepo.class);
    private static String AXIS2SERVICE_EPR;
    private static WSRegistryServiceClient registry = null;


    @Override
    public void init() {
        log.info("Running StratosAsReadWSDLFromRepo test...");
        String tenantId = "15";
        testClassName = StratosAsReadWSDLFromRepo.class.getName();
        log.info("Test " + testClassName + " Started using tenant ID " + tenantId);
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                tenantDetails.getTenantDomain() + "/calculatorImportSchema/";
        registry = new RegistryProvider().getRegistry(tenantId); //get remote registry instance
    }

    @Override
    public void runSuccessCase() {
        addCalculatorSchema();
        AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR); // wait for service deployment
        try {
            Thread.sleep(FrameworkSettings.SERVICE_DEPLOYMENT_DELAY);
        } catch (InterruptedException ignored) {
        }
        serviceTest();
    }

    @Override
    public void cleanup() {
    }

    private static boolean serviceTest() {
        boolean axis2ServiceStatus = false;
        OMElement result;
        try {
            OMElement payload = createPayLoad();
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(AXIS2SERVICE_EPR));
            opts.setAction("http://charitha.org/echoString");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);
            log.debug("Axis2Service EPR " + AXIS2SERVICE_EPR);

            result = serviceclient.sendReceive(payload);
            log.debug("Service response " + result);
            if ((result.toString().indexOf("420")) > 0) {
                axis2ServiceStatus = true;
            }
            assertTrue("Axis2Service invocation failed", axis2ServiceStatus);
        } catch (AxisFault axisFault) {
            log.error("Axis2Service invocation failed :" + axisFault.getMessage());
            fail("Axis2Service invocation failed :" + axisFault.getMessage());
        }
        return axis2ServiceStatus;
    }

    private static OMElement createPayLoad() {
        log.debug("Creating payload");
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://charitha.org", "char");
        OMElement method = fac.createOMElement("addition", omNs);
        OMElement valueOfx = fac.createOMElement("x", omNs);
        OMElement valueOfy = fac.createOMElement("y", omNs);
        valueOfx.addChild(fac.createOMText(valueOfx, "200"));
        valueOfy.addChild(fac.createOMText(valueOfy, "220"));
        method.addChild(valueOfx);
        method.addChild(valueOfy);
        log.debug("Payload is :" + method);

        return method;
    }

    private void addCalculatorSchema() {
        String filePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION +  File.separator + "artifacts" + File.separator
                + "GREG" + File.separator + "calculator-xsd.gar";
        String schema_path1 = "/_system/governance/trunk/schemas/org/charitha/calculator.xsd";
        try {
            Resource resource = registry.newResource();
            //create an Input Stream
            InputStream is1 = new BufferedInputStream(new FileInputStream(filePath));
            resource.setContentStream(is1);
            resource.setMediaType("application/vnd.wso2.governance-archive");
            registry.put("/dummy/xsd/path", resource);
            assertTrue("Schema doesn't exists:", registry.resourceExists(schema_path1));

        } catch (FileNotFoundException e) {
            log.error("Cannot find the given gar file " + e.getMessage());
            Assert.fail("Cannot find the given gar file " + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("Unable to upload gar file " + e.getMessage());
            Assert.fail("Unable to upload gar file " + e.getMessage());
        }
    }

}
