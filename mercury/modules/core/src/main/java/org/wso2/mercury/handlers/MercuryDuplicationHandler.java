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
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.message.RMApplicationMessage;
import org.wso2.mercury.message.Sequence;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.state.RMDContext;
import org.wso2.mercury.state.RMDSequence;

/**
 * This handler is useful only for response messages of duplex mode in out operations
 * In Axis2 when it sends a message it registers the messageID with the
 * Operation context in the Configuration context. When receiving the first response
 * message it removes this entry.
 * So sub sequence messages causes a Addressing validation fault since
 * the entry is not there.
 * But for RM it may receive duplicate messages.
 * Drop these duplication messages here to avoid fault propagation.
 */

public class MercuryDuplicationHandler extends AbstractHandler {

    private static Log log = LogFactory.getLog(MercuryDuplicationHandler.class);

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
                            RMDContext rmdContext =
                                    (RMDContext) msgContext.getConfigurationContext().getProperty(
                                            MercuryConstants.RMD_CONTEXT);
                            RMDSequence rmdSequence =
                                    rmdContext.getRMDSeqenceWithSequenceID(sequence.getSequenceID());
                            if (rmdSequence.isMessageReceived(sequence.getMessageNumber())) {
                                // Then this is a duplicate message
                                return InvocationResponse.ABORT;
                            }
                        }
                    }
                    return InvocationResponse.CONTINUE;
                } catch (RMMessageBuildingException e) {
                    log.error("Invalid soap message");
                    throw new AxisFault("Invalid soap message");
                }
            }
        } else {
            return InvocationResponse.CONTINUE;
        }
    }
}
