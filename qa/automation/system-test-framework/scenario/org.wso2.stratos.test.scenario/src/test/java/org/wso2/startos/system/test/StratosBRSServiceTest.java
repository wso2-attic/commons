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

package org.wso2.startos.system.test;

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
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.startos.system.test.stratosUtils.ServiceLoginClient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class StratosBRSServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosBRSServiceTest.class);
    private static String HTTP_RULE_STRATOSLIVE_URL;

    @Override
    public void init() {
        testClassName = StratosBRSServiceTest.class.getName();
        int tenantId = TenantListCsvReader.getTenantId("4");
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(tenantId);
        HTTP_RULE_STRATOSLIVE_URL = "http://" + FrameworkSettings.BRS_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain();
    }

    @Override
    public void runSuccessCase() {
        String dssServerHostName = FrameworkSettings.BRS_SERVER_HOST_NAME;
        ServiceLoginClient.loginChecker(dssServerHostName);
        greetingServiceTest();
        loadTestDemoProxyService();
    }

    @Override
    public void cleanup() {
    }

    public static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://brs.carbon.wso2.org", "ns1");
        OMNamespace nameNs = fac.createOMNamespace("http://greeting.samples/xsd", "ns2");
        OMElement method = fac.createOMElement("greetMe", omNs);
        OMElement value = fac.createOMElement("User", omNs);
        OMElement NameValue = fac.createOMElement("name", nameNs);
        NameValue.addChild(fac.createOMText(NameValue, "QAuser"));
        value.addChild(NameValue);
        method.addChild(value);
        return method;
    }

    public static Boolean greetingServiceTest() {
        Boolean greetingServiceTestStatus = false;
        OMElement payload = createPayLoad();
        try {
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            opts.setTo(new EndpointReference(HTTP_RULE_STRATOSLIVE_URL + "/GreetingService/"));
            opts.setAction("http://brs.carbon.wso2.org/greetMe");

            serviceclient.setOptions(opts);
            OMElement result;
            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("QAuser")) > 0) {
                greetingServiceTestStatus = true;
            }
            assertTrue("Greeting service invocation failed", greetingServiceTestStatus);

        } catch (AxisFault axisFault) {
            log.error("Greeting service invocation failed :" + axisFault.getMessage());
            fail("Greeting service invocation failed" + axisFault.getMessage());
        }
        return greetingServiceTestStatus;
    }

    public static void loadTestDemoProxyService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Greeting Service failed", greetingServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }
}
