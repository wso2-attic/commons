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

package org.wso2.caching.handlers;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.clustering.ClusteringFault;
import org.apache.axis2.clustering.state.Replicator;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.description.AxisModule;
import org.wso2.caching.CachableResponse;
import org.wso2.caching.CacheReplicationCommand;
import org.wso2.caching.CachingConstants;
import org.wso2.caching.util.SOAPMessageHelper;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;

public class CachingOutHandler extends CachingHandler {

    /**
     * This method will be invoked in the outflow to do the caching related out flow handling over
     * the message
     *
     * @param msgContext - MessageContext including the message to be processed
     * @return InvocationResponse.CONTINUE in order to continue after the handling
     * @throws AxisFault if any errors occured during the processing
     */
    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {

        AxisModule cachingModule =
                msgContext.getConfigurationContext().getAxisConfiguration().getModule("wso2caching");
        if(cachingModule == null) {
            return InvocationResponse.CONTINUE;
        }
        if (!msgContext.getAxisOperation().isEngaged(cachingModule) &&
                !msgContext.getAxisService().isEngaged(cachingModule)) {
            return InvocationResponse.CONTINUE;
        }

        if (log.isDebugEnabled()) {
            log.debug("Starting the execution of the CachingOutHandler");
        }

        if (!msgContext.isServerSide()) {
            throw new UnsupportedOperationException("Client side caching is not supported");
        }
        OperationContext opCtx = msgContext.getOperationContext();
        if (opCtx == null) {
            handleException("Unable to store the cached response : " +
                                    "OperationContext not found for the processing");
        }
        assert opCtx != null;
        CachableResponse response =
                (CachableResponse) opCtx.getPropertyNonReplicable(CachingConstants.CACHED_OBJECT);
        if (response != null && response.getResponseEnvelope() == null) { // Set the SOAP response env
            SOAPEnvelope envelope
                    = SOAPMessageHelper.cloneSOAPEnvelope(msgContext.getEnvelope());
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try {
                envelope.serialize(outStream);
                response.setResponseEnvelope(outStream.toByteArray());
                if (response.getTimeout() > 0) {
                    response.setExpireTimeMillis(System.currentTimeMillis()
                                                             + response.getTimeout());
                }
            } catch (XMLStreamException e) {
                handleException("Unable to store the cached response : " +
                                        "Error in serializing the response", e);
            }
        }

        // Finally, we may need to replicate the changes in the cache
        CacheReplicationCommand cacheReplicationCommand
                = (CacheReplicationCommand) opCtx.getPropertyNonReplicable(CachingConstants.STATE_REPLICATION_OBJECT);
        if (cacheReplicationCommand != null) {
            try {
                Replicator.replicateState(cacheReplicationCommand,
                                          opCtx.getRootContext().getAxisConfiguration());
            } catch (ClusteringFault clusteringFault) {
                log.error("Cannot replicate cache changes");
            }
        }
        return InvocationResponse.CONTINUE;
    }
}
