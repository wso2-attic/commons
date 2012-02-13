/*
 * Copyright (c) 2006, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.caching.transport;

import org.apache.axis2.context.MessageContext;

/**
 * This interface provides the API for the transport related processings of the caching module.
 * That is to check whether the caching is disabled or not by looking at transport headers and
 * so on. Implementation classes will contain the transport specific code.
 */
public interface TransportProcessor {

    /**
     * Implementations of this method checks the transport headers and retruns whether caching is
     * disabled by the transport or not
     * 
     * @return boolean - true if the caching is disabled by the transport headers,
     *  false if not disabled, or can not determine
     */
    public boolean isCachingDisabled();

    /**
     * Implementations of this method writes the requestHash to the transport headers for the
     * standard caching control for the client to determine whether to use these hash values for
     * further requests or not.
     * 
     * @param msgCtx - MessageContext of which the transport headers will be writen
     * @param requestHash - hash value of the request for cahce controls
     * @return boolean - true if the transport headers were written successfully,
     *  false if not.
     */
    public boolean writeHashKey(MessageContext msgCtx, String requestHash);

    /**
     * Implementations of this method will read the transport headers to find the hash key presense
     * for the request and if there are any depending on the specifications of the relevant
     * transport this will return the String key
     * 
     * @param msgCtx - MessageContext to be read for the transport headers
     * @return String - key hash value if there is a particular matching transport header,
     *  returns null if not
     */
    public String readHashKey(MessageContext msgCtx);

    /**
     * Implementations of this method will write the relevant transport headers to the message
     * context to disable the caching on the serverside depending on the transport specifications
     * 
     * @param msgCtx - MessageContext to which the transport headers will be written
     * @return boolean - true if the write successfully done, false if not
     */
    public boolean disableCaching(MessageContext msgCtx);
}
