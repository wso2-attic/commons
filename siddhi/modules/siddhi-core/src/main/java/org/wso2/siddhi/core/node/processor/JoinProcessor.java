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
import org.wso2.siddhi.api.eventstream.query.JoinQuery;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.queue.EventQueue;
import org.wso2.siddhi.core.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.processor.executor.Executor;
import org.wso2.siddhi.core.node.processor.utils.OutputGenerator;
import org.wso2.siddhi.core.parser.ConditionParser;
import org.wso2.siddhi.core.parser.QueryInputStreamParser;

import java.util.HashSet;
import java.util.Set;


/**
 * Query processor
 */
public class JoinProcessor extends AbstractProcessor {

    private static final Logger log = Logger.getLogger(JoinProcessor.class);
    private JoinQuery query;
    private Executor executor;

    // Event Windows
    EventQueue leftWindow;
    EventQueue rightWindow;

    private Set<EventCouple> sameTimeEventCouples = new HashSet<EventCouple>();

    private String leftStreamId;

    private volatile long eventTime = -1;
    private OutputGenerator outputGenerator;

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
            outputGenerator = new OutputGenerator(query, getInputEventStream());

            ConditionParser conditionParser = new ConditionParser(condition, query.getInputEventStreams());
            executor = conditionParser.getExecutor();

            //StreamIds
            leftStreamId = query.getInputEventStreams()[0].getStreamId();

        } catch (Exception ex) {
            throw new ProcessorInitializationException("Cannot initialize  Join  Processor query " + query.getStreamId(), ex);
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
                        executeTwoEvents(event, aRightWindowEvent, 0);
                    }
                } else {
                    //New Event belongs to the right Input Stream
                    for (Event aLeftWindowEvent : leftWindow) {
                        if (event.getTimeStamp() < aLeftWindowEvent.getTimeStamp()) {
                            break;
                        } else if (event.getTimeStamp() == aLeftWindowEvent.getTimeStamp() &&
                                   !sameTimeEventCouples.add(new EventCouple(event, aLeftWindowEvent))) {
                            continue;
                        }
                        executeTwoEvents(event, aLeftWindowEvent, 1);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug(this.getClass().getSimpleName() + " ended");
    }

    private void executeTwoEvents(Event eventFromEventQueue, Event eventFromWindow,
                                  int leftEventAt) {

        if (executor.execute(new Event[]{eventFromEventQueue, eventFromWindow})) {
            Event outputEvent;
            if (leftEventAt == 0) {
                outputEvent = outputGenerator.generateOutput(eventFromEventQueue, eventFromWindow);
            } else {
                outputEvent = outputGenerator.generateOutput(eventFromWindow, eventFromEventQueue);
            }
            if (outputEvent != null) {
                try {
                    outputEventStream.put(outputEvent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        log.debug(this.getClass().getSimpleName() + " ended");
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
