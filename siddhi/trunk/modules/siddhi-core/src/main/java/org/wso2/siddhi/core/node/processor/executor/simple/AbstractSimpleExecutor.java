/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.node.processor.executor.simple;

//import org.apache.log4j.Logger;

import org.mvel2.MVEL;
import org.wso2.siddhi.core.node.processor.executor.simple.property.ComplexProperty;
import org.wso2.siddhi.core.node.processor.executor.simple.property.Property;
import org.wso2.siddhi.core.node.processor.executor.simple.property.StreamProperty;
import org.wso2.siddhi.core.node.processor.executor.simple.property.ValueProperty;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.processor.executor.Executor;

import java.util.*;

public abstract class AbstractSimpleExecutor<T> implements Executor {

    //    private static final Logger log = Logger.getLogger(Executor.class);
    protected Set<String> checkingStreamNames;
    Class checkingClass = null;
    protected boolean evaluateBothSide = false;

    protected T value = null;
    protected StreamProperty leftStreamProperty = null;
    protected StreamProperty rightStreamProperty = null;
    protected ComplexProperty leftComplexProperty = null;

    protected ComplexProperty rightComplexProperty = null;

    private Map rightExecutorDataMap;
    private Map leftExecutorDataMap;

    public AbstractSimpleExecutor(Set<String> checkingStreamNames, Class checkingClass)
            throws SiddhiException {
        if (checkingStreamNames == null || checkingStreamNames.size() == 0) {
            throw new SiddhiException("Not checking Stream Name");
        }
        this.checkingStreamNames = checkingStreamNames;
//        else {
//            checkingStreamNames0StartsWith$ = checkingStreamNames[0].startsWith("$");
//            checkingStreamNames1StartsWith$ = checkingStreamNames[1].startsWith("$");
//        }
        this.checkingClass = checkingClass;
    }

    /**
     * Assigns the left property
     *
     * @param leftProperty the property
     * @throws SiddhiException if the property isn' a streamproperty nor a complex property
     */
    public void assignLeftProperty(Property leftProperty) throws SiddhiException {
        if (leftProperty instanceof StreamProperty) {
            leftStreamProperty = (StreamProperty) leftProperty;
        } else if (leftProperty instanceof ComplexProperty) {
            leftComplexProperty = (ComplexProperty) leftProperty;
            leftExecutorDataMap = new Hashtable();
        } else {
            throw new SiddhiException("property " + leftProperty + " is not StreamProperty or ComplexProperty");
        }
    }

    /**
     * Assigns the right property
     *
     * @param rightProperty the property
     * @throws SiddhiException if the property isn' a streamproperty nor a complex property
     */
    public void assignRightProperty(Property rightProperty) throws SiddhiException {
        if (rightProperty instanceof StreamProperty) {
            rightStreamProperty = (StreamProperty) rightProperty;
        } else if (rightProperty instanceof ComplexProperty) {
            rightComplexProperty = (ComplexProperty) rightProperty;
            rightExecutorDataMap = new Hashtable();
        } else {
            throw new SiddhiException("property " + rightProperty + " is not StreamProperty or ComplexProperty");
        }
    }

    public AbstractSimpleExecutor assignValue(ValueProperty valueProperty) {
        this.value = (T) valueProperty.getValue();
        return this;
    }

    public void setEvaluateBothSide(boolean evaluateBothSide) {
        this.evaluateBothSide = evaluateBothSide;
    }

    /**
     * Returns a Set<String> of eventstream names
     *
     * @return stream names set
     */
    public Set<String> getCheckingStreamNames() {
        return checkingStreamNames;
    }


/*//     // For sequence executor
    public boolean execute(Event[][] eventArray) {
//        leftAttributeEventPosition = getEventPosition(eventArray, leftAttributePosition);
//
//        //  log.debug("Executing event");
//        if (!checkingStreamNames0StartsWith$ &&
//                !eventArray[leftAttributePosition[0]][leftAttributeEventPosition].getStreamId().equals(checkingStreamNames[0])) {
//            return false;
//        }
//        if (evaluateBothSide) {
//            //    log.debug("Evaluating both sides = true");
//
//            rightAttributeEventPosition = getEventPosition(eventArray, rightAttributePosition);
//
//
//            if (!checkingStreamNames1StartsWith$ &&
//                    !eventArray[rightAttributePosition[0]][rightAttributeEventPosition].getStreamId().equals(checkingStreamNames[1])) {
//                return false;
//            }
//            return executeEvents((T) eventArray[leftAttributePosition[0]][leftAttributeEventPosition].getNthAttribute(leftAttributePosition[1]),
//                    (T) eventArray[rightAttributePosition[0]][rightAttributeEventPosition].getNthAttribute(rightAttributePosition[1]));
//        } else {
//            return executeEvents((T) eventArray[leftAttributePosition[0]][leftAttributeEventPosition].getNthAttribute(leftAttributePosition[1]), value);
//        }
        return false;//added as temp
    }*/

