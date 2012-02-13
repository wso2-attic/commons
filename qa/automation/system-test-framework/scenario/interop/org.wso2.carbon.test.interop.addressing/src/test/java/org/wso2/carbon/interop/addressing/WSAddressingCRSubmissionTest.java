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
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tempuri.submission.echo.*;
import org.tempuri.submission.notify.WSAddressingCRStub;
import org.wso2.carbon.interop.common.InteropSettings;
import org.wso2.carbon.interop.common.InteropConstants;


public class WSAddressingCRSubmissionTest extends WSAddressingCRTest {

    private static final Log log = LogFactory.getLog(WSAddressingCRSubmissionTest.class);

    public void testCustomBinding_Notify() {
        try {
            WSAddressingCRStub stub = new WSAddressingCRStub(
                    configurationContext,
                    InteropSettings.getProperty(InteropConstants.ENDPOINT_ADDRESSING_SOAP11));

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setProperty(
                    Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(InteropConstants.ENDPOINT_ADDRESSING_SOAP11));
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
                    new org.tempuri.last.notify1.WSAddressingCRStub(
                            configurationContext,
                            InteropSettings.getProperty(
                                    InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12));

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

            stub._getServiceClient().getOptions().setProperty(
                    Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12));
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

    public void testCustomBinding_Echo() {
        try {
            WSAddressingCRCustomBinding_EchoStub stub = new WSAddressingCRCustomBinding_EchoStub(
                    configurationContext,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP11));

            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);

            stub._getServiceClient().getOptions().setProperty(
                    Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP11));

            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallbackHandler wssWsAddressingCRCallbackHandler = new MyCallbackHandler();

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

    public void testCustomBinding_Echo1() {
        try {
            WSAddressingCRCustomBinding_Echo1Stub stub = new WSAddressingCRCustomBinding_Echo1Stub(
                    configurationContext,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12));
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);
            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12));
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallbackHandler wssWsAddressingCRCallbackHandler = new MyCallbackHandler();
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
            WSAddressingCRCustomBinding_Echo2Stub stub = new WSAddressingCRCustomBinding_Echo2Stub(
                    configurationContext,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP11_MANUAL));
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.INCLUDE_OPTIONAL_HEADERS, true);
            stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP11_MANUAL));
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallbackHandler wssWsAddressingCRCallbackHandler = new MyCallbackHandler();

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

    public void testCustomBinding_Echo3() {
        try {
            WSAddressingCRCustomBinding_Echo3Stub stub = new WSAddressingCRCustomBinding_Echo3Stub(
                    configurationContext,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12_MANUAL));
            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.INCLUDE_OPTIONAL_HEADERS, true);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);
            stub._getServiceClient().getOptions().setProperty(
                    Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12_MANUAL));
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallbackHandler wssWsAddressingCRCallbackHandler = new MyCallbackHandler();

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

    public void testCustomBinding_Echo4() {
        try {
            WSAddressingCRCustomBinding_Echo4Stub stub = new WSAddressingCRCustomBinding_Echo4Stub(
                    configurationContext,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP11_DUPLEX),
                    true);
            stub._getServiceClient().getOptions().setProperty(
                    Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP11_DUPLEX));
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallbackHandler wssWsAddressingCRCallbackHandler = new MyCallbackHandler();

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

    public void testCustomBinding_Echo5() {
        try {
            WSAddressingCRCustomBinding_Echo5Stub stub = new WSAddressingCRCustomBinding_Echo5Stub(
                    configurationContext,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12_DUPLEX),
                    true);
            stub._getServiceClient().getOptions().setSoapVersionURI(
                    SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            stub._getServiceClient().getOptions().setProperty(
                    AddressingConstants.WS_ADDRESSING_VERSION,
                    AddressingConstants.Submission.WSA_NAMESPACE);
            stub._getServiceClient().getOptions().setProperty(
                    Constants.Configuration.TRANSPORT_URL,
                    InteropSettings.getProperty(
                            InteropConstants.ENDPOINT_ADDRESSING_SUBMISSION_SOAP12_DUPLEX));
            String result = stub.echo("Test String");
            assertEquals(result, "Test String");

            MyCallbackHandler wssWsAddressingCRCallbackHandler = new MyCallbackHandler();

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

    private static class MyCallbackHandler extends WSAddressingCRCallbackHandler {

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
            done = true;
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
