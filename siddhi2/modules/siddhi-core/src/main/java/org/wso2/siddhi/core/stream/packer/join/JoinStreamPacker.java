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
package org.wso2.siddhi.core.stream.packer.join;

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.event.in.StateEvent;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.util.SchedulerQueue;
import org.wso2.siddhi.core.projector.QueryProjector;
import org.wso2.siddhi.core.stream.packer.SingleStreamPacker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class JoinStreamPacker extends SingleStreamPacker {

    public SchedulerQueue<StreamEvent> getWindow() {
        return prevStreamElement.getWindow();
    }

    protected SchedulerQueue<StreamEvent> window;
    protected ConditionExecutor onConditionExecutor;
    protected boolean triggerEvent;
    //    private int nextState;


    public JoinStreamPacker(ConditionExecutor onConditionExecutor, boolean triggerEvent) {
        this.onConditionExecutor = onConditionExecutor;
        this.triggerEvent=triggerEvent;
    }

    @Override
    public void setNext(QueryProjector queryProjector) {
        this.queryProjector = queryProjector;
//        this.nextState = state;
    }

    //written in thinking of LEFT
    // RIGHT is handled in the #createNewEvent()
    @Override
    public void process(ComplexEvent complexEvent) {
//        //System.out.println("Arrived");

        if (triggerEvent && complexEvent instanceof InStream) {
            if (complexEvent instanceof Event) {
                Iterator<StreamEvent> iterator = window.iterator();
                while (iterator.hasNext()) {
                    ComplexEvent windowComplexEvent = iterator.next();
                    if (windowComplexEvent instanceof Event) {
//                        Event newEvent = (new InComplexEvent(new Event[]{((Event) complexEvent), ((Event) windowComplexEvent)}));
                        StateEvent newEvent = createNewEvent(complexEvent, windowComplexEvent);
                        if (onConditionExecutor.execute(newEvent)) {
                            queryProjector.process(newEvent);
                        }
                    } else if (windowComplexEvent instanceof ListEvent) {
                        List<AtomicEvent> list = new ArrayList<AtomicEvent>();
                        Event[] events = ((ListEvent) windowComplexEvent).getEvents();
                        for (int i = 0, eventsLength = events.length; i < eventsLength; i++) {
//                            Event newEvent = (new InComplexEvent(new Event[]{((Event) complexEvent), ((Event) events[i])}));
                            StateEvent newEvent = createNewEvent(complexEvent, events[i]);
                            if (onConditionExecutor.execute(newEvent)) {
                                list.add(newEvent);
                            }
                        }
                        sendEventList(list);
                    } else {
                        //todo error Complex event not supported
                    }
                }
            } else if (complexEvent instanceof ListEvent) {
                List<AtomicEvent> list = new ArrayList<AtomicEvent>();
                Iterator<StreamEvent> iterator = window.iterator();
                for (Event event : ((ListEvent) complexEvent).getEvents()) {
                    while (iterator.hasNext()) {
                        ComplexEvent windowComplexEvent = iterator.next();
                        if (windowComplexEvent instanceof Event) {
//                            Event newEvent = (new InComplexEvent(new Event[]{((Event) event), ((Event) windowComplexEvent)}));
                            StateEvent newEvent = createNewEvent(event, windowComplexEvent);
                            if (onConditionExecutor.execute(newEvent)) {
                                list.add(newEvent);
                            }
                        } else if (windowComplexEvent instanceof ListEvent) {
                            Event[] events = ((ListEvent) windowComplexEvent).getEvents();
                            for (int i = 0, eventsLength = events.length; i < eventsLength; i++) {
                                StateEvent newEvent = createNewEvent(event, events[i]);
                                if (onConditionExecutor.execute(newEvent)) {
                                    list.add(newEvent);
                                }
                            }
                        } else {
                            //todo error Complex event not supported
                        }
                    }
                }
                sendEventList(list);
            }
        }

    }

    protected abstract StateEvent createNewEvent(ComplexEvent complexEvent,
                                                 ComplexEvent complexEvent1);

    protected void sendEventList(List<AtomicEvent> list) {
        int size = list.size();
//        if (size > 1) {
//            queryProjector.process((list.toArray(new AtomicEvent[list.size()])));
//        } else if (size == 1) {
//            queryProjector.process(list.get(0));
//        }
        if (size > 1) {
            queryProjector.process(list);
        } else if (size == 1) {
            queryProjector.process(list.get(0));
        }
    }

    public void setOppositeWindow(SchedulerQueue<StreamEvent> window) {
        this.window = window;
    }

}
