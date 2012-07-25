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
package org.wso2.siddhi.core.query.stream.recevier.sequence;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StateEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InListEvent;
import org.wso2.siddhi.core.event.management.PersistenceManagementEvent;
import org.wso2.siddhi.core.persistence.PersistenceObject;
import org.wso2.siddhi.core.statemachine.sequence.CountSequenceState;
import org.wso2.siddhi.core.query.stream.StreamProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class CountSequenceSingleStreamReceiver extends SequenceSingleStreamReceiver {
    static final Logger log = Logger.getLogger(SequenceSingleStreamReceiver.class);

    private int min = -1;
    private int max = -1;
    private boolean passed;

    public CountSequenceSingleStreamReceiver(CountSequenceState state,
                                             StreamProcessor firstSimpleStreamProcessor,
                                             int complexEventSize) {
        super(state, firstSimpleStreamProcessor, complexEventSize);
        this.min = state.getMin();
        this.max = state.getMax();

    }

//    @Override
//    public void receive(Event event) {
//        if (checkReset(event)) {
//            return;
//        }
//        System.out.println("cr state=" + currentState + " event=" + event + " ||currentEvents=" + currentEvents);
//        for (StateEvent currentEvent : currentEvents) {
//
//            if (currentEvent.getEventState() <= (state.getStateNumber())) {
//                SingleEventList singleEventList = (SingleEventList) currentEvent.getStreamEvent(currentState);
//                if (singleEventList == null) {
//                    singleEventList = new SingleEventList(max);
//                    currentEvent.setStreamEvent(currentState, singleEventList);
//                }
//                setPassed(false);
//                singleEventList.addEvent(((Event) event));
//                firstSimpleStreamProcessor.process(currentEvent);
//
//                if (!isPassed()) {
//                    singleEventList.removeLast();  //to stop aggregation of not passed events
////                    nextEvents.add(currentEvent);   //only to add to itself
//                }
//                init();
//
//            }
//        }
////        currentEvents.clear();
////        }
//    }

    protected void sendForProcess(StreamEvent streamEvent) {
        if (log.isDebugEnabled()) {
            log.debug("cr state=" + currentState + " event=" + streamEvent + " ||currentEvents=" + currentEvents);
        }
        for (StateEvent currentEvent : currentEvents) {
            if (isEventsWithin(streamEvent, currentEvent)) {
                if (currentEvent.getEventState() <= (state.getStateNumber())) {
                    ListEvent listEvent = (ListEvent) currentEvent.getStreamEvent(currentState);
                    if (listEvent == null) {
                        listEvent = new InListEvent(max);
                        currentEvent.setStreamEvent(currentState, listEvent);
                    }
                    setPassed(false);
                    if (!listEvent.addEvent(((Event) streamEvent))) {
                        continue;
                    }
                    firstSimpleStreamProcessor.process(currentEvent);

                    if (!isPassed()) {
                        listEvent.removeLast();  //to stop aggregation of not passed events
//                    nextEvents.add(currentEvent);   //only to add to itself
                    }
                }
//                init();
            }
        }
    }

//    protected InComplexEvent createFirstEvent(int complexEventSize) {
//        return new InComplexEvent(new SingleEventBundle[complexEventSize]);
//    }

    public synchronized void addToNextEvents(StateEvent stateEvent) {
//        System.out.println("in");
        if (min == 0) {
            state.getSequenceStreamPacker().process(stateEvent.cloneEvent(currentState));
//
//            if (stateEvent.getEventState() < currentState) {
//                if (nextState != null) {
//                    if (nextState instanceof OrSequenceState) {
//                        ((OrSequenceState) nextState).getPartnerState().getSequenceSingleStreamReceiver().addToNextEvents(stateEvent);
//                    }
//                    nextState.getSequenceSingleStreamReceiver().addToNextEvents(stateEvent);
////                    nextEvents.add(stateEvent);//to keep the reference of the next events
//                }
//            }
        } else {
            try {
                nextEvents.put(stateEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setPassed(boolean passed) {
        this.passed = passed;
    }

    public synchronized boolean isPassed() {
        return passed;
    }

    public void addOnlyToNextEvents(StateEvent stateEvent) {
        try {
            nextEvents.put(stateEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(PersistenceManagementEvent persistenceManagementEvent) {
        persistenceStore.save(persistenceManagementEvent, nodeId, new PersistenceObject(new ArrayList<StateEvent>(currentEvents), new ArrayList<StateEvent>(nextEvents), passed));
    }

    @Override
    public void load(PersistenceManagementEvent persistenceManagementEvent) {
        PersistenceObject persistenceObject = persistenceStore.load(persistenceManagementEvent, nodeId);
        currentEvents = new LinkedBlockingQueue<StateEvent>((List) persistenceObject.getData()[0]);
        nextEvents = new LinkedBlockingQueue<StateEvent>((List) persistenceObject.getData()[1]);
        passed = ((Boolean) persistenceObject.getData()[2]);
    }
}
