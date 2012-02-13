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

package org.wso2.caching.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.receivers.AbstractMessageReceiver;
import org.apache.axis2.saaj.util.SAAJUtil;
import org.apache.axis2.util.MessageContextBuilder;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.saaj.util.IDGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.soap.SOAPFactory;
import org.wso2.caching.CachableResponse;
import org.wso2.caching.CachingConstants;
import org.wso2.caching.CachingException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CacheMessageReceiver extends AbstractMessageReceiver {

    /**
     * This variable will hold the log4j appender
     */
    Log log = LogFactory.getLog(CacheMessageReceiver.class);

    /**
     * This method is just implemented to make the MR not abstract, will not be invoked
     *
     * @param messageCtx -
     * @throws AxisFault
     */
    protected void invokeBusinessLogic(MessageContext messageCtx) throws AxisFault {
        // No logic hence the response is served from the cache, this method will not be invoked
        // Need to implement this in order to make this MR not abstract
    }

    /**
     * This method will be called when the message is received to the MR
     * and this will serve the response from the cache
     *
     * @param messageCtx - MessageContext to be served
     * @throws AxisFault if there is any error in serving from the cache
     */
    public void receive(MessageContext messageCtx) throws AxisFault {

        MessageContext outMsgContext = MessageContextBuilder.createOutMessageContext(messageCtx);

        if (outMsgContext != null) {
            OperationContext opCtx = outMsgContext.getOperationContext();

            if (opCtx != null) {
                opCtx.addMessageContext(outMsgContext);
                if (log.isDebugEnabled()) {
                    log.debug("Serving from the cache...");
                }
                
                Object cachedObj =
                        opCtx.getPropertyNonReplicable(CachingConstants.CACHED_OBJECT);
                if (cachedObj != null && cachedObj instanceof CachableResponse) {
                    try {
                        MessageFactory mf = MessageFactory.newInstance();
                        SOAPMessage smsg;
                        if (messageCtx.isSOAP11()) {
                            smsg = mf.createMessage(new MimeHeaders(), new ByteArrayInputStream(
                                    ((CachableResponse) cachedObj).getResponseEnvelope()));
                            ((CachableResponse) cachedObj).setInUse(false);
                        } else {
                            MimeHeaders mimeHeaders = new MimeHeaders();
                            mimeHeaders.addHeader("Content-ID", IDGenerator.generateID());
                            mimeHeaders.addHeader("content-type",
                                    HTTPConstants.MEDIA_TYPE_APPLICATION_SOAP_XML);
                            smsg = mf.createMessage(mimeHeaders, new ByteArrayInputStream(
                                    ((CachableResponse) cachedObj).getResponseEnvelope()));
                            ((CachableResponse) cachedObj).setInUse(false);
                        }

                        if (smsg != null) {
                            org.apache.axiom.soap.SOAPEnvelope omSOAPEnv =
                                    SAAJUtil.toOMSOAPEnvelope(
                                            smsg.getSOAPPart().getDocumentElement());
                            if (omSOAPEnv.getHeader() == null) {
                                SOAPFactory fac = getSOAPFactory(messageCtx);
                                fac.createSOAPHeader(omSOAPEnv);
                            }
                            outMsgContext.setEnvelope(omSOAPEnv);
                        } else {
                            handleException("Unable to serve from the cache : " +
                                    "Couldn't build the SOAP response from the cached byte stream");
                        }
                        
                    } catch (SOAPException e) {
                        handleException("Unable to serve from the cache : " +
                            "Unable to get build the response from the byte stream", e);
                    } catch (IOException e) {
                        handleException("Unable to serve from the cache : " +
                            "I/O Error in building the response envelope from the byte stream");
                    }
                    
                    AxisEngine.send(outMsgContext);
                    
                } else {
                    handleException("Unable to find the response in the cache");
                }
            } else {
                handleException("Unable to serve from " +
                    "the cache : OperationContext not found for processing");
            }
        } else {
            handleException("Unable to serve from " +
                "the cache : Unable to get the out message context");
        }
    }

    private void handleException(String message) throws AxisFault {

        if (log.isDebugEnabled()) {
            log.debug(message);
        }
        throw new AxisFault(message, new CachingException(message));
    }

    private void handleException(String message, Throwable cause) throws AxisFault {

        if (log.isDebugEnabled()) {
            log.debug(message, cause);
        }
        throw new AxisFault(message, new CachingException(message, cause));
    }

}
