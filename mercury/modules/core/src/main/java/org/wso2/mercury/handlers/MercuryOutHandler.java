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

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.ClientUtils;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.wso2.mercury.keys.InternalSequenceKey;
import org.wso2.mercury.message.RMApplicationMessage;
import org.wso2.mercury.message.Sequence;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.persistence.dto.InternalKeyDto;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.state.*;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.RMUtil;
import org.wso2.mercury.util.MercuryParameterHandler;
import org.wso2.mercury.workers.RMSSequenceWorker;
import org.wso2.mercury.callback.MercuryErrorCallback;
import org.wso2.mercury.callback.MercuryTerminateCallback;
import org.wso2.mercury.security.RMSecurityManager;
import org.wso2.mercury.security.SecurityToken;
import org.wso2.mercury.exception.RMSecurityException;


public class MercuryOutHandler extends AbstractHandler {

    private static Log log = LogFactory.getLog(MercuryOutHandler.class);

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {

        // If this is a message context invoke from the mercury worker then
        // we have to skip it.
        if ((msgContext.getProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE) != null)
                && msgContext.getProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE).equals(Constants.VALUE_TRUE)) {
            return InvocationResponse.CONTINUE;
        }
        //RM only deals with the Application faults. for other faults just let it pass.
        if (msgContext.isProcessingFault() || (msgContext.getFLOW() == MessageContext.OUT_FAULT_FLOW)) {
            if ((msgContext.getProperty(Constants.APPLICATION_FAULT_STRING) == null) ||
                    !msgContext.getProperty(Constants.APPLICATION_FAULT_STRING).equals(Constants.VALUE_TRUE)) {
                return InvocationResponse.CONTINUE;
            }
        }

        // Set the default internal key if the value is not set
        if (msgContext.getProperty(MercuryClientConstants.INTERNAL_KEY) == null) {
            if (msgContext.isServerSide()) {
                // This is in the server side so use the session id as the internal key
                MessageContext inBoundMessageContext =
                        msgContext.getOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                if (inBoundMessageContext.getProperty(MercuryConstants.SESSION_ID) != null) {
                    msgContext.setProperty(MercuryClientConstants.INTERNAL_KEY,
                            inBoundMessageContext.getProperty(MercuryConstants.SESSION_ID));
                } else {
                    // If there is no session ID in an inbound message it is a non rm application message.
                    // Just let it pass through
                    return InvocationResponse.CONTINUE;
                }
            } else { //Not server side. Setting default internal key.
                msgContext.setProperty(MercuryClientConstants.INTERNAL_KEY, MercuryConstants.DEFAULT_INTERNAL_KEY);
            }
        }

        //First check whether there is a session already exists for this internal key
        // and endpoint address.
        InternalSequenceKey key = new InternalSequenceKey(msgContext.getTo().getAddress(),
                (String) msgContext.getProperty(MercuryClientConstants.INTERNAL_KEY));
        RMSContext rmsContext =
                (RMSContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMS_CONTEXT);

        // First check whether user wants to resume a session stored in the persistence storage.
        // If so get the date from the persistence store and start the session.
        if ((msgContext.getProperty(MercuryClientConstants.RESUME_SEQUENCE) != null) &&
                msgContext.getProperty(MercuryClientConstants.RESUME_SEQUENCE).equals(Constants.VALUE_TRUE)) {
            // User wants to resume a session given in the internal key
            try {
                log.info("Resuming Sequence with the Key -" + key.getInternalKey()
                        + " address-" + key.getEndPointAddress());
                rmsContext.resumeSequence(key, msgContext);
            } catch (PersistenceException e) {
                log.error("Can not load the saved state ", e);
                throw new AxisFault("Can not load the saved state ", e);
            }
            return InvocationResponse.ABORT;
        }

        RMSSequence rmsSequence = rmsContext.getRMSSeqenceWithInternalKey(key);

        // Set the last message from the inbound message context.
        if (msgContext.isServerSide() && (msgContext.getProperty(MercuryClientConstants.LAST_MESSAGE) == null)) {
            // Set this as the last message if the corresponding inbound massage is the last message.
            MessageContext inBoundMessageContext =
                    msgContext.getOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            if (inBoundMessageContext.getProperty(MercuryClientConstants.LAST_MESSAGE) != null) {
                msgContext.setProperty(MercuryClientConstants.LAST_MESSAGE,
                        inBoundMessageContext.getProperty(MercuryClientConstants.LAST_MESSAGE));
            }
        }
        if (rmsSequence == null) {

            if (msgContext.getOptions().getAction().equals(MercuryConstants.LAST_MESSAGE_ACTION)){
                // Terminate sending message.
                // This is important in server side. In server side we send the last message back but
                // for in-only operations have to stop this.
                return InvocationResponse.ABORT;
            }
            // i.e There is no started sequence for this
            boolean isLastMessage = false;
            log.info("Starting a new sequence to key -" + key.getInternalKey()
                    + " address-" + key.getEndPointAddress());
            if ((msgContext.getProperty(MercuryClientConstants.LAST_MESSAGE) != null) &&
                    (msgContext.getProperty(MercuryClientConstants.LAST_MESSAGE).equals(Constants.VALUE_TRUE))) {
                isLastMessage = true;
                rmsSequence = new RMSSequence(RMSSequence.STATE_0101, msgContext.getTo());
            } else {
                rmsSequence = new RMSSequence(RMSSequence.STATE_0100, msgContext.getTo());
            }

            // establish a security token if user has specified
            if (msgContext.isPropertyTrue(MercuryClientConstants.USE_SECURE_RM) &&
                    (msgContext.getProperty(MercuryConstants.RM_SECURITY_MANAGER) != null)){
                RMSecurityManager rmSecurityManager
                        = (RMSecurityManager) msgContext.getProperty(MercuryConstants.RM_SECURITY_MANAGER);
                try {
                    SecurityToken securityToken = rmSecurityManager.getSecurityToken(msgContext);
                    rmsSequence.setSecurityToken(securityToken);
                } catch (RMSecurityException e) {
                    log.error("Can not get the security token", e);
                    throw new AxisFault("Can not get the security token",e);
                }
            }

            // Set the error call back
            if (msgContext.getProperty(MercuryClientConstants.ERROR_CALLBACK) != null){
                MercuryErrorCallback errorCallback =
                        (MercuryErrorCallback) msgContext.getProperty(MercuryClientConstants.ERROR_CALLBACK);
                rmsSequence.setErrorCallback(errorCallback);
            }

            // set the map
            rmsSequence.setMep(msgContext.getAxisOperation().getMessageExchangePattern());

            RMApplicationMessage rmApplicationMessage = new RMApplicationMessage(msgContext.getEnvelope());
            Sequence sequence = new Sequence();
            sequence.setLastMessage(isLastMessage);
            rmApplicationMessage.setSequence(sequence);

            // If the orginal message mep is in-only then transport-in may not have been set.
            // But since RM needs two way communication we have to set that if original mesage context
            // has not been set.
            TransportOutDescription transportOut = msgContext.getTransportOut();
            TransportInDescription transportIn = msgContext.getTransportIn();
            if (transportIn == null) {
                transportIn = msgContext.getOptions().getTransportIn();
            }

            //If use seperate listner is false then we have to use the annonymous end point.
            if ((transportIn == null) && msgContext.getOptions().isUseSeparateListener()) {
                transportIn = ClientUtils.inferInTransport(
                        msgContext.getConfigurationContext().getAxisConfiguration(),
                        msgContext.getOptions(),
                        msgContext);
            }

            String addressingNamespace = (String) msgContext.getProperty(AddressingConstants.WS_ADDRESSING_VERSION);
            if (addressingNamespace == null) {
                // Set the default addressing name space as final
                addressingNamespace = AddressingConstants.Final.WSA_NAMESPACE;
            }

            Axis2Info axis2Info = new Axis2Info();
            axis2Info.setServiceContext(msgContext.getServiceContext());
            axis2Info.setOptions(msgContext.getOptions());
            axis2Info.setTransportIn(transportIn);
            axis2Info.setTransportOut(transportOut);
            axis2Info.setServerSide(msgContext.isServerSide());
            axis2Info.setSoapNamespaceURI(msgContext.getEnvelope().getNamespace().getNamespaceURI());
            axis2Info.setProperties(msgContext.getProperties());
            axis2Info.setAddressingNamespaceURI(addressingNamespace);


            rmsSequence.setAxis2Info(axis2Info);

            // set the times of the RMSSeuqence
            MercuryParameterHandler mercuryParameterHandler = new MercuryParameterHandler(msgContext.getAxisService());
            rmsSequence.setRetransmitTime(mercuryParameterHandler.getRMSSequenceRetransmitTime());
            rmsSequence.setTimeoutTime(mercuryParameterHandler.getRMSSequenceTimeout());
            rmsSequence.setMaximumRetrasmitCount(mercuryParameterHandler.getRMSMaximumRetransmitCount());
            rmsSequence.setExponentialBackoff(mercuryParameterHandler.getRMSExponentialBackoff());


            // Here at server side this code (i.e rmsSequence == null) is excecuted only for Duplex mode.
            // For Annonymous mode always RMS should offer the sequence and when
            // the response message hit the out handler there must be a sequence.

            if (msgContext.getOptions().isUseSeparateListener() || msgContext.isServerSide()) {
                // At the serverside always RM has to set an Acks to address to a valid address.
                rmsSequence.setAckToEpr(msgContext.getConfigurationContext().getListenerManager().getEPRforService(
                        msgContext.getAxisService().getName(),
                        msgContext.getAxisOperation().getName().getLocalPart(),
                        transportIn.getName()));
                rmsSequence.setAnnonymous(false);
            } else {
                rmsSequence.setAnnonymous(true);
                if (addressingNamespace.equals(AddressingConstants.Submission.WSA_NAMESPACE)) {
                    rmsSequence.setAckToEpr(new EndpointReference(AddressingConstants.Submission.WSA_ANONYMOUS_URL));
                } else {
                    rmsSequence.setAckToEpr(new EndpointReference(AddressingConstants.Final.WSA_ANONYMOUS_URL));
                }
            }

            // Process the sequence offer.
            // There can be a sequence offer for only the first message.
            // Offer can only be used in in-out meps.
            // If the rms sequence is anonymous, any way we have to offer a sequence.
            if (msgContext.getAxisOperation().getMessageExchangePattern().equals(WSDL2Constants.MEP_URI_OUT_IN)) {
                if (((msgContext.getProperty(MercuryClientConstants.SEQUENCE_OFFER) != null) &&
                        msgContext.getProperty(MercuryClientConstants.SEQUENCE_OFFER).equals(Constants.VALUE_TRUE))
                        || (rmsSequence.isAnnonymous())) {
                    String offerID = RMUtil.getUUID();
                    rmsSequence.setSequenceOffer(offerID);
                    RMDContext rmdContext = (RMDContext) msgContext.getProperty(MercuryConstants.RMD_CONTEXT);
                    RMDSequence offeredRMDSequence = rmdContext.createRMDSequenceWithOffer(
                            offerID, msgContext.getTo().getAddress(), axis2Info, rmsSequence.isAnnonymous());
                    if (msgContext.getProperty(MercuryClientConstants.TERMINATE_CALLBACK) != null){
                        offeredRMDSequence.setTerminateCallback(
                                (MercuryTerminateCallback) msgContext.getProperty(MercuryClientConstants.TERMINATE_CALLBACK));
                    }
                    if (rmsSequence.isAnnonymous()) {
                        offeredRMDSequence.setOfferedRMSSequence(rmsSequence);
                        rmsSequence.setOfferedRMDSequence(offeredRMDSequence);
                    }
                }
            }

            // Add this internal key and RMS to persistence if the persitance manager is set.
            if (msgContext.getConfigurationContext().getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER) != null) {
                PersistenceManager persistenceManager =
                        (PersistenceManager) msgContext.getConfigurationContext().getProperty(
                                MercuryConstants.RM_PERSISTANCE_MANAGER);
                InternalKeyDto internalKeyDto = new InternalKeyDto(key.getInternalKey(), key.getEndPointAddress());
                try {
                    persistenceManager.save(internalKeyDto);
                    rmsSequence.save(internalKeyDto.getId());
                } catch (PersistenceException e) {
                    log.error("Can not persists objects", e);
                    throw new AxisFault("Can not persists objects", e);
                }

            }
            try {
                rmsSequence.addRMMessageContext(rmApplicationMessage, msgContext);
            } catch (PersistenceException e) {
                log.error("Can not persists objects", e);
                throw new AxisFault("Can not save the message", e);
            }
            // Register this sequence in the application context.
            rmsContext.registerRMSSequenceToInternalKey(key, rmsSequence);

            // Set a sender worker who works on this sequence.
            RMSSequenceWorker rmsSequenceWorker = new RMSSequenceWorker(rmsSequence);
            rmsSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMSSequenceWorkerSleepTime());
            msgContext.getConfigurationContext().getThreadPool().execute(rmsSequenceWorker);

        } else {

            RMApplicationMessage rmApplicationMessage =
                    new RMApplicationMessage(msgContext.getEnvelope());
            Sequence sequence = new Sequence();
            rmApplicationMessage.setSequence(sequence);

            boolean isLastMessage = false;
            boolean isTerminateMessage = false;
            if ((msgContext.getProperty(MercuryClientConstants.LAST_MESSAGE) != null) &&
                    (msgContext.getProperty(MercuryClientConstants.LAST_MESSAGE).equals(Constants.VALUE_TRUE))) {
                log.debug("Last message received");
                isLastMessage = true;
            }

            if ((msgContext.getProperty(MercuryClientConstants.TERMINATE_MESSAGE) != null) &&
                    (msgContext.getProperty(MercuryClientConstants.TERMINATE_MESSAGE).equals(Constants.VALUE_TRUE))) {
                log.debug("Terminate message received");
                isTerminateMessage = true;
            }

            sequence.setLastMessage(isLastMessage);

            try {
                if (isLastMessage) {
                    rmsSequence.lastMessageReceivedFromClient(rmApplicationMessage, msgContext);
                } else if (isTerminateMessage) {
                    // For the terminate message we have to send a dummy application message.
                    sequence.setLastMessage(true);
                    msgContext.getOptions().setAction(MercuryConstants.LAST_MESSAGE_ACTION);
                    rmsSequence.lastMessageReceivedFromClient(rmApplicationMessage, msgContext);
                    RMDSequence offeredSequence = rmsSequence.getOfferedRMDSequence();
                    if (offeredSequence != null){
                       // if the user has set a terminate call back set it here
                       // to notify after sequence is finished
                       if (msgContext.getProperty(MercuryClientConstants.TERMINATE_CALLBACK) != null){
                            offeredSequence.setTerminateCallback(
                                    (MercuryTerminateCallback) msgContext.getProperty(MercuryClientConstants.TERMINATE_CALLBACK));
                       }
                    }

                } else {
                    // This is a normal application message
                    rmsSequence.applicationMessageReceivedFromClient(rmApplicationMessage, msgContext);
                }
            } catch (PersistenceException e) {
                log.error("Can not save the message", e);
                throw new AxisFault("Can not save the message", e);
            }
        }

        if (rmsSequence.isAnnonymous() &&
                msgContext.getAxisOperation().getMessageExchangePattern().equals(
                        WSDL2Constants.MEP_URI_OUT_IN)) {
            // For an inout anonymous client we have to wait until it get the response.
            MessageContext responseMessageContext = msgContext.getOperationContext().getMessageContext(
                    WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            synchronized (responseMessageContext) {
                try {
                    log.debug("Waiting until response message receives");
                    responseMessageContext.wait();
                } catch (InterruptedException e) {
                }
            }

            // if some thing happen wrong when processing rm messags and happend to terminate the
            // rm sequence then we weed to let this to the user.
            // if the problem is with the create sequence respose we need to get that
            // soap envelop and send to the client for more information.
            if (responseMessageContext.getProperty(MercuryConstants.RM_ERROR) != null){
                AxisFault axisFault = null;
                SOAPEnvelope soapEnvelope = responseMessageContext.getEnvelope();
                SOAPEnvelope rmFaultEnvelope
                        = (SOAPEnvelope) responseMessageContext.getProperty(MercuryConstants.RM_FAULT_ENVElOPE);
                if ((soapEnvelope != null) && (soapEnvelope.getBody().getFault() != null)) {
                    axisFault = new AxisFault(soapEnvelope.getBody().getFault());
                } else if ((rmFaultEnvelope != null) && (rmFaultEnvelope.getBody().getFault() != null)) {
                    axisFault = new AxisFault(rmFaultEnvelope.getBody().getFault());
                } else if (responseMessageContext.getProperty(MercuryConstants.RM_AXIS_FAULT) != null){
                    axisFault = (AxisFault) responseMessageContext.getProperty(MercuryConstants.RM_AXIS_FAULT);
                } else {
                    axisFault = new AxisFault((String) responseMessageContext.getProperty(MercuryConstants.RM_ERROR));
                }

                throw axisFault;
            }
        }



        return InvocationResponse.SUSPEND;
    }
}

