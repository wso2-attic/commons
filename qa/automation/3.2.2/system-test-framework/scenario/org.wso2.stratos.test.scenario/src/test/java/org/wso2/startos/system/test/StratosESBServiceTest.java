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


public class StratosESBServiceTest extends TestTemplate {
    private static Log log = LogFactory.getLog(StratosESBServiceTest.class);
    private static String HTTP_ESB_STRATOS_EPR;

    @Override
    public void init() {
        testClassName = StratosESBServiceTest.class.getName();
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        HTTP_ESB_STRATOS_EPR = "http://" + FrameworkSettings.ESB_SERVER_HOST_NAME + ":" + FrameworkSettings.ESB_NHTTP_PORT + "/services" + "/t/" + tenantDetails.getTenantDomain();
    }

    @Override
    public void runSuccessCase() {
        String esbServerHostName = FrameworkSettings.ESB_SERVER_HOST_NAME;
        ServiceLoginClient.loginChecker(esbServerHostName);
        demoProxyTestClient();
        loadTestDemoProxyService();
    }

    @Override
    public void cleanup() {
    }

    public static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "ns1");
        OMElement method = fac.createOMElement("echoString", omNs);
        OMElement value = fac.createOMElement("s", omNs);
        value.addChild(fac.createOMText(value, "Hello World"));
        method.addChild(value);
        return method;
    }

    public static Boolean demoProxyTestClient() {
        Boolean demoProxyTestClientStatus = false;

        OMElement payload = createPayLoad();
        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            opts.setTo(new EndpointReference(HTTP_ESB_STRATOS_EPR + "/DemoProxy"));
            opts.setAction("http://service.carbon.wso2.org/echoString");
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);
            if ((result.toString().indexOf("Hello World")) > 0) {
                demoProxyTestClientStatus = true;
            }
            assertTrue("Demo proxy service invocation failed", demoProxyTestClientStatus);

        } catch (AxisFault e) {
            log.error("Demo proxy service invocation failed :" + e.getMessage());
            fail("Demo proxy service invocation failed :" + e.getMessage());
        }
        return demoProxyTestClientStatus;
    }

    public static void loadTestDemoProxyService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Hello service BPEL failed", demoProxyTestClient());
                    }
                }
            };
            clientThread.start();
        }
    }

}
