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
package org.wso2.statistics.module;

import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisOperation;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.wso2.statistics.Counter;
import org.wso2.statistics.StatisticsConstants;

/**
 * Handler to count all Faults
 */
public class FaultCountHandler extends AbstractHandler {
    private static Log log = LogFactory.getLog(FaultCountHandler.class);

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {

        // Increment the global fault count
        Parameter globalFaultCounter =
                msgContext.getParameter(StatisticsConstants.GLOBAL_FAULT_COUNTER);
        if (globalFaultCounter == null) {
            log.warn(StatisticsConstants.GLOBAL_FAULT_COUNTER + " is null");
        } else {
            ((Counter) globalFaultCounter.getValue()).increment();
        }
        
        // Increment the service fault count
        AxisService axisService = msgContext.getAxisService();
        if (axisService != null) {
            Parameter parameter = axisService.getParameter(StatisticsConstants.SERVICE_FAULT_COUNTER);
            if (parameter != null) {
                ((Counter) parameter.getValue()).increment();
            } else {
                Counter counter = new Counter();
                counter.increment();
                parameter = new Parameter();
                parameter.setName(StatisticsConstants.SERVICE_FAULT_COUNTER);
                parameter.setValue(counter);
                axisService.addParameter(parameter);
            }
        }

        // Increment the operation fault count
        AxisOperation axisOperation = msgContext.getAxisOperation();
        if (axisOperation != null) {
            Parameter parameter = axisOperation.getParameter(StatisticsConstants.OPERATION_FAULT_COUNTER);
            if (parameter != null) {
                ((Counter) parameter.getValue()).increment();
            } else {
                Counter counter = new Counter();
                counter.increment();
                parameter = new Parameter();
                parameter.setName(StatisticsConstants.OPERATION_FAULT_COUNTER);
                parameter.setValue(counter);
                axisOperation.addParameter(parameter);
            }
        }
        return InvocationResponse.CONTINUE;
    }
}
