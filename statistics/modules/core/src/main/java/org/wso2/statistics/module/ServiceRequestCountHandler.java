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
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.handlers.AbstractHandler;


/**
 * Handler to count the number of request to a certain service
 */
public class ServiceRequestCountHandler extends AbstractHandler {
    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        final AxisService axisService = msgContext.getAxisService();

        if (axisService != null) {
            Parameter serviceParameter =
                    axisService.getParameter(StatisticsConstants.SERVICE_REQUEST_COUNTER);
            if (serviceParameter != null) {
                Object value = serviceParameter.getValue();
                if (value instanceof Counter) {
                    ((Counter) value).increment();
                }
            } else {
                Counter serviceCounter = new Counter();
                serviceCounter.increment();
                serviceParameter = new Parameter();
                serviceParameter.setName(StatisticsConstants.SERVICE_REQUEST_COUNTER);
                serviceParameter.setValue(serviceCounter);
                axisService.addParameter(serviceParameter);
            }
        }
        return InvocationResponse.CONTINUE;
    }
}
