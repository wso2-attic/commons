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

package org.wso2.siddhi.core;

import org.apache.log4j.Logger;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.JoinQuery;
import org.wso2.siddhi.api.eventstream.query.PatternQuery;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.SequenceQuery;
import org.wso2.siddhi.api.eventstream.query.SimpleQuery;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.compiler.SiddhiCompiler;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.eventstream.StreamReference;
import org.wso2.siddhi.core.eventstream.handler.InputStreamHandler;
import org.wso2.siddhi.core.exception.InvalidEventStreamDefinitionException;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.EventSink;
import org.wso2.siddhi.core.node.EventSource;
import org.wso2.siddhi.core.node.ExecutableNode;
import org.wso2.siddhi.core.node.InputHandler;
import org.wso2.siddhi.core.node.Node;
import org.wso2.siddhi.core.node.processor.JoinProcessor;
import org.wso2.siddhi.core.node.processor.PatternProcessor;
import org.wso2.siddhi.core.node.processor.Processor;
import org.wso2.siddhi.core.node.processor.SequenceProcessor;
import org.wso2.siddhi.core.node.processor.SimpleProcessor;
import org.wso2.siddhi.core.thread.SiddhiThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiddhiManager {

    private static final Logger log = Logger.getLogger(SiddhiManager.class);

    private SiddhiThreadPool threadPool;
    private Map<String, EventStream> eventStreamMap;
    private HashMap<String, InputHandler> inputHandlerMap;
    private static int currentNodeId = 0;

    private List<EventSource> eventSourceList;
    private List<EventSink> eventSinkList;
    private List<EventSource> newEventSourceList;
    private List<EventSink> newEventSinkList;
    private List<EventSource> oldEventSourceList;
    private List<EventSink> oldEventSinkList;


    public static final String POISON_PILL = "_PoisonPill";
    public static final int RESET_PROCESSORS = -3;
    public static final int KILL_ALL = -2;
    public static final int KILL = -1;
    private static EventGenerator systemEventGenerator =
            EventGenerator.DefaultFactory.create(POISON_PILL,
                                                 new String[]{"action"},
                                                 new Class[]{Integer.class}
            );


//    private static SiddhiManager siddhiManager = null;

    public SiddhiManager() {
        eventStreamMap = new HashMap<String, EventStream>();
        inputHandlerMap = new HashMap<String, InputHandler>();
        eventSourceList = new ArrayList<EventSource>();
        eventSinkList = new ArrayList<EventSink>();
        newEventSourceList = new ArrayList<EventSource>();
        newEventSinkList = new ArrayList<EventSink>();
        oldEventSourceList = new ArrayList<EventSource>();
        oldEventSinkList = new ArrayList<EventSink>();
        threadPool = new SiddhiThreadPool();
    }

//    /**
//     * get the query factory instance
//     *
//     * @return the instance of query factory
//     */
//    public static SiddhiManager getInstance() {
//        if (null == siddhiManager) {
//            siddhiManager = new SiddhiManager();
//        }
//        return siddhiManager;
//    }


    public static int getNextNodeId() {
        return currentNodeId++;
    }


    public InputHandler addInputEventStream(InputEventStream eventStream)
            throws SiddhiException {
        try {
            assignEventStream(eventStream);
        } catch (InvalidEventStreamDefinitionException e) {
            throw new SiddhiException(e);
        }
        InputHandler inputHandler = inputHandlerMap.get(eventStream.getStreamId());
        if (inputHandler == null) {
            inputHandler = new InputHandler(eventStream);
            this.inputHandlerMap.put(eventStream.getStreamId(), inputHandler);
            this.newEventSourceList.add(inputHandler);
        }
        return inputHandler;
    }

    /**
     * Returns the eventstream of the streamId. Either from inputeventstreams or the outputstreams of the query.
     *
     * @param streamId unique id of the eventstream
     * @return EventStream
     */
    public EventStream getEventStream(String streamId) {
        return this.eventStreamMap.get(streamId);
    }

    public StreamReference addQuery(Query query) throws SiddhiException {
        Processor processor = null;
        try {
            assignEventStream(query);
            processor = generateProcessor(query);
        } catch (InvalidQueryException e) {
            throw new SiddhiException(e);
        } catch (InvalidQueryInputStreamException e) {
            throw new SiddhiException(e);
        } catch (InvalidEventStreamDefinitionException e) {
            throw new SiddhiException(e);
        } catch (ProcessorInitializationException e) {
            throw new SiddhiException(e);
        } catch (InvalidEventStreamIdException e) {
            throw new SiddhiException(e);
        }
        this.newEventSourceList.add(processor);
        this.newEventSinkList.add(processor);
        return new StreamReference(query.getStreamId(), processor.getNodeId());
    }


    public SiddhiManager removeStream(StreamReference streamReference) {

        String duplicateEventStreamId = null;
        for (Node aNode : eventSourceList) {
            if (aNode.getNodeId() == streamReference.getNodeId()) {
                oldEventSourceList.add((EventSource) aNode);
                duplicateEventStreamId = ((EventSource) aNode).getStreamId();
                for (Node otherNode : eventSourceList) {
                    if (otherNode.getNodeId() != streamReference.getNodeId() && otherNode instanceof EventSource) {
                        if (((EventSource) otherNode).getStreamId().equals(((EventSource) aNode).getStreamId())) {
                            duplicateEventStreamId = null;
                            break;
                        }
                    }
                }

                break;
            }
        }
        if (duplicateEventStreamId != null) {
            eventStreamMap.remove(duplicateEventStreamId);
        }
        for (Node aNode : eventSinkList) {
            if (aNode.getNodeId() == streamReference.getNodeId()) {
                oldEventSinkList.add((EventSink) aNode);
                break;
            }
        }

        return this;
    }

    /**
     * to add call back
     *
     * @param callbackHandler the call back class
     * @return the SiddhiManager
     */
    public SiddhiManager addCallback(CallbackHandler callbackHandler) {
//        EventQueue eventQueue = new EventQueueImpl(callbackHandler.getId(), callbackHandler.getStreamId());
        callbackHandler.addSiddhiManager(this);
        this.newEventSinkList.add(callbackHandler);
        return this;

    }

    public InputHandler getInputHandler(String streamId) throws SiddhiException {
        InputHandler inputHandler = inputHandlerMap.get(streamId);

        if (inputHandler == null) {
            throw new SiddhiException("Stream" + streamId + "not found.");
        } else {
            return inputHandler;
        }
    }

    public static QueryFactory getQueryFactory() {
        return QueryFactory.getInstance();
    }

    public StreamReference addQuery(String siddhiQuery)
            throws SiddhiPraserException, SiddhiException {
        EventStream eventStream = SiddhiCompiler.parseSingleStream(siddhiQuery, new ArrayList<EventStream>(eventStreamMap.values()));
        return addQuery((Query) eventStream);
    }

    public List<StreamReference> addConfigurations(String siddhiConfigurations)
            throws SiddhiPraserException, SiddhiException {
        List<EventStream> eventStreamList = SiddhiCompiler.parse(siddhiConfigurations, new ArrayList<EventStream>(eventStreamMap.values()));
        List<StreamReference> streamReferences = new ArrayList<StreamReference>(eventStreamList.size());
        for (EventStream eventStream : eventStreamList) {
            if (eventStream instanceof InputEventStream) {
                streamReferences.add(new StreamReference(eventStream.getStreamId(), addInputEventStream((InputEventStream) eventStream).getNodeId()));
            } else {
                streamReferences.add(addQuery((Query) eventStream));
            }
        }
        return streamReferences;
    }

    public void init()
            throws SiddhiException {

        linkEventStreams();
    }

    /**
     * to initialize and update the siddhi processors and queues
     *
     * @throws org.wso2.siddhi.core.exception.ProcessorInitializationException
     *
     * @throws InvalidQueryException
     */
    public void update()
            throws SiddhiException {

        unlinkEventStreams();
        linkEventStreams();


    }

    /**
     * to rest processors after the given time
     *
     * @param resetTimeBufferInMs the time to wait
     */
    public void reset(int resetTimeBufferInMs) {
        try {
            Thread.sleep(resetTimeBufferInMs);
            reset();
            Thread.sleep(resetTimeBufferInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * to reset the processors
     */
    public void reset() {
        try {
            for (EventSink eventSink : eventSinkList) {
                if (eventSink instanceof ExecutableNode) {
                    ((ExecutableNode) eventSink).resetRunnable();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * to shutdown siddhi manager
     */
    public void shutDownTask() throws SiddhiException {
        //todo use of thread group
        for (InputHandler inputHandler : inputHandlerMap.values()) {
            inputHandler.sendEvent(generateKillAllEvent());
        }

        threadPool.shutdown();
        int waitedTimes = 0;
        while (waitedTimes < 10 && threadPool.getActiveCount() > 0) {
            waitedTimes++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (waitedTimes >= 10) {
            for (EventSink eventSink : eventSinkList) {
                if (eventSink instanceof ExecutableNode) {
                    try {
                        ((ExecutableNode) eventSink).stopRunnable();
                    } catch (InterruptedException e) {
                        throw new SiddhiException(e);
                    }
                }
            }
        }

        threadPool.shutdown();
        threadPool.shutdownNow();
        log.debug(this.getClass().getSimpleName() + " ended");

    }

    /**
     * Generates an event to specify that the end of all the streams has reached.
     * This will send the event to the outputeventqueue as well. See PatternProcessor.run for more info
     *
     * @return the kill event with the eventstream POISON_PILL
     */
    public static Event generateKillAllEvent() {
        return systemEventGenerator.createEvent(KILL_ALL);
    }

    /**
     * Generates an event to specify that the end of all the streams has reached.
     *
     * @return the kill event with the eventstream POISON_PILL
     */
    public static Event generateKillEvent() {
        return systemEventGenerator.createEvent(KILL);
    }

    /**
     * Resets the executor listeners maps. See PatternProcessor.reset
     *
     * @return the reset event with the eventstream POISON_PILL
     */
    public static Event generateResetEvent() {
        return systemEventGenerator.createEvent(RESET_PROCESSORS);
    }

    private void assignEventStream(EventStream eventStream)
            throws InvalidEventStreamDefinitionException {
        EventStream oldEventStream = eventStreamMap.get(eventStream.getStreamId());
        if (null == oldEventStream) {
            eventStreamMap.put(eventStream.getStreamId(), eventStream);
        } else if (!eventStream.equals(oldEventStream)) {
            throw new InvalidEventStreamDefinitionException("The added stream : " + eventStream.toString() + " not match with the existing old stream: " + oldEventStream.toString());

        }
    }

    private void linkEventStreams() {
        //link new eventSources to eventSinks
        for (EventSource eventSource : newEventSourceList) {
            for (EventSink eventSink : eventSinkList) {
                assignSourceToSink(eventSource, eventSink);
            }

            eventSourceList.add(eventSource);
        }
        newEventSourceList.clear();

        for (EventSink eventSink : newEventSinkList) {
            for (EventSource eventSource : eventSourceList) {
                assignSourceToSink(eventSource, eventSink);
            }
            eventSinkList.add(eventSink);
            if (eventSink instanceof ExecutableNode) {
                ((ExecutableNode) eventSink).startRunnable(threadPool);
            }
        }
        newEventSinkList.clear();
    }

    private void unlinkEventStreams() throws SiddhiException {
        //unlink old eventSources and eventSinks
        for (EventSource eventSource : oldEventSourceList) {
            eventSource.getOutputEventStream().removeAllStreamHandlers();
            eventSourceList.remove(eventSource);
        }
        oldEventSourceList.clear();

        for (EventSink eventSink : oldEventSinkList) {
            for (EventSource eventSource : eventSourceList) {
                if (!eventSink.equals(eventSource)) {
                    InputStreamHandler inputStreamHandler = eventSink.getInputEventStream().
                            getQueryInputStreamHandler(eventSource.getStreamId());
                    if (null != inputStreamHandler) {
                        try {
                            eventSource.getOutputEventStream().removeStreamHandler(inputStreamHandler);
                        } catch (InvalidQueryInputStreamException e) {
                            throw new SiddhiException(e.getMessage(), e);
                        }
                    }
                }
            }

            eventSinkList.remove(eventSink);
            try {
                if (eventSink instanceof ExecutableNode) {
                    ((ExecutableNode) eventSink).stopRunnable();
                }
            } catch (InterruptedException e) {
                throw new SiddhiException(e.getMessage(), e);
            }
        }
        oldEventSinkList.clear();
    }

    private void assignSourceToSink(EventSource eventSource, EventSink eventSink) {
        if (!eventSink.equals(eventSource)) {
            InputStreamHandler inputStreamHandler = eventSink.getInputEventStream().
                    getQueryInputStreamHandler(eventSource.getStreamId());
            if (null != inputStreamHandler) {
                eventSource.getOutputEventStream().assignStreamHandlers(inputStreamHandler);
            }
        }
    }

    private Processor generateProcessor(Query query)
            throws InvalidQueryException, InvalidQueryInputStreamException,
                   ProcessorInitializationException, SiddhiException,
                   InvalidEventStreamIdException {
        Processor processor;
        if (query instanceof SimpleQuery) {   // simple query
            processor = new SimpleProcessor((SimpleQuery) query);
        } else if (query instanceof PatternQuery) { // sequence query
            processor = new PatternProcessor((PatternQuery) query);
        } else if (query instanceof SequenceQuery) { // pattern query
            processor = new SequenceProcessor((SequenceQuery) query);
            if (!((SequenceCondition) query.getCondition()).isSkipTillFirstMatch()) {
                ((SequenceProcessor) processor).setSkipTillFirstMatch(false);
            }
        } else if (query instanceof JoinQuery) {       // join query
            processor = new JoinProcessor((JoinQuery) query);
        } else {
            throw new InvalidQueryException("Wrong query type: " + query.getStreamId());
        }
        return processor;
    }
}


