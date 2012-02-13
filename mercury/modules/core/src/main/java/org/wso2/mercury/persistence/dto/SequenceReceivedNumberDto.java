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
 * used to persist Sequence numbers received by the
 * RMD Sequence
 */
public class SequenceReceivedNumberDto {

    private long id;
    private long number;
    private long rmdSequenceID;
    private String relatesToMessageID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getRmdSequenceID() {
        return rmdSequenceID;
    }

    public void setRmdSequenceID(long rmdSequenceID) {
        this.rmdSequenceID = rmdSequenceID;
    }

    public String getRelatesToMessageID() {
        return relatesToMessageID;
    }

    public void setRelatesToMessageID(String relatesToMessageID) {
        this.relatesToMessageID = relatesToMessageID;
    }
}
