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
package org.wso2.siddhi.core.stream.recevier.sequence;

import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.StateEvent;
import org.wso2.siddhi.core.util.SchedulerQueue;
import org.wso2.siddhi.core.statemachine.sequence.SequenceState;
import org.wso2.siddhi.core.stream.StreamElement;
import org.wso2.siddhi.core.stream.StreamProcessor;
import org.wso2.siddhi.core.stream.recevier.StreamReceiver;

import java.util.LinkedList;
import java.util.List;

public class SequenceSingleStreamReceiver implements StreamReceiver, StreamElement {
    protected int complexEventSize;
    protected SequenceState state;
    protected SequenceState nextState;
    protected StreamProcessor firstSimpleStreamProcessor;
    protected List<StateEvent> currentEvents = new LinkedList<StateEvent>();
    protected List<StateEvent> nextEvents = new LinkedList<StateEvent>();
    //    private final boolean first;
    protected final int currentState;


    public SequenceSingleStreamReceiver(SequenceState state,
                                        StreamProcessor firstSimpleStreamProcessor,
                                        int complexEventSize) {
        this.state = state;
        this.nextState = state.getNextState();
        this.currentState = state.getStateNumber();
        this.complexEventSize = complexEventSize;
        this.firstSimpleStreamProcessor = firstSimpleStreamProcessor;
//        init(state, complexEventSize);
    }

    public void init() {
        if (state.isFirst()) {
            //first event
            addToNextEvents(new StateEvent(new StreamEvent[complexEventSize]));
        }
    }

    @Override
    public void receive(StreamEvent streamEvent) {
        if (streamEvent instanceof SequenceResetEvent) {
            //System.out.println("Before Sequence Reset || currentEvents=" +currentEvents.size()+" nextEvents="+nextEvents.size());
            currentEvents.clear();
            nextEvents.clear();
            //System.out.println("After  Sequence Reset || currentEvents=" +currentEvents.size()+" nextEvents="+nextEvents.size());
        }else {
            sendForProcess((StreamEvent) streamEvent);
        }
        init();

//        currentEvents.clear();
//        }
    }

    protected void sendForProcess(StreamEvent event) {
        //System.out.println("sr state=" +currentState+" event="+ event+" ||currentEvents="+currentEvents);
        for (StateEvent currentEvent : currentEvents) {
            currentEvent.setStreamEvent(currentState, event);
            firstSimpleStreamProcessor.process(currentEvent);
            if ( currentEvent.getEventState()<currentState) {
                currentEvent.setStreamEvent(currentState, null);
            }
        }
    }

    public String getStreamId() {
        return state.getSingleStream().getStreamId();
    }

    @Override
    public SchedulerQueue<StreamEvent> getWindow() {
        return null;
    }

    public synchronized void addToNextEvents(StateEvent stateEvent) {
//        //System.out.println("add to next ss");
        nextEvents.add(stateEvent);
    }

    public synchronized void moveNextEventsToCurrentEvents() {
        //todo need to check which is faster
        // 1
//        currentEvents.clear();
//        currentEvents.addAll(nextEvents);
//        nextEvents.clear();

//        // 2
        currentEvents = nextEvents;
        nextEvents = new LinkedList<StateEvent>();
    }

}
