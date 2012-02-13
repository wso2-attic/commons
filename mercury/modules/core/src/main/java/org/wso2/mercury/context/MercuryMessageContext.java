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
package org.wso2.mercury.context;

import org.wso2.mercury.message.RMApplicationMessage;
import org.wso2.mercury.persistence.dto.RMSMessageDto;
import org.wso2.mercury.persistence.dto.RMDMessageDto;
import org.apache.axis2.context.MessageContext;

/**
 * this class is used to wrap the message context and
 * the application message and keep it in the
 * buffers
 */
public class MercuryMessageContext {

    private long lastMessageSendTime = 0;
    private RMApplicationMessage rmApplicationMessage;
    private MessageContext messageContext;
    private RMSMessageDto rmsPersistanceDto;
    private RMDMessageDto rmdPersistanceDto;
    // keep the number of retransmissions
    private long retransmitCount = -1;

    // we need to keep the current handler index.
    // otherwise when resending messages handlers do not get invoked.
    private int currentHandlerIndex;

    public MercuryMessageContext(RMApplicationMessage rmApplicationMessage,
                                 MessageContext messageContext) {
        this.rmApplicationMessage = rmApplicationMessage;
        this.messageContext = messageContext;
        this.currentHandlerIndex = messageContext.getCurrentHandlerIndex();
    }

    public MercuryMessageContext(RMSMessageDto persistanceDto,
                                 RMApplicationMessage rmApplicationMessage,
                                 MessageContext messageContext) {
        this(rmApplicationMessage,messageContext);
        this.rmsPersistanceDto = persistanceDto;
    }

    public void increaseRetransmitCount(){
        retransmitCount ++;
    }

    public long getLastMessageSendTime() {
        return lastMessageSendTime;
    }

    public void setLastMessageSendTime(long lastMessageSendTime) {
        this.lastMessageSendTime = lastMessageSendTime;
    }

    public RMApplicationMessage getRmApplicationMessage() {
        return rmApplicationMessage;
    }

    public void setRmApplicationMessage(RMApplicationMessage rmApplicationMessage) {
        this.rmApplicationMessage = rmApplicationMessage;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    public RMSMessageDto getRmsPersistanceDto() {
        return rmsPersistanceDto;
    }

    public void setRmsPersistanceDto(RMSMessageDto rmsPersistanceDto) {
        this.rmsPersistanceDto = rmsPersistanceDto;
    }

    public RMDMessageDto getRmdPersistanceDto() {
        return rmdPersistanceDto;
    }

    public void setRmdPersistanceDto(RMDMessageDto rmdPersistanceDto) {
        this.rmdPersistanceDto = rmdPersistanceDto;
    }

    public int getCurrentHandlerIndex() {
        return currentHandlerIndex;
    }

    public void setCurrentHandlerIndex(int currentHandlerIndex) {
        this.currentHandlerIndex = currentHandlerIndex;
    }

    public long getRetransmitCount() {
        return retransmitCount;
    }

    public void setRetransmitCount(long retransmitCount) {
        this.retransmitCount = retransmitCount;
    }

}
