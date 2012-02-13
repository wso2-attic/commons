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

import org.wso2.mercury.keys.InternalSequenceKey;
import org.wso2.mercury.persistence.dto.InternalKeyDto;
import org.wso2.mercury.persistence.dto.RMSSequenceDto;
import org.wso2.mercury.persistence.dto.Axis2InfoDto;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.MercuryParameterHandler;
import org.wso2.mercury.util.RMDispatchInfo;
import org.wso2.mercury.workers.RMSSequenceWorker;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.ClientUtils;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * this class is used to keep sequence objects with different
 * ids.
 */

public class RMSContext {

    private static Log log = LogFactory.getLog(RMDContext.class);
    // used to keep the user sequence ids and squence map.
    // this is usfull in getting the sequence for a pirticular
    // key epr combination.
    private Map iSKRMSSequenceMap;
    // this is used to register sequences with the message ID when
    // sending the create sequence message.
    private Map messageIDRMSSequenceMap;

    // used to keep the sequence ID to sequence objects map
    // to keep the sequence objects at sequence active time.
    private Map sequenceIDRMSSequenceMap;

    private ConfigurationContext configurationContext;

    public RMSContext(ConfigurationContext configurationContext) {
        this.iSKRMSSequenceMap = new HashMap();
        this.messageIDRMSSequenceMap = new HashMap();
        this.sequenceIDRMSSequenceMap = new HashMap();
        this.configurationContext = configurationContext;
    }

    /**
     * there can be two create sequence responses comes symultaniously.
     * so we have to synchronise them.
     * if they have same message id then first one will get the RMSSequenceDto for that
     * message id, remove it and add it againg using the sequenceID.
     * then second one get a null and we think it is a duplicate one.
     * if they have different message IDs then only first one will register the
     * RMSSequenceDto.
     *
     * @param messageID
     * @param sequenceID
     * @return rms sequence
     */
    public synchronized RMSSequence getRMSSequence(String messageID, String sequenceID) {
        RMSSequence rmsSequence = getRMSSequenceWithMessageID(messageID);
        if (rmsSequence != null) {
            // i.e this is the first create sequence response message.
            unRegisterRMSSequenceWithMessageID(messageID);
            if (!isContainsRMSSequence(rmsSequence)) {
                registerRMSSequenceToSequenceID(sequenceID, rmsSequence);
                rmsSequence.setSequenceID(sequenceID);
            }
        }
        return rmsSequence;
    }

