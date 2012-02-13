/*
 * Copyright 2007 WSO2, Inc. http://www.wso2.org
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
package org.wso2.dynamicresponse;

import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.transport.http.HTTPConstants;

import javax.servlet.http.HttpServletRequest;

public class DynamicResponseHandler extends AbstractHandler {
    public InvocationResponse invoke(MessageContext messageContext) throws AxisFault {
        OperationContext operationContext = messageContext.getOperationContext();
        if (operationContext != null) {
            MessageContext inMessageContext = operationContext.getMessageContext(
                    WSDL2Constants.MESSAGE_LABEL_IN);
            if (inMessageContext != null) {
                HttpServletRequest request = (HttpServletRequest) inMessageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
                if (request != null) {
                    String responseFormat = request.getParameter("response");
                    if (responseFormat != null) {
                        Parameter parameter = getParameter(responseFormat);
                        if (parameter != null) {
                            responseFormat = (String) parameter.getValue();
                        }
                        messageContext.setProperty(Constants.Configuration.MESSAGE_TYPE, responseFormat);
                    }
                }
            }
        }
        return InvocationResponse.CONTINUE;
    }
}
   