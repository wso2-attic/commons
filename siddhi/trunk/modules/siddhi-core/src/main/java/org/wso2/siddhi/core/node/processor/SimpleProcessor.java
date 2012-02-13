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

package org.wso2.siddhi.core.node.processor;

import org.apache.log4j.Logger;
import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.eventstream.query.SimpleQuery;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.event.generator.EventGeneratorImpl;
import org.wso2.siddhi.core.eventstream.handler.InputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.window.AbstractWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.queue.EventQueue;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.InvalidAttributeCastException;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.PropertyFormatException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.exception.UndefinedPropertyException;
import org.wso2.siddhi.core.node.processor.aggregator.CountDataItem;
import org.wso2.siddhi.core.node.processor.aggregator.DataItem;
import org.wso2.siddhi.core.node.processor.aggregator.GeneralDataItem;
import org.wso2.siddhi.core.node.processor.aggregator.WindowedDataItem;
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
import org.wso2.siddhi.core.node.processor.eventmap.AggregatorMapObj;
import org.wso2.siddhi.core.node.processor.eventmap.DefaultMapObj;
import org.wso2.siddhi.core.node.processor.eventmap.MapObj;
import org.wso2.siddhi.core.node.processor.eventmap.StreamMapObj;
import org.wso2.siddhi.core.node.processor.executor.Executor;
import org.wso2.siddhi.core.parser.ConditionParser;
import org.wso2.siddhi.core.parser.QueryInputStreamParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Query processor
 */
public class SimpleProcessor extends AbstractProcessor {

    private static final Logger log = Logger.getLogger(SimpleProcessor.class);

    private SimpleQuery query;
    private Executor executor;
    private Executor havingExecutor;
    private List<MapObj> outPutEventGenMapList;
    private EventGenerator eventGenerator;
    private List<StreamMapObj> dataItemsInputMapList;                 //hold the mapping of Event attributes to groupsToDataItemMap
    private Map<String, List<DataItem>> groupsToDataItemMap = null;   // hold Aggregators
    private int[] groupByConditionPositions = new int[0];

    private int dataItemsInputMapListSize = -1;
    private int outPutEventGenMapListSize;

    public SimpleProcessor(SimpleQuery query)
            throws InvalidQueryInputStreamException, ProcessorInitializationException,
                   SiddhiException {
        this.query = query;
        assignQueryInputStream(query.getQueryInputStream());
        init();
    }

    @Override
    public String getStreamId() {
        return query.getStreamId();

    }

    @Override
    public void assignQueryInputStream(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        InputStreamHandler inputStreamHandler = QueryInputStreamParser.parse(queryInputStream);
        inputEventStream.assignInputStreamHandler(inputStreamHandler);
        if (dataItemsInputMapListSize > 0) {
            if (inputStreamHandler instanceof AbstractWindowQueryInputStreamHandler) {
                EventQueue eventQueue = ((AbstractWindowQueryInputStreamHandler) inputStreamHandler).getWindow();
                for (List<DataItem> dataItems : groupsToDataItemMap.values()) {
                    for (DataItem dataItem : dataItems) {
                        if (dataItem instanceof WindowedDataItem) {
                            ((WindowedDataItem) dataItem).setWindow(eventQueue);
                        }
                    }
                }
            }
        }
    }

