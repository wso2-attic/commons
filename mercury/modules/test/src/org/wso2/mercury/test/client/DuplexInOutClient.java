/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
package org.wso2.mercury.test.client;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPFault;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.client.MercuryClient;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;


public class DuplexInOutClient {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository";


    public void testDuplexInOutService1() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInOutService"));
            serviceClient.getOptions().setAction("urn:TestInOutOperation");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.engageModule("Mercury");
            for (int i = 0; i < 11; i++) {
                sendAsynchornousMessage(serviceClient, i, "Key1");
                sendAsynchornousMessage(serviceClient, i, "Key2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sendAsynchornousMessage(serviceClient, 11, "Key1");
            sendAsynchornousMessage(serviceClient, 11, "Key2");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

     public void testDuplexInOutService3() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInOutService"));
            serviceClient.getOptions().setAction("urn:TestInOutOperation");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.engageModule("Mercury");
            for (int i = 0; i < 11; i++) {
                sendAsynchornousMessage(serviceClient, i, "Key1");
                sendAsynchornousMessage(serviceClient, i, "Key2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            sendAsynchornousMessage(serviceClient, 11, "Key1");
            sendAsynchornousMessage(serviceClient, 11, "Key2");

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");
            mercuryClient.terminateSequence("Key2");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testRMTestInOutService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/wso2/services/TestInOutService"));
            serviceClient.getOptions().setAction("urn:TestInOutOperation");
            serviceClient.engageModule("Mercury");
            OMElement result = serviceClient.sendReceive(getTestOMElement(1));
            System.out.println("Got the om element ==> " + result);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testSMTPRMAsyncTestInOutService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(
                    new EndpointReference("mailto:oiositest2@wso2.com?x-service-path=/wso2/services/TestInOutService"));
            serviceClient.getOptions().setAction("urn:TestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.engageModule("Mercury");
            for (int i = 1; i < 10; i++) {
                sendAsynchornousMessage(serviceClient, i, "Key1");
                sendAsynchornousMessage(serviceClient, i, "Key2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sendAsynchornousMessage(serviceClient, 11, "Key1");
            sendAsynchornousMessage(serviceClient, 101, "Key2");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testDuplexInOutService2() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInOutService"));
            serviceClient.getOptions().setAction("urn:TestInOutOperation");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.SEQUENCE_OFFER, Constants.VALUE_TRUE);
//            for (int i = 0; i < 10; i++) {
//                sendAsynchornousMessage(serviceClient, i, "Key1");
//                sendAsynchornousMessage(serviceClient, i, "Key2");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                }
//            }
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sendAsynchornousMessage(serviceClient, 11, "Key1");
//            sendAsynchornousMessage(serviceClient, 11, "Key2");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testSecureInOutService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/SecureTestInOutService"));
            serviceClient.getOptions().setAction("urn:SecureTestInOutOperation");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.engageModule("Mercury");

            serviceClient.engageModule("rampart");

            StAXOMBuilder builder = new StAXOMBuilder("conf/policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY, policy);

            for (int i = 0; i < 11; i++) {
                sendAsynchornousMessage(serviceClient, i, "Key1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sendAsynchornousMessage(serviceClient, 11, "Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void testFaultInOutService() {
        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestFaultInOutService"));
            serviceClient.getOptions().setAction("urn:TestFaultInOutOperation");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sendAsynchornousMessage(serviceClient, 1, "Key1");

            try {
                Thread.sleep(2000000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void sendAsynchornousMessage(ServiceClient serviceClient, int i, String key) throws AxisFault {
        serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, key);
        AxisCallback axisCallback = new AxisCallback() {
            public void onMessage(MessageContext msgContext) {
                System.out.println("Got the message ==> " + msgContext.getEnvelope().getBody().getFirstElement());
            }

            public void onFault(MessageContext msgContext) {
                System.out.println("Got the fault ==> " + msgContext.getEnvelope().getBody().getFault().getDetail());
            }

            public void onError(Exception e) {
                e.printStackTrace();
            }

            public void onComplete() {

            }
        };
        serviceClient.sendReceiveNonBlocking(getTestOMElement(key + " " + i + " "), axisCallback);
    }

    private OMElement getTestOMElement(String text) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + text);
        return omElement;
    }

    private OMElement getTestOMElement(long number) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("test element " + number);
        return omElement;
    }

    public static void main(String[] args) {
//        new DuplexInOutClient().testDuplexInOutService1();
        new DuplexInOutClient().testDuplexInOutService2();
//        new DuplexInOutClient().testDuplexInOutService3();
//        new DuplexInOutClient().testSecureInOutService();
//        new DuplexInOutClient().testFaultInOutService();
    }


}
