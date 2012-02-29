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

import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.eventstream.InputEventStream;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.node.processor.aggregator.DataItem;
import org.wso2.siddhi.core.node.processor.aggregator.avg.AvgDataItemDouble;
import org.wso2.siddhi.core.node.processor.aggregator.avg.AvgDataItemFloat;
import org.wso2.siddhi.core.node.processor.aggregator.avg.AvgDataItemInteger;
import org.wso2.siddhi.core.node.processor.aggregator.avg.AvgDataItemLong;
import org.wso2.siddhi.core.node.processor.aggregator.count.CountDataItemLong;
import org.wso2.siddhi.core.node.processor.aggregator.max.MaxDataItemDouble;
import org.wso2.siddhi.core.node.processor.aggregator.max.MaxDataItemFloat;
import org.wso2.siddhi.core.node.processor.aggregator.max.MaxDataItemInteger;
import org.wso2.siddhi.core.node.processor.aggregator.max.MaxDataItemLong;
import org.wso2.siddhi.core.node.processor.aggregator.min.MinDataItemDouble;
import org.wso2.siddhi.core.node.processor.aggregator.min.MinDataItemFloat;
import org.wso2.siddhi.core.node.processor.aggregator.min.MinDataItemInteger;
import org.wso2.siddhi.core.node.processor.aggregator.min.MinDataItemLong;
import org.wso2.siddhi.core.node.processor.aggregator.sum.SumDataItemDouble;
import org.wso2.siddhi.core.node.processor.aggregator.sum.SumDataItemFloat;
import org.wso2.siddhi.core.node.processor.aggregator.sum.SumDataItemInteger;
import org.wso2.siddhi.core.node.processor.aggregator.sum.SumDataItemLong;
import org.wso2.siddhi.core.node.processor.eventmap.DefaultMapObj;
import org.wso2.siddhi.core.node.processor.eventmap.MapObj;
import org.wso2.siddhi.core.node.processor.eventmap.StreamMapObj;

import java.util.List;

/**
 * Util to help processor
 */
public class ProcessorHelper {

    /**
     * removes the * and adds a list of streamId.attrName as the output attributes
     *
     * @param outputAttributeList current output definition list
     * @param inputEventStreams   all input event streams
     * @return output attribute list
     */
    public static List<String> generateOutputPropertyList(List<String> outputAttributeList,
                                                          EventStream[] inputEventStreams) {
        if (outputAttributeList != null && outputAttributeList.size() == 1 && "*".equals(outputAttributeList.get(0))) { // If * (star) is given in the SELECT
            outputAttributeList.remove(0);
            if (inputEventStreams.length == 1) {
                EventStream inputEventStream = inputEventStreams[0];
                String[] attrNames = inputEventStream.getNames();
                for (String attName : attrNames) {
                    outputAttributeList.add(inputEventStream.getStreamId() + "." + attName);    //streamId.attrName
                }

            } else {
                for (EventStream inputEventStream : inputEventStreams) {
                    String[] attrNames = inputEventStream.getNames();
                    for (String attName : attrNames) {
                        outputAttributeList.add(inputEventStream.getStreamId() + "_" + attName + "=" +
                                                inputEventStream.getStreamId() + "." + attName);    //streamId_attrName=streamId.attrName
                    }
                }
            }
        }
        return outputAttributeList;
    }

    public static DataItem buildDataItem(String propertyAggregator, Class<?> classType,
                                   int propertyPosition, String streamId, InputEventStream inputEventStream)
            throws InvalidEventStreamIdException {
        DataItem dataItem = null;
        if (propertyAggregator.equals("AVG")) { // For Avg
            if (classType.equals(Integer.class)) {
                dataItem = new AvgDataItemInteger();
            } else if (classType.equals(Float.class)) {
                dataItem = new AvgDataItemFloat();
            } else if (classType.equals(Double.class)) {
                dataItem = new AvgDataItemDouble();
            } else if (classType.equals(Long.class)) {
                dataItem = new AvgDataItemLong();
            }

        } else if (propertyAggregator.equals("SUM")) {
            if (classType.equals(Integer.class)) {
                dataItem = new SumDataItemInteger();
            } else if (classType.equals(Float.class)) {
                dataItem = new SumDataItemFloat();
            } else if (classType.equals(Double.class)) {
                dataItem = new SumDataItemDouble();
            } else if (classType.equals(Long.class)) {
                dataItem = new SumDataItemLong();
            }
        } else if (propertyAggregator.equals("MIN")) {

            if (classType.equals(Integer.class)) {
                dataItem = new MinDataItemInteger(inputEventStream.getWindow(streamId), propertyPosition);
            } else if (classType.equals(Float.class)) {
                dataItem = new MinDataItemFloat(inputEventStream.getWindow(streamId), propertyPosition);
            } else if (classType.equals(Double.class)) {
                dataItem = new MinDataItemDouble(inputEventStream.getWindow(streamId), propertyPosition);
            } else if (classType.equals(Long.class)) {
                dataItem = new MinDataItemLong(inputEventStream.getWindow(streamId), propertyPosition);
            }

        } else if (propertyAggregator.equals("MAX")) {

            if (classType.equals(Integer.class)) {
                dataItem = new MaxDataItemInteger(inputEventStream.getWindow(streamId), propertyPosition);
            } else if (classType.equals(Float.class)) {
                dataItem = new MaxDataItemFloat(inputEventStream.getWindow(streamId), propertyPosition);
            } else if (classType.equals(Double.class)) {
                dataItem = new MaxDataItemDouble(inputEventStream.getWindow(streamId), propertyPosition);
            } else if (classType.equals(Long.class)) {
                dataItem = new MaxDataItemLong(inputEventStream.getWindow(streamId), propertyPosition);
            }

        } else if (propertyAggregator.equals("COUNT")) {
            dataItem = new CountDataItemLong();
        }
        return dataItem;
    }

    public static MapObj createDefaultMapObj(String valueString, Class classType) {
        MapObj mapObj;
        Object value;
        if (Integer.class == classType) {
            value = Integer.valueOf(valueString);
        } else if (Float.class == classType) {
            value = Float.valueOf(valueString);

        } else if (Double.class == classType) {
            value = Double.valueOf(valueString);

        } else if (Long.class == classType) {
            value = Long.valueOf(valueString);

        } else {
            value = valueString;
        }
        mapObj = new DefaultMapObj(value);
        return mapObj;
    }

    public static MapObj createStreamMapObj(Query query, String property) {
        MapObj mapObj;
        String streamId = property.split("\\.")[0];            //  Ex. eventstream
        String propertyAttribute = property.split("\\.")[1];   // Ex. price
        mapObj = new StreamMapObj(query.getInputEventStreamPosition(streamId),
                                  query.getInputEventStream(streamId).getAttributePositionForName(propertyAttribute));
        return mapObj;
    }
}
