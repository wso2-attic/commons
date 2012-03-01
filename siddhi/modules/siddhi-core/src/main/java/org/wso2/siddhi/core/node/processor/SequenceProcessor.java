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
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.SequenceQuery;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.util.OutputDefinitionParserUtil;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.event.generator.EventGeneratorImpl;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.processor.eventmap.SequenceOutputMapObj;
import org.wso2.siddhi.core.node.processor.executor.SequenceExecutor;
import org.wso2.siddhi.core.parser.ConditionParser;
import org.wso2.siddhi.core.parser.QueryInputStreamParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class contains the Query processor implementation for processing Sequence queries
 */
public class SequenceProcessor extends AbstractProcessor {

    private static final Logger log = Logger.getLogger(PatternProcessor.class);
    private SequenceQuery query;
    private List<SequenceOutputMapObj> outputEventMappingObjects;
    private EventGenerator eventGenerator;
    private List<SequenceExecutor> executorList;
    private List<SequenceExecutor> activeExecutors; // Events are sent to these Executors
    private Map<String, List<SequenceExecutor>> partitioningExecutors;
    private boolean cleanOldExecutors = false;
    private long executorCleaningTime = 0;
    private long executorCleaningInterval = 1000;
    private boolean skipTillNextMatch = true;
    private Object[][] partitioningPropertyPositions;

    /**
     * @param query sequence query
     * @throws org.wso2.siddhi.core.exception.ProcessorInitializationException
     *
     * @throws org.wso2.siddhi.core.exception.InvalidQueryInputStreamException
     *
     */
    public SequenceProcessor(SequenceQuery query)
            throws InvalidQueryInputStreamException, ProcessorInitializationException,
                   SiddhiException {
        this.query = query;
        for (QueryInputStream queryInputStream : query.getQueryInputStreamList()) {
            assignQueryInputStream(queryInputStream);
        }
        init();
    }

    @Override
    public String getStreamId() {
        return query.getStreamId();
    }

    /**
     * Set whether to skip till the next match
     *
     * @param isSkip whether to skip till the next match
     */
    public void setSkipTillFirstMatch(boolean isSkip) {
        skipTillNextMatch = isSkip;
    }

    @Override
    public void assignQueryInputStream(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        inputEventStream.assignInputStreamHandler(QueryInputStreamParser.parse(queryInputStream));
    }

    /**
     * add sequence of executors recursively for star executors ex. AB*C*D*E
     */
    private static void addNextExecutorOfStarExecutor(List<SequenceExecutor> executorList,
                                                      SequenceExecutor currentExecutor) {
        if (currentExecutor.isStarExecutor()) {
            SequenceExecutor nextExecutor = currentExecutor.getNextNewExecutor();
            executorList.add(nextExecutor);
            if (nextExecutor.isStarExecutor()) {
                addNextExecutorOfStarExecutor(executorList, nextExecutor);
            }
        }
    }

