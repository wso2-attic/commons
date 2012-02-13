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
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.handlers.AbstractHandler;


/**
 * Handler to count number of requests received by WSO2 WSAS services
 */
public class InOperationCountHandler extends AbstractHandler {
    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        final AxisOperation axisOperation = msgContext.getAxisOperation();

        if (axisOperation != null) {
            Parameter operationParameter =
                    axisOperation.getParameter(StatisticsConstants.IN_OPERATION_COUNTER);

            if (operationParameter != null) {
                Object value = operationParameter.getValue();

                if (value instanceof Counter) {
                    ((Counter) value).increment();
                }
            } else {
                Counter operationCounter = new Counter();
                operationCounter.increment();

                operationParameter = new Parameter();
                operationParameter.setName(StatisticsConstants.IN_OPERATION_COUNTER);
                operationParameter.setValue(operationCounter);
                axisOperation.addParameter(operationParameter);
            }
        }
        return InvocationResponse.CONTINUE;
    }
}
