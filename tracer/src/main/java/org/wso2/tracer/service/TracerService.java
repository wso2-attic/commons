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
package org.wso2.tracer.service;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.util.Loader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.tracer.TracerConstants;
import org.wso2.tracer.module.TracePersister;
import org.wso2.utils.CircularBuffer;

import java.util.ArrayList;
import java.util.Collections;
/*
 * 
 */

public class TracerService {

    private static Log log = LogFactory.getLog(TracerService.class);

    public TracerServiceInfo getMessages(int numberOfMessages, String filter) throws AxisFault {

        ConfigurationContext configurationContext =
                MessageContext.getCurrentMessageContext().getConfigurationContext();
        AxisConfiguration axisConfiguration = configurationContext.getAxisConfiguration();
        CircularBuffer msgSeqBuff =
                (CircularBuffer) configurationContext.getProperty(TracerConstants.MSG_SEQ_BUFFER);
        Object[] messageObjs;
        TracerServiceInfo tracerServiceInfo = new TracerServiceInfo();
        AxisModule axisModule = axisConfiguration.getModule(TracerConstants.WSO2_TRACER);

        if (axisModule == null) {
            throw new AxisFault(TracerConstants.WSO2_TRACER + " module is not available");
        }
        Parameter tracePersisterParam =
                MessageContext.getCurrentMessageContext()
                        .getParameter(TracerConstants.TRACE_PERSISTER_IMPL);
        TracePersister tracePersister = getTracePersister(tracePersisterParam);
        tracerServiceInfo.setTracePersister(tracePersister.getClass().getName());
        if (tracePersister.isTracingEnabled()) {
            if (!axisConfiguration.isEngaged(axisModule)) {
                axisConfiguration.engageModule(axisModule);
            }
            tracerServiceInfo.setFlag("ON");
        } else {
            if (axisConfiguration.isEngaged(axisModule)) {
                axisConfiguration.disengageModule(axisModule);
            }
            tracerServiceInfo.setFlag("OFF");
        }
        if (msgSeqBuff == null) {
            tracerServiceInfo.setEmpty(true);
            return tracerServiceInfo;
        } else {
            messageObjs = msgSeqBuff.getObjects(numberOfMessages);

            if (messageObjs.length == 0) {
                tracerServiceInfo.setEmpty(true);
                return tracerServiceInfo;

            } else {
                ArrayList msgInfoList = new ArrayList();
                boolean filterStatus = (filter != null && filter.length() != 0);
                tracerServiceInfo.setFilter(filterStatus);

                for (int i = 0; i < messageObjs.length; i++) {
                    MessageInfo mi = (MessageInfo) messageObjs[i];
                    if (filterStatus) {
                        MessagePayload miPayload = getMessage(mi.getServiceId(),
                                                              mi.getOperationName(),
                                                              mi.getMessageSequence());
                        if (miPayload.getRequest().indexOf(filter) > -1) {
                            msgInfoList.add(mi);
                        }
                        continue;
                    }
                    msgInfoList.add(mi);
                }

                if (filterStatus) {
                    tracerServiceInfo.setFilterString(filter);
                    if (msgInfoList.size() == 0) {
                        tracerServiceInfo.setEmpty(true);
                        return tracerServiceInfo;
                    }
                }

                Collections.reverse(msgInfoList);
                MessageInfo lastMessageInfo = (MessageInfo) msgInfoList.get(0);
                tracerServiceInfo.setMessageInfo(
                        (MessageInfo[]) msgInfoList.toArray(new MessageInfo[msgInfoList.size()]));
                MessagePayload lastMsg = getMessage(lastMessageInfo.getServiceId(),
                                                    lastMessageInfo.getOperationName(),
                                                    lastMessageInfo.getMessageSequence());
                tracerServiceInfo.setLastMessage(lastMsg);
                tracerServiceInfo.setEmpty(false);
            }
        }
        return tracerServiceInfo;
    }

