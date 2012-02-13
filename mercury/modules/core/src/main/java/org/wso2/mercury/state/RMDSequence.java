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

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.util.MessageContextBuilder;
import org.apache.axis2.util.CallbackReceiver;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.transport.RequestResponseTransport;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.context.MercuryMessageContext;
import org.wso2.mercury.message.*;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.workers.MessageWorker;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.persistence.dto.*;
import org.wso2.mercury.security.SecurityToken;
import org.wso2.mercury.callback.MercuryTerminateCallback;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

/**
 * This class keeps the sates of the RMD
 * State of the RMD is basically determined by two factors for
 * this model.
 * FMR - first message received.
 * LMR - last message received.
 * these two factors forms possible 4 states. but only
 * 3 can be really exists.
 * <p/>
 * This state machine state is changed by following possible events.
 * AMR(SC) - Application message received and this completes the sequence.
 * This means we have alrady received the last message and there is one
 * missing message to complete the sequence.
 * AMR(SNC) - Application message received and it does not complete the sequence.
 * Here it can be either last message is not received or there may be missing messages.
 * LMR(SC) - Last message received which completes the sequence.
 * LMR(SNC) - Last message received but sequence is not completed.
 */

public class RMDSequence {

    private static Log log = LogFactory.getLog(RMDSequence.class);

    // Possible states for this state machine.
    // This state machine state is depends on FMR,LMR.
    // 0 and 1 represents absent and present of each state.
    public static final int STATE_00 = 0;
    public static final int STATE_10 = 1;
    public static final int STATE_11 = 2;
    public static final int STATE_COMPLETED = 3;
    public static final int STATE_TERMINATED = 4;

    private long retransmitTime;
    private long timeoutTime;
    private boolean notifyThreads;

    private String sequenceID;
    private int state;

    // acksTo is sent by the RMS when creating the sequence. When sending
    // the responses we have to send them to endpoint acksTo.
    private EndpointReference acksTo;

    private long lastCreateSequceResponseMessageSentTime = 0;
    private long lastAcknowledgmentSentTime = 0;
    private long lastAccesedTime = 0;

    // This is the initial createSequenceMessage context received.
    // This is used when sending any createSequenceResponse.
    // This should be theoratically correct.
    // TODO: Think whether we need to update this upon receing new create seqence requests.
    private MessageContext createSequenceMessageContext;

    private Axis2Info axis2Info;

    private Set receivedMessageNumbers;
    private long lastMessageNumber = 0;

    private InvokerBuffer invokerBuffer;

    private RMDSequenceDto persistanceDto;

    private boolean isAnonymous;

    // keeps whether the RMS has offered a sequence or not.
    // this is usefull when sending create sequence Response where we have to send
    // and Accept header.
    private String selfAcksToEPR;

    private RMSSequence offeredRMSSequence;

    private SecurityToken securityToken;

    private MercuryTerminateCallback terminateCallback;

    public RMDSequence(int state) {
        this.state = state;
        this.receivedMessageNumbers = new HashSet();
        this.lastAccesedTime = System.currentTimeMillis();
        // creates a invoker buffer for this RMD
        this.invokerBuffer = new InvokerBuffer(InvokerBuffer.STATE_000);
    }

    public void setInvokerBufferPersistanceManager(){
        this.invokerBuffer.setPersistanceManager(getPersistanceManager());
    }

