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

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.client.MercuryClient;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class AsyncInOnlyClient {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository";


    private void inOnlyAsyncPlainClient() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/PlainTestInService"));
            serviceClient.getOptions().setAction("urn:PlainTestInOperation");
            serviceClient.engageModule("MessageDropModule");
            serviceClient.fireAndForget(getTestOMElement("org"));

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testInOnlySMTPAsyncPlainClient() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:oiositest2@wso2.com?x-service-path=/axis2/services/TestInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");
            serviceClient.engageModule("MessageDropModule");
            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement("Test"));

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testSecureAsyncInOnlyClient() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/SecureTestInService"));
            serviceClient.getOptions().setAction("urn:SecureTestInOperation");

            serviceClient.engageModule("rampart");

            StAXOMBuilder builder = new StAXOMBuilder("conf/policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY, policy);

            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");

            for (int i = 1; i < 6; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(6));

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

    private void testAsyncInOnlyClient1() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");

            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");

            for (int i = 1; i < 6; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSSequenceRetransmitTime(20000);
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(6));

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testAsyncInOnlyClient2() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");
            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");

            for (int i = 1; i < 6; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            serviceClient.fireAndForget(getTestOMElement(6));
            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private OMElement getTestOMElement(long number) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + number);
        return omElement;
    }


    private OMElement getTestOMElement(String text) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + text);
        return omElement;
    }

    public static void main(String[] args) {
//        new AsyncInOnlyClient().inOnlyAsyncPlainClient();
        new AsyncInOnlyClient().testAsyncInOnlyClient1();
//        new AsyncInOnlyClient().testAsyncInOnlyClient2();
//        new AsyncInOnlyClient().testSecureAsyncInOnlyClient();
//        new AsyncInOnlyClient().testInOnlySMTPAsyncPlainClient();
    }
}
