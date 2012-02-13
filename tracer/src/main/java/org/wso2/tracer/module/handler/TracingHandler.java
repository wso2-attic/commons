/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
 *
 */
package org.wso2.tracer.module.handler;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.handlers.AbstractHandler;
import org.wso2.tracer.TracerConstants;
import org.wso2.tracer.service.MessageInfo;
import org.wso2.tracer.module.TraceFilter;
import org.wso2.tracer.module.TracePersister;
import org.wso2.utils.CircularBuffer;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This handler will log all the message going through this so that one can
 * later see what messages have to which operations. Since this handler reads
 * the whole message, please do remember that this will build the whole SOAP
 * message.
 */
public class TracingHandler extends AbstractHandler {

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        AxisService axisService = msgContext.getAxisService();

        if (axisService == null) {
            return InvocationResponse.CONTINUE;
        }

        if (axisService.isClientSide()) {
            return InvocationResponse.CONTINUE;
        }

        ConfigurationContext configCtx = msgContext.getConfigurationContext();


        TraceFilter traceFilter =
                (TraceFilter) getParameter(TracerConstants.TRACE_FILTER_IMPL).getValue();
        if (traceFilter.isFilteredOut(msgContext)) {
            return InvocationResponse.CONTINUE;
        }

        if ((msgContext.getAxisOperation() != null) &&
            (msgContext.getAxisOperation().getName() != null)) {
            String operationName =
                    msgContext.getAxisOperation().getName().getLocalPart();
            String serviceName = axisService.getName();
            long msgSeq = storeMessage(operationName,
                                       serviceName,msgContext);

            // Add the message id to the CircularBuffer.
            // We need to track only the IN_FLOW msg, since with that sequence number,
            // we can retrieve all other related messages from the persister.
            if (msgContext.getFLOW() == MessageContext.IN_FLOW) {
                CircularBuffer msgSeqBuff =
                        (CircularBuffer) configCtx.getProperty(TracerConstants.MSG_SEQ_BUFFER);

                if (msgSeqBuff == null) {
                    msgSeqBuff = new CircularBuffer(TracerConstants.MSG_BUFFER_SZ);
                    configCtx.setProperty(TracerConstants.MSG_SEQ_BUFFER, msgSeqBuff);
                }

                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMessageSequence(msgSeq);
                messageInfo.setOperationName(operationName);
                messageInfo.setServiceId(serviceName);
                messageInfo.setTimestamp(cal);
                msgSeqBuff.append(messageInfo);
            }
        }
        return InvocationResponse.CONTINUE;
    }

    /**
     * Store the received message
     *
     * @param operationName
     * @param serviceName
     * @param msgCtxt
     * @return the sequence of the message stored with respect to the operation
     *         in the service
     */
    private long storeMessage(String operationName,
                              String serviceName,
                              MessageContext msgCtxt) {
        TracePersister tracePersister =
                (TracePersister) getParameter(TracerConstants.TRACE_PERSISTER_IMPL).getValue();
        tracePersister.setMsgContext(msgCtxt);
        return tracePersister
                .saveMessage(operationName, serviceName, msgCtxt.getFLOW(), msgCtxt.getEnvelope());
    }
}
