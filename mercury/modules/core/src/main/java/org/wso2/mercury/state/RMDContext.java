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

import org.wso2.mercury.message.CreateSequenceMessage;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.exception.RMSecurityException;
import org.wso2.mercury.util.RMUtil;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.MercuryParameterHandler;
import org.wso2.mercury.workers.RMDSequenceWorker;
import org.wso2.mercury.workers.InvokerWorker;
import org.wso2.mercury.workers.RMSSequenceWorker;
import org.wso2.mercury.keys.InternalSequenceKey;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.persistence.dto.RMDSequenceDto;
import org.wso2.mercury.persistence.dto.InternalKeyDto;
import org.wso2.mercury.persistence.dto.Axis2InfoDto;
import org.wso2.mercury.persistence.dto.PropertyDto;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.security.RMSecurityManager;
import org.wso2.mercury.security.SecurityToken;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.description.*;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.OMElement;

import java.util.*;

/**
 * this class is used to keep the RMD context
 * objects.
 */
public class RMDContext {

    private static Log log = LogFactory.getLog(RMDContext.class);
    // keep the RMD with the sequnce ID
    private Map sequenceIDRMDSequenceMap;

    private ConfigurationContext configurationContext;

    public RMDContext(ConfigurationContext configurationContext) {
        this.sequenceIDRMDSequenceMap = new HashMap();
        this.configurationContext = configurationContext;
    }

    /**
     * creates an RMD sequence at the RMD side once it gets a create sequence message
     * this sequence can eiter be an annonymous or duplex one. for annonymous we let the message to
     * countine and write the response at the out handler
     * @param messageContext
     */
    public synchronized RMDSequence processCreateSequenceRequest(MessageContext messageContext)
            throws RMMessageBuildingException, AxisFault {
        // first check whether there is a sequence alrady started for this
        // acks to
        CreateSequenceMessage createSequenceMessage =
                CreateSequenceMessage.fromSOAPEnvelope(messageContext.getEnvelope());

        // otherwise this may be a duplicate create seqeunce message
        String sequenceID = RMUtil.getUUID();

        // creates a sequence object for this sequence
        RMDSequence rmdSequence = new RMDSequence(RMDSequence.STATE_00);
        rmdSequence.setAcksTo(new EndpointReference(createSequenceMessage.getAcksToAddress()));
        rmdSequence.getInvokerBuffer().setAcksTo(new EndpointReference(createSequenceMessage.getAcksToAddress()));
        rmdSequence.setSequenceID(sequenceID);
        rmdSequence.setCreateSequenceMessageContext(messageContext);

        Axis2Info axis2Info = getAxis2Info(messageContext,
                createSequenceMessage.getAcksToAddress(),
                createSequenceMessage.getAddressingNamespace());
        rmdSequence.setAxis2Info(axis2Info);
        rmdSequence.setInvokerBufferPersistanceManager();

        // set the timing parameters
        MercuryParameterHandler mercuryParameterHandler = new MercuryParameterHandler(messageContext.getAxisService());
        rmdSequence.setRetransmitTime(mercuryParameterHandler.getRMDSequenceRetransmitTime());
        rmdSequence.setTimeoutTime(mercuryParameterHandler.getRMDSequenceTimeout());
        rmdSequence.getInvokerBuffer().setTimeoutTime(mercuryParameterHandler.getInvokerTimeout());
        rmdSequence.setNotifyThreads(mercuryParameterHandler.getNotifyThreads());

        // set the security token if it is there
        OMElement securityTokenReference = createSequenceMessage.getSecurityTokenReference();
        if (securityTokenReference != null) {
            RMSecurityManager rmSecurityManager
                    = (RMSecurityManager) messageContext.getConfigurationContext().getProperty(
                    MercuryConstants.RM_SECURITY_MANAGER);
            try {
                SecurityToken securityToken = rmSecurityManager.getSecurityToken(securityTokenReference, messageContext);
                rmdSequence.setSecurityToken(securityToken);
            } catch (RMSecurityException e) {
                log.error("Can not get the security Token");
                throw new AxisFault("Can not get the security Token");
            }

        }


        if (messageContext.getConfigurationContext().getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER) != null){
            // i.e RMDSequence must be persisted.
            try {
                rmdSequence.save();
            } catch (PersistenceException e) {
                log.error("Can not save to data base");
            }

        }


