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

import me.prettyprint.hector.api.Cluster;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
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
import org.wso2.startos.system.test.hectorClient.ExampleHelper;
import org.wso2.startos.system.test.hectorClient.HectorExample;
import org.wso2.startos.system.test.stratosUtils.ServiceLoginClient;

import javax.xml.stream.XMLStreamException;
import java.util.Scanner;


public class StratosDSSServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosDSSServiceTest.class);
    private static String HTTP_DATA_STRATOS_URL;

    @Override
    public void init() {
        log.info("Running DSS regression test");
        testClassName = StratosDSSServiceTest.class.getName();
        int tenantId = TenantListCsvReader.getTenantId("5");
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(tenantId);
        HTTP_DATA_STRATOS_URL = "http://" + FrameworkSettings.DSS_SERVER_HOST_NAME + "/services" + "/t/" + tenantDetails.getTenantDomain();
    }

    @Override
    public void runSuccessCase() {
        String dssServerHostName = FrameworkSettings.DSS_SERVER_HOST_NAME;
        ServiceLoginClient.loginChecker(dssServerHostName);
        googleSpreadsheetService();
        rssDataServiceTest();
        csvDataServiceTest();
        excelDataServiceTest();
        restDataServiceTest();
        HectorExample.executeKeySpaceSample();
        loadTestCSVService();
        loadTestExcelService();
        loadTestGSSampleService();
        loadTestRestService();
        loadTestRSSampleService();
    }

    @Override
    public void cleanup() {
    }

    private static boolean rssDataServiceTest() {
        boolean rssDataServiceStatus = false;
        OMElement result;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://test.org", "ns1");
        OMElement payload = fac.createOMElement("getEmployeeDepartments", omNs);
        OMElement value = fac.createOMElement("Dep_Name", omNs);
        value.addChild(fac.createOMText(value, "Dep1"));
        payload.addChild(value);


        try {
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_DATA_STRATOS_URL + "/CompanySampleDS/"));
            opts.getTo();
            opts.setAction("getEmployeeDepartments");
            //bypass http protocol exception
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Jayasuriya")) > 0) {
                rssDataServiceStatus = true;
            }
            assertTrue("RSS data service invocation failed", rssDataServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("RSS data service invocation failed : " + axisFault.getMessage());
            fail("RSS data service invocation failed : " + axisFault.getMessage());
        }
        return rssDataServiceStatus;
    }

    private static boolean csvDataServiceTest() {
        boolean csvDataServiceStatus = false;

        String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                " <soapenv:Header/>\n" +
                " <soapenv:Body/>\n" +
                "</soapenv:Envelope>";

        String action = "getProducts";

        try {
            OMElement result;
            result = sendRequest(payload, action, new EndpointReference(HTTP_DATA_STRATOS_URL + "/CSVSampleService"));

            if ((result.toString().indexOf("1969 Harley Davidson Ultimate Chopper")) > 0) {
                csvDataServiceStatus = true;
            }
            assertTrue("CSV data service invocation failed", csvDataServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("CSV data service invocation failed : " + axisFault.getMessage());
            fail("CSV data service invocation failed : " + axisFault.getMessage());
        } catch (XMLStreamException xmlStreamFault) {
            log.error("CSV data service invocation failed : " + xmlStreamFault.getMessage());
            fail("CSV data service invocation failed : " + xmlStreamFault.getMessage());
        }

        return csvDataServiceStatus;
    }

    private static boolean excelDataServiceTest() {
        boolean excelDataServiceStatus = false;

        String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                " <soapenv:Header/>\n" +
                " <soapenv:Body/>\n" +
                "</soapenv:Envelope>";

        String action = "getProducts";

        try {
            OMElement result;
            result = sendRequest(payload, action, new EndpointReference(HTTP_DATA_STRATOS_URL + "/ExcelSampleService"));

            if ((result.toString().indexOf("1952 Alpine Renault 1300")) > 0) {
                excelDataServiceStatus = true;
            }
            assertTrue("EXCEL DataService invocation failed", excelDataServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("EXCEL DataService invocation failed :" + axisFault.getMessage());
            fail("EXCEL DataService invocation failed :" + axisFault.getMessage());
        } catch (XMLStreamException xmlStreamFault) {
            log.error("EXCEL Data service invocation failed : " + xmlStreamFault.getMessage());
            fail("EXCEL Data service invocation failed : " + xmlStreamFault.getMessage());
        }
        return excelDataServiceStatus;
    }

    private static boolean restDataServiceTest() {
        boolean restDataServiceStatus = false;
        OMElement result;
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://product.abc.com", "p");
        OMElement payload = fac.createOMElement("_getproduct_productcode", omNs);
        OMElement value = fac.createOMElement("productCode", omNs);
        value.addChild(fac.createOMText(value, "S12_1108"));
        payload.addChild(value);

        ServiceClient serviceclient;
        try {
            serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(HTTP_DATA_STRATOS_URL + "/Rest_Sample/"));
            opts.getTo();
            opts.setAction("urn:_getproduct_productcode");
            //bypass http protocol exception
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("2001 Ferrari Enzo")) > 0) {
                restDataServiceStatus = true;
            }
            assertTrue("REST DataService invocation failed", restDataServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("REST DataService invocation failed :" + axisFault.getMessage());
            fail("REST DataService invocation failed :" + axisFault.getMessage());
        }
        return restDataServiceStatus;
    }

    public static Boolean googleSpreadsheetService() {
        Boolean googleSpreadsheetServiceStatus = false;
        OMElement result;
        OMElement payload = createPayLoad();
        try {
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();
            opts.setTo(new EndpointReference(HTTP_DATA_STRATOS_URL + "/GSpreadSample/"));
            opts.setAction("http://ws.wso2.org/dataservice/getCustomers");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Signal Gift Stores")) > 0) {
                googleSpreadsheetServiceStatus = true;
            }

            assertTrue("GSSample DataService invocation failed", googleSpreadsheetServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("GSSample DataService invocation failed :" + axisFault.getMessage());
            fail("GSSample DataService invocation failed :" + axisFault.getMessage());
        }
        return googleSpreadsheetServiceStatus;
    }

    public static void loadTestGSSampleService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on GSSample service failed", googleSpreadsheetService());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static void loadTestRSSampleService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on RSS Sample service failed", rssDataServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static void loadTestCSVService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on CSV Sample service failed", csvDataServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static void loadTestExcelService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Excel Sample service failed", excelDataServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static void loadTestRestService() {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread() {

                public void run() {
                    for (int i = 0; i < 5; i++) {
                        assertTrue("Load test on Rest Sample service failed", restDataServiceTest());
                    }
                }
            };
            clientThread.start();
        }
    }

    public static OMElement sendRequest(String payloadStr, String action, EndpointReference targetEPR)
            throws XMLStreamException, AxisFault {
        OMElement payload = AXIOMUtil.stringToOM(payloadStr);
        Options options = new Options();
        options.setTo(targetEPR);
        options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
        options.setAction("urn:" + action); //since soapAction = ""
        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OMElement result = sender.sendReceive(payload);

        return result;
    }

    public static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        return fac.createOMElement("getCustomers", omNs);
    }
}
