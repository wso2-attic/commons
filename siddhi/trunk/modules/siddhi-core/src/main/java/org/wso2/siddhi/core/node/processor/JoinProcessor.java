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
import org.wso2.siddhi.api.eventstream.query.JoinQuery;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.eventstream.query.jointstream.Join;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.event.generator.EventGeneratorImpl;
import org.wso2.siddhi.core.eventstream.queue.EventQueue;
import org.wso2.siddhi.core.exception.InvalidAttributeCastException;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.PropertyFormatException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.exception.UndefinedPropertyException;
import org.wso2.siddhi.core.node.processor.eventmap.StreamMapObj;
import org.wso2.siddhi.core.node.processor.executor.Executor;
import org.wso2.siddhi.core.parser.ConditionParser;
import org.wso2.siddhi.core.parser.QueryInputStreamParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Query processor
 */
public class JoinProcessor extends AbstractProcessor {
    private static final Logger log = Logger.getLogger(JoinProcessor.class);

    private JoinQuery query;

    private Executor executor;
    private List<StreamMapObj> outPutEventGenMapList;

    private EventGenerator eventGenerator;
    // Event Windows
    EventQueue leftWindow;
    EventQueue rightWindow;

    private Set<EventCouple> sameTimeEventCouples = new HashSet<EventCouple>();


    private String leftStreamId, rightStreamId;
    private volatile long eventTime = -1;

    public JoinProcessor(JoinQuery query)
            throws ProcessorInitializationException, InvalidQueryInputStreamException,
                   SiddhiException, InvalidEventStreamIdException {
        this.query = query;
        assignQueryInputStream(query.getJointStream().getQueryLeftInputStream());
        leftWindow = inputEventStream.getWindow(query.getJointStream().getQueryLeftInputStream().getEventStream().getStreamId());
        assignQueryInputStream(query.getJointStream().getQueryRightInputStream());
        rightWindow = inputEventStream.getWindow(query.getJointStream().getQueryRightInputStream().getEventStream().getStreamId());

        init();
    }

    @Override
    public String getStreamId() {
        return query.getStreamId();
    }

    @Override
    public void assignQueryInputStream(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        inputEventStream.assignInputStreamHandler(QueryInputStreamParser.parseForJoin(queryInputStream));
    }

