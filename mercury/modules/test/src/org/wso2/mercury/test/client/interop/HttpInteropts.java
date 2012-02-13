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

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.client.MercuryClient;
import org.wso2.mercury.callback.MercuryErrorCallback;
import org.wso2.mercury.callback.MercuryTerminateCallback;
import org.wso2.mercury.exception.RMException;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class HttpInteropts {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository_client";


    private void testPlainText() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointPlain.svc"));
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/PlainTestInOutService"));
            serviceClient.getOptions().setAction("urn:PlainTestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8088/InteropTest2/OiosiOmniEndpointPlain.svc");
            
            sendAsynchornousMessage(serviceClient, 101, "Key1");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testPlainFault() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointPlainFault.svc"));
            serviceClient.getOptions().setAction("urn:PlainTestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8078/InteropTest2/OiosiOmniEndpointPlainFault.svc");
            sendAsynchornousMessage(serviceClient, 101, "Key1");
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
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRM"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            MercuryTerminateCallback terminateCallBack = new MercuryTerminateCallback(){

                public void onComplete() {
                    System.out.println("Terminate message received");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.TERMINATE_CALLBACK, terminateCallBack);

            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            for (int i = 0; i < 1; i++) {
                OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1 " + i));
                System.out.println("OMElement ==>" + omElement.toString());
            }

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
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
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpoint60SecDelayNoSecurity.svc"));
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMDelay"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpoint60SecDelayNoSecurity.svc");
            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSMaximumRetransmitCount(10);

            MercuryErrorCallback errorCallback = new MercuryErrorCallback() {

                public void onError(RMException e) {
                    e.printStackTrace();
                    System.out.println("Got a sequence error");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.ERROR_CALLBACK, errorCallback);


            serviceClient.engageModule("Mercury");
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1 1"));
            System.out.println("OMElement ==>" + omElement.toString());

            mercuryClient.terminateSequence("Key1");


        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

        try {
            System.out.println("Waiting thread to sleep");
            Thread.sleep(200000);
        } catch (InterruptedException e) {
        }
        
    }

    private void testRMFault() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoSecurityFault.svc"));
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestFaultInOutService"));
            serviceClient.getOptions().setAction("urn:TestFaultInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointNoSecurityFault.svc");

            serviceClient.engageModule("Mercury");
            sendAsynchornousMessage(serviceClient, 101, "Key1");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {}

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testRMToNonRM() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointPlain.svc"));
            serviceClient.getOptions().setAction("urn:PlainTestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointPlain.svc");

            serviceClient.engageModule("Mercury");

            //send the error callback
            MercuryErrorCallback errorCallback = new MercuryErrorCallback(){

                public void onError(RMException e) {
                    e.printStackTrace();
                    System.out.println("Got a sequence error");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.ERROR_CALLBACK, errorCallback);
            sendAsynchornousMessage(serviceClient, 101, "Key1");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {}

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testPlainSecurity() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoRm.svc"));
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceSecurity"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8078/InteropTest2/OiosiOmniEndpointNoRm.svc");

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicySubject().attachPolicy(policy);

            sendAsynchornousMessage(serviceClient, 101, "Key1");
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

    private void testRMSecurity() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointA.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8078/InteropTest2/OiosiOmniEndpointA.svc");

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY,policy);

            serviceClient.engageModule("Mercury");

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1" + " " + 1 + " "));
            System.out.println("OMElement ==> " + omElement);

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
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
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurityDelay2"));
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpoint60SecDelay.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8078/InteropTest2/OiosiOmniEndpoint60SecDelay.svc");

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY,policy);

            serviceClient.engageModule("Mercury");

            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "Key1");
            OMElement omElement = serviceClient.sendReceive(getTestOMElement("Key1" + " " + 1 + " "));
            System.out.println("OMElement ==> " + omElement);

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
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

    private void testRMToSecurity() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoRm.svc"));
            serviceClient.getOptions().setAction("urn:PlainTestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointNoRm.svc");

            serviceClient.engageModule("Mercury");

            //send the error callback
            MercuryErrorCallback errorCallback = new MercuryErrorCallback() {

                public void onError(RMException e) {
                    e.printStackTrace();
                    System.out.println("Got a sequence error");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.ERROR_CALLBACK, errorCallback);
            sendAsynchornousMessage(serviceClient, 101, "Key1");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testRMToRMSecurity() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointA.svc"));
            serviceClient.getOptions().setAction("urn:PlainTestInOutOperation");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointA.svc");

            serviceClient.engageModule("Mercury");

            //send the error callback
            MercuryErrorCallback errorCallback = new MercuryErrorCallback() {

                public void onError(RMException e) {
                    e.printStackTrace();
                    System.out.println("Got a sequence error");
                }
            };

            serviceClient.getOptions().setProperty(MercuryClientConstants.ERROR_CALLBACK, errorCallback);
            sendAsynchornousMessage(serviceClient, 101, "Key1");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.terminateSequence("Key1");

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }


    private void testRMSecurityToPlain() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointPlain.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointPlain.svc");

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

    private void testRMSecurityToSec() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoRm.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointNoRm.svc");

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

    private void testRMSecurityToRM() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoSecurity.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL, "http://localhost:8078/InteropTest2/OiosiOmniEndpointNoSecurity.svc");

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

    private void testPlainTextToRM() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRM"));
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoSecurity.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8088/InteropTest2/OiosiOmniEndpointNoSecurity.svc");
            sendAsynchornousMessage(serviceClient, 101, "Key1");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testPlainTextToSecureRM() {
            try {

                ConfigurationContext configurationContext =
                        ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                                AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
                ServiceClient serviceClient = new ServiceClient(configurationContext, null);
                serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoSecurity.svc"));
                serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
                serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8088/InteropTest2/OiosiOmniEndpointNoSecurity.svc");
                sendAsynchornousMessage(serviceClient, 101, "Key1");
                try {
                    System.out.println("Waiting thread to sleep");
                    Thread.sleep(200000);
                } catch (InterruptedException e) {

                }
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();
            }
        }

    private void testPlainTextToSecurity() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceSecurity"));
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoSecurity.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8088/InteropTest2/OiosiOmniEndpointNoSecurity.svc");
            sendAsynchornousMessage(serviceClient, 101, "Key1");
            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    private void testSecureToSecureRM() {
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/InteropServiceRMSecurity"));
//            serviceClient.setTargetEPR(new EndpointReference("http://10.100.1.28/InteropTest2/OiosiOmniEndpointNoSecurity.svc"));
            serviceClient.getOptions().setAction("http://rep.oio.dk/oiosi.ehandel.gov.dk/xml/schemas/2007/09/01/Invoice201Interface/SubmitInvoiceRequest");
            serviceClient.getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,"http://localhost:8088/InteropTest2/OiosiOmniEndpointNoSecurity.svc");

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/interop/security-policy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicyInclude().addPolicyElement(PolicyInclude.AXIS_SERVICE_POLICY, policy);

            sendAsynchornousMessage(serviceClient, 101, "Key1");
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


    private void sendAsynchornousMessage(ServiceClient serviceClient, int i, String key) throws AxisFault {
        serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, key);
        AxisCallback axisCallback = new AxisCallback() {
            public void onMessage(MessageContext msgContext) {
                System.out.println("Got the message ==> " + msgContext.getEnvelope().getBody().getFirstElement());
            }

            public void onFault(MessageContext msgContext) {

            }

            public void onError(Exception e) {
//                e.printStackTrace();
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

    private OMElement getTestOMElement(String text) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + text);
        return omElement;
    }

    public static void main(String[] args) {
//       new HttpInteropts().testPlainText();
       new HttpInteropts().testRM();
//       new HttpInteropts().testRMDelay();
//       new HttpInteropts().testRMFault();
//       new HttpInteropts().testPlainFault();
//       new HttpInteropts().testRMToNonRM();
//       new HttpInteropts().testPlainSecurity();
//       new HttpInteropts().testRMSecurity();
//       new HttpInteropts().testRMSecurityDelay();
//       new HttpInteropts().testRMToSecurity();
//       new HttpInteropts().testRMToRMSecurity();
//       new HttpInteropts().testRMSecurityToPlain();
//       new HttpInteropts().testRMSecurityToSec();
//       new HttpInteropts().testRMSecurityToRM();
//       new HttpInteropts().testPlainTextToRM();
//       new HttpInteropts().testPlainTextToSecureRM();
//       new HttpInteropts().testSecureToSecureRM();
//       new HttpInteropts().testPlainTextToSecurity();
    }
}
