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
package org.wso2.mercury.test.client.interop;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.mercury.client.MercuryClient;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.callback.MercuryErrorCallback;
import org.wso2.mercury.exception.RMException;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;


public class SMTPInteropts {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository_client";


    private void testPlainText() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:nitaserver@wso2.com"));
            serviceClient.getOptions().setAction("urn:PlainTestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            serviceClient.getAxisService().addParameter("transport.mail.Address", "nitaclient@wso2.com");
            serviceClient.getAxisService().addParameter("transport.mail.Protocol", "pop3");
            serviceClient.getAxisService().addParameter("transport.PollInterval", "2");
            serviceClient.getAxisService().addParameter("mail.pop3.host", "pop.wso2.com");
            serviceClient.getAxisService().addParameter("mail.pop3.user", "nitaclient");
            serviceClient.getAxisService().addParameter("mail.pop3.password", "NitaClient");
            serviceClient.getOptions().setTimeOutInMilliSeconds(200000);

//            sendAsynchornousMessage(serviceClient, 101, "Key1");
            serviceClient.sendReceive(getTestOMElement("key 1"));
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testRM() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:nitaserver@wso2.com"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            serviceClient.engageModule("Mercury");

            serviceClient.getAxisService().addParameter("transport.mail.Address", "nitaclient@wso2.com");
            serviceClient.getAxisService().addParameter("transport.mail.Protocol", "pop3");
            serviceClient.getAxisService().addParameter("transport.PollInterval", "2");
            serviceClient.getAxisService().addParameter("mail.pop3.host", "pop.wso2.com");
            serviceClient.getAxisService().addParameter("mail.pop3.user", "nitaclient");
            serviceClient.getAxisService().addParameter("mail.pop3.password", "NitaClient");

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSSequenceRetransmitTime(20000);

            sendAsynchornousMessage(serviceClient, 101, "Key1");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {}

            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

     private void testRMDelay() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:nitaserver@wso2.com"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setTimeOutInMilliSeconds(120000);

            serviceClient.engageModule("Mercury");

            serviceClient.getAxisService().addParameter("transport.mail.Address", "nitaclient@wso2.com");
            serviceClient.getAxisService().addParameter("transport.mail.Protocol", "pop3");
            serviceClient.getAxisService().addParameter("transport.PollInterval", "2");
            serviceClient.getAxisService().addParameter("mail.pop3.host", "pop.wso2.com");
            serviceClient.getAxisService().addParameter("mail.pop3.user", "nitaclient");
            serviceClient.getAxisService().addParameter("mail.pop3.password", "NitaClient");

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSSequenceRetransmitTime(20000);


            sendAsynchornousMessage(serviceClient, 101, "Key1");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}

            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
            } catch (InterruptedException e) {

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

            }

            public void onError(Exception e) {
                e.printStackTrace();
                System.out.println("Received an error ...");
                if (e instanceof AxisFault) {
                    System.out.println("Got the error message ==> " + ((AxisFault) e).getDetail());
                }

            }

            public void onComplete() {

            }
        };
        serviceClient.sendReceiveNonBlocking(getTestOMElement(key + " " + i + " "), axisCallback);
    }

    private void testRMSecurity() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:nitaserver@wso2.com"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setTimeOutInMilliSeconds(200000);

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicySubject().attachPolicy(policy);

            serviceClient.engageModule("Mercury");
            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSSequenceRetransmitTime(30000);

            serviceClient.getAxisService().addParameter("transport.mail.Address", "nitaclient@wso2.com");
            serviceClient.getAxisService().addParameter("transport.mail.Protocol", "pop3");
            serviceClient.getAxisService().addParameter("transport.PollInterval", "2");
            serviceClient.getAxisService().addParameter("mail.pop3.host", "pop.wso2.com");
            serviceClient.getAxisService().addParameter("mail.pop3.user", "nitaclient");
            serviceClient.getAxisService().addParameter("mail.pop3.password", "NitaClient");

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
//            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1" + " " + 1 + " "));
            System.out.println("OMElement ==> " + omElement);

            mercuryClient.terminateSequence("Key1");
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

     private void testRMSecurityDelay() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:nitaserver@wso2.com"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicySubject().attachPolicy(policy);
            serviceClient.getOptions().setTimeOutInMilliSeconds(200000);

            serviceClient.engageModule("Mercury");
            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSSequenceRetransmitTime(30000);

            serviceClient.getAxisService().addParameter("transport.mail.Address", "nitaclient@wso2.com");
            serviceClient.getAxisService().addParameter("transport.mail.Protocol", "pop3");
            serviceClient.getAxisService().addParameter("transport.PollInterval", "2");
            serviceClient.getAxisService().addParameter("mail.pop3.host", "pop.wso2.com");
            serviceClient.getAxisService().addParameter("mail.pop3.user", "nitaclient");
            serviceClient.getAxisService().addParameter("mail.pop3.password", "NitaClient");

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1" + " " + 1 + " "));
            System.out.println("OMElement ==> " + omElement);

            mercuryClient.terminateSequence("Key1");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(2000000);
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

    private void testSecurity(){
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("mailto:nitaserver@wso2.com"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
             serviceClient.getOptions().setTimeOutInMilliSeconds(120000);

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicySubject().attachPolicy(policy);

            MercuryErrorCallback errorCallback = new MercuryErrorCallback() {

                public void onError(RMException e) {
                    e.printStackTrace();
                    System.out.println("Got a sequence error");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.ERROR_CALLBACK, errorCallback);

            serviceClient.getAxisService().addParameter("transport.mail.Address", "nitaclient@wso2.com");
            serviceClient.getAxisService().addParameter("transport.mail.Protocol", "pop3");
            serviceClient.getAxisService().addParameter("transport.PollInterval", "2");
            serviceClient.getAxisService().addParameter("mail.pop3.host", "pop.wso2.com");
            serviceClient.getAxisService().addParameter("mail.pop3.user", "nitaclient");
            serviceClient.getAxisService().addParameter("mail.pop3.password", "NitaClient");


            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1" + " " + 1 + " "));
            System.out.println("OMElement ==> " + omElement);


        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            System.out.println("Waiting thread to sleep");
            Thread.sleep(2000000);
        } catch (InterruptedException e) {

        }
    }

    private void testRMSecurityToSec() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
            serviceClient.setTargetEPR(new EndpointReference("mailto:oiositest2@wso2.com"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointNoRm.svc");

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY, policy);

            serviceClient.engageModule("Mercury");

            MercuryErrorCallback errorCallback = new MercuryErrorCallback() {

                public void onError(RMException e) {
                    e.printStackTrace();
                    System.out.println("Got a sequence error");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.ERROR_CALLBACK, errorCallback);

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1" + " " + 1 + " "));
            System.out.println("OMElement ==> " + omElement);

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            System.out.println("Waiting thread to sleep");
            Thread.sleep(2000000);
        } catch (InterruptedException e) {

        }
    }

    private OMElement getTestOMElement(String text) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + text);
        return omElement;
    }

    public static void main(String[] args) {
//       new SMTPInteropts().testPlainText();
//       new SMTPInteropts().testRM();
//       new SMTPInteropts().testRMDelay();
//       new SMTPInteropts().testSecurity();
//       new SMTPInteropts().testRMSecurity();
       new SMTPInteropts().testRMSecurityDelay();
//       new SMTPInteropts().testRMSecurityToSec();
    }

}
