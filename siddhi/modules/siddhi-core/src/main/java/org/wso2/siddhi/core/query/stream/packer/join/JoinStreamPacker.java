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
package org.wso2.siddhi.core.query.stream.packer.join;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StateEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.query.projector.QueryProjector;
import org.wso2.siddhi.core.query.stream.handler.window.WindowHandler;
import org.wso2.siddhi.core.query.stream.packer.SingleStreamPacker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class JoinStreamPacker extends SingleStreamPacker {
    static final Logger log = Logger.getLogger(JoinStreamPacker.class);
    //    protected SchedulerQueue<StreamEvent> oppositeWindow;
    protected WindowHandler windowHandler;
    protected final ReentrantLock lock;
    protected Class eventType;

    protected ConditionExecutor onConditionExecutor;
    protected boolean triggerEvent;
    private WindowHandler oppositeWindowHandler;
    protected long within = -1;
    //    private int nextState;


    public WindowHandler getWindowHandler() {
        return windowHandler;
    }

    public JoinStreamPacker(ConditionExecutor onConditionExecutor, boolean triggerEvent,
                            ReentrantLock lock) {
        this.lock = lock;
        this.onConditionExecutor = onConditionExecutor;
        this.triggerEvent = triggerEvent;
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
//        System.out.println("Arrived");

//        if (complexEvent instanceof InStream) {
        if (triggerEventTypeCheck(complexEvent)) {
            lock.lock();
            try {
                if (triggerEvent) {
                    if (complexEvent instanceof Event) {

                        Iterator<StreamEvent> iterator = oppositeWindowHandler.getWindow().iterator();
                        while (iterator.hasNext()) {
                            ComplexEvent windowComplexEvent = iterator.next();
                            if (windowComplexEvent instanceof Event) {
//                        Event newEvent = (new InComplexEvent(new Event[]{((Event) complexEvent), ((Event) windowComplexEvent)}));
                                if (isEventsWithin(complexEvent, windowComplexEvent)) {
                                    StateEvent newEvent = createNewEvent(complexEvent, windowComplexEvent);
                                    if (onConditionExecutor.execute(newEvent)) {
                                        queryProjector.process(newEvent);
                                    }
                                } else {
                                    break;
                                }
                            } else if (windowComplexEvent instanceof ListEvent) {
                                List<AtomicEvent> list = new ArrayList<AtomicEvent>();
                                Event[] events = ((ListEvent) windowComplexEvent).getEvents();
                                for (int i = 0, eventsLength = events.length; i < eventsLength; i++) {
//                            Event newEvent = (new InComplexEvent(new Event[]{((Event) complexEvent), ((Event) events[i])}));
                                    if (isEventsWithin(complexEvent, windowComplexEvent)) {
                                        StateEvent newEvent = createNewEvent(complexEvent, events[i]);
                                        if (onConditionExecutor.execute(newEvent)) {
                                            list.add(newEvent);
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (list.size() > 0) {
                                    sendEventList(list);
                                }
                            } else {
                                //todo error Complex event not supported
                            }
                        }
                    } else if (complexEvent instanceof ListEvent) {
                        List<AtomicEvent> list = new ArrayList<AtomicEvent>();
                        Iterator<StreamEvent> iterator = oppositeWindowHandler.getWindow().iterator();
                        for (Event event : ((ListEvent) complexEvent).getEvents()) {
                            while (iterator.hasNext()) {
                                ComplexEvent windowComplexEvent = iterator.next();
                                if (windowComplexEvent instanceof Event) {
//                            Event newEvent = (new InComplexEvent(new Event[]{((Event) event), ((Event) windowComplexEvent)}));
                                    if (isEventsWithin(complexEvent, windowComplexEvent)) {

                                        StateEvent newEvent = createNewEvent(event, windowComplexEvent);
                                        if (onConditionExecutor.execute(newEvent)) {
                                            list.add(newEvent);
                                        }
                                    } else {
                                        break;
                                    }
                                } else if (windowComplexEvent instanceof ListEvent) {
                                    Event[] events = ((ListEvent) windowComplexEvent).getEvents();
                                    for (int i = 0, eventsLength = events.length; i < eventsLength; i++) {
                                        if (isEventsWithin(complexEvent, windowComplexEvent)) {
                                            StateEvent newEvent = createNewEvent(event, events[i]);
                                            if (onConditionExecutor.execute(newEvent)) {
                                                list.add(newEvent);
                                            }
                                        } else {
                                            break;
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
                if (complexEvent instanceof InStream) {
                    windowHandler.process(complexEvent);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean isEventsWithin(ComplexEvent complexEvent, ComplexEvent windowComplexEvent) {
        if (within > -1) {
            long diff = Math.abs(complexEvent.getTimeStamp() - windowComplexEvent.getTimeStamp());
            if (diff > within || diff == Long.MIN_VALUE) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    protected abstract boolean triggerEventTypeCheck(ComplexEvent complexEvent);

    protected abstract StateEvent createNewEvent(ComplexEvent complexEvent,
                                                 ComplexEvent complexEvent1);

    protected void sendEventList(List<AtomicEvent> list) {
        if (log.isDebugEnabled()) {
            log.debug(list);
        }
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

    public void setOppositeWindowHandler(WindowHandler windowHandler) {
        this.oppositeWindowHandler = windowHandler;
    }

    public void setWindowHandler(WindowHandler windowHandler) {
        this.windowHandler = windowHandler;
    }

    public void setWithin(long within) {
        this.within = within;
    }

}
