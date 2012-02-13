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

package org.wso2.caching;

import java.io.Serializable;

/**
 * This object holds the cached response and the related properties of the cache
 * per request and will be stored in to the cache. This holds the response envelope
 * together with the request hash and the response hash. Apart from that this object
 * holds the refresh time of the cache and the timeout period. This implements the
 * Serializable interfaace to support the clustered caching.
 *
 * @see java.io.Serializable
 */
public class CachableResponse implements Serializable {

    /**
     * This holds the reference to the response envelope. To support clustered
     * caching, the response envelope has to be in a serializable format, but
     * because the SOAPEnvelope or OMElement is not serializable response envelope
     * has kept as its serilaized format as a byte[]
     */
    private byte[] responseEnvelope;

    /**
     * This boolean value defines whether this cached object is in use or not
     * Cache cleanup method will not remove cached object if cached object is in use
     * Upon cache hit, cached object will set inUse as true and set inUse as false after response
     * is served by cached object
     */
    private boolean inUse;

    /**
     * This holds the hash value of the request payload which is calculated form
     * the specified DigestGenerator, and is used to index the cached response
     */
    private String requestHash;

    /**
     * This holds the hash value of the response which is calculated from the
     * specified DigestGenerator. This is required only if the client side caching
     * is enabled, and in which case if the E-Tag (or the equivalent SOAP header)
     * value sent with the request by the client matches this response hash then
     * the server can respond with the not-modified http header (or the equivalent
     * SOAP header) to the client without sending the response.
     */
    private String responseHash;

    /**
     * This holds the time at which this particular cached response expires, in
     * the standard java system time format (i.e. System.currentTimeMillis())
     */
    private long expireTimeMillis;

    /**
     * This holds the timeout period of the cached response which will be used
     * at the next refresh time in order to generate the expireTimeMillis
     */
    private long timeout;

    /**
     * This method checks whether this cached respose is expired or not
     * 
     * @return boolean true if expired and false if not
     */
    public boolean isExpired() {
        return timeout <= 0 || expireTimeMillis < System.currentTimeMillis();
    }

    /**
     * This method will refresh the cached response stored in this object.
     * If further explained this method will set the response envelope and the
     * response hash to null and set the new refresh time as timeout + current time
     *
     * This is how an expired response is brought back to life
     * @param timeout The period for which this object is reincarnated
     */
    public void reincarnate(long timeout) {
        if(!isExpired()){
            throw new IllegalStateException("Unexpired Cached Responses cannot be reincarnated");
        }
        responseEnvelope = null;
        responseHash = null;
        expireTimeMillis = System.currentTimeMillis() + timeout;
        setTimeout(timeout);
    }

    /**
     * This gets the cached response envelope as a byte array
     * 
     * @return byte[] representing the cached response envelope
     */
    public byte[] getResponseEnvelope() {
        return responseEnvelope;
    }

    /**
     * This sets the response envelope to the cache as a byte array
     * 
     * @param responseEnvelope  - response envelope to be stored in to the
     *                            cache as a byte array
     */
    public void setResponseEnvelope(byte[] responseEnvelope) {
        this.responseEnvelope = responseEnvelope;
    }

    /**
     * This gets the hash value of the request payload stored in the cache
     *
     * @return String hash of the request payload
     */
    public String getRequestHash() {
        return requestHash;
    }

    /**
     * This sets the hash of the request to the cache
     * 
     * @param requestHash   - hash of the request payload to be set as an String
     */
    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    /**
     * This gets the hash value of the response stored in the cache
     *
     * @return String hash of the response
     */
    public String getResponseHash() {
        return responseHash;
    }

    /**
     * This sets the hash of the response to the cache
     *
     * @param responseHash  - hash of the response to be set as an String
     */
    public void setResponseHash(String responseHash) {
        this.responseHash = responseHash;
    }

    /**
     * This gets the expireTimeMillis in the standard java system time format
     * 
     * @return long refresh time in the standard java system time format
     */
    public long getExpireTimeMillis() {
        return expireTimeMillis;
    }

    /**
     * This sets the refresh time to the cached response
     *
     * @param expireTimeMillis    - refresh time in the standard java system time format
     */
    public void setExpireTimeMillis(long expireTimeMillis) {
        this.expireTimeMillis = expireTimeMillis;
    }

    /**
     * This gets the timeout period in milliseconds
     * 
     * @return timeout in milliseconds
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * This sets the timeout period as milliseconds
     *
     * @param timeout   - millisecond timeoutperiod to be set
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public synchronized void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public synchronized boolean isInUse() {
        return inUse;
    }
}