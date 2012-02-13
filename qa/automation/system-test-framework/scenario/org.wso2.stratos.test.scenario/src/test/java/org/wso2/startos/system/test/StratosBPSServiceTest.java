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

/*
Test class for Stratos BPS service test automation
 */
public class StratosBPSServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosBPSServiceTest.class);
    private static String HTTP_BPS_STRATOSLIVE_URL;

    @Override
    public void init() {
        testClassName = StratosBPSServiceTest.class.getName();
        int tenantId = TenantListCsvReader.getTenantId("4");
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(tenantId);
        HTTP_BPS_STRATOSLIVE_URL = "http://" + FrameworkSettings.BPS_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain();
    }

    @Override
    public void runSuccessCase() {
        String appServerHostName = FrameworkSettings.BPS_SERVER_HOST_NAME;
        ServiceLoginClient.loginChecker(appServerHostName);
        functionProcessServiceTest();
        helloWorldTest();
        customerInfoServiceTest();
        loadHelloService();
        loadCustomerInfoServiceTest();
        loadFunctionProcessServiceTest();
    }

    @Override
    public void cleanup() {
    }

    /*
   payload creation using
    */
    public static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ode/bpel/unit-test.wsdl", "ns1");
        OMElement method = fac.createOMElement("hello", omNs);
        OMElement value = fac.createOMElement("TestPart", null);
        value.addChild(fac.createOMText(value, "Hello"));
        method.addChild(value);
        return method;
    }

    public static Boolean helloWorldTest() {
        Boolean helloWorldTestStatus = false;
        OMElement payload = createPayLoad();

        try {
            OMElement result;

            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            opts.setTo(new EndpointReference(HTTP_BPS_STRATOSLIVE_URL + "/HelloService/"));
            opts.setAction("http://ode/bpel/unit-test.wsdl/hello");

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Hello World")) > 0) {
                helloWorldTestStatus = true;
            }
            assertTrue("HelloWorld BPEL invocation failed", helloWorldTestStatus);

        } catch (AxisFault e) {
            log.error("HelloWorld BPEL invocation failed :" + e.getMessage());
            fail("HelloWorld BPEL invocation failed " + e.getMessage());
        }
        return helloWorldTestStatus;
    }

    public static void loadHelloService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Hello service BPEL failed", helloWorldTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static Boolean customerInfoServiceTest() {
        Boolean customerInfoServiceTestStatus = false;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://wso2.org/bps/samples/loan_process/schema", "ns");
        OMElement payload = fac.createOMElement("CustomerInfo", omNs);
        OMElement name = fac.createOMElement("Name", omNs);
        OMElement email = fac.createOMElement("Email", omNs);
        OMElement cusID = fac.createOMElement("CustomerID", omNs);
        OMElement creditRating = fac.createOMElement("CreditRating", omNs);
        name.addChild(fac.createOMText(payload, "ManualQA0001"));
        email.addChild(fac.createOMText(payload, "testwso2qa@wso2.org"));
        cusID.addChild(fac.createOMText(payload, "1234"));
        creditRating.addChild(fac.createOMText(payload, "123"));
        payload.addChild(name);
        payload.addChild(email);
        payload.addChild(cusID);
        payload.addChild(creditRating);

        try {
            OMElement result;

            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            opts.setTo(new EndpointReference(HTTP_BPS_STRATOSLIVE_URL + "/CustomerInfoService/"));
            opts.setAction("http://wso2.org/bps/samples/loan_process/schema/getCustomerSSN");

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("43235678SSN")) > 0) {
                customerInfoServiceTestStatus = true;
            }
            assertTrue("CustomerInfo BPEL invocation failed", customerInfoServiceTestStatus);

        } catch (AxisFault e) {
            log.error("CustomerInfo  BPEL invocation failed :" + e.getMessage());
            fail("CustomerInfo BPEL invocation failed " + e.getMessage());
        }
        return customerInfoServiceTestStatus;
    }

    public static void loadCustomerInfoServiceTest() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on CustomerInfo service BPEL failed", customerInfoServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static Boolean functionProcessServiceTest() {
        Boolean functionProcessServiceTestStatus = false;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://www.example.org/messages", "mes");
        OMElement payload = fac.createOMElement("functionProcessServiceRequest", omNs);
        OMElement param0 = fac.createOMElement("param0", omNs);
        OMElement param1 = fac.createOMElement("param1", omNs);
        param0.addChild(fac.createOMText(payload, "2"));
        param1.addChild(fac.createOMText(payload, "2"));
        payload.addChild(param0);
        payload.addChild(param1);

        try {
            OMElement result;

            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            opts.setTo(new EndpointReference(HTTP_BPS_STRATOSLIVE_URL + "/FunctionProcessServiceService/"));
            opts.setAction("http://www.example.org/messages/FunctionProcessServiceOperation");

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);
            Thread.sleep(60000);
            if ((result.toString().indexOf("256")) > 0) {
                functionProcessServiceTestStatus = true;
            }
            assertTrue("FunctionProcessService BPEL invocation failed", functionProcessServiceTestStatus);

        } catch (AxisFault e) {
            log.error("FunctionProcessService  BPEL invocation failed :" + e.getMessage());
            fail("FunctionProcessService BPEL invocation failed " + e.getMessage());
        } catch (InterruptedException e) {
            log.error("Thread sleep interrupted :" + e.getMessage());
            fail("Thread sleep interrupted " + e.getMessage());
        }
        return functionProcessServiceTestStatus;
    }

    public static void loadFunctionProcessServiceTest() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on FunctionProcessService service failed", functionProcessServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }
}