        //register this sequence in the Application context
        registerRMDSequenceToSequenceID(sequenceID, rmdSequence);
        String acksToAddress = createSequenceMessage.getAcksToAddress();
        // process the offer ID
        if (createSequenceMessage.getOfferIdentifier() != null) {
            if (messageContext.getAxisOperation().getMessageExchangePattern().equals(WSDL2Constants.MEP_URI_IN_OUT)) {

                // set the self acts to EPR in RMDSequence
                rmdSequence.setSelfAcksToEPR(messageContext.getOptions().getTo().getAddress());

                RMSSequence rmsSequence =
                        new RMSSequence(RMSSequence.STATE_1000,
                                new EndpointReference(createSequenceMessage.getAcksToAddress()));
                // setting the wso2 info
                axis2Info = getAxis2Info(messageContext,
                        createSequenceMessage.getAcksToAddress(),
                        createSequenceMessage.getAddressingNamespace());
                // this can only happen at the server side and there to means this server address
                // so we have to change the to to the RMD address.
                axis2Info.getOptions().setTo(new EndpointReference(createSequenceMessage.getAcksToAddress()));
                rmsSequence.setAxis2Info(axis2Info);
                rmsSequence.setSequenceID(createSequenceMessage.getOfferIdentifier());

                // set the rms sequence times
                rmsSequence.setRetransmitTime(mercuryParameterHandler.getRMSSequenceRetransmitTime());
                rmsSequence.setTimeoutTime(mercuryParameterHandler.getRMSSequenceTimeout());
                rmsSequence.setMaximumRetrasmitCount(mercuryParameterHandler.getRMSMaximumRetransmitCount());

                InternalSequenceKey key = new InternalSequenceKey(createSequenceMessage.getAcksToAddress(), sequenceID);

                // Add this internal key and RMS to persistence if the persitance manager is set.
                if (messageContext.getConfigurationContext().getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER) != null) {
                    PersistenceManager persistenceManager =
                            (PersistenceManager) messageContext.getConfigurationContext().getProperty(
                                    MercuryConstants.RM_PERSISTANCE_MANAGER);
                    InternalKeyDto internalKeyDto = new InternalKeyDto(key.getInternalKey(),
                            key.getEndPointAddress());
                    try {
                        persistenceManager.save(internalKeyDto);
                        rmsSequence.save(internalKeyDto.getId());
                    } catch (PersistenceException e) {
                        log.error("Can not persists objects", e);
                        throw new AxisFault("Can not persists objects", e);
                    }

                }

                // no need to set the acks to epr since no need to send the create sequence message
                // register this sequence in the application context
                RMSContext rmsContext = (RMSContext) this.configurationContext.getProperty(MercuryConstants.RMS_CONTEXT);
                rmsContext.registerRMSSequenceToInternalKey(key, rmsSequence);
                rmsContext.registerRMSSequenceToSequenceID(createSequenceMessage.getOfferIdentifier(),rmsSequence);

                if (acksToAddress.equals(AddressingConstants.Submission.WSA_ANONYMOUS_URL) ||
                        acksToAddress.equals(AddressingConstants.Final.WSA_ANONYMOUS_URL)) {
                    rmsSequence.setAnnonymous(true);
                    rmdSequence.setOfferedRMSSequence(rmsSequence);
                    rmsSequence.setOfferedRMDSequence(rmdSequence);
                    // we don't have to start any threads here since this is in the server side
                } else {
                    rmsSequence.setAnnonymous(false);
                    RMSSequenceWorker rmsSequenceWorker = new RMSSequenceWorker(rmsSequence);
                    rmsSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMSSequenceWorkerSleepTime());
                     // set a sender worker who works on this sequence.
                    this.configurationContext.getThreadPool().execute(rmsSequenceWorker);
                }
            }
        }

        // if the create sequence message has an anonymous acks to address
        // then we can't initiate the threads from here.

        // start the InvokerWorker to pass the message to next level
        InvokerWorker invokerWorker = new InvokerWorker(rmdSequence.getInvokerBuffer());
        invokerWorker.setSleepTime(mercuryParameterHandler.getInvokerSleepTime());
        configurationContext.getThreadPool().execute(invokerWorker);


        if (acksToAddress.equals(AddressingConstants.Submission.WSA_ANONYMOUS_URL) ||
                acksToAddress.equals(AddressingConstants.Final.WSA_ANONYMOUS_URL)) {
            rmdSequence.setAnonymous(true);
            rmdSequence.getInvokerBuffer().setAnonymous(true);
        } else {
            rmdSequence.setAnonymous(false);
            rmdSequence.getInvokerBuffer().setAnonymous(false);
            // start the RMDSequenceWorker to poll the seqence.
            RMDSequenceWorker rmdSequenceWorker = new RMDSequenceWorker(rmdSequence);
            rmdSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMDSequenceWorkerSleepTime());
            configurationContext.getThreadPool().execute(rmdSequenceWorker);
        }
        return rmdSequence;
    }

    /**
     *
     * @param sequenceID
     * @param messageContext
     * @throws PersistenceException
     * @throws AxisFault
     *
     */

    public synchronized void loadRMDSequenceFromPersistanceStorage(String sequenceID,
                                                                   MessageContext messageContext)
            throws PersistenceException, AxisFault {
        PersistenceManager persistenceManager =
                (PersistenceManager) messageContext.getConfigurationContext().getProperty(
                        MercuryConstants.RM_PERSISTANCE_MANAGER);
        RMDSequenceDto rmdSequenceDto = persistenceManager.getRMDSequeceWithSequenceID(sequenceID);
        RMDSequence rmdSequence = new RMDSequence(rmdSequenceDto.getState());
        rmdSequence.setSequenceID(rmdSequenceDto.getSequenceID());
        rmdSequence.setAcksTo(new EndpointReference(rmdSequenceDto.getAcksTo()));
        rmdSequence.setLastMessageNumber(rmdSequenceDto.getLastMessageNumber());
        rmdSequence.setPersistanceDto(rmdSequenceDto);

        //creating the Axis2Info
        //TODO: set this correctly
        Axis2Info axis2Info = getAxis2Info(messageContext,rmdSequenceDto.getAcksTo(), null);
        rmdSequence.setAxis2Info(axis2Info);
        rmdSequence.setInvokerBufferPersistanceManager();
        rmdSequence.loadRMDSequenceDetails(messageContext);

        MercuryParameterHandler mercuryParameterHandler = new MercuryParameterHandler(messageContext.getAxisService());
        rmdSequence.setRetransmitTime(mercuryParameterHandler.getRMDSequenceRetransmitTime());
        rmdSequence.setTimeoutTime(mercuryParameterHandler.getRMDSequenceTimeout());
        rmdSequence.getInvokerBuffer().setTimeoutTime(mercuryParameterHandler.getInvokerTimeout());

        //register this sequence in the Application context
        registerRMDSequenceToSequenceID(rmdSequence.getSequenceID(), rmdSequence);

        // start the RMDSequenceWorker to poll the seqence.
        RMDSequenceWorker rmdSequenceWorker = new RMDSequenceWorker(rmdSequence);
        rmdSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMDSequenceWorkerSleepTime());
        configurationContext.getThreadPool().execute(rmdSequenceWorker);

        // start the InvokerWorker to pass the message to next level
        InvokerWorker invokerWorker = new InvokerWorker(rmdSequence.getInvokerBuffer());
        invokerWorker.setSleepTime(mercuryParameterHandler.getInvokerSleepTime());
        configurationContext.getThreadPool().execute(invokerWorker);


    }

    public synchronized void loadRMDSequenceFromPersistanceStorage(RMDSequenceDto rmdSequenceDto,
                                                                   Axis2InfoDto axis2InfoDto,
                                                                   PersistenceManager persistenceManager,
                                                                   ConfigurationContext configurationContext)
            throws PersistenceException, AxisFault {

        RMDSequence rmdSequence = new RMDSequence(rmdSequenceDto.getState());
        rmdSequence.setSequenceID(rmdSequenceDto.getSequenceID());
        rmdSequence.setAcksTo(new EndpointReference(rmdSequenceDto.getAcksTo()));
        rmdSequence.setLastMessageNumber(rmdSequenceDto.getLastMessageNumber());
        rmdSequence.setPersistanceDto(rmdSequenceDto);


        Axis2Info axis2Info = getAxis2Info(axis2InfoDto, configurationContext, persistenceManager);

        // set the transport out without setting the transport out
        // commons transport sender won't send the message.
        String transportURI = rmdSequenceDto.getAcksTo();
        String transport = transportURI.substring(0, transportURI.indexOf(":"));
        axis2Info.setTransportOut(configurationContext.getAxisConfiguration().getTransportOut(transport));

        rmdSequence.setAxis2Info(axis2Info);
        rmdSequence.setInvokerBufferPersistanceManager();
        rmdSequence.loadRMDSequenceDetails(axis2InfoDto, configurationContext, axis2Info.getServiceContext());

        MercuryParameterHandler mercuryParameterHandler =
                new MercuryParameterHandler(configurationContext.getAxisConfiguration().getService(axis2InfoDto.getServiceName()));
        rmdSequence.setRetransmitTime(mercuryParameterHandler.getRMDSequenceRetransmitTime());
        rmdSequence.setTimeoutTime(mercuryParameterHandler.getRMDSequenceTimeout());
        rmdSequence.getInvokerBuffer().setTimeoutTime(mercuryParameterHandler.getInvokerTimeout());

        //register this sequence in the Application context
        registerRMDSequenceToSequenceID(rmdSequence.getSequenceID(), rmdSequence);

        // start the RMDSequenceWorker to poll the seqence.
        RMDSequenceWorker rmdSequenceWorker = new RMDSequenceWorker(rmdSequence);
        rmdSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMDSequenceWorkerSleepTime());
        configurationContext.getThreadPool().execute(rmdSequenceWorker);

        // start the InvokerWorker to pass the message to next level
        InvokerWorker invokerWorker = new InvokerWorker(rmdSequence.getInvokerBuffer());
        invokerWorker.setSleepTime(mercuryParameterHandler.getInvokerSleepTime());
        configurationContext.getThreadPool().execute(invokerWorker);


    }

    private Axis2Info getAxis2Info(MessageContext messageContext,
                                   String acksToAddress,
                                   String addressingNamespaceURI) throws AxisFault {
        Axis2Info axis2Info = new Axis2Info();
        axis2Info.setServiceContext(messageContext.getServiceContext());
        axis2Info.setOptions(messageContext.getOptions());
        axis2Info.setTransportIn(messageContext.getTransportIn());

        TransportOutDescription transportOut = ClientUtils.inferOutTransport(
                messageContext.getConfigurationContext().getAxisConfiguration(),
                new EndpointReference(acksToAddress), messageContext);

        axis2Info.setTransportOut(transportOut);
        axis2Info.setServerSide(messageContext.isServerSide());
        axis2Info.setSoapNamespaceURI(messageContext.getEnvelope().getNamespace().getNamespaceURI());
        axis2Info.setAddressingNamespaceURI(addressingNamespaceURI);
        axis2Info.setProperties(messageContext.getProperties());
        axis2Info.setCurrentHandlerIndex(messageContext.getCurrentHandlerIndex());
        axis2Info.setCurrentPhaseIndex(messageContext.getCurrentPhaseIndex());
        return axis2Info;
    }

    /**
     * this method is used to create the Axis2Info when a server start up and it
     * gets details only from the persistance storage
     * @param axis2InfoDto
     * @param configurationContext
     * @return
     * @throws AxisFault
     */
    private Axis2Info getAxis2Info(Axis2InfoDto axis2InfoDto,
                                   ConfigurationContext configurationContext,
                                   PersistenceManager persistenceManager) throws AxisFault, PersistenceException {

        Axis2Info axis2Info = new Axis2Info();
        AxisService axisServce =
                configurationContext.getAxisConfiguration().getService(axis2InfoDto.getServiceName());
        AxisServiceGroup axisServiceGroup = axisServce.getAxisServiceGroup();
        ServiceGroupContext serviceGroupContext
                = configurationContext.createServiceGroupContext(axisServiceGroup);
        axis2Info.setServiceContext(serviceGroupContext.getServiceContext(axisServce));
        //TODO: save an set options


        axis2Info.setServerSide(axis2InfoDto.isServerSide());
        axis2Info.setSoapNamespaceURI(axis2InfoDto.getSoapNamespaceURI());
        axis2Info.setAddressingNamespaceURI(axis2InfoDto.getAddressingNamespaceURI());

        List<PropertyDto> properties = persistenceManager.getProperties(axis2InfoDto.getId());
        for (PropertyDto propertyDto : properties){
            axis2Info.getProperties().put(propertyDto.getName(), propertyDto.getValue());
        }

        axis2Info.setCurrentHandlerIndex(axis2InfoDto.getCurrentHanlderIndex());
        axis2Info.setCurrentPhaseIndex(axis2InfoDto.getCurrentPhaseIndex());
        return axis2Info;
    }

    public synchronized RMDSequence createRMDSequenceWithOffer(
            String offerID,
            String acksToAddress,
            Axis2Info axis2Info,
            boolean isAnonymous){
        // creates a sequence object for this sequence
        RMDSequence rmdSequence = new RMDSequence(RMDSequence.STATE_10);
        rmdSequence.setAcksTo(new EndpointReference(acksToAddress));
        rmdSequence.setSequenceID(offerID);
        rmdSequence.setAxis2Info(axis2Info);
        rmdSequence.setAnonymous(isAnonymous);
        // set the invoker buffer 
        rmdSequence.getInvokerBuffer().setAnonymous(isAnonymous);
        rmdSequence.setInvokerBufferPersistanceManager();

        MercuryParameterHandler mercuryParameterHandler = new MercuryParameterHandler(axis2Info.getAxisService());
        rmdSequence.setRetransmitTime(mercuryParameterHandler.getRMDSequenceRetransmitTime());
        rmdSequence.setTimeoutTime(mercuryParameterHandler.getRMDSequenceTimeout());
        rmdSequence.getInvokerBuffer().setTimeoutTime(mercuryParameterHandler.getInvokerTimeout());
        rmdSequence.setNotifyThreads(mercuryParameterHandler.getNotifyThreads());

        registerRMDSequenceToSequenceID(offerID,rmdSequence);

        if (axis2Info.getConfigurationContext().getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER) != null){
            // i.e RMDSequence must be persisted.
            try {
                rmdSequence.save();
            } catch (PersistenceException e) {
                log.error("Can not save to data base");
            }
        }

        if (!isAnonymous) {
            // start the RMDSequenceWorker to poll the seqence.
            // we have to start this only for duplex mode sequences.
            RMDSequenceWorker rmdSequenceWorker = new RMDSequenceWorker(rmdSequence);
            rmdSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMDSequenceWorkerSleepTime());
            configurationContext.getThreadPool().execute(rmdSequenceWorker);
        }

        // start the InvokerWorker to pass the message to next level
        InvokerWorker invokerWorker = new InvokerWorker(rmdSequence.getInvokerBuffer());
        invokerWorker.setSleepTime(mercuryParameterHandler.getInvokerSleepTime());
        configurationContext.getThreadPool().execute(invokerWorker);

        return rmdSequence;
    }

    public synchronized void removeExpiredSequences(){

        List expiredSequences = new ArrayList();

        String sequenceID = null;
        RMDSequence rmdSequence = null;
        for (Iterator iter = this.sequenceIDRMDSequenceMap.keySet().iterator(); iter.hasNext();) {
            sequenceID = (String) iter.next();
            rmdSequence = (RMDSequence) this.sequenceIDRMDSequenceMap.get(sequenceID);
            if (rmdSequence.isTerminated() ||
                ((System.currentTimeMillis() - rmdSequence.getLastAccesedTime()) > rmdSequence.getTimeoutTime())) {
                // this sequence is expired
                expiredSequences.add(sequenceID);
            }
        }

        for (Iterator iter = expiredSequences.iterator();iter.hasNext();){
            sequenceID = (String) iter.next();
            log.info("Removing the sequence " + sequenceID + " from the RMDContext ");
            this.sequenceIDRMDSequenceMap.remove(sequenceID);
        }
    }

    public void registerRMDSequenceToSequenceID(String sequenceID, RMDSequence rmdSequence) {
        this.sequenceIDRMDSequenceMap.put(sequenceID, rmdSequence);
    }

    public synchronized RMDSequence getRMDSeqenceWithSequenceID(String sequenceID) {
        return (RMDSequence) this.sequenceIDRMDSequenceMap.get(sequenceID);
    }

    public void unRegisterRMDSequenceWithSequenceID(String sequenceID) {
        this.sequenceIDRMDSequenceMap.remove(sequenceID);
    }

    public ConfigurationContext getConfigurationContext() {
        return configurationContext;
    }

    public void setConfigurationContext(ConfigurationContext configurationContext) {
        this.configurationContext = configurationContext;
    }

}