    /**
     * Initialize the processor.
     */
    public void init() throws ProcessorInitializationException, SiddhiException {
        try {
            //Query
            Condition condition = query.getCondition();
            EventStream eventStream = query.getQueryInputStream().getEventStream();

            if (query.hasHaving()) {
                Condition havingCondition = query.getHavingCondition();
                ConditionParser havingConditionParser = new ConditionParser(havingCondition, query);
                havingExecutor = havingConditionParser.getExecutor();
            }

            ConditionParser conditionParser = new ConditionParser(condition, eventStream);
            executor = conditionParser.getExecutor();


            //output
            eventGenerator = new EventGeneratorImpl(query.getStreamId(), query.getNames(), query.getTypes());
            List<String> outputDefinitionList = query.getOutputDefinition().getPropertyList();
            outPutEventGenMapList = new ArrayList<MapObj>();

            if ("*".equals(outputDefinitionList.get(0))) { // If * (star) is given in the SELECT
                String[] attrNames = eventStream.getNames();
                outputDefinitionList.remove(0);
                for (String attName : attrNames) {
                    outputDefinitionList.add(eventStream.getStreamId() + "." + attName);    //streamId.attrName
                }
            }

            for (String aProperty : outputDefinitionList) {

                /* There are two types of mapObj. For a aggregator - >
                 we put SIDDHI_AGGREGATOR as streamId and use index of the groupsToDataItemMap for the position
                 For simple property -> we put actual streamId and position from the event
                */
                MapObj mapObj = null;
                String property = aProperty.split("=")[1];

                if (property.contains("(")) { // has a aggregator      Ex. Avg(eventstream.price)

                    String[] propertyAttrs = property.split("\\(");
                    if ("".equals(propertyAttrs[0].toUpperCase())) {
                        String valueString = propertyAttrs[1].split("\\)")[1];
                        Class classType = query.getTypeForName(aProperty.split("=")[0]);
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

                    } else {
                        String propertyAggregator = propertyAttrs[0].toUpperCase();
                        // Create groupsToDataItemMap for Aggregators
                        if (groupsToDataItemMap == null) {

                            //initialize dataItem collections
                            groupsToDataItemMap = new HashMap<String, List<DataItem>>();
                            dataItemsInputMapList = new ArrayList<StreamMapObj>();

                            //initialize groupBy conditions
                            if (query.hasGroupBy()) {
                                String[] groupByNames = query.getGroupBy();
                                groupByConditionPositions = new int[groupByNames.length];
                                for (int i = 0; i < groupByNames.length; i++) {
                                    String groupByAttribute = groupByNames[i].split("\\.")[1];   //Ex. price
                                    groupByConditionPositions[i] = eventStream.getAttributePositionForName(groupByAttribute);
                                }
                            }
                            groupsToDataItemMap.put(null, new ArrayList<DataItem>());
                        }

                        String propertyStreamInformation = propertyAttrs[1].replace(")", "");   //Ex. eventstream.price) -> eventstream.price

                        String[] propertyStreamInformationAttrs = propertyStreamInformation.split("\\.");
                        String streamId = propertyStreamInformationAttrs[0];                    //Ex. eventstream
                        String propertyAttribute = propertyStreamInformationAttrs[1];           //Ex. price

                        Class<?> classType = getType(streamId, propertyAttribute);              // get the type of the eventstream.price

                        int propertyPosition = eventStream.getAttributePositionForName(propertyAttribute);
                        StreamMapObj dataItemMapObj = new StreamMapObj(streamId, propertyPosition);
                        dataItemsInputMapList.add(dataItemMapObj);
                        groupsToDataItemMap.get(null).add(buildDataItem(propertyAggregator, classType, propertyPosition, eventStream.getStreamId()));
                        mapObj = new AggregatorMapObj(dataItemsInputMapList.size() - 1);   //For aggregator property we select SIDDHI_AGGREGATOR as the streamId.
                    }
                } else { // without a aggregator             Ex. eventstream.price
                    String streamId = property.split("\\.")[0];            //  Ex. eventstream
                    String propertyAttribute = property.split("\\.")[1];   // Ex. price
                    mapObj = new StreamMapObj(streamId, eventStream.getAttributePositionForName(propertyAttribute));
                }
                outPutEventGenMapList.add(mapObj);
            }
        } catch (UndefinedPropertyException ex) {
            log.warn(ex.getMessage());
            throw new ProcessorInitializationException("UndefinedPropertyException occurred " + ex.getMessage());
        } catch (InvalidAttributeCastException ex) {
            log.warn(ex.getMessage());
            throw new ProcessorInitializationException("InvalidAttributeCastException occurred " + ex.getMessage());
        } catch (InvalidQueryException ex) {
            log.warn(ex.getMessage());
            throw new ProcessorInitializationException("InvalidQueryException occurred " + ex.getMessage());
        } catch (PropertyFormatException ex) {
            log.warn(ex.getMessage());
            throw new ProcessorInitializationException("PropertyFormatException occurred " + ex.getMessage());
        } catch (InvalidEventStreamIdException ex) {
            throw new ProcessorInitializationException("InvalidEventStreamId when fetching the window from the InputEventStream, " + ex.getMessage());

        }
        if (dataItemsInputMapList != null) {
            dataItemsInputMapListSize = dataItemsInputMapList.size();
        }
        outPutEventGenMapListSize = outPutEventGenMapList.size();
    }

