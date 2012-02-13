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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;

/**
 * Test multi tenancy of carbon applications (CAR) - Deploy a car from one tenant and
 * check whether it is available in other tenants
 */
public class CarFileMultitenancyTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(CarFileMultitenancyTest.class);
    private static String AXIS2SERVICE_EPR;
    private static boolean stratosStatus;

    @Override
    public void init() {
        log.info("Running CarFileMultitenancyTest test...");
        String tenantId = "13";
        stratosStatus = FrameworkSettings.getStratosTestStatus();
        String serviceName = "Calculator";


        testClassName = CarFileMultitenancyTest.class.getName();
        log.info("Test " + testClassName + " Started using tenant ID " + tenantId);
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));

        generateServiceEPR(tenantDetails, serviceName);

    }

    @Override
    public void runSuccessCase() {
        String operationName = "add";
        String expectedIntValue = "420";
        AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR); // wait for service deployment
        OMElement result = new AxisServiceClient().sendReceive(createPayLoad(), AXIS2SERVICE_EPR, operationName);
        log.debug("Response returned " + result);
        Assert.assertTrue((result.toString().indexOf(expectedIntValue) >= 1));

        if (stratosStatus) {
            //check service existence though other tenant login.
            String MultitenancyCheckerTenant = "12";
            TenantDetails secoundTenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(MultitenancyCheckerTenant));
            String serviceEPROfSecoundTenant = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                    secoundTenantDetails.getTenantDomain() + "/Calculator/";
            assertFalse("Same service deployed in other tenants",
                    AxisServiceClientUtils.isServiceAvailable(serviceEPROfSecoundTenant));
            log.info("Car multitenancy verify test passed");
        }
    }

    @Override
    public void cleanup() {
    }

    private static OMElement createPayLoad() {
        log.debug("Creating payload");
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://test.com", "test");
        OMElement method = fac.createOMElement("add", omNs);
        OMElement valueOfa = fac.createOMElement("a", omNs);
        OMElement valueOfb = fac.createOMElement("b", omNs);
        valueOfa.addChild(fac.createOMText(valueOfa, "200"));
        valueOfb.addChild(fac.createOMText(valueOfb, "220"));
        method.addChild(valueOfa);
        method.addChild(valueOfb);
        log.debug("Payload is :" + method);

        return method;
    }

    private void generateServiceEPR(TenantDetails tenantDetails, String serviceName) {
        if (stratosStatus) {
            AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                    tenantDetails.getTenantDomain() + "/" + serviceName + "/";
        } else {
            //construct HTTP EPR based on webcontext root availability
            if (FrameworkSettings.APP_SERVER_WEB_CONTEXT_ROOT != null) {
                AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" +
                        FrameworkSettings.APP_SERVER_HTTP_PORT + "/" + FrameworkSettings.APP_SERVER_WEB_CONTEXT_ROOT +
                        "/services" + "/" + serviceName;
            } else {
                AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" +
                        FrameworkSettings.APP_SERVER_HTTP_PORT +
                        "/services" + "/" + serviceName;
            }
            log.debug("Axis2 service EPR is " + AXIS2SERVICE_EPR);
        }
    }
}
