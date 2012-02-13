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
package org.wso2.mercury.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.util.Utils;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.context.MercuryMessageContext;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.exception.RMSecurityException;
import org.wso2.mercury.message.AcknowledgmentRange;
import org.wso2.mercury.message.CreateSequenceResponseMessage;
import org.wso2.mercury.message.RMApplicationMessage;
import org.wso2.mercury.message.Sequence;
import org.wso2.mercury.message.SequenceAcknowledgment;
import org.wso2.mercury.message.SequenceAcknowledgmentMessage;
import org.wso2.mercury.message.TerminateSequenceMessage;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.state.RMDContext;
import org.wso2.mercury.state.RMDSequence;
import org.wso2.mercury.state.RMSContext;
import org.wso2.mercury.state.RMSSequence;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.MercuryParameterHandler;
import org.wso2.mercury.util.AxisFaultUtil;
import org.wso2.mercury.security.RMSecurityManager;

/**
 * This handler buffers the incoming Application messages and
 * send them accordingly.
 */
public class MercuryInHandler extends AbstractHandler {

    private static Log log = LogFactory.getLog(MercuryInHandler.class);

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        
        // If this is a message context invoke from the mercury worker then
        // we have to skip it.
        log.debug("Receiving the message with action ==> " + msgContext.getOptions().getAction());
        if ((msgContext.getProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE) != null)
                && msgContext.getProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE).equals(Constants.VALUE_TRUE)) {
            return InvocationResponse.CONTINUE;
        }

        if (msgContext.getOptions().getAction().equals(MercuryConstants.CREATE_SEQUENCE_ACTION)) {
            // do the create sequence processing.
            RMDContext rmdContext =
                    (RMDContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMD_CONTEXT);
            try {
                // If this sequence is an duplex one we abort this thread
                // otherwise countine this thread.
                RMDSequence rmdSequence = rmdContext.processCreateSequenceRequest(msgContext);
                if (rmdSequence.isAnonymous()) {
                    // If this is a annonymous sequence we do not start the rmd sequence worker
                    // we have to use the incomming thread to send the message.
                    rmdSequence.sendCreateSequenceResponseMessage();
                }

            } catch (RMMessageBuildingException e) {
                log.error(e.getMessage(), e);
                throw new AxisFault(e.getMessage(), e);
            }
            return InvocationResponse.ABORT;

        } else if (msgContext.getOptions().getAction().equals(MercuryConstants.CREATE_SEQUENCE_RESPONSE_ACTION)) {
            RMSContext rmsContext =
                    (RMSContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMS_CONTEXT);
            CreateSequenceResponseMessage createSequenceResponseMessage = null;

            try {
                createSequenceResponseMessage = CreateSequenceResponseMessage.fromSOAPEnvolope(msgContext.getEnvelope());
                RMSSequence rmsSequence = rmsContext.getRMSSequence(
                        msgContext.getRelatesTo().getValue(),
                        createSequenceResponseMessage.getIdentifier());
                // check for security proof
                checkForSecurityTokenProof(rmsSequence, msgContext);
                if (rmsSequence != null) {
                    rmsSequence.createSequenceResponseReceived();
                }
            } catch (RMMessageBuildingException e) {
                log.error(e.getMessage(), e);
                RMSSequence rmsSequence =
                        rmsContext.getRMSSequenceWithMessageID(msgContext.getOptions().getRelatesTo().getValue());
                if (rmsSequence != null) {
                    rmsSequence.invalidCreateSequenceResponseReceived(msgContext.getEnvelope());
                }
                return InvocationResponse.ABORT;
            } catch (PersistenceException e) {
                //If we could not persists this message then have to roll back all
                // i.e. have to unregister this rms sequence with id
                rmsContext.unRegisterRMSSequenceWithSequenceID(createSequenceResponseMessage.getIdentifier());
                log.error(e.getMessage(), e);
                throw new AxisFault(e.getMessage(), e);
            }
            return InvocationResponse.ABORT;
        } else if (msgContext.getOptions().getAction().equals(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT_ACTION)) {
            // Now we have received a sequence acknowledgment.
            // update the state of the RMSSequenceDto
            SequenceAcknowledgmentMessage sequenceAcknowledgmentMessage = null;
            SequenceAcknowledgment sequenceAcknowledgment = null;
            try {
                sequenceAcknowledgmentMessage = SequenceAcknowledgmentMessage.fromSOAPEnvelope(msgContext.getEnvelope());
                sequenceAcknowledgment = sequenceAcknowledgmentMessage.getSequenceAcknowledgment();
                RMSContext rmsContext =
                        (RMSContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMS_CONTEXT);
                RMSSequence rmsSequence =
                        rmsContext.getRMSSequenceWithSequenceID(sequenceAcknowledgment.getIdentifier());
                if (rmsSequence != null) {
                    checkForSecurityTokenProof(rmsSequence, msgContext);
                    rmsSequence.sequenceAcknowledgmentReceived(sequenceAcknowledgment.getAcknowledgmentRanges());
                }
            } catch (RMMessageBuildingException e) {
                log.error(e.getMessage(), e);
                throw new AxisFault(e.getMessage(), e);
            } catch (PersistenceException e) {
                log.error(e.getMessage(), e);
                throw new AxisFault(e.getMessage(), e);
            }
            return InvocationResponse.ABORT;
        } else if (msgContext.getOptions().getAction().equals(MercuryConstants.TERMINATE_SEQUENCE_ACTION)) {
            TerminateSequenceMessage terminateSequenceMessage = null;
            try {
                terminateSequenceMessage = TerminateSequenceMessage.fromSOAPEnvelpe(msgContext.getEnvelope());
                RMDContext rmdContext =
                        (RMDContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMD_CONTEXT);
                RMDSequence rmdSequence = rmdContext.getRMDSeqenceWithSequenceID(terminateSequenceMessage.getIdentifier());
                if (rmdSequence != null) {
                    checkForSecurityTokenProof(rmdSequence, msgContext);
                    rmdSequence.terminateSequenceMessageReceived();
                }

                // if this is an annonymous in out operation. the acknowledgment for the out going
                // sequence comes with incomming messages

                // at the client side this may not be an important thing to do
                // because before sending terminate message RMS should have get acknowledgmetns
                // for all the elemetns. but may help in a situation where client sends a termainate
                // message of expirity of a sequence.
                if (rmdSequence.getOfferedRMSSequence() != null) {
                    // i.e this is annonymous inout sequence
                    RMSSequence offeredRMSequence = rmdSequence.getOfferedRMSSequence();
                    if (terminateSequenceMessage.getSequenceAcknowledgment() != null) {
                        offeredRMSequence.sequenceAcknowledgmentReceived(
                                terminateSequenceMessage.getSequenceAcknowledgment().getAcknowledgmentRanges());
                    }
                    if (msgContext.isServerSide()) {
                        // send the return sequence acknowledgment message using this thread.
                        offeredRMSequence.sendTerminateSequenceMessage(msgContext);
                    }
                }

            } catch (RMMessageBuildingException e) {
                log.error(e.getMessage(), e);
                throw new AxisFault(e.getMessage(), e);
            } catch (PersistenceException e) {
               log.error("Can not save the state");
            }
            return InvocationResponse.ABORT;
        } else {
            // here we assume we only gets the RM application messages
            // TODO: check this

            RMApplicationMessage rmApplicationMessage = null;
            try {
                rmApplicationMessage = RMApplicationMessage.fromSOAPEnvelope(msgContext.getEnvelope());
            } catch (RMMessageBuildingException e) {
                log.error("Can not build the sequence acknowledgmet message ", e);
                throw new AxisFault("Can not build the sequence acknowledgmet message ", e);
            }

            if (rmApplicationMessage != null) {
                Sequence sequence = rmApplicationMessage.getSequence();
                if (sequence.getSequenceID() == null) {
                    // Most probably we have found a in fault message.
                    // If it does not belongs to this sequence let it pass through.
                    return InvocationResponse.CONTINUE;
                }

                log.debug("Application message " + sequence.getMessageNumber() + " received for "
                    + sequence.getSequenceID());
                MercuryMessageContext mercuryMessageContext = new MercuryMessageContext(rmApplicationMessage, msgContext);
                // save the sessionID in the message context to identify the sequence in returned
                // message context
                msgContext.setProperty(MercuryConstants.SESSION_ID, sequence.getSequenceID());
                RMDContext rmdContext =
                        (RMDContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMD_CONTEXT);
                RMDSequence rmdSequence = rmdContext.getRMDSeqenceWithSequenceID(sequence.getSequenceID());

                if (rmdSequence == null) {
                    log.error("Unknow message with seuqnce ID ==> " + sequence.getSequenceID());
                    throw new AxisFault("Unknow message with seuqnce ID ==> "
                            + sequence.getSequenceID());
                }

                // check the validity of the message if it belongs to a secure conversation
                checkForSecurityTokenProof(rmdSequence, msgContext);

                try {
                    boolean newMessageReceived = false;
                    if (sequence.isLastMessage()) {
                        // Save the last message property to be used in sending return sequence.
                        msgContext.setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
                        // We only have to go inside this method
                        // if we have the invoker buffer lock.
                        // Otherwise dead lock would happen.
                        synchronized (rmdSequence.getInvokerBuffer()) {
                            newMessageReceived = rmdSequence.lastMessageReceived(
                                    sequence.getMessageNumber(), mercuryMessageContext);
                        }
                    } else {
                        synchronized(rmdSequence.getInvokerBuffer()){
                            newMessageReceived = rmdSequence.applicationMessageReceived(
                                sequence.getMessageNumber(), mercuryMessageContext);
                        }
                    }

                    if (rmdSequence.isAnonymous()) {
                        // since this sequence is anonymous we have to use this thread to send the acknowledgment
                        // message.
                        try {
                            // for last message the mep is in only. so have to use the offered sequence.
                            if (msgContext.getAxisOperation().getMessageExchangePattern().equals(
                                    WSDL2Constants.MEP_URI_IN_ONLY) && (rmdSequence.getOfferedRMSSequence() == null)) {
                                // for annonnymous in only operatins request must be wait until
                                // resquest is read.
                                if (newMessageReceived) {
                                    MercuryParameterHandler mercuryParameterHandler =
                                            new MercuryParameterHandler(msgContext.getAxisService());
                                    if (mercuryParameterHandler.getBuildMessageWithoutWaiting()) {
                                        msgContext.getEnvelope().build();
                                    } else {
                                        synchronized (mercuryMessageContext) {
                                            try {
                                                mercuryMessageContext.wait();
                                            } catch (InterruptedException e) {
                                            }
                                        }
                                    }
                                }
                                // if the message exchange pattern is in only the we can straight away
                                // send the sequence Acknowledgment
                                rmdSequence.sendSequenceAcknowledgementMessage(msgContext);

                            } else {
                                if ((msgContext.getProperty("NonBlockingTransport") != null)
                                        && (msgContext.getOptions().getRelatesTo() != null)) {
                                    // Non blocking transports set the isServerSide true even at the client side
                                    // Once we got an application mesage we have to acknowledge this corresponding
                                    // RMS Sequence. Here we have to keep in mind that the RMS have to repeat the
                                    // request message util it gets the response message.

                                    RMSSequence offeredRMSSequence = rmdSequence.getOfferedRMSSequence();
                                    AcknowledgmentRange acknowledgmentRange =
                                            new AcknowledgmentRange(sequence.getMessageNumber(),
                                                    sequence.getMessageNumber());
                                    List acknowledgmentRanges = new ArrayList();
                                    acknowledgmentRanges.add(acknowledgmentRange);
                                    offeredRMSSequence.sequenceAcknowledgmentReceived(acknowledgmentRanges);

                                } else if (msgContext.isServerSide()) {
                                    // if the message exchange pattern is in out then we have to send the response
                                    // with this thread and the sequence acknowledgment
                                    // for anonymous inout operation the sender must send an offer.
                                    RMSSequence offeredRMSSequence = rmdSequence.getOfferedRMSSequence();
                                    // set the acknowledged messages.
                                    SequenceAcknowledgment sequenceAcknowledgment =
                                            rmApplicationMessage.getSequenceAcknowledgment();
                                    if (sequenceAcknowledgment != null) {
                                        // This sequence acknowledgment should only do at the server side
                                        // at the client side RMS should not stop sending messages util
                                        // it gets the corresponding response message.

                                        offeredRMSSequence.sequenceAcknowledgmentReceived(
                                                sequenceAcknowledgment.getAcknowledgmentRanges());
                                    }
                                    offeredRMSSequence.sendApplicationMessageResponse(
                                            msgContext, sequence.getMessageNumber());
                                } else {

                                    // Once we got an application mesage we have to acknowledge this corresponding
                                    // RMS Sequence. Here we have to keep in mind that the RMS have to repeat the
                                    // request message util it gets the response message.

                                    RMSSequence offeredRMSSequence = rmdSequence.getOfferedRMSSequence();
                                    AcknowledgmentRange acknowledgmentRange =
                                            new AcknowledgmentRange(sequence.getMessageNumber(),
                                                    sequence.getMessageNumber());
                                    List acknowledgmentRanges = new ArrayList();
                                    acknowledgmentRanges.add(acknowledgmentRange);
                                    offeredRMSSequence.sequenceAcknowledgmentReceived(acknowledgmentRanges);

                                    rmdSequence.getInvokerBuffer().waitUntilMessageInvoke(sequence.getMessageNumber());

                                }

                            }

                        } catch (RMMessageBuildingException e) {
                            log.error("Can not build the sequence acknowledgmet message ", e);
                            throw new AxisFault("Can not build the sequence acknowledgmet message ", e);
                        }
                    } else {
                        MercuryParameterHandler mercuryParameterHandler =
                                        new MercuryParameterHandler(msgContext.getAxisService());
                        if (newMessageReceived) {
                            if (mercuryParameterHandler.getBuildMessageWithoutWaiting()) {
                                msgContext.getEnvelope().build();
                            } else {
                                // Here if we return the thread the input stream may be closed.
                                // So have to wait until user finish with the input message.
                                // We make this thread wait at the invoker buffer.
                                // We have to wait only if a new message received
                                synchronized (mercuryMessageContext) {
                                    try {
                                        mercuryMessageContext.wait();
                                    } catch (InterruptedException e) {
                                    }
                                }
                            }
                        }
                    }
                } catch (PersistenceException e) {
                    log.error("Can not save the message ", e);
                }
                return InvocationResponse.ABORT;
            } else {
                // This is not an mercury message, let the message pass through.
                // Here we have to check whether this is an invalid response to the
                // create sequence message.
                MercuryParameterHandler mercuryParameterHandler =
                        new MercuryParameterHandler(msgContext.getAxisService());
                boolean enforceRM = mercuryParameterHandler.getEnforceRM();
                if (msgContext.getOptions().getRelatesTo() != null) {
                    RMSContext rmsContext =
                            (RMSContext) msgContext.getConfigurationContext().getProperty(
                                    MercuryConstants.RMS_CONTEXT);
                    RMSSequence rmsSequence =
                            rmsContext.getRMSSequenceWithMessageID(msgContext.getOptions().getRelatesTo().getValue());
                    if (rmsSequence != null) {
                        rmsSequence.invalidCreateSequenceResponseReceived(msgContext.getEnvelope());
                        return InvocationResponse.ABORT;
                    }
                } else if (enforceRM){
                    // If it has enforced rm we have to send a fault to client

                    String nsURI = msgContext.getEnvelope().getNamespace().getNamespaceURI();
                    QName faultCode = null;

                    if (SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI.equals(nsURI)) {
                        faultCode = new QName(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAP12Constants.FAULT_CODE_SENDER, SOAP12Constants.SOAP_DEFAULT_NAMESPACE_PREFIX);
                    } else if (SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI.equals(nsURI)) {
                        faultCode = new QName(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI, SOAP11Constants.FAULT_CODE_SENDER, SOAP11Constants.SOAP_DEFAULT_NAMESPACE_PREFIX);
                    } else {
                        log.debug("Unknown Soap Envelope namespace : " + nsURI);
                    }

                    AxisFault tmp = new AxisFault("This endpoint requires an RM message");
                    if (faultCode != null) {
                        // set FaultCode and SubCode
                        ArrayList list = new ArrayList();
                        String addressingNamespace = getAddressingNamesapce(msgContext);
                        list.add(new QName(addressingNamespace, AddressingConstants.FAULT_ACTION_NOT_SUPPORTED, AddressingConstants.WSA_DEFAULT_PREFIX));

                        tmp.setFaultCode(faultCode);
                        tmp.setFaultSubCodes(list);
                    }

                    MessageContext faultContext = AxisFaultUtil.generateAxisFaultMessageContext(msgContext, tmp);
                    AxisFault fault = new AxisFault("This endpoint requires an RM message", faultContext);
                    throw fault;
                }
                return InvocationResponse.CONTINUE;
            }
        }

    }

    private void checkForSecurityTokenProof(RMSSequence rmsSequence, MessageContext msgContext) throws AxisFault {
        if (rmsSequence.getSecurityToken() != null){
            RMSecurityManager rmSecurityManager
                    = (RMSecurityManager) msgContext.getProperty(MercuryConstants.RM_SECURITY_MANAGER);
            try {
                rmSecurityManager.checkProofOfPossession(rmsSequence.getSecurityToken(),msgContext);
            } catch (RMSecurityException e) {
                throw new AxisFault("Security Token does not match");
            }
        }
    }

    private void checkForSecurityTokenProof(RMDSequence rmdSequence, MessageContext msgContext) throws AxisFault {
        if (rmdSequence.getSecurityToken() != null){
            RMSecurityManager rmSecurityManager
                    = (RMSecurityManager) msgContext.getProperty(MercuryConstants.RM_SECURITY_MANAGER);
            try {
                rmSecurityManager.checkProofOfPossession(rmdSequence.getSecurityToken(),msgContext);
            } catch (RMSecurityException e) {
                throw new AxisFault("Security Token does not match");
            }
        }
    }

    private String getAddressingNamesapce(MessageContext messageContext){
        String namespace = (String) messageContext.getProperty(AddressingConstants.WS_ADDRESSING_VERSION);
        if (namespace == null){
            namespace = Utils.getParameterValue(messageContext.getParameter(AddressingConstants.WS_ADDRESSING_VERSION));
        }
        if (namespace == null){
            namespace = AddressingConstants.Final.WSA_NAMESPACE;
        }
        return namespace;
    }

}
