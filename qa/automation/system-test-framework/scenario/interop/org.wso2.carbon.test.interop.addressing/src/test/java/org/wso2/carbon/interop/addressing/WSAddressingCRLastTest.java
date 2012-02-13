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

package org.wso2.carbon.interop.addressing;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tempuri.last.notify.WSAddressingCRStub;
import org.tempuri.last.echo.*;
import org.wso2.carbon.interop.common.InteropConstants;
import org.wso2.carbon.interop.common.InteropSettings;

public class WSAddressingCRLastTest extends WSAddressingCRTest {

    private static final Log log = LogFactory.getLog(WSAddressingCRLastTest.class);

    public void testCustomBinding_Notify() {
        try {
            WSAddressingCRStub stub = new WSAddressingCRStub(configurationContext);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);
//            stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
//                    "http://localhost:8085/WSAddressingCR_Service_WCF/WSAddressing10.svc/Soap11");
            stub.notify("Test String");
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void testCustomBinding_Notify1() {
        try {
            org.tempuri.last.notify1.WSAddressingCRStub stub =
                    new org.tempuri.last.notify1.WSAddressingCRStub(configurationContext);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);
            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//            stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
//                    "http://localhost:8085/WSAddressingCR_Service_WCF/WSAddressing10.svc/Soap12");
            stub.notify("Test String");
        } catch (AxisFault e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void testCustomBindingServicClient() {
        try {
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference(
                    InteropSettings.getProperty(InteropConstants.ENDPOINT_ADDRESSING_SOAP11)));
            serviceClient.getOptions().setAction("http://example.org/action/echoIn");
            serviceClient.getOptions().setProperty(AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);
            serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(InteropConstants.ENDPOINT_ADDRESSING_SOAP11));

            class ConditionalWait {

                private volatile boolean done;

                public synchronized void conditionSatisfied() {
                    done = true;
                    notifyAll();
                }

                public synchronized void waitForCondition() {
                    while (!done) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            final ConditionalWait conditionalWait = new ConditionalWait();

            serviceClient.sendReceiveNonBlocking(getOMElment(), new AxisCallback() {

                public void onMessage(MessageContext messageContext) {
                    conditionalWait.conditionSatisfied();
                }

                public void onFault(MessageContext messageContext) {
                    conditionalWait.conditionSatisfied();
                }

                public void onError(Exception exception) {
                    conditionalWait.conditionSatisfied();
                }

                public void onComplete() {
                    conditionalWait.conditionSatisfied();
                }
            });

            conditionalWait.waitForCondition();

        } catch (AxisFault axisFault) {
            log.error(axisFault.getMessage(), axisFault);
            fail();
        }
    }

    public void testCustomBinding_Echo() {
        try {
            WSAddressingCRCustomBinding_EchoStub stub = new WSAddressingCRCustomBinding_EchoStub(
                    configurationContext);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            /*MyCallBackHandler wssWsAddressingCRCallbackHandler = new MyCallBackHandler();

            // there is a bug.
            stub.startecho("Test String", wssWsAddressingCRCallbackHandler);

            wssWsAddressingCRCallbackHandler.waitForCondition();*/


        } catch (AxisFault e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void testCustomBinding_Echo1() {
        try {

            WSAddressingCRCustomBinding_Echo1Stub stub =
                    new WSAddressingCRCustomBinding_Echo1Stub(configurationContext);

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallBackHandler wssWsAddressingCRCallbackHandler = new MyCallBackHandler();
            stub.startecho("Test String", wssWsAddressingCRCallbackHandler);

            wssWsAddressingCRCallbackHandler.waitForCondition();

        } catch (AxisFault e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void testCustomBinding_Echo2() {
        try {
            WSAddressingCRCustomBinding_Echo2Stub stub =
                    new WSAddressingCRCustomBinding_Echo2Stub(configurationContext);

            stub._getServiceClient().getOptions().setProperty(AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.INCLUDE_OPTIONAL_HEADERS, true);

            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallBackHandler wssWsAddressingCRCallbackHandler = new MyCallBackHandler();

            stub.startecho("Test String", wssWsAddressingCRCallbackHandler);

            wssWsAddressingCRCallbackHandler.waitForCondition();

        } catch (AxisFault e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void testCustomBinding_Echo3() {
        try {
            WSAddressingCRCustomBinding_Echo3Stub stub =
                    new WSAddressingCRCustomBinding_Echo3Stub(configurationContext);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.INCLUDE_OPTIONAL_HEADERS, true);
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallBackHandler wssWsAddressingCRCallbackHandler = new MyCallBackHandler();

            stub.startecho("Test String", wssWsAddressingCRCallbackHandler);

            wssWsAddressingCRCallbackHandler.waitForCondition();

        } catch (AxisFault e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);

        }
    }

    public void testCustomBinding_Echo4() {
        try {
            WSAddressingCRCustomBinding_Echo4Stub stub = new WSAddressingCRCustomBinding_Echo4Stub(
                    configurationContext,
                    InteropSettings.getProperty(InteropConstants.ENDPOINT_ADDRESSING_SOAP11_DUPLEX),
                    true);

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);

            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallBackHandler wssWsAddressingCRCallbackHandler = new MyCallBackHandler();

            stub.startecho("Test String", wssWsAddressingCRCallbackHandler);

            wssWsAddressingCRCallbackHandler.waitForCondition();
        } catch (AxisFault axisFault) {
            log.error(axisFault.getMessage(), axisFault);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void testCustomBinding_Echo5() {
        try {
            WSAddressingCRCustomBinding_Echo5Stub stub = new WSAddressingCRCustomBinding_Echo5Stub(
                    configurationContext,
                    InteropSettings.getProperty(InteropConstants.ENDPOINT_ADDRESSING_SOAP11_DUPLEX),
                    true);

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Final.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallBackHandler wssWsAddressingCRCallbackHandler = new MyCallBackHandler();

            stub.startecho("Test String", wssWsAddressingCRCallbackHandler);

            wssWsAddressingCRCallbackHandler.waitForCondition();

        } catch (AxisFault e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (java.rmi.RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

        // finally sleep to stabilize the system
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }


    private OMElement getOMElment() {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://example.org/echo", "ns1");
        OMElement omElement = omFactory.createOMElement("echoIn", omNamespace);
        omElement.setText("Test String");
        return omElement;
    }

    private class MyCallBackHandler extends WSAddressingCRCallbackHandler {
        private volatile boolean done;

        @Override
        public void receiveResultecho(String result) {
            assertEquals(result, "Test String");
            synchronized (this) {
                done = true;
                this.notifyAll();
            }
        }

        @Override
        public void receiveErrorecho(Exception e) {
            fail();
        }

        public synchronized void waitForCondition() {
            while (!done) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
