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

package org.wso2.carbon.system.test.core.utils.axis2Client;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AxisServiceClientUtils {

    public final static boolean TWO_WAY = true;
    public final static boolean ONE_WAY = false;
    private static final Log log = LogFactory.getLog(AxisServiceClientUtils.class);

    public static OMElement sendRequest(String payloadStr, EndpointReference targetEPR)
            throws XMLStreamException, AxisFault {
        OMElement payload = AXIOMUtil.stringToOM(payloadStr);
        Options options = new Options();
        options.setTo(targetEPR);
        //options.setAction("urn:" + operation); //since soapAction = ""

        //Blocking invocation
        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OMElement result = sender.sendReceive(payload);

        //log.info(result.toString());
        return result;
    }

    public static void sendRequestOneWay(String payloadStr, EndpointReference targetEPR)
            throws XMLStreamException, AxisFault {
        OMElement payload = AXIOMUtil.stringToOM(payloadStr);
        Options options = new Options();
        options.setTo(targetEPR);
        //options.setAction("urn:" + operation); //since soapAction = ""

        //Blocking invocation
        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        sender.fireAndForget(payload);
    }

    public static boolean isServiceAvailable(String serviceUrl) {
        URL wsdlURL;
        InputStream wsdlIS;
        try {
            wsdlURL = new URL(serviceUrl + "?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        try {
            wsdlIS = wsdlURL.openStream();
        } catch (IOException e) {
            return false;// do nothing, wait for the service
        }

        if (wsdlIS != null) {
            byte[] bLine = new byte[1024];
            int read;
            try {
                read = wsdlIS.read(bLine);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return false;
            }

            if (read > 0) {
                String wsdl = new String(bLine);
                if (wsdl.contains("definitions")) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static void waitForServiceDeployment(String serviceUrl) {
        int serviceTimeOut = 0;
        while (!isServiceAvailable(serviceUrl)) {
            if (serviceTimeOut == 0) {
            } else if (serviceTimeOut > 100) { //Check for the service for 100 seconds
                // if Service not available assertfalse;
                Assert.fail(serviceUrl + " service is not found");
                break;
            }
            try {
                Thread.sleep(500);
                serviceTimeOut++;
            } catch (InterruptedException e) {
                Assert.fail(e.getMessage());
            }
        }
    }

    public static void sendRequest(String eprUrl, String operation, String payload,
                                   int numberOfInstances, List<String> expectedStrings, boolean twoWay) {
        waitForServiceDeployment(eprUrl);
        Assert.assertFalse("Service not found: " + eprUrl, !AxisServiceClientUtils.isServiceAvailable(eprUrl));

        for (int i = 0; i < numberOfInstances; i++) {
            try {
                EndpointReference epr = new EndpointReference(eprUrl + "/" + operation);
                if (twoWay) {
                    OMElement result = AxisServiceClientUtils.sendRequest(payload, epr);
                    if (expectedStrings != null) {
                        for (String expectedString : expectedStrings) {
                            Assert.assertFalse("Incorrect Test Result: " + result.toString(),
                                    !result.toString().contains(expectedString));
                        }
                    }
                } else {
                    AxisServiceClientUtils.sendRequestOneWay(payload, epr);
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();
                Assert.fail(axisFault.getMessage());
            }
        }
    }

    public static void sendRequest(String eprUrl, String operation, String payload,
                                   int numberOfInstances, String expectedException, boolean twoWay) {
        Assert.assertFalse("Service not found: " + eprUrl, !AxisServiceClientUtils.isServiceAvailable(eprUrl));

        for (int i = 0; i < numberOfInstances; i++) {
            try {
                EndpointReference epr = new EndpointReference(operation);
                if (twoWay) {
                    OMElement result = AxisServiceClientUtils.sendRequest(payload, epr);
                    Assert.fail("Exception expected!!! : " + result.toString());

                } else {
                    AxisServiceClientUtils.sendRequestOneWay(payload, epr);
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
                if (!e.getClass().getSimpleName().equals(expectedException)) {
                    Assert.fail(e.getMessage());
                }
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();
                if (!axisFault.getClass().getSimpleName().equals(expectedException)) {
                    Assert.fail(axisFault.getMessage());
                }
            }

            if (expectedException != null) {
                Assert.fail("Exception expected. But not found!!");
            }
        }
    }

    public static void waitForServiceUnDeployment(String serviceUrl) {
        int serviceTimeOut = 0;

        while (isServiceAvailable(serviceUrl)) {
            if (serviceTimeOut == 0) {
            } else if (serviceTimeOut > 60) { //Check for the service for 100 seconds
                Assert.fail("Service undeployment fail");
                break;
            }
            try {
                Thread.sleep(500);
                serviceTimeOut++;
            } catch (InterruptedException e) {
                log.error("failed to undeploy service");
                Assert.fail(e.getMessage());
            }
        }
    }
}


    

    
    

