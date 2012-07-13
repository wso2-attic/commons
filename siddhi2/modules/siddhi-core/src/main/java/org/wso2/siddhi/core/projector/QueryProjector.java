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
package org.wso2.siddhi.core.projector;

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.event.remove.RemoveEvent;
import org.wso2.siddhi.core.event.remove.RemoveStream;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.projector.attibute.generator.AbstractAggregateAttributeGenerator;
import org.wso2.siddhi.core.projector.attibute.generator.SimpleOutputAttributeGenerator;
import org.wso2.siddhi.core.projector.attibute.generator.groupby.GroupByOutputAttributeGenerator;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.util.parser.AggregatorParser;
import org.wso2.siddhi.core.util.parser.ExecutorParser;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.query.projection.Projector;
import org.wso2.siddhi.query.api.query.projection.attribute.AggregationAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.OutputAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.SimpleAttribute;

import java.util.ArrayList;
import java.util.List;

public class QueryProjector {

    //    private List<Object[]> dataList = new ArrayList<Object[]>();
    private List<OutputAttributeGenerator> outputAttributeGeneratorList;
    private List<OutputAttributeGenerator> aggregateOutputAttributeGeneratorList;
    private int outputSize;
    private String outputStreamId;
    private StreamDefinition outputStreamDefinition = new StreamDefinition();
    private StreamJunction outputStreamJunction;
    private Projector projector;
    private ConditionExecutor havingConditionExecutor = null;

    public QueryProjector(String outputStreamId, Projector projector,
                          List<QueryEventStream> queryEventStreamList) {
        this.outputStreamId = outputStreamId;
        outputStreamDefinition.name(outputStreamId);
        this.projector = projector;
        outputSize = projector.getProjectionList().size();
        outputAttributeGeneratorList = new ArrayList<OutputAttributeGenerator>(outputSize);
        aggregateOutputAttributeGeneratorList = new ArrayList<OutputAttributeGenerator>(outputSize);
        generateOutputAttributeGenerators(queryEventStreamList, generateGroupByOutputAttributeGenerator(projector.getGroupByList(), queryEventStreamList));
        generateHavingExecutor(projector.getHavingCondition());

    }

    private void generateHavingExecutor(Condition condition) {
        if (condition != null) {
            List<QueryEventStream> queryEventStreamList = new ArrayList<QueryEventStream>();
            queryEventStreamList.add(new QueryEventStream(outputStreamId, outputStreamId, outputStreamDefinition));
            havingConditionExecutor = ExecutorParser.parseCondition(condition, queryEventStreamList, outputStreamId);
        }
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

    private void generateOutputAttributeGenerators(List<QueryEventStream> queryEventStreamList,
                                                   GroupByOutputAttributeGenerator groupByOutputAttributeGenerator) {
        for (OutputAttribute outputAttribute : projector.getProjectionList()) {
            if (outputAttribute instanceof SimpleAttribute) {
                SimpleOutputAttributeGenerator attributeGenerator = new SimpleOutputAttributeGenerator(ExecutorParser.parseExpression(((SimpleAttribute) outputAttribute).getExpression(), queryEventStreamList, null));
                outputAttributeGeneratorList.add(attributeGenerator);
                outputStreamDefinition.attribute(outputAttribute.getRename(), attributeGenerator.getType());
            } else {  //Aggregations
                AbstractAggregateAttributeGenerator attributeGenerator = AggregatorParser.loadAggregatorClass(((AggregationAttribute) outputAttribute).getAggregationName());
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
//        System.out.println("Arrived ");
        Object[] data = new Object[outputSize];
        for (int i = 0; i < outputSize; i++) {
            OutputAttributeGenerator outputAttributeGenerator = outputAttributeGeneratorList.get(i);
            data[i] = outputAttributeGenerator.process(atomicEvent);
        }
        //   dataList.add(data);
        try {
            if (havingConditionExecutor == null) {
                if (atomicEvent instanceof InStream) {
                    outputStreamJunction.send(new InEvent(outputStreamId, atomicEvent.getTimeStamp(), data));
                } else {
                    outputStreamJunction.send(new RemoveEvent(outputStreamId, atomicEvent.getTimeStamp(), data,((RemoveStream)atomicEvent).getExpiryTime()));
                }
            } else {
                StreamEvent event;
                if (atomicEvent instanceof InStream) {
                    event = new InEvent(outputStreamId, atomicEvent.getTimeStamp(), data);
                } else {
                    event = new RemoveEvent(outputStreamId, atomicEvent.getTimeStamp(), data,((RemoveStream)atomicEvent).getExpiryTime());
                }
                if (havingConditionExecutor.execute((AtomicEvent) event)) {
                    outputStreamJunction.send(event);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //todo handle
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

