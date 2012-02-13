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

/**
 * used to send and receive persistence data from the
 * persistence storage
 */
public class RMSSequenceDto {

    /** this is the id to uniquly identify this object */
    private long id;

    private String sequenceID;
    private int state;
    private long messageNumber;
    private long lastMessageNumber;
    private String endPointAddress;
    private String ackToEpr;
    private String sequenceOffer;
    private long startTime;
    private long endTime;
    private long lastAccessedTime;
    private long internalKeyID;
    private long axis2InfoID;
    private String mep;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(long messageNumber) {
        this.messageNumber = messageNumber;
    }

    public long getLastMessageNumber() {
        return lastMessageNumber;
    }

    public void setLastMessageNumber(long lastMessageNumber) {
        this.lastMessageNumber = lastMessageNumber;
    }

    public String getEndPointAddress() {
        return endPointAddress;
    }

    public void setEndPointAddress(String endPointAddress) {
        this.endPointAddress = endPointAddress;
    }

    public String getAckToEpr() {
        return ackToEpr;
    }

    public void setAckToEpr(String ackToEpr) {
        this.ackToEpr = ackToEpr;
    }

    public String getSequenceOffer() {
        return sequenceOffer;
    }

    public void setSequenceOffer(String sequenceOffer) {
        this.sequenceOffer = sequenceOffer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getInternalKeyID() {
        return internalKeyID;
    }

    public void setInternalKeyID(long internalKeyID) {
        this.internalKeyID = internalKeyID;
    }

    public long getAxis2InfoID() {
        return axis2InfoID;
    }

    public void setAxis2InfoID(long axis2InfoID) {
        this.axis2InfoID = axis2InfoID;
    }

    public String getMep() {
        return mep;
    }

    public void setMep(String mep) {
        this.mep = mep;
    }
}