    /**
     * @param flag; support ON or OFF.
     *
     * @return The information about the Tracer service
     *
     * @throws AxisFault If the tracer module is not found
     */
    public TracerServiceInfo setMonitoring(String flag) throws AxisFault {
        if (!flag.equalsIgnoreCase("ON") && !flag.equalsIgnoreCase("OFF")) {
            throw new RuntimeException("IllegalArgument for monitoring status :" + flag);
        }
        TracerServiceInfo tracerServiceInfo = new TracerServiceInfo();
        ConfigurationContext configurationContext =
                MessageContext.getCurrentMessageContext().getConfigurationContext();
        AxisConfiguration axisConfiguration = configurationContext.getAxisConfiguration();
        AxisModule axisModule = axisConfiguration.getModule(TracerConstants.WSO2_TRACER);

        if (axisModule == null) {
            throw new RuntimeException(TracerService.class.getName() + " " +
                                       TracerConstants.WSO2_TRACER + " is not available");
        }

        if (flag.equalsIgnoreCase("ON")) {
            if (!axisConfiguration.isEngaged(axisModule.getName())) {
                try {
                    axisConfiguration.engageModule(axisModule);
                } catch (AxisFault axisFault) {
                    log.error(axisFault);
                    throw new RuntimeException(axisFault);
                }
            }
        } else if (flag.equalsIgnoreCase("OFF")) {
            if (axisConfiguration.isEngaged(axisModule.getName())) {
                axisConfiguration.disengageModule(axisModule);
                configurationContext.removeProperty(TracerConstants.MSG_SEQ_BUFFER);
            }
        }
        Parameter tracePersisterParam =
                MessageContext.getCurrentMessageContext()
                        .getParameter(TracerConstants.TRACE_PERSISTER_IMPL);
        TracePersister tracePersister = getTracePersister(tracePersisterParam);
        tracePersister.saveTraceStatus(flag);
        tracerServiceInfo.setEmpty(true);
        tracerServiceInfo.setFlag(flag);

        return tracerServiceInfo;
    }

    public MessagePayload getMessage(String serviceId,
                                     String operationName,
                                     long messageSequence) throws AxisFault {
        Parameter tracePersisterParam =
                MessageContext.getCurrentMessageContext()
                        .getParameter(TracerConstants.TRACE_PERSISTER_IMPL);
        MessagePayload messagePayload = new MessagePayload();
        TracePersister tracePersisterImpl = getTracePersister(tracePersisterParam);
        if (tracePersisterImpl != null) {
            String[] respArray =
                    tracePersisterImpl.getMessages(serviceId, operationName, messageSequence);
            if (respArray[0] != null) {
                messagePayload.setRequest(respArray[0]);
            }
            if (respArray[1] != null) {
                messagePayload.setResponse(respArray[1]);
            }
        } else {
            String message = "Tracer service encountered an error ";
            log.error(message);
            throw new RuntimeException(message);
        }

        return messagePayload;

    }

    private TracePersister getTracePersister(Parameter tracePersisterParam) throws AxisFault {
        TracePersister tracePersister = null;
        if (tracePersisterParam != null) {
            Object tracePersisterImplObj = tracePersisterParam.getValue();
            if (tracePersisterImplObj instanceof TracePersister) {
                tracePersister = (TracePersister) tracePersisterImplObj;
            } else if (tracePersisterImplObj instanceof String) {
                //This will need in TestSuite
                try {
                    tracePersister =
                            (TracePersister) Loader.loadClass(((String) tracePersisterImplObj).trim())
                                    .newInstance();
                } catch (Exception e) {
                    String message = "Cannot instatiate TracePersister ";
                    log.error(message, e);
                    throw new RuntimeException(message, e);
                }
            }
        } else {
            throw new AxisFault(TracerConstants.TRACE_PERSISTER_IMPL +
                                " parameter not defined in axis2.xml");
        }
        return tracePersister;
    }

}
