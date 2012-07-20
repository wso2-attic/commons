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
import org.wso2.siddhi.core.exception.EventStreamWithDifferentDefinitionAlreadyExistException;
import org.wso2.siddhi.core.persistence.PersistenceService;
import org.wso2.siddhi.core.persistence.PersistenceStore;
import org.wso2.siddhi.core.query.QueryProcessor;
import org.wso2.siddhi.core.query.stream.handler.RunnableHandler;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SiddhiManager {

    private SiddhiContext siddhiContext;
    private ThreadPoolExecutor threadPoolExecutor;
    ConcurrentMap<String, StreamJunction> streamJunctionMap = new ConcurrentHashMap<String, StreamJunction>(); //contains definition
    ConcurrentMap<String, StreamDefinition> streamDefinitionMap = new ConcurrentHashMap<String, StreamDefinition>(); //contains definition
    ConcurrentMap<String, QueryProcessor> queryProcessorMap = new ConcurrentHashMap<String, QueryProcessor>();
    ConcurrentMap<String, InputHandler> inputHandlerMap = new ConcurrentHashMap<String, InputHandler>();
//    LinkedBlockingQueue<StateEvent> inputQueue = new LinkedBlockingQueue<StateEvent>();

    public SiddhiManager() {
        this(new SiddhiConfiguration());
    }

    public SiddhiManager(SiddhiConfiguration configuration) {
        this.siddhiContext = new SiddhiContext();
        this.siddhiContext.setEventBatchSize(configuration.getEventBatchSize());
        this.siddhiContext.setSingleThreading(configuration.isSingleThreading());
        this.siddhiContext.setThreads(configuration.getThreads());
        this.siddhiContext.setExecutionPlanIdentifier(configuration.getExecutionPlanIdentifier());
        this.siddhiContext.setPersistenceService(new PersistenceService(siddhiContext));


        threadPoolExecutor = new ThreadPoolExecutor(configuration.getThreads(),
                                                    Integer.MAX_VALUE,
                                                    50,
                                                    TimeUnit.MICROSECONDS,
                                                    new LinkedBlockingQueue<Runnable>());
    }


    public InputHandler defineStream(StreamDefinition streamDefinition) {
        if (!checkEventStreamExist(streamDefinition)) {
            streamDefinitionMap.put(streamDefinition.getStreamId(), streamDefinition);
            StreamJunction streamJunction = streamJunctionMap.get(streamDefinition.getStreamId());
            if (streamJunction == null) {
                streamJunction = new StreamJunction(streamDefinition.getStreamId());
                streamJunctionMap.put(streamDefinition.getStreamId(), streamJunction);
            }
            InputHandler inputHandler = new InputHandler(streamDefinition.getStreamId(), streamJunction);
            inputHandlerMap.put(streamDefinition.getStreamId(), inputHandler);
            return inputHandler;
        } else {
            return inputHandlerMap.get(streamDefinition.getStreamId());
        }

    }

//    public InputHandler updateStream(StreamDefinition streamDefinition) {
//        try {
//            if (checkEventStreamExist(streamDefinition)) {
//                return inputHandlerMap.get(streamDefinition.getStreamId());
//            }
//        } catch (EventStreamWithDifferentDefinitionAlreadyExistException e) {
//            StreamJunction streamJunction = streamJunctionMap.get(streamDefinition.getStreamId());
//            streamDefinitionMap.replace(streamDefinition.getStreamId(), streamDefinition);
//            streamJunction.setStreamDefinition(streamDefinition);
//            return inputHandlerMap.get(streamDefinition.getStreamId());
//        }
//        throw new EventStreamNotExistException("Stream with name " + streamDefinition.getStreamId() + " does not exist! Hence update aborted!");
//    }
//
//    public void dropStream(String streamId) {
//        StreamDefinition streamDefinition = streamDefinitionMap.remove(streamId);
//        if (streamDefinition != null) {
//            streamJunctionMap.remove(streamId);
//            InputHandler inputHandler = inputHandlerMap.remove(streamId);
//            if (inputHandler != null) {
//                inputHandler.setStreamJunction(null);
//            }
//            for (QueryProcessor processor : queryProcessorMap.values()) {
//                if (processor.getQueryProjector().getOutputStreamDefinition().getStreamId().equals(streamId)) {
//                    processor.getQueryProjector().setStreamJunction(null);
//                }
//            }
//        }
//    }


    public InputHandler defineStream(String streamDefinition) throws SiddhiPraserException {
        return defineStream(SiddhiCompiler.parseStreamDefinition(streamDefinition));

    }

    private boolean checkEventStreamExist(StreamDefinition newStreamDefinition) {
        StreamDefinition streamDefinition = streamDefinitionMap.get(newStreamDefinition.getStreamId());
        if (streamDefinition != null) {
            if (!streamDefinition.getAttributeList().equals(newStreamDefinition.getAttributeList())) {
                throw new EventStreamWithDifferentDefinitionAlreadyExistException(newStreamDefinition.getStreamId() + " is already defined as " + streamDefinition);
            } else {
                return true;
            }
        }
        return false;
    }

    public String addQuery(String query) throws SiddhiPraserException {
        return addQuery(SiddhiCompiler.parseQuery(query));
    }

//    public void addExecutionPlan(String addExecutionPlan) throws SiddhiPraserException {
//        for (IQuery executionPlan : SiddhiCompiler.parse(addExecutionPlan)) {
//            if (executionPlan instanceof StreamDefinition) {
//                defineStream((StreamDefinition) executionPlan);
//            } else {
//                addQuery((Query) executionPlan);
//            }
//        }
//    }

    public String addQuery(Query query) {
        QueryProcessor queryProcessor = new QueryProcessor(query, streamDefinitionMap, streamJunctionMap, threadPoolExecutor, siddhiContext);
        defineStream(queryProcessor.getOutputStreamDefinition());
        queryProcessorMap.put(queryProcessor.getQueryId(), queryProcessor);
        return queryProcessor.getQueryId();

    }

    public void removeQuery(String queryId) {
        QueryProcessor queryProcessor = queryProcessorMap.remove(queryId);
        if (queryProcessor != null) {
            queryProcessor.removeQuery(streamJunctionMap, streamDefinitionMap);
        }
    }


//    private Map<String, StreamDefinition> getQueryStreamDefinitionMap(List<String> streamIds) {
//        Map<String, StreamDefinition> map = new HashMap<String, StreamDefinition>();
//        for (String streamId : streamIds) {
//            map.put(streamId, streamDefinitionMap.get(streamId));
//        }
//        return map;
//    }


//    private void checkQuery(Query query) {
//        List<String> streamIds = query.getInputStream().getStreamIds();
//        System.out.println(streamIds);
//    }

    public Query getQuery(String queryReference) {
        return queryProcessorMap.get(queryReference).getQuery();
    }

    public InputHandler getInputHandler(String streamId) {
        return inputHandlerMap.get(streamId);
    }

    public void addCallback(String streamId, Callback callback) {
        callback.setStreamId(streamId);
        callback.setSiddhiContext(siddhiContext);
        callback.setThreadPoolExecutor(threadPoolExecutor);
        StreamJunction streamJunction = streamJunctionMap.get(streamId);
        if (streamJunction == null) {
            streamJunction = new StreamJunction(streamId);
            streamJunctionMap.put(streamId, streamJunction);
        }
        streamJunction.addEventFlow(callback);
    }


    public void shutdown() {
        threadPoolExecutor.shutdown();
        for (RunnableHandler handler : siddhiContext.getRunnableHandlerList()) {
            handler.shutdown();
        }
    }

    public StreamDefinition getStreamDefinition(String streamId) {
        return streamDefinitionMap.get(streamId);
    }

    public void setPersistStore(PersistenceStore persistStore) {
        siddhiContext.getPersistenceService().setPersistenceStore(persistStore);
    }

    public String persist() {
        return siddhiContext.getPersistenceService().persist();
    }

    public void restoreRevision( String revision) {
        siddhiContext.getPersistenceService().restoreRevision(revision);
    }

    public void restoreLastRevision() {
        siddhiContext.getPersistenceService().restoreLastRevision();
    }

}
