/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
package org.wso2.statistics.module;

import org.wso2.statistics.StatisticsConstants;
import org.wso2.statistics.Counter;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;


/**
 * Handler to count all requests to WSO2 WSAS
 */
public class GlobalRequestCountHandler extends AbstractHandler {

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        msgContext.setProperty(StatisticsConstants.REQUEST_RECEIVED_TIME,
                               "" + System.currentTimeMillis());
        Parameter globalRequestParam =
                msgContext.getParameter(StatisticsConstants.GLOBAL_REQUEST_COUNTER);
        if (globalRequestParam != null) {
            ((Counter) globalRequestParam.getValue()).increment();
        }
        return InvocationResponse.CONTINUE;
    }
}
