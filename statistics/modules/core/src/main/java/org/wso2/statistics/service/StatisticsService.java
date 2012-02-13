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
package org.wso2.statistics.service;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.statistics.Counter;
import org.wso2.statistics.StatisticsConstants;
import org.wso2.statistics.module.ResponseTimeProcessor;
import org.wso2.statistics.module.StatisticsModule;
import org.wso2.utils.AbstractAdmin;
import org.wso2.utils.MBeanRegistrar;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 *
 */
public class StatisticsService extends AbstractAdmin implements StatisticsServiceMBean {

    static {
        MBeanRegistrar.registerMBean(new StatisticsService());
    }

    public StatisticsService() {
    }

    /**
     * Get the total requests received by the system which hosts this service
     *
     * @return the total requests received by the system which hosts this service
     * @throws AxisFault
     */
    public int getSystemRequestCount() throws AxisFault {
        Parameter globalCounter =
                getAxisConfig()
                        .getParameter(StatisticsConstants.GLOBAL_REQUEST_COUNTER);
        if (globalCounter != null) {
            Object value = globalCounter.getValue();
            if (value instanceof Counter) {
                return ((Counter) value).getCount();
            }
        }
        return 0;
    }

    public int getSystemFaultCount() throws AxisFault {
        Parameter globalCounter =
                getAxisConfig()
                        .getParameter(StatisticsConstants.GLOBAL_FAULT_COUNTER);
        if (globalCounter != null) {
            Object value = globalCounter.getValue();
            if (value instanceof Counter) {
                return ((Counter) value).getCount();
            }
        }
        return 0;
    }

    public int getSystemResponseCount() throws AxisFault {
        Parameter globalCounter =
                getAxisConfig()
                        .getParameter(StatisticsConstants.GLOBAL_RESPONSE_COUNTER);
        if (globalCounter != null) {
            Object value = globalCounter.getValue();
            if (value instanceof Counter) {
                return ((Counter) value).getCount();
            }
        }
        return 0;
    }

    public double getAvgSystemResponseTime() {
        return StatisticsModule.responseTimeProcessor.getAvgResponseTime();
    }

    public long getMaxSystemResponseTime() {
        return StatisticsModule.responseTimeProcessor.getMaxResponseTime();
    }

    public long getMinSystemResponseTime() {
        return StatisticsModule.responseTimeProcessor.getMinResponseTime();
    }

    /**
     * Obtain the number of requests that were received by the service
     *
     * @param serviceName
     * @return The number of requests that were received by the service
     */
    public int getServiceRequestCount(String serviceName) throws AxisFault {
        Parameter parameter =
                getAxisService(serviceName)
                        .getParameter(StatisticsConstants.SERVICE_REQUEST_COUNTER);
        if (parameter != null) {
            return ((Counter) parameter.getValue()).getCount();
        }
        return 0;
    }

    public int getServiceFaultCount(String serviceName) throws AxisFault {
        Parameter parameter =
                getAxisService(serviceName)
                        .getParameter(StatisticsConstants.SERVICE_FAULT_COUNTER);
        if (parameter != null) {
            return ((Counter) parameter.getValue()).getCount();
        }
        return 0;
    }

    public int getServiceResponseCount(String serviceName) throws AxisFault {
        AxisService axisService = getAxisService(serviceName);
        int count = 0;
        for (Iterator opIter = axisService.getOperations(); opIter.hasNext();) {
            AxisOperation axisOp = (AxisOperation) opIter.next();
            Parameter parameter = axisOp.getParameter(StatisticsConstants.OUT_OPERATION_COUNTER);
            if (parameter != null) {
                count += ((Counter) parameter.getValue()).getCount();
            }
        }
        return count;
    }

    public long getMaxServiceResponseTime(String serviceName) throws AxisFault {
        long max = 0;
        Parameter parameter =
                getAxisService(serviceName).
                        getParameter(StatisticsConstants.SERVICE_RESPONSE_TIME_PROCESSOR);
        if (parameter != null) {
            ResponseTimeProcessor proc = (ResponseTimeProcessor) parameter.getValue();
            max = proc.getMaxResponseTime();
        }
        return max;
    }

