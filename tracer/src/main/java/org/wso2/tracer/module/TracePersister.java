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
package org.wso2.tracer.module;

import org.apache.axis2.context.MessageContext;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 * The interface used for persisting trace messages
 */
public interface TracePersister {

    /**
     * Set the MessageContext
     * @param msgContext The MessageContext
     */
    public void setMsgContext(MessageContext msgContext);

    /**
     * Save a message
     * 
     * @param operationName
     * @param serviceName
     * @param messageFlow
     * @param env
     * @return The sequence number of the saved message
     */
    public long saveMessage(String operationName,
                            String serviceName,
                            int messageFlow,
                            SOAPEnvelope env);

    /**
     * Set the status of the Tracer. Tracer can be either on or off
     * @param onOff
     */
    public void saveTraceStatus(String onOff);

    /**
     * Check whether trcing is enabled
     * @return
     */
    public boolean isTracingEnabled();

    /**
     * Get all the persisted messages for an opera 
     * @param serviceId
     * @param operationName
     * @param messageSequence
     * @return
     */
    public String[] getMessages(String serviceId,
                                String operationName,
                                long messageSequence);
}