    public synchronized void resumeSequence(InternalSequenceKey key, MessageContext msgContext)
            throws PersistenceException, AxisFault {
        PersistenceManager persistenceManager = getPersistanceManager();
        if (persistenceManager != null) {
            InternalKeyDto internalKeyDto = getInternalKey(key, persistenceManager);
            List rmsSequeces = persistenceManager.getRMSSquenceWithInternalKey(internalKeyDto.getId());
            if (rmsSequeces.size() != 1) {
                throw new PersistenceException("There are either zero or more than one RMS Sequences" +
                        " for internak key with id " + internalKeyDto.getId());
            } else {
                RMSSequenceDto rmsSequenceDto = (RMSSequenceDto) rmsSequeces.get(0);
                RMSSequence rmsSequence = new RMSSequence(rmsSequenceDto.getState(),
                        new EndpointReference(rmsSequenceDto.getEndPointAddress()));

                // if the orginal message mep is in-only then transport in may not have set
                // but since RM needs two way communication we have to set that if original mesage context
                // has not been set
                TransportOutDescription transportOut = msgContext.getTransportOut();
                TransportInDescription transportIn = msgContext.getTransportIn();
                if (transportIn == null) {
                    transportIn = msgContext.getOptions().getTransportIn();
                }

                if (transportIn == null) {
                    transportIn = ClientUtils.inferInTransport(
                            msgContext.getConfigurationContext().getAxisConfiguration(),
                            msgContext.getOptions(),
                            msgContext);
                }

                Axis2InfoDto axis2InfoDto = getPersistanceManager().getAxis2InfoID(rmsSequenceDto.getAxis2InfoID());
                //TODO: think about how to persists options and all these things.
                Axis2Info axis2Info = new Axis2Info();
                axis2Info.setServiceContext(msgContext.getServiceContext());
                axis2Info.setOptions(msgContext.getOptions());
                axis2Info.setTransportIn(transportIn);
                axis2Info.setTransportOut(transportOut);
                axis2Info.setServerSide(axis2InfoDto.isServerSide());
                axis2Info.setSoapNamespaceURI(axis2InfoDto.getSoapNamespaceURI());
                axis2Info.setProperties(msgContext.getProperties());
                rmsSequence.setAxis2Info(axis2Info);
                rmsSequence.populatePersistnaceData(msgContext,rmsSequenceDto);

                // set the sequence times
                MercuryParameterHandler mercuryParameterHandler =
                        new MercuryParameterHandler(msgContext.getAxisService());
                rmsSequence.setRetransmitTime(mercuryParameterHandler.getRMSSequenceRetransmitTime());
                rmsSequence.setTimeoutTime(mercuryParameterHandler.getRMSSequenceTimeout());
                rmsSequence.setMaximumRetrasmitCount(mercuryParameterHandler.getRMSMaximumRetransmitCount());
                rmsSequence.setExponentialBackoff(mercuryParameterHandler.getRMSExponentialBackoff());

                // registering this with the context
                registerRMSSequenceToInternalKey(key,rmsSequence);
                if (rmsSequence.getSequenceID() != null){
                    registerRMSSequenceToSequenceID(rmsSequence.getSequenceID(),rmsSequence);
                }

                // if this sequence is an in out sequence we have to set a sequence ID dispatcher
                // for handling incomming messages.
                if (msgContext.getAxisOperation().getMessageExchangePattern().equals(WSDL2Constants.MEP_URI_OUT_IN)) {
                    // register the sequence ID distpather
                    RMDispatchInfo rmDispatchInfo =
                            (RMDispatchInfo) msgContext.getConfigurationContext().getProperty(
                                    MercuryConstants.RM_DISPATCH_INFO);

                    rmDispatchInfo.addMapping(rmsSequenceDto.getSequenceOffer(), msgContext.getAxisOperation());

                    // extract the RMD and starting it
                    RMDContext rmdContext =
                            (RMDContext) msgContext.getConfigurationContext().getProperty(MercuryConstants.RMD_CONTEXT);
                    rmdContext.loadRMDSequenceFromPersistanceStorage(rmsSequenceDto.getSequenceOffer(), msgContext);

                }
                // start the rms sender
                RMSSequenceWorker rmsSequenceWorker = new RMSSequenceWorker(rmsSequence);
                rmsSequenceWorker.setSequenceWorkerSleepTime(mercuryParameterHandler.getRMSSequenceWorkerSleepTime());
                this.configurationContext.getThreadPool().execute(rmsSequenceWorker);
            }
        }
    }

    private InternalKeyDto getInternalKey(InternalSequenceKey key, PersistenceManager persistenceManager)
            throws PersistenceException {
        // there can be may internak keys for this combination
        // take the lates one
        List internalKeys = persistenceManager.getInternalKey(key.getInternalKey(),key.getEndPointAddress());
        InternalKeyDto internalKeyDto = null;
        // this is a dummy key to used in the algoritum
        InternalKeyDto maxInternalKey = new InternalKeyDto();
        maxInternalKey.setId(0);
        if (internalKeys.size() == 0) {
            throw new PersistenceException("There are no internal keys for Key " + key.getInternalKey() +
                    " toAddress " + key.getEndPointAddress());
        } else {
            for (Iterator iter = internalKeys.iterator(); iter.hasNext();) {
                internalKeyDto = (InternalKeyDto) iter.next();
                if (maxInternalKey.getId() < internalKeyDto.getId()) {
                      maxInternalKey = internalKeyDto;
                }
            }
        }
       return maxInternalKey;
    }

