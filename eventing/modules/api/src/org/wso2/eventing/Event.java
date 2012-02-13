/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.eventing;

/**
 * An Event. A generic class to provide flexibility to store any type of a Java object as a event
 * content. E.g. a Map object can store by initiating a Event object like,
 * Event<HashMap> event = new Event(hashmap);
 */
public class Event<T> {
    private T message;
    private String topic;
    public Event(){
        
    }

    /**
     * Constuct the Event by using the message
     * @param message any Object
     */
    public Event(T message) {
        this.message = message;
    }

    /**
     * Get the message content
     * @return message object
     */
    public T getMessage() {
        return message;
    }

    /**
     * Set the message
     * @param message object 
     */
    public void setMessage(T message) {
        this.message = message;
    }

    /**
     * Set the topic
     * @param topic the topic 
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Get the topic
     * @return topic of event
     */
    public String getTopic() {
        return topic;
    }
}
