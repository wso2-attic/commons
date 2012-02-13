/*
* Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
*
*
*/
package org.wso2.statistics.module;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.statistics.Counter;
import org.wso2.statistics.StatisticsConstants;

/**
 * Handler to compute the response time
 */
public class ResponseTimeHandler extends AbstractHandler {

    private static final Log log = LogFactory.getLog(ResponseTimeHandler.class);

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        calculateResponseTimes(msgContext);
        return InvocationResponse.CONTINUE;
    }

    public void flowComplete(MessageContext msgContext) {

        if (msgContext != null) {
            OperationContext opContext = msgContext.getOperationContext();
            if (opContext != null) {
                AxisOperation axisOp = opContext.getAxisOperation();
                if (axisOp != null) {
                    String mep = axisOp.getMessageExchangePattern();
                    if (mep != null &&
                        (mep.equals(WSDL2Constants.MEP_URI_IN_ONLY) ||
                         mep.equals(WSDL2Constants.MEP_URI_ROBUST_IN_ONLY))) {
                        try {
                            calculateResponseTimes(msgContext);
                        } catch (AxisFault axisFault) {
                            log.error("Cannot compute reponse times", axisFault);
                        }
                    }
                }
            }
        }
    }

    private void calculateResponseTimes(MessageContext msgContext)
            throws AxisFault {
        OperationContext opctx = msgContext.getOperationContext();
        if (opctx != null) {
            MessageContext inMsgCtx = opctx.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            if (inMsgCtx != null) {
                Object receivedTime =
                        inMsgCtx.getProperty(StatisticsConstants.REQUEST_RECEIVED_TIME);
                if (receivedTime != null) {
                    long responseTime = System.currentTimeMillis() -
                                        Long.parseLong(receivedTime.toString());

                    // Handle global reponse time
                    Parameter globalReqCounterParam =
                            inMsgCtx.getParameter(StatisticsConstants.GLOBAL_REQUEST_COUNTER);
                    int globalReqCount = 0;
                    if (globalReqCounterParam != null) {
                        globalReqCount = ((Counter) globalReqCounterParam.getValue()).getCount();
                    }
                    StatisticsModule.responseTimeProcessor.
                            addResponseTime(responseTime, globalReqCount, msgContext);

                    // Handle service response time
                    AxisService axisService = msgContext.getAxisService();
                    if (axisService != null) {
                        Parameter parameter =
                                axisService.
                                        getParameter(StatisticsConstants.SERVICE_RESPONSE_TIME_PROCESSOR);
                        Parameter srcParam =
                                axisService.
                                        getParameter(StatisticsConstants.SERVICE_REQUEST_COUNTER);
                        int srcCount = 0;
                        if (srcParam != null) {
                            srcCount = ((Counter) srcParam.getValue()).getCount();
                        }
                        if (parameter != null) {
                            ((ResponseTimeProcessor) parameter.getValue()).
                                    addResponseTime(responseTime, srcCount, msgContext);
                        } else {
                            ResponseTimeProcessor proc = new ResponseTimeProcessor();
                            proc.addResponseTime(responseTime, srcCount, msgContext);
                            parameter = new Parameter();
                            parameter.setName(StatisticsConstants.SERVICE_RESPONSE_TIME_PROCESSOR);
                            parameter.setValue(proc);
                            axisService.addParameter(parameter);
                        }
                    }

                    // Handle operation response time
                    AxisOperation axisOperation = msgContext.getAxisOperation();
                    if (axisOperation != null) {
                        Parameter parameter =
                                axisOperation
                                        .getParameter(StatisticsConstants.OPERATION_RESPONSE_TIME_PROCESSOR);
                        Parameter opReqCounterParam =
                                axisOperation.getParameter(StatisticsConstants.IN_OPERATION_COUNTER);
                        int opReqCount = 0;
                        if (opReqCounterParam != null) {
                            opReqCount = ((Counter) opReqCounterParam.getValue()).getCount();
                        }
                        if (parameter != null) {
                            ((ResponseTimeProcessor) parameter.getValue()).
                                    addResponseTime(responseTime, opReqCount, msgContext);
                        } else {
                            ResponseTimeProcessor proc = new ResponseTimeProcessor();
                            proc.addResponseTime(responseTime, opReqCount, msgContext);
                            parameter = new Parameter();
                            parameter.setName(StatisticsConstants.OPERATION_RESPONSE_TIME_PROCESSOR);
                            parameter.setValue(proc);
                            axisOperation.addParameter(parameter);
                        }
                    }
                }
            }
        }
    }
}