    /**
     * @param eventArray The event array for processing
     * @return true/false on whether the events matches the given condition
     */
    public boolean execute(Event[] eventArray) {
        //  log.debug("Executing event");
        if (evaluateBothSide) {
            if (null != leftStreamProperty && null != rightStreamProperty) {
                if (!checkIsInStream(eventArray, leftStreamProperty.getAttributePositions())) {
                    return false;
                }
                if (!checkIsInStream(eventArray, rightStreamProperty.getAttributePositions())) {
                    return false;
                }
                return executeEvents((T) eventArray[leftStreamProperty.getAttributePositions()[0]].getNthAttribute(leftStreamProperty.getAttributePositions()[1]),
                                     (T) eventArray[rightStreamProperty.getAttributePositions()[0]].getNthAttribute(rightStreamProperty.getAttributePositions()[1]));
            } else if (null != leftStreamProperty && null != rightComplexProperty) {
                return executeStreamVsComplexProperties(eventArray, leftStreamProperty, rightComplexProperty, rightExecutorDataMap);
            } else if (null != rightStreamProperty && null != leftComplexProperty) {
                return executeStreamVsComplexProperties(eventArray, rightStreamProperty, leftComplexProperty, leftExecutorDataMap);
            } else {
                leftExecutorDataMap = populateExecutorDataMap(eventArray, leftComplexProperty, leftExecutorDataMap);
                rightExecutorDataMap = populateExecutorDataMap(eventArray, rightComplexProperty, rightExecutorDataMap);
                if (null != checkingClass) {
                    return executeEvents((T) MVEL.executeExpression(leftComplexProperty.getCompiledExpression(), leftExecutorDataMap, checkingClass), (T) MVEL.executeExpression(rightComplexProperty.getCompiledExpression(), rightExecutorDataMap, checkingClass));
                } else {
                    return executeEvents((T) MVEL.executeExpression(leftComplexProperty.getCompiledExpression(), leftExecutorDataMap), (T) MVEL.executeExpression(rightComplexProperty.getCompiledExpression(), rightExecutorDataMap));
                }
            }
        } else {
            if (null != leftStreamProperty) {
                if (!checkIsInStream(eventArray, leftStreamProperty.getAttributePositions())) {
                    return false;
                }
                return executeEvents((T) eventArray[leftStreamProperty.getAttributePositions()[0]].getNthAttribute(leftStreamProperty.getAttributePositions()[1]), value);
            } else if (null != rightStreamProperty) {
                if (!checkIsInStream(eventArray, rightStreamProperty.getAttributePositions())) {
                    return false;
                }
                return executeEvents((T) eventArray[rightStreamProperty.getAttributePositions()[0]].getNthAttribute(rightStreamProperty.getAttributePositions()[1]), value);
            } else if (null != leftComplexProperty) {
                leftExecutorDataMap = populateExecutorDataMap(eventArray, leftComplexProperty, leftExecutorDataMap);
                return checkExecuteEventLeftComplex(leftComplexProperty, leftExecutorDataMap);
            } else if (null != rightComplexProperty) {
                rightExecutorDataMap = populateExecutorDataMap(eventArray, rightComplexProperty, rightExecutorDataMap);
                return checkExecuteEventRightComplex(rightComplexProperty, rightExecutorDataMap);
            } else {
                try {
                    throw new SiddhiException("execution does not fall in to any defined category");
                } catch (SiddhiException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
    }

    public boolean execute(Event[][] eventArray) {
        //  log.debug("Executing event");
        if (evaluateBothSide) {
            if (null != leftStreamProperty && null != rightStreamProperty) {
                if (!checkIsInStream(eventArray, leftStreamProperty.getAttributePositions())) {
                    return false;
                }
                if (!checkIsInStream(eventArray, rightStreamProperty.getAttributePositions())) {
                    return false;
                }

                try {
                    return executeEvents((T) eventArray[leftStreamProperty.getAttributePositions()[0]][getEventPosition(eventArray, leftStreamProperty.getAttributePositions())].getNthAttribute(leftStreamProperty.getAttributePositions()[1]),
                                         (T) eventArray[rightStreamProperty.getAttributePositions()[0]][getEventPosition(eventArray, rightStreamProperty.getAttributePositions())].getNthAttribute(rightStreamProperty.getAttributePositions()[1]));
                } catch (Exception e) {


                    if (getEventPosition(eventArray, rightStreamProperty.getAttributePositions()) == 2) {
                        if (eventArray[rightStreamProperty.getAttributePositions()[0]][2] == null) {
                            eventArray[rightStreamProperty.getAttributePositions()[0]][2] = eventArray[rightStreamProperty.getAttributePositions()[0]][0];
                            return true;
                        }
                    } else if (getEventPosition(eventArray, leftStreamProperty.getAttributePositions()) == 2) {
                        if (eventArray[leftStreamProperty.getAttributePositions()[0]][2] == null) {
                            eventArray[leftStreamProperty.getAttributePositions()[0]][2] = eventArray[leftStreamProperty.getAttributePositions()[0]][0];
                            return true;
                        }
                    }

                }
//            } else if (null != leftStreamProperty && null != rightComplexProperty) {
//                return executeStreamVsComplexProperties(eventArray, leftStreamProperty, rightComplexProperty, rightExecutorDataMap);
//            } else if (null != rightStreamProperty && null != leftComplexProperty) {
//                return executeStreamVsComplexProperties(eventArray, rightStreamProperty, leftComplexProperty, leftExecutorDataMap);
//            } else {
//                leftExecutorDataMap = populateExecutorDataMap(eventArray, leftComplexProperty, leftExecutorDataMap);
//                rightExecutorDataMap = populateExecutorDataMap(eventArray, rightComplexProperty, rightExecutorDataMap);
//                if (null != checkingClass) {
//                    return executeEvents((T) MVEL.executeExpression(leftComplexProperty.getCompiledExpression(), leftExecutorDataMap, checkingClass), (T) MVEL.executeExpression(rightComplexProperty.getCompiledExpression(), rightExecutorDataMap, checkingClass));
//                } else {
//                    return executeEvents((T) MVEL.executeExpression(leftComplexProperty.getCompiledExpression(), leftExecutorDataMap), (T) MVEL.executeExpression(rightComplexProperty.getCompiledExpression(), rightExecutorDataMap));
//                }
            }
            return false;//todo remove

        } else {
            if (null != leftStreamProperty) {
                if (!checkIsInStream(eventArray, leftStreamProperty.getAttributePositions())) {
                    return false;
                }
                return executeEvents((T) eventArray[leftStreamProperty.getAttributePositions()[0]][getEventPosition(eventArray, leftStreamProperty.getAttributePositions())].getNthAttribute(leftStreamProperty.getAttributePositions()[1]), value);
            } else if (null != rightStreamProperty) {
                if (!checkIsInStream(eventArray, rightStreamProperty.getAttributePositions())) {
                    return false;
                }
                return executeEvents((T) eventArray[rightStreamProperty.getAttributePositions()[0]][getEventPosition(eventArray, rightStreamProperty.getAttributePositions())].getNthAttribute(rightStreamProperty.getAttributePositions()[1]), value);
            } else if (null != leftComplexProperty) {
                leftExecutorDataMap = populateExecutorDataMap(eventArray, leftComplexProperty, leftExecutorDataMap);
                return checkExecuteEventLeftComplex(leftComplexProperty, leftExecutorDataMap);
            } else if (null != rightComplexProperty) {
                rightExecutorDataMap = populateExecutorDataMap(eventArray, rightComplexProperty, rightExecutorDataMap);
                return checkExecuteEventRightComplex(rightComplexProperty, rightExecutorDataMap);
            } else {
                try {
                    throw new SiddhiException("execution does not fall in to any defined category");
                } catch (SiddhiException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
    }

    private boolean checkIsInStream(Event[] eventArray, int[] attributePosition) {
        return checkingStreamNames.contains(eventArray[attributePosition[0]].getEventStreamId());
    }

    private boolean checkIsInStream(Event[][] eventArray, int[] attributePosition) {
        try {
            return checkingStreamNames.contains(eventArray[attributePosition[0]][getEventPosition(eventArray, attributePosition)].getEventStreamId());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean execute(Event event) {
        //    log.debug("Executing event");
        if (!checkingStreamNames.contains(event.getEventStreamId())) {
            return false;
        }
        if (evaluateBothSide) {
            if (null != leftStreamProperty && null != rightStreamProperty) {
                return executeEvents((T) event.getNthAttribute(leftStreamProperty.getAttributePositions()[1]),
                                     (T) event.getNthAttribute(rightStreamProperty.getAttributePositions()[1]));
            } else if (null != leftStreamProperty && null != rightComplexProperty) {
                //todo fix left right issue
                return executeStreamVsComplexProperties(event, leftStreamProperty, rightComplexProperty, rightExecutorDataMap);
            } else if (null != rightStreamProperty && null != leftComplexProperty) {
                return executeStreamVsComplexProperties(event, rightStreamProperty, leftComplexProperty, leftExecutorDataMap);
            } else {
                leftExecutorDataMap = populateExecutorDataMap(event, leftComplexProperty, leftExecutorDataMap);
                rightExecutorDataMap = populateExecutorDataMap(event, rightComplexProperty, rightExecutorDataMap);
                if (null != checkingClass) {
                    return executeEvents((T) MVEL.executeExpression(leftComplexProperty.getCompiledExpression(), leftExecutorDataMap, checkingClass), (T) MVEL.executeExpression(rightComplexProperty.getCompiledExpression(), rightExecutorDataMap, checkingClass));
                } else {
                    return executeEvents((T) MVEL.executeExpression(leftComplexProperty.getCompiledExpression(), leftExecutorDataMap), (T) MVEL.executeExpression(rightComplexProperty.getCompiledExpression(), rightExecutorDataMap));
                }
            }
        } else {
            if (null != leftStreamProperty) {
                return executeEvents((T) event.getNthAttribute(leftStreamProperty.getAttributePositions()[1]), value);
            } else if (null != rightStreamProperty) {
                return executeEvents((T) event.getNthAttribute(rightStreamProperty.getAttributePositions()[1]), value);
            } else if (null != leftComplexProperty) {
                leftExecutorDataMap = populateExecutorDataMap(event, leftComplexProperty, leftExecutorDataMap);
                return checkExecuteEventLeftComplex(leftComplexProperty, leftExecutorDataMap);
            } else if (null != rightComplexProperty) {
                rightExecutorDataMap = populateExecutorDataMap(event, rightComplexProperty, rightExecutorDataMap);
                return checkExecuteEventRightComplex(rightComplexProperty, rightExecutorDataMap);
            } else {
                try {
                    throw new SiddhiException("execution does not fall in to any defined category");
                } catch (SiddhiException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
    }


    private boolean checkExecuteEventLeftComplex(ComplexProperty complexProperty,
                                                 Map executorDataMap) {
        if (null != checkingClass) {
            return executeEvents((T) MVEL.executeExpression(complexProperty.getCompiledExpression(), executorDataMap, checkingClass), value);
        } else {
            return executeEvents((T) MVEL.executeExpression(complexProperty.getCompiledExpression(), executorDataMap), value);
        }
    }

    private boolean checkExecuteEventRightComplex(ComplexProperty complexProperty,
                                                  Map executorDataMap) {
        if (null != checkingClass) {
            return executeEvents(value, (T) MVEL.executeExpression(complexProperty.getCompiledExpression(), executorDataMap, checkingClass));
        } else {
            return executeEvents(value, (T) MVEL.executeExpression(complexProperty.getCompiledExpression(), executorDataMap));
        }
    }

    private boolean executeStreamVsComplexProperties(Event event, StreamProperty streamProperty,
                                                     ComplexProperty complexProperty,
                                                     Map executorDataMap) {
        executorDataMap = populateExecutorDataMap(event, complexProperty, executorDataMap);
        Object obj = executeExpression(complexProperty, executorDataMap);
        return executeEvents((T) event.getNthAttribute(streamProperty.getAttributePositions()[1]), (T) obj);
    }

    private boolean executeStreamVsComplexProperties(Event[] eventArray,
                                                     StreamProperty streamProperty,
                                                     ComplexProperty complexProperty,
                                                     Map executorDataMap) {
        if (!checkIsInStream(eventArray, streamProperty.getAttributePositions())) {
            return false;
        }
        for (int[] position : complexProperty.getExpressionPropertyPositionMap().values()) {
            if (!checkIsInStream(eventArray, position)) {
                return false;
            }
        }
        executorDataMap = populateExecutorDataMap(eventArray, complexProperty, executorDataMap);
        Object obj = executeExpression(complexProperty, executorDataMap);
        return executeEvents((T) eventArray[streamProperty.getAttributePositions()[0]].getNthAttribute(streamProperty.getAttributePositions()[1]), (T) obj);
    }

    private Object executeExpression(ComplexProperty complexProperty, Map executorDataMap) {
        Object obj;
        if (null != checkingClass) {
            obj = MVEL.executeExpression(complexProperty.getCompiledExpression(), executorDataMap, checkingClass);
        } else {
            obj = MVEL.executeExpression(complexProperty.getCompiledExpression(), executorDataMap);
        }
        return obj;
    }

    private static Map populateExecutorDataMap(Event event, ComplexProperty complexProperty,
                                               Map executorDataMap) {
        for (String expressingName : complexProperty.getExpressionPropertyPositionMap().keySet()) {
            executorDataMap.put(expressingName, event.<Object>getNthAttribute(complexProperty.getExpressionPropertyPositionMap().get(expressingName)[1]));
        }
        return executorDataMap;
    }

    private static Map populateExecutorDataMap(Event[] eventArray, ComplexProperty complexProperty,
                                               Map executorDataMap) {
        for (String expressingName : complexProperty.getExpressionPropertyPositionMap().keySet()) {
            executorDataMap.put(expressingName, eventArray[complexProperty.getExpressionPropertyPositionMap().get(expressingName)[0]].<Object>getNthAttribute(complexProperty.getExpressionPropertyPositionMap().get(expressingName)[1]));
        }
        return executorDataMap;
    }

    private Map populateExecutorDataMap(Event[][] eventArray, ComplexProperty complexProperty,
                                        Map executorDataMap) {
        for (String expressingName : complexProperty.getExpressionPropertyPositionMap().keySet()) {
            executorDataMap.put(expressingName, eventArray[complexProperty.getExpressionPropertyPositionMap().get(expressingName)[0]][getEventPosition(eventArray, complexProperty.getExpressionPropertyPositionMap().get(expressingName))].<Object>getNthAttribute(complexProperty.getExpressionPropertyPositionMap().get(expressingName)[1]));
        }
        return executorDataMap;
    }

    private static int getEventPosition(Event[][] eventArray, int[] attributePosition) {
        int attributeEventPosition = 0;

        if (attributePosition.length == 3) {
            attributeEventPosition = attributePosition[2];

            if (attributePosition[2] == 0) {
                attributeEventPosition = 0;
            } else if (attributePosition[2] == 2) {
                attributeEventPosition = 2;
            } else {
                try {
                    if (eventArray[attributePosition[0]][1] == null) {
                        attributeEventPosition = 0;
                    } else {
                        attributeEventPosition = 1;
                    }
                } catch (Exception e) {
                    attributeEventPosition = 0;
                }
            }

        } else {
            try {
                if (eventArray[attributePosition[0]][1] == null) {
                    attributeEventPosition = 0;
                } else {
                    attributeEventPosition = 1;
                }
            } catch (Exception e) {
                attributeEventPosition = 0;
            }
        }
        return attributeEventPosition;
    }

    protected boolean executeEvents(T left, T right) {
        if (null == left || null == right) {
            return false;
        } else {
            return executeEventsLogic(left, right);
        }
    }

    protected abstract boolean executeEventsLogic(T left, T right);


}
