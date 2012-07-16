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
package org.wso2.siddhi.core;

import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.StateEvent;
import org.wso2.siddhi.core.exception.EventStreamAlreadyExistException;
import org.wso2.siddhi.core.projector.QueryProjector;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.stream.handler.RunnableHandler;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.stream.recevier.StreamReceiver;
import org.wso2.siddhi.core.util.parser.StreamParser;
import org.wso2.siddhi.query.api.ExecutionPlan;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.query.projection.attribute.OutputAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.SimpleAttribute;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SiddhiManager {

    private SiddhiContext siddhiContext;
    private ThreadPoolExecutor threadPoolExecutor;
    Map<String, StreamJunction> streamJunctionMap = new HashMap<String, StreamJunction>(); //contains definition
    Map<String, StreamDefinition> streamDefinitionMap = new HashMap<String, StreamDefinition>(); //contains definition
    List<Query> queryList = new ArrayList<Query>();
    LinkedBlockingQueue<StateEvent> inputQueue = new LinkedBlockingQueue<StateEvent>();

    public SiddhiManager() {
        this(new SiddhiConfiguration());
    }

    public SiddhiManager(SiddhiConfiguration configuration) {
        this.siddhiContext = new SiddhiContext();
        this.siddhiContext.setEventBatchSize(configuration.getEventBatchSize());
        this.siddhiContext.setSingleThreading(configuration.isSingleThreading());
        this.siddhiContext.setThreads(configuration.getThreads());

        threadPoolExecutor = new ThreadPoolExecutor(configuration.getThreads(),
                                                    Integer.MAX_VALUE,
                                                    50,
                                                    TimeUnit.MICROSECONDS,
                                                    new LinkedBlockingQueue<Runnable>());
    }


    public InputHandler defineStream(StreamDefinition streamDefinition) {
        checkEventStream(streamDefinition);
        StreamJunction streamJunction = new StreamJunction(streamDefinition);
        streamJunctionMap.put(streamDefinition.getStreamId(), streamJunction);
        streamDefinitionMap.put(streamDefinition.getStreamId(), streamDefinition);
        return new InputHandler(streamDefinition.getStreamId(), streamJunction);
    }

    public InputHandler defineStream(String streamDefinition) throws SiddhiPraserException {
        return defineStream(SiddhiCompiler.parseStreamDefinition(streamDefinition));

    }

    private void checkEventStream(StreamDefinition newStreamDefinition) {
        StreamDefinition streamDefinition = streamDefinitionMap.get(newStreamDefinition.getStreamId());
        if (streamDefinition != null) {
            if (!streamDefinition.getAttributeList().equals(newStreamDefinition.getAttributeList())) {
                throw new EventStreamAlreadyExistException(newStreamDefinition.getStreamId() + " is already defined as " + streamDefinition);
            }
        }
    }

    public void addQuery(String query) throws SiddhiPraserException {
        addQuery(SiddhiCompiler.parseQuery(query));
    }

    public void addExecutionPlan(String addExecutionPlan) throws SiddhiPraserException {
        for (ExecutionPlan executionPlan : SiddhiCompiler.parse(addExecutionPlan)) {
            if (executionPlan instanceof StreamDefinition) {
                defineStream((StreamDefinition) executionPlan);
            } else {
                addQuery((Query) executionPlan);
            }
        }
    }

    public void addQuery(Query query) {
        queryList.add(query);

        List<QueryEventStream> queryEventStreamList = query.getInputStream().constructQueryEventStreamList(streamDefinitionMap, new ArrayList<QueryEventStream>());
        initQuery(query, queryEventStreamList);

        QueryProjector queryProjector = new QueryProjector(query.getOutputStreamId(), query.getProjector(), queryEventStreamList);
        StreamDefinition outputStreamDefinition = queryProjector.getOutputStreamDefinition();
        defineStream(outputStreamDefinition);

        List<StreamReceiver> streamReceiverList = StreamParser.parseStream(query.getInputStream(), queryEventStreamList, queryProjector, threadPoolExecutor, siddhiContext);
        queryProjector.setStreamJunction(streamJunctionMap.get(outputStreamDefinition.getStreamId()));
        for (StreamReceiver streamReceiver : streamReceiverList) {
            StreamJunction streamJunction = streamJunctionMap.get(streamReceiver.getStreamId());
            streamJunction.addEventFlow(streamReceiver);
        }

    }

    private void initQuery(Query query, List<QueryEventStream> queryEventStreamList) {

        //   Map<String, StreamDefinition> queryStreamDefinitionMap = getQueryStreamDefinitionMap(query.getInputStream().getStreamIds());
        //populate projection for * case
        List<OutputAttribute> attributeList = query.getProjector().getProjectionList();
        if (attributeList.size() == 0) {
            for (QueryEventStream queryEventStream : queryEventStreamList) {
                for (Attribute attribute : queryEventStream.getStreamDefinition().getAttributeList()) {
                    attributeList.add(new SimpleAttribute(queryEventStream.getReferenceStreamId() + "_" + attribute.getName(), Expression.variable(queryEventStream.getReferenceStreamId(), attribute.getName())));
                }
            }
        }

    }

    private Map<String, StreamDefinition> getQueryStreamDefinitionMap(List<String> streamIds) {
        Map<String, StreamDefinition> map = new HashMap<String, StreamDefinition>();
        for (String streamId : streamIds) {
            map.put(streamId, streamDefinitionMap.get(streamId));
        }
        return map;
    }


//    private void checkQuery(Query query) {
//        List<String> streamIds = query.getInputStream().getStreamIds();
//        ////System.out.println(streamIds);
//    }

    public InputHandler getInputHandler(String streamId) {
        return new InputHandler(streamId, streamJunctionMap.get(streamId));
    }

    public void addCallback(String streamId, Callback callback) {
        callback.setStreamId(streamId);
        callback.setSiddhiContext(siddhiContext);
        callback.setThreadPoolExecutor(threadPoolExecutor);
        streamJunctionMap.get(streamId).addEventFlow(callback);
    }


    public void shutdown() {
        threadPoolExecutor.shutdown();
        for (RunnableHandler handler : siddhiContext.getRunnableHandlerList()) {
            handler.shutdown();
        }
    }
}
