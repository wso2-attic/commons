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
package org.wso2.mercury.util;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.MessageContextBuilder;
import org.apache.axis2.wsdl.WSDLConstants;

/**
 * this class provides various utility methods.
 */
public class AxisFaultUtil {


    public static MessageContext generateAxisFaultMessageContext(MessageContext msgContext, Throwable e) throws AxisFault {
        //
        MessageContext faultContext =
            MessageContextBuilder.createFaultMessageContext( msgContext, e );
        // set the Axis Message as the out message if a fault message is not
        // there
        //TODO: set the fault message it is available.
        AxisOperation axisOperation = msgContext.getAxisOperation();
        if (axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE) != null){
            faultContext.setAxisMessage(axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE));
        } else {
            faultContext.setAxisMessage(axisOperation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE));
        }

        faultContext.setProperty("RECV_RESULTS",
                msgContext.getProperty("RECV_RESULTS"));

        // set the servlet transport property
        faultContext.setProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE,
                msgContext.getProperty( HTTPConstants.MC_HTTP_SERVLETRESPONSE));

        return faultContext;
    }

}