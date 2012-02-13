/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.workers;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPFaultCode;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.server.AxisHttpResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.state.InvokerBuffer;

import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

/**
 * invoker worker works on the
 * Invoker buffer to invoke the messages in correct order.
 */

public class InvokerWorker implements Runnable{

    private static Log log = LogFactory.getLog(InvokerWorker.class);

    private long sleepTime;

    private InvokerBuffer invokerBuffer;

    public InvokerWorker(InvokerBuffer invokerBuffer) {
        this.invokerBuffer = invokerBuffer;
        this.invokerBuffer.setInvokerWorker( this );
    }

    private Object lock = new Object();
    
    public void run() {

        while ((invokerBuffer.getState() != InvokerBuffer.STATE_COMPLETE) &&
                (System.currentTimeMillis() - this.invokerBuffer.getLastAccessTime()) < this.invokerBuffer.getTimeoutTime()) {
            // do actions here.
            try {
                invokerBuffer.doActions();
            } catch (AxisFault axisFault) {
                log.error("Axis Fault has received to Invoker worker");
                if (axisFault.getFaultType() == Constants.APPLICATION_FAULT) {
                    try {

                        //set the transport status error if it is http
                        MessageContext faultMessageContext = axisFault.getFaultMessageContext();
                        // response status has to set only for annonymous operations
                        if ((faultMessageContext.getTo() == null)
                                || ((faultMessageContext.getTo().getAddress().equals(AddressingConstants.Final.WSA_ANONYMOUS_URL) ||
                                (faultMessageContext.getTo().getAddress().equals(AddressingConstants.Submission.WSA_ANONYMOUS_URL)))))
                        {
                            Object transportInfo = faultMessageContext.getProperty(Constants.OUT_TRANSPORT_INFO);
                            if (transportInfo instanceof AxisHttpResponse) {
                                AxisHttpResponse response = (AxisHttpResponse) transportInfo;
                                response.sendError(getStatusFromAxisFault(axisFault), axisFault.getMessage());
                            } else {
                                HttpServletResponse response =
                                        (HttpServletResponse) faultMessageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE);
                                if (response != null) {
                                    String status = (String) faultMessageContext.getProperty(Constants.HTTP_RESPONSE_STATE);
                                    if (status == null) {
                                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                    } else {
                                        response.setStatus(Integer.parseInt(status));
                                    }
                                    //TODO : Check for SOAP 1.2!
                                    SOAPFaultCode code = faultMessageContext.getEnvelope().getBody().getFault().getCode();

                                    OMElement valueElement = null;
                                    if (code != null) {
                                        valueElement = code.getFirstChildWithName(new QName(
                                                SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI,
                                                SOAP12Constants.SOAP_FAULT_VALUE_LOCAL_NAME));
                                    }

                                    if (valueElement != null) {
                                        if (SOAP12Constants.FAULT_CODE_SENDER.equals(valueElement.getTextAsQName().getLocalPart()))
                                        {
                                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                        }
                                    }
                                }
                            }
                        }
                        AxisEngine.sendFault(axisFault.getFaultMessageContext());
                    } catch (AxisFault e) {
                        log.error("Problem when sending the fault message", e);
                    }
                } else {
                    // TODO: what to do with these exceptions
                    log.error("Got a soap processing exception");
                }

            }
            try {
                log.debug("Wait");
                // this thread wait on a lock object to make notifiable in any time
                synchronized (lock) {
                    lock.wait(sleepTime);
                }
                log.debug("Wakeup...");
            } catch (InterruptedException e) {
                log.error("Interupted :" + e );
            }
        }
        // invoker is timed out
        log.info("Stopping the invoker worker ");
    }

    public int getStatusFromAxisFault(AxisFault fault) {
        QName faultCode = fault.getFaultCode();
        if (SOAP12Constants.QNAME_SENDER_FAULTCODE.equals(faultCode) ||
                SOAP11Constants.QNAME_SENDER_FAULTCODE.equals(faultCode)) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }

        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
    
    public void wakeUp() {
        try {
            log.debug("WakeUp");
            synchronized (lock) {
                lock.notify();
            }
            log.debug("Thread notified");
        } catch(Exception e) {
            log.error("Failed to notify : " +e, e);
        }
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

}
