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

public class StratosMSServiceTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosAppServiceTest.class);
    private static String HTTP_MASHUP_STRATOSLIVE_URL = "http://mashup.stratoslive.wso2.com/services/t/manualQA0001.org";
    private static String userName;

    @Override
    public void init() {

        testClassName = StratosMSServiceTest.class.getName();
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        HTTP_MASHUP_STRATOSLIVE_URL = "http://" + FrameworkSettings.MS_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain();
        String tenantUserName = tenantDetails.getTenantName();
        this.userName = tenantUserName.substring(0,tenantUserName.indexOf('@'));
    }

    @Override
    public void runSuccessCase() {
        String mashupServerHostName = FrameworkSettings.MS_SERVER_HOST_NAME;
        ServiceLoginClient.loginChecker(mashupServerHostName);

        schemaTestMashupTest();
        loadSchemaTestService();

        httpClientServiceTest();
        loadHttpClientTestService();

        requestServiceTest();
        loadRequestTestService();

        sessionServiceTest();
        loadSessionTestService();

        feedServiceTest();
        loadFeedTestService();

        WSRequestTest();
        loadWSRequestTestService();

        IMServiceTest();
        loadIMTestService();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            fail("Thread interrupted" + e.getMessage());
        }
    }

    @Override
    public void cleanup() {
    }

    public static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/schemaTest1", "ns1");
        OMElement method = fac.createOMElement("echoJSString", omNs);
        OMElement value = fac.createOMElement("param", null);
        value.addChild(fac.createOMText(value, "Hello World"));
        method.addChild(value);
        return method;
    }

    private static boolean schemaTestMashupTest() {
        Boolean schemaTestMashupTestStatus = false;

        OMElement payload = createPayLoad();

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName + "/TestMSServices/"));
            opts.setAction("http://services.mashup.wso2.org/schemaTest1");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);
            if ((result.toString().indexOf("Hello World")) > 0) {
                schemaTestMashupTestStatus = true;
            }
            assertTrue("Schema test mashup service invocation failed", schemaTestMashupTestStatus);

        } catch (AxisFault axis2Fault) {
            log.error("Schema test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("Schema test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return schemaTestMashupTestStatus;
    }

    private static void loadSchemaTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Schema Test mashup service failed", schemaTestMashupTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    private static boolean httpClientServiceTest() {
        Boolean status = false;

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/httpClient?xsd", "ns1");
        OMElement payload = fac.createOMElement("searchGoogle", omNs);
        OMElement query = fac.createOMElement("query", null);
        query.setText("wso2.com");
        payload.addChild(query);

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName + "/httpClient"));
            opts.setAction(userName + "/searchGoogle");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("html")) > 0) {
                status = true;
            }
            assertTrue("HttpClient test mashup service invocation failed", status);

        } catch (AxisFault axis2Fault) {
            log.error("Schema test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("HttpClient test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return status;
    }

    private static void loadHttpClientTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on HttpClient Test mashup service failed", httpClientServiceTest());
                    }
                }
            };
            clientThread.start();

        }
    }

    private static boolean requestServiceTest() {
        Boolean status = false;

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/request?xsd", "ns1");
        OMElement payload = fac.createOMElement("returnURL", omNs);

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName +"/request/"));
            opts.setAction(userName + "/returnURL");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);
            System.out.println();

            if ((result.toString().indexOf(userName)) > 0) {
                status = true;
            }
            assertTrue("Request test mashup service invocation failed", status);

        } catch (AxisFault axis2Fault) {
            log.error("Request test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("Request test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return status;
    }

    private static void loadRequestTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Request test on Schema Test mashup service failed", requestServiceTest());
                    }
                }
            };
            clientThread.start();

        }
    }

    private static boolean IMServiceTest() {
        Boolean schemaTestMashupTestStatus = false;

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/IMAllScenarios?xsd", "ns1");
        OMElement payload = fac.createOMElement("gabberSingleMsg", omNs);

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName + "/IMAllScenarios/"));
            opts.setAction(userName + "/gabberSingleMsg");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("successful")) > 0) {
                schemaTestMashupTestStatus = true;
            }
            assertTrue("IM test mashup service invocation failed", schemaTestMashupTestStatus);

        } catch (AxisFault axis2Fault) {
            log.error("IM test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("IM test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return schemaTestMashupTestStatus;
    }

    private static void loadIMTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("IM test on Schema Test mashup service failed", IMServiceTest());
                    }
                }
            };
            clientThread.start();

        }
    }

    private static boolean sessionServiceTest() {
        Boolean status = false;

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/session?xsd", "ns1");
        OMElement payload = fac.createOMElement("putValue", omNs);
        OMElement param = fac.createOMElement("param", null);
        payload.addChild(param);

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName + "/ApplicationScopeService/"));
            opts.setAction(userName + "/putValue");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("success")) > 0) {
                payload = fac.createOMElement("getValue", omNs);
                param = fac.createOMElement("param", null);
                payload.addChild(param);
                result = serviceclient.sendReceive(payload);
                if ((result.toString().indexOf("200")) > 0) {
                    status = true;
                }
            }
            assertTrue("Session test mashup service invocation failed", status);

        } catch (AxisFault axis2Fault) {
            log.error("Session test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("Session test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return status;
    }

    private static void loadSessionTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Session test on Schema Test mashup service failed", sessionServiceTest());
                    }
                }
            };
            clientThread.start();

        }
    }

    private static boolean feedServiceTest() {
        Boolean status = false;

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/feedReader2?xsd", "ns1");
        OMElement payload = fac.createOMElement("test", omNs);

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName + "/feedReader2/"));
            opts.setAction(userName + "/test");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("http://www.formula1.com/news/headlines/")) > 0) {
                status = true;

            }
            assertTrue("Feed test mashup service invocation failed", status);

        } catch (AxisFault axis2Fault) {
            log.error("Feed test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("Feed test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return status;
    }

    private static void loadFeedTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Feed test on Schema Test mashup service failed", feedServiceTest());
                    }
                }
            };
            clientThread.start();

        }
    }

    private static boolean WSRequestTest() {
        Boolean status = false;

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.mashup.wso2.org/WSRequest?xsd", "ns1");
        OMElement payload = fac.createOMElement("invokeGetVersion", omNs);

        try {
            OMElement result;
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_MASHUP_STRATOSLIVE_URL + "/" + userName + "/WSRequest/"));
            opts.setAction( userName + "/invokeGetVersion");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("WSO2 Stratos Application Server")) > 0) {
                status = true;

            }
            assertTrue("WSRequest test mashup service invocation failed", status);

        } catch (AxisFault axis2Fault) {
            log.error("WSRequest test mashup service invocation failed :" + axis2Fault.getMessage());
            fail("WSRequest test mashup service invocation failed :" + axis2Fault.getMessage());
        }
        return status;
    }

    private static void loadWSRequestTestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("WSRequest test on Schema Test mashup service failed", WSRequestTest());
                    }
                }
            };
            clientThread.start();

        }
    }
}