    public long getMinServiceResponseTime(String serviceName) throws AxisFault {
        long min = 0;
        Parameter parameter =
                getAxisService(serviceName).
                        getParameter(StatisticsConstants.SERVICE_RESPONSE_TIME_PROCESSOR);
        if (parameter != null) {
            ResponseTimeProcessor proc = (ResponseTimeProcessor) parameter.getValue();
            min = proc.getMinResponseTime();
        }
        return min;
    }

    public double getAvgServiceResponseTime(String serviceName) throws AxisFault {
        double avg = 0;
        Parameter parameter =
                getAxisService(serviceName).
                        getParameter(StatisticsConstants.SERVICE_RESPONSE_TIME_PROCESSOR);
        if (parameter != null) {
            ResponseTimeProcessor proc = (ResponseTimeProcessor) parameter.getValue();
            avg = proc.getAvgResponseTime();
        }
        return avg;
    }

    public int getOperationRequestCount(String serviceName, String operationName) throws AxisFault {
        AxisOperation axisOperation = getAxisOperation(serviceName, operationName);
        Parameter parameter =
                axisOperation.getParameter(StatisticsConstants.IN_OPERATION_COUNTER);
        if (parameter != null) {
            return ((Counter) parameter.getValue()).getCount();
        }
        return 0;
    }

    public int getOperationFaultCount(String serviceName, String operationName) throws AxisFault {
        AxisOperation axisOperation = getAxisOperation(serviceName, operationName);

        Parameter parameter =
                axisOperation.getParameter(StatisticsConstants.OPERATION_FAULT_COUNTER);
        if (parameter != null) {
            return ((Counter) parameter.getValue()).getCount();
        }
        return 0;
    }

    public int getOperationResponseCount(String serviceName,
                                         String operationName) throws AxisFault {
        AxisOperation axisOperation = getAxisOperation(serviceName, operationName);

        Parameter parameter =
                axisOperation.getParameter(StatisticsConstants.OUT_OPERATION_COUNTER);
        if (parameter != null) {
            return ((Counter) parameter.getValue()).getCount();
        }
        return 0;
    }

    public long getMaxOperationResponseTime(String serviceName,
                                            String operationName) throws AxisFault {
        long max = 0;
        Parameter parameter =
                getAxisOperation(serviceName, operationName).
                        getParameter(StatisticsConstants.OPERATION_RESPONSE_TIME_PROCESSOR);
        if (parameter != null) {
            max = ((ResponseTimeProcessor) parameter.getValue()).getMaxResponseTime();
        }
        return max;
    }

    public long getMinOperationResponseTime(String serviceName,
                                            String operationName) throws AxisFault {
        long min = 0;
        Parameter parameter =
                getAxisOperation(serviceName, operationName).
                        getParameter(StatisticsConstants.OPERATION_RESPONSE_TIME_PROCESSOR);
        if (parameter != null) {
            min = ((ResponseTimeProcessor) parameter.getValue()).getMinResponseTime();
        }
        return min;
    }

    public double getAvgOperationResponseTime(String serviceName,
                                              String operationName) throws AxisFault {
        double avg = 0;
        Parameter parameter =
                getAxisOperation(serviceName, operationName).
                        getParameter(StatisticsConstants.OPERATION_RESPONSE_TIME_PROCESSOR);
        if (parameter != null) {
            avg = ((ResponseTimeProcessor) parameter.getValue()).getAvgResponseTime();
        }
        return avg;
    }

    private AxisService getAxisService(String serviceName) {
        return getAxisConfig().getServiceForActivation(serviceName);
    }

    private AxisOperation getAxisOperation(String serviceName,
                                           String operationName) {
        return getAxisService(serviceName).getOperation(new QName(operationName));
    }
}
