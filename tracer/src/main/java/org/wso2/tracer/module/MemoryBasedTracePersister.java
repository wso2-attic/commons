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
package org.wso2.tracer.module;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;
import org.wso2.tracer.TracerConstants;
import org.wso2.tracer.TracerUtils;
import org.wso2.utils.CircularBuffer;

import java.util.HashMap;
import java.util.Map;

/**
 *  A memory based trace persister which holds the traced messages in a cicular buffer
 */
public class MemoryBasedTracePersister implements TracePersister {
    private static final String TRACING_MAP = "local_wso2tracer.map";
    private static final String REQUEST_NUMBER = "local_wso2tracer.request.number";

    private MessageContext msgContext;
    private CircularBuffer msgBuffer = new CircularBuffer(TracerConstants.MSG_BUFFER_SZ);
    private String tracingStatus;

    public void setMsgContext(MessageContext msgContext) {
        this.msgContext = msgContext;
    }

    public synchronized long saveMessage(String operationName, String serviceName,
                                         int messageFlow, SOAPEnvelope env) {
        long msgSequence = getMessageSequence(serviceName, operationName);
        TraceMessage message = new TraceMessage(operationName, serviceName,
                                                messageFlow,
                                                msgSequence,
                                                env);
        msgBuffer.append(message);
        return msgSequence;
    }

    public void saveTraceStatus(String onOff) {
        this.tracingStatus = onOff;
    }

    public boolean isTracingEnabled() {
        return tracingStatus != null && tracingStatus.equalsIgnoreCase("ON");
    }

    public synchronized String[] getMessages(String serviceId,
                                String operationName,
                                long messageSequence) {
        String[] responses = new String[2];
        Object[] objects = msgBuffer.getObjects(TracerConstants.MSG_BUFFER_SZ);
        for (int i = 0; i < objects.length; i++) {
            TraceMessage msg = (TraceMessage) objects[i];
            if (msg.getOperationName().equals(operationName) &&
                msg.getServiceName().equals(serviceId) &&
                msg.getMsgSequence() == messageSequence) {
                int messageFlow = msg.getMessageFlow();
                if (messageFlow == MessageContext.IN_FLOW ||
                    messageFlow == MessageContext.IN_FAULT_FLOW) {
                    responses[0] = TracerUtils.getPrettyString(msg.getSoapEnvelope(),
                                                               msgContext);
                } else if (messageFlow == MessageContext.OUT_FLOW ||
                           messageFlow == MessageContext.OUT_FAULT_FLOW) {
                    responses[1] = TracerUtils.getPrettyString(msg.getSoapEnvelope(),
                                                               msgContext);
                }
            }
        }
        return responses;
    }

    private long getMessageSequence(String serviceName, String operationName) {
        long msgSequence = 1;

        // check whether this is a continuation of an existing MEP
        Object requestNumber = msgContext.getOperationContext().getProperty(REQUEST_NUMBER);
        if ((requestNumber != null) && requestNumber instanceof Long) {
            msgSequence = ((Long) requestNumber).intValue();
        } else {
            // Need to have a counter for each and operation
            Map monitoringHandlerMap =
                    (Map) msgContext.getConfigurationContext().getProperty(TRACING_MAP);

            if (monitoringHandlerMap == null) {
                monitoringHandlerMap = new HashMap();
                msgContext.getConfigurationContext().setProperty(TRACING_MAP,
                                                                 monitoringHandlerMap);
            }

            String key = serviceName + "." + operationName;
            Object counterInt = monitoringHandlerMap.get(key);
            if (counterInt == null) {
                msgSequence = 0;
            } else if (counterInt instanceof Long) {
                msgSequence = ((Long) counterInt).intValue() + 1;
            }

            monitoringHandlerMap.put(key, new Long(msgSequence));
            msgContext.getOperationContext().setProperty(REQUEST_NUMBER, new Long(msgSequence));
        }

        return msgSequence;
    }
}
