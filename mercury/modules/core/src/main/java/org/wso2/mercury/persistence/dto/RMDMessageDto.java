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
package org.wso2.mercury.persistence.dto;

import javax.xml.namespace.QName;

/**
 * used to persists the
 */
public class RMDMessageDto {

    private long id;
    private long messageNumber;
    private String soapEnvelope;
    private boolean isSend;
    private long internalBufferID;

    private QName operationName;
    private String action;
    private String messageID;
    private String to;
    private String replyTo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(long messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getSoapEnvelope() {
        return soapEnvelope;
    }

    public void setSoapEnvelope(String soapEnvelope) {
        this.soapEnvelope = soapEnvelope;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public long getInternalBufferID() {
        return internalBufferID;
    }

    public void setInternalBufferID(long internalBufferID) {
        this.internalBufferID = internalBufferID;
    }

    public QName getOperationName() {
        return operationName;
    }

    public void setOperationName(QName operationName) {
        this.operationName = operationName;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