    @Override
    public void init() throws ProcessorInitializationException, SiddhiException {
        try {

            // Query
            Condition condition = query.getCondition();
            EventStream[] inputEventStreams = query.getInputEventStreams();
            ConditionParser conditionParser = new ConditionParser(condition, inputEventStreams);

            executorList = conditionParser.getSequenceExecutorList(); // Get list of Executors

            SequenceExecutor firstExecutor = executorList.get(0).getNewInstance();

            for (SequenceExecutor sequenceExecutor : executorList) {
                if (sequenceExecutor.getLifeTime() > -1) {
                    cleanOldExecutors = true;
                    break;
                }
            }
            if (query.hasGroupBy()) {
                partitioningExecutors = new HashMap<String, List<SequenceExecutor>>();
                partitioningExecutors.put(null, getInitialExecutorList(firstExecutor));

                String[] groupByNames = query.getGroupBy();
                partitioningPropertyPositions = new Object[groupByNames.length][2];
                for (int i = 0; i < groupByNames.length; i++) {
                    String streamId = groupByNames[i].split("\\.")[0];   //Ex. streamId
                    String groupByAttribute = groupByNames[i].split("\\.")[1];   //Ex. price
                    EventStream eventStream = query.getInputEventStream(streamId);
                    partitioningPropertyPositions[i][1] = eventStream.getAttributePositionForName(groupByAttribute);
                    partitioningPropertyPositions[i][0] = streamId;
                }

            } else {
              activeExecutors=  getInitialExecutorList(firstExecutor);
            }

            // Output
            eventGenerator = new EventGeneratorImpl(query.getStreamId(), query.getNames(), query.getTypes());
            List<String> outputDefinitionList = query.getOutputDefinition().getPropertyList();
            outputEventMappingObjects = new ArrayList<SequenceOutputMapObj>();

            List<String> eventStreamNameList = OutputDefinitionParserUtil.createStreamIdListFromConditions((SequenceCondition) condition);

            for (String aOutputDefinition : outputDefinitionList) {
                // use 0 as the first state
                int stateId = Integer.parseInt(aOutputDefinition.split("=")[1].split("\\.")[0].substring(1));
                int selfEventPosition = 0;
                String propertyAttribute;

                try {
                    propertyAttribute = aOutputDefinition.split("=")[1].split("\\.")[2];

                    if (aOutputDefinition.split("=")[1].split("\\.")[1].equals("first")) {
                        selfEventPosition = 0;
                    } else {
                        selfEventPosition = 1;
                    }

                } catch (Exception e) {
                    propertyAttribute = aOutputDefinition.split("=")[1].split("\\.")[1];
                }

                for (EventStream eventStream : inputEventStreams) {
                    if (eventStream.getStreamId().equals(eventStreamNameList.get(stateId))) {
                        SequenceOutputMapObj mapObj = new SequenceOutputMapObj(stateId, eventStream.getAttributePositionForName(propertyAttribute), selfEventPosition);
                        outputEventMappingObjects.add(mapObj);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new ProcessorInitializationException("Cannot initialize  Sequence  Processor query " + query.getStreamId(), ex);
        }

    }

    private List<SequenceExecutor> getInitialExecutorList(SequenceExecutor firstExecutor) {
        List<SequenceExecutor> executors = new LinkedList<SequenceExecutor>();
        executors.add(firstExecutor); //adding first SequenceExecutor to executorList
        addNextExecutorOfStarExecutor(executors, firstExecutor);
        return executors;
    }

    @Override
    public void run() {
        Event event = null;

        while (true) {

            try {
                event = inputEventStream.takeEvent();  //get event

                // Check for last event
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

            List<SequenceExecutor> newlyAddedActiveListeners = new ArrayList<SequenceExecutor>();
            boolean isEventFired = false;

            if (partitioningExecutors != null) {
                String groupByConditionKey = null;
                for (Object[] position : partitioningPropertyPositions) {
                    if (event.getEventStreamId().equals(position[0])) {
                        StringBuilder groupByConditionKeyBuf = new StringBuilder();

                        groupByConditionKeyBuf.append(event.getNthAttribute((Integer) position[1])).
                                append(":");
                        groupByConditionKey = groupByConditionKeyBuf.toString();
                    }
                }
                activeExecutors = partitioningExecutors.get(groupByConditionKey);
                if (activeExecutors == null) {
                    activeExecutors = getInitialExecutorList(executorList.get(0).getNewInstance());
                    partitioningExecutors.put(groupByConditionKey, activeExecutors);
                }
            }
            for (Iterator<SequenceExecutor> it = activeExecutors.iterator(); it.hasNext(); ) {
                SequenceExecutor currentSequenceExecutor = it.next();

                if (!currentSequenceExecutor.isAlive()) {     // Removing expired listeners
                    it.remove();
                    continue;
                } else if (currentSequenceExecutor.execute(event)) {
                    skipTillNextMatch = false;

                    // Fire Event - Generate output
                    if (currentSequenceExecutor.isFireEvent()) {
                        // Generate output event
                        Object[] outputValues = new Object[outputEventMappingObjects.size()];
                        for (int i = 0, outPutEventGenMapListSize = outputEventMappingObjects.size(); i < outPutEventGenMapListSize; i++) {
                            SequenceOutputMapObj sequenceOutputMapObj = outputEventMappingObjects.get(i);

                            try {
                                outputValues[i] = currentSequenceExecutor.getArrivedEvents()[sequenceOutputMapObj.getStateTypeId()][sequenceOutputMapObj.getselfEventPosition()].getNthAttribute(sequenceOutputMapObj.getPosition());
                            } catch (Exception e) {
                                try {
                                    outputValues[i] = currentSequenceExecutor.getArrivedEvents()[sequenceOutputMapObj.getStateTypeId()][0].getNthAttribute(sequenceOutputMapObj.getPosition());
                                } catch (Exception e2) {
                                    outputValues[i] = null;
                                }
                            }
                        }
                        // Send generated output event
                        try {
                            outputEventStream.put(eventGenerator.createEvent(outputValues));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isEventFired = true;
                        break;
                    }

                    // add executors to listener list
                    if (currentSequenceExecutor.isStarExecutor()) {
                        newlyAddedActiveListeners.add(currentSequenceExecutor.getNextThisExecutor());
                    }

                    if (currentSequenceExecutor.isNextExecutorExist()) {
                        SequenceExecutor nextExecutor = currentSequenceExecutor.getNextNewExecutor();
                        if (nextExecutor.isStarExecutor()) {
                            nextExecutor.execute(event);
                        }
                        newlyAddedActiveListeners.add(nextExecutor);
                        addNextExecutorOfStarExecutor(newlyAddedActiveListeners, nextExecutor);
                    }
                }

                if (skipTillNextMatch) {
                    currentSequenceExecutor.clearEvents();
                    continue;
                }
                it.remove();
            }

            if (isEventFired || activeExecutors.size() + newlyAddedActiveListeners.size() == 0) {
                reset();    //resetting listeners
            } else {
                activeExecutors.addAll(newlyAddedActiveListeners); //adding new listeners
            }

            if (cleanOldExecutors) {
                long currentTime = System.currentTimeMillis();
                if (executorCleaningTime < currentTime) {
                    executorCleaningTime = currentTime + executorCleaningInterval;

                    for (Iterator<SequenceExecutor> it = activeExecutors.iterator(); it.hasNext(); ) {
                        SequenceExecutor currentSequenceExecutor = it.next();
                        if (!currentSequenceExecutor.isAlive()) {
                            it.remove();
                        }
                    }
                    System.gc();
                }
            }
        }
    }

    /**
     * Reset activeExecutors
     */
    private void reset() {
        activeExecutors.clear();
        SequenceExecutor firstExecutor = executorList.get(0).getNewInstance();
        activeExecutors.add(firstExecutor.getNewInstance()); //adding first SequenceExecutor to activeExecutors
        addNextExecutorOfStarExecutor(activeExecutors, firstExecutor);
        skipTillNextMatch = true;
    }

    public Query getQuery() {
        return query;
    }
}
