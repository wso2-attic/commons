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
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.PatternQuery;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.util.OutputDefinitionParserUtil;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.event.generator.EventGeneratorImpl;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.processor.eventmap.StateMachineOutputMapObj;
import org.wso2.siddhi.core.node.processor.executor.FollowedByExecutor;
import org.wso2.siddhi.core.parser.ConditionParser;
import org.wso2.siddhi.core.parser.QueryInputStreamParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This class contains the Query processor implementation for processing Pattern queries
 */
public class PatternProcessor extends AbstractProcessor {

    private PatternQuery query;
    private List<StateMachineOutputMapObj> outputEventMappingObjects;
    private EventGenerator eventGenerator;
    private List<FollowedByExecutor> executorList;          // A list holding the executors 
    private long executorCleaningTime = 0;
    private long executorCleaningInterval = 1000;

    private Map<String, LinkedList<FollowedByExecutor>> activeExecutorsP1;          // A map holding active executor listeners for process type 1
    private Map<String, LinkedList<FollowedByExecutor>> newlyAddedActiveListeners;  //  A map holding newly added active executors
    private Map<String, Map<Event, FollowedByExecutor>> activeExecutorsP2;          // A map holding active executor listeners for process type 2

    private boolean cleanOldExecutors = false;
    private List<String> cleanStreamNames;

    private static final Logger log = Logger.getLogger(PatternProcessor.class);

    // Select process type
    //type 1:  sending all events (including duplicates)
    //type 2: ignore duplicate events
    private int processType = 2;

    @Override
    public String getStreamId() {
        return query.getStreamId();
    }

    /**
     * @param query pattern query
     */
    public PatternProcessor(PatternQuery query)
            throws InvalidQueryInputStreamException, ProcessorInitializationException,
                   SiddhiException {
        this.query = query;
        if (processType == 1) {
            activeExecutorsP1 = new Hashtable<String, LinkedList<FollowedByExecutor>>();
            newlyAddedActiveListeners = new Hashtable<String, LinkedList<FollowedByExecutor>>();
        } else {
            activeExecutorsP2 = new HashMap<String, Map<Event, FollowedByExecutor>>();
        }
        for (QueryInputStream queryInputStream : query.getQueryInputStreamList()) {
            assignQueryInputStream(queryInputStream);
        }
        init();
    }

    @Override
    public void assignQueryInputStream(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        inputEventStream.assignInputStreamHandler(QueryInputStreamParser.parse(queryInputStream));
    }