    public synchronized void doActions() throws AxisFault, RMMessageBuildingException {
        switch (state) {
            case STATE_00 : {
                //i.e Still first message or last message is not received.
                // The only possible thing is to send the create sequene response.
                if ((lastCreateSequceResponseMessageSentTime == 0) ||
                        ((System.currentTimeMillis() - lastCreateSequceResponseMessageSentTime) > retransmitTime)) {
                    // send the create sequence response message
                    sendCreateSequenceResponseMessage();
                    lastCreateSequceResponseMessageSentTime = System.currentTimeMillis();
                }
                break;
            }
            case STATE_10 : {
                // i.e Received the first message, but not completed.
                // The only thing we can do here is to send an acknowledgment.
                retransmitSequenceAcknowledgment();
                break;
            }
            case STATE_11 : {
                // i.e Received the first message, but not completed.
                // The only thing we can do here is to send an acknowledgment.
                retransmitSequenceAcknowledgment();
                break;
            }

            case STATE_COMPLETED : {
                // i.e Received all the required messages but still terminate
                // message is not received. So RMS may not have got acknowledgments for
                // all messages.
                retransmitSequenceAcknowledgment();
                break;
            }
        }
    }

    public void sendCreateSequenceResponseMessage() throws AxisFault, RMMessageBuildingException {

        // TODO: set the correct mercury and soap namesapce
        if (this.createSequenceMessageContext != null) {
            // the create Sequence Message Context is not null only for the sequences started with the
            // create sequence reqest
            // note that the persistence storage sequences start straight away with the
            // message received state. therefore the createSequence responses don't have to send

            CreateSequenceResponseMessage createSequenceResponseMessage = new CreateSequenceResponseMessage();
            createSequenceResponseMessage.setIdentifier(sequenceID);
            createSequenceResponseMessage.setSoapNamesapce(
                    this.createSequenceMessageContext.getEnvelope().getNamespace().getNamespaceURI());

            // Set the accept header if a sequence has occured
            if (this.selfAcksToEPR != null){
                Accept accept = new Accept();
                accept.setAcceptERP(this.selfAcksToEPR);
                accept.setAddressingNamespace(this.axis2Info.getAddressingNamespaceURI());
                createSequenceResponseMessage.setAccept(accept);
            }


            MessageContext messageContext =
                    MessageContextBuilder.createOutMessageContext(this.createSequenceMessageContext);
            messageContext.setProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);
            messageContext.setEnvelope(createSequenceResponseMessage.toSOAPEnvelope());

            messageContext.setTo(this.acksTo);
            messageContext.getOptions().setAction(MercuryConstants.CREATE_SEQUENCE_RESPONSE_ACTION);

            //use message worker to send this message.
            sendMessage(messageContext, false);
        }
    }

    private void sendMessage(MessageContext messageContext, boolean isResume) {
        MessageWorker messageWorker = new MessageWorker(messageContext, isResume);
        // for annonymous sequence messages we have to use the same thread
        if (isAnonymous){
            messageWorker.run();
        } else {
            messageContext.getConfigurationContext().getThreadPool().execute(messageWorker);
        }

    }

    /**
     * this message should not be a last message
     *
     * @param messageNumber
     */
    public synchronized boolean applicationMessageReceived(long messageNumber,
                                                        MercuryMessageContext mercuryMessageContext)
            throws PersistenceException {
        // if this message is the last message then set it.
        this.lastAccesedTime = System.currentTimeMillis();
        int currentState = this.state;
        boolean newMessageReceived = false;
        if (!this.receivedMessageNumbers.contains(new Long(messageNumber))){
            this.receivedMessageNumbers.add(new Long(messageNumber));
            newMessageReceived = true;
        }

        switch (state) {
            case STATE_00 : {
                state = STATE_10;
                if (this.persistanceDto != null){
                    this.persistanceDto.setState(state);
                }
                break;
            }
            case STATE_10 : {
                // nothing to do
                break;
            }
            case STATE_11 : {
                if (isAllMessagesReceived()) {
                    state = STATE_COMPLETED;
                    if (this.persistanceDto != null) {
                        this.persistanceDto.setState(state);
                    }
                }
                break;
            }
        }
        // pass this message to invoker buffer
        try {
            this.invokerBuffer.applicatinMessageReceived(messageNumber, mercuryMessageContext, this.persistanceDto);
            if (notifyThreads) {
                // notify the invokerworker about the received message
                // this is a pulus for even inonly messages since it does not
                // have to wait until invoker worker starts up
                this.invokerBuffer.continueWork();
            }

        } catch (PersistenceException e) {
            this.state = currentState;
            this.persistanceDto.setState(currentState);
            if (newMessageReceived){
                this.receivedMessageNumbers.remove(new Long(messageNumber));
            }
            log.error("Can not save the new message to persistence", e);
            throw new PersistenceException("Can not save the new message to persistence", e);
        }
        return newMessageReceived;
    }

    public synchronized boolean lastMessageReceived(long messageNumber,
                                                    MercuryMessageContext mercuryMessageContext)
            throws PersistenceException {
        // if this message is the last message then set it.
        this.lastAccesedTime = System.currentTimeMillis();

        // here we have to keep track of changes to roll back them if we could
        // not persists the state.

        boolean isLastMessageSet = false;
        if (this.lastMessageNumber > 0){
            isLastMessageSet = true;
        }
        this.lastMessageNumber = messageNumber;
        if (this.persistanceDto != null){
            this.persistanceDto.setLastMessageNumber(this.lastMessageNumber);
        }
        int currentState = this.state;
        boolean newMessageReceived = false;
        if (!this.receivedMessageNumbers.contains(new Long(messageNumber))){
            newMessageReceived = true;
            this.receivedMessageNumbers.add(new Long(messageNumber));
        }

        switch (state) {
            case STATE_00 : {
                if (isAllMessagesReceived()) {
                    state = STATE_COMPLETED;
                } else {
                    state = STATE_11;
                }
                if(this.persistanceDto != null){
                    this.persistanceDto.setState(state);
                }
                break;
            }
            case STATE_10 : {
                if (isAllMessagesReceived()) {
                    state = STATE_COMPLETED;
                } else {
                    state = STATE_11;
                }
                if(this.persistanceDto != null){
                    this.persistanceDto.setState(state);
                }
                break;
            }
            case STATE_11 : {
                // nothing to do
                break;
            }
        }

        // pass this message to invoker buffer.
        try {
            this.invokerBuffer.lastMessageReceived(messageNumber, mercuryMessageContext, this.persistanceDto);
            if (notifyThreads) {
                // notify the invokerworker about the received message
                // this is a pulus for even inonly messages since it does not
                // have to wait until invoker worker starts up
                this.invokerBuffer.continueWork();
            }

        } catch (PersistenceException e) {
            this.state = currentState;
            this.persistanceDto.setState(this.state);
            if (!isLastMessageSet){
               this.lastMessageNumber = 0;
               this.persistanceDto.setLastMessageNumber(0);
            }
            if (newMessageReceived){
               this.receivedMessageNumbers.remove(new Long(messageNumber));
            }
            log.error("can not save the receced last message", e);
            throw new PersistenceException("can not save the receced last message", e);
        }
        return newMessageReceived;
    }


    private boolean isAllMessagesReceived() {
        boolean isComplete = false;
        if (lastMessageNumber > 0) {
            isComplete = (this.receivedMessageNumbers.size() == lastMessageNumber);
        }
        return isComplete;
    }

    private void retransmitSequenceAcknowledgment()
            throws AxisFault, RMMessageBuildingException {
        if ((lastAcknowledgmentSentTime == 0) ||
                ((System.currentTimeMillis() - lastAcknowledgmentSentTime) > retransmitTime)) {
            sendSequenceAcknowledgementMessage(null);
            lastAcknowledgmentSentTime = System.currentTimeMillis();
        }
    }

    public void sendSequenceAcknowledgementMessage(MessageContext inboundMessageContext)
            throws AxisFault, RMMessageBuildingException {

        // there is no point in sending an empty sequece acknowledgment message.
        if (this.receivedMessageNumbers.size() > 0) {
            SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment(sequenceID);
            sequenceAcknowledgment.populateAcknowledgmentRanges(this.receivedMessageNumbers);
            sequenceAcknowledgment.setSoapNamesapce(this.getAxis2Info().getSoapNamespaceURI());

            SequenceAcknowledgmentMessage sequenceAcknowledgmentMessage =
                    new SequenceAcknowledgmentMessage(sequenceAcknowledgment);
            sequenceAcknowledgmentMessage.setSoapNamesapce(this.getAxis2Info().getSoapNamespaceURI());

            MessageContext messageContext =
                    getNewMessageContext(sequenceAcknowledgmentMessage, inboundMessageContext);

            messageContext.getOptions().setProperty(AddressingConstants.WS_ADDRESSING_VERSION,
                    this.axis2Info.getAddressingNamespaceURI());
            messageContext.setReplyTo(new EndpointReference(AddressingConstants.Final.WSA_NONE_URI));
            messageContext.getOptions().setAction(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT_ACTION);
            AxisService axisServce = this.axis2Info.getAxisService();
            AxisOperation inOnlyOperation =
                    axisServce.getOperationByAction(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT_ACTION);

            OperationContext operationContext =
                    this.axis2Info.getServiceContext().createOperationContext(inOnlyOperation);
            inOnlyOperation.registerOperationContext(messageContext, operationContext);

            messageContext.setAxisMessage(inOnlyOperation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));
            // we have to use the same message formatter type
            messageContext.setProperty(Constants.Configuration.MESSAGE_TYPE,
                    this.axis2Info.getProperty(Constants.Configuration.MESSAGE_TYPE));
            //sending the message
            sendMessage(messageContext, false);
        }
    }

    public synchronized SequenceAcknowledgment getSequenceAcknowledgment(){
        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment(this.sequenceID);
        sequenceAcknowledgment.populateAcknowledgmentRanges(this.receivedMessageNumbers);
        sequenceAcknowledgment.setSoapNamesapce(this.axis2Info.getSoapNamespaceURI());
        return sequenceAcknowledgment;
    }

    private MessageContext getNewMessageContext(
            SequenceAcknowledgmentMessage sequenceAcknowledgmentMessage,
            MessageContext inboundMessageContext) throws AxisFault, RMMessageBuildingException {
        MessageContext messageContext = new MessageContext();
        messageContext.setServiceContext(this.axis2Info.getServiceContext());
        messageContext.setEnvelope(sequenceAcknowledgmentMessage.toSOAPEnvelope());
        messageContext.setMessageID(UUIDGenerator.getUUID());

        // set the security details
        if (this.createSequenceMessageContext != null){
            messageContext.setProperty("RECV_RESULTS", this.createSequenceMessageContext.getProperty("RECV_RESULTS"));
        }

        // setting the options
        messageContext.setProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);
        if (inboundMessageContext == null) {
            // i.e. inbound Message context means this is a duplex sequence.
            messageContext.setTransportOut(this.axis2Info.getTransportOut());
            messageContext.getOptions().setTo(this.acksTo);
        } else {
            // i.e we have to send the message using the backchanel of the incomming message
            messageContext.setServerSide(true);
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

    public synchronized void terminateSequenceMessageReceived() throws PersistenceException {
        this.lastAccesedTime = System.currentTimeMillis();
        int currentState = this.state;
        switch (state) {
            case STATE_00 : {
                state = STATE_TERMINATED;
                if (this.persistanceDto != null){
                    this.persistanceDto.setState(this.state);
                }
                break;
            }
            case STATE_10 : {
                state = STATE_TERMINATED;
                if (this.persistanceDto != null){
                    this.persistanceDto.setState(this.state);
                }
                break;
            }
            case STATE_11 : {
                state = STATE_TERMINATED;
                if (this.persistanceDto != null){
                    this.persistanceDto.setState(this.state);
                }
                break;
            }
            case STATE_COMPLETED : {
                state = STATE_TERMINATED;
                if (this.persistanceDto != null){
                    this.persistanceDto.setState(this.state);
                }
                break;
            }
        }
        try {
            if (this.persistanceDto != null){
                this.persistanceDto.setEndTime(System.currentTimeMillis());
            }
            this.invokerBuffer.terminateMessageReceived(this.persistanceDto);

            // this marks the end of the sequence
            // for out in rm sequences this means every thing is fished and can inform the client.
            if (this.terminateCallback != null){
                this.terminateCallback.onComplete();
            }

        } catch (PersistenceException e) {
            this.state = currentState;
            this.persistanceDto.setState(currentState);
            log.error("Can not update the persistence state", e);
            throw new PersistenceException("Can not update the persistence state", e);
        }
    }

    public void save() throws PersistenceException {
        PersistenceManager persistenceManager = getPersistanceManager();
        if (persistenceManager != null){
            this.persistanceDto = new RMDSequenceDto();
            this.persistanceDto.setSequenceID(this.sequenceID);
            this.persistanceDto.setState(this.state);
            this.persistanceDto.setAcksTo(this.acksTo.getAddress());
            this.persistanceDto.setLastMessageNumber(this.lastMessageNumber);
            this.persistanceDto.setStartTime(System.currentTimeMillis());

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
            this.invokerBuffer.save(this.persistanceDto, axis2InfoDto);
        }

    }

    public void loadRMDSequenceDetails(MessageContext messageContext)
            throws PersistenceException, AxisFault {

        // load the SequenceReceivedNumber details
        PersistenceManager persistenceManager = getPersistanceManager();
        List sequenceReceivedNumbers =
                persistenceManager.getSequenceReceivedNumbersWithRMDSequenceID(this.persistanceDto.getId());
        SequenceReceivedNumberDto sequenceReceivedNumberDto;
        for (Iterator iter = sequenceReceivedNumbers.iterator(); iter.hasNext();) {
            sequenceReceivedNumberDto = (SequenceReceivedNumberDto) iter.next();
            this.receivedMessageNumbers.add(new Long(sequenceReceivedNumberDto.getNumber()));
            // remove any call back handlers
            MessageReceiver messageReceiver = messageContext.getAxisOperation().getMessageReceiver();
            if (messageReceiver instanceof CallbackReceiver){
                CallbackReceiver callbackReceiver = (CallbackReceiver) messageReceiver;
                callbackReceiver.getCallbackStore().remove(sequenceReceivedNumberDto.getRelatesToMessageID());
            }
        }

        InvokerBufferDto invokerBufferDto =
                persistenceManager.getInvokerBufferWithRMDSequenceID(this.persistanceDto.getId());
        this.invokerBuffer.setState(invokerBufferDto.getState());
        this.invokerBuffer.setLastMessageNumber(invokerBufferDto.getLastMessage());
        this.invokerBuffer.setLastMessageSendToApplication(invokerBufferDto.getLastMessageToApplication());
        this.invokerBuffer.setPersistanceDto(invokerBufferDto);

        //load the invokerBuffer message details
        this.invokerBuffer.loadInvokerBufferDetails(messageContext);

    }

    /**
     * this method is called when loading the RMD sequences from the
     * configuration context
     * @throws PersistenceException
     * @throws AxisFault
     */
    public void loadRMDSequenceDetails(Axis2InfoDto axis2InfoDto,
                                       ConfigurationContext configurationContext,
                                       ServiceContext serviceContext) throws PersistenceException, AxisFault {

        // load the SequenceReceivedNumber details
        PersistenceManager persistenceManager = getPersistanceManager();
        List sequenceReceivedNumbers =
                persistenceManager.getSequenceReceivedNumbersWithRMDSequenceID(this.persistanceDto.getId());
        SequenceReceivedNumberDto sequenceReceivedNumberDto;
        for (Iterator iter = sequenceReceivedNumbers.iterator(); iter.hasNext();) {
            sequenceReceivedNumberDto = (SequenceReceivedNumberDto) iter.next();
            this.receivedMessageNumbers.add(new Long(sequenceReceivedNumberDto.getNumber()));
        }

        InvokerBufferDto invokerBufferDto =
                persistenceManager.getInvokerBufferWithRMDSequenceID(this.persistanceDto.getId());
        this.invokerBuffer.setState(invokerBufferDto.getState());
        this.invokerBuffer.setLastMessageNumber(invokerBufferDto.getLastMessage());
        this.invokerBuffer.setLastMessageSendToApplication(invokerBufferDto.getLastMessageToApplication());
        this.invokerBuffer.setPersistanceDto(invokerBufferDto);

        //load the invokerBuffer message details
        this.invokerBuffer.loadInvokerBufferDetails(axis2InfoDto, configurationContext, serviceContext);

    }

    private PersistenceManager getPersistanceManager(){
        PersistenceManager persistenceManager = null;
        if (this.axis2Info.getConfigurationContext().getProperty(
                MercuryConstants.RM_PERSISTANCE_MANAGER) != null){
            persistenceManager = (PersistenceManager)
                    this.axis2Info.getConfigurationContext().getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER);
        }
        return persistenceManager;
    }

    /**
     * this method is called for an unexpected sequence termination
     */
    public synchronized void terminate(){
        this.state = STATE_TERMINATED;
        this.invokerBuffer.terminate();
    }

    public synchronized boolean isMessageReceived(long messageNumber){
        return this.receivedMessageNumbers.contains(new Long(messageNumber));
    }

    public synchronized int getState() {
        return state;
    }

    public boolean isTerminated(){
        return this.state == RMDSequence.STATE_TERMINATED;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLastAccesedTime() {
        return lastAccesedTime;
    }

    public void setLastAccesedTime(long lastAccesedTime) {
        this.lastAccesedTime = lastAccesedTime;
    }

    public EndpointReference getAcksTo() {
        return acksTo;
    }

    public void setAcksTo(EndpointReference acksTo) {
        this.acksTo = acksTo;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public MessageContext getCreateSequenceMessageContext() {
        return createSequenceMessageContext;
    }

    public void setCreateSequenceMessageContext(MessageContext createSequenceMessageContext) {
        this.createSequenceMessageContext = createSequenceMessageContext;
    }

    public InvokerBuffer getInvokerBuffer() {
        return invokerBuffer;
    }

    public void setInvokerBuffer(InvokerBuffer invokerBuffer) {
        this.invokerBuffer = invokerBuffer;
    }

    public Axis2Info getAxis2Info() {
        return axis2Info;
    }

    public void setAxis2Info(Axis2Info axis2Info) {
        this.axis2Info = axis2Info;
    }

    public long getLastMessageNumber() {
        return lastMessageNumber;
    }

    public void setLastMessageNumber(long lastMessageNumber) {
        this.lastMessageNumber = lastMessageNumber;
    }

    public RMDSequenceDto getPersistanceDto() {
        return persistanceDto;
    }

    public void setPersistanceDto(RMDSequenceDto persistanceDto) {
        this.persistanceDto = persistanceDto;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public RMSSequence getOfferedRMSSequence() {
        return offeredRMSSequence;
    }

    public void setOfferedRMSSequence(RMSSequence offeredRMSSequence) {
        this.offeredRMSSequence = offeredRMSSequence;
    }

    public String getSelfAcksToEPR() {
        return selfAcksToEPR;
    }

    public void setSelfAcksToEPR(String selfAcksToEPR) {
        this.selfAcksToEPR = selfAcksToEPR;
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

    public boolean getNotifyThreads() {
        return notifyThreads;
    }

    public void setNotifyThreads(boolean notifyThreads) {
        this.notifyThreads = notifyThreads;
    }

    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    public MercuryTerminateCallback getTerminateCallback() {
        return terminateCallback;
    }

    public void setTerminateCallback(MercuryTerminateCallback terminateCallback) {
        this.terminateCallback = terminateCallback;
    }

}
