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
package org.wso2.siddhi.core.query.projector;

import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.event.remove.RemoveStream;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.query.projector.attibute.generator.AbstractAggregateAttributeGenerator;
import org.wso2.siddhi.core.query.projector.attibute.generator.SimpleOutputAttributeGenerator;
import org.wso2.siddhi.core.query.projector.attibute.generator.groupby.GroupByOutputAttributeGenerator;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.util.parser.AggregatorParser;
import org.wso2.siddhi.core.util.parser.ExecutorParser;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.query.output.OutStream;
import org.wso2.siddhi.query.api.query.projection.Projector;
import org.wso2.siddhi.query.api.query.projection.attribute.AggregationAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.OutputAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.SimpleAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class QueryProjector {

    //    private List<Object[]> dataList = new ArrayList<Object[]>();
    private List<OutputAttributeGenerator> outputAttributeGeneratorList;
    private List<OutputAttributeGenerator> aggregateOutputAttributeGeneratorList;
    private int outputSize;
    private String outputStreamId;
    private StreamDefinition outputStreamDefinition;
    private StreamJunction outputStreamJunction;
    private Projector projector;
    private ConditionExecutor havingConditionExecutor = null;
    private OutStream outStream;
    public boolean currentOn = false;
    public boolean expiredOn = false;

    public QueryProjector(OutStream outStream, Projector projector,
                          List<QueryEventStream> queryEventStreamList,
                          ConcurrentMap<String, StreamJunction> streamJunctionMap,
                          SiddhiContext siddhiContext) {
        this.outStream = outStream;
        if (outStream.getOutputEvents() == OutStream.OutputEvents.CURRENT_EVENTS || outStream.getOutputEvents() == OutStream.OutputEvents.ALL_EVENTS) {
            currentOn = true;
        }
        if (outStream.getOutputEvents() == OutStream.OutputEvents.EXPIRED_EVENTS || outStream.getOutputEvents() == OutStream.OutputEvents.ALL_EVENTS) {
            expiredOn = true;
        }

        this.outputStreamId = outStream.getStreamId();
        this.projector = projector;

        outputSize = projector.getProjectionList().size();
        outputAttributeGeneratorList = new ArrayList<OutputAttributeGenerator>(outputSize);
        aggregateOutputAttributeGeneratorList = new ArrayList<OutputAttributeGenerator>(outputSize);
        outputStreamDefinition = new StreamDefinition();
        outputStreamDefinition.name(outputStreamId);
        populateOutputAttributes(queryEventStreamList, generateGroupByOutputAttributeGenerator(projector.getGroupByList(), queryEventStreamList), siddhiContext);

        havingConditionExecutor = generateHavingExecutor(projector.getHavingCondition(), outputStreamId, outputStreamDefinition);

        outputStreamJunction = streamJunctionMap.get(outputStreamId);
        if (outputStreamJunction == null) {
            outputStreamJunction = new StreamJunction(outputStreamId);
            streamJunctionMap.putIfAbsent(outputStreamId, outputStreamJunction);

        }

    }


    private ConditionExecutor generateHavingExecutor(Condition condition, String outputStreamId,
                                                     StreamDefinition outputStreamDefinition) {
        if (condition != null) {
            List<QueryEventStream> queryEventStreamList = new ArrayList<QueryEventStream>();
            queryEventStreamList.add(new QueryEventStream(outputStreamId, outputStreamId, outputStreamDefinition));
            return ExecutorParser.parseCondition(condition, queryEventStreamList, outputStreamId);
        }
        return null;
    }

    private GroupByOutputAttributeGenerator generateGroupByOutputAttributeGenerator(
            List<Variable> groupByList,
            List<QueryEventStream> queryEventStreamList) {

        if (groupByList.size() > 0) {
            return new GroupByOutputAttributeGenerator(projector.getGroupByList(), queryEventStreamList);
        } else {
            return null;
        }
    }

    private void populateOutputAttributes(List<QueryEventStream> queryEventStreamList,
                                          GroupByOutputAttributeGenerator groupByOutputAttributeGenerator,
                                          SiddhiContext siddhiContext) {
        for (OutputAttribute outputAttribute : projector.getProjectionList()) {
            if (outputAttribute instanceof SimpleAttribute) {
                SimpleOutputAttributeGenerator attributeGenerator = new SimpleOutputAttributeGenerator(ExecutorParser.parseExpression(((SimpleAttribute) outputAttribute).getExpression(), queryEventStreamList, null));
                outputAttributeGeneratorList.add(attributeGenerator);
                outputStreamDefinition.attribute(outputAttribute.getRename(), attributeGenerator.getType());
            } else {  //Aggregations
                AbstractAggregateAttributeGenerator attributeGenerator = AggregatorParser.loadAggregatorClass(((AggregationAttribute) outputAttribute).getAggregationName());

                //for persistence
                siddhiContext.getPersistenceService().addPersister(attributeGenerator);

                if (groupByOutputAttributeGenerator != null) { //for group
                    attributeGenerator = groupByOutputAttributeGenerator.createNewInstance().assignAggregateAttributeGenerator(attributeGenerator);
                }
                attributeGenerator.assignExpressions(((AggregationAttribute) outputAttribute).getExpressions(), queryEventStreamList);
                outputAttributeGeneratorList.add(attributeGenerator);
                aggregateOutputAttributeGeneratorList.add(attributeGenerator);
                outputStreamDefinition.attribute(outputAttribute.getRename(), attributeGenerator.getType());
            }
        }

    }


    public StreamDefinition getOutputStreamDefinition() {
        return outputStreamDefinition;
    }

    public void setStreamJunction(StreamJunction streamJunction) {
        this.outputStreamJunction = streamJunction;
    }

    public void process(AtomicEvent atomicEvent) {
        if ((!(atomicEvent instanceof InStream) || !currentOn) && (!(atomicEvent instanceof RemoveStream) || !expiredOn)) {
            for (OutputAttributeGenerator outputAttributeGenerator : aggregateOutputAttributeGeneratorList) {
                outputAttributeGenerator.process(atomicEvent);
            }
            return;
        }

        Object[] data = new Object[outputSize];
        for (int i = 0; i < outputSize; i++) {
            OutputAttributeGenerator outputAttributeGenerator = outputAttributeGeneratorList.get(i);
            data[i] = outputAttributeGenerator.process(atomicEvent);
        }
        //   dataList.add(data);
        if (outputStreamJunction != null) {
            StreamEvent event = null;
            if (havingConditionExecutor == null) {
                if (atomicEvent instanceof InStream) {
                    event = new InEvent(outputStreamId, atomicEvent.getTimeStamp(), data);
                    outputStreamJunction.send(event, null, event);
                } else {
                    event = new InEvent(outputStreamId, ((RemoveStream) atomicEvent).getExpiryTime(), data);
                    outputStreamJunction.send(null, event, event);
                }
            } else {
                if (atomicEvent instanceof InStream) {
                    event = new InEvent(outputStreamId, atomicEvent.getTimeStamp(), data);
                    if (havingConditionExecutor.execute((AtomicEvent) event)) {
                        outputStreamJunction.send(event, null, event);
                    }
                } else {
                    event = new InEvent(outputStreamId, ((RemoveStream) atomicEvent).getExpiryTime(), data);
                    if (havingConditionExecutor.execute((AtomicEvent) event)) {
                        outputStreamJunction.send(null, event, event);
                    }
                }
            }

        }
    }

    public void process(List<AtomicEvent> atomicEventList) {
        //todo
    }

    public void process(ListEvent listEvent) {
        Event[] events = listEvent.getEvents();
        for (int i = 0, iterateLength = events.length - 1; i < iterateLength; i++) {
            for (OutputAttributeGenerator outputAttributeGenerator : aggregateOutputAttributeGeneratorList) {
                outputAttributeGenerator.process(events[i]);
            }
        }
        process(events[events.length - 1]);
    }
}

