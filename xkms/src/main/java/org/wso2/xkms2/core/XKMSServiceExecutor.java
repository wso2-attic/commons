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
package org.wso2.xkms2.core;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.wso2.xkms2.ResultType;
import org.wso2.xkms2.XKMSException;

/**
 *
 */

public interface XKMSServiceExecutor {


    /**
     *
     * @param xkmsElement
     * @param factory
     * @param document
     * @param protocolExchange
     * @return
     * @throws XKMSException
     * @throws AxisFault
     */
    public ResultType execute(XKMSRequestData data, MessageContext messageContext)
            throws XKMSException, AxisFault;
    

    /**
     * Intialize the service
     *
     * @param configCtx
     * @throws AxisFault
     */
    public void init(ServiceContext serviceContext) throws AxisFault;


    /**
     * This will return the associated element type
     * @return String
     */
    public String[] getAssociatedElemenTypes();


}