    /**
     * Initialize the processor.
     *
     * @throws org.wso2.siddhi.core.exception.ProcessorInitializationException
     *
     */
    public void init() throws ProcessorInitializationException, SiddhiException {

        try {
            // Query
            Condition condition = query.getCondition();
            EventStream[] inputEventStreams = query.getInputEventStreams();

            ConditionParser conditionParser = new ConditionParser(condition, inputEventStreams);
            executorList = conditionParser.getFollowedbyExecutorList();                             // Get list of Executors

            if (processType == 1) {
                for (EventStream eventStream : inputEventStreams) {
                    activeExecutorsP1.put(eventStream.getStreamId(), new LinkedList<FollowedByExecutor>());
                    newlyAddedActiveListeners.put(eventStream.getStreamId(), new LinkedList<FollowedByExecutor>());
                }
                activeExecutorsP1.get(executorList.get(0).getCheckingStreamName()).add(executorList.get(0).getNewInstance()); //adding first FollowedByExecutor to eventListeners
            } else {
                for (EventStream eventStream : inputEventStreams) {
                    activeExecutorsP2.put(eventStream.getStreamId(), new HashMap<Event, FollowedByExecutor>());
                }
                activeExecutorsP2.put(null, new HashMap<Event, FollowedByExecutor>());//for NonOccurring conditions
                activeExecutorsP2.get(executorList.get(0).getCheckingStreamName()).put(null, executorList.get(0).getNewInstance()); //adding first FollowedByExecutor to eventListeners
            }

            // Output
            eventGenerator = new EventGeneratorImpl(query.getStreamId(), query.getNames(), query.getTypes());
            List<String> outputDefinitionList = query.getOutputDefinition().getPropertyList();
            outputEventMappingObjects = new ArrayList<StateMachineOutputMapObj>();

            List<String> eventStreamNames = OutputDefinitionParserUtil.createStreamIdListFromConditions((FollowedByCondition) condition);

            for (String aOutputDefinition : outputDefinitionList) {
                // use 0 as the first state
                int stateId = Integer.parseInt(aOutputDefinition.split("=")[1].split("\\.")[0].substring(1));
                String propertyAttribute = aOutputDefinition.split("=")[1].split("\\.")[1];

                for (EventStream eventStream : inputEventStreams) {
                    if (eventStream.getStreamId().equals(eventStreamNames.get(stateId))) {
                        StateMachineOutputMapObj mapObj = new StateMachineOutputMapObj(stateId, eventStream.getAttributePositionForName(propertyAttribute));
                        outputEventMappingObjects.add(mapObj);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new ProcessorInitializationException("Cannot initialize  Pattern  Processor query " + query.getStreamId(), ex);
        }

        // Event Streams with time window
        cleanStreamNames = new ArrayList<String>();
        for (FollowedByExecutor followedByExecutor : executorList) {
            if (followedByExecutor.getLifeTime() > -1) {
                cleanOldExecutors = true;
                if (!cleanStreamNames.contains(followedByExecutor.getCheckingStreamName())) {
                    cleanStreamNames.add(followedByExecutor.getCheckingStreamName());
                }
            }
        }

    }

    /**
     * Resetting the processor
     */
    private void reset() {
        EventStream[] inputEventStreams = query.getInputEventStreams();
        if (processType == 1) {
            for (EventStream eventStream : inputEventStreams) {
                activeExecutorsP1.get(eventStream.getStreamId()).clear();
                newlyAddedActiveListeners.get(eventStream.getStreamId()).clear();
            }
            activeExecutorsP1.get(executorList.get(0).getCheckingStreamName()).add(executorList.get(0).getNewInstance()); //adding first FollowedByExecutor to eventListeners

        } else {
            for (String eventStream : activeExecutorsP2.keySet()) {
                activeExecutorsP2.get(eventStream).clear();
            }
            activeExecutorsP2.get(executorList.get(0).getCheckingStreamName()).put(null, executorList.get(0).getNewInstance()); //adding first FollowedByExecutor to eventListeners
        }
    }

    /**
     * Execution of the Processor
     */
    public void run() {

        Event event = null;

        while (true) {
            try {
                event = inputEventStream.takeEvent();  //get event
                // For last event
                if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
                    Integer pill = (Integer) event.getNthAttribute(0);
                    if (pill == SiddhiManager.RESET_PROCESSORS) {
                        reset();
                        continue;
                    } else if (pill == SiddhiManager.KILL_ALL) {
                        outputEventStream.put(event);
                        break;
                    } else if (pill == SiddhiManager.KILL) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (processType == 1) {
                String streamName = event.getEventStreamId();
                List eventListeners = activeExecutorsP1.get(streamName);
                for (Iterator<FollowedByExecutor> it = eventListeners.iterator(); it.hasNext(); ) {
                    FollowedByExecutor currentFollowedByExecutor = it.next();

                    if (!currentFollowedByExecutor.isAlive()) {     // Removing expired events
                        it.remove();
                        break;
                    } else if (currentFollowedByExecutor.execute(event)) { // if state condition success

                        if (!currentFollowedByExecutor.isNextExecutorExist()) { // if final state reached - all state success
                            // Generate output event
                            Object[] outputValues = new Object[outputEventMappingObjects.size()];
                            for (int i = 0; i < outputEventMappingObjects.size(); i++) {
                                StateMachineOutputMapObj aStateMachineOutputMapObj = outputEventMappingObjects.get(i);
                                outputValues[i] = currentFollowedByExecutor.getArrivedEvents()[aStateMachineOutputMapObj.getStateTypeId()].getNthAttribute(aStateMachineOutputMapObj.getPosition());
                            }
                            // Send generated output event
                            try {
                                outputEventStream.put(eventGenerator.createEvent(outputValues));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else { //adding new FollowedByExecutor
                            FollowedByExecutor newFollowedByExecutor = currentFollowedByExecutor.getNewNextExecutor();
                            newlyAddedActiveListeners.get(newFollowedByExecutor.getCheckingStreamName()).add(newFollowedByExecutor);
                        }
                        //  For Every
                        if (currentFollowedByExecutor.isNextEveryExecutorExist()) {
                            FollowedByExecutor newFollowedByExecutor = currentFollowedByExecutor.getNewNextEveryExecutor();
                            newlyAddedActiveListeners.get(newFollowedByExecutor.getCheckingStreamName()).add(newFollowedByExecutor);
                        }

                        //remove the success state
                        it.remove();
                        break;
                    }
                }

                //adding new listeners from temp executor listener map
                for (String key : newlyAddedActiveListeners.keySet()) {
                    List<FollowedByExecutor> list = newlyAddedActiveListeners.get(key);
                    if (list.size() != 0) {
                        activeExecutorsP1.get(key).addAll(0, list);
                        list.clear();
                    }
                }

                if (cleanOldExecutors) {
                    long currentTime = System.currentTimeMillis();
                    if (executorCleaningTime < currentTime) {
                        executorCleaningTime = currentTime + executorCleaningInterval;
                        for (String tempStreamName : cleanStreamNames) {
                            for (Iterator<FollowedByExecutor> it = activeExecutorsP1.get(tempStreamName).iterator(); it.hasNext(); ) {
                                FollowedByExecutor currentFollowedByExecutor = it.next();
                                if (!currentFollowedByExecutor.isAlive()) {     // Removing expired events
                                    it.remove();
                                }
                            }
                        }
                    }
                    System.gc();
                }
            } else {         // For process type 2
                String streamName = event.getEventStreamId();
                List<FollowedByExecutor> newlyAddedActiveListeners = new LinkedList<FollowedByExecutor>();

                if (null == activeExecutorsP2.get(streamName).get(event) && null == activeExecutorsP2.get(null).get(event)) {   //to remove duplicate events

                    for (Iterator<FollowedByExecutor> it = activeExecutorsP2.get(streamName).values().iterator(); it.hasNext(); ) {
                        FollowedByExecutor currentFollowedByExecutor = it.next();
                        processFollowedByExecutor(event, newlyAddedActiveListeners, it, currentFollowedByExecutor);
                    }
                    for (Iterator<FollowedByExecutor> it = activeExecutorsP2.get(null).values().iterator(); it.hasNext(); ) {
                        FollowedByExecutor currentFollowedByExecutor = it.next();
                        processFollowedByExecutor(event, newlyAddedActiveListeners, it, currentFollowedByExecutor);
                    }

                    //adding new listeners
                    for (FollowedByExecutor followedByExecutor : newlyAddedActiveListeners) {
                        activeExecutorsP2.get(followedByExecutor.getCheckingStreamName()).put(event, followedByExecutor);
                    }

                    if (cleanOldExecutors) {
                        long currentTime = System.currentTimeMillis();
                        if (executorCleaningTime < currentTime) {
                            executorCleaningTime = currentTime + executorCleaningInterval;
                            for (String tempStreamName : cleanStreamNames) {
                                for (Iterator<FollowedByExecutor> it = activeExecutorsP2.get(tempStreamName).values().iterator(); it.hasNext(); ) {
                                    FollowedByExecutor currentFollowedByExecutor = it.next();
                                    if (!currentFollowedByExecutor.isAlive()) {     // Removing expired executors
                                        it.remove();
                                    }
                                }
                            }
                        }
                        System.gc();
                    }
                }
            }
        }
        log.debug(this.getClass().getSimpleName() + " ended");
    }

    private void processFollowedByExecutor(Event event,
                                           List<FollowedByExecutor> newlyAddedActiveListeners,
                                           Iterator<FollowedByExecutor> it,
                                           FollowedByExecutor currentFollowedByExecutor) {
        if (!currentFollowedByExecutor.isAlive()) {     // Removing expired events
            it.remove();
        } else if (currentFollowedByExecutor.execute(event)) { //if state success
            if (currentFollowedByExecutor.isConditionHolds()) {     // Removing expired events

                if (!currentFollowedByExecutor.isNextExecutorExist()) { // if final state reached - all state success

                    // Generate output event
                    Object[] obj = new Object[outputEventMappingObjects.size()];
                    for (int i = 0; i < outputEventMappingObjects.size(); i++) {
                        StateMachineOutputMapObj aStateMachineOutputMapObj = outputEventMappingObjects.get(i);
                        obj[i] = currentFollowedByExecutor.getArrivedEvents()[aStateMachineOutputMapObj.getStateTypeId()].getNthAttribute(aStateMachineOutputMapObj.getPosition());
                    }

                    // Send generated output event
                    try {
                        outputEventStream.put(eventGenerator.createEvent(obj));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else { //adding new FollowedByExecutor
                    if (null == activeExecutorsP2.get(currentFollowedByExecutor.getNextExecutor().getCheckingStreamName()).get(event)) {
                        FollowedByExecutor newFollowedByExecutor = currentFollowedByExecutor.getNewNextExecutor();
                        newlyAddedActiveListeners.add(newFollowedByExecutor);
                    }
                }

                //  For Every  Condition
                if (currentFollowedByExecutor.isNextEveryExecutorExist()) {
                    if (null == activeExecutorsP2.get(currentFollowedByExecutor.getNextEveryExecutor().getCheckingStreamName()).get(event)) {
                        FollowedByExecutor newFollowedByExecutor = currentFollowedByExecutor.getNewNextEveryExecutor();
                        newlyAddedActiveListeners.add(newFollowedByExecutor);
                    }

                }
            }

            //remove the success state
            it.remove();

        }

    }

    public Query getQuery() {
        return query;
    }
}
