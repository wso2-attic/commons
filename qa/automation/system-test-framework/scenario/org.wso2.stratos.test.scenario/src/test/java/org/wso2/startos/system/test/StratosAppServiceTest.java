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
import org.wso2.carbon.admin.service.AdminServiceSecurity;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.security.mgt.stub.config.SecurityAdminServiceSecurityConfigExceptionException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.TestConfigurationReader;
import org.wso2.startos.system.test.stratosUtils.ServiceLoginClient;
import org.wso2.startos.system.test.stratosUtils.asUtils.asSecurityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;


public class StratosAppServiceTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosAppServiceTest.class);
    private static String APPSERVER_STRATOS_URL;
    private static String HTTP_APPSERVER_STRATOSLIVE_URL;

    @Override
    public void init() {
        log.info("Running Appserver regression test");
        testClassName = StratosAppServiceTest.class.getName();
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        APPSERVER_STRATOS_URL = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain();
        HTTP_APPSERVER_STRATOSLIVE_URL = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain();
    }

    @Override
    public void runSuccessCase() {
        String appServerHostName = FrameworkSettings.APP_SERVER_HOST_NAME;

        String sessionCookie = ServiceLoginClient.loginChecker(appServerHostName);
        webappTest();
        JARServiceTest();
        JAXWSTest();
        axis2ServiceTest();
        springServiceTest();
        loadTestAxis2Service();
        loadTestJARService();
        loadTestJAXWSService();
        loadTestSpringService();
        loadTestWebApp();
//        securityTestService(sessionCookie, appServerHostName);
    }

    @Override
    public void cleanup() {
    }

    private static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "ns1");
        OMElement method = fac.createOMElement("echoString", omNs);
        OMElement value = fac.createOMElement("s", omNs);
        value.addChild(fac.createOMText(value, "Hello World"));
        method.addChild(value);
        return method;
    }

    private static boolean webappTest() {
        URL webAppURL;
        BufferedReader in;
        boolean webappStatus = false;


        try {
            log.info("Invoking webapp on app server service");
            webAppURL = new URL(APPSERVER_STRATOS_URL + "/webapps/SimpleServlet/simple-servlet");
            URLConnection yc;
            yc = webAppURL.openConnection();

            in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.indexOf("Hello, World") > 1) {
                    webappStatus = true;
                    break;
                }
            }

            in.close();
            assertTrue("Web app invocation failed", webappStatus);
        } catch (IOException e) {
            log.error("Web app invocation failed: IO Exception" + e.getMessage());
            fail("Web app invocation failed: IO Exception" + e.getMessage());
        }
        return webappStatus;
    }

    private static boolean axis2ServiceTest() {
        boolean axis2ServiceStatus = false;

        OMElement result;
        try {
            OMElement payload = createPayLoad();
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_APPSERVER_STRATOSLIVE_URL + "/Axis2Service"));
            opts.setAction("http://service.carbon.wso2.org/echoString");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Hello World")) > 0) {
                axis2ServiceStatus = true;
            }
            assertTrue("Axis2Service invocation failed", axis2ServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("Axis2Service invocation failed :" + axisFault.getMessage());
            fail("Axis2Service invocation failed :" + axisFault.getMessage());
        }

        return axis2ServiceStatus;
    }


    private static boolean JAXWSTest() {
        boolean JAXWSStatus = false;

        OMElement result;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://jaxws.carbon.wso2.org", "ns1");
        OMElement payload = fac.createOMElement("echoInt", omNs);
        OMElement value1 = fac.createOMElement("arg0", omNs);
        value1.addChild(fac.createOMText(value1, "1"));
        payload.addChild(value1);


        ServiceClient serviceclient;
        try {
            serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_APPSERVER_STRATOSLIVE_URL + "/JaxWSTestService"));
            opts.getTo();
            opts.setAction("echoInt");
            //bypass http protocol exception
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("1")) > 0) {
                JAXWSStatus = true;
            }
            assertTrue("Jax-WS service invocation failed", JAXWSStatus);

        } catch (AxisFault axisFault) {
            log.error("Jax-WS service invocation failed:" + axisFault.getMessage());
            fail("Jax-WS service invocation failed:" + axisFault.getMessage());
        }
        return JAXWSStatus;
    }

    private static boolean JARServiceTest() {
        boolean JARServiceStatus = false;
        OMElement result;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.apache.org/axis2", "ns1");
        OMElement payload = fac.createOMElement("echo", omNs);
        OMElement value = fac.createOMElement("args0", omNs);
        value.addChild(fac.createOMText(value, "Hello-World"));
        payload.addChild(value);

        ServiceClient serviceclient;
        try {
            serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_APPSERVER_STRATOSLIVE_URL + "/SimpleJarService"));
            opts.setAction("echo");
            //bypass http protocol exception
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Hello-World")) > 0) {
                JARServiceStatus = true;
            }
            assertTrue("Jar service invocation failed", JARServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("Jar service invocation failed:" + axisFault.getMessage());
            fail("Jar service invocation failed:" + axisFault.getMessage());
        }
        return JARServiceStatus;
    }

    private static boolean springServiceTest() {
        boolean springServiceStatus = false;
        OMElement result;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://echo.services.tungsten.wso2.com", "ns1");
        OMElement payload = fac.createOMElement("echoString", omNs);
        OMElement value = fac.createOMElement("in", omNs);
        value.addChild(fac.createOMText(value, "Hello World!"));
        payload.addChild(value);

        ServiceClient serviceclient;
        try {
            serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_APPSERVER_STRATOSLIVE_URL + "/echoBean"));
            opts.getTo();
            opts.setAction("echoString");
            //bypass http protocol exception
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Hello World!")) > 0) {
                springServiceStatus = true;
            }
            assertTrue("Jar service invocation failed", springServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("Spring service invocation failed:" + axisFault.getMessage());
            fail("Spring service invocation failed:" + axisFault.getMessage());
        }
        return springServiceStatus;
    }

    private static void loadTestAxis2Service() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on axis2service failed", axis2ServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    private static void loadTestWebApp() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on webapp failed", webappTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    private static void loadTestJAXWSService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Jax-WS service failed", JAXWSTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    private static void loadTestJARService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on jar service failed", JARServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    private static void loadTestSpringService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on spring service failed", springServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    private static void securityTestService(String sessionCookie, String securityAdminServiceURL) {
        OMElement result;
        String serviceEndpoint = "https://appserver.stratoslive.wso2.com/services/t/manualQA0001.org/SimpleService/";
        String clientKeyName = "wso2AloadPolicyutoClient.jks";
        String clientkeyPassword = "Admin123";
        String[] trustedKeyStore = {"wso2carbon.jks"};
        String privateStore = "wso2Autoservice.jks";
        String[] group = {"admin123"};
        String serviceName = "SimpleService";


        try {
            for (int scenarioNum = 1; scenarioNum <= 16; scenarioNum++) {

                AdminServiceSecurity adminServiceSecurity = new AdminServiceSecurity(securityAdminServiceURL);
                adminServiceSecurity.applySecurity(sessionCookie, serviceName, String.valueOf(scenarioNum), group, trustedKeyStore, privateStore);

                result = asSecurityUtils.runSecurityClient(scenarioNum, serviceEndpoint, "urn:echo",
                        "   <p:echo xmlns:p=\"http://ws.apache.org/axis2\">\n" +
                                "      <!--0 to 1 occurrence-->\n" +
                                "      <xs:args0 xmlns:xs=\"http://ws.apache.org/axis2\">Hello World, 123 !!!</xs:args0>\n" +
                                "   </p:echo>", clientKeyName, clientkeyPassword);
                System.out.println(scenarioNum);
                assertFalse("Incorrect Test Result: " + result.toString(),
                        !result.toString().contains("Hello World, 123 !!!"));

                Thread.sleep(5000);
                adminServiceSecurity.disableSecurity(sessionCookie, serviceName);
                Thread.sleep(5000);
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            log.error("Axis2 Exception login failed" + axisFault.getMessage());
            fail("Axis2 Exception login failed" + axisFault.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("RMI exception" + e.getMessage());
            fail("Securiy invocation failed" + e.getMessage());
        } catch (SecurityAdminServiceSecurityConfigExceptionException e) {
            e.printStackTrace();
            log.error("Security admin services failed" + e.getMessage());
            fail("Security Admin services failed" + e.getMessage());
        } catch (Exception e1) {
            e1.printStackTrace();
            log.error("Security failed" + e1.getMessage());
            fail("Security failed" + e1.getMessage());
        }
    }
}



