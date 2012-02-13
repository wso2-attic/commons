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
 * used to load and save the RMSMessages
 */
public class RMSMessageDto {

    private long id;
    private long messageNumber;
    private boolean isLastMessage;
    private String soapEnvelpe;
    private boolean isSend;
    private long rmsSequenceID;
    private String axisMessageID;
    private String relatesToMessageID;
    private String callBackClassName;
    private String action;
    private QName operationName;


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

    public boolean isLastMessage() {
        return isLastMessage;
    }

    public void setLastMessage(boolean lastMessage) {
        isLastMessage = lastMessage;
    }

    public String getSoapEnvelpe() {
        return soapEnvelpe;
    }

    public void setSoapEnvelpe(String soapEnvelpe) {
        this.soapEnvelpe = soapEnvelpe;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public long getRmsSequenceID() {
        return rmsSequenceID;
    }

    public void setRmsSequenceID(long rmsSequenceID) {
        this.rmsSequenceID = rmsSequenceID;
    }

    public String getAxisMessageID() {
        return axisMessageID;
    }

    public void setAxisMessageID(String axisMessageID) {
        this.axisMessageID = axisMessageID;
    }

    public String getCallBackClassName() {
        return callBackClassName;
    }

    public void setCallBackClassName(String callBackClassName) {
        this.callBackClassName = callBackClassName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public QName getOperationName() {
        return operationName;
    }

    public void setOperationName(QName operationName) {
        this.operationName = operationName;
    }

    public String getRelatesToMessageID() {
        return relatesToMessageID;
    }

    public void setRelatesToMessageID(String relatesToMessageID) {
        this.relatesToMessageID = relatesToMessageID;
    }

}
