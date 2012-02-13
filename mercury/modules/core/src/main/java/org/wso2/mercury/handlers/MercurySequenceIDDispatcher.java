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
package org.wso2.mercury.handlers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.message.RMApplicationMessage;
import org.wso2.mercury.message.Sequence;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.RMDispatchInfo;

/**
 * This handler dispatches the axis operation if it has
 * an entry
 */
public class MercurySequenceIDDispatcher extends AbstractHandler {

    private static Log log = LogFactory.getLog(MercurySequenceIDDispatcher.class);

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        if (msgContext.getOptions().getRelatesTo() != null) {
            // i.e if this is an reply
            String action = msgContext.getOptions().getAction();
            if (action.equals(MercuryConstants.CREATE_SEQUENCE_ACTION) ||
                    action.equals(MercuryConstants.CREATE_SEQUENCE_RESPONSE_ACTION) ||
                    action.equals(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT_ACTION) ||
                    action.equals(MercuryConstants.TERMINATE_SEQUENCE_ACTION)) {
                return InvocationResponse.CONTINUE;
            } else {
                try {
                    RMApplicationMessage rmApplicationMessage =
                            RMApplicationMessage.fromSOAPEnvelope(msgContext.getEnvelope());
                    if (rmApplicationMessage != null) {
                        Sequence sequence = rmApplicationMessage.getSequence();
                        if (sequence != null) {
                            String sequenceID = sequence.getSequenceID();
                            RMDispatchInfo rmDispatchInfo =
                                    (RMDispatchInfo) msgContext.getConfigurationContext().getProperty(
                                            MercuryConstants.RM_DISPATCH_INFO);
                            AxisOperation axisOperation = rmDispatchInfo.getAxisOperation(sequenceID);
                            if (axisOperation != null) {
                                if (msgContext.getAxisOperation() == null) {
                                    msgContext.setAxisOperation(axisOperation);
                                }
                                if (msgContext.getAxisService() == null) {
                                    msgContext.setAxisService(axisOperation.getAxisService());
                                }
                            }
                        }
                    }
                } catch (RMMessageBuildingException e) {
                    log.error("Invalid soap message");
                    throw new AxisFault("Invalid soap message");
                }
            }
        }
        return InvocationResponse.CONTINUE;

    }
}
