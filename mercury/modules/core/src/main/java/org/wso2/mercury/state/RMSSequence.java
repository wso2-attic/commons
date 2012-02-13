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
package org.wso2.mercury.state;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.RelatesTo;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.modules.Module;
import org.apache.axis2.util.CallbackReceiver;
import org.apache.axis2.transport.RequestResponseTransport;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.om.impl.builder.StAXBuilder;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.message.*;
import org.wso2.mercury.context.MercuryMessageContext;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.RMDispatchInfo;
import org.wso2.mercury.exception.*;
import org.wso2.mercury.workers.MessageWorker;
import org.wso2.mercury.workers.ErrorCallbackWorker;
import org.wso2.mercury.workers.RMSSequenceWorker;
import org.wso2.mercury.persistence.dto.*;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.callback.MercuryErrorCallback;
import org.wso2.mercury.security.SecurityToken;
import org.wso2.mercury.security.RMSecurityManager;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import java.util.*;
import java.io.ByteArrayInputStream;

/**
 * this class is used to keep the RMS sequence. RMS Sequence is
 * depends on four parameters
 * SS - Sequence Started
 * MIB - Messages in the buffer
 * TMR - terminate message received
 * LMR - last message received
 * Here we assume either TMR or LMR occurs.
 * Collectively these four state variables forms 16 possible states
 * but only 7 states can realy exists.
 * <p/>
 * External events that can change this state machine are
 * CRR - Create Sequence Response Receive
 * LMR - Last message receive
 * TMR - Terminate message receive
 * ACKALL - Receive Acknowledgments for all messages.
 * <p/>
 * when those event occurs they change the sate accordingly
 * seqence processor always go through this state table and do
 * the appropriate tasks according to the state.
 */

public class RMSSequence {

    private static Log log = LogFactory.getLog(RMSSequence.class);

    private long retransmitTime;
    private long timeoutTime;
    private long maximumRetrasmitCount;
    private boolean exponentialBackoff;

    private long createSequenceRetransmitCount = 1;

    // Possible states for this state machine.
    // This state machine state is depends on SS,MIB,TMR,LMR and 0
    // and 1 represents absent and present of each state.
    public static final int STATE_0100 = 0;
    public static final int STATE_0101 = 1;
    public static final int STATE_0110 = 2;
    public static final int STATE_1110 = 3;
    public static final int STATE_1000 = 4;
    public static final int STATE_1100 = 5;
    public static final int STATE_1101 = 6;
    public static final int STATE_TERMINATE = 7;

    private int state;

    // this is used to keep a set of application message context objects
    // to be send.
    private Map messageBuffer;

    private long messageNumber = 0;
    private long lastMessageNumber;

    //timing information
    private long lastCreateSequnceMessageSendTime = 0;
    private long lastAccessedTime;

    private EndpointReference endPointReference;
    private EndpointReference ackToEpr;

    // WSRM sequence ID for this sequence
    private String sequenceID;

    private Axis2Info axis2Info;

    private String sequenceOffer;

    private RMSSequenceDto persistanceDto;

    private boolean isAnnonymous;

    private RMDSequence offeredRMDSequence;

    private MercuryErrorCallback errorCallback;

    private RMSSequenceWorker rmsSequenceWorker;

    //TODO: think about a method to persists the security token.
    // security token should be set up before the sequence starts up if
    // the user has specified.
    private SecurityToken securityToken;

    // keep the mep for this RM Sequence. this is useful for persistance and hence recovering
    // RM sequences at a machine crash.
    private String mep;