    /**
     * Initialize the processor.
     */
    public void init() throws ProcessorInitializationException, SiddhiException {
        try {
            //Query
            Condition condition = query.getCondition();
            Join joinEventStream = query.getJointStream();
            List<EventStream> inputEventStreams = new ArrayList<EventStream>();
            inputEventStreams.add(joinEventStream.getQueryLeftInputStream().getEventStream());
            inputEventStreams.add(joinEventStream.getQueryRightInputStream().getEventStream());

            ConditionParser conditionParser = new ConditionParser(condition, inputEventStreams);
            executor = conditionParser.getExecutor();

            //StreamIds
            leftStreamId = inputEventStreams.get(0).getStreamId();
            rightStreamId = inputEventStreams.get(1).getStreamId();

            //output
            eventGenerator = new EventGeneratorImpl(query.getStreamId(), query.getNames(), query.getTypes());
            List<String> outputDefinitionList = query.getOutputDefinition().getPropertyList();
            outPutEventGenMapList = new ArrayList<StreamMapObj>();
            for (String aOutputDefinition : outputDefinitionList) {
                String streamId = aOutputDefinition.split("=")[1].split("\\.")[0];
                String propertyAttribute = aOutputDefinition.split("=")[1].split("\\.")[1];

                StreamMapObj mapObj = null;
                if (streamId.equals(leftStreamId)) {
                    mapObj = new StreamMapObj(streamId, inputEventStreams.get(0).getAttributePositionForName(propertyAttribute));
                } else {
                    mapObj = new StreamMapObj(streamId, inputEventStreams.get(1).getAttributePositionForName(propertyAttribute));
                }
                outPutEventGenMapList.add(mapObj);
            }
        } catch (UndefinedPropertyException ex) {
            log.error(ex.getMessage());
            throw new ProcessorInitializationException("UndefinedPropertyException occurred " + ex.getMessage());
        } catch (InvalidAttributeCastException ex) {
            log.error(ex.getMessage());
            throw new ProcessorInitializationException("InvalidAttributeCastException occurred " + ex.getMessage());
        } catch (InvalidQueryException ex) {
            log.error(ex.getMessage());
            throw new ProcessorInitializationException("InvalidQueryException occurred " + ex.getMessage());
        } catch (PropertyFormatException ex) {
            log.error(ex.getMessage());
            throw new ProcessorInitializationException("PropertyFormatException occurred " + ex.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                Event event = inputEventStream.takeEvent();
                String eventStreamId = event.getEventStreamId();
                if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
                    if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL) {
                        outputEventStream.put(event);
                    } else if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                        break;
                    }
                }
                if (event.isNew()) {
                    if (event.getTimeStamp() != eventTime) {
                        eventTime = event.getTimeStamp();
                        sameTimeEventCouples.clear();
                    }

                    if (eventStreamId.equals(leftStreamId)) {
                        // New Event belongs to the left Input Stream
                        for (Event aRightWindowEvent : rightWindow) {
                            if (event.getTimeStamp() < aRightWindowEvent.getTimeStamp()) {
                                break;
                            } else if (event.getTimeStamp() == aRightWindowEvent.getTimeStamp() &&
                                       !sameTimeEventCouples.add(new EventCouple(event, aRightWindowEvent))) {
                                continue;
                            }
                            executeTwoEvents(event, aRightWindowEvent, eventStreamId);
                        }

                    } else if (eventStreamId.equals(rightStreamId)) {
                        //New Event belongs to the right Input Stream
                        for (Event aLeftWindowEvent : leftWindow) {
                            if (event.getTimeStamp() < aLeftWindowEvent.getTimeStamp()) {
                                break;
                            } else if (event.getTimeStamp() == aLeftWindowEvent.getTimeStamp() &&
                                       !sameTimeEventCouples.add(new EventCouple(event, aLeftWindowEvent))) {
                                continue;
                            }
                            executeTwoEvents(event, aLeftWindowEvent, eventStreamId);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug(this.getClass().getSimpleName() + " ended");
    }

    private void executeTwoEvents(Event eventFromEventQueue, Event eventFromWindow,
                                  String eventFromEventQueueStreamId) {
        if (executor.execute(new Event[]{eventFromEventQueue, eventFromWindow})) {
            Object[] obj = new Object[outPutEventGenMapList.size()];
            for (int i = 0, outPutEventGenMapListSize = outPutEventGenMapList.size(); i < outPutEventGenMapListSize; i++) {
                StreamMapObj aStreamMapObj = outPutEventGenMapList.get(i);
                if (aStreamMapObj.getStreamId().equals(eventFromEventQueueStreamId)) {
                    obj[i] = eventFromEventQueue.getNthAttribute(aStreamMapObj.getPosition()); //NOTE: We are not doing the safety check here, see Simple Processor
                } else {
                    obj[i] = eventFromWindow.getNthAttribute(aStreamMapObj.getPosition());
                }
            }
            try {
                outputEventStream.put(eventGenerator.createEvent(obj));
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    class EventCouple {
        private final Event event1;
        private final Event event2;

        EventCouple(Event event1, Event event2) {
            this.event1 = event1;
            this.event2 = event2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            EventCouple that = (EventCouple) o;

            if (!(event1.equals(that.event1) && event2.equals(that.event2) || event1.equals(that.event2) && event2.equals(that.event1))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return event1.hashCode() + event2.hashCode();
        }
    }

    public Query getQuery() {
        return query;
    }
}
