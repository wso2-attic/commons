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

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.client.MercuryClient;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class DuplexInOnlyClient {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository_client";


    public void testDuplexInOnlyService1() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");
            serviceClient.engageModule("Mercury");
            serviceClient.engageModule("MessageDropModule");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            for (int i = 1; i < 100; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            serviceClient.fireAndForget(getTestOMElement(11));
            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(20000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testDuplexInOnlyService2() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");
            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            System.out.println("Invoke client using thread ==> " + Thread.currentThread().getName());
            for (int i = 1; i < 10; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(11));

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testSMTPInOnlyService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(
                    new EndpointReference("mailto:oiositest2@wso2.com?x-service-path=/axis2/services/PlainTestInService"));
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setAction("urn:PlainTestInOperation");
            serviceClient.getOptions().setUseSeparateListener(true);

            for (int i = 0; i < 10; i++) {
                serviceClient.fireAndForget(getTestOMElement(301));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {}
            }

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testSMTPDuplexInOnlyService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(
                    new EndpointReference("mailto:oiositest2@wso2.com?x-service-path=/axis2/services/TestInService"));
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setAction("urn:TestInOperation");
             serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setUseSeparateListener(true);

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            for (int i = 0; i < 100; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }

            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(101));

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void testSecureInOnlyService(){
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
            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY,policy);

            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setUseSeparateListener(true);

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(11));
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

    private OMElement getTestOMElement(long number) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + number);
        return omElement;
    }

    public static void main(String[] args) {
//        new DuplexInOnlyClient().testDuplexInOnlyService1();
        new DuplexInOnlyClient().testDuplexInOnlyService2();
//        new DuplexInOnlyClient().testSMTPInOnlyService();
//        new DuplexInOnlyClient().testSMTPDuplexInOnlyService();
//        new DuplexInOnlyClient().testSecureInOnlyService();
    }
}