    private DataItem buildDataItem(String propertyAggregator, Class<?> classType,
                                   int propertyPosition, String streamId)
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
                dataItem = new MinDataItemInteger(getInputEventStream().getWindow(streamId), propertyPosition);
            } else if (classType.equals(Float.class)) {
                dataItem = new MinDataItemFloat(getInputEventStream().getWindow(streamId), propertyPosition);
            } else if (classType.equals(Double.class)) {
                dataItem = new MinDataItemDouble(getInputEventStream().getWindow(streamId), propertyPosition);
            } else if (classType.equals(Long.class)) {
                dataItem = new MinDataItemLong(getInputEventStream().getWindow(streamId), propertyPosition);
            }

        } else if (propertyAggregator.equals("MAX")) {

            if (classType.equals(Integer.class)) {
                dataItem = new MaxDataItemInteger(getInputEventStream().getWindow(streamId), propertyPosition);
            } else if (classType.equals(Float.class)) {
                dataItem = new MaxDataItemFloat(getInputEventStream().getWindow(streamId), propertyPosition);
            } else if (classType.equals(Double.class)) {
                dataItem = new MaxDataItemDouble(getInputEventStream().getWindow(streamId), propertyPosition);
            } else if (classType.equals(Long.class)) {
                dataItem = new MaxDataItemLong(getInputEventStream().getWindow(streamId), propertyPosition);
            }

        } else if (propertyAggregator.equals("COUNT")) {
            dataItem = new CountDataItemLong();
        }
        return dataItem;
    }

    public void run() {
        while (true) {
            try {
                Event event = inputEventStream.takeEvent();
                String eventStreamId = event.getEventStreamId();
                if (eventStreamId.equals(SiddhiManager.POISON_PILL)) {
                    if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL) {
                        outputEventStream.put(event);
                        break;
                    } else if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                        break;
                    }
                }

                //If match found after executing the 'conditions' do the following
                if (executor.execute(event)) {
                    String groupByConditionKey = null;
                    if (dataItemsInputMapListSize > 0) {
                        StringBuffer groupByConditionKeyBuf = new StringBuffer();
                        for (int position : groupByConditionPositions) {
                            groupByConditionKeyBuf.append(event.getNthAttribute(position)).
                                    append(":");
                        }
                        groupByConditionKey = groupByConditionKeyBuf.toString();
                        List<DataItem> dataItemList = groupsToDataItemMap.get(groupByConditionKey);
                        if (dataItemList == null) {
                            dataItemList = new ArrayList<DataItem>();
                            for (DataItem dataItem : groupsToDataItemMap.get(null)) {
                                dataItemList.add(dataItem.getNewInstance());
                            }
                            groupsToDataItemMap.put(groupByConditionKey, dataItemList);
                        }
                        for (int i = 0; i < dataItemsInputMapListSize; i++) {
                            if (eventStreamId.equals(dataItemsInputMapList.get(i).getStreamId())) {
                                DataItem dataItem = dataItemList.get(i);
                                if (event.isNew()) {
                                    try {
                                        if (dataItem instanceof GeneralDataItem) {
                                            ((GeneralDataItem) dataItem).add(
                                                    event.getNthAttribute(dataItemsInputMapList.get(i).getPosition()));

                                        } else if (dataItem instanceof CountDataItem) {
                                            ((CountDataItemLong) dataItem).add(
                                                    event.getNthAttribute(dataItemsInputMapList.get(i).getPosition()));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                } else {

                                    try {
                                        if (dataItem instanceof GeneralDataItem) {
                                            ((GeneralDataItem) dataItem).remove(
                                                    event.getNthAttribute(dataItemsInputMapList.get(i).getPosition()));

                                        } else if (dataItem instanceof CountDataItem) {
                                            ((CountDataItemLong) dataItem).remove(
                                                    event.getNthAttribute(dataItemsInputMapList.get(i).getPosition()));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    Object[] obj = new Object[outPutEventGenMapListSize];

                    for (int i = 0; i < outPutEventGenMapListSize; i++) {
                        MapObj aMapObj = outPutEventGenMapList.get(i);

                        if (aMapObj instanceof AggregatorMapObj) {   // MapObj points to a aggregator
                            obj[i] = groupsToDataItemMap.get(groupByConditionKey).get(aMapObj.getPosition()).getValue();

                        } else if (aMapObj instanceof DefaultMapObj) {   // MapObj points to a aggregator
                            obj[i] = ((DefaultMapObj) aMapObj).getValue();

                        } else if (eventStreamId.equals(((StreamMapObj) aMapObj).getStreamId())) {  // MapObj is not point to a event attribute
                            obj[i] = event.getNthAttribute(aMapObj.getPosition());
                        }

                    }
                    Event generatedEvent;
                    if (event.isNew()) {
                        generatedEvent = eventGenerator.createEvent(obj);
                    } else {
                        generatedEvent = eventGenerator.createExpiredEvent(obj);
                    }
                    if (!query.hasHaving()) {
                        outputEventStream.put(generatedEvent);
                    } else {
                        if (havingExecutor.execute(generatedEvent)) {
                            outputEventStream.put(generatedEvent);
                        }
                    }


                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug(this.getClass().getSimpleName() + " ended");

    }

    private Class getType(String streamId, String propertyAttribute) {

        if (query.getQueryInputStream().getEventStream().getStreamId().equals(streamId)) {
            return query.getQueryInputStream().getEventStream().getTypeForName(propertyAttribute);
        }
        return null;
    }

    public Query getQuery() {
        return query;
    }
}
