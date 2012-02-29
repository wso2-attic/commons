/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.core.node.processor.utils;

import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.event.generator.EventGeneratorImpl;
import org.wso2.siddhi.core.eventstream.InputEventStream;
import org.wso2.siddhi.core.exception.InvalidAttributeCastException;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.PropertyFormatException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.exception.UndefinedPropertyException;
import org.wso2.siddhi.core.node.processor.eventmap.AggregatorMapObj;
import org.wso2.siddhi.core.node.processor.eventmap.DefaultMapObj;
import org.wso2.siddhi.core.node.processor.eventmap.MapObj;
import org.wso2.siddhi.core.node.processor.eventmap.StreamMapObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate the outputs and checks for having case
 */
public class OutputGenerator {

    private HavingHandler havingHandler = null;
    private List<String> outputPropertyList;
    private AggregationHandler aggregationHandler = null;
    private EventGenerator eventGenerator;
    private List<MapObj> outPutEventGenMapList;
    private int outputPropertySize;

    public OutputGenerator(Query query, InputEventStream inputEventStream)
            throws InvalidQueryException, PropertyFormatException, UndefinedPropertyException,
                   SiddhiException, InvalidAttributeCastException, InvalidEventStreamIdException {
        outputPropertyList = ProcessorHelper.
                generateOutputPropertyList(query.getOutputDefinition().getPropertyList(),
                                           query.getInputEventStreams());

        havingHandler = HavingHandler.assignHandler(query);
        aggregationHandler = new AggregationHandler(query);

        outPutEventGenMapList = createOutputEventGenMapList(query, inputEventStream);
        outputPropertySize = outPutEventGenMapList.size();
        eventGenerator = new EventGeneratorImpl(query.getStreamId(), query.getNames(), query.getTypes());
    }


    private List<MapObj> createOutputEventGenMapList(Query query, InputEventStream inputEventStream)
            throws InvalidEventStreamIdException {
        List<MapObj> outPutEventGenMapList = new ArrayList<MapObj>();

        for (String aProperty : outputPropertyList) {

            MapObj mapObj;
            String property = aProperty.split("=")[1];

            if (property.contains("(")) {

                String[] propertyAttrs = property.split("\\(");
                if ("".equals(propertyAttrs[0].toUpperCase())) {   // has a default      Ex. (int)75
                    mapObj = ProcessorHelper.createDefaultMapObj(propertyAttrs[1].split("\\)")[1], query.getTypeForName(aProperty.split("=")[0]));

                } else {    // has a aggregator      Ex. Avg(eventstream.price)
                    mapObj = aggregationHandler.createAggregatorDataMapObj(propertyAttrs[0], propertyAttrs[1].replace(")", "")/* Ex. eventstream.price) -> eventstream.price*/,
                                                                           query, inputEventStream);
                }
            } else { // without a aggregator             Ex. eventstream.price
                mapObj = ProcessorHelper.createStreamMapObj(query, property);
            }
            outPutEventGenMapList.add(mapObj);
        }
        return outPutEventGenMapList;
    }


    public Event generateOutput(Event event) {
        Object[] obj = new Object[outputPropertySize];

        for (int i = 0; i < outputPropertySize; i++) {
            MapObj aMapObj = outPutEventGenMapList.get(i);
            if (aMapObj instanceof StreamMapObj) {
                obj[i] = event.getNthAttribute(aMapObj.getAttributePosition());
            } else if (aMapObj instanceof AggregatorMapObj) {   // MapObj points to a aggregator
                obj[i] = aggregationHandler.handleAggregation(((AggregatorMapObj) aMapObj), event);
            } else if (aMapObj instanceof DefaultMapObj) {
                obj[i] = ((DefaultMapObj) aMapObj).getValue();
            }
        }
        Event generatedEvent;
        if (event.isNew()) {
            generatedEvent = eventGenerator.createEvent(obj);
        } else {
            generatedEvent = eventGenerator.createExpiredEvent(obj);
        }
        return checkHavingCondition(generatedEvent);
    }

    public Event generateOutput(Event leftEvent, Event rightEvent) {
        Object[] obj = new Object[outputPropertySize];
        for (int i = 0; i < outputPropertySize; i++) {
            MapObj aMapObj = outPutEventGenMapList.get(i);

            if (aMapObj instanceof StreamMapObj) {
                if (aMapObj.getStreamPosition() == 0) {
                    obj[i] = leftEvent.getNthAttribute(aMapObj.getAttributePosition());
                } else {
                    obj[i] = rightEvent.getNthAttribute(aMapObj.getAttributePosition());
                }
            } else if (aMapObj instanceof AggregatorMapObj) {   // MapObj points to a aggregator
                obj[i] = aggregationHandler.handleAggregation(((AggregatorMapObj) aMapObj), leftEvent, rightEvent, aMapObj.getStreamPosition());
            } else if (aMapObj instanceof DefaultMapObj) {
                obj[i] = ((DefaultMapObj) aMapObj).getValue();
            }
        }
        Event generatedEvent;
        if (leftEvent.isNew() || rightEvent.isNew()) {
            generatedEvent = eventGenerator.createEvent(obj);
        } else {
            generatedEvent = eventGenerator.createExpiredEvent(obj);
        }
        return checkHavingCondition(generatedEvent);
    }

    private Event checkHavingCondition(Event generatedEvent) {
        if (havingHandler == null || havingHandler.successHavingCondition(generatedEvent)) {
            return generatedEvent;
        } else {
            return null;
        }
    }
}
