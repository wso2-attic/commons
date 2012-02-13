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

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.TransportUtils;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.i18n.Messages;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.state.RMSSequence;

import java.io.InputStream;
import java.io.IOException;

/**
 * this class is used to send a message by forking a
 * new thread.
 */
public class MessageWorker implements Runnable {

    private static Log log = LogFactory.getLog(MessageWorker.class);

    // message context to send
    private MessageContext messageContext;
    private MessageContext responseMessageContext;
    private boolean isResume;
    private boolean isInvokeAsAnonClient;
    private RMSSequence rmsSequence;

    public MessageWorker(MessageContext messageContext, boolean isResume) {
        this.messageContext = messageContext;
        this.isResume = isResume;
    }

    public MessageWorker(MessageContext messageContext,
                         MessageContext responseMessageContext,
                         boolean resume,
                         boolean invokeAsAnonClient) {
        this(messageContext, resume);
        this.responseMessageContext = responseMessageContext;
        isInvokeAsAnonClient = invokeAsAnonClient;
    }

    public void run() {
        try {

            if (this.isResume) {
                if (messageContext.isProcessingFault()){
                    AxisEngine.resumeSendFault(messageContext);
                } else {
                    AxisEngine.resumeSend(messageContext);
                }
            } else {
                AxisEngine.send(messageContext);
            }

            if (isInvokeAsAnonClient) {
                // if the invocation is annon client then we have to push the respose back to
                // Axis2 engine

                // Copy RESPONSE properties which the transport set onto the request message context when it processed
                // the incoming response recieved in reply to an outgoing request.
                if (messageContext.getAxisOperation().getMessageExchangePattern().equals(
                        WSDL2Constants.MEP_URI_OUT_ONLY) ||
                    responseMessageContext.getOptions().getAction().equals(
                            MercuryConstants.CREATE_SEQUENCE_RESPONSE_ACTION) ||
                    responseMessageContext.getOptions().getAction().equals(
                            MercuryConstants.TERMINATE_SEQUENCE_ACTION)) {
                    responseMessageContext.setProperty(MessageContext.TRANSPORT_HEADERS,
                            messageContext.getProperty(MessageContext.TRANSPORT_HEADERS));
                    responseMessageContext.setProperty(HTTPConstants.MC_HTTP_STATUS_CODE,
                            messageContext.getProperty(HTTPConstants.MC_HTTP_STATUS_CODE));

                    responseMessageContext.setProperty(MessageContext.TRANSPORT_IN,
                            messageContext.getProperty(MessageContext.TRANSPORT_IN));
                    responseMessageContext.setTransportIn(messageContext.getTransportIn());
                    responseMessageContext.setTransportOut(messageContext.getTransportOut());

                    try{
                       handleResponse(responseMessageContext);
                    } catch (AxisFault e){
                        // if any exception happens here it must be a soap processing exception.
                        // rm can not handle such
                        // so it halt the process here.

                        // if rm receives an soap fault from the server then it receives this message at the
                        // in fault flow. so it handles at that level.
                        log.error(e.getMessage(), e);
                        if (this.rmsSequence != null){
                            this.rmsSequence.soapFaultOccured("Problem with sending soap message." + e.getMessage(),
                                    new AxisFault("Problem with sending soap message." + e.getMessage(),e));
                        }
                    }

                } else {
                    // for inout operations we have to stop the calling thread until we get a message
                    // so we have to notify the this thread
                    // have to take the lock before calling to notify.

                    // if this is a duplicat message then nothing would happen since
                    // the calling thread has already taken by the first message.
                    if ((responseMessageContext.getProperty(MessageContext.TRANSPORT_IN) != null) ||
                            (responseMessageContext.getEnvelope() != null)) {
                        synchronized (responseMessageContext) {
                            responseMessageContext.notify();
                        }
                    }

                }

            }
        } catch (Exception e) {
            if (e.getCause() instanceof IOException) {
                log.warn("Can not send the message due to IOException ...");
            } else {
                // if this is not an exception this may not be able to handle at the RM level.
                // hence try to terminate the sequence
                log.error(e.getMessage(), e);
                if (this.rmsSequence != null) {
                    try {
                        this.rmsSequence.soapFaultOccured("Problem with sending soap message. " + e.getMessage(),
                                new AxisFault("Problem with sending soap message. " + e.getMessage(), e));
                    } catch (AxisFault axisFault) {
                        // nothing we can do here.
                        log.error("Can not terminate the rm sequence",e);
                    }
                }
            }
        }
    }

    protected void handleResponse(MessageContext responseMessageContext) throws AxisFault {

        responseMessageContext.setSoapAction(null);
        // for http transport the soap envelop is not build. but for smtp transport
        // it is build. so frist we check whether the soap envelop is build or
        // not.
        SOAPEnvelope resenvelope = responseMessageContext.getEnvelope();
        if (resenvelope == null) {
            InputStream inStream =
                    (InputStream) responseMessageContext.getProperty(MessageContext.TRANSPORT_IN);
            // we don't care about the situation where input stream is null.
            // this means this message has lost for some reason. for mercury we ignore it.
            if (inStream != null) {
                resenvelope = TransportUtils.createSOAPMessage(responseMessageContext);
                if (resenvelope != null) {
                    responseMessageContext.setEnvelope(resenvelope);
                } else {
                    throw new AxisFault(Messages
                            .getMessage("blockingInvocationExpectsResponse"));
                }
            }
        }

        if (resenvelope != null) {
            AxisEngine.receive(responseMessageContext);
        }

    }

    public boolean isInvokeAsAnonClient() {
        return isInvokeAsAnonClient;
    }

    public void setInvokeAsAnonClient(boolean invokeAsAnonClient) {
        isInvokeAsAnonClient = invokeAsAnonClient;
    }

    public RMSSequence getRmsSequence() {
        return rmsSequence;
    }

    public void setRmsSequence(RMSSequence rmsSequence) {
        this.rmsSequence = rmsSequence;
    }

}
