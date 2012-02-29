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
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.InputEventStream;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.node.processor.aggregator.CountDataItem;
import org.wso2.siddhi.core.node.processor.aggregator.DataItem;
import org.wso2.siddhi.core.node.processor.aggregator.GeneralDataItem;
import org.wso2.siddhi.core.node.processor.aggregator.count.CountDataItemLong;
import org.wso2.siddhi.core.node.processor.eventmap.AggregatorMapObj;
import org.wso2.siddhi.core.node.processor.eventmap.MapObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all aggregations like avg,sum,min,max,count according to grouping conditions
 */
public class AggregationHandler {
    Map<String, List<DataItem>> dataItemsGroupingMap = new HashMap<String, List<DataItem>>();
    int[][] groupingPropertyPositions = null;
    int dataItemsListSize = 0;

    public AggregationHandler(Query query) {
        dataItemsGroupingMap.put(null, new ArrayList<DataItem>());
        if (query.hasGroupBy()) {
            String[] groupByNames = query.getGroupBy();
            groupingPropertyPositions = new int[groupByNames.length][2];
            for (int i = 0; i < groupByNames.length; i++) {
                String streamId = groupByNames[i].split("\\.")[0];   //Ex. streamId
                String groupByAttribute = groupByNames[i].split("\\.")[1];   //Ex. price
                EventStream eventStream = query.getInputEventStream(streamId);
                groupingPropertyPositions[i][1] = eventStream.getAttributePositionForName(groupByAttribute);
                groupingPropertyPositions[i][0] = query.getInputEventStreamPosition(streamId);
            }
        }
    }

    public MapObj createAggregatorDataMapObj(String propertyAggregator, String property,
                                             Query query, InputEventStream inputEventStream)
            throws InvalidEventStreamIdException {

        String[] propertyStreamInformationAttrs = property.split("\\.");
        String streamId = propertyStreamInformationAttrs[0];                    //Ex. eventstream
        String propertyAttribute = propertyStreamInformationAttrs[1];           //Ex. price

        EventStream eventStream = query.getInputEventStream(streamId);
        Class<?> classType = eventStream.getTypeForName(propertyAttribute);   // get the type of the eventstream.price
        int propertyPosition = eventStream.getAttributePositionForName(propertyAttribute);

        dataItemsGroupingMap.get(null).add(ProcessorHelper.buildDataItem(propertyAggregator.toUpperCase(), classType, propertyPosition, eventStream.getStreamId(), inputEventStream));
        dataItemsListSize = dataItemsGroupingMap.size();
        return new AggregatorMapObj(query.getInputEventStreamPosition(streamId), propertyPosition, dataItemsGroupingMap.get(null).size() - 1);
    }


    public Object handleAggregation(AggregatorMapObj aggregatorMapObj, Event event) {
        String groupByConditionKey = null;
        if (groupingPropertyPositions != null) {
            StringBuilder groupByConditionKeyBuf = new StringBuilder();
            for (int[] position : groupingPropertyPositions) {
                groupByConditionKeyBuf.append(event.getNthAttribute(position[1])).
                        append(":");
            }
            groupByConditionKey = groupByConditionKeyBuf.toString();
        }
        return updateDateItem(aggregatorMapObj, groupByConditionKey, event, event.isNew());
    }

    public Object handleAggregation(AggregatorMapObj aggregatorMapObj, Event leftEvent,
                                    Event rightEvent, int streamPosition) {
        String groupByConditionKey = null;
        if (groupingPropertyPositions != null) {
            StringBuilder groupByConditionKeyBuf = new StringBuilder();
            for (int[] position : groupingPropertyPositions) {
                if (position[0] == 0) {
                    groupByConditionKeyBuf.append(leftEvent.getNthAttribute(position[1])).append(":");
                } else {
                    groupByConditionKeyBuf.append(rightEvent.getNthAttribute(position[1])).append(":");
                }
            }
            groupByConditionKey = groupByConditionKeyBuf.toString();
        }
        if (streamPosition == 0) {
            return updateDateItem(aggregatorMapObj, groupByConditionKey, leftEvent, leftEvent.isNew() || rightEvent.isNew());
        } else {
            return updateDateItem(aggregatorMapObj, groupByConditionKey, rightEvent, leftEvent.isNew() || rightEvent.isNew());
        }
    }

    private List<DataItem> getDataItemList(String groupByConditionKey) {
        List<DataItem> dataItemList = dataItemsGroupingMap.get(groupByConditionKey);
        if (dataItemList == null) {
            dataItemList = new ArrayList<DataItem>(dataItemsGroupingMap.size());
            for (DataItem dataItem:dataItemsGroupingMap.get(null)){
                dataItemList.add(dataItem.getNewInstance());
            }
            dataItemsGroupingMap.put(groupByConditionKey, dataItemList);
        }
        return dataItemList;
    }

    private Object updateDateItem(AggregatorMapObj aggregatorMapObj, String groupByConditionKey,
                                  Event event, boolean isNew) {
        DataItem dataItem = getDataItemList(groupByConditionKey).get(aggregatorMapObj.getDataItemPosition());
        if (isNew) {
            if (dataItem instanceof GeneralDataItem) {
                ((GeneralDataItem) dataItem).add(
                        event.getNthAttribute(aggregatorMapObj.getAttributePosition()));

            } else if (dataItem instanceof CountDataItem) {
                ((CountDataItemLong) dataItem).add(
                        event.getNthAttribute(aggregatorMapObj.getAttributePosition()));
            }
        } else {
            if (dataItem instanceof GeneralDataItem) {
                ((GeneralDataItem) dataItem).remove(
                        event.getNthAttribute(aggregatorMapObj.getAttributePosition()));
            } else if (dataItem instanceof CountDataItem) {
                ((CountDataItemLong) dataItem).remove(
                        event.getNthAttribute(aggregatorMapObj.getAttributePosition()));
            }
        }
        return dataItem.getValue();
    }
}