    private PersistenceManager getPersistanceManager(){
        PersistenceManager persistenceManager = null;
        if (this.configurationContext.getProperty(
                MercuryConstants.RM_PERSISTANCE_MANAGER) != null){
            persistenceManager = (PersistenceManager)
                    this.configurationContext.getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER);
        }
        return persistenceManager;
    }

    public synchronized void removeExpiredSequences(){

        removeRMSSequenceFromMap(this.iSKRMSSequenceMap);
        removeRMSSequenceFromMap(this.messageIDRMSSequenceMap);
        removeRMSSequenceFromMap(this.sequenceIDRMSSequenceMap);
    }

    private void removeRMSSequenceFromMap(Map map){

        List expiredSequences = new ArrayList();

        Object key = null;
        RMSSequence rmsSequence = null;
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            key = iter.next();
            rmsSequence = (RMSSequence) map.get(key);
            if (rmsSequence.isTerminated() ||
                ((System.currentTimeMillis() - rmsSequence.getLastAccessedTime()) > rmsSequence.getTimeoutTime())){
                expiredSequences.add(key);
            }
        }

        for (Iterator iter = expiredSequences.iterator();iter.hasNext();){
            key = iter.next();
            log.info("Removing the sequence with key" + key.toString() + " from the RMSContext ");
            map.remove(key);
        }

    }

    public synchronized void registerRMSSequenceToInternalKey(InternalSequenceKey key, RMSSequence rmsSequence) {
        this.iSKRMSSequenceMap.put(key, rmsSequence);
    }

    public synchronized void registerRMSSequenceToMessageID(String messageID, RMSSequence rmsSequence) {
        this.messageIDRMSSequenceMap.put(messageID, rmsSequence);
    }

    public void registerRMSSequenceToSequenceID(String sequenceID, RMSSequence rmsSequence) {
        this.sequenceIDRMSSequenceMap.put(sequenceID, rmsSequence);
    }

    public synchronized RMSSequence getRMSSeqenceWithInternalKey(InternalSequenceKey key) {
        return (RMSSequence) this.iSKRMSSequenceMap.get(key);
    }

    public RMSSequence getRMSSequenceWithMessageID(String messageID) {
        return (RMSSequence) this.messageIDRMSSequenceMap.get(messageID);
    }

    public synchronized RMSSequence getRMSSequenceWithSequenceID(String sequenceID) {
        return (RMSSequence) this.sequenceIDRMSSequenceMap.get(sequenceID);
    }

    public void unRegisterRMSSequenceWithInternalKey(InternalSequenceKey key) {
        this.iSKRMSSequenceMap.remove(key);
    }

    public void unRegisterRMSSequenceWithMessageID(String messageID) {
        this.messageIDRMSSequenceMap.remove(messageID);
    }

    public synchronized void unRegisterRMSSequenceWithSequenceID(String sequenceID) {
        this.sequenceIDRMSSequenceMap.remove(sequenceID);
    }

    /**
     * this checks whether we have already started a this sequence
     *
     * @param rmsSequence
     * @return whether sequence is there or not.
     */
    public boolean isContainsRMSSequence(RMSSequence rmsSequence) {
        return this.sequenceIDRMSSequenceMap.values().contains(rmsSequence);
    }

    public Map getiSKRMSSequenceMap() {
        return iSKRMSSequenceMap;
    }

    public void setiSKRMSSequenceMap(Map iSKRMSSequenceMap) {
        this.iSKRMSSequenceMap = iSKRMSSequenceMap;
    }

    public Map getMessageIDRMSSequenceMap() {
        return messageIDRMSSequenceMap;
    }

    public void setMessageIDRMSSequenceMap(Map messageIDRMSSequenceMap) {
        this.messageIDRMSSequenceMap = messageIDRMSSequenceMap;
    }

    public Map getSequenceIDRMSSequenceMap() {
        return sequenceIDRMSSequenceMap;
    }

    public void setSequenceIDRMSSequenceMap(Map sequenceIDRMSSequenceMap) {
        this.sequenceIDRMSSequenceMap = sequenceIDRMSSequenceMap;
    }

    public ConfigurationContext getConfigurationContext() {
        return configurationContext;
    }

    public void setConfigurationContext(ConfigurationContext configurationContext) {
        this.configurationContext = configurationContext;
    }

}
