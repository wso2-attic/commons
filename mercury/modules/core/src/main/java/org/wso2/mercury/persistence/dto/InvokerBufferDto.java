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
 * used to persists the invoker buffer
 */
public class InvokerBufferDto {

    private long id;
    private int state;
    private long lastMessage;
    private long lastMessageToApplication;
    private long rmdSequenceID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(long lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageToApplication() {
        return lastMessageToApplication;
    }

    public void setLastMessageToApplication(long lastMessageToApplication) {
        this.lastMessageToApplication = lastMessageToApplication;
    }

    public long getRmdSequenceID() {
        return rmdSequenceID;
    }

    public void setRmdSequenceID(long rmdSequenceID) {
        this.rmdSequenceID = rmdSequenceID;
    }
}