    public RMSSequence(int state,
                       EndpointReference endPointReference) {
        this.state = state;
        this.endPointReference = endPointReference;
        this.messageBuffer = new HashMap();
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public synchronized void addRMMessageContext(RMApplicationMessage message,
                                                 MessageContext messageContext) throws PersistenceException {
        this.lastAccessedTime = System.currentTimeMillis();
        long number = ++messageNumber;
        if (this.persistanceDto != null) {
            this.persistanceDto.setMessageNumber(messageNumber);
        }
        Sequence sequence = message.getSequence();
        sequence.setMessageNumber(number);
        if (sequence.isLastMessage()) {
            lastMessageNumber = number;
            if (this.persistanceDto != null) {
                this.persistanceDto.setLastMessageNumber(lastMessageNumber);
            }
        }
        PersistenceManager persistenceManager = getPersistanceManager();
        RMSMessageDto rmsMessageDto = null;
        if (persistenceManager != null) {
            // persists this message.
            rmsMessageDto = new RMSMessageDto();
            rmsMessageDto.setMessageNumber(sequence.getMessageNumber());
            rmsMessageDto.setLastMessage(sequence.isLastMessage());
            rmsMessageDto.setSoapEnvelpe(message.getOriginalMessage().toString());
            rmsMessageDto.setSend(false);
            rmsMessageDto.setRmsSequenceID(this.persistanceDto.getId());
            rmsMessageDto.setAxisMessageID(messageContext.getMessageID());
            if (messageContext.getRelatesTo() != null) {
                rmsMessageDto.setRelatesToMessageID(messageContext.getRelatesTo().getValue());
            }
            rmsMessageDto.setCallBackClassName(getCallBackClassName(messageContext));
            rmsMessageDto.setAction(messageContext.getOptions().getAction());
            rmsMessageDto.setOperationName(messageContext.getAxisOperation().getName());

            try {
                persistenceManager.save(rmsMessageDto, this.persistanceDto);
            } catch (PersistenceException e) {
                messageNumber--;
                this.persistanceDto.setMessageNumber(messageNumber);
                if (sequence.isLastMessage()) {
                    this.lastMessageNumber = 0;
                    this.persistanceDto.setLastMessageNumber(0);
                }
                log.error("Can not save the message with message number "
                        + sequence.getMessageNumber(), e);
                throw new PersistenceException("Can not save the message with message number "
                        + sequence.getMessageNumber(), e);
            }
        }

        // store this message as MercuryMessageContext.
        this.messageBuffer.put(new Long(number),
                new MercuryMessageContext(rmsMessageDto, message, messageContext));
        // notify all the threads waiting for this object message receice.
        // this is important for annonymous inout operations.
        this.notifyAll();
    }


    private String getCallBackClassName(MessageContext messageContext){
        AxisOperation axisOperation = messageContext.getAxisOperation();
        MessageReceiver messageReceiver = axisOperation.getMessageReceiver();
        String className = null;
        if (messageReceiver instanceof CallbackReceiver){
            CallbackReceiver callbackReceiver = (CallbackReceiver) messageReceiver;
            AxisCallback axisCallback =
                    (AxisCallback) callbackReceiver.getCallbackStore().get(messageContext.getMessageID());
            if (axisCallback != null){
                className = axisCallback.getClass().getName();
            }
        }
        return className;
    }

    /**
     * sender worker invokes this method time to time and excecute the correct actions
     */
    public synchronized void doActions() throws AxisFault, RMMessageBuildingException {
        switch (state) {
            case STATE_0100 : {
                retransmitCreateSequenceMessage();
                break;
            }
            case STATE_0101 : {
                retransmitCreateSequenceMessage();
                break;
            }
            case STATE_0110 : {
                retransmitCreateSequenceMessage();
                break;
            }
            case STATE_1110 : {
                // i.e. it has got the create sequence response and knows there are
                // no more messages from client but still messages in the buffer.
                retransmitApplicationMessages();
                break;
            }
            case STATE_1000 : {
                // nothing to do since buffer is empty
                break;
            }
            case STATE_1100 : {
                retransmitApplicationMessages();
                break;
            }
            case STATE_1101 : {
                retransmitApplicationMessages();
                break;
            }
        }
    }

    private void retransmitCreateSequenceMessage() throws AxisFault, RMMessageBuildingException {
        // i.e still create sequence response message has not received. but there
        // is a message in the buffer.
        // the only thing we have to do here is to send another createSequence message
        // if it is time to send.
        if ((lastCreateSequnceMessageSendTime == 0) || // this means no message has send
                ((System.currentTimeMillis() - lastCreateSequnceMessageSendTime) > calculateRetransmitTime(createSequenceRetransmitCount))) {
            //we have to send a create sequence message
            createSequenceRetransmitCount++;
            sendCreateSequenceMessage();
            lastCreateSequnceMessageSendTime = System.currentTimeMillis();
        }
    }

    private void sendCreateSequenceMessage() throws AxisFault, RMMessageBuildingException {
        // create the create sequence message
        CreateSequenceMessage createSequenceMessage = new CreateSequenceMessage();
        createSequenceMessage.setAcksToAddress(this.ackToEpr.getAddress());
        createSequenceMessage.setSoapNamesapce(this.axis2Info.getSoapNamespaceURI());
        createSequenceMessage.setAddressingNamespace(this.axis2Info.getAddressingNamespaceURI());
        createSequenceMessage.setOfferIdentifier(this.sequenceOffer);


        MessageContext messageContext = getNewMessageContextUsingAxis2Info(createSequenceMessage);
        messageContext.getOptions().setAction(MercuryConstants.CREATE_SEQUENCE_ACTION);
        messageContext.getOptions().setTimeOutInMilliSeconds(this.axis2Info.getOptions().getTimeOutInMilliSeconds());

        AxisOperation axisOperation = null;
        if (this.axis2Info.isServerSide()){
            axisOperation = this.axis2Info.getAxisService().getOperationByAction(
                    MercuryConstants.CREATE_SEQUENCE_ACTION);
        } else {
            axisOperation = this.axis2Info.getAxisService().getOperation(ServiceClient.ANON_OUT_IN_OP);
        }

        OperationContext operationContext =
                this.axis2Info.getServiceContext().createOperationContext(axisOperation);

        axisOperation.registerOperationContext(messageContext, operationContext);
        messageContext.setAxisMessage(axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE));

        //register this message with the RMSContext
        RMSContext rmsContext =
                (RMSContext) messageContext.getConfigurationContext().getProperty(MercuryConstants.RMS_CONTEXT);
        rmsContext.registerRMSSequenceToMessageID(messageContext.getMessageID(), this);

        //sending the message
        if (!this.axis2Info.isServerSide() && isAnnonymous) {
            MessageContext responseMessageContext = messageContext.getConfigurationContext().createMessageContext();
            responseMessageContext.setServerSide(false);
            axisOperation.registerOperationContext(responseMessageContext, operationContext);
            responseMessageContext.setOptions(messageContext.getOptions());
            responseMessageContext.getOptions().setAction(MercuryConstants.CREATE_SEQUENCE_RESPONSE_ACTION);
            responseMessageContext.setMessageID(UUIDGenerator.getUUID());
            responseMessageContext.setServiceContext(messageContext.getServiceContext());
            responseMessageContext.setAxisMessage(axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));
            sendMessage(messageContext, responseMessageContext, false, true, false);
        } else {
            sendMessage(messageContext, null, false, false, false);
        }


    }

    /**
     * create sequence message received event has occured do the necessary state changes accordingly.
     * this event is only relavant to the sequence started state.
     */
    public synchronized void createSequenceResponseReceived() throws PersistenceException {
        this.lastAccessedTime = System.currentTimeMillis();
        int currentState = state;
        switch (state) {
            case STATE_0100 : {
                // advance state to 1100
                state = STATE_1100;
                break;
            }
            case STATE_0101 : {
                state = STATE_1101;
                break;
            }
            case STATE_0110 : {
                state = STATE_1110;
                break;
            }
            case STATE_1110 : {
                break;
            }
            case STATE_1000 : {
                break;
            }
            case STATE_1100 : {
                break;
            }
            case STATE_1101 : {
                break;
            }
        }

        PersistenceManager persistenceManager = getPersistanceManager();
        if (persistenceManager != null) {
            this.persistanceDto.setState(this.state);
            this.persistanceDto.setLastAccessedTime(this.lastAccessedTime);
            this.persistanceDto.setSequenceID(this.sequenceID);
            try {
                persistenceManager.update(this.persistanceDto);
            } catch (PersistenceException e) {
                // roll back the state
                this.persistanceDto.setState(currentState);
                this.state = currentState;
                this.sequenceID = null;
                log.error("Can not updated the RMS state for received create sequene " +
                        "response message ", e);
                throw new PersistenceException("Can not updated the RMS state for received create sequene " +
                        "response message ", e);
            }
        }

        // call continueWork to wake up RMSSequenceWorker
        // this is an performance improvement to call the RMSSequeceWorker immediately
        continueWork();
    }

    private void sendMessage(MessageContext messageContext,
                             MessageContext responseMessageContext,
                             boolean isResume,
                             boolean isInvokeAsAnonClient,
                             boolean isUseSameThread) {
        MessageWorker messageWorker =
                new MessageWorker(messageContext, responseMessageContext, isResume, isInvokeAsAnonClient);
        if (isInvokeAsAnonClient){
            messageWorker.setRmsSequence(this);
        }
        if (isUseSameThread) {
            messageWorker.run();
        } else {
            // we need to invoke this as annonymous client
            messageContext.getConfigurationContext().getThreadPool().execute(messageWorker);
        }
    }

    private MessageContext getNewMessageContextUsingAxis2Info(CreateSequenceMessage createSequenceMessage)
            throws AxisFault, RMMessageBuildingException {
        MessageContext messageContext = new MessageContext();
        messageContext.setServiceContext(this.axis2Info.getServiceContext());
        messageContext.setMessageID(UUIDGenerator.getUUID());
        messageContext.setTransportIn(this.axis2Info.getTransportIn());
        messageContext.setTransportOut(this.axis2Info.getTransportOut());
        messageContext.setReplyTo(this.ackToEpr);
        messageContext.setProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);
        messageContext.setOptions(new Options());
        // set the correct options
        messageContext.getOptions().setTo(this.axis2Info.getOptions().getTo());
        messageContext.getOptions().setUseSeparateListener(this.axis2Info.getOptions().isUseSeparateListener());
        messageContext.setServerSide(this.axis2Info.isServerSide());

        // create the security token reference element from the security token
        if (this.securityToken != null){
            RMSecurityManager rmSecurityManager = getRMSecurityManager();
            try {
                OMElement securityTokenReference = rmSecurityManager.createSecurityTokenReference(this.securityToken, messageContext);
                createSequenceMessage.setSecurityTokenReference(securityTokenReference);
            } catch (RMSecurityException e) {
                log.error("Can not create the securityTokenReference Element", e);
                throw new AxisFault("Can not create the securityTokenReference Element", e);
            }
        }

        messageContext.setEnvelope(createSequenceMessage.toSOAPEnvelope());
        return messageContext;
    }

    public void removeRelationShips(Options options) {
        if (options.getParent() != null) {
            removeRelationShips(options.getParent());
        }
        options.setRelationships(null);
    }

    private void retransmitApplicationMessages() throws AxisFault, RMMessageBuildingException {
        MercuryMessageContext mercuryMessageContext = null;
        RMApplicationMessage message = null;
        MessageContext applicationMessageContext = null;
        for (Iterator iter = messageBuffer.values().iterator(); iter.hasNext();) {
            mercuryMessageContext = (MercuryMessageContext) iter.next();
            long lastMessageSentTime = mercuryMessageContext.getLastMessageSendTime();
            long retransmitCount = mercuryMessageContext.getRetransmitCount();

            if ((lastMessageSentTime == 0) || // this means this has no transmitted
                    (((System.currentTimeMillis() - lastMessageSentTime) > calculateRetransmitTime(retransmitCount))
                            && (retransmitCount < maximumRetrasmitCount))) {

                // have to retransmit the message
                message = mercuryMessageContext.getRmApplicationMessage();

                //for annonymous sequeces we should not retransmit messages if
                // we have not get an acknowledgement for all the messages having a less
                // sequence number
                // in other way we will send only the message with the least message number
                if (this.isAnnonymous){
                    long messageNumber = message.getSequence().getMessageNumber();
                    if (this.messageBuffer.containsKey(new Long(messageNumber - 1))){
                        continue;
                    }
                }

                message.getSequence().setSequenceID(sequenceID);
                applicationMessageContext = mercuryMessageContext.getMessageContext();
                applicationMessageContext.setCurrentHandlerIndex(mercuryMessageContext.getCurrentHandlerIndex());

                // if this has an offered sequence we have to append the sequence
                // acknowledgment message
                if (this.offeredRMDSequence != null){
                    SequenceAcknowledgment sequenceAcknowledgment =
                            this.offeredRMDSequence.getSequenceAcknowledgment();
                    if (sequenceAcknowledgment.getAcknowledgmentRanges().size() > 0){
                        message.setSequenceAcknowledgment(sequenceAcknowledgment);
                    }
                }

                applicationMessageContext.setEnvelope(message.toSOAPEnvelope());
                applicationMessageContext.setProperty(
                        MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);

                // when sending in only application messages in annonymous client
                // the returend message is a sequence acknowledgment.
                // have to set this before sending messages.
                // async messages waits util response comes.
                mercuryMessageContext.setLastMessageSendTime(System.currentTimeMillis());
                mercuryMessageContext.increaseRetransmitCount();

                if (!this.axis2Info.isServerSide() && isAnnonymous) {
                    if (applicationMessageContext.getAxisOperation().getMessageExchangePattern().equals(
                            WSDL2Constants.MEP_URI_OUT_ONLY)) {
                        MessageContext responseMessageContext =
                                this.axis2Info.getConfigurationContext().createMessageContext();
                        responseMessageContext.setServerSide(false);
                        responseMessageContext.setServiceContext(this.axis2Info.getServiceContext());
                        responseMessageContext.setMessageID(UUIDGenerator.getUUID());

                        AxisService axisServce = this.axis2Info.getAxisService();
                        AxisOperation inOnlyOperation = null;
                        if (applicationMessageContext.getOptions().getAction().equals(MercuryConstants.LAST_MESSAGE_ACTION) &&
                                (this.offeredRMDSequence != null)) {
                             // this means this is a last message and in out message so we have to
                             // set the last message to get the message
                            responseMessageContext.getOptions().setAction(MercuryConstants.LAST_MESSAGE_ACTION);
                            inOnlyOperation = axisServce.getOperationByAction(MercuryConstants.LAST_MESSAGE_ACTION);
                        } else {
                            responseMessageContext.getOptions().setAction(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT_ACTION);
                            inOnlyOperation = axisServce.getOperationByAction(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT_ACTION);
                        }


                        OperationContext operationContext =
                                this.axis2Info.getServiceContext().createOperationContext(inOnlyOperation);
                        inOnlyOperation.registerOperationContext(responseMessageContext, operationContext);

                        responseMessageContext.setAxisMessage(
                                inOnlyOperation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));
                        // set this as the piggy back message for out going message context
                        // this is usful only to smtp transport
                        applicationMessageContext.setProperty(Constants.PIGGYBACK_MESSAGE, responseMessageContext);

                        sendMessage(applicationMessageContext, responseMessageContext, true, true, false);
                    } else {
                        // for inout operation it should be the response message context for the operation
                        MessageContext responseMessageContext =
                                applicationMessageContext.getOperationContext().getMessageContext(
                                        WSDLConstants.MESSAGE_LABEL_IN_VALUE);

                      sendMessage(applicationMessageContext, responseMessageContext, true, true, false);
                    }

                } else {
                    // this is a normal duplex channel mode inonly message
                    sendMessage(applicationMessageContext, null, true, false, false);
                }

            }
        }
    }

    public synchronized void sendApplicationMessageResponse(MessageContext inboundMessageContext,
                                                            long inMessageNumber)
            throws AxisFault, RMMessageBuildingException {
       // here we have to keep in mind that invoker buffer always gurantee the inorder delivary. therefore
       // the first message always corresponds to the first message in the return sequence
        if (this.messageBuffer.containsKey(new Long(inMessageNumber))) {
            // if the message buffer has this message number then we can send this using
            // orginal message context
            MercuryMessageContext mercuryMessageContext = (MercuryMessageContext)
                    this.messageBuffer.get(new Long(inMessageNumber));
            RMApplicationMessage message = mercuryMessageContext.getRmApplicationMessage();
            message.getSequence().setSequenceID(sequenceID);
            MessageContext outBoundMessageContext = mercuryMessageContext.getMessageContext();
            outBoundMessageContext.setCurrentHandlerIndex(mercuryMessageContext.getCurrentHandlerIndex());

            // set the sequence acknowledgment
            if (this.offeredRMDSequence != null) {
                SequenceAcknowledgment sequenceAcknowledgment =
                        this.offeredRMDSequence.getSequenceAcknowledgment();
                if (sequenceAcknowledgment.getAcknowledgmentRanges().size() > 0){
                    message.setSequenceAcknowledgment(sequenceAcknowledgment);
                }
            }

            outBoundMessageContext.setEnvelope(message.toSOAPEnvelope());
            outBoundMessageContext.setProperty(
                    MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);

            // copy the transport details.
            // i.e we have to send the message using the backchanel of the incomming message
            outBoundMessageContext.setTransportOut(inboundMessageContext.getTransportOut());
            outBoundMessageContext.setProperty(MessageContext.TRANSPORT_OUT,
                    inboundMessageContext.getProperty(MessageContext.TRANSPORT_OUT));
            outBoundMessageContext.setProperty(Constants.OUT_TRANSPORT_INFO,
                    inboundMessageContext.getProperty(Constants.OUT_TRANSPORT_INFO));
            // this is very important in writting the message using the same channel
            // once response input stream is written it updates this property
            outBoundMessageContext.setProperty(RequestResponseTransport.TRANSPORT_CONTROL,
                    inboundMessageContext.getProperty(RequestResponseTransport.TRANSPORT_CONTROL));
            try {
                if (outBoundMessageContext.isProcessingFault()){
                    AxisEngine.resumeSendFault(outBoundMessageContext);
                } else {
                    AxisEngine.resumeSend(outBoundMessageContext);
                }
            } catch (Exception e) {
                log.error("Can not resume message sending");
            }

            mercuryMessageContext.setLastMessageSendTime(System.currentTimeMillis());
        } else {
           if (inMessageNumber > messageNumber ){
               // this means that response message for this inMessage number has not yet received.
               // so wait until mesage receives

               try {
                   // this wait will notify in the last message receive and message received events
                   this.wait();
                   // then try again
                   sendApplicationMessageResponse(inboundMessageContext, inMessageNumber);
               } catch (InterruptedException e) {}
           } else {
               // this means an acknowledgment for this message is already received .
               // that means receiver has got the message correctly. so send the
               // sequence acknowledgment only.
               if (this.offeredRMDSequence != null){
                   this.offeredRMDSequence.sendSequenceAcknowledgementMessage(inboundMessageContext);
               }
           }
       }

    }

    public synchronized void sequenceAcknowledgmentReceived(List acknowledgmentRanges) throws PersistenceException {
        this.lastAccessedTime = System.currentTimeMillis();
        if (this.persistanceDto != null) {
            this.persistanceDto.setLastAccessedTime(this.lastAccessedTime);
        }
        // update the state of the RMSSequenceDto
        switch (state) {
            case STATE_0100 : {
                //TODO: we can not get an acknowledgment before starting the sequence
                log.error("Acknowledgment received before statring the sequence");
                break;
            }
            case STATE_0101 : {
                //TODO: we can not get an acknowledgment before starting the sequence
                log.error("Acknowledgment received before statring the sequence");
                break;
            }
            case STATE_0110 : {
                //TODO: we can not get an acknowledgment before starting the sequence
                log.error("Acknowledgment received before statring the sequence");
                break;
            }
            case STATE_1110 : {
                processRangesAndUpdateState(acknowledgmentRanges, STATE_TERMINATE);
                break;
            }
            case STATE_1000 : {
                // nothing todo
                break;
            }
            case STATE_1100 : {
                processRangesAndUpdateState(acknowledgmentRanges, STATE_1000);
                break;
            }
            case STATE_1101 : {
                processRangesAndUpdateState(acknowledgmentRanges, STATE_TERMINATE);
                break;
            }
        }

        // call continueWork to wake up RMSSequenceWorker
        // this is an performance improvement to call the RMSSequeceWorker immediately
        continueWork();
    }

    private void processRangesAndUpdateState(List acknowledgmentRanges, int newState)
            throws PersistenceException {
        Set acknowledgedMessages = processAcknowledgeMessages(acknowledgmentRanges);
        PersistenceManager persistenceManager = getPersistanceManager();
        if (persistenceManager != null) {
            // update the state in database
            if (this.messageBuffer.size() == acknowledgedMessages.size()) {
                // this means all the messaes has been acknoledged
                this.persistanceDto.setState(newState);
            }
            Set acknolwdgeMessageDtos = new HashSet();
            MercuryMessageContext mercuryMessageContext = null;
            for (Iterator iter = acknowledgedMessages.iterator(); iter.hasNext();) {
                mercuryMessageContext = (MercuryMessageContext) this.messageBuffer.get(iter.next());
                mercuryMessageContext.getRmsPersistanceDto().setSend(true);
                acknolwdgeMessageDtos.add(mercuryMessageContext.getRmsPersistanceDto());
            }
            // update the states
            try {
                persistenceManager.updateMessagesAsSend(acknolwdgeMessageDtos, this.persistanceDto);
            } catch (PersistenceException e) {
                this.persistanceDto.setState(this.state);
                for (Iterator iter = acknowledgedMessages.iterator(); iter.hasNext();) {
                    mercuryMessageContext = (MercuryMessageContext) this.messageBuffer.get(iter.next());
                    mercuryMessageContext.getRmsPersistanceDto().setSend(false);
                }
                log.error("Can not update the RMSMessages", e);
                throw new PersistenceException("Can not update the RMSMessages", e);
            }

        }

        // remove these messages from the internal buffer
        for (Iterator iter = acknowledgedMessages.iterator(); iter.hasNext();) {
            this.messageBuffer.remove(iter.next());
        }

        if (this.messageBuffer.size() == 0) {
            this.state = newState;
        }
    }

    private Set processAcknowledgeMessages(List acknowledgmentRanges) {
        // remove the buffered messages if they have acknowledged
        Set acknowledgedMessages = new HashSet();
        Long key;
        for (Iterator iter = this.messageBuffer.keySet().iterator(); iter.hasNext();) {
            key = (Long) iter.next();
            if (isNumberAcknowledged(key.longValue(), acknowledgmentRanges)) {
                acknowledgedMessages.add(key);
            }
        }
        return acknowledgedMessages;
    }

    public boolean isNumberAcknowledged(long number, List acknowledgments) {
        boolean isNumberAcknowledged = false;
        AcknowledgmentRange acknowledgmentRange = null;
        for (Iterator iter = acknowledgments.iterator(); iter.hasNext();) {
            acknowledgmentRange = (AcknowledgmentRange) iter.next();
            if (acknowledgmentRange.isNumberInRange(number)) {
                isNumberAcknowledged = true;
                break;
            }
        }
        return isNumberAcknowledged;
    }

    public synchronized void applicationMessageReceivedFromClient(
            RMApplicationMessage message,
            MessageContext messageContext) throws PersistenceException {
        this.lastAccessedTime = System.currentTimeMillis();
        if (this.persistanceDto != null) {
            this.persistanceDto.setLastAccessedTime(this.lastAccessedTime);
        }
        int currentState = state;
        try {
            switch (state) {
                case STATE_0100 : {
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_0101 : {
                    state = STATE_1101;
                    if (this.persistanceDto != null) {
                        this.persistanceDto.setState(state);
                    }
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_0110 : {
                    // it is not possible to get application messages now.
                    // TODO: throw exception
                    log.error("Receiving an application message after receiving the terminate message");
                    break;
                }
                case STATE_1110 : {
                    // it is not possible to get application messages now.
                    // TODO: throw exception
                    log.error("Receiving an application message after receiving the terminate message");
                    break;
                }
                case STATE_1000 : {
                    state = STATE_1100;
                    if (this.persistanceDto != null) {
                        this.persistanceDto.setState(state);
                    }
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_1100 : {
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_1101 : {
                    addRMMessageContext(message, messageContext);
                    break;
                }
            }
        } catch (PersistenceException e) {
            // restore the state
            this.state = currentState;
            this.persistanceDto.setState(currentState);
            log.error("Can not add the message ", e);
            throw new PersistenceException("Can not add the message ", e);
        }
    }

    public synchronized void lastMessageReceivedFromClient(
            RMApplicationMessage message,
            MessageContext messageContext) throws PersistenceException {
        this.lastAccessedTime = System.currentTimeMillis();
        if (this.persistanceDto != null) {
            this.persistanceDto.setLastAccessedTime(this.lastAccessedTime);
        }
        int currentState = state;
        try {
            switch (state) {
                case STATE_0100 : {
                    state = STATE_0101;
                    if (this.persistanceDto != null) {
                        this.persistanceDto.setState(this.state);
                    }
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_0101 : {
                    //Two last messages recevied this is wrong
                    //TODO: throw an exception
                    log.error("Receiving a last message after one last message receives");
                    break;
                }
                case STATE_0110 : {
                    //terminate message recevied this is wrong
                    //TODO: throw an exception
                    log.error("Receiving a last message after one last message receives");
                    break;
                }
                case STATE_1110 : {
                    //terminate message recevied this is wrong
                    // TODO: throw exception
                    log.error("Receiving a last message after one last message receives");
                    break;
                }
                case STATE_1000 : {
                    state = STATE_1101;
                    if (this.persistanceDto != null) {
                        this.persistanceDto.setState(this.state);
                    }
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_1100 : {
                    state = STATE_1101;
                    if (this.persistanceDto != null) {
                        this.persistanceDto.setState(this.state);
                    }
                    addRMMessageContext(message, messageContext);
                    break;
                }
                case STATE_1101 : {
                    //Two last messages recevied this is wrong
                    //TODO: throw an exception
                    break;
                }
            }
        } catch (PersistenceException e) {
            // restore the state
            this.state = currentState;
            this.persistanceDto.setState(currentState);
            log.error("Can not save the last message ", e);
            throw new PersistenceException("Can not save the last message ", e);
        }

        // call continueWork to wake up RMSSequenceWorker
        // this is an performance improvement to call the RMSSequeceWorker immediately
        continueWork();
    }

    public synchronized void terminateMessageReceivedFromClient() throws PersistenceException {
        this.lastAccessedTime = System.currentTimeMillis();
        if (this.persistanceDto != null) {
            this.persistanceDto.setLastAccessedTime(this.lastAccessedTime);
        }
        int currentState = this.state;
        switch (state) {
            case STATE_0100 : {
                state = STATE_0110;
                break;
            }
            case STATE_0101 : {
                // we assume not to happen this
                break;
            }
            case STATE_0110 : {
                //terminate message recevied this is wrong
                //TODO: throw an exception
                log.error("Receiving a termiante message after getting one terminate message");
                break;
            }
            case STATE_1110 : {
                //terminate message recevied this is wrong
                // TODO: throw exception
                log.error("Receiving a termiante message after getting one terminate message");
                break;
            }
            case STATE_1000 : {
                state = STATE_TERMINATE;
                break;
            }
            case STATE_1100 : {
                state = STATE_1110;
                break;
            }
            case STATE_1101 : {
                // we expect not to happen this state.
                break;
            }
        }

        PersistenceManager persistenceManager = getPersistanceManager();
        if (persistenceManager != null) {
            this.persistanceDto.setState(this.state);
            try {
                persistenceManager.update(this.persistanceDto);
            } catch (PersistenceException e) {
                //rever the state
                this.persistanceDto.setState(currentState);
                this.state = currentState;
                log.error("Can not update the state", e);
                throw new PersistenceException("Can not update the state", e);
            }
        }
    }

    public synchronized void sendTerminateSequenceMessage(MessageContext inboundMessageContext)
            throws AxisFault, RMMessageBuildingException {

        // we have to send the terminate message only if this sequence has established correctly.
        if (this.sequenceID == null){
            log.info("Sequence has been terminated due to an error");
            return;
        }

        // set the seuqence end time
        // if there is a problem with the persistence we don't care
        log.info("Sending the termainate message for the sequence " + this.sequenceID);
        PersistenceManager persistenceManager = getPersistanceManager();
        if (persistenceManager != null) {
            this.persistanceDto.setLastAccessedTime(System.currentTimeMillis());
            this.persistanceDto.setEndTime(System.currentTimeMillis());
            try {
                persistenceManager.update(this.persistanceDto);
            } catch (PersistenceException e) {
                log.error("Error in updating the sequence to terminate state. how ever sequene" +
                        " is terminated correctly ");
            }
        }
        TerminateSequenceMessage terminateSequenceMessage = new TerminateSequenceMessage(sequenceID);
        terminateSequenceMessage.setSoapNamesapce(this.axis2Info.getSoapNamespaceURI());

        MessageContext messageContext =
                getNewMessageContextUsingAxis2Info(terminateSequenceMessage, inboundMessageContext);

        messageContext.getOptions().setAction(MercuryConstants.TERMINATE_SEQUENCE_ACTION);
        messageContext.setReplyTo(new EndpointReference(AddressingConstants.Final.WSA_NONE_URI));

        // here we have to use OutOnly operation otherwise smtp transport sender
        // may register a call back for this.
        AxisOperation axisOperation = null;
        if (this.axis2Info.isServerSide()) {
            axisOperation = this.axis2Info.getAxisService().getOperationByAction(
                    MercuryConstants.TERMINATE_SEQUENCE_ACTION);
            messageContext.setAxisMessage(axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));
        } else {
            axisOperation = this.axis2Info.getAxisService().getOperation(ServiceClient.ANON_OUT_ONLY_OP);
            messageContext.setAxisMessage(axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE));
        }

        OperationContext operationContext =
                this.axis2Info.getServiceContext().createOperationContext(axisOperation);
        axisOperation.registerOperationContext(messageContext, operationContext);

        //sending the message
        if (inboundMessageContext == null){
            // if there is no inbound messagecontext we send this message
            // using a new thread. i.e at the client side

            // if this is an annonymous and inout operation then we have to register
            // a response message context
            if (this.offeredRMDSequence != null) {
                MessageContext responseMessageContext =
                        this.axis2Info.getConfigurationContext().createMessageContext();
                responseMessageContext.setServerSide(false);
                responseMessageContext.setServiceContext(this.axis2Info.getServiceContext());
                responseMessageContext.setMessageID(UUIDGenerator.getUUID());

                OperationContext responseOperationContext =
                        this.axis2Info.getServiceContext().createOperationContext(axisOperation);
                responseMessageContext.getOptions().setAction(MercuryConstants.TERMINATE_SEQUENCE_ACTION);
                //for incomming messages we can use the terminate operation
                axisOperation = axisOperation = this.axis2Info.getAxisService().getOperationByAction(
                    MercuryConstants.TERMINATE_SEQUENCE_ACTION);
                axisOperation.registerOperationContext(responseMessageContext, responseOperationContext);

                responseMessageContext.setAxisMessage(
                        axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));

                // set the piggy back message
                messageContext.setProperty(Constants.PIGGYBACK_MESSAGE, responseMessageContext);

                sendMessage(messageContext, responseMessageContext, false, true, false);
            } else {
                sendMessage(messageContext, null, false, false, false);
            }
        } else {
            // for annonymous messages we use the same thread.
            // this happens at the server side.
            sendMessage(messageContext, null, false, false, true);
        }

    }

    private MessageContext getNewMessageContextUsingAxis2Info(
            TerminateSequenceMessage terminateSequenceMessage, MessageContext inboundMessageContext)
            throws AxisFault, RMMessageBuildingException {
        MessageContext messageContext = new MessageContext();
        messageContext.setServiceContext(this.axis2Info.getServiceContext());
        if (this.offeredRMDSequence != null){
            // this means this is an Annonymous in out sequence and we have to append the
            // sequence acknowledgement for it
           SequenceAcknowledgment sequenceAcknowledgment = this.offeredRMDSequence.getSequenceAcknowledgment();
           if (sequenceAcknowledgment.getAcknowledgmentRanges().size() > 0){
               terminateSequenceMessage.setSequenceAcknowledgment(sequenceAcknowledgment);
           }
        }
        messageContext.setEnvelope(terminateSequenceMessage.toSOAPEnvelope());
        messageContext.setMessageID(UUIDGenerator.getUUID());

        messageContext.setProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);
        messageContext.setOptions(new Options());
        messageContext.getOptions().setUseSeparateListener(this.axis2Info.getOptions().isUseSeparateListener());
        messageContext.setServerSide(this.axis2Info.isServerSide());
        messageContext.getOptions().setProperty(AddressingConstants.WS_ADDRESSING_VERSION,
                this.axis2Info.getAddressingNamespaceURI());

        if (inboundMessageContext == null) {
            // i.e. inbound Message context means this is a duplex sequence.
            messageContext.setTransportOut(this.axis2Info.getTransportOut());
            messageContext.setTo(this.axis2Info.getOptions().getTo());
        } else {
            // set some security related properties
            // TODO: read these from the module xml file
            messageContext.setProperty("RECV_RESULTS", inboundMessageContext.getProperty("RECV_RESULTS"));
            // i.e we have to send the message using the backchanel of the incomming message
            messageContext.setProperty(Constants.Configuration.MESSAGE_TYPE,
                    inboundMessageContext.getProperty(Constants.Configuration.MESSAGE_TYPE));
            messageContext.setTransportOut(inboundMessageContext.getTransportOut());
            messageContext.setProperty(MessageContext.TRANSPORT_OUT,
                    inboundMessageContext.getProperty(MessageContext.TRANSPORT_OUT));
            messageContext.setProperty(Constants.OUT_TRANSPORT_INFO,
                    inboundMessageContext.getProperty(Constants.OUT_TRANSPORT_INFO));
            // this is very important in writting the message using the same channel
            // once response input stream is written it updates this property
            messageContext.setProperty(RequestResponseTransport.TRANSPORT_CONTROL,
                    inboundMessageContext.getProperty(RequestResponseTransport.TRANSPORT_CONTROL));
        }
        return messageContext;
    }

    public void save(long internalKeyID) throws PersistenceException {
        RMSSequenceDto rmsSequenceDto = new RMSSequenceDto();
        rmsSequenceDto.setState(this.state);
        rmsSequenceDto.setMessageNumber(this.messageNumber);
        rmsSequenceDto.setLastMessageNumber(this.lastMessageNumber);
        rmsSequenceDto.setEndPointAddress(this.endPointReference.getAddress());
        if (this.ackToEpr != null){
            rmsSequenceDto.setAckToEpr(this.ackToEpr.getAddress());
        }
        rmsSequenceDto.setSequenceOffer(this.sequenceOffer);
        rmsSequenceDto.setInternalKeyID(internalKeyID);
        rmsSequenceDto.setStartTime(System.currentTimeMillis());
        rmsSequenceDto.setSequenceID(this.sequenceID);
        rmsSequenceDto.setMep(this.mep);

        //saving the axis2 info
        Axis2InfoDto axis2InfoDto = new Axis2InfoDto();
        axis2InfoDto.setServiceName(this.axis2Info.getAxisService().getName());
        axis2InfoDto.setCurrentHanlderIndex(this.axis2Info.getCurrentHandlerIndex());
        axis2InfoDto.setCurrentPhaseIndex(this.axis2Info.getCurrentPhaseIndex());
        axis2InfoDto.setServerSide(this.axis2Info.isServerSide());
        axis2InfoDto.setSoapNamespaceURI(this.axis2Info.getSoapNamespaceURI());
        axis2InfoDto.setAddressingNamespaceURI(this.axis2Info.getAddressingNamespaceURI());

        //add the properties to save. for the moment set the message type
        // TODO: read the properties to be saved from
        PropertyDto propertyDto = new PropertyDto();
        if (this.axis2Info.getProperty(Constants.Configuration.MESSAGE_TYPE) != null) {
            propertyDto.setName(Constants.Configuration.MESSAGE_TYPE);
            propertyDto.setValue(this.axis2Info.getProperty(Constants.Configuration.MESSAGE_TYPE).toString());
            axis2InfoDto.addProperty(propertyDto);
        }

        // save the engaged modules
        EngagedModuleDto engagedModuleDto = null;

        for (Object module : this.axis2Info.getAxisService().getEngagedModules()){
             engagedModuleDto = new EngagedModuleDto();
             engagedModuleDto.setModuleName(((AxisModule)module).getName());
             axis2InfoDto.addEngagedModule(engagedModuleDto);
        }

        getPersistanceManager().save(rmsSequenceDto, axis2InfoDto);
        this.persistanceDto = rmsSequenceDto;
    }

    private PersistenceManager getPersistanceManager() {
        PersistenceManager persistenceManager = null;
        if (this.axis2Info.getConfigurationContext().getProperty(
                MercuryConstants.RM_PERSISTANCE_MANAGER) != null) {
            persistenceManager = (PersistenceManager)
                    this.axis2Info.getConfigurationContext().getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER);
        }
        return persistenceManager;
    }

    private RMSecurityManager getRMSecurityManager() {
        RMSecurityManager rmSecurityManager = null;
        if (this.axis2Info.getConfigurationContext().getProperty(
                MercuryConstants.RM_SECURITY_MANAGER) != null) {
            rmSecurityManager = (RMSecurityManager)
                    this.axis2Info.getConfigurationContext().getProperty(MercuryConstants.RM_SECURITY_MANAGER);
        }
        return rmSecurityManager;
    }



    public void populatePersistnaceData(MessageContext messageContext, RMSSequenceDto rmsSequenceDto)
            throws PersistenceException, AxisFault {
        // set the RMS Sequence object status
        this.persistanceDto = rmsSequenceDto;
        this.sequenceID = rmsSequenceDto.getSequenceID();
        this.messageNumber = rmsSequenceDto.getMessageNumber();
        this.lastMessageNumber = rmsSequenceDto.getLastMessageNumber();
        this.ackToEpr = new EndpointReference(rmsSequenceDto.getAckToEpr());
        this.sequenceOffer = rmsSequenceDto.getSequenceOffer();
        this.lastAccessedTime = System.currentTimeMillis();

        // populate the message buffer
        PersistenceManager persistenceManager = getPersistanceManager();
        // persistence manager should not be null
        List rmsMessageDtos =
                persistenceManager.getRMSMessagesWithRMSSequenceID(this.persistanceDto.getId());
        RMSMessageDto rmsMessageDto = null;
        for (Iterator iter = rmsMessageDtos.iterator(); iter.hasNext();) {
            rmsMessageDto = (RMSMessageDto) iter.next();
            // create message contexts only for not send services
            if (!rmsMessageDto.isSend()) {
                // creating the message context
                MessageContext newMessageContext = new MessageContext();

                newMessageContext.setCurrentHandlerIndex(messageContext.getCurrentHandlerIndex());
                newMessageContext.setCurrentPhaseIndex(messageContext.getCurrentPhaseIndex());
                newMessageContext.setExecutionChain(messageContext.getExecutionChain());

                newMessageContext.setServiceContext(messageContext.getServiceContext());
                newMessageContext.setMessageID(rmsMessageDto.getAxisMessageID());
                if (rmsMessageDto.getRelatesToMessageID() != null) {
                    newMessageContext.addRelatesTo(new RelatesTo(rmsMessageDto.getRelatesToMessageID()));
                }
                newMessageContext.setTransportIn(messageContext.getTransportIn());
                newMessageContext.setTransportOut(messageContext.getTransportOut());

                newMessageContext.setTo(messageContext.getTo());
                newMessageContext.setReplyTo(messageContext.getReplyTo());

                newMessageContext.setOptions(new Options());
                newMessageContext.getOptions().setTo(messageContext.getTo());
                newMessageContext.getOptions().setUseSeparateListener(messageContext.getOptions().isUseSeparateListener());
                newMessageContext.getOptions().setAction(rmsMessageDto.getAction());
                newMessageContext.setServerSide(messageContext.isServerSide());


                AxisOperation inOutOperation =
                        messageContext.getAxisService().getOperationByAction(rmsMessageDto.getAction());
                if (inOutOperation == null){
                       inOutOperation = messageContext.getAxisOperation();
                }

                OperationContext operationContext =
                        messageContext.getServiceContext().createOperationContext(inOutOperation);
                inOutOperation.registerOperationContext(newMessageContext, operationContext);
                messageContext.setAxisMessage(inOutOperation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE));

                RMApplicationMessage rmApplicationMessage =
                        new RMApplicationMessage(getSoapEnvelope(rmsMessageDto.getSoapEnvelpe()));
                Sequence sequence = rmApplicationMessage.getSequence();
                if (sequence == null) {
                    sequence = new Sequence();
                    rmApplicationMessage.setSequence(sequence);
                }
                sequence.setLastMessage(rmsMessageDto.isLastMessage());
                sequence.setMessageNumber(rmsMessageDto.getMessageNumber());

                MercuryMessageContext mercuryMessageContext =
                        new MercuryMessageContext(rmsMessageDto, rmApplicationMessage, newMessageContext);
                this.messageBuffer.put(new Long(rmsMessageDto.getMessageNumber()), mercuryMessageContext);
            }

            if (messageContext.getAxisOperation().getMessageExchangePattern().equals(WSDL2Constants.MEP_URI_OUT_IN)){
                // register message receiver call backs with the message ids
               MessageReceiver messageReceiver = messageContext.getAxisOperation().getMessageReceiver();
               if (messageReceiver instanceof CallbackReceiver){
                   CallbackReceiver callbackReceiver = (CallbackReceiver) messageReceiver;
                   String className = rmsMessageDto.getCallBackClassName();
                   try {
                       Class callBackClass = Class.forName(className);
                       Object callBackObject = callBackClass.newInstance();
                       callbackReceiver.addCallback(rmsMessageDto.getAxisMessageID(), (AxisCallback) callBackObject);
                   } catch (ClassNotFoundException e) {
                       log.error("Can not instantiate the callback class with name " + className);
                       throw new PersistenceException("Can not instantiate the callback class with name " + className);
                   } catch (IllegalAccessException e) {
                       log.error("Can not access the callback class with name " + className);
                       throw new PersistenceException("Can not access the callback class with name " + className);
                   } catch (InstantiationException e) {
                       log.error("Can not instantiate the callback class with name " + className);
                       throw new PersistenceException("Can not instantiate the callback class with name " + className);
                   }
               }
            }
        }

    }

    private SOAPEnvelope getSoapEnvelope(String soapEnvelpe)
            throws PersistenceException {
        try {
            XMLStreamReader xmlReader =
                    StAXUtils.createXMLStreamReader(new ByteArrayInputStream(soapEnvelpe.getBytes()));
            StAXBuilder builder = new StAXSOAPModelBuilder(xmlReader);
            SOAPEnvelope soapEnvelope = (SOAPEnvelope) builder.getDocumentElement();
            soapEnvelope.build();
            String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
            if (soapEnvelope.getHeader() == null) {
                SOAPFactory soapFactory = null;
                if (soapNamespace.equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
                    soapFactory = OMAbstractFactory.getSOAP12Factory();
                } else {
                    soapFactory = OMAbstractFactory.getSOAP11Factory();
                }
                soapFactory.createSOAPHeader(soapEnvelope);
            }
            return soapEnvelope;
        } catch (XMLStreamException e) {
            log.error("Problem with the stored message", e);
            throw new PersistenceException("Problem with the stored message", e);
        }
    }

    public synchronized void invalidCreateSequenceResponseReceived(SOAPEnvelope faultEnvelope) throws AxisFault {

        terminateRMSSequence("Invalid Create Sequence Response Received", faultEnvelope, null);

        if (this.errorCallback != null){
            ErrorCallbackWorker errorCallbackWorker = new ErrorCallbackWorker(this.errorCallback,
                    new RMSequenceCreationException("Invalid Create Sequence Response Received"));
            this.axis2Info.getConfigurationContext().getThreadPool().execute(errorCallbackWorker);
        }

    }

    public synchronized void rmsSequenceExpired() throws AxisFault {

        terminateRMSSequence("RMS Sequence has expires before properly terminating the sequence", null, null);

        if (this.errorCallback != null){
            ErrorCallbackWorker errorCallbackWorker = new ErrorCallbackWorker(this.errorCallback,
                    new RMSExpiresException("RMS Sequence has expires before properly terminating the sequence"));
            this.axis2Info.getConfigurationContext().getThreadPool().execute(errorCallbackWorker);
        }

    }

    private void terminateRMSSequence(String reason, SOAPEnvelope faultEnvelope, AxisFault e) throws AxisFault {
        // set the state as terminated
        this.state = STATE_TERMINATE;

        // notify all the waiting threads
        MercuryMessageContext mercuryMessageContext = null;
        MessageContext messageContext = null;
        for (Iterator iter = this.messageBuffer.values().iterator(); iter.hasNext();){
            mercuryMessageContext = (MercuryMessageContext) iter.next();
            messageContext = mercuryMessageContext.getMessageContext();
            if (messageContext.getAxisOperation().getMessageExchangePattern().equals(
                    WSDL2Constants.MEP_URI_OUT_IN)){
                MessageContext inMessageContext = messageContext.getOperationContext().getMessageContext(
                    WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                inMessageContext.setProperty(MercuryConstants.RM_ERROR, reason);
                inMessageContext.setProperty(MercuryConstants.RM_FAULT_ENVElOPE, faultEnvelope);
                inMessageContext.setProperty(MercuryConstants.RM_AXIS_FAULT, e);
                synchronized(inMessageContext){
                    inMessageContext.notify();
                }
            }
        }

        // terminate offeredRMD sequence if it is there
        if (this.offeredRMDSequence != null){
            this.offeredRMDSequence.terminate();
        }
    }

    public synchronized void soapFaultOccured(String reason, AxisFault e) throws AxisFault {
        terminateRMSSequence(reason, null, e);

        if (this.errorCallback != null) {
            ErrorCallbackWorker errorCallbackWorker = new ErrorCallbackWorker(this.errorCallback,
                    new SoapProcessingFaultException(reason));
            this.axis2Info.getConfigurationContext().getThreadPool().execute(errorCallbackWorker);
        }

    }

    public void continueWork() {
        log.debug("continueWork");
        if (rmsSequenceWorker != null) {
            rmsSequenceWorker.wakeUp();
        } else {
            // not an error
            log.debug("NO RMS Sequence Worker ??? ");
        }
    }

    public boolean isTerminated(){
       return this.state == RMSSequence.STATE_TERMINATE;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public EndpointReference getAckToEpr() {
        return ackToEpr;
    }

    public void setAckToEpr(EndpointReference ackToEpr) {
        this.ackToEpr = ackToEpr;
    }

    public synchronized int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public Axis2Info getAxis2Info() {
        return axis2Info;
    }

    public void setAxis2Info(Axis2Info axis2Info) {
        this.axis2Info = axis2Info;
    }

    public String getSequenceOffer() {
        return sequenceOffer;
    }

    public void setSequenceOffer(String sequenceOffer) {
        this.sequenceOffer = sequenceOffer;
    }

    public RMSSequenceDto getPersistanceDto() {
        return persistanceDto;
    }

    public void setPersistanceDto(RMSSequenceDto persistanceDto) {
        this.persistanceDto = persistanceDto;
    }

    public boolean isAnnonymous() {
        return isAnnonymous;
    }

    public void setAnnonymous(boolean annonymous) {
        isAnnonymous = annonymous;
    }

    public RMDSequence getOfferedRMDSequence() {
        return offeredRMDSequence;
    }

    public void setOfferedRMDSequence(RMDSequence offeredRMDSequence) {
        this.offeredRMDSequence = offeredRMDSequence;
    }

    public MercuryErrorCallback getErrorCallback() {
        return errorCallback;
    }

    public void setErrorCallback(MercuryErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    public void setRMSSequenceWorker(RMSSequenceWorker w) {
        rmsSequenceWorker = w;
    }

    public long getRetransmitTime() {
        return retransmitTime;
    }

    public void setRetransmitTime(long retransmitTime) {
        this.retransmitTime = retransmitTime;
    }

    public long getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(long timeoutTime) {
        this.timeoutTime = timeoutTime;
    }

    public long getMaximumRetrasmitCount() {
        return maximumRetrasmitCount;
    }

    public void setMaximumRetrasmitCount(long maximumRetrasmitCount) {
        this.maximumRetrasmitCount = maximumRetrasmitCount;
    }

    public boolean getExponentialBackoff() {
        return exponentialBackoff;
    }

    public void setExponentialBackoff(boolean value) {
        exponentialBackoff = value;
    }

    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    /**
     * calculate retransmitTime basted on exponentialBackup,
     */
    public long calculateRetransmitTime(long retransmissionCount) {
        if(!exponentialBackoff) {
            log.debug("RMS Sequence : calculateRetransmitTime : no exp " + retransmitTime);
            return retransmitTime;
        }
        log.debug("RMS Sequence : calculateRetransmitTime : EXP " + (retransmitTime << retransmissionCount));
        return retransmitTime << retransmissionCount;
    }

    public String getMep() {
        return mep;
    }

    public void setMep(String mep) {
        this.mep = mep;
    }

}
