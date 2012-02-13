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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.caching.CachingException;

public class TransportProcessorFactory {

    /**
     * This variable holds the log appender for the logs to be written
     */
    private static final Log log = LogFactory.getLog(TransportProcessorFactory.class);

    /**
     * This static method will get the TransportProcessor depending on the transport for the
     * description of the transportIn of the message context
     *
     * @param msgCtx - MessageContext of which the TransportIn description will be checked to get
     *  the relevant transport processor
     * @return TransportProcessor - depending on the transport and if no impl found for a
     *  particular transport, then null will be return
     * @throws CachingException - in case of a failure to find the transport
     */
    public static TransportProcessor getInTransportProcessor(
            MessageContext msgCtx) throws CachingException {
        return getTransportProcessor(msgCtx.getTransportIn().getName());
    }

    /**
     * This static method will get the TransportProcessor depending on the transport for the
     * description of the transportOut of the message context
     *
     * @param msgCtx - MessageContext of which the TransportOut description will be checked to get
     *  the relevant transport processor
     * @return TransportProcessor - depending on the transport and if no impl found for a
     *  particular transport, then null will be return
     * @throws CachingException - in case of a failure to find the transport
     */
    public static TransportProcessor getOutTransportProcessor(
            MessageContext msgCtx) throws CachingException {
        return getTransportProcessor(msgCtx.getTransportOut().getName()); 
    }

    /**
     * Provides the functioanlity to the above described public methods. String transport name
     * will be used to derive the TransportProcessor.
     * 
     * @param transport - String representing the transport name
     * @return TransportProcessor - relevant to the transport string
     * @throws CachingException - incase of a failure to get the processor
     */
    private static TransportProcessor getTransportProcessor(
            String transport) throws CachingException {

        TransportProcessor processor = null;
        String className = transport.toUpperCase() + "TransportProcessor";
        Class clazz = TransportProcessor.class;
        try {
            clazz = Class.forName(className);
            Object o = clazz.newInstance();
            if (o instanceof TransportProcessor) {
                processor = (TransportProcessor) o;
            }
        } catch (ClassNotFoundException e) {
            handleException("Unable to find the transport processor " +
                    "for the transport " + transport, e);
        } catch (IllegalAccessException e) {
            handleException("Unable to instantiate the transport processor " + clazz, e);
        } catch (InstantiationException e) {
            handleException("Unable to instantiate the transport processor " + className, e);
        }

        return processor;
    }

    private static void handleException(String message, Throwable e) throws CachingException {
        if (log.isDebugEnabled()) {
            log.debug(message, e);
        }
        throw new CachingException(message, e);
    }
}
