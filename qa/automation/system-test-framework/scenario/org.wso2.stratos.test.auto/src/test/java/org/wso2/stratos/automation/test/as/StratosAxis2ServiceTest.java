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

package org.wso2.stratos.automation.test.as;

import junit.framework.Assert;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;


/*
Test multi-tenancy of services - Deploy services from one tenant and check whether those are available in other tenants
 */
public class StratosAxis2ServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosAxis2ServiceTest.class);
    private static String AXIS2SERVICE_EPR;
    private static boolean stratosStatus;
    private static final String AXIS2_SERVICE_NAME = "Axis2Service";

    @Override
    public void init() {
        stratosStatus = FrameworkSettings.getStratosTestStatus();
        testClassName = StratosAxis2ServiceTest.class.getName();
        String tenantId = "15";
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        generateServiceEPR(tenantDetails);
    }


    @Override
    public void runSuccessCase() {
        log.info("Running StratosAxis2ServiceTest...");
        String operation = "echoInt";
        String expectedValue = "123";

        AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR); // wait for service deployment
        OMElement result = new AxisServiceClient().sendReceive(createPayLoad(operation, expectedValue),
                AXIS2SERVICE_EPR, operation);
        log.debug("Response returned " + result);
        Assert.assertTrue((result.toString().indexOf(expectedValue) >= 1));

        if (stratosStatus) {
            //check service existence though other tenant login.
            String MultitenancyCheckerTenant = "12";
            TenantDetails secoundTenantDetails = TenantListCsvReader.getTenantDetails
                    (TenantListCsvReader.getTenantId(MultitenancyCheckerTenant));
            String serviceEPROfSecoundTenant = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                    secoundTenantDetails.getTenantDomain() + "/" + AXIS2_SERVICE_NAME + "/";
            assertFalse("Same service deployed in other tenants",
                    AxisServiceClientUtils.isServiceAvailable(serviceEPROfSecoundTenant));
            log.info(AXIS2_SERVICE_NAME + " multitenancy verify test passed");
        }
    }

    @Override
    public void cleanup() {
    }

    private static OMElement createPayLoad(String operation, String expectedValue) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "ns1");
        OMElement method = fac.createOMElement(operation, omNs);
        OMElement value = fac.createOMElement("x", omNs);
        value.addChild(fac.createOMText(value, expectedValue));
        method.addChild(value);
        log.debug("Created payload is :" + method);
        return method;
    }

    private void generateServiceEPR(TenantDetails tenantDetails) {
        if (stratosStatus) {
            AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                    tenantDetails.getTenantDomain() + "/" + AXIS2_SERVICE_NAME + "/";
        } else {
            //construct HTTP EPR based on webcontext root availability
            if (FrameworkSettings.APP_SERVER_WEB_CONTEXT_ROOT != null) {
                AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" +
                        FrameworkSettings.APP_SERVER_HTTP_PORT + "/" + FrameworkSettings.APP_SERVER_WEB_CONTEXT_ROOT +
                        "/services" + "/" + AXIS2_SERVICE_NAME;
            } else {
                AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" +
                        FrameworkSettings.APP_SERVER_HTTP_PORT +
                        "/services" + "/" + AXIS2_SERVICE_NAME;
            }
            log.debug("Axis2 service EPR is " + AXIS2SERVICE_EPR);
        }
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }


}
